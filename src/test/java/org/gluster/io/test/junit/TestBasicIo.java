package org.gluster.io.test.junit;

import static org.junit.Assert.assertTrue;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.gluster.fs.GlusterFile;
import org.gluster.fs.GlusterInputStream;
import org.gluster.fs.GlusterOutputStream;
import org.gluster.fs.IGlusterOutputStream;
import org.gluster.fs.IGlusterInputStream;


import org.junit.Test;

public class TestBasicIo extends TestLibgfapiJava{

	public static String GLUSTER_MOUNT = "/mnt/glusterfs";
	

	public void testXAttr(){
		GlusterFile base = getTestBase();
		GlusterFile out = new GlusterFile(base,"a");
		out.createNewFile();
		String xattrOUt = out.getXAttr("trusted.glusterfs.pathinfo");
		assert(xattrOUt!=null && "".compareTo(xattrOUt)!=0 );
		//System.out.println("Xattr Value  : " + xattrOUt);
	}
	

	public void testMode(){
		GlusterFile base = getTestBase();
		GlusterFile out = new GlusterFile(base,"a");
		out.createNewFile();
	
		int mode = 0100700;
		out.chmod(mode);
		assertTrue(mode==out.getMod());
		
		mode = 0100750;
		out.chmod(mode);
		assertTrue(mode==out.getMod());
		
		mode = 0100750;
		out.chmod(mode);
		System.out.println("new mode" + Long.toString(out.getMod(),8));
	}
	@Test
	public void testConsistent() throws Exception{
	    GlusterFile base = getTestBase();
	    GlusterFile out = new GlusterFile(base,"a");
        out.createNewFile();
        assertTrue(out.exists());
	}
	
	
    public void testLargRead() throws Exception{
        GlusterFile base = getTestBase();
        GlusterFile out = new GlusterFile(base,"test1.txt");
        
        long time = System.currentTimeMillis();
        if(!out.exists()){
            out.createNewFile();
        
            /* libgfapi big file test */
            
            byte[] someValue = "this is a test its only a test.".getBytes();
            IGlusterOutputStream outStream = out.outputStream();
            System.out.println("Starting creation of file through libgfapi");
            
            for(int i=0;i<10000000;i++){
                outStream.write(someValue,0,someValue.length);
                
            }
            outStream.flush();
            outStream.close();
            
            System.out.println("Finished creation of file through libgfapi in " + (System.currentTimeMillis() - time) + "ms");  
        }
        
        File foutFile = new File(GLUSTER_MOUNT + "/test/test1.txt");
        InputStream is = new BufferedInputStream(new FileInputStream(foutFile));
        
        int read = 0;
        System.out.println("Benchmarking fuse start read..");
        time = System.currentTimeMillis();
        while(read != -1){
            read = is.read();
        }
        is.close();
        System.out.println("Finished read of file through fuse in " + (System.currentTimeMillis() - time) + "ms"); 
        
        
        
        /* gluster mount big file test */
         is = new BufferedInputStream((InputStream)out.bufferedInputStream());
        read = 0;
        time = System.currentTimeMillis();
        System.out.println("Start of read file through libgfapi");
        while(read != -1){
            read = is.read();
        }
        is.close();
        
        System.out.println("Finished read of file through libgfapi in " + (System.currentTimeMillis() - time) + "ms");  
        
        
        
        
      }
	
	
    public void testLargeWrite() throws Exception{
		GlusterFile base = getTestBase();
		GlusterFile out = new GlusterFile(base,"test1.txt");
		out.createNewFile();
	
        /* libgfapi big file test */
		
        byte[] someValue = "this is a test its only a test.".getBytes();
        IGlusterOutputStream outStream = out.outputStream();
      
        System.out.println("Starting creation of file through libgfapi");
        long time = System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
        	outStream.write(someValue,0,someValue.length);
        	
        }
        outStream.flush();
        outStream.close();
        
        System.out.println("Finished creation of file through libgfapi in " + (System.currentTimeMillis() - time) + "ms");
        
        /* gluster mount big file test */
        File foutFile = new File(GLUSTER_MOUNT + "/test/test12.txt");
        foutFile.delete();
        foutFile.createNewFile();
        BufferedOutputStream foutStream = new BufferedOutputStream(new FileOutputStream(foutFile));
        time = System.currentTimeMillis();
        System.out.println("Starting creation of file through FUSE:" + foutFile.getPath());
        for(int i=0;i<1000000;i++){
        	foutStream.write(someValue);
        	
        }
        foutStream.flush();
        foutStream.close();
        System.out.println("Finished creation of file through FUSE in " + (System.currentTimeMillis() - time) + "ms");
      }
    
    
    public void testRead() throws Exception{
        GlusterFile base = getTestBase();
        GlusterFile out = new GlusterFile(base,"test1.txt");
        File fout = new File(GLUSTER_MOUNT + "/test/test1_fuse.txt");
        
        if(out.exists())
            out.delete();
        if(fout.exists())
            fout.delete();
        
            out.createNewFile();
            fout.createNewFile();
            
            /* libgfapi big file test */
            
            byte[] someValue = "abc123".getBytes();
            System.out.println("Writing these bytes:");
            for(int i=0;i<someValue.length;i++){
                System.out.print(someValue[i] + " ");
            }
            
            IGlusterOutputStream outStream = out.outputStream();
            FileOutputStream fOutStream = new FileOutputStream(fout);
            System.out.println("\nStarting creation of file through libgfapi\n");
            
            //for(int i=0;i<1;i++){
                outStream.write(someValue,0,someValue.length);
                fOutStream.write(someValue);
                
           // }
            outStream.flush();
            outStream.close();
            
            fOutStream.flush();
            fOutStream.close();
            
        
            InputStream gis = new BufferedInputStream((InputStream)out.bufferedInputStream());
            int read = 0;
            System.out.println("Gluster File:");
            while(read != -1){
                read = gis.read();
                System.out.format("%d ", read);
            }
            gis.close();
            gis = new FileInputStream(fout);
            read = 0;
            System.out.println("\nRegular File:");
            while(read != -1){
                read = gis.read();
                System.out.format("%d ", read);
            }
            gis.close();
            
      }
    
	public void testRename(){
	
	     GlusterFile base = getTestBase();
	  
	    GlusterFile theFile = (new GlusterFile(base,"startFileName.txt"));
	    theFile.createNewFile();
	    
	   assertTrue(theFile.renameTo("newFileName.txt"));
	     
	   GlusterFile theFile2 = (new GlusterFile(base,"newFileName.txt"));
	   assertTrue(theFile2.exists());
	
	 }
	
	
	public void testDirList(){
		 String[] paths = {"a","b","c","1","2","3"};
	     GlusterFile base = getTestBase();
	     for(int i=0;i<paths.length;i++){
	    	 (new GlusterFile(base,paths[i])).mkdir();
	     }
	   
	     GlusterFile[] listings = base.listFiles();
	     
	     assertTrue(listings.length == paths.length);
	     for(int i=0;i<listings.length;i++){
	    	 boolean found = false;
	    	 for(int j=0;j<paths.length && !found;j++){
	    		 found = paths[j].compareTo(listings[i].getName())==0;
	    	 }
	    	 assertTrue(found);
	     }
	
	 }


}