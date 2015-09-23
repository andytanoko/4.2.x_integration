#
# Table for holding those JMS msg that failed to be sent out
#
USE userdb;

CREATE TABLE jms_failed_msg (
   UID INTEGER(19) NOT NULL,
   DestinationType VARCHAR(20),
   JmsConfigProps BLOB,
   DestName VARCHAR(100) NOT NULL,
   MsgObj BLOB NOT NULL,
   MsgProps BLOB,
   CreatedDate TIMESTAMP(3) NOT NULL,
   RetryCount INTEGER(10) DEFAULT 0 NOT NULL,
   PRIMARY KEY(UID)
);