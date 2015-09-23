USE appdb;


DELETE FROM entitymetainfo WHERE EntityName = 'License';
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.app.license.model.License","License","license");


# License
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE '%License';
INSERT INTO fieldmetainfo VALUES
(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.app.license.model.License","license.uid","0","0","0","1","1","","999","displayable=false\r\nmandatory=false\r\neditable=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_prodKey","PRODUCT_KEY","ProductKey","java.lang.String","com.gridnode.pdip.app.license.model.License","license.productKey","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=true\r\n","type=text\r\ntext.length.max=30\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_prodName","PRODUCT_NAME","ProductName","java.lang.String","com.gridnode.pdip.app.license.model.License","license.productName","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\ntext.length.max=80\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_prodVersion","PRODUCT_VERSION","ProductVersion","java.lang.String","com.gridnode.pdip.app.license.model.License","license.productVersion","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=text\r\ntext.length.max=20\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_startDate","START_DATE","StartDate","java.util.Date","com.gridnode.pdip.app.license.model.License","license.startDate","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=datetime\r\ndatetime.date=true\r\ndatetime.time=false\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_endDate","END_DATE","EndDate","java.util.Date","com.gridnode.pdip.app.license.model.License","license.endDate","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=datetime\r\ndatetime.date=true\r\ndatetime.time=false\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL,"_state","STATE","State","java.lang.Short","com.gridnode.pdip.app.license.model.License","license.state","0","0","0","1","1","","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\nlicense.state.valid=0\r\nlicense.state.notCommenced=1\r\nlicense.state.expired=2\r\nlicense.state.revoked=3\r\n");
INSERT INTO fieldmetainfo VALUES
(NULL, '_version', 'VERSION', 'Version', 'java.lang.Double', 'com.gridnode.pdip.app.license.model.License', 'license.version', 0, 0, 0, 1, 1, NULL, 999, 'displayable=false\r\neditable=false\r\nmandatory=false\r\n', 'type=range\r\n');

