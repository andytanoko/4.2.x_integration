package com.gridnode.pdip.framework.db.cursor;

import java.util.*;
import javax.ejb.*;
import java.rmi.RemoteException;

public interface  ICursorHome extends EJBHome{

	public ICursorObj create()  throws CreateException, RemoteException;

	public ICursorObj create(Collection col) throws CreateException, RemoteException;

}
