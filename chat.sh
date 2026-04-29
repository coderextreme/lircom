#!/bin/zsh

source env.sh
${JAVA} "${JAVA_OPTS[@]}" -jar target/lircom-3-jar-with-dependencies.jar lircom.Chat
