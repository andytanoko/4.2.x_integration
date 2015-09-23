#!/bin/bash

logfile=$HOME/GridTalk_setup.log
if [ -d $1 ]; then
  cd $1
  echo [setup] Changed directory to $1 >> $logfile
fi

echo [setup] before setenv >> $logfile
. setenv.sh
echo [setup] after setenv `set` >> $logfile

# application server environment settings
properties=
#properties=-Dappserver.name=jboss
#properties="$properties -Dappserver.version=4.0.2"
#properties="$properties -Dappserver.home=$APPSERVER_HOME"
#properties="$properties -Dappserver.deploy.dir=$APPSERVER_HOME/server/default/deploy"

# database environment settings
#properties="$properties -Ddb.name=mysql"
#properties="$properties -Ddb.home=$DB_HOME"
#properties="$properties -Ddb.script.cmd=./dbscript.sh"
#properties="$properties -Ddb.start.cmd=./startmysql.sh"
#properties="$properties -Ddb.stop.cmd=./stopmysql.sh"
#properties="$properties -Ddb.exec.cmd=bin/mysql"
#properties="$properties -Ddb.root.password=gtasdb"
#properties="$properties -Ddb.check.cmd=./pingmysql.sh"

# application environment settings
#properties="$properties -Dapplication.name=gtas"
#properties="$properties -Dapplication.bin.dir=$GRIDTALK_HOME/bin"
#properties="$properties -Dapplication.docs.dir=$GRIDTALK_HOME/docs"
#properties="$properties -Dapplication.data.dir=$APPSERVER_HOME/bin/gtas/data"
#properties="$properties -Dapplication.conf.dir=$APPSERVER_HOME/bin/conf"
#properties="$properties -Dapplication.backend.dir=$GRIDTALK_HOME/backend"

properties=-Dsetup.properties=setup.properties
ant_params="-debug $properties -buildfile $1/setup.xml"

echo [setup] copy tools.jar >> $logfile
# Put tools.jar into JRE to prevent exception for JSP
if [ ! -f "$JAVA_HOME/lib/tools.jar" ]; then
  cp ./jre150_20/lib/tools.jar $JAVA_HOME/lib/tools.jar;
fi

# Replace the security policy file, as Bouncy Castle API requires key length greater than the default one
echo replacing  local_policy.jar >> $logfile
echo replacing  US_export_policy.jar >> $logfile
cp -p ./jre150_20/lib/security/local_policy.jar $JAVA_HOME/lib/security/local_policy.jar
cp -p ./jre150_20/lib/security/US_export_policy.jar $JAVA_HOME/lib/security/US_export_policy.jar


#echo [setup] setting up GridTalk with these properties: $properties >> $logfile
#. ant -quiet -buildfile "$1/setup.xml" $properties  >> $logfile

echo [setup] calling ant with: -debug $properties -buildfile $1/setup.xml >> $logfile
ant $properties -buildfile $1/setup.xml >> $logfile
