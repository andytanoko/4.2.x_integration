/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CloseWindowAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-12     Andrew Hill         Created
 * 2003-03-19     Andrew Hill         If opener is listview then try to refresh it
 */
package com.gridnode.gtas.client.web.navigation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionBase;

public class CloseWindowAction extends GTActionBase
{
  public ActionForward execute(ActionMapping actionMapping,
                                ActionForm form,
                                HttpServletRequest request,
                                HttpServletResponse response)
                                throws Exception
  {
    //Nb: its so simple dont even need bother with rendering stuff. Just write it already.
    //later might refactor, maybe put in a little message, build a campfire, sing a few songs...
    //gameoverwindowgameover!

    StringBuffer buffer = new StringBuffer(); //20030319AH - Use a StringBuffer now
    buffer.append("<html><head><script type=\"text/javascript\"><!--\n");
    //20030319AH - Refresh parent window if it is a listview
    buffer.append("try\n");
    buffer.append("{\n");
    buffer.append("  if(window.opener != null)\n");
    buffer.append("  {\n");
    buffer.append("    var refreshLv = window.opener['refreshListview'];\n");
    buffer.append("    if(refreshLv) window.opener.refreshListview(window.opener._refreshUrl);\n");
    buffer.append("  }\n");
    buffer.append("}\n");
    buffer.append("catch(error)\n");
    buffer.append("{\n");
    buffer.append("  //ignore\n");
    buffer.append("}\n");
    //...
    buffer.append("window.close();\n");
    buffer.append("//--></script></head><body></body></html>");
    response.getWriter().println(buffer.toString());
    return null;
  }

}

