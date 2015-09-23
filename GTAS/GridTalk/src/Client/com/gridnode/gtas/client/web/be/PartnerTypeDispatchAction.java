/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerTypeDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-09     Andrew Hill         Created
 * 2002-11-25     Andrew Hill         Now under "navgroup_partner"
 */
package com.gridnode.gtas.client.web.be;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPartnerTypeEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class PartnerTypeDispatchAction extends EntityDispatchAction
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_partner";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PARTNER_TYPE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new PartnerTypeRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.PARTNER_TYPE_UPDATE : IDocumentKeys.PARTNER_TYPE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    if(entity.isNewEntity())
    {
    }
    else
    {
      PartnerTypeAForm partnerTypeAForm = (PartnerTypeAForm)actionContext.getActionForm();
      partnerTypeAForm.setPartnerType((String)entity.getFieldValue(IGTPartnerTypeEntity.PARTNER_TYPE));
      partnerTypeAForm.setDescription((String)entity.getFieldValue(IGTPartnerTypeEntity.DESCRIPTION));
    }
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new PartnerTypeAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_PARTNER_TYPE;
  }

  protected boolean doSave(ActionContext actionContext, ActionErrors errors)
    throws GTClientException
  {
    //ISimpleResourceLookup rLookup = 
	createResourceLookup(actionContext);
    OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());

    if(opContext == null)
      throw new java.lang.NullPointerException("No OperationContext found");

    IGTEntity entity = (IGTEntity)opContext.getAttribute(IOperationContextKeys.ENTITY);
    if(entity == null)
      throw new java.lang.NullPointerException("No entity found in Operation Context");

    PartnerTypeAForm partnerTypeAForm = (PartnerTypeAForm)actionContext.getActionForm();


    // Validate the request parameters specified by the user
    boolean failed = false;
    if(partnerTypeAForm.getPartnerType().equals(""))
    {
      errors.add("partnerType", new ActionError("error.partnerType.required"));
      failed = true;
    }
    if(partnerTypeAForm.getDescription().equals(""))
    {
      errors.add("description", new ActionError("error.description.required"));
      failed = true;
    }
    if(!failed)
    {
      entity.setFieldValue(IGTPartnerTypeEntity.PARTNER_TYPE, partnerTypeAForm.getPartnerType());
      entity.setFieldValue(IGTPartnerTypeEntity.DESCRIPTION,  partnerTypeAForm.getDescription());

      IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
      IGTManager manager = gtasSession.getManager(IGTManager.MANAGER_PARTNER_TYPE);
      try
      {
        if(entity.isNewEntity())
        {
          manager.create(entity);
        }
        else
        {
          manager.update(entity);
        }
      }
      catch(Exception e)
      {
        throw new GTClientException("Error saving PartnerType",e);
      }
    }
    return failed;
  }
}