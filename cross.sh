#!/bin/bash

. env.sh

${JAVA_OPTS} lircom.Peer ${PORTS} | xargs -L ${NUM_PORTS} ${JAVA_OPTS} solitaire.Game dealer | xargs -L 1 ${JAVA_OPTS} impact.Impact3D
