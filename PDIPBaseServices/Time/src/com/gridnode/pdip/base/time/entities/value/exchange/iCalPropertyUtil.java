package com.gridnode.pdip.base.time.entities.value.exchange;

import com.gridnode.pdip.base.time.entities.value.iCalPropertyKind;
import com.gridnode.pdip.base.time.entities.value.iCalValueKind;




class iCalPropertyUtil implements iCalValueKind, iCalPropertyKind
{
 
   static class PropertyKindNameMap
  {
    int PropertyKind;
    String PropertyName;
    int valueKind;
    PropertyKindNameMap(int kind, String name, int avalueKind)
    {
      PropertyKind = kind;
      PropertyName = name;
      valueKind = avalueKind;
    }
  }

  static PropertyKindNameMap[] PropertyeterMap =
    new PropertyKindNameMap[] {
   new PropertyKindNameMap(ICAL_ATTACH_PROPERTY,"ATTACH",ICAL_ATTACH_VALUE),
   new PropertyKindNameMap(ICAL_ATTENDEE_PROPERTY,"ATTENDEE",ICAL_CALADDRESS_VALUE),
   new PropertyKindNameMap(ICAL_CALSCALE_PROPERTY,"CALSCALE",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_CATEGORIES_PROPERTY,"CATEGORIES",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_CLASS_PROPERTY,"CLASS",ICAL_CLASS_VALUE),
   new PropertyKindNameMap(ICAL_COMMENT_PROPERTY,"COMMENT",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_COMPLETED_PROPERTY,"COMPLETED",ICAL_DATETIME_VALUE),
   new PropertyKindNameMap(ICAL_CONTACT_PROPERTY,"CONTACT",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_CREATED_PROPERTY,"CREATED",ICAL_DATETIME_VALUE),
   new PropertyKindNameMap(ICAL_DESCRIPTION_PROPERTY,"DESCRIPTION",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_DTEND_PROPERTY,"DTEND",ICAL_DATETIME_VALUE),
   new PropertyKindNameMap(ICAL_DTSTAMP_PROPERTY,"DTSTAMP",ICAL_DATETIME_VALUE),
   new PropertyKindNameMap(ICAL_DTSTART_PROPERTY,"DTSTART",ICAL_DATETIME_VALUE),
   new PropertyKindNameMap(ICAL_DUE_PROPERTY,"DUE",ICAL_DATETIME_VALUE),
   new PropertyKindNameMap(ICAL_DURATION_PROPERTY,"DURATION",ICAL_DURATION_VALUE),
   new PropertyKindNameMap(ICAL_EXDATE_PROPERTY,"EXDATE",ICAL_DATETIME_VALUE),
   new PropertyKindNameMap(ICAL_EXRULE_PROPERTY,"EXRULE",ICAL_RECUR_VALUE),
   new PropertyKindNameMap(ICAL_FREEBUSY_PROPERTY,"FREEBUSY",ICAL_PERIOD_VALUE),
   new PropertyKindNameMap(ICAL_GEO_PROPERTY,"GEO",ICAL_GEO_VALUE),
   new PropertyKindNameMap(ICAL_LASTMODIFIED_PROPERTY,"LAST-MODIFIED",ICAL_DATETIME_VALUE),
   new PropertyKindNameMap(ICAL_LOCATION_PROPERTY,"LOCATION",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_MAXRESULTS_PROPERTY,"MAXRESULTS",ICAL_INTEGER_VALUE),
   new PropertyKindNameMap(ICAL_MAXRESULTSSIZE_PROPERTY,"MAXRESULTSSIZE",ICAL_INTEGER_VALUE),
   new PropertyKindNameMap(ICAL_ORGANIZER_PROPERTY,"ORGANIZER",ICAL_CALADDRESS_VALUE),
   new PropertyKindNameMap(ICAL_PERCENTCOMPLETE_PROPERTY,"PERCENT-COMPLETE",ICAL_INTEGER_VALUE),
   new PropertyKindNameMap(ICAL_PRIORITY_PROPERTY,"PRIORITY",ICAL_INTEGER_VALUE),
   new PropertyKindNameMap(ICAL_PRODID_PROPERTY,"PRODID",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_QUERY_PROPERTY,"QUERY",ICAL_QUERY_VALUE),
   new PropertyKindNameMap(ICAL_QUERYNAME_PROPERTY,"QUERYNAME",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_RDATE_PROPERTY,"RDATE",ICAL_DATETIMEPERIOD_VALUE),
   new PropertyKindNameMap(ICAL_RECURRENCEID_PROPERTY,"RECURRENCE-ID",ICAL_DATETIME_VALUE),
   new PropertyKindNameMap(ICAL_RELATEDTO_PROPERTY,"RELATED-TO",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_REPEAT_PROPERTY,"REPEAT",ICAL_INTEGER_VALUE),
   new PropertyKindNameMap(ICAL_REQUESTSTATUS_PROPERTY,"REQUEST-STATUS",ICAL_REQUESTSTATUS_VALUE),
   new PropertyKindNameMap(ICAL_RESOURCES_PROPERTY,"RESOURCES",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_RRULE_PROPERTY,"RRULE",ICAL_RECUR_VALUE),
   new PropertyKindNameMap(ICAL_SCOPE_PROPERTY,"SCOPE",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_SEQUENCE_PROPERTY,"SEQUENCE",ICAL_INTEGER_VALUE),
   new PropertyKindNameMap(ICAL_STATUS_PROPERTY,"STATUS",ICAL_STATUS_VALUE),
   new PropertyKindNameMap(ICAL_SUMMARY_PROPERTY,"SUMMARY",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_TARGET_PROPERTY,"TARGET",ICAL_CALADDRESS_VALUE),
   new PropertyKindNameMap(ICAL_TRANSP_PROPERTY,"TRANSP",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_TRIGGER_PROPERTY,"TRIGGER",ICAL_DURATION_VALUE),
   new PropertyKindNameMap(ICAL_TZID_PROPERTY,"TZID",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_TZNAME_PROPERTY,"TZNAME",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_TZOFFSETFROM_PROPERTY,"TZOFFSETFROM",ICAL_UTCOFFSET_VALUE),
   new PropertyKindNameMap(ICAL_TZOFFSETTO_PROPERTY,"TZOFFSETTO",ICAL_UTCOFFSET_VALUE),
   new PropertyKindNameMap(ICAL_TZURL_PROPERTY,"TZURL",ICAL_URI_VALUE),
   new PropertyKindNameMap(ICAL_UID_PROPERTY,"UID",ICAL_TEXT_VALUE),
   new PropertyKindNameMap(ICAL_URL_PROPERTY,"URL",ICAL_URI_VALUE),
   new PropertyKindNameMap(ICAL_VERSION_PROPERTY,"VERSION",ICAL_TEXT_VALUE),
    };

  static int getPropertyKind(String PropertyName)
  {
    for (int i = 0; i < PropertyeterMap.length; i++)
    {
      if (PropertyeterMap[i].PropertyName.equalsIgnoreCase(PropertyName))
        return PropertyeterMap[i].PropertyKind;
    }
    throw new IllegalArgumentException("iCalPropertyUtil.getPropertyKind() PropertyName=" + PropertyName + "not supported");
  }

  static String getPropertyName(int PropertyKind)
  {
    for (int i = 0; i < PropertyeterMap.length; i++)
    {
      if (PropertyeterMap[i].PropertyKind == PropertyKind)
        return PropertyeterMap[i].PropertyName;
    }
    throw new IllegalArgumentException("iCalPropertyUtil.getPropertyName() PropertyKind=" + PropertyKind + "not supported");
  }
 

  static int getiCalValueType(int propKind)
  {
    for (int i = 0; i < PropertyeterMap.length; i++)
    {
      if (PropertyeterMap[i].PropertyKind == propKind)
        return PropertyeterMap[i].valueKind;
    }
//    throw new IllegalArgumentException("iCalPropertyUtil.getPropertyName() propKind=" + propKind + "not supported");
  	return ICAL_STRING_VALUE;
  } 


}
