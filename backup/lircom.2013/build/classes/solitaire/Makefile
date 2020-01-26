KEYSTORE=/Users/johncarlson/mykeystore

random: ../solitaire.jar
	java -Xmx256m -classpath "..:../solitaire.jar:../lircom.jar" solitaire.Game random coderextreme.net 8180

methodical: ../solitaire.jar
	java -Xmx256m -classpath "..:../solitaire.jar:../lircom.jar" solitaire.Game methodical coderextreme.net 8180

display: ../solitaire.jar
	java -Xmx256m -classpath "..:../solitaire.jar:../lircom.jar" solitaire.Game display coderextreme.net 8180

dealer: ../solitaire.jar
	java -Xmx256m -classpath "..:../solitaire.jar:../lircom.jar" solitaire.Game dealer coderextreme.net 8180

../solitaire.jar: *.java
	javac -target 1.5 -classpath "..:../lircom.jar" -g *java
	(cd ..;  jar -cmf solitaire/solitaire.manifest solitaire.jar solitaire; jarsigner -keystore $(KEYSTORE) solitaire.jar myself)

clean:
	rm *.class
