/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PortRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-19     Andrew Hill         Created
 * 2002-05-22     Daniel D'Cotta      Added running sequence number support
 * 2005-08-22     Tam Wei Xiang       Remove field IGTPortEntity.ATTACHMENT_DIR from the Number array _mainFields (To improve the attachment feature of GTAS)
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Collection;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPortEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.*;

public class PortRenderer extends AbstractRenderer
{ 
  private boolean _edit;
  private static Number[] _mainFields =
  {
    IGTPortEntity.NAME,
    IGTPortEntity.DESCRIPTION,
    IGTPortEntity.IS_RFC,
    IGTPortEntity.HOST_DIR,
    IGTPortEntity.IS_OVERWRITE,
    IGTPortEntity.IS_DIFF_FILE_NAME,
    IGTPortEntity.IS_EXPORT_GDOC,
    IGTPortEntity.FILE_GROUPING,
  };
  private static Number[] _rfcFields =
  {
    IGTPortEntity.RFC,
  };
  private static Number[] _fileFields =
  {
    IGTPortEntity.FILE_NAME,
    IGTPortEntity.IS_ADD_FILE_EXT,
  };
  private static Number[] _fileExtFields =
  {
    IGTPortEntity.FILE_EXT_TYPE,
    IGTPortEntity.FILE_EXT_VALUE, //todo - special handling
  };
  private static Number[] _sequenceFields =
  {
    IGTPortEntity.START_NUM,
    IGTPortEntity.ROLLOVER_NUM,
    IGTPortEntity.NEXT_NUM,
    IGTPortEntity.IS_PADDED,
  };
  private static Number[] _sequencePadFields =
  {
    IGTPortEntity.FIX_NUM_LENGTH,
  };

  public PortRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();  
      IGTPortEntity port = (IGTPortEntity)getEntity();    
      PortAForm form = (PortAForm)getActionForm();        
      renderCommonFormElements(port.getType(),_edit);

      BindingFieldPropertyRenderer bfpr = renderFields(null,port,_mainFields);
      boolean isExportGdoc = StaticUtils.primitiveBooleanValue( form.getIsExportGdoc() );
      if(isExportGdoc)
      {
        Element attachmentDirValueNode = getElementById("attachmentDir_value",false);
        if(attachmentDirValueNode != null)
        {
          attachmentDirValueNode.setAttribute("class","mandatory");
        }
      }
      boolean isRfc = StaticUtils.primitiveBooleanValue( form.getIsRfc() );
      if(isRfc)
      {
        if(_edit)
        {
          renderLabel("rfc_create","port.rfc.create",false);
        }
        renderFields(bfpr,port,_rfcFields);
      }
      else
      {
        removeNode("rfc_details",false);
      }
      boolean isDiffFileName = StaticUtils.primitiveBooleanValue( form.getIsDiffFileName() );
      boolean isRunningSequence = false;
      if(isDiffFileName)
      {
        renderFields(bfpr,port,_fileFields);
        boolean isAddFileExt = StaticUtils.primitiveBooleanValue( form.getIsAddFileExt() );
        if(isAddFileExt)
        {
          renderFields(bfpr,port,_fileExtFields);
          Integer fileExtType = StaticUtils.integerValue( form.getFileExtType() );
          if(fileExtType == null)
          {
            removeNode("fileExtValue_details",false);
          }
          else if(IGTPortEntity.FILE_EXT_TYPE_GDOC.equals(fileExtType))
          {
            if(_edit)
            {
              IOptionValueRetriever fieldRetriever = new EntityFieldNameValueRetriever(
                                                            rContext.getResourceLookup(),
                                                            EntityFieldNameValueRetriever.SUBMIT_ID,
                                                            IGTEntity.ENTITY_GRID_DOCUMENT);
              Collection gdocFields = EntityFieldNameValueRetriever.buildFmiCollection(
                                      port.getSession(),IGTEntity.ENTITY_GRID_DOCUMENT,false);
              renderSelectOptions("fileExtValue_value",gdocFields,fieldRetriever,true,"");
              Element node = getElementById("fileExtValue_value",true);
              renderSelectedOptions(node, form.getFileExtValue());
            }
            else
            {
              Number field =  StaticUtils.integerValue( form.getFileExtValue() );
              String display = "";
              try
              {
                IGTManager gdocMgr = port.getSession().getManager(IGTManager.MANAGER_GRID_DOCUMENT);
                IGTFieldMetaInfo fmi = gdocMgr.getSharedFieldMetaInfo(
                                                  IGTEntity.ENTITY_GRID_DOCUMENT, field);
                display = IGTEntity.ENTITY_GRID_DOCUMENT + "." + fmi.getFieldName();
              }
              catch(Throwable probableException)
              {
                //ignore exception
              }
              renderLabel("fileExtValue_value",display,false);
            }
          }
          else if(IGTPortEntity.FILE_EXT_TYPE_SEQUENCE.equals(fileExtType))
          {
            removeNode("fileExtValue_details",false);

            isRunningSequence = true;
            renderFields(bfpr,port,_sequenceFields);

            Boolean isPadded = StaticUtils.booleanValue( form.getIsPadded() );
            if(isPadded.booleanValue())
            {
              renderFields(bfpr,port,_sequencePadFields);
            }
            else
            {
              removeNode("fixNumLength_details",false);
            }
          }
        }
        else
        {
          removeNode("fileExtType_details",false);
          removeNode("fileExtValue_details",false);
        }
      }
      else
      {
        removeNode("fileName_details",false);
        removeNode("isAddFileExt_details",false);
        removeNode("fileExtType_details",false);
        removeNode("fileExtValue_details",false);
      }
      if(!isRunningSequence)
      {
        Node isPaddedParentNode = getElementById("isPadded_details",true).getParentNode();

        removeNode("startNum_details",false);
        removeNode("rolloverNum_details",false);
        removeNode("nextNum_details",false);
        removeNode("isPadded_details",false);
        removeNode("fixNumLength_details",false);

        // 20030530 DDJ: Hack due to IsPadded checkbox being hidden
        // when FileExtType is not Running Sequence Number
        Element isPadded = _target.createElement("input");
        isPadded.setAttribute("name","isPadded");
        isPadded.setAttribute("type","hidden");
        isPadded.setAttribute("value",form.getIsPadded());
        isPaddedParentNode.appendChild(isPadded);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering port screen",t);
    }
  }
}