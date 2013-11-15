package org.gluster.fs;

import java.io.IOException;
import java.io.OutputStream;

import org.gluster.io.glfs_javaJNI;

public class GlusterOutputStream extends OutputStream{
	    private long fd;

	    protected GlusterOutputStream(String path, long handle) throws IOException {
	        fd = glfs_javaJNI.glfs_java_open_write(handle, path);
	        if (fd == 0) {
	            throw new IOException();
	        }
	    }

	    public void write(byte [] buf) {
	        glfs_javaJNI.glfs_java_write (fd, buf, buf.length);
	        return;
	    }

	    public void write(byte [] buf, int length) {
	        glfs_javaJNI.glfs_java_write (fd, buf, length);
	        return;
	    }

	    public void pwrite(byte [] buf, int offset) {
	        glfs_javaJNI.glfs_java_pwrite (fd, buf, buf.length, offset);
	        return;
	    }

	    public void pwrite(byte [] buf, int length, int offset) {
	        glfs_javaJNI.glfs_java_pwrite (fd, buf, length, offset);
	        return;
	    }

	    public void write(int b) {
	        byte[] buf = new byte[1];
	        buf[0] = (byte)b;
	        glfs_javaJNI.glfs_java_write (fd, buf, 1);
	        return;
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
