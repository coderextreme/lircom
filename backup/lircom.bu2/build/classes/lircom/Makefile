KEYSTORE=E:/cygwin/home/carlsonj/mykeystore

lircom: ../lircom.jar
	java -Xmx256m -classpath "../solitaire.jar;../lircom.jar" lircom.MainWindow lircom.Chat

channel: ../lircom.jar
	java -Xmx256m -classpath "../solitaire.jar;../lircom.jar" lircom.MainWindow lircom.Channel

kanal: ../lircom.jar
	java -Xmx256m -classpath "../solitaire.jar;../lircom.jar" lircom.MainWindow lircom.Kanal

peer: ../lircom.jar
	java -Xmx256m -classpath "../solitaire.jar;../lircom.jar" lircom.Peer 5891

../lircom.jar: *.java
	javac -classpath ../solitaire.jar *.java
	(cd ..; jar -cmf lircom/lircom.manifest lircom.jar lircom; jarsigner -keystore $(KEYSTORE) lircom.jar myself)
