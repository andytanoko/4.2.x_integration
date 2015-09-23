package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.model.rnif.IProcessInstance;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerObj;
import com.gridnode.gtas.server.rnif.helpers.util.IRnifTestConstants;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerHome;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerObj;
import com.gridnode.pdip.app.workflow.runtime.model.IGWFRtProcess;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
public class ProcessInstanceActionHelperTest extends RnifTestCase implements IProcessInstance
{

  protected static IGWFWorkflowManagerHome _gwfMgrHome= null;
  protected static IGWFWorkflowManagerObj _gwfMgr= null;
  
  public ProcessInstanceActionHelperTest(String arg0)
  {
    super(arg0);
  }

  public static IGWFWorkflowManagerObj getWorkflowMgr() throws ServiceLookupException
  {
    return (IGWFWorkflowManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IGWFWorkflowManagerHome.class.getName(),
      IGWFWorkflowManagerHome.class,
      new Object[0]);
  }

  //  public void testGetProcessInstancces() throws Throwable
  //  {
  //    try
  //    {
  //      Logger.debug("testGetProcessInstancces Enter");
  //      
  //      _rnifMgr = _rnifMgrHome.create();
  // 
  //      IDataFilter filter = EntityUtil.getEqualFilter(new Number[]{IGWFRtProcess.ENGINE_TYPE}, new Object[]{"BPSS"} );
  //      Collection instlist = (Collection)_rnifMgr.invokeMethod(null, "com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper", "findProcessInstances", 
  //       new Class[]{IDataFilter.class}, new Object[]{filter});
  //      if(instlist == null || instlist.isEmpty())
  //        Logger.log("testGetProcessInstancces find zero record, return");
  //      
  //      Iterator iter = instlist.iterator();
  //      String str = "";
  //      for(;iter.hasNext();)
  //      {
  //        Map map = (Map)iter.next();
  //        str +=instMap2Str(map) ;
  //      }
  //      Logger.log("testGetProcessInstancces result is " + str);
  //      
  ////      Object res = _rnifMgr.invokeMethod(null, "com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelperTest", "getProcessInstance", 
  ////       new Class[]{ProcessDef.class}, new Object[]{def});
  // //     ActionHelper.createProcessDef(def);
  //    }
  //    catch (Throwable ex)
  //    {
  //      Logger.err("testGetProcessInstancces", ex);
  //      throw ex;
  //    }
  //    Logger.debug("testGetProcessInstancces Exit");
  //  }

  public void testGetProcess() throws Throwable
  {
    try
    {
      Logger.debug("testGetProcess Enter");

      //    getProcessInstance(new Long(287), _rnifMgr);
      getProcessInstanceListByDefName(_rnifMgr, IRnifTestConstants.DEF_NAME_3A4);
    }
    catch (Throwable ex)
    {
      Logger.err("testGetProcess", ex);
      throw ex;
    }
    Logger.debug("testGetProcess Exit");
  }

  public static void getAllProcessInstanceList(IRnifManagerObj rnifMgr)
  {
    try
    {
      IDataFilter filter=
        EntityUtil.getEqualFilter(
          new Number[] { IGWFRtProcess.ENGINE_TYPE, IGWFRtProcess.PROCESS_TYPE },
          new Object[] { "BPSS", "BpssBinaryCollaboration" });
      //   Collection instlist= UtilEntity.getEntityByFilterForReadOnly(filter, "GWFRtProcess", false);

      //      _gwfMgr= getWorkflowMgr();
      //      Collection instlist= _gwfMgr.getProcessInstanceList(filter);

      //    Collection instlist=
      //      (Collection) rnifMgr.invokeMethod(
      //        null,
      //        "com.gridnode.pdip.framework.util.UtilEntity",
      //        "getEntityByFilterForReadOnly",
      //        new Class[] { IDataFilter.class, String.class, Boolean.class},
      //        new Object[] { filter, "GWFRtProcess", Boolean.TRUE});

      Collection instlist=
        (Collection) rnifMgr.invokeMethod(
          null,
          "com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper",
          "findProcessInstances",
          new Class[] { IDataFilter.class },
          new Object[] { filter });

      if (instlist == null || instlist.isEmpty())
        Logger.log("getAllProcessInstanceList find zero record, return");

      Iterator iter= instlist.iterator();
      String str= "";
      for (; iter.hasNext();)
      {
        Map map= (Map) iter.next();
        str += instMap2Str(map);
      }
      Logger.log("getAllProcessInstanceList result is " + str);
    }
    catch (Throwable ex)
    {
      Logger.err("getAllProcessInstanceList, error occured ", ex);
    }

  }

  public static void getProcessInstance(Long instUid, IRnifManagerObj rnifMgr) throws Exception
  {
    Map map=
      (Map) rnifMgr.invokeMethod(
        null,
        "com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper",
        "getProcessInstance",
        new Class[] { Long.class },
        new Object[] { new Long(287)});
    if (map == null || map.isEmpty())
      Logger.log("getProcessInstance find zero record, return");

    String str= instMap2Str(map);
    Logger.log("getProcessInstance result is " + str);
  }

  public static void getProcessInstanceListByDefName(IRnifManagerObj rnifMgr, String defName)
  {
    try
    {
      Collection instlist=
        (Collection) rnifMgr.invokeMethod(
          null,
          "com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper",
          "findProcessInstances",
          new Class[] { String.class },
          new Object[] { defName });

      if (instlist == null || instlist.isEmpty())
        Logger.log("getProcessInstanceListByDefName find zero record, return");

      Iterator iter= instlist.iterator();
      String str= "";
      for (; iter.hasNext();)
      {
        Map map= (Map) iter.next();
        str += instMap2Str(map);
      }
      Logger.log("getProcessInstanceListByDefName result is " + str);
      
//     Iterator iter= instlist.iterator();
//      String str= "";
//      for (; iter.hasNext();)
//      {
//        Map map= (Map) iter.next();
//        Long uid = map.get(IProcessInstance.UID);
//        rnifMgr.invokeMethod(
//          null,
//          "com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper",
//          "deleteProcessInstance",
//          new Class[] { String.class },
//          new Object[] { defName });
//        
//        
//      }
//      Logger.log("getProcessInstanceListByDefName result is " + str);
    }
    catch (Throwable ex)
    {
      Logger.err("getProcessInstanceListByDefName, error occured ", ex);
    }
  }
  

  static String instMap2Str(Map map)
  {
    String res= "\n";
    res= "UID=" + map.get(UID);
    res += "\nPROCESS_DEF_NAME=" + map.get(PROCESS_DEF_NAME);
    res += "\nROLE_TYPE=" + map.get(ROLE_TYPE);
    res += "\nPARTNER" + map.get(PARTNER);
    res += "\nPROCESS_INSTANCE_ID=" + map.get(PROCESS_INSTANCE_ID);
    res += "\nSTATE=" + map.get(STATE);
    res += "\nSTART_TIME=" + map.get(START_TIME);
    res += "\nEND_TIME=" + map.get(END_TIME);
    res += "\nRETRY_NUM=" + map.get(RETRY_NUM);
    res += "\nIS_FAILED=" + map.get(IS_FAILED);
    res += "\nFAIL_REASON=" + map.get(FAIL_REASON);
    res += "\nDETAIL_REASON=" + map.get(DETAIL_REASON);
    res += "\nASSOC_DOCS=" + map.get(ASSOC_DOCS);
    return res;
  }

}
