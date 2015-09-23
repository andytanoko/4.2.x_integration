/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-22     Andrew Hill         Created
 * 2002-12-18     Andrew Hill         Render tabpane
 * 2003-03-26     Andrew Hill         Render the reset logins label carefully (now has an icon)
 * 2005-03-24     Andrew Hill         Hide the diversion link for the roles (2 level access now)
 * 2007-07-18     Tam Wei Xiang       Add in the prompting of error if there is any error in the fields
 *                                    within the tabs.
 */
package com.gridnode.gtas.client.web.user;

import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTAccountStateEntity;
import com.gridnode.gtas.client.ctrl.IGTBusinessEntityEntity;
import com.gridnode.gtas.client.ctrl.IGTRoleEntity;
import com.gridnode.gtas.client.ctrl.IGTUserEntity;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.ITabDef;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.renderers.TabDef;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;

public class UserRenderer extends AbstractRenderer implements IFilter
{
  private static final ITabDef[] _editTabs = { //20021218AH
    new TabDef("user.tabs.details","details_tab"),
    new TabDef("user.tabs.state","state_tab"),
    new TabDef("user.tabs.password","password_tab"),
  };

  private static final ITabDef[] _viewTabs = { //20021218AH
    _editTabs[0],
    _editTabs[1],
  };

  private boolean _edit;

  private static final Number[] _userFields =     {
                                        IGTUserEntity.EMAIL,
                                        IGTUserEntity.PHONE,
                                        IGTUserEntity.PROPERTY,
                                        IGTUserEntity.USER_ID,
                                        IGTUserEntity.USER_NAME,
                                        IGTUserEntity.BES,
                                      }; 
                                      
  private static final Number[] _rolesField = { IGTUserEntity.ROLES }; //20050324AH

  private static final Number[] _profileFields =  {
                                        IGTAccountStateEntity.STATE,
                                        IGTAccountStateEntity.CREATED,
                                        IGTAccountStateEntity.CREATED_BY,
                                        IGTAccountStateEntity.LAST_LOGIN_TIME,
                                        IGTAccountStateEntity.LAST_LOGOUT_TIME,
                                        IGTAccountStateEntity.FROZEN,
                                        IGTAccountStateEntity.FREEZE_TIME,
                                      };


  public UserRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }


  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      UserAForm form = (UserAForm)getActionForm();
      IGTUserEntity user = (IGTUserEntity)getEntity();
      IGTAccountStateEntity profile = (IGTAccountStateEntity)user.getFieldValue(IGTUserEntity.PROFILE);
      ActionErrors errors = rContext.getActionErrors();

      renderCommonFormElements(user.getType(),_edit);
      renderLabel("profile_label","user.profile");
      renderLabel("passwords_label","user.passwords");
      if(_edit)
      {
        renderLabel("roles_create","user.roles.create",false);
        renderLabel("bes_create","user.bes.create",false);
      }

      if(user.isNewEntity())
      {
        removeNode("profile_value",false);
      }
      else
      {
        if(!loadedFrozen(profile))
        {
          removeNode("freezeTime_details");
        }
        if(_edit)
        {
          renderLabelCarefully("reset_logins","user.loginAttempts.reset",false); //20030326AH
          if(!loadedFrozen(profile))
          {
            replaceNodeWithNewElement("span","frozen_value",false,false);
          }
        }
      }

      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      renderFields(bfpr, user, _userFields, (IFilter)this, form, null);
      
      //20050324AH - Roles are no longer configurable or divertable...
      bfpr.setFbdEnabled(false);
      renderFields(bfpr, user, _rolesField, (IFilter)this, form, null);
      bfpr.setFbdEnabled(true);
      //..........
      
      renderFields(bfpr, profile, _profileFields, (IFilter)this, form, null);
      renderPasswords(bfpr,errors, user);
      renderLoginAttempts(bfpr,errors,profile);

      renderTabs(rContext, "userTab", (_edit ? _editTabs : _viewTabs) ); //20021218AH
      
      //TWX 18072007:  Add in the pop up to notify the user if there is any input error from user 
      if(_edit) 
      {
        includeJavaScript(IGlobals.JS_ENTITY_FORM_METHODS);
        appendEventMethod(getBodyNode(),"onload","tabErrorNotifier();");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering user screen",t);
    }
  }

  protected boolean loadedFrozen(IGTAccountStateEntity profile)
  {
    try
    {
      if(profile == null) return false;
      return Boolean.TRUE.equals(profile.getFieldValue(profile.FROZEN));
    }
    catch(Throwable t)
    {
      return false;
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
    bfpr.setMandatory(user.isNewEntity());
    bfpr.setLabelKey("user.password");
    bfpr.setValue("");
    bfpr.setErrorKey(MessageUtils.getFirstErrorKey(errors,"password"));
    bfpr.setEditable(_edit);
    bfpr.render(_target);

    bfpr.reset();

    bfpr.setBindings("newPassword",false);
    bfpr.setVisible(_edit);
    bfpr.setMandatory(user.isNewEntity());
    bfpr.setLabelKey("user.newPassword");
    bfpr.setValue("");
    bfpr.setErrorKey(MessageUtils.getFirstErrorKey(errors,"newPassword"));
    bfpr.setEditable(_edit);
    bfpr.render(_target);
  }

  protected void renderLoginAttempts( BindingFieldPropertyRenderer bfpr,
                                      ActionErrors errors,
                                      IGTAccountStateEntity profile)
    throws RenderingException
  {
    bfpr.reset();
    bfpr.setBindings(profile,profile.BAD_LOGIN_ATTEMPTS,errors);
    bfpr.setEditable(_edit);
    bfpr.render(_target);
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    if(object instanceof IGTBusinessEntityEntity)
    {
      //only show BEs that are of our own enterprise - that is be.isPartner==false
      return !StaticUtils.primitiveBooleanValue(
            ((IGTBusinessEntityEntity)object).getFieldString(
            IGTBusinessEntityEntity.IS_PARTNER) );
    }
    else if(object instanceof IGTRoleEntity)
    { //20050324AH - Quick hack to stop the unwanted role showing
      String roleName = ((IGTRoleEntity)object).getFieldString(IGTRoleEntity.ROLE);
      if("User Administrator".equals(roleName))
      {
        return false;
      }
    }
    return true;
  }

}












