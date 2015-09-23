/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFileListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-20     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.document;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Element;

import com.gridnode.gtas.client.ctrl.IGTMappingFileEntity;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.AnchorConversionRenderer;
import com.gridnode.gtas.client.web.renderers.IURLRewriter;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

/**
 * Will 'decorate' the listview dom by adding the type parameter to the end of the
 * url when required to preset the type field when creating new mapping file.
 */
public class MappingFileListDecoratorRenderer  extends AbstractRenderer
{
  private Short _mappingFileType;
  private ActionMapping _mapping;
  
  private final String IMPORT_SCHEMA_ICON_SRC = "images/actions/import.gif";
  private final String IMPORT_SCHEMA_FWD_NAME = "importSchema";
  /**
   * Constructor
   * @param rContext
   * @param mappingFileType a short with type being shown in listview, null for all
   */
  public MappingFileListDecoratorRenderer(RenderingContext rContext,
                                          Short mappingFileType, ActionMapping mapping)
  {
    super(rContext);
    _mappingFileType = mappingFileType;
    
    //TWX 07032006
    if(mapping == null)
    {
    	throw new NullPointerException("[MappingFileListDecoratorRenderer]mapping is null.");
    }
    _mapping = mapping;
  }

  protected void render() throws RenderingException
  {
  	
    if(_mappingFileType != null)
    {
      // Modify heading to display which type of mapping rules we are showing.
      if(IGTMappingFileEntity.TYPE_CONVERSION_RULE.equals(_mappingFileType))
      {
        renderLabel("listview_heading","mappingFile.listview.heading.cr",false);
      }
      else if(IGTMappingFileEntity.TYPE_REFERENCE_DOC.equals(_mappingFileType))
      {
        renderLabel("listview_heading","mappingFile.listview.heading.rd",false);
      }
      else if(IGTMappingFileEntity.TYPE_XSL.equals(_mappingFileType))
      {
        renderLabel("listview_heading","mappingFile.listview.heading.xsl",false);
      }
      else if(IGTMappingFileEntity.TYPE_JAVA_BINARY.equals(_mappingFileType))
      {
        renderLabel("listview_heading","mappingFile.listview.heading.javabinary",false);
      }
      else if(IGTMappingFileEntity.TYPE_DTD.equals(_mappingFileType))
      { //20021214AH
        renderLabel("listview_heading","mappingFile.listview.heading.dtd",false);
      }
      else if(IGTMappingFileEntity.TYPE_SCHEMA.equals(_mappingFileType))
      { //20021214AH
      	renderLabel("listview_heading","mappingFile.listview.heading.schema",false);
      	
      	//TWX 07032006
        Element buttonCell = getElementById("button_cell",false);
        if(buttonCell != null)
        {
        	ActionForward importSchemaFwd = _mapping.findForward(IMPORT_SCHEMA_FWD_NAME);
        	
        	if(importSchemaFwd != null)
        	{
        		Element importSchemaLink = createButtonLink("importBatchSchema","mappingFile.listview.import.schema", IMPORT_SCHEMA_ICON_SRC,
        		                                            importSchemaFwd.getPath(), false);
        		buttonCell.appendChild(importSchemaLink);
        		//to make the window pop-up
        		renderElementAttribute("importBatchSchema","target",AnchorConversionRenderer.TARGET_DETAIL_VIEW);
        	}
        }
      }
      else if(IGTMappingFileEntity.TYPE_DICT.equals(_mappingFileType))
      { //20021214AH
        renderLabel("listview_heading","mappingFile.listview.heading.dict",false);
      }
      else if(IGTMappingFileEntity.TYPE_XPATH.equals(_mappingFileType))
      { //20030218AH
        renderLabel("listview_heading","mappingFile.listview.heading.xpath",false);
      }

      // Modify the create link href to append the type so it is preset for us when we open
      // the create screen.
      Element createLink = getElementById("create",false);
      if(createLink != null)
      {
        String href = createLink.getAttribute("href");
        if(href != null)
        {
          href = StaticWebUtils.addParameterToURL(href, "type","" + _mappingFileType);
          createLink.setAttribute("href",href);
        }
      }
    }
  }
}