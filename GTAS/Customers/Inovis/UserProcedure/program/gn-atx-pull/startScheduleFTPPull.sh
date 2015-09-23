curr=`pwd`
if [ -e ftp.pid ]; then
   echo FTP pull task already running, stop it first before starting a new one.
   exit 1
fi;

$GRIDTALK_HOME/jre150_10/bin/java -Dapplog.home="$GRIDTALK_HOME/logs" -Djava.io.tmpdir="$GRIDTALK_HOME/data/GNapps/temp" -jar startScheduleFTPPull.jar & 
echo $! > ftp.pid
cd $curr
cd `pwd`