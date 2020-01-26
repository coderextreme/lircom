KEYSTORE=E:/cygwin/home/carlsonj/mykeystore

.SUFFIXES: .class .java


.java.class:
	@echo -n "//"
	e:/j2sdk1.4.2_05/bin/javac -g $<

game: all
	e:/j2sdk1.4.2_05/bin/java -Xmx256m -jar solitaire.jar random

miner: all
	e:/j2sdk1.4.2_05/bin/java -Xmx256m GameMiner < log1095643511155.txt

run: all
	@echo -n "/*" > Solitaire.java
	e:/j2sdk1.4.2_05/bin/java -Xmx256m GameMiner < logout.txt >> Solitaire.java
#	e:/j2sdk1.4.2_05/bin/java -Xmx256m GameMiner < log1094519681169.txt

all: VersionSpace.class CardItem.class EventMiner.class Game.class GameMiner.class Log.class Stack.class StackLayout.class
	jar -cmf solitaire.manifest solitaire.jar *.class *.java Makefile solitaire.manifest cardset-standard/*
	jarsigner -keystore $(KEYSTORE) solitaire.jar myself

clean:
	rm *.class

sol: Solitaire.class
