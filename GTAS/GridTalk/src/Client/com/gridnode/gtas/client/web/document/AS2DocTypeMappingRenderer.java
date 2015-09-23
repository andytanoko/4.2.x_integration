/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-10-17    Wong Yee Wah         Created
 */
package com.gridnode.gtas.client.web.document;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTAS2DocTypeMappingEntity;
import com.gridnode.gtas.client.ctrl.IGTBizCertMappingEntity;
import com.gridnode.gtas.client.ctrl.IGTAS2DocTypeMappingManager;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.BindingFieldPropertyRenderer;
import com.gridnode.gtas.client.web.renderers.IBFPROptionSource;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class AS2DocTypeMappingRenderer extends AbstractRenderer implements IBFPROptionSource
{
  
  private boolean _edit;
  private IGTAS2DocTypeMappingManager _admManager;
  private IGTAS2DocTypeMappingEntity _adm;
 

  protected static final Number[] fields = new Number[]{IGTAS2DocTypeMappingEntity.AS2_DOC_TYPE,
                                                        IGTAS2DocTypeMappingEntity.DOC_TYPE,
                                                        IGTAS2DocTypeMappingEntity.PARTNER_ID };
  
  public AS2DocTypeMappingRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
  }
  
  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      IGTAS2DocTypeMappingEntity adm = (IGTAS2DocTypeMappingEntity)getEntity();
      _adm = adm;
      
      
      IGTSession gtasSession = adm.getSession();
      _admManager = (IGTAS2DocTypeMappingManager)
                    gtasSession.getManager(IGTManager.MANAGER_AS2_DOC_TYPE_MAPPING);

      renderCommonFormElements(adm.getType(),_edit);
      
      boolean creating    = _edit && adm.isNewEntity();
      boolean updating    = _edit && (!adm.isNewEntity());
      boolean viewing     = _edit == false;
      
      BindingFieldPropertyRenderer bfpr = new BindingFieldPropertyRenderer(rContext);
      bfpr.setOptionSource(this);
      
      if(updating)
      { 
//        replaceValueNodeWithNewElement("span",IGTBizCertMappingEntity.PARTNER_ID,false,false);
//        removeNode("partnerId_create",false);
      }
      
      bfpr = renderFields(bfpr,adm,fields);
      
      if(_edit)
      { 
        renderLabel("partnerId_create","as2DocTypeMapping.partnerId.create",false);
        renderLabel("as2DocTypeMapping_create","as2DocTypeMapping.as2DocTypeMapping.create",false);
        renderLabel("docType_create","as2DocTypeMapping.docType.create",false);
      }
   
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering as2DocTypeMapping screen",t);
    }
  }
  
  public Collection getOptions(RenderingContext rContext, BindingFieldPropertyRenderer bfpr)
  throws GTClientException
  {
   
    IGTEntity entity = bfpr.getBoundEntity();
    Number fieldId = bfpr.getFieldId();
  
    if(_adm != entity) return null; //Same instance? - no then not this sources job...
    if(IGTAS2DocTypeMappingEntity.PARTNER_ID.equals(fieldId))
    {
      System.out.println("AS2DocTypeMapping>getoption1");
      Collection partners = _admManager.getApplicablePartnerList();
      return partners;
    }
    else if(IGTAS2DocTypeMappingEntity.DOC_TYPE.equals(fieldId))
    {
      System.out.println("AS2DocTypeMapping>getoption1");
      return _admManager.getApplicableDocumentTypeList();
    }
    else
    {
      return null; //Default option handling please....
    }
  }
}
