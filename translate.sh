#!/bin/zsh
source venv/Scripts/activate || source venv/bin/activate
python3 translation_server.py &
source lirshort.sh
