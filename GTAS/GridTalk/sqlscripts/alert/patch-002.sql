# 03 Mar 2003 I8 v2.0.18 [Neo Sok Lay] Update CanDelete to false for default Alerts & templates.
#

USE userdb;

UPDATE alert_type SET CanDelete='0' WHERE UID IN ('1','2','3','4','5');
UPDATE action SET CanDelete='0' WHERE UID IN ('-1','-2','-3');
UPDATE alert SET CanDelete='0' WHERE UID IN ('-1','-2','-3');
UPDATE message_template SET CanDelete='0' WHERE UID IN ('-1','-2','-3');
