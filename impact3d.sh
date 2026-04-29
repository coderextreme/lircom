#!/bin/zsh

source env.sh

${JAVA} "${JAVA_OPTS[@]}" lircom.Peer ${PORT_NUM} &
echo $!
sleep 1
${JAVA} "${JAVA_OPTS[@]}" "${APPLE_JOGL_OPTS[@]}" impact.Impact3D localhost:${PORT_NUM} &
${JAVA} "${JAVA_OPTS[@]}" "${APPLE_JOGL_OPTS[@]}" impact.Impact3D localhost:${PORT_NUM} &
