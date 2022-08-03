JAVAC=javac
JAVA=java
JAR=jar
KEYSTORE=../../../../mykeystore
M2=../../../../m2
# CLASSPATH="$(M2)/xmlParserAPIs-2.6.1.jar:$(M2)/nekohtml-1.9.22.jar:..:../../../../target/lircom-2.jar:$(M2)/httpunit.jar:$(M2)/martyr.jar:$(M2)/servlet-api-2.4.jar:$(M2)/js-1.6R5.jar"
CLASSPATH="$(M2)/xmlParserAPIs-2.6.1.jar;$(M2)/nekohtml-1.9.22.jar;..;../../../../target/lircom-2.jar;$(M2)/httpunit.jar;$(M2)/martyr.jar;$(M2)/servlet-api-2.4.jar;$(M2)/js-1.6R5.jar"

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
