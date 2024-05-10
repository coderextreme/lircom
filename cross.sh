#!/bin/bash

. env.sh

${JAVA_OPTS} solitaire.Game dealer localhost 8180 &
${JAVA_OPTS} impact.Impact3D &
