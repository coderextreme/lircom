#!/bin/bash -x
cd lircom
make peer &
sleep 1
cd ..
java -Xmx256m -classpath /Users/johncarlson/Downloads/jogamp-all/jogamp-all-platforms/jar/jogl-all-natives-macosx-universal.jar:/Users/johncarlson/Downloads/jogamp-all/jogamp-all-platforms/jar/jogl-all.jar:impact3d.jar:.:/Users/johncarlson/Downloads/jogamp-all/jogamp-all-platforms/jar/gluegen-rt.jar impact.Impact3D

