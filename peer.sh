#!/bin/zsh

source env.sh

${JAVA} "${JAVA_OPTS[@]}" lircom.Peer ${PORT_NUM} &
echo $!
sleep 1
