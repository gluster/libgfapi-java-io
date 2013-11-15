package org.gluster.fs;

import org.gluster.io.glfs_javaJNI;

public class GlusterVolume{
	
	private String name;
	private long handle;
	
	public GlusterVolume(String name, long handle){
		this.name = name;
		this.handle = handle;
	}

	public GlusterFile open(String path){
		return new GlusterFile(path,handle);
		
	}
	
    public int setLogging(String LogFile, int LogLevel) {
    	int ret;
    	ret = glfs_javaJNI.glfs_set_logging(handle, LogFile, LogLevel);
    	return ret;
    }

	public String getName() {
		return this.name;
	}
}
