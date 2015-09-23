# this script ping the mysql server until it is alive

# . setenv.sh

DBSTATE=1
until [ $DBSTATE -eq 0 ]; do
  $DB_HOME/bin/mysqladmin --port=3306 --socket=/tmp/gt_mysql.sock -u root -p$1 ping
  DBSTATE=$?
done

