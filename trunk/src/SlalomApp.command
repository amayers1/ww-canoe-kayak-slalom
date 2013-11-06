#!/bin/sh
cd "`dirname "$0"`"
java -jar SlalomScoring.jar >> SlalomApp.log

#& disown %1
#exit


