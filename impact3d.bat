set DIR=C:\Users\john\lircom
set JARS=%DIR%\m2
set DWN=C:\Users\john\Downloads

%DIR%\peer.sh

java -Xmx256m -cp %DIR%\target\lircom-2.jar;%DWN%\gluegen-rt.jar;%DWN%\jogl-all.jar;%DWN%\gluegen-rt-natives-windows-amd64.jar;%DWN%\jogl-all-natives-windows-amd64.jar impact.Impact3D



