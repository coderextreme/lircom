JAVAC=/usr/bin/javac
JAVA=/usr/bin/java
JAR=/usr/bin/jar
CLASSPATH=/Users/johncarlson/Downloads/jogamp-all/jogamp-all-platforms/jar/jogl-all-natives-macosx-universal.jar:/Users/johncarlson/Downloads/jogamp-all/jogamp-all-platforms/jar/jogl-all.jar:..:.:/Users/johncarlson/Downloads/jogamp-all/jogamp-all-platforms/jar/gluegen-rt.jar


all:  ../impact3d.jar
	(cd ..; ./impact3d.sh)

smooth:
	$(JAVAC) -classpath $(CLASSPATH) smooth.java
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) smooth


compile:
	$(JAVAC) -classpath $(CLASSPATH) *.java

../impact3d.jar : compile
	(cd ..; $(JAR) -cmf impact3d.manifest impact3d.jar impact/*.class impact/*.java)

picking:
	$(JAVAC) -classpath $(CLASSPATH) Picking.java
	$(JAVA) -classpath $(CLASSPATH) impact.Picking
