. $GRIDTALK_HOME/bin/setenv.sh

curr=`pwd`
if [ -e ftp.pid ]; then
   echo FTP pull task already running, stop it first before starting a new one.
   exit 1
fi;

$JAVA_HOME/bin/java -Dapplog.home="$GRIDTALK_HOME/logs" -jar startScheduleFTPPull.jar & 
echo $! > ftp.pid
cd $curr
cd `pwd`
