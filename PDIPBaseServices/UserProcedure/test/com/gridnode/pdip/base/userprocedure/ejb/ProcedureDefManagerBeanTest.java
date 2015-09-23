/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcedureDefLogger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * AUG 01 2002    Jagadeesh         Created
 */



package com.gridnode.pdip.base.userprocedure.ejb;


import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.helpers.IProcedureDefFilePathConfig;
import com.gridnode.pdip.base.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.model.*;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

/**
 * Test case for testing ProcedureDefManagerBean.
 *
 * Before You Proceed with testing please ensure that
 * the test case are written with some "UID Dependency".
 *
 * Make sure that this UID's Present and uncomment
 * to run the TestCase.
 *
 */


public class ProcedureDefManagerBeanTest extends TestCase
{
  IUserProcedureManagerHome _procedureDefManagerHome;
  IUserProcedureManagerObj _procedureDefManagerObj;
  ProcedureDefFile procedureDefFile;
  ProcedureDef procedureDef;


  public ProcedureDefManagerBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ProcedureDefManagerBeanTest.class);
  }

  public void setUp() throws Exception
  {
    try
    {
      Logger.log("TEST [ProcedureDefManagerBeanTest.setUp] Enter");
      lookupProcedureDefMgr();
    }
    finally
    {
      Logger.log("TEST [ProcedureDefManagerBeanTest.setUp] Exit");
    }
  }

  public void lookupProcedureDefMgr() throws Exception
  {
    _procedureDefManagerHome = (IUserProcedureManagerHome)ServiceLookup.getInstance(
                  ServiceLookup.CLIENT_CONTEXT).getHome(
                  IUserProcedureManagerHome.class);
    assertNotNull("ProcedureDefManager Home is null", _procedureDefManagerHome);
    _procedureDefManagerObj = _procedureDefManagerHome.create();
    assertNotNull("ProcedureDefManager Object is null", _procedureDefManagerObj);
  }


  public void testCreateProcedureDefFile()
  {
    try
    {
      Logger.log(" ProcedureDefManagerBeanTest [testCreateProcedureDefFile] Enter");
      createtestDataProcedureDefFile();
      Logger.log("ProcedureDefManagerBeanTest [testCreateProcedureDefFile] Before Calling");
      Long uid=_procedureDefManagerObj.createProcedureDefinitionFile(procedureDefFile);
      Logger.log("ProcedureDefManagerBeanTest [testCreateProcedureDefFile] After Calling");
      Logger.log("ProcedureDefManagerBeanTest [testCreateProcedureDefFile] UID IS "+uid);
    }
    catch(Exception ex)
    {
      Logger.log("ProcedureDefManagerBeanTest [testCreateProcedureDefFile]"+
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }


  public void testExecute()
  {

    try
    {
      Logger.log(" ProcedureDefManagerBeanTest [testCreateProcedureDefFile] Enter");
      createtestDataProcedureDefFile();
      UserProcedure uproc = new UserProcedure();
      uproc.setDescription("adfa");
      uproc.setProcedureType(IProcedureType.PROC_TYPE_JAVA);
      JavaProcedure jproc = new JavaProcedure();
      jproc.setClassName("mytest.TestLoader");
      jproc.setMethodName("add");
      jproc.setIsLocal(true);
      jproc.setType(IProcedureType.PROC_TYPE_JAVA);
      uproc.setName("ava");
      uproc.setProcedureDefFile(procedureDefFile);
      uproc.setProcedureDef(jproc);
      ParamDef paramDef = new ParamDef();
      paramDef.setActualValue(new Integer(1));
      paramDef.setSource(ParamDef.SOURCE_USER_DEFINED);
      paramDef.setType(ParamDef.DATA_TYPE_INTEGER);
      paramDef.setName("Param 1");
      ParamDef paramDef2 = new ParamDef();
      paramDef2.setActualValue(new Integer(2));
      paramDef2.setSource(ParamDef.SOURCE_USER_DEFINED);
      paramDef2.setType(ParamDef.DATA_TYPE_INTEGER);
      paramDef2.setName("Param 2");
      Vector paramDefVect = new Vector();
      paramDefVect.add(paramDef);
      paramDefVect.add(paramDef2);
//       HashMap hs = new HashMap();
//       hs.put("1",new Integer(1));
//       hs.put("2",new Integer(2));

      Logger.log("ProcedureDefManagerBeanTest [testCreateProcedureDefFile] Before Calling");
      //Object uid=_procedureDefManagerObj.execute(hs,uproc);
      Object uid=_procedureDefManagerObj.execute(paramDefVect,uproc);
      Logger.log("ProcedureDefManagerBeanTest [testCreateProcedureDefFile] After Calling");
      Logger.log("ProcedureDefManagerBeanTest [testCreateProcedureDefFile] UID IS "+uid);
    }
    catch(Exception ex)
    {
      Logger.log("ProcedureDefManagerBeanTest [testCreateProcedureDefFile]"+
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }


  }


/*
  public void testCreateProcedureDef()
  {
    try
    {
      Logger.log(" ProcedureDefManagerBeanTest [testCreateProcedureDef] Enter");
      createtestDataProcedureDefinition();
      Logger.log("ProcedureDefManagerBeanTest [testCreateProcedureDef] Before Calling");
      Long uid=_procedureDefManagerObj.createProcedureDefinition(procedureDef);
      Logger.log("ProcedureDefManagerBeanTest [testCreateProcedureDef] After Calling");
      Logger.log("ProcedureDefManagerBeanTest [testCreateProcedureDef] UID IS "+uid);
    }
    catch(Exception ex)
    {
      Logger.log("ProcedureDefManagerBeanTest [testCreateProcedureDef]"+
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }



  public void testgetProcedureDefinition()
  {
    try
    {
      Logger.log(" ProcedureDefManagerBeanTest [test getProcedureDefinition] Enter");
      ProcedureDef procedureDef = _procedureDefManagerObj.getProcedureDefinition(new Long(4));
      Logger.log("ProcedureDefManagerBeanTest [test getProcedureDef] After Calling");
      Logger.log("ProcedureDefManagerBeanTest [test getProcedureDef] Name IS "+
      procedureDef.getName());
    }
    catch(Exception ex)
    {
      Logger.log("ProcedureDefManagerBeanTest [testgetProcedureDef]"+
      "in Exception "+ex.getMessage());
      ex.printStackTrace();

    }

  }



  public void testProcedureDefinitionByFilter()
  {
    try
    {
      Logger.log(" ProcedureDefManagerBeanTest [test getProcedureDefinitionByFilter] Enter");
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,ProcedureDef.PROC_TYPE,filter.getEqualOperator(),
      new Integer(1),false);
      Collection cols = _procedureDefManagerObj.getProcedureDefinition(filter);
      if(cols != null)
      {

        Object object[] = cols.toArray();
        for(int i=0;i<object.length;i++)
        {
          ProcedureDef procDef = (ProcedureDef)object[i];
          Logger.log(" ProcedureDefManagerBeanTest [test getProcedureDefinitionByFilter]"+
          "ProcedureName"+procDef.getName());
          Logger.log(" ProcedureDefManagerBeanTest [test getProcedureDefinitionByFilter]"+
          "procedure Definiton File "+procDef.getProcedureDefFile().getFilePath());

        }
      }
      Logger.log("ProcedureDefManagerBeanTest [test getProcedureDefinitionByFilter] After Calling");
    }
    catch(Exception ex)
    {
      Logger.log("ProcedureDefManagerBeanTest [getProcedureDefinitionByFilter]"+
      "in Exception "+ex.getMessage());
      ex.printStackTrace();

    }

  }



  public void testDeletProcedureDefinition()
  {
    try
    {
      Logger.log(" ProcedureDefManagerBeanTest [testDeletProcedureDefinition] Enter");
      _procedureDefManagerObj.deleteProcedureDefinition(new Long(3));
      Logger.log("ProcedureDefManagerBeanTest [testDeletProcedureDefinition] After Calling");
    }
    catch(Exception ex)
    {
      Logger.log("ProcedureDefManagerBeanTest [testDeletProcedureDefinition]"+
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }




  public void testDeletProcedureDefinitionFile()
  {
    try
    {
      Logger.log(" ProcedureDefManagerBeanTest [testDeletProcedureDefinitionFile] Enter");
      _procedureDefManagerObj.deleteProcedureDefinitionFile(new Long(3));
      Logger.log("ProcedureDefManagerBeanTest [testDeletProcedureDefinitionFile] After Calling");
    }
    catch(Exception ex)
    {
      Logger.log("ProcedureDefManagerBeanTest [testDeletProcedureDefinitionFile]"+
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }



  public void testupdateProcedureDefinition()
  {
    try
    {
      Logger.log(" ProcedureDefManagerBeanTest [testupdateProcedureDefinition] Enter");
      createtestDataProcedureDefinition();
      procedureDef.setUId(4);
      _procedureDefManagerObj.updateProcedureDefinition(procedureDef);
      Logger.log("ProcedureDefManagerBeanTest [testupdateProcedureDefinition] After Calling");
    }
    catch(Exception ex)
    {
      Logger.log("ProcedureDefManagerBeanTest [testDeletProcedureDefinitionFile]"+
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }

  }



  public void testupdateProcedureDefinitionFile()
  {
    try
    {
      Logger.log(" ProcedureDefManagerBeanTest [testupdateProcedureDefinitionFile] Enter");
      createtestDataProcedureDefFile();
      procedureDefFile.setUId(4);
      procedureDefFile.setDescription("Updated Description ");
      _procedureDefManagerObj.updateProcedureDefinitionFile(procedureDefFile);
      Logger.log("ProcedureDefManagerBeanTest [testupdateProcedureDefinitionFile] After Calling");
    }
    catch(Exception ex)
    {
      Logger.log("ProcedureDefManagerBeanTest [testDeletProcedureDefinitionFile]"+
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }
  }



  public void  testgetProcedureDefinitionFileByFilter()
  {
   try
   {
      Logger.log(" ProcedureDefManagerBeanTest [testgetProcedureDefinitionFileByFilter] Enter");
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,procedureDefFile.FILE_NAME,filter.getEqualOperator(),
      new String("MyFileName"),false);
      Collection col = _procedureDefManagerObj.getProcedureDefinitionFile(filter);
      if(col != null)
      {
          Iterator ite = col.iterator();
          while(ite.hasNext())
          {
            ProcedureDefFile procDefFile = (ProcedureDefFile)ite.next();
            Logger.log("ProcedureDefManagerBeanTest [testgetProcedureDefinitionFileByFilter]"+
            "Description "+procDefFile.getDescription()+"File Path "+procDefFile.getFilePath());
          }
      }

      Logger.log("ProcedureDefManagerBeanTest [testgetProcedureDefinitionFileByFilter] After Calling");
   }
   catch(Exception ex)
   {
      Logger.log("ProcedureDefManagerBeanTest [getProcedureDefinitionFileByFilter]"+
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
   }

  }



  public void createtestDataProcedureDefinition()
  {
    try
    {
      Vector v1 = new Vector();
      v1.add(new String("test"));
      procedureDef = new ProcedureDef();
      procedureDef.setName("Name");
      procedureDef.setDescription("Description");
      procedureDef.setIsSynchronous(false);
      procedureDef.setProcedureDefAction(1);
      procedureDef.setProcedureDefAlert(new Long(1));
      procedureDef.setProcedureParamList(v1);
      procedureDef.setProcedureReturnList(v1);
      procedureDef.setProcedureType(1);
      procedureDef.setReturnDataType(1);
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,procedureDefFile.FILE_NAME,filter.getEqualOperator(),
      new String("MyFileName"),false);

      Collection procdeffiles = _procedureDefManagerObj.getProcedureDefinitionFile(
      filter);
      ProcedureDefFile procDefFile=null;
      if(procdeffiles != null)
      {
          Iterator ite = procdeffiles.iterator();
          while(ite.hasNext())
          {
            procDefFile = (ProcedureDefFile)ite.next();
            Logger.log("ProcedureDefManagerBeanTest [createtestDataProcedureDefinition]"+
            "Description "+procDefFile.getDescription()+"File Path "+procDefFile.getUId());
            break;
          }
      }

      procedureDef.setProcedureDefFile(procDefFile);
      HashMap hm = new HashMap();
      hm.put("first","First");
      procedureDef.setProcedureDefDetail(hm);
    }
    catch(Exception ex)
    {
      Logger.log("ProcedureDefManagerBeanTest [createProcedureDefTestData]"+
      "in Exception "+ex.getMessage());
      ex.printStackTrace();
    }


  }


*/

  public void createtestDataProcedureDefFile()
  {
    procedureDefFile = new ProcedureDefFile();
    procedureDefFile.setName("File Name");
    procedureDefFile.setDescription("Description ");
    procedureDefFile.setFileName("TestLoader.class");
    procedureDefFile.setFilePath(IProcedureDefFilePathConfig.PATH_CLASSES);
  }


}