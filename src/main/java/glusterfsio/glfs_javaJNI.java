/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.8
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package glusterfsio;

public class glfs_javaJNI {
  public final static native int glfs_java_volume_size(long jarg1, String jarg2);
  public final static native int glfs_java_volume_free(long jarg1, String jarg2);
  public final static native String glfs_java_getxattr(long jarg1, String jarg2, String jarg3);
  public final static native int glfs_java_chown(long jarg1, String jarg2, long jarg3, long jarg4);
  public final static native int glfs_java_chmod(long jarg1, String jarg2, long jarg3);
  public final static native long glfs_java_getmod(long jarg1, String jarg2);
  public final static native long glfs_java_getuid(long jarg1, String jarg2);
  public final static native long glfs_java_getgid(long jarg1, String jarg2);
  public final static native int glfs_java_getblocksize(long jarg1, String jarg2);
  public final static native int glfs_java_getmtime(long jarg1, String jarg2);
  public final static native int glfs_java_getctime(long jarg1, String jarg2);
  public final static native int glfs_java_getatime(long jarg1, String jarg2);
  public final static native long glfs_new(String jarg1);
  public final static native int glfs_init(long jarg1);
  public final static native int glfs_fini(long jarg1);
  public final static native int glfs_set_logging(long jarg1, String jarg2, int jarg3);
  public final static native int glfs_set_volfile_server(long jarg1, String jarg2, String jarg3, int jarg4);
  public final static native int glfs_set_volfile(long jarg1, String jarg2);
  public final static native int glfs_java_file_length(long jarg1, String jarg2);
  public final static native boolean glfs_java_file_exists(long jarg1, String jarg2);
  public final static native boolean glfs_java_file_delete(long jarg1, String jarg2);
  public final static native boolean glfs_java_file_renameTo(long jarg1, String jarg2, String jarg3);
  public final static native boolean glfs_java_file_createNewFile(long jarg1, String jarg2);
  public final static native boolean glfs_java_file_mkdir(long jarg1, String jarg2);
  public final static native boolean glfs_java_file_isDirectory(long jarg1, String jarg2);
  public final static native boolean glfs_java_file_isFile(long jarg1, String jarg2);
  public final static native String[] glfs_java_list_dir(long jarg1, String jarg2);
  public final static native long glfs_java_open_read(long jarg1, String jarg2);
  public final static native long glfs_java_open_write(long jarg1, String jarg2);
  public final static native int glfs_java_seek_set(long jarg1, long jarg2);
  public final static native int glfs_java_seek_current(long jarg1, long jarg2);
  public final static native int glfs_java_seek_end(long jarg1, int jarg2);
  public final static native int glfs_java_read(long jarg1,  byte[]  jarg2, long jarg3);
  public final static native int glfs_java_pread(long jarg1,  byte[]  jarg2, long jarg3, long jarg4);
  public final static native int glfs_java_write(long jarg1,  byte[]  jarg2, long jarg3);
  public final static native int glfs_java_pwrite(long jarg1,  byte[]  jarg2, long jarg3, long jarg4);
  public final static native int glfs_java_close(long jarg1);
}