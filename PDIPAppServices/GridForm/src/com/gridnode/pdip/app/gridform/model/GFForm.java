/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GFForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 22 2002    Jared Low           Created.
 */
package com.gridnode.pdip.app.gridform.model;

import java.io.Serializable;

/**
 * Value object that wraps the template, definition and depository.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class GFForm implements Serializable
{
  private LayoutTemplate _template;
  private LayoutDefinition _definition;
  private DataDepository _depository;

  public GFForm()
  {
  }

  public void setTemplate(LayoutTemplate template)
  {
    _template = template;
  }

  public LayoutTemplate getTemplate()
  {
    return _template;
  }

  public void setDefinition(LayoutDefinition definition)
  {
    _definition = definition;
  }

  public LayoutDefinition getDefinition()
  {
    return _definition;
  }

  public void setDepository(DataDepository depository)
  {
    _depository = depository;
  }

  public DataDepository getDepository()
  {
    return _depository;
  }
}