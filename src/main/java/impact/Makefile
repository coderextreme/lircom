JAVAC=/usr/bin/javac
JAVA=/usr/bin/java
JAR=/usr/bin/jar
# CLASSPATH=/Users/johncarlson/Downloads/jogamp-all/jogamp-all-platforms/jar/jogl-all-natives-macosx-universal.jar:/Users/johncarlson/Downloads/jogamp-all/jogamp-all-platforms/jar/jogl-all.jar:..:.:/Users/johncarlson/Downloads/jogamp-all/jogamp-all-platforms/jar/gluegen-rt.jar
# CLASSPATH=../../../../m2/jogl-all-natives-linux-amd64.jar:../../../../m2/jogl-all.jar:../../../../m2/gluegen-rt.jar

CLASSPATH=../../../../m2/joal-natives-linux-amd64.jar:../../../../m2/jocl-natives-linux-amd64.jar:../../../../m2/jogl-all-natives-linux-amd64.jar:../../../../m2/jogl-all-noawt-natives-linux-amd64.jar:../../../../m2/jogl-cg-natives-linux-amd64.jar:../../../../m2/jogl-natives-linux-amd64.jar:../../../../m2/nativewindow-natives-linux-amd64.jar:../../../../m2/newt-natives-linux-amd64.jar:../../../../m2/oculusvr-natives-linux-amd64.jar:..:.:../../../../m2/jogl-all.jar:../../../../m2/gluegen-rt.jar:../../../../m2/gluegen-rt-natives-linux-amd64.jar:../../../../m2/httpunit-1.7.3.jar


all:  impact3d.jar
	(cd ../../../..; ./impact3d.sh)

smooth:
	$(JAVAC) -classpath $(CLASSPATH) smooth.java
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) smooth


compile:
	$(JAVAC) -classpath $(CLASSPATH) *.java

impact3d.jar : compile
	($(JAR) -cmf ../../../../impact3d.manifest impact3d.jar *.class *.java)

picking:
	$(JAVAC) -classpath $(CLASSPATH) Picking.java
	$(JAVA) -classpath $(CLASSPATH) impact.Picking
