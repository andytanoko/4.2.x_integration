/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SelectionTableRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

import org.w3c.dom.*;

import com.gridnode.gtas.client.utils.StaticUtils;

//@todo - initPrototypesFromLayout() method
public class SelectionTableRenderer extends ObjectTableRenderer
{  
  private boolean _isSelectable = true; // 20031019 DDJ
  private boolean _multiple;
  protected String[] _selectedItems;
  private boolean _mandatory;
  protected Element _prototypeSelectCell;
  protected Element _prototypeSelectHeaderCell;
  protected IOptionValueRetriever _valueRetriever;
  private boolean _highlightAlternateRows;
  
  public SelectionTableRenderer(RenderingContext rContext)
  {
    super(rContext);
  }

  public void setSelectable(boolean isSelectable)
  { _isSelectable = isSelectable; }

  public boolean isSelectable()
  { return _isSelectable; }

  public void reset()
  {
    super.reset();
    _isSelectable = true;
    _multiple = true;
    _selectedItems = null;
    _mandatory = false;
  }
  
  public void initPrototypesFromLayout(String documentKey)
    throws RenderingException
  {
    if (documentKey == null)
      throw new NullPointerException("documentKey is null");
    try
    {
      //@todo'
throw new UnsupportedOperationException("todo");
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error using document with key \""
                                    + documentKey
                                    + "\" to obtain layout prototype nodes",t);
    }
  }
  
  protected String getSelectionValue(RenderingContext rContext, int rowCount, Object object)
    throws RenderingException
  {
    try
    {
      return _valueRetriever == null ? StaticUtils.stringValue(object) :  _valueRetriever.getOptionValue(object);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error getting selection value for row "
                                    + rowCount
                                    + " where object="
                                    + object, t);
    }
  }
  
  public boolean isMandatory()
  {
    return _mandatory;
  }

  public boolean isMultiple()
  {
    return _multiple;
  }

  public String[] getSelectedItems()
  {
    return _selectedItems;
  }

  public void setMandatory(boolean b)
  {
    _mandatory = b;
  }

  public void setMultiple(boolean b)
  {
    _multiple = b;
  }

  public void setSelectedItems(String[] strings)
  {
    _selectedItems = strings;
  }

  public void setPrototypeSelectCell(Element element)
  {
    assertElementType(element,"td");
    _prototypeSelectCell = element;
  }

  public Element getPrototypeSelectCell()
  { return _prototypeSelectCell; }

  public void setPrototypeSelectHeaderCell(Element element)
  {
    assertElementType(element,"th");
    _prototypeSelectHeaderCell = element;
  }

  public Element getPrototypeSelectHeaderCell()
  { return _prototypeSelectHeaderCell; }

  protected Element createSelectHeaderCell(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      String tbodyId = _tableName + "_body"; //20030411AH
      Element selectHeaderCell = null;
      if(_prototypeSelectHeaderCell == null)
      {
        selectHeaderCell = _target.createElement("th");
      }
      else
      {
        selectHeaderCell = (Element)_prototypeSelectHeaderCell.cloneNode(true);
      }
      
      //20030617AH - commentout -renderLabelCarefully(selectHeaderCell,"listview.select"); //20030411AH
      replaceTextCarefully(selectHeaderCell,""); //20030617AH
      return selectHeaderCell;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating select header cell",t);
    }
  }

  protected void decorateBodyCell(RenderingContext rContext,Element td,int col,int rowCount,Object object)
    throws RenderingException
  {
    if(isSelectable())
    {
      if(col == 0)
      {
        Element row = (Element)td.getParentNode();
        Element selectCell = createSelectCell(rContext, rowCount, object);
        if(selectCell != null)
        {
          row.insertBefore(selectCell,td);
        }
      }
      if(isHighlightAlternateRows())
      {
        //@todo - allow colours to be spec'd.
        //nb: for reasons beyond my understanding, using a css class to spec the cell bgcolor doesnt
        //work - its just ignored so set it in a style instead
        td.setAttribute("style", rowCount % 2 == 0 ? "background-color: #dae2ff;" : "background-color: #dfe2f0;" );
      }
    }
  }
  
  protected void decorateHeaderCell(RenderingContext rContext,Element td,int col)
    throws RenderingException
  {
    if(isSelectable())
    {
      if(col == 0)
      {
        Element row = (Element)td.getParentNode();
        Element selectHeaderCell = createSelectHeaderCell(rContext);
        if(selectHeaderCell != null)
        {
          row.insertBefore(selectHeaderCell,td);
        }
      }
    }
  }

  protected Element createSelectCell(RenderingContext rContext, int rowCount, Object object)
    throws RenderingException
  {
    try
    {
      if(object == null) throw new java.lang.NullPointerException("Null object for rowCount=" + rowCount);
      Element selectCell = null;
      Element input = null;
      if(_prototypeSelectCell == null)
      {
        selectCell = _target.createElement("td");
      }
      else
      {
        selectCell = (Element)_prototypeSelectCell.cloneNode(true);
      }
      if( isMandatory() )
      {
        selectCell.setAttribute("class","mandatory");
      }
      String selectionValue = getSelectionValue(rContext,rowCount,object); 
      input = getChildElement("input",selectCell);
      if(input == null)
      {
        input = _target.createElement("input");
        input.setAttribute("name",_tableName);
        input.setAttribute("type", (_multiple ? "checkbox" : "radio"));
        input.setAttribute("class","checkbox");
        selectCell.appendChild(input);
      }
      if(selectionValue != null)
      {
        input.setAttribute("value", selectionValue );
      }
      else
      {
        input.removeAttribute("value");
      }
      if(_selectedItems != null)
      { 
        if(StaticUtils.arrayContains(_selectedItems, selectionValue))
        {
          input.setAttribute("checked","checked");
        }
      }
      return selectCell;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating select cell",t);
    }
  }

  public IOptionValueRetriever getValueRetriever()
  {
    return _valueRetriever;
  }

  public void setValueRetriever(IOptionValueRetriever retriever)
  {
    _valueRetriever = retriever;
  }

  public boolean isHighlightAlternateRows()
  {
    return _highlightAlternateRows;
  }

  public void setHighlightAlternateRows(boolean b)
  {
    _highlightAlternateRows = b;
  }

}
