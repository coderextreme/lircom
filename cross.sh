#!/bin/bash

source env.sh

${JAVA} "${JAVA_OPTS[@]}" lircom.Peer ${PORTS} | xargs -L ${NUM_PORTS} ${JAVA} "${JAVA_OPTS[@]}" solitaire.Game dealer | xargs -L 1 ${JAVA} "${JAVA_OPTS[@]}" impact.Impact3D
