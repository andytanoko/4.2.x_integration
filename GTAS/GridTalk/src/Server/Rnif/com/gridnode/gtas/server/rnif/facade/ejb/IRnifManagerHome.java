
package com.gridnode.gtas.server.rnif.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * LocalHome interface 
 *
 * @author Liu Xiaohua
 *
 * @version 2.0
 * @since 2.0
 */
public interface IRnifManagerHome
  extends        EJBHome
{
  /**
   * Create the RnifManagerBean.
   *
   * @returns EJBLObject for the DocumentTypeManagerBean.
   */
  public IRnifManagerObj create()
    throws CreateException, RemoteException;
}