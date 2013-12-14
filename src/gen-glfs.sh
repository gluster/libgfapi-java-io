./generate-interface.sh ./glfs-java.h glfs_java glfs_java.i
swig -java -o glfs_java_wrap.c -outdir ./main/java/glusterfsio -module glfs_java -package glusterfsio glfs_java.i
