#!/bin/zsh

source env.sh
${JAVA} "${JAVA_OPTS[@]}" lircom.IRCClient config/afternet.config config/symbion.config
