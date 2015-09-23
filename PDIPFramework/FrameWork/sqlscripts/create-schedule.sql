USE jbossdb;

DROP TABLE IF EXISTS schedule;
CREATE TABLE IF NOT EXISTS schedule
(
  schedule_uid bigint auto_increment,
  target VARCHAR(100),
  method_name VARCHAR(100),
  method_signature VARCHAR(100),
  start_date VARCHAR(20),
  period BIGINT,
  repetitions INTEGER,
  date_format VARCHAR(20),
  PRIMARY KEY (schedule_uid)
);
