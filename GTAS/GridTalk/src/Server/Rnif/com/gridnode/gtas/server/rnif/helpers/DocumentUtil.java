/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 20 2003    Neo Sok Lay         Modify processReceiveRnifDoc() parameters.
 * May 09 2003    Neo Sok Lay         Add methods: completeDocTrans(),
 *                                    findEarliestNoAckDoc(), hasAnyAckedDoc()
 * Jan 29 2004    Neo Sok Lay         Additional completeTransNow parameter for
 *                                    documentMgr.handleDocAccepted() & handleDocRejected().
 * Jun 22 2006    Tam Wei Xiang       Add method findGridDocByProfileUIDAndFolder(...)      
 * Apr 24 2009    Tam Wei Xiang       #854 allow to sort the result given the sortBy field and its sort order                             
 */
package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.bizreg.helpers.Logger;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.helpers.FileHelper;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.base.rnif.helper.XMLUtil;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.keygen.KeyGen;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.util.TimeUtil;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DocumentUtil
{
  static SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmssSSS");

  public static IDocumentManagerObj getDocumentManager() throws ServiceLookupException
  {
    return (IDocumentManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IDocumentManagerHome.class.getName(),
      IDocumentManagerHome.class,
      new Object[0]);
  }

  static final String DELIVERY_MSG_ID= "DeliveryMsgId";
  public static String getUniqueMsgTrackId()
  {
    try
    {
      return KeyGen.getNextId(DELIVERY_MSG_ID).toString();
    }
    catch (Exception ex)
    {
      Logger.warn("Error in generating unique delivery message Id!", ex);
    }
    return "DeliveryMsgId";
  }

  static public GridDocument getDocumentByKey(Long docUid) throws Exception
  {
    return getDocumentManager().findGridDocument(docUid);
  }

  public static void updateDocument(GridDocument gDoc) throws Exception
  {
    getDocumentManager().updateGridDocument(gDoc);
  }

  static public GridDocument addGridDocument(GridDocument gDoc) throws Exception
  {
    IDocumentManagerObj docMgr= getDocumentManager();
//    Long gDocUid= docMgr.createGridDocument(gDoc);
//    return docMgr.findGridDocument(gDocUid);
    Logger.debug("[DocumentUtil.addGridDocument] Before create GridDoc version : " + gDoc.getVersion());
    gDoc = docMgr.createGridDocument(gDoc);
    Logger.debug("[DocumentUtil.addGridDocument] After create GridDoc version : " + gDoc.getVersion());
    return gDoc;
  }
  
  //TWX 11 Dec 2007: create the GDOC with a new transaction
  public static GridDocument addGDocWithNewTrans(GridDocument gDoc) throws Exception
  {
    IDocumentManagerObj docMgr= getDocumentManager();
    gDoc = docMgr.createGridDocumentWithNewTrans(gDoc);
    return gDoc;
  }
  
  static public String getFullUDocPath(GridDocument gDoc) throws Exception
  {
    File udocFile= FileHelper.getUdocFile(gDoc);
    if (udocFile == null)
      throw new Exception("Udoc not found");
    return udocFile.getAbsolutePath();
  }

  static public File getUDoc(GridDocument gDoc) throws Exception
  {
   File udocFile= FileHelper.getUdocFile(gDoc);
   if (udocFile == null)
      throw new Exception("Udoc not found");
   return udocFile.getAbsoluteFile();
  }

  static public String getUDocName(GridDocument gDoc) throws Exception
  {
   File udocFile= FileHelper.getUdocFile(gDoc);
   if (udocFile == null)
      throw new Exception("Udoc not found");
   return udocFile.getName();
  }

  static public String getProprietaryDocId(GridDocument gDoc)
  {
    Long nodeId= gDoc.getSenderNodeId();
    String str= nodeId == null ? "" : nodeId.toString() + "/";
    return str
      + gDoc.getSenderBizEntityId()
      + "_"
      + gDoc.getFolder()
      + "_"
      + gDoc.getGdocId()
      + "_"
      + gDoc.getUdocFilename();

  }

  static public String getProprietaryDocIdByDateTime()
  {
    Date now = TimeUtil.localToUtcTimestamp();
    return sdf.format(now);
  }

  static public String getSimpleProprietaryDocId(GridDocument gDoc)
  {
    Long nodeId= gDoc.getSenderNodeId();
    String str= nodeId == null ? "" : nodeId.toString() + "/";
    return str
      + gDoc.getSenderBizEntityId()
      + "_"
      + gDoc.getUdocFilename();

  }
  //  /**
  //   * Checks that the number of attachments match the number specified in the manifest.
  //   */
  //  private void checkManifest(GridDocument gdoc) throws RnifException
  //  {
  //    int specifiedatts= 0;
  //    int physicalatts= 0;
  //
  //    String atts= gdoc.getAttachmentNames();
  //    Logger.debug("checkManifest: gdoc.getAttachmentNames = [" + atts + "]");
  //    Logger.debug("checkManifest: gdoc.getNumberOfAttas = " + gdoc.getNumberOfAttas());
  //    if (atts != null)
  //    {
  //      if (atts.length() > 0)
  //      {
  //        // calculate the number of colon-delimited attachments
  //        java.util.StringTokenizer tkzr= new java.util.StringTokenizer(atts, ":");
  //        physicalatts= tkzr.countTokens();
  //        Logger.debug("checkManifest: physicalatts = " + physicalatts);
  //      }
  //    }
  //    if (gdoc.getNumberOfAttas() != null)
  //    {
  //      specifiedatts= gdoc.getNumberOfAttas().intValue();
  //    }
  //
  //    if (specifiedatts != physicalatts)
  //    {
  //      GNException.throwEx(
  //        RosettaNetException.UNP_SHDR_MNFSTERR,
  //        "Wrong number of attachments specified in the manifest, "
  //          + "expected["
  //          + physicalatts
  //          + "], found["
  //          + specifiedatts
  //          + "]");
  //    }
  //  }

  /**
   * Returns an XPath value from an XML document.  The provided DTD path is the
   * system path to any DTDs referred to by the document.  uDocPath is the path
   * to the XML file.
   */
  public static String extractValueFromUDoc(GridDocument gDoc, String xpath) throws RnifException
  {
    String udocPath= null;
    String res= null;
    try
    {
      udocPath= getFullUDocPath(gDoc);
      // check for empty xpath values
      if (null == xpath)
        return null;
      if (xpath.length() == 0)
        return null;
      res= XMLUtil.extractXPathValue(udocPath, xpath);
    }
    catch (Exception ex)
    {
      throw RnifException.fileProcessErr(
        "Cannot extract value from UDoc " + udocPath + " xpath " + xpath,
        ex);
    }

    return res;
  }

  static GridDocument processReceivedRnifDoc(GridDocument gDoc,
                                             String[] header,
                                             String[] data,
                                             File[] files) throws RnifException
  {
    try
    {
      return getDocumentManager().processRnifDoc(gDoc, header, data, files);
    }
    catch (Throwable ex)
    {
      throw RnifException.fileProcessErr(
        "Cannot call DocumentManger to process Received RN document!",
        ex);
    }
  }

  static void receiveRnifDoc(GridDocument gDoc) throws RnifException
  {
    try
    {
      getDocumentManager().receiveDoc(gDoc);
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentUtil.receiveRnifDoc]",ex);
      throw RnifException.fileProcessErr("Cannot call DocumentManger to received RN document!", ex);
    }
  }
  
  static void receiveFailedRnifDoc(GridDocument gDoc, boolean isValFailed, boolean isRetry) throws RnifException
  {
    try
    {
      getDocumentManager().receiveFailedRnifDoc(gDoc, isValFailed, isRetry);
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentUtil.receiveFailedRnifDoc]",ex);
      throw RnifException.fileProcessErr(
        "Cannot call DocumentManger to received Failed or repeated RN document!",
        ex);
    }
  }

  static void deleteReleventGridDoc(Collection profileKeys, boolean alsoDeleteDocument)
    throws Exception
  {
    try
    {
      IDocumentManagerObj docMgr= getDocumentManager();
      if (alsoDeleteDocument)
      {
        Collection docKeys= findGridDocKeysByProfileUid(docMgr, profileKeys);
        if (docKeys == null)
          return;
        for (Iterator iter= docKeys.iterator(); iter.hasNext();)
        {
          Long docUid= (Long) iter.next();
          docMgr.deleteGridDocument(docUid);
        }
      }
      else
      {
        Collection docs= findGridDocsByProfileUid(docMgr, profileKeys);
        if (docs == null)
          return;
        for (Iterator iter= docs.iterator(); iter.hasNext();)
        {
          GridDocument doc= (GridDocument) iter.next();
          doc.setRnProfileUid(null);
          docMgr.updateGridDocument(doc);
          
        }
      }
    }
    catch (Exception ex)
    {
      throw RnifException.fileProcessErr("Error in delete/detach documents of the process ", ex);
    }
  }

  static Collection findGridDocKeysByProfileUid(IDocumentManagerObj docMgr, Collection profileUids)
    throws Exception
  {
    if (docMgr == null)
      docMgr= getDocumentManager();
    IDataFilter filter= new DataFilterImpl();
    filter.addDomainFilter(null, GridDocument.RN_PROFILE_UID, profileUids, false);

    return docMgr.findGridDocumentsKeys(filter);
  }

  //TWX 24042009 #854 allow to sort the result given the sortBy field and its sort order
  static Collection findGridDocKeysByProfileUid(IDocumentManagerObj docMgr, Collection profileUids, Number sortBy, boolean isAsc)
    throws Exception
  {
    if (docMgr == null)
      docMgr= getDocumentManager();
    IDataFilter filter= new DataFilterImpl();
    filter.addDomainFilter(null, GridDocument.RN_PROFILE_UID, profileUids, false);
    filter.addOrderField(sortBy, isAsc);
    
    return docMgr.findGridDocumentsKeys(filter);
  }
  
  static Collection findGridDocsByProfileUid(IDocumentManagerObj docMgr, Collection profileUids)
    throws Exception
  {
    if (docMgr == null)
      docMgr= getDocumentManager();

    IDataFilter filter= new DataFilterImpl();
    filter.addDomainFilter(null, GridDocument.RN_PROFILE_UID, profileUids, false);

    return docMgr.findGridDocuments(filter);
  }
  
  /**
   * Complete the document transaction for a sent document.
   *
   * @param sentGdoc The sent GridDocument.
   * @param isRejected The document was rejected by the recipient partner. I.e.
   * a RosettaNet exception signal has been received.
   * @param isProcessCompleted <b>true</b> if an acknowledgement receipt or
   * RosettaNet exception signal has been received for one or more documents in the
   * same process instance. <b>false</b> otherwise.
   */
  static void completeDocTrans(GridDocument sentGdoc, boolean isRejected, boolean isProcessCompleted)
    throws RnifException
  {
    try
    {
      if (isRejected)
      {
        getDocumentManager().handleDocRejected(sentGdoc, true);
        if (!isProcessCompleted)
          AlertUtil.alertExceptionReceived(sentGdoc);
      }
      else //accepted
      {
        getDocumentManager().handleDocAccepted(sentGdoc, null, null, !isProcessCompleted, true);
      }
    }
    catch (Throwable ex)
    {
      Logger.warn("[DocumentUtil.completeDocTrans]",ex);
      throw RnifException.fileProcessErr("Cannot call DocumentManger to completeDocTrans", ex);
    }
  }

  /**
   * Find the earliest GridDocument that have not received any acknowledgement,
   * either RN_ACK or RN_EXCEPTION. Note that this may not be accurate if the
   * user has already deleted a document that fit the criteria.
   *
   * @param docKeys UIDs of the GridDocuments within which to find.
   * @return The earliest GridDocuments that have not received any acknowledgement
   * <b>null</b> if no such doc found.
   */
  static GridDocument findEarliestNoAckDoc(Collection docKeys) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, GridDocument.UID, docKeys, false);
    filter.addSingleFilter(filter.getAndConnector(), GridDocument.IS_REC_ACK_PROCESSED,
      filter.getNotEqualOperator(), Boolean.TRUE, false);
    filter.setOrderFields(new Object[] {GridDocument.UID});

    Collection results = getDocumentManager().findGridDocumentsKeys(filter);
    GridDocument gdoc = null;

    if (results != null && !results.isEmpty())
      gdoc = getDocumentByKey((Long)results.toArray()[0]);

    return gdoc;
  }

  /**
   * Check whether there are any acked sent document, either by RN_ACK or RN_EXCEPTION.
   * Note that this may not be accurate if user has already deleted a document that
   * fit the criteria.
   *
   * @param docKeys UIDs of the GridDocuments within which to find.
   * @return <b>true</b> if there are at least one such document, <b>false</b> otherwise.
   */
  static boolean hasAnyAckedDoc(Collection docKeys) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, GridDocument.UID, docKeys, false);
    filter.addSingleFilter(filter.getAndConnector(), GridDocument.IS_REC_ACK_PROCESSED,
      filter.getEqualOperator(), Boolean.TRUE, false);

    Collection results = getDocumentManager().findGridDocumentsKeys(filter);

    return results!=null && !results.isEmpty();
  }
  
  /**
   * TWX 19 June 2006
   * @param rnProfileUID
   * @param folder
   * @return null if no record found else return GridDocument obj.
   * @throws Exception
   */
  static GridDocument findGridDocByProfileUIDAndFolder(Long rnProfileUID, String folder)
		throws Exception
  {
  	IDataFilter filter = new DataFilterImpl();
  	filter.addSingleFilter(null, GridDocument.RN_PROFILE_UID, filter.getEqualOperator(), rnProfileUID, false);
  	filter.addSingleFilter(filter.getAndConnector(), GridDocument.FOLDER, filter.getEqualOperator(), folder, false);
	
  	Collection c = getDocumentManager().findGridDocuments(filter);
  	if(c!=null && c.size() > 0)
  	{
  		return (GridDocument)c.iterator().next();
  	}
  	else
  	{
  		return null;
  	}
  }
}
