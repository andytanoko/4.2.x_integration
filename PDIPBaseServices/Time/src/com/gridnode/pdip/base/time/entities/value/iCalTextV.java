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

package com.gridnode.pdip.base.time.entities.value;

import java.text.ParseException;

public class iCalTextV extends iCalValueV
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 247350502583874095L;
	protected String _text;
  
  protected iCalTextV()
  {
  }


  public iCalTextV(int kind)
  {
    if (kind != iCalValueKind.ICAL_TEXT_VALUE)
      throw new IllegalArgumentException("Wrong Kind=" + kind);
    _kind = (short) kind;
  }

  public iCalTextV(String value)
  {
    _kind = (short) iCalValueKind.ICAL_TEXT_VALUE;
    _text = value;
  }

  public String getTextValue()
  {
    return _text;
  }
  static final int ICAL_TEXT_VALUE = 1;
  static final ValueFieldInfo[] FieldInfo = new ValueFieldInfo[] { new ValueFieldInfo("_text", "Text", ICAL_TEXT_VALUE)};

  public ValueFieldInfo[] getFieldInfos()
  {
    return FieldInfo;
  }

  public iCalValueV parseStr(String in) throws ParseException
  {
    char source[] = new char[in.length()];
    StringBuffer target = new StringBuffer(in.length());
    source = in.toCharArray();
    boolean nextEscaped = false;
    for (int i = 0; i < source.length; i++)
    {
      if (nextEscaped)
      {
        nextEscaped = false;
        if (source[i] == 'n' || source[i] == 'N')
        {
          target.append('\n');
        }
        else if (source[i] == '\\')
        {
          target.append('\\');
        }
        else if (source[i] == ',')
        {
          target.append(',');
        }
        else
        {
          throw new ParseException("Unknown escape code", i);
        }
      }
      else
      {
        if (source[i] == '\\')
        {
          nextEscaped = true;
        }
        else if (source[i] == ',')
        {
          throw new ParseException("Unescaped comma is invalid", i);
        }
        else
        {
          target.append(source[i]);
        }
      }
    }
    return new iCalTextV(target.toString());
  }

  public String toString()
  {
    char source[] = new char[_text.length()];
    StringBuffer target = new StringBuffer(_text.length());
    source = _text.toCharArray();
    for (int i = 0; i < source.length; i++)
    {
      if (source[i] == '\n')
      {
        target.append("\\n");
      }
      else if (source[i] == '\\')
      {
        target.append("\\\\");
      }
      else if (source[i] == ',')
      {
        target.append("\\,");
      }
      else if (source[i] == ';')
      {
        target.append("\\;");
      }
      else
      {
        target.append(source[i]);
      }
    }
    return target.toString();
  }

}
