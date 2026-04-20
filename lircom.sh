#!/bin/bash -x

. env.sh

${JAVA_OPTS} lircom.Peer ${PORT_NUM} &
sleep 1
${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat Michael &
${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat John &
${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat Julie &
${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat Kathy &
