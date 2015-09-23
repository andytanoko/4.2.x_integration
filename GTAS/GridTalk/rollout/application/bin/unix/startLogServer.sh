#!/bin/bash
# this script starts the remote logging server for GridTalk

. $GRIDTALK_HOME/bin/setenv.sh

java -Duser.dir=$GRIDTALK_HOME -Dapplog.home="$LOGS_HOME" -Dhost.id="$HOST_ID" -cp "$APPSERVER_HOME/server/default/lib/log4j.jar:$APPSERVER_HOME/server/default/lib/custom-log.jar:$APPSERVER_HOME/server/default/lib/joesnmp.jar:$APPSERVER_HOME/server/default/lib/log4j-snmp-appender.jar" com.gridnode.pdip.framework.log.GNSocketServer 12345 "$APPS_HOME/conf/default/lcf" &

# For clustering
#java -Duser.dir=$GRIDTALK_HOME -Dapplog.home="$LOGS_HOME" -Dhost.id="$HOST_ID" -cp "$APPSERVER_HOME/server/default/lib/log4j.jar:$APPSERVER_HOME/server/default/lib/custom-log.jar:$APPSERVER_HOME/server/default/lib/joesnmp.jar:$APPSERVER_HOME/server/default/lib/log4j-snmp-appender.jar" com.gridnode.pdip.framework.log.GNSocketServer 12345 "$APPS_HOME/conf/default/lcf" &

echo $! > $LOGSERVER_PID_FILE
