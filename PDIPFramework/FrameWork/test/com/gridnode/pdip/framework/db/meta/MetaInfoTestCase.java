package com.gridnode.pdip.framework.db.meta;

import com.gridnode.pdip.framework.j2ee.*;
import com.gridnode.pdip.framework.log.*;
import com.gridnode.pdip.framework.db.meta.*;
import junit.framework.*;
/**
 * Title:        PDIP
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode
 * @author Mahesh
 * @version 1.0
 */

public class MetaInfoTestCase extends TestCase {
	IEntityMetaInfoHome home;
	IEntityMetaInfoObj remote;

    public MetaInfoTestCase(String name) {
        super(name);
    }

    public static Test suite() {
    	return new TestSuite(MetaInfoTestCase.class);
    }

    public void setUp(){
    	home=(IEntityMetaInfoHome)ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(IEntityMetaInfoHome.class);
		initializeData();
    }

    public void initializeData(){
    }

	public void testCreate() throws Exception {
		assertNotNull("home [IEntityMetaInfoHome] is null in testCreate",home);
		EntityMetaInfo entityMetaInfo=new EntityMetaInfo();
		FieldMetaInfo fieldMetaInfo=null;
		entityMetaInfo.setEntityName("com.gridnode.pdip.gridtalk.entity.FileType");
		entityMetaInfo.setSqlName("filetype");
		entityMetaInfo.setObjectName("com.gridnode.pdip.gridtalk.entity.FileType");

		fieldMetaInfo=new FieldMetaInfo();
		fieldMetaInfo.setDisplayable(false);
		fieldMetaInfo.setEditable(false);
		fieldMetaInfo.setMandatory(false);
		fieldMetaInfo.setProxy(false);
		fieldMetaInfo.setLength(0);
		fieldMetaInfo.setSequence(999);
		fieldMetaInfo.setFieldName("CAN_DELETE");
		fieldMetaInfo.setValueClass("java.lang.Boolean");
		fieldMetaInfo.setSqlName("CanDelete");
		fieldMetaInfo.setOqlName("canDelete");
		fieldMetaInfo.setObjectName("_canDelete");
		fieldMetaInfo.setEntityName("com.gridnode.pdip.gridtalk.entity.FileType");

		fieldMetaInfo=new FieldMetaInfo();
		fieldMetaInfo.setDisplayable(true);
		fieldMetaInfo.setEditable(false);
		fieldMetaInfo.setMandatory(true);
		fieldMetaInfo.setProxy(false);
		fieldMetaInfo.setLength(10);
		fieldMetaInfo.setSequence(1);
		fieldMetaInfo.setFieldName("FILE_TYPE");
		fieldMetaInfo.setValueClass("java.lang.String");
		fieldMetaInfo.setSqlName("FieldType");
		fieldMetaInfo.setOqlName("fileType");
		fieldMetaInfo.setObjectName("_fileType");
		fieldMetaInfo.setEntityName("com.gridnode.pdip.gridtalk.entity.FileType");
		entityMetaInfo.addFieldMetaInfo(fieldMetaInfo);

		fieldMetaInfo=new FieldMetaInfo();
		fieldMetaInfo.setDisplayable(false);
		fieldMetaInfo.setEditable(false);
		fieldMetaInfo.setMandatory(false);
		fieldMetaInfo.setProxy(false);
		fieldMetaInfo.setLength(0);
		fieldMetaInfo.setSequence(999);
		fieldMetaInfo.setFieldName("CAN_DELETE");
		fieldMetaInfo.setValueClass("java.lang.Boolean");
		fieldMetaInfo.setSqlName("CanDelete");
		fieldMetaInfo.setOqlName("canDelete");
		fieldMetaInfo.setObjectName("_canDelete");
		fieldMetaInfo.setEntityName("com.gridnode.pdip.gridtalk.entity.FileType");
		entityMetaInfo.addFieldMetaInfo(fieldMetaInfo);
		home.create(entityMetaInfo);
	}

	public void testFindAllEntityMetaInfo() throws Exception {
	}

	public void testFindByPrimaryKey() throws Exception {
		remote=home.findByPrimaryKey("com.gridnode.pdip.gridtalk.entity.FileType");
		assertNotNull("remote [IEntityMetaInfo] is null in testFindByPrimaryKey",remote);
		EntityMetaInfo entityMetaInfo=remote.getData();
		Log.debug("MetaInfoTestCase","Data Retrived using find by primary key "+entityMetaInfo.toString());
                FieldMetaInfo [] fieldMetaInfo=entityMetaInfo.getFieldMetaInfo();
                for(int loop=0;fieldMetaInfo!=null && loop<fieldMetaInfo.length;loop++){
                        Log.debug("MetaInfoTestCase","Data Retrived using find by primary key "+fieldMetaInfo[loop].toString());
                }
	}

	public void testRemove() throws Exception {
		remote=home.findByPrimaryKey("com.gridnode.pdip.gridtalk.entity.FileType");
		assertNotNull("remote [IEntityMetaInfo] is null in testRemove",remote);
		remote.remove();
	}

    public static void main(String args[]){
        junit.textui.TestRunner.run (suite());
    }

}