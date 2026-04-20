#!/bin/bash -x

. env.sh

${JAVA_OPTS} lircom.Peer ${PORT_NUM} &
echo $!
sleep 1
