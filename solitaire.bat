set JAVAC=javac
set JAVA=java
set JAR=jar
set KEYSTORE=mykeystore
set M2=m2
set CLASSPATH="target/lircom-3.jar"

%JAVA% -Xmx256m -classpath %CLASSPATH% solitaire.Game dealer localhost 8180
