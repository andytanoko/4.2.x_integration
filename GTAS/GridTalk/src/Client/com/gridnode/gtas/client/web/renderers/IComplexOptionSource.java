/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IComplexOptionSource.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

import com.gridnode.gtas.client.GTClientException;


public interface IComplexOptionSource extends IBFPROptionSource
{
  public IOptionValueRetriever getOptionValueRetriever(RenderingContext rContext, BindingFieldPropertyRenderer bfpr )
    throws GTClientException;
    
  public IColumnObjectAdapter getColumnObjectAdapter(RenderingContext rContext, BindingFieldPropertyRenderer bfpr )
    throws GTClientException;
}
