/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocAlertProviderList.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 30 2003    Neo Sok Lay         Created
 * Feb 26 2003    Neo Sok Lay         Remove SystemData data provider.
 *                                    Automatically provided in super class.
 */
package com.gridnode.gtas.server.docalert.model;

import com.gridnode.gtas.server.document.helpers.FileHelper;
import com.gridnode.gtas.server.document.model.GridDocument;

import com.gridnode.pdip.app.alert.providers.IDataProvider;
import com.gridnode.pdip.app.alert.providers.DefaultProviderList;

/**
 * ProviderList for use when raising alerts related to documents.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class DocAlertProviderList extends DefaultProviderList
{
  /**
   * Construct the DocAlertProviderList with the GridDocument data and
   * User document data.
   *
   * @param gdocData The data provider for GridDocument.
   * @param udocData The data provider for User document.
   */
  public DocAlertProviderList(
    GridDocumentData gdocData, UserDocumentData udocData)
  {
    super(new IDataProvider[] {gdocData, udocData});
  }

  /**
   * Construct the DocAlertProviderList with the GridDocument data.
   * @param gdocData The data provider for GridDocument.
   */
  public DocAlertProviderList(
    GridDocumentData gdocData)
  {
    super(new IDataProvider[] {gdocData});
  }


  /**
   * Get a DocAlertProviderList for a GridDocument.
   *
   * @param gdoc The GridDocument.
   * @param recipient Any custom email code recipients (for the User document
   * data provider).
   * @return the constructed DocAlertProviderList.
   */
  public static DocAlertProviderList newProviderList(GridDocument gdoc, String recipients)
  {
    GridDocumentData gdocData = new GridDocumentData(gdoc);
    UserDocumentData udocData = null;
    try
    {
      String udocFilename = FileHelper.getUdocFile(gdoc).getCanonicalPath();
      udocData = new UserDocumentData(udocFilename, recipients);
    }
    catch (Exception ex)
    {
    }
    if (udocData != null)
      return new DocAlertProviderList(gdocData, udocData);
    else
      return new DocAlertProviderList(gdocData);
  }
}