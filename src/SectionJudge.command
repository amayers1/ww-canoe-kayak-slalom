#!/bin/sh
cd "`dirname "$0"`"
#java -cp SectionJudge.jar com.tcay.slalom.UI.client.SectionJudgeClient
java -cp SlalomApp.jar com.tcay.slalom.UI.client.SectionJudgeClient


& disown %1
exit

