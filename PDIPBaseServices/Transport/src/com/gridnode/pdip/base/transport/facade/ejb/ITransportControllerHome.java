package com.gridnode.pdip.base.transport.facade.ejb;

/**
 * <p>Title: PDIP : Peer Data Interchange Platform</p>
 * <p>Description: Transport Module - for PDIP</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd - Singapore</p>
 * @author Jagadeesh
 * @version 1.0
 */

  import javax.ejb.EJBHome;
  import javax.ejb.CreateException;
  import java.rmi.RemoteException;


  public interface ITransportControllerHome extends EJBHome
  {
    public ITransportControllerObj create() throws CreateException,RemoteException;

  }