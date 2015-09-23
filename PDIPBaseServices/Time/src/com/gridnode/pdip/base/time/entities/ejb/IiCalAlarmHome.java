// %1023788045465:com.gridnode.pdip.base.time.ejb%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: 
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.ejb;

import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.filter.*;

import java.rmi.*;

import java.util.*;

import javax.ejb.*;

public interface IiCalAlarmHome
  extends EJBHome
{

  /**
   * DOCUMENT ME!
   * 
   * @param data DOCUMENT ME!
   * @return DOCUMENT ME! 
   * @throws CreateException DOCUMENT ME!
   * @throws RemoteException DOCUMENT ME!
   */
  public IiCalAlarmObj create(IEntity data)
                       throws CreateException, RemoteException;

  /**
   * DOCUMENT ME!
   * 
   * @param primaryKey DOCUMENT ME!
   * @return DOCUMENT ME! 
   * @throws FinderException DOCUMENT ME!
   * @throws RemoteException DOCUMENT ME!
   */
  public IiCalAlarmObj findByPrimaryKey(Long primaryKey)
                                 throws FinderException, RemoteException;

  /**
   * DOCUMENT ME!
   * 
   * @param filter DOCUMENT ME!
   * @return DOCUMENT ME! 
   * @throws FinderException DOCUMENT ME!
   * @throws RemoteException DOCUMENT ME!
   */
  public Collection findByFilter(IDataFilter filter)
                          throws FinderException, RemoteException;
}