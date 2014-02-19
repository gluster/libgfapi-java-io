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
import java.io.OutputStream;

import glusterfsio.glfs_javaJNI;

public class GlusterOutputStream extends OutputStream{
	    private long fd;
	    protected static final int BUFFER_SIZE=1024*512;
	    protected byte buf[];
	    protected int count;
	    
	    
	    protected GlusterOutputStream(String path, long handle) throws IOException {
	        fd = glfs_javaJNI.glfs_java_open_write(handle, path);
	        if (fd == 0) {
	        	glfs_javaJNI.glfs_java_file_createNewFile(handle, path);
	       
	        	fd = glfs_javaJNI.glfs_java_open_write(handle, path);
	        	if (fd == 0) 
	        		throw new IOException("Error opening io stream.");
	        	}
	        buf = new byte[BUFFER_SIZE];

	        count = 0;
	    }

	    public void position(long position){
	    	try {
				flushBuffer();
			} catch (IOException e) {
			}
	    	glfs_javaJNI.glfs_java_seek_set(fd, position);
	    }
	    
	    private void flushBuffer() throws IOException {
	        if (count >= 0) {
	        	glfs_javaJNI.glfs_java_write (fd, buf, count);
	            count = 0;
	        }
	    }
	    /*
	    public synchronized void write(byte b[], int off, int len) throws IOException {
	        if (len >= buf.length) {
	            flushBuffer();
	            glfs_javaJNI.glfs_java_write (fd, b, buf.length);
	            return;
	        }
	        if (len > buf.length - count) {
	            flushBuffer();
	        }
	        System.arraycopy(b, off, buf, count, len);
	        count += len;
	    }
=======
	    }

	    private void flushBuffer() throws IOException {
	        if (count >= 0) {
	        	glfs_javaJNI.glfs_java_write (fd, buf, count);
	            count = 0;
	        }
	    }
	    /*
	    public synchronized void write(byte b[], int off, int len) throws IOException {
	        if (len >= buf.length) {
	            flushBuffer();
	            glfs_javaJNI.glfs_java_write (fd, b, buf.length);
	            return;
	        }
	        if (len > buf.length - count) {
	            flushBuffer();
	        }
	        System.arraycopy(b, off, buf, count, len);
	        count += len;
	    }
>>>>>>> de1544a6571b57f558a7490f3bc1da5b67a096d8
	    */
	    
	    public synchronized void write(byte b[], int off, int len) throws IOException {
	    	for(int i=off;i<len;i++){
	    		write(b[i]);
	    	}
	    }
	    
	  
	/*
	    public void pwrite(byte [] buf, int offset) {
		glfs_javaJNI.glfs_java_pwrite (fd, buf, buf.length, offset);
		return;
	    }

	    public void pwrite(byte [] buf, int length, int offset) {
		glfs_javaJNI.glfs_java_pwrite (fd, buf, length, offset);
		return;
	    }
	*/
	 

	    public void close() {
	    	if (fd != 0) {
	    		try {
					flushBuffer();
				} catch (IOException e) {
					
				}
			    glfs_javaJNI.glfs_java_close(fd);
			    fd = 0;
			}
	    	
			
	    }

	    protected void finalize() {
			close();
	    }
	    public synchronized void write(int b) throws IOException {
	        if (count >= buf.length) {
	            flushBuffer();
	        }
	        buf[count++] = (byte)b;
	    }
}
