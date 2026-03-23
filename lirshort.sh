#!/bin/bash -x

. env.sh

${JAVA_OPTS} lircom.Peer 8180 &
sleep 1
${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat Michael &
${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat John &
