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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

public class LibUtil {

    public static String PATH_SEP = Character.toString(File.separatorChar);
    public static File copyFromJar(String path, File dst) throws IOException{

        if(!path.startsWith(PATH_SEP)) {
            path = PATH_SEP + path;
        }

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

    /**
     * Attempt to clean direct memory reserved by a java.nio.DirectByteBuffer.
     * This assumes we are dealing with the Sun implementation that relies on a phantom references cleanup based sun.misc.Cleaner.
     * If this is not the case (or the buffer is not even a DirectByteBuffer), this method will silently ignore it and to nothing.
     * As all this is private or package private we hack around using reflection. It's the only way to free direct memory without triggering a gc.
     */
    public static void trySilentlyReleaseDirectByteBuffer(ByteBuffer buffer) {
        try {
            Method cleanerMethod = buffer.getClass().getMethod("cleaner");
            cleanerMethod.setAccessible(true);
            Object cleaner = cleanerMethod.invoke(buffer);
            Method cleanMethod = cleaner.getClass().getMethod("clean");
            cleanMethod.setAccessible(true);
            cleanMethod.invoke(cleaner);
        } catch (IllegalAccessException e) {
            // ignore
        } catch (InvocationTargetException e) {
            // ignore
        } catch (NoSuchMethodException e) {
            // ignore
        }

    }
}
