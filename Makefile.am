
lib_LTLIBRARIES = libgfapi-java-io.la
noinst_HEADERS = glfs-java.h

glfs_java.i: glfs-java.h
	$(srcdir)/generate-interface.sh $(srcdir)/glfs-java.h glfs_java glfs_java.i

glfs_java_wrap.c: glfs_java.i
	swig -java -o glfs_java_wrap.c -outdir . -module glfs_java -package glusterfsio glfs_java.i

libgfapi_java_io_la_SOURCES = glfs-java.c
nodist_libgfapi_java_io_la_SOURCES = glfs_java_wrap.c

libgfapi_java_io_la_CFLAGS = $(GLFS_CFLAGS) -Wall $(JAVA_CPPFLAGS)
libgfapi_java_io_la_LIBADD = $(GLFS_LIBS)

EXTRA_DIST = generate-interface.sh \
	GlusterFileSystem.java \
	GlusterFile.java \
	GlusterFileInputStream.java \
	GlusterFileOutputStream.java

pkgdata_DATA = glusterfsio.jar

glfs_java.java: glfs_java_wrap.c

glfs_javaJNI.java: glfs_java_wrap.c

%.class: %.java
	javac $<

glusterfsio/%.java: %.java
	mkdir -p glusterfsio/
	cp $^ glusterfsio/

glusterfsio/glfs_java.class: glusterfsio/glfs_javaJNI.class

glusterfsio/GlusterFileSystem.class: glusterfsio/glfs_java.class

glusterfsio/GlusterFile.class: glusterfsio/GlusterFileSystem.class

glusterfsio/GlusterFileInputStream.class: glusterfsio/GlusterFile.class

glusterfsio/GlusterFileOutputStream.class: glusterfsio/GlusterFile.class

glusterfsio.jar: glusterfsio/glfs_java.class glusterfsio/glfs_javaJNI.class glusterfsio/GlusterFileSystem.class glusterfsio/GlusterFile.class glusterfsio/GlusterFileInputStream.class glusterfsio/GlusterFileOutputStream.class
	jar -cf $@ glusterfsio;

CLEANFILES = glfs_java.i glfs_java_wrap.c glfs_java.java glfs_javaJNI.java *.class glusterfsio.jar

clean-local: javaclean

javaclean:
	rm -rf glusterfsio