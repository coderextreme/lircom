set JAVAC=javac
set JAVA=java
set JAR=jar
set KEYSTORE=mykeystore
set M2=m2
set CLASSPATH="%M2%/xmlParserAPIs-2.6.1.jar;%M2%/nekohtml-0.9.5.jar;target/lircom-2.jar;%M2%/httpunit.jar;%M2%/martyr.jar;%M2%/servlet-api-2.4.jar;%M2%/js-1.6R5.jar;.."

%JAVA% -Xmx256m -classpath %CLASSPATH% solitaire.Game dealer localhost 8180
