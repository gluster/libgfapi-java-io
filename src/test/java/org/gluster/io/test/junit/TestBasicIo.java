package org.gluster.io.test.junit;

import static org.junit.Assert.assertTrue;

import org.gluster.fs.GlusterFile;
import org.junit.Test;

public class TestBasicIo extends TestLibgfapiJava{


	
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