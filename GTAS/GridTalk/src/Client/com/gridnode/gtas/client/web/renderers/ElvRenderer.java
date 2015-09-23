/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ElvRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-05     Andrew Hill         Created (based on ListViewRenderer)
 * 2002-09-10     Andrew Hill         Modify to accomodate changes to IColumnEntityAdapter
 * 2002-09-23     Andrew Hill         Refactor to use EntityListRenderer (& EmbeddedEntityListRenderer)
 * 2002-11-11     Andrew Hill         Allow EntityListRenderer used to be overridden
 * 2002-11-11     Daniel D'Cotta      Modify to handle multiple Elves in one document
 * 2003-03-19     Andrew Hill         Call ieTableHeaderStyleHack('form') in onload
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
 
public class ElvRenderer extends AbstractRenderer
{
  private IListViewOptions _listOptions;
  private Collection _entities;
  private String _embedNodeId;
  private boolean _allowsOrdering;
  private boolean _embeddedList;
  private String _tableName = "elv";
  private boolean _indexModeDefault = false;
  /*private boolean _convertDivert = true; // treat as divert mapping names automatically*/
  private String[] _order = new String[0];
  private EntityListRenderer _renderer = null;

  public ElvRenderer( RenderingContext rContext,
                      String embedNodeId,
                      IListViewOptions listOptions,
                      Collection entities)
  {
    super(rContext);
    if(listOptions == null)
    {
      throw new java.lang.NullPointerException("No listview options specified");
    }
    if(entities == null)
    {
      throw new java.lang.NullPointerException("No entity collection specified");
    }
    if(embedNodeId == null)
    {
      throw new java.lang.NullPointerException("No nodeId to insert elv specified");
    }
    _entities = entities;
    _listOptions = listOptions;
    _embedNodeId = embedNodeId;
  }

  public void setRenderer(EntityListRenderer renderer)
  {
    _renderer = renderer;
  }

  public EntityListRenderer getRenderer()
  {
    return _renderer;
  }

  /*public void setConvertDivert(boolean flag)
  { _convertDivert = flag; }*/

  /*public boolean isConvertDivert()
  { return _convertDivert; }*/

  public void setTableName(String name)
  { _tableName = name; }

  public String getTableName()
  { return _tableName; }

  public void setAllowsOrdering(boolean flag)
  { _allowsOrdering = flag; }

  public boolean isAllowsOrdering()
  { return _allowsOrdering; }

  public void setEmbeddedList(boolean flag)
  { _embeddedList = flag; }

  public boolean isEmbeddedList()
  { return _embeddedList; }

  public void setOrder(String[] order)
  {
    //@todo: change to take an imploded order?
    if(order == null)
    {
      _order = new String[0];
    }
    else
    {
      _order = order;
    }
  }

  public String[] getOrder()
  { return _order; }

  public void setIndexModeDefault(boolean indexModeDefault)
  { _indexModeDefault = indexModeDefault; }

  public boolean isIndexModeDefault()
  { return _indexModeDefault;  }


  protected void render() throws RenderingException
  {
    try
    {
      // remove elv index div if already added
      removeNode("elv_index_details");

      RenderingContext rContext = getRenderingContext();
      ISimpleResourceLookup rLookup = getRenderingContext().getResourceLookup();
      IURLRewriter urlRewriter = getRenderingContext().getUrlRewriter();
      IColumnEntityAdapter columnAdapter = _listOptions.getColumnAdapter();

      addElvNode(rContext, _embedNodeId);

      if(rContext.isExplorer())
      {
        // 20030319AH
        // IE has a problem where it is not applying the css style to all cells in the header
        // row when the page is first shown. The method called here will force ie to redraw the
        // form, and the second time it will get it right.
        appendOnloadEventMethod("ieTableHeaderStyleHack('form');"); //20030319AH
      }

      String viewURL    = _listOptions.getViewURL();
      String editURL    = _listOptions.getUpdateURL();
      String createURL  = _listOptions.getCreateURL();
      String deleteURL  = _listOptions.getDeleteURL();

      renderLabel("elv_header", _listOptions.getHeadingLabelKey());
      renderLabel("elv_message",_listOptions.getMessageLabelKey());

      if(_listOptions.getCreateURL() != null)
      {
        renderLabelCarefully("elv_create",_listOptions.getCreateLabelKey(), false); //20030319AH
        String encodedCreateURL = "javascript:elvDivert('" + createURL + "','new');";
        renderElementAttribute("elv_create","href",encodedCreateURL);
      }
      else
      {
        removeNode("elv_create_details",false);
      }

      if(_listOptions.getDeleteURL() != null)
      {
        renderLabelCarefully("elv_delete",_listOptions.getDeleteLabelKey(), false); //20030319AH
        String encodedDeleteURL = null;
        if(_allowsOrdering)
        {
          encodedDeleteURL = "javascript:removeRows('" + _tableName + "_body','" + _tableName + "Order');";
        }
        else
        {
          encodedDeleteURL = "javascript:elvDivert('" + deleteURL + "','multiple');";
        }
        renderElementAttribute("elv_delete","href",encodedDeleteURL);
      }
      else
      {
        removeNode("elv_delete_details",false);
      }

      Element table = getElementById("elv_table", true);
      if(_allowsOrdering)
      {
        Element orderField = _target.createElement("input");
        orderField.setAttribute("name",_tableName + "Order");
        orderField.setAttribute("type","hidden");
        //orderField.setAttribute("type","hidden");
        orderField.setAttribute("value", StaticUtils.implode(_order,","));
        table.getParentNode().insertBefore(orderField,table);

        if(_listOptions.isAllowsEdit())
        {
          Element up = getElementById("elv_up",true);
          //replaceText(up,rLookup.getMessage("generic.up"));
          renderLabelCarefully("elv_up", "generic.up", false ); //20030319AH
          up.removeAttribute("id");
          String upUrl = "javascript: void move('" + _tableName + "_body'"
                          + ",true,'" + _tableName + "Order" + "');";
          up.setAttribute("href",upUrl);

          Element down = getElementById("elv_down",true);
          //replaceText(down,rLookup.getMessage("generic.down"));
          renderLabelCarefully("elv_down", "generic.down", false ); //20030319AH
          down.removeAttribute("id");
          String downUrl = "javascript: void move('" + _tableName + "_body'"
                          + ",false,'" + _tableName + "Order" + "');";
          down.setAttribute("href",downUrl);
        }
        else
        {
          removeNode("elv_up_details",false);
          removeNode("elv_down_details",false);
        }
      }
      else
      {
        removeNode("elv_up_details",false);
        removeNode("elv_down_details",false);
      }

      Element headerRow = getElementById("column_header_row", true);
      Element detailRow = getElementById("detail_row", true);
      Element headerCell = getElementById("column_header_cell", true);
      Element selectHeaderCell = getElementById("select_header_cell",true);
      Element editHeaderCell = getElementById("edit_header_cell",true);

      Element selectionCell = getElementById("selection_cell", true);

      Element checkbox = findElement(selectionCell,"input","type","checkbox");
      if(checkbox == null) throw new RenderingException("Couldnt find prototype selection checkbox");
      checkbox.setAttribute("onclick","highlightRows('" + _tableName + "_body');");

      Element editCell = getElementById("edit_cell", true);
      Element viewCell = getElementById("viewlink_cell", true);
      Element defaultCell = getElementById("default_cell", true);
      Element selectionInput = getElementById("selection_input", true);
      Element editLink = getElementById("edit_link", true);
      Element viewLink = getElementById("view_link", true);

      // Remove id attributes from the nodes. We don't need them any more as we now have references
      // and we do not want dupicate ids in our output.
      // We shall leave the table id in place as we only have one table. Those elements being used
      // as a 'stamp' will lose their ids.
      headerRow.removeAttribute("id"); //20030411AH
      detailRow.removeAttribute("id"); //20030411AH
      headerCell.removeAttribute("id"); //20030411AH
      selectionCell.removeAttribute("id"); //20030411AH
      editCell.removeAttribute("id"); //20030411AH
      viewCell.removeAttribute("id"); //20030411AH
      defaultCell.removeAttribute("id"); //20030411AH
      selectionInput.removeAttribute("id"); //20030411AH
      editLink.removeAttribute("id"); //20030411AH
      viewLink.removeAttribute("id"); //20030411AH

      removeAllChildren(headerRow);
      removeAllChildren(detailRow);
      removeAllChildren(defaultCell);
      removeAllChildren(headerCell);
      EntityListRenderer elr = getRenderer();
      if(elr == null)
      { //20021111AH = Allow full overriding of renderer used
        elr = _embeddedList ? new EmbeddedEntityListRenderer(rContext) : new EntityListRenderer(rContext, null); //20030325AH
      }
      elr.setMagicLinksEnabled(false); //20030325AH - ELVs dont do magic links (yet)
      elr.setTableName(_tableName);
      elr.setPrototypeHeaderRow(headerRow);
      elr.setPrototypeBodyRow(detailRow);
      elr.setPrototypeValueCell(defaultCell);
      elr.setPrototypeHeaderCell(headerCell);
      elr.setPrototypeSelectHeaderCell(selectHeaderCell); //20030411AH
      elr.setPrototypeEditHeaderCell(editHeaderCell); //20030411AH
      elr.setPrototypeSelectCell(selectionCell);
      elr.setPrototypeEditCell(editCell);
      elr.setObjects(_entities);
      elr.setListViewOptions(_listOptions);
      elr.setInsertElement(table);
      if(isAllowsOrdering())
      {
        elr.setReferenceMode(elr.REF_MODE_INDEX);
      }
      else
      {
        if(_indexModeDefault)
        {
          elr.setReferenceMode(elr.REF_MODE_INDEX);
        }
        else
        {
          elr.setReferenceMode(elr.REF_MODE_FUID);
        }
      }
      elr.render(_target);

      removeIdAttribute("elv_content",false);
      removeIdAttribute("elv_header",false);
      removeIdAttribute("elv_create",false);
      removeIdAttribute("elv_delete",false);
      removeIdAttribute("elv_up_details",false);
      removeIdAttribute("elv_down_details",false);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering embedded list view",t);
    }

  }

  private void addElvNode(RenderingContext rContext, String nodeId)
    throws RenderingException
  {
    InsertionDef iDef = new InsertionDef("elv_content",nodeId,true,false,IDocumentKeys.EMBED_LISTVIEW,false);
    ArrayList insertions = new ArrayList();
    insertions.add(iDef);
    MultiNodeInsertionRenderer mnir = new MultiNodeInsertionRenderer(rContext,insertions);
    mnir.render(_target);
  }
}