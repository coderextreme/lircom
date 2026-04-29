#!/bin/zsh

source env.sh

${JAVA} "${JAVA_OPTS[@]}" lircom.MainWindow lircom.Kanal &
