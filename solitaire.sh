#!/bin/bash

. env.sh

#${JAVA_OPTS} solitaire.Game display
#${JAVA_OPTS} solitaire.Game random
#${JAVA_OPTS} solitaire.Game methodical

${JAVA_OPTS} lircom.Peer ${PORTS} | xargs -L ${NUM_PORTS} ${JAVA_OPTS} solitaire.Game random &
sleep 1
echo ${PORTS} | awk '{ for(i=1; i<=NF; i++) { print $i; }}' | sed 's/^/localhost:/' | xargs -L ${NUM_PORTS} ${JAVA_OPTS} solitaire.Game dealer &
