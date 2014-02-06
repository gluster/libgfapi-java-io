package org.gluster.io.test.junit;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;


import org.gluster.fs.GlusterFile;
import org.gluster.fs.GlusterInputStream;
import org.gluster.fs.GlusterOutputStream;
import org.junit.Test;

public class TestBasicIo extends TestLibgfapiJava{

	@Test
	public void testXAttr(){
		GlusterFile base = getTestBase();
		GlusterFile out = new GlusterFile(base,"a");
		out.createNewFile();
		String xattrOUt = out.getXAttr("trusted.glusterfs.pathinfo");
		assert(xattrOUt!=null && "".compareTo(xattrOUt)!=0 );
		//System.out.println("Xattr Value  : " + xattrOUt);
	}
	
	@Test
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
    public void testMoveToLocal() throws Exception{
		GlusterFile base = getTestBase();
		GlusterFile out = new GlusterFile(base,"aFile.txt");
		out.createNewFile();
	
        
        byte[] someValue = "this is a test its only a test.".getBytes();
        GlusterOutputStream outStream = out.outputStream();
        outStream.write(someValue);
        outStream.close();
        File toFile = new File("/tmp/out1.txt");
        toFile.delete();
        toFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(toFile);
        GlusterInputStream inStream = out.inputStream();
        
        int read = 1;
        byte[] buffer = new byte[4096];
        while(read>0){
        	read = inStream.read(buffer, 0,buffer.length);
        	if(read>0)
        		fos.write(buffer);
        }
        
        fos.close();
        inStream.close();

     }
    
	
	@Test
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