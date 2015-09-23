/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-12     Andrew Hill         Created
 * 2003-04-14     Andrew Hill         Do setMagicDownloadsEnabled(false) to renderer
 * 2003-04-16     Andrew Hill         Add decorator to pipeline
 * 2003-07-03     Andrew Hill         Support categorisation based on isPartner
 * 2006-07-28     Tam Wei Xiang       New partnerColumns isCA
 */
package com.gridnode.gtas.client.web.channel;

import java.util.*;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.strutsbase.*;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.renderers.*;

public class CertificateListAction extends EntityListAction
{
  public static final String IS_PARTNER = "isPartner"; //20030703AH

  private static final Object[] _columns = new Object[]
  {
    IGTCertificateEntity.NAME,
    IGTCertificateEntity.ID,
    IGTCertificateEntity.IS_PARTNER,
    IGTCertificateEntity.IS_IN_KS,
    IGTCertificateEntity.IS_IN_TS,
  }; //20030117AH - Made static

  private static final Object[] _ownColumns = new Object[]
  { //20030703AH
    IGTCertificateEntity.NAME,
    IGTCertificateEntity.ID,
    IGTCertificateEntity.IS_IN_KS,
  };

  private static final Object[] _partnerColumns = new Object[]
  { //20030703AH
    IGTCertificateEntity.NAME,
    IGTCertificateEntity.ID,
    IGTCertificateEntity.IS_CA,
    IGTCertificateEntity.IS_IN_TS,
  };

  protected Object[] getColumnReferences(ActionContext actionContext)
  { //20030117AH - Change to newer methodology, 20030703AH - Choose columns based on isPartner
    Boolean isPartner = isPartner(actionContext);
    if(isPartner == null)
    {
      return _columns;
    }
    else
    {
      return isPartner.booleanValue() ? _partnerColumns : _ownColumns;
    }
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_CERTIFICATE;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "certificate";
  }

  protected IDocumentRenderer getListViewRenderer(ActionContext actionContext,
                                                  RenderingContext rContext,
                                                  IListViewOptions listOptions,
                                                  Collection listItems)
    throws GTClientException
  { //20030414AH
    //Cert files need a dlh which isnt supported (yet) by listview magic download rendering so we
    //disable this feature for the cert listview. Currently cert listview doesnt have column for
    //this anyway - but its best to be explicit...
    IDocumentRenderer lvr = super.getListViewRenderer(actionContext,rContext,listOptions,listItems);
    if(lvr instanceof ListViewRenderer) ((ListViewRenderer)lvr).setMagicDownloadsEnabled(false);
    return lvr;
  }

  protected void processPipeline( ActionContext actionContext,
                                  RenderingContext rContext,
                                  IRenderingPipeline rPipe)
    throws GTClientException
  { //20030416AH
    Boolean isPartner = isPartner(actionContext); //20030703AH
    CertificateListDecoratorRenderer cldr = new CertificateListDecoratorRenderer(rContext,
                                                   actionContext.getMapping(), isPartner ); //20030703AH
    rPipe.addRenderer(cldr);
    super.processPipeline(actionContext, rContext, rPipe);
  }

  private Boolean isPartner(ActionContext actionContext)
  { //20030703AH
    String isPartnerString = actionContext.getRequest().getParameter(IS_PARTNER);
    Boolean isPartner = StaticUtils.stringEmpty(isPartnerString) ? null : StaticUtils.booleanValue( isPartnerString );
    if(isPartner == null)
    {
      isPartner = (Boolean)actionContext.getRequest().getAttribute(IS_PARTNER);
    }
    return isPartner;
  }

  protected String appendRefreshParameters(ActionContext actionContext, String refreshUrl)
      throws GTClientException
  { //20030703AH
    Boolean isPartner = isPartner(actionContext);
    if(isPartner != null)
    {
      refreshUrl = StaticWebUtils.addParameterToURL(refreshUrl,IS_PARTNER,"" + isPartner);
    }
    return refreshUrl;
  }

  protected IGTListPager getListPager(ActionContext actionContext)
      throws GTClientException
  { //20030703AH
    IGTCertificateManager manager = (IGTCertificateManager)getManager(actionContext);
    Boolean isPartner = isPartner(actionContext);
    return manager.getListPager(isPartner);
  }
}