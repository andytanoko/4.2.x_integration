/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NavigationRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-02     Andrew Hill         Created
 * 2002-10-23     Andrew Hill         Refactored & moved to navigation package
 * 2003-01-15     Daniel D'Cotta      DynamicNavLink Support
 * 2003-03-03     Andrew Hill         NavTree Support
 * 2003-03-05     Andrew Hill         Include support
 * 2003-03-06     Andrew Hill         Determine Navgroup with call from renderer
 * 2003-03-10     Andrew Hill         Most stuff factored out to NavPageRenderer
 * 2003-03-11     Andrew Hill         Load both header and bar from page
 * 2003-03-27     Andrew Hill         Highlighter support
 * 2003-04-08     Andrew Hill         Use script url constants in IGlobals
 */
package com.gridnode.gtas.client.web.navigation;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.StaticWebUtils;
import com.gridnode.gtas.client.web.renderers.AbstractRenderer;
import com.gridnode.gtas.client.web.renderers.RenderingContext;
import com.gridnode.gtas.client.web.renderers.RenderingException;

/**
 * Called by the page in the main frame, this renderer is responsible for ensuring
 * that the appropriate left and top navigation frame contents get rendered.
 * It will take care of the highlighting (with help from other classes) for the
 * left navtree, and will render javascript to load the appropriate header in the frame
 * at the top and see that the appropriate fancytab is highlighted.
 */
public class NavigationRenderer extends AbstractRenderer
{
  //private static final String NAV_MAPPING_PARAMETER = NavPageAction.NAV_MAPPING_PARAMETER;
  private static final String NAV_GROUP_PARAMETER = NavPageAction.NAV_GROUP_PARAMETER;
  private static final String NAVPAGE_MAPPING = NavPageRenderer.NAVPAGE_MAPPING; //20030310AH

  private String _path; //20030306AH
  private ActionMapping _actionMapping; //20030310AH

  /**
   * Constructor.
   * @param rContext The rendering context
   * @param path The mapping path for this page
   * @param actionMapping The actionMapping for this pages action
   */
  public NavigationRenderer(RenderingContext rContext, String path, ActionMapping actionMapping)
  {
    super(rContext);
    _path = path;
    _actionMapping = actionMapping;
  }

  public void render() throws RenderingException
  { //20030310AH, mod 20030311AH
    try
    {     
      if(_path == null) return;

      RenderingContext rContext = getRenderingContext();
      
      //Retrieve the navigation configurations (from servlet context)
      NavigationConfig config = NavPageRenderer.getNavigationConfig(rContext);//20050315AH //getNavigationConfig(rContext);
      if(config == null)
      { //Sanity check
        throw new NullPointerException("null navigation config");
      }
      //Get the id mapped to this path
      String navMapping = getNavMapping(rContext, config);
      if(navMapping == null)
      {
        return;
      }
      //Get the resolver that is mapped to this id.      
      AbstractResolver resolver = config.getResolver(navMapping);
      if(resolver == null)
      {
        throw new NullPointerException("No resolver found with mapping id:" + navMapping);
      }
      //Use the resolver to determine which navgroup the page belongs to
      String navgroupId = resolver.getNavgroupId(rContext.getRequest());
      if(navgroupId == null)
      { //If none, return without rendering anything
        return;
      }
      //Look up the configurations for this navgroup from the navigation configurations
      Navgroup navgroup = config.getNavgroup(navgroupId);
      if(navgroup != null)
      {
        String headgroupId = navgroup.getHeadgroup();
        //20030327AH - Get highlightId
        String highlightId = null;
        String highlighterId = navgroup.getHighlighter();
        if(highlighterId != null)
        {
          AbstractHighlighter highlighter = config.getHighlighter(highlighterId);
          if(highlighter == null) throw new NullPointerException("Couldnt find highlighter:" + highlighterId);
          highlightId = highlighter.getHighlight(config, navgroup, _path, rContext.getRequest());
        }
        //...

        includeJavaScript(IGlobals.JS_FRAME_METHODS); //20030408AH
        //Render script to load left navigator, and highlight appropriate node (usually in tree)
        renderLoadNavigator(rContext, navgroupId, highlightId, null, false); //20030327AH
        if(headgroupId != null)
        {
          //Render script to load the top navigator and highlight appropriate header
          //group node (which would usually be a fancytab)
          String headlightId = navgroup.getHeadlight(); //20030328AH
          renderLoadNavigator(rContext, headgroupId, null, headlightId, true); //20030328AH
        }
      }
      else
      {
        throw new NullPointerException("Unable to find navgroup:" + navgroupId);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error performing navigation rendering",t);
    }
  }

  /**
   * Renders javascript that will cause either the left or the top navigator to be loaded
   * and a node highlighted to indicate current page in the navigation tab and tree (or other
   * node type possibly). The id of the node to highlight is passed in either highlightId
   * or headlightId, and the isLoadHeader flag set accordingly. Im not sure why its different
   * (wrote the code long before this javadoc). The left navigator may itself use this renderer
   * to load the header after the page has loaded the leftnav???
   * @param rContext
   * @param navgroupId Id of the navgroup that is to be rendered in the frame
   * @param highlightId Id of the leftNav node to highlight
   * @param headlignt Id of header node to highlight
   * @param isLoadHeader true for topnav, false for leftnav
   */
  protected void renderLoadNavigator( RenderingContext rContext,
                                      String navgroupId,
                                      String highlightId,
                                      String headlightId,
                                      boolean isLoadHeader)
    throws RenderingException
  { //20030311AH
    try
    {
      ActionForward navPageForward = _actionMapping.findForward(NAVPAGE_MAPPING);
      String navUrl = StaticWebUtils.addParameterToURL(navPageForward.getPath(),
                          NAV_GROUP_PARAMETER,
                          navgroupId);

      StringBuffer buffer = new StringBuffer();
      buffer.append(isLoadHeader ? "loadHeader('" : "loadNav('");
      buffer.append(navgroupId);
      buffer.append("','");
      buffer.append( rContext.getUrlRewriter().rewriteURL(navUrl) );
      buffer.append("'");
      if(!isLoadHeader && (highlightId != null))
      { //20030327AH
        buffer.append(",'");
        buffer.append(highlightId);
        buffer.append("'");
      }
      else  if(isLoadHeader && (headlightId != null))
      { //20030328AH
        buffer.append(",'");
        buffer.append(headlightId);
        buffer.append("'");
      }
      buffer.append(");");
      //Element body = getBodyNode();
      //appendEventMethod(body,"onload",buffer.toString());
      addJavaScriptNode(null,buffer.toString());
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error in renderLoadNavigator",t);
    }
  }

  /**
   * Return the id mapped to the current pages path from the navigation configuration
   * mappings
   * @param rContext
   * @param config
   * @return id
   * @throws NullPointerException if no mapping found
   */
  public String getNavMapping(RenderingContext rContext, NavigationConfig config)
    throws GTClientException
  { //20030310AH
    Mapping mapping = config.getMapping(_path);
    if(mapping == null)
    {
      return null;
    }
    return mapping.getId();
  }

  /**
   * @deprecated
   */
  public NavigationConfig getNavigationConfig(RenderingContext rContext)
    throws GTClientException
  { //20030310AH
    return NavPageRenderer.getNavigationConfig(rContext);
  }
}