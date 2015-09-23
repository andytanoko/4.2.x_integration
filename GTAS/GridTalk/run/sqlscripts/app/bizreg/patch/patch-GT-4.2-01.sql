-- Tam Wei Xiang     16 May 2009       Change "Language" & "Country" to type varchar
--                                     as the "Char" type in Postgres will be auto appended
--                                     with white space

set search_path='userdb';

alter table "whitepage" ALTER COLUMN "Language" type varchar(3);
alter table "whitepage" ALTER COLUMN "Country" type varchar(3);