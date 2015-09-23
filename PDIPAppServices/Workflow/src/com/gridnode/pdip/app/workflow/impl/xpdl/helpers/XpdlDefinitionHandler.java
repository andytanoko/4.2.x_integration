/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XpdlProcessEngine.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 05 2003    Mahesh              Created
 *
 */

package com.gridnode.pdip.app.workflow.impl.xpdl.helpers;
 
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.app.workflow.util.WorkflowUtil;
import com.gridnode.pdip.base.gwfbase.xpdl.helpers.XpdlDefinitionCache;
import com.gridnode.pdip.base.gwfbase.xpdl.helpers.XpdlDefinitionHelper;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlActivity;
import com.gridnode.pdip.base.gwfbase.xpdl.model.XpdlProcess;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.KeyConverter;

public class XpdlDefinitionHandler {

    private static Hashtable handlerHt=new Hashtable();
    private static Hashtable lastAccessedHt=new Hashtable();

    private boolean isCacheEnabled=false;

    private String processId;
    private String packageId;
    private String pkgVersionId;
    
    private XpdlDefinitionHandler(String processDefKey) throws SystemException{
        //String propertyConfigFileKey="";
        String str=WorkflowUtil.getProperty(IWorkflowConstants.WORKFLOW_XPDL_CACHE);
        if(str==null) str="false";
        isCacheEnabled=new Boolean(str).booleanValue();
        packageId=KeyConverter.getValue(processDefKey,2);
        pkgVersionId=KeyConverter.getValue(processDefKey,3);
        processId=KeyConverter.getValue(processDefKey,5);
        Logger.log("[XpdlDefinitionHandler.constructor] packageId="+packageId+",pkgVersionId="+pkgVersionId+",processId="+processId+",isCacheEnabled="+isCacheEnabled);
    }

    public static XpdlDefinitionHandler getInstance(String processDefKey) throws SystemException {
        XpdlDefinitionHandler definitionHandler=(XpdlDefinitionHandler)handlerHt.get(processDefKey);
        if(definitionHandler==null){
            definitionHandler=new XpdlDefinitionHandler(processDefKey);
            if(definitionHandler!=null)
                handlerHt.put(processDefKey,definitionHandler);
        }
        lastAccessedHt.put(processDefKey,new Long(System.currentTimeMillis()));
        return definitionHandler;
    }

    public static String getProcessDefKey(String packageId,String pkgVersionId,String processId){
        String processDefKey="http://"+IWorkflowConstants.XPDL_ENGINE+"/"+packageId+"/"+pkgVersionId+"/"+XpdlProcess.ENTITY_NAME+"/"+processId;
        return processDefKey;
    }
    
    public static void checkAndUnloadDefinition(long timeout)
    {
      Logger.log("[XpdlDefinitionHandler.checkAndUnloadDefinition] Enter, timeout="+timeout);
      synchronized(lastAccessedHt)
      {
        long currentTimeMillis=System.currentTimeMillis();
        for(Enumeration keys = lastAccessedHt.keys();keys.hasMoreElements();)
        {
          String key = (String)keys.nextElement();
          Long lastTime = (Long)lastAccessedHt.get(key);
          if((currentTimeMillis-lastTime.longValue())>=timeout)
          {
            lastAccessedHt.remove(key);
            handlerHt.remove(key);
            Logger.log("[XpdlDefinitionHandler.checkAndUnloadDefinition] Unloading XPDL Definition, processDefKey="+key);
          }
        }
      }
    }

    private XpdlDefinitionCache cache() throws SystemException {
        return XpdlDefinitionCache.getXpdlDefinitionCache(packageId,pkgVersionId);
    }

    public XpdlProcess getXpdlProcess(Long uId) throws Throwable {
        if(isCacheEnabled)
            return (XpdlProcess)cache().getDefinationEntity(uId,XpdlProcess.ENTITY_NAME);
        else return XpdlDefinitionHelper.getProcess(uId);
    }

    public XpdlProcess getXpdlProcess(String xpdlProcessId) throws Throwable {
        if(isCacheEnabled)
            return cache().getProcess(xpdlProcessId);
        else {
            Collection coll=XpdlDefinitionHelper.getProcesses(xpdlProcessId,packageId,pkgVersionId);
            if(coll!=null && coll.size()>0)
                return (XpdlProcess)coll.iterator().next();
            else return null;
        }
    }

    public XpdlActivity getXpdlActivity(Long uId) throws Throwable {
        if(isCacheEnabled)
            return (XpdlActivity)cache().getDefinationEntity(uId,XpdlActivity.ENTITY_NAME);
        else return XpdlDefinitionHelper.getActivity(uId);
    }

    public XpdlActivity getXpdlActivity(String activityId) throws Throwable {
        if(isCacheEnabled)
            return cache().getActivity(activityId,processId);
        else return XpdlDefinitionHelper.getActivity(activityId,processId,packageId,pkgVersionId);
    }

    public Collection getDataFields(String processId) throws Throwable {
        if(isCacheEnabled)
            return cache().getDataFields(processId);
        else return XpdlDefinitionHelper.getDataFields(processId,packageId,pkgVersionId);
    }

    public Collection getApplications(String applicationId,String processId) throws Throwable{
        if(isCacheEnabled)
            return cache().getApplications(applicationId,processId);
        else return XpdlDefinitionHelper.getApplications(applicationId,processId,packageId,pkgVersionId);
    }

    public Collection getFormalParams(String applicationId,String processId) throws Throwable{
        if(isCacheEnabled)
            return cache().getFormalParams(applicationId,processId);
        else return XpdlDefinitionHelper.getFormalParams(applicationId,processId,packageId,pkgVersionId);
    }

    public Collection getSubFlows(String activityId) throws Throwable {
        if(isCacheEnabled)
            return cache().getSubFlows(activityId,processId);
        else return XpdlDefinitionHelper.getSubFlows(activityId,processId,packageId,pkgVersionId);
    }

    public Collection getTools(String activityId) throws Throwable {
        if(isCacheEnabled)
            return cache().getTools(activityId,processId);
        else return XpdlDefinitionHelper.getTools(activityId,processId,packageId,pkgVersionId);
    }

    public Collection getTransitionRefs(Long transitionRefListUId) throws Throwable {
        if(isCacheEnabled)
            return cache().getTransitionRefs(transitionRefListUId);
        else return XpdlDefinitionHelper.getTransitionRefs(transitionRefListUId);
    }

    public Collection getTransFromTransRefListUId(Long transitionRefListUId) throws Throwable {
        if(isCacheEnabled)
            return cache().getTransFromTransRefListUId(transitionRefListUId,processId);
        else return XpdlDefinitionHelper.getTransFromTransRefListUId(transitionRefListUId,processId,packageId,pkgVersionId);
    }

    public Collection getTransitions(String fromActivityId,String toActivityId) throws Throwable {
        if(isCacheEnabled)
            return cache().getTransitions(fromActivityId,toActivityId,processId);
        else return XpdlDefinitionHelper.getTransitions(fromActivityId,toActivityId,processId,packageId,pkgVersionId);
    }

    public Collection getTransitionRestrictions(Long transitionRestrictionListUId) throws Throwable{
        if(isCacheEnabled)
            return cache().getTransitionRestrictions(transitionRestrictionListUId);
        else return XpdlDefinitionHelper.getTransitionRestrictions(transitionRestrictionListUId);
    }

}