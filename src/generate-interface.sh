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
%typemap(jni) signed char[ANY], signed char[] %{ jbyteArray %}
%typemap(jtype) signed char[ANY], signed char[] %{ byte[] %}
%typemap(jstype) signed char[ANY], signed char[] %{ byte[] %}

%typemap(in) void *
%{ \$1 = (void *) (*jenv)->GetByteArrayElements (jenv, \$input, 0);
   if (!\$1) return \$null; %}

%typemap(argout) void *, signed char[ANY], signed char[]
%{ (*jenv)->ReleaseByteArrayElements (jenv, \$input, arg\$argnum, 0); %}

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