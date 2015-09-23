/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchGridNodeResults.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 15 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.model;

import com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode;

import com.gridnode.pdip.framework.db.DataObject;

/**
 * This entity represents a list of GridNodes returned as search results of
 * a submitted GridNode search. This entity is not persistent to the database.
 *
 * The Model:<BR><PRE>
 *   GridNodes  - Array of SyncGridNode(s) representing the GridNodes returned.
 * </PRE>
 * <P>
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SearchGridNodeResults extends DataObject
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2005854053722166279L;
	private SyncGridNode[] _gridnodes;

  public SearchGridNodeResults()
  {
  }

  public SyncGridNode[] getGridNodes()
  {
    return _gridnodes;
  }

  public void setGridNodes(SyncGridNode[] gridnodes)
  {
    _gridnodes = gridnodes;
  }

  // **************** Overrides DataObject *********************************

  public void preSerialize()
  {
    if (_gridnodes != null)
    {
      for (int i=0; i<_gridnodes.length; i++)
        _gridnodes[i].preSerialize();
    }
  }

  public void postSerialize()
  {
    if (_gridnodes != null)
    {
      for (int i=0; i<_gridnodes.length; i++)
        _gridnodes[i].postSerialize();
    }
  }

  public void postDeserialize()
  {
    if (_gridnodes != null)
    {
      for (int i=0; i<_gridnodes.length; i++)
        _gridnodes[i].postDeserialize();
    }
  }

}