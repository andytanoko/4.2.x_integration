/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDispatchAction2.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-18     Andrew Hill         Created
 * 2002-??-??     Andrew Hill         Lots of stuff!
 * 2002-10-25     Andrew Hill         Modify exception handling
 * 2003-05-22     Andrew Hill         Support for RETURN_TO_VIEW_FLAG
 */
package com.gridnode.gtas.client.web.strutsbase;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.ResponseException;
import com.gridnode.gtas.client.web.IRequestKeys;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;

// Further abstraction of the EntityDispatchAction
public abstract class EntityDispatchAction2 extends EntityDispatchAction
{
  private static final String RETURN_TO_VIEW_FLAG = "com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2.returnToView"; //20030522AH
  
  protected abstract String getEntityName();

  protected abstract IDocumentRenderer getFormRenderer( ActionContext actionContext,
                                                        RenderingContext rContext,
                                                        boolean edit)
    throws GTClientException;

  protected abstract String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException;

  //@todo: create a default implementation that uses struts-config setting and reflection
  protected abstract ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException;

  /**
   * Subclasses should override this method to perform validation of input fields.
   * If a problem is found the method should add to actionErrors. If there are any actionErrors
   * after the method has completed, then the submission is deemed to have failed validation and
   * the user will be returned to the form to view the errors.
   * @param actionContext
   * @param form
   * @param actionErrors
   * @throws GTClientException
   */
  protected abstract void validateActionForm( ActionContext actionContext,
                                              IGTEntity entity,
                                              ActionForm form,
                                              ActionErrors actionErrors)
    throws GTClientException;

  /**
   * Subclasses should override this method to update the fields in the entity with the data
   * submitted to the actionform.
   * @param actionCOntext
   * @param entity
   * @throws GTClientException
   */
  protected abstract void updateEntityFields( ActionContext actionContext,
                                              IGTEntity entity)
    throws GTClientException;

  /**
   * An implementation of the doSave() method in EntityDispatchAction that factors out
   * some common methodology. This method will call the following methods in this order:
   * validateActionForm
   * updateEntityFields
   * handleSaveException (only if an exception is thrown using the manager to save)
   * @param actionContext
   * @param actionErrors
   * @return flag of true if validation failed
   */
  protected boolean doSave(ActionContext actionContext, ActionErrors actionErrors)
    throws GTClientException
  {
    IGTEntity entity = getEntity(actionContext);

    try
    {
      validateActionForm(actionContext, entity, actionContext.getActionForm(), actionErrors);
    }
    catch(Throwable validationExc) //20030425AH Catch Throwable not GTClientException
    {
      throw new GTClientException("Error validating submitted parameters", validationExc);
    }

    boolean failed = (actionErrors.size() != 0);
    if(!failed)
    {
      try
      {
        updateEntityFields(actionContext, entity);
      }
      catch(Exception updateExc)
      {
        throw new GTClientException("Error updating fields of entity:" + entity,updateExc);
      }
      int mType = getIGTManagerType(actionContext);
      IGTSession gtasSession = null;
      IGTManager manager = null;
      try
      {
        gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
        manager = gtasSession.getManager(mType);
      }
      catch(Exception mgrExc)
      {
        throw new GTClientException("Error obtaining manager of type " + mType
                                    + " from IGTSession " + gtasSession,mgrExc);
      }
      try
      {
        saveWithManager(actionContext, manager, entity); //20030522AH
      }
      catch(Throwable saveExc)
      {
        if(!handleSaveException(saveExc, actionContext, manager, entity, actionErrors))
        {
          throw new GTClientException("Error saving entity:"+entity,saveExc);
        }
        else
        {
          return true;
        }
      }
    }
    //20030522AH - It is now possible to cause a return to the view in similar manner
    //as if the validation failed or an exception was handled by storing Boolean.TRUE
    //in the ActionContext under the key RETURN_TO_VIEW_FLAG which we check now.
    return isReturnToView(actionContext) || failed;
    //...
  }


  /**
   * @param exception
   * @param actionContext
   * @param manager
   * @param entity
   * @param actionErrors
   * @return true to return to the view, false to throw exception
   * @throws GTClientException
   */
  protected boolean handleSaveException(Throwable saveException,
                                        ActionContext actionContext,
                                        IGTManager manager,
                                        IGTEntity entity,
                                        ActionErrors actionErrors)
    throws GTClientException
  {
    actionContext.getRequest().setAttribute(IRequestKeys.SAVE_EXCEPTION, saveException);
    if(saveException instanceof GTClientException)
    {
      Throwable rootException = ((GTClientException)saveException).getRootException();
      actionContext.getRequest().setAttribute(IRequestKeys.SAVE_ROOT_EXCEPTION, rootException);
      if(rootException instanceof ResponseException)
      {
        ResponseException e = (ResponseException)rootException;
        ActionError error = getActionError(e);
        if(error != null)
        {
          actionErrors.add(getErrorField(e) , error );
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Will call update or create in the manager passing the entity.
   * Subclass may override this if other methods also need to be used to perform the save.
   * 20030522AH - You can now request the save method to return false (like it does for
   * validation errors) by calling setReturnToView() with a true value.
   * @param actionContext
   * @param manager
   * @param entity
   * @throws GTClientException
   */
  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
    throws GTClientException
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
  
  /*
   * This may be set during execution of the save method (for example inside saveWithManager)
   * to force a return to the view.
   * @param actionContext
   * @param rtv Set true to force a return to view, false for normal handling
   */
  protected final void setReturnToView(ActionContext actionContext, boolean rtv)
  { //20030522AH
    if (actionContext == null)
      throw new NullPointerException("actionContext is null");
    actionContext.setAttribute(RETURN_TO_VIEW_FLAG, new Boolean(rtv));
  }
  
  protected final boolean isReturnToView(ActionContext actionContext)
  { //20030522AH
    if (actionContext == null)
      throw new NullPointerException("actionContext is null");
    Boolean rtvf = (Boolean)actionContext.getAttribute(RETURN_TO_VIEW_FLAG);
    return Boolean.TRUE.equals(rtvf);
  }
}