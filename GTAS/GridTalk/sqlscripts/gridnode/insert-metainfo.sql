USE appdb;

# Entity meta info
DELETE from entitymetainfo WHERE EntityName IN ('GnCategory','GridNode','ConnectionStatus');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.gridnode.model.GnCategory', 'GnCategory', 'gncategory');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.gridnode.model.GridNode', 'GridNode', 'gridnode');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.gridnode.model.ConnectionStatus', 'ConnectionStatus', 'connection_status');

# GnCategory
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%GnCategory';
INSERT INTO fieldmetainfo VALUES
(NULL, '_name', 'CATEGORY_NAME', 'Name', 'java.lang.String', 'com.gridnode.gtas.server.gridnode.model.GnCategory', 'gnCategory.name', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=text\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_code', 'CATEGORY_CODE', 'Code', 'java.lang.String', 'com.gridnode.gtas.server.gridnode.model.GnCategory', 'gnCategory.code', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=uid');
INSERT INTO fieldmetainfo VALUES
(NULL, '_nodeUsage', 'NODE_USAGE', 'NodeUsage', 'java.lang.String', 'com.gridnode.gtas.server.gridnode.model.GnCategory', 'gnCategory.nodeUsage', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=text');

# GridNode
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%gridnode.model.GridNode';
INSERT INTO fieldmetainfo VALUES
(NULL, '_uId', 'UID', 'UID', 'java.lang.Long', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.uid', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=uid\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_id', 'ID', 'ID', 'java.lang.String', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.id', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=text\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_name', 'NAME', 'Name', 'java.lang.String', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.name', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=true\r\n', 'type=text\r\ntext.length.max=80\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_category', 'CATEGORY', 'Category', 'java.lang.String', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.category', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=foreign\r\nforeign.key=gnCategory.code\r\nforeign.display=gnCategory.name\r\nforeign.cached=false\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_state', 'STATE', 'State', 'java.lang.Short', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.state', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=enum\r\ngridNode.state.me=0\r\ngridNode.state.deleted=1\r\ngridNode.state.active=2\r\ngridNode.state.inactive=3\r\ngridNode.state.gm=4\r\n');
#(NULL, '_state', 'STATE', 'State', 'java.lang.Short', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.state', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=enum\r\ngridNode.state.me=0\r\ngridNode.state.deleted=1\r\ngridNode.state.new=2\r\ngridNode.state.activated=3\r\ngridNode.state.deactivated=4\r\ngridNode.state.inPending=5\r\ngridNode.state.outPending=6\r\ngridNode.state.inDenied=7\r\ngridNode.state.outRejected=8\r\ngridNode.state.outAborted=9\r\ngridNode.state.server=10\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_activationReason', 'ACTIVATION_REASON', 'ActivationReason', 'java.lang.String', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.activationReason', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=text\r\ntext.length.max=255\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtCreated', 'DT_CREATED', 'DTCreated', 'java.sql.Timestamp', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.dtCreated', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtReqActivate', 'DT_REQ_ACTIVATE', 'DTReqActivate', 'java.sql.Timestamp', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.dtReqActivate', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtActivated', 'DT_ACTIVATED', 'DTActivated', 'java.sql.Timestamp', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.dtActivated', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtDeactivated', 'DT_DEACTIVATED', 'DTDeactivated', 'java.sql.Timestamp', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.dtDeactivated', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_coyProfileUID', 'COY_PROFILE_UID', 'CoyProfileUID', 'java.lang.Long', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.coyProfileUid', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=foreign\r\nforeign.key=coyProfile.uid\r\nforeign.display=coyProfile.coyName\r\nforeign.cached=false\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_version', 'VERSION', 'Version', 'java.lang.Double', 'com.gridnode.gtas.server.gridnode.model.GridNode', 'gridNode.version', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=range\r\n');

# ConnectionStatus
DELETE from fieldmetainfo WHERE EntityObjectName LIKE '%ConnectionStatus';
INSERT INTO fieldmetainfo VALUES
(NULL, '_uId', 'UID', 'UID', 'java.lang.Long', 'com.gridnode.gtas.server.gridnode.model.ConnectionStatus', 'connectionStatus.uid', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=uid\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_nodeID', 'GRIDNODE_ID', 'NodeID', 'java.lang.String', 'com.gridnode.gtas.server.gridnode.model.ConnectionStatus', 'connectionStatus.gridnodeId', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=foreign\r\nforeign.key=gridnode.id\r\nforeign.display=gridnode.name\r\nforeign.cached=false\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_statusFlag', 'STATUS_FLAG', 'StatusFlag', 'java.lang.Short', 'com.gridnode.gtas.server.gridnode.model.ConnectionStatus', 'connectionStatus.statusFlag', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=enum\r\nconnectionStatus.statusFlag.online=0\r\nconnectionStatus.statusFlag.offline=1\r\nconnectionStatus.statusFlag.connecting=2\r\nconnectionStatus.statusFlag.reconnecting=3\r\nconnectionStatus.statusFlag.disconnectng=4\r\nconnectionStatus.statusFlag.expired=5\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtLastOnline', 'DT_LAST_ONLINE', 'DTLastOnline', 'java.sql.Timestamp', 'com.gridnode.gtas.server.gridnode.model.ConnectionStatus', 'connectionStatus.dtLastOnline', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_dtLastOffline', 'DT_LAST_OFFLINE', 'DTLastOffline', 'java.sql.Timestamp', 'com.gridnode.gtas.server.gridnode.model.ConnectionStatus', 'connectionStatus.dtLastOffline', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=datetime\r\ndatetime.date=true\r\ndatetime.time=true\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_reconnectionKey', 'RECONNECTION_KEY', 'ReconnectionKey', 'java.lang.String', 'com.gridnode.gtas.server.gridnode.model.ConnectionStatus', 'connectionStatus.reconnectionKey', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', NULL);
INSERT INTO fieldmetainfo VALUES
(NULL, '_connectedServerNode', 'CONNECTED_SERVER_NODE', 'ConnectedServerNode', 'java.lang.String', 'com.gridnode.gtas.server.gridnode.model.ConnectionStatus', 'connectionStatus.connectedServerNode', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=foreign\r\nforeign.key=gridnode.id\r\nforeign.display=gridnode.name\r\nforeign.cached=false\r\n');


