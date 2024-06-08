#!/bin/bash

. env.sh

${JAVA_OPTS} lircom.Peer 8180 &
echo $!
sleep 1
${JAVA_OPTS} impact.Impact3D localhost:8180 &
${JAVA_OPTS} impact.Impact3D localhost:8180 &
