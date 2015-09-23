/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MapperDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.mapper.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerHome;
import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerObj;
import com.gridnode.gtas.server.mapper.model.GridTalkMappingRule;
import com.gridnode.gtas.server.mapper.model.IDocumentMetaInfo;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class provides mapping services.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class MapperDelegate
{
  /**
   * Obtain the EJBObject for the GridTalkMappingManager.
   *
   * @return The EJBObject to the GridTalkMappingManager.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IGridTalkMappingManagerObj getManager()
    throws ServiceLookupException
  {
    return (IGridTalkMappingManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IGridTalkMappingManagerHome.class.getName(),
      IGridTalkMappingManagerHome.class,
      new Object[0]);
  }

  /**
   * This method will be called by the workflow module, based on the type of
   * exit, it will called the relevent method.
   *
   * @param ruleUid   The uid of the GridTalkMappingRule to use.
   * @param gdocs     The collection of griddocuments to be process.
   * @return the collection of processed griddocuments.
   *
   * @since 2.0
   */
  public static Collection execute(String ruleUid, Collection gdocs)
    throws Throwable
  {
    Logger.debug("[MapperDelegate.execute] Starts");
    Logger.debug("[MapperDelegate.execute] ruleUid : "+ruleUid);
    Logger.debug("[MapperDelegate.execute] gdocs : "+gdocs);
    ArrayList returnGdocs = new ArrayList();
    Long uid = new Long(ruleUid);
    for (Iterator i = gdocs.iterator(); i.hasNext(); )
    {
      IDocumentMetaInfo gdoc = (IDocumentMetaInfo)i.next();
      Collection resultGdocs = getManager().execute(uid, gdoc);
      returnGdocs.addAll(resultGdocs);
    }
    Logger.debug("[MapperDelegate.execute] returning : "+returnGdocs);
    return returnGdocs;
  }

  /**
   * Transform the GridDocument using the Mapping Rule. The GridDocument will
   * first be converted into a DOM representation before being transformed.
   * After the transformation, its DOM representation will be converted back
   * to a GridDocument entity.
   *
   * @param gdoc The IDocumentMetaInfo to transform.
   * @param mappingRuleUID The UID of the Mapping Rule used.
   * @return The IDocumentMetaInfo of the transformed GridDocument.
   *
   * @since 2.0
   */
  public static IDocumentMetaInfo doHeaderTransform(IDocumentMetaInfo gdoc,
                                                    Long mappingRuleUID)
                                                    throws Exception
  {
    GridTalkMappingRule gtMR =
                        getManager().findGridTalkMappingRule(mappingRuleUID);
    if (!gtMR.isHeaderTransformation())
    {
      throw new Exception("MappingRule is not header mapping rule");
    }
    return getManager().transformHeader(gtMR, gdoc);
  }
}