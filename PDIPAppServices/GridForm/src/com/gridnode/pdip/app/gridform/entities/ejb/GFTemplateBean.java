/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GFTemplateBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.entities.ejb;

import com.gridnode.pdip.app.gridform.model.GFTemplate;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * This local entity bean provides persistency services for GFTemplate
 * records.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class GFTemplateBean extends AbstractEntityBean
{
  public String getEntityName()
  {
    return GFTemplate.ENTITY_NAME;
  }
}
