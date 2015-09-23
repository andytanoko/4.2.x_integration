/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReturnDefRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 * 2003-06-04     Andrew Hill         Support for alert field as an FER
 * 2003-06-13     Daniel D'Cotta      Added diversion for creating an alert
 *                                    Added value1 & value2 when operator is 'between'
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Collection;
import java.util.Collections;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.*;

public class ReturnDefRenderer extends AbstractRenderer implements IBFPROptionSource, IFilter
{
  private boolean _edit;
  private static final Number _fields[] = //20030604AH add '_' to variable name
  {
    IGTReturnDefEntity.RETURN_LIST_OPERATOR,
    IGTReturnDefEntity.RETURN_LIST_VALUE,
    IGTReturnDefEntity.RETURN_LIST_ACTION,
    IGTReturnDefEntity.RETURN_LIST_ALERT,
  };

  public ReturnDefRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  { //20030604AH
    try
    {
      RenderingContext rContext = getRenderingContext();
      IGTReturnDefEntity returnDef = (IGTReturnDefEntity)getEntity();
      ReturnDefAForm form = (ReturnDefAForm)getActionForm();

      renderCommonFormElements(IGTEntity.ENTITY_RETURN_DEF,_edit);
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      bfpr.setOptionSource(this);
      bfpr = renderFields(bfpr, returnDef, _fields, this, form, null);

      // Render diversion labels
      if(_edit)
      {
        renderLabel("alert_create", "returnDef.alert.create", false); // 20030613 DDJ: Added

        // 20030613 DDJ: Display value1 & value2 when operator is 'between'
        // However, still store it into 1 field for B-Tier
        if(IGTReturnDefEntity.OPERATOR_BETWEEN.equals(form.getOperatorInteger()))
        {
          renderElementValue("value1_value", form.getValue1());
          renderElementValue("value2_value", form.getValue2());

          removeNode("value_value");
        }
        else
        {
          removeNode("multiValues_details");
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering returnDef",t);
    }
  }

  public Collection getOptions(RenderingContext rContext,BindingFieldPropertyRenderer bfpr)
    throws GTClientException
  { //20030604AH
    try
    {
      if( bfpr.getFieldId().equals(IGTReturnDefEntity.RETURN_LIST_ALERT) )
      { //Return a collection of all the user defined alerts.
        //This means first looking up the userdefined alertType entity so that we can
        //use its uid to getByKey() all those alerts that have it as their type
        try
        {
          IGTSession gtasSession = getEntity().getSession();

          IGTAlertTypeManager alertTypeMgr = (IGTAlertTypeManager)gtasSession.getManager(IGTManager.MANAGER_ALERT_TYPE);
          IGTAlertManager alertMgr = (IGTAlertManager)gtasSession.getManager(IGTManager.MANAGER_ALERT);
          IGTAlertTypeEntity udAlertType = (IGTAlertTypeEntity)
                                          StaticUtils.getFirst(
                                          alertTypeMgr.getByKey(
                                          IGTAlertTypeEntity.NAME_USER_DEFINED,
                                          IGTAlertTypeEntity.NAME));
          Long udAlertTypeUid = udAlertType == null ? null : udAlertType.getUidLong();
          Collection opts = udAlertTypeUid == null ? Collections.EMPTY_LIST : alertMgr.getByKey(udAlertTypeUid, IGTAlertEntity.TYPE);
          return opts;
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error providing options for RETURN_LIST_ALERT field",t);
        }
      }
      else
      {
        return null;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error providing options",t);
    }
  }

  public boolean allows(Object object, Object context) throws GTClientException
  { //20030604AH
    //BFPR / renderFields() currently has a bug where if filter is specified as null, instead
    //of using the BFPR itself as the filter impl, it will not render options for foreign entities!
    //For now I shall use a nominal filter here to make things work.
    //@todo: fix bfpr asap!
    return true;
  }
}