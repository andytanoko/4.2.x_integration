SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
set search_path='userdb';

alter table "certificate" alter "IssuerName" type varchar(1000);