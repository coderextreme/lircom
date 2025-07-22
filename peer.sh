#!/bin/bash -x

. env.sh

${JAVA_OPTS} lircom.Peer 8180 8181 &
echo $!
sleep 1
