#!/bin/bash

. env.sh
${JAVA_OPTS} lircom.IRCClient config/afternet.config config/symbion.config
