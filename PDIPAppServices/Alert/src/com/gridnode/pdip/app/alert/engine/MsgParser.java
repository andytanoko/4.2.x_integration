/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MsgParser.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * Nov 30 2002    Srinath             Creation
 * Jun 23 2003    Neo Sok Lay         Moved from helpers package.
 *                                    If value of term is null, allow custom
 *                                    replacement of literals.
 * Feb 28 2006    Neo Sok Lay         Use generics for Vector.                                   
 */

package com.gridnode.pdip.app.alert.engine;

import java.util.*;

/**
 * This class is used for the parsing the message.
 *
 * @author Srinath
 */

public class MsgParser
{
  static final String DEFAULT_NULL_TERM = "(n.a.)";

  static final char START = '<';
  static final char END = '>';
  static final char PER = '%';
  static final char HASH = '#';

  static final int LOOKING_4_HEAD = 0;
  static final int LOOKING_4_TAIL = 1;

  String message;
  Vector<String> fields;
  Vector<Point> fieldPointers;

  /**
   * Create a default class
   * @param message message to parse
   *
   */
  public MsgParser(String message)
  {
    this.message = message;
  }

  /**
   * Start parsing the message
   * Will record down all the fields that appears in the message
   *
   */
  public void parseIt()
  {
    StringBuffer buf = new StringBuffer();
    if(message != null) buf.append(message);

    boolean match = false;
    int mode = LOOKING_4_HEAD;

    Vector<Point> result = new Vector<Point>();
    Point p = null;
    int start = 0, end = 0;

    int count = buf.length();
    char c = 0, tc = 0;

    for (int i = 0; i < count; i++)
    {
      c = buf.charAt(i);
      switch(c)
      {
        case START:
          match = false;
          if((i+1) < count)
          {
            tc = buf.charAt(i+1);
            if(tc == PER)
            {
              match = true;
            }
            else if(tc == HASH)
            {
              match = true;
            }
          }

          if(match)
          {
            if(mode == LOOKING_4_HEAD)
            {
              start = i;
              i++;
              mode = LOOKING_4_TAIL;
            }
          }

          break;

        case END:

          break;

        case PER:
          match = false;
          if((i+1) < count)
          {
            tc = buf.charAt(i+1);
            if(tc == END)
            {
              match = true;
            }
          }

          if(match)
          {
            if(mode == LOOKING_4_TAIL)
            {
              end = i+2;
              i++;
              mode = LOOKING_4_HEAD;
              p = new Point(start, end, buf.substring(start+2, end-2));
              result.addElement(p);
            }
          }
          break;

          case HASH:
          match = false;
          if((i+1) < count)
          {
            tc = buf.charAt(i+1);
            if(tc == END)
            {
              match = true;
            }
          }

          if(match)
          {
            if(mode == LOOKING_4_TAIL)
            {
              end = i+2;
              i++;
              mode = LOOKING_4_HEAD;
              p = new Point(start, end, buf.substring(start+2, end-2));
              result.addElement(p);
            }
          }
          break;
      }
    }
    fieldPointers = result;

    if(!result.isEmpty())
    {
      int count2 = result.size();
      fields = new Vector<String>(count2);
      String field = null;
      for (int i = 0; i < count2; i++)
      {
        field = result.elementAt(i).getTerm();
        if(!fields.contains(field))
        {
          fields.addElement(field);
        }
      }

    }
    else
    {
      fields = new Vector<String>(0);
    }

  }

  /**
   * Return all the <code>Fields</code> that appears in the message
   *
   */
  public Vector<String> getFields()
  {
    return new Vector<String>(fields);
  }

  /**
   * Replace all the <code>Fields</code> with the term found in the
   * <code>definition</code>
   *
   * @param definition term to replace with
   *
   * @return the formatted message
   *
   */
  public String format(HashMap<String,String> definition)
  {
    StringBuffer buf = new StringBuffer();
    if(message != null)
      buf.append(message);

    String term = null;
    Point p = null;
    int count = fieldPointers.size() - 1;
    for (int i = count; i >= 0 ; i--)
    {
      p = fieldPointers.elementAt(i);
      term = definition.get(p.getTerm());
      if(term != null)
      {
        buf.replace(p.getStart(), p.getEnd(), term);
      }
      else
      {
        buf.replace(p.getStart(), p.getEnd(), p.getNullTerm());
      }
    }
    System.out.println("buf.toString()>>" + buf.toString());
    return buf.toString();
  }

  // Inner class *************************************************************
  /*
   */
  class Point
  {
    int x,y;
    String str;
    String nullTerm = DEFAULT_NULL_TERM;
    public Point(int start, int end, String term)
    {
      x = start;
      y = end;
      str = term;
      setNullTerm(term);
    }

    int getStart()
    {
      return x;
    }

    int getEnd()
    {
      return y;
    }

    String getTerm()
    {
      return str;
    }
    
    private void setNullTerm(String term)
    {
      if (term.length() > 4)
      {
        if (term.startsWith("?N"))
        {
          int end = term.indexOf("N?", 2);
          if (end > -1)
          {
            nullTerm = term.substring(2, end);       
            str = term.substring(end+2);
          }
        } 
      }
    }
    
    String getNullTerm()
    {
      return nullTerm;
    }
  }

}