/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveDocumentRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-17     Andrew Hill         Created
 * 2002-09-22     Daniel D'Cotta      Commented out GDOC_FILTER, added re-designed 
 *                                    ProcessInstance and GridDocument options
 * 2006-08-31     Tam Wei Xiang       Merge from ESTORE stream. 
 * 2006-09-14     Tam Wei Xiang       Render field ENABLE_RESTORE_ARCHIVED,
 *                                    ENABLE_SEARCH_ARCHIVED, PARTNER                                   
 */

package com.gridnode.gtas.client.web.archive;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.ctrl.IGTArchiveDocumentEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.archive.helpers.Logger;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class ArchiveDocumentRenderer extends AbstractRenderer
{
  private boolean _edit;

  private static final Number[] _fields = { 
                                        IGTArchiveDocumentEntity.NAME,
                                        IGTArchiveDocumentEntity.DESCRIPTION,
                                        IGTArchiveDocumentEntity.FROM_DATE,
                                        IGTArchiveDocumentEntity.FROM_TIME,
                                        IGTArchiveDocumentEntity.TO_DATE,
                                        IGTArchiveDocumentEntity.TO_TIME,
                                        IGTArchiveDocumentEntity.ARCHIVE_TYPE,
                                        IGTArchiveDocumentEntity.ENABLE_RESTORE_ARCHIVED,
                                        IGTArchiveDocumentEntity.ENABLE_SEARCH_ARCHIVED,
                                        IGTArchiveDocumentEntity.PARTNER
                                      };

  private static final Number[] _processInstanceFields = { 
                                        IGTArchiveDocumentEntity.PROCESS_DEF,
                                        IGTArchiveDocumentEntity.INCLUDE_INCOMPLETE,
                                      };

  private static final Number[] _documentFields = { 
                                        IGTArchiveDocumentEntity.FOLDER,
                                        IGTArchiveDocumentEntity.DOC_TYPE,
                                      };

  public ArchiveDocumentRenderer(RenderingContext rContext, boolean edit)
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
      if (_edit)
        includeJavaScript(IGlobals.JS_DATE_TIME_PICKER);

      RenderingContext rContext = getRenderingContext();
      renderCommonFormElements(IGTEntity.ENTITY_ARCHIVE_DOCUMENT,_edit);
      ArchiveDocumentAForm form = (ArchiveDocumentAForm)getActionForm();
      
      IGTArchiveDocumentEntity archiveDocument = (IGTArchiveDocumentEntity)getEntity();
      
      if(archiveDocument.isNewEntity())
      { 
        /*        
        DataFilterRenderer dfr = new DataFilterRenderer(rContext, _edit, "gdocFilter", IGTEntity.ENTITY_GRID_DOCUMENT);
        dfr.render(_target);
        */
        renderLabelCarefully("edit_message", "archiveDocument.edit.message.new", false);
        renderLabelCarefully("optionMsg", "archiveDocument.option.message", false);
        //removeNode("archiveFile_details",false);
        
        BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
        renderFields(bfpr, archiveDocument, _fields);

        if (isValidDateFormat(form.getFromDate()))
        {
          renderElementValue("fromDate_value", form.getFromDate()); // 20040922 DDJ: Quick fix for Start Date not displaying due to BFPR
        }

        if (isValidDateFormat(form.getToDate()))
        {
          renderElementValue("toDate_value", form.getToDate()); // 20040922 DDJ: Quick fix for Start Date not displaying due to BFPR
        }

        String archiveType = form.getArchiveType();
        if(IGTArchiveDocumentEntity.ARCHIVE_TYPE_PROCESS_INSTANCE.equals(archiveType))
        {
          renderFields(bfpr, archiveDocument, _processInstanceFields);

          removeNode("folder_details",false);
          removeNode("docType_details",false);
        }
        else if(IGTArchiveDocumentEntity.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
        {
          renderFields(bfpr, archiveDocument, _documentFields);

          removeNode("processDef_details",false);
          removeNode("includeIncomplete_details",false);
        }
        else
        {
          removeNode("folder_details",false);
          removeNode("docType_details",false);

          removeNode("processDef_details",false);
          removeNode("includeIncomplete_details",false);
        }
      }
      else
      {
        //renderArchiveFile(rContext, form, archiveDocument);
        renderLabelCarefully("edit_message", "archiveDocument.edit.message.download", false);
        removeNode("archive_details",false);
        removeNode("ok_button",false);
      }
      
      
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering archiveDocument screen",t);
    }
  }
  
  /*
  private void fixCheckBox()
  {
  	RenderingContext rContext = getRenderingContext();
	  /* SC: When user selects archive type drop down list, value of isEstore checkbox is lost.  The following code fix this. */
  	/*
	  Element checkBox = getElementById("isEstore_value");
	  String isEstoreStr = rContext.getRequest().getParameter("isEstore");
	  debug("isEstoreStr = " + isEstoreStr);
	  if ("true".equals(isEstoreStr))
	  {
	  	checkBox.setAttribute("checked", "checked");
	  }
  }*/
  
  /*
  private void renderArchiveFile(RenderingContext rContext,
                                ArchiveDocumentAForm form,
                                IGTArchiveDocumentEntity archiveDocument)
    throws RenderingException
  { //20030127AH
    try
    {
      String fileName = form.getArchiveFile();
      FormFileElement ffe = new FormFileElement("0", fileName );
      FormFileElement[] formFileElements = { ffe };

      //String dlhKey = StaticWebUtils.getDlhKey(archiveDocument, rContext.getOperationContext(), archiveDocument.ARCHIVE_FILE);
      MultifilesRenderer mfr = new MultifilesRenderer(rContext);
      //mfr.setDlhKey(dlhKey);
      mfr.setDownloadable(true);
      mfr.setEntity(archiveDocument);
      mfr.setFieldId(archiveDocument.ARCHIVE_FILE);
      mfr.setFormFileElements(formFileElements);
      mfr.setViewOnly(true);
      mfr.setInsertId("archiveFile_value");
      mfr.setCollection(false);

      mfr.render(_target);

      renderLabel("archiveFile_label","archiveDocument.archiveFile",false);

    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering field for certificate export",t);
    }
  }
  */

  private boolean isValidDateFormat(String dateStr)
  {
    boolean valid = false;
    try
    {
      Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
      valid = date != null;
    }
    catch (Exception e)
    {
    }
    
    return valid;
  }
}