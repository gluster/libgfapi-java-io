/*
  Copyright (c) 2013 Red Hat, Inc. <http://www.redhat.com>
  This file is part of GlusterFS.

  This file is licensed to you under your choice of the GNU Lesser
  General Public License, version 3 or any later version (LGPLv3 or
  later), or the GNU General Public License, version 2 (GPLv2), in all
  cases as published by the Free Software Foundation.
*/

package org.gluster.fs;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;

import glusterfsio.glfs_javaJNI;


public class GlusterFile {

	private String path;
	private long handle;

	protected GlusterFile(String path, long handle) {
		this.handle = handle;
		/* all paths should not have trailing slash.  tolerate + strip it off */
		
		if(path.length()>1 && path.charAt(path.length()-1)==GlusterClient.PATH_SEPARATOR){
			this.path = path.substring(0,path.length()-1);
		}else{
			this.path = path;
		}
	}

	public GlusterFile(GlusterFile parent, String path) {
		this( parent.getPath() + GlusterClient.PATH_SEPARATOR + path,parent.handle);
	}

	/*
	 * convenience method to return any file on the same file system given its
	 * path
	 */
	public GlusterFile open(String path) {
		return new GlusterFile(path, this.handle);

	}

	private static String getName(String fullPath){
		int index = fullPath.lastIndexOf(GlusterClient.PATH_SEPARATOR);
		return fullPath.substring(index + 1);
	}
	
	public String pathOnly() {
		int index = path.lastIndexOf(GlusterClient.PATH_SEPARATOR);
		if(index<1) return Character.toString(GlusterClient.PATH_SEPARATOR);
		return path.substring(0,index + 1);
	}
	
	public String getName() {
	
		return getName(this.path);
	}

	public String getPath(){
		return this.path;
	}
	
    public String getParent() {
        int index = path.lastIndexOf(GlusterClient.PATH_SEPARATOR);
        return path.substring(0, index);
   }
    
    public GlusterFile getAbsoluteFile() {
        return this;
    }
    
    private static String slashify(String path, boolean isDirectory) {
        String p = path;
        if (GlusterClient.PATH_SEPARATOR != '/')
            p = p.replace(GlusterClient.PATH_SEPARATOR, '/');
        if (!p.startsWith("/"))
            p = "/" + p;
        if (!p.endsWith("/") && isDirectory)
            p = p + "/";
        return p;
    }
    
    public URI toURI() {
        try {
        	String sp = slashify(getPath(), isDirectory());
            if (sp.startsWith("//"))
                sp = "//" + sp;
            return new URI("glusterfs", null, sp, null, null);
        } catch (Exception e) {
			// impossible.
		}
        return null;
    }

	public boolean mkdirs() {
		String[] pieces = (path.indexOf(GlusterClient.PATH_SEPARATOR) == 0 ? path.substring(1) : path).split(String.valueOf(GlusterClient.PATH_SEPARATOR));
		String p = "";
		int i;
		GlusterFile f = null;

		for (i = 0; i < pieces.length; i++) {
			p = p.concat(String.valueOf(GlusterClient.PATH_SEPARATOR)).concat(
					pieces[i]);
			f = open(p);
			if (!f.exists()) {
				if (!f.mkdir())
					return false;
			} else if (!f.isDirectory()) {
				return false;
			}
		}

		return (f != null && f.isDirectory());
	}

	public GlusterOutputStream outputStream() throws IOException {
		return new GlusterOutputStream(this.path, this.handle);
	}

	public GlusterInputStream inputStream() throws IOException {
		return new GlusterInputStream(this.path, this.handle);
	}

	public GlusterBufferedInputStream bufferedInputStream(){
	    try {
            return new GlusterBufferedInputStream(this);
        } catch (IOException e) {
            return null;
        }
	}
	
	public String fullPath(String childName){
		return this.path + GlusterClient.PATH_SEPARATOR + childName;
	}
	
	public GlusterFile[] listFiles() {
		String[] ss = list();
		if (ss == null)
			return null;
		int n = ss.length;
		GlusterFile[] fs = new GlusterFile[n];
		for (int i = 0; i < n; i++) {
			fs[i] = open(fullPath(ss[i]));
		}
		return fs;
	}
	
	public GlusterFile[] listFiles(FilenameFilter filter) {
        String ss[] = list();
        if (ss == null) return null;
        ArrayList<GlusterFile> files = new ArrayList<GlusterFile>();
        for (String s : ss)
            if ((filter == null) || filter.accept(new File(this.path),s))
                files.add(open(fullPath(s)));
        return files.toArray(new GlusterFile[files.size()]);
    }
	
	public String[] list() {
		
		if(isFile()) return null;
		
		String list[] = glfs_javaJNI.glfs_java_list_dir(handle, this.path + GlusterClient.PATH_SEPARATOR);
		/* API returns null for no elements */
		if(list==null)
			list = new String[0];
		
		for(int i=0;i<list.length;i++){
			list[i] = getName(list[i]);
		}
		return list;
	}
	
	public String[] list(FilenameFilter filter) {
        String names[] = list();
        if ((names == null) || (filter == null)) {
            return names;
        }
        ArrayList v = new ArrayList();
        for (int i = 0 ; i < names.length ; i++) {
            if (filter.accept(new File(this.path),names[i])) {
                v.add(getName(names[i]));
            }
        }
        return (String[])(v.toArray(new String[v.size()]));
    }
	
	public boolean delete(boolean recursive) throws IOException{
	    	if(isDirectory() && recursive){
		    	String files[] = list();
		        for (String temp : files) {
		        	new GlusterFile(this,temp).delete(recursive);
		        }
		    }
	    	return delete();
	}
	
	public String toString() {
		return path;
	}
	
	
	public String getXAttr(String attr) {
		return  glfs_javaJNI.glfs_java_getxattr(handle, path, attr);
	}
	
	public boolean chmod(int mode) {
		return  glfs_javaJNI.glfs_java_chmod(handle,path,mode)==0;
	}
	
	public long getMod() {
		return  glfs_javaJNI.glfs_java_getmod(handle,path);
	}
	
	public long getUid() {
		return  glfs_javaJNI.glfs_java_getuid(handle,path);
	}

	public void setUid(long uid){
		long gid = getUid();
		chown(uid,gid);		
	}
	
	public void setGid(long gid){
		long uid = getUid();
		chown(uid,gid);
	}
	
	public void chown(long uid, long gid){
		 glfs_javaJNI.glfs_java_chown(handle,path,uid,gid);
	}
	
	public long getGid() {
		return  glfs_javaJNI.glfs_java_getgid(handle,path);
	}

	public long getAtime() {
		return  glfs_javaJNI.glfs_java_getatime(handle,path);
	}
	
	public long getMtime() {
		return  glfs_javaJNI.glfs_java_getmtime(handle,path);
	}

	public long getCtime() {
		return  glfs_javaJNI.glfs_java_getctime(handle,path);
	}
	
	public int getBlockSize() {
		return  glfs_javaJNI.glfs_java_getblocksize(handle,path);
	}
	
	public boolean renameTo(String dstpath) {
		if(dstpath.charAt(0)==GlusterClient.PATH_SEPARATOR){
			return glfs_javaJNI.glfs_java_file_renameTo(handle, path, dstpath);
		}
		String pathBase = pathOnly();
		return glfs_javaJNI.glfs_java_file_renameTo(handle, path, pathBase + dstpath);
		
	}

	public long length() {
		return glfs_javaJNI.glfs_java_file_length(handle, path);
	}

	public boolean exists() {
		return glfs_javaJNI.glfs_java_file_exists(handle, path);
	}

	public boolean createNewFile() {
		return glfs_javaJNI.glfs_java_file_createNewFile(handle, path);
	}

	public boolean mkdir() {
		return glfs_javaJNI.glfs_java_file_mkdir(handle, path);
	}

	public boolean isDirectory() {
		return glfs_javaJNI.glfs_java_file_isDirectory(handle, path);
	}

	public boolean isFile() {
		return glfs_javaJNI.glfs_java_file_isFile(handle, path);
	}
	
	public boolean delete() {
		return glfs_javaJNI.glfs_java_file_delete(handle, path);
	}

	public boolean renameTo(GlusterFile dst) {
		return renameTo(dst.path);
	}
}
