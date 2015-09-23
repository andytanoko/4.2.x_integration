#!/bin/bash
# this script starts up the GridTalk application components: Mysql, Log Server and JBoss

#Script for nohup.out archive #############
. rollNohup.sh

mkdir -p $archive_path

# move and rename nohup.out into the folder of "log_archive"
cp $log_path/nohup.out $archive_path/nohup_`date +%Y%m%d`_`date +%H%M%S`.$file_ext
cat /dev/null > $log_path/nohup.out

# file listing for the files to be delete
echo 'Deleted log file:'
find $archive_path -mtime +$file_age -exec ls {} \;

# delete log files according to file age
find $archive_path -mtime +$file_age -exec rm {} \;

##############################

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
. $LOGSERVER_STOP_CMD
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

