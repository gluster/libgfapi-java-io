package glusterfsio;

public class GlusterFileSystem {
    static {
	System.loadLibrary("gfapi-java");
    }

    private String volname = null;
    private long fs;

    private void init(String VolumeName, String Server, int Port, String Transport) throws InstantiationException {
	int ret;

	fs = glfs_javaJNI.glfs_new(VolumeName);

	if (fs == 0) {
	    throw new InstantiationException();
	}

	ret = glfs_javaJNI.glfs_set_volfile_server(fs, Transport, Server, Port);
	if (ret == -1) {
	    glfs_javaJNI.glfs_fini(fs);
	    throw new InstantiationException();
	}

	ret = glfs_javaJNI.glfs_init(fs);
	if (ret == -1) {
	    glfs_javaJNI.glfs_fini(fs);
	    throw new InstantiationException();
	}

	volname = VolumeName;
    }

    public GlusterFileSystem(String VolumeName) throws InstantiationException{
	init(VolumeName, "localhost", 0, "tcp");
    }

    public GlusterFileSystem(String VolumeName, String Server) throws InstantiationException {
	init(VolumeName, Server, 0, "tcp");
    }

    public GlusterFileSystem(String VolumeName, String Server, int Port, String Transport) throws InstantiationException {
	init(VolumeName, Server, Port, Transport);
    }

    public long internal_handle() {
	return fs;
    }

    public String getVolumeName() {
	return volname;
    }

    public int setLogging(String LogFile, int LogLevel) {
	int ret;
	ret = glfs_javaJNI.glfs_set_logging(fs, LogFile, LogLevel);
	return ret;
    }

    public int setVolFile(String VolFile) {
	int ret;
	ret = glfs_javaJNI.glfs_set_volfile(fs, VolFile);
	return ret;
    }

    public int setVolFileServer(String Transport, String Host, int Port) {
	int ret;
	ret = glfs_javaJNI.glfs_set_volfile_server(fs, Transport, Host, Port);
	return ret;
    }
}
