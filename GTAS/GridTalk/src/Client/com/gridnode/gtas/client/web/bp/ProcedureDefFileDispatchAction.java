/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcedureDefFileDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 * 2003-04-10     Andrew Hill         Validate file extension
 * 2003-07-30     Andrew Hill         Support for soap files
 */
package com.gridnode.gtas.client.web.bp;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTProcedureDefFileEntity;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class ProcedureDefFileDispatchAction extends EntityDispatchAction2
{
  private static final String[] EXECUTABLE_EXTENSIONS = null; //20030410AH
  private static final String[] JAVA_EXTENSIONS = new String[] { "jar","class" }; //20030410AH
  private static final String[] SOAP_EXTENSIONS = new String[] { "wsdl","xml" }; //20030730AH

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_PROCEDURE_DEF_FILE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new ProcedureDefFileRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.PROCEDURE_DEF_FILE_UPDATE : IDocumentKeys.PROCEDURE_DEF_FILE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTProcedureDefFileEntity procedureDefFile = (IGTProcedureDefFileEntity)entity;

    ProcedureDefFileAForm form = (ProcedureDefFileAForm)actionContext.getActionForm();
    form.setName(       procedureDefFile.getFieldString(IGTProcedureDefFileEntity.NAME));
    form.setDescription(procedureDefFile.getFieldString(IGTProcedureDefFileEntity.DESCRIPTION));
    form.setType(       procedureDefFile.getFieldString(IGTProcedureDefFileEntity.TYPE));
    form.setFileName(   procedureDefFile.getFieldString(IGTProcedureDefFileEntity.FILE_NAME));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new ProcedureDefFileAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_PROCEDURE_DEF_FILE;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    //IGTProcedureDefFileEntity procedureDefFile = (IGTProcedureDefFileEntity)entity;
    ProcedureDefFileAForm tform = (ProcedureDefFileAForm)form;

    basicValidateString(errors, IGTProcedureDefFileEntity.NAME,        form, entity);
    basicValidateString(errors, IGTProcedureDefFileEntity.DESCRIPTION, form, entity);
    basicValidateString(errors, IGTProcedureDefFileEntity.TYPE,        form, entity);
    //20030410AH - basicValidateString(errors, IGTProcedureDefFileEntity.FILE_NAME,   form, entity);

    //20030410AH - Validate the file
    Integer type = tform.getTypeInteger();
    if(IGTProcedureDefFileEntity.TYPE_EXECUTABLE.equals(type))
    {
      basicValidateFiles(errors, IGTProcedureDefFileEntity.FILE_NAME, tform, entity, EXECUTABLE_EXTENSIONS );
    }
    else if(IGTProcedureDefFileEntity.TYPE_JAVA.equals(type))
    {
      basicValidateFiles(errors, IGTProcedureDefFileEntity.FILE_NAME, tform, entity, JAVA_EXTENSIONS );
    }
    else if(IGTProcedureDefFileEntity.TYPE_SOAP.equals(type))
    { //20030730AH
      basicValidateFiles(errors, IGTProcedureDefFileEntity.FILE_NAME, tform, entity, SOAP_EXTENSIONS );
    }
    else
    {
      //Type not spec'd yet so cant tell if file ext ok yet.
      basicValidateFiles(errors, IGTProcedureDefFileEntity.FILE_NAME, tform, entity, null );
    }
    //..
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTProcedureDefFileEntity procedureDefFile = (IGTProcedureDefFileEntity)entity;
    ProcedureDefFileAForm form = (ProcedureDefFileAForm)actionContext.getActionForm();

    procedureDefFile.setFieldValue(IGTProcedureDefFileEntity.NAME,         form.getName());
    procedureDefFile.setFieldValue(IGTProcedureDefFileEntity.DESCRIPTION,  form.getDescription());
    procedureDefFile.setFieldValue(IGTProcedureDefFileEntity.TYPE,         form.getTypeInteger());
    transferFieldFiles(actionContext, procedureDefFile, IGTProcedureDefFileEntity.FILE_NAME, false);
  }
}