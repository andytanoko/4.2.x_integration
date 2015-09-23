#!/bin/bash
# this script handles the changing of ports used by the GridTalk applications
# a single parameter is expected as the filename of the map file of old and new ports.
# if not specified, it will use the ports.map in the current directory.

#set +v

mapfile="$1"
if  [ $# -eq 0 ]; then
  mapfile=ports.map 
fi

. $GRIDTALK_HOME/bin/setenv.sh
APP_BASE=$GRIDTALK_HOME
APP_SERVER=$APPSERVER_HOME

echo Mapping ports using $mapfile

#change the access writes of the files to be changed
chmod u+w $APP_BASE/bin/*.sh
chmod -R u+w $APP_BASE/backend/*.*
chmod -R u+w $APP_BASE/data/GNapps/conf/default/*.*
chmod -R u+w $APP_SERVER/server/default/conf/*.*
chmod -R u+w $APP_SERVER/server/default/deploy/*.*
if [ -d $APP_SERVER/server/default/deploy-hasingleton/ ];
then
  chmod -R u+w $APP_SERVER/server/default/deploy-hasingleton/*.*
fi


echo App: $APP_BASE
echo Jboss: $APP_SERVER

mapcmd=/tmp/mapped.sh
echo "echo executing $mapcmd" > $mapcmd
cat $mapfile | awk -F= '{ print "perl -e \"s/" $2 "/" $3 "/g;\" -pi $APP_BASE/bin/*.sh" >> outfile}' outfile=$mapcmd
cat $mapfile | awk -F= '{ print "perl -e \"s/" $2 "/" $3 "/g;\" -pi $APP_BASE/bin/admin/*.sh" >> outfile}' outfile=$mapcmd
cat $mapfile | awk -F= '{ print "perl -e \"s/" $2 "/" $3 "/g;\" -pi $(find $APP_BASE/data/GNapps/conf/default -type f)" >> outfile}' outfile=$mapcmd
cat $mapfile | awk -F= '{ print "perl -e \"s/" $2 "/" $3 "/g;\" -pi $APP_SERVER/server/default/conf/*.xml" >> outfile}' outfile=$mapcmd
cat $mapfile | awk -F= '{ print "perl -e \"s/" $2 "/" $3 "/g;\" -pi $APP_SERVER/server/default/deploy/*.xml" >> outfile}' outfile=$mapcmd
cat $mapfile | awk -F= '{ print "perl -e \"s/" $2 "/" $3 "/g;\" -pi $APP_SERVER/server/default/deploy/jboss-web.deployer/*.xml" >> outfile}' outfile=$mapcmd
cat $mapfile | awk -F= '{ print "perl -e \"s/" $2 "/" $3 "/g;\" -pi $APP_SERVER/server/default/deploy/jms/*.xml" >> outfile}' outfile=$mapcmd
cat $mapfile | awk -F= '{ print "perl -e \"s/" $2 "/" $3 "/g;\" -pi $APP_SERVER/server/default/deploy/http-invoker.sar/META-INF/*.xml" >> outfile}' outfile=$mapcmd
if [ -d $APP_SERVER/server/default/deploy-hasingleton/ ];
then
  cat $mapfile | awk -F= '{ print "perl -e \"s/" $2 "/" $3 "/g;\" -pi $APP_SERVER/server/default/deploy-hasingleton/jms/*.xml" >> outfile}' outfile=$mapcmd
fi

chmod a+rwx $mapcmd
. $mapcmd

echo finish changing ports
