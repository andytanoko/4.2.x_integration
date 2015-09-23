/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-12-17     Ong Eu Soon         Created   
 * 2008-04-01     Tam Wei Xiang       #122 :Add field isArchiveFrequencyOnce, archiveOlderThan.
 *                                          Render field "fromStartDate/Time", "toStartDate/Time"
 *                                          if we indicate "isArchiveFrequencyOnce" is true.  
 * 2009-04-11     Tam Wei Xiang       #122 - Remove ARCHIVE_NAME                      
 */

package com.gridnode.gtas.client.web.archive;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.gridnode.gtas.client.ctrl.IGTArchiveEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class ArchiveRenderer extends AbstractRenderer
{
  private boolean _edit;

  private static final Number[] _fields = { 
                                        IGTArchiveEntity.ARCHIVE_ID,
                                        IGTArchiveEntity.ARCHIVE_DESCRIPTION,
                                        IGTArchiveEntity.FROM_START_DATE,
                                        IGTArchiveEntity.FROM_START_TIME,
                                        IGTArchiveEntity.TO_START_DATE,
                                        IGTArchiveEntity.TO_START_TIME,
                                        IGTArchiveEntity.ARCHIVE_TYPE,
                                        IGTArchiveEntity.ENABLE_RESTORE_ARCHIVED,
                                        IGTArchiveEntity.ENABLE_SEARCH_ARCHIVED,
                                        IGTArchiveEntity.PARTNER_ID_FOR_ARCHIVE,
                                        IGTArchiveEntity.IS_ARCHIVE_FREQUENCY_ONCE,
                                        IGTArchiveEntity.ARCHIVE_OLDER_THAN
                                      };

  private static final Number[] _processInstanceFields = { 
                                        IGTArchiveEntity.PROCESS_DEF_NAME_LIST,
                                        IGTArchiveEntity.INCLUDE_INCOMPLETE_PROCESSES,
                                      };

  private static final Number[] _documentFields = { 
                                        IGTArchiveEntity.FOLDER_LIST,
                                        IGTArchiveEntity.DOCUMENT_TYPE_LIST,
                                      };

  public ArchiveRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      if (_edit)
        includeJavaScript(IGlobals.JS_DATE_TIME_PICKER);

      RenderingContext rContext = getRenderingContext();
      renderCommonFormElements(IGTEntity.ENTITY_ARCHIVE,_edit);
      ArchiveAForm form = (ArchiveAForm)getActionForm();
      
      IGTArchiveEntity scheduledArchive = (IGTArchiveEntity)getEntity();
      
     
      if (_edit)
      { // Render diversion labels

        renderLabelCarefully("edit_message", "archive.edit.message.new", false);
        renderLabelCarefully("optionMsg", "archive.option.message", false);
        renderLabelCarefully("optionMsg2", "archiveMetaInfo.option2.message" ,false); //show the explanation for the archive older than field
      }
      else
      {
        //renderArchiveFile(rContext, form, archiveDocument);
        renderLabelCarefully("edit_message", "archive.edit.message.download", false);
        removeNode("ok_button",false);
      }
        
        BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
        renderFields(bfpr, scheduledArchive, _fields);

        if (isValidDateFormat(form.getFromStartDate())) 
        {
          renderElementValue("fromStartDate_value", form.getFromStartDate()); // 20040922 DDJ: Quick fix for Start Date not displaying due to BFPR
        }

        if (isValidDateFormat(form.getToStartDate()))
        {
          renderElementValue("toStartDate_value", form.getToStartDate()); // 20040922 DDJ: Quick fix for Start Date not displaying due to BFPR
        }

        String archiveType = form.getArchiveType();
        if(IGTArchiveEntity.ARCHIVE_TYPE_PROCESS_INSTANCE.equals(archiveType))
        {
          renderFields(bfpr, scheduledArchive, _processInstanceFields);

          removeNode("folderList_details",false);
          removeNode("documentTypeList_details",false);
        }
        else if(IGTArchiveEntity.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
        {
          renderFields(bfpr, scheduledArchive, _documentFields);

          removeNode("processDefNameList_details",false);
          removeNode("isIncludeIncomplete_details",false);
        }
        else
        {
          removeNode("folderList_details",false);
          removeNode("documentTypeList_details",false);

          removeNode("processDefNameList_details",false);
          removeNode("isIncludeIncomplete_details",false);
        }
      
        //TWX 30032009: Render the fromStart/toStart time if the archive frequency is 'Once'.
        if(! IGTArchiveEntity.ARCHIVE_FREQUENCE_ONCE.equals(form.getisArchiveFrequencyOnce()))
        {
          removeNode("fromStartDate_details", false);
          removeNode("fromStartTime_details", false);
          removeNode("toStartDate_details", false);
          removeNode("toStartTime_details", false);
          
        }
        else
        {
          removeNode("archiveRecordOlderThan_details", false);
          removeNode("optionMsg2", false);
        }
      
        System.out.println("Archive form render: "+form.getisArchiveFrequencyOnce());
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering scheduledArchive screen",t);
    }
  }

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