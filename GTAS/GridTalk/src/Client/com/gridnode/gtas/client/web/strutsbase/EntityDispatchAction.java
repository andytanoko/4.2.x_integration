/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-15     Andrew Hill         Created
 * 2002-07-03     Andrew Hill         OperationContext stacking support
 * 2002-07-06     Andrew Hill         Added Elv diversion support
 * 2002-08-28     Andrew Hill         Factor out some core functionality to OperationDispatchAction
 * 2002-10-03     Daniel D'Cotta      Modified transferFieldFiles() to handle file in B-Tier
 * 2002-10-29     Andrew Hill         Modify entry methods to throw GTClientException
 * 2002-11-03     Andrew Hill         reorderListInEntityField()
 * 2002-12-20     Andrew Hill         return true from validateXXX if ok, false if failed validation
 * 2003-03-12     Andrew Hill         Check if in detail view and set flag
 * 2003-03-19     Andrew Hill         Override forceUseDvTemplate() to return true for edas
 * 2003-03-21     Andrew Hill         Listview opcon support for delete
 * 2003-04-10     Andrew Hill         Split up some parts of delete method to aid overriding subclasses
 * 2003-05-22     Andrew Hill         getNewEntityInstance() hook
 * 2003-06-02     Andrew Hill         Repasted getNewEntityInstance() hook as it had been lost by ClearCase
 * 2003-06-26     Andrew Hill         Override getResumeForward() to provide support for returning in view mode
 *                                    Also added warning message to deprecated checkReloadActionForm()
 * 2003-07-23     Andrew Hill         Modify delete() method for deleteException handling etc...
 * 2003-08-22     Andrew Hill         Override getDivertForward() to support field based diversions
 * 2003-09-18     Daniel D'Cotta      Added basicValidatStringArray()
 * 2004-02-11     Andrew Hill         Added basicValidateUrl()
 * 2005-03-17     Andrew Hill         Made getRequestedEntity() no longer final
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.IOperationContextKeys;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;

public abstract class EntityDispatchAction extends TaskDispatchAction
{
  public static final String LISTVIEW_FORWARD = "listview"; //20030321AH
  public static final String IS_NOT_ELV_DIVERT = "isNotElvD"; //20030321AH
  public static final String REFRESH_LISTVIEW_URL = "refreshListview"; //20030321AH fieldname for refreshUrl

  protected static final String VIEW_MODE_ATTRIBUTE = "isViewMode"; //20030626AH

  protected abstract String getEntityName();

  protected abstract void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException;

  protected abstract int getIGTManagerType(ActionContext actionContext)
    throws GTClientException;

  protected abstract boolean doSave(ActionContext actionContext, ActionErrors errors)
    throws GTClientException;

  protected ActionForward getResumeForward( ActionContext actionContext,
                                              ActionForm actionForm)
      throws GTClientException
  { //20030626AH
    ActionForward resume = null;
    OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
    Boolean vma = (Boolean)opCon.getAttribute(VIEW_MODE_ATTRIBUTE);
    if(Boolean.TRUE.equals(vma))
    {
      resume = actionContext.getMapping().findForward(RESUME_VIEW_MAPPING);
      if( resume == null )
      { //A small 'hack' to save us having to edit all the existing entries for now
        resume = super.getResumeForward(actionContext, actionForm);
        if(resume != null)
        {
          String newPath = StaticUtils.replaceSubstring(resume.getPath(), "method=update", "method=view");
          resume = new ActionForward(newPath, resume.getRedirect() );
        }
      }
    }
    else
    {
      resume = super.getResumeForward(actionContext, actionForm);
    }
    return resume;
  }

  /**
   * Set up the necessary parameters and forward to the view to display an entity.
   */
  public ActionForward view(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws IOException, ServletException, GTClientException
  {
    ActionContext actionContext = new ActionContext(mapping, actionForm, request, response);
    try
    {
      OperationContext opCon = prepareOperationContext(actionContext);

      opCon.setAttribute(VIEW_MODE_ATTRIBUTE, Boolean.TRUE); //20030626AH

      checkDetailView(actionContext); //20030312AH
      performViewProcessing(actionContext);
      prepareView(actionContext, opCon, false);
      return mapping.findForward(IGlobals.VIEW_FORWARD);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error preparing entity view",t);
    }
  }

  /**
   * By passing a parameter named "reloadActionForm" with a value of "true" the actionForm
   * will be reinitialised from the entity when an update request occurs.
   * This is a workaround for moving from the view to edit mode for a screen with checkboxes
   * where the fields in the actionForm are reset, but parameters for the checkboxes are
   * not actually passed in. (For such screens add an appropriate hidden field to the form).
   * @deprecated the new reset functionality fixes the problem this is a workaround for
   */
  protected void checkReloadActionForm(ActionContext actionContext, OperationContext opCon)
    throws GTClientException
  {
    String reloadActionForm = actionContext.getRequest().getParameter("reloadActionForm");
    if("true".equals(reloadActionForm))
    {
      Log log = actionContext.getLog();
      if(log.isWarnEnabled())
      { //20030626AH
        log.warn("Deprecated checkReloadActionForm() code was invoked by " + this);
      }
      IGTEntity entity = getEntity(actionContext);
      if(entity != null)
      {
        initialiseActionForm(actionContext,entity);
      }
      else
      {
        throw new GTClientException("Cannot reinitialise ActionForm from entity. Entity not found");
      }
    }
  }

  /**
   * Subclass may override to provide custom processing for an update operation before
   * preparing the view renderers occurs.
   */
  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    
  }

  /**
   * Subclass may override to provide custom processing before save occurs
   */
  protected void performPreSaveProcessing(ActionContext actionContext)
    throws GTClientException
  {
    
  }

  /**
   * Subclass may override to provide custom processing for a view operation before
   * preparing the view renderers occurs.
   */
  protected void performViewProcessing(ActionContext actionContext)
    throws GTClientException
  {
    
  }

  protected String[] getDeleteIds(ActionContext actionContext) throws GTClientException
  { //20030410AH
    try
    {
      HttpServletRequest request = actionContext.getRequest();
      //nb: as we may have got here through a divert from an elv, the request params
      // may have a lot of things not relevant to us. (non-redirecting request to facilitate
      // passing the multiple fuid values).
      String[] uids = null;

      boolean isNotElvD = StaticUtils.primitiveBooleanValue( request.getParameter(IS_NOT_ELV_DIVERT) );
      OperationContext opCon = OperationContext.getOperationContext(request);
      if(opCon != null)
      {
        if(isNotElvD) //Listviews now can have opCon too. Check that this isnt an elv divert
        {
          uids = request.getParameterValues("uid");
        }
        else
        {
          OperationContext previousOC = opCon.getPreviousContext();
          if(previousOC != null)
          {
            uids = request.getParameterValues("fuid");
          }
        }
      }
      else
      {
        uids = request.getParameterValues("uid");
      }
      //..........
      return uids;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting delete ids",t);
    }
  }

  protected ActionForward getDeleteReturnForward(ActionContext actionContext) throws GTClientException
  { //20030410AH
    try
    {
      HttpServletRequest request = actionContext.getRequest();
      String refreshUrl = request.getParameter(REFRESH_LISTVIEW_URL); //20030321AH
      ActionForward forward = StaticUtils.stringNotEmpty(refreshUrl) ? null : actionContext.getMapping().findForward(LISTVIEW_FORWARD); //20030321AH

      boolean isNotElvD = StaticUtils.primitiveBooleanValue( request.getParameter(IS_NOT_ELV_DIVERT) );
      OperationContext opCon = OperationContext.getOperationContext(request);
      if(opCon != null)
      {
        if(isNotElvD) //Listviews now can have opCon too. Check that this isnt an elv divert
        { //20030321AH - nb: I decided for now not to use processSOCForward
          String path = (forward == null) ? refreshUrl : forward.getPath(); //20030321AH
          path = StaticWebUtils.addParameterToURL(path,
                                                         OperationContext.ID_PARAMETER,
                                                         opCon.getOperationContextId());
          path = StaticWebUtils.addParameterToURL(path,EntityListAction.REFRESH,"true");
          forward = new ActionForward(path,true);
        }
        else
        {
          // Following code allows transparent diverting to a delete and resumption of
          // an operation thereafter. (For use by elv)
          OperationContext previousOC = opCon.popOperationContext();
          if(previousOC != null)
          {
            //processPopOpCon(actionContext, opCon, previousOC);
            OperationContext.saveOperationContext(previousOC, request);
            forward = processSOCForward( previousOC.getResumeForward(), previousOC );
          }
        }
      }
      return forward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting delete return forward",t);
    }
  }

  public final ActionForward delete(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, GTClientException
  {
    ActionContext actionContext = new ActionContext(mapping,actionForm,request,response); //20030310AH
    ActionForward forward = null; //20030723AH
    //Getting the forward also involves popping the opCon if necessary and thus must be
    //left till after doDelete() is called.
    try
    {
      doDelete(actionContext); //20030709AH
      forward = getDeleteReturnForward(actionContext); //20030410AH, 20030723AH - Moved to after delete
    }
    catch(Throwable t)
    { //20030709AH
      //20030724AH - Catch and detect type - some overidden subclass doDelete wrap the error!
      if(t instanceof GTClientException)
      {
        Throwable rootEx = ((GTClientException)t).getRootException();
        if(rootEx instanceof DeleteException) 
        { 
          DeleteException deleteException = (DeleteException)rootEx;
          forward = getDeleteReturnForward(actionContext); //20030723AH
          String key = GTRequestProcessor.getInlineExceptionKey(request,true);
          request.getSession().setAttribute( key, deleteException );
          String url = StaticWebUtils.addParameterToURL(forward.getPath(), GTRequestProcessor.INLINE_EXCEPTION, key);
          forward = new ActionForward(url, forward.getRedirect()); 
          return forward;
        }
      }
      throw new GTClientException("Error in delete operation",t);
    }
    return forward;
  }
  
  public void doDelete(ActionContext actionContext)
    throws GTClientException
  { //20030709AH
    String[] uids = getDeleteIds(actionContext); //20030310AH
    if( !((uids == null) || (uids.length == 0)) )
    {
      IGTSession gtasSession = getGridTalkSession(actionContext);
      IGTManager manager = gtasSession.getManager(getIGTManagerType(actionContext));

      long[] uidLongs = StaticUtils.primitiveLongArrayValue(uids, true); //20030410AH, 20030424AH
      manager.delete(uidLongs); //20030410AH
    }
  }

  /**
   * Performs rudimentary string field validation checking for the presence of data in
   * fields that are mandatory and the maximum length of the field.
   */
  protected boolean basicValidateString(ActionErrors actionErrors,
                                      String field,
                                      String value,
                                      Number entityField,
                                      IGTEntity entity)
    throws GTClientException
  {
    //20021220AH - return value now
    return EntityFieldValidator.basicValidateString(actionErrors,field,value,entityField,entity);
  }

  protected boolean basicValidateStringArray( ActionErrors actionErrors,
                                              String field,
                                              String[] value,
                                              Number entityField,
                                              IGTEntity entity)
    throws GTClientException
  {
    return EntityFieldValidator.basicValidateStringArray(actionErrors, field, value, entityField, entity);
  }

  protected boolean basicValidateFiles( ActionErrors actionErrors,
                                     Number fieldId,
                                     ActionForm form,
                                     IGTEntity entity)
    throws GTClientException
  {
    //20021220AH - return value now
    return EntityFieldValidator.basicValidateFiles(actionErrors,fieldId,form,entity,null); //20030410AH
  }

  protected boolean basicValidateFiles( ActionErrors actionErrors,
                                     Number fieldId,
                                     ActionForm form,
                                     IGTEntity entity,
                                     String[] extensions)
    throws GTClientException
  { //20030410AH
    return EntityFieldValidator.basicValidateFiles(actionErrors,fieldId,form,entity,extensions);
  }

  protected boolean basicValidateString( ActionErrors actionErrors,
                                      Number fieldId,
                                      ActionForm form,
                                      IGTEntity entity )
    throws GTClientException
  {
    //20021220AH - return value now
    return EntityFieldValidator.basicValidateString(actionErrors,fieldId,form,entity);
  }

  protected boolean basicValidateUrl( ActionErrors actionErrors,
                                        Number fieldId,
                                        ActionForm form,
                                        IGTEntity entity )
  throws GTClientException
  { //20040211AH
    return EntityFieldValidator.basicValidateUrl(actionErrors,fieldId,form,entity);
  }

  protected boolean basicValidateStringArray( ActionErrors actionErrors,
                                              Number fieldId,
                                              ActionForm form,
                                              IGTEntity entity )
    throws GTClientException
  {
    return EntityFieldValidator.basicValidateStringArray(actionErrors,fieldId,form,entity);
  }

  public boolean basicValidateKeys( ActionErrors actionErrors,
                            Number fieldId,
                            ActionForm form,
                            IGTEntity entity )
    throws GTClientException
  {
    try
    {
      String fieldName = entity.getFieldName(fieldId);
      String[] value = (String[])PropertyUtils.getSimpleProperty(form, fieldName);
      //20021220AH - return value now
      return EntityFieldValidator.validateKeys(actionErrors, fieldName, value, fieldId, entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("Error validating keys in field:" + fieldId,e);
    }
  }

  /**
   * Convienience method to validate string value in ActionForm against entity metainfo.
   * This method has more overhead than its equivalents with more parameters. The OperationContext
   * is used to obtain the entity.
   * @param actionErrors
   * @param fieldId
   * @param actionContext
   * @throws GTClientException
   * @deprecated - use the version that takes entity and form as parameters
   */
  protected boolean basicValidateString( ActionErrors actionErrors,
                                      Number fieldId,
                                      ActionContext actionContext)
    throws GTClientException
  {
    try
    {
      IGTEntity entity = getEntity(actionContext);
      //20021220AH - return value now
      return EntityFieldValidator.basicValidateString(actionErrors,fieldId,actionContext.getActionForm(), entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("Error validating field:" + fieldId,e);
    }
  }

  /**
   * When creating a new entity this method provides a hook that subclasses may override
   * to perform custom initialisation.
   * @param actionContext
   * @param entity
   * @throws GTClientException
   */
  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    
  }

  protected Object convertLinkKey(ActionContext actionContext,
                                  Number keyFieldId,
                                  String keyField,
                                  String keyString,
                                  IGTManager manager,
                                  String entityType,
                                  IGTFieldMetaInfo fmi)
    throws GTClientException
  { //20030106AH
    String valueClass = fmi.getValueClass();
    try
    {
      if("java.lang.String".equals(valueClass))
      {
        return keyString;
      }
      else if("java.lang.StringBuffer".equals(valueClass))
      {
        return new StringBuffer(keyString);
      }
      else if("java.lang.Short".equals(valueClass))
      {
        return StaticUtils.shortValue(keyString);
      }
      else if("java.lang.Integer".equals(keyString))
      {
        return StaticUtils.integerValue(keyString);
      }
      else if("java.lang.Long".equals(keyString))
      {
        return StaticUtils.longValue(keyString);
      }
      else if("java.lang.Boolean".equals(keyString))
      {
        return StaticUtils.booleanValue(keyString);
      }
      else
      {
        throw new java.lang.UnsupportedOperationException(
          "Automatic conversion from string to "
          + valueClass
          + " for is not supported by default convertLinkKey() method");
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException(
        "convertLinkKey() was unable to convert key for keyField '"
        + keyField
        + "' with value of '"
        + keyString + "' to object of type "
        + valueClass,t);
    }
  }

  /**
   * Where multiple entities match the link conditions it is up to the subclass to override this
   * method and choose the appropriate entity from the collection to return.
   * This default implementation will throw an UnsupportedOperationException.
   */
  protected IGTEntity selectRequestedEntity(ActionContext actionContext,
                                                  Collection candidates,
                                                  Number keyFieldId,
                                                  Object convertedKey,
                                                  String keyField,
                                                  String keyString,
                                                  IGTManager manager)
    throws GTClientException
  { //20030106AH
    throw new java.lang.UnsupportedOperationException(
      "selectRequestedEntity() has not been implemented for keyField '"
      + keyField
      + "' with value of '"
      + keyString + "'");
  }

  /*
   * Subclasses that have to do something wierd to get an entity instance may override this
   * method to do so. It will be called by getRequestedEntity() when a new entity is
   * requestec.
   * @param actionContext
   * @param manager
   * @return entity
   * @throws GTClientException
   */
  protected IGTEntity getNewEntityInstance( ActionContext actionContext,
                                            IGTManager manager)
    throws GTClientException
  { //20030522AH
    //20030602AH - Pasted in again as it seems to have been lost in the ClearCase merges!
    return manager.newEntity();
  }

  /**
   * Based on the request parameters passed will determine the appropriate method
   * to use to retreieve or create the entity instance and will then do so
   * @param actionContext
   * @return entity The entity being viwed,created,or edited
   * @throws GTClientException
   */
  protected IGTEntity getRequestedEntity(ActionContext actionContext)
    throws GTClientException
  { //20030106AH
    //20030602AH - Pasted in again as it seems to have been lost in the ClearCase merges!
    //20050317AH - no longer final
    try
    {
      IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
      IGTManager manager = gtasSession.getManager(getIGTManagerType(actionContext));
      IGTEntity entity = null;
      String keyField = actionContext.getRequest().getParameter("keyField");
      if(!StaticUtils.stringNotEmpty(keyField))
      {
        keyField = "uid";
      }
      String keyString = actionContext.getRequest().getParameter(keyField);
      if( "new".equals(keyString) && "uid".equals(keyField) )
      {
        try
        { //20030522AH
          entity = getNewEntityInstance(actionContext, manager);
        }
        catch(Throwable t)
        {
          throw new GTClientException("Unable to obtain new entity instance",t);
        }
        initialiseNewEntity(actionContext, entity);
      }
      else if(keyString != null)
      {
        if("uid".equals(keyField))
        {
          try
          {
            long uid = Long.parseLong(keyString);
            entity = manager.getByUid(uid); //20030522AH - Call the non-deprecated spelling
            if(entity == null)
            {
              throw new GTClientException("Entity with uid=" + uid +
                " not found by manager " + manager);
            }
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error getting requested entity by uid with value of '"
                                        + keyString + "'",t);
          }
        }
        else
        {
          try
          {
            String entityType = getEntityName();
            Number keyFieldId = manager.getFieldId(entityType,keyField);
            IGTFieldMetaInfo fmi = manager.getSharedFieldMetaInfo(entityType,keyFieldId);
            switch(fmi.getConstraintType())
            {
              case IConstraint.TYPE_DYNAMIC_ENTITY:
                throw new java.lang.UnsupportedOperationException(
                  "Dynamic entities are not supported as link fields"
                  +" by the getRequestedEntity() method");
              case IConstraint.TYPE_FOREIGN_ENTITY:
                throw new java.lang.UnsupportedOperationException(
                  "Foreign entities are not supported as link fields"
                  +" by the getRequestedEntity() method");
              case IConstraint.TYPE_LOCAL_ENTITY:
                throw new java.lang.UnsupportedOperationException(
                  "Embedded entities are not supported as link fields"
                  +" by the getRequestedEntity() method");
            }
            Object convertedKey = convertLinkKey( actionContext,
                                                  keyFieldId,
                                                  keyField,
                                                  keyString,
                                                  manager,
                                                  entityType,
                                                  fmi);
            Collection candidates = manager.getByKey(convertedKey, keyFieldId);
            if( (candidates == null) || candidates.isEmpty() )
            {
              throw new GTClientException("No entities of type '"
                                          + entityType
                                          + "' found where keyField '"
                                          + keyField
                                          + "' has value of '"
                                          + keyString
                                          + "'");
            }
            else
            {
              int numberOfCandidates = candidates.size();
              if(numberOfCandidates > 1)
              {
                try
                {
                  entity = selectRequestedEntity( actionContext,
                                                  candidates,
                                                  keyFieldId,
                                                  convertedKey,
                                                  keyField,
                                                  keyString,
                                                  manager);
                  if(entity == null)
                  {
                    throw new java.lang.NullPointerException(
                      "Requested entity not found by selectRequestedEntity() method");
                  }
                }
                catch(Throwable t)
                {
                  throw new GTClientException("Unable to determine requested entity from choice of "
                                              + numberOfCandidates + " matching candidates",t);
                }
              }
              else
              {
                entity = (IGTEntity)StaticUtils.getFirst(candidates);
              }
            }
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error getting requested entity where keyField '"
                                        + keyField + "' has value '" + keyString + "'",t);
          }
        }
      }
      else
      {
        throw new GTClientException("No entity specified by request, keyField=" + keyField);
      }
      if(entity == null)
      { //Shouldnt be possible (unfound ones should be handled already)
        throw new java.lang.NullPointerException("Internal assertion failure - null entity");
      }
      return entity;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting requested IGTEntity instance",t);
    }
  }


  protected void processPreparedOperationContext( ActionContext actionContext,
                                                  OperationContext opContext)
    throws GTClientException
  {
    IGTEntity entity = getRequestedEntity(actionContext); //20030106AH
    opContext.setAttribute(IOperationContextKeys.ENTITY, entity);
    ActionForward submitForward = actionContext.getMapping().findForward("submit");
    opContext.setAttribute(IOperationContextKeys.FORM_SUBMIT_URL, submitForward.getPath());
  }

  public ActionForward save(ActionMapping mapping, ActionForm actionForm,
                            HttpServletRequest request, HttpServletResponse response)
                            throws IOException, ServletException, GTClientException
  {
    try
    {
      ActionContext wastedAC = new ActionContext(mapping, actionForm, request, response);
      if(getEntity(wastedAC).canEdit())
      {
        return complete(mapping,actionForm,request,response);
      }
      else
      {
        throw new java.lang.IllegalStateException("Entity may not be modified");
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error saving entity",t);
    }
  }

  protected IGTEntity getEntity(ActionContext actionContext, OperationContext opContext)
    throws GTClientException
  { //20030821AH - new signature
    if(opContext == null)
    {
      opContext = OperationContext.getOperationContext(actionContext.getRequest());
      if(opContext == null)
      {
        throw new java.lang.NullPointerException("No OperationContext found");
      }
    }
    IGTEntity entity = (IGTEntity)opContext.getAttribute(IOperationContextKeys.ENTITY);
    if(entity == null)
    {
      throw new java.lang.NullPointerException("No entity found in Operation Context");
    }
    return entity;
  }
  
  protected IGTEntity getEntity(ActionContext actionContext)
    throws GTClientException
  { //20030821 - Factored out code
    return getEntity(actionContext, null);
  }

  /**
   * Copy the values from the specified fields in the entity to the actionForm by using the
   * appropriate set methods.
   * @param actionContext
   * @param fieldIds[] array of field ids in the entity
   * @throws GTClientException
   */
  protected void initFormFields(ActionContext actionContext, Number[] fieldIds)
    throws GTClientException
  {
    initFormFields(actionContext, getEntity(actionContext), fieldIds);
  }

  /**
   * Copy the values from the specified fields in the entity to the actionForm by using the
   * appropriate set methods.
   * @param actionContext
   * @param entity
   * @param fieldIds[] array of field ids in the specified entity
   * @throws GTClientException
   */
  protected void initFormFields(ActionContext actionContext, IGTEntity entity, Number[] fieldIds)
    throws GTClientException
  {
    for(int i=0; i < fieldIds.length; i++)
    {
      initFormField(actionContext, entity, fieldIds[i]);
    }
  }

  /**
   * Copy the value from the specified field in the entity to the actionForm by using the
   * appropriate set method.
   * @param actionContext
   * @param fieldIds field id in the entity
   * @throws GTClientException
   */
  protected void initFormField(ActionContext actionContext, Number fieldId)
    throws GTClientException
  {
    initFormField(actionContext, getEntity(actionContext), fieldId);
  }

  /**
   * Copy the value from the specified field in the entity to the actionForm by using the
   * appropriate set method.
   * @param actionContext
   * @param entity
   * @param fieldId field id in the specified entity
   * @throws GTClientException
   */
  protected void initFormField(ActionContext actionContext, IGTEntity entity, Number fieldId)
    throws GTClientException
  {
    try
    {
      PropertyUtils.setSimpleProperty(actionContext.getActionForm(),
                                      entity.getFieldName(fieldId),
                                      entity.getFieldString(fieldId));
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error initialising form for field "
                          + fieldId + " from entity " + entity,t);
    }
  }

  protected ActionForward performSaveForwardProcessing(ActionContext actionContext,
                                                       OperationContext completedOpCon,
                                                       ActionForward forward)
    throws GTClientException
  {
    return forward;
  }

  protected boolean transferFieldFiles(ActionContext actionContext, IGTEntity entity, Number fieldId, boolean saveFullPath)
    throws GTClientException
  { //20030123AH - Mod to return boolean if any files were actually transferred
    // nb: does not call destroy on the FormFile. You need to do this yourself when you are finished
    // with it.
    // 20030725 DDJ: Remember to change SaveGridDocumentAction implementation if this changes
    try
    {
      if(actionContext == null) throw new NullPointerException("actionContext is null"); //20030416AH
      if(entity == null) throw new NullPointerException("entity is null"); //20030416AH
      if(fieldId == null) throw new NullPointerException("fieldId is null"); //20030416AH

      ActionForm form = actionContext.getActionForm();
     if(form == null) throw new NullPointerException("form is null"); //20030416AH
      if(!(form instanceof GTActionFormBase))
      {
        throw new java.lang.UnsupportedOperationException("Transfer of files from field using "
                  + "forms of class '" + form.getClass().getName()
                  + "' (not a subclass of GTActionFormBase) is not supported");
      }
      GTActionFormBase formBase = (GTActionFormBase)form;

      IGTFieldMetaInfo fmi = entity.getFieldMetaInfo(fieldId);
      if(fmi.getConstraintType() != IConstraint.TYPE_FILE)
      {
        throw new java.lang.IllegalArgumentException("Field '" + fmi.getFieldName() + "'("
                                                + fieldId + ") - constraint is not of type File");
      }

      String fieldName = fmi.getFieldName();
      FormFileElement[] elements = formBase.getFormFileElements(fieldName, false);
      if(elements == null)
      {
        return false; //20030123AH
      }
      if(elements.length == 0) return false; //20030123AH

//      IFileConstraint constraint = (IFileConstraint)fmi.getConstraint();
//      String pathKeyFieldName = constraint.getPathKeyFieldName();
//      String subPathFieldName = constraint.getSubPathFieldName();
//
//      String pathKey = IPathConfig.PATH_TEMP; //20020902AH - Always transfer to temp
//      String subPath = null;
//
//      if(subPathFieldName != null)
//      {
//        subPath = entity.getFieldString(subPathFieldName);
//        if("".equals(subPath))
//        {
//          throw new java.lang.IllegalStateException("No subPath value in field '"
//            + subPathFieldName + "' of entity:" + entity);
//        }
//      }
//      else
//      {
//        subPath = entity.getSession().getUserId() + "/in/";
//      }
      // 20030725 DDJ: the above is not used more, not sure why it was ever used either.
      String pathKey = IPathConfig.PATH_TEMP; //20020902AH - Always transfer to temp
      String subPath = entity.getSession().getUserId() + "/in/";

      for(int i=0; i < elements.length; i++)
      {
        if(elements[i].isToUpload())  // 03102002 DDJ: Check if file is not already in B-Tier
        {
          FormFile ff = elements[i].getFormFile();
          String transferAs = elements[i].getTransferAs(); //20030801AH
          try
          {
            InputStream stream = ff.getInputStream();
            
            String fileName = FileUtil.create( pathKey, subPath, transferAs, stream ); //20030801AH
            String fullPath = FileUtil.getPath( pathKey ) + subPath + fileName;
            if(saveFullPath)
            {
              elements[i].setUploadedFilename(fullPath);
            }
            else
            {
              elements[i].setUploadedFilename(fileName);
            }
          }
          catch(Throwable t)
          {
            throw new GTClientException("Error transferring file #" + i + " ('" + ff.getFileName() + "')",t);
          }
        }
      }
      entity.setFieldValue(fieldId,elements); // 08102002 DDJ: Store the FormFileElement[]
      return true; //20030123AH
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error transferring files to gtas for field id=" + fieldId
                                  + " of entity:" + entity,t);
    }
  }

  protected final void initialiseActionForm(ActionContext actionContext)
    throws GTClientException
  {
    IGTEntity entity = getEntity(actionContext);
    initialiseActionForm(actionContext,entity);
  }

  protected final void performPreCompleteProcessing(ActionContext actionContext)
    throws GTClientException
  {
    performPreSaveProcessing(actionContext);
  }

  protected final ActionForward performCompleteForwardProcessing(ActionContext actionContext,
                                                       OperationContext completedOpCon,
                                                       ActionForward forward)
    throws GTClientException
  {
    return performSaveForwardProcessing(actionContext,completedOpCon,forward);
  }

  protected final boolean doComplete(ActionContext actionContext, ActionErrors errors)
    throws GTClientException
  { //Wraps the historical 'doSave' nomenclature...
    return doSave(actionContext, errors);
  }

  public final Object processForwardDivMsg(Object context, Object divMsg) throws GTClientException
  { //20030102AH
    //Later will also check for internal msgs here etc....
    return super.processForwardDivMsg(context,divMsg);
  }

  public final Object processBackDivMsg(Object context, Object divMsg) throws GTClientException
  { //20030102AH
    //Later will also check for internal msgs here etc....
    return super.processBackDivMsg(context,divMsg);
  }

  protected boolean forceUseDvTemplate(ActionContext actionContext) throws GTClientException
  { //20030319AH
    return true;
  }
  
  /**
   * This has been overridden to provide field based diversion support. If the divertMapping parameter
   * does not specify a field based diversion then the superclass behaviour for this method will be invoked.
   * Check the javadocs for FieldDivPath for more information on field based diversion paths.
   * NOTE: subclasses that override this method, MUST delegate back to their superclass's getDivertForward
   * for any mappingName they arent planning to take specific action for.
   * @param actionContext
   * @param opCon The new opCon that will be used by the diversion
   * @param mapping
   * @param divertMapping The name of the mapping to divert to or a divPath string
   * @return divForward The forward containing the path to which we will divert
   */
  protected ActionForward getDivertForward(ActionContext actionContext,
                                           OperationContext opCon,
                                           ActionMapping mapping,
                                           String divertMapping)
    throws GTClientException
  { //20030826AH
    if( FieldDivPath.isDivPath(divertMapping) )
    { //We only do the following if the divertMapping specifies a field divPath (ie: begins with "field:")
      try
      {
        //Create a FieldDivPath object to facilitate interpretation of the divPath string
        FieldDivPath divPath = new FieldDivPath(divertMapping);
        //The 'current' opCon is now the one we are about to divert to but the stuff we want to look at is
        //in the 'previous' one
        OperationContext originOpCon = opCon.getPreviousContext(); 
        //Get the entity object. We will need this to obtain the metaInfo from which we can work
        //out the url for the diversion forward given the info supplied to us in the divPath
        IGTEntity entity = getEntity(actionContext, originOpCon);
        //Get the mode (create,update,view) from the divPath. It may be an empty string in which case
        String mode = divPath.getMode();
        if(FieldDivPath.MODE_DEFAULT.equals(mode))
        { //we do some logic to work out what mode to use
          if( Boolean.TRUE.equals( (Boolean)originOpCon.getAttribute(VIEW_MODE_ATTRIBUTE) ) )
          { //If we are currently in view mode we will use that
            mode = FieldDivPath.MODE_VIEW;
          }
          else
          { //Otherwise we will for now assume that it is update mode we want. Depending on the value for
            //the keyField we may later change our minds and switch this to create mode.
            mode = FieldDivPath.MODE_UPDATE; 
          }
        }
        //Get the name of the field on which this diversion is based. The metainfo for this
        //field will supply us with the information we need to divert
        String fieldName = divPath.getFieldName();
        IGTFieldMetaInfo fmi = entity.getFieldMetaInfo(fieldName);
        if( fmi.getConstraintType() != IConstraint.TYPE_FOREIGN_ENTITY )
        { //We can only divert on FER fields so if its not one, throw an exception and fail
          throw new IllegalArgumentException("Expecting a foreign entity field but field "
                                              + fieldName
                                              + " is of constraint type "
                                              + fmi.getConstraintType());
        }
        IForeignEntityConstraint constraint = (IForeignEntityConstraint)fmi.getConstraint();
        //Now we get the keyField. This is the field in the target entity, the value of which we have
        //a copy (unless its a create) and which we will use to search for the target entity. (If this
        //turns up multiple results there will be trouble, however that is not our concern in this method)
        String keyField = constraint.getKeyFieldName();
        //Get the value to divert on. This might not actually be specified, in which case we will
        //need to pull it from the actionForm
        String keyValue = divPath.getValue();
        if( FieldDivPath.VALUE_DEFAULT.equals(keyValue) && (!FieldDivPath.MODE_CREATE.equals(divPath.getMode())))
        { //If it wasnt specified we will try and obtain it from the actionForm
          Object value = PropertyUtils.getSimpleProperty(actionContext.getActionForm(), fieldName);
          if(value == null)
          { //The actionForm has no value for this field either. Looks like we will be doing a create
            keyValue = null;
          }
          else if( value instanceof String[] )
          { //You should really specify the required value for multivalue FERS in the divPath. Here
            //this has not been done so we will just go grab the first one. (You can only divert to
            //one of the values after all)
            String[] values = (String[])value;
            keyValue = values.length > 0 ? values[0] : null;
          }
          else if( value instanceof String )
          { //Simple case of a String value.
            keyValue = (String)value;
          }
          else
          {
            //The actionForm property isnt a String. You had better hope that the dispatchAction
            //in question knows how to convert the value back to do its search for the entity!
            keyValue = StaticUtils.stringValue(value);
          }
        }
        if(StaticUtils.stringEmpty(keyValue))
        { //If its still empty then we must be doing a create.
          mode = FieldDivPath.MODE_CREATE;
        }
        //Determine the name of the actionForward to look up from the config based on the simple
        //convention of "modeEntity" - for example "viewProcessInstance", "updateGridDocument" etc....
        String forwardName = mode + StringUtils.capitalise(constraint.getEntityType());
        ActionForward forward = mapping.findForward(forwardName);
        if(forward == null)
        { //If the appropriate forward wasnt found there isnt much we can do. (In theory we could try
          //and guess the name of the dispatchAction but Im not going to implement this as if its a
          //valid divertable action then it must have an actionMapping!)
          throw new NullPointerException("Cannot find forward \""
                                          + forwardName
                                          + "\" for use with field based diversion: "
                                          + divPath);
        }        
        if(!FieldDivPath.MODE_CREATE.equals(mode))
        { //If not creating we have to append the keyField and its value to the url so the
          //dispatchAction we divert to can find the entity record
          String path = StaticWebUtils.addParameterToURL(forward.getPath(), "keyField", keyField);
          path = StaticWebUtils.addParameterToURL(path, keyField, keyValue);
          forward = new ActionForward(path, forward.getRedirect());
        }
        //Return the actionForward after giving subclass a chance to play with it further
        return processSOCForward( forward, opCon );
      }
      catch(Throwable t)
      { //Opps...
        throw new GTClientException("Error processing a field based diversion:" + divertMapping,t);
      }
    }
    else
    { //Not a FBD. Do the normal thing.
      return super.getDivertForward(actionContext, opCon, mapping, divertMapping);
    }
  }
}