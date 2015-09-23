package com.gridnode.pdip.base.gwfbase.bpss.helpers;

import java.util.*;

import com.gridnode.pdip.base.gwfbase.bpss.model.*;
import com.gridnode.pdip.base.gwfbase.util.*;
import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.*;

public class BpssDefinitionCache {

    static Hashtable bpssDefinitionCacheHt=new Hashtable();

    BpssProcessSpecification bpssProcessSpecification=null;
    Hashtable definitionHt=new Hashtable();
    Object lock = new Object(); //GNDB00028507: TWX

    private BpssDefinitionCache(String specName,String specVersion,String specUUId) throws SystemException {
        try{
            loadDefinition(specName,specVersion,specUUId);
        }catch(Throwable th){
            throw new SystemException("[BpssDefinitionCache.constructor]",th);
        }

    }

    private void loadDefinition(String specName,String specVersion,String specUUId) throws Throwable{
        Logger.log("[BpssDefinitionCache.loadDefinition] Start time "+new Date()+"\t\tparams="+specName+","+specVersion+","+specUUId);
        IDataFilter filter = new DataFilterImpl();
        filter.addSingleFilter(null, IBpssProcessSpecification.UUID,filter.getEqualOperator(), specUUId, false);
        filter.addSingleFilter(filter.getAndConnector(),IBpssProcessSpecification.VERSION,filter.getEqualOperator(),specVersion,false);
        Collection processSpecificationColl=UtilEntity.getEntityByFilter(filter,BpssProcessSpecification.ENTITY_NAME,true);
        if(processSpecificationColl!=null){
            for(Iterator iterator1=processSpecificationColl.iterator();iterator1.hasNext();){
                BpssProcessSpecification tmpBpssProcessSpecification=(BpssProcessSpecification)iterator1.next();
                if(tmpBpssProcessSpecification.getSpecName()!=null && tmpBpssProcessSpecification.getSpecName().equals(specName)){
                  
                  
                    Logger.log("BpssDefinitionCache obtained is : uuid: "+tmpBpssProcessSpecification.getSpecUUId()+". version: "+tmpBpssProcessSpecification.getSpecVersion());
                    bpssProcessSpecification=tmpBpssProcessSpecification;
                    
                    filter = new DataFilterImpl();
                    filter.addSingleFilter(null, IBpssProcessSpecEntry.SPEC_UID,filter.getEqualOperator(),bpssProcessSpecification.getKey(), false);
                    Collection processSpecEntryColl=UtilEntity.getEntityByFilterForReadOnly(filter,BpssProcessSpecEntry.ENTITY_NAME,true);
                    if(processSpecEntryColl!=null){
                        definitionHt.put(BpssProcessSpecEntry.ENTITY_NAME,processSpecEntryColl);
                        
                        for(Iterator iterator2=processSpecEntryColl.iterator();iterator2.hasNext();){
                            BpssProcessSpecEntry processSpecEntry=(BpssProcessSpecEntry)iterator2.next();
                            Collection defColl=(Collection)definitionHt.get(processSpecEntry.getEntryType());
                            if(defColl==null){
                                defColl=new Vector();
                                definitionHt.put(processSpecEntry.getEntryType(),defColl);
                            }
                            defColl.add(UtilEntity.getEntityByKeyForReadOnly(new Long(processSpecEntry.getEntryUId()),processSpecEntry.getEntryType(),true));
                        }
                    }
                    break;
                }
            }
        }
        Logger.log("[BpssDefinitionCache.loadDefinition] End time "+new Date()+"\t\tparams="+specName+","+specVersion+","+specUUId);
    }

    public BpssProcessSpecification getBpssProcessSpecification(){
      
      synchronized(lock)
      {
        return bpssProcessSpecification;
      }
    }

    public BpssBinaryCollaboration getBpssBinaryCollaboration(String name){
        Collection coll=getDefinationHt(BpssBinaryCollaboration.ENTITY_NAME);
        for(Iterator iterator=coll.iterator();iterator.hasNext();){
            BpssBinaryCollaboration binaryCollaboration=(BpssBinaryCollaboration)iterator.next();
            if(eq(binaryCollaboration.getBinaryCollaborationName(),name))
                return binaryCollaboration;
        }
        return null;
    }

    public Collection getBpssStartColl(Long binaryCollaborationUId){
        Collection coll=getDefinationHt(BpssStart.ENTITY_NAME);
        Collection retColl=new Vector();
        for(Iterator iterator=coll.iterator();iterator.hasNext();){
            BpssStart bpssStart=(BpssStart)iterator.next();
            if(eq(bpssStart.getKey(),binaryCollaborationUId))
                retColl.add(bpssStart);
        }
        return retColl;
    }

    public Collection getBpssCompletionStates(String fromBusinessStateKey, Long processUId, String processType, Long mpcUId) {
        Collection coll=getDefinationHt(BpssCompletionState.ENTITY_NAME);
        Collection retColl=new Vector();
        for(Iterator iterator=coll.iterator();iterator.hasNext();){
            BpssCompletionState bpssComp=(BpssCompletionState)iterator.next();
            if(eq(bpssComp.getProcessUID(),processUId) &&
               eq(bpssComp.getFromBusinessStateKey(),fromBusinessStateKey) &&
               eq(bpssComp.getProcessType(),processType)){
                if(mpcUId==null || eq(bpssComp.getMpcUId(),mpcUId))
                    retColl.add(bpssComp);
            }
        }
        return retColl;
    }


    public Collection getBpssTransitionsFrom(String fromKey,Long inProcessUId,String inProcessType) throws SystemException
    {
        Collection transitionColl=getChildEntities(inProcessUId,inProcessType,BpssTransition.ENTITY_NAME);
        Collection retColl=new Vector();
        for(Iterator iterator=transitionColl.iterator();iterator.hasNext();){
            BpssTransition transition=(BpssTransition)iterator.next();
            if(eq(transition.getFromBusinessStateKey(),fromKey))
                retColl.add(transition);
        }
        return retColl;
    }

    public Collection getBpssTransitionsTo(String toKey,Long inProcessUId,String inProcessType) throws SystemException
    {
        Collection transitionColl=getChildEntities(inProcessUId,inProcessType,BpssTransition.ENTITY_NAME);
        Collection retColl=new Vector();
        for(Iterator iterator=transitionColl.iterator();iterator.hasNext();){
            BpssTransition transition=(BpssTransition)iterator.next();
            if(eq(transition.getToBusinessStateKey(),toKey))
                retColl.add(transition);
        }
        return retColl;
    }

    public BpssDocumentEnvelope getBpssDocumentEnvelope(String documentType){
        Collection docEnvelopeColl=getDefinationHt(BpssDocumentEnvelope.ENTITY_NAME);
        for(Iterator iterator=docEnvelopeColl.iterator();iterator.hasNext();){
            BpssDocumentEnvelope docEnvelope=(BpssDocumentEnvelope)iterator.next();
            if(eq(docEnvelope.getBusinessDocumentIDRef(),documentType))
                return docEnvelope;
        }
        return null;
    }

    public BpssBusinessDocument getBusinessDocumentFromDocumentType(String documentType){
        BpssDocumentEnvelope docEnvelope=getBpssDocumentEnvelope(documentType);
        if(docEnvelope!=null && docEnvelope.getBusinessDocumentName()!=null){
            Collection businessDocColl=getDefinationHt(BpssBusinessDocument.ENTITY_NAME);
            for(Iterator iterator=businessDocColl.iterator();iterator.hasNext();){
                BpssBusinessDocument businessDoc=(BpssBusinessDocument)iterator.next();
                if(eq(businessDoc.getName(),docEnvelope.getBusinessDocumentName()))
                    return businessDoc;
            }
        }
        return null;
    }

    public BpssBinaryCollaboration getBpssBinaryCollaborationDocEnvelop(String documentType)  throws SystemException{
        BpssDocumentEnvelope docEnvelope=getBpssDocumentEnvelope(documentType);
        BpssProcessSpecEntry processSpecEntry=getBpssProcessSpecEntry((Long)docEnvelope.getKey(),BpssDocumentEnvelope.ENTITY_NAME);
        Collection coll=getDefinationHt(BpssProcessSpecEntry.ENTITY_NAME);
        BpssProcessSpecEntry parentSpecEntry=null;
        for(Iterator iterator=coll.iterator();iterator.hasNext();){
            BpssProcessSpecEntry tmpProcessSpecEntry=(BpssProcessSpecEntry)iterator.next();
            if(tmpProcessSpecEntry.getUId()==processSpecEntry.getParentEntryUId()
                && ( tmpProcessSpecEntry.getEntryType().equals(BpssReqBusinessActivity.ENTITY_NAME)
                     || tmpProcessSpecEntry.getEntryType().equals(BpssResBusinessActivity.ENTITY_NAME) )){
                parentSpecEntry=tmpProcessSpecEntry;
                break;
            }
        }
        if(parentSpecEntry!=null){
            Long businessTransUId=null;
            for(Iterator iterator=coll.iterator();iterator.hasNext();){
                BpssProcessSpecEntry tmpProcessSpecEntry=(BpssProcessSpecEntry)iterator.next();
                if(tmpProcessSpecEntry.getUId()==parentSpecEntry.getParentEntryUId()
                    && tmpProcessSpecEntry.getEntryType().equals(BpssBusinessTrans.ENTITY_NAME)){
                    businessTransUId=new Long(tmpProcessSpecEntry.getEntryUId());
                    break;
                }
            }
            if(businessTransUId!=null){
                Collection businessTransActColl=getDefinationHt(BpssBusinessTransActivity.ENTITY_NAME);
                for(Iterator iterator=businessTransActColl.iterator();iterator.hasNext();){
                    BpssBusinessTransActivity businessTransActivity=(BpssBusinessTransActivity)iterator.next();
                    if(businessTransActivity.getBusinessTransUId().equals(businessTransUId)){
                        return (BpssBinaryCollaboration)getParentEntity((Long)businessTransActivity.getKey(),BpssBusinessTransActivity.ENTITY_NAME,BpssBinaryCollaboration.ENTITY_NAME);
                    }
                }
            }
        }
        return null;
    }

    public Collection getChildEntities(Long entryUId,String entryType,String childEntryType) throws SystemException 
    {
        BpssProcessSpecEntry processSpecEntry=getBpssProcessSpecEntry(entryUId,entryType);

        Collection coll=getDefinationHt(BpssProcessSpecEntry.ENTITY_NAME);
        Collection uidColl=new Vector();
        for(Iterator iterator=coll.iterator();iterator.hasNext();){
            BpssProcessSpecEntry tmpProcessSpecEntry=(BpssProcessSpecEntry)iterator.next();
            if(tmpProcessSpecEntry.getParentEntryUId()==processSpecEntry.getUId() && tmpProcessSpecEntry.getEntryType().equals(childEntryType))
                uidColl.add(new Long(tmpProcessSpecEntry.getEntryUId()));
        }
        return getDefinationEntities(uidColl,childEntryType);
    }


    public AbstractEntity getParentEntity(Long entryUId,String entryType,String parentEntryType)  throws SystemException {
        BpssProcessSpecEntry processSpecEntry=getBpssProcessSpecEntry(entryUId,entryType);

        Collection coll=getDefinationHt(BpssProcessSpecEntry.ENTITY_NAME);
        for(Iterator iterator=coll.iterator();iterator.hasNext();){
            BpssProcessSpecEntry tmpProcessSpecEntry=(BpssProcessSpecEntry)iterator.next();
            if(tmpProcessSpecEntry.getUId()==processSpecEntry.getParentEntryUId() && tmpProcessSpecEntry.getEntryType().equals(parentEntryType))
                return getDefinationEntity(new Long(tmpProcessSpecEntry.getEntryUId()),parentEntryType);
        }
        return null;
    }

    public BpssProcessSpecEntry getBpssProcessSpecEntry(Long entryUId,String entryType) throws SystemException
    {
        BpssProcessSpecEntry processSpecEntry = getBpssProcessSpecEntryFromCache(entryUId, entryType);
        
        //GNDB00028507: TWX
        if(processSpecEntry == null)
        {
          Logger.log("BpssDefinitionCache: getBpssProcessSpecEntry() Can't find BpssProcessSpecEntry with entryUId "+entryUId+" entryType: "+entryType);
          refreshInternalCache();
          return getBpssProcessSpecEntryFromCache(entryUId, entryType);
        }
        else
        {
          return processSpecEntry;
        }
    }
    
    private BpssProcessSpecEntry getBpssProcessSpecEntryFromCache(Long entryUId,String entryType){
      Collection coll=getDefinationHt(BpssProcessSpecEntry.ENTITY_NAME);
      for(Iterator iterator=coll.iterator();iterator.hasNext();){
          BpssProcessSpecEntry processSpecEntry=(BpssProcessSpecEntry)iterator.next();
          if(processSpecEntry.getEntryUId()==entryUId.longValue() && processSpecEntry.getEntryType().equals(entryType))
              return processSpecEntry;
      }
      return null;
    } 
    
    public Collection getDefinationEntities(Collection uIdColl,String entityName) throws SystemException
    {
      Collection coll=getDefinationHt(entityName);
      Collection retColl=new Vector();
      for(Iterator iterator=coll.iterator();iterator.hasNext();){
          AbstractEntity entity=(AbstractEntity)iterator.next();
          if(uIdColl.contains(entity.getKey()))
              retColl.add(entity);
      }
      return retColl;
    }
    
    public AbstractEntity getDefinationEntity(Long uId,String entityName) throws SystemException
    {
      AbstractEntity abe = getDefEntityFromCache(uId, entityName);
      
      //GNDB00028507: TWX Invalid cache detected, refresh again. This happen in cluster mode after we update process def
      if(abe == null)
      {
        Logger.log("BpssDefinitionCache: getDefinationEntity with UID: "+uId+" entityName: "+entityName+" can't find from cache");
        refreshInternalCache();
      }
      else
      {
        return abe;
      }
      
      return getDefEntityFromCache(uId, entityName); //if it is null again, it may be due to some SystemException eg DB error
    }
    
    private AbstractEntity getDefEntityFromCache(Long uId, String entityName)
    {
      Collection coll=getDefinationHt(entityName);
      for(Iterator iterator=coll.iterator();iterator.hasNext();){
          AbstractEntity entity=(AbstractEntity)iterator.next();
          
          if(entity.getKey().equals(uId))
          {
              return entity;
          }
      }
      return null;

    }
    
    private void invalidateInternalCache()
    {
        definitionHt = new Hashtable();
        bpssProcessSpecification = null;
    }
    
    //GNDB00028507: TWX 24 Apr 2008
    private void refreshInternalCache() throws SystemException
    {
      Logger.log("BpssDefinitionCache: refreshInternalCache");
      synchronized(lock)
      {
        String specUUId = "";
        String specVersion = "";
        String specName = "";
        if(bpssProcessSpecification != null)
        {
          specUUId = bpssProcessSpecification.getSpecUUId();
          specVersion = bpssProcessSpecification.getSpecVersion();
          specName = bpssProcessSpecification.getSpecName();
        }
        
        invalidateInternalCache();
        
        try
        {
          Logger.log("BpssDefinitionCache: refreshInternalCache, reload definition for UUID: "+specUUId+" specVersion:"+specVersion+" specName:"+specName);
          loadDefinition(specName, specVersion, specUUId);
        }
        catch(Throwable th)
        {
          throw new SystemException("BpssDefinitionCache reload definition failed", th);
        }
      }
    }
    
    public String getCacheKey(){
      
      synchronized(lock)
      {
        return bpssProcessSpecification.getSpecName()+"/"+bpssProcessSpecification.getSpecVersion()+"/"+bpssProcessSpecification.getSpecUUId();
      }
    }

    public static BpssDefinitionCache getBpssDefinitionCache(String cacheKey) throws SystemException{
        List list=UtilString.split(cacheKey,"/");
        if(list!=null && list.size()==3){
            return getBpssDefinitionCache((String)list.get(0),(String)list.get(1),(String)list.get(2));
        }
        return null;
    }

    public static BpssDefinitionCache getBpssDefinitionCache(String specName,String specVersion,String specUUId) throws SystemException{
        String cacheKey=specName+"/"+specVersion+"/"+specUUId;
        BpssDefinitionCache bpssDefinitionCache=(BpssDefinitionCache)bpssDefinitionCacheHt.get(cacheKey);
        if(bpssDefinitionCache==null){
            synchronized(bpssDefinitionCacheHt){
                bpssDefinitionCache=(BpssDefinitionCache)bpssDefinitionCacheHt.get(cacheKey);
                if(bpssDefinitionCache==null){
                    bpssDefinitionCache=new BpssDefinitionCache(specName,specVersion,specUUId);
                    bpssDefinitionCacheHt.put(cacheKey,bpssDefinitionCache);
                }
            }
        }
        return bpssDefinitionCache;
    }

    public static void removeBpssDefinitionCache(String specName,String specVersion,String specUUId) throws SystemException
    {
        String cacheKey=specName+"/"+specVersion+"/"+specUUId;
        
        Logger.log("RemoveBpssDefinitionCahce : isExist CacheKey: "+cacheKey+" "+bpssDefinitionCacheHt.containsKey(cacheKey));;
        
        synchronized(bpssDefinitionCacheHt){
            bpssDefinitionCacheHt.remove(cacheKey);
        }
    }

    private Collection getDefinationHt(String entityName){
      
      synchronized(lock)
      {
        Collection coll=(Collection)definitionHt.get(entityName);
        if(coll==null)
            coll=new Vector();
        return coll;
      }
    }

    private static boolean eq(Object obj1,Object obj2){
        if(obj1==obj2 || (obj1!=null && obj2!=null && obj1.equals(obj2)))
            return true;
        return false;
    }

}