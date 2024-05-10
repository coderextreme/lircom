#!/bin/bash

. env.sh

${JAVA_OPTS} lircom.MainWindow lircom.Chat Christoph &
${JAVA_OPTS} lircom.MainWindow lircom.Chat john &
