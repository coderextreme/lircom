#!/bin/zsh

source env.sh

${JAVA} "${JAVA_OPTS[@]}" lircom.Peer ${PORT_NUM} &
sleep 1

${JAVA} "${JAVA_OPTS[@]}" solitaire.Game random localhost:${PORT_NUM} &
#${JAVA} "${JAVA_OPTS[@]}" solitaire.Game methodical localhost:${PORT_NUM} 
${JAVA} "${JAVA_OPTS[@]}" solitaire.Game display localhost:${PORT_NUM} &
${JAVA} "${JAVA_OPTS[@]}" solitaire.Game dealer localhost:${PORT_NUM} &
