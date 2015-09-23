/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ElvLightRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 17, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

/**
 * This class is implemented with refer to the ElvRenderer.
 * 
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class ElvLightRenderer extends AbstractRenderer
{
	private Collection _entities;
	private IListViewOptions _listOptions;
	private String _embedNodeId;
	private EntityListRenderer _renderer;
	private String _tableName = "elv";
	private boolean _embeddedList;
	private boolean _indexModeDefault = false;
	private boolean _isRenderedHeader = true;
	
	public ElvLightRenderer (RenderingContext rContext,
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
	
	public void setTableName(String name)
  { _tableName = name; }

  public String getTableName()
  { return _tableName; }
	
  public void setEmbeddedList(boolean flag)
  { _embeddedList = flag; }

  public boolean isEmbeddedList()
  { return _embeddedList; }
  
  public void setIndexModeDefault(boolean indexModeDefault)
  { _indexModeDefault = indexModeDefault; }

  public boolean isIndexModeDefault()
  { return _indexModeDefault;  }
  
  public boolean isRenderHeader()
  {
  	return _isRenderedHeader;
  }
  
  public void setIsRenderHeader(boolean isRenderedHeader)
  {
  	_isRenderedHeader = isRenderedHeader;
  }
  
	@Override
	protected void render() throws RenderingException
	{
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
		
		String viewURL = _listOptions.getViewURL();
		String editURL = _listOptions.getUpdateURL();
		String createURL = _listOptions.getCreateURL();
		String deleteURL = _listOptions.getDeleteURL();
		
		if(_isRenderedHeader)
		{
			renderLabel("elv_header", _listOptions.getHeadingLabelKey());
		}
		else
		{
			removeNode("elv_header");
		}
		
		if(createURL != null)
		{
			renderLabelCarefully("elv_create", _listOptions.getCreateLabelKey(), false);
			String encodedCreateURL = "javascript:elvDivert('" + createURL + "','new');";
			renderElementAttribute("elv_create", "href", encodedCreateURL);
		}
		else
		{
			removeNode("elv_create_details", false);
		}
		
		if(deleteURL != null)
		{
			renderLabelCarefully("elv_delete", _listOptions.getDeleteLabelKey(), false);
			//String encodedDeleteURL =  "javascript:elvDivert('" + deleteURL + "','multiple');";
			String encodedDeleteURL =  "javascript:removeElvRow('" + _tableName + "_body','markAsDelete');";
			renderElementAttribute("elv_delete", "href", encodedDeleteURL);
		}
		else
		{
			removeNode("elv_delete_details", false);
		}
		
		
		//the table header & table content
		Element table = getElementById("elv_table", true);
		
		//add in a hidden field
		Element deleteField = _target.createElement("input");
		deleteField.setAttribute("name","markAsDelete");
		deleteField.setAttribute("type","hidden");
		deleteField.setAttribute("value", StaticUtils.implode(new String[0],","));
    table.getParentNode().insertBefore(deleteField,table);
		
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
    //Element defaultCell = getElementById("default_cell", true);
    Element selectionInput = getElementById("selection_input", true);
    Element editLink = getElementById("edit_link", true);
    Element viewLink = getElementById("view_link", true);
    
    //  Remove id attributes from the nodes. We don't need them any more as we now have references
    // and we do not want dupicate ids in our output.
    // We shall leave the table id in place as we only have one table. Those elements being used
    // as a 'stamp' will lose their ids.
    headerRow.removeAttribute("id"); //20030411AH
    detailRow.removeAttribute("id"); //20030411AH
    headerCell.removeAttribute("id"); //20030411AH
    selectionCell.removeAttribute("id"); //20030411AH
    editCell.removeAttribute("id"); //20030411AH
    viewCell.removeAttribute("id"); //20030411AH
    //defaultCell.removeAttribute("id"); //20030411AH
    selectionInput.removeAttribute("id"); //20030411AH
    editLink.removeAttribute("id"); //20030411AH
    viewLink.removeAttribute("id"); //20030411AH

    removeAllChildren(headerRow);
    removeAllChildren(detailRow);
    //removeAllChildren(defaultCell);
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
    //elr.setPrototypeValueCell(defaultCell);
    elr.setPrototypeHeaderCell(headerCell);
    elr.setPrototypeSelectHeaderCell(selectHeaderCell); //20030411AH
    elr.setPrototypeEditHeaderCell(editHeaderCell); //20030411AH
    elr.setPrototypeSelectCell(selectionCell);
    elr.setPrototypeEditCell(editCell);
    elr.setObjects(_entities);
    elr.setListViewOptions(_listOptions);
    elr.setInsertElement(table);
    
    if(_indexModeDefault)
    {
        elr.setReferenceMode(elr.REF_MODE_INDEX);
    }
    else
    {
        elr.setReferenceMode(elr.REF_MODE_FUID);
    }
    
    elr.render(_target);

    removeIdAttribute("elv_content",false);
    removeIdAttribute("elv_header",false);
    removeIdAttribute("elv_create",false);
    removeIdAttribute("elv_delete",false);
    removeIdAttribute("elv_up_details",false);
    removeIdAttribute("elv_down_details",false); 
	}
	
	private void addElvNode(RenderingContext rContext, String nodeId)
  	throws RenderingException
  {
		InsertionDef iDef = new InsertionDef("elv_content",nodeId,true,false,IDocumentKeys.EMBEDDED_LIST_VIEW_LIGHT,false);
		ArrayList insertions = new ArrayList();
		insertions.add(iDef);
		MultiNodeInsertionRenderer mnir = new MultiNodeInsertionRenderer(rContext,insertions);
		mnir.render(_target);
  }
}
