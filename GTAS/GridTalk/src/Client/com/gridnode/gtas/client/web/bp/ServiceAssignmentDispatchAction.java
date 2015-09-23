/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceAssignmentDispatchAction.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Collection;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTServiceAssignmentEntity;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ServiceAssignmentDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_SERVICE_ASSIGNMENT;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ServiceAssignmentRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.SERVICE_ASSIGNMENT_UPDATE : IDocumentKeys.SERVICE_ASSIGNMENT_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTServiceAssignmentEntity serviceAssignment = (IGTServiceAssignmentEntity)entity;

    ServiceAssignmentAForm form = (ServiceAssignmentAForm)actionContext.getActionForm();
    
    form.setUserName(serviceAssignment.getFieldString(IGTServiceAssignmentEntity.USER_NAME));
    form.setUserType(IGTServiceAssignmentEntity.PARTNER_TYPE);
    //form.setUserPassword(serviceAssignment.getFieldString(IGTServiceAssignmentEntity.PASSWORD));
    Collection webServiceUids=(Collection)serviceAssignment.getFieldValue(IGTServiceAssignmentEntity.WEBSERVICE_UIDS);
    if(webServiceUids==null || webServiceUids.isEmpty())
      form.setWebServiceUids(StaticUtils.EMPTY_STRING_ARRAY);
    else
    {
      String uIds[] = StaticUtils.getStringArray(webServiceUids);
      form.setWebServiceUids(uIds);   
    }
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ServiceAssignmentAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_SERVICE_ASSIGNMENT;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //IGTServiceAssignmentEntity serviceAssignment = (IGTServiceAssignmentEntity)entity;
    ServiceAssignmentAForm tform = (ServiceAssignmentAForm)form;
    
    basicValidateString(errors, IGTServiceAssignmentEntity.USER_NAME, form, entity);
    basicValidateString(errors, IGTServiceAssignmentEntity.USER_TYPE, form, entity);
    String password = tform.getUserPassword();
    if(entity.isNewEntity() || (password!=null && password.trim().length()>0))
    {
      basicValidateString(errors, IGTServiceAssignmentEntity.PASSWORD, form, entity);
    }
    basicValidateStringArray(errors, IGTServiceAssignmentEntity.WEBSERVICE_UIDS, form, entity);
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTServiceAssignmentEntity serviceAssignment = (IGTServiceAssignmentEntity)entity;
    ServiceAssignmentAForm form = (ServiceAssignmentAForm)actionContext.getActionForm();
    serviceAssignment.setFieldValue(IGTServiceAssignmentEntity.USER_NAME, form.getUserName());
    serviceAssignment.setFieldValue(IGTServiceAssignmentEntity.USER_TYPE, IGTServiceAssignmentEntity.PARTNER_TYPE);
    String password = form.getUserPassword();
    if(entity.isNewEntity() || (password!=null && password.trim().length()>0))
    {
      serviceAssignment.setFieldValue(IGTServiceAssignmentEntity.PASSWORD, form.getUserPassword());
    }
    
    Collection webServiceUids=StaticUtils.collectionValue(form.getWebServiceUids()); 
    serviceAssignment.setFieldValue(IGTServiceAssignmentEntity.WEBSERVICE_UIDS, webServiceUids);
  }

}