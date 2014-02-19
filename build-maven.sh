#!/bin/sh
# build 
rm -rf target
./autogen.sh; ./configure ; make
mkdir -p ./src/main/resources 
cp -f ./src/.libs/libgfapi-java-io.so  ./src/main/resources

# copy the shared library into the resources directory so maven includes it in the jar
export GLUSTER_JAVA_LIB=`pwd`/src/main/resources/libgfapi-java-io.so
mvn package -DskipTests=true
# this is to start maven in debugger mode
# mvn package -Dmaven.surefire.debug
