/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InitialisationServlet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 04 2003    Neo Sok Lay         Created
 * Jan 31 2007    Neo Sok Lay         Change InitialisationNotifier interface.
 */
package com.gridnode.pdip.framework.init;

import javax.naming.InitialContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
 
public class InitialisationServlet extends HttpServlet
{
//  IDomainConfigHome _domainConfigHome = null;
//  IDomainConfigObj _domainConfigObj = null;

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1410322581403897840L;

	public void init() throws ServletException
  {
    try
    {
      System.out.println("[InitialisationServlet.init] Start initialising the application...");
      super.init();
      ServletConfig ctx = getServletConfig();
      InitialContext ic = getInitialContext(ctx);
      initialiseDomain(ctx);
      instructInitialisation(ic);
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      throw new ServletException("[InitialisationServlet.init] Error ", ex);
    }
  }

  private InitialContext getInitialContext(ServletConfig ctx) throws Exception
  {
//    Properties props = new Properties();
//    props.setProperty(InitialContext.INITIAL_CONTEXT_FACTORY, ctx.getInitParameter("INITIAL_CONTEXT_FACTORY"));
//    props.setProperty(InitialContext.PROVIDER_URL, ctx.getInitParameter("PROVIDER_URL"));
//    props.setProperty(InitialContext.URL_PKG_PREFIXES, ctx.getInitParameter("URL_PKG_PREFIXES"));
//    return new InitialContext(props);
    return new InitialContext();
  }

  private void instructInitialisation(InitialContext ctx) throws Exception
  {
    //InitialisationNotifier.getInstance(ctx).broadcastInitialisation();
    InitialisationNotifier notifier = new InitialisationNotifier(ctx);
    notifier.broadcastInitialisation();
  }

  private void initialiseDomain(ServletConfig config)
  {
//      String domainName = config.getInitParameter("DOMAIN_NAME");
//      System.out.println("DomainConfig Name is "+domainName);
//      _domainConfigObj = getDomainConfigObj(ctx,domainName);
//      _domainConfigObj.initilizeDomain(domainName);
  }

//  private IDomainConfigObj getDomainConfigObj(Context ctx,String domainName) throws Exception
//  {
//    String jndiName =  domainName+"/"+"com.gridnode.pdip.framework.domain.ejb.IDomainConfigHome";
//    _domainConfigHome =
//    (IDomainConfigHome)PortableRemoteObject.narrow(ctx.lookup(jndiName),IDomainConfigHome.class);
//    return (_domainConfigHome.create());
//  }


}