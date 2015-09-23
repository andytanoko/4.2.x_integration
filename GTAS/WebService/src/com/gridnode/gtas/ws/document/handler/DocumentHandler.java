/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: DocumentHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 6, 2010   Tam Wei Xiang       Created
 * Oct 26,2010   Tam Wei Xiang       #1897 - Modified markGdocAsRead(..)
 */
package com.gridnode.gtas.ws.document.handler;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.activation.DataHandler;

import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.folder.OutboundFolder;
import com.gridnode.gtas.server.document.helpers.DocumentUtil;
import com.gridnode.gtas.server.document.helpers.FileHelper;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.ws.document.exception.GdocRetrieveException;
import com.gridnode.gtas.ws.document.exception.GdocUpdateException;
import com.gridnode.gtas.ws.document.model.MessageInfo;
import com.gridnode.gtas.ws.document.model.MessagePayload;
import com.gridnode.gtas.ws.document.model.MessageRetrieveCriteria;
import com.gridnode.gtas.ws.helpers.Logger;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.TimeUtil;

/**
 * This class handle the document related operation that is specific to the 
 * document retrieving webservice.
 * 
 * @author Tam Wei xiang
 * @since 4.2.3
 */
public class DocumentHandler
{
  private static final String CLASS_NAME = "DocumentHandler";
  
  public DocumentHandler()
  {
    
  }
  
  /**
   * Get the list of Message info given the criteria
   * @param criteria The MessageRetrieveCriteria
   * @param ownerId The owner of the document.
   * @return list of matching message info or empty list if none can be found.
   * @throws GdocRetrieveException
   */
  public ArrayList<MessageInfo> getMessageInfos(MessageRetrieveCriteria criteria, String ownerId) throws GdocRetrieveException
  {
    String mn = "getMessageInfos";
    ArrayList<MessageInfo> msgInfos = new ArrayList<MessageInfo>();
    
    IDataFilter docFilter = convertToDataFilter(criteria, ownerId);
    Collection<GridDocument> gdocList;
    try
    {
      Logger.debugLog(CLASS_NAME, mn, "looking up DocumentManager");
      gdocList = getDocumentMgr().findGridDocuments(docFilter);
    }
    catch (Exception e)
    {
      throw new GdocRetrieveException("Error in retrieving the gdoc given the filter="+docFilter+" owner="+ownerId, e);
    }
    if(gdocList == null || gdocList.size() == 0)
    {
      return msgInfos;
    }
    else
    {
      for(GridDocument gdoc: gdocList)
      {
        msgInfos.add(convertGdocToMessageInfo(gdoc));
      }
    }
    
    return msgInfos;
  }
  
  public MessagePayload getMessagePayload(MessageInfo msgInfo, String owner) throws GdocRetrieveException
  {
    String mn = "getMessagePayload";
    assertInput(msgInfo, owner);
    
    String msgId = msgInfo.getMsgId();
    GridDocument gdoc = getGdoc(msgInfo, owner);
    if(gdoc == null)
    {
      Logger.infoLog(CLASS_NAME, mn, "No gdoc can be found given "+msgInfo+" owner:"+owner);
      return null;
    }
    else
    {
      gdoc.setTempUdocFilename("");
      File udoc;
      try
      {
        udoc = FileHelper.getUdocFile(gdoc);
      }
      catch (Exception e)
      {
        throw new GdocRetrieveException("Error in retrieving the udoc file for gdoc with message id="+msgId+" owner="+owner, e);
      }
      MessagePayload msgPayload = new MessagePayload();
      
      MessageInfo retrievedMsgInfo = convertGdocToMessageInfo(gdoc);
      DataHandler udocDataHandler = getDataHandler(udoc);
      
      //set msg info and udoc
      msgPayload.setMsgInfo(retrievedMsgInfo);
      msgPayload.setDataContentHandler(udocDataHandler);
      
      
      
      return msgPayload;
    }
  }
  
  public DataHandler[] getAttachment(MessageInfo msgInfo, String owner) throws GdocRetrieveException
  {
    String mn = "getAttachment";
    
    String msgId = msgInfo.getMsgId();
    GridDocument gdoc = getGdoc(msgInfo, owner);
    try
    {
      DataHandler[] attDataHandler = new DataHandler[]{};
      Collection<Attachment> attachments = getDocumentMgr().findAttachments(gdoc);
      if(attachments != null && attachments.size() > 0)
      {
        Logger.debugLog(CLASS_NAME, mn, "Retrieving attachment: ");
        
        attDataHandler = new DataHandler[attachments.size()];
        int i = 0;
        for(Attachment att : attachments)
        {
          Logger.debugLog(CLASS_NAME, mn, "Retrieving attachment name:"+att.getFilename());
          
          File attFile = FileUtil.getFile(IDocumentPathConfig.PATH_ATTACHMENT, att.getFilename());
          attDataHandler[i] = getDataHandler(attFile);
          i++;
        }
        
      }
      
      return attDataHandler;
    }
    catch (Exception e)
    {
      throw new GdocRetrieveException("Error in locating the attachment(s) for gdoc with message id="+msgId+" owner="+owner, e);
    }
  }
  
  public void markGdocAsRead(MessageInfo[] msgInfos, String owner) throws GdocRetrieveException, GdocUpdateException
  {
    if(msgInfos != null && msgInfos.length > 0)
    {
      for(MessageInfo msgInfo: msgInfos)
      {
        markGdocAsRead(msgInfo, owner);
      }
    }
  }
  
  public void markGdocAsRead(MessageInfo msgInfo, String owner) throws GdocRetrieveException, GdocUpdateException
  {
    assertInput(msgInfo, owner);
    GridDocument gdoc = getGdoc(msgInfo, owner);
    if(gdoc == null)
    {
      throw new GdocRetrieveException("No gdoc can be found given "+msgInfo+" owner="+owner);
    }
    
    gdoc.setDateTimeTransComplete(TimeUtil.localToUtcTimestamp());
    gdoc.setRead(true);
    
    try
    {
      getDocumentMgr().updateGridDocument(gdoc);
    }
    catch (Exception e)
    {
      throw new GdocUpdateException("Error in updating the gdoc given msgInfo="+msgInfo+" owner="+owner);
    }
    
  }
  
  private void assertInput(MessageInfo msgInfo, String owner)
  {
    if(msgInfo == null)
    {
      throw new IllegalArgumentException("The given msgInfo can not be null!");
    }
    
    if(owner == null)
    {
      throw new IllegalArgumentException("The given owner can not be null!");
    }
  }
  
  private GridDocument getGdoc(MessageInfo msgInfo, String owner) throws GdocRetrieveException
  {
    String msgId = msgInfo.getMsgId();
    //String folder = DocumentUtil.getFolder(msgId);
    String folder = OutboundFolder.FOLDER_NAME; //we only allow the client to retrieve doc resided in the outbound
    String gdocId = DocumentUtil.getGdocId(msgId);
    
    IDataFilter docFilter = new DataFilterImpl();
    docFilter.addSingleFilter(null, GridDocument.FOLDER, docFilter.getEqualOperator(), folder, false);
    docFilter.addSingleFilter(docFilter.getAndConnector(), GridDocument.G_DOC_ID, docFilter.getEqualOperator(), gdocId, false);
    docFilter.addSingleFilter(docFilter.getAndConnector(), GridDocument.R_PARTNER_ID, docFilter.getEqualOperator(), owner, false);
    
    Collection<GridDocument> gdocList = null;
    try
    {
      gdocList = getDocumentMgr().findGridDocuments(docFilter);
    }
    catch (Exception e)
    {
      throw new GdocRetrieveException("Error in retrieving the gdoc given the filter="+docFilter, e);
    }
    
    if(gdocList == null || gdocList.size() == 0)
    {
      return null;
    }
    else
    {
      return gdocList.iterator().next();
    }
  }
  
  private DataHandler getDataHandler(File file) throws GdocRetrieveException
  {
    //FileDataSource fileDataSource = new FileDataSource(file);
    DataHandler dataHandler;
    try
    {
      dataHandler = new DataHandler(file.toURL());
    }
    catch (MalformedURLException e)
    {
      throw new GdocRetrieveException("Failed to init data handler given file "+file.getAbsolutePath(), e);
    }
    return dataHandler;
  }
  
  public MessageInfo convertGdocToMessageInfo(GridDocument gdoc)
  {
    String mn = "convertGdocToMessageInfo";
    String docType = gdoc.getUdocDocType();
    String sender = gdoc.getSenderBizEntityId();
    String msgId = DocumentUtil.getMessageID(gdoc.getFolder(), ""+gdoc.getGdocId());
    String docNumber = gdoc.getUdocNum();
    String docFilename = gdoc.getUdocFilename();
    boolean isContainAttachment = (gdoc.getAttachments() == null || gdoc.getAttachments().size() ==0) ? false: true;
    boolean isRead = "Read".equals(gdoc.getCustom1()); //TODO: refine the read flag
    //long creationTimestamp = TimeUtil.localToUtcTimestamp(gdoc.getDateTimeCreate()).getTime();
    long creationTimestamp = gdoc.getDateTimeCreate()!= null? TimeUtil.utcToLocal(gdoc.getDateTimeCreate().getTime()): 0;
    
    MessageInfo msgInfo = new MessageInfo(msgId, docType, sender, isRead, creationTimestamp, docNumber, docFilename, isContainAttachment);
    Logger.debugLog(CLASS_NAME, mn, ""+msgInfo);
    
    return msgInfo;
  }
  
  private IDataFilter convertToDataFilter(MessageRetrieveCriteria criteria, String ownerId)
  {
    String mn = "convertToDataFilter";
    Logger.debugLog(CLASS_NAME, mn, "using "+criteria);

    
    
    IDataFilter docFilter = new DataFilterImpl();
    
    //Compulsary fetching criteria
      //As all the document that is delivered to the TP is resided in Outbound folder, 
      //we only allow the external client to fetch the document that resided in Outbound folder.
    docFilter.addSingleFilter(docFilter.getAndConnector(), GridDocument.FOLDER, docFilter.getEqualOperator(), OutboundFolder.FOLDER_NAME, false);
    docFilter.addSingleFilter(docFilter.getAndConnector(), GridDocument.R_PARTNER_ID, docFilter.getEqualOperator(), ownerId, false);
    
    if(criteria != null)
    {
      String msgId = trimEmptySpace(criteria.getMsgId());
      long fromCreationDate = criteria.getFromCreationDate();
      long toCreationDate = criteria.getToCreationDate();
      boolean isRead = criteria.isRead();
      String sender = trimEmptySpace(criteria.getSender());
      String docNumber = trimEmptySpace(criteria.getDocNumber());
      String docType = trimEmptySpace(criteria.getDocType());
      int totalFetch = criteria.getTotalFetch();
      String gdocId = null;
      
      docFilter.addSingleFilter(docFilter.getAndConnector(), GridDocument.IS_READ, docFilter.getEqualOperator(), isRead, false);
      
      if(! isEmptyString(msgId))
      {
        gdocId = DocumentUtil.getGdocId(msgId);
      }
      
      if(sender != null && sender.trim().length() > 0)
      {
        docFilter.addSingleFilter(docFilter.getAndConnector(), GridDocument.S_BIZ_ENTITY_ID, docFilter.getEqualOperator(), sender, false);
      }
      
      if(!isEmptyString(docNumber))
      {
        docFilter.addSingleFilter(docFilter.getAndConnector(), GridDocument.U_DOC_NUM, docFilter.getEqualOperator(), docNumber, false);
      }
      
      if(!isEmptyString(msgId))
      {
        docFilter.addSingleFilter(docFilter.getAndConnector(), GridDocument.G_DOC_ID, docFilter.getEqualOperator(), gdocId, false);
      }
      
      if(!isEmptyString(docType))
      {
        docFilter.addSingleFilter(docFilter.getAndConnector(), GridDocument.U_DOC_DOC_TYPE, docFilter.getEqualOperator(), docType, false);
      }
      
      if(fromCreationDate != 0 && toCreationDate != 0)
      {
        Date fromDate = new Date(TimeUtil.localToUtc(fromCreationDate));
        Date toDate = new Date(TimeUtil.localToUtc(toCreationDate));
        Logger.debugLog(CLASS_NAME, mn, "fromDate: "+fromDate+" toDate:"+toDate);
        
        docFilter.addRangeFilter(docFilter.getAndConnector(), GridDocument.DT_CREATE, fromDate, toDate, false);
      }
    }
    else
    {
      docFilter.addSingleFilter(docFilter.getAndConnector(), GridDocument.IS_READ, docFilter.getEqualOperator(), false, false);
    }
    
    //TODO: restrict the number of doc to be fetched.
    
    Logger.debugLog(CLASS_NAME, mn, "DataFilter "+docFilter.getFilterExpr());
    
    return docFilter;
  }
  
  private boolean isEmptyString(String s)
  {
    if(s == null || s.trim().length() == 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  private String trimEmptySpace(String s)
  {
    if(s != null)
    {
      return s.trim();
    }
    
    return s;
  }
  
  private IDocumentManagerObj getDocumentMgr() throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).
              getObj(IDocumentManagerHome.class.getName(), IDocumentManagerHome.class, new Object[]{});
  }
}
