/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityInfoRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 * 2002-10-08     Andrew Hill         "PartnerCat" stuff
 * 2006-04-25     Neo Sok Lay         Hide PartenrCat if NoP2P
 */
package com.gridnode.gtas.client.web.channel;

import org.w3c.dom.Node;

import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.*;
import com.gridnode.gtas.client.utils.*;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.GTClientException;

public class SecurityInfoRenderer extends AbstractRenderer implements IFilter
{
  private boolean _edit;
  private boolean _isPartner;

  protected static final Number[] fields = new Number[]
  {
    IGTSecurityInfoEntity.NAME,
    IGTSecurityInfoEntity.DESCRIPTION,
    IGTSecurityInfoEntity.ENC_TYPE,
    IGTSecurityInfoEntity.PARTNER_CAT,
    IGTSecurityInfoEntity.IS_PARTNER,
    IGTSecurityInfoEntity.REF_ID,
    IGTSecurityInfoEntity.SEQUENCE, // 20031126 DDJ
  };

  public SecurityInfoRenderer(RenderingContext rContext,
                          boolean edit)
  {
    super(rContext);
    _edit = edit;
  }

  protected void render() throws RenderingException
  {
    try
    {
      IGTSecurityInfoEntity sInfo = (IGTSecurityInfoEntity)getEntity();
      SecurityInfoAForm form = (SecurityInfoAForm)getActionForm();

      _isPartner = StaticUtils.primitiveBooleanValue( form.getIsPartner() );

      renderCommonFormElements(IGTEntity.ENTITY_SECURITY_INFO, _edit);
      renderLabel("encryption_heading","securityInfo.encryption");
      renderLabel("signature_heading","securityInfo.signature");
      renderLabel("compression_heading","securityInfo.compression");  // 20031126 DDJ

      BindingFieldPropertyRenderer bfpr = renderFields(null, sInfo, fields, this, form, "");
      if (isNoP2P())
      {
      	removeNode("partnerCategory_details");
      }
      
      if( form.encTypeHasValue() )
      {
        renderField(bfpr, sInfo, IGTSecurityInfoEntity.ENC_LEVEL);
        renderField(bfpr, sInfo, IGTSecurityInfoEntity.ENC_CERT);
        
        // 20031204 DDJ: Selectively display EncLevel based on EncType
        Node encLevelNode = getElementById("encLevel_value");
        if(IGTSecurityInfoEntity.ENC_TYPE_ASYMMETRIC.equals(form.getEncType()))
        {
          deleteElementByAttributeValue(IGTSecurityInfoEntity.ENC_LEVEL_40.toString(), "value", encLevelNode);
          deleteElementByAttributeValue(IGTSecurityInfoEntity.ENC_LEVEL_128.toString(), "value", encLevelNode);
          deleteElementByAttributeValue(IGTSecurityInfoEntity.ENC_LEVEL_168.toString(), "value", encLevelNode);
        }
        if(IGTSecurityInfoEntity.ENC_TYPE_SMIME.equals(form.getEncType()))
        {
          deleteElementByAttributeValue(IGTSecurityInfoEntity.ENC_LEVEL_256.toString(), "value", encLevelNode);
          deleteElementByAttributeValue(IGTSecurityInfoEntity.ENC_LEVEL_512.toString(), "value", encLevelNode);
          deleteElementByAttributeValue(IGTSecurityInfoEntity.ENC_LEVEL_1024.toString(), "value", encLevelNode);
        }
      }
      else
      {
        removeNode("encryptionAlgorithm_details");  
        removeNode("encLevel_details");
        removeNode("encCert_details");
      }
      
      // 20031126 DDJ: Added Encryption Algorithm which is displayed only for SMINE 
      if(IGTSecurityInfoEntity.ENC_TYPE_SMIME.equals(form.getEncType()))
      {
        renderField(bfpr, sInfo, IGTSecurityInfoEntity.ENCRYPTION_ALGORITHM);
      }
      else
      {
        removeNode("encryptionAlgorithm_details");  
      }

      if(_isPartner == false)
      { 
        removeNode("sequence_details"); // 20031126 DDJ  
        
        // If it is our own SecurityInfo, signature is not applicable 20021008AH
        removeNode("signature_details",true);
        removeNode("compression_details",true); // 20031126 DDJ
      }
      else
      {
        // 20031126 DDJ: Added Sequence, refactor to a shorter way
        Node sequenceNode = getElementById("sequence_value"); 
        boolean encrypt     = form.encTypeHasValue();
        boolean signature   = form.sigTypeHasValue();
        boolean compression = form.compressionTypeHasValue();
        if(encrypt && signature && compression)
        {
          deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_S_C, "value", sequenceNode);
          deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_C_S, "value", sequenceNode);
          deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_S_E, "value", sequenceNode);
          deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_E_S, "value", sequenceNode);
          deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_C_E, "value", sequenceNode);
        }
        else
        {
          deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_S_C_E, "value", sequenceNode);
          deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_C_S_E, "value", sequenceNode);
          deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_C_E_S, "value", sequenceNode);

          if(encrypt && signature)
          {
            deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_S_C, "value", sequenceNode);
            deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_C_S, "value", sequenceNode);
            deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_C_E, "value", sequenceNode);
          
          }
          else if(encrypt && compression)
          {
            deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_S_C, "value", sequenceNode);
            deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_C_S, "value", sequenceNode);
            deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_S_E, "value", sequenceNode);
            deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_E_S, "value", sequenceNode);
          }
          else if(signature && compression)
          {
            deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_S_E, "value", sequenceNode);
            deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_E_S, "value", sequenceNode);
            deleteElementByAttributeValue(IGTSecurityInfoEntity.SEQUENCE_C_E, "value", sequenceNode);
          }
          else
          {
            removeNode("sequence_details");  
          }
        }

        renderField(bfpr, sInfo, IGTSecurityInfoEntity.SIG_TYPE);
        if( form.sigTypeHasValue() )
        {
          renderField(bfpr, sInfo, IGTSecurityInfoEntity.DIGEST_ALGORITHM);
          renderField(bfpr, sInfo, IGTSecurityInfoEntity.SIG_ENC_CERT);
        }
        else
        {
          removeNode("digestAlgorithm_details");
          removeNode("sigEncCert_details");
        }

        // 20031126 DDJ: Added Compression subsection 
        renderField(bfpr, sInfo, IGTSecurityInfoEntity.COMPRESSION_TYPE);
        if( form.compressionTypeHasValue() )
        {
          renderField(bfpr, sInfo, IGTSecurityInfoEntity.COMPRESSION_METHOD);
          renderField(bfpr, sInfo, IGTSecurityInfoEntity.COMPRESSION_LEVEL);
        }
        else
        {
          removeNode("compressionMethod_details");
          removeNode("compressionLevel_details");
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering securityInfo screen",t);
    }
  }

  //NSL20060425
  protected boolean isNoP2P()
  {
  	IGTSession gtasSession = StaticWebUtils.getGridTalkSession(getRenderingContext().getRequest());
  	
  	return gtasSession.isNoP2P();
  }

  public boolean allows(Object object, Object context) throws GTClientException
  {
    // Filtering rules:
    // If we are a partner securityInfo then for the encryption certificate we allow selection of
    // certificates whose isPartner=true. If we are not a partner securityInfo then we only allow
    // selection of certs whose isPartner=false.
    // For signture cert we allow the selection of certs where isPartner=false if we are a partner
    // security info. If we are not a partner security info, selection of signature certs is not
    // applicable.
    BindingFieldPropertyRenderer bfpr = (BindingFieldPropertyRenderer)context;
    if(object instanceof IGTCertificateEntity)
    {
      Boolean isPartnerCert = (Boolean)((IGTCertificateEntity)
                          object).getFieldValue(IGTCertificateEntity.IS_PARTNER);
      if(isPartnerCert == null) throw new NullPointerException("isPartnerCert is null"); //20030416AH
      if(IGTSecurityInfoEntity.ENC_CERT.equals(bfpr.getFieldId()))
      {
        if(_isPartner)
        {
          return isPartnerCert.booleanValue();
        }
        else
        {
          return !(isPartnerCert.booleanValue());
        }
      }
      else if(IGTSecurityInfoEntity.SIG_ENC_CERT.equals(bfpr.getFieldId()))
      {
        return (_isPartner && (!isPartnerCert.booleanValue()));
      }
    }
    return true;
  }
  
  private void deleteElementByAttributeValue(String id, String attribute, Node node) throws RenderingException
  {
    Node toBeDeleted;
    toBeDeleted = getElementByAttributeValue(id, attribute, node);
    removeNode(toBeDeleted, false);
  }
}