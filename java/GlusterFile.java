package glusterfsio;

public class GlusterFile {
    public GlusterFileSystem _fs;
    private String _path;

    public GlusterFile (GlusterFileSystem fs, String path) {
	_fs = fs;
	_path = path;
    }

    public GlusterFile (GlusterFile parent, String path) {
	_fs = parent._fs;
	_path = parent._path.concat("/").concat(path);
    }

    public String toString() {
	return _path;
    }

    public long length() {
	return glfs_javaJNI.glfs_java_file_length (_fs.internal_handle(), _path);
    }

    public boolean exists() {
	return glfs_javaJNI.glfs_java_file_exists (_fs.internal_handle(), _path);
    }

    public boolean createNewFile() {
	return glfs_javaJNI.glfs_java_file_createNewFile (_fs.internal_handle(), _path);
    }

    public boolean mkdir() {
	return glfs_javaJNI.glfs_java_file_mkdir (_fs.internal_handle(), _path);
    }

    public boolean isDirectory() {
	return glfs_javaJNI.glfs_java_file_isDirectory (_fs.internal_handle(), _path);
    }

    public boolean isFile() {
	return glfs_javaJNI.glfs_java_file_isFile (_fs.internal_handle(), _path);
    }

    public boolean mkdirs() {
	String [] pieces = _path.split("/");
	String p = "";
	int i;
	GlusterFile f = null;

	for (i = 0; i < pieces.length; i++) {
	    p = p.concat("/").concat(pieces[i]);
	    f = new GlusterFile (_fs, p);
	    if (!f.exists()) {
		if (!f.mkdir())
		    return false;
	    } else if (!f.isDirectory()) {
		return false;
	    }
	}

	return (f != null && f.isDirectory());
    }

    public boolean delete() {
	return glfs_javaJNI.glfs_java_file_delete (_fs.internal_handle(), _path);
    }

    public boolean renameTo(GlusterFile dst) {
	if (dst._fs != _fs)
	    return false;

	return glfs_javaJNI.glfs_java_file_renameTo (_fs.internal_handle(), _path, dst._path);
    }

    public boolean renameTo(String dstpath) {
	return glfs_javaJNI.glfs_java_file_renameTo (_fs.internal_handle(), _path, dstpath);
    }
}
