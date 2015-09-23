package com.gridnode.pdip.framework.db.keygen;

/**
 * Title:        PDIP
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode
 * @author Mahesh
 * @version 1.0
 */
import javax.ejb.*;
import java.rmi.*;
public interface IKeyGeneratorHome extends EJBHome {
    public IKeyGeneratorObj create(String primaryKey,long id)
        throws  CreateException, RemoteException;

    public IKeyGeneratorObj findByPrimaryKey(String primaryKey)
        throws  FinderException, RemoteException;

}