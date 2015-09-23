USE appdb;

DELETE FROM entitymetainfo WHERE EntityName IN ('NetworkSetting','ConnectionSetupParam','ConnectionSetupResult','JmsRouter');
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.connection.model.NetworkSetting', 'NetworkSetting', NULL);
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.connection.model.ConnectionSetupParam', 'ConnectionSetupParam', NULL);
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.connection.model.ConnectionSetupResult', 'ConnectionSetupResult', NULL);
INSERT INTO entitymetainfo VALUES ('com.gridnode.gtas.server.connection.model.JmsRouter', 'JmsRouter', 'jms_router');

# NetworkSetting
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%NetworkSetting';
INSERT INTO fieldmetainfo VALUES
(NULL, '_connectionLevel', 'CONNECTION_LEVEL', NULL, 'java.lang.Short', 'com.gridnode.gtas.server.connection.model.NetworkSetting', 'networkSetting.connectionLevel', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=true\r\n', 'type=enum\r\nnetworkSetting.connectionLevel.noFirewall=0\r\n\r\nnetworkSetting.connectionLevel.firewallNoProxy=1\r\nnetworkSetting.connectionLevel.proxyNoAuth=2\r\nnetworkSetting.connectionLevel.proxyWithAuth=3\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_localJmsRouter', 'LOCAL_JMS_ROUTER', NULL, 'java.lang.String', 'com.gridnode.gtas.server.connection.model.NetworkSetting', 'networkSetting.localJmsRouter', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=false\r\n', 'type=text\r\ntext.length.max=50\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_httpProxyServer', 'HTTP_PROXY_SERVER', NULL, 'java.lang.String', 'com.gridnode.gtas.server.connection.model.NetworkSetting', 'networkSetting.httpProxyServer', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=false\r\n', 'type=text\r\ntext.length.max=50\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_httpProxyPort', 'HTTP_PROXY_PORT', NULL, 'java.lang.Integer', 'com.gridnode.gtas.server.connection.model.NetworkSetting', 'networkSetting.httpProxyPort', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=false\r\n', 'type=range\r\nrange.min=1\r\nrange.max=65535\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_proxyAuthUser', 'PROXY_AUTH_USER', NULL, 'java.lang.String', 'com.gridnode.gtas.server.connection.model.NetworkSetting', 'networkSetting.proxyAuthUser', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=false\r\n', 'type=text\r\ntext.length.max=50\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_proxyAuthPassword', 'PROXY_AUTH_PASSWORD', NULL, 'java.lang.String', 'com.gridnode.gtas.server.connection.model.NetworkSetting', 'networkSetting.proxyAuthPassword', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=false\r\n', 'type=text\r\ntext.length.max=20\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_keepAliveInterval', 'KEEP_ALIVE_INTERVAL', NULL, 'java.lang.Integer', 'com.gridnode.gtas.server.connection.model.NetworkSetting', 'networkSetting.keepAliveInterval', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=true\r\n', 'type=range\r\nrange.min=2\r\nrange.max=10\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_responseTimeout', 'RESPONSE_TIMEOUT', NULL, 'java.lang.Integer', 'com.gridnode.gtas.server.connection.model.NetworkSetting', 'networkSetting.responseTimeout', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=true\r\n', 'type=range\r\nrange.min=10\r\nrange.max=60\r\n');

# ConnectionSetupParam
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%ConnectionSetupParam';
INSERT INTO fieldmetainfo VALUES
(NULL, '_currentLocation', 'CURRENT_LOCATION', NULL, 'java.lang.String', 'com.gridnode.gtas.server.connection.model.ConnectionSetupParam', 'connectionSetupParam.currentLocation', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=true\r\n', 'type=foreign\r\nforeign.key=countryCode.alpha3Code\r\nforeign.display=countryCode.name\r\nforeign.cached=false\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_servicingRouter', 'SERVICING_ROUTER', NULL, 'java.lang.String', 'com.gridnode.gtas.server.connection.model.ConnectionSetupParam', 'connectionSetupParam.servicingRouter', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=true\r\n', 'type=text\r\ntext.length.max=50\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_originalLocation', 'ORIGINAL_LOCATION', NULL, 'java.lang.String', 'com.gridnode.gtas.server.connection.model.ConnectionSetupParam', 'connectionSetupParam.originalLocation', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=foreign\r\nforeign.key=countryCode.alpha3Code\r\nforeign.display=countryCode.name\r\nforeign.cached=false\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_originalServicingRouter', 'ORIGINAL_SERVICING_ROUTER', NULL, 'java.lang.String', 'com.gridnode.gtas.server.connection.model.ConnectionSetupParam', 'connectionSetupParam.originalServicingRouter', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=text\r\ntext.length.max=50\r\n');

# ConnectionSetupResult
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%ConnectionSetupResult';
INSERT INTO fieldmetainfo VALUES
(NULL, '_status', 'STATUS', NULL, 'java.lang.Short', 'com.gridnode.gtas.server.connection.model.ConnectionSetupResult', 'connectionSetupResult.status', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=enum\r\nconnectionSetupResult.status.notDone=0\r\n\r\nconnectionSetupResult.status.success=1\r\nconnectionSetupResult.status.failure=2\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_setupParams', 'SETUP_PARAMETERS', NULL, 'com.gridnode.gtas.server.connection.model.ConnectionSetupParam', 'com.gridnode.gtas.server.connection.model.ConnectionSetupResult', 'connectionSetupResult.setupParameters', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=embedded\r\nembedded.type=connectionSetupParam\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_failureReason', 'FAILURE_REASON', NULL, 'java.lang.String', 'com.gridnode.gtas.server.connection.model.ConnectionSetupResult', 'connectionSetupResult.failureReason', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=text\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_availableGridMasters', 'AVAILABLE_GRIDMASTERS', NULL, 'java.util.Collection', 'com.gridnode.gtas.server.connection.model.ConnectionSetupResult', 'connectionSetupResult.availableGridMasters', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=false\r\n', 'collection=true\r\ntype=foreign\r\nforeign.key=gridNode.uid\r\nforeign.cached=true\r\nforeign.display=gridNode.name\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_availableRouters', 'AVAILABLE_ROUTERS', NULL, 'java.util.Collection', 'com.gridnode.gtas.server.connection.model.ConnectionSetupResult', 'connectionSetupResult.availableRouters', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=true\r\nmandatory=false\r\n', 'collection=true\r\ntype=foreign\r\nforeign.key=jmsRouter.uid\r\nforeign.cached=true\r\nforeign.display=jmsRouter.name\r\n');

# JmsRouter
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%JmsRouter';
INSERT INTO fieldmetainfo VALUES
(NULL, '_uId', 'UID', 'UID', 'java.lang.Long', 'com.gridnode.gtas.server.connection.model.JmsRouter', 'jmsRouter.uid', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=uid\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_name', 'NAME', 'Name', 'java.lang.String', 'com.gridnode.gtas.server.connection.model.JmsRouter', 'jmsRouter.name', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=text\r\n');
INSERT INTO fieldmetainfo VALUES
(NULL, '_ipAddress', 'IP_ADDRESS', 'IpAddress', 'java.lang.String', 'com.gridnode.gtas.server.connection.model.JmsRouter', 'jmsRouter.ipAddress', 0, 0, 0, 1, 1, NULL, 999, 'displayable=true\r\neditable=false\r\nmandatory=false\r\n', 'type=text\r\n');


