/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-13     Daniel D'Cotta      Created
 * 2003-03-25     Andrew Hill         Hacked in a hurry to be compatible with newUi
 * 2003-03-31     Andrew Hill         Base ids on id in nav config rather than hardcoding
 */
package com.gridnode.gtas.client.web.bp;

import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTProcessDefEntity;
import com.gridnode.gtas.client.ctrl.IGTProcessDefManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.navigation.DynamicNavlink;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.AnchorConversionRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

public class ProcessInstanceNavRenderer extends AbstractRenderer
{
  protected DynamicNavlink _dynamicNavlink; //20030325AH

  public ProcessInstanceNavRenderer(RenderingContext rContext, DynamicNavlink dynamicNavlink)
  {
    super(rContext);
    if (dynamicNavlink == null)
      throw new NullPointerException("dynamicNavlink is null"); //20030424AH
    _dynamicNavlink = dynamicNavlink;
  }

  protected String[] getProcessDefNames()
    throws RenderingException
  {
    try
    {
      IGTSession gtasSession = StaticWebUtils.getGridTalkSession(getRenderingContext().getSession());
      IGTProcessDefManager manager = (IGTProcessDefManager)gtasSession.getManager(IGTManager.MANAGER_PROCESS_DEF);
      Collection processDefs = manager.getAll();
      Iterator iterator = processDefs.iterator();

      String[] processDefNames = new String[processDefs.size()];
      for(int i = 0; i < processDefNames.length; i++)
      {
        IGTProcessDefEntity processDef = (IGTProcessDefEntity)iterator.next();
        processDefNames[i] = processDef.getFieldString(IGTProcessDefEntity.DEF_NAME);
      }
      return processDefNames;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Unable to get partner watch list data",t);
    }
  }

/**
 *  <tbody id="nav_row_parent">
 *    <tr>
 *      <td align="left" nowrap="nowrap" colspan="2">
 *        <a id="nav_processInstance" href="">
 *    <tr id="nav_processInstance_row">
 *      <td align="left" nowrap="nowrap">
 *        <a id="nav_processInstance_process" href="">
 */
  protected void render() throws RenderingException
  {
    String[] processDefNames = getProcessDefNames();
    try
    {
      RenderingContext rContext = getRenderingContext(); //20030325AH

      if( (processDefNames==null) || (processDefNames.length == 0) )
      {
        removeNode(_dynamicNavlink.getId() + "_row", false); //20030331AH
      }
      else
      {
        Element tbody = getElementById(_dynamicNavlink.getId() + "_row_parent", true);
        Element row = getElementById(_dynamicNavlink.getId() + "_row", true);
        Element process = getElementById(_dynamicNavlink.getId() + "_process", true);
        // Remove id attributes from the nodes. We don't need them any more as we now have references
        // and we do not want dupicate ids in our output.
        // We shall leave the tbody id in place as we only have one tbody. Those elements being used
        // as a 'stamp' will lose their ids.
        row.getAttributes().removeNamedItem("id");
        process.getAttributes().removeNamedItem("id");
        tbody.removeChild(row);

        //20030325AH - String baseHref = getElementById("nav_processInstance", true).getAttribute("href");
        String baseHref = rContext.getUrlRewriter().rewriteURL( _dynamicNavlink.getValue() ); //20030325AH
        for(int i = 0; i < processDefNames.length; i++)
        { //20030325AH - Modified method for rendering link
          replaceTextCarefully(process, processDefNames[i]);
          String thisHref = StaticWebUtils.addParameterToURL(baseHref, "processDefName" , processDefNames[i]);
          String onclick = "loadFrame('gtas_body_frame','" + thisHref + "');";
          process.setAttribute("onclick",onclick); //20030312AH
          process.setAttribute("href", AnchorConversionRenderer.NOOP );
          process.setAttribute("id",_dynamicNavlink.getId() + "_" + processDefNames[i]); //20030331AH
          tbody.appendChild(row.cloneNode(true));
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering navigation for processInstance screen", t);
    }
  }
}