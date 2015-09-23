/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CompanyProfileDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-10     Andrew Hill         Created
 * 2002-11-25     Andrew Hill         Now part of navgroup_be
 * 2003-07-04     Andrew Hill         Remove deprecated getNavgroup()
 */

package com.gridnode.gtas.client.web.gridnode;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class CompanyProfileDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_COMPANY_PROFILE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new CompanyProfileRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.COMPANY_PROFILE_UPDATE : IDocumentKeys.COMPANY_PROFILE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    CompanyProfileAForm form = (CompanyProfileAForm)actionContext.getActionForm();
    IGTCompanyProfileEntity profile = (IGTCompanyProfileEntity)entity;
    initialiseActionForm(form,profile);
  }

  public static void initialiseActionForm(CompanyProfileAForm form, IGTCompanyProfileEntity profile)
    throws GTClientException
  {
    if (form == null)
      throw new NullPointerException("form is null"); //20030424AH
    if (profile == null)
      throw new NullPointerException("profile is null"); //20030424AH
    form.setCoyName( profile.getFieldString(profile.COY_NAME) );
    form.setEmail( profile.getFieldString(profile.EMAIL) );
    form.setAltEmail( profile.getFieldString(profile.ALT_EMAIL) );
    form.setTel( profile.getFieldString(profile.TEL) );
    form.setAltTel( profile.getFieldString(profile.ALT_TEL) );
    form.setFax( profile.getFieldString(profile.FAX) );
    form.setAddress( profile.getFieldString(profile.ADDRESS) );
    form.setCity( profile.getFieldString(profile.CITY) );
    form.setState( profile.getFieldString(profile.STATE) );
    form.setZipCode( profile.getFieldString(profile.POSTCODE) );
    String country = profile.getFieldString(profile.COUNTRY);
    form.setCountry( country );
    form.setLanguage( profile.getFieldString(profile.LANGUAGE) );
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new CompanyProfileAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_COMPANY_PROFILE;
  }

  protected void validateActionForm(  ActionContext actionContext,
                                      IGTEntity entity,
                                      ActionForm form,
                                      ActionErrors actionErrors)
    throws GTClientException
  {
    validateActionForm((IGTCompanyProfileEntity)entity, (CompanyProfileAForm)form, actionErrors);
  }

  public static void validateActionForm(IGTCompanyProfileEntity profile,
    CompanyProfileAForm form,ActionErrors actionErrors)
    throws GTClientException
  {
    EntityFieldValidator.basicValidateString(actionErrors,profile.COY_NAME,form,profile);
    EntityFieldValidator.basicValidateString(actionErrors,profile.EMAIL,form,profile);
    EntityFieldValidator.basicValidateString(actionErrors,profile.ALT_EMAIL,form,profile);
    EntityFieldValidator.basicValidateString(actionErrors,profile.TEL,form,profile);
    EntityFieldValidator.basicValidateString(actionErrors,profile.ALT_TEL,form,profile);
    EntityFieldValidator.basicValidateString(actionErrors,profile.FAX,form,profile);
    EntityFieldValidator.basicValidateString(actionErrors,profile.ADDRESS,form,profile);
    EntityFieldValidator.basicValidateString(actionErrors,profile.CITY,form,profile);
    EntityFieldValidator.basicValidateString(actionErrors,profile.STATE,form,profile);
    EntityFieldValidator.basicValidateString(actionErrors,profile.POSTCODE,form,profile);
    EntityFieldValidator.basicValidateString(actionErrors,profile.COUNTRY,form,profile);
    EntityFieldValidator.basicValidateString(actionErrors,profile.LANGUAGE,form,profile);
  }

  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    IGTCompanyProfileEntity profile = (IGTCompanyProfileEntity)entity;
    CompanyProfileAForm form = (CompanyProfileAForm)actionContext.getActionForm();
    updateEntityFields(form,profile);
  }

  public static void updateEntityFields(CompanyProfileAForm form, IGTCompanyProfileEntity profile)
    throws GTClientException
  {
    profile.setFieldValue(profile.COY_NAME, form.getCoyName());
    profile.setFieldValue(profile.EMAIL, form.getEmail());
    profile.setFieldValue(profile.ALT_EMAIL, form.getAltEmail());
    profile.setFieldValue(profile.TEL, form.getTel());
    profile.setFieldValue(profile.ALT_TEL, form.getAltTel());
    profile.setFieldValue(profile.FAX, form.getFax());
    profile.setFieldValue(profile.ADDRESS, form.getAddress());
    profile.setFieldValue(profile.CITY,form.getCity());
    profile.setFieldValue(profile.STATE,form.getState());
    profile.setFieldValue(profile.POSTCODE, form.getZipCode());
    profile.setFieldValue(profile.COUNTRY, form.getCountry());
    profile.setFieldValue(profile.LANGUAGE,form.getLanguage());
  }

  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    IGTCompanyProfileEntity entity = null;
    IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
    IGTCompanyProfileManager manager = (IGTCompanyProfileManager)gtasSession.getManager(IGTManager.MANAGER_COMPANY_PROFILE);

    entity = manager.getMyCompanyProfile();
    if(entity == null)
    {
      throw new GTClientException("No company profile available to view/update");
    }

    opContext.setAttribute(IOperationContextKeys.ENTITY, entity);
    ActionForward submitForward = actionContext.getMapping().findForward("submit");
    opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
  }

}