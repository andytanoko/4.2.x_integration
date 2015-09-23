/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ACLLogger.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * MMM DD YYYY    ????			             Created
 * Feb 08 2007		Alain Ah Ming					 Use error codes. Otherwise use warning log
 */
package com.gridnode.pdip.base.gwfbase.xpdl.helpers;

import java.util.*;

import com.gridnode.pdip.base.gwfbase.util.Logger;
import com.gridnode.pdip.base.gwfbase.xpdl.model.*;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.UtilString;
 
/**
 * 
 * @author 
 * @since
 * @version GT 4.0 VAN 
 */
public class XpdlDefinitionCache {

    static Hashtable xpdlDefinitionCacheHt=new Hashtable();

    Hashtable definitionHt=new Hashtable();
    String packageId;
    String pkgVersionId;

    private XpdlDefinitionCache(String packageId, String pkgVersionId) throws SystemException {
        try{
            loadDefinition(packageId,pkgVersionId);
            this.packageId=packageId;
            this.pkgVersionId=pkgVersionId;
        }catch(Throwable th){
            Logger.warn("[XpdlDefinitionCache.constructor]",th);
            throw new SystemException("Error in loading definition, packageId:"+packageId,th);
        }
    }

    private void loadDefinition(String packageId, String pkgVersionId) throws Throwable{
        Logger.log("[XpdlDefinitionCache.loadDefinition] Start time "+new Date()+"\t\tparams="+packageId+","+pkgVersionId);
        List entities=XpdlDefinitionHelper.getPackageTree(packageId,pkgVersionId);
        for(Iterator iterator=entities.iterator();iterator.hasNext();){
            AbstractEntity entity=(AbstractEntity)iterator.next();
            Collection defColl=(Collection)definitionHt.get(entity.getEntityName());
            if(defColl==null){
                defColl=new Vector();
                definitionHt.put(entity.getEntityName(),defColl);
            }
            defColl.add(entity);
        }
        Logger.log("[XpdlDefinitionCache.loadDefinition] End time "+new Date()+"\t\tparams="+packageId+","+pkgVersionId);
    }

    public Collection getDefinationEntities(Collection uIdColl,String entityName){
        Collection coll=getDefinationHt(entityName);
        Collection retColl=new Vector();
        for(Iterator iterator=coll.iterator();iterator.hasNext();){
            AbstractEntity entity=(AbstractEntity)iterator.next();
            if(uIdColl.contains(entity.getKey()))
                retColl.add(entity);
        }
        return retColl;
    }

    public AbstractEntity getDefinationEntity(Long uId,String entityName){
        Collection coll=getDefinationHt(entityName);
        for(Iterator iterator=coll.iterator();iterator.hasNext();){
            AbstractEntity entity=(AbstractEntity)iterator.next();
            if(entity.getKey().equals(uId))
                return entity;
        }
        return null;
    }

    public String getCacheKey(){
        return packageId+"/"+pkgVersionId;
    }

    public static XpdlDefinitionCache getXpdlDefinitionCache(String cacheKey) throws SystemException{
        List list=UtilString.split(cacheKey,"/");
        if(list!=null && list.size()==2){
            return getXpdlDefinitionCache((String)list.get(0),(String)list.get(1));
        }
        return null;
    }

    public static XpdlDefinitionCache getXpdlDefinitionCache(String packageId, String pkgVersionId) throws SystemException{
        String cacheKey=packageId+"/"+pkgVersionId;
        XpdlDefinitionCache xpdlDefinitionCache=(XpdlDefinitionCache)xpdlDefinitionCacheHt.get(cacheKey);
        if(xpdlDefinitionCache==null){
            synchronized(xpdlDefinitionCacheHt){
                xpdlDefinitionCache=(XpdlDefinitionCache)xpdlDefinitionCacheHt.get(cacheKey);
                if(xpdlDefinitionCache==null){
                    xpdlDefinitionCache=new XpdlDefinitionCache(packageId,  pkgVersionId);
                    xpdlDefinitionCacheHt.put(cacheKey,xpdlDefinitionCache);
                }
            }
        }
        return xpdlDefinitionCache;
    }

    public static void removeXpdlDefinitionCache(String packageId, String pkgVersionId) throws SystemException{
        String cacheKey=packageId+"/"+pkgVersionId;
        synchronized(xpdlDefinitionCacheHt){
            xpdlDefinitionCacheHt.remove(cacheKey);
        }
    }

    private Collection getDefinationHt(String entityName){
        Collection coll=(Collection)definitionHt.get(entityName);
        if(coll==null)
            coll=new Vector();
        return coll;
    }

    private static boolean eq(Object obj1,Object obj2){
        if(obj1==obj2 || (obj1!=null && obj2!=null && obj1.equals(obj2)))
            return true;
        return false;
    }

    public Collection getApplications(String applicationId,String processId) throws Throwable{
        try{
            Collection retColl=new Vector();
            Collection entities=getDefinationHt(XpdlApplication.ENTITY_NAME);
            for(Iterator iterator=entities.iterator();iterator.hasNext();){
                XpdlApplication application =(XpdlApplication)iterator.next();
                boolean b=false;
                b=(processId==null || eq(application.getProcessId(),processId));
                b = b && (applicationId==null || eq(application.getApplicationId(),applicationId));
                if(b) retColl.add(application);
            }
            return retColl;
        }catch(Throwable th){
            Logger.warn("[XpdlDefinitionCache.getApplications] Error ",th);
            throw new SystemException("[XpdlDefinitionCache.getApplications] Error ",th);
        }
    }

    public XpdlProcess getProcess(String processId) throws Throwable {
        try{
            Collection entities=getDefinationHt(XpdlProcess.ENTITY_NAME);
            for(Iterator iterator=entities.iterator();iterator.hasNext();){
                XpdlProcess process =(XpdlProcess)iterator.next();
                if(eq(process.getProcessId(),processId))
                    return process;
            }
            return null;
        }catch(Throwable th){
            Logger.warn("[XpdlDefinitionCache.getProcess] Error ",th);
            throw new SystemException("Error in gettting XPDL process",th);
        }
    }


    public XpdlActivity getActivity(String activityId, String processId) throws Throwable{
        try{
            Collection entities=getDefinationHt(XpdlActivity.ENTITY_NAME);
            for(Iterator iterator=entities.iterator();iterator.hasNext();){
                XpdlActivity activity =(XpdlActivity)iterator.next();
                if(eq(activity.getProcessId(),processId) && eq(activity.getActivityId(),activityId))
                    return activity;
            }
            return null;
        }catch(Throwable th){
            Logger.warn("[XpdlDefinitionCache.getActivity] Error ",th);
            throw new SystemException("[XpdlDefinitionCache.getActivity] Error ",th);
        }
    }


    public Collection getDataFields(String processId) throws Throwable {
        try{
            Collection retColl=new Vector();
            Collection entities=getDefinationHt(XpdlDataField.ENTITY_NAME);
            for(Iterator iterator=entities.iterator();iterator.hasNext();){
                XpdlDataField dataField =(XpdlDataField)iterator.next();
                if(processId!=null){
                    if(eq(dataField.getProcessId(),processId))
                        retColl.add(dataField);
                } else retColl.add(dataField);
            }
            return retColl;
        }catch(Throwable th){
            Logger.warn("[XpdlDefinitionCache.getDataFields] Error ",th);
            throw new SystemException("[XpdlDefinitionCache.getDataFields] Error ",th);
        }
    }

    public Collection getFormalParams(String applicationId,String processId) throws Throwable{
        try{
            Collection retColl=new Vector();
            Collection entities=getDefinationHt(XpdlFormalParam.ENTITY_NAME);
            for(Iterator iterator=entities.iterator();iterator.hasNext();){
                XpdlFormalParam formalParam =(XpdlFormalParam)iterator.next();
                if(eq(formalParam.getProcessId(),processId) && eq(formalParam.getApplicationId(),applicationId)){
                    retColl.add(formalParam);
                }
            }
            return retColl;
        }catch(Throwable th){
            Logger.warn("[XpdlDefinitionCache.getFormalParams] Error ",th);
            throw new SystemException("Error in getting formal parameters",th);
        }
    }

    public Collection getSubFlows(String activityId, String processId) throws Throwable {
        try{
            Collection retColl=new Vector();
            Collection entities=getDefinationHt(XpdlSubFlow.ENTITY_NAME);
            for(Iterator iterator=entities.iterator();iterator.hasNext();){
                XpdlSubFlow subflow =(XpdlSubFlow)iterator.next();
                if(eq(subflow.getProcessId(),processId) && eq(subflow.getActivityId(),activityId))
                    retColl.add(subflow);
            }
            return retColl;
        }catch(Throwable th){
            Logger.warn("[XpdlDefinitionCache.getSubFlows] Error ",th);
            throw new SystemException("Error in getting sub flow activity",th);
        }
    }

    public Collection getTools(String activityId, String processId) throws Throwable {
        try{
            Collection retColl=new Vector();
            Collection entities=getDefinationHt(XpdlTool.ENTITY_NAME);
            for(Iterator iterator=entities.iterator();iterator.hasNext();){
                XpdlTool tool =(XpdlTool)iterator.next();
                if(eq(tool.getProcessId(),processId) && eq(tool.getActivityId(),activityId))
                    retColl.add(tool);
            }
            return retColl;
        }catch(Throwable th){
            Logger.warn("[XpdlDefinitionCache.getTools] Error ",th);
            throw new SystemException("[XpdlDefinitionCache.getTools] Error ",th);
        }
    }

    public Collection getTransitionRefs(Long transitionRefListUId) throws Throwable {
        try{
            Collection retColl=new Vector();
            Collection entities=getDefinationHt(XpdlTransitionRef.ENTITY_NAME);
            for(Iterator iterator=entities.iterator();iterator.hasNext();){
                XpdlTransitionRef transRef =(XpdlTransitionRef)iterator.next();
                if(eq(transRef.getListUId(),transitionRefListUId))
                    retColl.add(transRef);
            }
            return retColl;
        }catch(Throwable th){
            Logger.warn("[XpdlDefinitionCache.getTransitionRefs] Error ",th);
            throw new SystemException("[XpdlDefinitionCache.getTransitionRefs] Error ",th);
        }
    }

    public Collection getTransFromTransRefListUId(Long transitionRefListUId,String processId) throws Throwable {
        try{
            Collection retColl=new Vector();
            Collection transRefColl=getTransitionRefs(transitionRefListUId);
            Collection transColl=getDefinationHt(XpdlTransition.ENTITY_NAME);
            for(Iterator iterator=transRefColl.iterator();iterator.hasNext();){
                String transitionId=((XpdlTransitionRef)iterator.next()).getTransitionId();
                for(Iterator iterator1=transColl.iterator();iterator1.hasNext();){
                    XpdlTransition transition=(XpdlTransition)iterator1.next();
                    if(eq(transition.getProcessId(),processId) && eq(transition.getTransitionId(),transitionId))
                        retColl.add(transition);
                }
            }
            return retColl;
        }catch(Throwable th){
            Logger.warn("[XpdlDefinitionCache.getTransFromTransRefListUId] Error ",th);
            throw new SystemException("[XpdlDefinitionCache.getTransFromTransRefListUId] Error ",th);
        }
    }

    public Collection getTransitions(String fromActivityId,String toActivityId,String processId) throws Throwable{
        try{
            Collection retColl=new Vector();
            Collection entities=getDefinationHt(XpdlTransition.ENTITY_NAME);
            for(Iterator iterator=entities.iterator();iterator.hasNext();){
                XpdlTransition transition=(XpdlTransition)iterator.next();
                if(eq(transition.getProcessId(),processId)){
                    boolean b=false;
                    b=(fromActivityId==null || eq(transition.getFromActivityId(),fromActivityId));
                    b = b && (toActivityId==null || eq(transition.getToActivityId(),toActivityId));
                    if(b) retColl.add(transition);
                }
            }
            return retColl;
        }catch(Throwable th){
            Logger.warn("[XpdlDefinitionCache.getTransitions] Error ",th);
            throw new SystemException("[XpdlDefinitionCache.getTransitions] Error ",th);
        }
    }

    public Collection getTransitionRestrictions(Long transitionRestrictionListUId) throws Throwable{
        try{
            Collection retColl=new Vector();
            Collection entities=getDefinationHt(XpdlTransitionRestriction.ENTITY_NAME);
            for(Iterator iterator=entities.iterator();iterator.hasNext();){
                XpdlTransitionRestriction transRes =(XpdlTransitionRestriction)iterator.next();
                if(eq(transRes.getListUId(),transitionRestrictionListUId))
                    retColl.add(transRes);
            }
            return retColl;
        }catch(Throwable th){
            Logger.warn("[XpdlDefinitionCache.getTransitionRestrictions] Error ",th);
            throw new SystemException("[XpdlDefinitionCache.getTransitionRestrictions] Error ",th);
        }
    }

}