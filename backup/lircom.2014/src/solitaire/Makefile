JAVAC="/c/Program Files/Java/jdk1.7.0_02/bin/javac"
JAVA="/c/Program Files/Java/jdk1.7.0_02/bin/java"
JAR="/c/Program Files/Java/jdk1.7.0_02/bin/jar"
KEYSTORE=../../../mykeystore
CLASSPATH="../../lib/xercesImpl-2.6.1.jar;../../lib/xmlParserAPIs-2.6.1.jar;../../lib/nekohtml-0.9.5.jar;..;../solitaire.jar;../lircom.jar;../../lib/httpunit.jar;../../lib/martyr.jar;../../lib/log4j-1.2.14.jar;../../lib/servlet-api-2.4.jar;../../lib/js-1.6R5.jar"

random: ../solitaire.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) solitaire.Game random localhost 8180

methodical: ../solitaire.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) solitaire.Game methodical localhost 8180

display: ../solitaire.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) solitaire.Game display localhost 8180

dealer: ../solitaire.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) solitaire.Game dealer localhost 8180

../solitaire.jar: *.java
	$(JAVAC) -target 1.7 -classpath $(CLASSPATH) -g *.java
	(cd ..;  $(JAR) -cmf solitaire/solitaire.manifest solitaire.jar solitaire; jarsigner -keystore $(KEYSTORE) solitaire.jar myself)

clean:
	rm *.class
