
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDisplayOptions.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 24 2002    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.taglib.display;

public interface IDisplayOptions
{
  public static final String HTML_TEXT_INPUT_TAG    = "<input type=\"text\"";// Tag is not closed because the name of the field is generated dynamically

  public static final String HTML_TABLE_START_TAG   = "<table width=\"80%\" border=\"0\">";
  public static final String HTML_TABLE_END_TAG     = "</table>";

  public static final String HTML_ROW_START_TAG     = "<tr>";
  public static final String HTML_ROW_END_TAG       = "</tr>";

  public static final String HTML_CELL_START_TAG    = "<td>";
  public static final String HTML_CELL_END_TAG      = "</td>";

  public static final String HTML_LINE_BREAK        = "<BR>";
  public static final String HTML_HIDDEN_INPUT_TAG  = "<input type=\"hidden\"";// Tag is not closed because the name and value of the field is generated dynamically

  public static final String HTML_SPACE_TAG         = "&nbsp;";

  public static final String HTML_CHECKBOX_INPUT_TAG="<input type=\"checkbox\"";// Tag is not closed because the name and value of the field is generated dynamically

  public static final String HTML_SELECT_START_TAG  ="<select";// Tag is not closed because the name and value of the field is generated dynamically
  public static final String HTML_SELECT_END_TAG    ="</select";// Tag is not closed because the name and value of the field is generated dynamically

  public static final String HTML_OPTION_START_TAG    ="<option";// Tag is not closed because the name and value of the field is generated dynamically
  public static final String HTML_OPTION_END_TAG    ="</option>";// Tag is not closed because the name and value of the field is generated dynamically

}