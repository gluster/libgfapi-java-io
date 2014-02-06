/*
  Copyright (c) 2013 Red Hat, Inc. <http://www.redhat.com>
  This file is part of GlusterFS.

  This file is licensed to you under your choice of the GNU Lesser
  General Public License, version 3 or any later version (LGPLv3 or
  later), or the GNU General Public License, version 2 (GPLv2), in all
  cases as published by the Free Software Foundation.
*/


package org.gluster.fs;

import java.io.IOException;
import java.io.InputStream;

import glusterfsio.glfs_javaJNI;

public class GlusterInputStream extends InputStream{
	    private long fd;
	    
	    /* user should never directly instantiate */
	    protected GlusterInputStream(String file, long handle) throws IOException {
	        fd = glfs_javaJNI.glfs_java_open_read(handle, file);
	        if (fd == 0) {
	            throw new IOException();
	        }
	    }

	    public boolean seek(int location){
	    	/* need to error if out of bounds.  not sure how its handled lower */
	    	glfs_javaJNI.glfs_java_seek_set(fd, location);
	    
	    	return true;
	    }
	    
	    public long offset(){
	    	return glfs_javaJNI.glfs_java_seek_set(fd, 0);
	    }
	    
	    public int read(byte [] buf, int size) {
	        int read = glfs_javaJNI.glfs_java_read(fd, buf, size);
	        
	        if(read<1) return -1;
	        
	        return read;
	    }
	    
	    public int read(byte b[], int off, int len) throws IOException {
	    	  if(off>0 || len < b.length){
	    		  byte[] newBuffer = new byte[len-off];
	    		  int read = read(newBuffer, len);
	    		  int start = off;
	    		  for(int j=0;j<read;j++){
	    			  b[start++] = newBuffer[j];
	    		  }
	    		  
	    	  }
	    	  return read(b,len);
	    }
	    

	    public int read() {
	        byte[] buf = new byte[1];

	        glfs_javaJNI.glfs_java_read(fd, buf, 1);

	        return buf[0];
	    }

	    public void close() {
	        glfs_javaJNI.glfs_java_close(fd);
	        fd = 0;
	    }

	    protected void finalize() {
	        if (fd != 0) {
	            glfs_javaJNI.glfs_java_close(fd);
	            fd = 0;
	        }
	    }
}
