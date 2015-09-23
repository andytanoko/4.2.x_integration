# 29 Apr 2003 v2.1 I1 [Neo Sok Lay] Add Raise Alert Workflow Activity
# 08 Feb 2006 GT4.0   [Neo Sok Lay] Increase PartnerFunctionId column to 30 chars.

use appdb;

#
# Dumping data for table 'entitymetainfo'
#
# Field 1 ObjectName: full qualified class name of the entity
# Field 2 EntityName: short class name of the entity
# Field 3 SqlName: table name of the entity

# entitymetainfo for gridtalk_mapping_rule
DELETE FROM entitymetainfo WHERE EntityName = 'PartnerFunction';
DELETE FROM entitymetainfo WHERE EntityName = 'WorkflowActivity';
INSERT INTO entitymetainfo VALUES("com.gridnode.gtas.server.partnerfunction.model.PartnerFunction","PartnerFunction","partner_function");
INSERT INTO entitymetainfo VALUES("com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity","WorkflowActivity","workflow_activity");

#
# Dumping data for table 'fieldmetainfo'
#
# UID bigint(20) NOT NULL auto_increment,
# ObjectName: field name in Entity class ,
# FieldName: field ID in Entity Interface class ,
# SqlName: Field column name in Table,
# ValueClass: field data type,
# EntityObjectName: full qualified class name of the entity
# Label: field display label
# Length: valid field length ,
# Proxy: '1' if proxy, '0' otherwise,,
# Mandatory: '1' if mandatory, '0' otherwise,
# Editable: '1' if editable, '0' otherwise
# Displayable: '1' if displayable, '0' otherwise
# OqlName: ,
# Sequence: default '999' ,
# Presentation: properties for presentation of the field
# Constraints: constraint imposed on the values of the field
#

# fieldmetainfo for partner_function
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%PartnerFunction';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.partnerfunction.model.PartnerFunction","partnerFunction.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.partnerfunction.model.PartnerFunction","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.partnerfunction.model.PartnerFunction","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_partnerFunctionId","PARTNER_FUNCTION_ID","PartnerFunctionId","java.lang.String",
"com.gridnode.gtas.server.partnerfunction.model.PartnerFunction","partnerFunction.id",
"30","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=false\r\neditable.create=true",
"type=text\r\ntext.length.max=30"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.gtas.server.partnerfunction.model.PartnerFunction","partnerFunction.description",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_triggerOn","TRIGGER_ON","TriggerOn","java.lang.Integer",
"com.gridnode.gtas.server.partnerfunction.model.PartnerFunction","partnerFunction.triggerOn",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\npartnerFunction.triggerOn.import=0\r\npartnerFunction.triggerOn.receive=1\r\npartnerFunction.triggerOn.manualSend=2\r\npartnerFunction.triggerOn.manualExport=3"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_workflowActivityUids","WORKFLOW_ACTIVITY_UIDS","WorkflowActivityUids","java.lang.ArrayList",
"com.gridnode.gtas.server.partnerfunction.model.PartnerFunction","partnerFunction.workflowActivitiesUids",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=foreign\r\nforeign.key=workflowActivity.uid\r\nforeign.display=workflowActivity.description\r\nforeign.cached=false\r\ncollection=true"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_workflowActivities","WORKFLOW_ACTIVITIES",null,"java.lang.ArrayList",
"com.gridnode.gtas.server.partnerfunction.model.PartnerFunction","partnerFunction.workflowActivities",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=embedded\r\nembedded.type=workflowActivity\r\ncollection=true"
);

# fieldmetainfo for workflow_activity
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%WorkflowActivity';
INSERT INTO fieldmetainfo VALUES(
NULL,"_uId","UID","UID","java.lang.Long",
"com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity","workflowActivity.uid",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=uid"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_version","VERSION","Version","java.lang.Double",
"com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=range"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_canDelete","CAN_DELETE","CanDelete","java.lang.Boolean",
"com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity","",
"0","0","0","0","0","","999",
"displayable=false\r\nmandatory=false\r\neditable=false",
"type=enum\r\ncandelete.enabled=true\r\ncandelete.disabled=false"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_activityType","ACTIVITY_TYPE","ActivityType","java.lang.Integer",
"com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity","workflowActivity.activityType",
"0","0","1","0","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=enum\r\nworkflowActivity.activityType.mappingRule=0\r\nworkflowActivity.activityType.userProcedure=1\r\nworkflowActivity.activityType.exitToImport=2\r\nworkflowActivity.activityType.exitToOutbound=3\r\nworkflowActivity.activityType.exitToExport=4\r\nworkflowActivity.activityType.exitWorkflow=5\r\nworkflowActivity.activityType.exitToPort=6\r\nworkflowActivity.activityType.saveToFolder=7\r\nworkflowActivity.activityType.exitToChannel=8\r\nworkflowActivity.activityType.raiseAlert=9"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_description","DESCRIPTION","Description","java.lang.String",
"com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity","workflowActivity.description",
"80","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=text\r\ntext.length.max=80"
);
INSERT INTO fieldmetainfo VALUES(
NULL,"_paramList","PARAM_LIST","ParamList","java.util.Vector",
"com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity","workflowActivity.paramList",
"0","0","1","1","1","","999",
"displayable=true\r\nmandatory=true\r\neditable=true",
"type=other\r\ncollection=true\r\ncollection.element=java.lang.Object"
);
