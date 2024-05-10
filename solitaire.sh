#!/bin/bash

. env.sh

#${JAVA_OPTS} solitaire.Game display localhost 8180 &
${JAVA_OPTS} solitaire.Game dealer localhost 8180 &
#${JAVA_OPTS} solitaire.Game random localhost 8180 &
#${JAVA_OPTS} solitaire.Game methodical localhost 8180 &
