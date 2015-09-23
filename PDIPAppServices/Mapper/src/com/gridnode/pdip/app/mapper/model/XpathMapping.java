/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFile.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 26 2002    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class XpathMapping
  extends    AbstractEntity
  implements IXpathMapping
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 570411960365437727L;
	protected String  _rootElement;
  protected Long    _xpathUid;

  public XpathMapping()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getRootElement() + "-" + getXpathUid();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public String getRootElement()
  {
    return _rootElement;
  }

  public Long getXpathUid()
  {
    return _xpathUid;
  }

  // *************** Setters for attributes *************************

  public void setRootElement(String rootElement)
  {
    _rootElement = rootElement;
  }

  public void setXpathUid(Long xpathUid)
  {
    _xpathUid = xpathUid;
  }

}