#!/bin/sh
# build 
rm -rf target
# use this to try the regular make, going to build again with different linking.
# make configure ; make

# gcc -c -Wall -fpic ./src/glfs-java.c ./src/glfs_java_wrap.c -I/usr/include/glusterfs/  -I/usr/java/jdk1.7.0_40/include/linux -I/usr/java/jdk1.7.0_40/include
# ld -shared glfs-java.o glfs_java_wrap.o -lgfapi -o libgfapi.so
# rm glfs-java.o glfs_java_wrap.o
./configure ; make
cp -f ./src/.libs/libgfapi-java-io.so  ./src/main/resources
# cp -f libgfapi.so  ./src/main/resources

# copy the shared library into the resources directory so maven includes it in the jar
export GLUSTER_JAVA_LIB=`pwd`/src/main/resources/libgfapi-java-io.so
mvn package
# mvn package -Dmaven.surefire.debug
