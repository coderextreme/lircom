KEYSTORE=/home/carlsonj/mykeystore

random: ../solitaire.jar
	java -Xmx256m -classpath "..:../solitaire.jar:../lircom.jar" solitaire.Game random localhost 5891

methodical: ../solitaire.jar
	java -Xmx256m -classpath "..:../solitaire.jar:../lircom.jar" solitaire.Game methodical localhost 5891

display: ../solitaire.jar
	java -Xmx256m -classpath "..:../solitaire.jar:../lircom.jar" solitaire.Game display localhost 5891

dealer: ../solitaire.jar
	java -Xmx256m -classpath "..:../solitaire.jar:../lircom.jar" solitaire.Game dealer localhost 5891

../solitaire.jar: *.java
	javac -classpath "..:../lircom.jar" -g *java
	(cd ..;  jar -cmf solitaire/solitaire.manifest solitaire.jar solitaire; jarsigner -keystore $(KEYSTORE) solitaire.jar myself)

clean:
	rm *.class
