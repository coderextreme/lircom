#!/bin/bash

. env.sh

#${JAVA_OPTS} solitaire.Game random localhost:${PORT_NUM} 
#${JAVA_OPTS} solitaire.Game methodical localhost:${PORT_NUM} 
#${JAVA_OPTS} solitaire.Game display localhost:${PORT_NUM} 
#${JAVA_OPTS} solitaire.Game dealer localhost:${PORT_NUM} 

${JAVA_OPTS} lircom.Peer ${PORT_NUM} &
sleep 1
${JAVA_OPTS} solitaire.Game random localhost:${PORT_NUM} &
${JAVA_OPTS} solitaire.Game display localhost:${PORT_NUM} &
${JAVA_OPTS} solitaire.Game dealer localhost:${PORT_NUM} &
