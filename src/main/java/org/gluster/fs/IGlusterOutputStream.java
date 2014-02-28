package org.gluster.fs;

import java.io.IOException;

public interface IGlusterOutputStream {
    public void position(long position);
    public void write(byte b[], int off, int len) throws IOException;
    public void write(int b) throws IOException;
    public void close();
    public void flush() throws IOException;
}
