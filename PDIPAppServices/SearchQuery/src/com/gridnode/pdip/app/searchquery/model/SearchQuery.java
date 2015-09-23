/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchQuery.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 21 2003    Koh Han Sing        Created
 * Apr 26 2009    Tam Wei Xiang       #149 - add field "isPublic"
 */
package com.gridnode.pdip.app.searchquery.model;

import java.util.ArrayList;
import java.util.List;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class SearchQuery
  extends    AbstractEntity
  implements ISearchQuery
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5742912008698137439L;
	protected String      _name;
  protected String      _description;
  protected String      _createdBy;
  protected ArrayList   _conditions;
  protected boolean _isPublic = false;
  
  public SearchQuery()
  {
    _conditions = new ArrayList();
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getName() + "/" + getDescription();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public String getName()
  {
    return _name;
  }

  public String getDescription()
  {
    return _description;
  }

  public String getCreateBy()
  {
    return _createdBy;
  }

  public List getConditions()
  {
    return _conditions;
  }
  
  public boolean isPublic()
  {
    return _isPublic;
  }

  // *************** Setters for attributes *************************

  public void setName(String name)
  {
    _name = name;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setCreateBy(String createdBy)
  {
    _createdBy = createdBy;
  }

  public void setConditions(List conditions)
  {
    _conditions = new ArrayList(conditions);
  }

  public void addCondition(Condition condition)
  {
    _conditions.add(condition);
  }

  public void setPublic(boolean isPublic)
  {
    _isPublic = isPublic;
  }
  
}