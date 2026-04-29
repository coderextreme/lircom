#!/bin/zsh

source env.sh

${JAVA} "${JAVA_OPTS[@]}" lircom.Peer ${PORT_NUM} &
sleep 1
${JAVA} "${JAVA_OPTS[@]}" lircom.MainWindow lircom.Channel Discussion_Channel
