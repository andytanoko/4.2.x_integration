/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocExtractionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 27 2003    Neo Sok Lay         Created
 * Nov 10 2005    Neo Sok Lay         Use Local Context lookup for XML service
 */
package com.gridnode.gtas.server.docalert.helpers;

import com.gridnode.gtas.server.docalert.exceptions.ResponseTrackingException;
import com.gridnode.gtas.server.docalert.model.ActiveTrackRecord;
import com.gridnode.gtas.server.docalert.model.ReminderAlert;
import com.gridnode.gtas.server.docalert.model.ResponseTrackRecord;
import com.gridnode.gtas.server.document.helpers.FileHelper;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.GridDocument;

import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;

import java.util.Date;
import java.util.List;

/**
 * This is a helper class that provide document extraction services and other
 * services that require manipulation of GridDocument data and user document.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class DocExtractionHelper
{
  /**
   * Determine the start tracking date for a document.
   *
   * @param trackRecord The ResponseTrackRecord for tracking the type of document.
   * @param reminderAlert The first ReminderAlert that will be triggered for
   * the document.
   * @param doc The GridDocument for the sent document.
   * @throws Exception Unable to extract the tracking date from the document
   * @throws Exception Invalid tracking date e.g not present.
   */
  public static Date determineTrackingDate(ResponseTrackRecord trackRecord,
                                           ReminderAlert reminderAlert,
                                           GridDocument doc)
    throws Throwable
  {
    Date trackDate = null;
    String xpath = trackRecord.getStartTrackDateXpath();
    if (isEmptyStr(xpath))
      trackDate = doc.getDateTimeSendStart();
    else
    {
      // extract using xpath
      List vals = extractFromXpath(xpath, doc);
      if (vals == null || vals.isEmpty())
        throw ResponseTrackingException.unableToDetermineTrackDate(xpath, doc.getUdocFilename());
      String dateStr = (String)vals.toArray()[0];

      trackDate = DateUtil.convertToDate(dateStr);
    }

    if (trackDate == null)
      throw ResponseTrackingException.invalidTrackDate(doc.getUdocFilename());

    return DateUtil.rollDate(trackDate, reminderAlert.getDaysToReminder());
  }


  public static boolean isEmptyStr(String val)
  {
    return (val == null) || (val.length() == 0);
  }

  /**
   * Get the Document Identifier from a document.
   *
   * @param xpath The Xpath for extracting the document identifier from the
   * document. If this is not present, the UDocNum in the GridDocument will be
   * used instead.
   * @param doc The GridDocument for the document.
   * @throws Exception Unable to extract the Document Identifier from the document.
   * @throws Exception Invalid Document Identifier e.g. not present.
   */
  public static String getDocumentIdentifier(String xpath,
                                             GridDocument doc)
    throws Throwable
  {
    String docId = null;

    if (isEmptyStr(xpath))
      docId = doc.getUdocNum();
    else
    {
      // extract using xpath
      List vals = extractFromXpath(xpath, doc);
      if (vals == null || vals.isEmpty())
        throw ResponseTrackingException.unableToDetermineDocIdentifier(
              xpath, doc.getUdocFilename());
      docId = (String)vals.toArray()[0];
    }

    if (isEmptyStr(docId))
      throw ResponseTrackingException.invalidDocIdentifier(doc.getUdocFilename());

    return docId;
  }

  /**
   * Extract values from the user document using a xpath.
   *
   * @param xpath The Xpath to use for extraction.
   * @param doc The GridDocument for the user document.
   * @return List of values (String) extracted.
   */
  public static List extractFromXpath(String xpath,
                                      GridDocument doc)
  {
    List vals = null;
    if (!isEmptyStr(xpath))
    {
      // extract
      try
      {
        String udocFile = FileHelper.getUdocFile(doc).getCanonicalPath();
        vals = getXmlMgr().getXPathValues(udocFile, xpath);
      }
      catch (Exception ex)
      {
        Logger.warn("[DocExtractionHelper.extractFromXpath] Error extracting xpath values", ex);
      }
    }

    return vals;
  }

  /**
   * Extract values from a xml file using a xpath.
   *
   * @param xpath The Xpath to use for extraction.
   * @param fileName The filename of the xml file.
   * @return List of values (String) extracted.
   */
  public static List extractFromXpath(String xpath, String fileName)
  {
    List vals = null;
    if (!isEmptyStr(xpath))
    {
      // extract
      try
      {
        vals = getXmlMgr().getXPathValues(fileName, xpath);
      }
      catch (Exception ex)
      {
        Logger.warn("[DocExtractionHelper.extractFromXpath] Error extracting xpath values", ex);
      }
    }

    return vals;
  }

  /**
   * Extract a value from a xml file using a xpath.
   *
   * @param xpath The Xpath to use for extraction.
   * @param fileName The filename of the xml file.
   * @return The value extracted, or <b>null</b> if none exists. Note that if
   * the xpath points to multiple occurrences of elements, then only the first
   * value is returned.
   */
  public static String getValueAtXpath(String xpath, String fileName)
  {
    String val = null;
    List values = extractFromXpath(xpath, fileName);

    if (values != null && !values.isEmpty())
      val = (String)values.get(0);

    return val;
  }

  /**
   * Check the document identifier of a previous sent document against the
   * document identifier of a currently received document.
   *
   * @param trackRecord The ResponseTrackRecord
   * @param activeTR The ActiveTrackRecord for the sent document
   * @param receivedDoc The document identifier of the received document.
   * @return <b>true</b> if the document identifiers are the same, <b>false</b>
   * otherwise.
   */
  public static boolean checkDocumentIdentifier(ResponseTrackRecord trackRecord,
                                                ActiveTrackRecord activeTR,
                                                String receivedDocId)
    throws Throwable
  {
    boolean match = false;
    GridDocument sentGdoc = findGridDocument(activeTR.getSentGridDocUID());

    if (sentGdoc != null)
    {
      String sentDocId = getDocumentIdentifier(trackRecord.getSentDocIdXpath(),
                                               sentGdoc);
      if (isEmptyStr(sentDocId))
        throw new ResponseTrackingException("Invalid Document Identifier in sent document: "+sentDocId);

      if (sentDocId.equals(receivedDocId))
        match = true;
    }

    return match;
  }

  /**
   * Find a GridDocument
   * @param gdocUID UID of the GridDocument.
   * @return The GridDocument found, or <b>null</b> if none exists.
   */
  public static GridDocument findGridDocument(Long gdocUID)
  {
    GridDocument gdoc = null;
    try
    {
      gdoc = getDocMgr().findGridDocument(gdocUID);
    }
    catch (Exception ex)
    {
      Logger.debug("[DocExtractionHelper.findGridDocument] Unable to find GridDoc: "+
                   ex.getMessage());
    }

    return gdoc;
  }

  protected static IDocumentManagerObj getDocMgr() throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IDocumentManagerHome.class.getName(),
             IDocumentManagerHome.class,
             new Object[0]);
  }

  protected static IXMLServiceLocalObj getXmlMgr() throws ServiceLookupException
  {
    return (IXMLServiceLocalObj)ServiceLocator.instance(
             ServiceLocator.LOCAL_CONTEXT).getObj(
             IXMLServiceLocalHome.class.getName(),
             IXMLServiceLocalHome.class,
             new Object[0]);
  }

}