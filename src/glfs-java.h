/*
  Copyright (c) 2013 Red Hat, Inc. <http://www.redhat.com>
  This file is part of GlusterFS.

  This file is licensed to you under your choice of the GNU Lesser
  General Public License, version 3 or any later version (LGPLv3 or
  later), or the GNU General Public License, version 2 (GPLv2), in all
  cases as published by the Free Software Foundation.
*/


#ifndef _GLFS_JAVA_H
#define _GLFS_JAVA_H

#include <stdbool.h>
#include <api/glfs.h>


glfs_t *glfs_new (const char *volname);
int glfs_init (glfs_t *fs);
int glfs_fini (glfs_t *fs);
int glfs_set_logging (glfs_t *fs, const char *logfile, int loglevel);
int glfs_set_volfile_server (glfs_t *fs, const char *transport,
			     const char *host, int port);
int glfs_set_volfile (glfs_t *fs, const char *volfile);


long glfs_java_file_length (glfs_t *fs, const char *path);
bool glfs_java_file_exists (glfs_t *fs, const char *path);
bool glfs_java_file_delete (glfs_t *fs, const char *path);
bool glfs_java_file_renameTo (glfs_t *glfs, const char *src, const char *dst);
bool glfs_java_file_createNewFile (glfs_t *fs, const char *path);
bool glfs_java_file_mkdir (glfs_t *fs, const char *path);
bool glfs_java_file_isDirectory (glfs_t *fs, const char *path);
bool glfs_java_file_isFile (glfs_t *fs, const char *path);

glfs_fd_t *glfs_java_open_read (glfs_t *fs, const char *filename);
glfs_fd_t *glfs_java_open_write (glfs_t *fs, const char *filename);

long glfs_java_read (glfs_fd_t *fd, void *io_data, size_t size);
long glfs_java_pread (glfs_fd_t *fd, void *io_data, size_t size, off_t offset);
long glfs_java_write (glfs_fd_t *fd, void *io_data, size_t size);
long glfs_java_pwrite (glfs_fd_t *fd, void *io_data, size_t size, off_t offset);


int glfs_java_close (glfs_fd_t *fd);

#endif /* !_GLFS_JAVA_H */
