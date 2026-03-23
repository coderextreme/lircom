#!/bin/bash -x

. env.sh

${JAVA_OPTS} lircom.Peer ${PORTS} | \
	xargs -L ${NUM_PORTS} ${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat Michael | \
	xargs -L ${NUM_PORTS} ${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat John
