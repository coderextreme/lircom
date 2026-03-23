#!/bin/bash

. env.sh


${JAVA_OPTS} lircom.Peer ${PORTS} | \
	xargs -L ${NUM_PORTS} ${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat Shibu | \
	xargs -L ${NUM_PORTS} ${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat John | \
	xargs -L ${NUM_PORTS} ${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat Michael | \
	xargs -L ${NUM_PORTS} ${JAVA_OPTS} -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat Jason
