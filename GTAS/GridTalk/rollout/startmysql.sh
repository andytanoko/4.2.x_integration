set +v
# . setenv.sh
echo In startmysql.sh $DB_HOME >>$HOME/GridTalk_mysqlsetup.log
cd $DB_HOME
# ./bin/safe_mysqld -u root --basedir=$DB_HOME --datadir=$DB_HOME/data --socket=/tmp/mysql.sock --port=3306 >>$HOME/GridTalk_mysqlsetup.log &
$DB_HOME/bin/mysqld_safe --user=mysql --basedir=$DB_HOME --datadir=$DB_HOME/data --socket=/tmp/gt_mysql.sock --port=3306 --default-character-set=utf8 --default-collation=utf8_unicode_ci >>$HOME/GridTalk_mysqlsetup.log &

