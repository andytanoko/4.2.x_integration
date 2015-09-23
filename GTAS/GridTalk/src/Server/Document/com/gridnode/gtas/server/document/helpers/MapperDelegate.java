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
 * Aug 13 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerHome;
import com.gridnode.gtas.server.mapper.facade.ejb.IGridTalkMappingManagerObj;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class provides mapping services used by document management.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class MapperDelegate
{
  /**
   * Obtain the EJBObject for the GridTalkMappingManagerBean.
   *
   * @return The EJBObject to the GridTalkMappingManagerBean.
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
   * Obtain the EJBObject for the MappingManagerBean.
   *
   * @return The EJBObject to the MappingManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IMappingManagerObj getAppMappingManager()
    throws ServiceLookupException
  {
    return (IMappingManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IMappingManagerHome.class.getName(),
      IMappingManagerHome.class,
      new Object[0]);
  }

  /**
   * Transform the GridDocument using the Mapping Rule. The GridDocument will
   * first be converted into a DOM representation before being transformed.
   * After the transformation, its DOM representation will be converted back
   * to a GridDocument entity.
   *
   * @param gdoc The fullpath of the GridDocument to transform.
   * @param mappingRuleUID The UID of the Mapping Rule used.
   * @return collection of the transformed GridDocuments.
   *
   * @since 2.0
   */
/*  public static Collection doHeaderTransform(GridDocument gdoc,
                                             Long mappingRuleUID)
                                             throws Exception
  {
    GridTalkMappingRule gtMR = getManager().findGridTalkMappingRule(mappingRuleUID);
    if (!gtMR.isHeaderTransformation())
    {
      throw new Exception("MappingRule is not header mapping rule");
    }
    return getManager().transform(gtMR, gdoc);
  }
*/
}