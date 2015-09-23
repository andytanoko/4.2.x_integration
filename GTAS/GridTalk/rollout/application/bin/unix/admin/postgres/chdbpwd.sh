#!/bin/bash
# this script handles changing of GridTalk root user database password
if [ $# -lt 3 -o "$1" == "-h" ]; then
  echo "**** Change postgres root/DB role password ****"
  echo Usage: change PW option 1 : change default 'postgres' role password
  echo Usage: change PW option 2 : change GTAS DB role password
  echo Usage: $0 [PW option 1 or 2] [current password] [new password]
  echo Version: 4.2
  exit
fi

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

. $GRIDTALK_HOME/bin/setenv.sh
. $GRIDTALK_HOME/bin/$DB_STARTUP_CMD
sh $GRIDTALK_HOME/bin/$DB_CHECK_CMD

if [ $1 -eq 1 ] ;
then

    ## wait a while so that we can obtain unix socket without issue
    sleep 2
    ../../pgsql/bin/psql --username postgres --port 35432 -c "ALTER USER postgres ENCRYPTED PASSWORD '$3';"
	
    if [ $? -ne 0 ] ;
    then
      . $GRIDTALK_HOME/bin/$DB_SHUTDOWN_CMD
	  exit
    fi

	perl -e "s/GRIDTALK_DB_PASSWORD=\"$2\"/GRIDTALK_DB_PASSWORD=\"$3\"/g;" -pi $GRIDTALK_HOME/bin/setenv.sh

	echo Database root password changed.
fi


if [ $1 -eq 2 ] ;
then

    ## wait a while so that we can obtain unix socket without issue
    sleep 2
	../../pgsql/bin/psql --username postgres --port 35432 -c "ALTER USER userdb ENCRYPTED PASSWORD '$3';ALTER USER userdb ENCRYPTED PASSWORD '$3';ALTER USER appdb ENCRYPTED PASSWORD '$3';ALTER USER jbossdb ENCRYPTED PASSWORD '$3';ALTER USER archivedb ENCRYPTED PASSWORD '$3';"
	
  if [ $? -ne 0 ] ;
    then
      . $GRIDTALK_HOME/bin/$DB_SHUTDOWN_CMD
	  exit
  fi

	# change the password in related files
	perl -e "s/<password>$2<\/password>/<password>$3<\/password>/g;" -pi $APPSERVER_HOME/server/default/deploy/postgres-ds.xml

	echo Password changed successfully
fi

. $GRIDTALK_HOME/bin/$DB_SHUTDOWN_CMD


