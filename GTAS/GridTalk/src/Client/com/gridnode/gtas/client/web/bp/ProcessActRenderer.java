/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessActRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-14     Daniel D'Cotta      Created
 * 2002-12-24     Daniel D'Cotta      Commented out some fields as they have been
 *                                    moved to ProcessAct, but not implemented yet
 * 2003-02-14     Daniel D'Cotta      Added new fields
 * 2003-11-05     Daniel D'Cotta      Display BIZ_ACTIVITY_IDENTIFIER base on
 *                                    RNIF_VERSION of processDef
 * 2007-11-07     Tam Wei Xiang       Added field "is_compress_required". Render header
 *                                    "Compression"                                   
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Iterator;

import org.apache.struts.action.ActionErrors;
import org.w3c.dom.Element;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTMappingFileEntity;
import com.gridnode.gtas.client.ctrl.IGTProcessActEntity;
import com.gridnode.gtas.client.ctrl.IGTProcessDefEntity;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class ProcessActRenderer extends AbstractRenderer implements IFilter
{
  private boolean _edit;
  private static final Number fields[] =
  {
    IGTProcessActEntity.MSG_TYPE,
    IGTProcessActEntity.DICT_FILE,
    IGTProcessActEntity.XML_SCHEMA,

    IGTProcessActEntity.BIZ_ACTIVITY_IDENTIFIER,
    IGTProcessActEntity.BIZ_ACTION_CODE,
    IGTProcessActEntity.RETRIES,
    IGTProcessActEntity.TIME_TO_ACKNOWLEDGE,

    IGTProcessActEntity.IS_AUTHORIZATION_REQUIRED,
    IGTProcessActEntity.IS_NON_REPUDIATION_REQUIRED,
    IGTProcessActEntity.IS_SECURE_TRANSPORT_REQUIRED,
    IGTProcessActEntity.DISABLE_DTD,
    IGTProcessActEntity.DISABLE_SCHEMA,
    IGTProcessActEntity.VALIDATE_AT_SENDER,

    IGTProcessActEntity.DISABLE_ENCRYPTION,
    IGTProcessActEntity.DISABLE_SIGNATURE,
    IGTProcessActEntity.ONLY_ENCRYPT_PAYLOAD,
    IGTProcessActEntity.DIGEST_ALGORITHM,
    IGTProcessActEntity.ENCRYPTION_ALGORITHM,
    IGTProcessActEntity.ENCRYPTION_ALGORITHM_LENGTH,
    IGTProcessActEntity.IS_COMPRESS_REQUIRED
  };

  public ProcessActRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTProcessActEntity processAct = (IGTProcessActEntity)getEntity();
      ProcessActAForm form = (ProcessActAForm)getActionForm();
      RenderingContext rContext = getRenderingContext();

      renderCommonFormElements(processAct.getType(), _edit);
      //BindingFieldPropertyRenderer bfpr = 
      renderFields(null, processAct, fields, this, form, "");

      // Render group headings
      renderLabel("files_heading",      "processAct.files.heading",       false);
      renderLabel("details_heading",    "processAct.details.heading",     false);
      renderLabel("options_heading",    "processAct.options.heading",     false);
      renderLabel("encryption_heading", "processAct.encryption.heading",  false);
      renderLabel("compression_heading","processAct.compression.heading",false);

      if(_edit)
      { // Render Diversion
        renderLabel("msgType_create",   "processAct.msgType.create",    false);
        renderLabel("dictFile_create",  "processAct.dictFile.create",   false);
        renderLabel("xmlSchema_create", "processAct.xmlSchema.create",  false);
      }
      
      // 20031105 DDJ: GNDB00015684 - Use the RNIF_VERSION in the request
      ProcessDefAForm processDefAForm = (ProcessDefAForm)getRenderingContext().getOperationContext()
                                        .getPreviousContext().getActionForm();
      if(IGTProcessDefEntity.RNIF_1_1.equals(processDefAForm.getRnifVersion()))
      {
        renderLabel("bizActivityIdentifier_label",   "processAct.globalProcessCode",    false);
        
        ActionErrors actionErrors = rContext.getActionErrors();
        if(actionErrors != null)
        {
          actionErrors.get(processAct.getFieldName(IGTProcessActEntity.BIZ_ACTIVITY_IDENTIFIER));
          Iterator actionErrorsIterator = actionErrors.get(processAct.getFieldName(IGTProcessActEntity.BIZ_ACTIVITY_IDENTIFIER)); 
          if(actionErrorsIterator.hasNext())
          {
            Element bizActivityIdentifierErrorElement = (Element)getFieldErrorNode(IGTProcessActEntity.BIZ_ACTIVITY_IDENTIFIER, false);
            renderLabelCarefully(bizActivityIdentifierErrorElement, "processAct.error.globalProcessCode.required");
          }
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering processAct screen",t);
    }
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    if(object instanceof IGTMappingFileEntity)
    {
      Short type = (Short)((IGTMappingFileEntity)object).getFieldValue(IGTMappingFileEntity.TYPE);
      Number fieldId = ((BindingFieldPropertyRenderer)context).getFieldId();
      if(!((IGTProcessActEntity.MSG_TYPE.equals(fieldId)    && IGTMappingFileEntity.TYPE_DTD.equals(type)) ||
           (IGTProcessActEntity.XML_SCHEMA.equals(fieldId)  && IGTMappingFileEntity.TYPE_SCHEMA.equals(type)) ||
           (IGTProcessActEntity.DICT_FILE.equals(fieldId)   && IGTMappingFileEntity.TYPE_DICT.equals(type))))
        return false;
    }
    return true;
  }
}