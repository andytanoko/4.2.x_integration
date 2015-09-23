/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkMappingRuleDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-17     Andrew Hill         Created
 * 2002-12-03     Andrew Hill         Rearranged some field display rules
 */
package com.gridnode.gtas.client.web.document;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class GridTalkMappingRuleDispatchAction extends EntityDispatchAction2
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_docconfig";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_GRIDTALK_MAPPING_RULE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new GridTalkMappingRuleRenderer( rContext,
                                            edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.GRIDTALK_MAPPING_RULE_UPDATE : IDocumentKeys.GRIDTALK_MAPPING_RULE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTGridTalkMappingRuleEntity gtMappingRule = (IGTGridTalkMappingRuleEntity)entity;
    IGTMappingRuleEntity mappingRule = (IGTMappingRuleEntity)gtMappingRule.getFieldValue(gtMappingRule.MAPPING_RULE);
    GridTalkMappingRuleAForm form = (GridTalkMappingRuleAForm)actionContext.getActionForm();

    form.setDescription(gtMappingRule.getFieldString(gtMappingRule.DESCRIPTION));
    form.setHeaderTransformation(gtMappingRule.getFieldString(gtMappingRule.IS_HEADER_TRANSFORMATION));
    form.setKeepOriginal(mappingRule.getFieldString(mappingRule.IS_KEEP_ORIGINAL));
    form.setMappingFile(mappingRule.getFieldString(mappingRule.MAPPING_FILE));
    form.setType(mappingRule.getFieldString(mappingRule.TYPE));
    form.setName(gtMappingRule.getFieldString(gtMappingRule.NAME));
    form.setParamName(mappingRule.getFieldString(mappingRule.PARAM_NAME));
    form.setRefDocUid(mappingRule.getFieldString(mappingRule.REF_DOC_UID));
    form.setSourceDocType(gtMappingRule.getFieldString(gtMappingRule.SOURCE_DOC_TYPE));
    form.setSourceDocFileType(gtMappingRule.getFieldString(gtMappingRule.SOURCE_DOC_FILE_TYPE));
    form.setTargetDocType(gtMappingRule.getFieldString(gtMappingRule.TARGET_DOC_TYPE));
    form.setTargetDocFileType(gtMappingRule.getFieldString(gtMappingRule.TARGET_DOC_FILE_TYPE));
    form.setTransformRefDoc(mappingRule.getFieldString(mappingRule.IS_TRANSFORM_REF_DOC));
    form.setTransformWithHeader(gtMappingRule.getFieldString(gtMappingRule.IS_TRANSFORM_WITH_HEADER));
    form.setTransformWithSource(gtMappingRule.getFieldString(gtMappingRule.IS_TRANSFORM_WITH_SOURCE));
    form.setXpath(mappingRule.getFieldString(mappingRule.XPATH));
    form.setMappingClass(mappingRule.getFieldString(mappingRule.MAPPING_CLASS));
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new GridTalkMappingRuleAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_GRIDTALK_MAPPING_RULE;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm form,
                                    ActionErrors errors)
    throws GTClientException
  {
    GridTalkMappingRuleAForm gridTalkMappingRuleAForm = (GridTalkMappingRuleAForm)form;
    IGTSession gtasSession = getGridTalkSession(actionContext);
    GridTalkMappingRuleAForm gtMappingRuleAForm = (GridTalkMappingRuleAForm)form;
    IGTGridTalkMappingRuleEntity gridTalkMappingRule = (IGTGridTalkMappingRuleEntity)entity;
    IGTMappingRuleEntity mappingRule = (IGTMappingRuleEntity)
                              gridTalkMappingRule.getFieldValue(gridTalkMappingRule.MAPPING_RULE);

    Short type = StaticUtils.shortValue(gridTalkMappingRuleAForm.getType());
    Boolean headerTransformation = StaticUtils.booleanValue( gridTalkMappingRuleAForm.getHeaderTransformation() );

    basicValidateString(errors, gridTalkMappingRule.NAME, gridTalkMappingRuleAForm, gridTalkMappingRule);
    basicValidateString(errors, gridTalkMappingRule.DESCRIPTION, gridTalkMappingRuleAForm, gridTalkMappingRule);
    basicValidateString(errors, mappingRule.TYPE, gridTalkMappingRuleAForm, mappingRule);
    basicValidateString(errors, mappingRule.MAPPING_FILE, gridTalkMappingRuleAForm, mappingRule);

    if(IGTMappingRuleEntity.TYPE_MAPPING_TRANSFORM.equals(type))
    {
      boolean transformRefDoc = StaticUtils.primitiveBooleanValue( gridTalkMappingRuleAForm.getTransformRefDoc() );
      if(transformRefDoc)
      {
        basicValidateString(errors, mappingRule.REF_DOC_UID, gridTalkMappingRuleAForm, mappingRule );
      }
    }
    else if(IGTMappingRuleEntity.TYPE_MAPPING_SPLIT.equals(type))
    {
      basicValidateString(errors, mappingRule.PARAM_NAME, gridTalkMappingRuleAForm, mappingRule );
      basicValidateString(errors, mappingRule.XPATH, gridTalkMappingRuleAForm, mappingRule );
    }

    if(!(Boolean.TRUE.equals(headerTransformation)))
    {
      basicValidateString(errors, gridTalkMappingRule.SOURCE_DOC_TYPE, gridTalkMappingRuleAForm, gridTalkMappingRule);
      basicValidateString(errors, gridTalkMappingRule.TARGET_DOC_TYPE, gridTalkMappingRuleAForm, gridTalkMappingRule);
      basicValidateString(errors, gridTalkMappingRule.SOURCE_DOC_FILE_TYPE, gridTalkMappingRuleAForm, gridTalkMappingRule);
      basicValidateString(errors, gridTalkMappingRule.TARGET_DOC_FILE_TYPE, gridTalkMappingRuleAForm, gridTalkMappingRule);
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    GridTalkMappingRuleAForm form = (GridTalkMappingRuleAForm)actionContext.getActionForm();
    IGTGridTalkMappingRuleEntity gtMappingRule = (IGTGridTalkMappingRuleEntity)entity;
    IGTMappingRuleEntity mappingRule = (IGTMappingRuleEntity)gtMappingRule.getFieldValue(gtMappingRule.MAPPING_RULE);

    Boolean headerTransformation = StaticUtils.booleanValue(form.getHeaderTransformation());

    gtMappingRule.setFieldValue( gtMappingRule.DESCRIPTION, form.getDescription());
    gtMappingRule.setFieldValue( gtMappingRule.IS_HEADER_TRANSFORMATION, headerTransformation );
    gtMappingRule.setFieldValue( gtMappingRule.IS_TRANSFORM_WITH_HEADER, StaticUtils.booleanValue(form.getTransformWithHeader()) );
    gtMappingRule.setFieldValue( gtMappingRule.IS_TRANSFORM_WITH_SOURCE, StaticUtils.booleanValue(form.getTransformWithSource()) );
    gtMappingRule.setFieldValue( gtMappingRule.NAME, form.getName() );
//    gtMappingRule.setFieldValue( gtMappingRule.SOURCE_DOC_FILE_TYPE, form.getTargetDocFileType() );
    gtMappingRule.setFieldValue( gtMappingRule.SOURCE_DOC_FILE_TYPE, form.getSourceDocFileType() ); // 20021230 DDJ
    gtMappingRule.setFieldValue( gtMappingRule.SOURCE_DOC_TYPE, form.getSourceDocType() );
    gtMappingRule.setFieldValue( gtMappingRule.TARGET_DOC_FILE_TYPE, form.getTargetDocFileType() );
    gtMappingRule.setFieldValue( gtMappingRule.TARGET_DOC_TYPE, form.getTargetDocType() );

    mappingRule.setFieldValue( mappingRule.DESCRIPTION, form.getDescription() ); // for completeness
    mappingRule.setFieldValue( mappingRule.NAME, form.getName() ); // for completeness

    mappingRule.setFieldValue( mappingRule.IS_KEEP_ORIGINAL, StaticUtils.booleanValue(form.getKeepOriginal()) );
    mappingRule.setFieldValue( mappingRule.IS_TRANSFORM_REF_DOC, StaticUtils.booleanValue(form.getTransformRefDoc()) );
    mappingRule.setFieldValue( mappingRule.MAPPING_FILE, StaticUtils.longValue(form.getMappingFile()) );
    mappingRule.setFieldValue( mappingRule.XPATH, form.getXpath() );
    mappingRule.setFieldValue( mappingRule.PARAM_NAME, form.getParamName() );
    mappingRule.setFieldValue( mappingRule.REF_DOC_UID, StaticUtils.longValue(form.getRefDocUid()) );
    mappingRule.setFieldValue( mappingRule.TYPE, StaticUtils.shortValue(form.getType()) );
    mappingRule.setFieldValue( mappingRule.MAPPING_CLASS, form.getMappingClass());
  }

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    presetHeaderTransformation(actionContext);
  }

  private void presetHeaderTransformation(ActionContext actionContext)
    throws GTClientException
  {
    // This was here due to a problem with form population. That was fixed, but now this has a new
    // life in setting the value when one is creating a new mapping rule from one of the
    // filtered listviews. This is because in our system the actionform isnt created when we
    // hit the update method the first time to do a create (or update) so it wont be automatically
    // populated based on what parameters we tacked onto the url of the create link in the
    // listview. We will now explicitly check for the headerTransformation parameter and set
    // the value in the actionForm manually.
    GridTalkMappingRuleAForm form = (GridTalkMappingRuleAForm)actionContext.getActionForm();
    String headerTransformation = actionContext.getRequest().getParameter("headerTransformation");
    if(headerTransformation != null)
    {
      form.setHeaderTransformation(headerTransformation);
    }
  }

  protected ActionForward performSaveForwardProcessing(ActionContext actionContext,
                                                        OperationContext completedOpCon,
                                                       ActionForward forward)
    throws GTClientException
  {
    return appendHeaderTransformation(actionContext, completedOpCon, forward);
  }

  /**
   * We have overridden this to allow us to tack on a parameter value for headerTransformation so
   * that the listview we return to will filter accordingly.
   * @param actionContext
   * @return forward
   */
  protected ActionForward getCancelForward(ActionContext actionContext)
    throws GTClientException
  {
    ActionForward forward = super.getCancelForward(actionContext);
    OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
    return appendHeaderTransformation(actionContext, opCon, forward);
  }

  private ActionForward appendHeaderTransformation(ActionContext actionContext,
                                                   OperationContext opCon,
                                                   ActionForward forward)
    throws GTClientException
  {
    GridTalkMappingRuleAForm form = (GridTalkMappingRuleAForm)opCon.getActionForm();
    Boolean headerTransformation = StaticUtils.booleanValue( form.getHeaderTransformation() );
    if(headerTransformation != null)
    {
      String path = forward.getPath();
      path = StaticWebUtils.addParameterToURL(path,"headerTransformation","" + headerTransformation);
      forward = new ActionForward(path, forward.getRedirect());
    }
    return forward;
  }
}