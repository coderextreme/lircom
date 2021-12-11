JAVAC=/usr/bin/javac
JAVA=/usr/bin/java
JAR=/usr/bin/jar

# For Mac
# CLASSPATH=../../../../m2/jogl-all-natives-macosx-universal.jar:..:.:../../../../m2/jogl-all.jar:..:.:../../../../m2/gluegen-rt.jar:../../../../m2/gluegen-rt-natives-macosx-universal.jar

# For Windows
# CLASSPATH=../../../../m2/jogl-all-natives-linux-amd64.jar;..;.;../../../../m2/jogl-all.jar;../../../../m2/gluegen-rt.jar;../../../../m2/gluegen-rt-natives-linux-amd64.jar

# For Linux
CLASSPATH=../../../../m2/jogl-all-natives-linux-amd64.jar:..:.:../../../../m2/jogl-all.jar:../../../../m2/gluegen-rt.jar:../../../../m2/gluegen-rt-natives-linux-amd64.jar


all:  impact3d.jar
	(cd ../../../..; ./impact3d.sh)

smooth:
	$(JAVAC) -classpath $(CLASSPATH) smooth.java
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) impact.smooth


compile:
	$(JAVAC) -classpath $(CLASSPATH) *.java

impact3d.jar : compile
	($(JAR) -cmf ../../../../impact3d.manifest impact3d.jar *.class *.java)

picking:
	$(JAVAC) -classpath $(CLASSPATH) Picking.java
	$(JAVA) -classpath $(CLASSPATH) impact.Picking
