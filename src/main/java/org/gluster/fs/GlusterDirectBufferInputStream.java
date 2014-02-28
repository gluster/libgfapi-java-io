/**
 *
 * Copyright (c) 2013 Red Hat, Inc. <http://www.redhat.com>
 * This file is part of GlusterFS.
 *
 * Licensed under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 */

package org.gluster.fs;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GlusterDirectBufferInputStream extends InputStream implements IGlusterInputStream{
    
    public static final int MAX_BUFFER = 1024*256;
    private ByteBuffer buf;
    private GlusterInputStream in;
    private long position;
    
    public GlusterDirectBufferInputStream(GlusterInputStream in,int bufferSize) {
        this.buf = ByteBuffer.allocateDirect(bufferSize).order(ByteOrder.nativeOrder());
        this.in = in;
        position=0;
        buf.limit(0);
        
    }    
    
    public GlusterDirectBufferInputStream(GlusterFile file) throws IOException {
        this((GlusterInputStream)file.inputStream());
    }
    
    public GlusterDirectBufferInputStream(GlusterInputStream in) {
        this(in,MAX_BUFFER);
    }
    
    private long fill(){
        buf.rewind();
        int end = in.read(buf, buf.capacity());
        buf.limit(end);
        return end;
    }
    
    public int read()  {
        if (!(buf.hasRemaining())) {
            if(fill() <= 0){
                return -1;
            }
        }
        position++;
        return buf.get() & 0xFF;
    }

    public int read(byte[] bytes, int off, int len) {
        if (!(buf.hasRemaining())) {
            if(fill() <= 0){
                return -1;
            }
        }
        
        len = Math.min(len, buf.remaining());
        buf.get(bytes, off, len);
        position+=len;
        return len;
    }
    
    public boolean seek(long l){
        
        buf.limit(0);
        position = l;
        in.seek(position);
        return true;
    }

    public long offset(){
        return position;
    }


}
