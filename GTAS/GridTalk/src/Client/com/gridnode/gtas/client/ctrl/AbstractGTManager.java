/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractGTManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 * 2002-06-08     Andrew Hill         Added member to reference IGTSession object
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;
import java.util.Collection;

abstract class AbstractGTManager implements IGTManager
{
  protected int _type;
  protected IGTSession _session;


  public abstract void create(IGTEntity entity) throws GTClientException;

  public abstract void update(IGTEntity entity) throws GTClientException;

  public abstract void delete(IGTEntity entity) throws GTClientException;

  public abstract Collection getAll() throws GTClientException;

  /**
   * @deprecated
   */
  public abstract IGTEntity getByUID(long uID) throws GTClientException;

  public abstract IGTEntity newEntity() throws GTClientException;


  /**
   * Constructor.
   * Records the session reference and manager type.
   */
  AbstractGTManager(int type, IGTSession session)
  {
    if(session == null)
    {
      throw new java.lang.NullPointerException("No session");
    }
    _type = type;
    _session = session;
  }

  /**
   * Returns the manager tyoe as defined in IGTManager
   * @return managerType
   */
  public int getType()
  {
    return _type;
  }

  /**
   * If we arent logged in then throw a java.lang.IllegalStateException
   * @throws java.lang.IllegalStateException
   */
  protected void assertLogin()
  {
    if(!_session.isLoggedIn())
      throw new java.lang.IllegalStateException("Not logged in");
  }

  /*protected IGTFieldMetaInfo[] concatFieldMetaInfo(IGTFieldMetaInfo[] array1, IGTFieldMetaInfo[] array2)
  {
    IGTFieldMetaInfo[] newArray = new IGTFieldMetaInfo[array1.length + array2.length];
    int index = 0;
    for(int i = 0; i < array1.length; i++)
    {
      newArray[index] = array1[i];
      index++;
    }
    for(int i = 0; i < array2.length; i++)
    {
      newArray[index] = array2[i];
      index++;
    }
    return newArray;
  }*/

}