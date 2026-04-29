#!/bin/zsh

set -x

source env.sh

${JAVA} "${JAVA_OPTS[@]}" lircom.Peer ${PORT_NUM} &
sleep 1
${JAVA} "${JAVA_OPTS[@]}" -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat Michael &
${JAVA} "${JAVA_OPTS[@]}" -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat John &
${JAVA} "${JAVA_OPTS[@]}" -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat Julie &
${JAVA} "${JAVA_OPTS[@]}" -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat Kathy &
