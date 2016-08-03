#!/bin/sh
cd "`dirname "$0"`"
#java -cp SectionJudge.jar com.tcay.slalom.UI.client.SectionJudgeClientSectionSelector
java -cp SlalomApp.jar com.tcay.slalom.UI.client.SectionJudgeClientSectionSelector


& disown %1
exit

