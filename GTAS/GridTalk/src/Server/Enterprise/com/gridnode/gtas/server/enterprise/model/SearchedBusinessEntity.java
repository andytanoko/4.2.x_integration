/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchedBusinessEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.model;

import com.gridnode.pdip.app.bizreg.model.WhitePage;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is an object model for SearchedBusinessEntity entity. A SearchedBusinessEntity 
 * represents an unit of result returned from a SearchRegistryQuery.<P>
 *
 * The Model:<BR><PRE>
 *   UUId            - Universal Unique identifier of the SearchedBusinessEntity.
 *   BusEntId        - ID of this SearchedBusinessEntity.
 *   EnterpriseId    - ID of the Enterprise owning this SearchedBusinessEntity.
 *   Description     - Description of the SearchedBusinessEntity.
 *   State           - The state of this SearchedBusinessEntity.
 *   WhitePage       - WhitePage of the SearchedBusinessEntity.
 *   Channels        - Collection of ChannelInfos associated with this SearchedBusinessEntity.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class SearchedBusinessEntity
  extends    AbstractEntity
  implements ISearchedBusinessEntity
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3708049693156422376L;
	protected String _uuid;
  protected String  _enterpriseId;
  protected String  _busEntId;
  protected String  _desc;
  protected WhitePage _whitePage;
  protected int     _state            = STATE_NEW_BE;
  protected Collection _channels = new ArrayList();
  
  public SearchedBusinessEntity()
  {
  }

  // *************** Methods from AbstractEntity ***************************
  public Number getKeyId()
  {
    return UUID;
  }

  public String getEntityDescr()
  {
    return new StringBuffer(_uuid).append('/').append(_desc).toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  // ************************* Getters for attributes ***********************

  public String getUuid()
  {
    return _uuid;
  }
  
  public String getEnterpriseId()
  {
    return _enterpriseId;
  }

  public String getBusEntId()
  {
    return _busEntId;
  }

  public String getDescription()
  {
    return _desc;
  }

  public WhitePage getWhitePage()
  {
    return _whitePage;
  }

  public int getState()
  {
    return _state;
  }

  public Collection getChannels()
  {
    return _channels;
  }
  
  // ********************** Setters for attributes ***************************

  public void setUuid(String uuid)
  {
    _uuid = uuid;
  }
  
  public void setEnterpriseId(String enterpriseId)
  {
    _enterpriseId = enterpriseId;
  }

  public void setBusEntId(String busEntId)
  {
    _busEntId = busEntId;
  }

  public void setDescription(String desc)
  {
    _desc = desc;
  }

  public void setWhitePage(WhitePage wp)
  {
    _whitePage = wp;
  }

  public void setState(int state)
  {
    _state = state;
  }

  public void setChannels(Collection channels)
  {
    _channels = channels;
  }
}
