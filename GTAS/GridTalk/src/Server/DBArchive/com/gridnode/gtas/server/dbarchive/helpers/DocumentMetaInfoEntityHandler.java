/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentMetaInfoEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13, 2005        Tam Wei Xiang       Created
 * Jan 19, 2006        Tam Wei Xiang       Modified method: createDocumentMetaInfo(),
 *                                                          checkDuplicate()
 * Apr 27, 2006        Tam Wei Xiang       To handle the case where duplicate process
 *                                         instance ID + originatorId with associated diff doc 
 *                                         meta info can exist.
 *                                         Added method  findByPIIDAndOriginatorIDAndPIUID(...)
 *                                         Modifeid method createDocumentMetaInfo()  
 * May 17, 2006        Tam Wei Xiang       The creation of documentMetaInfo become slower
 *                                         while the record in estore become more.
 *                                         Change the way on creating the record.                                                                                              
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import java.util.Collection;

import javax.ejb.CreateException;

import com.gridnode.gtas.server.dbarchive.entities.ejb.IDocumentMetaInfoLocalHome;
import com.gridnode.gtas.server.dbarchive.entities.ejb.IDocumentMetaInfoLocalObj;
import com.gridnode.gtas.server.dbarchive.model.DocumentMetaInfo;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.dao.EntityDAOImpl;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.util.ServiceLocator;
/**
 *
 *
 * Tam Wei Xiang
 * 
 * @version 1.0
 * @since 1.0
 */
public class DocumentMetaInfoEntityHandler
						 extends LocalEntityHandler
{
	private DocumentMetaInfoEntityHandler()
  {
    super(DocumentMetaInfo.ENTITY_NAME);
  }

  /**
   * Get an instance of a DocumentMetaInfoEntityHandler.
   */
  public static DocumentMetaInfoEntityHandler getInstance()
  {
    DocumentMetaInfoEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(DocumentMetaInfo.ENTITY_NAME, true))
    {
      handler = (DocumentMetaInfoEntityHandler)EntityHandlerFactory.getHandlerFor(
                  DocumentMetaInfo.ENTITY_NAME, true);
    }
    else
    {
      handler = new DocumentMetaInfoEntityHandler();
      EntityHandlerFactory.putEntityHandler(DocumentMetaInfo.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }
  
  /**
   * Find the DocumentMetaInfo based on processInstanceID and partnerID.
   *
   * @param processInstanceID The processInstanceID of a process.
   * @param originatorID The partner's originatorID
   * @param sortFilter user indicate the document metainfo field we need to sort based on it.
   * @return the DocumentMetaInfo object
   */
  public Collection findByProcessInstanceIDAndOriginatorID(String processInstanceID, 
  			String originatorID, IDataFilter sortFilter) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    
    if(sortFilter!=null)
    {
    	filter.setOrderFields(sortFilter.getOrderFields(), sortFilter.getSortOrders());
    }
    
    filter.addSingleFilter(null, DocumentMetaInfo.Process_Instance_ID, 
    		filter.getEqualOperator(),processInstanceID, false);
    filter.addSingleFilter(filter.getAndConnector(), DocumentMetaInfo.Originator_ID, filter.getEqualOperator() ,
    		originatorID,false);

    Collection result = super.getEntityByFilterForReadOnly(filter);
    
    return result;
  } 
  
  /**
   * 26 Apr 2006 :Find the DocumentMetaInfo based on processInstanceID, partnerID, and processInstanceInfoUID.
   * @param processInstanceID The processInstanceID of a process.
   * @param originatorID The partner's originatorID
   * @param processInstanceUID process instance info UID
   * @param sortFilter user indicate the document metainfo field we need to sort based on it.
   * @return
   * @throws Exception
   */
  public Collection findByPIIDAndOriginatorIDAndPIUID(String processInstanceID, 
			String originatorID, Long processInstanceUID ,IDataFilter sortFilter) throws Exception
  {
  	DataFilterImpl filter = new DataFilterImpl();
    
    if(sortFilter!=null)
    {
    	filter.setOrderFields(sortFilter.getOrderFields(), sortFilter.getSortOrders());
    }
    
    filter.addSingleFilter(null, DocumentMetaInfo.Process_Instance_ID, 
    		filter.getEqualOperator(),processInstanceID, false);
    filter.addSingleFilter(filter.getAndConnector(), DocumentMetaInfo.Originator_ID, filter.getEqualOperator() ,
    		originatorID,false);
    filter.addSingleFilter(filter.getAndConnector(), DocumentMetaInfo.PROCESS_INSTANCE_INFO_UID, filter.getEqualOperator(),
    		processInstanceUID, false);
    
    Collection result = super.getEntityByFilterForReadOnly(filter);
    
    return result;
    
  }
  
  /**
   * Find the DocumentMetaInfo based on processInstanceID and partnerID.
   * The return result will be sorted based on the given filter
   * @param processInstanceID The processInstanceID of a process.
   * @param originatorID The partner's originatorID
   * @param filter It is responsible for sorting.
   * @return a group of DocumentMetaInfo UID (PK) or empty collection no record
   *         satisfy such a query.
   */
  public Collection findByPIIDAndOriginatorIDAndFilter(IDataFilter filter, String processInstanceID, 
  			String originatorID) throws Exception
  {
    filter.addSingleFilter(null, DocumentMetaInfo.Process_Instance_ID, 
    		filter.getEqualOperator(),processInstanceID, false);
    filter.addSingleFilter(filter.getAndConnector(), DocumentMetaInfo.Originator_ID, filter.getEqualOperator() ,
    		originatorID,false);
    Collection result = super.getKeyByFilterForReadOnly(filter);
    
    return result;
  } 
  
  
  /**
   * This create method will check whether the record we are going to insert
   * is existing in DB table or not. IF it doesn't, it will insert the doc info.
   * 
   * @param metaInfo The document metainfo that we will insert to DB
   * @param doc The DocInfo object
   */
  public void createDocumentMetaInfo(DocumentMetaInfo metaInfo)
  			 throws Exception
  {
  	try
  	{
  		//19012006 change of method signature
  		checkDuplicate(metaInfo.getGdocID(), metaInfo.getFolder());
  	}
  	catch(DuplicateEntityException ex)
  	{
  		Logger.log("[DocumentMetaInfoEntityHandler.createDocumentMetaInfo] Duplicate entry found in DB, gdocID "+metaInfo.getGdocID()+" folder is "+ metaInfo.getFolder());
  		//update the entity instead
  		try
  		{
  			DocumentMetaInfo duplicateInfo = findByGdocIDAndFolder(metaInfo.getGdocID(), metaInfo.getFolder());  
  			
  			//30 Mar metainfo contain the latest data
  			metaInfo.setVersion(duplicateInfo.getVersion());
  			metaInfo.setKey(duplicateInfo.getKey());
  			metaInfo.setCanDelete(duplicateInfo.canDelete());
  			metaInfo.setProcessInstanceInfoUID(duplicateInfo.getProcessInstanceInfoUID()); //the process instance uid not in
  			                                                                               //the record we are inserting.
  			//09052006 if the doc info already exist in DB, we will package them later into the zip file
  			//         that reside in the file path we save previously. This can help to reduce the usage of disk
  			//         space. The filename of the zip file compose of piID+originatorID+piStartTime
  			//         which is unique
//  			if(! isArchiveByDocument)
//  			{
  			metaInfo.setFilePath(duplicateInfo.getFilePath());
//  			}
  			
  			super.update(metaInfo);
  			return;
  		}
  		catch(EntityModifiedException ex1)
    	{
    		Logger.warn("[DocumentMetaInfoEntityHandler.createDocumentMetaInfo] App Exception ",ex1);
    		throw new UpdateEntityException(ex1.getMessage());
    	}
  		catch(Throwable e)
  		{
  			Logger.warn("[DocumentMetaInfoEntityHandler.createDocumentMetaInfo] Error ", e);
        throw new SystemException(
          "DocumentMetaInfoEntityHandler.createDocumentMetaInfo Error ",
          e);
  		}
  	}
  	try
  	{
  		//17052006
  		//super.create(metaInfo); can be quite slow when there is many record exist in DB
  		
  		EntityDAOImpl docMetaDAO = (EntityDAOImpl)super.getDAO();
  		Long uniqueKey = docMetaDAO.createNewKey(false);
  		metaInfo.setKey(uniqueKey);
  		
  		super.create(metaInfo, Boolean.TRUE);
  	}
  	catch(CreateException ex)
		{
			Logger.warn("[DocumentMetaInfoEntityHandler.createDocumentMetaInfo] Exception ",ex);
			throw new CreateEntityException(ex.getMessage());
		}
  	catch(Throwable ex)
  	{
  		Logger.warn("[DocumentMetaInfoEntityHandler.createDocumentMetaInfo] Error ", ex);
      throw new SystemException(
        "DocumentMetaInfoEntityHandler.createDocumentMetaInfo Error ",
        ex);
  	}
  }
  
  /**
   * 19012006: The uniqueness of DocumentMetaInfo is gdocID+folder
   * This checkDuplicate method is used by createDocumentMetaInfo
   * @param gdocID
   * @param folder
   * @throws Exception
   */
  public void checkDuplicate(Long gdocID, String folder) throws Exception
  {
  	DataFilterImpl filter = new DataFilterImpl();
  	filter.addSingleFilter(null,DocumentMetaInfo.GDOC_ID,filter.getEqualOperator(),
  												gdocID,false);
  	filter.addSingleFilter(filter.getAndConnector(),DocumentMetaInfo.Folder, filter.getEqualOperator(), 
  			                  folder,false);
  	if(super.getEntityCount(filter) > 0)
  	{
  		throw new DuplicateEntityException("[DocumentMetaInfoEntityHandler.checkDuplicate] Entry with GDOC ID "+gdocID+" " +
  				"and folder "+folder+" has already exist in table Document_Meta_Info");
  	}
  }
  
/**
   * 19012006 Find the DocumentMetaInfo based on GdocID+folder which is a unique key for
   * table document_meta_info.
   * @param gdocID  griddocument's gdocID
   * @param folder  griddocument's folder eg inbound, outbound etc  
   * @return the DocumentMetaInfo object
   */
  public DocumentMetaInfo findByGdocIDAndFolder(Long gdocID, String folder) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, DocumentMetaInfo.GDOC_ID,
    		filter.getEqualOperator(),gdocID, false);
    filter.addSingleFilter(filter.getAndConnector(),DocumentMetaInfo.Folder, 
    		filter.getEqualOperator(), folder,false);

    Collection result = super.getEntityByFilterForReadOnly(filter);
    if(result ==null || result.isEmpty())
    {
    	return null;
    }
    return (DocumentMetaInfo)(result.iterator()).next();
  } 
  
  /**
   * Locate the document meta info obj based on its UID (PK for table document_meta_info)
   * @param UID PK for table document_meta_info
   * @return document meta info obj
   * @throws Exception
   */
  public DocumentMetaInfo findByUID(Long UID) throws Exception
  {
  	DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, DocumentMetaInfo.UID,
    		filter.getEqualOperator(),UID, false);

    Collection result = super.getEntityByFilterForReadOnly(filter);
    if(result ==null || result.isEmpty())
    {
    	return null;
    }
    return (DocumentMetaInfo)(result.iterator()).next();
  }
  
  /**
   * Retrieve a collection of docType field from Document_Meta_Info table.
   * @return
   * @throws Exception
   */
  public Collection getDocumentTypes() throws Exception
  {
  	DataFilterImpl filter = new DataFilterImpl();
  	filter.setSelectFields(new Object[]{DocumentMetaInfo.Doc_Type,}, true);
  	return getDAO().getFieldValuesByFilter(DocumentMetaInfo.Doc_Type, filter);
  }
  

  /**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IDocumentMetaInfoLocalObj.class;
  }

	@Override
	protected Class getHomeInterfaceClass() throws Exception
	{
		return IDocumentMetaInfoLocalHome.class;
	}
}
