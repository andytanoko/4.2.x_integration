/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Highlighter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-27     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.navigation;

import java.util.*;
import javax.servlet.http.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.*;

/**
 * The usual Highlighter implementation.
 * Implements the getHighlight method to analyse the request and mappingPath against the
 * data for the navgroup and returns the id of the element that is to be highlighted in the
 * navigation page, or null if a suitable element cannot be determined.
 * I have tried to break this class up in a way that makes it easy to extend though I fear that
 * when this is necessary it will be due to complication that require a rather different approach
 * and should thus extend AbstractHighlighter directly. Time will tell.
 * This Highlighter only supports Navlink highlighting (including children thereof, though it
 * is unlikely to return correct results for DynamicNavlinks at this stage of development).
 */
public class Highlighter extends AbstractHighlighter
{
  //...
    class MatchInfo
    { //Inner class
      private int _matchLevel = -1;
      private Navlink _match = null;
      private Object _extraData = null;

      public Navlink getMatch()
      { return _match; }

      public int getMatchLevel()
      { return _matchLevel; }

      public void setMatch(Navlink match)
      { _match = match; }

      public void setMatchLevel(int matchLevel)
      { _matchLevel = matchLevel; }

      public void setExtraData(Object extraData)
      { _extraData = extraData; }

      public Object getExtraData()
      { return _extraData; }

      public String toString()
      { return "MatchInfo[" + _matchLevel + ", " + _match + ", " + _extraData + "]"; }
    }
  //...

  public String toString()
  {
    return "Highlighter[" + getId() + "]";
  }

  /**
   * Return the id of the element in the navigation page that is to be highlighted.
   * @param navConfig
   * @param navgroup
   * @param mappingPath
   * @param request
   * @return highlightNodeId
   * @throws GTClientException
   */
  public String getHighlight( NavigationConfig navConfig,
                              Navgroup navgroup,
                              String mappingPath,
                              HttpServletRequest request)
    throws GTClientException
  {
    try
    {
      if(navgroup == null) return null;
      if(mappingPath == null) throw new NullPointerException("mappingPath is null");
      MatchInfo matchInfo = processNavgroup(navConfig, navgroup, mappingPath, request, null);
      return getMatchReturnString( matchInfo );
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting highlight in "
                                  + navgroup
                                  + " for mappingPath \""
                                  + mappingPath + "\"",t);
    }
  }

  /**
   * Based on the matching navlink, determine which element is to be highlighted.
   * Subclass may wish to override in order to make use of extraData property instead of
   * simply returning the navlink id.
   * @param matchInfo
   * @return highlightNodeId
   * @throws GTClientException
   */
  protected String getMatchReturnString(MatchInfo matchInfo) throws GTClientException
  {
    try
    {
      if(matchInfo != null)
      {
        if(matchInfo.getMatchLevel() > -1)
        {
          Navlink match = matchInfo.getMatch();
          if(match == null)
          {
            throw new NullPointerException("Internal assertion failure: matching Navlink is null!");
          }
          return match.getId();
        }
      }
      return null;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting match return String for " + matchInfo, t);
    }
  }

  protected MatchInfo processNavgroup(NavigationConfig navConfig,
                                      Navgroup navgroup,
                                      String mappingPath,
                                      HttpServletRequest request,
                                      MatchInfo matchInfo)
    throws GTClientException
  {
    try
    {
      Iterator i = navgroup.getChildren();
      while(i.hasNext())
      {
        IdentifiedBean child = (IdentifiedBean)i.next();
        matchInfo = processChild(navConfig, child, mappingPath, request, matchInfo);
      }
      return matchInfo;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error processing children of " + navgroup,t);
    }
  }

  protected MatchInfo processChild(NavigationConfig navConfig,
                                    IdentifiedBean child,
                                    String mappingPath,
                                    HttpServletRequest request,
                                    MatchInfo matchInfo)
    throws GTClientException
  {
    try
    {
      if(child instanceof Navlink)
      {
        matchInfo = processNavlink((Navlink)child,mappingPath,request,matchInfo);
      }
      else if(child instanceof NavTree)
      {
        Navlink root = ((NavTree)child).getTree();
        if(root != null)
        {
          matchInfo = processNavlink(root,mappingPath,request,matchInfo);
        }
      }
      else if(child instanceof Include)
      {
        Navgroup include = navConfig.getNavgroup(child.getId());
        if(include != null)
        {
          matchInfo = processNavgroup(navConfig,include,mappingPath,request,matchInfo);
        }
        else
        {
          throw new IllegalStateException("Included navgroup not found for " + child);
        }
      }
      return matchInfo;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error processing navgroup child:" + child, t);
    }
  }

  /**
   * Process the navlink and all its children recursively searching for better matches than
   * the one we have in matchInfo, updating matchInfo whenever we find a better match than the
   * one it contains. The matchInfo parameter is a simple bean used to pass around a reference to
   * the best match we have so far and its level of matching so that we can compare our candidate(s)
   * to it and update it if we have found a better match. Note that matchInfo is created on demand,
   * and may be passed in as null. On return be sure to overwrite your existing (probably null)
   * reference to matchInfo with whats returned. This method aditionally reserves the right to
   * return a different matchInfo than the one that was passed in, though currently the only time
   * it does this is when null was passed in , and a match is found , as described already.
   * The matchLevel is an indication of how closely the request path and its parameters match the
   * value of the navlink (whose path and parameters have been extracted from value already for
   * efficiency at config freeze time). A matchLevel of -1 indicates no match. A match level of
   * 0 indicates that the path matches and that no parameters conflict. If value has parameters
   * as well as a path, then matching parameters with matching values must be found in the request
   * or it is not a match. The more matching parameters found the higher the match level.
   * NB: When a match is found, the matchInfo should also clear any extraData set by a previous match!
   * This method delegates to getMatchLevel() to determine the level of matching between the
   * request and the navlink value. Note that you have to pass in the mappingPath seperately as Im
   * to lazy to work out how to extract it from the request path, and we already saved a copy of
   * it somewhere in the request when we were in the request processor under some attribute
   * somewhere. (This is the same mappingPath struts uses to work out what url matches what action,
   * and that we use to choose a navgroup resolver).
   * @param navlink A navlink object (or navtreenode etc...)
   * @param mappingPath The same path used to determine which action, navgroup resolver etc.. is used
   * @param request
   * @param matchInfo - A bean used to hold info on best match so far
   * @return matchInfo - The updated matchInfo bean which is not necessarily the same instance passed in
   * @throws GTClientException but only when its feeling grumpy
   */
  protected MatchInfo processNavlink(  Navlink navlink,
                                              String mappingPath,
                                              HttpServletRequest request,
                                              MatchInfo matchInfo)
    throws GTClientException
  {
    if(navlink == null) throw new NullPointerException("navlink is null");
    if(mappingPath == null) throw new NullPointerException("mappingPath is null");
    if(request == null) throw new NullPointerException("request is null");
    try
    {
      Iterator iterator = navlink.getChildren();
      if(iterator != null)
      {
        while(iterator.hasNext())
        {
          Navlink child = (Navlink)iterator.next();
          matchInfo = processNavlink(child, mappingPath, request, matchInfo);
        }
      }

      int matchLevel = getNavlinkMatchLevel(navlink, mappingPath, request);
      if(matchLevel > -1)
      { // A potential match - but is it the best so far?
        if(matchInfo == null) matchInfo = new MatchInfo(); //Create on demand
        if(matchLevel > matchInfo.getMatchLevel())
        {
          matchInfo.setMatchLevel(matchLevel);
          matchInfo.setMatch(navlink);
          matchInfo.setExtraData(null);
        }
      }
      return matchInfo; // MatchInfo will have the best we have found so far
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting highligh match level for "
                                  + navlink
                                  + " with mappingPath=\""
                                  + mappingPath + "\"",t);
    }
  }

  /**
   * See docs for processMatchCandidate
   */
  protected int getNavlinkMatchLevel(Navlink navlink, String mappingPath, HttpServletRequest request)
    throws GTClientException
  {
    try
    {
      int matchLevel = -1;
      if(mappingPath.equals(navlink.getValuePath()))
      {
        matchLevel = 0;
        NameValuePair[] parameters = navlink.getParameters();
        for(int i=0; i < parameters.length; i++)
        {
          String paramName = parameters[i].getName();
          String paramValue = parameters[i].getValue();
          if(paramName == null) throw new NullPointerException("Internal assertion failed. paramName is null");
          String requestParamValue = request.getParameter(paramName);
          matchLevel = StaticUtils.objectsEqual( paramValue , requestParamValue ) ? matchLevel + 1 : -1;
        }
      }
      return matchLevel;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error determining navlink match level for " + navlink,t);
    }
  }
}

