/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MultiselectorRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-16     Andrew Hill         Created
 * 2002-09-23     Andrew Hill         Now uses includeJavaScript() to include selectUtils.js
 * 2002-10-24     Andrew Hill         Correct evil hardcoded layout key
 * 2003-04-08     Andrew Hill         Use script url constants in IGlobals
 */
package com.gridnode.gtas.client.web.renderers;

import org.w3c.dom.*;

import com.gridnode.gtas.client.web.xml.*;
import com.gridnode.gtas.client.web.IGlobals;

public class MultiselectorRenderer  extends AbstractRenderer
{
  protected String _choicesLabel;
  protected String _selectionLabel;
  protected boolean _allowsOrdering;
  protected String _insertId;
  protected String _layoutKey;
  protected String _fieldName;
  protected boolean _mandatory;

  protected Element _valueElement;
  protected Element _errorElement;

  public MultiselectorRenderer(RenderingContext rContext)
  {
    super(rContext);
    reset();
  }

  public void reset()
  {
    _choicesLabel = "multiselector.choices";
    _selectionLabel = "multiselector.selection";
    _allowsOrdering = true;
    _insertId = null;
    _layoutKey = IDocumentKeys.MULTISELECTOR;
    _fieldName = null;
    _mandatory = false;
    _valueElement = null;
    _errorElement = null;
  }

  public void setChoicesLabel(String value)
  {
    _choicesLabel = value;
  }

  public String getChoicesLabel()
  {
    return _choicesLabel;
  }

  public void setSelectionLabel(String value)
  {
    _selectionLabel = value;
  }

  public String getSelectionLabel()
  {
    return _selectionLabel;
  }

  public void setAllowsOrdering(boolean allowsOrdering)
  {
    _allowsOrdering = allowsOrdering;
  }

  public boolean isAllowsOrdering()
  {
    return _allowsOrdering;
  }

  public void setInsertId(String id)
  {
    _insertId = id;
  }

  public String getInsertId()
  {
    return _insertId;
  }

  public void setLayoutKey(String documentKey)
  {
    _layoutKey = documentKey;
  }

  public String getLayoutKey()
  {
    return _layoutKey;
  }

  public void setFieldName(String fieldName)
  {
    _fieldName = fieldName;
  }

  public String getFieldName()
  {
    return _fieldName;
  }

  public void setMandatory(boolean mandatory)
  {
    _mandatory = mandatory;
  }

  public boolean isMandatory()
  {
    return _mandatory;
  }

  public Element getValueElement()
  {
    if(_valueElement == null)
    {
      throw new java.lang.IllegalStateException("Value element has not been created");
    }
    return _valueElement;
  }

  public Element getErrorElement()
  {
    if(_errorElement == null)
    {
      throw new java.lang.IllegalStateException("Error element has not been created");
    }
    return _errorElement;
  }

  protected void render() throws RenderingException
  {
    try
    {
      if(_insertId == null)
      {
        throw new java.lang.NullPointerException("null insertId property");
      }
      if(_fieldName == null)
      {
        throw new java.lang.NullPointerException("null fieldName property");
      }
      RenderingContext rContext = getRenderingContext();
      if(insertMultiselectorLayout(rContext))
      {
        String choiceFieldName = _fieldName + "Choices";

        //20030317AH... Fix bug where label elements dont exist
        Element choicesLabel = renderLabel("multiselector_choices",_choicesLabel,false);
        if(choicesLabel != null) choicesLabel.setAttribute("id",_fieldName + "_choiceLabel");
        Element selectionLabel = renderLabel("multiselector_selection",_selectionLabel,false);
        if(selectionLabel != null) selectionLabel.setAttribute("id",_fieldName + "_selectionLabel");
        //...

        Element field = (Element)getElementById("multiselector_right",true);
        field.setAttribute("name",_fieldName);
        field.setAttribute("id",_fieldName + "_value");
        _valueElement = field;
        Element choiceField = (Element)getElementById("multiselector_left",true);
        choiceField.setAttribute("name",choiceFieldName);
        choiceField.setAttribute("id",_fieldName + "_choices");
        Element error = (Element)getElementById("multiselector_error",true);
        error.setAttribute("id",_fieldName + "_error");
        _errorElement = error;

        Element include = (Element)getElementById("multiselector_include",true);
        include.setAttribute("onclick","doTransfer('" + choiceFieldName
                              + "','" + _fieldName + "');");
        removeIdAttribute(include);
        Element exclude = (Element)getElementById("multiselector_exclude",true);
        exclude.setAttribute("onclick","doTransfer('" + _fieldName
                              + "','" + choiceFieldName + "');");
        removeIdAttribute(exclude);
        if(!_allowsOrdering)
        {
          removeNode("multiselector_moveUp");
          removeNode("multiselector_moveDown");
        }
        else
        {
          Element moveUp = (Element)getElementById("multiselector_moveUp",true);
          moveUp.setAttribute("onclick","moveSelectedOptions('" + _fieldName + "',true);");
          removeIdAttribute(moveUp);
          Element moveDown = (Element)getElementById("multiselector_moveDown",true);
          moveDown.setAttribute("onclick","moveSelectedOptions('" + _fieldName + "',false);");
          removeIdAttribute(moveDown);
        }
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering multiselector",t);
    }
  }

  public void initFromLayout(Element element)
    throws RenderingException
  {
    try
    {
      if(element == null) return;
      String text = getNodeText(element);
      if( (text == null) || (text.length() == 0) ) return;
      int index = text.indexOf("allowsOrdering");
      setAllowsOrdering(index != -1);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error initialising MultiselectorRenderer based on layout content of element:" + element,t);
    }
  }

  protected boolean insertMultiselectorLayout(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      Node insertNode = getElementById(_insertId,false);
      if(insertNode == null) return false;
      IDocumentManager docManager = rContext.getDocumentManager();
      if(docManager == null)
      {
        throw new java.lang.NullPointerException("null documentManager");
      }
      Document mLayout = docManager.getDocument(_layoutKey,false);
      Node multiselector = getElementByAttributeValue("multiselector_content","id",mLayout);
      if(multiselector == null)
      {
        throw new RenderingException("Could not find node with id \"multiselector_content\" "
                                      + "in document with key \"" + _layoutKey + "\"");
      }
      multiselector = importAndSubstitute(multiselector, insertNode, true);

      /*Node scriptNode = getElementById("include_selectUtils",false);
      if(scriptNode == null)
      {
        scriptNode = getElementByAttributeValue("include_selectUtils","id",mLayout);
        if(scriptNode == null)
        {
          throw new RenderingException("Could not find node with id \"include_selectUtils\" "
                                        + "in document with key \"" + _layoutKey + "\"");
        }
        Node headNode = getElementById("xhtml_head",true);
        scriptNode = _target.importNode(scriptNode,true);
        headNode.appendChild(scriptNode);
      }*/
      includeJavaScript(IGlobals.JS_SELECT_UTILS); //20020923AH, 20030408AH
      return true;
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error inserting layout xhtml for multiselector",t);
    }
  }
}