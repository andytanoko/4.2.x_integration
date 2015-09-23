/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmptyDtdServlet.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-24     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.download;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * 20030225AH - This is a hack to allow the xml grid documents to be viewed inline in IE
 * when clicked on from the GridDocument view. IE will ask for the DTD - relative to the path
 * of the file (which is in fact the DownloadAction path). This DTD is not used to validate, just
 * to deal with xml entity references. If the DTD is not found, the user cannot view the xml
 * inline in Internet Explorer (and has to use rightClick->saveTargetAs instead to pull the file).
 * This servlet will simply provide an empty response to the browsers request for any filename
 * ending in .dtd located in the gtas path.
 * Most of the xml documents being handled by the user will not
 * have entity refs and so we are safe in returning the empty response. If the file does need
 * entity refs then IE will show an appropriate message about undefined entities when the file is
 * viewed. We are not addressing that issue at this time as it is not anticipated to become a
 * problem, however if this assesment is wrong we will need to return the actual dtd files - and
 * linked files such as .ent files.
 */
public class EmptyDtdServlet extends HttpServlet
{
  public void init(ServletConfig config) throws ServletException
  {
    
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    
  }


  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    
  }

  public void destroy()
  {
    
  }
}