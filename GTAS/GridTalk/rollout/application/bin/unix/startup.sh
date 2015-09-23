#!/bin/bash
# this script starts up the GridTalk application components: Mysql, Log Server and JBoss

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

echo Executing startup script for $APPLICATION...
#
echo Starting the application database now...
sh $DB_STARTUP_CMD
sh $DB_CHECK_CMD
echo Application Database started successfully
#
# clear appserver logs
if [ ! -d $APPSERVER_LOG_DIR ]; then
  mkdir $APPSERVER_LOG_DIR
else
  rm $APPSERVER_LOG_DIR/*.$APPSERVER_LOG_EXT
  echo > $APPSERVER_LOG_DIR/dummy.$APPSERVER_LOG_EXT 
fi  
#
echo Starting the logging server now...
cd $GRIDTALK_HOME/bin
sh $LOGSERVER_STOP_CMD
sh $LOGSERVER_START_CMD &
#
echo Starting the application server now, please wait...
JBOSS_HOME=$APPSERVER_HOME
export JBOSS_HOME
echo AppServer Cmd Dir=$APPSERVER_CMD_DIR
echo AppServer Startup Cmd=$APPSRV_STARTUP_CMD
#
cd $APPSERVER_CMD_DIR
sh $APPSRV_STARTUP_CMD &
appserver_pid=$!
#
cd $GRIDTALK_HOME/bin
# grep startup msg to ensure appserver is up
while true; do
 grep -i -w "$APPSRV_STARTUP_MSG" $APPSERVER_LOGS >/dev/null
 if [ $? -eq 0 ]; then
   break
 fi
done
#
echo Application Server startup successful
echo Successfully startup $APPLICATION

# wait for the appserver to end before ending this script
wait $appserver_pid
