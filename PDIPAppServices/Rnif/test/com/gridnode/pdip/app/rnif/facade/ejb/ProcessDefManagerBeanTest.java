package com.gridnode.pdip.app.rnif.facade.ejb;

import com.gridnode.pdip.app.rnif.model.IProcessDef;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.app.rnif.helpers.Logger;
import com.gridnode.pdip.framework.util.ServiceLocator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ProcessDefManagerBeanTest extends TestCase
{
  private IRNProcessDefManagerObj _defMgr;
  private ProcessDef[] _defs;
  private Long[] _uIDs;

  public ProcessDefManagerBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ProcessDefManagerBeanTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Logger.log("[ProcessDefManagerBeanTest.setUp] Enter");

      _defMgr = getProcessDefMgr();
      cleanUp();
    } finally
    {
      Logger.log("[ProcessDefManagerBeanTest.setUp] Exit");
    }
  }

  protected void tearDown() throws java.lang.Exception
  {
    Logger.log("[ProcessDefManagerBeanTest.tearDown] Enter");
    cleanUp();
    Logger.log("ProcessDefManagerBeanTest.tearDown] Exit");
  }

  // ************** Finders ******************************************

  // ******************  utility methods ****************************
  private IRNProcessDefManagerObj getProcessDefMgr() throws Exception
  {
    IRNProcessDefManagerHome processDefHome =
      (IRNProcessDefManagerHome) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(
        IRNProcessDefManagerHome.class.getName(),
        IRNProcessDefManagerHome.class);
    return processDefHome.create();
  }

  private void cleanUp() throws Exception
  {}

  public void testAddRemove() throws Exception
  {
    
    String processname = "MyProcess";
    String messagetype = "msgtype";
    Integer retries = new Integer(4);
    Integer timeout = new Integer(600);
    ProcessDef def = null;
    // Remove the ProcessDef first.
    try
    { 
      Long defUid = _defMgr.findProcessDefKeyByName(processname);
      if(defUid  != null)
         _defMgr.deleteProcessDef(defUid);
    } catch (Exception e)
    {
      Logger.err("[ProcessDefManagerBeanTest.testAddRemove] ", e);
      fail("Error deleting ProcessDef: " + processname);
    }
    try
    {
      def = createSingleActionDef(processname, messagetype, retries, timeout);
    } catch (Exception e)
    {
      Logger.err("[ProcessDefManagerBeanTest.testAddRemove] ", e);
      fail("Cannot create ProcessDef" + e.toString());
    }
    assertNotNull("Cannot create process definition", def);
    // first add
    try
    {
      _defMgr.createProcessDef(def);
    } catch (Exception e)
    {
      Logger.err("[ProcessDefManagerBeanTest.testAddRemove] ", e);
      fail("Cannot add process definitions" + e.toString());
    }
    try
    {
      _defMgr.createProcessDef(def);
      fail("Cannot add duplicate process definitions");
    } catch (Exception e)
    {}
    try
    {     
      Long defUid = _defMgr.findProcessDefKeyByName(processname);
      if(defUid  != null)
         _defMgr.deleteProcessDef(defUid);
    } catch (Exception e)
    {
      Logger.err("[ProcessDefManagerBeanTest.testAddRemove]", e);
      fail("Cannot remove process definition" + e.toString());
    }
    assertNull(_defMgr.findProcessDefByName(processname));
  }

  static ProcessDef createSingleActionDef(String processName, String messageType, Integer retries, Integer timeout)
  {
    ProcessDef def = new ProcessDef();
    def.setFieldValue(IProcessDef.DEF_NAME, processName);
    def.setProcessType(ProcessDef.TYPE_SINGLE_ACTION);
    def.setRNIFVersion(ProcessDef.RNIF_2_0);

    ProcessAct requestAct = new ProcessAct();
    requestAct.setFieldValue(ProcessAct.MSG_TYPE, messageType);
    requestAct.setFieldValue(ProcessAct.IS_AUTHORIZATION_REQUIRED, Boolean.TRUE);
    requestAct.setFieldValue(ProcessAct.IS_NON_REPUDIATION_REQUIRED, Boolean.TRUE);
    requestAct.setFieldValue(ProcessAct.IS_SECURE_TRANSPORT_REQUIRED, Boolean.TRUE);
    requestAct.setFieldValue(ProcessAct.RETRIES, retries);
    requestAct.setFieldValue(ProcessAct.TIME_TO_ACKNOWLEDGE, timeout);

    def.setFieldValue(IProcessDef.REQUEST_ACT, requestAct);
    return def;
  }

//  static ProcessDef createInitiatorDef() 
//  {
//    return createInitiatorDef(PROCESSDEF_NAME, MESSAGE_TYPE, new Integer(3), new Integer(7200));
//  }

  // *********************** Runner *****************************

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

}