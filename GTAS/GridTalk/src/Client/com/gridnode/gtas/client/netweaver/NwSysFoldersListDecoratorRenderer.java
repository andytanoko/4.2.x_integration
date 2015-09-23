/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SysFoldersListDecoratorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-28     Andrew Hill         Created
 * 2002-12-10     Andrew Hill         Show folder specific listview heading
 * 2003-03-21     Andrew Hill         Make path to import gif a constant
 * 2003-03-25     Andrew Hill         Use createButtonLink() to make send link button
 * 2004-10-15     Daniel D'Cotta      Added Manual Export link for Import and Inbound folder
 * 2005-03-15     Andrew Hill         Hide certain features from users without admin access
 * 2005-11-10	    Sumedh C.						Disable export link for netweaver porting.
 */
package com.gridnode.gtas.client.netweaver;

import javax.servlet.http.HttpSession;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTGridDocumentEntity;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.ctrl.IGTUserEntity;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class NwSysFoldersListDecoratorRenderer  extends AbstractRenderer
{
	private static final String GTAS_IMAGE_SRC = "images/gridNodeLogo16.gif";
	
  private static final String IMPORT_IMAGE_SRC = "images/actions/import.gif"; //2003021AH
  private static final String EXPORT_IMAGE_SRC = "images/actions/export.gif"; // 20031015 DDJ
  private static final String SEND_IMAGE_SRC = "images/actions/send.gif"; //2003025AH
  String _folder;

  public NwSysFoldersListDecoratorRenderer( RenderingContext rContext,
                                          String folder)
  {
    super(rContext);
    _folder = folder;
  }

  protected void render() throws RenderingException
  {
    RenderingContext rContext = getRenderingContext();
    //20050315AH - Check for admin rights and remove certain features if user lacks it
    IGTSession gtasSession = StaticWebUtils.getGridTalkSession(rContext.getRequest());
    boolean showAdminButtons = gtasSession.isAdmin();
    if(!showAdminButtons)
    { //20050315
      removeNode("delete_details",false);
      removeNode("create_details",false);
    }
    
    if(null == _folder)
    {
      // manual import
      if(showAdminButtons)
      {
        appendParameter("create","href","isManualImport","true",false);
        renderElementAttribute("create_icon","src",IMPORT_IMAGE_SRC);//20021212AH, 20030321AH
      }
    }
    else if(IGTGridDocumentEntity.FOLDER_IMPORT.equalsIgnoreCase(_folder))
    {
      // manual import
      if(showAdminButtons)
      {
        appendParameter("create","href","isManualImport","true",false);
        renderElementAttribute("create_icon","src",IMPORT_IMAGE_SRC);//20021212AH, 20030321AH
      
        // send
        String sendSubmitMsg = rContext.getResourceLookup().getMessage("gridDocument.listview.confirmSend");
        Element sendLinkButton = createButtonLink("send",
                                                  "gridDocument.listview.send",
                                                  SEND_IMAGE_SRC,
                                                  "submitMultipleEntities('send','" + sendSubmitMsg + "');",
                                                  true); //20030325AH
        //delete
        Node deleteLink = getElementById("delete_submit",true);
        deleteLink.getParentNode().insertBefore(sendLinkButton,deleteLink);
        
        // manual export
        String exportSubmitMsg = rContext.getResourceLookup().getMessage("gridDocument.listview.confirmExport");
        Element exportLinkButton = createButtonLink("export",
                                                    "gridDocument.listview.export",
                                                    EXPORT_IMAGE_SRC,
                                                    "submitMultipleEntities('export','" + exportSubmitMsg + "');",
                                                    true);

        Node importLink = getElementById("create", true);
        importLink.getParentNode().appendChild(exportLinkButton);
      }
    }
    else if(IGTGridDocumentEntity.FOLDER_INBOUND.equalsIgnoreCase(_folder))
    {
      // inbound import
      removeNode("create",false); // for now we havent implemented so hide link.
      
      removeButtons();
      
      /* remove view img tags */
      Util.removeAllViewImgTags(_target);
      
      /* remove anchor tags */
      Util.removeAnchorTags(_target);
      
      /* remove pager cell: relating to paging capability */
      removeNode("pager_cell");
      
      /* remove checkbox */
      Util.removeAllCheckBox(_target);
      
      fixForNetweaverTheme();
      
      addOpenNewWindowButton();
      
      /* Disable export link for netweaver porting. */
//      // manual export
//      if(showAdminButtons)
//      {
//        Node deleteLink = getElementById("delete_submit",true);
//  
//        String exportSubmitMsg = rContext.getResourceLookup().getMessage("gridDocument.listview.confirmExport");
//        Element exportLinkButton = createButtonLink("export",
//                                                    "gridDocument.listview.export",
//                                                    EXPORT_IMAGE_SRC,
//                                                    "submitMultipleEntities('export','" + exportSubmitMsg + "');",
//                                                    true);
//  
//        deleteLink.getParentNode().appendChild(exportLinkButton);
//      }
    }
    else
    {
      // no import
      removeNode("create",false);
    }
    if(_folder != null)
    { //20021210AH - Ensure appropriate heading rendered based on folder
      renderLabel("listview_heading","gridDocument.listview.heading." + _folder.toLowerCase(),false);
    }
  }
  
  private void log(String message)
  {
  	com.gridnode.gtas.client.netweaver.helper.Logger.debug("[NwSysFoldersListDecoratorRenderer]" + message);
  }
  
  private void removeButtons() throws RenderingException
  {
  	removeNode("refresh_details");
  	removeNode("delete_details");
  }
  
  private void fixForNetweaverTheme() throws RenderingException
  {
  	/* remove css */
    removeNode("xhtml_stylesheet", true);
    
    /* remove background because background contains white area */
    Element body = getElementById("xhtml_body");
    body.removeAttribute("background");
    body.removeAttribute("bgproperties");
  }
  
  private void addOpenNewWindowButton()
  {
  	Element a = _target.createElement("a");
  	HttpSession session = getRenderingContext().getSession();
  	String username = (String) session.getAttribute("nw_username");
  	String password = (String) session.getAttribute("nw_password");
  	String href = "login.do?username=" + username + "&password=" + password;
  	a.setAttribute("href", href);
  	a.setAttribute("target", "_blank");
  	Text text = _target.createTextNode("Launch GridTalk");
  	a.appendChild(text);
  	Element buttonCell = getElementById("button_cell");
  	buttonCell.appendChild(a);
  }
}