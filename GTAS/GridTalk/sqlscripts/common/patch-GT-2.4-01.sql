USE mysql;

update user set password=password("gtasdb") where user="root"; 
flush privileges;