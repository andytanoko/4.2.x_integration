/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataFilterValidator.java.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 20/05/2003     Andrew Hill         Created 
 */
package com.gridnode.gtas.client.web.query;

import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.ctrl.IGTSession;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.FieldValidator;

public class DataFilterValidator extends FieldValidator
{
  public static boolean validateDataFilter( ActionErrors actionErrors,
                                            DataFilterAForm filterForm,
                                            boolean required,
                                            String entityType,
                                            String fieldName,
                                            String errorMsgPrefix,
                                            IGTSession gtasSession )
    throws GTClientException
  {
    if (gtasSession == null)
      throw new NullPointerException("gtasSession is null");
    if (entityType == null)
      throw new NullPointerException("entityType is null");
    if( actionErrors != null)
    {
      if(errorMsgPrefix == null)
        throw new NullPointerException("errorMsgPrefix cannot be null if actionErrors object is provided");
      if(fieldName == null)
        throw new NullPointerException("fieldName cannot be null if actionErrors object is provided");
    }
    
    try
    {
      if(required)
      {
        if(filterForm == null)
        {
          if(actionErrors != null) createActionError(actionErrors,fieldName,errorMsgPrefix+ "." + REQUIRED,null);
          return false;
        }
      }
      else
      {
        if(filterForm == null) return true;
      }
      
      IGTManager manager = gtasSession.getManager(entityType);
      if (manager == null) //Sanity check
        throw new NullPointerException("manager is null");      
        
      Object[] valueFilterForms = filterForm.getValueFilters();
      int numValueForms = valueFilterForms == null ? 0 : valueFilterForms.length;
      if(required)
      {
        if( (valueFilterForms == null) || (valueFilterForms.length == 0) )
        {
          if(actionErrors != null) createActionError(actionErrors,fieldName,errorMsgPrefix+ "." + REQUIRED,null);
          return false;
        }
      }
      else
      {      
        //nothing to validate here. Move along please.
        return true;
      }
      boolean someFailed = false;
      for(int i=0; i < numValueForms; i++)
      {
        ValueFilterAForm valueFilterForm = (ValueFilterAForm)valueFilterForms[i];
        if (valueFilterForm == null) //Sanity check
          throw new NullPointerException("valueFilterForm is null");
        boolean valueFilterOk = validateValueFilter(actionErrors,
                                                    valueFilterForm,
                                                    entityType,
                                                    fieldName,
                                                    i,
                                                    manager);
        if(!valueFilterOk) someFailed = true;
      }
      if(someFailed)
      {
        if(actionErrors != null) createActionError(actionErrors,fieldName,errorMsgPrefix+ "." + INVALID,null);
        return false;
      }  
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error validating data filter field",t);
    }
    return true;
  }
  
  protected static boolean validateValueFilter( ActionErrors actionErrors,
                                                ValueFilterAForm valueFilterForm,
                                                String entityType,
                                                String fieldName,
                                                int rowIndex,
                                                IGTManager manager )
    throws GTClientException
  {
    try
    {     
      boolean rowOk = true;
      String rowFieldName = fieldName + "." + "valueFilters[" + rowIndex + "]";
      //We dont bother to validate the connector at this stage
      //Validate the field field
      String fieldIdString = valueFilterForm.getField();
      
      if(!validateValue(actionErrors,
                        fieldIdString,
                        Number.class.getName(),
                        true,
                        null,
                        rowFieldName + ".field",
                        "generic") )
      {
        rowOk = false;  
      }
      //Validate the params
      String[] params = valueFilterForm.getParams();
      if( StaticUtils.stringArrayEmpty(params, false) )
      {
        if(actionErrors != null)
        {
          createActionError(actionErrors,rowFieldName + ".params","generic." + REQUIRED,null);
          createActionError(actionErrors,rowFieldName + ".params[0]","generic." + REQUIRED,null);
        }
        rowOk = false;
      }
      else
      {
        Number fieldId = StaticUtils.integerValue( fieldIdString );
        IGTFieldMetaInfo fmi = manager.getSharedFieldMetaInfo(entityType, fieldId);
        for(int i=0; i < params.length; i++)
        {
          
          if(!validateValue(actionErrors,
                            params[i],
                            fmi.getValueClass(),
                            true,
                            fmi.getConstraint(),
                            rowFieldName + ".params[" + i + "]",
                            "generic" ) )
          {
            rowOk = false;
          }
        }
      }
      return rowOk;  
    }
    catch (Throwable t)
    {
      throw new GTClientException("Error validating value filter",t);
    }
  }
}
