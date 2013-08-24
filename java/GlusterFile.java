package glusterfsio;

public class GlusterFile {
    public GlusterFileSystem _fs;
    private String _path;

    public GlusterFile (GlusterFileSystem fs, String path) {
	_fs = fs;
	_path = path;
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
}
