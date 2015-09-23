#$1 : Path to sql executable command psql
#$2 : Path to sql script file
#$3 : DB password 

#psql client will get the password from this variable. And it will not
#prompt the user for entering the password.
PGPASSWORD=$3
export PGPASSWORD

$1 --username postgres --port 35432 --dbname gtdb --file $2
