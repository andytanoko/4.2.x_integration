package com.gridnode.pdip.base.worklist.entities.ejb;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */


import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.filter.*;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;


public interface GWFWorkListUserEntityHome extends EJBHome {

    public GWFWorkListUserEntityObject create(IEntity data)
    throws CreateException,RemoteException;

    public GWFWorkListUserEntityObject findByPrimaryKey(Long primaryKey)
    throws FinderException,RemoteException;

    public Collection findByFilter(IDataFilter filter)
    throws FinderException,RemoteException;
}
