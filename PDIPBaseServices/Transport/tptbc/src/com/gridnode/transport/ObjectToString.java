package com.gridnode.transport;

/**
 * Title:        GridMaster
 * Description:
 * Copyright:    Copyright (c) 2000
 * Company:      GridNode
 * @author Goh Kan Mun
 * @version 1.0
 */

import java.io.UnsupportedEncodingException;
//import com.gridnode.transport.tplog.TpLog;

abstract public class ObjectToString
{

  static protected String getNextItem(String event, int[] startPos, String separator)
  {
    int position = event.indexOf(separator, startPos[0]);
    if (position >= 0)
    {
      String item = event.substring(startPos[0], position);
      startPos[0] = position + separator.length();
      if (item.equalsIgnoreCase("null"))
      {
        return null;
      }
      return item;
    }
    return null;
  }

  static protected byte[] getNextItemByte(String event, int[] startPos, String separator)
  {
    int position = event.indexOf(separator, startPos[0]);
    if (position >= 0)
    {
      String item = event.substring(startPos[0], position);
      startPos[0] = position + separator.length();
      if (!item.equalsIgnoreCase("null"))
      {
        return (item.getBytes());
      }
    }
    return null;
  }

  static protected String[] getNextItem(String event, int[] startPos, int noOfItem,
          String separator)
  {
    if (noOfItem <= 0)
      return null;
    String[] eventData = new String[noOfItem];
    for (int i = 0; i < noOfItem; i++)
      eventData[i] = getNextItem(event, startPos, separator);
    return eventData;
  }

  static protected int getNextItemInt(String event, int[] startPos, String separator)
  {
    return (Integer.parseInt(getNextItem(event, startPos, separator)));
  }

  static protected boolean getNextItemBoolean(String event, int[] startPos, String separator)
  {
    return (Boolean.valueOf(getNextItem(event, startPos, separator)).booleanValue());
  }

  static protected void setNextItem(StringBuffer event, String data, String separator)
  {
    event.append(data);
    event.append(separator);
  }

  static protected void setNextItem(StringBuffer event, int data, String separator)
  {
    setNextItem(event, "" + data, separator);
  }

  static protected void setNextItem(StringBuffer event, String[] data, int count, String separator)
  {
    for (int i = 0; i < count; i++)
      setNextItem(event, data[i], separator);
  }

  static protected void setNextItem(StringBuffer event, byte[] data, String separator)
  {
    if (data != null)
      setNextItem(event, new String(data), separator);
    else
      setNextItem(event, (String) null, separator);
  }

  static protected void setNextItem(StringBuffer event, boolean data, String separator)
  {
    if (data)
      setNextItem(event, Boolean.TRUE.toString(), separator);
    else
      setNextItem(event, Boolean.FALSE.toString(), separator);
  }

  /**
   * Returns the string passed in padded with the character 'pad',
   * to make it up to 'len' characters long.
   * If 'padLeft' is true, then the padding characters are added to the left of
   * the string, otherwise they are added to the right.
   */
  static public String padString(String s, int len, char pad, boolean padLeft)
  {
    Character c = new Character(pad);
    String ret = "";
    if (padLeft==false) {
      ret = ret.concat(s);
    }
    for(int n=s.length(); n<len; n++) {
      ret = ret.concat( c.toString() );
    }
    if (padLeft==true) {
      ret = ret.concat(s);
    }
    return( ret );
  }

/*  abstract private void setSeparater(String newSep);
  {
    sep = newSep;
  }*/

/*  abstract private String getSeparater()
  {
    return sep;
  }*/

  static public byte[] stringToByteArray(String s)
  {
    try {
      if (s==null)
        s = "_NULL_-_!";

        /*
      byte b[] = new byte[s.length()];
      for(int i=0; i<s.length(); i++)
      {
         b[i] = (byte)s.charAt(i);
      }
      return( b );
        */
      return( s.getBytes("UTF-8") );

    }
    catch(UnsupportedEncodingException uee)
    {
//      TpLog.err( "ObjectToString.stringToByteArray() exception : ", uee);
      throw new IllegalArgumentException( uee.getMessage() );
    }
  }

  static public String byteArrayToStr(byte b[])
  {
    /*
    char c[] = new char[b.length];
    for(int i=0; i<b.length; i++)
    {
       c[i] = (char)b[i];
    }
    String s = new String(c);
    */

    try
    {
      String s = new String(b, "UTF-8" );

      if (s.compareTo("_NULL_-_!")==0)
        s = null;

      return(s);
    }
    catch(UnsupportedEncodingException uee)
    {
//      TpLog.err( "ObjectToString.byteArrayToStr() exception : ", uee);
      throw new IllegalArgumentException( uee.getMessage() );
    }
  }

}