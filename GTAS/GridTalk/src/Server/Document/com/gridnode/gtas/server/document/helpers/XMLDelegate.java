/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 22 2002    Koh Han Sing        Created
 * Jun 08 2004    Neo Sok Lay         Added checkRootElement() to check the
 *                                    root element of an XML file for match
 *                                    against a specified root element name.
 * Nov 10 2005    Neo Sok Lay         Use Local context to lookup XMLService                                   
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;
import com.gridnode.pdip.base.xml.helpers.XMLServiceHandler;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.xml.adapters.GNDocument;

import java.io.File;

/**
 * This class provides XML services.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.3.4
 * @since 2.0
 */
public class XMLDelegate
{

  /**
   * Obtain the EJBObject for the XMLServiceBean.
   *
   * @return The EJBObject to the XMLServiceBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IXMLServiceLocalObj getManager()
    throws ServiceLookupException
  {
    return (IXMLServiceLocalObj)ServiceLocator.instance(
      ServiceLocator.LOCAL_CONTEXT).getObj(
      IXMLServiceLocalHome.class.getName(),
      IXMLServiceLocalHome.class,
      new Object[0]);
  }
  
  /**
   * Check the root element of the specified XML file against the given
   * root element name.
   * 
   * @param xmlFile The XML file handle.
   * @param rootElement The root element name.
   * @return <b>true</b> if <code>xmlFile</code> is a valid XML file and
   * it's root element matches <code>rootElement</code>.
   */
  public static boolean checkRootElement(File xmlFile, String rootElement)
  {
    boolean match = false;
    try
    {
      GNDocument doc = XMLServiceHandler.getInstance().getDocument(xmlFile);
      match = doc.getRootElement().getName().equals(rootElement);
    }
    catch (Exception ex)
    {
      Logger.warn("[XMLDelegate.checkRootElement] Not an XML file!");
    }
    return match;
  }
}