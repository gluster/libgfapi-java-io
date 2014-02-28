/*
  Copyright (c) 2013 Red Hat, Inc. <http://www.redhat.com>
  This file is part of GlusterFS.

  This file is licensed to you under your choice of the GNU Lesser
  General Public License, version 3 or any later version (LGPLv3 or
  later), or the GNU General Public License, version 2 (GPLv2), in all
  cases as published by the Free Software Foundation.
*/

#include "glfs-java.h"




int glfs_java_chown(glfs_t *glfs, const char *path, unsigned int uid, unsigned int gid){
	return  glfs_chown (glfs, path,  uid, gid);
}

int glfs_java_chmod(glfs_t *glfs, const char *path, unsigned long mode){
	return  glfs_chmod (glfs, path,  mode);

}

unsigned long glfs_java_getmod(glfs_t *glfs, const char *path){
	
	struct stat buf;
	long ret;

	ret = glfs_lstat (glfs, path, &buf);

	if (ret < 0)
		return -1;

	return buf.st_mode;
}

unsigned int glfs_java_getuid(glfs_t *glfs, const char *path){
	
	struct stat buf;
	long ret;

	ret = glfs_lstat (glfs, path, &buf);

	if (ret < 0)
		return -1;

	return buf.st_uid;
}

unsigned int glfs_java_getgid(glfs_t *glfs, const char *path){
	
	struct stat buf;
	long ret;

	ret = glfs_lstat (glfs, path, &buf);

	if (ret < 0)
		return -1;

	return buf.st_gid;
}

int glfs_java_getblocksize(glfs_t *glfs, const char *path){
	
	struct stat buf;
	long ret;

	ret = glfs_lstat (glfs, path, &buf);

	if (ret < 0)
		return -1;

	return buf.st_blksize;
}

long glfs_java_getmtime(glfs_t *glfs, const char *path){
	
	struct stat buf;
	long ret;

	ret = glfs_lstat (glfs, path, &buf);

	if (ret < 0)
		return -1;

	return buf.st_mtime;
}

long glfs_java_getctime(glfs_t *glfs, const char *path){
	
	struct stat buf;
	long ret;

	ret = glfs_lstat (glfs, path, &buf);

	if (ret < 0)
		return -1;

	return buf.st_ctime;
}

long glfs_java_getatime(glfs_t *glfs, const char *path){
	
	struct stat buf;
	long ret;

	ret = glfs_lstat (glfs, path, &buf);

	if (ret < 0)
		return -1;

	return buf.st_atime;
}
char* glfs_java_getxattr(glfs_t *glfs, const char *path, const char *name)
{
	int size = 1024;
	char* buf = malloc(size * sizeof(char));
	
	glfs_lgetxattr (glfs, path, name, buf, size);
	
	return buf;
}

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
glfs_java_seek_set (glfs_fd_t *glfd, off_t location)
{
	return glfs_lseek (glfd, location, SEEK_SET);
}

long
glfs_java_seek_current (glfs_fd_t *glfd, off_t location)
{
	return glfs_lseek (glfd, location, SEEK_CUR);
}

long
glfs_java_seek_end (glfs_fd_t *glfd, off_t location)
{
	return glfs_lseek (glfd, location, SEEK_END);
}

long
glfs_java_read (glfs_fd_t *glfd, b_array *io_data, size_t size)
{
	return glfs_read (glfd, io_data, size, 0);
}

long
glfs_java_write (glfs_fd_t *glfd, b_array *io_data, size_t size)
{
	return glfs_write (glfd, io_data, size, 0);
}

bool
glfs_java_file_createNewFile (glfs_t *glfs, const char *path)
{
	int ret;

	ret = glfs_mknod (glfs, path, S_IFREG | 0644, 0);

	return (ret == 0);
}

long
glfs_java_volume_size(glfs_t *glfs,  const char *path){
        struct statvfs statvfs;
	long ret;
        ret = glfs_statvfs(glfs,path,&statvfs);
        if (ret < 0) {
                return -1;
        }
	return statvfs.f_blocks * statvfs.f_bsize;

}

long
glfs_java_volume_free(glfs_t *glfs,  const char *path){
	struct statvfs statvfs;
	long ret;
        if (ret < 0) {
		return -1;
	}
        ret = glfs_statvfs(glfs,path, &statvfs);
	return statvfs.f_bfree * statvfs.f_bsize;

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
	return glfs_rename (glfs, src, dst)==0;
}

char**
glfs_java_list_dir (glfs_t *fs, const char *path)
{
        char** listing = NULL;
        unsigned int size = 0;
        glfs_fd_t *fd = NULL;
        char buf[512];
        struct dirent *entry = NULL;

        fd = glfs_opendir (fs, path);
        if (!fd) {
            return listing;
        }

        while (glfs_readdir_r (fd, (struct dirent *)buf, &entry), entry) {
         /* skip over . and .. entries */
	 if(strcmp(entry->d_name,".")==0 || strcmp(entry->d_name,"..")==0) continue;

         // one extra for a NULL
	 listing=realloc(listing, sizeof(char *) * (++size+1));
         listing[size-1]= (char*) malloc((strlen(entry->d_name) + 1) * sizeof(char));
         strcpy (listing[size-1], entry->d_name);
         listing[size] = (char*)NULL;
        }

        glfs_closedir (fd);
        return listing;
}
