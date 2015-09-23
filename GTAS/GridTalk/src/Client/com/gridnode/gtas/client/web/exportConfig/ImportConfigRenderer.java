/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportConfigRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-06-02     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.exportConfig;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTImportConfig;
import com.gridnode.gtas.client.ctrl.IGTMheReference;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.MheReferenceRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class ImportConfigRenderer extends AbstractRenderer
{
  private boolean _edit;
  
  private static final Number[] _beforeImportFields = 
  {
    IGTImportConfig.IS_OVERWRITE,
    IGTImportConfig.IMPORT_FILE,
  };
  
  private static final Number[] _afterImportFields = 
  { // These are only displayed if there were conflicts
    IGTImportConfig.OVERWRITE_ENTITIES,
  };

  public ImportConfigRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
    if(!_edit)
    {
      throw new UnsupportedOperationException("View mode not supported");
    }
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      ImportConfigAForm form = (ImportConfigAForm)getActionForm();
      IGTImportConfig importConfig = (IGTImportConfig)getEntity();     
      renderCommonFormElements(IGTEntity.ENTITY_IMPORT_CONFIG,_edit);
      
      IGTMheReference conflictingEntities = (IGTMheReference)importConfig.getFieldValue(importConfig.CONFLICTING_ENTITIES);
      
      BindingFieldPropertyRenderer bfpr = null;
      
      if(conflictingEntities == null)
      {
        renderLabelCarefully("edit_message", "importConfig.edit.message.upload", false);     
        bfpr = renderFields(bfpr,
                            importConfig,
                            _beforeImportFields,
                            null,
                            form,
                            null);
        removeFields(importConfig, _afterImportFields, null);
      }
      else
      {
        Element selectedEntitiesNode = getElementById("overwriteEntities_value",false);
        if(selectedEntitiesNode != null)
        {
          MheReferenceRenderer mherr = new MheReferenceRenderer(rContext, _edit);
          mherr.setFieldName("overwriteEntities");
          mherr.setLabel( importConfig.getFieldMetaInfo(importConfig.CONFLICTING_ENTITIES).getLabel() );
          mherr.setInsertElement(selectedEntitiesNode);
          mherr.setSelectableEntities(form.getConflictingEntities());
          mherr.setSelection(form.getOverwriteEntities());
          mherr.render(_target);
        }                            
        renderLabelCarefully("edit_message", "importConfig.edit.message.resolve", false);
        removeFields(importConfig, _beforeImportFields, null);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering importConfig screen",t);
    }
  }
}