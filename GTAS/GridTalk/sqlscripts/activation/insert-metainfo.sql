USE appdb;

# Entity meta info
DELETE from entitymetainfo WHERE EntityName IN ('SearchGridNodeCriteria','SearchGridNodeQuery','SearchedGridNode','ActivationRecord','GridNodeActivation');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria', 'SearchGridNodeCriteria', '');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.activation.model.SearchGridNodeQuery', 'SearchGridNodeQuery', '');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.activation.model.SearchedGridNode', 'SearchedGridNode', '');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.activation.model.ActivationRecord', 'ActivationRecord', 'activation_record');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.activation.model.GridNodeActivation', 'GridNodeActivation', '');

# SearchGridNodeCriteria
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%SearchGridNodeCriteria';
INSERT INTO fieldmetainfo VALUES
(NULL,"_criteriaType","CRITERIA","","java.lang.Short","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.criteriaType","0","0","0","1","1","","999","displayable=true\r\nmandatory=true\r\neditable=true\r\neditable.display=false\r\n","type=enum\r\nsearchGridNodeCriteria.criteriaType.none=1\r\nsearchGridNodeCriteria.criteriaType.gridNode=2\r\nsearchGridNodeCriteria.criteriaType.whitePage=3\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_matchType","MATCH","","java.lang.Short","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.matchType","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=true\r\neditable.display=false\r\n","type=enum\r\nsearchGridNodeCriteria.matchType.all=1\r\nsearchGridNodeCriteria.matchType.any=2\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_gridnodeID","GRIDNODE_ID","","java.lang.Integer","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.gridNodeId","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=range\r\nrange.min=1\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_gridnodeName","GRIDNODE_NAME","","java.lang.String","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.gridNodeName","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=text\r\ntext.length.max=80\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_category","CATEGORY","","java.lang.String","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.category","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=foreign\r\nforeign.key=gnCategory.code\r\nforeign.display=gnCategory.name\r\nforeign.cached=false\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_busDesc","BUSINESS_DESC","","java.lang.String","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.businessDesc","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=text\r\ntext.length.max=80\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_dUNS","DUNS","CONTACT_PERSON","java.lang.String","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.duns","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=text\r\ntext.length.max=20\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_contactPerson","CONTACT_PERSON","","java.lang.String","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.contactPerson","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=text\r\ntext.length.max=80\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_email","EMAIL","","java.lang.String","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.email","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_tel","TEL","","java.lang.String","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.tel","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=text\r\ntext.length.max=16\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_fax","FAX","","java.lang.String","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.fax","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=text\r\ntext.length.max=16\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_website","WEBSITE","","java.lang.String","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.website","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=text\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_country","COUNTRY","","java.lang.String","com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria","searchGridNodeCriteria.country","0","0","0","1","1","","999","displayable=true\r\neditable=true\r\nmandatory=false\r\neditable.display=false\r\n","type=foreign\r\nforeign.key=countryCode.alpha3Code\r\nforeign.display=countryCode.name\r\nforeign.cached=false\r\n");

# SearchGridNodeQuery
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%SearchGridNodeQuery';
INSERT INTO fieldmetainfo VALUES
(NULL, '_searchID', 'SEARCH_ID', '', 'java.lang.Long', 'com.gridnode.gtas.server.activation.model.SearchGridNodeQuery', 'searchGridNodeQuery.searchId', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=uid\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtSubmitted', 'DT_SUBMITTED', '', 'java.sql.Timestamp', 'com.gridnode.gtas.server.activation.model.SearchGridNodeQuery', 'searchGridNodeQuery.dtSubmitted', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtResponded', 'DT_RESPONDED', '', 'java.sql.Timestamp', 'com.gridnode.gtas.server.activation.model.SearchGridNodeQuery', 'searchGridNodeQuery.dtResponded', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_criteria', 'CRITERIA', '', 'com.gridnode.gtas.server.activation.model.SearchGridNodeCriteria', 'com.gridnode.gtas.server.activation.model.SearchGridNodeQuery', 'searchGridNodeQuery.criteria', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=embedded\r\nembedded.type=searchGridNodeCriteria');
INSERT INTO fieldmetainfo VALUES
(NULL, '_results', 'RESULTS', '', 'java.util.Collection', 'com.gridnode.gtas.server.activation.model.SearchGridNodeQuery', 'searchGridNodeQuery.results', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'collection=true\r\ntype=embedded\r\nembedded.type=searchedGridNode\r\n');

# SearchedGridNode
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%SearchedGridNode';
INSERT INTO fieldmetainfo VALUES
(NULL, '_gridnodeID', 'GRIDNODE_ID', '', 'java.lang.Integer', 'com.gridnode.gtas.server.activation.model.SearchedGridNode', 'searchedGridNode.gridNodeId', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=range\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_gridnodeName', 'GRIDNODE_NAME', '', 'java.lang.String', 'com.gridnode.gtas.server.activation.model.SearchedGridNode', 'searchedGridNode.gridNodeName', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=text\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_state', 'STATE', '', 'java.lang.Short', 'com.gridnode.gtas.server.activation.model.SearchedGridNode', 'searchedGridNode.state', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=enum\r\nsearchedGridNode.state.me=0\r\nsearchedGridNode.state.active=1\r\nsearchedGridNode.state.inactive=2\r\nsearchedGridNode.state.pending=3\r\n');

# ActivationRecord
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%ActivationRecord';
INSERT INTO fieldmetainfo VALUES
(NULL, '_uId', 'UID', 'UID', 'java.lang.Long', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.uid', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=uid\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_currentType', 'CURRENT_TYPE', 'CurrentType', 'java.lang.Short', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.currentType', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=enum\r\activationRecord.currentType.activation=0\r\activationRecord.currentType.deactivation=1\r\activationRecord.currentType.approval=2\r\activationRecord.currentType.denial=3\r\activationRecord.currentType.abortion=4\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_actDirection', 'ACT_DIRECTION', 'ActDirection', 'java.lang.Short', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.actDirection', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=enum\r\activationRecord.actDirection.incoming=1\r\activationRecord.actDirection.outgoing=2\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_deactDirection', 'DEACT_DIRECTION', 'DeactDirection', 'java.lang.Short', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.deactDirection', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=enum\r\activationRecord.deactDirection.incoming=1\r\activationRecord.deactDirection.outgoing=2\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_gridnodeID', 'GRIDNODE_ID', 'GridNodeID', 'java.lang.Integer', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.gridNodeId', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\neditable.create=true\r\nmandatory=true\r\n', 'type=range\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_gridnodeName', 'GRIDNODE_NAME', 'GridNodeName', 'java.lang.String', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.gridNodeName', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\neditable.create=true\r\nmandatory=true\r\n', 'type=text\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtRequested', 'DT_REQUESTED', 'DTRequested', 'java.sql.Timestamp', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.dtRequested', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtApproved', 'DT_APPROVED', 'DTApproved', 'java.sql.Timestamp', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.dtApproved', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtAborted', 'DT_ABORTED', 'DTAborted', 'java.sql.Timestamp', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.dtAborted', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtDenied', 'DT_DENIED', 'DTDenied', 'java.sql.Timestamp', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.dtDenied', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtDeactivated', 'DT_DEACTIVATED', 'DTDeactivated', 'java.sql.Timestamp', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.dtDeactivated', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_isLatest', 'IS_LATEST', 'IsLatest', 'java.lang.Boolean', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.isLatest', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=enum\r\ngeneric.yes=true\r\ngeneric.no=false');
INSERT INTO fieldmetainfo VALUES
(NULL, '_activationDetails', 'ACTIVATION_DETAILS', 'ActivationDetails', 'com.gridnode.gtas.server.activation.model.GridNodeActivation', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.activationDetails', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=embedded\r\nembedded.type=gridNodeActivation');
INSERT INTO fieldmetainfo VALUES
(NULL, '_transCompleted', 'TRANS_COMPLETED', 'TransCompleted', 'java.lang.Boolean', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.isTransCompleted', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=enum\r\ngeneric.yes=true\r\ngeneric.no=false');
INSERT INTO fieldmetainfo VALUES
(NULL, '_transFailReason', 'TRANS_FAIL_REASON', 'TransFailReason', 'java.lang.String', 'com.gridnode.gtas.server.activation.model.ActivationRecord', 'activationRecord.transFailReason', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=text');

# GridNodeActivation
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%GridNodeActivation';
INSERT INTO fieldmetainfo VALUES
(NULL, '_activateReason', 'ACTIVATE_REASON', '', 'java.lang.String', 'com.gridnode.gtas.server.activation.model.GridNodeActivation', 'gridNodeActivation.activateReason', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\neditable.create=true\r\nmandatory=true\r\n', 'type=text\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_requestDetails', 'REQUEST_DETAILS', '', 'com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode', 'com.gridnode.gtas.server.activation.model.GridNodeActivation', 'gridNodeActivation.requestDetails', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', '');
INSERT INTO fieldmetainfo VALUES
(NULL, '_approveDetails', 'APPROVE_DETAILS', '', 'com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode', 'com.gridnode.gtas.server.activation.model.GridNodeActivation', 'gridNodeActivation.approveDetails', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', '');
INSERT INTO fieldmetainfo VALUES
(NULL, '_requestorBeList', 'REQUESTOR_BE_LIST', '', 'java.util.Collection', 'com.gridnode.gtas.server.activation.model.GridNodeActivation', 'gridNodeActivation.requestorBeList', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\neditable.create=true\r\nmandatory=true\r\n', 'collection=true\r\ntype=foreign\r\nforeign.key=businessEntity.uid\r\nforeign.display=businessEntity.id\r\nforeign.additionalDisplay=enterpriseId\r\nforeign.cached=true');
INSERT INTO fieldmetainfo VALUES
(NULL, '_approverBeList', 'APPROVER_BE_LIST', '', 'java.util.Collection', 'com.gridnode.gtas.server.activation.model.GridNodeActivation', 'gridNodeActivation.approverBeList', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\neditable.update=true\r\nmandatory=true\r\n', 'collection=true\r\ntype=foreign\r\nforeign.key=businessEntity.uid\r\nforeign.display=businessEntity.id\r\nforeign.additionalDisplay=enterpriseId\r\nforeign.cached=true');

