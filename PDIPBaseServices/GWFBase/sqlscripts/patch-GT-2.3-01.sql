# 26 Mar 2004 GT 2.3.2 [Neo Sok Lay] Add state field to xpdb_package table.


USE userdb;

ALTER TABLE xpdl_package
ADD COLUMN State smallint(1)  DEFAULT 1 NOT NULL
;


USE appdb;

INSERT INTO fieldmetainfo VALUES
(NULL,"_state","STATE","State","java.lang.Short","com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlPackage","XpdlPackage.state","1","0","1","1","1","","999","displayable=false","")
;
