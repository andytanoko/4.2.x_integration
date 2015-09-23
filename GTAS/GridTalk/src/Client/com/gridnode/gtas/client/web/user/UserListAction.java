/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-16     Andrew Hill         Created
 * 2002-06-03     Andrew Hill         Refactored to use new xmlc-free view framework
 * 2002-06-22     Andrew Hill         Refactored to use EntityListAction
 * 2002-11-25     Andrew Hill         Now under navgroup_server
 */
package com.gridnode.gtas.client.web.user;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.strutsbase.*;
import com.gridnode.gtas.client.GTClientException;

public class UserListAction extends EntityListAction
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_server";
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    Object[] columns = {  IGTUserEntity.USER_ID,
                          IGTUserEntity.USER_NAME,
                          IGTUserEntity.EMAIL,
                          IGTUserEntity.PHONE,
                          "accountState.state",
                          "accountState.created",
                       };
    return columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_USER;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return "user";
  }
}


//import org.apache.struts.action.ActionMapping;
//import org.apache.struts.action.ActionForm;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import org.apache.struts.action.ActionForward;
//import javax.servlet.ServletException;
//import java.io.IOException;
//import java.util.Collection;
//import org.apache.struts.util.MessageResources;
//import java.util.Locale;
//
//import com.gridnode.gtas.client.web.strutsbase.GTActionBase;
//import com.gridnode.gtas.client.GTClientException;
//import com.gridnode.gtas.client.ctrl.*;
//import com.gridnode.gtas.client.web.ISessionKeys;
//import com.gridnode.gtas.client.web.IRequestKeys;
//import com.gridnode.gtas.client.web.renderers.ColumnEntityAdapter;
//import com.gridnode.gtas.client.web.renderers.IColumnEntityAdapter;
//import com.gridnode.gtas.client.web.renderers.*;
//import com.gridnode.gtas.client.web.xml.*;
//
//public class UserListAction extends GTActionBase
//{
//  protected static final int _managerType = IGTManager.MANAGER_USER;
//
//  protected static final Number[] _columns = {  IGTUserEntity.USER_ID,
//                                              IGTUserEntity.USER_NAME,
//                                              IGTUserEntity.EMAIL,
//                                              IGTUserEntity.PHONE,
//                                            };
//
//
//  public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
//                                HttpServletRequest request, HttpServletResponse response)
//                                throws IOException, ServletException
//  {
//    try
//    {
//      Locale locale = getLocale(request);
//      MessageResources messageResources = getResources(request);
//
//      HttpSession httpSession = request.getSession();
//      IGTSession gtSession = getGridTalkSession(httpSession);
//      IGTManager manager = gtSession.getManager(_managerType);
//      Collection listItems = manager.getAll();
//      request.setAttribute(IRequestKeys.ENTITY_LIST, listItems);
//      ListViewOptionsImpl listOptions = new ListViewOptionsImpl();
//      IColumnEntityAdapter adapter = new ColumnEntityAdapter(_columns);
//      listOptions.setColumnAdapter(adapter);
//      ActionForward viewUserForward = mapping.findForward("invokeViewUser");
//      ActionForward updateUserForward = mapping.findForward("invokeUpdateUser");
//      ActionForward createUserForward = mapping.findForward("invokeCreateUser");
//      ActionForward deleteUserForward = mapping.findForward("invokeDeleteUsers");
//      listOptions.setViewURL(viewUserForward.getPath());
//      listOptions.setCreateURL(createUserForward.getPath());
//      listOptions.setUpdateURL(updateUserForward.getPath());
//      listOptions.setDeleteURL(deleteUserForward.getPath());
//      listOptions.setCreateLabelKey("user.listview.create");
//      listOptions.setDeleteLabelKey("user.listview.delete");
//      listOptions.setConfirmDeleteMsgKey("user.listview.confirmDelete");
//      listOptions.setHeadingLabelKey("user.listview.heading");
//
//      // Create a simple resource lookup object to resolve 118n keys into associated messages
//      ISimpleResourceLookup rLookup = new SimpleResourceLookup(locale, messageResources);
//
//      IDocumentManager docMgr = getDocumentManager();
//      IURLRewriter urlRewriter = new URLRewriterImpl(response, request.getContextPath());
//      TemplateRenderer templateRenderer = new TemplateRenderer(IDocumentKeys.TEMPLATE_LISTVIEW,
//                                                          null,docMgr,"listview_content",null);
//      BaseTagRenderer baseTagRenderer = new BaseTagRenderer(request, "base_tag");
//      ListViewRenderer listViewRenderer = new ListViewRenderer(listOptions,urlRewriter
//                                                          ,listItems, rLookup);
//      RenderingPipelineImpl rPipe = new RenderingPipelineImpl(IDocumentKeys.TEMPLATE, docMgr);
//      rPipe.addRenderer(templateRenderer);
//      rPipe.addRenderer(baseTagRenderer);
//      rPipe.addRenderer(listViewRenderer);
//      request.setAttribute(IRequestKeys.RENDERERS, rPipe);
//
//      return mapping.findForward("view");
//    }
//    catch(Exception exception)
//    {
//      throw new ServletException("Error preparing user list view for rendering",exception);
//    }
//  }
//}