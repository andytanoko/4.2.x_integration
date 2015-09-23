/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FieldValidator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-19     Andrew Hill         Created & made superclass of EntityFieldValidator
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.util.Collection;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IEnumeratedConstraint;
import com.gridnode.gtas.client.ctrl.IRangeConstraint;
import com.gridnode.gtas.client.ctrl.ITextConstraint;
import com.gridnode.gtas.client.utils.StaticUtils;

public class FieldValidator
{
  public static final String REQUIRED = "required";
  public static final String INVALID  = "invalid";
  public static final String MISMATCH = "mismatch";
  public static final String INVALID_EXTENSION = "invalidExtension";

  public static boolean validateValue(ActionErrors actionErrors,
                                      String value,
                                      String valueClass,
                                      boolean required,
                                      Object constraint,
                                      String fieldName,
                                      String errorMsgPrefix)
    throws GTClientException
  {
    //@todo: for the IConstraints we should move the checking into the constraint itself
    //as a new method that must be implemented and then we call that from here
    if (valueClass == null)
        throw new NullPointerException("valueClass is null");
    if( actionErrors != null)
    {
      if(errorMsgPrefix == null)
        throw new NullPointerException("errorMsgPrefix cannot be null if actionErrors object is provided");
      if(fieldName == null)
        throw new NullPointerException("fieldName cannot be null if actionErrors object is provided");
    }
    try
    {
      boolean valueEmpty = StaticUtils.stringEmpty(value);
      
      if(required && valueEmpty)
      {
        if(actionErrors != null) createActionError(actionErrors,fieldName,errorMsgPrefix + "." + REQUIRED,null);
        return false;
      }
      
      Object convertedValue = null;
      if(!valueEmpty)
      {
        try
        {
          //We will try to convert the value to the specified valueclass using the normal
          //methods and see if it works.
          convertedValue = StaticUtils.convert(value, valueClass, true, false);
          //We keep the converted value reference as we might use it later in this method
          //depending on our constraints
        }
        catch(UnsupportedOperationException ambiguous)
        {
           //swallow - it didnt work but that was
           //because the convert method didnt recognise the valueClass so we still dont
           //know if our string was valid or not. We will give it the benefit of the doubt
           //and assume that it is.
        }
        catch(GTClientException badValue)
        {
          if(actionErrors != null) createActionError(actionErrors,fieldName,errorMsgPrefix + "." + INVALID,null);
          return false;
        }
      }
      
      if(constraint != null)
      {
        if(constraint instanceof String[])
        { //nb: String[] array we will compare with string value directly
          if(!StaticUtils.arrayContains((String[])constraint, value))
          {
            if(actionErrors != null) createActionError(actionErrors,fieldName,errorMsgPrefix + "." + INVALID,null);
            return false;
          }
        }
        else if(constraint instanceof Object[])
        { //Other object type we compare with the converted value
          if(!StaticUtils.arrayContains((Object[])constraint, convertedValue))
          {
            if(actionErrors != null) createActionError(actionErrors,fieldName,errorMsgPrefix + "." + INVALID,null);
            return false;
          }
        }
        if(constraint instanceof Collection)
        { //and the same goes for collection (even a collection of string)
          if(!StaticUtils.collectionContains((Collection)constraint, convertedValue))
          {
            if(actionErrors != null) createActionError(actionErrors,fieldName,errorMsgPrefix + "." + INVALID,null);
            return false;
          }
        }
        else if(constraint instanceof ITextConstraint)
        {
          ITextConstraint textConstraint = (ITextConstraint)constraint;
          if(textConstraint.getMaxLength() > 0)
          {
            int valueLength = value == null ? 0 : value.length();
            if( (valueLength > textConstraint.getMaxLength())
                || (valueLength < textConstraint.getMinLength()) )
            {
              if(actionErrors != null) createActionError(actionErrors,fieldName,errorMsgPrefix + "." + INVALID,null);
              return false;
            }
          }
        }
        else if( constraint instanceof IEnumeratedConstraint )
        {
          try
          {
            if(!valueEmpty)
            {
              IEnumeratedConstraint enumeratedConstraint = (IEnumeratedConstraint)constraint;
              int size = enumeratedConstraint.getSize();
              boolean foundValue = false;
              for(int i=0; (i < size) && !foundValue; i++)
              {
                //nb we compare the string values here - not the converted ones
                String compareMe = enumeratedConstraint.getValue(i);
                if (compareMe == null)
                  throw new NullPointerException("enumeratedConstraint.getValue(" + i + ") returned null");
                if(compareMe.equals(value)) foundValue = true;
              }
              if(!foundValue)
              {
                if(actionErrors != null) createActionError(actionErrors,fieldName,errorMsgPrefix + "." + INVALID,null);
                return false;
              }
            }
          }
          catch(Throwable t)
              {
                throw new GTClientException("Error validating (raw) value "
                                            + value
                                            + " against enumerated constraint", t);
              }
        }
        else if( (constraint instanceof IRangeConstraint)
            && (convertedValue instanceof Number)
            && (!(convertedValue instanceof java.math.BigDecimal))
            && (!(convertedValue instanceof java.math.BigInteger)) )
        { //Currently we dont support BigXxx , but should be easy to do so if the need arises
          if(!valueEmpty)
          {
            IRangeConstraint rangeConstraint = (IRangeConstraint)constraint;
            int size = rangeConstraint.getSize();
            boolean isValid = false;
            for(int range=0; (range < size) && !isValid; range++)
            {
              Number number = (Number)convertedValue;
              Number min = rangeConstraint.getMin(range);
              Number max = rangeConstraint.getMax(range);
              try
              {
                if (min == null)
                  throw new NullPointerException("min is null");
                if (max == null)
                  throw new NullPointerException("max is null");
                //Im sure there has to be a better way of doing the following:
                //(Which certainly wont work too well for BigDecimal, BigInteger etc...) :-(
                if( !(   (number.doubleValue() < min.doubleValue())
                     || (number.doubleValue() > max.doubleValue())) )
                {
                  //If its not outside the range (which is inclusive) then it passed
                  isValid = true;
                }
              }
              catch(Throwable t)
              {
                throw new GTClientException("Error validating (converted) value "
                + number
                + " against range constraint ["
                + range
                + "] where min="
                + min
                + ", max="
                + max, t);
              }
            }
            if(!isValid)
            {
              if(actionErrors != null) createActionError(actionErrors,fieldName,errorMsgPrefix + "." + INVALID,null);
              return false;
            }
          }
        }
      }      
    }
    catch(Throwable t)
    {
      if(fieldName != null)
      {
        throw new GTClientException("Error validating value "
                                    + value
                                    + " of valueClass:"
                                    + valueClass
                                    + " (fieldName="
                                    + fieldName
                                    + ", errorMsgPrefix="
                                    + errorMsgPrefix
                                    + ")", t);
      }
      else
      {
        throw new GTClientException("Error validating value "
                                    + value
                                    + " of valueClass:"
                                    + valueClass, t);
      }
    }
    //If execution reaches this point then no validation issues were discovered
    return true;
  }

  /*
   * Convienience method to create and return an ActionError object using the specified error
   * message key. If an optional actionErrors reference is provided then the error will be
   * added to it under the fieldName provided. If no actionErrors is provided then the fieldName
   * parameter is ignored. The params argument is optional , but if provided will be passed
   * to the constructor of the ActionError. If not required you may leave the argument null.
   * @param actionErrors Optional ActionErrors object to which the ActionError will be added
   * @param fieldName If actionErrors provided this is the key the error is stored under
   * @param errorMsgKey A key into the i18n bundle for the message describing the error
   * @param params An array of objects that will act as i18n substitution parameters
   * @return error An ActionError object created based on the provided parameters
   */
  public static ActionError createActionError(ActionErrors actionErrors,
                                              String fieldName,
                                              String errorMsgKey,
                                              Object[] params)
  {
    if( (fieldName == null) && (actionErrors != null) )
      throw new NullPointerException("fieldName cannot be null if ActionErrors object is provided");
    if(errorMsgKey == null)
      throw new NullPointerException("errorMsgKey is null");
    ActionError error = null;
    if(params == null)
    {
      error = new ActionError(errorMsgKey);
    }
    else
    {
      error = new ActionError(errorMsgKey, params);
    }
    if(actionErrors != null)
    {
      actionErrors.add(fieldName,error);
    }
    return error;
  }
}