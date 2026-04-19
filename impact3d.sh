#!/bin/bash

. env.sh

${JAVA_OPTS} lircom.Peer ${PORT_NUM} &
echo $!
sleep 1
${JAVA_OPTS} impact.Impact3D localhost:${PORT_NUM} &
${JAVA_OPTS} impact.Impact3D localhost:${PORT_NUM} &
