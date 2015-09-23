/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractPersistable.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 29, 2006   i00107              Created
 */

package com.gridnode.util.db;

import com.gridnode.util.UidGenerator;

/**
 * @author i00107
 * Abstract implementation of a persistable object.
 */
public abstract class AbstractPersistable implements IPersistable
{
  private String _uid = UidGenerator.createUid(); //default
  private Integer _version;
  
  /**
   * @see com.gridnode.util.db.IPersistable#getUID()
   */
  public String getUID()
  {
    return _uid;
  }

  /**
   * @see com.gridnode.util.db.IPersistable#getVersion()
   */
  public Integer getVersion()
  {
    return _version;
  }

  /**
   * @see com.gridnode.util.db.IPersistable#setUID(java.lang.String)
   */
  public void setUID(String uid)
  {
    _uid = uid;
  }

  /**
   * @see com.gridnode.util.db.IPersistable#setVersion(java.lang.Integer)
   */
  public void setVersion(Integer version)
  {
    _version = version;

  }

  /**
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    
    if (!(getClass().isInstance(obj)))
    {
      return false;
    }
    
    if (_uid == null)
    {
      return false;
    }
    
    AbstractPersistable other = (AbstractPersistable)obj;
    
    return _uid.equals(other.getUID());
  }

  /**
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    if (_uid != null)
    {
      return _uid.hashCode();
    }
    else
    {
      return super.hashCode();
    }
  }

  /**
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return getClass().getName() + "[uid="+_uid+"]";
  }

  
}
