package org.gluster.fs;

import java.io.IOException;
import java.io.InputStream;

import org.gluster.io.glfs_javaJNI;

public class GlusterInputStream extends InputStream{
	    private long fd;
	    
	    /* user should never directly instantiate */
	    protected GlusterInputStream(String file, long handle) throws IOException {
	        fd = glfs_javaJNI.glfs_java_open_read(handle, file);
	        if (fd == 0) {
	            throw new IOException();
	        }
	    }

	    public int read(byte [] buf, int size) {
	        return glfs_javaJNI.glfs_java_read(fd, buf, size);
	    }

	    public int pread(byte [] buf, int size, int offset) {
	        return glfs_javaJNI.glfs_java_pread(fd, buf, size, offset);
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
