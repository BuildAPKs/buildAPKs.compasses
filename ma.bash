#!/bin/env bash
# Copyright 2019 (c) all rights reserved 
# by BuildAPKs https://buildapks.github.io
#####################################################################
set -Eeuo pipefail
shopt -s nullglob globstar

_SMATRPERROR_() { # run on script error
	local RV="$?"
	echo "$RV" ma.bash
	printf "\\e[?25h\\n\\e[1;48;5;138mBuildAPKs %s ERROR:  Generated script error %s near or at line number %s by \`%s\`!\\e[0m\\n" "${PWD##*/}" "${1:-UNDEF}" "${2:-LINENO}" "${3:-BASH_COMMAND}"
	exit 147
}

_SMATRPEXIT_() { # run on exit
	printf "\\e[?25h\\e[0m"
	set +Eeuo pipefail 
	exit
}

_SMATRPSIGNAL_() { # run on signal
	local RV="$?"
	printf "\\e[?25h\\e[1;7;38;5;0mBuildAPKs %s WARNING:  Signal %s received!\\e[0m\\n" "ma.bash" "$RV"
 	exit 148 
}

_SMATRPQUIT_() { # run on quit
	local RV="$?"
	printf "\\e[?25h\\e[1;7;38;5;0mBuildAPKs %s WARNING:  Quit signal %s received!\\e[0m\\n" "ma.bash" "$RV"
 	exit 149 
}

trap '_SMATRPERROR_ $? $LINENO $BASH_COMMAND' ERR 
trap _SMATRPEXIT_ EXIT
trap _SMATRPSIGNAL_ HUP INT TERM 
trap _SMATRPQUIT_ QUIT 

cd "$JDR"
# _AT_ arpitjain099/Android-Accelerometer-Compass-GPS-sensors-data-logging 13845848c7fe3cbb542b3bed805c25c23a4dba28
_AT_ farsitel/android_packages_apps_QiblaCompass d30afb971dbac8f49cf0b438572832d56cc32d12
_AT_ iliu/Android-Compass c570a99cb37b9f100de83eac8a1ffc9bac1b9557
_AT_ JosephChenEcho/Android-Compass 88d1bd8e32a118600e3fd229defd87709b600795
_AT_ JosephChenEcho/Compass-V1.0 ee900c23f0af1a8153bcba213beb4e2f444dd165
_AT_ ldo/3D-Compass 3583e0490cd9c8a6a42e4507451e220c359b8405
# _AT_ oliverskawronek/compass-navigator 712a2192352dab13940a9cddaae362126c7e222c
_AT_ phishman3579/android-compass 6fada1435b3dca1f30e72233ff05ae516cdd01e4
# _AT_ umair13adil/DirectionMeter 5975dd049f511992698bdd1ae51654824c177010

#OEF
