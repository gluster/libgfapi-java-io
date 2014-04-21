/*
  Copyright (c) 2013 Red Hat, Inc. <http://www.redhat.com>
  This file is part of GlusterFS.

  This file is licensed to you under your choice of the GNU Lesser
  General Public License, version 3 or any later version (LGPLv3 or
  later), or the GNU General Public License, version 2 (GPLv2), in all
  cases as published by the Free Software Foundation.
*/


package org.gluster.fs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import glusterfsio.glfs_javaJNI;

public class GlusterClient {
	
	private static String LIB_NAME="libgfapi-java-io";
	private static String LIB_FILE=GlusterClient.LIB_NAME + ".so";
	private static File loadedLib = null;
	private static int CONNECT_WAIT=1*1000;
	
	
	public static char PATH_SEPARATOR = File.separatorChar;

	private long fs;
	
    static {
    	try{
    		/* try and load the system library */
    		System.loadLibrary(LIB_NAME);
    	}catch(UnsatisfiedLinkError ex){
    		/* system library didn't load, attempt a few other methods */
    		String overRideLib = System.getProperty("GLUSTER_JAVA_LIB");
	    	if(overRideLib!=null && !"".equals(overRideLib)){
	    		GlusterClient.LIB_FILE = overRideLib;
	    		System.load(GlusterClient.LIB_FILE);
	    	}else{
		    	try {
		    		File tempFile = new File(System.getProperty("java.io.tmpdir"));
		    		loadedLib = new File(tempFile + File.separator + LIB_FILE);
		    		
		    		if(!loadedLib.exists())
		    			LibUtil.copyFromJar(LIB_FILE,tempFile);
		    		//loadedLib.deleteOnExit();
		    		
				} catch (IOException e) {
					throw new RuntimeException("Error loading native library:" + e);
				}
		    	System.load(loadedLib.getAbsolutePath());
	    	}
    	}
    }
    
    private String server = null; 
    private int port = -1; 
    private String transport = null;
    
    public GlusterClient(String server, int port, String transport){
    	this.server = server;
    	this.port = port;
    	this.transport = transport;
    }

    public GlusterClient(String server){
    	this(server,0,"tcp");
    }

    public GlusterClient(){
    	this("localhost",0,"tcp");
    }
    
    public int setLogging(String LogFile, int LogLevel, long handle) {
        int ret;
        ret = glfs_javaJNI.glfs_set_logging(handle, LogFile, LogLevel);
        return ret;
    }
    public String getPid(){
        InputStream    fis=null;
        BufferedReader br=null;
        String         line=null;

        try {
            fis = new FileInputStream("/proc/self/status");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
        try {
            br.readLine();
            br.readLine();
            br.readLine();
            String val = br.readLine();
            String split[] = val.split("\\s+");
            return split[1];
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
       return null;
        
    }

	public GlusterVolume connect(String volumeName) throws IOException {
        int ret=1;
       
        fs = glfs_javaJNI.glfs_new(volumeName);

        if (fs == 0) {
        	throw new IOException("Error connecting to gluster volume:" + server + ":" + port + "/" + volumeName);
        }
       
       
            ret = glfs_javaJNI.glfs_set_volfile_server(fs, transport, server, port);
            
        if (ret == -1) {
            glfs_javaJNI.glfs_fini(fs);
            throw new IOException("Error connecting to gluster volume:" + server + ":" + port + "/" + volumeName);
        }
        
        setLogging("/tmp/libgfapi-java-io-" + volumeName + "-" + getPid() + ".log", 7, fs);
        ret = glfs_javaJNI.glfs_init(fs);
        
        int i=0;
        for(i=0 ;i<15 && !(ret==-1 || ret==0);i++){
           
            ret = glfs_javaJNI.glfs_init_wait(fs);
            if(ret==1){
                try{
                Thread.sleep(GlusterClient.CONNECT_WAIT);
                }catch(InterruptedException ex){
                    throw new IOException("Error connecting to gluster volume:" + volumeName);
                }
                }
        }
        
        if (i == 15)  throw new IOException("Error connecting to gluster volume, tried 15 times..:" + volumeName);
        
        if (ret == -1) {
            glfs_javaJNI.glfs_fini(fs);
            throw new IOException("Error connecting to gluster volume:" + server + ":" + port + "/" + volumeName);
        }

        return new GlusterVolume(volumeName,fs);
    }


    
    
 
}
