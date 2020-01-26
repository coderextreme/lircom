JAVAC="/c/Program Files/Java/jdk1.7.0_02/bin/javac"
JAVA="/c/Program Files/Java/jdk1.7.0_02/bin/java"
JAR="/c/Program Files/Java/jdk1.7.0_02/bin/jar"
CLASSPATH="..;.;../../../../jogamp/jogamp-windows-amd64/jar/jogl.all.jar;../../../../jogamp/jogamp-windows-amd64/jar/gluegen-rt.jar"


all:  ../impact3d.jar
	(cd ..; ./impact3d.sh)

smooth:
	$(JAVAC) -classpath $(CLASSPATH) smooth.java
	(export JAVA_LIB_PATH=../../jogamp/jogamp-windows-amd64/lib/; $(JAVA) -Xmx256m -Djava.library.path=${JAVA_LIB_PATH} -classpath $(CLASSPATH) smooth)


compile:
	$(JAVAC) -classpath $(CLASSPATH) *.java

../impact3d.jar : compile
	(cd ..; $(JAR) -cmf impact3d.manifest impact3d.jar impact/*.class impact/*.java)

picking:
	$(JAVAC) -classpath $(CLASSPATH) Picking.java
	(export LD_LIBRARY_PATH=../obj; $(JAVA) -classpath $(CLASSPATH) impact.Picking) 

clean:
	rm *.class
