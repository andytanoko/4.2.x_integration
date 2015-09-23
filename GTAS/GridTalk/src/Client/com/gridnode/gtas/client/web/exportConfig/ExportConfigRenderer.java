/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportConfigRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-29     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.exportConfig;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTExportConfig;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.MheReferenceRenderer;
import com.gridnode.gtas.client.web.renderers.MultifilesRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;

public class ExportConfigRenderer extends AbstractRenderer
{
  private boolean _edit;
  
  private static final Number[] _beforeExportFields = 
  {
    IGTExportConfig.SELECTED_ENTITIES,
  };
  
  private static final Number[] _afterExportFields = 
  {
    IGTExportConfig.EXPORT_FILE,
  };

  public ExportConfigRenderer(RenderingContext rContext, boolean edit)
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
      ExportConfigAForm form = (ExportConfigAForm)getActionForm();
      IGTExportConfig exportConfig = (IGTExportConfig)getEntity();
      
      renderCommonFormElements(IGTEntity.ENTITY_EXPORT_CONFIG,_edit);
      
      if(exportConfig.isNewEntity())
      {
        Element selectedEntitiesNode = getElementById("selectedEntities_value",false);
        if(selectedEntitiesNode != null)
        {
          MheReferenceRenderer mherr = new MheReferenceRenderer(rContext, _edit);
          mherr.setFieldName("selectedEntities");
          mherr.setInsertElement(selectedEntitiesNode);
          mherr.setSelectableEntities(form.getExportableEntities());
          mherr.setSelection(form.getSelectedEntities());
          mherr.setLabel( exportConfig.getFieldMetaInfo(exportConfig.EXPORTABLE_ENTITIES).getLabel() );
          mherr.render(_target);
        }
        removeFields(exportConfig, _afterExportFields, null);
        renderLabelCarefully("edit_message", "exportConfig.edit.message.new", false);      
      }
      else
      {
        renderExportFile(rContext, form, exportConfig);
        removeFields(exportConfig, _beforeExportFields, null);
        renderLabelCarefully("edit_message", "exportConfig.edit.message.download", false);
        removeNode("ok_button",false); 
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering exportConfig screen",t);
    }
  }
  
  
  private void renderExportFile(RenderingContext rContext,
                                ExportConfigAForm form,
                                IGTExportConfig exportConfig)
    throws RenderingException
  { 
    try
    {
      String fileName = form.getExportFile();
      FormFileElement ffe = new FormFileElement("0", fileName );
      FormFileElement[] formFileElements = { ffe };

      //String dlhKey = StaticWebUtils.getDlhKey(archiveDocument, rContext.getOperationContext(), archiveDocument.ARCHIVE_FILE);
      MultifilesRenderer mfr = new MultifilesRenderer(rContext);
      //mfr.setDlhKey(dlhKey);
      mfr.setDownloadable(true);
      mfr.setEntity(exportConfig);
      mfr.setFieldId(exportConfig.EXPORT_FILE);
      mfr.setFormFileElements(formFileElements);
      mfr.setViewOnly(true);
      mfr.setInsertId("exportFile_value");
      mfr.setCollection(false);

      mfr.render(_target);

      renderLabel("exportFile_label","exportConfig.exportFile",false);

    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering field for configuration export file",t);
    }
  }
}