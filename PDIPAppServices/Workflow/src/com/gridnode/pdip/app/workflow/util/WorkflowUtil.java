package com.gridnode.pdip.app.workflow.util;

import java.util.*;

import com.gridnode.pdip.app.workflow.engine.IGWFRouteDispatcher;
import com.gridnode.pdip.app.workflow.engine.ejb.IGWFActivityManagerHome;
import com.gridnode.pdip.app.workflow.engine.ejb.IGWFActivityManagerObj;
import com.gridnode.pdip.app.workflow.engine.ejb.IGWFProcessManagerHome;
import com.gridnode.pdip.app.workflow.engine.ejb.IGWFProcessManagerObj;
import com.gridnode.pdip.app.workflow.engine.ejb.IGWFRestrictionManagerHome;
import com.gridnode.pdip.app.workflow.engine.ejb.IGWFRestrictionManagerObj;
import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.expression.ExpressionParser;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerHome;
import com.gridnode.pdip.app.workflow.facade.ejb.IGWFWorkflowManagerObj;
import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcess;
import com.gridnode.pdip.base.contextdata.facade.ejb.IDataManagerHome;
import com.gridnode.pdip.base.contextdata.facade.ejb.IDataManagerObj;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity;
import com.gridnode.pdip.base.worklist.manager.ejb.GWFWorkListSessionHome;
import com.gridnode.pdip.base.worklist.manager.ejb.GWFWorkListSessionObject;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.domain.GWFException;
import com.gridnode.pdip.framework.jms.JMSRetrySender;
import com.gridnode.pdip.framework.jms.JMSSender;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.UtilEntity;

public class WorkflowUtil {

    private static Properties _workflowProps;

    public static void loadWorkflowProperties() throws SystemException {
        try{
            _workflowProps=getProperties(IWorkflowConstants.CONFIG_WORKFLOW);
        }catch(Throwable th){
            Logger.warn("[WorkflowUtil.loadWorkflowProperties] Unable to load workflow properties ",th);
            throw new SystemException("Unable to load workflow properties ",th);
        }
    }

    public static Properties getProperties(String propertyConfigFileKey) throws SystemException {
        try{
            Configuration config = ConfigurationManager.getInstance().getConfig(propertyConfigFileKey);
            return config.getProperties();
        }catch(Throwable th){
            Logger.warn("[WorkflowUtil.getProperties] Unable to load properties file, propertyConfigFileKey="+propertyConfigFileKey,th);
            throw new SystemException("Unable to load workflow properties file, propertyFileKey="+propertyConfigFileKey,th);
        }
    }

    public static String getProperty(String key) throws SystemException {
        if(_workflowProps==null)
            loadWorkflowProperties();
        return _workflowProps.getProperty(key);
    }

    public static IGWFProcessManagerObj getProcessManager() throws ServiceLookupException {
        return (IGWFProcessManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IGWFProcessManagerHome.class.getName(),IGWFProcessManagerHome.class,new Object[]{});
    }

    public static IGWFActivityManagerObj getActivityManager() throws ServiceLookupException {
        return (IGWFActivityManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IGWFActivityManagerHome.class.getName(),IGWFActivityManagerHome.class,new Object[]{});
    }

    public static IGWFRestrictionManagerObj getRestrictionManager() throws ServiceLookupException {
        return (IGWFRestrictionManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IGWFRestrictionManagerHome.class.getName(),IGWFRestrictionManagerHome.class,new Object[]{});
    }

    public static IDataManagerObj getDataManager() throws ServiceLookupException {
        return (IDataManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IDataManagerHome.class.getName(),IDataManagerHome.class,new Object[]{});
    }

    public static GWFWorkListSessionObject getWorklistManager() throws ServiceLookupException {
        return (GWFWorkListSessionObject)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(GWFWorkListSessionHome.class.getName(),GWFWorkListSessionHome.class,new Object[]{});
    }

    public static IGWFWorkflowManagerObj getWorkflowManager() throws ServiceLookupException {
        return (IGWFWorkflowManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(IGWFWorkflowManagerHome.class.getName(),IGWFWorkflowManagerHome.class,new Object[]{});
    }

    public static boolean executeExpression(String expression,Long rtProcessUId) throws SystemException {
        try{
            if(expression==null || expression.trim().length()==0)
                return true;
            GWFRtProcess rtProcess=(GWFRtProcess)UtilEntity.getEntityByKey(rtProcessUId,GWFRtProcess.ENTITY_NAME,true);
            HashMap contextData=getDataManager().getContextData(rtProcess.getContextUId());
            contextData.put(IWorkflowConstants.RTPROCESS_UID,rtProcessUId);
            return executeExpression(expression,contextData);
        }catch(SystemException ex){
            throw ex;
        }catch(Throwable th){
            throw new SystemException("Unable to execute expression ("+expression+"): "+th.getMessage(),th);
        }
    }

    public static boolean executeExpression(String expression,HashMap contextData) throws SystemException {
        try{
            if(expression==null || expression.trim().length()==0)
                return true;
            HashMap ctxData=new HashMap();
            for(Iterator iterator=contextData.keySet().iterator();iterator.hasNext();){
                Object key=iterator.next();
                if(key!=null)
                    ctxData.put(key.toString(),contextData.get(key));
            }
            return ExpressionParser.processExpression(expression,ctxData);
        }catch(Throwable th){
            throw new SystemException("Unable to execute expression ("+expression+"): "+th.getMessage(),th);
        }
    }

    public static void addWorkItem(String performer,Long rtActivityUID, String activityId, String processDefKey,Long contextUId) throws GWFException {
        GWFWorkListValueEntity entity = new GWFWorkListValueEntity();
        try {
            entity.setPerformer(performer);
            entity.setCreationDate(new Date());
            entity.setRtActivityUId(rtActivityUID);
            entity.setActivityId(activityId);
            entity.setProcessDefKey(processDefKey);
            entity.setContextUId(contextUId);
            Logger.debug("[WorkflowUtil.addWorkItem] GWFWorkListValueEntity ="+entity);
            getWorklistManager().addWorkItem(entity);
        } catch (Exception e) {
            Logger.warn("[WorkflowUtil.addWorkItem] Exception GWFWorkListValueEntity ="+entity, e);
            throw new GWFException("Exception in addWorkItem GWFWorkListValueEntity ("+entity+"): "+e.getMessage(), e);
        }
    }

    public static void removeWorkitem(Long rtActivityUId) throws WorkflowException,SystemException{
        try{
            GWFWorkListSessionObject wlManager=getWorklistManager();
            Collection coll=wlManager.getWorkItemsByRtActivity(rtActivityUId);
            if(coll!=null && coll.size()>0){
                for(Iterator iterator=coll.iterator();iterator.hasNext();){
                    GWFWorkListValueEntity entity=(GWFWorkListValueEntity)iterator.next();
                    wlManager.dropWorkItem((Long)entity.getKey());
                }
            }
        }catch(ApplicationException ex){
            throw new WorkflowException("Exception ",ex);
        }catch(Throwable th){
            throw new SystemException("Error ",th);
        }
    }

    public static long getTimeInterval(String intervalStr) throws Exception {
        if(intervalStr==null || intervalStr.trim().length()==0)
            return -1;
        Calendar bcal=new GregorianCalendar();
        Calendar cal=new GregorianCalendar();
        //cal.clear();
        String tmpStr="PYMWDTHMS";
        StringTokenizer strTok=new StringTokenizer(intervalStr,tmpStr,true);
        //boolean hasP=false,hasT=false;
        String in="";
        int num=0; //,ind=-1;
        //long interval=0;
        while(strTok.hasMoreTokens()){
            String tok=strTok.nextToken();
            int curInd=tmpStr.indexOf(tok);
            if(tok.equals("P")||tok.equals("T")) {
                in=tok;
            } else if(curInd==-1) {
                num=Integer.parseInt(tok);
            } else if(curInd!=-1) {
                if(tok.equals("Y"))
                    cal.add(Calendar.YEAR,num);
                else if(tok.equals("M") && in.equals("P"))
                    cal.add(Calendar.MONTH,num);
                else if(tok.equals("W") && in.equals("P"))
                    cal.add(Calendar.DATE,num*7);
                else if(tok.equals("D"))
                    cal.add(Calendar.DATE,num);
                else if(tok.equals("H"))
                    cal.add(Calendar.HOUR,num);
                else if(tok.equals("M") && in.equals("T"))
                    cal.add(Calendar.MINUTE,num);
                else if(tok.equals("S"))
                    cal.add(Calendar.SECOND,num);
                else throw new WorkflowException("Invalid Format, interval="+intervalStr);
                num=0;
            }
        }
        return cal.getTime().getTime()-bcal.getTime().getTime();
    }

    public static void addAlarm(String rtKey, long timeInterval) throws SystemException {
        Logger.debug("[WorkflowUtil.addAlarm] rtKey="+rtKey+", timeInterval="+timeInterval);
        try {
            addAlarm(rtKey,rtKey,"CheckState",timeInterval);
        } catch (Throwable e) {
            throw new SystemException("Error: "+e.getMessage(), e);
        }
    }

    public static void addAlarm(String senderKey,String receiverKey,String category,long timeInterval) throws SystemException {
        try {
            iCalAlarm alarm = new iCalAlarm();
            alarm.setCategory(category);
            alarm.setSenderKey(senderKey);
            alarm.setReceiverKey(receiverKey);
            alarm.setStartDt(new Date(new Date().getTime()+timeInterval));
            addAlarm(alarm);
        } catch (Throwable e) {
            throw new SystemException("Error in adding alarm ,senderKey="+senderKey+", timeInterval="+timeInterval+": "+e.getMessage(), e);
        }
    }

    public static void addRetryAlarm(String senderKey,String receiverKey,String category,long timeInterval) throws SystemException {
        Logger.debug("[WorkflowUtil.addRetryAlarm] senderKey="+senderKey+",receiverKey="+receiverKey+",category="+category+", timeInterval="+timeInterval);
        try {
            iCalAlarm alarm = new iCalAlarm();
            alarm.setCategory(category);
            alarm.setSenderKey(senderKey);
            alarm.setReceiverKey(receiverKey);
            alarm.setStartDt(new Date(new Date().getTime()+timeInterval));
            alarm.setDelayPeriod(new Long(timeInterval/1000)); // since timeInterval is in milliseconds
            addAlarm(alarm);
        } catch (Throwable e) {
            throw new SystemException("Error ,senderKey="+senderKey+", timeInterval="+timeInterval, e);
        }
    }


    public static void addAlarm(iCalAlarm alarm) throws SystemException {
        try {
            //IiCalTimeMgrHome mgrHome =
            //    (IiCalTimeMgrHome) ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(IiCalTimeMgrHome.class);

            //IiCalTimeMgrObj mgrObject = mgrHome.create();

        	IiCalTimeMgrObj mgrObject = (IiCalTimeMgrObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
        	                               IiCalTimeMgrHome.class.getName(),
        	                               IiCalTimeMgrHome.class,
        	                               new Object[0]);
            mgrObject.addAlarm(alarm);
        } catch (Throwable e) {
            throw new SystemException("Error while adding alarm,alarm="+alarm+": "+e.getMessage(), e);
        }
    }


    public static void cancelAlarm(String rtKey) throws SystemException {
        try {
            IDataFilter filter = new DataFilterImpl();
            filter.addSingleFilter(null, iCalAlarm.SENDER_KEY, filter.getEqualOperator(), rtKey, false);
            filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.CATEGORY, filter.getEqualOperator(), "CheckState", false);
            cancelAlarmByFilter(filter);
        } catch (Throwable e) {
            throw new SystemException("Error while cancelling alarm: "+e.getMessage(), e);
        }
    }

    public static void cancelAlarm(String senderKey,String receiverKey,String category) throws SystemException {
        try {
            IDataFilter filter = new DataFilterImpl();
            filter.addSingleFilter(null, iCalAlarm.SENDER_KEY, filter.getEqualOperator(), senderKey, false);
            filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.RECEIVER_KEY, filter.getEqualOperator(), receiverKey, false);
            filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.CATEGORY, filter.getEqualOperator(), category, false);
            cancelAlarmByFilter(filter);
        } catch (Throwable e) {
            throw new SystemException("Error while cancelling Alarm,senderKey="+senderKey+",receiverKey="+receiverKey+",category="+category+",exception: "+e.getMessage(), e);
        }
    }

    public static void cancelAlarmByFilter(IDataFilter filter) throws SystemException {
        try {
            //IiCalTimeMgrHome mgrHome =
            //    (IiCalTimeMgrHome) ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(IiCalTimeMgrHome.class);

            //IiCalTimeMgrObj mgrObject = mgrHome.create();
        	IiCalTimeMgrObj mgrObject = (IiCalTimeMgrObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
          	                               IiCalTimeMgrHome.class.getName(),
          	                               IiCalTimeMgrHome.class,
          	                               new Object[0]);
            mgrObject.cancelAlarmByFilter(filter);
        } catch (Throwable e) {
            throw new SystemException("Error ,filter="+filter.getFilterExpr()+": "+e.getMessage(), e);
        }
    }

    public static void setRetryAlarm(String senderKey,String receiverKey,String category,long timeInterval,boolean refresh) throws SystemException 
    {
      Logger.debug("[WorkflowUtil.setRetryAlarm] senderKey="+senderKey+",receiverKey="+receiverKey+",category="+category+", timeInterval="+timeInterval+", refresh="+refresh);
      try 
      {
        //IiCalTimeMgrHome mgrHome =(IiCalTimeMgrHome) ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(IiCalTimeMgrHome.class);
        //IiCalTimeMgrObj mgrObject = mgrHome.create();
      	IiCalTimeMgrObj mgrObject = (IiCalTimeMgrObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
       	                               IiCalTimeMgrHome.class.getName(),
       	                               IiCalTimeMgrHome.class,
       	                               new Object[0]);

        IDataFilter filter = new DataFilterImpl();
        filter.addSingleFilter(null, iCalAlarm.SENDER_KEY, filter.getEqualOperator(), senderKey, false);
        filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.RECEIVER_KEY, filter.getEqualOperator(), receiverKey, false);
        filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.CATEGORY, filter.getEqualOperator(), category, false);
        Collection alarmColl = mgrObject.findAlarms(filter);
        if(refresh && alarmColl!=null && !alarmColl.isEmpty())
        {
          mgrObject.cancelAlarmByFilter(filter);
          alarmColl=null;
        }
        if(alarmColl==null || alarmColl.isEmpty())
        {
          addRetryAlarm(senderKey,receiverKey,category,timeInterval);
        }
      } catch (Throwable e) {
          throw new SystemException("Error in setting retry alarm,senderKey="+senderKey+", timeInterval="+timeInterval+": "+e.getMessage(), e);
      }
    }

  public static iCalAlarm findAlarm(String senderKey,String receiverKey,String category) throws Throwable { 
      try {
          IDataFilter filter = new DataFilterImpl();
          filter.addSingleFilter(null, iCalAlarm.SENDER_KEY, filter.getEqualOperator(), senderKey, false);
          filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.RECEIVER_KEY, filter.getEqualOperator(), receiverKey, false);
          filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.CATEGORY, filter.getEqualOperator(), category, false);

          //IiCalTimeMgrHome mgrHome =
          //    (IiCalTimeMgrHome) ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(IiCalTimeMgrHome.class);

          //IiCalTimeMgrObj mgrObject = mgrHome.create();
        	IiCalTimeMgrObj mgrObject = (IiCalTimeMgrObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
         	                               IiCalTimeMgrHome.class.getName(),
         	                               IiCalTimeMgrHome.class,
         	                               new Object[0]);
          Collection alarmColl = mgrObject.findAlarms(filter);
          if(alarmColl!=null && !alarmColl.isEmpty())
          {
            Logger.debug("[WorkflowUtil.findAlarm] iCalAlarm found");
            return (iCalAlarm)alarmColl.iterator().next();
          }
      } catch (Throwable th) {
          Logger.warn("[WorkflowUtil.findAlarm] Error ,senderKey="+senderKey+", receiverKey="+receiverKey+", category="+category, th);
          throw th;
      }
      return null;
  }

  public static void sendMessage(String destName, HashMap paramMap) throws WorkflowException 
  {
    try 
    {
      Hashtable<String,String> props = null;
      if (paramMap.containsKey(IGWFRouteDispatcher.ENGINE_TYPE))
      {
        props = new Hashtable<String,String>();
        props.put(IGWFRouteDispatcher.ENGINE_TYPE, (String)paramMap.get(IGWFRouteDispatcher.ENGINE_TYPE));
      }
      
      if(! JMSRetrySender.isSendViaDefMode())
      {
        JMSRetrySender.sendMessage(IWorkflowConstants.CONFIG_WORKFLOW_JMS, destName, paramMap, props);
      }
      else
      {
        JMSSender.sendMessage(IWorkflowConstants.CONFIG_WORKFLOW_JMS, destName, paramMap, props);
      }
    } 
    catch (Throwable th) 
    {
      throw new WorkflowException("Exception while sending to destination, destName="+destName+",paramMap="+paramMap, th);
    }
  }
  /*NSL20070130 replaced by generic method above
    public static void sendMessageToQueue(String queueName, HashMap paramMap) throws WorkflowException {
        try {
            JMSSender.sendMessageToQueue(IWorkflowConstants.CONFIG_WORKFLOW_JMS,queueName, paramMap);
        } catch (Throwable th) {
            throw new WorkflowException("[WorkflowUtil.sendMessageToQueue] Exception while sending to queue, queueName="+queueName+",paramMap="+paramMap, th);
        }
    }

    public static void sendMessageToTopic(String topicName,HashMap paramMap) throws WorkflowException{
        try {
            JMSSender.sendMessageToTopic(IWorkflowConstants.CONFIG_WORKFLOW_JMS,topicName, paramMap);
        } catch (Throwable th) {
            throw new WorkflowException("[WorkflowUtil.sendMessageToTopic] Exception while sending to topicName, topicName="+topicName+",paramMap="+paramMap, th);
        }
    }
  */
    public static AbstractEntityHandler getHandlerFor(String entityName)
    {
      return EntityHandlerFactory.getHandlerFor(entityName, true);
    }


    ///testing
    public static void main(String args[]) throws Exception {
        String interval="T3M";
        System.out.println("TimeInterval for "+interval+"="+getTimeInterval(interval));
    }

}