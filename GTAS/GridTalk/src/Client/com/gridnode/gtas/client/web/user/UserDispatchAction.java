/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-15     Andrew Hill         Created
 * 2002-06-04     Andrew Hill         Refactored
 * 2002-06-22     Andrew Hill         Refactored to use EntityDispatchAction2
 * 2002-10-30     Andrew Hill         Use log instead of System.out
 * 2002-11-25     Andrew Hill         Now under navgroup_server
 * 2003-01-06     Andrew Hill         Modify validation method
 * 2009-04-17     Tam Wei Xiang       #146 - Check whether user has entered the
 *                                           password with pre-defined length
 *                                           (def at least 6 chars)
 */
package com.gridnode.gtas.client.web.user;

import java.util.*;
import org.apache.struts.action.*;
import org.apache.commons.logging.*;

import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.client.web.strutsbase.*;
import com.gridnode.gtas.client.utils.*;

public class UserDispatchAction extends EntityDispatchAction2
{
  private static final Log _log = LogFactory.getLog(UserDispatchAction.class); // 20031209 DDJ

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_server";
  }

  private static final Number[] _userFields =     {
                                        IGTUserEntity.EMAIL,
                                        IGTUserEntity.PHONE,
                                        IGTUserEntity.PROPERTY,
                                        IGTUserEntity.USER_ID,
                                        IGTUserEntity.USER_NAME,
                                      };

  private static final Number[] _profileFields =  {
                                        IGTAccountStateEntity.BAD_LOGIN_ATTEMPTS,
                                        IGTAccountStateEntity.CREATED,
                                        IGTAccountStateEntity.CREATED_BY,
                                        IGTAccountStateEntity.FREEZE_TIME,
                                        IGTAccountStateEntity.LAST_LOGIN_TIME,
                                        IGTAccountStateEntity.LAST_LOGOUT_TIME,
                                        IGTAccountStateEntity.STATE,
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
    return new UserRenderer(rContext, edit);
  }

  protected String getNavigatorDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return null;
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.USER_UPDATE : IDocumentKeys.USER_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    UserAForm form = (UserAForm)actionContext.getActionForm();
    IGTUserEntity user = (IGTUserEntity)entity;
    IGTAccountStateEntity profile = (IGTAccountStateEntity)entity.getFieldValue(IGTUserEntity.PROFILE);

    initFormFields(actionContext, _userFields);
    initFormFields(actionContext, profile, _profileFields);

    boolean frozen = false;
    if(!entity.isNewEntity())
    {
      frozen = ((Boolean)profile.getFieldValue(profile.FROZEN)).booleanValue();
    }
    form.setFrozen("" + frozen);

    form.setPassword("");
    form.setNewPassword("");

    Collection roles = (Collection)user.getFieldValue(user.ROLES);
    String[] rolesArray = StaticUtils.getStringArray(roles);
    form.setRoles(rolesArray);

    Collection bes = (Collection)user.getFieldValue(user.BES);
    String[] besArray = StaticUtils.getStringArray(bes);
    form.setBes(besArray);
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new UserAForm();
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
    //20030106AH - Changed to use non-deprecated basicValidateString, cast entity and use the
    //fieldkeys from the instance rather then the interface, and to cast the form (renaming the
    //param as actionForm) and to use the EntityFieldValidator addFieldError() method
    UserAForm form = (UserAForm)actionForm; //20030106AH
    IGTUserEntity user = (IGTUserEntity)entity; //20030106AH
    IGTAccountStateEntity profile = (IGTAccountStateEntity)user.getFieldValue(user.PROFILE); //20030106AH

    String password = form.getPassword(); //20030106AH
    if(password == null) password = "";
    String newPassword = form.getNewPassword(); //20030106AH
    if(newPassword == null) newPassword = "";

    String userType = user.getType(); //20030106AH
    if( entity.isNewEntity() || (!password.equals("")) )
    {
      // If its a new user or password field had data then we need to check the two are the same!
      // For new users its mandatory!
      if(entity.isNewEntity() && password.equals(""))
      {
        EntityFieldValidator.addFieldError( actionErrors,"password",userType,
                                            EntityFieldValidator.REQUIRED,null); //20030106AH
      }
      if(entity.isNewEntity() && password.equals(""))
      {
        EntityFieldValidator.addFieldError( actionErrors,"newPassword",userType,
                                            EntityFieldValidator.REQUIRED,null); //20030106AH
      }
    }
    if( (!password.equals("")) || (!newPassword.equals("")) )
    {
      if(!password.equals(newPassword))
      {
        EntityFieldValidator.addFieldError( actionErrors,"newPassword",userType,
                                            EntityFieldValidator.MISMATCH,null); //20030106AH
      }
    }

    basicValidateString(actionErrors, user.NEW_PASSWORD, form, user);//20090416 TWX #146
    
    basicValidateString(actionErrors, user.USER_ID, form, user); //20030106AH
    basicValidateString(actionErrors, user.USER_NAME, form, user); //20030106AH
    basicValidateString(actionErrors, user.PHONE, form, user); //20030106AH
    basicValidateString(actionErrors, user.EMAIL, form, user); //20030106AH
    basicValidateString(actionErrors, user.PROPERTY, form, user); //20030106AH

    if(!entity.isNewEntity())
    {
      basicValidateString(actionErrors, profile.STATE,form,profile); //20030106AH
      basicValidateString(actionErrors, profile.FROZEN,form,profile); //20030106AH
    }
  }

  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    UserAForm userAForm = (UserAForm)actionContext.getActionForm();
    IGTUserEntity user = (IGTUserEntity)entity;

    user.setFieldValue(IGTUserEntity.USER_ID, userAForm.getUserId());
    user.setFieldValue(IGTUserEntity.USER_NAME, userAForm.getUserName());
    user.setFieldValue(IGTUserEntity.PASSWORD, userAForm.getPassword());
    user.setFieldValue(IGTUserEntity.NEW_PASSWORD, userAForm.getNewPassword());
    user.setFieldValue(IGTUserEntity.PHONE, userAForm.getPhone());
    user.setFieldValue(IGTUserEntity.EMAIL, userAForm.getEmail());
    user.setFieldValue(IGTUserEntity.PROPERTY, userAForm.getProperty());

    String[] besArray = userAForm.getBes();
    Collection bes = StaticUtils.getLongCollection(besArray);
    user.setFieldValue(user.BES, bes);

    String[] rolesArray = userAForm.getRoles();
    Collection roles = StaticUtils.getLongCollection(rolesArray);
    user.setFieldValue(user.ROLES, roles);

    //nb: we dont update modified profile fields here. saveWithManager has been overriden to
    //send special events based on comparison of values in entity and form to see if things
    //were changed
  }

  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
  {
    // We override saveWithManager as user has special stuff to save...
    UserAForm userAForm = (UserAForm)actionContext.getActionForm();
    IGTUserEntity user = (IGTUserEntity)entity;

    if(entity.isNewEntity())
    {
      manager.create(entity);
    }
    else
    {
      UserAdminOptions usrOpts = new UserAdminOptions();
      IGTAccountStateEntity profile = (IGTAccountStateEntity)user.getFieldValue(IGTUserEntity.PROFILE);
      boolean wasFrozen = false;
      boolean isFrozen = false;
      try
      {
        wasFrozen = ((Boolean)profile.getFieldValue(profile.FROZEN)).booleanValue();
        String ff = userAForm.getFrozen();
        if(ff == null) ff = "false";
        if(ff.equals("false"))
        {
          isFrozen = false;
        }
        else
        {
          isFrozen = true;
        }
      }
      catch(Exception bob)
      {
        throw new GTClientException("Error determining if frozen status changed", bob);
      }
      if(isFrozen != wasFrozen)
      {
        usrOpts.setUnfreezeAccount(!isFrozen);
      }
      try
      {
        Number badLoginsObject = (Number)profile.getFieldValue(profile.BAD_LOGIN_ATTEMPTS);
        int badLogins = badLoginsObject.intValue();
        if( (badLogins > 0) && (userAForm.getLoginAttempts().equals("0")))
        {
          usrOpts.setResetBadLoginAttempts(true);
        }
      }
      catch(Exception e1)
      {
        if(_log.isErrorEnabled())
        {
          _log.error("Unable to evaluate is reset bad login rqd due to exception!", e1);
        }
      }
      if(userAForm.getPassword() != null)
      {
        if(!userAForm.getPassword().equals(""))
        {
          usrOpts.setNewPassword(userAForm.getPassword());
        }
      }
      try
      {
        short state = Short.parseShort(userAForm.getState());
        if(state != ((Short)profile.getFieldValue(profile.STATE)).shortValue())
        {
          usrOpts.setState(state);
        }
      }
      catch(Exception z)
      {
        z.printStackTrace();
      }
      ((IGTUserManager)manager).update(user, usrOpts);
    }
    /*20021030AH - commenting out the following as the managers should now
    be updating the entity with data send by GTAS in the response
    if(user.isNewEntity())
    {
      String userId = user.getFieldString(IGTUserEntity.USER_ID);
      // Lookup user again if it was new so we can have its uid for adding roles
      user = (IGTUserEntity)((IGTUserManager)manager).getUserByID(userId);
    }*/
  }
}