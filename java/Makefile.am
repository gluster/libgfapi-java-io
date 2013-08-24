pkgdata_DATA = glusterfsio.jar

EXTRA_DIST = \
	GlusterFileSystem.java \
	GlusterFile.java \
	GlusterFileInputStream.java \
	GlusterFileOutputStream.java

glfs_java.java:
	make -C ../src glfs_java_wrap.c

glfs_javaJNI.java:
	make -C ../src glfs_java_wrap.c

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

CLEANFILES = glfs_java.java glfs_javaJNI.java *.class glusterfsio.jar

clean-local: javaclean

javaclean:
	rm -rf glusterfsio