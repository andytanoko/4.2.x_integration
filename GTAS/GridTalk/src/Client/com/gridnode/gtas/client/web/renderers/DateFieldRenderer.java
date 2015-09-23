/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DateFieldRenderer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-18     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.renderers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gridnode.gtas.client.ctrl.ITimeConstraint;
import com.gridnode.gtas.client.utils.DateUtils;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.IGlobals;
import com.gridnode.gtas.client.web.xml.IDocumentKeys;
import com.gridnode.gtas.client.web.xml.IDocumentManager;
 
//@todo: support this field type in the BFPR using this renderer
//@todo: support view mode
//@todo: verify correct behaviour in timeOnly or dateOnly fields
public class DateFieldRenderer  extends AbstractRenderer
{
  //If you change the following constants please remember to also change their
  //hardcoded equivelents in dateFieldUtils.js!
  private static final String CONTENT_ID        = "dateField_content"; 
  private static final String DATE_ID           = "dateField_date";
  private static final String TIME_ID           = "dateField_time"; 
  private static final String YEAR_VALUE_ID     = "dateField_year_value";
  private static final String MONTH_VALUE_ID    = "dateField_month_value";
  private static final String DAY_VALUE_ID      = "dateField_day_value";
  private static final String HOUR_VALUE_ID     = "dateField_hour_value";
  private static final String MINUTE_VALUE_ID   = "dateField_minute_value";
  private static final String SECOND_VALUE_ID   = "dateField_second_value";
  private static final String VALUE_ID          = "dateField_value";
  
  private static final String YEAR_POSTFIX    = "year";
  private static final String MONTH_POSTFIX   = "month";
  private static final String DAY_POSTFIX     = "day";
  private static final String HOUR_POSTFIX    = "hour";
  private static final String MINUTE_POSTFIX  = "minute";
  private static final String SECOND_POSTFIX  = "second";
  //... 
  
  private boolean _edit;
  private ITimeConstraint _constraint;
  private Element _insertElement;
  private String _fieldName;
  private Locale _locale = Locale.getDefault();
  private TimeZone _timeZone  = TimeZone.getDefault();
  private String _layoutKey = IDocumentKeys.VARIOUS;
  private int _paramIndex;
  private String _value;

  public DateFieldRenderer(RenderingContext rContext, boolean edit)
  {
    super(rContext);
    _edit = edit;
    reset();
  }

  public void reset()
  {
    _constraint = null;
    _insertElement = null;
    _fieldName = null;
    _paramIndex = 0;
  }

  protected void render() throws RenderingException
  {
    try
    {
      RenderingContext rContext = getRenderingContext();
      if(_insertElement == null) return;
      if(_edit)
      {
        insertLayout(rContext);
        removeUnusedFields(rContext);
        prepareFields(rContext);
        addInitialisationScript(rContext);
        cleanupIds(rContext);
      }
      else
      {
        throw new UnsupportedOperationException("view mode not yet supported");
      }      
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering date field",t);
    }
  }

  private void addInitialisationScript(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      addJavaScriptNode(_insertElement, getUpdateCallScript() );
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering datefield initialisation script",t);
    }
  }

  private void insertLayout(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      IDocumentManager docManager = rContext.getDocumentManager();
      if(docManager == null)
      {
        throw new java.lang.NullPointerException("null documentManager");
      }
      Document layout = docManager.getDocument(_layoutKey,false);
      Node dateField = getElementByAttributeValue(CONTENT_ID,"id",layout);
      if(dateField == null)
      {
        throw new RenderingException("Could not find node with id \""+ CONTENT_ID + "\" "
                                      + "in document with key \"" + _layoutKey + "\"");
      }
      dateField = _target.importNode(dateField, true);
      if (_insertElement == null)
        throw new NullPointerException("insertElement has not been initialised");
      removeAllChildren(_insertElement);
      _insertElement.appendChild(dateField);
      includeJavaScript(IGlobals.JS_DATEFIELD_UTILS);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error inserting dateField layout in target document",t);
    }
  }
  
  private Element getDfElement(String id)
    throws RenderingException
  {
    Element dfElement = findElement(_insertElement,null,"id",id);
    return dfElement;
  }
  
  private void removeDfElementId(String id)
    throws RenderingException
  {
    Element dfElement = getDfElement(id);
    if(dfElement != null)
    {
      dfElement.removeAttribute(id);
    }
  }
  
  private void prepareFields(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      Element valueNode   = getDfElement(VALUE_ID);
      valueNode.setAttribute("name", _fieldName);
      
      Element yearNode    = getDfElement(YEAR_VALUE_ID);
      Element monthNode   = getDfElement(MONTH_VALUE_ID);
      Element dayNode     = getDfElement(DAY_VALUE_ID);
      Element hourNode    = getDfElement(HOUR_VALUE_ID);
      Element minuteNode  = getDfElement(MINUTE_VALUE_ID);
      Element secondNode  = getDfElement(SECOND_VALUE_ID);
      renderNumericOptions(monthNode,1,12,true);
      renderNumericOptions(dayNode,1,31,true);
      renderNumericOptions(hourNode,0,23,true);
      renderNumericOptions(minuteNode,0,59,true);
      renderNumericOptions(secondNode,0,59,true);
      renderOnchange(yearNode);
      renderOnchange(monthNode);
      renderOnchange(dayNode);
      renderOnchange(hourNode);
      renderOnchange(minuteNode);
      renderOnchange(secondNode);
      renderFieldName(yearNode,YEAR_POSTFIX);
      renderFieldName(monthNode,MONTH_POSTFIX);
      renderFieldName(dayNode,DAY_POSTFIX);
      renderFieldName(hourNode,HOUR_POSTFIX);
      renderFieldName(minuteNode,MINUTE_POSTFIX);
      renderFieldName(secondNode,SECOND_POSTFIX);
      
      if(StaticUtils.stringEmpty(_value))
      {
        _value = getNow();
      }
      valueNode.setAttribute("value", _value); 
      
      try
      {
        String[] a = StaticUtils.explode(_value, " ");
        String dateString = a[0];
        String timeString = a[1];
        
        String[] b = StaticUtils.explode(dateString, "-");
        String year = b[0];
        String month = b[1];
        String day = b[2];
        
        String[] c = StaticUtils.explode(timeString, ":");
        String hour = c[0];
        String minute = c[1];
        String second = c[2];
        
        renderFieldValue(yearNode,year);
        renderFieldValue(monthNode,month);
        renderFieldValue(dayNode,day);
        renderFieldValue(hourNode,hour);
        renderFieldValue(minuteNode,minute);
        renderFieldValue(secondNode,second);
      }
      catch(Throwable oops)
      {
        oops.printStackTrace(); //duh!
      }  
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error preparing dateField select options",t);
    }
  }
  
  public String getNow()
  {
    java.util.Date now = new Date();
    DateFormat dateFormat = new SimpleDateFormat(DateUtils.DATE_INPUT_PATTERN_YYYY);
    String nowString = DateUtils.formatDateInZone(now, _timeZone, _locale, dateFormat);
    return nowString;
  }
  
  private void renderFieldValue(Element node, String value)
    throws RenderingException
  {
    try
    {
      if(node == null) return;
      if( "select".equals( node.getNodeName()) )
      {
        renderSelectedOptions(node, value);
      }
      else
      {
        node.setAttribute("value",value);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering field value",t);
    }
  }
  
  private void renderFieldName(Element node, String postfix)
    throws RenderingException
  {
    try
    {
      if(node == null) return;
      String name = _fieldName + "_" + _paramIndex + "_" + postfix;
      node.setAttribute("name",name);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering fieldname",t);
    }
  }
  
  private void renderNumericOptions(Element node,
                                    int first,
                                    int last,
                                    boolean pad)
    throws RenderingException
  {
    try
    {
      if(node == null) return;
      removeAllChildren(node);
      for(int i=first; i <= last; i++)
      {
        String numberString = getPadded(i);
        Element option = _target.createElement("option");
        option.setAttribute("value", numberString);
        replaceText(option, numberString);
        node.appendChild(option);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering numeric options",t);
    }
  }
  
  private String getPadded(int number)
  {
    String numberString = "" + number;
    if(numberString.length() < 2)
    {
      numberString = "0" + numberString;
    }
    return numberString;
  }
  
  private void renderOnchange(Element node)
    throws RenderingException
  {
    try
    {
      if(node == null) return;
      node.setAttribute("onchange", getUpdateCallScript() );
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error rendering onchange script",t);
    }  
  }
  
  private String getUpdateCallScript()
  {
    return "updateDateField('"
            + _fieldName + "',"
            + _paramIndex
            +");";
  }
  
  private void cleanupIds(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      removeDfElementId(CONTENT_ID);
      removeDfElementId(DATE_ID);
      removeDfElementId(TIME_ID);
      removeDfElementId(YEAR_VALUE_ID);
      removeDfElementId(MONTH_VALUE_ID);
      removeDfElementId(DAY_VALUE_ID);
      removeDfElementId(HOUR_VALUE_ID);
      removeDfElementId(MINUTE_VALUE_ID);
      removeDfElementId(SECOND_VALUE_ID);
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error cleaning up id attributes",t);
    }
  }
  
  private void removeUnusedFields(RenderingContext rContext)
    throws RenderingException
  {
    try
    {
      if(!_constraint.hasDate())
      {
        removeNode(DATE_ID,false);
      }
      if(!_constraint.hasTime())
      {
        removeNode(TIME_ID,false);
      }
    }
    catch(Throwable t)
    {
      throw new RenderingException("Error removing unused date fields",t);
    }
  }


  //Property accessors............................
  
  public ITimeConstraint getConstraint()
  {
    return _constraint;
  }

  public Element getInsertElement()
  {
    return _insertElement;
  }

  public void setConstraint(ITimeConstraint constraint)
  {
    _constraint = constraint;
  }

  public void setInsertElement(Element insertElement)
  {
    _insertElement = insertElement;
  }

  public String getFieldName()
  {
    return _fieldName;
  }

  public void setFieldName(String fieldName)
  {
    _fieldName = fieldName;
  }

  public Locale getLocale()
  {
    return _locale;
  }

  public void setLocale(Locale locale)
  {
    _locale = locale;
  }

  public TimeZone getTimeZone()
  {
    return _timeZone;
  }

  public void setTimeZone(TimeZone timeZone)
  {
    _timeZone = timeZone;
  }

  public String getLayoutKey()
  {
    return _layoutKey;
  }

  public int getParamIndex()
  {
    return _paramIndex;
  }

  public void setLayoutKey(String layoutKey)
  {
    _layoutKey = layoutKey;
  }

  public void setParamIndex(int paramIndex)
  {
    _paramIndex = paramIndex;
  }

  public String getValue()
  {
    return _value;
  }

  public void setValue(String value)
  {
    _value = value;
  }

}