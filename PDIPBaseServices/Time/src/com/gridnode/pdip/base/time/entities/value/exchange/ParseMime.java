package com.gridnode.pdip.base.time.entities.value.exchange;

import com.gridnode.pdip.base.time.entities.model.iCalEntity;
import com.gridnode.pdip.base.time.entities.value.iCalIntV;
import com.gridnode.pdip.base.time.entities.value.iCalParameterKind;
import com.gridnode.pdip.base.time.entities.value.iCalParameterV;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.base.time.entities.value.iCalValueKind;
import com.gridnode.pdip.base.time.entities.value.iCalValueV;
import com.gridnode.pdip.base.time.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.log.Log;

import se.metamatrix.mimedir.ContentHandler;
import se.metamatrix.mimedir.ErrorHandler;
import se.metamatrix.mimedir.Parameter;
import se.metamatrix.mimedir.Parser;

import java.io.FileReader;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ParseMime implements ContentHandler, ErrorHandler
{
  public static String LogCat = "BASE.TIME";

  Parser _parser = new Parser(this, this);

  public List parse(String filename) throws Exception
  {
    return parse(new FileReader(filename));
  }

  public List parse(Reader in) throws Exception
  {
    _parser.parse(in);
    return _compList;
  }


  public void error(int level, int code, String details)
  {
    System.out.println("level: " + level + " detail: " + details);
  }

  public void contentLine(String name, Parameter param[], Object value, String group)
  {
    name = name.toUpperCase();
    System.out.println("name: [" + name + "] value: [" + value + "]");
    for (int i = 0; i < param.length; i++)
    {
      System.out.println(" parameter name: [" + param[i].getName() + "] value: [" + param[i].getValue() + "]");
    }
    if (value == null || value.equals(""))
    {
      return;
    }
    try
    {
      if ((START == _state) && name.equals("BEGIN"))
      {
        if ("VCALENDAR".equalsIgnoreCase((String) value))
          _state = IN_CORE;
        else
        {
          componentStart(value);
        }
      }
      else if ((IN_CORE == _state))
      {
        if (name.equals("BEGIN"))
        {
          componentStart(value);
        }
        else if (name.equals("END"))
        {
          if (!"VCALENDAR".equalsIgnoreCase((String) value))
            throw new ParseException("", 0);
          _state = START;
        }
        else
        {
          //calendar properties
        }
      }
      else if ((IN_COMP == _state))
      {
        parseComponent(name, param, value, group);
      }
      else if ((IN_UNSUPORTED_COMP == _state))
      {
        parseUnsuportedComponent(name, param, value, group);
      }
    }
    catch (ParseException ex)
    {
      Log.error(ILogErrorCodes.TIME_CONTENT_PARSING_ERROR, 
                LogCat, "[ParseMime.contentLine] Parsing error in Mime contentLine",  ex);
    }
  }

  private void componentStart(Object value)
  {
    try
    {
      _curComp = iCalEntityUtil.getComponent((String) value);
      _state = IN_COMP;
    }
    catch (IllegalArgumentException ex)
    {
      Log.log(LogCat, "ignore component " + value);
      _curComponentName = (String) value;
      _state = IN_UNSUPORTED_COMP;
    }
  }

  public void parseComponent(String name, Parameter param[], Object value, String group) throws ParseException
  {
    if (name.equals("END"))
    {
      if (!iCalEntityUtil.getCompiCalName(_curComp).equalsIgnoreCase((String) value))
        throw new IllegalArgumentException("");
      _compList.add(_curComp);
      _state = IN_CORE;
    }
    else
    {

      int valueKind = iCalValueKind.ICAL_ANY_VALUE;
      short propKind;
      try
      {
        propKind = (short) iCalPropertyUtil.getPropertyKind(name);
      }
      catch (IllegalArgumentException ex)
      {
        Log.log(LogCat, "ignore Property " + name);
        return;
      }
      iCalPropertyV prop = new iCalPropertyV(propKind);
      for (int i = 0; i < param.length; i++)
      {
        iCalParameterV icalParam;
        try
        {
          icalParam = iCalParameterUtil.getiCalParam(param[i]);
        }
        catch (IllegalArgumentException ex)
        {
          Log.log(LogCat, "ignore Parameter " + param[i]);
          continue;
        }
        prop.addParam(icalParam);
        if (icalParam.getKind() == iCalParameterKind.ICAL_VALUE_PARAMETER)
          valueKind = iCalParameterUtil.valueTypeToValueKind(((iCalIntV) icalParam.getValue()).getIntValue());
      }
      if (valueKind == iCalValueKind.ICAL_ANY_VALUE)
        valueKind = iCalPropertyUtil.getiCalValueType((int) propKind);
      iCalValueV icalvalue;
      try
      {
        icalvalue = iCalValueV.getValueByKind((short) valueKind);
      } catch (IllegalArgumentException ex)
        {
          Log.log(LogCat, "ignore Property because value type not supported " + valueKind);
          return;
        }
      catch (Exception ex)
      {
        Log.warn(LogCat, ex);
        return;
      }
      icalvalue = icalvalue.parseStr((String) value);
      prop.setValue(icalvalue);
      _curComp.addAnyProperty(prop);
    }
  }
  public void parseUnsuportedComponent(String name, Parameter param[], Object value, String group) throws ParseException
  {
    if (name.equals("END"))
    {
      if (_curComponentName.equalsIgnoreCase((String) value))
        //        throw new IllegalArgumentException(_curComponentName + "expected, but is " + value);
        _state = IN_CORE;
    }
    else
    {
      //just ignore the properties
    }
  }
  public static final int START = 0;
  public static final int IN_CORE = 1;
  public static final int IN_COMP = 2;
  public static final int IN_UNSUPORTED_COMP = 3;

  int _state = START;
  iCalEntity _curComp = null;
  List _compList = new ArrayList();

  String _curComponentName;

}