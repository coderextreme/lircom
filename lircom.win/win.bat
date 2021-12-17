del lircom*.*
set HOME=%HOMEDRIVE%\%HOMEPATH%
%HOME%\bin\wget64.exe https://coderextreme.net/lircom.zip
jar -xf lircom.zip
set CLASSPATH=%HOME%\lircom\target\lircom-2.jar;%HOME%\lircom\m2\httpunit-1.7.3.jar;%HOME%\lircom\m2\log4j-2.16.0.jar;%HOME%\lircom\m2\nekohtml-1.9.22.jar;%HOME%\lircom\m2\xercesImpl-2.11.0.jar;%HOME%\lircom\m2\rhino-1.7R4.jar;%HOME%\lircom\m2\json-20190722.jar

java -Xmx256m -classpath %CLASSPATH% lircom.MainWindow lircom.Chat
