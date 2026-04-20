#!/bin/bash

set -x

. env.sh

keytool -genkeypair -keyalg DSA -keysize 1024 -keystore ${KEYSTORE} -alias myself -storepass foobar 

${JAVA_OPTS} lircom.Peer ${PORT_NUM} &
sleep 1
${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat Michael &
${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat John &
${JAVA_OPTS} solitaire.Game dealer localhost:${PORT_NUM} &
${JAVA_OPTS} solitaire.Game random localhost:${PORT_NUM} &
${JAVA_OPTS} impact.Impact3D localhost:${PORT_NUM} &
${JAVA_OPTS} impact.Impact3D localhost:${PORT_NUM} &
