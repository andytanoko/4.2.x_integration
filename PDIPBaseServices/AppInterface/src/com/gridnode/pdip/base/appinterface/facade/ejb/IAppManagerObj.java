package com.gridnode.pdip.base.appinterface.facade.ejb;

import java.rmi.RemoteException;

import com.gridnode.pdip.base.appinterface.data.AppDefinitionDoc;
import com.gridnode.pdip.base.appinterface.exception.AppExecutionException;
import com.gridnode.pdip.base.appinterface.exception.AppNotInitializedException;

public interface IAppManagerObj extends javax.ejb.EJBObject
{
  public Object executeApp(AppDefinitionDoc appDefDoc, int appType, Object params)
    throws AppNotInitializedException, AppExecutionException, RemoteException;
}