/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IBFPROptionSource.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-14     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.*;

import com.gridnode.gtas.client.GTClientException;

public interface IBFPROptionSource
{
  /**
   * Returns a collection of objects that the bfpr will use if it needs to render options for
   * a field. If no options are provided, this should return an empty collection. If the bfpr
   * is to use its standard methodology for providing options then you should return null.
   * You may identify the field & entity being rendered by its fieldId as supplied by the
   * bfprs get methods. Be sure to return the correct type of data for the options being rendered
   * or bad things will happen!
   * The BFPR will still send the returned options through any filter you may have specified
   * so you get an extra chance to narrow things down.
   * nb: Currently only FER fields have OptionSource support. When I see how well this goes I
   * plan to refactor significantly and expand the features and supported fields for OptionSource.
   * 20030114AH
   * @param rContext - The BFPRs RenderingContext
   * @param bfpr - the BindingFieldPropertyRenderer itself
   * @returns collection of appropriate object instances to render options or null
   * @throws GTClientException
   */
  public Collection getOptions(RenderingContext rContext, BindingFieldPropertyRenderer bfpr )
    throws GTClientException;
}