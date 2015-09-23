/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BusinessEntityRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-07     Andrew Hill         Created
 * 2002-10-08     Andrew Hill         "partnerCat" stuff
 * 2002-12-17     Andrew Hill         Use tabs for display
 * 2004-01-02     Daniel D'Cotta      Added DomainIdentifiers
 * 2006-04-25     Neo Sok Lay         Hide PartnerCategory if NoP2P is on.
 */
package com.gridnode.gtas.client.web.be;

import java.util.Collection;

import org.apache.struts.action.ActionError;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.IFilter;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;

public class BusinessEntityRenderer extends AbstractRenderer
  implements IFilter
{
  private boolean _edit;
  private boolean _isPartner;
  protected static final Number[] fields = {    IGTBusinessEntityEntity.DESCRIPTION,
                                                IGTBusinessEntityEntity.ENTERPRISE_ID,
                                                IGTBusinessEntityEntity.ID,
                                                IGTBusinessEntityEntity.IS_PARTNER,
                                                IGTBusinessEntityEntity.IS_PUBLISHABLE,
                                                IGTBusinessEntityEntity.IS_SYNC_TO_SERVER,
                                                IGTBusinessEntityEntity.STATE,
                                                IGTBusinessEntityEntity.CHANNELS,
                                                IGTBusinessEntityEntity.PARTNER_CAT  };

  protected static final Number[] wpFields = {  IGTWhitePageEntity.ADDRESS,
                                                IGTWhitePageEntity.BUSINESS_DESC,
                                                IGTWhitePageEntity.CITY,
                                                IGTWhitePageEntity.CONTACT_PERSON,
                                                IGTWhitePageEntity.COUNTRY,
                                                IGTWhitePageEntity.DUNS,
                                                IGTWhitePageEntity.EMAIL,
                                                IGTWhitePageEntity.FAX,
                                                IGTWhitePageEntity.G_SUPPLY_CHAIN_CODE,
                                                IGTWhitePageEntity.LANGUAGE,
                                                IGTWhitePageEntity.POSTCODE,
                                                IGTWhitePageEntity.PO_BOX,
                                                IGTWhitePageEntity.STATE,
                                                IGTWhitePageEntity.TEL,
                                                IGTWhitePageEntity.WEBSITE, };

  private static Number[] _domainIdentifierFields =
  {
    IGTDomainIdentifierEntity.TYPE,
    IGTDomainIdentifierEntity.VALUE,
  };
  
  public static final ITabDef[] _tabs = { //20021217AH
    new TabDef("businessEntity.tabs.be", "be_tab"),
    new TabDef("businessEntity.tabs.whitePage", "whitePage_tab"),
    new TabDef("businessEntity.tabs.domainIdentifiers", "domainIdentifiers_tab"),
  };

  public BusinessEntityRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext(); //20021217AH

      IGTBusinessEntityEntity be = (IGTBusinessEntityEntity)getEntity();
      IGTWhitePageEntity whitePage = (IGTWhitePageEntity)be.getFieldValue(IGTBusinessEntityEntity.WHITE_PAGE);
      BusinessEntityAForm beForm = (BusinessEntityAForm)getActionForm();

      _isPartner = StaticUtils.primitiveBooleanValue(beForm.getForPartner());

      renderCommonFormElements(be.getType(),_edit);
      // renderLabel("whitePage_label","businessEntity.whitePage",false); // 20040102 DDJ: Commented out
      if(_edit) renderLabel("channels_create","businessEntity.channels.create",false);
      BindingFieldPropertyRenderer bfpr = renderFields(null, be, fields, this, beForm, "");
      //NSL20060425 Check for noP2P & noUDDI
    	IGTSession gtasSession = StaticWebUtils.getGridTalkSession(getRenderingContext().getRequest());
      if (gtasSession.isNoP2P())
      {
      	removeNode("partnerCategory_details");
      	removeNode("syncToServer_details");
      }
      if (gtasSession.isNoUDDI())
      {
      	removeNode("publishable_details");
      }
      bfpr = renderFields(bfpr,whitePage,wpFields);

      renderTabs(rContext, "beTab", _tabs); //20021217AH
      
//    TWX 18072007:  Add in the pop up to notify the user if there is any input error from user 
      if(_edit)
      {
        includeJavaScript(IGlobals.JS_ENTITY_FORM_METHODS);
        appendEventMethod(getBodyNode(),"onload","tabErrorNotifier();");
      }
      
      renderDomainIdentifiers();
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering businessEntity screen",t);
    }
  }

  // 20040102 DDJ: render conditions using stamping of html
  protected void renderDomainIdentifiers() throws Throwable
  {
    RenderingContext rContext = getRenderingContext();
    IGTBusinessEntityEntity be = (IGTBusinessEntityEntity)getEntity();
    BusinessEntityAForm beForm = (BusinessEntityAForm)rContext.getOperationContext().getActionForm();

    renderLabel("domainIdentifier.type_label",         "domainIdentifier.type");
    renderLabel("domainIdentifier.value_label",        "domainIdentifier.value");

    renderLabelCarefully("addDomainIdentifier",        "domainIdentifier.addDomainIdentifier",     false);
    renderLabelCarefully("removeDomainIdentifier",     "domainIdentifier.removeDomainIdentifier",  false);

    Node domainIdentifierRow = getElementById("domainIdentifier_row");
    Node domainIdentifierRowParent = domainIdentifierRow.getParentNode();
    domainIdentifierRowParent.removeChild(domainIdentifierRow);
    Node controlRow = getElementById("control_row");
    
    // Note: meta-info states value class as ArrayList, but event returned Vector...
    Collection domainIdentifiersCollection = (Collection)be.getFieldValue(IGTBusinessEntityEntity.DOMAIN_IDENTIFIERS);
    IGTDomainIdentifierEntity[] domainIdentifiers = (IGTDomainIdentifierEntity[])domainIdentifiersCollection.toArray(new IGTDomainIdentifierEntity[domainIdentifiersCollection.size()]);
    DomainIdentifierAForm[] domainIdentifierForms = beForm.getDomainIdentifiers();
    for(int i = 0; i < domainIdentifierForms.length; i++)
    {
      try
      {
        Node clonedRow = domainIdentifierRow.cloneNode(true);
        domainIdentifierRowParent.insertBefore(clonedRow, controlRow);
        
        // Render domainIdentifier
        IGTDomainIdentifierEntity domainIdentifier = domainIdentifiers[i];
        DomainIdentifierAForm domainIdentifierForm = domainIdentifierForms[i];

        renderFields(null, domainIdentifier, _domainIdentifierFields, domainIdentifierForm, "");

        // Make id unique for each row
        String idPrefix = "domainIdentifiers[" + i + "].";

        if(_edit)
        {
          Element selectCol = getElementById("selected_value");
          selectCol.setAttribute("id", idPrefix + "selected_value");
          selectCol.setAttribute("name", idPrefix + "selected");

          if(domainIdentifierForm.isSelected())
          {
            selectCol.setAttribute("checked", "checked");
          }
        }
        
        Element typeCol = getElementById("type_value");
        typeCol.setAttribute("id", idPrefix + "type_value");
        typeCol.setAttribute("name", idPrefix + "type");
        
        Element valueCol = getElementById("value_value");
        valueCol.setAttribute("id", idPrefix + "value_value");
        valueCol.setAttribute("name", idPrefix + "value");

        // 20040512 DDJ: manually display error
        if (_edit)
        {
          Element errorCol = getElementById("domainIdentifier_error");
          errorCol.setAttribute("id", idPrefix + "domainIdentifier_error");

          ActionError error = MessageUtils.getFirstError(rContext.getActionErrors(), idPrefix + "error.typeAndValue.required"); 
          if(error != null)
          {
            //String errorMsg = rContext.getResourceLookup().getMessage(error.getKey());
            String errorMsg = rContext.getResourceLookup().getMessage("domainIdentifier.error.typeAndValue.required");
            if(errorMsg == null) errorMsg = "";
            replaceMultilineText(errorCol, errorMsg);
            errorCol.setAttribute("class", "errortext");
          }
        }
      }
      catch(Throwable t)
      {
        throw new RenderingException("Error rendering domainIdentifier, i=" + i, t);
      }
    }
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    if(object instanceof IGTChannelInfoEntity)
    {
      Boolean isPartnerChannelInfo = (Boolean)((IGTChannelInfoEntity)object).getFieldValue(IGTChannelInfoEntity.IS_PARTNER);
      Short partnerCatChannelInfo = (Short)((IGTChannelInfoEntity)object).getFieldValue(IGTChannelInfoEntity.PARTNER_CAT);
      if(_isPartner)
      {
        // If we are a partner BE, the channel Infos must be selected from those whose
        // partnerCat is others
        return (IGTChannelInfoEntity.PARTNER_CAT_OTHERS.equals(partnerCatChannelInfo));
      }
      else
      {
        // If however we are not a partner BE, then we may select only those channelInfos
        // which are not partner channelInfos
        return (Boolean.FALSE.equals(isPartnerChannelInfo));
      }
    }
    return true;
  }
}

