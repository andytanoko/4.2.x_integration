package com.gridnode.pdip.base.worklist.manager.ejb;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */

import java.rmi.*;
import javax.ejb.*;
//import com.gridnode.pdip.app.worklist.*;


public interface GWFWorkListSessionHome extends EJBHome{

  public GWFWorkListSessionObject create() throws
                                  CreateException,RemoteException;
}
