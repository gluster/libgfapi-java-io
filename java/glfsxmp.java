import glusterfsio.*;
import java.io.*;

public class glfsxmp {
    public static void main(String [] args) throws IOException, InstantiationException, InterruptedException {
	GlusterFileSystem fs = new GlusterFileSystem("patchy");

	System.out.println(fs.getVolumeName());

	fs.setLogging("/dev/stdout", 7);

	GlusterFile filein = new GlusterFile (fs, "/subdir/file-input");
	GlusterFile fileout = new GlusterFile (fs, "/subdir/file-output");

	GlusterFileInputStream fdin = new GlusterFileInputStream (filein);

	byte[] data = new byte[1024];

	int ret = fdin.read (data, 1024);
	System.out.printf("read ret=%d, data=%s\n", ret, new String(data));

	GlusterFileOutputStream fdout = new GlusterFileOutputStream (fileout);
	fdout.write (data);
	Thread.sleep (1000);
    }
}
