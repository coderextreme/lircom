set DIR="C:/Users/jcarl/lircom"
set M2="C:/Users/jcarl/.m2/repository"
set CLASSPATH="%DIR%/target/Impact.jar;%DIR%/target/lircom-3.jar;%DIR%/target/lircom-3-jar-with-dependencies.jar;%M2%/org/httpunit/httpunit/1.7.3/httpunit-1.7.3.jar;%M2%/org/htmlunit/neko-htmlunit/4.21.0/neko-htmlunit-4.21.0.jar;%M2%/com/squareup/okhttp3/okhttp/4.9.3/okhttp-4.9.3.jar;%M2%/org/mozilla/rhino/1.9.1/rhino-1.9.1.jar;%M2%/com/fasterxml/jackson/core/jackson-annotations/2.18.6/jackson-annotations-2.18.6.jar;%M2%/com/fasterxml/jackson/core/jackson-core/2.18.6/jackson-core-2.18.6.jar;%M2%/com/fasterxml/jackson/core/jackson-databind/2.18.6/jackson-databind-2.18.6.jar;%M2%/com/fasterxml/jackson/jr/jackson-jr-objects/2.18.6/jackson-jr-objects-2.18.6.jar;%M2%/com/fasterxml/classmate/1.7.0/classmate-1.7.0.jar"

start java -Xmx256m -cp %CLASSPATH% lircom.Peer 8180

start java -Xmx256m -cp %CLASSPATH% lircom.MainWindow lircom.Chat funkytown
start java -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat george
