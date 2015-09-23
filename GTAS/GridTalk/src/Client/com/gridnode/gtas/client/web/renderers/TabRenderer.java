/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TabRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-16     Andrew Hill         Created
 * 2002-12-17     Andrew Hill         Refactoring structure to accomodate Netscape6 bug #34297
 * 2003-04-08     Andrew Hill         Use script url constants defined in IGlobals
 */
package com.gridnode.gtas.client.web.renderers;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.w3c.dom.Element;

import com.gridnode.gtas.client.web.IGlobals;

public class TabRenderer  extends AbstractRenderer
{
  private static final String OLD_TAB_FIELD_SUFFIX    = "Old";
  private static final String ACTIVE_TAB_STYLECLASS   = "activeTab";
  private static final String INACTIVE_TAB_STYLECLASS = "inactiveTab";
  private static final String VISIBLE_TAB_STYLECLASS  = "visibleTab";
  private static final String HIDDEN_TAB_STYLECLASS   = "hiddenTab";
  private static final String DEFAULT_TAB_SCRIPT_PATH = IGlobals.JS_TAB_UTILS; //20030408AH
  private static final String STD_FORM_METHODS_PATH   = IGlobals.JS_ENTITY_FORM_METHODS; //20030408AH

  private ITabDef[] _tabs;
  private String _name;
  private String _scriptPath = DEFAULT_TAB_SCRIPT_PATH;
  private boolean _useServerRefresh;
  private String _oldName;

  public TabRenderer(RenderingContext rContext)
  {
    super(rContext);
  }

  /**
   * If set to true will cause each tab change to invoke a server refresh. For editable forms
   * in Netscape6 this has to been done due to the submit bug for display:none fields. It may
   * also be useful in other situations where rendering the tab only on demand will improve
   * performance. This value defaults to false except where NS6 is detected.
   * See also: http://bugzilla.mozilla.org/show_bug.cgi?id=34297
   * Nb that this functionality means that tabbed pages must be serverRefresh() compatible.
   * stdFormMethods.js will be autoincluded by this renderer.
   * I also implies that you need to provide special tab-aware code in the reset() method and
   * even worse that said code only fires fire NS6 submissions as such code wouldnt be compatible
   * with non-NS6 requests... (Ill be adding a special overrideable reset() method just for NS6
   * to ActionFormBase)...
   */
  public void setUseServerRefresh(boolean usr)
  {
    _useServerRefresh = usr;
  }

  public boolean isUseServerRefresh()
  {
    return _useServerRefresh;
  }

  /**
   * Set the path to the script used to manage tabs on the clientside. This will default to
   * scripts/tabUtils.js, but you may override it to implement customised handling. You may
   * also set it to null if your scripts are inline on your layout doc. Be sure to define
   * setVisibleTab(name,baseId) function if you are not also overriding the rendering of the
   * tab title node by subclassing this renderer. The default scripts assume serverRefresh()
   * support.
   */
  public void setScriptPath(String scriptPath)
  {
    _scriptPath = scriptPath;
  }

  public String getScriptPath()
  {
    return _scriptPath;
  }

  /**
   * Set the name of this tabber. This is also the name of an input field, usually hidden, that
   * will be used to track which tab is visible. To facilitate passing this data back to the server
   * there should also be a corresponding field on the appropriate actionForm.
   */
  public void setName(String name)
  {
    _name = name;
  }

  public String getName()
  {
    return _name;
  }

  /**
   * The oldName is used to store the name of a field that tracks the tab the user was viewing before
   * switching to the one whose id is stored in the named field. This is a hack to help support
   * the reset() method for requests submitted by netscape 6 which has a nasty bug which stops it
   * submitting fields whith style display:none. Obviously checkbox et.al. fields not on the current tab
   * would get reset when the serverRefresh on tabSwitch is invoked. By causing the 'previous' tabs
   * id to be sent in the request, we can do a check in the reset method when submitting from netscape
   * and make sure we only reset fields on that tab. Obviously this also has the evil effect of meaning
   * that the actionForm has to know which fields are on which tabs (as well as already having to know
   * the types of certain fields...) :-(((((
   * getOldName() will default to getName() + "Old" and the TabRenderer will automatically render
   * both tracking fields to the target if it doesnt find them there already so you will probably
   * never want to set the oldName yourself... (Furthermore if you do you will need to implement your
   * own javascript as the current script assumes the 'Old' suffix...)
   */
  void setOldName(String oldName)
  { //Package protected as ppl shouldn't play with it!
    _oldName = oldName;
  }

  public String getOldName()
  {
    if(_oldName == null)
    {
      setOldName( getName() + OLD_TAB_FIELD_SUFFIX );
    }
    return _oldName;
  }

  /**
   * Set the array of tab definitions defining which tabs are included in this tabber and
   * node and labelling information for this tabs. null elements ARE allowed in this array to
   * facilitate more convienient runtime removal of certain tabs. (nb: obviously you may also
   * need to remove the stuff for these dead tabs from the layout doc yourself)
   */
  public void setTabs(ITabDef[] tabs)
  {
    _tabs = tabs;
  }

  public ITabDef[] getTabs()
  {
    return _tabs;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();

      //Hack for dealing with NS6 display:none submit bug
      //http://bugzilla.mozilla.org/show_bug.cgi?id=34297
      if(rContext.isNetscape6()) setUseServerRefresh(true);
      //--------------------------------------------------

      ITabDef[] tabs = getTabs();
      if(tabs == null)
      {
        return;
      }
      ITabDef selectedTab = getSelectedTab(rContext);
      renderTabs(rContext, selectedTab);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering tabs",t);
    }
  }

  /**
   * Modifies the appropriate node(s) such that this tab is hidden when rendered in the browser
   */
  protected void renderTabAsHidden(RenderingContext rContext, ITabDef tab)
    throws RenderingException
  {
    String panelId = tab.getBaseId() + "_panel";
    Element tabNode = getElementById(panelId);
    if(tabNode != null)
    {
      tabNode.setAttribute("class",HIDDEN_TAB_STYLECLASS);
    }
  }

  /**
   * Modifies the appropriate node(s) such that this tab is visible when rendered in the browser
   */
  protected void renderTabAsVisible(RenderingContext rContext, ITabDef tab)
    throws RenderingException
  {
    String panelId = tab.getBaseId() + "_panel";
    Element tabNode = getElementById(panelId);
    if(tabNode != null)
    {
      tabNode.setAttribute("class",VISIBLE_TAB_STYLECLASS);
    }
  }

  /**
   * Will render the title for a tab. Usually this title will be anchor tag. Its id is defined
   * by the baseId() defined in the tabDef. This id is also used to keep track of which tag is
   * selected. For anchors will render a javascript href, for all other types an onclick attribute
   * that calls setVisibleTab(). This method is in the tabUtils.js file which the renderer will
   * ensure is linked by the page.
   */
  protected void renderTabTitle(RenderingContext rContext, ITabDef tab, boolean selected)
    throws RenderingException
  {
    try
    {
      if(tab == null) throw new NullPointerException("tab is null"); //20030416AH
      String baseId = tab.getBaseId();
      String name = getName();
      Element titleNode = getElementById(baseId);
      if(titleNode == null)
      {
        throw new java.lang.NullPointerException("Unable to find tab node for id=" + baseId);
      }

      int titleType = getNodeType(titleNode);
      if(titleType == INodeTypes.LINK)
      {
        String href = "javascript: void setCurrentTab('" + name + "','" + baseId + "');";
        titleNode.setAttribute("href",href);
      }
      else
      {
        String event = "setCurrentTab('" + name + "','" + baseId + "');";
        appendEventMethod(titleNode,"onclick",event);
      }
      String titleText = getTitleText(rContext, tab);
      replaceTextCarefully(titleNode,titleText);
      if(selected)
      {
        titleNode.setAttribute("class",ACTIVE_TAB_STYLECLASS);
      }
      else
      {
        titleNode.setAttribute("class",INACTIVE_TAB_STYLECLASS);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering tab title node",t);
    }
  }

  protected String getTitleText(RenderingContext rContext, ITabDef tab)
    throws RenderingException
  {
    return rContext.getResourceLookup().getMessage(tab.getTitleKey());
  }

  /**
   * Will interrogate the actionForm property or request parameter defined by name (see setName)
   * and determine which tab is currently visible based on the baseId it finds. If this is null
   * then it is assumed that the first tab is visible. If it doesnt correspond to a defined baseId
   * will throw an exception.
   */
  protected ITabDef getSelectedTab(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      String name = getName();
      ActionForm form = getActionForm();
      ITabDef[] tabs = getTabs();
      if(tabs == null) throw new NullPointerException("tabs is null"); //20030416AH
      String id = null;
      try
      {
        // try the action form first
        id = (String)PropertyUtils.getSimpleProperty(form,name);
      }
      catch(Throwable t)
      {
        // if we couldnt get the value from the action form then try the request itself
        id = rContext.getRequest().getParameter(name);
      }
      if( (id == null) || "".equals(id) )
      {
        return tabs[0];
      }
      else
      {
        // Now find the tabDef with this baseId
        for(int i=0; i < tabs.length; i++)
        {
          if(tabs[i] != null)
          {
            String thisBaseId = tabs[i].getBaseId();
            if(thisBaseId == null) throw new NullPointerException("thisBaseId is null"); //20030416AH
            if(thisBaseId.equals(id))
            {
              return tabs[i];
            }
          }
        }
        throw new java.lang.IllegalArgumentException("Tab with id='" + id + "' not found");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error obtaining definition for selected tab",t);
    }
  }

  /**
   * Will render the tab selection to the named field and the tab titles and associated
   * javascript. Will then iterate through all defined tags calling renderTabAsHidden
   * or renderTabAsVisible to set tab nodes to their appropriate status.
   * Invokes inclusion of necessary javascripts.
   */
  protected void renderTabs(RenderingContext rContext, ITabDef selectedTab)
    throws RenderingException
  {
    try
    {
      includeJavaScript( getScriptPath() );
      includeJavaScript( STD_FORM_METHODS_PATH );
      StringBuffer usrScript = new StringBuffer("var ");
      usrScript.append( getName() );
      usrScript.append( "_usr=" );
      usrScript.append( isUseServerRefresh() );
      usrScript.append( ';' );
      renderScript(usrScript.toString());
      renderTrackingFields(rContext, selectedTab);
      ITabDef[] tabs = getTabs();
      setSelectedTab(rContext, selectedTab);
      for(int i=0; i < tabs.length; i++)
      {
        ITabDef tab = tabs[i];
        if(tab != null)
        {
          if(tab.equals(selectedTab))
          {
            renderTabTitle(rContext,tab, true);
            renderTabAsVisible(rContext, tab);
          }
          else
          {
            renderTabTitle(rContext,tab, false);
            renderTabAsHidden(rContext, tab);
          }
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering tab as selected",t);
    }
  }

  /**
   * Will render the currently selected tabs id into the field named by getName() which if it
   * exists must be an input field (compatible with text - selects will have problems!).
   */
  protected void setSelectedTab(RenderingContext rContext, ITabDef selectedTab)
    throws RenderingException
  {
    try
    {
      if(selectedTab == null) throw new NullPointerException("selectedTab is null"); //20030416AH
      String name = getName();
      Element fieldNode = getElementByAttributeValue(name,"name",_target);
      if(fieldNode == null)
      {
        return;
      }
      else
      {
        if("input".equalsIgnoreCase(fieldNode.getNodeName()))
        {
          fieldNode.setAttribute("value",selectedTab.getBaseId());
        }
        else
        {
          throw new java.lang.UnsupportedOperationException("Fields of node type '"
            + fieldNode.getNodeName()
            + "' may not be used to track tab selection status");
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error setting selected tab",t);
    }
  }

  /**
   * Renders two hidden fields for use in tracking tab selections both clientside and during trips
   * to the server. The first field is named by this renderers name property (getName()) and the
   * second by the oldName property (getOldName()) which defaults to getname() + "Old".
   * The oldName shouldnt be changed from this default as the javascript expects this convention to
   * be followed. Both fields are hidden and are appended to the entityForm. (If an entityForm is
   * not found an exception will be thrown).
   * You can also create the two fields in the layout yourself if you wish and duplicates will not
   * be created, but this should not be necessary.
   */
  protected void renderTrackingFields(RenderingContext rContext, ITabDef selectedTab)
    throws RenderingException
  {
    try
    {
      String trackingFieldName = getName();
      String oldFieldName = getOldName();
      Element field = findElement(_target,"input","name",trackingFieldName);
      Element oldField = findElement(_target,"input","name",oldFieldName);
      if( (field == null) || (oldField == null) )
      {
        Element form = findElement(_target,"form","name","entityForm");
        if(form == null)
        {
          throw new java.lang.NullPointerException("Couldnt find entityForm in target document");
        }
        if(field == null)
        {
          field = createTrackingField(rContext,selectedTab,trackingFieldName);
          form.appendChild(field);
        }
        if(oldField == null)
        {
          field = createTrackingField(rContext,selectedTab,oldFieldName);
          form.appendChild(field);
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering tracking fields",t);
    }
  }

  /**
   * Creates and returns input element for use in tracking tab selections.
   * The created node will be an input element with name provided by fieldName parameter
   * and the attributes hidden="true" and the value will be the baseId of the selectedTab.
   * @param rContext - the rendering context
   * @param selectedTab - the currently selected TabDef
   * @param fieldName - name of tracking field to create
   * @return fieldNode
   */
  protected Element createTrackingField(RenderingContext rContext,
                                        ITabDef selectedTab,
                                        String fieldName)
    throws RenderingException
  {
    try
    {
      Element field = _target.createElement("input");
      field.setAttribute("type","hidden");
      field.setAttribute("value",selectedTab.getBaseId());
      field.setAttribute("name",fieldName);
      return field;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error creating tracking field:" + fieldName,t);
    }
  }
}