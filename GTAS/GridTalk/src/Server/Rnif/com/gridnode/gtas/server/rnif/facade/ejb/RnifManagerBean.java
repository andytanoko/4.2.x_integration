package com.gridnode.gtas.server.rnif.facade.ejb;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.act.FailureNotificationAction;
import com.gridnode.gtas.server.rnif.act.FailureNotificationAction_11;
import com.gridnode.gtas.server.rnif.entities.ejb.IRNProfileLocalObj;
import com.gridnode.gtas.server.rnif.helpers.AlertUtil;
import com.gridnode.gtas.server.rnif.helpers.DocumentUtil;
import com.gridnode.gtas.server.rnif.helpers.IRnifConstant;
import com.gridnode.gtas.server.rnif.helpers.Logger;
import com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper;
import com.gridnode.gtas.server.rnif.helpers.ProcessUtil;
import com.gridnode.gtas.server.rnif.helpers.ProfileUtil;
import com.gridnode.gtas.server.rnif.helpers.RNProfileEntityHandler;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

public class RnifManagerBean implements SessionBean, IRnifManager
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1185851403658611199L;
	transient private SessionContext _sessionCtx= null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx= sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  /**
   * Create a new RNProfile.
   *
   * @param def The RNProfile entity.
   * @return The UID of the created RNProfile.
   */
  public RNProfile createRNProfile(RNProfile profile) throws CreateEntityException, SystemException
  {
    FacadeLogger logger= Logger.getRnifFacadeLogger();
    String methodName= "createRNProfile";
    Object[] params= new Object[] { profile };
    try
    {
      logger.logEntry(methodName, params);
      RNProfile created= (RNProfile) getEntityHandler().createEntity(profile);
      return created;
    }
    catch (CreateException ex)
    {
      logger.logCreateError(methodName, params, ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      logger.logCreateError(methodName, params, ex);
      throw ex;
    }
    catch (ApplicationException ex)
    {
      logger.logCreateError(methodName, params, ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      logger.logCreateError(methodName, params, ex);
      throw new SystemException("RnifManagerBean.createRNProfile(RNProfile) Error ", ex);
    }
    finally
    {
      Logger.log("[RnifManagerBean.createRNProfile] Exit");
    }
  }

  /**
   * Update a RNProfile.
   *
   * @param bizEntity The RNProfile entity with changes.
   */
  public void updateRNProfile(RNProfile profile) throws UpdateEntityException, SystemException
  {
    FacadeLogger logger= Logger.getRnifFacadeLogger();
    String methodName= "updateRNProfile";
    Object[] params= new Object[] { profile };

    try
    {
      logger.logEntry(methodName, params);
      getEntityHandler().update(profile);
    }
    catch (EntityModifiedException ex)
    {
      logger.logUpdateError(methodName, params, ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (ApplicationException ex)
    {
      logger.logUpdateError(methodName, params, ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
      throw new SystemException("RnifManagerBean.updateRNProfile(RNProfile) Error ", ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
  * Delete a RNProfile.
  *
  * @param defUId The UID of the RNProfile to delete.
  */
  public void deleteRNProfile(Long defUId) throws DeleteEntityException, SystemException
  {
    FacadeLogger logger= Logger.getRnifFacadeLogger();
    String methodName= "deleteRNProfile";
    Object[] params= new Object[] { defUId };

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().remove(defUId);
    }
    catch (Throwable ex)
    {
      logger.logDeleteError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  // ********************** Finders ******************************************

  /**
   * Find a RNProfile using the RNProfile UID.
   *
   * @param uID The UID of the RNProfile to find.
   * @return The RNProfile found
   * @exception FindRNProfileException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public RNProfile findRNProfile(Long uID) throws FindEntityException, SystemException
  {
    FacadeLogger logger= Logger.getRnifFacadeLogger();
    String methodName= "findRNProfile";
    Object[] params= new Object[] { uID };

    RNProfile profile= null;

    try
    {
      logger.logEntry(methodName, params);

      IRNProfileLocalObj profileObj= (IRNProfileLocalObj) getEntityHandler().findByPrimaryKey(uID);
      if(profileObj != null)
        profile = (RNProfile)profileObj.getData();
      else
        profile = null;
    }
    catch (ApplicationException ex)
    {
      logger.logFinderError(methodName, params, ex);
      return null;
    }
    catch (SystemException ex)
    {
      logger.logFinderError(methodName, params, ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
      throw new SystemException("RnifManagerBean.findRNProfile(UID) Error ", ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return profile;
  }

  /**
   * Find a number of RNProfiles that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of RNProfiles found, or empty collection if none
   * exists.
   */

  public Collection findRNProfiles(IDataFilter filter) throws FindEntityException, SystemException
  {
    FacadeLogger logger= Logger.getRnifFacadeLogger();
    String methodName= "findRNProfiles";
    Object[] params= new Object[] { filter };

    Collection profiles= new ArrayList();

    try
    {
      logger.logEntry(methodName, params);
      Collection profileObjColl = getEntityHandler().findByFilter(filter);
      for (Iterator iter= profileObjColl.iterator(); iter.hasNext();)
      {
        IRNProfileLocalObj defObj= (IRNProfileLocalObj) iter.next();
        profiles.add(defObj.getData());
      }
    }
    catch (ApplicationException ex)
    {
      logger.logFinderError(methodName, params, ex);
      return profiles;
    }
    catch (SystemException ex)
    {
      logger.logFinderError(methodName, params, ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
      throw new SystemException("RnifManagerBean.findRNProfiles(UID) filter ", ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
    return profiles;
  }
  
    /**
   * Find the keys of the RNProfiles that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of RNProfiles found, or empty
   * collection if none.
   * @excetpion FindEntityException Error in executing the finder.
   */
  public Collection findRNProfilesKeys(IDataFilter filter) throws FindEntityException, SystemException
  {
    Collection profileKeys = null;
    try
    {
      Logger.log("[RnifManagerBean.findRNProfilesKeys] filter: " + filter.getFilterExpr());

      profileKeys = getEntityHandler().getKeyByFilterForReadOnly(filter);
    } catch (ApplicationException ex)
    {
      Logger.warn("[RnifManagerBean.findRNProfilesKeys] BL Exception", ex);
      return new ArrayList();
    } catch (SystemException ex)
    {
      Logger.warn("[RnifManagerBean.findRNProfilesKeys] System Exception", ex);
      throw ex;
    } catch (Throwable ex)
    {
      Logger.warn("[RnifManagerBean.findRNProfilesKeys] Error ", ex);
      throw new SystemException("RnifManagerBean.findRNProfilesKeys(filter) Error ", ex);
    }

    return profileKeys;
  }

  

  private RNProfileEntityHandler getEntityHandler()
  {
    return RNProfileEntityHandler.getInstance();
  }


  public Object invokeMethod(Object obj, String className,  String methodName, Class[] paramTypes, Object[] params) throws Exception
  {
     Class aclass = Class.forName(className);
     try
     {
     if(obj == null)
      obj = aclass.newInstance();
     }catch(Throwable ex)
     {
     	Logger.warn("[RnifManagerBean.invokeMethod] Error in new Object of class " + className, ex);
     }
     Method methodInvoked = aclass.getMethod(methodName, paramTypes);
    Object result = methodInvoked.invoke(obj, params);
    return result;
  }

  //cancel the process
//TWX 27012006 start
  public void cancelProcess(Long processInstanceUID, String reason, String userid)
    throws Exception
  {
    Logger.debug("[RnifManagerBean.cancelProcess] cancelling process "+processInstanceUID);
      GWFRtProcessDoc rtProcessDoc = getRtProcessDoc(processInstanceUID);
      
      if (rtProcessDoc == null)
      {
        Logger.log("[RnifManagerBean.cancelProcess] No process document found for processUID "+processInstanceUID+"!");
        return;
      }
      
      if(GWFRtProcess.CLOSED_ABNORMALCOMPLETED != rtProcessDoc.getStatus() &&
         GWFRtProcess.CLOSED_ABNORMALCOMPLETED_ABORTED != rtProcessDoc.getStatus() &&
         GWFRtProcess.CLOSED_ABNORMALCOMPLETED_TERMINATED != rtProcessDoc.getStatus())
      {
        ProcessInstanceActionHelper.cancelProcessInstance(processInstanceUID,
                                                          reason);
        
        RNProfile requestProfile = getIsRequestRNProfile(rtProcessDoc);
        GridDocument originalDoc = getFirstRequestGDoc(requestProfile);
        if (originalDoc != null)
        {
          raiseProcessInstanceFailureAlert(originalDoc, reason); //raise alert for cancelled process
          
          initiate0A1(originalDoc, requestProfile.getProcessOriginatorId(), reason, userid);
        }
      }
  }
  
  private GWFRtProcessDoc getRtProcessDoc(Long processInstanceUID)
    throws Exception
  {
    Collection result = ProcessInstanceActionHelper.getWorkflowMgr().getRtProcessDocList(processInstanceUID);
    GWFRtProcessDoc rtProcessDoc = null;
    if(result!= null && result.size() > 0)
    {
      rtProcessDoc = (GWFRtProcessDoc)result.iterator().next();
    }
    return rtProcessDoc;
  }
  
  private RNProfile getIsRequestRNProfile(GWFRtProcessDoc rtProcessDoc)
    throws Exception
  {
    ProfileUtil proUtil = new ProfileUtil();
  
    String processDef = getProcessDef(rtProcessDoc.getRequestDocType());
    String processInstanceID = getProcessInstanceID(rtProcessDoc.getDocumentId());
    String partnerKey = getOriginatorID(rtProcessDoc.getDocumentId());

    return proUtil.getProfileFromProcessInstanceId(processDef, processInstanceID, partnerKey);
  }
  
  private String getProcessDef(String docType)
  {
    String defName = "";
    if (docType != null)
    {
      int index= docType.lastIndexOf('_');
      defName= docType.substring(0, index);
      
      //strip the first "_" (due to the Bpss dtd not allowed process def start with number, so we always append "_" on it)
      if (defName.charAt(0) == '_')
      {
        defName = defName.substring(1);
      }
    }
    return defName;
  }
  
  private String getProcessInstanceID(String documentID)
  {
    return documentID.substring(0, documentID.lastIndexOf("/"));
  }
  
  private String getOriginatorID(String documentID)
  {
    return documentID.substring(documentID.lastIndexOf("/")+1, documentID.length());
  }
  
  private GridDocument getFirstRequestGDoc(RNProfile requestProfile)
    throws Exception
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, GridDocument.RN_PROFILE_UID, 
                         filter.getEqualOperator(), requestProfile.getKey(), false);
    ArrayList<String> folders = new ArrayList<String>();
    folders.add(GridDocument.FOLDER_INBOUND);
    folders.add(GridDocument.FOLDER_OUTBOUND);
    filter.addDomainFilter(filter.getAndConnector(), GridDocument.FOLDER, folders, false);
  
    Collection result = DocumentUtil.getDocumentManager().findGridDocuments(filter);
    if(result != null && result.size() > 0)
    {
      return (GridDocument)result.iterator().next();
    }
    return null;
  }
  
/**
   * This method is referred from 
   * SendFailureNotificationAction.sendFailureNotification(GridDocument, String)
   * @param originalDoc
   * @param originalID
   * @param reason
   */
  private void initiate0A1(GridDocument originalGDoc, String originalID, String reason, String userid)
    throws Exception
  {
    String defName = originalGDoc.getProcessDefId();
    if(defName == null || defName.length() == 0)
    {
      //not RN doc, no NoF required - precaution check
      Logger.log("[RnifManagerBean.initiate0A1] Not a process-linked document. Weird finding...");
      return;
    }
    
    ProcessDef def = ProcessUtil.getProcessDef(defName); //original process def
    String rnifVersion = def.getRNIFVersion();
    if(IRnifConstant.RN_FAILNOTIFY_DEFNAME20.equals(defName) ||
        IRnifConstant.RN_FAILNOTIFY_DEFNAME11.equals(defName))
    {
      //do not send the NoF for NoF - precaution check
      Logger.log("[RnifManagerBean.initiate0A1] Fail for a NoF, not required to send NoF for NoF.");
    }
    else
    {
      Logger.log("[RnifManagerBean.initiate0A1] Sending NoF for original doc uid="+originalGDoc.getUId());
      Object[] params = {reason, isInitiatedBySELF(originalID), userid};
      if ((rnifVersion != null)&&(rnifVersion.equals(ProcessDef.RNIF_1_1)))
      {
        def = ProcessUtil.getProcessDef(IRnifConstant.RN_FAILNOTIFY_DEFNAME11);
        FailureNotificationAction_11 action = new FailureNotificationAction_11();
        action.execute(originalGDoc, true, def, def.getRequestAct(), params);
      }
      else
      {
        def = ProcessUtil.getProcessDef(IRnifConstant.RN_FAILNOTIFY_DEFNAME20);
        FailureNotificationAction action = new FailureNotificationAction();
        action.execute(originalGDoc, true, def, def.getRequestAct(), params);
      }
      
    }
  }
  
  private void raiseProcessInstanceFailureAlert(GridDocument originalDoc, String reason)
  {
    AlertUtil.alertUserCancelledPip(originalDoc, reason);
  }
  
  private Boolean isInitiatedBySELF(String originalID)
  {
    return "SELF".equals(originalID);
  }
  //end cancel
}