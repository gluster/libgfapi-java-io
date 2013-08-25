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
	return (ret == 0);
}


bool
glfs_java_file_isDirectory (glfs_t *glfs, const char *path)
{
	struct stat buf;
	int ret;

	ret = glfs_lstat (glfs, path, &buf);
	return (ret == 0 && S_ISDIR (buf.st_mode));
}


bool
glfs_java_file_isFile (glfs_t *glfs, const char *path)
{
	struct stat buf;
	int ret;

	ret = glfs_lstat (glfs, path, &buf);
	return (ret == 0 && !S_ISDIR (buf.st_mode));
}


long
glfs_java_read (glfs_fd_t *glfd, void *io_data, size_t size)
{
	return glfs_read (glfd, io_data, size, 0);
}


long
glfs_java_pread (glfs_fd_t *glfd, void *io_data, size_t size, off_t offset)
{
	return glfs_pread (glfd, io_data, size, offset, 0);
}


long
glfs_java_write (glfs_fd_t *glfd, void *io_data, size_t size)
{
	return glfs_write (glfd, io_data, size, 0);
}


long
glfs_java_pwrite (glfs_fd_t *glfd, void *io_data, size_t size, off_t offset)
{
	return glfs_pwrite (glfd, io_data, size, offset, 0);
}


bool
glfs_java_file_createNewFile (glfs_t *glfs, const char *path)
{
	int ret;

	ret = glfs_mknod (glfs, path, S_IFREG | 0644, 0);

	return (ret == 0);
}


bool
glfs_java_file_delete (glfs_t *glfs, const char *path)
{
	int ret;
	struct stat buf;

	ret = glfs_lstat (glfs, path, &buf);

	if (ret != 0)
		return false;

	if (S_ISDIR (buf.st_mode))
		ret = glfs_rmdir (glfs, path);
	else
		ret = glfs_unlink (glfs, path);

	return (ret == 0);
}


bool
glfs_java_file_mkdir (glfs_t *glfs, const char *path)
{
	int ret;

	ret = glfs_mkdir (glfs, path, S_IFREG | 0755);

	return (ret == 0);
}


glfs_fd_t *
glfs_java_open_read (glfs_t *glfs, const char *path)
{
	return glfs_open (glfs, path, O_LARGEFILE|O_RDONLY);
}


glfs_fd_t *
glfs_java_open_write (glfs_t *glfs, const char *path)
{
	return glfs_open (glfs, path, O_LARGEFILE|O_WRONLY);
}


int
glfs_java_close (glfs_fd_t *glfd)
{
	return glfs_close (glfd);
}


bool
glfs_java_file_renameTo (glfs_t *glfs, const char *src, const char *dst)
{
	return glfs_rename (glfs, src, dst);
}
