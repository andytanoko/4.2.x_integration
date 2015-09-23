#!/bin/bash
# this script stops the mysql database

. $GRIDTALK_HOME/bin/setenv.sh

cd $DB_HOME
./bin/mysqladmin -u root --socket=/tmp/gt_mysql.sock --port=3306 -p$GRIDTALK_DB_PASSWORD shutdown
