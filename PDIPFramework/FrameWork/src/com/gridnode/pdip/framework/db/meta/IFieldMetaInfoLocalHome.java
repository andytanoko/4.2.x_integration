/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFieldMetaInfoLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 11 2002    Neo Sok Lay         Renamed from IFieldMetaInfoHome.java
 *                                    Renamed IFieldMetaInfo to IFieldMetaInfoLocalObj
 *                                    to follow naming convention.
 * May 21 2003    Koh Han Sing        Added method to find by label.
 */
package com.gridnode.pdip.framework.db.meta;

import java.util.Collection;

import javax.ejb.*;

/**
 * Home interface for FieldMetaInfo entity bean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0
 */
public interface IFieldMetaInfoLocalHome extends EJBLocalHome
{
  public IFieldMetaInfoLocalObj create(FieldMetaInfo metaInfo)
    throws CreateException;

  public IFieldMetaInfoLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  public Collection findByEntityName(String entityName)
    throws FinderException;

  public Collection findByLabel(String label)
    throws FinderException;
}