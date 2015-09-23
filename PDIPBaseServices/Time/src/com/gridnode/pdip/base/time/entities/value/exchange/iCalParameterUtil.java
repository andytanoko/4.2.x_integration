package com.gridnode.pdip.base.time.entities.value.exchange;

import com.gridnode.pdip.base.time.entities.value.*;
import com.gridnode.pdip.framework.log.Log;

import se.metamatrix.mimedir.Parameter;

class iCalParameterUtil implements IValueType, iCalValueKind
{
  static String LogCat = "iCalParameterUtil";
  static class ParamKindNameMap
  {
    int paramKind;
    String paramName;
    ParamKindNameMap(int kind, String name)
    {
      paramKind = kind;
      paramName = name;
    }
  }

  static ParamKindNameMap[] parameterMap =
    new ParamKindNameMap[] {
      new ParamKindNameMap(iCalParameterKind.ICAL_ALTREP_PARAMETER, "ALTREP"),
      new ParamKindNameMap(iCalParameterKind.ICAL_CN_PARAMETER, "CN"),
      new ParamKindNameMap(iCalParameterKind.ICAL_CUTYPE_PARAMETER, "CUTYPE"),
      new ParamKindNameMap(iCalParameterKind.ICAL_DELEGATEDFROM_PARAMETER, "DELEGATED-FROM"),
      new ParamKindNameMap(iCalParameterKind.ICAL_DELEGATEDTO_PARAMETER, "DELEGATED-TO"),
      new ParamKindNameMap(iCalParameterKind.ICAL_DIR_PARAMETER, "DIR"),
      new ParamKindNameMap(iCalParameterKind.ICAL_ENCODING_PARAMETER, "ENCODING"),
      new ParamKindNameMap(iCalParameterKind.ICAL_FBTYPE_PARAMETER, "FBTYPE"),
      new ParamKindNameMap(iCalParameterKind.ICAL_FMTTYPE_PARAMETER, "FMTTYPE"),
      new ParamKindNameMap(iCalParameterKind.ICAL_LANGUAGE_PARAMETER, "LANGUAGE"),
      new ParamKindNameMap(iCalParameterKind.ICAL_MEMBER_PARAMETER, "MEMBER"),
      new ParamKindNameMap(iCalParameterKind.ICAL_PARTSTAT_PARAMETER, "PARTSTAT"),
      new ParamKindNameMap(iCalParameterKind.ICAL_RANGE_PARAMETER, "RANGE"),
      new ParamKindNameMap(iCalParameterKind.ICAL_RELATED_PARAMETER, "RELATED"),
      new ParamKindNameMap(iCalParameterKind.ICAL_RELTYPE_PARAMETER, "RELTYPE"),
      new ParamKindNameMap(iCalParameterKind.ICAL_ROLE_PARAMETER, "ROLE"),
      new ParamKindNameMap(iCalParameterKind.ICAL_RSVP_PARAMETER, "RSVP"),
      new ParamKindNameMap(iCalParameterKind.ICAL_SENTBY_PARAMETER, "SENT-BY"),
      new ParamKindNameMap(iCalParameterKind.ICAL_TZID_PARAMETER, "TZID"),
      new ParamKindNameMap(iCalParameterKind.ICAL_VALUE_PARAMETER, "VALUE"),
      new ParamKindNameMap(iCalParameterKind.ICAL_X_PARAMETER, "X")};

  static int getParamKind(String paramName)
  {
    for (int i = 0; i < parameterMap.length; i++)
    {
      if (parameterMap[i].paramName.equalsIgnoreCase(paramName))
        return parameterMap[i].paramKind;
    }
    throw new IllegalArgumentException("iCalParameterUtil.getParamKind() paramName=" + paramName + "not supported");
  }

  static String getParamName(int paramKind)
  {
    for (int i = 0; i < parameterMap.length; i++)
    {
      if (parameterMap[i].paramKind == paramKind)
        return parameterMap[i].paramName;
    }
    throw new IllegalArgumentException("iCalParameterUtil.getParamName() paramKind=" + paramKind + "not supported");
  }

  static class ParamValueMap
  {
    int kind;
    int value;
    String str;
    ParamValueMap(int paramKind, int paramvalue, String paramStr)
    {
      kind = paramKind;
      value = paramvalue;
      str = paramStr;
    }
  }

  static ParamValueMap[] valueMap = new ParamValueMap[] {
    //   new ParamValueMap(iCalParameterKind.ICAL_CUTYPE_PARAMETER,ICAL_CUTYPE_INDIVIDUAL,"INDIVIDUAL"),
    //    new ParamValueMap(iCalParameterKind.ICAL_CUTYPE_PARAMETER,ICAL_CUTYPE_GROUP,"GROUP"),
    //    new ParamValueMap(iCalParameterKind.ICAL_CUTYPE_PARAMETER,ICAL_CUTYPE_RESOURCE,"RESOURCE"),
    //    new ParamValueMap(iCalParameterKind.ICAL_CUTYPE_PARAMETER,ICAL_CUTYPE_ROOM,"ROOM"),
    //    new ParamValueMap(iCalParameterKind.ICAL_CUTYPE_PARAMETER,ICAL_CUTYPE_UNKNOWN,"UNKNOWN"),
    //    new ParamValueMap(iCalParameterKind.ICAL_ENCODING_PARAMETER,ICAL_ENCODING_8BIT,"8BIT"),
    //    new ParamValueMap(iCalParameterKind.ICAL_ENCODING_PARAMETER,ICAL_ENCODING_BASE64,"BASE64"),
    //    new ParamValueMap(iCalParameterKind.ICAL_FBTYPE_PARAMETER,ICAL_FBTYPE_FREE,"FREE"),
    //    new ParamValueMap(iCalParameterKind.ICAL_FBTYPE_PARAMETER,ICAL_FBTYPE_BUSY,"BUSY"),
    //    new ParamValueMap(iCalParameterKind.ICAL_FBTYPE_PARAMETER,ICAL_FBTYPE_BUSYUNAVAILABLE,"BUSYUNAVAILABLE"),
    //    new ParamValueMap(iCalParameterKind.ICAL_FBTYPE_PARAMETER,ICAL_FBTYPE_BUSYTENTATIVE,"BUSYTENTATIVE"),
    //    new ParamValueMap(iCalParameterKind.ICAL_RANGE_PARAMETER,ICAL_RANGE_THISANDPRIOR,"THISANDPRIOR"),
    //    new ParamValueMap(iCalParameterKind.ICAL_RANGE_PARAMETER,ICAL_RANGE_THISANDFUTURE,"THISANDFUTURE"),
    new ParamValueMap(iCalParameterKind.ICAL_RELATED_PARAMETER, IRelated.START, "START"), new ParamValueMap(iCalParameterKind.ICAL_RELATED_PARAMETER, IRelated.END, "END "),
    //    new ParamValueMap(iCalParameterKind.ICAL_RELTYPE_PARAMETER,ICAL_RELTYPE_PARENT,"PARENT"),
    //    new ParamValueMap(iCalParameterKind.ICAL_RELTYPE_PARAMETER,ICAL_RELTYPE_CHILD,"CHILD"),
    //    new ParamValueMap(iCalParameterKind.ICAL_RELTYPE_PARAMETER,ICAL_RELTYPE_SIBLING,"SIBLING"),
    //    new ParamValueMap(iCalParameterKind.ICAL_ROLE_PARAMETER,ICAL_ROLE_CHAIR,"CHAIR"),
    //    new ParamValueMap(iCalParameterKind.ICAL_ROLE_PARAMETER,ICAL_ROLE_REQPARTICIPANT,"REQ-PARTICIPANT"),
    //    new ParamValueMap(iCalParameterKind.ICAL_ROLE_PARAMETER,ICAL_ROLE_OPTPARTICIPANT,"OPT-PARTICIPANT"),
    //    new ParamValueMap(iCalParameterKind.ICAL_ROLE_PARAMETER,ICAL_ROLE_NONPARTICIPANT,"NON-PARTICIPANT"),
    //    new ParamValueMap(iCalParameterKind.ICAL_RSVP_PARAMETER,ICAL_RSVP_TRUE,"TRUE"),
    //    new ParamValueMap(iCalParameterKind.ICAL_RSVP_PARAMETER,ICAL_RSVP_FALSE,"FALSE"),
    new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_BINARY, "BINARY"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_BOOLEAN, "BOOLEAN"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_DATE, "DATE"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_DURATION, "DURATION"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_FLOAT, "FLOAT"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_INTEGER, "INTEGER"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_PERIOD, "PERIOD"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_RECUR, "RECUR"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_TEXT, "TEXT"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_URI, "URI"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_ERROR, "ERROR"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_DATETIME, "DATE-TIME"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_UTCOFFSET, "UTC-OFFSET"),
      new ParamValueMap(iCalParameterKind.ICAL_VALUE_PARAMETER, ICAL_VALUE_CALADDRESS, "CAL-ADDRESS")};

  static String paramEnum2Str(int paramKind, int enumVal)
  {
    for (int i = 0; i < valueMap.length; i++)
    {
      if ((valueMap[i].kind == paramKind) && (valueMap[i].value == enumVal))
      {
        return valueMap[i].str;
      }
    }
    return null;
  }

  static iCalValueV iCalValueFromStr(int paramkind, String valueStr)
  {

    boolean found_kind = false;

    for (int i = 0; i < valueMap.length; i++)
    {
      if (valueMap[i].kind == paramkind)
      {
        found_kind = true;
        if (valueMap[i].str.equalsIgnoreCase(valueStr))
        {
          iCalValueV icalvalue = new iCalIntV(new Integer(valueMap[i].value));
          return icalvalue;
        }
      }
    }

    if (found_kind)
    {
      Log.warn(LogCat, "param has an XValue");
    }
    iCalValueV icalvalue = new iCalStringV(iCalValueKind.ICAL_STRING_VALUE, valueStr);
    return icalvalue;
  }

  static iCalParameterV getiCalParam(Parameter param)
  {
    int paramKind = getParamKind(param.getName());
    iCalParameterV icalParam = new iCalParameterV((short) paramKind);
    icalParam.setValue(iCalValueFromStr(paramKind, param.getValue()));
    return icalParam;
  }

  static String toString(iCalParameterV param)
  {
    StringBuffer buf = new StringBuffer();
    buf.append(getParamName((short) param.getKind()).toUpperCase());
    buf.append("=");
    String valueStr = null;
    int kind = (int) param.getKind();
    iCalValueV value = param.getValue();
    if (value instanceof iCalIntV)
    {
      valueStr = paramEnum2Str(kind, ((iCalIntV) value).getIntValue());
    }
    else
      valueStr = value.toString();
    buf.append(valueStr);

    return buf.toString();
  }

  static class ValueKindMap
  {
    int valueType;
    int valueKind; //iCal value kind 
    ValueKindMap(int type, int kind)
    {
      valueType = type;
      valueKind = kind;
    }
  }

  static ValueKindMap[] valueKindMap =
    new ValueKindMap[] {
      new ValueKindMap(ICAL_VALUE_BINARY, ICAL_BINARY_VALUE),
      new ValueKindMap(ICAL_VALUE_BOOLEAN, ICAL_BOOLEAN_VALUE),
      new ValueKindMap(ICAL_VALUE_DATE, ICAL_DATE_VALUE),
      new ValueKindMap(ICAL_VALUE_DURATION, ICAL_DURATION_VALUE),
      new ValueKindMap(ICAL_VALUE_FLOAT, ICAL_FLOAT_VALUE),
      new ValueKindMap(ICAL_VALUE_INTEGER, ICAL_INTEGER_VALUE),
      new ValueKindMap(ICAL_VALUE_PERIOD, ICAL_PERIOD_VALUE),
      new ValueKindMap(ICAL_VALUE_RECUR, ICAL_RECUR_VALUE),
      new ValueKindMap(ICAL_VALUE_TEXT, ICAL_TEXT_VALUE),
      new ValueKindMap(ICAL_VALUE_URI, ICAL_URI_VALUE),
      new ValueKindMap(ICAL_VALUE_DATETIME, ICAL_DATETIME_VALUE),
      new ValueKindMap(ICAL_VALUE_UTCOFFSET, ICAL_UTCOFFSET_VALUE),
      new ValueKindMap(ICAL_VALUE_CALADDRESS, ICAL_CALADDRESS_VALUE),
      };

  static int valueTypeToValueKind(int valueType)
  {
    for (int i = 0; i < valueKindMap.length; i++)
    {
      if (valueType == valueKindMap[i].valueType)
        return valueKindMap[i].valueKind;
    }
    throw new IllegalArgumentException("iCalParameterUtil.valueTypeToValueKind() valueType=" + valueType + "not supported");
  }
}