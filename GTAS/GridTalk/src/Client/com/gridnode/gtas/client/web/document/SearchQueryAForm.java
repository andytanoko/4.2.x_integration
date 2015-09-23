/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchQueryAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-10-27     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.web.document;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class SearchQueryAForm extends GTActionFormBase
{
  private boolean _isNewEntity;
  private String _name;
  private String _description;
  private String _createdBy;
  private String _isPublic = "false";
  
  private ArrayList _conditions;
  private String _updateAction;
  
  public SearchQueryAForm()
  {
    _conditions = new ArrayList();
  }

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    // reset all the nested action forms
    if(_conditions != null)
    {
      Iterator i = _conditions.iterator();
      while(i.hasNext())
      {
        ((ConditionAForm)i.next()).reset(mapping, request);
      }
    }
    
    _isPublic = "false";
  }

  public String getName()
  {
    return _name;
  }

  public void setName(String string)
  {
    _name = string;
  }

  public String getDescription()
  {
    return _description;
  }

  public void setDescription(String string)
  {
    _description = string;
  }

  public String getCreatedBy()
  {
    return _createdBy;
  }

  public void setCreatedBy(String string)
  {
    _createdBy = string;
  }

  public ConditionAForm[] getConditions()
  {
//System.out.println("### SearchQueryAForm.getConditions(): _conditions.size()=" + _conditions.size());
    return (ConditionAForm[])_conditions.toArray(new ConditionAForm[_conditions.size()]);
  }

  public void setConditions(ConditionAForm[] forms)
  {
    _conditions = StaticUtils.arrayListValue(forms);
//System.out.println("### SearchQueryAForm.setConditions(): _conditions.size()=" + _conditions.size());
  }
  
  public void addNewCondition(ConditionAForm condition)
  {
    if(condition == null)
    {
      condition = new ConditionAForm();
    } 
    _conditions.add(condition);
  }
  
  public void removeSelectedConditions()
  {
    if( _conditions == null) return;
    ListIterator li = _conditions.listIterator();
    while(li.hasNext())
    {
      ConditionAForm conditionForm = (ConditionAForm)li.next();
      if(conditionForm.isSelected())
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
  
  public String getIsPublic()
  {
    return _isPublic;
  }
  
  public void setIsPublic(String isPublic)
  {
    
    _isPublic = isPublic;
  }
}