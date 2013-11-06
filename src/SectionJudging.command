#!/bin/sh
cd "`dirname "$0"`"
java -cp SlalomScoring.jar com.tcay.slalom.UI.client.SectionJudgeClient
