/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AnchorConversionRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-07     Andrew Hill         Created
 * 2003-03-12     Andrew Hill         DETAIL_VIEW virtual target, and use onclick
 * 2003-03-26     Andrew Hill         PWL virtual target
 * 2003-04-08     Andrew Hill         Use script urls defined in IGlobals
 * 2004-08-03     Daniel D'Cotta      Added help
 */
package com.gridnode.gtas.client.web.renderers;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gridnode.gtas.client.web.IGlobals;
 
/**
 * Reformats all non-javascript href attributes of 'a' tags (except those with
 * a class attribute of 'external' or where the target is defined) to go
 * through the jalan javascript method
 * (in navigationMethods.js) where we have control to do evil things to it. (So far
 * using location.replace() as part of our diabolical conspiracy to anihilate the
 * effects of the annoying programmer unfriendly back-button! muhahahaha)
 */
public class AnchorConversionRenderer  extends AbstractRenderer
{
  public static final String TARGET_DETAIL_VIEW = "_detailView";
  public static final String TARGET_PWL = "_pwlView";
  public static final String TARGET_HELP = "helpWindow";

  public static final String NOOP = "javascript: void(0);"; //20030312AH

  public static final String ANCHOR_NAME = "a";
  private static final String NAV_FUNCTION = "jalan";
  private static final String DETAIL_FUNCTION = "pergi";
  private static final String PWL_FUNCTION = "openPwl"; //20030326AH
  private static final String HELP_FUNCTION = "openHelp";
  private static final String EXTERNAL_CLASS = "external";

  public AnchorConversionRenderer()
  {
    super(new RenderingContext());
  }

  public void render() throws RenderingException
  {
    try
    {
      includeJavaScript(IGlobals.JS_NAVIGATION_METHODS); //20030408AH
      convertNodesAnchors(_target);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error performing anchor conversion rendering",t);
    }
  }

  private void convertNodesAnchors(Node node) throws RenderingException
  {
    if(nodeIsElement(node))
    {
      if(ANCHOR_NAME.equals(node.getNodeName()))
      {
        convertAnchor((Element)node);
      }
    }
    NodeList children = node.getChildNodes();
    int length = children.getLength();
    if(length > 0)
    {
      for(int i = 0; i < length; i++)
      {
        convertNodesAnchors(children.item(i));
      }
    }
  }

  private void convertAnchor(Element anchor) throws RenderingException
  {
    //@todo: refactor if statement to make easier to add new vtarget types
    String url = anchor.getAttribute("href");
    String prevOnclick = anchor.getAttribute("onclick");
    String target = anchor.getAttribute("target");
    String cssClass = anchor.getAttribute("class");
    if(TARGET_DETAIL_VIEW.equals(target))
    { //20030312AH
      String onclick = DETAIL_FUNCTION + "('" + url + "'); return false;";
      anchor.setAttribute("href",NOOP);
      anchor.setAttribute("onclick",onclick);
    }
    if(TARGET_PWL.equals(target))
    { //20030326AH
      String onclick = PWL_FUNCTION + "('" + url + "'); return false;";
      anchor.setAttribute("href",NOOP);
      anchor.setAttribute("onclick",onclick);
    }
    else if(TARGET_HELP.equals(target))
    { 
      String onclick = HELP_FUNCTION + "('" + url + "'); return false;";
      anchor.setAttribute("href",NOOP);
      anchor.setAttribute("onclick",onclick);
    }
    else if( ("".equals(target) || (target==null))
        && ("".equals(prevOnclick) || (prevOnclick==null))
        && (url != null)
        && (!EXTERNAL_CLASS.equals(cssClass)) )
    {
      if(url.indexOf("javascript:") == -1)
      {
        //url = "javascript: " + NAV_FUNCTION + "('" + url + "');";
        //anchor.setAttribute("href",url);
        //20030312AH - To stop side effects we now use the onclick

        String onclick = NAV_FUNCTION + "('" + url + "'); return false;";
        anchor.setAttribute("href",NOOP);
        anchor.setAttribute("onclick",onclick);
      }
    }
  }
}