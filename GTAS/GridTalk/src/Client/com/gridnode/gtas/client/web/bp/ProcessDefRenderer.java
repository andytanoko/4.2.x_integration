/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDefRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-14     Daniel D'Cotta      Created
 * 2002-11-28     Daniel D'Cotta      Aligned embeded entities diversions
 * 2002-12-24     Daniel D'Cotta      Commented out some fields as they have been
 *                                    moved to ProcessAct, but not implemented yet
 * 2003-02-14     Daniel D'Cotta      Added new fields
 * 2003-08-20     Andrew Hill         Support userTrackingIdentifier field
 */
package com.gridnode.gtas.client.web.bp;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTProcessDefEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;

public class ProcessDefRenderer extends AbstractRenderer
{
  private boolean _edit;
  private static final Number _fields[] = //20030820AH - Renamed with underscore in line with our coding conventions
  {
    IGTProcessDefEntity.DEF_NAME,
    IGTProcessDefEntity.IS_SYNCHRONOUS,
    IGTProcessDefEntity.PROCESS_TYPE,
    IGTProcessDefEntity.ACTION_TIME_OUT,
    IGTProcessDefEntity.RNIF_VERSION,
    IGTProcessDefEntity.VERSION_IDENTIFIER,
    IGTProcessDefEntity.PROCESS_INDICATOR_CODE,
    IGTProcessDefEntity.USAGE_CODE,
    //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
    //IGTProcessDefEntity.DIGEST_ALG_CODE,

    IGTProcessDefEntity.FROM_PARTNER_CLASS_CODE,
    IGTProcessDefEntity.FROM_PARTNER_ROLE_CLASS_CODE,
    IGTProcessDefEntity.FROM_BIZ_SERVICE_CODE,

    IGTProcessDefEntity.TO_PARTNER_CLASS_CODE,
    IGTProcessDefEntity.TO_PARTNER_ROLE_CLASS_CODE,
    IGTProcessDefEntity.TO_BIZ_SERVICE_CODE,

    IGTProcessDefEntity.REQUEST_DOC_THIS_DOC_IDENTIFIER,
    IGTProcessDefEntity.RESPONSE_DOC_THIS_DOC_IDENTIFIER,
    IGTProcessDefEntity.RESPONSE_DOC_REQUEST_DOC_IDENTIFIER,

    //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
    //IGTProcessDefEntity.DISABLE_DTD,
    //IGTProcessDefEntity.DISABLE_SCHEMA,
    //IGTProcessDefEntity.DISABLE_SIGNATURE,
    //IGTProcessDefEntity.DISABLE_ENCRYPTION,
    //IGTProcessDefEntity.ENABLE_ENCRYPT_PAYLOAD,
    //IGTProcessDefEntity.VALIDATE_AT_SENDER,
    
    IGTProcessDefEntity.USER_TRACKING_IDENTIFIER, //20030820AH
  };

  public ProcessDefRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTProcessDefEntity processDef = (IGTProcessDefEntity)getEntity();
      ProcessDefAForm form = (ProcessDefAForm)getActionForm();
      RenderingContext rContext = getRenderingContext();
      ActionErrors errors = rContext.getActionErrors();

      renderCommonFormElements(processDef.getType(), _edit);

      //20030820AH - co: BindingFieldPropertyRenderer bfpr = renderFields(fields, this);
      //BindingFieldPropertyRenderer bfpr = 
      renderFields(null,processDef,_fields,null,form,null); //20030820AH

      // Render group headings
      renderLabel("general_heading",        "processDef.general.heading", false);
      renderLabel("from_heading",           "processDef.from.heading",    false);
      renderLabel("to_heading",             "processDef.to.heading",      false);
      renderLabel("docIdentifier_heading",  "processDef.docIdentifier.heading",      false);
      //20021224 DDJ: Not implemented yet! May be moved to ProcessAct?
      //renderLabel("options_heading",        "processDef.options.heading", false);

      String editType = _edit ? "edit" : "view";
      if(IGTProcessDefEntity.TYPE_SINGLE_ACTION.equals(form.getProcessType()))
      {
        renderLabel("requestAct_" + editType, "processDef.requestAct." + editType ,false);
        removeNode("responseAct_details");
        removeNode("actionTimeOut_details");

        renderEmbededEntitiesError(errors, IGTProcessDefEntity.REQUEST_ACT, processDef);
      }
      else if(IGTProcessDefEntity.TYPE_TWO_ACTION.equals(form.getProcessType()))
      {
        renderLabel("requestAct_" + editType, "processDef.requestAct." + editType ,false);
        renderLabel("responseAct_" + editType, "processDef.responseAct." + editType, false);

        renderEmbededEntitiesError(errors, IGTProcessDefEntity.REQUEST_ACT, processDef);
        renderEmbededEntitiesError(errors, IGTProcessDefEntity.RESPONSE_ACT, processDef);
      }
      else
      {
        removeNode("requestAct_details");
        removeNode("responseAct_details");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering processDef screen", t);
    }
  }

  /** @todo see if this can be made generic */
  protected void renderEmbededEntitiesError(ActionErrors actionErrors,
                                            Number fieldId,
                                            IGTEntity entity)
     throws RenderingException
  {
    try
    {
      String fieldName = entity.getFieldName(fieldId);
      ActionError error = MessageUtils.getFirstError(actionErrors, fieldName);
      if(error != null)
      {
        renderLabel(fieldName + "_error", error.getKey());
        getElementById(fieldName + "_error", true).setAttribute("class", "errortext");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering errors for embeded entities", t);
    }
  }
}