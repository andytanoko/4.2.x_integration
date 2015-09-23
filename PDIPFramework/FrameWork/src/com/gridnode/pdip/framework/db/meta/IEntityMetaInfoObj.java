/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityMetaInfoObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 11 2002    Neo Sok Lay         Renamed from IEntityMetaInfo.java to
 *                                    conform to naming convention.
 */
package com.gridnode.pdip.framework.db.meta;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * This is the Remote interface for the EntityMetaInfoBean.
 *
 * @author Neo Sok Lay
 * @version 2.0 I7
 * @since 2.0
 */
public interface IEntityMetaInfoObj extends EJBObject
{
  public EntityMetaInfo getData() throws RemoteException;
}