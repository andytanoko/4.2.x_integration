#!/bin/bash
# this script shuts down the GridTalk application components: Jboss, Log server

if [ ! -d $GRIDTALK_HOME ];
then
  if [ -e setenv.sh ];
  then
    PROGNAME=GTAS
    CURR_DIR=`pwd`
    GRIDTALK_HOME=$CURR_DIR/..
    export PROGNAME CURR_DIR GRIDTALK_HOME
  else
    echo GRIDTALK_HOME not set. Please setenv GRIDTALK_HOME.
    exit
  fi
fi

cd $GRIDTALK_HOME/bin

. setenv.sh

echo Shutting down the application server now, please wait ....
JBOSS_HOME=$APPSERVER_HOME
export JBOSS_HOME
cd $APPSERVER_CMD_DIR
sh $JBOSS_HOME/bin/$APPSRV_SHUTDOWN_CMD &>/dev/null
#
cd $GRIDTALK_HOME/bin
echo grepping shutdown msg ....
while true; do
 grep -i -w "$APPSRV_SHUTDOWN_MSG" $APPSERVER_LOGS >/dev/null
 if [ $? -eq 0 ]; then
   break
 fi
done
echo shutdown msg grepped
#
echo Shutting down the log server now...
. $LOGSERVER_STOP_CMD

echo Successfully shutdown $APPLICATION
