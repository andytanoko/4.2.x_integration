package com.gridnode.pdip.base.worklist.manager.ejb;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */

//import com.gridnode.pdip.base.gwfbase.baseentity.*;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.ObjectNotFoundException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.base.worklist.IWorklistConfig;
import com.gridnode.pdip.base.worklist.entities.ejb.GWFWorkListEntityObject;
import com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity;
import com.gridnode.pdip.base.worklist.exception.WLAssignmentException;
import com.gridnode.pdip.base.worklist.exception.WorklistException;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.jms.JMSSender;
import com.gridnode.pdip.framework.util.UtilEntity;

/**
* GWFWorkListSessionBean is a WorkList Data Access Controller Bean.
* It is essentially responsible to  act as WorkListManager, which
* performs the basic services such as,
*       1.  Insert the WorkItems.
*       2.  Reject WorkItems.
*       3.  Assign WorkItems.
* The core business logic for tasks's such as assignWorkItems() - are subject
* to change respective to the version.
*/



public class GWFWorkListSessionBean implements SessionBean{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8568545006549166020L;
		private SessionContext ctx;

    public void ejbCreate(){
    }

    public void ejbRemove(){
    }

    public void ejbActivate(){
    }

    public void ejbPassivate(){
    }

    public void setSessionContext(SessionContext ctx){
    }

    /**
     * Add's a WorkItem with the values in GWFWorkListValueEntity value object.
     * @param entity GWFWorkListValueEntity value object.
     */
    public void addWorkItem(GWFWorkListValueEntity entity) throws SystemException{
        try{
          getHandlerFor(GWFWorkListValueEntity.ENTITY_NAME).create(entity);
        }catch(Throwable th){
            System.out.println("GWFWorkListSessionBean : addWorkItem "+th.getMessage());
            throw new SystemException("[GWFWorkListSessionBean.addWorkItem] Error ",th);
        }
    }

    /**
     * Drop's a WorkItem with workItemUId .
     * @param entity GWFWorkListValueEntity value object.
     */
    public void dropWorkItem(Long workItemUId) throws WorklistException,SystemException{
        try{
          getHandlerFor(GWFWorkListValueEntity.ENTITY_NAME).remove(workItemUId);
        }catch(ObjectNotFoundException ex){
            throw new WorklistException("[GWFWorkListSessionBean.dropWorkItem] GWFWorkListValueEntity does not exist workItemUId="+workItemUId,ex);
        }catch(Throwable th){
            System.out.println("GWFWorkListSessionBean : dropWorkItem "+th.getMessage());
            throw new SystemException("[GWFWorkListSessionBean.dropWorkItem] Error ",th);
        }
    }

    /**
     * Drop's a all WorkItems matching UID in Collection .
     * @param entity GWFWorkListValueEntity value object.
     */
    public void dropWorkItems(Collection workItemUIdColl) throws SystemException {
        try{
            IDataFilter filter = new DataFilterImpl();
            filter.addDomainFilter(null,GWFWorkListValueEntity.UID,workItemUIdColl,false);
            UtilEntity.remove(filter,GWFWorkListValueEntity.ENTITY_NAME,true);
        }catch(Throwable th){
            System.out.println("GWFWorkListSessionBean : dropWorkItems "+th.getMessage());
            throw new SystemException("[GWFWorkListSessionBean.dropWorkItems] Error ",th);
        }
    }


    /**
     * Performs's a WorkItem with workItemUId .
     * This basically represents a user executing the WorkItem.
     * @param entity GWFWorkListValueEntity value object.
     */
    public void performWorkItem(Long workItemUId) throws WorklistException,SystemException {
        try{
          GWFWorkListEntityObject worklistObj=(GWFWorkListEntityObject)getHandlerFor(GWFWorkListValueEntity.ENTITY_NAME).findByPrimaryKey(workItemUId);
          GWFWorkListValueEntity entity=(GWFWorkListValueEntity)worklistObj.getData();

          HashMap paramMap=new HashMap(1);
          paramMap.put("rtActivityUId",entity.getRtActivityUId());
          paramMap.put("ProcessDefKey",entity.getProcessDefKey());
          sendMessageToTopic(paramMap);
          System.out.println("From performWorkItem:rtActivityUId="+entity.getRtActivityUId());
          worklistObj.remove();
        }catch(WorklistException ex){
            throw ex;
        }catch(ObjectNotFoundException ex){
            throw new WorklistException("[GWFWorkListSessionBean.performWorkItem] GWFWorkListValueEntity does not exist workItemUId="+workItemUId,ex);
        }catch(Throwable th){
          System.out.println("performWorkItem() "+th.getMessage());
          throw new SystemException("[GWFWorkListSessionBean.performWorkItem] Error ",th);
        }
    }

    /**
     * Assigns WorkItem with workItemUId to the specified user
     * @param workItemUId
     * @param user
     * @throws SystemException
     */
    public void assignWorkItem(Long workItemUId,String user) throws WorklistException,WLAssignmentException,SystemException {
        try{
          GWFWorkListEntityObject worklistObj=(GWFWorkListEntityObject)getHandlerFor(GWFWorkListValueEntity.ENTITY_NAME).findByPrimaryKey(workItemUId);
          GWFWorkListValueEntity entity=(GWFWorkListValueEntity)worklistObj.getData();
          if(entity.getUserId()==null || entity.getUserId().trim().length()==0){
              entity.setUserId(user);
              worklistObj.setData(entity);
          } else throw new WLAssignmentException("[GWFWorkListSessionBean.assignWorkItem] WorkItem already assined to "+entity.getUserId());
        }catch(ObjectNotFoundException ex){
            throw new WorklistException("[GWFWorkListSessionBean.assignWorkItem] GWFWorkListValueEntity does not exist workItemUId="+workItemUId,ex);
        }catch(WLAssignmentException ex){
            throw ex;
        }catch(Throwable th){
          System.out.println("assignWorkItem "+th.getMessage());
          throw new SystemException("[GWFWorkListSessionBean.assignWorkItem] Error ",th);
        }
    }

    /**
     * Reject's a WorkItem for workItemUId. A workitem is disassociated
     * with the any user when the reject method is called. The user who rejected this
     * workitem., the information is stored in GWFWorkListUserEntity.
     * @param entity GWFWorkListValueEntity value object.
     */
    public void rejectWorkItem(Long workItemUId,String comments) throws WorklistException,WLAssignmentException,SystemException {
        try{
          GWFWorkListEntityObject worklistObj=(GWFWorkListEntityObject)getHandlerFor(GWFWorkListValueEntity.ENTITY_NAME).findByPrimaryKey(workItemUId);
          GWFWorkListValueEntity valueEntity=(GWFWorkListValueEntity)worklistObj.getData();
          if(valueEntity.getUserId()==null || valueEntity.getUserId().trim().length()==0)
            throw new WLAssignmentException("[GWFWorkListSessionBean.rejectWorkItem] Cannot reject unassigned WorkItem");
          //GWFWorkListUserEntity userentity = new GWFWorkListUserEntity();
          //userentity.setWorkItemID((Long)valueEntity.getKey());
          //userentity.setUserID(valueEntity.getUserId());
          //UtilEntity.create(userentity,true);

          valueEntity.setWorkItemComments(comments);
          valueEntity.setUserId(null);
          UtilEntity.update(valueEntity,true);
        }catch(ObjectNotFoundException ex){
            throw new WorklistException("[GWFWorkListSessionBean.rejectWorkItem] GWFWorkListValueEntity does not exist workItemUId="+workItemUId,ex);
        }catch(WLAssignmentException ex){
            throw ex;
        }catch(Throwable th){
          System.out.println("GWFWorkListSessionBean : rejectWorkItem "+th.getMessage());
          throw new SystemException("[GWFWorkListSessionBean.rejectWorkItem] Error ",th);
        }
    }

    /**
     * Returns GWFWorkListValueEntity with primary key workItemUId
     * @param workItemUId Long Primary Key
     * @return GWFWorkListValueEntity
     * @throws SystemException
     */
    public GWFWorkListValueEntity getWorkItem(Long workItemUId) throws WorklistException,SystemException{
        try{
            return (GWFWorkListValueEntity)UtilEntity.getEntityByKey(workItemUId,GWFWorkListValueEntity.ENTITY_NAME,true);
        }catch(ObjectNotFoundException ex){
            throw new WorklistException("[GWFWorkListSessionBean.getWorkItem] GWFWorkListValueEntity does not exist workItemUId="+workItemUId,ex);
        }catch(Throwable th){
            throw new SystemException("[GWFWorkListSessionBean.getWorkItem] Exception ",th);
        }
    }

    /**
     * Return a Collection of GWFWorkListValueEntity value objects mataching the user criteria.
     * @param user String User Name.
     * @return Collection of GWFWorkListValueEntity.
     */
    public Collection getWorkItemsByUser(String userName) throws SystemException {
        IDataFilter filter=new DataFilterImpl();
        filter.addSingleFilter(null,GWFWorkListValueEntity.USER_ID,filter.getEqualOperator(),userName,false);
        return getWorkItemsByFilter(filter);
    }


    /**
     * Return a Collection of GWFWorkListValueEntity value objects mataching the processDefKey and  activityId criteria.
     * @param processDefKey String Process Defination Key ,format:- http://enginetype/packageId/packageVersion/processType/processName .
     * @param activityId String Activity ID.
     * @return Collection of GWFWorkListValueEntity.
     */
    public Collection getWorkItemsByActivity(String processDefKey,String activityId) throws SystemException {
        IDataFilter filter=new DataFilterImpl();
        filter.addSingleFilter(null,GWFWorkListValueEntity.PROCESSDEF_KEY,filter.getEqualOperator(),processDefKey,false);
        filter.addSingleFilter(filter.getAndConnector(),GWFWorkListValueEntity.ACTIVITY_ID,filter.getEqualOperator(),activityId,false);
        return getWorkItemsByFilter(filter);
    }

    /**
     * Return a Collection of GWFWorkListValueEntity value objects mataching the performer criteria.
     * @param performer String Performer.
     * @return Collection of GWFWorkListValueEntity.
     */
    public Collection getWorkItemsByPerformer(String performer) throws SystemException {
        IDataFilter filter=new DataFilterImpl();
        filter.addSingleFilter(null,GWFWorkListValueEntity.PERFORMER,filter.getEqualOperator(),performer,false);
        return getWorkItemsByFilter(filter);
    }

    /**
     * Return a Collection of GWFWorkListValueEntity value objects mataching the rtActivityUId criteria.
     * @param rtActivityUId String Runtime activity UID.
     * @return Collection of GWFWorkListValueEntity.
     */
    public Collection getWorkItemsByRtActivity(Long rtActivityUId) throws SystemException {
        IDataFilter filter=new DataFilterImpl();
        filter.addSingleFilter(null,GWFWorkListValueEntity.RTACTIVITY_UID,filter.getEqualOperator(),rtActivityUId,false);
        return getWorkItemsByFilter(filter);
    }

    /**
     * Returns a Collection of GWFWorkListValueEntity objects matching the filter criteria.
     * @param filter IDataFilter criteria.
     * @return Collection of GWFWorkListValueEntity objects matching the filter criteria.
     */
    public Collection getWorkItemsByFilter(IDataFilter filter) throws SystemException {
        try{
            return UtilEntity.getEntityByFilter(filter,GWFWorkListValueEntity.ENTITY_NAME,true);
        }catch(Throwable th){
            throw new SystemException("[GWFWorkListSessionBean.getWorkItemsByFilter] Exception ",th);
        }
    }

    /**
     * Sends a Message to Topic
     * @param paramMap HashMap of parameters
     * @throws Exception
     */
    public void sendMessageToTopic(HashMap paramMap) throws Exception {
        try {
            Configuration config = ConfigurationManager.getInstance().getConfig(IWorklistConfig.WORKLIST_CONFIG);
            String performWorkitemTopic=config.getString(IWorklistConfig.WORKLIST_PERFORMWORKITEM_DEST);

            if(performWorkitemTopic!=null )
            {
              JMSSender.sendMessageToTopic(IFrameworkConfig.FRAMEWORK_JMS_CONFIG,performWorkitemTopic, paramMap);
            } 
            else 
              System.out.println("[GWFWorkListSessionBean.sendMessageToTopic] performWorkitemTopic="+performWorkitemTopic);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } 
    }

  private AbstractEntityHandler getHandlerFor(String entityName)
  {
   return EntityHandlerFactory.getHandlerFor(entityName, true);
  }

}
