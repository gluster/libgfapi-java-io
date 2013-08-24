/*
  Copyright (c) 2013 Red Hat, Inc. <http://www.redhat.com>
  This file is part of GlusterFS.

  This file is licensed to you under your choice of the GNU Lesser
  General Public License, version 3 or any later version (LGPLv3 or
  later), or the GNU General Public License, version 2 (GPLv2), in all
  cases as published by the Free Software Foundation.
*/

#include "glfs-java.h"


long
glfs_java_file_length (glfs_t *glfs, const char *path)
{
	struct stat buf;
	long ret;

	ret = glfs_lstat (glfs, path, &buf);

	if (ret < 0)
		return -1;

	return buf.st_size;
}


bool
glfs_java_file_exists (glfs_t *glfs, const char *path)
{
	struct stat buf;
	int ret;

	ret = glfs_lstat (glfs, path, &buf);
	if (ret == 0)
		return true;
	return false;
}


long
glfs_java_read (glfs_fd_t *glfd, void *io_data, size_t size)
{
	return glfs_read (glfd, io_data, size, 0);
}


long
glfs_java_write (glfs_fd_t *glfd, void *io_data, size_t size)
{
	return glfs_write (glfd, io_data, size, 0);
}


bool
glfs_java_file_createNewFile (glfs_t *glfs, const char *path)
{
	int ret;

	ret = glfs_mknod (glfs, path, S_IFREG | 0644, 0);
	if (ret == 0)
		return true;
	return false;
}


bool
glfs_java_file_mkdir (glfs_t *glfs, const char *path)
{
	int ret;

	ret = glfs_mkdir (glfs, path, S_IFREG | 0755);
	if (ret == 0)
		return true;
	return false;
}


glfs_fd_t *
glfs_open_read (glfs_t *glfs, const char *path)
{
	return glfs_open (glfs, path, O_LARGEFILE|O_RDONLY);
}


glfs_fd_t *
glfs_open_write (glfs_t *glfs, const char *path)
{
	return glfs_open (glfs, path, O_LARGEFILE|O_WRONLY);
}
