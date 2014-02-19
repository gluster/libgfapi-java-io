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

import java.io.BufferedInputStream;
import java.io.IOException;

public class GlusterBufferedInputStream extends BufferedInputStream {
    
    public static final int MAX_BUFFER = 1024*256;
    public static final boolean FORCE_CONSISTANCY = false;
    long mtime = -1;
    GlusterFile file;
    
    
    public GlusterBufferedInputStream(GlusterFile file) throws IOException {
        super(file.inputStream(), MAX_BUFFER);
        mtime = file.getMtime();
        this.file = file;
    }

    public int offset(){
        return pos;
    }
    
    public void seek(long l){
        
            long skip = l-pos;
            try {
                super.skip((int)skip);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        
            return;
            /*
            ((GlusterInputStream)in).seek(pos);
            this.pos = (int)((GlusterInputStream)in).offset();
            markpos = -1;
            */
    }
    
    
    /* 
     * this nasty little method checks the modified time for a file.  If the file has been modified it dumps the buffer
     * This should not be needed, especially when chewing through small chunks.
     * 
     * There is a chance however that the data has gone stale, so maybe put this on a timer/thread.
     * 
     */
    private void checkBuffer(){
 
        if(FORCE_CONSISTANCY && file.getMtime()!=mtime){
            mtime = file.getMtime();
            markpos = -1;
           
        }
    }
    
    public synchronized int read() throws IOException{
        checkBuffer();
        return super.read();
    }

    public synchronized int read(byte[] b, int off, int len) throws IOException{
        checkBuffer();
        return super.read(b, off, len);
    }

    public synchronized long skip(long n) throws IOException{
        checkBuffer();
        return super.skip(n);
    }

    public synchronized int available() throws IOException{
        checkBuffer();
        return super.available();
    }
    
}
