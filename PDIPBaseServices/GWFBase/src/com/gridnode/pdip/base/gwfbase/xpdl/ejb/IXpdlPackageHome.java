// %1023949933952:com.gridnode.pdip.base.gwfbase.xpdl.ejb%
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
 * Jun 10 2002   Mahesh	      Created
 * Jun 13 2002   Mathew         Repackaged
 */

package com.gridnode.pdip.base.gwfbase.xpdl.ejb;

import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.filter.*;

import java.rmi.*;

import java.util.*;

import javax.ejb.*;

public interface IXpdlPackageHome
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
  public IXpdlPackageObj create(IEntity data)
                            throws CreateException, RemoteException;

  /**
   * DOCUMENT ME!
   *
   * @param primaryKey DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws FinderException DOCUMENT ME!
   * @throws RemoteException DOCUMENT ME!
   */
  public IXpdlPackageObj findByPrimaryKey(Long primaryKey)
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