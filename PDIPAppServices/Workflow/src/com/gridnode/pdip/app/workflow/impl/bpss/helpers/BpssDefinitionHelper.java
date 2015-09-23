package com.gridnode.pdip.app.workflow.impl.bpss.helpers;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.gridnode.pdip.app.workflow.exceptions.WorkflowException;
import com.gridnode.pdip.app.workflow.util.IWorkflowConstants;
import com.gridnode.pdip.app.workflow.util.Logger;
import com.gridnode.pdip.base.gwfbase.bpss.helpers.BpssDefinitionCache;
import com.gridnode.pdip.base.gwfbase.bpss.model.*;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.KeyConverter;
import com.gridnode.pdip.framework.util.UtilEntity;

public class BpssDefinitionHelper {

    public static BpssDefinitionCache getBpssDefCacheFromDocumentType(String documentType) throws SystemException,WorkflowException{
        try{
            IDataFilter filter=new DataFilterImpl();
            filter.addSingleFilter(null,BpssDocumentEnvelope.BUSINESS_DOCUMENTID_REF,filter.getEqualOperator(),documentType,false);
            Collection coll=UtilEntity.getEntityByFilter(filter,BpssDocumentEnvelope.ENTITY_NAME,true);
            Logger.debug("[BpssDefinitionHelper.getBpssDefCacheFromDocumentType] doc env coll "+coll);
            if(coll!=null && coll.size()>0){
                BpssDocumentEnvelope docEnvelope=(BpssDocumentEnvelope)coll.iterator().next();
                filter=new DataFilterImpl();
                filter.addSingleFilter(null,BpssProcessSpecEntry.ENTRY_UID,filter.getEqualOperator(),docEnvelope.getKey(),false);
                filter.addSingleFilter(filter.getAndConnector(),BpssProcessSpecEntry.ENTRY_TYPE,filter.getEqualOperator(),BpssDocumentEnvelope.ENTITY_NAME,false);
                coll=UtilEntity.getEntityByFilter(filter,BpssProcessSpecEntry.ENTITY_NAME,true);
                if(coll!=null && coll.size()>0){
                    BpssProcessSpecEntry processSpecEntry=(BpssProcessSpecEntry)coll.iterator().next();
                    BpssProcessSpecification processSpecification=(BpssProcessSpecification)UtilEntity.getEntityByKey(processSpecEntry.getSpecUId(),BpssProcessSpecification.ENTITY_NAME,true);
                    return BpssDefinitionCache.getBpssDefinitionCache(processSpecification.getSpecName(),processSpecification.getSpecVersion(),processSpecification.getSpecUUId());
                }
            }
        }catch(Throwable th){
            throw new  SystemException("[BpssDefinitionHelper.getBpssDefCacheFromDocumentType] Error ",th);
        }
        throw new  WorkflowException("[BpssDefinitionHelper.getBpssDefCacheFromDocumentType] Unable to get BpssDefinitionCache using documentType="+documentType);
    }

    public static BpssBinaryCollaboration getBinaryCollaboration(String binaryCollaborationKey) throws Throwable {
        String processSpecName=KeyConverter.getValue(binaryCollaborationKey,2);
        String processSpecUUId=KeyConverter.getValue(binaryCollaborationKey,3);
        String processSpecVersion=KeyConverter.getValue(binaryCollaborationKey,4);
        String binaryCollaborationType=KeyConverter.getValue(binaryCollaborationKey,5);
        String binaryCollaborationName=KeyConverter.getValue(binaryCollaborationKey,6);
        return (BpssBinaryCollaboration)getBpssDefinition(binaryCollaborationName,binaryCollaborationType, processSpecName, processSpecUUId, processSpecVersion);
    }

    //TWX: 09 Dec 2005 To remove the processSpecName from the search criteria.
    public static Object getBpssDefinition(String definitionName,String definitionType,String processSpecName,String processSpecVersion,String processSpecUUId) throws Throwable {
        IDataFilter filter=new DataFilterImpl();
        //filter.addSingleFilter(null,BpssProcessSpecification.NAME,filter.getEqualOperator(),processSpecName,false);
        filter.addSingleFilter(null,BpssProcessSpecification.UUID,filter.getEqualOperator(),processSpecUUId,false);
        filter.addSingleFilter(filter.getAndConnector(),BpssProcessSpecification.VERSION,filter.getEqualOperator(),processSpecVersion,false);
        BpssProcessSpecification bpssProcessSpecification=(BpssProcessSpecification)UtilEntity.getEntityByFilter(filter,BpssProcessSpecification.ENTITY_NAME,true).iterator().next();

        filter=new DataFilterImpl();
        filter.addSingleFilter(null,BpssProcessSpecEntry.SPEC_UID,filter.getEqualOperator(),bpssProcessSpecification.getKey(),false);
        filter.addSingleFilter(filter.getAndConnector(),BpssProcessSpecEntry.ENTRY_TYPE,filter.getEqualOperator(),definitionType,false);
        filter.addSingleFilter(filter.getAndConnector(),BpssProcessSpecEntry.ENTRY_NAME,filter.getEqualOperator(),definitionName,false);
        BpssProcessSpecEntry bpssProcessSpecEntry=(BpssProcessSpecEntry)UtilEntity.getEntityByFilter(filter,BpssProcessSpecEntry.ENTITY_NAME,true).iterator().next();

        return UtilEntity.getEntityByKey(bpssProcessSpecEntry.getEntryUId(),definitionType,true);
    }



    public static Collection getDocumentEnvelopes(Long entryUId,String entryType) throws Throwable {
        Collection coll=getChildEntities(entryUId,entryType,BpssDocumentEnvelope.ENTITY_NAME);
        return coll;
    }

    public static Collection getDocumentation(Long entryUId,String entryType) throws Throwable {
        Collection coll=getChildEntities(entryUId,entryType,BpssDocumentation.ENTITY_NAME);
        return coll;
    }

    public static Collection getChildEntities(Long entryUId,String entryType,String childEntryType) throws Throwable {
        Collection coll=getChildProcessSpecEntry(entryUId,entryType,childEntryType);
        if(coll!=null && coll.size()>0){
            Collection uIdColl=new Vector();
            for(Iterator iterator=coll.iterator();iterator.hasNext();){
                uIdColl.add(new Long(((BpssProcessSpecEntry)iterator.next()).getEntryUId()));
            }
            IEntity entity=UtilEntity.createNewInstance(childEntryType,true);
            IDataFilter filter=new DataFilterImpl();
            filter.addDomainFilter(null,entity.getKeyId(),uIdColl,false);
            coll=UtilEntity.getEntityByFilter(filter,childEntryType,true);
            return coll;
        } else return null;
    }

    public static Collection getChildProcessSpecEntry(Long entryUId,String entryType,String childEntryType) throws Throwable{
        BpssProcessSpecEntry processSpecEntry=getProcessSpecEntry(entryUId,entryType);
        if(processSpecEntry!=null){
            IDataFilter filter=new DataFilterImpl();
            filter.addSingleFilter(null,BpssProcessSpecEntry.PARENT_ENTRY_UID,filter.getEqualOperator(),(Long)processSpecEntry.getKey(),false);
            filter.addSingleFilter(filter.getAndConnector(),BpssProcessSpecEntry.ENTRY_TYPE,filter.getEqualOperator(),childEntryType,false);
            return UtilEntity.getEntityByFilter(filter,BpssProcessSpecEntry.ENTITY_NAME,true);
        }
        return null;
    }


    public static BpssProcessSpecEntry findParentProcessSpecEntry(Long entryUId,String entryType,String parentEntryType) throws Throwable{
        BpssProcessSpecEntry processSpecEntry=getProcessSpecEntry(entryUId,entryType);
        return findParentProcessSpecEntry(processSpecEntry,parentEntryType);
    }

    public static BpssProcessSpecEntry findParentProcessSpecEntry(BpssProcessSpecEntry processSpecEntry,String parentEntryType) throws Throwable{
        if(processSpecEntry!=null && processSpecEntry.getParentEntryUId()>0){
            BpssProcessSpecEntry parentProcessSpecEntry=(BpssProcessSpecEntry)UtilEntity.getEntityByKey(new Long(processSpecEntry.getParentEntryUId()),BpssProcessSpecEntry.ENTITY_NAME,true);
            if(!parentEntryType.equals(parentProcessSpecEntry.getEntryType()))
                return findParentProcessSpecEntry(parentProcessSpecEntry,parentEntryType);
            else return parentProcessSpecEntry;
        }
        return null;
    }


    public static BpssProcessSpecEntry getProcessSpecEntry(Long entryUId,String entryType) throws Throwable{
        IDataFilter filter=new DataFilterImpl();
        filter.addSingleFilter(null,BpssProcessSpecEntry.ENTRY_UID,filter.getEqualOperator(),entryUId,false);
        filter.addSingleFilter(filter.getAndConnector(),BpssProcessSpecEntry.ENTRY_TYPE,filter.getEqualOperator(),entryType,false);
        Collection coll=UtilEntity.getEntityByFilter(filter,BpssProcessSpecEntry.ENTITY_NAME,true);
        if(coll==null || coll.size()!=1) return null;
        return (BpssProcessSpecEntry)coll.iterator().next();
    }

    public static String getProcessDefinitionKey(Long entryUId,String entryType) throws Throwable{
        String processDefKey="http://"+IWorkflowConstants.BPSS_ENGINE+"/";
        BpssProcessSpecEntry bpssProcessSpecEntry=getProcessSpecEntry(entryUId,entryType);
        BpssProcessSpecification bpssProcessSpecification=(BpssProcessSpecification)UtilEntity.getEntityByKey(bpssProcessSpecEntry.getSpecUId(),BpssProcessSpecification.ENTITY_NAME,true);
        processDefKey+=bpssProcessSpecification.getSpecName()+"/";
        processDefKey+=bpssProcessSpecification.getSpecVersion()+"/";
        processDefKey+=bpssProcessSpecification.getSpecUUId()+"/";
        processDefKey+=entryType+"/";
        processDefKey+=bpssProcessSpecEntry.getEntryName();
        return processDefKey;
    }
}