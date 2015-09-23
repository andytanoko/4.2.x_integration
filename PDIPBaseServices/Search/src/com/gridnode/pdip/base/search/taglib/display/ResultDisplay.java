/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResultDisplay.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 24 2002    H.Sushil            Created
 */

package com.gridnode.pdip.base.search.taglib.display;

import com.gridnode.pdip.base.search.helpers.FrameworkDbHelper;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspTagException;

import javax.servlet.jsp.JspWriter;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class ResultDisplay extends TagSupport
{
   private List resultSet;
   private Set keySet;
   private String rowId;

   public int doStartTag() throws JspTagException
   {
      try
      {

	JspWriter out  = pageContext.getOut();
	Iterator iter;
	HashMap hTemp = (HashMap)resultSet.get(0);

	keySet = hTemp.keySet();
	iter = keySet.iterator();

	out.println(IDisplayOptions.HTML_TABLE_START_TAG);
	iter = keySet.iterator();

        out.println(IDisplayOptions.HTML_ROW_START_TAG);

	out.println(IDisplayOptions.HTML_CELL_START_TAG);

	out.println(IDisplayOptions.HTML_SPACE_TAG);

	out.println(IDisplayOptions.HTML_CELL_END_TAG);

	while(iter.hasNext())
	{
	    out.println(IDisplayOptions.HTML_CELL_START_TAG);
	    out.println(iter.next());
	    out.println(IDisplayOptions.HTML_CELL_END_TAG);
	}

        out.println(IDisplayOptions.HTML_ROW_END_TAG);

	for(int i=0;i<resultSet.size();i++)
	{
	  hTemp = (HashMap)resultSet.get(i);
	  iter = keySet.iterator();

 	  out.println(IDisplayOptions.HTML_ROW_START_TAG);

	  out.println(IDisplayOptions.HTML_CELL_START_TAG);
          out.println(IDisplayOptions.HTML_CHECKBOX_INPUT_TAG+" name = \""+rowId+i+"\""+ " value = \""+hTemp.get(rowId)+"\">");
	  out.println(IDisplayOptions.HTML_CELL_END_TAG);

	  while(iter.hasNext())
	  {
	    out.println(IDisplayOptions.HTML_CELL_START_TAG);
	    out.println(hTemp.get(iter.next()));
	    out.println(IDisplayOptions.HTML_CELL_END_TAG);
	  }

	  out.println(IDisplayOptions.HTML_ROW_END_TAG);
	}

	out.println(IDisplayOptions.HTML_TABLE_END_TAG);

	return SKIP_BODY;
      }
      catch (Exception e)
      {
         throw new JspTagException(e.getMessage());
      }
    }

    public void setResultSet(ArrayList resultSet)
    {
      this.resultSet = resultSet;
    }

    public void setRowId(String rowId)
    {
      this.rowId = rowId;
    }
}