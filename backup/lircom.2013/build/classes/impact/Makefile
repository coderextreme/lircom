JAVAC=/usr/bin/javac
JAVA=/usr/bin/java
JAR=/usr/bin/jar
CLASSPATH=..:.:/Users/johncarlson/dev/jogl-1.1.0-macosx-universal/lib/jogl.jar:/Users/johncarlson/dev/jogl-1.1.0-macosx-universal/lib/gluegen-rt.jar


all:  ../impact3d.jar
	(cd ..; ./impact3d.sh)

compile:
	$(JAVAC) -target 1.5 -classpath $(CLASSPATH) *.java

../impact3d.jar : compile
	(cd ..; $(JAR) -cmf impact3d.manifest impact3d.jar impact/*.class impact/*.java)

picking:
	$(JAVAC) -classpath ../jogl.jar:.:.. Picking.java
	(export LD_LIBRARY_PATH=../obj; $(JAVA) -classpath $(CLASSPATH) impact.Picking) 
