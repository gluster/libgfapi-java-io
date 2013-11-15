package org.gluster.fs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.io.InputStream;

public class LibUtil {
	
	public static File copyFromJar(String path, File dst) throws IOException{
		InputStream stream = LibUtil.class.getResourceAsStream(path);
	    if (stream == null) {
	        throw new IOException(path + " not found in jar.");
	    }
	    OutputStream resStreamOut = null;
	    int readBytes;
	    
	    String[] split = path.split("/");
	    String fileName= split.length==0? path : split[split.length-1];
	    File result = null;
	    byte[] buffer = new byte[4096];
	    try {
	    	result = new File(dst, fileName);
	        resStreamOut = new FileOutputStream(result);
	        while ((readBytes = stream.read(buffer)) > 0) {
	            resStreamOut.write(buffer, 0, readBytes);
	        }
	    } catch (IOException e1) {
	        e1.printStackTrace();
	    } finally {
	        stream.close();
	        resStreamOut.close();
	    }
	    return result;
	}

}
