#!/bin/bash

. env.sh

keytool -genkeypair -keyalg DSA -keysize 1024 -keystore ${KEYSTORE} -alias myself -storepass foobar 

${JAVA_OPTS} lircom.Peer ${PORTS} | xargs -L ${NUM_PORTS} ${JAVA_OPTS} lircom.MainWindow lircom.Chat Christoph | xargs -L ${NUM_PORTS} ${JAVA_OPTS} lircom.MainWindow lircom.Chat john | xargs -P 0 -L ${NUM_PORTS} ${JAVA_OPTS} solitaire.Game dealer &

${JAVA_OPTS} impact.Impact3D &
${JAVA_OPTS} impact.Impact3D &
