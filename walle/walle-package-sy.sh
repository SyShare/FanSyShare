#!/usr/bin/env bash

##############################################################################
##
##  Walle package script for UN*X
##
##############################################################################

cd "`dirname $0`"
cd ../

./gradlew --stacktrace clean assembleReleaseChannels -PchannelList=hdwawa
