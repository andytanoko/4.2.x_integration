/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFieldMetaInfoLocalObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 11 2002    Neo Sok Lay         Renamed from IFieldMetaInfo.java to
 *                                    follow naming convention.
 */
package com.gridnode.pdip.framework.db.meta;

import javax.ejb.EJBLocalObject;

/**
 * This is the Local interface for the FieldMetaInfo entity bean.
 *
 * @author Neo Sok Lay
 * @version 2.0 I7
 * @since 2.0
 */
public interface IFieldMetaInfoLocalObj extends EJBLocalObject
{
  public FieldMetaInfo getData();
}