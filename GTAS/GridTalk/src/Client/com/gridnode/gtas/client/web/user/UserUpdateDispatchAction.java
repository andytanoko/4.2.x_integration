/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserUpdateDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2005-03-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.user;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.IGTUserEntity;
import com.gridnode.gtas.client.ctrl.IGTUserManager;
import com.gridnode.gtas.client.ctrl.ITextConstraint;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;


public class UserUpdateDispatchAction extends EntityDispatchAction2
{
  private static final Number[] _userFields =     {
                                        IGTUserEntity.EMAIL,
                                        IGTUserEntity.PHONE,
                                        IGTUserEntity.USER_ID,
                                        IGTUserEntity.USER_NAME,
                                      };

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_USER;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new UserUpdateRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return IDocumentKeys.UPDATE_USER_UPDATE ;
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    UserUpdateAForm form = (UserUpdateAForm)actionContext.getActionForm();
    IGTUserEntity user = (IGTUserEntity)entity;
    initFormFields(actionContext, _userFields);
    form.setPassword("");
    form.setNewPassword("");
    form.setConfirmPassword("");
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new UserUpdateAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_USER;
  }

  protected void validateActionForm(  ActionContext actionContext,
                                      IGTEntity entity,
                                      ActionForm actionForm,
                                      ActionErrors actionErrors)
    throws GTClientException
  {
    UserUpdateAForm form = (UserUpdateAForm)actionForm; 
    IGTUserEntity user = (IGTUserEntity)entity;

    String password = form.getPassword();
    if(password == null) password = "";
    String newPassword = form.getNewPassword(); 
    if(newPassword == null) newPassword = "";
    String confirmPassword = form.getConfirmPassword(); 
    if(confirmPassword == null) confirmPassword = "";

      // If password field had data then we need to check the two are the same!
    if(password.equals(""))
    {
      EntityFieldValidator.addFieldError( actionErrors,"password","userUpdate",
                                          EntityFieldValidator.REQUIRED,null); 
    } 
    
    if(newPassword.equals(""))
    {
      EntityFieldValidator.addFieldError( actionErrors,"newPassword","userUpdate",
                                          EntityFieldValidator.REQUIRED,null); 
    }
    else
    {
      IGTFieldMetaInfo pwFmi = user.getFieldMetaInfo(IGTUserEntity.NEW_PASSWORD);
      ITextConstraint constraint = (ITextConstraint)pwFmi.getConstraint();
      int minLength = constraint.getMinLength();
      int maxLength = constraint.getMaxLength();
      if( (newPassword.length() < minLength) || (newPassword.length()> maxLength) )
      {
        EntityFieldValidator.addFieldError( actionErrors,"newPassword","userUpdate",
                                            EntityFieldValidator.INVALID,null);
      } 
    }
    
    if(confirmPassword.equals(""))
    {
      EntityFieldValidator.addFieldError( actionErrors,"confirmPassword","userUpdate",
                                          EntityFieldValidator.REQUIRED,null); 
    }
    if(!newPassword.equals(confirmPassword))
    {
      EntityFieldValidator.addFieldError( actionErrors,"confirmPassword","userUpdate",
                                          EntityFieldValidator.MISMATCH,null); 
    }
    
    
    
  }

  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    UserUpdateAForm form = (UserUpdateAForm)actionContext.getActionForm();
    IGTUserEntity user = (IGTUserEntity)entity;
    String newPassword = form.getNewPassword();
    user.setFieldValue(IGTUserEntity.NEW_PASSWORD, newPassword);
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    // We override saveWithManager as user has special stuff to save...
    IGTUserEntity user = (IGTUserEntity)entity;

    if(entity.isNewEntity())
    {
      throw new IllegalStateException("new users not supported by this action!");
    }
    else
    {
      UserUpdateAForm form = (UserUpdateAForm)actionContext.getActionForm();
      String oldPassword = form.getPassword();
      IGTUserManager userMgr = (IGTUserManager)manager;
      userMgr.updatePasswordOnly(user, oldPassword);
    }
  }

  protected IGTEntity getNewEntityInstance(ActionContext actionContext, IGTManager manager) throws GTClientException
  {
    throw new IllegalStateException("Can only edit current account");
  }

  protected IGTEntity getRequestedEntity(ActionContext actionContext) throws GTClientException
  { //Can only edit the current user account
    IGTSession gtasSession = getGridTalkSession(actionContext);
    IGTUserEntity currentAccount = gtasSession.getUser();
    return currentAccount;    
  }

}