#!/bin/bash

. env.sh

${JAVA_OPTS} lircom.Peer ${PORT_NUM} &
sleep 1
${JAVA_OPTS} lircom.MainWindow lircom.Channel Discussion_Channel
