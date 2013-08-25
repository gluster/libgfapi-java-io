import glusterfsio.*;
import java.io.*;

public class glfsxmp {
    public static void main(String [] args) throws IOException, InstantiationException, InterruptedException {
	GlusterFileSystem fs = new GlusterFileSystem("patchy");

	System.out.println(fs.getVolumeName());

	fs.setLogging("/dev/stdout", 7);

	GlusterFile subdir = new GlusterFile (fs, "subdir");

	GlusterFile filein = new GlusterFile (fs, "file-input");

	GlusterFile fileout = new GlusterFile (subdir, "file-output");

	GlusterFile subdirs = new GlusterFile (fs, "subdir1/subdir2/subdir3/subdir4");

	subdir.mkdir();

	filein.createNewFile();
	fileout.createNewFile();

	GlusterFileInputStream fdin = new GlusterFileInputStream (filein);

	byte[] data = new byte[1024];

	int ret = fdin.read (data, 1024);
	System.out.printf("read ret=%d, data=%s\n", ret, new String(data));

	GlusterFileOutputStream fdout = new GlusterFileOutputStream (fileout);
	fdout.write (data, ret);
	fdin.close();
	fdout.close ();

	fileout.delete();
	subdir.delete();
	subdirs.mkdirs();
    }
}
