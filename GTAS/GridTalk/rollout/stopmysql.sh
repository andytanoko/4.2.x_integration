set +v
#. setenv.sh
echo In stopmysql.sh $DB_HOME >>$HOME/GridTalk_mysqlsetup.log
cd $DB_HOME
echo Command ./bin/mysqladmin -u root --socket=/tmp/gt_mysql.sock --port=3306 shutdown >>$HOME/GridTalk_mysqlsetup.log
./bin/mysqladmin -u root -p$1 --socket=/tmp/gt_mysql.sock --port=3306 shutdown >>$HOME/GridTalk_mysqlsetup.log &
