#!/bin/bash

# to replace existing gtvan-httpbc-gthb_*.jar with gtvan-httpbc-gthb.jar contents.
. $GRIDTALK_HOME/bin/setenv.sh

cd $APPS_HOME/gtas/data/sys/uproc/jar
for JAR in `ls gtvan-httpbc-gthb_*.jar`; do
  cp -p gtvan-httpbc-gthb.jar $JAR
done

