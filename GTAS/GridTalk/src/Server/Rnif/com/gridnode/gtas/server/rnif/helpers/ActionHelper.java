package com.gridnode.gtas.server.rnif.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.gridnode.gtas.model.rnif.RnifFieldID;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerHome;
import com.gridnode.pdip.app.rnif.facade.ejb.IRNProcessDefManagerObj;
import com.gridnode.pdip.app.rnif.helpers.BpssGenerator;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerObj;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.base.gwfbase.bpss.model.BpssProcessSpecification;
import com.gridnode.pdip.base.gwfbase.facade.ejb.IGWFBaseManagerHome;
import com.gridnode.pdip.base.gwfbase.facade.ejb.IGWFBaseManagerObj;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class provides helper methods for use in the Action classes
 * of the Rnif module.
 *
 */
public final class ActionHelper
{
  private static final String PROCESS_DEF_PATH_KEY = "rnif.path.processdef";
  
  // ***************** Get Manager Helpers *****************************

  /**
   * Obtain the EJBObject for the ProcessDefManagerBean.
   *
   * @return The EJBObject to the ProcessDefManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IRNProcessDefManagerObj getProcessDefManager()
    throws ServiceLookupException
  {
    return (IRNProcessDefManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IRNProcessDefManagerHome.class.getName(),
      IRNProcessDefManagerHome.class,
      new Object[0]);
  }

  // ********************** Verification Helpers **********************

  /**
   * Verify the existence of a ProcessDef based on the specified uID.
   *
   * @param uID The UID of the ProcessDef.
   * @return The ProcessDef retrieved using the specified uID.
   * @exception ServiceLookupException Error in obtaining the RNProcessDefManagerObj.
   * @exception Exception Bad ProcessDef UID. No ProcessDef exists with the specified
   * uID.
   *
   * @since 2.0
   */
  public static ProcessDef verifyProcessDef(Long uID)
    throws Exception
  {
    try
    {
      return getProcessDefManager().findProcessDef(uID);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Bad ProcessDef UID: "+uID);
    }
  }


  /**
   * Checks if a String value is null or its trimmed length is 0.
   *
   * @param val The String value to check.
   * @return <B>true</B> if the above condition met, <B>false</B> otherwise.
   *
   * @since 2.0
   */
  public static boolean isEmpty(String val)
  {
    return val==null || val.trim().length() == 0;
  }

  // ******************** Conversion Helpers ******************************

  /**
   * Convert a collection of ProcessDefs to Map objects.
   *
   * @param defList The collection of ProcessDefs to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of ProcessDefs.
   *
   * @since 2.0
   */
  public static Collection convertDefsToMapObjects(Collection defList)
  {
    return ProcessDef.convertEntitiesToMap(
             (ProcessDef[])defList.toArray(new ProcessDef[defList.size()]),
             RnifFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an ProcessDef to Map object.
   *
   * @param def The ProcessDef to convert.
   * @return A Map object converted from the specified ProcessDef.
   *
   * @since 2.0
   */
  public static Map convertProcessDefToMap(ProcessDef def)
  {
    return ProcessDef.convertToMap(
             def,
             RnifFieldID.getEntityFieldID(),
             null);
  }

  public static void copyProcessDefFields(Map from, AbstractEntity entity)
  {
    if (from != null)
    {
      for (Iterator i=from.keySet().iterator(); i.hasNext(); )
      {
        Number fieldID = (Number)i.next();
        entity.setFieldValue(fieldID, from.get(fieldID));
      }
    }
  }

  public static void copyProcessActFields(Map from, AbstractEntity entity)
  {
   if (from == null)
      return;

//    Number[] fileFieldIds = new Number[]{ProcessAct.MSG_TYPE, ProcessAct.DICT_FILE,
//         ProcessAct.XML_SCHEMA};
//    for(int i = 0; i< fileFieldIds.length; i++)
//    {
//      Number  fieldId = fileFieldIds[i];
//      Long fileUid = (Long)from.get(fileFieldIds[i]);
//      if(fileUid == null)
//        continue;
//      MappingFile mappingFile =  new MappingFile();
//      mappingFile.setUId(fileUid.longValue());
//      entity.setFieldValue(fieldId, mappingFile);
//      from.remove(fieldId);
//    }

    for (Iterator i=from.keySet().iterator(); i.hasNext(); )
    {
      Number fieldID = (Number)i.next();
      entity.setFieldValue(fieldID, from.get(fieldID));
    }
  }

  public static  Collection findProcessDefKeys(IDataFilter filter) throws java.lang.Exception
  {
    Collection res = getProcessDefManager().findProcessDefsKeys(filter);
    if(res == null)
     res = new ArrayList();
    return res;
  }


  public static  Long createProcessDef(ProcessDef def) throws java.lang.Exception
  {
    IRNProcessDefManagerObj defMgr = getProcessDefManager();
    Long res =  defMgr.createProcessDef(def);
    try
    {
      new BpssGenerator(def).createBPSS();
    }catch(Exception ex)
    {
      Logger.warn("Error occured in deploying the RN process definitaion to Workflow engine!", ex);
      defMgr.deleteProcessDef(res);
      throw ex;
    }

    /*NSL20060503 Factor out to RnifInitialiserMDBean on entity event change
    //update the schema file
    SchemaManager.getInstance().generateSchema(def.getRNIFVersion());
    */
    return res;
  }

  public static  void deleteProcessDef(Long defUid) throws java.lang.Exception
  {
    IRNProcessDefManagerObj defMgr = getProcessDefManager();
    ProcessDef def = defMgr.findProcessDef(defUid);
    defMgr.deleteProcessDef(defUid);
    new BpssGenerator(def).deleteBPSS();

    /*NSL20060503 Factor out to RnifInitialiserMDBean on entity event change
    //regenerate the schema file
    SchemaManager.getInstance().generateSchema(def.getRNIFVersion());
    */
  }

  public static  void updateProcessDef(ProcessDef def) throws java.lang.Exception
  {
    IRNProcessDefManagerObj defMgr = getProcessDefManager();
    ProcessDef oldDef = defMgr.findProcessDef((Long)def.getKey());
    
    //TWX: 7 May 2008 GNDB00028507: We will perform checking on whether there is running
    //                              process that correspond to the process def that will
    //                              be updated.
    //                              As long as the process def is updated, the existing
    //                              process instance that is running will not be able to
    //                              complete successfully.
    if(isProcessDefStrictCheck())
    {
      Logger.log("updateProcessDef isProcessDefStrictCheck enabled");
      
      BpssGenerator gen = new BpssGenerator(oldDef);
      IGWFBaseManagerObj gwfBaseMgr = getGWFBaseManager();
      BpssProcessSpecification processSpec = gwfBaseMgr.getBpssProcessSpec(gen.getUuid(), gen.getVersion());
      if(processSpec != null)
      {
        //Get list of rtprocessdoc that is still running and referencing the processSpec
        Collection rtprocessDocList = getRTProcessDocList((Long)processSpec.getKey());
        if(rtprocessDocList != null && rtprocessDocList.size() > 0)
        { 
          debugRTProcessDoc(rtprocessDocList, def.getDefName());
          throw new ApplicationException("Some existing process instance with \"Running\" state is referencing the Process Def with UUID: "+gen.getUuid()+" version: "+gen.getVersion()+
                                         ". Ensure all process state has complete prior update the ProcessDef");
        }
      }
    }

      new BpssGenerator(oldDef).deleteBPSS();
      defMgr.updateProcessDef(def);
      new BpssGenerator(def).createBPSS();

      /*NSL20060503 Factor out to RnifInitialiserMDBean on entity event change
      //update the schema
      SchemaManager.getInstance().generateSchema(def.getRNIFVersion());
       */
  }
  
  public static IGWFBaseManagerObj getGWFBaseManager()
    throws ServiceLookupException
  {
    return (IGWFBaseManagerObj)ServiceLocator.instance(
                                                            ServiceLocator.CLIENT_CONTEXT).getObj(
                                                                                                  IGWFBaseManagerHome.class.getName(),
                                                                                                  IGWFBaseManagerHome.class,
                                                                                                  new Object[0]);
  }
  
  private static Collection getRTProcessDocList(Long processSpecUID) throws Exception
  {
    IGWFWorkflowManagerObj wfMgr = ProcessInstanceActionHelper.getWorkflowMgr();
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, GWFRtProcessDoc.BINARY_COLLABORATION_UID, filter.getEqualOperator(),processSpecUID, false);
    filter.addSingleFilter(filter.getAndConnector(), GWFRtProcessDoc.STATUS, filter.getEqualOperator(), 1, false);
    
    return wfMgr.getRtProcessDocList(filter);
  }
  
  private static void debugRTProcessDoc(Collection rtprocessDocList, String processDef)
  {
    if(rtprocessDocList != null && rtprocessDocList.size() > 0)
    {
      for(Iterator ite = rtprocessDocList.iterator(); ite.hasNext(); )
      {
        GWFRtProcessDoc rtprocessDoc = (GWFRtProcessDoc)ite.next();
        Logger.log("ProcessInstance with id:"+rtprocessDoc.getDocumentId()+" is referencing the process def:"+processDef+". Its state is still running.");
      }
    }
  }
  
  private static boolean isProcessDefStrictCheck()  
  {
    Properties processDefConfig = loadProcessDefConfig();
    
    if(processDefConfig != null)
    {
      boolean isStrictCheck = new Boolean(processDefConfig.getProperty("is.enable.strict.check", "false")).booleanValue();
      return isStrictCheck;
    }
    return false;
  }
  
  private static Properties loadProcessDefConfig()
  {
    Properties processDefConf = new Properties();
    FileInputStream in = null;
    try
    {
      File processDefConfig = FileUtil.getFile(PROCESS_DEF_PATH_KEY, "processDef.conf");
      
      if(processDefConfig != null)
      {
        in = new FileInputStream(processDefConfig);
        processDefConf.load(in);
        
        return processDefConf;
      }
    }
    catch(Exception ex)
    {
      Logger.warn("Can't locate the processDef configuration file", ex);
    }
    finally
    {
      if(in != null)
      {
        try
        {
          in.close();
        }
        catch(Exception ex)
        {
          
        }
      }
    }
    return processDefConf;
  }
}