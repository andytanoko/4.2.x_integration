/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTransactionNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 1,  2006    Tam Wei Xiang       Created
 * Jan 10, 2007    Tam Wei Xiang       Added field isRetry
 */
package com.gridnode.gtas.server.document.notification;

import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.framework.notification.AbstractNotification;

/**
 * This class will be used to indicate there is a creation of IB or OB document by the Document module.
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class DocumentTransactionNotification extends AbstractNotification
{
  private GridDocument _gdoc;
  private boolean _isDuplicate; //indicate whether we are receiving the same doc from Partner
  private boolean _isRetry; //indicate whether we resend the doc from GT
  
  public DocumentTransactionNotification(GridDocument gdoc, boolean isDuplicate, boolean isRetry)
  {
    _gdoc = gdoc;
    _isDuplicate = isDuplicate;
    _isRetry = isRetry;
  }
  
  public GridDocument getGdoc()
  {
    return _gdoc;
  }

  public void setGdoc(GridDocument _gdoc)
  {
    this._gdoc = _gdoc;
  }

  public boolean isDuplicate()
  {
    return _isDuplicate;
  }

  public void setIsDuplicate(boolean duplicate)
  {
    _isDuplicate = duplicate;
  }
  
  public boolean isRetry()
  {
    return _isRetry;
  }

  public void setRetry(boolean retry)
  {
    _isRetry = retry;
  }

  /* (non-Javadoc)
   * @see com.gridnode.pdip.framework.notification.INotification#getNotificationID()
   */
  public String getNotificationID()
  {
    // TODO Auto-generated method stub
    return "DocumentTransaction";
  }
  
  public String toString()
  {
    if(_gdoc!=null)
    {
      String docNo = _gdoc.getUniqueDocIdentifier();
      String docType = _gdoc.getUdocDocType();
      String docDirection = _gdoc.getFolder();
      String messageID = _gdoc.getFolder()+"-"+_gdoc.getGdocId();
      String dateTimeSent = _gdoc.getDateTimeSendEnd() == null ? "" : _gdoc.getDateTimeSendEnd().toString();
      String dateTimeReceived = _gdoc.getDateTimeReceiveEnd() == null ? "" : _gdoc.getDateTimeReceiveEnd().toString();
      String tracingID = _gdoc.getTracingID();
      String docSize = _gdoc.getUdocFileSize() == null ? "0" : _gdoc.getUdocFileSize().toString();
      String isDuplicate = new Boolean(_isDuplicate).toString();
      String isRetry = new Boolean(_isRetry).toString();
      String userTrackingID = _gdoc.getUserTrackingID();
      String beID = "".equals(_gdoc.getSenderBizEntityId()) ? _gdoc.getRecipientBizEntityId() : _gdoc.getSenderBizEntityId();
      String pipName = _gdoc.getProcessDefId();
      String rnprofileUID = _gdoc.getRnProfileUid() == null ? "0" : _gdoc.getRnProfileUid().toString();
      String recipientPartnerName = _gdoc.getRecipientPartnerName();
      String sendPartnerName = _gdoc.getSenderPartnerName();
      String processUID = _gdoc.getProcessInstanceUid() == null ? "0" : _gdoc.getProcessInstanceUid().toString();
      String udocFilename = _gdoc.getUdocFilename();
      
      List attachmentFilename = _gdoc.getAttachments();
      String attUID = "";
      if(attachmentFilename != null && attachmentFilename.size() > 0)
      {
        Iterator i = attachmentFilename.iterator();
        while(i.hasNext())
        {
          attUID += ""+i.next()+" ";
        }
      }
      
      return "docNo :"+docNo + " docType: "+docType+" docDirection: "+docDirection+" messageID :"+messageID+" dateTimeSent :"+dateTimeSent+"\n"+
             "dateTijmeReceived "+dateTimeReceived+" tracingID "+tracingID +" docSize "+docSize+" isDuplicate "+isDuplicate+" isRetry "+isRetry+"\n"+
             "userTrackingID "+userTrackingID+" beID "+beID+" pipName "+pipName +" rnprofileUID "+rnprofileUID+" recipeintName "+recipientPartnerName+"\n"+
             "senderPartnerName "+sendPartnerName+" processUID "+processUID+" udocfilename "+udocFilename+" attUID "+attUID;
    }
    return "";
  }
}
