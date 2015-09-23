#!/bin/bash
# this script handles the changing of database connection strings & passwords used by GridTalk applications
# a single parameter is expected as the filename of the map file of old and new JDBC urls & passwords.

#set +v

mapfile="$1"
if  [ $# -eq 0 ]; then
  echo Map file required.
  echo Usage: ./switchdb.sh db.map [-r]
  exit 1
fi


APP_BASE=$GRIDTALK_HOME
JBOSS_BASE=$APP_BASE/jboss-4.0.5.GA
echo App: $APP_BASE
echo Jboss: $JBOSS_BASE

SWITCH_DIR=`date '+switch_%Y%m%d%H%M%S'`
mkdir $SWITCH_DIR
BAK_DIR=$SWITCH_DIR/bak
mkdir $BAK_DIR
echo Backing up files to be changed under $BAK_DIR ...
cp $APP_BASE/data/GNapps/gtvan/txmr/config.jsp $BAK_DIR
cp $JBOSS_BASE/server/default/deploy/gt*-ds.xml $BAK_DIR

echo Changing database connection strings using $mapfile ...

#change the access writes of the files to be changed
chmod u+w $APP_BASE/data/GNapps/gtvan/txmr/config.jsp
chmod u+w $APP_BASE/jboss-4.0.5.GA/server/default/deploy/gt*-ds.xml

mapcmd=./$SWITCH_DIR/switch.sh
echo "echo Executing $mapcmd ..." > $mapcmd

if [ "$2" == "-r" ]; then
    cat $mapfile | awk '{ print "perl -e \"s/" $3 "/" $1 "/g;\" -pi $APP_BASE/data/GNapps/gtvan/txmr/config.jsp" >> outfile}' outfile=$mapcmd
    cat $mapfile | awk '{ print "perl -e \"s/" $3 "/" $1 "/g;\" -pi $JBOSS_BASE/server/default/deploy/gt*-ds.xml" >> outfile}' outfile=$mapcmd
else
    cat $mapfile | awk '{ print "perl -e \"s/" $1 "/" $3 "/g;\" -pi $APP_BASE/data/GNapps/gtvan/txmr/config.jsp" >> outfile}' outfile=$mapcmd
    cat $mapfile | awk '{ print "perl -e \"s/" $1 "/" $3 "/g;\" -pi $JBOSS_BASE/server/default/deploy/gt*-ds.xml" >> outfile}' outfile=$mapcmd
fi

chmod a+rwx $mapcmd
. $mapcmd

echo Finished changing database connection strings
echo 
NEW_DIR=$SWITCH_DIR/new
mkdir $NEW_DIR
echo Copying changed files to $NEW_DIR for subsequent nodes ...
cp $JBOSS_BASE/server/default/deploy/gt*-ds.xml $NEW_DIR

cpcmd=./$SWITCH_DIR/cpswitch.sh
echo Writing out $cpcmd for subsequent nodes...
echo "echo Executing $cpcmd ..." > $cpcmd
echo "cp $NEW_DIR/gt*-ds.xml \$GRIDTALK_HOME/jboss-4.0.5.GA/server/default/deploy/" >> $cpcmd
chmod a+rwx $cpcmd
echo Done.
