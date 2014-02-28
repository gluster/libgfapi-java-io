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
import java.nio.ByteBuffer;

import glusterfsio.glfs_javaJNI;

public class GlusterInputStream extends InputStream implements IGlusterInputStream {
    private long fd;
    boolean closing = false;

    /* user should never directly instantiate */
    protected GlusterInputStream(String file, long handle) throws IOException {
        fd = glfs_javaJNI.glfs_java_open_read(handle, file);
        if (fd == 0) {
            throw new IOException();
        }
    }

    public boolean seek(long location){
        /* need to error if out of bounds. not sure how its handled lower */
        glfs_javaJNI.glfs_java_seek_set(fd, location);

        return true;
    }

    public long offset(){
        return glfs_javaJNI.glfs_java_seek_set(fd, 0);
    }

    public int read(byte[] bytes, int off, int len)  {
        ByteBuffer buf = ByteBuffer.allocateDirect(bytes.length);
        read(buf,len);
        buf.get(bytes, off, len);
       return len;
    }

    public int read(ByteBuffer buf, int size){
        return glfs_javaJNI.glfs_java_read(fd, buf, size);

    }

    public int read(){
        ByteBuffer buf = ByteBuffer.allocateDirect(1);
        if (read(buf,1) == 0)
            return -1;

        return buf.get();
    }

    public void close() throws IOException{
            if (fd != 0 && !closing) {
                closing = true;
                glfs_javaJNI.glfs_java_close(fd);
                fd = 0;
            }
     
    }

    protected void finalize(){

    }
}
