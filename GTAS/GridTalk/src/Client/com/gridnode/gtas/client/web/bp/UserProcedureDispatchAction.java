/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 * 2003-06-04     Andrew Hill         Remove deprecated getNavgroup() method
 * 2003-07-16     Andrew Hill         Support for the gridDocField field
 * 2003-07-30     Andrew Hill         soapProcedure support
 * 2003-08-26     Andrew Hill         Corrected illegal impl of getDivertForward() that didnt respect delegation contract with superclass
 */
package com.gridnode.gtas.client.web.bp;

import java.util.List;
import java.util.Vector;

import org.apache.struts.action.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.strutsbase.SetOpConAttributeDivMsg;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class UserProcedureDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_USER_PROCEDURE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new UserProcedureRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.USER_PROCEDURE_UPDATE : IDocumentKeys.USER_PROCEDURE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)entity;

    UserProcedureAForm form = (UserProcedureAForm)actionContext.getActionForm();
    form.setName(           userProcedure.getFieldString(IGTUserProcedureEntity.NAME));
    form.setDescription(    userProcedure.getFieldString(IGTUserProcedureEntity.DESCRIPTION));
    form.setIsSynchronous(  userProcedure.getFieldString(IGTUserProcedureEntity.IS_SYNCHRONOUS));
    form.setProcType(       userProcedure.getFieldString(IGTUserProcedureEntity.PROC_TYPE));
    form.setProcDefFile(    userProcedure.getFieldString(IGTUserProcedureEntity.PROC_DEF_FILE));
    form.setReturnDataType( userProcedure.getFieldString(IGTUserProcedureEntity.RETURN_DATA_TYPE));
    form.setDefAction(      userProcedure.getFieldString(IGTUserProcedureEntity.DEF_ACTION));
    form.setDefAlert(       userProcedure.getFieldString(IGTUserProcedureEntity.DEF_ALERT));
    form.setGridDocField(   userProcedure.getFieldString(IGTUserProcedureEntity.GRID_DOC_FIELD)); //20030716AH

    List procParamList = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_PARAM_LIST);
    form.initProcParamListOrder(procParamList == null ? 0 : procParamList.size());

    List procReturnList = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_RETURN_LIST);
    form.initProcReturnListOrder(procReturnList == null ? 0 : procReturnList.size());
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new UserProcedureAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_USER_PROCEDURE;
  }

  /** @todo see if this can be made generic */
  protected void initialiseEmbededEntity(ActionContext actionContext, Number fieldId)
    throws GTClientException
  {
    // Set a flag to represent embeded entity is valid (eg. "[fieldName]NotValid")
    IGTEntity entity = (IGTEntity)getEntity(actionContext);
    String fieldName = entity.getFieldName(fieldId);
    OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
    if(opContext.getAttribute(fieldName + "NotValid") == null)
      opContext.setAttribute(fieldName + "NotValid", Boolean.TRUE);
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)entity;
    UserProcedureAForm upForm = (UserProcedureAForm)form;

    basicValidateString(errors, IGTUserProcedureEntity.NAME,             form, entity);
    basicValidateString(errors, IGTUserProcedureEntity.DESCRIPTION,      form, entity);
    basicValidateString(errors, IGTUserProcedureEntity.IS_SYNCHRONOUS,   form, entity);
    basicValidateString(errors, IGTUserProcedureEntity.PROC_TYPE,        form, entity);
    basicValidateString(errors, IGTUserProcedureEntity.PROC_DEF_FILE,    form, entity);
    basicValidateString(errors, IGTUserProcedureEntity.RETURN_DATA_TYPE, form, entity);
    basicValidateString(errors, IGTUserProcedureEntity.DEF_ACTION,       form, entity);
    basicValidateString(errors, IGTUserProcedureEntity.DEF_ALERT,        form, entity);
    basicValidateString(errors, IGTUserProcedureEntity.GRID_DOC_FIELD,   form, entity); //20030716AH

    // 20030214 DDJ: Currently Shell Executable procedures have no need for validation
    //if(userProcedure.TYPE_EXECUTABLE.equals(upForm.getProcTypeInteger()) || userProcedure.TYPE_JAVA.equals(upForm.getProcTypeInteger()))
    if(IGTUserProcedureEntity.TYPE_JAVA.equals(upForm.getProcTypeInteger()))
    {
      validateEmbededEntitiy(actionContext, errors, IGTUserProcedureEntity.PROC_DEF, entity);
    }
    if(IGTUserProcedureEntity.TYPE_SOAP.equals(upForm.getProcTypeInteger()))
    { //20030730AH
      validateEmbededEntitiy(actionContext, errors, IGTUserProcedureEntity.PROC_DEF, entity);
    }

    // Note: Assuming both userProcedure.PROC_PARAM_LIST and userProcedure.PROC_RETURN_LIST can be empty vectors
  }

  /** @todo see if this can be made generic */
  protected void validateEmbededEntitiy(ActionContext actionContext,
                                        ActionErrors actionErrors,
                                        Number fieldId,
                                        IGTEntity entity)
    throws GTClientException
  {
    String fieldName = entity.getFieldName(fieldId);
    OperationContext opContext = OperationContext.getOperationContext(actionContext.getRequest());
    if(Boolean.TRUE.equals(opContext.getAttribute(fieldName + "NotValid")))
    {
      actionErrors.add(fieldName, new ActionError(entity.getType() + ".error." + fieldName + ".required"));
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)entity;
    UserProcedureAForm form = (UserProcedureAForm)actionContext.getActionForm();

    userProcedure.setFieldValue(IGTUserProcedureEntity.NAME,             form.getName());
    userProcedure.setFieldValue(IGTUserProcedureEntity.DESCRIPTION,      form.getDescription());
    userProcedure.setFieldValue(IGTUserProcedureEntity.IS_SYNCHRONOUS,   form.getIsSynchronousBoolean());
    userProcedure.setFieldValue(IGTUserProcedureEntity.PROC_TYPE,        form.getProcTypeInteger());
    userProcedure.setFieldValue(IGTUserProcedureEntity.PROC_DEF_FILE,    form.getProcDefFileLong());
    userProcedure.setFieldValue(IGTUserProcedureEntity.RETURN_DATA_TYPE, form.getReturnDataTypeInteger());
    userProcedure.setFieldValue(IGTUserProcedureEntity.DEF_ACTION,       form.getDefActionInteger());
    userProcedure.setFieldValue(IGTUserProcedureEntity.DEF_ALERT,        form.getDefAlertLong());
    userProcedure.setFieldValue(IGTUserProcedureEntity.GRID_DOC_FIELD,   form.getGridDocFieldInteger()); //20030716AH
  }

  protected ActionForward getDivertForward( ActionContext actionContext,
                                            OperationContext opCon,
                                            ActionMapping mapping,
                                            String divertTo)
    throws GTClientException
  { //20030826AH - Rearranged to work with fbds and future changes
    //It wasnt respecting the contract with the superclasss - that is to delegate back for mappings
    //it doesnt understand, and not to do things for mappings it has no business doing things for
    //(like throwing exceptions when it encounters an fbd mapping)
    //I cant be bothered doing it neater, so theres now some duplicated code , but such is life.   
    if(("divertUpdateParamDef".equals(divertTo)) || ("divertViewParamDef".equals(divertTo)))
    {
      String index = actionContext.getRequest().getParameter("singleIndex");
      ActionForward divertForward = mapping.findForward(divertTo);
      if(divertForward == null)
      {
        throw new GTClientException("No mapping found for " + divertTo);
      }
      index = translateParamDefIndex(actionContext,index);
      divertForward = new ActionForward(  StaticWebUtils.addParameterToURL(divertForward.getPath(),"index",index),
                                          divertForward.getRedirect());
      return processSOCForward(divertForward, opCon);
    }
    else if(("divertUpdateReturnDef".equals(divertTo)) || ("divertViewReturnDef".equals(divertTo)))
    {
      String index = actionContext.getRequest().getParameter("singleIndex");
      ActionForward divertForward = mapping.findForward(divertTo);
      if(divertForward == null)
      {
        throw new GTClientException("No mapping found for " + divertTo);
      }
      index = translateReturnDefIndex(actionContext,index);
      divertForward = new ActionForward(  StaticWebUtils.addParameterToURL(divertForward.getPath(),"index",index),
                                          divertForward.getRedirect());
      return processSOCForward(divertForward, opCon);
    }
    else
    { //20030826AH
      return super.getDivertForward(actionContext, opCon, mapping, divertTo);
    }    
  }

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    arrangeParamDef(actionContext);
    arrangeReturnDef(actionContext);

    IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)getEntity(actionContext);
    if(userProcedure.isNewEntity())
    {
      initialiseEmbededEntity(actionContext, IGTUserProcedureEntity.PROC_DEF);
    }
  }


  protected void performPreSaveProcessing(ActionContext actionContext)
    throws GTClientException
  {
    arrangeParamDef(actionContext);
    arrangeReturnDef(actionContext);
  }

  protected String performPreDivertProcessing(ActionContext actionContext, String divertMappingName)
    throws GTClientException
  {
    arrangeParamDef(actionContext);
    arrangeReturnDef(actionContext);
    return divertMappingName;
  }

  private String translateParamDefIndex(ActionContext actionContext, String oldIndex)
  {
    if("new".equals(oldIndex)) return oldIndex;
    String newIndex = null;
    UserProcedureAForm form = (UserProcedureAForm)actionContext.getActionForm();
    String[] order = form.getProcParamListOrderExploded();
    for(int i=0; i < order.length; i++)
    {
      if(order[i].equals(oldIndex))
      {
        newIndex = "" + i;
      }
    }
    return newIndex;
  }

  private String translateReturnDefIndex(ActionContext actionContext, String oldIndex)
  {
    if("new".equals(oldIndex)) return oldIndex;
    String newIndex = null;
    UserProcedureAForm form = (UserProcedureAForm)actionContext.getActionForm();
    String[] order = form.getProcReturnListOrderExploded();
    for(int i=0; i < order.length; i++)
    {
      if(order[i].equals(oldIndex))
      {
        newIndex = "" + i;
      }
    }
    return newIndex;
  }

  private void arrangeParamDef(ActionContext actionContext)
    throws GTClientException
  {
    UserProcedureAForm form = (UserProcedureAForm)actionContext.getActionForm();
    IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)getEntity(actionContext);
    List oldOrder = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_PARAM_LIST);
    if( (oldOrder == null) || (oldOrder.size() == 0) )
    {
      return;
    }

    String[] order = form.getProcParamListOrderExploded();
    if(order == null)
    {
      order = new String[0];
    }
    List newOrder = new Vector(order.length);

    for(int i=0; i < order.length; i++)
    {
      String nextStr = order[i];
      int next = StaticUtils.primitiveIntValue(nextStr);
      IGTParamDefEntity entity = (IGTParamDefEntity)oldOrder.get(next);
      newOrder.add(entity);
    }
    userProcedure.setFieldValue(IGTUserProcedureEntity.PROC_PARAM_LIST, newOrder);
  }

  private void arrangeReturnDef(ActionContext actionContext)
    throws GTClientException
  {
    UserProcedureAForm form = (UserProcedureAForm)actionContext.getActionForm();
    IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)getEntity(actionContext);
    List oldOrder = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_RETURN_LIST);
    if( (oldOrder == null) || (oldOrder.size() == 0) )
    {
      return;
    }

    String[] order = form.getProcReturnListOrderExploded();
    if(order == null)
    {
      order = new String[0];
    }
    List newOrder = new Vector(order.length);

    for(int i=0; i < order.length; i++)
    {
      String nextStr = order[i];
      int next = StaticUtils.primitiveIntValue(nextStr);
      IGTReturnDefEntity entity = (IGTReturnDefEntity)oldOrder.get(next);
      newOrder.add(entity);
    }
    userProcedure.setFieldValue(IGTUserProcedureEntity.PROC_RETURN_LIST, newOrder);
  }

  protected void prepareView( ActionContext actionContext,
                                    OperationContext opCon,
                                    boolean edit)
    throws GTClientException
  {
    try
    {
      UserProcedureAForm form = (UserProcedureAForm)actionContext.getActionForm();
      IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)getEntity(actionContext);

      List procParamList = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_PARAM_LIST);
      form.initProcParamListOrder(procParamList == null ? 0 : procParamList.size());

      List procReturnList = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_RETURN_LIST);
      form.initProcReturnListOrder(procReturnList == null ? 0 : procReturnList.size());

      super.prepareView(actionContext,opCon,edit);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error preparing entity view",t);
    }
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    entity.setFieldValue(IGTUserProcedureEntity.PROC_TYPE, IGTUserProcedureEntity.TYPE_JAVA);

    // 20030123 DDJ: Currently only Synchronous User Procedures are supported
    entity.setFieldValue(IGTUserProcedureEntity.IS_SYNCHRONOUS, Boolean.TRUE);

    // 20030213 DDJ: Currently only the 'continue' action is supported
    entity.setFieldValue(IGTUserProcedureEntity.DEF_ACTION, IGTUserProcedureEntity.ACTION_CONTINUE);

    // 20030123 DDJ: Currently alerts are not supported
    entity.setFieldValue(IGTUserProcedureEntity.DEF_ALERT, new Long(0));
  }
  
  protected void processPushOpCon(ActionContext actionContext,
                                    OperationContext opCon,
                                    OperationContext newOpCon,
                                    String mappingName)
    throws GTClientException
  {
    //20030716AH
    if( (mappingName.indexOf("JavaProcedure") != -1)
     || (mappingName.indexOf("SoapProcedure") != -1) ) //20030730AH - Also for soapProcedure
    {
      //Send a divMsg that will cause the attribute under PROC_DEF_FILE_UID_KEY in the
      //JavaProcedure operationContext to be set to the uid of the procedureDefFile entity
      //to allow the JavaProcedureRenderer to lookup class and method names from the
      //ProcedureDefFileManager
      UserProcedureAForm form = (UserProcedureAForm)actionContext.getActionForm();
      SetOpConAttributeDivMsg msg = new SetOpConAttributeDivMsg();
      msg.setAttribute(JavaProcedureDispatchAction.PROC_DEF_FILE_UID_KEY);
      msg.setValue(form.getProcDefFileLong());
      msg.setSource(opCon);
      newOpCon.addForwardDivMsg(msg);
    }
  }

}