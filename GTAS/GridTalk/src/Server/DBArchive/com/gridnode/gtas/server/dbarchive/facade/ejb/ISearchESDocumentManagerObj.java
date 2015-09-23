/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchDocumentManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 8, 2005        Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.dbarchive.facade.ejb;

import com.gridnode.gtas.server.dbarchive.model.AuditFileInfo;
import com.gridnode.gtas.server.dbarchive.model.DocumentMetaInfo;
import com.gridnode.gtas.server.dbarchive.model.FieldValueCollection;
import com.gridnode.gtas.server.dbarchive.model.ProcessInstanceMetaInfo;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.EJBObject;

/**
 * Remote Interface for SearchESDocumentManagerBean
 *
 * @author Tam Wei Xiang
 * 
 * @version
 * @since
 */
public interface ISearchESDocumentManagerObj extends EJBObject
{
	/**
   * Locate a number of DocumentMEtaInfo Obj that satisfy the
   * filtering condition.
   *
   * @param processInstanceID 
   * @param originatorID
   * @return a Collection of DocumentMEtaInfo found, or empty collection if none
   * exists.
   */
	public Collection findByProcessInstanceIDAndOriginatorID(String processInstanceID, String originatorID)
		throws FindEntityException, SystemException, RemoteException;
	
/**
	 * Locate a number of ProcessInstanceMetaInfo UID(PK) based on the given search criteria.
	 * @param The filter is created at action side. It is generated based on the criteria a 
	 * users key in (eg they wanna search doc meta info based on doc no they specified).
	 */
	public Collection findEsPiKeys(IDataFilter filter)
		throws FindEntityException, SystemException, RemoteException;
	
/**
	 * Locate a list of ProcessInstance MetaInfo that fulfil the filter condition
	 * @param filter filter it contain a domain filter which take in list of doc UID.
	 * @return a list of ProcessInstance MetaInfo or empty list if not fulfil the filter
	 * 				 condition.
	 */
	public Collection findEsPiEntityList(IDataFilter filter)
				 throws FindEntityException, SystemException, RemoteException;
	
	/**
	 * Locate a number of DocumentMetaInfo UID(PK) based on the given search criteria.
	 * The filter is created at action side. It is generated based on the criteria a 
	 * users key in (eg they wanna search doc meta info based on doc no they specified). 
	 * @param filter
	 * @return
	 * @throws FindEntityException
	 * @throws SystemException
	 */
	public Collection findEsDocKeys(IDataFilter filter) 
	    throws FindEntityException, SystemException, RemoteException;
	
	/**
	 * Locate a DocumentMetaInfo based on its UID
	 * @param uid DocumentMetaInfo's UID
	 * @return DocumentMetaInfo OBJ or null if not exist
	 * @throws FindEntityException
	 * @throws SystemException
	 */
	public DocumentMetaInfo findEsDoc(Long UID)
				 throws FindEntityException, SystemException, RemoteException;
	
/**
	 * Locate a collection of DocumentMEtaInfo obj based on the given search criteria
	 * @param filter it contain a domain filter which take in list of doc UID.
	 * @return
	 * @throws FindEntityException
	 * @throws SystemException
	 */
	public Collection findEsDocEntityList(IDataFilter filter)
	 				throws FindEntityException, SystemException, RemoteException;
	
	/**
	 * Locate a ProcessInstanceMetaInfo by its UID
	 * @param uid ProcessInstanceMetaInfo's UID
	 * @return ProcessInstanceMetaInfo OBJ or null if not exist
	 * @throws FindEntityException
	 * @throws SystemException
	 */
	public ProcessInstanceMetaInfo findEsPIByUID(Long uid)
				 throws FindEntityException, SystemException, RemoteException;
	
/**
	 * Retrieve a list of Document meta info UID which tie to a process instance meta info.
	 * The retrieved result will also be sorted based on the given filter.
	 * @param filter It will contain the UID of a PI Meta Info and sort criteria.
	 * @return a list of document meta info's UIDs(PK) or empty list if no record
	 *         satisfy such query.
	 */
	public Collection findAssocEsDocKeys(IDataFilter filter)
				 throws FindEntityException, SystemException, RemoteException;
	
/**
	 * Return a list of code from code_value table. 
	 * 
	 * ProcessDef, docType, processState will be pre-stored into code_value table while estoring.
	 * This will be efficient if we fetch the above elements from code_value table instead of
	 * fetching from process_instance_meta_info, document_meta_info table directly.
	 * @param UID 1 ProcessInstanceMetaInfo.processDefList  2 DocumentMetaInfo.docTypeList  3 ProcessInstanceMetaInfo.processStateList
	 * @return FieldValueCollection or null if no docType or process def found in Process_Instance_MetaInf
	 * 				 or document_meta_info table.
	 * @throws Exception
	 */
	public FieldValueCollection getProcessDef(Long UID) throws Exception, RemoteException;
	
	/**
	 * To locate the audit file info given the document meta info uid.
	 * The audit file info will be used by UI to render the audit file.
	 * @param UID the document meta info's uid
	 * @return
	 */
	public AuditFileInfo findAuditFileInfoByUID(Long UID)
		throws ApplicationException, RemoteException;
	
	/**
	 * To locate the receipt audit file info given the document meta info uid.
	 * The audit file info will be used by UI to render the audit file.
	 * @param UID
	 * @return
	 * @throws ApplicationException
	 */
	public AuditFileInfo findReceiptAuditInfoByUID(Long UID)throws ApplicationException, RemoteException;
	
  /**
   * Get the physical gdoc file given the DocumentMetaInfo UID. Udoc file will also be extracted.
   * @param uid the UID of DocumentMetaInfo obj.
   * @return the physical gdoc file or Null if the gdoc file is not existed
   * @throws ApplicationException if we have problem in getting the necessary file eg the gdoc and udoc file. 
   */
  public File getGDocInfoByDocUID(Long uid) throws ApplicationException, RemoteException;
}
