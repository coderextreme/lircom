#!/bin/bash

. env.sh

${JAVA_OPTS} lircom.Peer ${PORTS} | xargs -L ${NUM_PORTS} ${JAVA_OPTS} lircom.MainWindow lircom.Chat Christoph | xargs -L ${NUM_PORTS} ${JAVA_OPTS} lircom.MainWindow lircom.Chat john
