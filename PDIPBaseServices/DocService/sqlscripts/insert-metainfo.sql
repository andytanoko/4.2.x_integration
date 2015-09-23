USE appdb;

#
# Dumping data FOR TABLE 'entitymetainfo'
#
DELETE FROM entitymetainfo WHERE EntityName IN ("Domain","Folder","Document","File");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.docservice.filesystem.model.Domain","Domain","domain");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.docservice.filesystem.model.Folder","Folder","folder");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.docservice.filesystem.model.Document","Document","document");
INSERT INTO entitymetainfo VALUES("com.gridnode.pdip.base.docservice.filesystem.model.File","File","file");


#
# Dumping data FOR TABLE 'fieldmetainfo'
#

### Role
DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%Domain";
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.Domain","domain.uid","20","0","0","0","0","0","999","displayable=false\r\neditable=false\r\nmandatory=false\r\n","type=uid\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_domainName","DOMAINNAME","DomainName","java.lang.String","com.gridnode.pdip.base.docservice.filesystem.model.Domain","domain.domainName","30","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=2\r\ntext.length.max=30\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_propertyName","PROPERTYNAME","PropertyName","java.lang.String","com.gridnode.pdip.base.docservice.filesystem.model.Domain","domain.propertyName","255","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=1\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_childCount","CHILDCOUNT","ChildCount","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.Domain","domain.childCount","0","0","1","1","1","0","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");


DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%Folder";
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.Folder","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_folderName","FOLDERNAME","FolderName","java.lang.String","com.gridnode.pdip.base.docservice.filesystem.model.Folder","folder.folderName","30","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=2\r\ntext.length.max=30\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_domainId","DOMAINID","DomainId","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.Folder","folder.domainId","255","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=1\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_parentId","PARENTID","ParentId","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.Folder","folder.parentId","255","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=1\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_childCount","CHILDCOUNT","ChildCount","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.Folder","folder.childCount","0","0","1","1","1","0","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");

DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%Document";
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.Document","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_documentName","DOCUMENTNAME","DocumentName","java.lang.String","com.gridnode.pdip.base.docservice.filesystem.model.Document","","30","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=2\r\ntext.length.max=30\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_docType","DOCTYPE","DocType","java.lang.String","com.gridnode.pdip.base.docservice.filesystem.model.Document","","30","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=2\r\ntext.length.max=30\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_parentId","PARENTID","ParentId","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.Document","","255","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=1\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_domainId","DOMAINID","DomainId","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.Document","","255","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=1\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_fileCount","FILECOUNT","FileCount","java.lang.Integer","com.gridnode.pdip.base.docservice.filesystem.model.Document","","0","0","1","1","1","0","999","displayable=true\r\neditable=false\r\nmandatory=false\r\n","type=enum\r\ngeneric.yes=true\r\ngeneric.no=false\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_author","AUTHOR","Author","java.lang.String","com.gridnode.pdip.base.docservice.filesystem.model.Document","","30","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=2\r\ntext.length.max=30\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_size","SIZE","Size","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.Document","","255","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=1\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_createdOn","CREATEDON","CreatedOn","java.util.Date","com.gridnode.pdip.base.docservice.filesystem.model.Document","","255","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=1\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_lastAccessed","LASTACCESSED","LastAccessed","java.util.Date","com.gridnode.pdip.base.docservice.filesystem.model.Document","","255","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=1\r\ntext.length.max=255\r\n");

DELETE FROM fieldmetainfo WHERE EntityObjectName LIKE "%File";
INSERT INTO fieldmetainfo VALUES(NULL,"_uId","UID","UID","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.File","",0,"0","0","1","1","",999,'displayable=false','');
INSERT INTO fieldmetainfo VALUES(NULL,"_fileName","FILENAME","FileName","java.lang.String","com.gridnode.pdip.base.docservice.filesystem.model.File","","30","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=2\r\ntext.length.max=30\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_isMainFile","ISMAINFILE","IsMainFile","java.lang.Boolean","com.gridnode.pdip.base.docservice.filesystem.model.File","","30","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=2\r\ntext.length.max=30\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_documentId","DOCUMENTID","DocumentId","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.File","","255","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=1\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_parentId","PARENTID","ParentId","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.File","","255","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=1\r\ntext.length.max=255\r\n");
INSERT INTO fieldmetainfo VALUES(NULL,"_domainId","DOMAINID","DomainId","java.lang.Long","com.gridnode.pdip.base.docservice.filesystem.model.File","","255","0","1","1","1","0","1","displayable=true\r\neditable=true\r\nmandatory=true\r\n","type=text\r\ntext.length.min=1\r\ntext.length.max=255\r\n");
