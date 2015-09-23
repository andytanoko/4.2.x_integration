/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 27 2002   MAHESH              Created
 * Dec 14 2005    Tam Wei Xiang       To resolve the pass by value issue:
 *                                    use UtilEntity.updateEntity instead of
 *                                    UtilEntity.update
 * Feb 09 2007		Alain Ah Ming				Add error code to error logs. Otherwise, use warning log                                   
 */
package com.gridnode.pdip.base.data.facade.ejb;


import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.util.*;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.base.data.entities.model.*;
import com.gridnode.pdip.base.data.facade.exceptions.DataException;
import com.gridnode.pdip.base.data.facade.helpers.Logger;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.keygen.KeyGen;
import com.gridnode.pdip.framework.file.access.FileAccess;
import com.gridnode.pdip.framework.util.KeyConverter;
import com.gridnode.pdip.framework.util.UtilEntity;

/**
 * 
 * @author Mahesh
 * @since
 * @version GT 4.0 VAN
 */
public class DataManagerBean implements SessionBean {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4299572687556676609L;

		private SessionContext ctx;

    String DATA_FILE_DIR = "dataitem";
    String CONTEXT_NAME="ContextKey";

    private static final String DEF_ENCODING = "UTF-8";
    
    public void setSessionContext(SessionContext context) {
        ctx = context;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void ejbCreate() throws CreateException, RemoteException {
    }

    public Long createContextUId() throws DataException {
        try {
            return KeyGen.getNextId(CONTEXT_NAME);
        } catch (Throwable th) {
            throw new DataException("Unable to create contextUid", th);
        }
    }

    public String createData(IData data) throws DataException {
        return copyToEntity(data,null);
    }

    public String createDataDefinition(IDataDefinition dataDefinition) throws DataException{
        return copyToEntity(dataDefinition,null);
    }

    public DataItem getContextData(Long contextUId,String dataDefName,String dataDefType) throws DataException{
        try{
            IDataFilter filter = new DataFilterImpl();

            filter.addSingleFilter(null, DataItem.CONTEXT_UID, filter.getEqualOperator(), contextUId, false);
            filter.addSingleFilter(filter.getAndConnector(), DataItem.DATA_DEF_NAME, filter.getEqualOperator(), dataDefName, false);
            filter.addSingleFilter(filter.getAndConnector(), DataItem.DATA_DEF_TYPE, filter.getEqualOperator(), dataDefType, false);
            Collection coll=UtilEntity.getEntityByFilter(filter, DataItem.ENTITY_NAME, true);
            if(coll.size()!=1)
                throw new DataException("Query returned "+coll.size()+" Dataitem's for contextUid=" + contextUId + ",dataDefName=" + dataDefName+ ",dataDefType=" + dataDefType);
            else return (DataItem)coll.iterator().next();
        }catch(DataException e){
            throw e;
        }catch(Throwable th){
            throw new DataException("Unable to fetch contextd data with contextUid=" + contextUId + ",dataDefName=" + dataDefName+ ",dataDefType=" + dataDefType, th);
        }
    }

    /**
     * This method returns collection of DataItems whith the specified
     * contextUId.
     * @param contextUId
     * @return Collection
     * @throws DataException
     */
    public Collection getContextData(Long contextUId) throws DataException {
        try {
            IDataFilter filter = new DataFilterImpl();

            filter.addSingleFilter(null, DataItem.CONTEXT_UID, filter.getEqualOperator(), contextUId, false);
            return UtilEntity.getEntityByFilter(filter, DataItem.ENTITY_NAME, true);
        } catch (Throwable th) {
            throw new DataException("Unable to fetch contextd data with contextUid=" + contextUId, th);
        }
    }

    /**
     * Removes DataItems with the specified contextUId
     * @param contextUId
     * @throws DataException
     */
    public void removeContextData2(Long contextUId){
        try {
            Collection contextDataColl = getContextData(contextUId);
            Iterator dataitemIterator = contextDataColl.iterator();

            while (dataitemIterator.hasNext()) {
                DataItem dataItem = (DataItem) dataitemIterator.next();

                removeDataItem((Long) dataItem.getKey());
            }
        } catch (Throwable th) {
            throw new EJBException(new DataException("Unable to fetch contextd data with contextUid=" + contextUId, th));
        }
    }

    /**
     * Removes DataItems with the specified contextUId
     * @param contextUId
     * @throws DataException
     */
    public void removeContextData(Long contextUId){
        try {
            IDataFilter dataItemFilter = new DataFilterImpl();
            dataItemFilter.addSingleFilter(null, DataItem.CONTEXT_UID, dataItemFilter.getEqualOperator(), contextUId, false);
            Collection contextDataColl = UtilEntity.getEntityByFilter(dataItemFilter,DataItem.ENTITY_NAME,true);
            Hashtable dataItemUIdHt=new Hashtable();
            for(Iterator iterator=contextDataColl.iterator();iterator.hasNext();){
                DataItem dataItem = (DataItem) iterator.next();
                dataItemUIdHt.put(dataItem.getKey(),dataItem.getIsCopy());
            }

            IDataFilter dataItemLocationMapFilter=new DataFilterImpl();
            dataItemLocationMapFilter.addDomainFilter(null,DataItemLocationMap.DATA_ITEM_UID,dataItemUIdHt.keySet(),false);
            Collection dataItemLocationMapColl=UtilEntity.getEntityByFilter(dataItemLocationMapFilter,DataItemLocationMap.ENTITY_NAME,true);
            for(Iterator iterator=dataItemLocationMapColl.iterator();iterator.hasNext();){
                DataItemLocationMap dataItemLocationMap = (DataItemLocationMap) iterator.next();
                Boolean bool=(Boolean)dataItemUIdHt.get(dataItemLocationMap.getKey());
                if(bool!=null && bool.booleanValue()){
                    removeData(dataItemLocationMap.getDataLocation());
                }
            }
            UtilEntity.remove(dataItemLocationMapFilter,DataItemLocationMap.ENTITY_NAME,true);
            UtilEntity.remove(dataItemFilter,DataItem.ENTITY_NAME,true);
        } catch (Throwable th) {
            throw new EJBException(new DataException("Unable to fetch contextd data with contextUid=" + contextUId, th));
        }
    }


    /**
     * Creates DataItem for specified dataDefKey
     * @param dataDefKey
     * @param dataContextType
     * @param contextUId
     * @param isCopy
     * @return DataItem
     * @throws DataException
     */
    public DataItem createDataItem(String dataDefKey, String dataContextType, Long contextUId, boolean isCopy){
        try {
            IDataDefinition dataDefinition = getDataDefinition(dataDefKey);

            DataItem dataItem = new DataItem();
            dataItem.setDataDefKey(dataDefKey);
            dataItem.setDataContextType(dataContextType);
            dataItem.setContextUId(contextUId);
            dataItem.setIsCopy(new Boolean(isCopy));
            dataItem.setDataDefName(dataDefinition.getName());
            dataItem.setDataDefType(KeyConverter.getEntityName(dataDefKey));
            dataItem = (DataItem) UtilEntity.createEntity(dataItem, true);

            Map dataLocationKeysMap = dataDefinition.getDataLocationKeys();
            Iterator fieldNameKeys = dataLocationKeysMap.keySet().iterator();

            while (fieldNameKeys.hasNext()) {
                String dataFileldName = (String) fieldNameKeys.next();
                String dataLocation = (String) dataLocationKeysMap.get(dataFileldName);

                if (dataLocation != null) {
                    if (isCopy) {
                        dataLocation = copyData((Long) dataItem.getKey(), dataLocation, null);
                    }
                    createDataItemLocationMap((Long) dataItem.getKey(), dataFileldName, dataLocation);
                }
            }
            return dataItem;
        } catch (Throwable th) {
            throw new EJBException(new DataException("Unable to createDataItem with contextUid=" + contextUId, th));
        }
    }

    public DataItem createDataItem(Long dataItemUId, String dataContextType, Long contextUId){
        try {
            DataItem dataItem=getDataItem(dataItemUId);
            dataItem.setContextUId(contextUId);
            //dataItem.setIsCopy(new Boolean(isCopy));
            dataItem.setDataContextType(dataContextType);
            dataItem=(DataItem) UtilEntity.createEntity(dataItem, true);
            Collection dataItemLocationMapColl=getDataItemLocationMaps(dataItemUId);
            for(Iterator iterator=dataItemLocationMapColl.iterator();iterator.hasNext();){
                DataItemLocationMap dataItemLocationMap=(DataItemLocationMap)iterator.next();
                dataItemLocationMap.setDataItemUId((Long)dataItem.getKey());
                String dataLocation=dataItemLocationMap.getDataLocation();
                if(dataItem.getIsCopy().booleanValue()){
                    if(dataLocation!=null){
                        dataLocation=copyData((Long)dataItem.getKey(),dataLocation,null);
                    }
                }
                createDataItemLocationMap((Long)dataItem.getKey(),dataItemLocationMap.getDataFieldName(),dataLocation);
            }
            return dataItem;
        } catch (Throwable th) {
            throw new EJBException(new DataException("Unable to createDataItem with contextUId=" + contextUId, th));
        }
    }


    /**
     * Fetches the DataItem with the specified dataItemUId
     * @param dataItemUId
     * @return DataItem
     * @throws DataException
     */
    public DataItem getDataItem(Long dataItemUId) throws DataException {
        try {
            return (DataItem) UtilEntity.getEntityByKey(dataItemUId, DataItem.ENTITY_NAME, true);
        } catch (Throwable th) {
            throw new DataException("Unable to fetch DataItem with dataItemUId=" + dataItemUId, th);
        }
    }

    /**
     * Removes DataItem with the specified dataItemUId
     * @param dataItemUId
     * @throws DataException
     */
    public void removeDataItem(Long dataItemUId){
        try {
            DataItem dataItem = getDataItem(dataItemUId);
            boolean isCopy = dataItem.getIsCopy().booleanValue();

            Iterator dataItemLocationMaps = getDataItemLocationMaps(dataItemUId).iterator();

            while (dataItemLocationMaps.hasNext()) {
                DataItemLocationMap dataItemLocationMap = (DataItemLocationMap) dataItemLocationMaps.next();

                //System.out.println("removeDataItem :" + dataItemUId + " ,ISCOPY=" + isCopy);
                if (isCopy) {
                    removeData(dataItemLocationMap.getDataLocation());
                }
                UtilEntity.remove(dataItemLocationMap.getUId(), DataItemLocationMap.ENTITY_NAME, true);
            }
            UtilEntity.remove(dataItemUId, DataItem.ENTITY_NAME, true);
        } catch (Throwable th) {
            throw new EJBException(new DataException("Unable to removeDataItem with dataItemUId=" + dataItemUId, th));
        }
    }

    /**
     * Saves the content to the Master of DataItem specified by dataItemUId
     * @param dataItemUId
     * @throws DataException
     */
    public void saveToMaster(Long dataItemUId){
        try {
            DataItem dataItem = getDataItem(dataItemUId);
            //String dataDefKey=dataItem.getDataDefKey();
            boolean isCopy = dataItem.getIsCopy().booleanValue();

            if (isCopy) {
                IDataDefinition dataDefinition = getDataDefinition(dataItem.getDataDefKey());
                Map masterMap = dataDefinition.getDataLocationKeys();
                Map copyMap = getFieldDataLocationMaps(dataItemUId);

                Iterator fieldNameKeys = masterMap.keySet().iterator();

                while (fieldNameKeys.hasNext()) {
                    String dataFileldName = (String) fieldNameKeys.next();
                    String masterDataLocation = (String) masterMap.get(dataFileldName);
                    String copyDataLocation = (String) copyMap.get(dataFileldName);

                    copyData(null, copyDataLocation, masterDataLocation);
                }
            }
        } catch (Exception e) {
            throw new EJBException(new DataException("Unable to saveToMaster with dataItemUId=" + dataItemUId, e));
        }
    }

    /**
     * Fetches the datalocations for the specified dataItemUId
     * @param dataItemUId
     * @return Map of fieldnames and datalocation
     * @throws DataException
     */
    public Map getFieldDataLocationMaps(Long dataItemUId) throws DataException {
        try {
            Map map = new HashMap();
            Collection coll = getDataItemLocationMaps(dataItemUId);

            if (coll != null) {
                Iterator iterator = coll.iterator();

                while (iterator.hasNext()) {
                    DataItemLocationMap dataItemLocationMap = (DataItemLocationMap) iterator.next();

                    map.put(dataItemLocationMap.getDataFieldName(), dataItemLocationMap.getDataLocation());
                }
            }
            return map;
        } catch (Exception e) {
            throw new DataException("Unable to getFieldDataLocationMaps with dataItemUId=" + dataItemUId, e);
        }
    }

    /**
     * Creates DataItemLocationMap with dataItemUId,dataFileldName,dataLocation
     * @param dataItemUId
     * @param dataFileldName
     * @param dataLocation
     * @return DataItemLocationMap
     * @throws DataException
     */
    public DataItemLocationMap createDataItemLocationMap(Long dataItemUId, String dataFileldName, String dataLocation) throws DataException {
        try {
            DataItemLocationMap dataItemLocationMap = new DataItemLocationMap();

            dataItemLocationMap.setDataItemUId(dataItemUId);
            dataItemLocationMap.setDataFieldName(dataFileldName);
            dataItemLocationMap.setDataLocation(dataLocation);
            dataItemLocationMap = (DataItemLocationMap) UtilEntity.createEntity(dataItemLocationMap, true);
            return dataItemLocationMap;
        } catch (DataException de) {
            throw de;
        } catch (Throwable th) {
            throw new DataException("Unable to createDataItemLocationMap with dataItemUId=" + dataItemUId + ", dataLocation=" + dataLocation, th);
        }
    }

    /**
     * Gets the DataItemLocationMap with the specified dataItemLocationMapUId
     * @param dataItemLocationMapUId
     * @return DataItemLocationMap
     * @throws DataException
     */
    public DataItemLocationMap getDataItemLocationMap(Long dataItemLocationMapUId) throws DataException {
        try {
            return (DataItemLocationMap) UtilEntity.getEntityByKey(dataItemLocationMapUId, DataItemLocationMap.ENTITY_NAME, true);
        } catch (Throwable th) {
            throw new DataException("Unable to getDataItemLocationMap with dataItemLocationMapUId=" + dataItemLocationMapUId, th);
        }
    }

    /**
     * Fetches the DataItemLocationMap's for the specified dataItemUId
     * @param dataItemUId
     * @return Collection
     * @throws DataException
     */
    public Collection getDataItemLocationMaps(Long dataItemUId) throws DataException {
        try {
            IDataFilter filter = new DataFilterImpl();

            filter.addSingleFilter(null, DataItemLocationMap.DATA_ITEM_UID, filter.getEqualOperator(), dataItemUId, false);
            return UtilEntity.getEntityByFilter(filter, DataItemLocationMap.ENTITY_NAME, true);
        } catch (Throwable th) {
            throw new DataException("Unable to getDataItemLocationMaps with dataItemUId=" + dataItemUId, th);
        }
    }

    /**
     * Fetches DataDefinition with the specified dataDefKey
     * @param dataDefKey
     * @return
     * @throws DataException
     */
    public IDataDefinition getDataDefinition(String dataDefKey) throws DataException {
        try {
            return (IDataDefinition) getEntityByKey(dataDefKey);
        } catch (Throwable th) {
            throw new DataException("Unable to getDataDefinition with dataDefKey=" + dataDefKey, th);
        }
    }

    public IDataDefinition getDataDefinition(String dataDefName,String dataDefType) throws DataException {
        try {
            IDataFilter filter=new DataFilterImpl();
            filter.addSingleFilter(null,IDataDefinition.NAME,filter.getEqualOperator(),dataDefName,false);
            Collection coll=UtilEntity.getEntityByFilter(filter,dataDefType,true);
            if(coll!=null && coll.size()>0)
                return (IDataDefinition)coll.iterator().next();
            else return null;
        } catch (Throwable th) {
            throw new DataException("Unable to getDataDefinition with dataDefName=" + dataDefName, th);
        }
    }


    public IEntity getEntityByKey(String key) throws DataException {
        try {
            Long uId = KeyConverter.getUID(key);
            String entityName = KeyConverter.getEntityName(key);

            return UtilEntity.getEntityByKey(uId, entityName, true);
        } catch (Throwable th) {
            throw new DataException("Unable to getEntityByKey with key=" + key, th);
        }
    }

    /**
     * Copies the sorce location to destinition
     * if destinition location is null it will  copy to new location
     * and returns new location
     * @param dataItemUId
     * @param srcDataLocation
     * @param desDataLocation
     * @return String destinition location
     * @throws DataException
     */
    private String copyData(Long dataItemUId, String srcDataLocation, String desDataLocation) throws DataException {
        try {
            //System.out.println("Inside copyData , dataItemUId:" + dataItemUId + ", srcDataLocation:" + srcDataLocation + ", desDataLocation:" + desDataLocation);
            boolean isMem = false;
            Long contextUId = null;

            if (dataItemUId != null) {
                DataItem dataItem = getDataItem(dataItemUId);
                String defKey = dataItem.getDataDefKey();
                String type = KeyConverter.getType(defKey);

                contextUId = dataItem.getContextUId();
                isMem = IData.MEM_TYPE.equals(type);
            }
            String srcDataCacheName = null, desDataCacheName = null;
            String srcType = null, desType = null;

            if (srcDataLocation != null) {
                srcType = KeyConverter.getType(srcDataLocation);
                if (IData.MEM_TYPE.equals(srcType))
                    srcDataCacheName = KeyConverter.getEntityName(srcDataLocation);
            }

            if (desDataLocation != null) {
                desType = KeyConverter.getType(desDataLocation);
                if (IData.MEM_TYPE.equals(desType))
                    desDataCacheName = KeyConverter.getEntityName(desDataLocation);
            } else if (isMem) {
                desType = IData.MEM_TYPE;
                desDataCacheName = contextUId.toString();
            } else if (srcType != null && IData.ENTITY_TYPE.equals(srcType)) {
                desType = srcType;
            }

            Object srcObj = null;

            if (IData.MEM_TYPE.equals(srcType)) {
                srcObj = MemoryDataCache.getInstance(srcDataCacheName).getData(srcDataLocation);
            } else if (IData.ENTITY_TYPE.equals(srcType)) {
                Long uId = KeyConverter.getUID(srcDataLocation);
                String entityName = KeyConverter.getEntityName(srcDataLocation);

                srcObj = UtilEntity.getEntityByKey(uId, entityName, true);
            } else if (IData.FILE_TYPE.equals(srcType)) {
                String domain = KeyConverter.getValue(srcDataLocation, 2);
                String filePath = decodeStr(KeyConverter.getValue(srcDataLocation, 3));
                String newFilePath = null;

                if (desDataLocation == null) {
                    newFilePath = DATA_FILE_DIR + File.separatorChar + dataItemUId + File.separatorChar + filePath;
                } else {

                    newFilePath = decodeStr(KeyConverter.getValue(desDataLocation, 3));
                    //newFilePath = DATA_FILE_DIR + File.separatorChar + dataItemUId + File.separatorChar + newFilePath;
                    //newFilePath = DATA_FILE_DIR + File.separatorChar + newFilePath;
                }
                //System.out.println("makeDataCopy :src=" + filePath);
                //System.out.println("makeDataCopy :des=" + newFilePath);
                FileAccess fileAccess = new FileAccess();

                fileAccess.copyFile(domain, filePath, newFilePath, true);
                desDataLocation = KeyConverter.getKey(srcDataLocation, 3, encodeStr(newFilePath));
            }

            if (srcObj != null && desType != null && IData.MEM_TYPE.equals(desType)) {
                desDataLocation = copyToMem(srcObj, desDataCacheName, desDataLocation);
            } else if (srcObj != null && desType != null && IData.ENTITY_TYPE.equals(desType)) {
                desDataLocation = copyToEntity(srcObj, desDataLocation);
            }
            //System.out.println("desDataLocation:" + desDataLocation);
            return desDataLocation;
        } catch (Throwable th) {
            throw new DataException("Unable to copyData with srcDataLocation=" + srcDataLocation + ", desDataLocation=" + desDataLocation, th);
        }
    }

    private String copyToEntity(Object obj, String desDataLocation) throws DataException {
        try {
            AbstractEntity entity = (AbstractEntity) obj;

            if (desDataLocation != null) {
                Long uId = KeyConverter.getUID(desDataLocation);
                String entityName = KeyConverter.getEntityName(desDataLocation);
                AbstractEntity desEntity = (AbstractEntity) UtilEntity.getEntityByKey(uId, entityName, true);

                entity.setKey(uId);
                entity.setVersion(desEntity.getVersion());
                entity = (AbstractEntity)UtilEntity.updateEntity(entity, true);
            } else {
                //entity.setKey(null);
                entity = (AbstractEntity) UtilEntity.createEntity(entity, true);
            }
            return KeyConverter.getKey(new Long(entity.getUId()), entity.getEntityName(), IData.ENTITY_TYPE);
        } catch (Throwable th) {
            throw new DataException("Unable to copyToEntity with desDataLocation=" + desDataLocation, th);
        }
    }

    private String copyToMem(Object obj, String dataCacheName, String desDataLocation) throws DataException {
        try {
            if (desDataLocation != null)
                MemoryDataCache.getInstance(dataCacheName).putData(desDataLocation, obj);
            else desDataLocation = MemoryDataCache.getInstance(dataCacheName).putData(obj);
            return desDataLocation;
        } catch (Exception e) {
            throw new DataException("Unable to copyToMem with desDataLocation=" + desDataLocation, e);
        }
    }

    private void removeData(String dataLocation) throws DataException {
        try {
            //System.out.println("inside removeData ,dataLocation:" + dataLocation);
            String type = KeyConverter.getType(dataLocation);

            if (IData.MEM_TYPE.equals(type)) {
                String dataCacheName = KeyConverter.getEntityName(dataLocation);
                //Object obj = 
                MemoryDataCache.getInstance(dataCacheName).removeData(dataLocation);

                //System.out.println("removeData from MemoryDataCache " + obj);
            } else if (IData.FILE_TYPE.equals(type)) {
                String domain = KeyConverter.getValue(dataLocation, 2);
                String filePath = decodeStr(KeyConverter.getValue(dataLocation, 3));
                FileAccess fileAccess = new FileAccess();

                fileAccess.deleteFile(domain, filePath);
            } else if (IData.ENTITY_TYPE.equals(type)) {
                Long uId = KeyConverter.getUID(dataLocation);
                String entityName = KeyConverter.getEntityName(dataLocation);

                UtilEntity.remove(uId, entityName, true);
            }
        } catch (Throwable th) {
            throw new DataException("Unable to removeData with dataLocation=" + dataLocation, th);
        }
    }

    public Object getObjectData(String dataLocation) throws DataException {
        try{
            String type=KeyConverter.getType(dataLocation);
            if(IData.FILE_TYPE.equals(type)){
                String domain=KeyConverter.getValue(dataLocation,2);
                String filePath=decodeStr(KeyConverter.getValue(dataLocation,3));
                FileAccess fileAccess=new FileAccess();
                File file=fileAccess.getFile(domain,filePath);
                return fileAccess.readFromFile(domain,filePath,1,(int)file.length());
            } else if(IData.ENTITY_TYPE.equals(type)) {
                Long uId=KeyConverter.getUID(dataLocation);
                String entityName=KeyConverter.getEntityName(dataLocation);
                IData data =(IData)UtilEntity.getEntityByKey(uId,entityName,true);
                return AbstractEntity.convert(data.getData(),data.getDataType());
            } else if(IData.MEM_TYPE.equals(type)){
                String dataCacheName = KeyConverter.getEntityName(dataLocation);
                return MemoryDataCache.getInstance(dataCacheName).removeData(dataLocation);
            }
            return null;
        }catch(Throwable th){
            throw new DataException("Unable to getObjectData dataLocation="+dataLocation,th);
        }
    }

    public void  setObjectData(String dataLocation,Object obj) throws DataException {
        try{
            String type=KeyConverter.getType(dataLocation);
            if(IData.FILE_TYPE.equals(type)){
                String domain=KeyConverter.getValue(dataLocation,2);
                String filePath=decodeStr(KeyConverter.getValue(dataLocation,3));
                System.out.println("setObjectData filePath="+filePath);
                System.out.println("setObjectData setData="+new String((byte[])obj));
                FileAccess fileAccess=new FileAccess();
                fileAccess.writeToFile(domain,filePath,(byte[])obj,true);
            } else if(IData.ENTITY_TYPE.equals(type)) {
                Long uId=KeyConverter.getUID(dataLocation);
                String entityName=KeyConverter.getEntityName(dataLocation);
                IData data =(IData)UtilEntity.getEntityByKey(uId,entityName,true);
                data.setData(obj);
                UtilEntity.update((IEntity)data,true);
            } else if(IData.ENTITY_TYPE.equals(type)) {
                String dataCacheName = KeyConverter.getEntityName(dataLocation);
                MemoryDataCache.getInstance(dataCacheName).putData(dataLocation, obj);
            }
        }catch(Throwable th){
            throw new DataException("Unable to setObjectData dataLocation="+dataLocation,th);
        }
    }

    private static String decodeStr(String val)
    {
    	try
    	{
    		return URLDecoder.decode(val, DEF_ENCODING);
    	}
    	catch (Exception e)
    	{
    		Logger.warn("[DataManagerBean.decodeStr] Error", e);
    		return val;
    	}
    }
    
    private static String encodeStr(String val)
    {
    	try
    	{
    		return URLEncoder.encode(val, DEF_ENCODING);
    	}
    	catch (Exception e)
    	{
    		Logger.warn("[DataManagerBean.encodeStr] Error", e);
    		return val;
    	}
    }

}
