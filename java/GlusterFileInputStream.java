package glusterfsio;

import java.io.*;

public class GlusterFileInputStream extends InputStream {
    private long fd;

    public GlusterFileInputStream(GlusterFile file) throws IOException {
	fd = glfs_javaJNI.glfs_open_read(file._fs.internal_handle(), file.toString());
	if (fd == 0) {
	    throw new IOException();
	}
    }

    public int read(byte [] buf, int size) {
	return glfs_javaJNI.glfs_java_read(fd, buf, size);
    }

    public int read() {
	byte[] buf = new byte[1];

	glfs_javaJNI.glfs_java_read(fd, buf, 1);

	return buf[0];
    }
}
