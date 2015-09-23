/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityFieldNameValueRetriever.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-20     Andrew Hill         Created
 * 2003-02-20     Andrew Hill         buildFmiCollection doesnt include unlabelled fields
 * 2003-05-18     Andrew Hill         Allow specification fo filter to restrict fields returned
 * 2003-06-09     Andrew Hill         Only include displayable fields
 */
package com.gridnode.gtas.client.web.renderers;

import java.util.*;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.*;

/**
 * OptionValueRetriever for fieldnames. The choices passed should be instances of
 * IGTFieldMetaInfo.
 */
public class EntityFieldNameValueRetriever implements IOptionValueRetriever
{
  public static final int SUBMIT_NAME = 0;
  public static final int SUBMIT_ID = 1;

  private int _maxDisplayLength = 0;
  private int _submitType;
  private ISimpleResourceLookup _rLookup;
  private String _entityType;
  private IFilter _filter; //20030518AH

  public EntityFieldNameValueRetriever( ISimpleResourceLookup rLookup ,int submitType, String entityType )
  {
    setResourceLookup(rLookup);
    setSubmitType(submitType);
    setEntityType(entityType);
  }

  public void setEntityType(String entityType)
  {
    if(entityType == null) throw new NullPointerException("entityType is null"); //20030416AH
    _entityType = entityType;
  }

  public String getEntityType()
  {
    return _entityType;
  }

  public void setResourceLookup(ISimpleResourceLookup rLookup)
  {
    if(rLookup == null) throw new NullPointerException("rLookup is null"); //20030416AH
    _rLookup = rLookup;
  }

  public ISimpleResourceLookup getResourceLookup()
  {
    return _rLookup;
  }

  public void setSubmitType(int submitType)
  {
    switch(submitType)
    {
      case SUBMIT_ID:
      case SUBMIT_NAME:
        _submitType = submitType;
        break;

      default:
        throw new java.lang.IllegalArgumentException("Unrecognised submit type:" + submitType);
    }
  }

  public int getSubmitType()
  {
    return _submitType;
  }

  public void setMaxDisplayLength(int length)
  {
    _maxDisplayLength = length;
  }

  public int getMaxDisplayLength()
  {
    return _maxDisplayLength;
  }

  /**
   * Returns the text to be displayed for the specified field (also does i18n)
   * @param choice an Object whose class implements IGTFieldMetaInfo
   * @return String text
   * @throws GTClientException
   */
  public String getOptionText(Object choice)
    throws GTClientException
  {
    IGTFieldMetaInfo fmi = (IGTFieldMetaInfo)choice;
    String text = fmi.getFieldName();
    text = _rLookup.getMessage(_entityType + "." + text);
    if( (text == null) || (_maxDisplayLength == 0) || (text.length() < _maxDisplayLength) )
    {
      return text;
    }
    else
    {
      return text.substring(0,_maxDisplayLength);
    }
  }

  /**
   * Returns the value to be submitted/stored for the specified choice
   * @param choice an Object whose class implements IGTFieldMetaInfo
   * @return String value
   * @throws GTClientException
   */
  public String getOptionValue(Object choice)
    throws GTClientException
  {
    IGTFieldMetaInfo fmi = (IGTFieldMetaInfo)choice;
    switch(_submitType)
    {
      case SUBMIT_ID:
        return StaticUtils.stringValue( fmi.getFieldId() );

      case SUBMIT_NAME:
        return fmi.getFieldName();

      default:
        throw new java.lang.IllegalStateException("_submitType=" + _submitType);
    }
  }

  /**
   * Utility method to get a collection of fmi to pass to renderSelectOptions etc...
   */
  public static Collection buildFmiCollection(IGTSession gtasSession,
                                              String entityType,
                                              boolean includeVirtualFields)
  throws GTClientException
  { 
    //20030518AH - Delegate to newer version
    return buildFmiCollection(gtasSession,entityType,includeVirtualFields,null,null);
  }
  
  public static Collection buildFmiCollection(IGTSession gtasSession,
                                              String entityType,
                                              boolean includeVirtualFields,
                                              IFilter filter,
                                              Object filterContext)
  throws GTClientException
  { //20030518AH - Now takes a filter
    try
    {
      if(gtasSession == null) throw new NullPointerException("gtasSession is null"); //20030416AH
      if(entityType == null) throw new NullPointerException("entityType is null"); //20030416AH
      IGTManager manager = gtasSession.getManager(entityType);
      IGTFieldMetaInfo[] fmiArray = manager.getSharedFieldMetaInfo(entityType);
      ArrayList fmiCollection = new ArrayList(fmiArray.length);
      for(int i=0; i < fmiArray.length; i++)
      { //20030220AH - Refactored syntax
        IGTFieldMetaInfo fmi = fmiArray[i];
        if(includeField(fmi, includeVirtualFields, filter, filterContext))
        {
          fmiCollection.add(fmi);
        }
      }
      return fmiCollection;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error building fmi collection",t);
    }
  }

  private static boolean includeField(IGTFieldMetaInfo fmi,
                                      boolean includeVirtualFields,
                                      IFilter filter,
                                      Object filterContext)
    throws GTClientException
  { //20030220AH - Factored out check to see if field should be included
    //20030518AH - Support a filter
    try
    {
      boolean include = true;
      if(StaticUtils.stringNotEmpty(fmi.getLabel()))
      { //Never include unlabeled fields - though there should never be any anyhow!
        if(!includeVirtualFields)
        {
          if(fmi.isVirtualField()) include = false;
        }
        //20030609AH - Only include displayable fields
        boolean displayable = fmi.isDisplayable(true) || fmi.isDisplayable(false);
        if(!displayable) include = false;
        //...
      }
      else
      {
        include = false;
      }
      if(include && (filter != null))
      {
        include = filter.allows(fmi, null);
      }
      return include;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error determining if field should be included",t);
    }
  }
  
  public IFilter getFilter()
  {
    return _filter;
  }

  public void setFilter(IFilter filter)
  {
    _filter = filter;
  }

}