package org.gluster.fs;

import java.io.IOException;


public interface IGlusterInputStream {
    public boolean seek(long location);
    public long offset();
    public int read(byte[] bytes, int off, int len) ;
    public int read();
    public void close() throws IOException;
    
}
