#!/bin/bash

logfile=$HOME/GridTalk_setup.log
if [ -d $1 ]; then
  cd $1
  echo [setup] Changed directory to $1 >> $logfile
fi

echo [setup] before setenv >> $logfile
. ./setenv.sh
echo [setup] after setenv `set` >> $logfile

# application server environment settings
properties=
properties=-Dsetup.properties=setup.properties
#ant_params="-debug $properties -buildfile $1/setup.xml"

if [ "$2" = "-data" ]; then
  properties="-Dsetup.commondata=yes $properties"
fi

echo [setup] copy tools.jar >> $logfile
# Put tools.jar into JRE to prevent exception for JSP
if [ ! -f "$JAVA_HOME/lib/tools.jar" ]; then
  cp ./jre150_10/lib/tools.jar $JAVA_HOME/lib/tools.jar;
fi


echo [setup] calling ant with: -debug $properties -buildfile $1/setup.xml >> $logfile
ant $properties -buildfile $1/setup.xml >> $logfile
