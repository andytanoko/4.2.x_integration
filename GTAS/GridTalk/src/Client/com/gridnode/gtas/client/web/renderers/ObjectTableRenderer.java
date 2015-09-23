/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ObjectTableRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-04     Andrew Hill         Created
 * 2003-04-08     Andrew Hill         Use script url in IGlobals
 * 2003-04-11     Andrew Hill         Replace header cell text carefully
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
 
public class ObjectTableRenderer extends AbstractRenderer implements IFilter
{
  protected String _tableName = null;

  protected IFilter _filter;
  protected Collection _objects;
  protected IColumnObjectAdapter _adapter;
  protected Element _table;

  protected Element _prototypeHeaderCell;
  protected Element _prototypeValueCell;

  protected Element _prototypeHeaderRow;
  protected Element _prototypeBodyRow;

  protected Element _prototypeThead;
  protected Element _prototypeTbody;

  public ObjectTableRenderer(RenderingContext rContext)
  {
    super(rContext);
    reset();
  }

  public void reset()
  {
    _filter = this;
    _objects = null;
    _table = null;
    _tableName = null;
  }

  public void setTableName(String name)
  {
    _tableName = name;
  }

  public String getTableName()
  {
    return _tableName;
  }

  public void setInsertElement(Element element)
  {
    if(element == null) throw new NullPointerException("element is null"); //20030416AH
    assertElementType(element,"table");
    _table = element;
  }

  public Element getInsertElement()
  { return _table; }

  public void setPrototypeThead(Element element)
  {
    assertElementType(element,"thead");
    _prototypeThead = element;
  }

  public Element getPrototypeThead()
  { return _prototypeThead; }

  public void setPrototypeTbody(Element element)
  {
    assertElementType(element,"tbody");
    _prototypeTbody = element;
  }

  public Element getPrototypeTbody()
  { return _prototypeTbody; }

  public void setPrototypeHeaderRow(Element element)
  {
    assertElementType(element,"tr");
    _prototypeHeaderRow = element;
  }

  public Element getPrototypeHeaderRow()
  { return _prototypeBodyRow; }

  public void setPrototypeBodyRow(Element element)
  {
    assertElementType(element,"tr");
    _prototypeBodyRow = element;
  }

  public Element getPrototypeBodyRow()
  { return _prototypeBodyRow; }

  public void setPrototypeValueCell(Element element)
  {
    assertElementType(element,"td");
    _prototypeValueCell = element;
  }

  public Element getPrototypeValueCell()
  { return _prototypeValueCell; }

  public void setPrototypeHeaderCell(Element element)
  {
    assertElementType(element,"th");
    _prototypeHeaderCell = element;
  }

  public Element getPrototypeHeaderCell()
  { return _prototypeHeaderCell; }

  public void setColumnObjectAdapter(IColumnObjectAdapter adapter)
  { _adapter = adapter; }

  public IColumnObjectAdapter getColumnObjectAdapter()
  { return _adapter; }

  public void setObjects(Collection objects)
  { _objects = objects; }

  public Collection getObjects()
  { return _objects; }

  public void setFilter(IFilter filter)
  {
    if(filter == null) filter = this;
    _filter = filter;
  }

  public IFilter getFilter()
  { return _filter; }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    return (object != null);
  }

  protected void render() throws RenderingException
  {
    try
    {
      Object objects = getObjects();
      if(_table == null) throw new NullPointerException("_table is null"); //20030416AH
      if(_objects == null) throw new NullPointerException("_objects is null"); //20030416AH
      if(_adapter == null) throw new NullPointerException("_adapter is null"); //20030416AH

      RenderingContext rContext = getRenderingContext();
      includeJavaScript(IGlobals.JS_TABLE_UTILS); //20030408AH
      if(_tableName != null)
      {
        _table.setAttribute("id",_tableName + "_table");
      }
      removeAllChildren(_table);
      renderHeader(rContext);
      renderBody(rContext);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering object table",t);
    }
  }

  protected Element createHeaderCell(RenderingContext rContext, int col)
    throws RenderingException
  {
    try
    {
      if(_prototypeHeaderCell == null)
      {
        return _target.createElement("th");
      }
      else
      {
        return (Element)_prototypeHeaderCell.cloneNode(true);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating header cell for column " + col,t);
    }
  }

  protected Element createBodyCell(RenderingContext rContext, int col, int rowCount, Object object)
    throws RenderingException
  {
    try
    {
      if(_prototypeValueCell == null)
      {
        return _target.createElement("td");
      }
      else
      {
        return (Element)_prototypeValueCell.cloneNode(true);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating body cell for column "
                                    + col + ", row " + rowCount,t);
    }
  }

  protected void appendHeaderCell(RenderingContext rContext,
                                  Element row,
                                  int col,
                                  String label)
    throws RenderingException
  {
    Element th = createHeaderCell(rContext,col);
    replaceTextCarefully(th,label); //20030411AH
    row.appendChild(th);
    decorateHeaderCell(rContext,th,col);
  }

  protected void appendBodyCell(RenderingContext rContext,
                                  Element row,
                                  int col,
                                  int rowCount,
                                  Object value,
                                  Object object)
    throws RenderingException
  {
    String strValue = StaticUtils.stringValue(value);
    Element td = createBodyCell(rContext,col,rowCount,object);
    if(StaticUtils.stringEmpty(strValue)) //20030325AH
    {
      /*20030325AH - nbsp dont work anymore! - Node nbsp = _target.createEntityReference("nbsp");
      td.appendChild(nbsp);*/
      Node br = _target.createElement("br"); //20030225AH
      td.appendChild(br); //20030325AH
    }
    else
    {
      Node text = _target.createTextNode( strValue );
      td.appendChild(text);
    }
    row.appendChild(td);
    decorateBodyCell(rContext,td,col,rowCount,object);
  }

  protected Element createHeaderRow(RenderingContext rContext) throws RenderingException
  {
    try
    {
      if(_prototypeHeaderRow == null)
      {
        return _target.createElement("tr");
      }
      else
      {
        return (Element)_prototypeHeaderRow.cloneNode(true);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating header row",t);
    }
  }

  protected Element createBodyRow(RenderingContext rContext, int row, Object object)
    throws RenderingException
  {
    try
    {
      if(_prototypeBodyRow == null)
      {
        return _target.createElement("tr");
      }
      else
      {
        return (Element)_prototypeBodyRow.cloneNode(true);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating body row",t);
    }
  }

  protected void renderHeader(RenderingContext rContext) throws RenderingException
  {
    try
    {
      Element thead = createThead(rContext);
      Element row = createHeaderRow(rContext);
      int colCount = _adapter.getSize();
      for(int i=0; i < colCount; i++)
      {
        String labelKey = _adapter.getColumnLabel(i);
        String label = rContext.getResourceLookup().getMessage(labelKey);
        appendHeaderCell(rContext,row,i,label);
      }
      appendHeaderRow(rContext,thead,row);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error preparing header row",t);
    }
  }

  protected void appendBodyRow( RenderingContext rContext,
                                Element tbody,
                                Element row,
                                int rowCount,
                                Object object)
    throws RenderingException
  {
    
    tbody.appendChild(row);
    decorateBodyRow(rContext, row, rowCount, object); //20030722AH
  }
  
  protected void decorateBodyRow(RenderingContext rContext,Element row,int rowCount, Object object)
    throws RenderingException
  { //20030722AH
    
  }
  
  protected void decorateHeaderRow(RenderingContext rContext,Element row)
    throws RenderingException
  { //20030722AH
    
  }
  
  protected void decorateThead(RenderingContext rContext,Element row)
    throws RenderingException
  { //20030722AH
    
  }

  protected void appendHeaderRow( RenderingContext rContext,
                                  Element thead,
                                  Element row)
    throws RenderingException
  {
    thead.appendChild(row);
    decorateHeaderRow(rContext, row); //20030722AH
    _table.appendChild(thead);
    decorateThead(rContext, thead); //20030722AH
  }

  protected void renderBody(RenderingContext rContext) throws RenderingException
  {
    try
    {
      int rowCount = 0;
      int colCount = _adapter.getSize();
      Iterator i = _objects.iterator();
      Element tbody = createTbody(rContext);
      while(i.hasNext())
      {
        Object object = i.next();
        if(_filter.allows(object,this))
        {
          Element row = createBodyRow(rContext, rowCount, object);
          for(int col = 0; col < colCount; col++)
          {
            Object value = _adapter.getColumnValue(object, col);
            try
            {
              appendBodyCell(rContext,row,col,rowCount,value, object);
            }
            catch(Throwable t)
            {
              throw new RenderingException("Error rendering column " + col
                                            + " of row " + rowCount
                                            +" with value='" + value + "'",t);
            }
          }
          appendBodyRow(rContext,tbody,row,rowCount,object);
          rowCount++;
        }
      }
      appendBody(rContext,tbody);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering object table body",t);
    }
  }

  protected void appendBody(RenderingContext rContext,Element tbody)
    throws RenderingException
  {
    _table.appendChild(tbody);
  }

  protected void decorateBodyCell(RenderingContext rContext,Element td,int col,int rowCount,Object object)
    throws RenderingException
  {
    
  }

  protected void decorateHeaderCell(RenderingContext rContext,Element td,int col)
    throws RenderingException
  {
    
  }

  protected Element createThead(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      Element thead = null;
      if(_prototypeThead == null)
      {
        thead = _target.createElement("thead");
      }
      else
      {
        thead = (Element)_prototypeThead.cloneNode(true);
      }
      if(_tableName != null)
      {
        thead.setAttribute("id",_tableName + "_head");
      }
      return thead;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating thead",t);
    }
  }

  protected Element createTbody(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      Element tbody = null;
      if(_prototypeTbody == null)
      {
        tbody = _target.createElement("tbody");
      }
      else
      {
        tbody = (Element)_prototypeTbody.cloneNode(true);
      }
       if(_tableName != null)
      {
        tbody.setAttribute("id",_tableName + "_body");
      }
      return tbody;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating tbody",t);
    }
  }
}