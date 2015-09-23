/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Daniel D'Cotta      Created
 * 2002-11-28     Daniel D'Cotta      Aligned embeded entities diversions
 * 2003-06-04     Andrew Hill         Implement support for defAlert field
 * 2003-06-13     Daniel D'Cotta      Added diversion for creating an alert
 * 2003-06-18     Daniel D'Cotta      Added workaround because old versions of GTAS
 *                                    stored 0 instead of null (GNDB00014337)
 * 2003-07-16     Andrew Hill         Support the gridDocField field
 * 2003-07-30     Andrew Hill         SoapProcedure support
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;

public class UserProcedureRenderer extends AbstractRenderer implements IFilter, IBFPROptionSource
{
  private boolean _edit;
  private static final Number _fields[] = //20030604AH - Add '_' to variable name
  {
    IGTUserProcedureEntity.NAME,
    IGTUserProcedureEntity.DESCRIPTION,
    IGTUserProcedureEntity.IS_SYNCHRONOUS,
    IGTUserProcedureEntity.PROC_TYPE,
    IGTUserProcedureEntity.PROC_DEF_FILE,
    IGTUserProcedureEntity.RETURN_DATA_TYPE,
    IGTUserProcedureEntity.DEF_ACTION,
    IGTUserProcedureEntity.DEF_ALERT,
    IGTUserProcedureEntity.GRID_DOC_FIELD, //20030716AH 
  };

  public UserProcedureRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)getEntity();
      UserProcedureAForm form = (UserProcedureAForm)getActionForm();
      RenderingContext rContext = getRenderingContext();
      ActionErrors errors = rContext.getActionErrors();

      // 20030618 DDJ: Workaround because old versions of GTAS stored 0 instead of null (GNDB00014337)
      if(new Integer(0).equals(form.getReturnDataTypeInteger()))
        form.setReturnDataType("");
      if(new Integer(0).equals(form.getDefActionInteger()))
        form.setDefAction("");
      if(new Long(0).equals(form.getDefAlertLong()))
        form.setDefAlert("");

      renderCommonFormElements(IGTEntity.ENTITY_USER_PROCEDURE,_edit);
      //20030604AH - Use optionSource (for defAlert)
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      bfpr.setOptionSource(this);
      bfpr = renderFields(bfpr,userProcedure,_fields,this,form,null);
      //...

      // Render embeded listview
      addParamDefElv(rContext,form);

      // Render return info only if isSynchronous
      if("true".equalsIgnoreCase(form.getIsSynchronous()))
      {
        addReturnDefElv(rContext,form);
      }
      else
      {
        removeNode("returnDataType_details");
        removeNode("defAction_details");
        removeNode("defAlert_details");
        removeNode("procReturnList_details");
      }

      // Render diversion labels
      if(_edit)
      {
        renderLabel("procDefFile_create", "userProcedure.procDefFile.create", false);
        renderLabel("defAlert_create",    "userProcedure.defAlert.create",    false); // 20030613 DDJ: Added
      }

      String editType = _edit ? "edit" : "view";
      if(IGTUserProcedureEntity.TYPE_EXECUTABLE.equals(form.getProcTypeInteger()))
      {
        renderLabel("shellExecutable_" + editType, "userProcedure.shellExecutable." + editType, false);
        removeNode("javaProcedure_" + editType);
        removeNode("soapProcedure_" + editType);

        renderEmbededEntitiesError(errors, IGTUserProcedureEntity.PROC_DEF, userProcedure);
      }
      else if(IGTUserProcedureEntity.TYPE_JAVA.equals(form.getProcTypeInteger()))
      {
        renderLabel("javaProcedure_" + editType, "userProcedure.javaProcedure." + editType, false);
        removeNode("shellExecutable_" + editType);
        removeNode("soapProcedure_" + editType);

        renderEmbededEntitiesError(errors, IGTUserProcedureEntity.PROC_DEF, userProcedure);
      }
      else if(IGTUserProcedureEntity.TYPE_SOAP.equals(form.getProcTypeInteger()))
      { //20030730AH
        renderLabel("soapProcedure_" + editType, "userProcedure.soapProcedure." + editType, false);
        removeNode("shellExecutable_" + editType);
        removeNode("javaProcedure_" + editType);
  
        renderEmbededEntitiesError(errors, IGTUserProcedureEntity.PROC_DEF, userProcedure);
      }
      else
      {
        removeNode("procDef_details");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering userProcedure screen",t);
    }
  }

  /** @todo see if this can be made generic */
  protected void renderEmbededEntitiesError(ActionErrors actionErrors,
                                            Number fieldId,
                                            IGTEntity entity)
     throws RenderingException
  {
    try
    {
      String fieldName = entity.getFieldName(fieldId);
      ActionError error = MessageUtils.getFirstError(actionErrors, fieldName);
      if(error != null)
      {
        renderLabel(fieldName + "_error", error.getKey());
        getElementById(fieldName + "_error", true).setAttribute("class", "errortext");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering errors for embeded entities", t);
    }
  }

  private void addParamDefElv(RenderingContext rContext,
                              UserProcedureAForm form)
    throws RenderingException
  {
    try
    {
      IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)getEntity();
      Number[] columns = {  IGTParamDefEntity.PARAM_LIST_NAME,
                            IGTParamDefEntity.PARAM_LIST_DESCRIPTION,
                            IGTParamDefEntity.PARAM_LIST_TYPE,
                            IGTParamDefEntity.PARAM_LIST_SOURCE};

      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      String entityType = IGTEntity.ENTITY_PARAM_DEF;
      IGTManager manager = gtasSession.getManager(entityType);
      ColumnEntityAdapter adapter = new ColumnEntityAdapter(columns, manager, entityType);
      List procParamList = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_PARAM_LIST);
      if(procParamList == null) throw new NullPointerException("procParamList is null"); //20030416AH

      ListViewOptionsImpl listOptions = new ListViewOptionsImpl();
      listOptions.setColumnAdapter(adapter);
      if(_edit)
      {
        listOptions.setCreateURL("divertUpdateParamDef");
        listOptions.setDeleteURL("divertDeleteParamDef");
        listOptions.setCreateLabelKey("userProcedure.paramDef.create");
        listOptions.setDeleteLabelKey("userProcedure.paramDef.delete");
        listOptions.setAllowsSelection(true);
        listOptions.setAllowsEdit(true);
      }
      else
      {
        listOptions.setCreateURL(null);
        listOptions.setDeleteURL(null);
        listOptions.setAllowsSelection(false);
        listOptions.setAllowsEdit(false);
      }

      listOptions.setHeadingLabelKey("userProcedure.procParamList");
      listOptions.setUpdateURL("divertUpdateParamDef");
      listOptions.setViewURL("divertViewParamDef");
      ElvRenderer elvRenderer = new ElvRenderer(rContext,
                                                "procParamList_details",
                                                listOptions,
                                                procParamList);
      elvRenderer.setEmbeddedList(true);
      elvRenderer.setAllowsOrdering(true);
      elvRenderer.setTableName("procParamList");
      elvRenderer.setOrder(form.getProcParamListOrderExploded());
      elvRenderer.render(_target);
    }
    catch(Exception e)
    {
      throw new RenderingException("Unable to render the procParamList table for the userProcedure",e);
    }
  }

  private void addReturnDefElv( RenderingContext rContext,
                                UserProcedureAForm form)
    throws RenderingException
  {
    try
    {
      IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)getEntity();
      Number[] columns = {  IGTReturnDefEntity.RETURN_LIST_OPERATOR,
                            IGTReturnDefEntity.RETURN_LIST_VALUE,
                            IGTReturnDefEntity.RETURN_LIST_ACTION,
                            IGTReturnDefEntity.RETURN_LIST_ALERT };

      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
      String entityType = IGTEntity.ENTITY_RETURN_DEF;
      IGTManager manager = gtasSession.getManager(entityType);
      ColumnEntityAdapter adapter = new ColumnEntityAdapter(columns,manager,entityType);
      List procReturnList = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_RETURN_LIST);
      if(procReturnList == null) throw new NullPointerException("procReturnList is null"); //20030416AH

      ListViewOptionsImpl listOptions = new ListViewOptionsImpl();
      listOptions.setColumnAdapter(adapter);
      if(_edit)
      {
        listOptions.setCreateURL("divertUpdateReturnDef");
        listOptions.setDeleteURL("divertDeleteReturnDef");
        listOptions.setCreateLabelKey("userProcedure.returnDef.create");
        listOptions.setDeleteLabelKey("userProcedure.returnDef.delete");
        listOptions.setAllowsSelection(true);
        listOptions.setAllowsEdit(true);
      }
      else
      {
        listOptions.setCreateURL(null);
        listOptions.setDeleteURL(null);
        listOptions.setAllowsSelection(false);
        listOptions.setAllowsEdit(false);
      }

      listOptions.setHeadingLabelKey("userProcedure.procReturnList");
      listOptions.setUpdateURL("divertUpdateReturnDef");
      listOptions.setViewURL("divertViewReturnDef");
      ElvRenderer elvRenderer = new ElvRenderer(rContext,
                                                "procReturnList_details",
                                                listOptions,
                                                procReturnList);
      elvRenderer.setEmbeddedList(true);
      elvRenderer.setAllowsOrdering(true);
      elvRenderer.setTableName("procReturnList");
      elvRenderer.setOrder(form.getProcReturnListOrderExploded());
      elvRenderer.render(_target);
    }
    catch(Exception e)
    {
      throw new RenderingException("Unable to render the procReturnList table for the userProcedure",e);
    }
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    if(object instanceof IGTProcedureDefFileEntity)
    { // If we are filtering a procedureDefFile check its type matches that on the form
      return ((IGTProcedureDefFileEntity)object).getFieldString(IGTProcedureDefFileEntity.TYPE).equals(((UserProcedureAForm)getActionForm()).getProcType());
    }
    else
    {
      return true;
    }
  }

  public Collection getOptions(RenderingContext rContext,BindingFieldPropertyRenderer bfpr)
    throws GTClientException
  { //20030604AH
    try
    {
      if( bfpr.getFieldId().equals(IGTUserProcedureEntity.DEF_ALERT) )
      { //Return a collection of all the user defined alerts.
        //This means first looking up the userdefined alertType entity so that we can
        //use its uid to getByKey() all those alerts that have it as their type
        try
        {
          IGTSession gtasSession = getEntity().getSession();

          IGTAlertTypeManager alertTypeMgr = (IGTAlertTypeManager)gtasSession.getManager(IGTManager.MANAGER_ALERT_TYPE);
          IGTAlertManager alertMgr = (IGTAlertManager)gtasSession.getManager(IGTManager.MANAGER_ALERT);
          IGTAlertTypeEntity udAlertType = (IGTAlertTypeEntity)
                                          StaticUtils.getFirst(
                                          alertTypeMgr.getByKey(
                                          IGTAlertTypeEntity.NAME_USER_DEFINED,
                                          IGTAlertTypeEntity.NAME));
          Long udAlertTypeUid = udAlertType == null ? null : udAlertType.getUidLong();
          return udAlertTypeUid == null ? Collections.EMPTY_LIST : alertMgr.getByKey(udAlertTypeUid, IGTAlertEntity.TYPE);
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error providing options for DEF_ALERT field",t);
        }
      }
      else
      {
        return null;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error providing options",t);
    }
  }
}