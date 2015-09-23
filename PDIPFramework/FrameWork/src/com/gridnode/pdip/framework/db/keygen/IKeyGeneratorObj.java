/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IKeyGeneratorObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 08 2002    Neo Sok Lay         Get next Id, excluding generated id
 *                                    contained in a specified key set.
 * Nov 06 2006    Tam Wei Xiang       Add method setNextRefNum(Long maxId)
 *                                    to set the nextRef number.                                    
 */
package com.gridnode.pdip.framework.db.keygen;

/**
 * Title:        PDIP
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode
 * @author Mahesh
 * @version 1.0
 */
import java.rmi.RemoteException;

import javax.ejb.EJBObject;

public interface IKeyGeneratorObj extends EJBObject
{
  public long getNextId() throws RemoteException;

  public long getNextId(long maxId) throws RemoteException;
  
}