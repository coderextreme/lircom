#!/bin/bash

. env.sh

export CLASSPATH="${CLASSPATH}:src/main/resources"

${JAVA_OPTS} net.coderextreme.impactVL.Impact
${JAVA_OPTS} net.coderextreme.impactVL.Cell 4x4
${JAVA_OPTS} net.coderextreme.impactVL.Cell
