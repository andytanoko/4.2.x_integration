/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SectionRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-10     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

import org.w3c.dom.Element;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class SectionRenderer extends AbstractRenderer
{
  private String _name;
  private Element _insertionPoint;
  private boolean _append;
  private boolean _locked;
  private boolean _open;
  private String _labelKey;
  private String _contentElementName;
  
  private String _iconOpen = "images/controls/sectionIsOpen.gif"; //Not reset
  private String _iconClosed = "images/controls/sectionIsClosed.gif"; //Not reset
  private String _iconLockedOpen = "images/controls/sectionIsLockedOpen.gif"; //Not reset
  private String _iconLockedClosed = "images/controls/sectionIsLockedClosed.gif"; //Not reset
 
  private Element _headerNode;
  private Element _labelNode;
  private Element _contentNode;
  private Element _expanderNode; 
   
  public SectionRenderer(RenderingContext rContext)
  {
    super(rContext);
    reset();
  }
  
  protected void reset()
  {
    _name = null;
    _insertionPoint = null;
    _append = false;
    _locked = false;
    
    _headerNode = null;
    _labelNode = null;
    _contentNode = null;
    _expanderNode = null;
    
    _contentElementName = null;
  }
  
  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      importLayout(rContext);
      massageIds(rContext);
      insertJavaScript(rContext);
      initialiseExpanderIcon(rContext);
      renderHeaderLabel(rContext);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering section " + _name,t);
    }
  }
  
  protected void insertJavaScript(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      includeJavaScript(IGlobals.JS_SECTION_METHODS);
      StringBuffer buffer = new StringBuffer();
      appendIconUrlInitScriptNStuff(buffer, "isOpen", getIconOpen() );
      appendIconUrlInitScriptNStuff(buffer, "isClosed", getIconClosed() );
      appendIconUrlInitScriptNStuff(buffer, "isLockedOpen", getIconLockedOpen() );
      appendIconUrlInitScriptNStuff(buffer, "isLockedClosed", getIconLockedClosed() );
      addJavaScriptNode(null, buffer.toString());
      
      if(!_locked)
      {
        _expanderNode.setAttribute("onclick", "toggleSectionExpansion('" + _name + "');");
      }
      else
      {
        _expanderNode.removeAttribute("onclick");
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error preparing section javascript",t);
    }
  }
  
  private void appendIconUrlInitScriptNStuff(StringBuffer buffer, String state, String url)
  {
    //ie something like: 
    buffer.append("var iconUrlSection_");
    buffer.append( _name );
    buffer.append(state);
    buffer.append(" = '");
    buffer.append(url);
    buffer.append("';\n");
  }
  
  protected void importLayout(RenderingContext rContext)
    throws RenderingException
  {
    if (_insertionPoint == null)
      throw new NullPointerException("insertionPoint not specified");
    _headerNode = getLayoutElement(IDocumentKeys.VARIOUS, "section_header");
    if (_headerNode == null)
      throw new NullPointerException("headerNode not found");
    _contentNode = getLayoutElement(IDocumentKeys.VARIOUS, "section_content");
    if (_contentNode == null)
      throw new NullPointerException("_contentNode not found");
    if(!_append)
    {
      removeAllChildren(_insertionPoint);
    }
    _insertionPoint.appendChild(_headerNode);
    _insertionPoint.appendChild(_contentNode);
    if(_contentElementName != null)
    {
      if(!_contentElementName.equals(_contentNode.getNodeName()))
      {
        String cssClass = _contentNode.getAttribute("class");
        _contentNode = replaceNodeWithNewElement(_contentElementName, _contentNode, false);
        if( StaticUtils.stringNotEmpty(cssClass) )
        {
          _contentNode.setAttribute("class", cssClass);
        }
      }
    }
    
    _labelNode = findElement(_headerNode, null, "id", "section_label");
    if (_labelNode == null)
      throw new NullPointerException("labelNode not found");
    _expanderNode = findElement(_headerNode, null, "id", "section_expander");
    if (_expanderNode == null)
      throw new NullPointerException("expanderNode not found");
  }
  
  protected void massageIds(RenderingContext rContext)
    throws RenderingException
  {
    if (_name == null) _name = "untitled" + System.currentTimeMillis(); //Not as unique as it ought to be
    _headerNode.setAttribute("id", _name + "_section_header");
    _contentNode.setAttribute("id", _name + "_section_content");
    _labelNode.setAttribute("id", _name + "_section_label");
    _expanderNode.setAttribute("id", _name + "_section_expander");
  }
  
  protected void initialiseExpanderIcon(RenderingContext rContext)
    throws RenderingException
  {
    String icon = _open ? _locked ? _iconLockedOpen : _iconOpen : _locked ? _iconLockedClosed : _iconClosed;
    _expanderNode.setAttribute("src", icon);
  }
  
  protected void renderHeaderLabel(RenderingContext rContext)
    throws RenderingException
  {
    if(StaticUtils.stringEmpty(_labelKey))
    {
      replaceText(_labelNode, " " );
    }
    else
    {
      replaceText(_labelNode, rContext.getResourceLookup().getMessage(_labelKey));
    }
  }
  
  public boolean isAppend()
  {
    return _append;
  }

  public String getIconClosed()
  {
    return _iconClosed;
  }

  public String getIconLockedClosed()
  {
    return _iconLockedClosed;
  }

  public String getIconLockedOpen()
  {
    return _iconLockedOpen;
  }

  public String getIconOpen()
  {
    return _iconOpen;
  }

  public Element getInsertionPoint()
  {
    return _insertionPoint;
  }

  public boolean isLocked()
  {
    return _locked;
  }

  public String getName()
  {
    return _name;
  }

  public void setAppend(boolean b)
  {
    _append = b;
  }

  public void setIconClosed(String string)
  {
    _iconClosed = string;
  }

  public void setIconLockedClosed(String string)
  {
    _iconLockedClosed = string;
  }

  public void setIconLockedOpen(String string)
  {
    _iconLockedOpen = string;
  }

  public void setIconOpen(String string)
  {
    _iconOpen = string;
  }

  public void setInsertionPoint(Element element)
  {
    _insertionPoint = element;
  }

  public void setLocked(boolean b)
  {
    _locked = b;
  }

  public void setName(String string)
  {
    _name = string;
  }

  public boolean isOpen()
  {
    return _open;
  }

  public void setOpen(boolean b)
  {
    _open = b;
  }

  public String getLabelKey()
  {
    return _labelKey;
  }

  public void setLabelKey(String string)
  {
    _labelKey = string;
  }

  public Element getContentNode()
  {
    return _contentNode;
  }

  public Element getExpanderNode()
  {
    return _expanderNode;
  }

  public Element getHeaderNode()
  {
    return _headerNode;
  }

  public Element getLabelNode()
  {
    return _labelNode;
  }

  public String getContentElementName()
  {
    return _contentElementName;
  }

  public void setContentElementName(String string)
  {
    _contentElementName = string;
  }

}
