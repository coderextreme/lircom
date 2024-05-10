#!/bin/bash -x

. env.sh

${JAVA_OPTS} lircom.Peer 8180 &
echo $!
