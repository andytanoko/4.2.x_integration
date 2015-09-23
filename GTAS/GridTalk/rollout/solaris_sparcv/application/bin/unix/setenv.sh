
echo $PROGNAME
APPSERVER_HOME=$GRIDTALK_HOME/jboss-4.0.5.GA
#
JAVA_HOME=$GRIDTALK_HOME/jre150_10
#
ANT_HOME=$GRIDTALK_HOME/../jakarta-ant-1.5.1-bin
#
DB_HOME=$GRIDTALK_HOME/mysql
#
# set path to Ant and Java
PATH=$ANT_HOME/bin:$JAVA_HOME/bin:$GRIDTALK_HOME/bin:$PATH
#
# application specific environment settings
APPLICATION="GT(GridTalk)"
#
# database specific environment settings
DB_STARTUP_CMD="startmysql.sh"
DB_SHUTDOWN_CMD="stopmysql.sh"
DB_CHECK_CMD="pingmysql.sh"
GRIDTALK_DB_PASSWORD="gtasdb"
#
DATA_HOME=$GRIDTALK_HOME/data
APPS_HOME=$DATA_HOME/GNapps
LOGS_HOME=$GRIDTALK_HOME/logs
#
# application server specific environment settings
APPSERVER_LOG_DIR=$LOGS_HOME/appserver
APPSERVER_LOG_EXT=log
APPSERVER_LOGS=$APPSERVER_LOG_DIR/*.$APPSERVER_LOG_EXT
APPSERVER_CMD_DIR="$APPSERVER_HOME/bin"
APPSRV_STARTUP_CMD="run.sh"
APPSRV_STARTUP_MSG="Started in"
APPSRV_SHUTDOWN_CMD="shutdown.sh --server=localhost:31099"
APPSRV_SHUTDOWN_MSG="Shutdown complete"
#
# log server specific settings
LOGSERVER_START_CMD="startLogServer.sh"
LOGSERVER_STOP_CMD="stopLogServer.sh"
LOGSERVER_PID_FILE="$GRIDTALK_HOME/bin/gt_logger.pid"

# change this to uniquely identify the node on the cluster
HOST_ID=gnode1

#
# environment settings
#
JAVA_OPTS="-Dentity.ejb.use.remote=false"
#
# sys.global.dir : set the shared data folder
# java.io.tmpdir : set io temp dir
# hostid         : set the unique host identifier for this node in the cluster
JAVA_OPTS="-Dsys.global.dir=$DATA_HOME -Djava.io.tmpdir=$APPS_HOME/temp/ -Dhostid=$HOST_ID $JAVA_OPTS"
#
# node.clustered : set whether to turn on clustering
# jboss.partition.name: Name of cluster partition to join
JAVA_OPTS="-Dnode.clustered=false -Djboss.partition.name=GNGtasPartition $JAVA_OPTS"
#
 
export APPSERVER_HOME JAVA_HOME ANT_HOME DB_HOME PATH APPLICATION JAVA_OPTS
export DB_STARTUP_CMD DB_SHUTDOWN_CMD DB_CHECK_CMD GRIDTALK_DB_PASSWORD
export APPSERVER_LOG_DIR APPSERVER_LOG_EXT APPSERVER_LOGS APPSERVER_CMD_DIR
export APPSRV_STARTUP_CMD APPSRV_STARTUP_MSG APPSRV_SHUTDOWN_CMD APPSRV_SHUTDOWN_MSG
export LOGSERVER_START_CMD LOGSERVER_STOP_CMD LOGSERVER_PID_FILE
export DATA_HOME APPS_HOME HOST_ID LOGS_HOME