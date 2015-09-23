/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocInfoCollator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 * Jan 10, 2007    Tam Wei Xiang       Added new field isRetry in DocInfo.
 * Feb 12, 2007    Tam Wei Xiang       The retrieval of the BusinessEntity should
 *                                     go through the session bean instead of using the
 *                                     BizEntityDAOHelper directly
 */
package com.gridnode.gtas.audit.extraction.collator;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.audit.extraction.exception.AuditInfoCollatorException;
import com.gridnode.gtas.audit.extraction.helper.AttachmentHelper;
import com.gridnode.gtas.audit.extraction.helper.BusinessDocumentHelper;
import com.gridnode.gtas.audit.extraction.helper.IGTATConstant;
import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.gtas.audit.common.model.BusinessDocument;
import com.gridnode.gtas.audit.common.model.DocInfo;
import com.gridnode.gtas.audit.common.model.ProcessSummary;
import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.gtas.server.document.folder.InboundFolder;
import com.gridnode.gtas.server.document.folder.OutboundFolder;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.notification.DocumentTransactionNotification;
import com.gridnode.gtas.server.rnif.facade.ejb.IRnifManagerObj;
import com.gridnode.gtas.server.rnif.helpers.ProfileUtil;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.bizreg.facade.IBizRegistryManager;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerHome;
import com.gridnode.pdip.app.bizreg.helpers.BizEntityDAOHelper;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.notification.AbstractNotification;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is used to collate information from the DocumentTransactionNotification and
 * generate the AuditTrailData based on the information.
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class DocInfoCollator extends AbstractInfoCollator
{
  
  private static DocInfoCollator _infoCollator = new DocInfoCollator();
  private static String CLASS_NAME = "DocInfoCollator";
  
  private DocInfoCollator()
  {
    
  }
  
  public static DocInfoCollator getInstance()
  {
    return _infoCollator;
  }
  
  @Override
  public AuditTrailData gatherInfo(AbstractNotification notify) throws AuditInfoCollatorException
  {
    try
    {
      assertNotification(notify);
      DocumentTransactionNotification notification = (DocumentTransactionNotification)notify;
      return createAuditTrailData(notification);
    }
    catch(Exception ex)
    {
      throw new AuditInfoCollatorException("["+CLASS_NAME+".gatherInfo] Error in creating the AuditTrailData. ", ex);
    }
  }
  
  /**
   * Create the AuditTrailData given the notification.
   * @param notification The DocumentTransactionNotification
   * @return 
   * @throws Exception
   */
  private AuditTrailData createAuditTrailData(DocumentTransactionNotification notification) throws Exception
  {
    GridDocument gdoc = notification.getGdoc();
    Boolean isDuplicate = notification.isDuplicate();
    Boolean isRetry = notification.isRetry();
    
    ProcessSummary processSummary = createProcessSummary(gdoc);
    DocInfo info = createDocInfo(gdoc, isDuplicate, isRetry);
    info.setProcessSummary(processSummary);
    
    //Biz Document (include udocs & attachments)
    BusinessDocument[] bizDocuments = getBusinessDocument(gdoc);
    return new AuditTrailData(info,false, bizDocuments);
  }
  
  /**
   * Get an array of BusinessDocument instance. The udoc file, and the attachment files(if any) will be encapsulated
   * in the BusinessDocument instace.
   * @param gdoc The GridDocument instance
   * @return an array of BusinessDocument instance
   * @throws Exception throw if error in creating array of BusinessDocument instance.
   */
  private BusinessDocument[] getBusinessDocument(GridDocument gdoc) throws Exception
  {
    ArrayList<BusinessDocument> bizDocumentList = new ArrayList<BusinessDocument>();
    
    //create business document for udoc
    File udoc = getUdocFile(gdoc);
    
    BusinessDocument bizDocument = null;
    bizDocument = BusinessDocumentHelper.createBusinessDocument(udoc, true);
    bizDocumentList.add(bizDocument);
    
    //create the business document for attachment
    Collection<File> attFiles = getAttachmentFile(gdoc);
    Iterator<File> ite = attFiles.iterator();
    while(ite.hasNext())
    {
      bizDocument = BusinessDocumentHelper.createBusinessDocument(ite.next(), true);
      bizDocumentList.add(bizDocument);
    }
    return bizDocumentList.toArray(new BusinessDocument[attFiles.size()]);
  }
  
  private File getUdocFile(GridDocument gdoc) throws Exception
  {
    //create the business document for udoc
    File udoc = FileUtil.getFile(IDocumentPathConfig.PATH_UDOC, gdoc.getFolder()+"/", gdoc.getUdocFilename());
    if(udoc == null)
    {
      throw new NullPointerException("["+CLASS_NAME+".getBusinessDocument] The udoc file "+gdoc.getUdocFilename()+" for gdoc id :"+gdoc.getGdocId()+" folder:"+gdoc.getFolder()+" is not existed !");
    }
    return udoc;
  }
  
  private Collection<File> getAttachmentFile(GridDocument gdoc) throws Exception
  {
    try
    {
      return AttachmentHelper.getAttachmentFiles(gdoc.getAttachments());
    }
    catch(Exception ex)
    {
      throw new AuditInfoCollatorException("["+CLASS_NAME+".getAttachmentFile] Error in getting attachment files for gdoc [gdocID:"+gdoc.getGdocId()+" folder:"+gdoc.getFolder()+"]", ex);
    }
  }
  
  private ProcessSummary createProcessSummary(GridDocument gdoc) throws AuditInfoCollatorException
  {
    String pipName = gdoc.getProcessDefId();
    RNProfile profile = getRNProfile(gdoc.getRnProfileUid());
    if(profile == null) //for some case we may not have the rnprofile uid 
                        //eg we have problem in packaging the payload we are sending out
    {
      return null;
    }
    else
    {
      String folder = gdoc.getFolder();
      String pipVersion = profile.getPIPVersionIdentifier();
      String tradingPartnerDuns = getTradingPartnerDuns(folder, profile);
      String tradingPartnerName = getPartnerName(gdoc);
      String customerDuns = getCustomerDuns(folder, profile);
      String customerName = getCustomerName(gdoc);
      Long processInstanceUID = gdoc.getProcessInstanceUid();
      String initiatorID = profile.getProcessOriginatorId();
      
      return new ProcessSummary(pipName, pipVersion, tradingPartnerDuns, tradingPartnerName, customerDuns, customerName, processInstanceUID, initiatorID);
    }
  }
  
  private DocInfo createDocInfo(GridDocument gdoc, Boolean isDuplicate, Boolean isRetry) throws Exception
  {
    String docNo = gdoc.getUdocNum();
    String documentType = gdoc.getUdocDocType();
    String documentDirection = gdoc.getFolder();
    String messageID = getMessageID(gdoc.getFolder(), gdoc.getGdocId());
    Date dateTimeSent = null;
    Date dateTimeReceived = null;
    String tracingID = gdoc.getTracingID();
    String userTrackingID = gdoc.getUserTrackingID();
    String beID = getOwnBEID(gdoc);
    
    DocInfo info = new DocInfo();
    info.setDocNo(docNo);
    info.setDocumentType(documentType);
    info.setDocumentDirection(documentDirection);
    info.setMessageID(messageID);
    info.setDateTimeSent(dateTimeSent);
    info.setDateTimeReceived(dateTimeReceived);
    info.setTracingID(tracingID);
    info.setIsDuplicate(isDuplicate);
    info.setRetry(isRetry);
    info.setUserTrackingID(userTrackingID);
    info.setBeID(beID);
    
    File udoc = getUdocFile(gdoc);
    info.setDocumentSize(getFileSize(udoc));
    
    return info;
  }
  
  private Long getFileSize(File f)
  {
    long fileSize = Math.round(f.length()/1024);
    if (fileSize == 0)
    {
      fileSize = 1;
    }
    return fileSize;
  }
  
/**
   * Get the MessageID given the gdoc.
   * @param gdoc
   * @return the concatenate str of gdoc folder(short form) and gdocID
   */
  private static String getMessageID(String folder, Long gdocID)
  {
    if(gdocID == null || "".equals(gdocID))
    {
      throw new IllegalArgumentException("["+CLASS_NAME+".getMessageID] GDOC ID is expected. gdocID is null or empty string !");
    }
    
    String folderAbbreviate = "";
    if(InboundFolder.FOLDER_NAME.equals(folder))
    {
      folderAbbreviate = "IB";
    }
    else if(OutboundFolder.FOLDER_NAME.equals(folder))
    {
      folderAbbreviate = "OB";
    }
    else
    {
      throw new IllegalArgumentException("["+CLASS_NAME+".getMessageID] Folder "+folder+" is not supported !");
    }
    return folderAbbreviate+"-"+gdocID;
  }
  
  private static String getOwnBEID(GridDocument gdoc)
  {
    String folder = gdoc.getFolder();
    if(InboundFolder.FOLDER_NAME.equals(folder))
    {
      return gdoc.getRecipientBizEntityId();
    }
    else if(OutboundFolder.FOLDER_NAME.equals(folder))
    {
      return gdoc.getSenderBizEntityId();
    }
    else
    {
      throw new IllegalArgumentException("[DocumentFlowNotifyHelper.getOwnBEID] folder "+folder+" is not supported !");
    }
  }
  
  private String getTradingPartnerDuns(String folder, RNProfile profile)
  {
    if(InboundFolder.FOLDER_NAME.equals(folder))
    {
      return profile.getSenderGlobalBusIdentifier();
    }
    else if(OutboundFolder.FOLDER_NAME.equals(folder))
    {
      return profile.getReceiverGlobalBusIdentifier();
    }
    else
    {
      throw new IllegalArgumentException("["+CLASS_NAME+".getTradingPartnerDuns] Expecting either IB or OB folder. The passed in one is "+folder);
    }
  }
  
  private String getCustomerDuns(String folder, RNProfile profile)
  {
    if(InboundFolder.FOLDER_NAME.equals(folder))
    {
      return profile.getReceiverGlobalBusIdentifier();
    }
    else if(OutboundFolder.FOLDER_NAME.equals(folder))
    {
      return profile.getSenderGlobalBusIdentifier();
    }
    else
    {
      throw new IllegalArgumentException("["+CLASS_NAME+".getCustomerDuns] Expecting either IB or OB folder. The passed in one is "+folder);
    }
  }
  
  private String getPartnerName(GridDocument gdoc)
  {
    String folder = gdoc.getFolder();
    if(InboundFolder.FOLDER_NAME.equals(folder))
    {
      return gdoc.getSenderPartnerName();
    }
    else if(OutboundFolder.FOLDER_NAME.equals(folder))
    {
      return gdoc.getRecipientPartnerName();
    }
    else
    {
      throw new IllegalArgumentException("["+CLASS_NAME+".getPartnerName] Expecting either IB or OB folder. The passed in one is "+folder);
    }
  }
  
  private String getCustomerName(GridDocument gdoc) throws AuditInfoCollatorException
  {
    String folder = gdoc.getFolder();
    if(InboundFolder.FOLDER_NAME.equals(folder))
    {
      return getBizEntityDescription(gdoc.getRecipientBizEntityId(), false);
    }
    else if(OutboundFolder.FOLDER_NAME.equals(folder))
    {
      return getBizEntityDescription(gdoc.getSenderBizEntityId(), false);
    }
    else
    {
      throw new IllegalArgumentException("["+CLASS_NAME+".getCustomerName] Expecting gdoc in either IB or OB folder. The passed in one is in folder "+folder);
    }
  }
  
  private String getBizEntityDescription(String bizEntityID, boolean isPartner) throws AuditInfoCollatorException
  {
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, IBusinessEntity.ID, filter.getEqualOperator(), bizEntityID, false);
      filter.addSingleFilter(filter.getAndConnector(), IBusinessEntity.IS_PARTNER, filter.getEqualOperator(),isPartner, false);
      
      Collection bizEntities = null;
      try
      {
        IBizRegistryManager mgr = getBizRegistryManager();
        bizEntities = mgr.findBusinessEntities(filter);
      }
      catch(Exception ex)
      {
        throw new AuditInfoCollatorException("["+CLASS_NAME+".getBizEntityDescription] Error in locating the BusinessEntity given the filter "+filter.getFilterExpr(), ex);
      }
      if(bizEntities == null || bizEntities.size() == 0)
      {
        throw new AuditInfoCollatorException("["+CLASS_NAME+".getBizEntityDescription] No BusinessEntity can be found given the filter "+filter.getFilterExpr());
      }
      else
      {
        return ((BusinessEntity)bizEntities.iterator().next()).getDescription();
      }
  }
  
  private RNProfile getRNProfile(Long rnProfileUID) throws AuditInfoCollatorException
  {
    try
    {
      IRnifManagerObj rnifMgr = ProfileUtil.getRnifManager();
      RNProfile rnprofile = rnifMgr.findRNProfile(rnProfileUID);
      return rnprofile;
    }
    catch(Exception ex)
    {
      throw new AuditInfoCollatorException("["+CLASS_NAME+".getRNProfile] Error in getting the RNProfile given UID "+rnProfileUID, ex);
    }
  }
  
  private void assertNotification(AbstractNotification notification)
  {
    if(! (notification instanceof DocumentTransactionNotification))
    {
      throw new IllegalArgumentException("["+CLASS_NAME+".assertNotification] The given notification is not an instance of DocumentTransactionNotification !");
    }
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IGTATConstant.LOG_TYPE, CLASS_NAME);
  }
  
  private IBizRegistryManager getBizRegistryManager() throws Exception
  {
    JndiFinder jndi = new JndiFinder(null);
    IBizRegistryManagerHome home = (IBizRegistryManagerHome)jndi.lookup(IBizRegistryManagerHome.class.getName(), IBizRegistryManagerHome.class);
    return home.create();
  }
  
}
