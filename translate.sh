#!/bin/zsh
source venv/Scripts/activate || source venv/bin/activate
python translation_server.py &
source lirshort.sh
