#!/bin/zsh

pushd ../../..
source env.sh
popd
javac --release 9 {solitaire,impactVL,lircom}/*java `find f00f -name '*java'|grep -v test|grep -v Test` 2>&1
