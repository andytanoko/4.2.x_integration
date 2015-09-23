/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: DefaultSearchEsPiDocumentManager.java
 * 
 * ***************************************************************************
 * Date               Author                  Changes
 * ***************************************************************************
 * Sep 21, 2006       Tam Wei Xiang           Created
 * Oct 12, 2006       Regina Zeng             Add user tracking ID, remark (forpi)
 * Dec 14  2006       Tam Wei Xiang           Added remark (indicate valid or invalid)
 *                                            for entity SearchEsDoc
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.Properties;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.model.document.IGridDocument;
import com.gridnode.gtas.model.dbarchive.IProcessInstanceMetaInfo;
import com.gridnode.gtas.model.dbarchive.docforpi.IDocumentMetaInfo;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * Manager for handling the search criteria page of EStore Process Instance.
 * 
 * It is responsible for taking care the entity IGTEntity.ENTITY_SEARCH_ES_PI_DOCUMENT
 * and IGTEntity.ENTITY_SEARCH_ES_DOC_DOCUMENT since they belongs to the same function
 * group.
 * 
 * @author Tam Wei Xiang
 * @author Regina Zeng
 * 
 * @since GT 4.0
 */
public class DefaultSearchEsPiPageManager extends DefaultAbstractManager
	implements IGTSearchEsPiPageManager
{

	DefaultSearchEsPiPageManager(DefaultGTSession gtSession) throws GTClientException
	{
		super(IGTManager.MANAGER_SEARCH_ES_PI_DOCUMENT, gtSession);

		// SearchEsDocDocument is not the primary entity so we need to do this
		// explicitly
		loadFmi(IGTEntity.ENTITY_SEARCH_ES_DOC_DOCUMENT);
	}

	@Override
	protected IEvent getGetEvent(Long uid) throws EventException
	{
		throw new UnsupportedOperationException("Get event is not supported.");
	}

	@Override
	protected IEvent getGetListEvent(IDataFilter filter) throws EventException
	{
		throw new UnsupportedOperationException("Get list event is not supported.");
	}

	@Override
	protected IEvent getDeleteEvent(Collection uids) throws EventException
	{
		throw new UnsupportedOperationException("Delete event is not supported.");
	}

	@Override
	/**
	 * This manager is responsible for handling two entity.
	 */
	protected AbstractGTEntity createEntityObject(String entityType) throws GTClientException
	{
		if (IGTEntity.ENTITY_SEARCH_ES_PI_DOCUMENT.equals(entityType))
		{
			return new DefaultGTSearchEsPiDocumentEntity();
		}
		else if(IGTEntity.ENTITY_SEARCH_ES_DOC_DOCUMENT.equals(entityType))
		{
			return new DefaultGTSearchEsDocDocumentEntity();
		}
		else
		{
			throw new UnsupportedOperationException(
																							"Manager:"
																									+ this
																									+ " cannot create entity object of type "
																									+ entityType);
		}
	}

	@Override
	protected void doUpdate(IGTEntity entity) throws GTClientException
	{
		throw new UnsupportedOperationException("Update event is not supported.");
	}

	@Override
	protected void doCreate(IGTEntity entity) throws GTClientException
	{
		throw new UnsupportedOperationException("Create event is not supported.");
	}

	@Override
	protected int getManagerType()
	{
		return IGTManager.MANAGER_SEARCH_ES_PI_DOCUMENT;
	}

	@Override
	protected String getEntityType()
	{
		return IGTEntity.ENTITY_SEARCH_ES_PI_DOCUMENT;
	}

	/**
	 * If the manager is responsible for any virtual entities, must override this
	 * and return true for virtual entity types.
	 * 
	 * @param entityType
	 * @return isVirtual
	 */
	@Override
	boolean isVirtual(String entityType)
	{
		return true;
	}

	@Override
	protected IGTFieldMetaInfo[] defineVirtualFields(String entityType) throws GTClientException
	{
		if (IGTEntity.ENTITY_SEARCH_ES_PI_DOCUMENT.equals(entityType))
		{
			Properties detail = null;
			IConstraint constraint = null;
			VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[14];
			int f = -1;

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsPi.processDef",
																							IGTSearchEsPiDocumentEntity.PROCESS_DEF);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "80");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsPi.processState",
																							IGTSearchEsPiDocumentEntity.PROCESS_STATE);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "20");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsPi.partnerID",
																							IGTSearchEsPiDocumentEntity.PARTNER_ID);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "15");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsPi.partnerName",
																							IGTSearchEsPiDocumentEntity.PARTNER_NAME);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "20");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsPi.processFromStartTime",
																							IGTSearchEsPiDocumentEntity.PROCESS_FROM_START_TIME);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "10");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsPi.fromSTHour",
																							IGTSearchEsPiDocumentEntity.FROM_ST_HOUR);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "5");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsPi.processToStartTime",
																							IGTSearchEsPiDocumentEntity.PROCESS_TO_START_TIME);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "10");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsPi.toSTHour",
																							IGTSearchEsPiDocumentEntity.TO_ST_HOUR);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "5");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsPi.docNo",
																							IGTSearchEsPiDocumentEntity.DOC_NO);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "50");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}
      
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI(
                                              "searchEsPi.userTrackingID",
                                              IGTSearchEsPiDocumentEntity.USER_TRACKING_ID);
        sharedVfmi[f].setMandatoryCreate(false);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(false);
        sharedVfmi[f].setValueClass("java.lang.String");
        detail = new Properties();
        detail.setProperty("type", "text");
        detail.setProperty("text.length.max", "80");
        constraint = new TextConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      
			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsPi.fromDocDate",
																							IGTSearchEsPiDocumentEntity.FROM_DOC_DATE);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "10");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsPi.toDocDate",
																							IGTSearchEsPiDocumentEntity.TO_DOC_DATE);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "10");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}
			
			{
        f++;
        //we will render the error msg(if user didn't specify any criteria) in the html element 
        //that is identified by this fieldname.
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsPi.formMsg",
																							IGTSearchEsPiDocumentEntity.FORM_MSG);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "80");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}
      
      {
        f++;
        sharedVfmi[f] = new VirtualSharedFMI(
                                              "searchEsPi.remark",
                                              IGTSearchEsPiDocumentEntity.REMARK);
        sharedVfmi[f].setMandatoryCreate(false);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(true);
        sharedVfmi[f].setValueClass("java.utils.Vector");
        sharedVfmi[f].setElementClass("java.lang.String");
        detail = new Properties();
        detail.setProperty("type", "enum");
        detail.setProperty("searchEsPi.remark.valid",
                            IProcessInstanceMetaInfo.REMARK_VALID);
        detail.setProperty("searchEsPi.remark.invalid",
                            IProcessInstanceMetaInfo.REMARK_INVALID);
        constraint = new EnumeratedConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
			
			return sharedVfmi;
		}
		else if (IGTEntity.ENTITY_SEARCH_ES_DOC_DOCUMENT.equals(entityType))
		{
			Properties detail = null;
			IConstraint constraint = null;
			VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[24];
			int f = -1;

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.partnerID",
																							IGTSearchEsDocDocumentEntity.PARTNER_ID);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "15");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.partnerName",
																							IGTSearchEsDocDocumentEntity.PARTNER_NAME);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "20");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.folder",
																							IGTSearchEsDocDocumentEntity.FOLDER);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(true);
				sharedVfmi[f].setValueClass("java.utils.Vector");
				sharedVfmi[f].setElementClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "enum");
				detail.setProperty("searchEsDoc.folder.import",
														IGridDocument.FOLDER_IMPORT);
				detail.setProperty("searchEsDoc.folder.export",
														IGridDocument.FOLDER_EXPORT);
				detail.setProperty("searchEsDoc.folder.inbound",
														IGridDocument.FOLDER_INBOUND);
				detail.setProperty("searchEsDoc.folder.outbound",
														IGridDocument.FOLDER_OUTBOUND);
				constraint = new EnumeratedConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.docType",
																							IGTSearchEsDocDocumentEntity.DOC_TYPE);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "12");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.fromCreateDate",
																							IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "10");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.fromCreateDateHour",
																							IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE_HOUR);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "5");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.toCreateDate",
																							IGTSearchEsDocDocumentEntity.TO_CREATE_DATE);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "10");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.toCreateDateHour",
																							IGTSearchEsDocDocumentEntity.TO_CREATE_DATE_HOUR);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "5");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.fromSentDate",
																							IGTSearchEsDocDocumentEntity.FROM_SENT_DATE);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "10");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.fromSentDateHour",
																							IGTSearchEsDocDocumentEntity.FROM_SENT_DATE_HOUR);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "5");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.toSentDate",
																							IGTSearchEsDocDocumentEntity.TO_SENT_DATE);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "10");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.toSentDateHour",
																							IGTSearchEsDocDocumentEntity.TO_SENT_DATE_HOUR);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "5");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.fromReceivedDate",
																							IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "10");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.fromReceivedDateHour",
																							IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE_HOUR);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "5");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.toReceivedDate",
																							IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "10");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.toReceivedDateHour",
																							IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE_HOUR);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "5");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.docNo",
																							IGTSearchEsDocDocumentEntity.DOC_NO);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "50");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.fromDocDate",
																							IGTSearchEsDocDocumentEntity.FROM_DOC_DATE);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "10");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.fromDocDateHour",
																							IGTSearchEsDocDocumentEntity.FROM_DOC_DATE_HOUR);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "5");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.toDocDate",
																							IGTSearchEsDocDocumentEntity.TO_DOC_DATE);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "10");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.toDocDateHour",
																							IGTSearchEsDocDocumentEntity.TO_DOC_DATE_HOUR);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "5");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}

			{
				f++;
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.userTrackingID",
																							IGTSearchEsDocDocumentEntity.USER_TRACKING_ID);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "80");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}
			
			{
				f++;
        //we will render the error msg(if user didn't specify any criteria) in the html element 
        //that is identified by this fieldname.
				sharedVfmi[f] = new VirtualSharedFMI(
																							"searchEsDoc.formMsg",
																							IGTSearchEsDocDocumentEntity.FORM_MSG);
				sharedVfmi[f].setMandatoryCreate(false);
				sharedVfmi[f].setMandatoryUpdate(false);
				sharedVfmi[f].setEditableCreate(true);
				sharedVfmi[f].setEditableUpdate(true);
				sharedVfmi[f].setDisplayableCreate(true);
				sharedVfmi[f].setDisplayableUpdate(true);
				sharedVfmi[f].setCollection(false);
				sharedVfmi[f].setValueClass("java.lang.String");
				detail = new Properties();
				detail.setProperty("type", "text");
				detail.setProperty("text.length.max", "80");
				constraint = new TextConstraint(detail);
				sharedVfmi[f].setConstraint(constraint);
			}
      
			{
        f++;
        sharedVfmi[f] = new VirtualSharedFMI(
                                              "searchEsDoc.remark",
                                              IGTSearchEsDocDocumentEntity.REMARK);
        sharedVfmi[f].setMandatoryCreate(false);
        sharedVfmi[f].setMandatoryUpdate(false);
        sharedVfmi[f].setEditableCreate(true);
        sharedVfmi[f].setEditableUpdate(true);
        sharedVfmi[f].setDisplayableCreate(true);
        sharedVfmi[f].setDisplayableUpdate(true);
        sharedVfmi[f].setCollection(true);
        sharedVfmi[f].setValueClass("java.utils.Vector");
        sharedVfmi[f].setElementClass("java.lang.String");
        detail = new Properties();
        detail.setProperty("type", "enum");
        detail.setProperty("searchEsDoc.remark.valid",
                           IGTSearchEsDocDocumentEntity.REMARK_VALID);
        detail.setProperty("searchEsDoc.remark.invalid",
                           IGTSearchEsDocDocumentEntity.REMARK_INVALID);
        constraint = new EnumeratedConstraint(detail);
        sharedVfmi[f].setConstraint(constraint);
      }
      
			return sharedVfmi;
		}
		else
		{
			return new IGTFieldMetaInfo[0];
		}
	}
	
	public IGTSearchEsDocDocumentEntity getSearchEsDocument() throws GTClientException
	{
		try
		{
			AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_SEARCH_ES_DOC_DOCUMENT);
			entity.setNewEntity(true);
			return (IGTSearchEsDocDocumentEntity)entity;
		}
		catch(Exception ex)
		{
			throw new GTClientException("[DefaultSearchEsPageManager.getSearchEsDocument] Error creating instance for SearchEsDocument. ", ex);
		}
	}
}
