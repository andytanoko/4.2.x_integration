/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerGroupDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 * 2002-11-25     Andrew Hill         Now under "navgroup_partner"
 */
package com.gridnode.gtas.client.web.be;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTPartnerGroupEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class PartnerGroupDispatchAction extends EntityDispatchAction
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_partner";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PARTNER_GROUP;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    /*IGTEntity entity = null;
    try
    {
      entity = getEntity(actionContext);
    }
    catch(Exception e)
    {
      throw new RenderingException("Unable to get entity",e);
    }
    PartnerGroupAForm form = (PartnerGroupAForm)actionContext.getActionForm();

    IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
    IGTPartnerTypeManager ptMgr = (IGTPartnerTypeManager)gtasSession.getManager(IGTManager.MANAGER_PARTNER_TYPE);

    boolean canEditPartnerTypes = (edit && entity.isNewEntity());
    Collection partnerTypes = getPartnerTypesToRender(ptMgr, form, canEditPartnerTypes);

    return new PartnerGroupRenderer(rContext, edit, partnerTypes);*/
    return new PartnerGroupRenderer(rContext, edit);
  }
  /*
  private Collection getPartnerTypesToRender( IGTPartnerTypeManager ptMgr,
                                              PartnerGroupAForm form,
                                              boolean editable)
    throws GTClientException
  {
    Collection partnerTypes = null;
    if(editable)
    {
      partnerTypes = ptMgr.getAll();
    }
    else
    {
      partnerTypes = new ArrayList();
      try
      {
        long partnerTypeUid = Long.parseLong(form.getPartnerType());
        IGTEntity ptEntity = ptMgr.getByUID(partnerTypeUid);
        partnerTypes.add(ptEntity);
      }
      catch(Exception e)
      {
        throw new GTClientException("Error looking up partnerType uid=" + form.getPartnerType(),e);
      }
    }
    if(partnerTypes == null) partnerTypes = new ArrayList();
    return partnerTypes;
  }*/


  protected String getNavigatorDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return null;
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.PARTNER_GROUP_UPDATE : IDocumentKeys.PARTNER_GROUP_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    if(entity.isNewEntity())
    {
    }
    else
    {
      PartnerGroupAForm form = (PartnerGroupAForm)actionContext.getActionForm();
      form.setDescription(entity.getFieldString(IGTPartnerGroupEntity.DESCRIPTION));
      form.setName(entity.getFieldString(IGTPartnerGroupEntity.NAME));
      form.setPartnerType(entity.getFieldString(IGTPartnerGroupEntity.PARTNER_TYPE));
    }
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new PartnerGroupAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_PARTNER_GROUP;
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

    PartnerGroupAForm form = (PartnerGroupAForm)actionContext.getActionForm();


    // Validate the request parameters specified by the user
    boolean failed = false;
    basicValidateString(errors,"description",form.getDescription(),IGTPartnerGroupEntity.DESCRIPTION,entity);
    basicValidateString(errors,"name",form.getName(),IGTPartnerGroupEntity.NAME,entity);
    basicValidateString(errors,"partnerType",form.getPartnerType(),IGTPartnerGroupEntity.PARTNER_TYPE,entity);
    failed = (errors.size() != 0);
    if(!failed)
    {
      entity.setFieldValue(IGTPartnerGroupEntity.DESCRIPTION, form.getDescription());
      entity.setFieldValue(IGTPartnerGroupEntity.NAME, form.getName());
      entity.setFieldValue(IGTPartnerGroupEntity.PARTNER_TYPE, new Long(form.getPartnerType()));

      IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
      IGTManager manager = gtasSession.getManager(this.getIGTManagerType(actionContext));
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
        throw new GTClientException("Error saving PartnerGroup",e);
      }
    }
    return failed;
  }

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    // If we are a child operation diverted to by partnerDispatchAction then we may have
    // the uid of the partnerType for this group from that, so set it to the actionForm
    // if this is the case.
    OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
    PartnerGroupAForm form = (PartnerGroupAForm)actionContext.getActionForm();
    String partnerType = (String)opCon.getAttribute("partner.partnerType");
    if(partnerType != null)
    {
      form.setPartnerType(partnerType);
    }
  }
}