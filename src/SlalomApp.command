#!/bin/sh
cd "`dirname "$0"`"
java -jar SlalomApp.jar >> SlalomApp.log

& disown %1
exit


