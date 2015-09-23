# 07 May 2004 GT2.3.3 [Guo Jianyu] Add AS2 packaging envelope

USE appdb;

UPDATE fieldmetainfo
SET Constraints="type=enum\r\npackagingInfo.envelope.none=NONE\r\npackagingInfo.envelope.rnif1=RNIF1\r\npackagingInfo.envelope.rnif2=RNIF2\r\npackagingInfo.envelope.as2=AS2\r\n"
WHERE FieldName="ENVELOPE"
AND EntityObjectName="com.gridnode.pdip.app.channel.model.PackagingInfo";

