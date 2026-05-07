#!/bin/zsh

# TODO broken no class

source env.sh
export DIR=`pwd`
export JAVAC=javac
export JAVA=java
if [[ "$OSTYPE" == "darwin"* ]]; then
	export USER_HOME=/Users/marycarlson
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
	export USER_HOME=/home/yottzumm
elif [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
	export USER_HOME=C:/Users/jcarl
fi
export M2=${USER_HOME}/.m2/repository
export DWN=${USER_HOME}//Downloads
export CLASSPATH="${DIR}/target/Impact.jar:${DIR}/target/lircom-3.jar:${M2}/org/httpunit/httpunit/1.7.3/httpunit-1.7.3.jar:${M2}/org/htmlunit/neko-htmlunit/4.21.0/neko-htmlunit-4.21.0.jar:${M2}/com/squareup/okhttp3/okhttp/4.9.3/okhttp-4.9.3.jar:${M2}/org/mozilla/rhino/1.9.1/rhino-1.9.1.jar:${M2}/com/fasterxml/jackson/core/jackson-annotations/2.18.6/jackson-annotations-2.18.6.jar:${M2}/com/fasterxml/jackson/core/jackson-core/2.18.6/jackson-core-2.18.6.jar:${M2}/com/fasterxml/jackson/core/jackson-databind/2.18.6/jackson-databind-2.18.6.jar:${M2}/com/fasterxml/jackson/jr/jackson-jr-objects/2.18.6/jackson-jr-objects-2.18.6.jar:${M2}/com/fasterxml/classmate/1.7.0/classmate-1.7.0.jar"

export AGENT_JAR="${USER_HOME}/lircom/patch-agent/target/patch-agent-1.0.jar"
export LIB_PATH=${DIR}/natives/macosx-universal

export JAVA_OPTS=(
	"-javaagent:${AGENT_JAR}"
	"-Djava.library.path=${LIB_PATH}"
	"--add-exports"
	"java.base/java.lang=ALL-UNNAMED"
	"--add-exports"
	"java.desktop/sun.awt=ALL-UNNAMED"
	"--add-exports"
	"java.desktop/sun.java2d=ALL-UNNAMED"
	"--enable-final-field-mutation=ALL-UNNAMED"
	"--enable-native-access=ALL-UNNAMED"
)

if [[ "$OSTYPE" == "darwin"* ]]; then
    echo "Running on macOS"
    export PLATFORM_JOGL_OPTS=(
	"-Dapple.awt.UIElement=false"
	"-XstartOnFirstThread"
    )
elif [[ "$OSTYPE" == "linux-gnu"* ]]; then
    echo "Running on Linux"
    export PLATFORM_JOGL_OPTS=(
    )
elif [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
    echo "Running on Windows"
    export PLATFORM_JOGL_OPTS=(
    )
fi

set -x
 export CLASSPATH=./jar/gluegen-rt.jar:./jar/jogl-all.jar:.:./jar/gluegen-rt-natives-macosx-universal.jar:./jar/gluegen-rt-android-natives-macosx-universal.jar:./jar/jogl-all-natives-macosx-universal.jar:./natives/macosx-universal/libnativewindow_macosx.jnilib:${CLASSPATH}
${JAVAC} Main.java
${JAVA} "${JAVA_OPTS[@]}" "${PLATFORM_JOGL_OPTS[@]}" Main

