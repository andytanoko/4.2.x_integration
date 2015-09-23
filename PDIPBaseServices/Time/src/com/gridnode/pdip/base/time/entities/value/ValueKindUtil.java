// %1023788050918:com.gridnode.pdip.base.time.value%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: 
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.value;

public abstract class ValueKindUtil
{
  private static  String pkgName = "com.gridnode.pdip.base.time.entities.value.";
  /**
   * DOCUMENT ME!
   * 
   * @param valueKind DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public static boolean isPrimaryValue(int valueKind)
  {
    return true;
  }
  
  /**
   * DOCUMENT ME!
   * 
   * @param valueKind DOCUMENT ME!
   * @return DOCUMENT ME! 
   * @throws Exception DOCUMENT ME!
   */
  public static Class getValueClass(int valueKind)
                             throws Exception
  {
    String className;
   switch (valueKind)
   {
      case iCalValueKind.ICAL_RECUR_VALUE:
          className = pkgName + "iCalRecurrenceV";
          break;
      case iCalValueKind.ICAL_DATE_VALUE:
      case iCalValueKind.ICAL_DATETIME_VALUE:
         className = pkgName +"iCalDateV";
         break;
      case  iCalValueKind.ICAL_STATUS_VALUE:
      case  iCalValueKind.ICAL_TRANSP_VALUE:
      case  iCalValueKind.ICAL_CLASS_VALUE:
      case  iCalValueKind.ICAL_STRING_VALUE:
      case  iCalValueKind.ICAL_CALADDRESS_VALUE:
         className = pkgName +"iCalStringV";
         break;
      case  iCalValueKind.ICAL_TEXT_VALUE:
         className = pkgName +"iCalTextV";
         break;      
      case  iCalValueKind.ICAL_INTEGER_VALUE:
         className= pkgName +"iCalIntV";
         break;
      case  iCalValueKind.ICAL_PERIOD_VALUE:
         className = pkgName +"iCalPeriodV";
         break;
      case  iCalValueKind.ICAL_BOOLEAN_VALUE:
         className = pkgName +"iCalBooleanV";
         break;        
      case  iCalValueKind.ICAL_DURATION_VALUE:
         className = pkgName +"iCalDurationV";
         break;      
      case  iCalValueKind.ICAL_URI_VALUE:
         className = pkgName +"iCalURIV";
         break;        
      default:
         throw new IllegalArgumentException("ValueKindUtil.getValueClass(): valueKind="+valueKind+" not supported yet");      
   } 

    return Class.forName(className);
  }
}