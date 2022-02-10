set CLASSPATH=..\target\lircom-2.jar;..\m2\httpunit-1.7.3.jar;..\m2\nekohtml-1.9.22.jar;../m2/rhino-1.7R4.jar;../m2/json-20190722.jar


java -Xmx256m -classpath %CLASSPATH% lircom.MainWindow lircom.Chat
