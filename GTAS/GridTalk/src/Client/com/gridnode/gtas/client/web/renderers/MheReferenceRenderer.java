/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MheReferenceRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-30     Andrew Hill         Created
 * 2003-07-08     Andrew Hill         Support for view mode
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.w3c.dom.Element;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.strutsbase.MessageUtils;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;

public class MheReferenceRenderer  extends AbstractRenderer
{
  private static final String NOSCROLL_CSS_CLASS = "mheReferenceNoScroll"; //20030711AH
  public static final String SECTION_IS_OPEN   = "images/controls/sectionIsOpen.gif";
  public static final String SECTION_IS_CLOSED = "images/controls/sectionIsClosed.gif";
  public static final String DEFAULT_EMPTY_LABEL = "generic.empty";
  
  private boolean _edit;
  private boolean _noScrollBar; //20030708AH
  
  private IGTMheReference _selectableEntities;
  private String[] _selection;
  private Element _insertElement;
  private boolean _append = false; //Not reset //20030710AH
  private String _fieldName;
  private String _layoutKey = IDocumentKeys.VARIOUS;
  private String _label;
  private Object[] _labelParams; //20030710AH
  private String _labelClass; //20030710AH
  
  private Element _headerStamp;
  private Element _labelNode;
  private Element _selectAll;
  
  private Element _entityTypeStamp;
  private Element _entityTypeLabelStamp;
  private Element _entityTypeValueStamp; //20030710AH
  private Element _entityGroupStamp;
  private Element _entityGroupExpanderStamp;
  private Element _entityGroupSelectStamp;
  
  private Element _entityDetailsStamp;
  private Element _entityValueStamp;
  private Element _entityLabelStamp;
  
  private Element _currentGroupNode;
  private String _groupId;
  private Element _tableNode;
  
  StringBuffer _typeSetupScript;

  public MheReferenceRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
    //20030708AH - co: if(!_edit) throw new UnsupportedOperationException("view mode not yet supported by this renderer");
    reset();
  }

  public void reset()
  {
    _selectableEntities = null;
    _selection = StaticUtils.EMPTY_STRING_ARRAY;
    _insertElement = null;
    _fieldName = null;
    _label = DEFAULT_EMPTY_LABEL;
    _labelParams = null; //20030710AH
    _labelClass = null; //20030710AH
    _typeSetupScript = null;
    _noScrollBar = false; //20030710AH
  }

  protected void render() throws RenderingException
  {
    try
    {
      if(_insertElement == null) return;
      
      RenderingContext rContext = getRenderingContext();
      if (_selectableEntities == null)
        throw new NullPointerException("selectableEntities is null");
      if (_selection == null)
        throw new NullPointerException("selection is null");
      if (_fieldName == null)
        throw new NullPointerException("fieldname is null");
      
      insertLayout(rContext);
      extractStamps(rContext);
      renderHeader(rContext);
      renderSelectableEntities(rContext);
      renderSelection(rContext);
      renderErrors(rContext);
      
      includeJavaScript(IGlobals.JS_TABLE_UTILS);
      includeJavaScript(IGlobals.JS_MHEREFERENCE_UTILS);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering mheReference field",t);
    }
  }
  
  private void insertLayout(RenderingContext rContext) throws RenderingException
  {
    try
    {      
      /*20030710AH - co: Document layoutDoc = rContext.getDocumentManager().getDocument(_layoutKey, false);
      if (layoutDoc == null)
        throw new NullPointerException("layoutDoc is null");
      Element element = getElementByAttributeValue("mheReference_content", "id", layoutDoc);
      if(element == null)
        throw new RenderingException("Couldnt find element with id 'mheReference_content' in layout document");
      element = (Element)_target.importNode(element.cloneNode(true), true);*/
      
      Element element = getLayoutElement(_layoutKey, "mheReference_content"); //20030710AH
      if(element == null)
        throw new RenderingException("Couldnt find element with id 'mheReference_content' in layout document");
      element.setAttribute("id",_fieldName + "_mheReference_content");
      if(rContext.isMozillaRv() || rContext.isNetscape6() || isNoScrollBar() )
      { //Degrade style for mozilla clients that dont handle overflow as we would like them to
        element.setAttribute("class",NOSCROLL_CSS_CLASS); //20030710AH - Use const for css class
      }
      
      if(!_append)
      { //20030710AH
        removeAllChildren(_insertElement);
      }
      _insertElement.appendChild(element);
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error inserting layout from document with key:" + _layoutKey,t);
    }
  }
  
  private void extractStamps(RenderingContext rContext) throws RenderingException
  {
    try
    {
      //Nb: Currently we expect all the important elements to be present in the layout dom
      
      _headerStamp = getElementById("mheReference_header",true);
      _labelNode = getElementById("mheReference_label",true);
      _selectAll = getElementById("mheReference_selectAll",true);
      
      _tableNode = getElementById("mheReference_table",true);      
      _entityTypeStamp = getElementById("mheReference_entityType_details",true);
      _entityTypeLabelStamp = getElementById("mheReference_entityType_label",true);
      _entityGroupStamp = getElementById("mheReference_entity_group",true);
      _entityGroupExpanderStamp = getElementById("mheReference_entity_group_expander",true);
      _entityGroupSelectStamp = getElementById("mheReference_entityType_value",true);
      _entityDetailsStamp = getElementById("mheReference_entity_details",true);
      _entityValueStamp = getElementById("mheReference_entity_value",true);
      _entityLabelStamp = getElementById("mheReference_entity_label",true);
      
      _headerStamp.removeAttribute("id");
      _labelNode.removeAttribute("id");
      _selectAll.removeAttribute("id");
      _tableNode.removeAttribute("id");
      _entityTypeStamp.removeAttribute("id");
      _entityTypeLabelStamp.removeAttribute("id");
      _entityGroupStamp.removeAttribute("id");
      _entityGroupExpanderStamp.removeAttribute("id");
      _entityGroupSelectStamp.removeAttribute("id");
      _entityDetailsStamp.removeAttribute("id");
      if(_edit) 
      { //20030708AH
        _entityValueStamp.removeAttribute("id");
      }
      else
      { //20030708AH - Remove checkboxes for viewmode
        removeNode(_entityValueStamp,false);
        removeNode(_selectAll,false); 
        removeNode(_entityGroupSelectStamp,false); 
      }
      _entityLabelStamp.removeAttribute("id");
      
      _tableNode.removeChild(_entityTypeStamp);
      _tableNode.removeChild(_entityGroupStamp);
      _entityGroupStamp.removeChild(_entityDetailsStamp);
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error extracting stamps",t);
    }
  }
  
  private void renderHeader(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      String labelText = rContext.getResourceLookup().getMessage(_label, _labelParams); //20030710AH
      replaceTextCarefully(_labelNode,labelText); //20030710AH
      _labelNode.setAttribute("id", _fieldName + "_label" );
      if( StaticUtils.stringNotEmpty(_labelClass) )
      { //20030710AH
        _labelNode.setAttribute("class", _labelClass);
      }
      if(_edit)
      { //20030710AH
        _selectAll.setAttribute("id", _fieldName + "_selectAll");
        _selectAll.setAttribute("onclick", "setAllRecordsSelected('"
                                                        + _fieldName
                                                        + "');" );
      }    
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering mheReference header",t);
    }
  }
  
  private void appendTypeRow(RenderingContext rContext, String entityType)
    throws RenderingException
  {
    try
    {
      renderLabelCarefully(_entityTypeLabelStamp, entityType );
      if(_edit)
      { //20030710AH
        String selectAllFn = _fieldName + "_selectAll";
        String selectAllId = _fieldName + "_" + entityType + "_selectAll";
        _entityGroupSelectStamp.setAttribute("id", selectAllId);
        _entityGroupSelectStamp.setAttribute("name", selectAllFn);
        _entityGroupSelectStamp.setAttribute("onclick", "setAllInGroup('"
                                                        + _fieldName
                                                        + "','"
                                                        + entityType
                                                        +"');" );
      }
      String expanderId = _fieldName + "_" + entityType + "_expander";
      _entityGroupExpanderStamp.setAttribute("id",expanderId);
      _entityGroupExpanderStamp.setAttribute("onclick", "toggleGroupExpansion('"
                                                      + _fieldName
                                                      + "','"
                                                      + entityType
                                                      +"');" );                                                     
      Element typeRow = (Element)_entityTypeStamp.cloneNode(true);
      _tableNode.appendChild(typeRow);
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error appending type row for " + entityType,t);
    }
  }
  
  private void appendGroup(RenderingContext rContext, String entityType )
    throws RenderingException
  {
    try
    {
      _groupId = _fieldName + "_" + entityType + "_group";
      
      _currentGroupNode = (Element)_entityGroupStamp.cloneNode(true);
      _currentGroupNode.setAttribute("id",_groupId);
      _tableNode.appendChild(_currentGroupNode);
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error appending groupNode",t);
    }
  }
  
  private void appendEntityRow(RenderingContext rContext, IGTEntityReference ref)
    throws GTClientException
  {
    try
    {
//System.out.println("appending entityRow for " + ref);
      replaceTextCarefully(_entityLabelStamp, ref.toString() );
      if(_edit)
      { //200300708AH
        _entityValueStamp.setAttribute("value", getSubmitableValue(ref));
        _entityValueStamp.setAttribute("name", _fieldName);
        if(_edit)
        { //20030710AH
          _entityValueStamp.setAttribute("onclick", "updateGroupSelector('"
                                                    + _fieldName
                                                    + "','"
                                                    + ref.getType()
                                                    +"',true);" );
        }
      } 
      Element entityRow = (Element)_entityDetailsStamp.cloneNode(true);
      _currentGroupNode.appendChild(entityRow);
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error rendering entity row for " + ref,t);
    }
  }
  
  private void renderSelection(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      if(_edit) return; //20030710AH
      //To set the selections all we do is look for the checkboxes under the insertElement in the
      //target whose value corresponds to what we have in our selection array
      for(int i=0; i < _selection.length; i++)
      {
        String value = _selection[i];
        Element input = findElement(_insertElement, "input", "value", value);
        if(input != null)
        { //We are assuming its a checkbox!
          input.setAttribute("checked","checked");
        }
      }
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error rendering selectable entities",t);
    }
  }
  
  private void renderSelectableEntities(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      
      String[] types = _selectableEntities.getReferencedTypes();
      int scriptSizeGuesstimate = (types.length + 1) * 16;
      _typeSetupScript = new StringBuffer( scriptSizeGuesstimate );      
      _typeSetupScript.append("_");
      _typeSetupScript.append(_fieldName);
      _typeSetupScript.append("_entityTypes = [");
      
      for(int i=0; i < types.length; i++)
      {
        List references = _selectableEntities.get(types[i]);
        if(!references.isEmpty())
        {
          appendTypeRow(rContext, types[i]);
          appendGroup(rContext, types[i]);
          Iterator iterator = references.iterator();
          while(iterator.hasNext())
          {
            IGTEntityReference ref = (IGTEntityReference)iterator.next();
            appendEntityRow(rContext, ref);
          }
          _typeSetupScript.append("'");
          _typeSetupScript.append(types[i]);
          _typeSetupScript.append("'");
          if( i < (types.length - 1) )
          {
            _typeSetupScript.append(",");
          }
        }
      }
      
      _typeSetupScript.append("];");
      addJavaScriptNode(_headerStamp, _typeSetupScript.toString());
      if(_edit) //20030710AH
      {
        appendOnloadEventMethod("updateSelectorStates('" + _fieldName + "');");
      }
    }
    catch (Throwable t)
    {
      throw new RenderingException("Error rendering entity selections",t);
    }
  } 
  
  private void renderErrors(RenderingContext rContext)
    throws RenderingException
  {
    try
    {      
      ActionErrors errors = rContext.getActionErrors();
      ActionError error = MessageUtils.getFirstError(errors, _fieldName);
      if(error != null)
      {
        Element errorElement = getElementById("mheReference_error",false);
        if(errorElement != null)
        {
          errorElement.removeAttribute("id");
          replaceTextCarefully(errorElement, rContext.getResourceLookup().getMessage(error.getKey()));
        }
      }
      else
      {
        removeNode("mheReference_error",false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering action errors",t);
    }
  }

  public static String getSubmitableValue(IGTEntityReference ref)
  {
    //todo: factor out this and its opposite into a utility class
    return ref.getType() + "." + ref.getKeyFieldId() + "==" + ref.getKeyValue();
  }

  public static IGTEntityReference getEntityReference(String sValue, IGTSession gtasSession)
    throws GTClientException
  {
    //@todo: factor out somewhere else - this functionality doesnt belong in the renderer
    //im just parking it here for now with the lights blinking....
    if (gtasSession == null)
      throw new NullPointerException("gtasSession is null");
    try
    {
      if (StaticUtils.stringEmpty(sValue))
        throw new NullPointerException("sValue is null or empty");
      int dotIndex = sValue.indexOf(".");
      int equalIndex = sValue.indexOf("==");
      String entityType = sValue.substring(0, dotIndex);
      String keyFieldIdStr = sValue.substring(dotIndex+1, equalIndex);
      String keyValueStr = sValue.substring(equalIndex + 2);
      Number keyFieldId = StaticUtils.integerValue(keyFieldIdStr);
      IGTManager manager = gtasSession.getManager(entityType);
      IGTFieldMetaInfo fmi = manager.getSharedFieldMetaInfo(entityType, keyFieldId);
      Object keyValue = StaticUtils.convert(keyValueStr, fmi.getValueClass(), true, true);
      SimpleEntityReference ref = new SimpleEntityReference(entityType,keyFieldId,keyValue);
      return ref;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error converting " + sValue + " to IGTEntityReference",t);
    }  
  }
  
  public static IGTMheReference getMheReference(String[] sValues, IGTSession gtasSession)
    throws GTClientException
  {
    try
    {
      if (sValues == null) return null;
      IGTEntityReference[] refs = new IGTEntityReference[sValues.length];
      for(int i=0; i<sValues.length;i++)
      {
        try
        {
          refs[i] = getEntityReference(sValues[i],gtasSession);
        }
        catch(Throwable t)
        {
          throw new GTClientException("Error converting array value (" + sValues[i] + ") at index " + i,t);
        }
      }
      SimpleMheReference mheRef = new SimpleMheReference(refs);
      return mheRef;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error converting string array into IGTMheReference instance",t);
    }
  }

  public String[] getSelection()
  {
    return _selection;
  }

  public void setSelection(String[] selection)
  {
    if(selection == null) selection = StaticUtils.EMPTY_STRING_ARRAY;
    _selection = selection;
  }

  public Element getInsertElement()
  {
    return _insertElement;
  }

  public void setInsertElement(Element insertElement)
  {
    _insertElement = insertElement;
  }

  public IGTMheReference getSelectableEntities()
  {
    return _selectableEntities;
  }

  public void setSelectableEntities(IGTMheReference selectableEntities)
  {
    _selectableEntities = selectableEntities;
  }

  public String getFieldName()
  {
    return _fieldName;
  }

  public void setFieldName(String fieldname)
  {
    _fieldName = fieldname;
  }

  public String getLayoutKey()
  {
    return _layoutKey;
  }

  public void setLayoutKey(String layoutKey)
  {
    _layoutKey = layoutKey;
  }

  /**
   * @return
   */
  public String getLabel()
  {
    return _label;
  }

  /**
   * @param string
   */
  public void setLabel(String string)
  {
    _label = string;
  }

  public boolean isNoScrollBar()
  { //20030708AH
    return _noScrollBar;
  }

  public void setNoScrollBar(boolean b)
  { //20030708AH
    _noScrollBar = b;
  }

  public Object[] getLabelParams()
  { //20030710AH
    return _labelParams;
  }

  public void setLabelParams(Object[] objects)
  { //20030710AH
    _labelParams = objects;
  }

  public String getLabelClass()
  { //20030710AH
    return _labelClass;
  }

  public void setLabelClass(String string)
  { //20030710AH
    _labelClass = string;
  }

  public boolean isAppend()
  { //20030710AH
    return _append;
  }

  public void setAppend(boolean b)
  { //20030710AH
    _append = b;
  }

}