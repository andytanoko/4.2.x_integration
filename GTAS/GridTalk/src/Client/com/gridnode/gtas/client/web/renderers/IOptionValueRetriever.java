/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IOptionValueRetriever.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-15     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

import com.gridnode.gtas.client.GTClientException;

public interface IOptionValueRetriever
{
  public String getOptionText(Object choice) throws GTClientException;
  public String getOptionValue(Object choice) throws GTClientException;
  //public boolean isOptionSelected(Object option) throws GTClientException;
}