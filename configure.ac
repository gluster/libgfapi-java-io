
AC_INIT([libgfapi-java-io],[0.1],[gluster-devel@nongu.org])

AM_INIT_AUTOMAKE

AC_CONFIG_FILES([Makefile
		src/Makefile
		java/Makefile])

AC_PROG_CC
AC_PROG_LIBTOOL

PKG_CHECK_MODULES([GLFS], [glusterfs-api >= 3])

## swig section
AC_CHECK_PROG(HAVE_SWIG, swig, "yes", "no")
## end swig section

## Java support
AC_ARG_WITH(java, AS_HELP_STRING([--without-java], [Disable Java]))
AC_ARG_WITH(javac,
	AS_HELP_STRING([--with-javac=PATH],[Set location of javac executable])
	[JAVACBIN="$withval"],
	[JAVACBIN=])

BUILD_JAVA=no
# First, check for "--without-java" or "--with-java=no".
if test x"${JAVABIN}" = xno -o x"${JAVACBIN}" = xno; then
   AC_MSG_NOTICE([Disabling Java])
   JAVA=
else
    if test -z "$JAVACBIN"; then
       AC_PATH_PROGS(JAVAC, javac)
    else
	if ! test -x "$JAVACBIN"; then
	   AC_MSG_WARN([Executable not found: $JAVACBIN])
	   exit 1;
	fi
	JAVAC="$JAVACBIN"
    fi

    REAL_JAVAC=$(readlink -f "$JAVAC")

    if test -x "$REAL_JAVAC"; then
       AC_ARG_WITH(javaincl,AS_HELP_STRING([--with-javaincl=PATH],
					[Set location of Java include directory]),
					[JAVAINCDIR="$withval"], [JAVAINCDIR=])
	if test -z "$JAVAINCDIR"; then
	   JAVAINCDIR=$(dirname $(find $(dirname $REAL_JAVAC)/../include -name jni.h))
	fi
	JNIMDDIR=$(dirname $(find $JAVAINCDIR -name jni_md.h))
	JAVA_CPPFLAGS="-I$JAVAINCDIR -I$JNIMDDIR";
	orig_CPPFLAGS=$CPPFLAGS
	CPPFLAGS="$JAVA_CPPFLAGS $CPPFLAGS"
	AC_CHECK_HEADERS([jni.h],[BUILD_JAVA=yes])
	CPPFLAGS="$orig_CPPFLAGS"
    fi
fi

AC_SUBST(HAVE_SWIG)
AC_SUBST(JAVA_CPPFLAGS)

AC_OUTPUT
