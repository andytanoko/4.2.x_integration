/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserUpdateRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2005-03-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.user;

import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.ctrl.IGTAccountStateEntity;
import com.gridnode.gtas.client.ctrl.IGTUserEntity;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;


public class UserUpdateRenderer extends AbstractRenderer
{
  private boolean _edit;
  
  private static final Number[] _userFields =     {
                                        IGTUserEntity.EMAIL,
                                        IGTUserEntity.PHONE,
                                        IGTUserEntity.USER_ID,
                                        IGTUserEntity.USER_NAME,
                                      };

  public UserUpdateRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }


  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();    
      UserUpdateAForm form = (UserUpdateAForm)getActionForm();
      IGTUserEntity user = (IGTUserEntity)getEntity();
      IGTAccountStateEntity profile = (IGTAccountStateEntity)user.getFieldValue(IGTUserEntity.PROFILE);
      ActionErrors errors = rContext.getActionErrors();

      renderCommonFormElements(user.getType(),_edit);

      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      Number[] _userFields =     {
                                        IGTUserEntity.EMAIL,
                                        IGTUserEntity.PHONE,
                                        IGTUserEntity.USER_ID,
                                        IGTUserEntity.USER_NAME,
                                      };
      renderFields(bfpr, user, _userFields);
      renderPasswords(bfpr,errors, user);
      renderLabelCarefully("ok", "userUpdate.savePassword", false);  
      removeNode("help_button",false);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering user screen",t);
    }
  }

  protected void renderPasswords( BindingFieldPropertyRenderer bfpr,
                                  ActionErrors errors,
                                  IGTUserEntity user)
    throws RenderingException
  {
    bfpr.reset();
    bfpr.setBindings("password",false);
    bfpr.setVisible(_edit);
    bfpr.setMandatory(true);
    bfpr.setLabelKey("userUpdate.password");
    bfpr.setValue("");
    bfpr.setErrorKey(MessageUtils.getFirstErrorKey(errors,"password"));
    bfpr.setEditable(_edit);
    bfpr.render(_target);

    bfpr.reset();

    bfpr.setBindings("newPassword",false);
    bfpr.setVisible(_edit);
    bfpr.setMandatory(true);
    bfpr.setLabelKey("userUpdate.newPassword");
    bfpr.setValue("");
    bfpr.setErrorKey(MessageUtils.getFirstErrorKey(errors,"newPassword"));
    bfpr.setEditable(_edit);
    bfpr.render(_target);
    
    bfpr.reset();
    
    bfpr.setBindings("confirmPassword",false);
    bfpr.setVisible(_edit);
    bfpr.setMandatory(true);
    bfpr.setLabelKey("userUpdate.confirmPassword");
    bfpr.setValue("");
    bfpr.setErrorKey(MessageUtils.getFirstErrorKey(errors,"confirmPassword"));
    bfpr.setEditable(_edit);
    bfpr.render(_target);
  }


}













