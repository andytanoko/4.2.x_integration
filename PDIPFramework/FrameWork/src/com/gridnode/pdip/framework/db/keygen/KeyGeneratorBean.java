/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IKeyGenerator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 08 2002    Neo Sok Lay         Get next Id, excluding generated id
 *                                    contained in a specified key set.
 * Nov 06 2006    Tam Wei Xiang       Add method to set the nextRef number.                                   
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

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

public abstract class KeyGeneratorBean implements EntityBean
{
  private EntityContext _ctx = null;

  public void setEntityContext(EntityContext ctx)
  {
    _ctx = ctx;
  }

  public void unsetEntityContext()
  {
    _ctx = null;
  }

  public String ejbCreate(String primaryKey,long id)
    throws CreateException, RemoteException
  {
    setRefName(primaryKey);
    setLastRefNum(id);
    return null;
  }

  public void ejbPostCreate(String primaryKey,long id)
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  public void ejbLoad()
  {
  }

  public void ejbStore()
  {
  }

  public abstract void setRefName(String refName);
  public abstract String getRefName();

  public abstract void setLastRefNum(long lastRefNum);
  public abstract long getLastRefNum();

  public long getNextId()
  {
    setLastRefNum(getLastRefNum()+1);
    return getLastRefNum();
  }
  
  public long getNextId(long maxId)
  {
  	//NSL20061113 Check the maxId is larger than the current lastRefNum, if not, just use the normal getNextId()  
  	if (maxId > getLastRefNum())
  	{
  	  long nextRefNum = maxId + 1;
        setLastRefNum(nextRefNum);
        return nextRefNum;
  	}
  	else
  	{
  	  return getNextId();	
  	}
  }
}