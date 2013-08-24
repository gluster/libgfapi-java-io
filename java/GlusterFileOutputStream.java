package glusterfsio;

import java.io.*;

public class GlusterFileOutputStream extends OutputStream {
    private long fd;

    public GlusterFileOutputStream(GlusterFile file) throws IOException {
	fd = glfs_javaJNI.glfs_open_write(file._fs.internal_handle(), file.toString());
	if (fd == 0) {
	    throw new IOException();
	}
    }

    public void write(byte [] buf) {
	glfs_javaJNI.glfs_java_write (fd, buf, buf.length);
	return;
    }

    public void write(int b) {
	byte[] buf = new byte[1];
	buf[0] = (byte)b;
	glfs_javaJNI.glfs_java_write (fd, buf, 1);
	return;
    }
}
