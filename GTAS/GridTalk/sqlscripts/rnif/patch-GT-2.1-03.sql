# 05 Dec 2003 GT 2.1 [Neo Sok Lay] Increase column size for ProcessInstanceId,PIPInstanceIdentifier,ProcessTransactionId in rn_profile table
#                                  and ProcessInstance

USE userdb;

ALTER TABLE rn_profile
MODIFY ProcessInstanceId VARCHAR(200),
MODIFY PIPInstanceIdentifier VARCHAR(200),
MODIFY ProcessTransactionId VARCHAR(200),
MODIFY ProcessActionId VARCHAR(240)
;


USE appdb;

UPDATE fieldmetainfo
SET Length=200
WHERE Label IN ("RNProfile.processInstanceId","RNProfile.PIPInstanceIdentifier","RNProfile.processTransactionId","processInstance.processInstanceId")
;

UPDATE fieldmetainfo
SET Length=240
WHERE Label='RNProfile.processActionId'
;

UPDATE fieldmetainfo
SET Constraints="displayable=true\r\neditable=false\nmandatory=false\r\n','type=text\r\ntext.length.max=200\r\n"
WHERE Label="processInstance.processInstanceId"
;