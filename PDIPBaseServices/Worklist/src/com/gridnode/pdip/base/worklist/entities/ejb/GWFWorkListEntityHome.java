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

/**
 * GWFWorkListEntityHome is WorkList specific Entity Home which publishes
 * life cycle methods for home object. Value object is passed as parameter
 * for creating and querying the table.
 *
 * Table                 Database                Descriptin.
 * ----                 ---------                ----------
 * 1.worklistvalue       GWFDB                   This table stores worklist items(user specific).
 */


public interface GWFWorkListEntityHome extends EJBHome {

    public GWFWorkListEntityObject create(IEntity data)
    throws CreateException,RemoteException;

    public GWFWorkListEntityObject findByPrimaryKey(Long primaryKey)
    throws FinderException,RemoteException;

    public Collection findByFilter(IDataFilter filter)
    throws FinderException,RemoteException;
}
