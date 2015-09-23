/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistrationInfoRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-27     Andrew Hill         Created
 * 2002-12-16     Andrew Hill         First use of new tab renderer
 * 2003-03-26     Andrew Hill         Support for newUi
 * 2003-04-16     Andrew Hill         NodeLock changes
 * 2003-05-02     Andrew Hill         Fix bugs with expired registrations
 * 2003-07-04     Andrew Hill         Cause header to be refreshed (so can show updated gnid info)
 * 2003-11-06     Daniel              Fix bug GNDB00016116
 * 2005-03-24     Andrew Hill         Hide editable stuff from non-admin users
 */
package com.gridnode.gtas.client.web.registration;

import java.util.ArrayList;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.gridnode.gtas.client.ctrl.IGTCompanyProfileEntity;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTRegistrationInfoEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.gridnode.CompanyProfileRenderer;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.ITabDef;
import com.gridnode.gtas.client.web.renderers.InsertionDef;
import com.gridnode.gtas.client.web.renderers.MultiNodeInsertionRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.renderers.TabDef;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class RegistrationInfoRenderer extends AbstractRenderer
{
  //private static final String DONE_IMAGE_SRC = "images/actions/done.gif"; //20030326AH
  private static final String REGISTER_IMAGE_SRC = "images/actions/registration.gif"; //20030326AH

  private boolean _edit;
  protected static final Number[] _fields = {
    IGTRegistrationInfoEntity.REG_STATE,
    IGTRegistrationInfoEntity.GRIDNODE_ID,
    IGTRegistrationInfoEntity.GRIDNODE_NAME,
    IGTRegistrationInfoEntity.CATEGORY,
    IGTRegistrationInfoEntity.PRODUCT_KEY_F1,
    IGTRegistrationInfoEntity.PRODUCT_KEY_F2,
    IGTRegistrationInfoEntity.PRODUCT_KEY_F3,
    IGTRegistrationInfoEntity.PRODUCT_KEY_F4,
    IGTRegistrationInfoEntity.SECURITY_PASSWORD,
    IGTRegistrationInfoEntity.CONFIRM_PASSWORD,
    IGTRegistrationInfoEntity.BIZ_CONNECTIONS,
    IGTRegistrationInfoEntity.LICENSE_FILE, //20030416AH
    IGTRegistrationInfoEntity.OS_NAME, //20030416AH
    IGTRegistrationInfoEntity.OS_VERSION, //20030416AH
    IGTRegistrationInfoEntity.MACHINE_NAME, //20030416AH
  };

  protected static final Number[] _licDateFields = {
    IGTRegistrationInfoEntity.LIC_START_DATE,
    IGTRegistrationInfoEntity.LIC_END_DATE,
  }; //20030115AH

  protected static final ITabDef[] _tabs = {
    new TabDef("registrationInfo.tabs.main","main_tab"),
    new TabDef("registrationInfo.tabs.profile","profile_tab"),
  };

  public RegistrationInfoRenderer( RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext(); //20021216AH
      checkReloadHeader(rContext); //20030704AH
      
      IGTRegistrationInfoEntity rego = (IGTRegistrationInfoEntity)getEntity(); //20030115AH
      RegistrationInfoAForm form = (RegistrationInfoAForm)getActionForm();
      renderCommonFormElements(IGTEntity.ENTITY_REGISTRATION_INFO,_edit);
      renderLabel("prodKey_label","registrationInfo.prodKey",false);
      Short state = StaticUtils.shortValue( form.getRegistrationState() );

      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext); //20030115AH
      if(IGTRegistrationInfoEntity.REG_STATE_NOT_REG.equals(state))
      {

        removeNode("licDate_details",false); //20030422
        removeNode("category_details",false); //20030422AH
        removeNode("security_details",false); //20030422AH
        removeNode("bizConnections_details",false); //20030422AH
        removeNode("cancel",false); //20030422AH
        removeNode("button_divider_1",false); //20030422AH
        removeNode("prodKey_details",false); //20030416AH
      }
      else if(IGTRegistrationInfoEntity.REG_STATE_IN_PROGRESS.equals(state))
      {
        if(_edit)
        {
          makeRegoReadOnly(); //20030417AH
        }
        else
        { //20030417AH - In progress in view mode
          Element stateLabel = getElementById("registrationState_value",false);
          if(stateLabel != null)
          { //Highlight the fact they havent finsihed yet!
            stateLabel.setAttribute("class","errortext");
          }
          renderLabelCarefully("edit","registrationInfo.view.continue",false);
        }
        renderLabel("security_heading","registrationInfo.security.heading",false);
        renderLabel("security_message","registrationInfo.security.message",false);
        renderLabelCarefully("ok","registrationInfo.edit.ok.confirm",false); //20030326AH
        Element okIcon = getElementById("ok_icon",false); //20030326AH
        if(okIcon != null)
        { //20030326AH
          okIcon.setAttribute("src",REGISTER_IMAGE_SRC);
        }
        renderLabelCarefully("cancel","registrationInfo.edit.cancelRegistration",false); //20030326AH
        renderLicencePeriod(form,bfpr,rego); //20030115AH
        removeNode("licFile_details",false); //20030417AH
        
        if(rego.getSession().isNoSecurity())
        { //20031105AH - HACK for GNDB00016109
          removeNode("security_details");
        }
        
      }
      else if( IGTRegistrationInfoEntity.REG_STATE_REG.equals(state)
               || IGTRegistrationInfoEntity.REG_STATE_EXPIRED.equals(state) ) //20030502AH
      {
        if(_edit) makeRegoReadOnly(); //20030417AH
        if(_edit) removeNode("security_details",false); //20030417AH
        //20030417AH - removeNode("ok",false);
        //20030417AH - removeNode("button_divider_1",false);
        if(!_edit)
        { //20030417AH
          removeNode("cancel_button",false); //20030417AH
        }
        renderLicencePeriod(form,bfpr,rego); //20030115AH
      }
      ActionErrors actionErrors = getRenderingContext().getActionErrors();
      bfpr = renderFields(bfpr,rego,_fields); //20030416AH
      
      // 20031105 DDJ: GNDB00016116 - remove the cancel button id, so that it will 
      //                              not be removed by CompanyProfileRenderer later
      //20050324AH - Dont remove, but rather rename as I need to find it again later!
      Element cancelButton = getElementById("cancel_button", false);
      if(cancelButton != null)
      {
        //20050324AH - co: cancelButton.removeAttribute("id");
        cancelButton.setAttribute("id", "cancel_button_id"); //20050324AH
      } 
      
      renderEmbeddedProfile();
      if(actionErrors != null)
      {
        ActionError prodKey = MessageUtils.getFirstError(actionErrors, "prodKey");
        if(prodKey != null)
        {
          renderLabel("prodKey_error",prodKey.getKey());
        }
        ActionError responseError = MessageUtils.getFirstError(actionErrors, "error");
        if(responseError != null)
        {
          renderLabel("response_error",responseError.getKey());
        }
      }
      renderTabs(rContext, "registrationTab", _tabs); //20021217AH
      if( IGTRegistrationInfoEntity.REG_STATE_EXPIRED.equals(state) )
      { //20030502AH
        renderLabelCarefully("expired_message","registrationInfo.registrationState.expired.message",false);
        //20050324AH - Hide non editable fields from non-admin users
        IGTSession gtasSession = rego.getSession();
        if(!gtasSession.isAdmin())
        {
          removeNode("licFile_details",false);
          removeNode("ok_button",false);
          removeNode("help_button",false);
          removeNode("cancel_button_id",false);    
        }
        //...
      }
      else
      {
        removeNode("expired_message",false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering registrationInfo screen",t);
    }
  }

  private void renderLicencePeriod( RegistrationInfoAForm form,
                                    BindingFieldPropertyRenderer bfpr,
                                    IGTRegistrationInfoEntity rego)
    throws RenderingException
  { //20030115AH - Modified method of rendering lic start and end dates
    try
    {
      renderLabel("licDate_label","registrationInfo.licDate",false);
      /*String licDate =  form.getLicStartDate()
                        + " - "
                        + form.getLicEndDate();
      replaceText("licDate_value",licDate,true);*/
      renderFields(bfpr,rego,_licDateFields); //20030416AH
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering licence period",t);
    }
  }

  private void makeRegoReadOnly() throws RenderingException
  {
    try
    {
      replaceNodeWithNewElement("span","gridnodeId_value",false,false);
      replaceNodeWithNewElement("span","gridnodeName_value",false,false);
      Text divider = _target.createTextNode("-");
      Element span = _target.createElement("span");
      Element prodKey = getElementById("prodKey_value",true);
      removeAllChildren(prodKey);

      span.setAttribute("id","prodKeyF1_value");
      prodKey.appendChild(span.cloneNode(true));
      prodKey.appendChild(divider.cloneNode(true));

      span.setAttribute("id","prodKeyF2_value");
      prodKey.appendChild(span.cloneNode(true));
      prodKey.appendChild(divider.cloneNode(true));

      span.setAttribute("id","prodKeyF3_value");
      prodKey.appendChild(span.cloneNode(true));
      prodKey.appendChild(divider.cloneNode(true));

      span.setAttribute("id","prodKeyF4_value");
      prodKey.appendChild(span.cloneNode(true));
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error making registration fields read only",t);
    }
  }

  private void renderEmbeddedProfile() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      RegistrationInfoAForm form = (RegistrationInfoAForm)getActionForm();
      IGTRegistrationInfoEntity entity = (IGTRegistrationInfoEntity)getEntity();
      IGTCompanyProfileEntity profile = (IGTCompanyProfileEntity)entity.getFieldValue(entity.COMPANY_PROFILE);

      boolean profileEditable = _edit && form.isProfileEditable(); //20030422AH

      String profileDocKey = profileEditable ? IDocumentKeys.COMPANY_PROFILE_UPDATE : IDocumentKeys.COMPANY_PROFILE_VIEW;
      InsertionDef profileInsertion = new InsertionDef("companyProfile_details",
                                                        "companyProfile_details",
                                                        true,
                                                        true,
                                                        profileDocKey,
                                                        false);
      ArrayList insertions = new ArrayList(1);
      insertions.add(profileInsertion);

      MultiNodeInsertionRenderer mnir = new MultiNodeInsertionRenderer(rContext,insertions);
      CompanyProfileRenderer profileRenderer = new CompanyProfileRenderer(rContext, profileEditable);
      profileRenderer.setProfileEntity(profile);
      profileRenderer.setRenderCommonContent(false);

      mnir.render(_target);
      profileRenderer.render(_target);
      renderLabel("companyProfile_heading","registrationInfo.companyProfile",false);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering embedded companyProfile details",t);
    }
  }

  private void checkReloadHeader(RenderingContext rContext)
    throws RenderingException
  { //20030704AH
    //@todo: only do when necessary (ie: just registered)
    appendOnloadEventMethod("reloadHeader();");
    if(_edit)
    {
      appendOnloadEventMethod("tabErrorNotifier()"); //TWX 18072007:  Add in the pop up to notify the user if there is any input error from user 
    }
  }
}
  
