/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchEsDocPageDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 28, 2006    Tam Wei Xiang       Created (This is to comply the estore 
 *                                              search page with GT UI standard)
 * Dec 01, 2006    Regina Zeng         Removed pop up window rmoc.confirmation   
 * Dec 14  2006    Tam Wei Xiang       Added in field remark                                          
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
import com.gridnode.gtas.client.ctrl.IGTSearchEsPiPageManager;
import com.gridnode.gtas.client.ctrl.IGTSearchEsDocDocumentEntity;
import com.gridnode.gtas.client.utils.DateUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.archive.helpers.Logger;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.strutsbase.RmocConfirmationFlagDivMsg;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class SearchEsDocPageDispatchAction extends EntityDispatchAction2
{
	@Override
	protected String getEntityName()
	{
		return IGTEntity.ENTITY_SEARCH_ES_DOC_DOCUMENT;
	}

	@Override
	protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
																							RenderingContext rContext,
																							boolean edit) throws GTClientException
	{
		return new EsDocSearchPageRenderer(actionContext, rContext, edit);
	}

	@Override
	/**
	 * A place for the UI framework to find out what will be the .html page
	 * this dispatch action will tie to.
	 */
	protected String getFormDocumentKey(boolean edit, ActionContext actionContext) throws GTClientException
	{
		if(!edit)
    {
      throw new UnsupportedOperationException("View mode not supported");
    }
		return IDocumentKeys.ES_DOC_SEARCH_PAGE;
	}

	@Override
	protected ActionForm createActionForm(ActionContext actionContext) throws GTClientException
	{
		return new EsDocSearchPageAForm();
	}

	@Override
	protected void validateActionForm(ActionContext actionContext,
																		IGTEntity entity,
																		ActionForm form,
																		ActionErrors actionErrors) throws GTClientException
	{
		// TODO Auto-generated method stub
		Logger.log("[SearchEsDocPageDispatchAction.validateActionForm] validating ...");
		IGTSearchEsDocDocumentEntity esDocEntity = (IGTSearchEsDocDocumentEntity)entity;
		EsDocSearchPageAForm esDocForm = (EsDocSearchPageAForm)form;
		
		//TODO loop through all the field in the entity, if all field is empty, indicate at least one field is required.
		boolean isAtLeastOneFieldEntered = validateFieldsEntered(esDocEntity, esDocForm, actionErrors);
		if(! isAtLeastOneFieldEntered)
		{
			return;
		}
		
		basicValidateString(actionErrors, IGTSearchEsDocDocumentEntity.PARTNER_ID, form, entity);
		basicValidateString(actionErrors, IGTSearchEsDocDocumentEntity.PARTNER_NAME , form ,entity);
		basicValidateString(actionErrors, IGTSearchEsDocDocumentEntity.FOLDER , form ,entity);
		basicValidateString(actionErrors, IGTSearchEsDocDocumentEntity.DOC_TYPE , form ,entity);
		basicValidateString(actionErrors, IGTSearchEsDocDocumentEntity.USER_TRACKING_ID , form ,entity);
		basicValidateString(actionErrors, IGTSearchEsDocDocumentEntity.DOC_NO , form ,entity);
		basicValidateString(actionErrors, IGTSearchEsDocDocumentEntity.REMARK, form, entity);
    
		String dateFormat = "yyyy-MM-dd";
		String hourFormat = "HH:mm";
		
		//TODO 
		
		
		validateDate(esDocEntity.getType(), IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE, 
		             esDocForm.getFromCreateDate(), true,IGTSearchEsDocDocumentEntity.TO_CREATE_DATE, esDocForm.getToCreateDate(),
		             false, dateFormat, actionErrors, entity);
		validateDate(esDocEntity.getType(), IGTSearchEsDocDocumentEntity.FROM_SENT_DATE, 
		             esDocForm.getFromSentDate(), true, IGTSearchEsDocDocumentEntity.TO_SENT_DATE, esDocForm.getToSentDate(),
		             false, dateFormat, actionErrors, entity);
		validateDate(esDocEntity.getType(), IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE, 
		             esDocForm.getFromReceivedDate(), true,IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE, esDocForm.getToReceivedDate(),
		             false, dateFormat, actionErrors, entity);
		validateDate(esDocEntity.getType(), IGTSearchEsDocDocumentEntity.FROM_DOC_DATE, 
		             esDocForm.getFromDocDate(), true, IGTSearchEsDocDocumentEntity.TO_DOC_DATE, esDocForm.getToDocDate(),
		             false, dateFormat, actionErrors, entity);
		
		//Validate the hour field and hour field correspond date fields
		//CreateDateHour
		validateHourAndDate(esDocEntity, 
		                    IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE_HOUR, IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE, true, IGTSearchEsDocDocumentEntity.TO_CREATE_DATE, false,
		                    dateFormat, hourFormat, esDocForm.getFromCreateDateHour(), 
		                    esDocForm.getFromCreateDate(), esDocForm.getToCreateDate(), actionErrors);
		validateHourAndDate(esDocEntity, 
		                    IGTSearchEsDocDocumentEntity.TO_CREATE_DATE_HOUR, IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE, true, IGTSearchEsDocDocumentEntity.TO_CREATE_DATE, true,
		                    dateFormat, hourFormat, esDocForm.getToCreateDateHour(), 
		                    esDocForm.getFromCreateDate(), esDocForm.getToCreateDate(), actionErrors);
		//SentDateHour
		validateHourAndDate(esDocEntity, 
                        IGTSearchEsDocDocumentEntity.FROM_SENT_DATE_HOUR, IGTSearchEsDocDocumentEntity.FROM_SENT_DATE, true,IGTSearchEsDocDocumentEntity.TO_SENT_DATE, false,
                        dateFormat, hourFormat, esDocForm.getFromSentDateHour(), 
                        esDocForm.getFromSentDate(), esDocForm.getToSentDate(), actionErrors);
		validateHourAndDate(esDocEntity, 
                        IGTSearchEsDocDocumentEntity.TO_SENT_DATE_HOUR, IGTSearchEsDocDocumentEntity.FROM_SENT_DATE, true,IGTSearchEsDocDocumentEntity.TO_SENT_DATE, true,
                        dateFormat, hourFormat, esDocForm.getToSentDateHour(), 
                        esDocForm.getFromSentDate(), esDocForm.getToSentDate(), actionErrors);
		
		//ReceivedDateHour
		validateHourAndDate(esDocEntity, 
                        IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE_HOUR, IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE, true,IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE, false,
                        dateFormat, hourFormat, esDocForm.getFromReceivedDateHour(), 
                        esDocForm.getFromReceivedDate(), esDocForm.getToReceivedDate(), actionErrors);
		validateHourAndDate(esDocEntity, 
                        IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE_HOUR, IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE, true,IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE, true,
                        dateFormat, hourFormat, esDocForm.getToReceivedDateHour(), 
                        esDocForm.getFromReceivedDate(), esDocForm.getToReceivedDate(), actionErrors);
		
		//DocDateHour
		validateHourAndDate(esDocEntity, 
                        IGTSearchEsDocDocumentEntity.FROM_DOC_DATE_HOUR, IGTSearchEsDocDocumentEntity.FROM_DOC_DATE, true,IGTSearchEsDocDocumentEntity.TO_DOC_DATE, false,
                        dateFormat, hourFormat, esDocForm.getFromDocDateHour(), 
                        esDocForm.getFromDocDate(), esDocForm.getToDocDate(), actionErrors);
		validateHourAndDate(esDocEntity, 
                        IGTSearchEsDocDocumentEntity.TO_DOC_DATE_HOUR, IGTSearchEsDocDocumentEntity.FROM_DOC_DATE, true, IGTSearchEsDocDocumentEntity.TO_DOC_DATE, true,
                        dateFormat, hourFormat, esDocForm.getToDocDateHour(), 
                        esDocForm.getFromDocDate(), esDocForm.getToDocDate(), actionErrors);
		
	}

	@Override
	protected void updateEntityFields(ActionContext actionContext,
																		IGTEntity entity) throws GTClientException
	{
		Logger.log("[SearchEsDocPageDispatchAction.updateEntityFields] updating entity ...");
		IGTSearchEsDocDocumentEntity esDocEntity = (IGTSearchEsDocDocumentEntity)entity;
		EsDocSearchPageAForm docSearchForm = (EsDocSearchPageAForm)actionContext.getActionForm();
		
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.PARTNER_ID ,              docSearchForm.getPartnerID());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.PARTNER_NAME ,            docSearchForm.getPartnerName());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.FOLDER ,                  docSearchForm.getFolder());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.DOC_TYPE ,                docSearchForm.getDocType());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE ,        docSearchForm.getFromCreateDate());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE_HOUR ,   docSearchForm.getFromCreateDateHour());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.TO_CREATE_DATE ,          docSearchForm.getToCreateDate());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.TO_CREATE_DATE_HOUR ,     docSearchForm.getToCreateDateHour());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.FROM_SENT_DATE ,          docSearchForm.getFromSentDate());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.FROM_SENT_DATE_HOUR ,     docSearchForm.getFromSentDateHour());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.TO_SENT_DATE ,            docSearchForm.getToSentDate());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.TO_SENT_DATE_HOUR ,       docSearchForm.getToSentDateHour());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE ,      docSearchForm.getFromReceivedDate());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE_HOUR , docSearchForm.getFromReceivedDateHour());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE ,        docSearchForm.getToReceivedDate());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE_HOUR ,   docSearchForm.getToReceivedDateHour());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.DOC_NO ,                  docSearchForm.getDocNo());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.FROM_DOC_DATE ,           docSearchForm.getFromDocDate());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.FROM_DOC_DATE_HOUR ,      docSearchForm.getFromDocDateHour());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.TO_DOC_DATE ,             docSearchForm.getToDocDate());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.TO_DOC_DATE_HOUR ,        docSearchForm.getToDocDateHour());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.USER_TRACKING_ID ,        docSearchForm.getUserTrackingID());
		esDocEntity.setFieldValue(IGTSearchEsDocDocumentEntity.REMARK,                   docSearchForm.getRemark());
	}

	@Override
	protected void initialiseActionForm(ActionContext actionContext,
																			IGTEntity entity) throws GTClientException
	{
		IGTSearchEsDocDocumentEntity esDocEntity = (IGTSearchEsDocDocumentEntity)entity;
		EsDocSearchPageAForm docSearchForm = (EsDocSearchPageAForm)actionContext.getActionForm();
		
		docSearchForm.setPartnerID                    ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.PARTNER_ID));
		docSearchForm.setPartnerName                  ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.PARTNER_NAME));
		docSearchForm.setFolder                       ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.FOLDER));
		docSearchForm.setDocType                      ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.DOC_TYPE));
		docSearchForm.setFromCreateDate               ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE));
		docSearchForm.setFromCreateDateHour           ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.FROM_CREATE_DATE_HOUR));
		docSearchForm.setToCreateDate                 ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.TO_CREATE_DATE));
		docSearchForm.setToCreateDateHour             ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.TO_CREATE_DATE_HOUR));
		docSearchForm.setFromSentDate                 ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.FROM_SENT_DATE));
		docSearchForm.setFromSentDateHour             ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.FROM_SENT_DATE_HOUR));
		docSearchForm.setToSentDate                   ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.TO_SENT_DATE));
		docSearchForm.setToSentDateHour               ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.TO_SENT_DATE_HOUR));
		docSearchForm.setFromReceivedDate             ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE));
		docSearchForm.setFromReceivedDateHour         ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.FROM_RECEIVED_DATE_HOUR));
		docSearchForm.setToReceivedDate               ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE));
		docSearchForm.setToReceivedDateHour           ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.TO_RECEIVED_DATE_HOUR));
		docSearchForm.setDocNo                        ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.DOC_NO));
		docSearchForm.setFromDocDate                  ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.FROM_DOC_DATE));
		docSearchForm.setFromDocDateHour              ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.FROM_DOC_DATE_HOUR));
		docSearchForm.setToDocDate                    ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.TO_DOC_DATE));
		docSearchForm.setToDocDateHour                ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.TO_DOC_DATE_HOUR));
		docSearchForm.setUserTrackingID               ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.USER_TRACKING_ID));
    docSearchForm.setRemark                       ((String)esDocEntity.getFieldValue(IGTSearchEsDocDocumentEntity.REMARK));
	}

	@Override
	protected int getIGTManagerType(ActionContext actionContext) throws GTClientException
	{
		return IGTManager.MANAGER_SEARCH_ES_PI_DOCUMENT;
	}
	
	@Override
	/**
	 * We will use the same IGTManager as the SearchEsPiPageDispatchAction
	 */
  protected IGTEntity getNewEntityInstance( ActionContext actionContext,
                                            IGTManager manager)
		throws GTClientException
  {
  	IGTSearchEsPiPageManager esDocManager = (IGTSearchEsPiPageManager)manager;
  	return esDocManager.getSearchEsDocument();
  }

	@Override
	/**
	 * We are not saving the entity to backend, instead we save it to session and pass
	 * it to entityListAction.
	 */
	protected void saveWithManager(ActionContext actionContext, IGTManager manager, IGTEntity entity) throws GTClientException
	{
		Logger.log("[SearchEsDocPageDispatchAction.saveWithManager] saving entity ..."+entity.getType());
    actionContext.getSession().setAttribute(IGTSearchEsDocDocumentEntity.SEARCH_DOC_ENTITY, entity);

	}
	
	/*
	protected ActionForward getCompleteForward(ActionContext actionContext)
		throws GTClientException
  {
		Logger.log("[SearchEsDocPageDispatchAction.getCompleteForward] Forwading to espiListView ....");
		return actionContext.getMapping().findForward("divertEsDocListView");
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
	
	/**
	 * As long as the user has specified the hour field, the correspond date field need to be filled in also
	 */
	private void validateHourAndDate(IGTSearchEsDocDocumentEntity entity, Number hourField, Number startDateField, 
	                                 boolean isStartDateRequired, Number endDateField, boolean isEndDateRequired,
	                                 String dateFormat, String hourFormat, String hourValue, String startDateValue, String endDateValue,
	                                 ActionErrors actionErrors)
		throws GTClientException
	{
		if(hourValue != null && ! "".equals(hourValue))
		{
			String hourFieldName = getFieldname(entity, hourField);
			String startDateFieldName = getFieldname(entity, startDateField);
			String endDateFieldName = getFieldname(entity, endDateField);
			
			validateDate(entity.getType(), hourFieldName, hourValue, hourFormat, actionErrors, true);
			validateDate(entity.getType(), startDateFieldName, startDateValue, dateFormat, actionErrors, isStartDateRequired);
			validateDate(entity.getType(), endDateFieldName, endDateValue, dateFormat, actionErrors, isEndDateRequired); //the end date field is not compulsary
		}
	}
	
	private boolean validateFieldsEntered(IGTSearchEsDocDocumentEntity entity,
	                                      EsDocSearchPageAForm form, ActionErrors actionErrors)
		throws GTClientException
	{
		boolean isAtLeastOneFieldEnteredValue = true;
		
		String partnerID = form.getPartnerID();
		String partnerName = form.getPartnerName();
		String folder = form.getFolder();
		String docType = form.getDocType();
		String userTrackingID = form.getUserTrackingID();
		
		String fromCreateDate = form.getFromCreateDate();
		String fromCreateDateHour = form.getFromCreateDateHour();
		String toCreateDate = form.getToCreateDate();
		String toCreateDateHour = form.getToCreateDateHour();
		
		String fromSentDate = form.getFromSentDate();
		String fromSentDateHour = form.getFromSentDateHour();
		String toSentDate = form.getToSentDate();
		String toSentDateHour = form.getToSentDateHour();
		
		String fromReceivedDate = form.getFromReceivedDate();
		String fromReceivedDateHour = form.getFromReceivedDateHour();
		String toReceivedDate = form.getToReceivedDate();
		String toReceivedDateHour = form.getToReceivedDateHour();
		
		String docNo = form.getDocNo();
		String fromDocDate = form.getFromDocDate();
		String fromDocDateHour = form.getFromDocDateHour();
		String toDocDate = form.getToDocDate();
		String toDocDateHour = form.getToDocDateHour();
		
    String remark = form.getRemark();
    
		if("".equals(partnerID) && "".equals(partnerName) && "".equals(folder) && "".equals(docType) && "".equals(userTrackingID) &&
				"".equals(fromCreateDate) && "".equals(fromCreateDateHour) && "".equals(toCreateDate) && "".equals(toCreateDateHour) &&
				"".equals(fromSentDate) && "".equals(fromSentDateHour) && "".equals(toSentDate) && "".equals(toSentDateHour)&&
				"".equals(fromReceivedDate) && "".equals(fromReceivedDateHour) && "".equals(toReceivedDate) && "".equals(toReceivedDateHour) &&
				"".equals(docNo) && "".equals(fromDocDate) && "".equals(fromDocDateHour) && "".equals(toDocDate) && "".equals(toDocDateHour) &&
        "".equals(remark))
		{
			isAtLeastOneFieldEnteredValue = false;
		}
		
		Logger.log("[SearchEsDocPageDispatchAction.validateFieldsEntered] isAtLeastOneFieldEnteredValue "+isAtLeastOneFieldEnteredValue);
		if(! isAtLeastOneFieldEnteredValue)
		{
			String fieldName = getFieldname(entity, IGTSearchEsDocDocumentEntity.FORM_MSG);
			EntityFieldValidator.addFieldError(actionErrors, fieldName, 
  		                                   entity.getType(), EntityFieldValidator.REQUIRED, null);
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
