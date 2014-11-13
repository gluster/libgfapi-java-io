/*
  Copyright (c) 2013 Red Hat, Inc. <http://www.redhat.com>
  This file is part of GlusterFS.

  This file is licensed to you under your choice of the GNU Lesser
  General Public License, version 3 or any later version (LGPLv3 or
  later), or the GNU General Public License, version 2 (GPLv2), in all
  cases as published by the Free Software Foundation.
*/


package org.gluster.fs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import glusterfsio.glfs_java;

public class GlusterVolume{



	private String name;
	private long handle;

	public GlusterVolume(String name, long handle){
		this.name = name;
		this.handle = handle;

	}



	public GlusterFile open(String path){

		if(path==null) return null;

		return new GlusterFile(path,handle);


	}



	public String getName() {
		return this.name;
	}

	public long getFree(){
		return glfs_java.glfs_java_volume_free(handle,"/");
	}

	public long getSize(){
		return glfs_java.glfs_java_volume_size(handle,"/");
	}

	public void close(){
	    glfs_java.glfs_fini(this.handle);
	}

	public void finalize(){
	        close();
	}

}
