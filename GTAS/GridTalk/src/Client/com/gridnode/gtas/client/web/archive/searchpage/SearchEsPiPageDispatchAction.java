/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchEsPageDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 22, 2006    Tam Wei Xiang       Created
 * Oct 12, 2006    Regina Zeng         Add user tracking ID, remark
 * Dec 01, 2006    Regina Zeng         Removed pop up window rmoc.confirmation
 */
package com.gridnode.gtas.client.web.archive.searchpage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSearchEsPiDocumentEntity;
import com.gridnode.gtas.client.utils.DateUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.archive.helpers.Logger;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.IRenderingPipeline;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

/**
 * @author Tam Wei Xiang
 * @author Regina Zeng
 * 
 * @since GT 4.0
 */
public class SearchEsPiPageDispatchAction extends EntityDispatchAction2
{
	
	@Override
	protected String getEntityName()
	{
		return IGTEntity.ENTITY_SEARCH_ES_PI_DOCUMENT;
	}

	@Override
	protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
																							RenderingContext rContext,
																							boolean edit) throws GTClientException
	{
		return new EsPiSearchPageRenderer(actionContext, rContext, edit);
	}
	
	/**
	 * To render the error msg if the data/value in the form is invalid
	 */
	@Override
	protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  {
		RenderUtil.renderError(actionContext, rContext, rPipe);
  }
	
	@Override
	protected String getFormDocumentKey(boolean edit, ActionContext actionContext) throws GTClientException
	{
		if(! edit)
		{
			throw new UnsupportedOperationException("View mode not supported");
		}
		else
		{
			return IDocumentKeys.ES_PI_SEARCH_PAGE;
		}
	}

	@Override
	protected ActionForm createActionForm(ActionContext actionContext) throws GTClientException
	{
		return new EsPiSearchPageAForm();
	}

	@Override
	protected void validateActionForm(ActionContext actionContext,
																		IGTEntity entity,
																		ActionForm form,
																		ActionErrors actionErrors) throws GTClientException
	{
		Logger.log("[SearchEsPageDispatchAction.validateActionForm] validating ...");
		IGTSearchEsPiDocumentEntity searchEsDoc = (IGTSearchEsPiDocumentEntity)entity;
		EsPiSearchPageAForm searchPageForm = (EsPiSearchPageAForm)form;
		
		boolean isAtLeastOneFieldEntered = validateFieldsEntered(searchEsDoc, searchPageForm, actionErrors);
		if(! isAtLeastOneFieldEntered)
		{
			return;
		}
		
		basicValidateString(actionErrors, IGTSearchEsPiDocumentEntity.PROCESS_DEF, form, entity);
		basicValidateString(actionErrors, IGTSearchEsPiDocumentEntity.PROCESS_STATE, form, entity);
		basicValidateString(actionErrors, IGTSearchEsPiDocumentEntity.PARTNER_ID, form, entity);
		basicValidateString(actionErrors, IGTSearchEsPiDocumentEntity.PARTNER_NAME, form, entity);
		basicValidateString(actionErrors, IGTSearchEsPiDocumentEntity.DOC_NO, form, entity);
    basicValidateString(actionErrors, IGTSearchEsPiDocumentEntity.USER_TRACKING_ID, form, entity);
    basicValidateString(actionErrors, IGTSearchEsPiDocumentEntity.REMARK, form, entity); //04122006 RZ: Added
		
		validateDate(searchEsDoc.getType(), IGTSearchEsPiDocumentEntity.PROCESS_FROM_START_TIME, searchPageForm.getProcessFromStartTime(),
		             true, IGTSearchEsPiDocumentEntity.PROCESS_TO_START_TIME, searchPageForm.getProcessToStartTime(), false,
		             "yyyy-MM-dd", actionErrors, entity);
		
		validateDate(searchEsDoc.getType(), IGTSearchEsPiDocumentEntity.FROM_DOC_DATE, searchPageForm.getFromDocDate(),
		             true, IGTSearchEsPiDocumentEntity.TO_DOC_DATE, searchPageForm.getToDocDate(), false,
		             "yyyy-MM-dd", actionErrors, entity);
		
		//If user has specified the hour, the process start time is also required to be specified
		if(! "".equals(searchPageForm.getFromSTHour()))
		{
			String fromSTHourFieldName = getFieldname(entity, IGTSearchEsPiDocumentEntity.FROM_ST_HOUR);
			String processStartFromTimeFieldName = getFieldname(entity, IGTSearchEsPiDocumentEntity.PROCESS_FROM_START_TIME);
			String processStartToTimeFieldName = getFieldname(entity, IGTSearchEsPiDocumentEntity.PROCESS_TO_START_TIME);
			
			validateDate(searchEsDoc.getType(), fromSTHourFieldName, searchPageForm.getFromSTHour(), "HH:mm", actionErrors, true);
			validateDate(searchEsDoc.getType(), processStartFromTimeFieldName, searchPageForm.getProcessFromStartTime(),"yyyy-MM-dd", actionErrors, true);
			validateDate(searchEsDoc.getType(), processStartToTimeFieldName, searchPageForm.getProcessToStartTime(),"yyyy-MM-dd", actionErrors, false);
		}
		
		if(! "".equals(searchPageForm.getToSTHour()))
		{
			String toSTHourFieldName = getFieldname(entity, IGTSearchEsPiDocumentEntity.TO_ST_HOUR);
			String processStartFromTimeFieldName = getFieldname(entity, IGTSearchEsPiDocumentEntity.PROCESS_FROM_START_TIME);
			String processStartToTimeFieldName = getFieldname(entity, IGTSearchEsPiDocumentEntity.PROCESS_TO_START_TIME);
			
			validateDate(searchEsDoc.getType(), toSTHourFieldName, searchPageForm.getToSTHour(), "HH:mm", actionErrors, true);
			validateDate(searchEsDoc.getType(), processStartFromTimeFieldName, searchPageForm.getProcessFromStartTime(),"yyyy-MM-dd", actionErrors, true);
			validateDate(searchEsDoc.getType(), processStartToTimeFieldName, searchPageForm.getProcessToStartTime(),"yyyy-MM-dd", actionErrors, true);
		}
	}

	@Override
	protected void updateEntityFields(ActionContext actionContext,
																		IGTEntity entity) throws GTClientException
	{
		IGTSearchEsPiDocumentEntity searchEsDoc = (IGTSearchEsPiDocumentEntity)entity;
		EsPiSearchPageAForm form = (EsPiSearchPageAForm)actionContext.getActionForm();

  		searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.PROCESS_DEF, form.getProcessDef());
  		searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.PROCESS_STATE, form.getProcessState());
  		searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.PARTNER_ID, form.getPartnerID());
  		searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.PARTNER_NAME, form.getPartnerName());
  		searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.PROCESS_FROM_START_TIME, form.getProcessFromStartTime());
  		searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.PROCESS_TO_START_TIME, form.getProcessToStartTime());
  		searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.FROM_ST_HOUR, form.getFromSTHour());
  		searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.TO_ST_HOUR, form.getToSTHour());
  		searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.DOC_NO, form.getDocNo());
      searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.USER_TRACKING_ID, form.getUserTrackingID());
  		searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.FROM_DOC_DATE, form.getFromDocDate());
  		searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.TO_DOC_DATE, form.getToDocDate());
      searchEsDoc.setFieldValue(IGTSearchEsPiDocumentEntity.REMARK, form.getRemark());
  		
  		Logger.log("Process Def ="+form.getProcessDef()+" ProcessState ="+form.getProcessState()+" PartnerID ="+form.getPartnerID());
  		Logger.log("Partner Name ="+form.getPartnerName()+" ProcessFromStartTime ="+form.getProcessFromStartTime());
  		Logger.log("Process To Start Time ="+form.getProcessToStartTime()+" From ST Hour "+form.getFromSTHour());
  		Logger.log("To ST Hour ="+form.getToSTHour()+" DocNo="+form.getDocNo());
  		Logger.log("From Doc Date="+form.getFromDocDate()+" ToDocDate ="+form.getToDocDate());
      Logger.log("User Tracking ID="+form.getUserTrackingID());
      Logger.log("Remark="+form.getRemark());
	}

	@Override
	protected void initialiseActionForm(ActionContext actionContext,
																			IGTEntity entity) throws GTClientException
	{
		EsPiSearchPageAForm form = (EsPiSearchPageAForm)actionContext.getActionForm();
		IGTSearchEsPiDocumentEntity searchDocEntity = (IGTSearchEsPiDocumentEntity)entity;
		
		form.setProcessDef                         (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.PROCESS_DEF));
		form.setProcessState                       (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.PROCESS_STATE));
		form.setPartnerID                          (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.PARTNER_ID));
		form.setPartnerName                        (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.PARTNER_NAME));
		form.setProcessFromStartTime               (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.PROCESS_FROM_START_TIME));
		form.setFromSTHour                         (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.FROM_ST_HOUR));
		form.setProcessToStartTime                 (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.PROCESS_TO_START_TIME));
		form.setToSTHour                           (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.TO_ST_HOUR));
		form.setDocNo                              (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.DOC_NO));
		form.setFromDocDate                        (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.FROM_DOC_DATE));
		form.setToDocDate                          (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.TO_DOC_DATE));
    form.setUserTrackingID                     (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.USER_TRACKING_ID));
    form.setRemark                             (searchDocEntity.getFieldString(IGTSearchEsPiDocumentEntity.REMARK));
	}

	@Override
	protected int getIGTManagerType(ActionContext actionContext) throws GTClientException
	{
		return IGTManager.MANAGER_SEARCH_ES_PI_DOCUMENT;
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
	@Override
  protected void saveWithManager( ActionContext actionContext,
                                  IGTManager manager,
                                  IGTEntity entity)
		throws GTClientException
	{
		Logger.log("[SearchEsPageDispatchAction.saveWithManager] saving entity ....");
		actionContext.getSession().setAttribute(IGTSearchEsPiDocumentEntity.SEARCH_PI_ENTITY, entity);
	}
	
	/*
	protected ActionForward getCompleteForward(ActionContext actionContext)
  	throws GTClientException
  {
		Logger.log("[SearchEsPageDispatchAction.getCompleteForward] Forwading to espiListView ....");
		return actionContext.getMapping().findForward("divertEsPiListView");
  }*/
	
	
	//MUST delegate back to their superclass's getDivertForward
  //for any mappingName they aren't planning to take specific action for.
	
	/*protected ActionForward getDivertForward( ActionContext actionContext, OperationContext opCon, ActionMapping mapping, String divertTo)
    throws GTClientException
  {
		Logger.log("[SearchEsPageDispatchAction.getDivertForward] start searching...");
    IGTEntity entity = (IGTEntity)this.getEntity(actionContext);
    actionContext.getSession().setAttribute(IGTSearchEsPiDocumentEntity.SEARCH_PI_ENTITY, entity);
    String index = actionContext.getRequest().getParameter("singleIndex");
    ActionForward divertForward = mapping.findForward(divertTo);
    if(divertForward == null)
    {
      throw new GTClientException("[SearchEsPageDispatchAction.getDivertForward] No forward mapping found for " + divertTo);
    }
    if("search".equals(divertTo))
    {
      divertForward = new ActionForward(
                          StaticWebUtils.addParameterToURL(divertForward.getPath(),"index",index),
                          divertForward.getRedirect());
    } 
    return processSOCForward( divertForward, opCon );
  } */
	
	/**
	 * Validate a pair of dates. As long as we specified processStartFromDate, processStartToDate
	 * must be specified. This is applicable to DocDate, processStartTimeHour also .
	 * Note: this method will ignore the checking on the fmi constraint. For checking on the contraint,
	 * pls call basicValidateString prior calling this method.
	 * @param entityType
	 * @param field1
	 * @param field2
	 * @param dateValue1
	 * @param dateValue2
	 * @param format
	 * @param actionErrors
	 */
private void validateDate(String entityType, Number field1, String dateValue1, boolean isField1Required,
	                          Number field2, String dateValue2, boolean isField2Required,
	                          String format, ActionErrors actionErrors, IGTEntity entity)
		throws GTClientException
	{
		if("".equals(dateValue1) &&  "".equals(dateValue2))
		{
			return;
		}
		else
		{
			String fieldname1 = getFieldname(entity, field1);
			String fieldname2 = getFieldname(entity, field2);
			
			//If either field1 or field2 has been specified, field2 or field1 must be specified
			validateDate(entityType, fieldname1, dateValue1, format, actionErrors, isField1Required);
			validateDate(entityType, fieldname2, dateValue2, format, actionErrors, isField2Required);
		}
	}
	
	/**
	 * Validate the validity of a date. 
	 * @param entityType
	 * @param field
	 * @param value
	 * @param format
	 * @param actionErrors
	 * @param isRequired
	 */
	protected void validateDate(String entityType, String field, String value,
	                            String format, ActionErrors actionErrors, boolean isRequired)
	{
	    Date date = null;
	    if (!value.equals(""))
	    {         
	       date = DateUtils.parseDate(value, null, null, new SimpleDateFormat(format));
	                            
	       if (date == null)
	       {
	          EntityFieldValidator.addFieldError(actionErrors, field,
	                                  entityType, EntityFieldValidator.INVALID,null);
	       }
	    }
	    else
	    {
	    	if(isRequired)
	    	{
	    		EntityFieldValidator.addFieldError(actionErrors, field, 
	    		                                   entityType, EntityFieldValidator.REQUIRED, null);
	    	}
	    }
	}
	
	private String getFieldname(IGTEntity entity, Number field)
		throws GTClientException
	{
		IGTFieldMetaInfo fmi1 = entity.getFieldMetaInfo(field);
		if(fmi1 == null)
    {
      throw new java.lang.NullPointerException("No fieldMetaInfo for field:"
                                                + field + " of entity " + entity);
    }
		return fmi1.getFieldName();
	}
	
	private boolean validateFieldsEntered(IGTSearchEsPiDocumentEntity searchEsDoc, EsPiSearchPageAForm searchPageForm, 
	                                      ActionErrors actionErrors)
		throws GTClientException
	{
		String processDef = searchPageForm.getProcessDef();
		String processState = searchPageForm.getProcessState();
		String partnerID = searchPageForm.getPartnerID();
		String partnerName = searchPageForm.getPartnerName();
		
		String fromStartTime = searchPageForm.getProcessFromStartTime();
		String fromStartTimeHour = searchPageForm.getFromSTHour();
		String toStartTime = searchPageForm.getProcessToStartTime();
		String toStartTimeHour = searchPageForm.getToSTHour();
		
		String docNo = searchPageForm.getDocNo();
		String fromDocDate = searchPageForm.getFromDocDate();
		String toDocDate = searchPageForm.getToDocDate();
    String userTrackingID = searchPageForm.getUserTrackingID();  
    String remark = searchPageForm.getRemark();
		
		boolean isAtLeastOneFieldEnteredValue = true;
		
		if("".equals(processDef) && "".equals(processState) && "".equals(partnerID) && "".equals(partnerName) &&
				"".equals(fromStartTime) && "".equals(fromStartTimeHour) && "".equals(toStartTime) && "".equals(toStartTimeHour) &&
				"".equals(docNo) && "".equals(fromDocDate) && "".equals(toDocDate) && "".equals(userTrackingID) && "".equals(remark))
		{
			isAtLeastOneFieldEnteredValue = false;
		}
		
		Logger.log("[SearchEsPiPageDispatchAction.validateFieldsEntered] isAtLeastOneFieldEnteredValue "+isAtLeastOneFieldEnteredValue);
		
		if(! isAtLeastOneFieldEnteredValue)
		{
			String fieldName = getFieldname(searchEsDoc, IGTSearchEsPiDocumentEntity.FORM_MSG);
			EntityFieldValidator.addFieldError(actionErrors, fieldName, 
			                                   searchEsDoc.getType(), EntityFieldValidator.REQUIRED, null);
		}
		
		return isAtLeastOneFieldEnteredValue;
	}
  
  //01122006 RZ: Override
  @Override
  public ActionForward update(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response)
                              throws IOException, ServletException, GTClientException
  {
    ActionContext actionContext = new ActionContext(mapping, actionForm, request, response);
    try
    {
      OperationContext opCon = prepareOperationContext(actionContext);
      flagRmocConfirmation(actionContext, opCon, false); 
      checkDetailView(actionContext); 
      performUpdateProcessing(actionContext);
      prepareView(actionContext, opCon, true);
      return mapping.findForward(IGlobals.VIEW_FORWARD);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error in dispatch action", t);
    }
  }
}