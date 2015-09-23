/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SortedTableRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-16     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.renderers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
 
//@todo - initPrototypesFromLayout() method
public class SortedTableRenderer extends SelectionTableRenderer
{  
  private boolean _isSortable = false;
  private boolean _isAscending = true;
  protected String _sortedColumn = null;  // 'null' means currently not sorted
  
  public SortedTableRenderer(RenderingContext rContext)
  {
    super(rContext);
  }

  public void reset()
  {
    super.reset();
    _isSortable = false;
    _isAscending = true;
    _sortedColumn = null;
  }

  public void setAscending(boolean isAscending)
  {
    _isAscending = isAscending;
  }

  public boolean isAscending()
  {
    return _isAscending;
  }

  public void setSortable(boolean isSortable)
  {
    _isSortable = isSortable;
  }

  public boolean isSortable()
  {
    return _isSortable;
  }

  public void setSortedColumn(String sortedColumn)
  {
    _sortedColumn = sortedColumn;
  }

  public String getSortedColumn()
  {
    return _sortedColumn;
  }

  protected void decorateHeaderCell(RenderingContext rContext, Element td, int col)
    throws RenderingException
  {
    super.decorateHeaderCell(rContext, td, col);

    if(isSortable())
    {
      Node textNode = td.getFirstChild();
      if(textNode.getNodeType() == Node.TEXT_NODE)
      {
        Document doc = textNode.getOwnerDocument();
        Element anchorNode = doc.createElement("a");
        td.replaceChild(anchorNode, textNode);
        anchorNode.setAttribute("class", "listheader");
        anchorNode.setAttribute("style", "font-size: 10px;"); // hard code because A overrides .listheader in .css
        anchorNode.setAttribute("href", "javascript: void jalan(appendParameterToUrl(_refreshUrl, 'newSortColumn', '" + (col + 1)  + "'));");
        anchorNode.appendChild(textNode);
      }
      else
      {
        throw new IllegalStateException("textNode.getNodeType()=" + textNode.getNodeType());
      }
    }      
  }
}
