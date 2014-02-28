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
import java.nio.ByteBuffer;

import glusterfsio.glfs_javaJNI;

public class GlusterOutputStream extends OutputStream implements IGlusterOutputStream {
    private long fd;
    protected static final int BUFFER_SIZE = 1024 * 512;
    protected ByteBuffer buf;

    protected GlusterOutputStream(String path, long handle) throws IOException {
        fd = glfs_javaJNI.glfs_java_open_write(handle, path);
        if (fd == 0) {
            glfs_javaJNI.glfs_java_file_createNewFile(handle, path);

            fd = glfs_javaJNI.glfs_java_open_write(handle, path);
            if (fd == 0)
                throw new IOException("Error opening io stream.");
        }
        buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
        buf.rewind();
    }

    public synchronized void position(long position){
        try {
            flush();
        } catch (IOException e) {
        }
        glfs_javaJNI.glfs_java_seek_set(fd, position);
    }

    public void flush() throws IOException{
        glfs_javaJNI.glfs_java_write(fd, buf, buf.position());
        buf.rewind();
    }

    public synchronized void write(byte b[], int off, int len) throws IOException{
        for (int i = off; i < len; i++) {
            write(b[i]);
        }
    }
    
    public synchronized void write(int b) throws IOException{
        if (buf.position()+1 >= buf.capacity()) {
            flush();
        }
        buf.put((byte) b);
    }

    
    public void close(){
        if (fd != 0) {
            try {
                flush();
            } catch (IOException e) {

            }
            glfs_javaJNI.glfs_java_close(fd);
            fd = 0;
        }

    }

    protected void finalize(){
        close();
    }


}
