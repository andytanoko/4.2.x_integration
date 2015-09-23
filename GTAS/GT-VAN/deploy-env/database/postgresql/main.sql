-- Database: gtvandb

-- DROP DATABASE gtvandb;

CREATE DATABASE gtvandb
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default;


CREATE ROLE gtvan WITH LOGIN ENCRYPTED PASSWORD 'gridnode';

CREATE SCHEMA gtvan
  AUTHORIZATION gtvan;