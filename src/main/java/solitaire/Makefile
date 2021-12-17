JAVAC=javac
JAVA=java
JAR=jar
KEYSTORE=../../../../mykeystore
CLASSPATH="../../../../m2/xercesImpl-2.6.1.jar:../../../../m2/xmlParserAPIs-2.6.1.jar:../../../../m2/nekohtml-0.9.5.jar:..:../solitaire.jar:../lircom.jar:../../../../m2/httpunit.jar:../../../../m2/martyr.jar:../../../../m2/log4j-api-2.16.0.jar:../../../../m2/log4j-core-2.16.0.jar:../../../../m2/servlet-api-2.4.jar:../../../../m2/js-1.6R5.jar:.."

random: solitaire.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) solitaire.Game random localhost 8180

methodical: solitaire.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) solitaire.Game methodical localhost 8180

display: solitaire.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) solitaire.Game display localhost 8180

dealer: solitaire.jar
	$(JAVA) -Xmx256m -classpath $(CLASSPATH) solitaire.Game dealer localhost 8180

solitaire.jar: *.java cardset-standard
	$(JAVAC) -target 18 -classpath $(CLASSPATH) -g *.java
	($(JAR) -cmf solitaire.manifest solitaire.jar *.class *.java cardset-standard; jarsigner -keystore $(KEYSTORE) solitaire.jar myself)

clean:
	rm *.class
