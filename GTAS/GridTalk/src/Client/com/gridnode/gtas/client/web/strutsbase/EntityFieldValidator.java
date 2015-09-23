/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityFieldValidator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-29     Andrew Hill         Created
 * 2002-11-16     Andrew Hill         validateKeys() method
 * 2002-12-20     Andrew Hill         addFieldError() & validate methods return false
 *                                    if validation failed
 * 2003-01-06     Andrew Hill         Added MISMATCH key
 * 2003-01-31     Andrew Hill         actionErrors now optional for addFieldError()
 * 2003-04-10     Andrew Hill         basicValidateFiles() can now check file name extension
 * 2003-05-15     Andrew Hill         basicValidateKeys() now checks array contents too
 * 2003-05-19     Andrew Hill         Factored out some code in FieldValidator and make this class extend it
 * 2003-09-18     Daniel D'Cotta      Added basicValidatStringArray()
 * 2004-02-11     Andrew Hill         Added basicValidateUrl() (based on code by Mahesh)
 * 2004-07-13     Mahesh              Added check for range constraint
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.net.URL;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.utils.StaticUtils;

/**
 * @todo: refactor and reduce number of methods (preferably to one!)
 * @todo: add method for adding std error types for specified fields
 */
public class EntityFieldValidator extends FieldValidator
{
  public static boolean validateKeys(ActionErrors actionErrors,
                                  String field,
                                  String[] keys,
                                  Number entityField,
                                  IGTEntity entity)
    throws GTClientException
  {
    try
    {
      IGTFieldMetaInfo fmi = entity.getFieldMetaInfo(entityField);
      boolean newEntity = entity.isNewEntity();
      if(fmi == null)
      {
        throw new java.lang.NullPointerException("No fieldMetaInfo for field:"
                                                  + entityField + "("
                                                  + field +") of entity " + entity);
      }
      if( (!fmi.isDisplayable(newEntity)) || (!fmi.isEditable(newEntity)) )
      {
        return true;
      }
      if(fmi.isMandatory(newEntity))
      {
        if( (keys == null) || (keys.length == 0) )
        {
          //actionErrors.add(field,new ActionError(entity.getType() + ".error." + field + ".required"));
          addFieldError(actionErrors,field,entity.getType(),REQUIRED,null); //20021220AH
          return false;
        }
        else
        { //20030515AH - Check each key to make sure it has a value as we could well
          //just have an array with a single empty string (or such like)
          boolean someOk = false;
          boolean someEmpty = false;
          for(int i=0; i < keys.length; i++)
          {
            if( StaticUtils.stringEmpty(keys[i]) )
            {
              someEmpty = true;
            }
            else
            {
              someOk = true;
            } 
          }
          if(someEmpty)
          {
            addFieldError(actionErrors,field,entity.getType(),someOk ? INVALID : REQUIRED,null);
            return false;
          }
        }
      }
      return true;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error validating field " + entityField,t);
    }
  }

  /**
   * Performs rudimentary string field validation checking for the presence of data in
   * fields that are mandatory and the maximum length of the field.
   */
  public static boolean basicValidateString(ActionErrors actionErrors,
                                      String field,
                                      String value,
                                      Number entityField,
                                      IGTEntity entity)
    throws GTClientException
  {
    try
    {
      IGTFieldMetaInfo fmi = entity.getFieldMetaInfo(entityField);
      boolean newEntity = entity.isNewEntity();
      if(fmi == null)
      {
        throw new java.lang.NullPointerException("No fieldMetaInfo for field:"
                                                  + entityField + "("
                                                  + field +") of entity " + entity);
      }
      if( (!fmi.isDisplayable(newEntity)) || (!fmi.isEditable(newEntity)) )
      {
        return true;
      }
      if(fmi.isMandatory(newEntity))
      {
        if( (value == null) || (value.equals("")) )
        {
          //actionErrors.add(field,new ActionError(entity.getType() + ".error." + field + ".required"));
          addFieldError(actionErrors,field,entity.getType(),REQUIRED,null); //20021220AH
          return false;
        }
      }

      if(fmi.getConstraintType() == IConstraint.TYPE_TEXT)
      {
        ITextConstraint textConstraint = (ITextConstraint)fmi.getConstraint();
        /* 20030918 DDJ: Fixed checking of minLength when maxLength = -1         
        if(textConstraint.getMaxLength() > 0)
        {
          int valueLength = value==null ? 0 : value.length();
          if( (valueLength > textConstraint.getMaxLength())
              || (valueLength < textConstraint.getMinLength()) )
        */
        int valueLength = (value == null) ? 0 : value.length();
        if((textConstraint.getMaxLength() > 0 && valueLength > textConstraint.getMaxLength()) ||
           (valueLength < textConstraint.getMinLength()))
        {
          //actionErrors.add(field,new ActionError(entity.getType() + ".error." + field + ".invalid"));
          addFieldError(actionErrors,field,entity.getType(),INVALID,null); //20021220AH
          return false;
        }
      }
      
      boolean valueEmpty = ( (value == null) || ("".equals(value)) );
      Number convertedValue = null;
      if("java.lang.Integer".equals(fmi.getValueClass()) && !valueEmpty)
      {
        try
        {
          convertedValue = new Integer(value);
        }
        catch(Exception e)
        {
          //actionErrors.add(field,new ActionError(entity.getType() + ".error." + field + ".invalid"));
          addFieldError(actionErrors,field,entity.getType(),INVALID,null); //20021220AH
          return false;
        }
      }
      else if("java.lang.Short".equals(fmi.getValueClass()) && !valueEmpty)
      {
        try
        {
          convertedValue = new Short(value);
        }
        catch(Exception e)
        {
          //actionErrors.add(field,new ActionError(entity.getType() + ".error." + field + ".invalid"));
          addFieldError(actionErrors,field,entity.getType(),INVALID,null); //20021220AH
          return false;
        }
      }
      else if("java.lang.Long".equals(fmi.getValueClass()) && !valueEmpty)
      {
        try
        {
          convertedValue = new Long(value);
        }
        catch(Exception e)
        {
          //actionErrors.add(field,new ActionError(entity.getType() + ".error." + field + ".invalid"));
          addFieldError(actionErrors,field,entity.getType(),INVALID,null); //20021220AH
          return false;
        }
      }
      
      if(convertedValue!= null && fmi.getConstraintType() == IConstraint.TYPE_RANGE)
      { //This is to apply range constraint
        try
        {
          IRangeConstraint rangeConstraint= (IRangeConstraint)fmi.getConstraint();
          Number min = rangeConstraint.getMin(0);
          Number max = rangeConstraint.getMax(0);
          if(min==null)
            throw new NullPointerException("min is null");
          if(max==null)
            throw new NullPointerException("min is null");  
          if((convertedValue.doubleValue() < min.doubleValue()) || (convertedValue.doubleValue() > max.doubleValue()))
            throw new Exception("Value out of range, constraint min="+ min + ", max=" + max);
        }
        catch(Exception ex)
        {
          addFieldError(actionErrors,field,entity.getType(),INVALID,null);
          return false;
        }
      }
      return true;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error validating field " + entityField,t);
    }
  }

  // 20030918 DDJ: Added method
  public static boolean basicValidateStringArray( ActionErrors actionErrors,
                                                  String field,
                                                  String[] value,
                                                  Number entityField,
                                                  IGTEntity entity)
    throws GTClientException
  {
    try
    {
      IGTFieldMetaInfo fmi = entity.getFieldMetaInfo(entityField);
      if(fmi == null)
      {
        throw new java.lang.NullPointerException("No fieldMetaInfo for field:"
                                                  + entityField + "("
                                                  + field +") of entity " + entity);
      }
      boolean newEntity = entity.isNewEntity();
      if(!fmi.isDisplayable(newEntity) || !fmi.isEditable(newEntity))
      {
        return true;
      }
      if(fmi.isMandatory(newEntity))
      {
        if(value == null || value.length == 0)
        {
          //actionErrors.add(field,new ActionError(entity.getType() + ".error." + field + ".required"));
          addFieldError(actionErrors, field, entity.getType(), REQUIRED, null);
          return false;
        }
      }
      // validate based on value class
      String valueClass = fmi.getValueClass();
      for(int i = 0; i < value.length; i++)
      {
        try
        {        
          StaticUtils.convert(value[i], valueClass, true, false);
        }
        catch(UnsupportedOperationException valueClassUnsupported)
        {
          // swallow, ignore testing unsupported value class
        }
        catch(Throwable t)
        {
          addFieldError(actionErrors, field, entity.getType(), INVALID, null);
          return false;
        }        
      }
      return true;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error validating field " + entityField, t);
    }
  }

  public static boolean basicValidateFiles( ActionErrors actionErrors,
                                     Number fieldId,
                                     ActionForm form,
                                     IGTEntity entity)
    throws GTClientException
  { //20030410AH
    return basicValidateFiles(actionErrors,fieldId,form,entity,null);
  }

  public static boolean basicValidateFiles( ActionErrors actionErrors,
                                     Number fieldId,
                                     ActionForm form,
                                     IGTEntity entity,
                                     String[] extensions)
    throws GTClientException
  {
    try
    {
      boolean newEntity = entity.isNewEntity();
      if(!(form instanceof GTActionFormBase))
      {
        throw new java.lang.UnsupportedOperationException("Form must be a subclass of GTActionFormBase");
      }
      IGTFieldMetaInfo fmi = entity.getFieldMetaInfo(fieldId);
      if(fmi == null)
      {
        throw new java.lang.NullPointerException("No fieldMetaInfo for field:"
                                                  + fieldId + " of entity " + entity);
      }
      if( (!fmi.isDisplayable(newEntity)) || (!fmi.isEditable(newEntity)) )
      {
        return true;
      }
      String fieldName = fmi.getFieldName(); //20030410AH
      FormFileElement[] elements = ((GTActionFormBase)form).getFormFileElements(fieldName); //20030410AH
      if(fmi.isMandatory(newEntity))
      {
        if( (elements == null) || (elements.length == 0) )
        {
          addFieldError(actionErrors,fieldName,entity.getType(),REQUIRED,null); //20021220AH
          return false;
        }
      }
      if(extensions != null)
      { //20030410AH - Check file extensions are of those allowed
        boolean allOk = true;
        if(elements != null)
        {
          for(int i=0; i < elements.length; i++)
          {
            String filename = elements[i].getFileName();
            if(filename == null) throw new NullPointerException("Internal assertion failure: filename is null");
            String extension = StaticUtils.getFileExtension(filename);
            if(StaticUtils.findValueInArray(extensions,extension,false) == -1)
            {
              String[] params = new String[] { filename };
              addFieldError(actionErrors,fieldName,entity.getType(),INVALID_EXTENSION,params);
              return false;
            }
          }
        }
      }
      return true;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error validating file field:" + fieldId,t);
    }
  }

  public static boolean basicValidateString( ActionErrors actionErrors,
                                      Number fieldId,
                                      ActionForm form,
                                      IGTEntity entity )
    throws GTClientException
  {
    try
    {
      String fieldName = entity.getFieldName(fieldId);
      String value = (String)PropertyUtils.getSimpleProperty(form, fieldName);
      return basicValidateString(actionErrors,fieldName,value,fieldId,entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("Error validating field:" + fieldId,e);
    }
  }
  
  public static boolean basicValidateUrl( ActionErrors actionErrors,
                                          Number fieldId,
                                          ActionForm form,
                                          IGTEntity entity)
    throws GTClientException
  { //20040211AH
    try
    {
      String fieldName = entity.getFieldName(fieldId);
      String value = (String)PropertyUtils.getSimpleProperty(form, fieldName);
      try
      {
        new URL(value);
      }
      catch(Exception e)
      {
        addFieldError(actionErrors,fieldName,entity.getType(),INVALID,null);
        return false;
      }
      return true;
    }
    catch(Exception e)
    {
      throw new GTClientException("Error validating (url) field:" + fieldId,e);
    }
  }

  public static boolean basicValidateStringArray( ActionErrors actionErrors,
                                                  Number fieldId,
                                                  ActionForm form,
                                                  IGTEntity entity )
    throws GTClientException
  {
    try
    {
      String fieldName = entity.getFieldName(fieldId);
      String[] value = (String[])PropertyUtils.getSimpleProperty(form, fieldName);
      return basicValidateStringArray(actionErrors,fieldName,value,fieldId,entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("Error validating field:" + fieldId,e);
    }
  }

  /**
   * Will create and add an ActionError for the specified field with i18n key of the form
   * entityType.error.fieldname.keySuffix
   * The ActionError object will also be returned to the caller.
   * 20021220AH
   * @param actionErrors - the action errors object (optional)
   * @param fieldName - name of the field - keys the error in the action errors
   * @param entityType - prefix for the i18n key which is usually the entity type
   * @param keySuffix - postfix for the i18n key, usually "required" or "invalid" (use constants supplied)
   * @param params - array of objects use as i18n substition parameters (pass null if not wanted)
   * @return ActionError object that was created and added to actionErrors
   */
  public static ActionError addFieldError(ActionErrors actionErrors,
                                          String fieldName,
                                          String entityType,
                                          String keySuffix,
                                          Object[] params)
  { //20021220AH
    //20030131AH - Made actionErrors optional & no longer throws exception
    if(fieldName == null) throw new NullPointerException("fieldName is null"); //20030416AH
    if(entityType == null) throw new NullPointerException("entityType is null"); //20030416AH
    if(keySuffix == null) throw new NullPointerException("keySuffix is null"); //20030416AH
    StringBuffer buffer = new StringBuffer();
    buffer.append(entityType);
    buffer.append(".error.");
    buffer.append(fieldName);
    buffer.append(".");
    buffer.append(keySuffix);
    //20030519AH - Factored out ActionError creation and addition to new method
    return createActionError(actionErrors, fieldName, buffer.toString(), params);
  }
}