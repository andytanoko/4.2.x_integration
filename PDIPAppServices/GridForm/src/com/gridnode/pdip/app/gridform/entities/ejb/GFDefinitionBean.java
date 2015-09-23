/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GFDefinitionBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.entities.ejb;

import com.gridnode.pdip.app.gridform.model.GFDefinition;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * This local entity bean provides persistency services for GFDefinition
 * records.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class GFDefinitionBean extends AbstractEntityBean
{
  public String getEntityName()
  {
    return GFDefinition.ENTITY_NAME;
  }
}