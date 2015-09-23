/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateDispatchAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-20     Andrew Hill         Created
 * 2003-01-31     Andrew Hill         Expected error handling
 * 2003-04-04     Andrew Hill         Set flag in actionForm is x500 read had errors
 * 2003-04-15     Andrew Hill         Certificate renaming and exporting to store
 * 2004-03-26     Daniel D'Cotta      Added RELATED_CERT_UID
 * 2006-07-26     Tam Wei Xiang       Add SERIAL_NUM, START_DATE, END_DATE.
 *                                    Modified: initialiseActionForm, performUpdateProcessing (serverRefresh)
 * 2006-07-28     Tam Wei Xiang       Added isCA.
 *                                    Modified: getFormDocumentKey(...), updateEntityField(...)
 *                                    To handle error invalidCACert
 *                                    Modified: getActionError(...) , getErrorField(...) 
 * 2006-08-28     Tam Wei Xiang       Added REPLACEMENT_CERT_UID.
 *                                    Modified: initialiseActionForm(...)      
 * 2008-08-01	  Wong Yee Wah		 #38   Added method : validateDate(...)
 * 									  Added swapDate, swapTime
 * 									  Modified: initialiseActionForm(...)     
 * 									  Modified: validateActionForm(....)                                                                                           
 */
package com.gridnode.gtas.client.web.channel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.DateUtils;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.IDocumentRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.strutsbase.*;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.exceptions.IErrorCode;

public class CertificateDispatchAction extends EntityDispatchAction2
{
  protected String getEntityName()
  {
    return IGTEntity.ENTITY_CERTIFICATE;
  }

  protected IDocumentRenderer getFormRenderer(ActionContext actionContext,
                                              RenderingContext rContext,
                                              boolean edit)
    throws GTClientException
  {
    return new CertificateRenderer(rContext, edit);
  }


  protected String getFormDocumentKey(boolean edit, ActionContext actionContext)
    throws GTClientException
  {
    return (edit ? IDocumentKeys.CERTIFICATE_UPDATE : IDocumentKeys.CERTIFICATE_VIEW);
  }

  protected void initialiseActionForm(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTCertificateEntity cert = (IGTCertificateEntity)entity;
    IGTCertificateSwappingEntity certSwap = (IGTCertificateSwappingEntity)cert.getFieldValue(IGTCertificateEntity.CERTIFICATE_SWAPPING);
    CertificateAForm form = (CertificateAForm)actionContext.getActionForm();
    form.setId( cert.getFieldString(IGTCertificateEntity.ID) );
    form.setName( cert.getFieldString(IGTCertificateEntity.NAME) );
    form.setIsPartner( cert.getFieldString(IGTCertificateEntity.IS_PARTNER) );
    form.setRelatedCertUid( cert.getFieldString(IGTCertificateEntity.RELATED_CERT_UID) ); // 20040326 DDJ
    //form.setSerialNum(cert.getFieldString(IGTCertificateEntity.SERIAL_NUM));
    form.setPassword( null );
    form.setSwapDate(certSwap.getFieldString(IGTCertificateSwappingEntity.SWAP_DATE));
    form.setSwapTime(certSwap.getFieldString(IGTCertificateSwappingEntity.SWAP_TIME));

    if(cert.isNewEntity())
    {
      form.setShowX500Name(false);
    }
    else
    {
      form.setShowX500Name(true);
      IGTX500NameEntity issuerDetails = (IGTX500NameEntity)cert.getFieldValue(IGTCertificateEntity.ISSUER_DETAILS);
      initialiseX500NameForm(form.getIssuerDetails(), issuerDetails);
      IGTX500NameEntity subjectDetails = (IGTX500NameEntity)cert.getFieldValue(IGTCertificateEntity.SUBJECT_DETAILS);
      initialiseX500NameForm(form.getSubjectDetails(), subjectDetails);
      prepareCertificateDlh(actionContext, cert);
      
      //25072006 TWX
      form.setShowCertDetail(true);
      
      form.setSerialNum(cert.getFieldString(IGTCertificateEntity.SERIAL_NUM)); //24112006 RZ Change from getFieldValue to getFieldString
      form.setStartDate((Date)cert.getFieldValue(IGTCertificateEntity.START_DATE));
      form.setEndDate((Date)cert.getFieldValue(IGTCertificateEntity.END_DATE));
      
      form.setIsCA(((Boolean)cert.getFieldValue(IGTCertificateEntity.IS_CA)).toString());
      form.setReplacementCertUid(cert.getFieldString(IGTCertificateEntity.REPLACEMENT_CERT_UID));
    }
    form.setCertFileChanged(false);
  }

  protected void initialiseX500NameForm(X500NameAForm form, IGTX500NameEntity entity)
    throws GTClientException
  {
    form.setCountry( entity.getFieldString(IGTX500NameEntity.COUNTRY) );
    form.setState( entity.getFieldString(IGTX500NameEntity.STATE) );
    form.setOrganization( entity.getFieldString(IGTX500NameEntity.ORGANIZATION) );
    form.setLocality( entity.getFieldString(IGTX500NameEntity.LOCALITY) );
    form.setOrganizationalUnit( entity.getFieldString(IGTX500NameEntity.ORGANIZATIONAL_UNIT) );
    form.setStreetAddress( entity.getFieldString(IGTX500NameEntity.STREET_ADDRESS) );
    form.setCommonName( entity.getFieldString(IGTX500NameEntity.COMMON_NAME) );
    form.setTitle( entity.getFieldString(IGTX500NameEntity.TITLE) );
    form.setEmailAddress( entity.getFieldString(IGTX500NameEntity.EMAIL_ADDRESS) );
    form.setBusinessCategory( entity.getFieldString(IGTX500NameEntity.BUSINESS_CATEGORY) );
    form.setTelephoneNumber( entity.getFieldString(IGTX500NameEntity.TELEPHONE_NUMBER) );
    form.setPostalCode( entity.getFieldString(IGTX500NameEntity.POSTAL_CODE) );
    form.setUnknownAttributeType( entity.getFieldString(IGTX500NameEntity.UNKNOWN_ATTRIBUTE_TYPE) );
  }

  protected ActionForm createActionForm(ActionContext actionContext)
    throws GTClientException
  {
    CertificateAForm form = new CertificateAForm();
    return form;
  }

  protected int getIGTManagerType(ActionContext actionContext)
    throws GTClientException
  {
    return IGTManager.MANAGER_CERTIFICATE;
  }

  protected void validateActionForm(ActionContext actionContext,
                                    IGTEntity entity,
                                    ActionForm actionForm,
                                    ActionErrors errors)
    throws GTClientException
  {
    IGTCertificateEntity cert = (IGTCertificateEntity)entity;
    IGTCertificateSwappingEntity certSwap = (IGTCertificateSwappingEntity)cert.getFieldValue(IGTCertificateEntity.CERTIFICATE_SWAPPING);
    CertificateAForm form = (CertificateAForm)actionForm;

    basicValidateString(errors, IGTCertificateEntity.NAME, form, cert);

    if(cert.isNewEntity())
    { //20030415AH
      basicValidateString(errors, IGTCertificateEntity.IS_PARTNER, form, cert);

      basicValidateFiles(errors, IGTCertificateEntity.CERT_FILE, form, cert);

      boolean isPartner = StaticUtils.primitiveBooleanValue(form.getIsPartner());
      if(isPartner)
      {
        
      }
      else
      {
        basicValidateString(errors, IGTCertificateEntity.PASSWORD, form, cert);
      }
    }
    
    String swapDate = form.getSwapDate();
    String swapTime = form.getSwapTime();
    if((swapDate!=null && !swapDate.equals("")) || (swapTime!=null && !swapTime.equals("")))
    {
  	  basicValidateString       (errors, IGTCertificateSwappingEntity.SWAP_DATE, form, certSwap);
  	  basicValidateString       (errors, IGTCertificateSwappingEntity.SWAP_TIME, form, certSwap);
  	  validateDate(certSwap.getType(), "swapDate", form.getSwapDate(), "yyyy-MM-dd", errors);
  	  validateDate(certSwap.getType(), "swapTime",   form.getSwapTime(),   "HH:mm",  errors);
    }
  }

  protected void updateEntityFields(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
    IGTCertificateEntity cert = (IGTCertificateEntity)entity;
    IGTCertificateSwappingEntity certSwap = (IGTCertificateSwappingEntity)cert.getFieldValue(IGTCertificateEntity.CERTIFICATE_SWAPPING);
    CertificateAForm form = (CertificateAForm)actionContext.getActionForm();

    cert.setFieldValue( IGTCertificateEntity.NAME, form.getName() );
    cert.setFieldValue( IGTCertificateEntity.RELATED_CERT_UID, StaticUtils.longValue(form.getRelatedCertUid()) ); // 20040326 DDJ
    
    //TWX 20060728
    cert.setFieldValue(IGTCertificateEntity.IS_CA, StaticUtils.booleanValue(form.getIsCA()));
    
    //WYW 20070721
    certSwap.setFieldValue(IGTCertificateSwappingEntity.SWAP_DATE, form.getSwapDate());
    certSwap.setFieldValue(IGTCertificateSwappingEntity.SWAP_TIME, form.getSwapTime());
    
    if(cert.isNewEntity())
    { //20030415AH
      cert.setFieldValue( IGTCertificateEntity.IS_PARTNER, StaticUtils.booleanValue(form.getIsPartner()) );
      cert.setFieldValue( IGTCertificateEntity.PASSWORD, form.getPassword() );
      transferFieldFiles(actionContext, cert, IGTCertificateEntity.CERT_FILE, false );
    }
  }

  protected void performUpdateProcessing(ActionContext actionContext)
    throws GTClientException
  {
    CertificateAForm form = (CertificateAForm)actionContext.getActionForm();
    IGTCertificateEntity cert = (IGTCertificateEntity)getEntity(actionContext); //twx

    if(form.isCertFileChanged())
    { //try to get the x500 stuff here
      try
      {
      	//26072006 TWX moved from method updateX500NameDetails
      	boolean tfrAny = transferFieldFiles( actionContext, cert, IGTCertificateEntity.CERT_FILE, false );
        form.setX500Error(false); //20030404AH
        form.setShowX500Name( updateX500NameDetails(actionContext, tfrAny) );
        
        //twx determine whether user add/remove any cert file.
        boolean isShowCertDetail = tfrAny;
        form.setShowCertDetail(isShowCertDetail);
        
        if(isShowCertDetail)
        {
        	form.setSerialNum(cert.getFieldString(IGTCertificateEntity.SERIAL_NUM)); //24112006 RZ Change from getFieldValue to getFieldString
        	form.setStartDate((Date)cert.getFieldValue(IGTCertificateEntity.START_DATE));
        	form.setEndDate((Date)cert.getFieldValue(IGTCertificateEntity.END_DATE));
        }
        else
        {
        	form.setSerialNum(null);
        	form.setStartDate(null);
        	form.setEndDate(null);
        }
      }
      catch(IncompleteFieldsException t)
      {
        form.setShowX500Name(false);
      }
      catch(Throwable t)
      {
        if(!handleCertificateError(actionContext, t))
        { //20030130AH
          throw new GTClientException("Error obtaining certificate X500Name details",t);
        }
        else
        {
          form.setShowX500Name(false);
          form.setX500Error(true); //20030404AH
        }
      }
      form.setCertFileChanged(false);
    }
    
    //
  }

  protected boolean updateX500NameDetails(ActionContext actionContext, boolean tfrAny)
    throws GTClientException
  {
    IGTCertificateEntity cert = (IGTCertificateEntity)getEntity(actionContext);
    CertificateAForm form = (CertificateAForm)actionContext.getActionForm();

    cert.setFieldValue( IGTCertificateEntity.IS_PARTNER, StaticUtils.booleanValue(form.getIsPartner()) );
    cert.setFieldValue( IGTCertificateEntity.PASSWORD, form.getPassword() );
    
    //twx
    //boolean tfrAny = transferFieldFiles( actionContext, cert, IGTCertificateEntity.CERT_FILE, false );
    
    if(tfrAny)
    { //Prompt the loading to occur now so we can catch any errors more easily
      //@todo: catch errors... ;-)
      IGTX500NameEntity issuerDetails = (IGTX500NameEntity)cert.getFieldValue(IGTCertificateEntity.ISSUER_DETAILS);
      initialiseX500NameForm(form.getIssuerDetails(), issuerDetails);
      IGTX500NameEntity subjectDetails = (IGTX500NameEntity)cert.getFieldValue(IGTCertificateEntity.SUBJECT_DETAILS);
      initialiseX500NameForm(form.getSubjectDetails(), subjectDetails);
      //if couldnt get details execution wont reach here - an IncompleteFieldsException thrown already
      return true;
    }
    else
    {
      form.clearCertificateDetails();
      return false;
    }
  }

  protected void initialiseNewEntity(ActionContext actionContext, IGTEntity entity)
    throws GTClientException
  {
	  /*
	IGTCertificateEntity cert = (IGTCertificateEntity)entity;
	IGTCertificateSwappingEntity certSwap = (IGTCertificateSwappingEntity)cert.getFieldValue(IGTCertificateEntity.CERTIFICATE_SWAPPING);
	certSwap.setFieldValue(IGTCertificateSwappingEntity.SWAP_DATE, null);
    certSwap.setFieldValue(IGTCertificateSwappingEntity.SWAP_TIME, "");
    */
    entity.setFieldValue(IGTCertificateEntity.IS_PARTNER, Boolean.TRUE);
    //entity.setFieldValue(IGTCertificateEntity.CERTIFICATE_SWAPPING, certSwap);
  }

  protected void prepareCertificateDlh(ActionContext actionContext, IGTCertificateEntity cert)
    throws GTClientException
  {
    try
    {
      if(cert.isNewEntity()) return; //No download for new ones.
      //(nb: if mfr refactor to allow then may need to change)

      OperationContext opCon = OperationContext.getOperationContext(actionContext.getRequest());
      String dlhKey = StaticWebUtils.getDlhKey(cert, opCon, IGTCertificateEntity.CERT_FILE);
      CertificateDownloadHelper certDlh = (CertificateDownloadHelper)actionContext.getAttribute(dlhKey);
      if(certDlh == null)
      {
        certDlh = new CertificateDownloadHelper(cert);
        //Remind me again why we dont put the dlHelpers in actionContext and do all this stuff with
        //the session? There was a reason for it, but I cant remember what it is so cant judge
        //if it was a good reason or not! 20030128AH
        actionContext.getSession().setAttribute(dlhKey, certDlh);
        addOpReq(actionContext, new SessionCleanupOpReq(dlhKey) );
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting CertificateDownloadHelper",t);
    }
  }

  private boolean handleCertificateError(ActionContext actionContext, Throwable t)
    throws GTClientException
  { //20030131AH
    if(t instanceof GTClientException)
    {
      Throwable rootEx = ((GTClientException)t).getRootException();
      if(rootEx instanceof ResponseException)
      {
        ResponseException rex = (ResponseException)rootEx;
        ActionErrors  errors = new ActionErrors();
        String errorField = getErrorField(rex);
        ActionError error = getActionError(rex);
        errors.add(errorField,error);
        saveErrors(actionContext.getRequest(), errors);
        return true;
      }
      else
      {
        return false;
      }
    }
    return false;
  }

  protected String getErrorField(Throwable t)
  { //20030131AH
    if( (t != null) && (t instanceof ResponseException) )
    {
      ResponseException rex = (ResponseException)t;
      int code = rex.getErrorCode();
      switch(code)
      {
        case IErrorCode.INVALID_FILETYPE_ERROR:
        case IErrorCode.INVALID_PASSWORD_OR_FILETYPE_ERROR:
        case IErrorCode.DUPLICATE_CERTIFICATE_IMPORT_ERROR:
        case IErrorCode.INVALID_CA_CERTIFICATE_ERROR:	
          return "certFile";
      }
    }
    return super.getErrorField(t);
  }

  protected ActionError getActionError(ResponseException rex)
  { //20030131AH
    int code = rex.getErrorCode();
    switch(code)
    {
      case IErrorCode.INVALID_FILETYPE_ERROR:
        return EntityFieldValidator.addFieldError(null, "certFile", "certificate", "invalid", null);

      case IErrorCode.INVALID_PASSWORD_OR_FILETYPE_ERROR:
        return EntityFieldValidator.addFieldError(null, "certFile", "certificate", "invalidFileOrPassword", null);

      case IErrorCode.DUPLICATE_CERTIFICATE_IMPORT_ERROR:
        return EntityFieldValidator.addFieldError(null, "certFile", "certificate", "duplicate", null);
      
      case IErrorCode.INVALID_CA_CERTIFICATE_ERROR:
      	return EntityFieldValidator.addFieldError(null,"certFile", "certificate", "invalidCA", null);
    }
    return super.getActionError(rex);
  }

  protected boolean handleSaveException(Throwable saveException,
                                        ActionContext actionContext,
                                        IGTManager manager,
                                        IGTEntity entity,
                                        ActionErrors actionErrors)
    throws GTClientException
  {
    CertificateAForm form = (CertificateAForm)actionContext.getActionForm();
    form.setShowX500Name(false);
    form.setCertFileChanged(false);
    return super.handleSaveException(saveException,actionContext,manager,entity,actionErrors);
  }

  private long[] getExportIds(ActionContext actionContext)
    throws GTClientException
  { //20030415AH
    try
    {
      String[] uids = getDeleteIds(actionContext); //@todo: differentiate based on where we requested from
      long[] uidLongs = StaticUtils.primitiveLongArrayValue(uids,true); //20030424AH
      return uidLongs;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting uids of certificates for export",t);
    }
  }

  public ActionForward exportTrustStore(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws IOException, ServletException, GTClientException
  { //20030415AH
    try
    {
      ActionContext actionContext = new ActionContext(mapping,actionForm,request,response);
      long[] uids = getExportIds(actionContext);
      ActionForward forward = getDeleteReturnForward(actionContext);
      if( !((uids == null) || (uids.length == 0)) )
      {
        IGTSession gtasSession = getGridTalkSession(actionContext);
        IGTCertificateManager manager = (IGTCertificateManager)gtasSession.getManager(IGTManager.MANAGER_CERTIFICATE);

        for(int i=0; i < uids.length; i++)
        {
          manager.exportTrustStore(new Long(uids[i]));
        }
      }
      return forward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error exporting certificates to trust store",t);
    }
  }

  public ActionForward exportKeyStore(ActionMapping mapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
                                throws IOException, ServletException, GTClientException
  { //20030415AH
    try
    {
      ActionContext actionContext = new ActionContext(mapping,actionForm,request,response);
      long[] uids = getExportIds(actionContext);
      ActionForward forward = getDeleteReturnForward(actionContext);
      if( !((uids == null) || (uids.length == 0)) )
      {
        IGTSession gtasSession = getGridTalkSession(actionContext);
        IGTCertificateManager manager = (IGTCertificateManager)gtasSession.getManager(IGTManager.MANAGER_CERTIFICATE);

        manager.exportKeyStore(new Long(uids[0])); // there can be only one!
      }
      return forward;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error exporting certificate to key store",t);
    }
  }
  
  protected void validateDate(
		    String entityType,
		    String field,
		    String value,
		    String format,
		    ActionErrors actionErrors)
  {
    Date date = null;
    if (!value.equals(""))
    {         
      date = DateUtils.parseDate(value, null, null, new SimpleDateFormat(format));
    
      if (date == null)
      {
        EntityFieldValidator.addFieldError(
          actionErrors,
          field,
          entityType,
          EntityFieldValidator.INVALID,
          null);
      }
    }else
    {
    	 EntityFieldValidator.addFieldError(
    	          actionErrors,
    	          field,
    	          entityType,
    	          EntityFieldValidator.REQUIRED,
    	          null);
    }
  }
}