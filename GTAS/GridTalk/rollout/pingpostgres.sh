# this script ping the postgres server until it is alive


DBSTATE=1
until [ $DBSTATE -eq 0 ]; do
  $GRIDTALK_HOME/pgsql/bin/pg_ctl status -D $GRIDTALK_HOME/db/
  DBSTATE=$?
done

