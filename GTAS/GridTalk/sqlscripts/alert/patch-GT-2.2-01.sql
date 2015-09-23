# 30 Dec 03 GT 2.2 I4 [Neo Sok Lay] Add Web Service user procedure type for user procedure message template.

USE userdb;

UPDATE message_template
SET Message=REPLACE(Message,'[1] Shell Executable [2] Java Procedure\r\n', '[1] Shell Executable [2] Java Procedure [3] Web Service\r\n')
WHERE Message LIKE ('%[1] Shell Executable [2] Java Procedure%')
;