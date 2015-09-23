// %1023788049262:com.gridnode.pdip.base.time.value%
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

public interface iCalValueKind
{
  public static final int ICAL_ANY_VALUE = 1000;
  public static final int ICAL_QUERY_VALUE = 1001;
  public static final int ICAL_TRIGGER_VALUE = 1002;
  public static final int ICAL_STATUS_VALUE = 1003;
  public static final int ICAL_TRANSP_VALUE = 1004;
  public static final int ICAL_CLASS_VALUE = 1005;
  public static final int ICAL_DATE_VALUE = 1006;
  public static final int ICAL_STRING_VALUE = 1007;
  public static final int ICAL_INTEGER_VALUE = 1008;
  public static final int ICAL_PERIOD_VALUE = 1009;
  public static final int ICAL_TEXT_VALUE = 1010;
  public static final int ICAL_DURATION_VALUE = 1011;
  public static final int ICAL_BOOLEAN_VALUE = 1012;
  public static final int ICAL_URI_VALUE = 1013;
  public static final int ICAL_DATETIMEPERIOD_VALUE = 1014;
  public static final int ICAL_GEO_VALUE = 1015;
  public static final int ICAL_DATETIME_VALUE = 1016;
  // public static final int   ICAL_XLICCLASS_VALUE=1017;
  public static final int ICAL_UTCOFFSET_VALUE = 1018;
  public static final int ICAL_ATTACH_VALUE = 1019;
  public static final int ICAL_ACTION_VALUE = 1020;
  public static final int ICAL_CALADDRESS_VALUE = 1021;
  public static final int ICAL_X_VALUE = 1022;
  public static final int ICAL_FLOAT_VALUE = 1023;
  public static final int ICAL_REQUESTSTATUS_VALUE = 1024;
  // public static final int   ICAL_METHOD_VALUE=1025;
  public static final int ICAL_BINARY_VALUE = 1026;
  public static final int ICAL_RECUR_VALUE = 1027;
  public static final int ICAL_NO_VALUE = 1028;
}