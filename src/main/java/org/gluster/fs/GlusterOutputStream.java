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

import glusterfsio.glfs_java;

public class GlusterOutputStream extends OutputStream implements IGlusterOutputStream {
    protected long fd;
    protected static final int BUFFER_SIZE = 512 * 1024;
    protected ByteBuffer buf;

    protected GlusterOutputStream(String path, long handle) throws IOException {
        fd = glfs_java.glfs_java_open_write(handle, path);
        if (fd == 0) {
            glfs_java.glfs_java_file_createNewFile(handle, path);

            fd = glfs_java.glfs_java_open_write(handle, path);
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
        glfs_java.glfs_java_seek_set(fd, position);
    }

    public void flush() throws IOException{
        glfs_java.glfs_java_write(fd, buf, buf.position());
        buf.rewind();
    }

    /** 
     * Write data using direct allocated ByteBuffer to avoid data copy.  
     * ByteBuffer must with allocated with ByteBuffer.allocateDirect(size).
     *
     * @param buffer containing data to be written into output stream.
     *
     */
    public synchronized void write(ByteBuffer buffer) throws IOException {
      glfs_java.glfs_java_write(fd, buffer, buffer.position());
      buffer.rewind();
    }

    /**
     * Original implementation copied the given byte array to the buf one
     * byte at a time, which was extremely slow.  This version segments the
     * given byte array into segements that fit within the capacity of buf,
     * and then copy and send those segments, substantially increasing 
     * performance.
     *
     */
    public synchronized void write(byte b[], int off, int len) throws IOException{
      int segmentLength = buf.capacity();
      int segments = (len / segmentLength);

      //If the given buffer is less than one segment size, then just write 
      //the buffer as a segement.
      if( segments == 0 )
      {
        writeSegment(b, off, len);
      }
      else
      {
        //Check if we have any bytes hanging off our segment boundary,
        //if so, send one additional non-full segment.
        if( (len % segmentLength) != 0 )
        {
          segments++;
        }
        for(int i = 0; i < segments; i++)
        {
          if( ((i * segmentLength) + segmentLength) < len )
          {
            writeSegment(b, (off + (i * segmentLength)), segmentLength);
          }
          else
          {
            int remaining = (len - (i * segmentLength));
            writeSegment(b, (off + (i * segmentLength)), remaining);
          }
        }
      }
      /* Old implementation is to slow for large byte arrays.
        for (int i = off; i < len; i++) {
            write(b[i]);
        }
      */
    }

    private synchronized void writeSegment(byte b[], int off, int len) throws IOException
    {
      if( len > buf.capacity() ) {
        throw new IOException("segment length must not be greater than " + 
            "buf.capacity() [" + buf.capacity() + " bytes].");
      }
      if( (buf.position() + len) > buf.capacity() ) {
        flush();
      }
      buf.put(b, off, len);
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

            }finally{
                LibUtil.trySilentlyReleaseDirectByteBuffer(buf);
                glfs_java.glfs_java_close(fd);
                fd = 0;
            }

        }

    }

    protected void finalize(){
        close();
    }


}
