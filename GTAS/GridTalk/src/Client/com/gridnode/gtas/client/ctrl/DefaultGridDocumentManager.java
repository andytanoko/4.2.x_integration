package com.gridnode.gtas.client.ctrl;
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultGridDocumentManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-22     Andrew Hill         Created
 * 2002-10-08     Daniel D'Cotta      Modified handling of filenames
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2002-12-05     Andrew Hill         Attachment support (on hold)
 * 2003-01-29     Andrew Hill         Continue with attachment support mods
 * 2003-02-13     Jared Low           Filter away GDocs that does not have the
 *                                    attachments linked yet.
 * 2003-03-24     Andrew Hill         Modify attachment link filtering logic to use a DataFilterImpl
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 * 2003-10-15     Daniel D'Cotta      Added manual export
 * 2003-11-17     Daniel D'Cotta      Added support for Search
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.events.document.DeleteGridDocumentEvent;
import com.gridnode.gtas.events.document.EditDocumentEvent;
import com.gridnode.gtas.events.document.GetGridDocumentEvent;
import com.gridnode.gtas.events.document.GetGridDocumentListEvent;
import com.gridnode.gtas.events.document.ImportDocumentEvent;
import com.gridnode.gtas.events.document.ManualExportDocumentEvent;
import com.gridnode.gtas.events.document.ManualSendDocumentEvent;
import com.gridnode.gtas.model.document.IGridDocument;
import com.gridnode.pdip.framework.db.filter.DataFilterFactory;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

class DefaultGridDocumentManager extends DefaultAbstractManager
  implements IGTGridDocumentManager
{
  private static final String ATTACHMENT_PATH_KEY = "document.path.attachment"; //20030129AH

  DefaultGridDocumentManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_GRID_DOCUMENT, session);
    // Must manually invoke initialisation of the Virtual Entity MetaInfo
    loadFmi(IGTEntity.ENTITY_IMPORT_DOCUMENTS);
  }

  // 20031117 DDJ: For use by DefaultSearchDocumentManager sub-class
  DefaultGridDocumentManager(int managerType, DefaultGTSession session)
    throws GTClientException
  {
    super(managerType, session);
    loadFmi(IGTEntity.ENTITY_IMPORT_DOCUMENTS);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTGridDocumentEntity gridDocument = (IGTGridDocumentEntity)entity;
    try
    {
      Long   uid = (Long)gridDocument.getFieldValue(IGTGridDocumentEntity.UID);
      // Transfer of files to gtas is done outside of the ctrl package (breaking our standards completely)
      // As a result we need only inform GTAS the files were uploaded
      EditDocumentEvent event = new EditDocumentEvent(uid);
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    if(IGTEntity.ENTITY_IMPORT_DOCUMENTS.equals( entity.getType() ))
    {
      importDocuments((IGTImportDocuments)entity);
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Direct creation of GridDocument entities "
                + "not supported. Use an IGTImportDocuments object");
    }
  }

  public void importDocuments(IGTImportDocuments importDocs)
    throws GTClientException
  {
    if(importDocs == null)
    {
      throw new java.lang.NullPointerException("importDocs is null");
    }
    try
    {
      Boolean isManual = (Boolean)importDocs.getFieldValue(IGTImportDocuments.IS_MANUAL);
      if(isManual == null)
      {
        throw new java.lang.NullPointerException("isManual flag not initialised");
      }
      if(isManual.booleanValue())
      {
        String senderId = importDocs.getFieldString(IGTImportDocuments.SENDER_ID);
        String documentType = importDocs.getFieldString(IGTImportDocuments.DOC_TYPE);
        ArrayList importFiles = StaticUtils.arrayListValue(importDocs.getFieldStringArray(IGTImportDocuments.FILENAMES));
        ArrayList recipients = new ArrayList( (Collection)importDocs.getFieldValue(IGTImportDocuments.RECIPIENTS) );

        ArrayList attachments = StaticUtils.arrayListValue(
                                importDocs.getFieldStringArray(IGTImportDocuments.ATTACHMENTS)); //20021205AH

        ImportDocumentEvent event = new ImportDocumentEvent(senderId,
                                                            documentType,
                                                            importFiles,
                                                            recipients,
                                                            attachments);
        handleEvent(event);
      }
      else
      {
        throw new java.lang.UnsupportedOperationException("Inbound import not implemented yet");
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error importing documents",t);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_GRID_DOCUMENT;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_GRID_DOCUMENT;
  }

  protected IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetGridDocumentEvent(uid);
  }

  protected IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetGridDocumentListEvent(filter);
  }

  protected IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteGridDocumentEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_GRID_DOCUMENT.equals(entityType))
    {
      return new DefaultGridDocumentEntity();
    }
    else if(IGTEntity.ENTITY_IMPORT_DOCUMENTS.equals(entityType))
    {
      return new DefaultImportDocuments();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  /**
   * Retrive all GridDocs.
   *
   * Jared: Overrided to return only GridDocs that has the attachment link
   * updated.
   * Andrew: Refactor to do attachment link filtering stuff with a DataFilterImpl
   * nb: at this stage getByKey doesnt do this filtering!
   *
   * @returns A collection of IGridDocumentEntity.
   * @throws GTClientException
   */
   public Collection getAll() throws GTClientException
  {
    try
    {
      DataFilterImpl attachmentLinkFilter = getAttachmentLinkFilter();
      IEvent event = getGetListEvent(attachmentLinkFilter);
      return handleGetListEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error processing getAll() request", t);
    }
  }

/* 20031127 DDJ
  public IGTListPager getAllInFolder(String folder) throws GTClientException
  { //20030324AH
    try
    {
      if(! this.supportsGtasListPaging() )
      { //Internal assertion
        throw new UnsupportedOperationException("This operation requires gtas list paging support");
      }

      DataFilterFactory ff = new DataFilterFactory();
      DataFilterImpl folderFilter = new DataFilterImpl();
      folderFilter.addSingleFilter(null,IGridDocument.FOLDER,ff.getEqualOperator(),folder,false);
      DataFilterImpl attachmentLinkFilter = getAttachmentLinkFilter();
      DataFilterImpl filter = (attachmentLinkFilter == null)  ? folderFilter
                                                              : new DataFilterImpl( attachmentLinkFilter,
                                                                                    ff.getAndConnector(),
                                                                                    folderFilter);
      ListPager listPager = new ListPager();
      listPager.setManager(this);
      listPager.setSession(_session);
      listPager.setFilter(filter);
      return listPager;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error attempting to get ListPager for documents in folder '"
                                  + folder +"'",t);
    }
  }
*/

  protected DataFilterImpl getAttachmentLinkFilter() throws GTClientException
  { //20030324AH - Move functionality of filterNonAttachmentLinkUpdated to a DataFilter
    //@todo: this filter doesnt change. We should cache it.
    DataFilterFactory ff = new DataFilterFactory();

    DataFilterImpl hua = new DataFilterImpl(); //has updated attachment filter
    hua.addSingleFilter(null,
                        IGridDocument.HAS_ATTACHMENT,
                        ff.getEqualOperator(),
                        Boolean.TRUE,
                        false);
    hua.addSingleFilter(hua.getAndConnector(),
                        IGridDocument.IS_ATTACHMENT_LINK_UPDATED,
                        ff.getEqualOperator(),
                        Boolean.TRUE,
                        false);

    DataFilterImpl noa = new DataFilterImpl(); //no attachment filter
    noa.addSingleFilter(null,
                        IGridDocument.HAS_ATTACHMENT,
                        ff.getEqualOperator(),
                        Boolean.FALSE,
                        false);


    //20030324AH - NOT(hasAttachment==true && linkUpdated==false) might be a simpler way of saying it?

    DataFilterImpl attachmentLinkFilter = new DataFilterImpl(hua, ff.getOrConnector(), noa);

    return attachmentLinkFilter;
  }

  /**
   * The newEntity() is a special case for the Grid Document Manager. It returns an ImportDocuments
   * virtual entity, whose fields may be set and then apssed to the create() method to perform an
   * import. Direct programmatic creation of GridDocument entities is not possible.
   * @return IGTImportDocument object
   * @throws GTClientException
   */
  public IGTEntity newEntity() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_IMPORT_DOCUMENTS);
    entity.setNewEntity(true);
    return entity;
  }

  boolean isVirtual(String entityType)
  {
    if(IGTEntity.ENTITY_IMPORT_DOCUMENTS.equals(entityType))
    {
      return true;
    }
    return false;
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_GRID_DOCUMENT.equals(entityType))
    { //20021209
      Properties detail = null;
      IConstraint constraint = null;
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[1];
      //attachmentFilenames
      //Collection of filepath strings
      sharedVfmi[0] = new VirtualSharedFMI("gridDocument.attachmentFilenames",
                                            IGTGridDocumentEntity.ATTACHMENT_FILENAMES);
      sharedVfmi[0].setMandatoryCreate(false);
      sharedVfmi[0].setCollection(true);
      sharedVfmi[0].setValueClass("java.util.Collection");
      sharedVfmi[0].setElementClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("file.downloadable","true");
      detail.setProperty("file.fixedKey",ATTACHMENT_PATH_KEY);
      constraint = new FileConstraint(detail);
      sharedVfmi[0].setConstraint(constraint);

      return sharedVfmi;
    }
    else if(IGTEntity.ENTITY_IMPORT_DOCUMENTS.equals(entityType))
    {
      Properties detail = null;
      IConstraint constraint = null;

      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[7];
      //senderId
      //A businessEntity.id references
      sharedVfmi[0] = new VirtualSharedFMI("importDocuments.senderId", IGTImportDocuments.SENDER_ID);
      sharedVfmi[0].setMandatoryCreate(true);
      sharedVfmi[0].setCollection(false);
      sharedVfmi[0].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","businessEntity.id");
      detail.setProperty("foreign.display","businessEntity.id");
      detail.setProperty("foreign.cached","false");
      constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[0].setConstraint(constraint);

      //docType
      //A documentType.docType references
      sharedVfmi[1] = new VirtualSharedFMI("importDocuments.docType", IGTImportDocuments.DOC_TYPE);
      sharedVfmi[1].setMandatoryCreate(true);
      sharedVfmi[1].setCollection(false);
      sharedVfmi[1].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","documentType.docType");
      detail.setProperty("foreign.display","documentType.docType");
      detail.setProperty("foreign.cached","false");
      constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[1].setConstraint(constraint);

      //filenames
      //Collection of filepath strings
      sharedVfmi[2] = new VirtualSharedFMI("importDocuments.filenames", IGTImportDocuments.FILENAMES);
      sharedVfmi[2].setMandatoryCreate(true);
      sharedVfmi[2].setCollection(true);
      sharedVfmi[2].setValueClass("java.lang.Object[]");
      sharedVfmi[2].setElementClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("file.pathKey","importDocuments.pathKey"); // hmmm...
      constraint = new FileConstraint(detail);
      sharedVfmi[2].setConstraint(constraint);

      //recipients
      //Collection of partner.partnerId references
      sharedVfmi[3] = new VirtualSharedFMI("importDocuments.recipients", IGTImportDocuments.RECIPIENTS);
      sharedVfmi[3].setMandatoryCreate(true);
      sharedVfmi[3].setCollection(false);
      sharedVfmi[3].setValueClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","partner.partnerId");
      detail.setProperty("foreign.display","partner.partnerId");
      detail.setProperty("foreign.cached","false");
      constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[3].setConstraint(constraint);

      //isManual
      //Boolean specifiying if this is manual or inbound import
      sharedVfmi[4] = new VirtualSharedFMI("importDocuments.isManual", IGTImportDocuments.IS_MANUAL);
      sharedVfmi[4].setMandatoryCreate(true);
      sharedVfmi[4].setCollection(false);
      sharedVfmi[4].setValueClass("java.lang.Boolean");
      detail = new Properties();
      detail.setProperty("type","enum");
      detail.setProperty("importDocuments.isManual.true","true");
      detail.setProperty("importDocuments.isManual.false","false");
      constraint = new EnumeratedConstraint(detail);
      sharedVfmi[4].setConstraint(constraint);

      //gdocUids
      //Collection of gridDocument.uid references
      sharedVfmi[5] = new VirtualSharedFMI("importDocuments.gdocUids", IGTImportDocuments.G_DOC_UIDS);
      sharedVfmi[5].setMandatoryCreate(true);
      sharedVfmi[5].setCollection(true);
      sharedVfmi[5].setValueClass("java.util.Vector");
      sharedVfmi[5].setValueClass("java.lang.Long");
      detail = new Properties();
      detail.setProperty("type","foreign");
      detail.setProperty("foreign.key","gridDocument.uid");
      detail.setProperty("foreign.display","gridDocument.gdocId");
      detail.setProperty("foreign.cached","false");
      constraint = new ForeignEntityConstraint(detail);
      sharedVfmi[5].setConstraint(constraint);


      //attachments
      //Collection of filepath strings
      sharedVfmi[6] = new VirtualSharedFMI("importDocuments.attachments", IGTImportDocuments.ATTACHMENTS);
      sharedVfmi[6].setMandatoryCreate(false);
      sharedVfmi[6].setCollection(true);
      sharedVfmi[6].setValueClass("java.util.Collection");
      sharedVfmi[6].setElementClass("java.lang.String");
      detail = new Properties();
      detail.setProperty("file.fixedKey",ATTACHMENT_PATH_KEY);
      constraint = new FileConstraint(detail);
      sharedVfmi[6].setConstraint(constraint);

      return sharedVfmi;
    }
    else
    {
      return new IGTFieldMetaInfo[0];
    }
  }

  void initVirtualEntityFields(String entityType,
                        AbstractGTEntity entity,
                        Map fieldMap)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_GRID_DOCUMENT.equals(entityType))
    { //20021209
      entity.setNewFieldValue(IGTGridDocumentEntity.ATTACHMENT_FILENAMES,
                              new UnloadedFieldToken());
    }
  }

  protected void loadField(Number fieldId, AbstractGTEntity entity)
    throws GTClientException
  { //20021209AH
    try
    {
      if(IGTEntity.ENTITY_GRID_DOCUMENT.equals(entity.getType()))
      {
        if(IGTGridDocumentEntity.ATTACHMENT_FILENAMES.equals(fieldId))
        {
          //Get list of attachment entities
          Collection attachments = entity.getFieldEntities(IGTGridDocumentEntity.ATTACHMENTS);
          ArrayList filenames = null;
          if(attachments != null)
          {
            if(attachments.size() > 0)
            { // If there are any, iterate through and make a list of their filenames
              filenames = new ArrayList(attachments.size()); //20030129AH
              Iterator i = attachments.iterator();
              while(i.hasNext())
              {
                IGTAttachmentEntity attachment = (IGTAttachmentEntity)i.next();
                String filename = attachment.getFieldString(IGTAttachmentEntity.FILENAME);
                filenames.add(filename);
              }
            }
          }
          //(20021209AH - do we need to return an empty list for null attachments field or
          //an empty collection? For now we try return null in this case)
          //Save list of filenames in vfield
          entity.setNewFieldValue(fieldId,filenames);
        }
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error loading field " + fieldId + " for entity " + entity,t);
    }
  }

  public void send(long[] uids) throws GTClientException
  {
    if(uids == null) throw new java.lang.NullPointerException("Null uids array");
    if(uids.length == 0) return;
    try
    {
      ArrayList uidCollection = new ArrayList(uids.length);
      for(int i=0; i < uids.length; i++)
      {
        Long uid = new Long(uids[i]);
        uidCollection.add( uid );
      }
      ManualSendDocumentEvent event = new ManualSendDocumentEvent(uidCollection);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error sending gridDocuments",t);
    }
  }

  public void export(long[] uids) throws GTClientException
  {
    if(uids == null) throw new java.lang.NullPointerException("Null uids array");
    if(uids.length == 0) return;
    try
    {
      ArrayList uidCollection = new ArrayList(uids.length);
      for(int i=0; i < uids.length; i++)
      {
        Long uid = new Long(uids[i]);
        uidCollection.add( uid );
      }
      ManualExportDocumentEvent  event = new ManualExportDocumentEvent(uidCollection);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error exporting gridDocuments", t);
    }
  }

  public IGTListPager getSearchQueryListPager(Long searchUid, Number[] sortField, boolean[] sortAscending) throws GTClientException
  { // 20031117 DDJ
    try
    {
      SearchDocumentListPager listPager = new SearchDocumentListPager();
      listPager.setManager(this);
      listPager.setSession(_session);
      listPager.setSearchUid(searchUid);
      
      // 20031127 DDJ: Added sorting
      boolean sorted = sortField != null && sortAscending != null && sortField.length > 0 && sortAscending.length > 0;if( sorted )
      {
        DataFilterImpl filter = new DataFilterImpl();
        filter.setOrderFields(sortField, sortAscending);
        listPager.setFilter(filter); 
      }                      

      return listPager;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error attempting to get ListPager for documents with search uid '"
                                  + searchUid +"'",t);
    }
  }
}