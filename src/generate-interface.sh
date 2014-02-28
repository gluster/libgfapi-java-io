#!/bin/sh

inp=$1;
mod=$2;
out=$3;

if test $# != 3; then
    echo "Usage: $0 <input.h> <modname> <output.i>"
    exit 1
fi

hdr=`basename $inp`;

cat > $out <<EOF
%module $mod
EOF

if [ $mod = glfs_java ]; then
    cat >> $out <<EOF
    
/* direct buffering additions */
%include "buffers.i" 

%apply void* BUFF   {b_array *}
    

/* This allows a C function to return a char ** as a Java String array */
%typemap(out) char** {
    if(!result) return NULL;
    
    int i;

    int len=-1;
    while(result[++len] !=NULL);
   
    jstring temp_string;
    const jclass clazz = (*jenv)->FindClass(jenv, "java/lang/String");

    jresult = (*jenv)->NewObjectArray(jenv, len, clazz, NULL);
    /* exception checking omitted */

    for (i=0; i<len; i++) {
      temp_string = (*jenv)->NewStringUTF(jenv, result[i]);
      (*jenv)->SetObjectArrayElement(jenv, jresult, i, temp_string);
      (*jenv)->DeleteLocalRef(jenv, temp_string);
      free(result[i]);
    }

   free(result);
}

/* These 3 typemaps tell SWIG what JNI and Java types to use */
%typemap(jni) char** "jobjectArray"
%typemap(jtype) char** "String[]"
%typemap(jstype) char** "String[]"

/* These 2 typemaps handle the conversion of the jtype to jstype typemap type and vice versa */
%typemap(javain) char** "$javainput"
%typemap(javaout) char** {
    return \$jnicall;
}
%typemap(jni) signed char[ANY], signed char[] %{ jbyteArray %}
%typemap(jtype) signed char[ANY], signed char[] %{ byte[] %}
%typemap(jstype) signed char[ANY], signed char[] %{ byte[] %}

/* replaced with native/direct buffer provided by buffer.i*/
/*
%typemap(in) void *
%{ \$1 = (void *) (*jenv)->GetByteArrayElements (jenv, \$input, 0);
   if (!\$1) return \$null; %}

%typemap(argout) void *, signed char[ANY], signed char[]
%{ (*jenv)->ReleaseByteArrayElements (jenv, \$input, arg\$argnum, 0); %}
*/



//%typemap(argout) signed char[ANY], signed char[] %{ %}
//%typemap(out) signed char[ANY], signed char[] %{ %}
%typemap(freearg) signed char[ANY], signed char[] %{ %}

%typemap(javain) signed char[ANY], signed char[] "\$javainput"
%typemap(javaout) signed char[ANY], signed char[] {
    return \$jnicall;
}

%typecheck(SWIG_TYPECHECK_INT8_ARRAY) /* Java byte[] */
    signed char[ANY], signed char[]
    ""

%apply signed char[] {void *}
EOF

fi

cat >> $out <<EOF

%apply unsigned long { off_t }
%apply unsigned int { size_t }
%apply unsigned int { ssize_t }
%apply unsigned long { glfs_t * }
%apply unsigned long { glfs_fd_t * }

%{
#include "$hdr"
%}
$(cat $inp)
EOF
