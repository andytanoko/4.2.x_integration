/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CerificateListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-04-16     Andrew Hill         Created
 * 2003-07-03     Andrew Hill         Categorisation on isPartner
 */
package com.gridnode.gtas.client.web.channel;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.w3c.dom.Element;

import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.IURLRewriter;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;
import com.gridnode.gtas.client.web.strutsbase.EntityDispatchAction;

public class CertificateListDecoratorRenderer  extends AbstractRenderer
{
  private static final String EXPORT_KS_IMAGE_SRC = "images/actions/exportKs.gif";
  private static final String EXPORT_TS_IMAGE_SRC = "images/actions/exportTs.gif";
  private static final String EXPORT_KS_FWD_NAME = "exportKs";
  //private static final String EXPORT_TS_FWD_NAME = "exportTs";

  private Boolean _isPartner; //20030703AH
  private ActionMapping _mapping;

  public CertificateListDecoratorRenderer( RenderingContext rContext, ActionMapping mapping, Boolean isPartner)
  {
    super(rContext);
    if(mapping == null) throw new NullPointerException("mapping is null");
    _mapping = mapping;
    _isPartner = isPartner; //20030703AH (May be null)
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      IURLRewriter rewriter = rContext.getUrlRewriter();

      Element buttonCell = getElementById("button_cell",false);
      if(buttonCell != null)
      {

        ActionForward exportKsFwd = _mapping.findForward(EXPORT_KS_FWD_NAME);
        ActionForward exportTsFwd = _mapping.findForward(EXPORT_KS_FWD_NAME);

        /*20030703AH - co: if(!( (exportKsFwd == null) && (exportTsFwd == null) ))
        {
          //Insert a break or two to get the buttons on next line
          Node br = _target.createElement("br");
          buttonCell.appendChild(br);
          buttonCell.appendChild(br.cloneNode(true));
        }*/


        if(Boolean.FALSE.equals(_isPartner)) 
        { //20030703AH
          renderLabel("listview_heading","certificate.listview.heading.own");
          if(exportKsFwd != null) 
          {
            String exportKsMsg = rContext.getResourceLookup().getMessage("certificate.listview.exportKsConfirm");
            String exportKsUrl = rewriter.rewriteURL( exportKsFwd.getPath(), false );
            exportKsUrl = StaticWebUtils.addParameterToURL(exportKsUrl,
                                                          EntityDispatchAction.IS_NOT_ELV_DIVERT,
                                                          "true");
            Element exportKsLink = createButtonLink("exportKeyStore",
                                              "certificate.listview.exportKs",
                                              EXPORT_KS_IMAGE_SRC,
                                              "submitMultipleEntities('exportKeyStore','"
                                              + exportKsMsg + "','" + exportKsUrl + "');",
                                              true);
            buttonCell.appendChild(exportKsLink);
          }
        }

        if(Boolean.TRUE.equals(_isPartner)) 
        { //20030703AH
          renderLabel("listview_heading","certificate.listview.heading.partner");
          if(exportTsFwd != null ) 
          {
            String exportTsMsg = rContext.getResourceLookup().getMessage("certificate.listview.exportTsConfirm");
            String exportTsUrl = rewriter.rewriteURL( exportTsFwd.getPath(), false );
            exportTsUrl = StaticWebUtils.addParameterToURL(exportTsUrl,
                                                          EntityDispatchAction.IS_NOT_ELV_DIVERT,
                                                          "true");
            Element exportTsLink = createButtonLink("exportTrustStore",
                                              "certificate.listview.exportTs",
                                              EXPORT_TS_IMAGE_SRC,
                                              "submitMultipleEntities('exportTrustStore','"
                                              + exportTsMsg + "','" + exportTsUrl + "');",
                                              true);
            buttonCell.appendChild(exportTsLink);
          }
        }

      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error decorating Certificate listview",t);
    }
  }
}