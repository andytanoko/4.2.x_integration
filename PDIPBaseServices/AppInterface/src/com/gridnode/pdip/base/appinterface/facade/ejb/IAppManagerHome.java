package com.gridnode.pdip.base.appinterface.facade.ejb;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

public interface IAppManagerHome extends javax.ejb.EJBHome
{
  public IAppManagerObj create() throws CreateException, RemoteException;
}