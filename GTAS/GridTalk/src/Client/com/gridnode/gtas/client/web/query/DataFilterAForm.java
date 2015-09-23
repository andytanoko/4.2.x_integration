/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataFilterAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

/*
 * For use in the UI query editor widget to represent a DataFilter.
 * For now I will just support filters that dont do complicated stuff - that is just support
 * a bunch of valuefilters ANDed together. Later I would like to expand it to be able
 * to manage any kind of filter, but get this working first to learn the pitfalls. ;-)
 */
public class DataFilterAForm extends GTActionFormBase
{
  private ArrayList _valueFilters;
  private String _updateAction;

  public DataFilterAForm()
  {
    _valueFilters = new ArrayList(1);
    _valueFilters.add( new ValueFilterAForm() );
  }

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {    
    if(_valueFilters != null)
    {
      Iterator i = _valueFilters.iterator();
      while(i.hasNext())
      {
        ((ValueFilterAForm)i.next()).reset(mapping, request);
      }
    }
  }

  public Object[] getValueFilters()
  {
    return _valueFilters.toArray();
  }
  
  public void addValueFilter(ValueFilterAForm valueFilter)
  {
    if(valueFilter == null) valueFilter = new ValueFilterAForm();
    _valueFilters.add(valueFilter);
  }
  
  public void removeSelectedFilters()
  {
    if( _valueFilters == null) return;
    ListIterator li = _valueFilters.listIterator();
    while(li.hasNext())
    {
      ValueFilterAForm valueFilterForm = (ValueFilterAForm)li.next();
      if(valueFilterForm.isSelected())
      {
        li.remove();
      } 
    }
  }

  public String getUpdateAction()
  {
    return _updateAction;
  }

  public void setUpdateAction(String updateAction)
  {
    _updateAction = updateAction;
  }

}