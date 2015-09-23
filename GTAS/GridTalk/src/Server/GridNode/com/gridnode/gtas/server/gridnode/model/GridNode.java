/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 17 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.gridnode.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.sql.Timestamp;

/**
 * This entity keeps track of a virtual node in the Gridnode network.
 * The data model:<p>
 * <PRE>
 * UID                - UID of this GridNode instance.
 * ID                 - ID of this GridNode instance.
 * Name               - Name of this GridNode instance.
 * Category           - GnCategory (code) of this GridNode instance.
 * State              - Current state of this GridNode instance.
 * ActivateReason     - Reason for activating this GridNode (if applicable).
 * DTCreated          - Timestamp for the time when This GridNode is created.
 * DTReqActivate      - Timestamp for the time when this GridNode is in pending
 *                      state for activation.
 * DTActivated        - Timestamp for the time when this GridNode becomes
 *                      activated.
 * CoyProfileUID      - UID of the CompanyProfile of this GridNode instance.
 * ConnectionStatus   - ConnectionStatus for this GridNode instance.
 * </PRE>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GridNode
  extends    AbstractEntity
  implements IGridNode
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7572195968710736441L;
	private String  _id;
  private String  _name;
  private String  _category;
  private short   _state;
  private String  _activationReason;
  private Timestamp _dtCreated;
  private Timestamp _dtReqActivate;
  private Timestamp _dtActivated;
  private Timestamp _dtDeactivated;
  private Long    _coyProfileUID;

  public GridNode()
  {
  }

  public String getEntityDescr()
  {
    return getID() + " - " + getName();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ****************** Getters & Setters **********************************

  public void setID(String id)
  {
    _id = id;
  }

  public String getID()
  {
    return _id;
  }

  public void setName(String name)
  {
    _name = name;
  }

  public String getName()
  {
    return _name;
  }

  public void setCategory(String category)
  {
    _category = category;
  }

  public String getCategory()
  {
    return _category;
  }

  public void setState(short state)
  {
    _state = state;
  }

  public short getState()
  {
    return _state;
  }

  public void setActivationReason(String reason)
  {
    _activationReason = reason;
  }

  public String getActivationReason()
  {
    return _activationReason;
  }

  public void setDTCreated(Timestamp dtCreated)
  {
    _dtCreated = dtCreated;
  }

  public Timestamp getDTCreated()
  {
    return _dtCreated;
  }

  public void setDTReqActivate(Timestamp dtReqActivate)
  {
    _dtReqActivate = dtReqActivate;
  }

  public Timestamp getDTReqActivate()
  {
    return _dtReqActivate;
  }

  public void setDTActivated(Timestamp dtActivated)
  {
    _dtActivated = dtActivated;
  }

  public Timestamp getDTActivated()
  {
    return _dtActivated;
  }

  public void setDTDeactivated(Timestamp dtDeactivated)
  {
    _dtDeactivated = dtDeactivated;
  }

  public Timestamp getDTDeactivated()
  {
    return _dtDeactivated;
  }

  public void setCoyProfileUID(Long profileUID)
  {
    _coyProfileUID = profileUID;
  }

  public Long getCoyProfileUID()
  {
    return _coyProfileUID;
  }

}