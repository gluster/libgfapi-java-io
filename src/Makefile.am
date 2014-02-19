
lib_LTLIBRARIES = libgfapi-java-io.la
noinst_HEADERS = glfs-java.h

glfs_java.i: glfs-java.h
	$(srcdir)/generate-interface.sh $(srcdir)/glfs-java.h glfs_java glfs_java.i

glfs_java_wrap.c: glfs_java.i
	mkdir -p $(srcdir)/main/java/glusterfsio
	swig -java -o glfs_java_wrap.c -outdir $(srcdir)/main/java/glusterfsio -module glfs_java -package glusterfsio glfs_java.i

libgfapi_java_io_la_SOURCES = glfs-java.c glfs_java_wrap.c

libgfapi_java_io_la_CFLAGS = $(GLFS_CFLAGS) -Wall $(JAVA_CPPFLAGS)
libgfapi_java_io_la_LIBADD = $(GLFS_LIBS)

EXTRA_DIST = generate-interface.sh

CLEANFILES = glfs_java.i glfs_java_wrap.c
