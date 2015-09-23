package com.gridnode.pdip.framework.db.cursor;

import java.util.*;
import javax.ejb.*;
import java.rmi.RemoteException;

public interface ICursorObj extends EJBObject{

	public void setData(Collection col) throws RemoteException;

	public int size() throws RemoteException;

	public Object get(int  index) throws RemoteException;

	public Collection get(int index,int count) throws RemoteException;

	public void first() throws RemoteException;

	public void last() throws RemoteException;

	public Object next() throws RemoteException;

	public Collection next(int count) throws RemoteException;

	public Object previous() throws RemoteException;

	public Collection previous(int count) throws RemoteException;

}


