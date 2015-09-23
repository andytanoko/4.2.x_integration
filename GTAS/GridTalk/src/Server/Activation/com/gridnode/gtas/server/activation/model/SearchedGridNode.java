/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchedGridNode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.model;

import com.gridnode.pdip.framework.db.entity.*;

/**
 * This entity represents a view of a GridNode that is being returned from a
 * Search GridNode function. This entity is not persistent to the database.
 *
 * The Model:<BR><PRE>
 *   GridNodeID   - ID of the GridNode
 *   GridNodeName - Name of the GridNode
 *   State        - Current state of the GridNode as known.
 * </PRE>
 * <P>
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SearchedGridNode
  extends    AbstractEntity
  implements ISearchedGridNode
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3368272254754658815L;
	private Integer _gridnodeID;
  private String  _gridnodeName;
  private Short   _state;

  public SearchedGridNode()
  {
  }

  public String getEntityDescr()
  {
    return getGridNodeID() + "- State: "+getState();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return GRIDNODE_ID;
  }

  // *************************** Getters & Setters *****************************

  public Integer getGridNodeID()
  {
    return _gridnodeID;
  }

  public void setGridNodeID(Integer gridnodeID)
  {
    _gridnodeID = gridnodeID;
  }

  public String getGridNodeName()
  {
    return _gridnodeName;
  }

  public void setGridNodeName(String gridnodeName)
  {
    _gridnodeName = gridnodeName;
  }

  public Short getState()
  {
    return _state;
  }

  public void setState(Short state)
  {
    _state = state;
  }
}