/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NavigationConfigManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-22     Andrew Hill         Created
 * 2002-12-10     Andrew Hill         Image Support
 * 2003-01-15     Daniel D'Cotta      DynamicNavLink Support
 * 2003-03-03     Andrew Hill         NavTree Support
 * 2003-03-05     Andrew Hill         Include Support
 * 2003-03-06     Andrew Hill         Resolver & Mapping Support
 * 2003-03-27     Andrew Hill         Highlighter support
 * 2003-06-17     Andrew Hill         Insert support
 * 2003-07-01     Andrew Hill         Modify init() to suit 1.1 final signature, use the non-deprecated digester syntax
 * 2003-10-17     Daniel D'Cotta      Add generic DynaminNavTreeNod support, remove old DynamicNavLink support  
 */
package com.gridnode.gtas.client.web.navigation;

import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.FactoryCreateRule;
import org.apache.commons.digester.ObjectCreateRule;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.SetNextRule;
import org.apache.commons.digester.SetPropertiesRule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import com.gridnode.gtas.client.utils.IdentifiedBean;

public class NavigationConfigManager implements PlugIn
{
  private static final Log _log = LogFactory.getLog(NavigationConfigManager.class); // 20031209 DDJ


  /* 20030617AH - Commentout - now use class.getName() syntax
  public static final String IDENTIFIED_BEAN_CLASS = IdentifiedBean.IDENTIFIED_BEAN_CLASS;
  public static final String NAVIGATION_CONFIG_CLASS = "com.gridnode.gtas.client.web.navigation.NavigationConfig";
  public static final String NAVGROUP_CLASS = "com.gridnode.gtas.client.web.navigation.Navgroup";
  public static final String NAVLINK_CLASS = "com.gridnode.gtas.client.web.navigation.Navlink";
  public static final String MESSAGE_CLASS = "com.gridnode.gtas.client.web.navigation.Message";
  public static final String IMAGE_CLASS = "com.gridnode.gtas.client.web.navigation.Image"; //20021210AH
  public static final String DYNAMIC_NAVLINK_CLASS = "com.gridnode.gtas.client.web.navigation.DynamicNavlink";
  public static final String NAVTREE_CLASS = "com.gridnode.gtas.client.web.navigation.NavTree"; //20030303AH
  public static final String NAVTREENODE_CLASS = "com.gridnode.gtas.client.web.navigation.NavTreeNode"; //20030303AH
  public static final String INCLUDE_CLASS = "com.gridnode.gtas.client.web.navigation.Include"; //20030305AH
  public static final String RESOLVER_CLASS = "com.gridnode.gtas.client.web.navigation.AbstractResolver"; //20030306AH
  public static final String SIMPLE_RESOLVER_CLASS = "com.gridnode.gtas.client.web.navigation.SimpleResolver"; //20030306AH
  public static final String AREA_RESOLVER_CLASS = "com.gridnode.gtas.client.web.navigation.AreaResolver"; //20030306AH
  public static final String MAPPING_CLASS = "com.gridnode.gtas.client.web.navigation.Mapping"; //20030306AH
  public static final String HIGHLIGHTER_CLASS = "com.gridnode.gtas.client.web.navigation.AbstractHighlighter"; //20030327AH
  */
  
  //20030617AH - Changed to use the class.getName() syntax
  public static final String IDENTIFIED_BEAN_CLASS = IdentifiedBean.class.getName();
  public static final String NAVIGATION_CONFIG_CLASS = NavigationConfig.class.getName();
  public static final String NAVGROUP_CLASS = Navgroup.class.getName();
  public static final String NAVLINK_CLASS = Navlink.class.getName();
  public static final String MESSAGE_CLASS = Message.class.getName();
  public static final String IMAGE_CLASS = Image.class.getName();
  public static final String DYNAMIC_NAVLINK_CLASS = DynamicNavlink.class.getName();
  public static final String NAVTREE_CLASS = NavTree.class.getName();
  public static final String NAVTREENODE_CLASS = NavTreeNode.class.getName();
  public static final String DYNAMIC_NAVTREENODE_CLASS = DynamicNavTreeNode.class.getName(); // 20031027 DDJ
  public static final String INCLUDE_CLASS = Include.class.getName();
  public static final String RESOLVER_CLASS = AbstractResolver.class.getName();
  public static final String SIMPLE_RESOLVER_CLASS = SimpleResolver.class.getName();
  public static final String AREA_RESOLVER_CLASS = AreaResolver.class.getName();
  public static final String MAPPING_CLASS = Mapping.class.getName();
  public static final String HIGHLIGHTER_CLASS = AbstractHighlighter.class.getName();
  public static final String INSERT_CLASS = Insert.class.getName(); //20030617AH (new)
  //...

  public static final String NAVIGATION_CONFIG_KEY = NAVIGATION_CONFIG_CLASS;

  private static final String NAVGROUP_MAPPING = "*/navgroup";
  private static final String NAVIGATION_CONFIG_MAPPING = "navigation-config";
  private static final String NAVLINK_MAPPING = "*/navgroup/navlink";
  private static final String MESSAGE_MAPPING = "*/navgroup/message";
  private static final String IMAGE_MAPPING = "*/navgroup/image"; //20021210AH
  private static final String DYNAMIC_NAVLINK_MAPPING = "*/navgroup/dynamic-navlink"; //20030115 DDJ
  private static final String NAVTREE_MAPPING = "*/navgroup/navtree"; //20030303AH
  private static final String NAVTREENODE_MAPPING = "*/navtreenode"; //20030303AH
  private static final String DYNAMIC_NAVTREENODE_MAPPING = "*/dynamic-navtreenode"; // 20031027 DDJ
  private static final String INCLUDE_MAPPING = "*/navgroup/include"; //20030305AH
  private static final String SIMPLE_RESOLVER_MAPPING = "navigation-config/resolvers/simpleresolver"; //20030305AH
  private static final String AREA_RESOLVER_MAPPING = "navigation-config/resolvers/arearesolver"; //20030305AH
  private static final String MAPPING_MAPPING = "navigation-config/resolver-mappings/mapping"; //20030306AH
  private static final String HIGHLIGHTER_MAPPING = "navigation-config/highlighters/highlighter"; //20030327AH
  private static final String INSERT_MAPPING = "*/navgroup/insert"; //20030617AH

  private String _configUrl;
  private ServletContext _context;
  private NavigationConfig _navigationConfig;

  public void setConfigUrl(String configUrl)
  {
    _configUrl = configUrl;
  }

  public String getConfigUrl()
  {
    return _configUrl;
  }

  public void destroy()
  {
    
  }

  public void init(ActionServlet servlet, ModuleConfig config) throws ServletException //20030701 - Use ModuleConfig parameter
  { //20030701AH - Modified digester rule setup to not use deprecated syntax
    try
    {
      _context = servlet.getServletContext();
      String configUrl = getConfigUrl();
      if(_log.isInfoEnabled())
      {
        _log.info("Parsing navigation configuration file \"" + configUrl + "\"");
      }
      if (configUrl == null)
        throw new NullPointerException("configUrl is null"); //20030424AH
      InputStream stream = _context.getResourceAsStream(configUrl);
      Digester digester = new Digester();
      digester.setNamespaceAware(false);
      digester.setValidating(false);
      //digester.setLogger(_log); // 20031211 DDJ: Commented out to allow it to use its own class to log

      Rule setProperties = new SetPropertiesRule();

      Rule navigationConfig = new ObjectCreateRule(NAVIGATION_CONFIG_CLASS);
      Rule addConfig = new SetNextRule("setNavigationConfig",NAVIGATION_CONFIG_CLASS);
      digester.addRule(NAVIGATION_CONFIG_MAPPING,navigationConfig);
      digester.addRule(NAVIGATION_CONFIG_MAPPING,addConfig);

      Rule navgroup = new ObjectCreateRule(NAVGROUP_CLASS);
      Rule addNavgroup = new SetNextRule("addNavgroup", NAVGROUP_CLASS);
      digester.addRule(NAVGROUP_MAPPING,navgroup);
      digester.addRule(NAVGROUP_MAPPING,setProperties);
      digester.addRule(NAVGROUP_MAPPING,addNavgroup);

      //20030306AH Resolver & Mapping support
      Rule addResolver = new SetNextRule("addResolver", RESOLVER_CLASS);

      Rule simpleResolver = new ObjectCreateRule(SIMPLE_RESOLVER_CLASS);
      digester.addRule(SIMPLE_RESOLVER_MAPPING,simpleResolver);
      digester.addRule(SIMPLE_RESOLVER_MAPPING,setProperties);
      digester.addRule(SIMPLE_RESOLVER_MAPPING,addResolver);

      Rule areaResolver = new ObjectCreateRule(AREA_RESOLVER_CLASS);
      digester.addRule(AREA_RESOLVER_MAPPING,areaResolver);
      digester.addRule(AREA_RESOLVER_MAPPING,setProperties);
      digester.addRule(AREA_RESOLVER_MAPPING,addResolver);

      Rule addMapping = new SetNextRule("addMapping", MAPPING_CLASS);
      Rule mapping = new ObjectCreateRule(MAPPING_CLASS);
      digester.addRule(MAPPING_MAPPING,mapping);
      digester.addRule(MAPPING_MAPPING,setProperties);
      digester.addRule(MAPPING_MAPPING,addMapping);
      //...

      //20030327AH Highlighter Support
      HighlighterCreationFactory hcf = new HighlighterCreationFactory();
      hcf.setDigester(digester);

      Rule highlighter = new FactoryCreateRule(hcf);
      Rule addHighlighter = new SetNextRule("addHighlighter", HIGHLIGHTER_CLASS);
      digester.addRule(HIGHLIGHTER_MAPPING,highlighter);
      digester.addRule(HIGHLIGHTER_MAPPING,setProperties);
      digester.addRule(HIGHLIGHTER_MAPPING,addHighlighter);
      //...

      Rule addIdBean = new SetNextRule("addChild", IDENTIFIED_BEAN_CLASS);

      Rule include = new ObjectCreateRule(INCLUDE_CLASS);
      digester.addRule(INCLUDE_MAPPING,include);
      digester.addRule(INCLUDE_MAPPING,setProperties);
      digester.addRule(INCLUDE_MAPPING,addIdBean);

      //20030305AH - Include Support
      Rule navlink = new ObjectCreateRule(NAVLINK_CLASS);
      digester.addRule(NAVLINK_MAPPING,navlink);
      digester.addRule(NAVLINK_MAPPING,setProperties);
      digester.addRule(NAVLINK_MAPPING,addIdBean);
      //...

      Rule message = new ObjectCreateRule(MESSAGE_CLASS);
      digester.addRule(MESSAGE_MAPPING,message);
      digester.addRule(MESSAGE_MAPPING,setProperties);
      digester.addRule(MESSAGE_MAPPING,addIdBean);

      Rule image = new ObjectCreateRule(IMAGE_CLASS);
      digester.addRule(IMAGE_MAPPING,image);
      digester.addRule(IMAGE_MAPPING,setProperties);
      digester.addRule(IMAGE_MAPPING,addIdBean);

      Rule dynamicNavlink = new ObjectCreateRule(DYNAMIC_NAVLINK_CLASS);
      digester.addRule(DYNAMIC_NAVLINK_MAPPING, dynamicNavlink);
      digester.addRule(DYNAMIC_NAVLINK_MAPPING, setProperties);
      digester.addRule(DYNAMIC_NAVLINK_MAPPING, addIdBean);

      //20030303AH NavTree support
      Rule navtree = new ObjectCreateRule(NAVTREE_CLASS);
      digester.addRule(NAVTREE_MAPPING,navtree);
      digester.addRule(NAVTREE_MAPPING,setProperties);
      digester.addRule(NAVTREE_MAPPING,addIdBean);

      Rule navtreenode = new ObjectCreateRule(NAVTREENODE_CLASS);
      Rule addTreeNode = new SetNextRule("addChild", NAVTREENODE_CLASS);
      digester.addRule(NAVTREENODE_MAPPING,navtreenode);
      digester.addRule(NAVTREENODE_MAPPING,setProperties);
      digester.addRule(NAVTREENODE_MAPPING,addTreeNode);

      Rule dynamicNavtreenode = new ObjectCreateRule(DYNAMIC_NAVTREENODE_CLASS);
      digester.addRule(DYNAMIC_NAVTREENODE_MAPPING,dynamicNavtreenode);
      digester.addRule(DYNAMIC_NAVTREENODE_MAPPING,setProperties);
      digester.addRule(DYNAMIC_NAVTREENODE_MAPPING,addTreeNode);
      //...
      
      //20030617AH Insert Support
      Rule insert = new ObjectCreateRule(digester,INSERT_CLASS);
      Rule addInsert = new SetNextRule(digester, "addNavgroup", NAVGROUP_CLASS);
      digester.addRule(INSERT_MAPPING,insert);
      digester.addRule(INSERT_MAPPING,setProperties);
      digester.addRule(INSERT_MAPPING,addIdBean);
      //..

      digester.push(this);
      digester.parse(stream);

      _navigationConfig.freeze();
    }
    catch(Throwable t)
    {
      if(_log.isErrorEnabled())
      {
        _log.error("Exception thrown in NavigationConfigManager",t);
      }
      throw new ServletException("Error initialising navigation config",t);
    }
  }

  public void setNavigationConfig(NavigationConfig config)
  {
    _context.setAttribute(NAVIGATION_CONFIG_KEY,config);
    _navigationConfig = config;
  }
}