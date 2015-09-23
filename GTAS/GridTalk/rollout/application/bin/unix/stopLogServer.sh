#!/bin/bash
# this script stops the remote logging server for GridTalk

. $GRIDTALK_HOME/bin/setenv.sh
echo pid file $LOGSERVER_PID_FILE
if [ -e $LOGSERVER_PID_FILE ]; then
  kill `cat $LOGSERVER_PID_FILE`
  rm $LOGSERVER_PID_FILE
fi
