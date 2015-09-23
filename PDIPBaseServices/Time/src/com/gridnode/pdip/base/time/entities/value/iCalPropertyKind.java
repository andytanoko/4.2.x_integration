// %1023788048918:com.gridnode.pdip.base.time.value%
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

public interface iCalPropertyKind
{
  public static final int ICAL_ANY_PROPERTY = -1;
  public static final int ICAL_ACTION_PROPERTY = 0;
  public static final int ICAL_ATTACH_PROPERTY = 1;
  public static final int ICAL_ATTENDEE_PROPERTY = 2;
  public static final int ICAL_CALSCALE_PROPERTY = 3;
  public static final int ICAL_CATEGORIES_PROPERTY = 4;
  public static final int ICAL_CLASS_PROPERTY = 5;
  public static final int ICAL_COMMENT_PROPERTY = 6;
  public static final int ICAL_COMPLETED_PROPERTY = 7;
  public static final int ICAL_CONTACT_PROPERTY = 8;
  public static final int ICAL_CREATED_PROPERTY = 9;
  public static final int ICAL_DESCRIPTION_PROPERTY = 10;
  public static final int ICAL_DTEND_PROPERTY = 11;
  public static final int ICAL_DTSTAMP_PROPERTY = 12;
  public static final int ICAL_DTSTART_PROPERTY = 13;
  public static final int ICAL_DUE_PROPERTY = 14;
  public static final int ICAL_DURATION_PROPERTY = 15;
  public static final int ICAL_EXDATE_PROPERTY = 16;
  public static final int ICAL_EXRULE_PROPERTY = 17;

  public static final int ICAL_GEO_PROPERTY = 18;
  public static final int ICAL_LASTMODIFIED_PROPERTY = 19;
  public static final int ICAL_LOCATION_PROPERTY = 20;
  public static final int ICAL_MAXRESULTS_PROPERTY = 30;
  public static final int ICAL_MAXRESULTSSIZE_PROPERTY = 31;
  public static final int ICAL_METHOD_PROPERTY = 32;
  public static final int ICAL_ORGANIZER_PROPERTY = 33;
  public static final int ICAL_PERCENTCOMPLETE_PROPERTY = 34;
  public static final int ICAL_PRIORITY_PROPERTY = 35;
  public static final int ICAL_PRODID_PROPERTY = 36;
  public static final int ICAL_QUERY_PROPERTY = 37;
  public static final int ICAL_QUERYNAME_PROPERTY = 38;
  public static final int ICAL_RDATE_PROPERTY = 39;
  public static final int ICAL_RECURRENCEID_PROPERTY = 40;
  public static final int ICAL_RELATEDTO_PROPERTY = 50;
  public static final int ICAL_REPEAT_PROPERTY = 51;
  public static final int ICAL_REQUESTSTATUS_PROPERTY = 52;
  public static final int ICAL_RESOURCES_PROPERTY = 53;
  public static final int ICAL_RRULE_PROPERTY = 54;
  public static final int ICAL_SCOPE_PROPERTY = 55;
  public static final int ICAL_SEQUENCE_PROPERTY = 56;
  public static final int ICAL_STATUS_PROPERTY = 57;
  public static final int ICAL_SUMMARY_PROPERTY = 58;
  public static final int ICAL_TARGET_PROPERTY = 59;
  public static final int ICAL_TRANSP_PROPERTY = 60;
  
  public static final int ICAL_TZID_PROPERTY = 61;
  public static final int ICAL_TZNAME_PROPERTY = 62;
  public static final int ICAL_TZOFFSETFROM_PROPERTY = 63;
  public static final int ICAL_TZOFFSETTO_PROPERTY = 64;
  public static final int ICAL_TZURL_PROPERTY = 65;
  public static final int ICAL_UID_PROPERTY = 66;
  public static final int ICAL_URL_PROPERTY = 67;
  public static final int ICAL_VERSION_PROPERTY = 68;
  public static final int ICAL_X_PROPERTY = 69;
  public static final int ICAL_FREEBUSY_PROPERTY = 70;
  
  public static final int  ICAL_TRIGGER_PROPERTY=71;
  public static final int ICAL_NO_PROPERTY = 72;
}