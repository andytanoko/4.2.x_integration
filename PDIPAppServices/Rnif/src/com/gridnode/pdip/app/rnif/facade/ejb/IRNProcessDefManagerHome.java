
package com.gridnode.pdip.app.rnif.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * LocalHome interface for RNProcessDefManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface IRNProcessDefManagerHome
  extends        EJBHome
{
  /**
   * Create the RNProcessDefManagerBean.
   *
   * @returns EJBObject for the RNProcessDefManagerBean.
   */
  public IRNProcessDefManagerObj create()
    throws CreateException, RemoteException;
}