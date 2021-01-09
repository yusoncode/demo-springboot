#!/usr/bin/env sh

BASE_DIR=`cd "$(dirname $0)/.." >/dev/null; pwd`

#############  Please Modify the following custom variables ############

# application name
APP_NAME=demo-springboot

# jvm options
JVM_OPTS="-server -Xms128m -Xmx128m\
 -XX:+HeapDumpOnOutOfMemoryError\
 -XX:HeapDumpPath=${BASE_DIR}/logs/dump.hprof"

#########################################################################
