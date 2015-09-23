/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WorkflowActivityRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-19     Andrew Hill         Created
 * 2002-12-20     Andrew Hill         Render portUids field
 * 2003-01-15     Andrew Hill         Ports are now for exitToPort
 * 2003-01-16     Andrew Hill         UserProcedure selection
 * 2003-05-15     Andrew Hill         raiseAlert support
 * 2004-04-01     Daniel D'Cotta      Added support for SUSPEND_ACTIVITY
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Collection;
import java.util.Collections;

import org.w3c.dom.*;

import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.*;

public class WorkflowActivityRenderer extends AbstractRenderer implements IFilter, IBFPROptionSource
{
  private static final Number[] _alertType =
  { //20030515AH
    IGTWorkflowActivityEntity.ALERT_TYPE,
  };
  
  private static final Number[] _userDefinedAlertUids =
  { //20030515AH
    IGTWorkflowActivityEntity.USER_DEFINED_ALERT_UIDS,
  };
  
  private static final String[] _applicableAlertTypes =
  { //20030515AH
    IGTAlertTypeEntity.NAME_DOCUMENT_RECEIVED,
    IGTAlertTypeEntity.NAME_DOCUMENT_RESPONSE_RECEIVED,
    IGTAlertTypeEntity.NAME_USER_DEFINED,
  };
  
  private boolean _edit;

  public WorkflowActivityRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      IGTWorkflowActivityEntity wfa = (IGTWorkflowActivityEntity)getEntity();
      WorkflowActivityAForm form = (WorkflowActivityAForm)getActionForm();
      renderCommonFormElements(wfa.getType(),_edit);

      renderLabel("general_heading", "workflowActivity.general.heading", false); // 20040401 DDJ
      renderLabel("suspend_heading", "workflowActivity.suspend.heading", false); // 20040401 DDJ

      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);

      renderField(bfpr,wfa,IGTWorkflowActivityEntity.TYPE);
      renderField(bfpr,wfa,IGTWorkflowActivityEntity.DESCRIPTION);

      Integer type = form.getActivityTypeInteger();

      if(IGTWorkflowActivityEntity.TYPE_MAPPING_RULE.equals(type) )
      {
        renderField(bfpr, wfa, IGTWorkflowActivityEntity.MAPPING_RULE_UIDS);
        if(_edit)
        {
          renderLabel("mappingRuleUids_create","workflowActivity.mappingRuleUids.create",false);
        }
      }
      else
      {
        removeNode("mappingRuleUids_details",false);
      }

      if(IGTWorkflowActivityEntity.TYPE_USER_PROCEDURE.equals(type) )
      { //20030116AH
        renderField(bfpr, wfa, IGTWorkflowActivityEntity.USER_PROCEDURE_UIDS);
        if(_edit)
        {
          renderLabel("userProcedureUids_create","workflowActivity.userProcedureUids.create",false);
        }
      }
      else
      {
        removeNode("userProcedureUids_details",false);
      }

      if(IGTWorkflowActivityEntity.TYPE_EXIT_TO_PORT.equals(type) ) //20030115AH
      {

        renderField(bfpr, wfa, IGTWorkflowActivityEntity.PORT_UIDS);

        //20021220AH - Force it back to single selection:
        //@todo: investigate if bfpr should be forcing it multiple like it does right now or if
        //that should be a function of the layout and if anything will break if we change it
        //to be like that
        Element portUidsNode = getElementById("portUids_value",false);
        if(portUidsNode != null)
        { //later when can support mutiports in gtas will remove this
          portUidsNode.removeAttribute("multiple");
        }
        if(_edit)
        {
          renderLabel("portUids_create","workflowActivity.portUids.create",false);
        }
      }
      else
      {
        removeNode("portUids_details",false);
      }
      
      if(IGTWorkflowActivityEntity.TYPE_RAISE_ALERT.equals(type) ) //20030515AH
      {
        renderFields(bfpr, wfa, _alertType, this, form, null);
        String alertType = form.getAlertType();
        if( IGTAlertTypeEntity.NAME_USER_DEFINED.equals(alertType) )
        {
          bfpr.setOptionSource(this);
          renderFields(bfpr, wfa, _userDefinedAlertUids, this, form, null);
          Element userDefinedAlertUidsNode = getElementById("userDefinedAlertUids_value",false);
          if(userDefinedAlertUidsNode != null)
          { 
            userDefinedAlertUidsNode.removeAttribute("multiple");
          }
          if(_edit)
          {
            renderLabel("userDefinedAlertUids_create","workflowActivity.userDefinedAlertUids.create",false);
          }
        }
        else
        {
          removeNode("userDefinedAlertUids_details",false);  
        }
      }
      else
      {
        removeNode("userDefinedAlertUids_details",false);
        removeNode("alertType_details",false);
      }
      
      renderField(bfpr,wfa,IGTWorkflowActivityEntity.DISPATCH_INTERVAL);  // 20040401 DDJ
      renderField(bfpr,wfa,IGTWorkflowActivityEntity.DISPATCH_COUNT);     // 20040401 DDJ
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering workflowActivity screen",t);
    }
  }
  
  public boolean allows(Object object, Object context) throws GTClientException
  { //20030515AH
    try
    {
      BindingFieldPropertyRenderer bfpr = (BindingFieldPropertyRenderer)context;
      Number fieldId = bfpr.getFieldId();
      
      if( IGTWorkflowActivityEntity.ALERT_TYPE.equals(fieldId) )
      {
        String name = ((IGTAlertTypeEntity)object).getFieldString(IGTAlertTypeEntity.NAME);
        return StaticUtils.arrayContains(_applicableAlertTypes, name);
      }
      else if( IGTWorkflowActivityEntity.USER_DEFINED_ALERT_UIDS.equals(fieldId) )
      { 
        //We used an optionSource to get these options so no need for further filtration
        return true;
      }    
      return true;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error filtering object "
                                  + object
                                  + " for filter context "
                                  + context, t);
    }
  }

  public Collection getOptions(RenderingContext rContext,
                                BindingFieldPropertyRenderer bfpr)
    throws GTClientException
  {
    try
    {
      if( bfpr.getFieldId().equals(IGTWorkflowActivityEntity.USER_DEFINED_ALERT_UIDS) )
      {
        try
        {
          WorkflowActivityAForm form = (WorkflowActivityAForm)getActionForm();
          
          IGTWorkflowActivityEntity wfa = (IGTWorkflowActivityEntity)getEntity();
          IGTSession gtasSession = wfa.getSession();
          
          IGTAlertTypeManager alertTypeMgr = (IGTAlertTypeManager)gtasSession.getManager(IGTManager.MANAGER_ALERT_TYPE);
          IGTAlertManager alertMgr = (IGTAlertManager)gtasSession.getManager(IGTManager.MANAGER_ALERT);
          IGTAlertTypeEntity alertType = (IGTAlertTypeEntity)
                                          StaticUtils.getFirst(
                                          alertTypeMgr.getByKey(
                                          form.getAlertType(),
                                          IGTAlertTypeEntity.NAME));      
          Long alertTypeUid = alertType == null ? null : alertType.getUidLong();
          return alertTypeUid == null ? Collections.EMPTY_LIST : alertMgr.getByKey(alertTypeUid, IGTAlertEntity.TYPE);
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error providing options for USER_DEFINED_ALERT_UIDS field",t);
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

}

