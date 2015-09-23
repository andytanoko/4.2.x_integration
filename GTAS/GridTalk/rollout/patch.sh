#!/bin/bash

logfile=patchlog.txt

echo Please review the patch log after patching for any errors

if [ ! -d $GRIDTALK_HOME ]; then
  echo No installed GridTalk found at GRIDTALK_HOME: $GRIDTALK_HOME
  echo Patch aborted.
  exit 1
fi

echo
echo Patching GridTalk at this location $GRIDTALK_HOME
. $GRIDTALK_HOME/bin/setenv.sh

echo "The GridTalk application must be shutdown before patching. Please shut it down now if you have not done so."
read -n 1 -p  "Are you ready to proceed for patching (y/n)?" PROCEED

echo
if [ "$PROCEED" != "y" ]; then
  echo "Patch is cancelled."
  exit 2
fi

# create backup dir
BACKUP_DIR=`date '+backup_%Y%m%d%H%M%S'`
mkdir $BACKUP_DIR

BACKUP_APP=$BACKUP_DIR/gridtalk
mkdir $BACKUP_APP

BACKOUT_LIST=$BACKUP_DIR/backout.list
BACKOUT_SH=$BACKUP_DIR/backout.sh

# start patching
properties="-Dpatch.properties=patch.properties -Dbackup.dir=$BACKUP_APP -Dbackout.list=${BACKOUT_LIST}"
properties="-Dgtas.version.file=$GRIDTALK_HOME/bin/gtas.version $properties"

if [ "$1" = "-data" ]; then
  properties="-Dpatch.commondata=yes $properties"
fi

echo Patching starts...
cp -p $GRIDTALK_HOME/bin/gtas.version $BACKUP_DIR/
ant $properties -buildfile patch-main.xml -logfile $logfile
if [ "$?" != 0 ]; then
  echo "Some problems found during patching. Please check the log file $logfile for details."
  exit 3
fi

echo "Creating backout script: $BACKOUT_SH ..."
echo "echo Performing backout using $BACKOUT_SH" > $BACKOUT_SH
cat $BACKOUT_LIST | awk '{ print "rm -rf " $1 }' >> $BACKOUT_SH
echo "cp -r -p $BACKUP_APP/* $GRIDTALK_HOME" >> $BACKOUT_SH
echo "cp -p $BACKUP_DIR/gtas.version $GRIDTALK_HOME/bin" >> $BACKOUT_SH
BACKV=`cat $BACKUP_DIR/gtas.version`
echo "echo GridTalk reverted back to v$BACKV" >> $BACKOUT_SH

chmod +x $BACKOUT_SH

echo Patch completed
