/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsRouter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This JmsRouter entity holds the information about a Jms Router that
 * the GridTalk server can contact for communication with GridMaster.<p>
 *
 * The data model:<PRE>
 * UID       - The UID of the Jms router entity.
 * Name      - The name given to the Jms router.
 * IpAddress - The IP address of the Jms router.
 * </PRE>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class JmsRouter
  extends    AbstractEntity
  implements IJmsRouter
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2540394549908429781L;
	private String        _name;
  private String        _ipAddress;

  public JmsRouter()
  {
  }

  // ************** Methods from AbstractEntity ***************************

  public String getEntityDescr()
  {
    return getName() + " - " + getIpAddress();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters & Setters *********************************

  public void setName(String name)
  {
    _name = name;
  }

  public String getName()
  {
    return _name;
  }

  public void setIpAddress(String ipAddress)
  {
    _ipAddress = ipAddress;
  }

  public String getIpAddress()
  {
    return _ipAddress;
  }

}