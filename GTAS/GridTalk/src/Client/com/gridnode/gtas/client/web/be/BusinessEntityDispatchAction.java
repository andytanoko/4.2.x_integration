/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BusinessEntityDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-07     Andrew Hill         Created
 * 2002-10-08     ANdrew Hill         "partnerCat" stuff
 * 2004-01-02     Daniel D'Cotta      Added DomainIdentifiers
 * 2008-07-17     Teh Yu Phei		  Add activate (Ticket 31)
 */
package com.gridnode.gtas.client.web.be;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction2;
import com.gridnode.gtas.client.web.strutsbase.EntityFieldValidator;
import com.gridnode.gtas.client.web.strutsbase.FieldValidator;
import com.gridnode.gtas.client.web.strutsbase.OperationContext;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class BusinessEntityDispatchAction extends EntityDispatchAction2
{
  protected static final String ADD_CONDITION     = "addDomainIdentifier";
  protected static final String REMOVE_CONDITION  = "removeDomainIdentifier";

  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_be";
  }

  protected String getEntityName()
  {
    return IGTEntity.ENTITY_BUSINESS_ENTITY;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new BusinessEntityRenderer(rContext, edit);
  }

  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.BUSINESS_ENTITY_UPDATE : IDocumentKeys.BUSINESS_ENTITY_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    BusinessEntityAForm form = (BusinessEntityAForm)actionContext.getActionForm();
    IGTBusinessEntityEntity be = (IGTBusinessEntityEntity)entity;
    IGTWhitePageEntity whitePage = (IGTWhitePageEntity)be.getFieldValue(IGTBusinessEntityEntity.WHITE_PAGE);
    form.setIsNewEntity(be.isNewEntity());

    form.setAddress( whitePage.getFieldString(IGTWhitePageEntity.ADDRESS) );
    form.setBeState( be.getFieldString(IGTBusinessEntityEntity.STATE) );
    form.setBusinessDesc( whitePage.getFieldString(IGTWhitePageEntity.BUSINESS_DESC) );
    form.setCity( whitePage.getFieldString(IGTWhitePageEntity.CITY) );
    form.setContactPerson( whitePage.getFieldString(IGTWhitePageEntity.CONTACT_PERSON));
    form.setCountry( whitePage.getFieldString(IGTWhitePageEntity.COUNTRY) );
    form.setDescription( be.getFieldString(IGTBusinessEntityEntity.DESCRIPTION) );
    form.setDuns( whitePage.getFieldString(IGTWhitePageEntity.DUNS) );
    form.setEmail( whitePage.getFieldString(IGTWhitePageEntity.EMAIL) );
    form.setEnterpriseId( be.getFieldString(IGTBusinessEntityEntity.ENTERPRISE_ID) );
    form.setFax( whitePage.getFieldString(IGTWhitePageEntity.FAX) );
    form.setForPartner( be.getFieldString(IGTBusinessEntityEntity.IS_PARTNER) );
    form.setGlobalSupplyChainCode( whitePage.getFieldString(IGTWhitePageEntity.G_SUPPLY_CHAIN_CODE) );
    form.setId( be.getFieldString(IGTBusinessEntityEntity.ID) );
    form.setLanguage( whitePage.getFieldString(IGTWhitePageEntity.LANGUAGE) );
    form.setPublishable( be.getFieldString(IGTBusinessEntityEntity.IS_PUBLISHABLE) );
    form.setPoBox( whitePage.getFieldString(IGTWhitePageEntity.PO_BOX) );
    form.setState( whitePage.getFieldString(IGTWhitePageEntity.STATE) );
    form.setSyncToServer( be.getFieldString(IGTBusinessEntityEntity.IS_SYNC_TO_SERVER) );
    form.setTel( whitePage.getFieldString(IGTWhitePageEntity.TEL) );
    form.setWebsite( whitePage.getFieldString(IGTWhitePageEntity.WEBSITE) );
    form.setZipCode( whitePage.getFieldString(IGTWhitePageEntity.POSTCODE) );
    form.setPartnerCategory( be.getFieldString(IGTBusinessEntityEntity.PARTNER_CAT) );

    // Channels

    String[] channelUids = null;
    if(be.isNewEntity())
    {
      channelUids = new String[0];
    }
    else
    {
      Collection channels = (Collection)be.getFieldValue(IGTBusinessEntityEntity.CHANNELS);
      channelUids = new String[channels.size()];
      Iterator channelIterator = channels.iterator();
      for(int i=0; i < channelUids.length; i++)
      {
        channelUids[i] = "" + channelIterator.next();
      }
    }
    form.setChannels(channelUids);

    // DomainIdentifiers
    if(entity.isNewEntity())
    {
      /*040512NSL Do not add an empty row automatically
      if(form.getDomainIdentifiers().length == 0)
      {
        addNewDomainIdentifier(be, form);
      } 
      */     
    }
    else
    {
      int noOfDomainIdentifiers = getDomainIdentifierArray(be).length;
      for(int i = 0; i < noOfDomainIdentifiers; i++)
      {
        form.addNewDomainIdentifier(null);
      }      
    }
    
    IGTDomainIdentifierEntity[] domainIdentifiers = getDomainIdentifierArray(be); 
    DomainIdentifierAForm[] domainIdentifierForms = form.getDomainIdentifiers();
    for(int i = 0; i < domainIdentifiers.length; i++)
    {
      domainIdentifierForms[i].setType  (domainIdentifiers[i].getFieldString(IGTDomainIdentifierEntity.TYPE));
      domainIdentifierForms[i].setValue (domainIdentifiers[i].getFieldString(IGTDomainIdentifierEntity.VALUE));
    }
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    return new BusinessEntityAForm();
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_BUSINESS_ENTITY;
  }

  protected void validateActionForm(  ActionContext actionContext,
                                      IGTEntity entity,
                                      ActionForm form,
                                      ActionErrors actionErrors)
    throws GTClientException
  {
    IGTBusinessEntityEntity be = (IGTBusinessEntityEntity)entity;
    IGTWhitePageEntity whitePage = (IGTWhitePageEntity)be.getFieldValue(IGTBusinessEntityEntity.WHITE_PAGE);

    basicValidateString(actionErrors, IGTBusinessEntityEntity.ENTERPRISE_ID, form, be);
    basicValidateString(actionErrors, IGTBusinessEntityEntity.ID, form, be);
    basicValidateString(actionErrors, IGTBusinessEntityEntity.DESCRIPTION, form, be);
    basicValidateString(actionErrors, IGTBusinessEntityEntity.IS_PARTNER, form, be);
    basicValidateString(actionErrors, IGTBusinessEntityEntity.IS_PUBLISHABLE, form, be);
    basicValidateString(actionErrors, IGTBusinessEntityEntity.IS_SYNC_TO_SERVER, form, be);
    basicValidateString(actionErrors, IGTBusinessEntityEntity.STATE, form, be);

    basicValidateString(actionErrors, IGTWhitePageEntity.BUSINESS_DESC, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.DUNS, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.G_SUPPLY_CHAIN_CODE, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.CONTACT_PERSON, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.EMAIL, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.TEL, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.FAX, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.WEBSITE, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.ADDRESS, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.PO_BOX, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.CITY, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.STATE, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.POSTCODE, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.COUNTRY, form, whitePage);
    basicValidateString(actionErrors, IGTWhitePageEntity.LANGUAGE, form, whitePage);

    // DomainIdentifiers
    //IGTDomainIdentifierEntity[] domainIdentifiers = getDomainIdentifierArray(be); 
    DomainIdentifierAForm[] domainIdentifierForms = ((BusinessEntityAForm)form).getDomainIdentifiers();
    for(int i = 0; i < domainIdentifierForms.length; i++)
    {
      boolean typeEmpty = isEmpty(domainIdentifierForms[i].getType());
      boolean valEmpty = isEmpty(domainIdentifierForms[i].getValue());
      
      if (!(typeEmpty && valEmpty))
      {
        if (typeEmpty || valEmpty)
        {
          // add action error
          EntityFieldValidator.addFieldError(actionErrors, "domainIdentifiers[" + i + "].error.typeAndValue.required", entity.getType(), FieldValidator.REQUIRED, null); //20040512 DDJ
        }
      }
    }    

  }

  private boolean isEmpty(String val)
  {
    return (val == null || val.trim().length()==0);
  }
  
  protected void updateEntityFields( ActionContext actionContext,
                                     IGTEntity entity)
    throws GTClientException
  {
    BusinessEntityAForm form = (BusinessEntityAForm)actionContext.getActionForm();
    IGTBusinessEntityEntity be = (IGTBusinessEntityEntity)entity;
    IGTWhitePageEntity whitePage = (IGTWhitePageEntity)be.getFieldValue(IGTBusinessEntityEntity.WHITE_PAGE);

    be.setFieldValue(IGTBusinessEntityEntity.ENTERPRISE_ID, form.getEnterpriseId());
    be.setFieldValue(IGTBusinessEntityEntity.ID, form.getId() );
    be.setFieldValue(IGTBusinessEntityEntity.DESCRIPTION, form.getDescription());
    be.setFieldValue(IGTBusinessEntityEntity.IS_PARTNER, StaticUtils.booleanValue(form.getForPartner()));
    be.setFieldValue(IGTBusinessEntityEntity.IS_PUBLISHABLE, StaticUtils.booleanValue(form.getPublishable()));
    be.setFieldValue(IGTBusinessEntityEntity.IS_SYNC_TO_SERVER, StaticUtils.booleanValue(form.getSyncToServer()));
    be.setFieldValue(IGTBusinessEntityEntity.STATE, StaticUtils.integerValue(form.getBeState()));

    whitePage.setFieldValue(IGTWhitePageEntity.BUSINESS_DESC, form.getBusinessDesc());
    whitePage.setFieldValue(IGTWhitePageEntity.DUNS, form.getDuns());
    whitePage.setFieldValue(IGTWhitePageEntity.G_SUPPLY_CHAIN_CODE, form.getGlobalSupplyChainCode());
    whitePage.setFieldValue(IGTWhitePageEntity.CONTACT_PERSON, form.getContactPerson());
    whitePage.setFieldValue(IGTWhitePageEntity.EMAIL, form.getEmail());
    whitePage.setFieldValue(IGTWhitePageEntity.TEL, form.getTel());
    whitePage.setFieldValue(IGTWhitePageEntity.FAX, form.getFax());
    whitePage.setFieldValue(IGTWhitePageEntity.WEBSITE, form.getWebsite());
    whitePage.setFieldValue(IGTWhitePageEntity.ADDRESS, form.getAddress());
    whitePage.setFieldValue(IGTWhitePageEntity.PO_BOX, form.getPoBox());
    whitePage.setFieldValue(IGTWhitePageEntity.CITY, form.getCity());
    whitePage.setFieldValue(IGTWhitePageEntity.STATE, form.getState());
    whitePage.setFieldValue(IGTWhitePageEntity.POSTCODE, form.getZipCode());
    whitePage.setFieldValue(IGTWhitePageEntity.COUNTRY, form.getCountry());
    whitePage.setFieldValue(IGTWhitePageEntity.LANGUAGE, form.getLanguage());

    // Channels    
    String[] channelUids = form.getChannels();
    Collection channels = null;
    if(channelUids != null)
    {
      channels = new Vector(channelUids.length);
      for(int i=0; i < channelUids.length; i++)
      {
        channels.add(StaticUtils.longValue(channelUids[i]));
      }
    }
    be.setFieldValue(IGTBusinessEntityEntity.CHANNELS, channels);

    // DomainIdentifiers
    IGTDomainIdentifierEntity[] domainIdentifiers = getDomainIdentifierArray(be); 
    DomainIdentifierAForm[] domainIdentifierForms = form.getDomainIdentifiers();
    Vector identifierList = new Vector();
    ArrayList identifierFormList = new ArrayList();
    for(int i = 0; i < domainIdentifierForms.length; i++)
    {
      if (!isEmpty(domainIdentifierForms[i].getType()) && !isEmpty(domainIdentifierForms[i].getValue()))
      {
        domainIdentifiers[i].setFieldValue(IGTDomainIdentifierEntity.TYPE,  domainIdentifierForms[i].getType());
        domainIdentifiers[i].setFieldValue(IGTDomainIdentifierEntity.VALUE, domainIdentifierForms[i].getValue());
        identifierList.add(domainIdentifiers[i]);
        identifierFormList.add(domainIdentifierForms[i]);
      }
    }
    form.setDomainIdentifiers((DomainIdentifierAForm[])identifierFormList.toArray(new DomainIdentifierAForm[identifierList.size()]));
    be.setFieldValue(IGTBusinessEntityEntity.DOMAIN_IDENTIFIERS, identifierList);    
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTBusinessEntityEntity be = (IGTBusinessEntityEntity)entity;

    String isPartner = actionContext.getRequest().getParameter("isPartner");
    if(isPartner != null)
    {
      be.setFieldValue(IGTBusinessEntityEntity.IS_PARTNER, StaticUtils.booleanValue(isPartner));
    }
  }

  // Helper method 
  public static IGTDomainIdentifierEntity[] getDomainIdentifierArray(IGTBusinessEntityEntity be) throws GTClientException
  {
    Collection domainIdentifiers = (Collection)be.getFieldValue(IGTBusinessEntityEntity.DOMAIN_IDENTIFIERS);
    return (IGTDomainIdentifierEntity[])domainIdentifiers.toArray(new IGTDomainIdentifierEntity[domainIdentifiers.size()]);
  }

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    IGTBusinessEntityEntity be = (IGTBusinessEntityEntity)getEntity(actionContext);
    BusinessEntityAForm beForm = (BusinessEntityAForm)actionContext.getActionForm();
    if(beForm == null) return;
    
    String updateAction = beForm.getUpdateAction();
    if(ADD_CONDITION.equals(updateAction))
    {
      addNewDomainIdentifier(be, beForm);
    }
    else if(REMOVE_CONDITION.equals(updateAction))
    {
      removeSelectedDomainIdentifiers(be, beForm);
    }
    
    beForm.setUpdateAction(null);
  }
  
  private void addNewDomainIdentifier(IGTBusinessEntityEntity be, BusinessEntityAForm beForm) throws GTClientException
  {
    // Add a new domainIdentifier to the entity
    IGTBusinessEntityManager manager = (IGTBusinessEntityManager)be.getSession().getManager(IGTManager.MANAGER_BUSINESS_ENTITY);
    Collection domainIdentifiers = (Collection)be.getFieldValue(IGTBusinessEntityEntity.DOMAIN_IDENTIFIERS);
    domainIdentifiers.add(manager.newDomainIdentifier()); 
    be.setFieldValue(IGTBusinessEntityEntity.DOMAIN_IDENTIFIERS, domainIdentifiers);

    // Add a new domainIdentifier to the action form
    beForm.addNewDomainIdentifier(null);
  }

  private void removeSelectedDomainIdentifiers(IGTBusinessEntityEntity be, BusinessEntityAForm beForm) throws GTClientException
  {
    // Remove selected domainIdentifiers to the entity
    Collection domainIdentifiers = (Collection)be.getFieldValue(IGTBusinessEntityEntity.DOMAIN_IDENTIFIERS);
    DomainIdentifierAForm[] domainIdentifierForms = beForm.getDomainIdentifiers();

    if(domainIdentifierForms == null) return;

    ListIterator iterator = ((List)domainIdentifiers).listIterator();
    for(int i = 0; i < domainIdentifierForms.length; i++)
    {
      iterator.next();
      if(domainIdentifierForms[i].isSelected())
      {
        iterator.remove();
      }      
    }
    be.setFieldValue(IGTBusinessEntityEntity.DOMAIN_IDENTIFIERS, domainIdentifiers);
    
    // Remove selected domainIdentifiers to the action form
    beForm.removeSelectedDomainIdentifiers();
  }
  
  public ActionForward activate(ActionMapping mapping, ActionForm actionForm,
           						HttpServletRequest request, HttpServletResponse response)
          						throws Exception
  {
	  try
	  {
		ActionForward forward = mapping.findForward("listview");
		String url = StaticWebUtils.addParameterToURL(forward.getPath(),"isState","1");
		forward = new ActionForward(url, forward.getRedirect());
		String[] uids = null;
		
		OperationContext opCon = OperationContext.getOperationContext(request);
		
		if(opCon != null)
		{
			OperationContext previousOC = opCon.popOperationContext();
			if(previousOC != null)
			{
				OperationContext.saveOperationContext(previousOC, request);
				forward = processSOCForward( previousOC.getResumeForward(), previousOC );
				uids = request.getParameterValues("fuid");
			}
		}
		else
		{
			uids = request.getParameterValues("uid");
		}
		
		
		if( (uids == null) || (uids.length == 0) )
		{
			return forward;
		}
		ActionContext actionContext = new ActionContext(mapping, actionForm, request, response);
		IGTSession gtasSession = getGridTalkSession(actionContext.getRequest().getSession());
		IGTBusinessEntityManager manager = (IGTBusinessEntityManager)gtasSession.getManager(IGTManager.MANAGER_BUSINESS_ENTITY);
		long[] bizEntities = StaticUtils.primitiveLongArrayValue(uids);
		manager.activate(bizEntities);
		return forward; 
	}
	catch(Throwable t)
	{
	throw new GTClientException("Error Activate BEs ",t);
	}
  }
}