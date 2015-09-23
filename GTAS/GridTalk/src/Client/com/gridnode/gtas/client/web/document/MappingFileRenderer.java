/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFileRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-15     Andrew Hill         Created
 * 2002-02-26     Daniel D'Cotta      Added SubPath
 */
package com.gridnode.gtas.client.web.document;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTMappingFileEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class MappingFileRenderer extends AbstractRenderer
{
  protected boolean _edit;
  protected static final Number[] _fields = new Number[]{IGTMappingFileEntity.DESCRIPTION,
                                                        IGTMappingFileEntity.NAME,
                                                        IGTMappingFileEntity.FILENAME,
                                                        IGTMappingFileEntity.TYPE};

  protected static final Number[] _schemaFields = new Number[]{IGTMappingFileEntity.SUB_PATH};

  public MappingFileRenderer( RenderingContext rContext, boolean edit )
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      renderCommonFormElements(IGTEntity.ENTITY_MAPPING_FILE,_edit);
      //renderFields(_fields);
      BindingFieldPropertyRenderer bfpr = renderFields(null, getEntity(), _fields);
  
      MappingFileAForm mappingFileForm = (MappingFileAForm)getActionForm();
      if(IGTMappingFileEntity.TYPE_SCHEMA.toString().equals(mappingFileForm.getType()))
      {
        //renderFields(_schemaFields);
        renderFields(bfpr, getEntity(), _schemaFields);
      }
      else
      {
        removeNode("subPath_details");
      }
    }
    catch(Exception ex)
    {
      throw new RenderingException("Error rendering mapping file", ex);
    }
  }

  /*protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      final ISimpleResourceLookup rLookup = rContext.getResourceLookup();
      IGTMappingFileEntity mappingFile = (IGTMappingFileEntity)getEntity();

      renderCommonFormElements("mappingFile",_edit);

      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);

      //Code to render the type field:
      if(_edit)
      {
        final IGTMappingFileEntity fMappingFile = mappingFile;
        IOptionValueRetriever typeRetriever = new IOptionValueRetriever()
        {
          public String getOptionText(Object choice) throws GTClientException
          {
            try
            {
              String key = fMappingFile.getTypeLabelKey((Short)choice);
              return rLookup.getMessage(key);
            }
            catch(Exception e)
            {
              throw new GTClientException("Bad type exception",e);
            }
          }
          public String getOptionValue(Object choice) throws GTClientException
          {
            try
            {
              return choice.toString();
            }
            catch(Exception e)
            {
              throw new GTClientException("Bad type exception",e);
            }
          }
        };
        renderSelectOptions("type_value",mappingFile.getAllowedTypes(),typeRetriever);
        bfpr.setBindings(mappingFile.TYPE);
        bfpr.render(_target);
      }
      else
      {
        String typeString = mappingFile.getTypeLabelKey();
        typeString = rContext.getResourceLookup().getMessage(typeString);
        bfpr.setBindings(mappingFile,mappingFile.TYPE,typeString,rContext.getActionErrors());
        bfpr.render(_target);
      }
      //End of code to render the type field

      renderFields( bfpr, mappingFile, new Number[] { mappingFile.NAME,
                                                      mappingFile.DESCRIPTION,
                                                      mappingFile.PATH, } );
    }
    catch(Exception e)
    {
      if(e instanceof RenderingException)
        throw (RenderingException)e;
      else
        throw new RenderingException("Error rendering mappingFile screen",e);
    }
  }*/
}