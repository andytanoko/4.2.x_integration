/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: StringValueRetriever.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

import com.gridnode.gtas.client.GTClientException;

/**
 * Class that may be used by the xhtml rendering methods to obtain the
 * text to display and the value to be submitted for fields that invlove selecting
 * choice(s) from a list.
 * This class implements IOptionValueRetriever and calls toString on the passed objects
 * returning this for both methods.
 */
public class StringValueRetriever implements IOptionValueRetriever
{
  /**
   * Returns the text to be displayed for the specified choice.
   * If choice object is null returns an empty string.
   * @param choice
   * @return choice.toString() or ""
   * @throws GTClientException
   */
  public String getOptionText(Object choice)
    throws GTClientException
  {
    return getOptionValue(choice);
  }

  /**
   * Returns the value to be submitted/stored for the specified choice.
   * If choice object is null returns an empty string.
   * @param choice
   * @return choice.toString() or ""
   * @throws GTClientException
   */
  public String getOptionValue(Object choice)
    throws GTClientException
  {
    if(choice == null)
      return "";
    else
      return choice.toString();
  }
}