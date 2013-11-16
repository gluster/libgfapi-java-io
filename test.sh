#!/bin/sh
#
# This is script is mostly for dev time test.  It links the native library through an environment variable.
# this could be conditionally set.  it may be set higher up as part of the global env variables.
#
rm -rf target/
export GLUSTER_JAVA_LIB=`pwd`/src/main/resources/libgfapi.so
mvn package -Dmaven.surefire.debug 
# mvn package
