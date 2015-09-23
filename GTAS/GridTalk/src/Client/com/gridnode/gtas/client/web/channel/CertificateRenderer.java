/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-20     Andrew Hill         Created
 * 2003-01-28     Andrew Hill         Nearly finished.... <sigh/>
 * 2003-04-04     Andrew Hill         Show error msg in tabs if x500 not read properly
 * 2003-04-15     Andrew Hill         inKeyStore & inTrustStore fields
 * 2004-03-26     Daniel D'Cotta      Added RELATED_CERT_UID
 * 2006-07-26     Tam Wei Xiang       Render SerialNum, StartDate, EndDate
 * 2006-07-28     Tam Wei Xiang       Render isCA
 * 2006-08-28     Tam Wei Xiang       Render REPLACEMENT_CERT_UID, Modified method allows(...)
 * 2006-08-01	  Wong Yee Wah		  Render SWAP_DATE, SWAP_TIME
 */
package com.gridnode.gtas.client.web.channel;

import java.util.Date;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.strutsbase.*;

public class CertificateRenderer extends AbstractRenderer implements IFilter
{
  private static final String EVIL_HARDCODED_CERT_FILENAME = "certificate.cer";
  private boolean _edit;

  private static final Number[] _fields = {
    IGTCertificateEntity.NAME,
    IGTCertificateEntity.ID,
    IGTCertificateEntity.IS_PARTNER,
    IGTCertificateEntity.IS_IN_KS, //20030415AH
    IGTCertificateEntity.IS_IN_TS, //20030415AH
    IGTCertificateEntity.RELATED_CERT_UID, // 20040326 DDJ
    IGTCertificateEntity.REPLACEMENT_CERT_UID //20060828 TWX
  };

  private static final Number[] _certFile = {
    IGTCertificateEntity.CERT_FILE,
  };

  private static final Number[] _ownFields = {
    IGTCertificateEntity.PASSWORD,
  };

  private static Number[] _x500NameFields =
  {
    IGTX500NameEntity.COUNTRY,
    IGTX500NameEntity.STATE,
    IGTX500NameEntity.ORGANIZATION,
    IGTX500NameEntity.LOCALITY,
    IGTX500NameEntity.ORGANIZATIONAL_UNIT,
    IGTX500NameEntity.STREET_ADDRESS,
    IGTX500NameEntity.COMMON_NAME,
    IGTX500NameEntity.TITLE,
    IGTX500NameEntity.EMAIL_ADDRESS,
    IGTX500NameEntity.BUSINESS_CATEGORY,
    IGTX500NameEntity.TELEPHONE_NUMBER,
    IGTX500NameEntity.POSTAL_CODE,
    IGTX500NameEntity.UNKNOWN_ATTRIBUTE_TYPE,
  };
  
  private static Number[] _certDetailFields = 
  {
  	IGTCertificateEntity.START_DATE,
  	IGTCertificateEntity.END_DATE,
  	IGTCertificateEntity.SERIAL_NUM,
  };
  
  private static Number[] _certSwapDetailFields =
  {
	  IGTCertificateSwappingEntity.SWAP_DATE,
	  IGTCertificateSwappingEntity.SWAP_TIME
  };
  
  protected static final ITabDef[] _tabs = {
    new TabDef("certificate.tabs.certificate","main_tab"),
    new TabDef("certificate.tabs.issuerDetails","issuerDetails_tab"),
    new TabDef("certificate.tabs.subjectDetails","subjectDetails_tab"),
  };

  public CertificateRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
    
	  includeJavaScript(IGlobals.JS_DATE_TIME_PICKER);	
    	
      RenderingContext rContext = getRenderingContext();
      CertificateAForm form = (CertificateAForm) getActionForm();
      IGTCertificateEntity cert = (IGTCertificateEntity) getEntity();
      IGTCertificateSwappingEntity certSwap = (IGTCertificateSwappingEntity)cert.getFieldValue(IGTCertificateEntity.CERTIFICATE_SWAPPING);
      
      boolean importing = _edit && cert.isNewEntity();
      boolean updating = _edit && (!cert.isNewEntity());
      boolean viewing = _edit == false;
      boolean isPartner = StaticUtils.primitiveBooleanValue(form.getIsPartner());
      boolean isShowX500Name = form.isShowx500Name();
      boolean isX500Error = form.isX500Error(); //20030404AH
      
      FormFileElement[] certFile = (FormFileElement[])cert.getFieldValue(IGTCertificateEntity.CERT_FILE);
      boolean isCertFileAdded = form.isShowCertDetail();
      
      /*if(updating)
      { //Current requirements do not allow for editing of an existing certificate
        throw new java.lang.UnsupportedOperationException(
          "Updating of existing certificate entities is not supported");
      }*/

      
      renderCommonFormElements(cert.getType(), _edit);
      BindingFieldPropertyRenderer bfpr = renderFields(null,cert,_fields, this, form, ""); // 20040326 DDJ: Added IFliter

      //if( (!isPartner) && importing && cert.isNewEntity() ) //20030415AH
      if ( (!isPartner) && importing) 
      { //If its our own cert and we are importing we must render the PASSWORD field
        bfpr = renderFields(bfpr, cert, _ownFields);
        if (StaticUtils.stringNotEmpty(form.getPassword())) 
        { //Render text to indicate we are remembered an already typed pw
          renderLabel("password_entered", "certificate.password.entered", false);
        }
      }
      else
      { //Otherwise we dont use that field
        removeNode("password_details", false);
      }
      
      if(isPartner)
      {
      	renderField(bfpr, cert, IGTCertificateEntity.IS_CA);
      }
      else
      {
      	removeFields(cert, new Number[]{IGTCertificateEntity.IS_CA}, null);
      }
      
      //if(importing && cert.isNewEntity() ) //20030415AH
      if (importing) 
      { //If importing we render the CERT_FILE vField to allow file selection
        bfpr = renderFields(bfpr, cert, _certFile);
      }

      //if(viewing || (!cert.isNewEntity()) ) //20030415AH
      if (viewing || updating) 
      { //20040108NSL
        renderExportFile(rContext, form, cert); //20030127AH
      }

      if (isShowX500Name) 
      {
        IGTX500NameEntity issuerDetails = (IGTX500NameEntity) cert.
            getFieldValue(IGTCertificateEntity.ISSUER_DETAILS);
        bfpr = renderFields(bfpr, issuerDetails, _x500NameFields,
                            form.getIssuerDetails(), "issuerDetails.");
        renderLabel("issuerDetails_label", "certificate.issuerDetails", false);
        if (isX500Error) { //20030404AH
          renderLabelCarefully("issuerDetails_selectCert",
                               "certificate.issuerDetails.error", false);
        }
        else 
        {
          removeNode("issuerDetails_selectCert", false);
        }

        //IGTX500NameEntity subjectDetails = (IGTX500NameEntity) cert.
        //    getFieldValue(IGTCertificateEntity.SUBJECT_DETAILS);
        bfpr = renderFields(bfpr, issuerDetails, _x500NameFields,
                            form.getSubjectDetails(), "subjectDetails.");
        renderLabel("subjectDetails_label", "certificate.subjectDetails", false);
        if (isX500Error) 
        { //20030404AH
          renderLabelCarefully("subjectDetails_selectCert",
                               "certificate.subjectDetails.error", false);
        }
        else 
        {
          removeNode("subjectDetails_selectCert", false);
        }
      }
      else 
      {
        removeNode("issuerDetails_details", false);
        removeNode("subjectDetails_details", false);
        if (isX500Error) 
        { //20030404AH
          renderLabelCarefully("issuerDetails_selectCert",
                               "certificate.issuerDetails.error", false);
          renderLabelCarefully("subjectDetails_selectCert",
                               "certificate.subjectDetails.error", false);
        }
        else 
        {
          renderLabelCarefully("issuerDetails_selectCert",
                               "certificate.issuerDetails.select", false);
          renderLabelCarefully("subjectDetails_selectCert",
                               "certificate.subjectDetails.select", false);
        }
      }
      if(updating||viewing)
      {
	      if(certSwap!=null && cert.getFieldValue(IGTCertificateEntity.REPLACEMENT_CERT_UID)!=null)
	      {
	    	  //renderFields(bfpr, certSwap, _certSwapDetailFields);
	    	  bfpr = renderFields(bfpr,certSwap,_certSwapDetailFields);
	      }else
	      {
	    	  removeNode("swapDate_details", false);
	    	  removeNode("swapTime_details", false);
	      }

	      //bfpr = renderFields(bfpr,certSwap,_certSwapDetailFields);
      }
      else if(importing)
      {
    	  removeNode("swapDate_details", false);
    	  removeNode("swapTime_details", false);
      }
      
      //25072006
      if(isCertFileAdded)
      {
        renderFields(bfpr, cert, _certDetailFields);
      }
      else
      {
      	/*
      	removeNode("serialNum_details", false);
      	removeNode("startDate_details", false);
      	removeNode("endDate_details", false); */
      	removeFields(cert, _certDetailFields, null);
      }
      
      //TWX 28082006
      if(importing)
      {
      	removeFields(cert, new Number[]{IGTCertificateEntity.REPLACEMENT_CERT_UID},null);
      }
      
      renderTabs(rContext, "certificateTab", _tabs);
      
      //TWX 18072007:  Add in the pop up to notify the user if there is any input error from user 
      if(_edit)
      {
        includeJavaScript(IGlobals.JS_ENTITY_FORM_METHODS);
        appendEventMethod(getBodyNode(),"onload","tabErrorNotifier();");
      }
      
      removeNode("inTrustStore_details", false); //not implemented yet 20030415AH
    }
    catch (Throwable t) 
    {
      throw new RenderingException("Error rendering certificate screen", t);
    }

  }

  private void renderExportFile(RenderingContext rContext,
                                CertificateAForm form,
                                IGTCertificateEntity cert)
    throws RenderingException
  { //20030127AH
    try
    {
      //20030128AH - There isnt actually a form file element in the certFile field so we shall
      //for now hardcode it here. This code might belong better elsewhere (such as when initing the
      //form, but I dont have time to consider it properly right now!)
      FormFileElement ffe = new FormFileElement("0",EVIL_HARDCODED_CERT_FILENAME);
      FormFileElement[] formFileElements = { ffe };
      //...

      String dlhKey = StaticWebUtils.getDlhKey(cert, rContext.getOperationContext(), IGTCertificateEntity.CERT_FILE);
      MultifilesRenderer mfr = new MultifilesRenderer(rContext);
      mfr.setDlhKey(dlhKey);
      mfr.setDownloadable(true);
      mfr.setEntity(cert);
      mfr.setFieldId(IGTCertificateEntity.CERT_FILE);
      mfr.setFormFileElements(formFileElements);
      mfr.setViewOnly(true);
      mfr.setInsertId("certFile_value");
      mfr.setCollection(false);

      mfr.render(_target);

      renderLabel("certFile_label","certificate.certFile",false);

    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering field for certificate export",t);
    }
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    if(object instanceof IGTCertificateEntity)
    { // If we are filtering a partnerGroup check its partnerType matches that on the form
      boolean choiceIsPartner =
        StaticUtils.primitiveBooleanValue(((IGTCertificateEntity)object
        ).getFieldString(IGTCertificateEntity.IS_PARTNER));
      boolean entityIsPartner = StaticUtils.primitiveBooleanValue(((CertificateAForm
        )getActionForm()).getIsPartner());
      //return (choiceIsPartner == entityIsPartner); // 20040402 DDJ
      if(choiceIsPartner == entityIsPartner)
      {
        if(getEntity().isNewEntity())
        {
          return true;
        }
        else
        {
        	//TWX 28082006 if the replacement cert for currentCertEntity is same as the uid of certificates fetch from BL,
        	//that cert fetch from BL will not be displayed in the drop down list
        	IGTCertificateEntity currentCertEntity = (IGTCertificateEntity)getEntity();
        	String currentCertReplacementCertUID = currentCertEntity.getFieldString(IGTCertificateEntity.REPLACEMENT_CERT_UID);
        	String currentRelatedCertUID = currentCertEntity.getFieldString(IGTCertificateEntity.RELATED_CERT_UID);
          
        	IGTCertificateEntity certEntity = (IGTCertificateEntity)object;
        	String uid = certEntity.getFieldString(IGTCertificateEntity.UID);
        	boolean isReplacementCertEmpty = "".equals((String)certEntity.getFieldString(IGTCertificateEntity.REPLACEMENT_CERT_UID));
          boolean isEqualRelatedCertUID = uid.equals(currentRelatedCertUID);
          
          return isEqualRelatedCertUID || ( !(((IGTCertificateEntity)object).getUid() == getEntity().getUid()) && ! currentCertReplacementCertUID.equals(uid) && isReplacementCertEmpty); 
        }
      }
      else
      {
        return false;
      }
    }
    return true;
  }

/*
  // 20040326 DDJ
  public boolean allows(Object object, Object context) throws GTClientException
  {
    if(object instanceof IGTCertificateEntity)
    { // If we are filtering a partnerGroup check its partnerType matches that on the form
      boolean choiceIsPartner = StaticUtils.primitiveBooleanValue(((IGTCertificateEntity)object).getFieldString(IGTCertificateEntity.IS_PARTNER));
      boolean entityIsPartner = StaticUtils.primitiveBooleanValue(((CertificateAForm)getActionForm()).getIsPartner());
      return (choiceIsPartner == entityIsPartner);
    }
    return true;
  }
*/
}