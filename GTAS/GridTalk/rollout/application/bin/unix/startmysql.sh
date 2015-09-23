. $GRIDTALK_HOME/bin/setenv.sh

$DB_HOME/bin/mysqld --user=mysql --basedir=$DB_HOME --datadir=$DB_HOME/data --socket=/tmp/gt_mysql.sock --port=3306 --default-character-set=utf8 --default-collation=utf8_unicode_ci --max_allowed_packet=200M &

