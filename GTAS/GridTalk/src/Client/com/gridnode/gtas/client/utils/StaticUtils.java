/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: StaticUtils.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-29     Andrew Hill         Created
 * 2002-07-17     Andrew Hill         simple converters
 * 2002-08-01     Andrew Hill         collection filterer
 * 2002-09-24     Andrew Hill         explode(), implode()
 * 2002-10-24     Andrew Hill         Use cloneNode() for documents (with fallback ability)
 * 2002-10-30     Andrew Hill         Log bad clone attempts
 * 2002-11-01     Andrew Hill         boolValue() now supports "maybe" for when you cant decide...
 * 2002-11-08     Andrew Hill         Move list ordering utils here. Refactor to be more generic
 * 2002-11-28     Andrew Hill         stringNotEmpty()
 * 2002-12-05     Andrew Hill         arrayListValue(Collection)
 * 2002-12-27     Andrew Hill         EMPTY_STRING_ARRAY constant
 * 2003-01-10     Andrew Hill         Modify stringValue to return dates as milliseconds string!
 * 2003-01-13     Andrew Hill         collectionContains()
 * 2003-01-21     Andrew Hill         objectsEqual()
 * 2003-01-22     Andrew Hill         dumpMap()
 * 2003-01-23     Andrew Hill         dumpFormFileElement()
 * 2003-01-27     Andrew Hill         extractFileName(), extractPath()
 * 2003-03-21     Andrew Hill         getFilteredCollection returns List
 * 2003-03-25     Andrew Hill         capitalise()
 * 2003-04-10     Andrew Hill         getLongArray()
 * 2003-04-10     Andrew Hill         findValueInArray(), getFileExtension()
 * 2003-04-14     Andrew Hill         deprecate assertNotNull()
 * 2003-04-24     Andrew Hill         Removed several deprecated methods including 'assertNotNull'
 * 2003-05-13     Andrew Hill         concatArrays()
 * 2003-05-14     Andrew Hill         clean()
 * 2003-05-15     Andrew Hill         Modified behaviour of getLongCollection() to not include nulls
 * 2003-05-19     Andrew Hill         convert()
 * 2003-05-21     Andrew Hill         reDim() (String version only so far)
 * 2003-05-22     Andrew Hill         getArrayLength()
 * 2003-06-02     Andrew Hill         TRUE_STRING, FALSE_STRING constants
 */
package com.gridnode.gtas.client.utils;

import java.io.*;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.strutsbase.FormFileElement;


public class StaticUtils
{
  private static final Log _log = LogFactory.getLog(StaticUtils.class); // 20031209 DDJ

  /*
   * The string constant "false". You can refer to this when resetting checkbox fields
   * in an actionForm etc.... 
   */
  public static final String FALSE_STRING = "false"; //20030602AH
  
  /*
   * The string constant "true". You can refer to this when resetting checkbox fields
   * in an actionForm etc.... 
   */
  public static final String TRUE_STRING = "true"; //20030602AH
  
  public static final String[] EMPTY_STRING_ARRAY = new String[0]; //20021227AH
  public static final long[] EMPTY_PRIMITIVE_LONG_ARRAY = new long[0]; //20030513AH
  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0]; //20030513AH
  public static final Random rnd = new java.util.Random();

  /**
   * Uses serialization and byte array streams to deep clone an object.
   * The object must implement Serializable.
   * nb: now uses Document.cloneNode() to clone if object instanceof org.w3c.Document
   * but will fallback to serialization if that fails (which it will in certain xerces versions)
   * @param object
   * @return clonedObject
   */
  public static Object cloneObject(Object src) throws GTClientException
  {
    ByteArrayOutputStream baos = null;
    ObjectOutputStream objOS = null;
    ByteArrayInputStream bais = null;
    ObjectInputStream objIS = null;
    try
    {
      Object ret = null;
      if(src instanceof Document)
      { // Its a lot quicker to use cloneNode to clone documents BUT certain versions of xerces
        // have a nasty bug which causes this to fail so we have to be ready to catch that! :-(
        try
        {
          ret = ((Document)src).cloneNode(true);
          return ret;
        }
        catch(Throwable t)
        {
          if(_log.isErrorEnabled())
          {
            _log.error("Document.cloneNode() failed (" + t.getMessage() + ")! - falling back to cloning using streams");
          }
        }
      }
      //write
      baos = new ByteArrayOutputStream();
      objOS = new ObjectOutputStream(new BufferedOutputStream(baos));
      objOS.writeObject(src);
      objOS.close();

      //read
      bais = new ByteArrayInputStream(baos.toByteArray());
      objIS = new ObjectInputStream(new BufferedInputStream(bais));
      ret = objIS.readObject();
      objIS.close();
      return ret;
    }
    catch (Throwable t)
    {
      throw new GTClientException("Error cloning object of class " + getObjectClassName(src),t);
    }
    finally
    {
      try
      {
        if(objOS != null) { objOS.close(); baos.close(); }
        if(objIS != null) { objIS.close(); bais.close(); }
      }
      catch (Throwable ex)
      {
        throw new GTClientException("Error cleaning up streams after object cloning",ex);
      }
    }
  }

  /**
   * Search the specified string array to see if it contains the string value passed.
   * Nb:it is able to handle tests to see if any array elements array null
   * @param array
   * @param value
   * @returns true if found, flase if not found or array is null
   */
  public static boolean arrayContains(String[] array, String value)
  {
    if(array != null)
    {
      for(int i=0; i < array.length; i++)
      {
        if(array[i] != null)
        {
          if(array[i].equals(value))
            return true;
        }
        else
        {
          if(value == null)
            return true;
        }
      }
    }
    return false;
  }
  
  public static boolean arrayContains(Object[] array, Object value)
  { //20030519AH
    if(array != null)
    {
      for(int i=0; i < array.length; i++)
      {
        if(array[i] != null)
        {
          if(array[i].equals(value))
            return true;
        }
        else
        {
          if(value == null)
            return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns the first object in a collection or null if the collection is null or empty.
   * @param Collection
   * @returns first object in collection
   */
  public static Object getFirst(Collection collection)
  {
    if(collection == null) return null;
    if(collection.isEmpty()) return null;
    Iterator i = collection.iterator();
    return i.next();
  }

  /**
   * Returns the first object in an array or null if the array is null or empty.
   * @param Collection
   * @returns first object in collection
   */
  public static Object getFirst(Object[] array)
  {
    if(array == null) return null;
    if(array.length == 0) return null;
    return array[0];
  }

  /**
   * Convert a string to a boolean based on a hardcoded set of false values.
   * Case insensitive.
   * The following values are considered false. All others are considered true.
   * null
   * ""
   * "false"
   * "off"
   * "no"
   * "0"
   * "tails"
   * "never"
   * "unchecked"
   * "negative"
   * "disabled"
   * For the value "maybe" it returns a random true or false value. :-)
   * Implementation note: Of the infinite set of true values, using "true" is the most
   * efficient as there is an explicit test for this at the start of the method and so
   * all the false checks are avoided if value is "true".
   * For other true values all the hardcoded false values must first be eliminated.
   * @param value
   * @return booleanValue
   */
  public static boolean primitiveBooleanValue(String value)
  {
    if(value==null) return false;
    
    //20030522AH - Explicitly check for "true" first since its very common and we want to avoid
    //executing lots of unnecessary comparisons below.
    if(TRUE_STRING.equalsIgnoreCase(value)) return true; //20030602AH - Refer to the constant
    
    if("".equals(value)) return false;
    if(FALSE_STRING.equalsIgnoreCase(value)) return false; //20030602AH - Refer to the constant
    if("off".equalsIgnoreCase(value)) return false;
    if("no".equalsIgnoreCase(value)) return false;
    if("0".equals(value)) return false;
    if("tails".equalsIgnoreCase(value)) return false;
    if("never".equalsIgnoreCase(value)) return false;
    if("unchecked".equalsIgnoreCase(value)) return false;
    if("negative".equalsIgnoreCase(value)) return false;
    if("disabled".equalsIgnoreCase(value)) return false; //20030522AH

    // Just for laughs...
    if("maybe".equalsIgnoreCase(value)) return rnd.nextBoolean();

    // All other non-empty strings considered as true
    return true;
  }

  /**
   * Convert a string into an appropriate Boolean object.
   * If passed the string "null" returns null, if passed null returns false.
   * (Theres a reason for that, but I cant remember what it is now)
   * See primitiveBooleanValue() for details on returned value for other parameters
   * @param valueString
   * @return Boolean value or null
   */
  public static Boolean booleanValue(String value)
  {
    if("null".equals(value)) return null; // hehe. Yes that IS the string "null". :-)
    return new Boolean(primitiveBooleanValue(value));
  }

  /**
   * If value can be converted to Long returns that else returns null
   * @param stringValue
   * @return LongValue
   */
  public static Long longValue(String value)
  {
    try
    {
      return new Long(value);
    }
    catch(Exception e)
    {
      return null;
    }
  }

  /**
   * If value can be converted to Short returns that else returns null
   * @param stringValue
   * @return ShortValue
   */
  public static Short shortValue(String value)
  {
    try
    {
      return new Short(value);
    }
    catch(Exception e)
    {
      return null;
    }
  }

  /*
   * If valu can be converted to Integer returns that else returns null
   * @param stringValue
   * @return IntegerValue
   */
  public static Integer integerValue(String value)
  {
    try
    {
      return new Integer(value);
    }
    catch(Exception e)
    {
      return null;
    }
  }
  
  /*
   * If value can be converted to Byte returns that else returns null
   * @param stringValue
   * @return ByteValue
   */
  public static Byte byteValue(String value)
  { //20030519AH
    try
    {
      return new Byte(value);
    }
    catch(Exception e)
    {
      return null;
    }
  }
  
  /*
   * If value can be converted to Double returns that else returns null
   * @param stringValue
   * @return double
   */
  public static Double doubleValue(String value)
  { //20030519AH
    try
    {
      return new Double(value);
    }
    catch(Exception e)
    {
      return null;
    }
  }
  
  /*
   * If value can be converted to Float returns that else returns null
   * @param stringValue
   * @return float
   */
  public static Float floatValue(String value)
  { //20030519AH
    try
    {
      return new Float(value);
    }
    catch(Exception e)
    {
      return null;
    }
  }


  /*
   * Converts the passed string to a primitive int value.
   * If the string is null or cannot be converted will return 0. If this is a problem it is
   * suggested you use the Object conversion methods and check for null, or convert yourself
   * and catch exceptions as appropriate.
   * @param value a string containing representation of value to convert
   * @return intValue
   */
  public static int primitiveIntValue(String value)
  {
    try
    {
      return Integer.parseInt(value);
    }
    catch(Exception e)
    {
      return 0;
    }
  }

  /*
   * Converts the passed string to a primitive long value.
   * If the string is null or cannot be converted will return 0. If this is a problem it is
   * suggested you use the Object conversion methods and check for null, or convert yourself
   * and catch exceptions as appropriate.
   * @param value a string containing representation of value to convert
   * @return longValue
   */
  public static long primitiveLongValue(String value)
  {
    try
    {
      return Long.parseLong(value);
    }
    catch(Exception e)
    {
      return 0;
    }
  }

  /**
   * Will convert the string of millisecond digits to a Date.
   * If the string is empty, null, or invalid (it doesnt throw exception!), will return null
   * @param msString - a string of digits representing milliseconds past epoch in utc
   * @return date - a java.util.Date
   */
  public static java.util.Date dateValue(String msString)
  { //20030110AH
    return dateValue(msString,null);
  }

  /**
   * Same as dateValue(String) but takes the already instantiated date object that will have its
   * value set based on the conversion. This date object is what will be returned unless it is
   * necessary to return a null.
   * If you pass in null for the date a new Date object is instantiated for you.
   * @param msString - a string of digits representing milliseconds past epoch in utc
   * @param date - an instantiated date object whose value will be modified
   * @return date - a java.util.Date
   */
  public static java.util.Date dateValue(String msString, Date date)
  { //20030110AH
    if(msString == null || "".equals(msString))
    {
      return null;
    }
    try
    {
      long ms = Long.parseLong(msString);
      if(date == null)
      {
        return new java.util.Date(ms);
      }
      else
      {
        date.setTime(ms);
        return date;
      }
    }
    catch(Throwable t)
    {
      return null;
    }
  }

  public static java.sql.Date sqlDateValue(String msString)
  { //20030520AH
    return sqlDateValue(msString,null);
  }


  public static java.sql.Date sqlDateValue(String msString, java.sql.Date date)
  { //20030520AH
    if(msString == null || "".equals(msString))
    {
      return null;
    }
    try
    {
      long ms = Long.parseLong(msString);
      if(date == null)
      {
        return new java.sql.Date(ms);
      }
      else
      {
        date.setTime(ms);
        return date;
      }
    }
    catch(Throwable t)
    {
      return null;
    }
  }

  /**
   * Will convert the string of millisecond digits to a Timestamp. Note that as the value
   * is specified in integral milliseconds it is not possible to set the nanos beyond milli
   * accuracy.
   * @param msString - a string of digits representing milliseconds past epoch in utc
   * @return timestamp - a java.sql.Timestamp
   */
  public static java.sql.Timestamp timestampValue(String msString)
  { //20030110AH
    if(msString == null || "".equals(msString))
    {
      return null;
    }
    try
    {
      long ms = Long.parseLong(msString);
      return DateUtils.getSqlTimestamp(ms * 1000000);
    }
    catch(Throwable t)
    {
      return null;
    }
  }

  /**
   * Creates a new collection containing the results of filtering each item of the passed
   * collection using the specified filter and context.
   * The original type of collection is not preserved, rather an ArrayList is used for the
   * returned collection. If synced is true, this will however wrap this ArrayList with
   * a synchronized collection thinghy.
   * If null is passed for the collection, null will be returned. If the filter is null, an
   * empty collection will be returned.
   * Nb that the underlying arrayList is created with room enough to hold the entire contents of
   * the original collection.
   * @param collection
   * @param filter
   * @param context
   * @param synced
   * @return Collection
   * @throws GTClientException
   */
  public static List getFilteredCollection(Collection collection,
                                          IFilter filter,
                                          Object context,
                                          boolean synced)
    throws GTClientException
  { //20030321AH - Now returns a List instead of generic Collection
    //This can still be treated like any Collection, but also makes it safe to assume it
    //supports list concepts. The input collection of course may be anything still.
    try
    {
      if(collection == null) return null;
      ArrayList results = new ArrayList(collection.size());
      if(filter != null)
      {
        Iterator i = collection.iterator();
        while(i.hasNext())
        {
          Object object = i.next();
          if(filter.allows(object,context))
          {
            results.add(object);
          }
        }
      }
      if(synced)
      {
        return Collections.synchronizedList(results); //20030321AH
      }
      else
      {
        return results;
      }
    }
    catch(Throwable t)
    { //20030114AH - Added try/catch
      throw new GTClientException("Error filtering collection",t);
    }
  }

  /*
   * Convert a collection of objects to an array of String.
   * The stringValue() method is used to obtain the string representation of the object.
   * @param collection
   * @return stringArray
   */
  public static String[] getStringArray(Collection collection)
  {
    if(collection==null) return null;
    String[] array = new String[collection.size()];
    Iterator iterator = collection.iterator();
    for(int i=0; i < array.length; i++)
    {
      Object o = iterator.next();
      array[i] = stringValue(o);
    }
    return array;
  }

  /*
   * Convert an array of String to a collection of Long objects
   * The longValue() method is used to obtain the Long objects. The returned collection
   * is guaranteed to retain the order of the input array and you may cast it to a List.
   * Currently its returning an ArrayList but this behaviour may change in future versions
   * so dont rely on it.
   * 20030515AH - Changed the behaviour slightly - strings that dont convert to a long value
   * (ie that longValue() will return as null) are no longer included in the result array.
   * @param stringArray
   * @return collection (ArrayList) of Long
   */
  public static Collection getLongCollection(String[] array)
  {
    if(array == null) return null;
    ArrayList collection = new ArrayList(array.length); //20030424AH - Use ArrayList instead of Vector
    for(int i=0; i < array.length; i++)
    {
      Long value = longValue(array[i]);
      if(value != null)
      { //20030515AH - Dont include nulls!
        collection.add( value );
      }
    }
    return collection;
  }

  /*
   * Convert an array of primitive long into a collection of Long objects.
   * The returned collection
   * is guaranteed to retain the order of the input array and you may cast it to a List.
   * Currently its returning an ArrayList but this behaviour may change in future versions
   * so dont rely on it.
   * @param longArray
   * @return longCollection
   */
  public static Collection getLongCollection(long[] array)
  {
    if(array == null) return null;
    ArrayList collection = new ArrayList(array.length); //20030424AH - Use ArrayList instead of Vector
    for(int i=0; i < array.length; i++)
    {
      collection.add( new Long(array[i]) );
    }
    return collection;
  }

  /**
   * Returns a string representation of the object.
   * For collections or arrays returns a string representation of element zero.
   * (nb: Recursive collection reference detection is very primitive. Its really up to you not to
   * pass recursively referencing collections as recursion is used to output contents of
   * collections within collections and infinite looping is a possibility!)
   * For null, or empty collection or array returns ""
   * (Never returns null)
   * 20030110AH - For java.util.Date subclasses (including Timestamp) returns a string with
   * the milliseconds past epoch.
   * @param object
   * @return string
   */
  public static String stringValue(Object o)
  {
    if(o == null)
    {
      return "";
    }
    else
    {
      if(o instanceof String)
      {
        return (String)o;
      }
      else if(o instanceof Object[])
      {
        Object[] oArray = (Object[])o;
        if(oArray.length == 0)
        {
          return "";
        }
        else
        {
          return stringValue(oArray[0]); //hmmm
        }
      }
      else if(o instanceof Collection)
      {
        Collection oCollection = (Collection)o;
        if(oCollection.isEmpty())
        {
          return "";
        }
        else
        {
          StringBuffer buffer = new StringBuffer();
          int size = oCollection.size();
          Iterator iterator = oCollection.iterator();
          for(int i=0; i < size; i++)
          {
            Object next = iterator.next();
            if(next == oCollection)
            {
              next = "<rcr>"; //recursive collection reference!
              //(if we recurse into this we will be in an infinite loop. Nb: our rcr
              //detection is pretty simple and only detects a fraction of the possible
              //recursive references!)
            }
            buffer.append(stringValue(next));
            if(i < (size-1) )
            {
              buffer.append(",");
            }
          }
          return buffer.toString();
        }
      }
      else if(o instanceof java.util.Date)
      { //20030109AH - Return milliseconds past epoch GMT as a string
        //Im thinking maybe I should return it as nanoseconds to account for the accuracy
        //allowed by java.sql.Timestamp???? If it proves necessary to allow for this then
        //may have to refactor later!
        return "" + DateUtils.getMilliseconds((java.util.Date)o);
      }
      else
      {
        return o.toString();
      }
    }
  }

  /**
   * Returns an array containing stringValue representations of object.
   * The resulting array will not be null, but may be of zero length.
   * @param o an object to convert to a String arry
   * @return stringArray of 0 or greater length
   */
  public static String[] stringArrayValue(Object o)
  {
    if(o == null)
    {
      return new String[]{};
    }
    else if(o instanceof Object[])
    {
      // Nb: We even do this long-winded copy for String[] to ensure that the output array
      // never contains a null.
      Object[] oArray = (Object[])o;
      String[] stringArray = new String[oArray.length];
      for(int i=0; i < oArray.length; i++)
      {
        stringArray[i] = stringValue(oArray[i]);
      }
    }
    else if(o instanceof Collection)
    {
      Collection oCollection = (Collection)o;
      if(oCollection.isEmpty())
      {
        return new String[]{};
      }
      else
      {
        return getStringArray(oCollection);
      }
    }
    String[] array = new String[1];
    array[0] = stringValue(o);
    return array;
  }

  /*
   * Create a collection containing all objects in sepcified array.
   * Currently uses Vector for the returned collection.
   * @param array
   * @return collection
   */
  public static Collection collectionValue(Object[] array)
  {
    int length = array==null?0:array.length;
    Vector v = new Vector(length);
    for(int i=0; i < length; i++)
    {
      v.add( array[i] );
    }
    return v;
  }

  /* Create an ArrayList containing all objects in sepcified collection.
   * @param collection
   * @return arrayList
   */
  public static ArrayList arrayListValue(Collection collection) // 20021205AH
  {
    if(collection instanceof ArrayList)
    {
      return (ArrayList)collection;
    }
    else
    {
      if(collection == null)
      {
        return new ArrayList(0);
      }
      else
      {
        ArrayList aList = new ArrayList(collection);
        return aList;
      }
    }
  }

  /*
   * Create an ArrayList containing objects in sepcified array.
   * @param array
   * @return arrayList
   */
  public static ArrayList arrayListValue(Object[] array)  // 08102002 DDJ
  {
    int length = array==null?0:array.length;
    ArrayList a = new ArrayList(length);
    for(int i=0; i < length; i++)
    {
      a.add( array[i] );
    }
    return a;
  }

  /*
   * Explode a single String into an array of Strings based on a specified delimiter.
   * StringTokenizer is used to do the parsing.
   * @param string
   * @param delimeter
   * @return stringArray
   */
  public static String[] explode(String str, String delim)
  {
    if(str == null) return null;
    if(delim == null) throw new java.lang.NullPointerException("null delimiter");
    StringTokenizer tokenizer = new StringTokenizer(str,delim);
    String[] results = new String[tokenizer.countTokens()];
    int index = 0;
    while(tokenizer.hasMoreTokens())
    {
      results[index] = tokenizer.nextToken();
      index++;
    }
    return results;
  }

  /*
   * Explode a String into an array of primitive int based on a delimeter.
   * StrinkTokenizer is used to extract the elements, and primitiveIntValue() to convert
   * to int.
   * @param string
   * @param delimeter
   * @return intArray
   */
  public static int[] explodeInt(String str, String delim)
  {
    if(str == null) return null;
    if(delim == null) throw new java.lang.NullPointerException("null delimiter");
    StringTokenizer tokenizer = new StringTokenizer(str,delim);
    int[] results = new int[tokenizer.countTokens()];
    int index = 0;
    while(tokenizer.hasMoreTokens())
    {
      results[index] = primitiveIntValue(tokenizer.nextToken());
      index++;
    }
    return results;
  }

  /*
   * Explode a single String into a collection of Strings based on a specified delimiter.
   * StringTokenizer is used to do the parsing. Currently returns an object of type
   * Vector though this may be changed in the future so dont count on it. It is guaranteed
   * however that the returned object can be cast to List.
   * @param string
   * @param delimeter
   * @return stringArray
   */
  public static Collection explodeCollection(String str, String delim)
  {
    if(str == null) return null;
    if(delim == null) throw new java.lang.NullPointerException("null delimiter");
    StringTokenizer tokenizer = new StringTokenizer(str,delim);
    Vector results = new Vector(tokenizer.countTokens());
    while(tokenizer.hasMoreTokens())
    {
      results.add(tokenizer.nextToken());
    }
    return results;
  }

  /*
   * Create a single String containing string representations of the object passed.
   * The stringArrayValue() method is used to obtain a String array representation of the object
   * which is then concatenated into a single string using implode(String[],String) using the
   * specified delimiter.
   * @param object
   * @param delimiter
   * @return String
   */
  public static String implode(Object values, String delim)
  {
    String[] array = stringArrayValue(values);
    return implode(array,delim);
  }

  /*
   * Concatenate an array of String into a single String with the individual elements seperated by
   * the specified delimeter. The delimeter may also be null, in which case there is no seperation
   * between the elements in the string.
   * @param array
   * @param delimiter
   * @return implodedString
   */
  public static String implode(String[] array, String delim)
  {
    if(array == null) return null;
    if(array.length == 0) return "";
    StringBuffer buffer = new StringBuffer();
    for(int i = 0; i < array.length; i++)
    {
      buffer.append(array[i]);
      if( (delim != null) && (i < (array.length-1)) ) //20030424AH - Allow null delimiter
      {
        buffer.append(delim);
      }
    }
    return buffer.toString();
  }

  /*
   * Concatenate a collection of objects  into a single String with the individual 
   * String representations of the elements seperated by
   * the specified delimeter. The delimeter may also be null, in which case there is no seperation
   * between the elements in the string.
   * @param array
   * @param delimiter
   * @return implodedString
   */
  public static String implode(Collection collection, String delim)
  {
    if(collection == null) return null;
    int size = collection.size();
    if(size == 0) return "";
    StringBuffer buffer = new StringBuffer();
    Iterator i = collection.iterator();
    int index = 0;
    while(i.hasNext())
    {
      buffer.append( stringValue(i.next()) ); //20030424AH - Use stringValue() to get String
      if( (delim != null) && (index < (size-1)) ) //20030424AH - Allow null delimiter
      {
        buffer.append(delim);
      }
      index++;
    }
    return buffer.toString();
  }

  /**
   * Return a string of the form "0,1,2,3,4" (where size=5) for use in reorderable elvs
   * (Specifying an initialOrder in the form that is same as that in List in field)
   * 20021103AH
   * @param size
   * @return initialOrderString
   */
  public static String initialOrderString(int size)
  {
    if(size == 0)
    {
      return "";
    }
    else if(size == 1)
    {
      return "0";
    }
    else
    {
      StringBuffer order = new StringBuffer();
      for(int i=0; i < size; i++)
      {
        order.append(i);
        if(i < (size-1)) order.append(",");
      }
      return order.toString();
    }
  }

  /*
   * Convienience method to shallow copy between two lists using list.clear() and list.addAll()
   * Neither list may be null. The destination list is modified by this method.
   * @param fromList
   * @param toList
   */
  public static void transferList(List fromList, List toList)
  {
    if (fromList == null) //20030424AH
      throw new NullPointerException("fromList is null");
    if (toList == null) //20030424AH
      throw new NullPointerException("toList is null");
    toList.clear();
    toList.addAll(fromList);
  }

  /*
   * Reorder the contents of a List based on order. This version simply converts the
   * String array to an int[] array and calls arrangeList(List, int[]).
   * The List passed in is modified.
   * @param list
   * @param order 
   */
  public static void arrangeList(List list, String[] order)
  {
    int[] intOrder = primitiveIntArrayValue(order);
    arrangeList(list, intOrder);
  }

  /*
   * Reorder the contents of a List based on order. This version simply explodes the
   * String to an int[] array and calls arrangeList(List, int[]).
   * The List passed in is modified.
   * @param list
   * @param order 
   */
  public static void arrangeList(List list, String order)
  {
    int[] intOrder = explodeInt(order,",");
    arrangeList(list, intOrder);
  }

  /*
   * Re-order the contents of the specified list based on an array of new positions.
   * For example if order[0]=4 then item 4 in the list is moved to index 0, etc...
   * Items that are not included in the array will no longer appear in the modified list.
   * If items not in the list (out of index range) are specified then an error will result.
   * @param list to be modified
   * @param order an array of ints specifying new positions
   */
  public static void arrangeList(List list, int[] order)
  {
    if(list == null) return;
    if(order == null)
    {
      order = new int[0];
    }
    int listSize = list.size();
    if(listSize == 0)
    {
      return;
    }
    List newOrder = new ArrayList(order.length);
    for(int i=0; i < order.length; i++)
    {
      int next = order[i];
      if( (next < 0) || (next >= listSize) )
      {
        throw new IllegalArgumentException("order[" + i 
                  + "] specifies object at list["
                  + next + "] (list.size()==" + listSize + ")");
      }
      Object nextObject = list.get(next);
      newOrder.add(nextObject);
    }
    transferList(newOrder,list);
  }

  /*
   * Return an array of primitive int based on passed array of String.
   * The primitiveIntValue() method is called to obtain the int values.
   * @param stringArray
   * @return intArray
   */ 
  public static int[] primitiveIntArrayValue(String[] stringArray)
  { //20030424AH - Renamed from intArrayValue
    if(stringArray == null) return null;
    int[] intArray = new int[stringArray.length];
    for(int i=0; i < intArray.length; i++)
    {
      intArray[i] = primitiveIntValue(stringArray[i]);
    }
    return intArray;
  }

  /*
   * Return an array of primitive int based on passed array of String.
   * The primitiveIntValue() method is called to obtain the int values.
   * @param stringArray
   * @return intArray
   */ 
  public static long[] primitiveLongArrayValue(String[] stringArray)
  { //20030424AH - Renamed from longArrayValue
    if(stringArray == null) return null;
    long[] longArray = new long[stringArray.length];
    for(int i=0; i < longArray.length; i++)
    {
      longArray[i] = primitiveLongValue(stringArray[i]);
    }
    return longArray;
  }
  
  /*
   * Return an array of primitive int based on passed array of String.
   * If checkValid is false then the primitiveIntValue() method is called to obtain the
   * int values, otherwise Long.parseLong is used directly and an exception is thrown if
   * an invalid value is encountered.
   * @param stringArray
   * @param checkValid set to true to indicate values should be checked to see if can convert
   * @return intArray
   */ 
  public static long[] primitiveLongArrayValue(String[] stringArray, boolean checkValid)
  { //20030424AH
    if(checkValid)
    {
      if(stringArray == null) return null;
      long[] longArray = new long[stringArray.length];
      for(int i=0; i < longArray.length; i++)
      {
        try
        {
          longArray[i] = Long.parseLong(stringArray[i]);
        }
        catch(Exception e)
        {
          throw new IllegalArgumentException("Element at index " + i + " with value of "
                                              + stringArray[i]
                                              + " cannot be converted to a long");
        }
      }
      return longArray;
    }
    else
    {
      return primitiveLongArrayValue(stringArray);
    }
  }
  

  /**
   * Returns the classname of the object or null if the object reference is null
   * @param object
   * @return className
   */
  public static String getObjectClassName(Object o)
  {
    if(o == null)
    {
      return null;
    }
    else
    {
      return o.getClass().getName();
    }
  }

  /**
   * Returns false if the string is null or ""
   * @param string
   * @returns notEmpty
   */
  public static boolean stringNotEmpty(String string)
  {
    if(string == null) return false;
    if("".equals(string)) return false;
    return true;
  }

  /**
   * Returns true if string is null or ""
   * @param string
   * @returns empty
   */
  public static boolean stringEmpty(String string)
  { //20030113AH - I seem to need this way round more often!
    if(string == null) return true;
    if("".equals(string)) return true;
    return false;
  }

  /**
   * Returns true if the collection contains any element that matches that specified according to
   * the equals() method, or if null is passed for object and an null element encountered.
   * If the collection is null or empty returns false.
   * @param collection to be searched
   * @param object to search for
   * @returns boolean true if found false otherwise
   */
  public static boolean collectionContains(Collection collection, Object object)
  { //20030113AH
    if( (collection == null) || collection.isEmpty() )
    {
      return false;
    }
    boolean found = false;
    Iterator i = collection.iterator();
    while(i.hasNext())
    {
      Object element = i.next();
      if(element == null)
      {
        if(object == null)
        {
          found = true;
        }
      }
      if(element.equals(object))
      {
        found = true;
      }
      if(found) return found;
    }
    return found;
  }

  /**
   * Tests to see if object1.equals(object2) where both objects might be null. (If both are null
   * will return true).
   * @param object1
   * @param object2
   * @return equal
   */
  public static boolean objectsEqual(Object object1, Object object2)
  { //20030121AH
    if(object1 == null)
    {
      return object2 == null;
    }
    else
    {
      return object1.equals(object2);
    }
  }

  /*
   * Utility method for use in debugging. Will dump the contents of a map, including
   * class names of contents to log
   * @todo refactor to take stream and/or log param
   * @param map
   */
  public static void dumpMap(Map map)
  { //20030122AH - Method for debugging
    if(_log.isDebugEnabled())
    {
      _log.debug("Started ------------------------------------------");
      if(map == null)
      {
        _log.debug("map is null");
      }
      else
      {
        _log.debug("Map class=" + getObjectClassName(map));
        Set entrySet = map.entrySet();
        _log.debug("entrySet.size=" + entrySet.size());
        Iterator i = entrySet.iterator();
        while(i.hasNext())
        {
          Map.Entry entry = (Map.Entry)i.next();
          Object key = entry.getKey();
          Object value = entry.getValue();
          _log.debug("[Entry].......................");
          _log.debug("Key class=" + getObjectClassName(key));
          _log.debug("Key value=" + key);
          _log.debug("Value class=" + getObjectClassName(value));
          _log.debug("Value value=" + value);
        }
      }
      _log.debug("Completed ----------------------------------------");
    }
  }

  /*
   * Utility method for use in debugging. Will dump the contents of a collection, including
   * class names of contents to log
   * @todo refactor to take stream and/or log param
   * @param map
   */
  public static void dumpCollection(Collection collection)
  { // 20030213 DDJ - Method for debugging
    if(_log.isDebugEnabled())
    {
      _log.debug("Started ------------------------------------------");
      if(collection == null)
      {
        _log.debug("collection is null");
      }
      else
      {
        _log.debug("Collection class=" + getObjectClassName(collection));
        _log.debug("collection.size=" + collection.size());
        Iterator i = collection.iterator();
        while(i.hasNext())
        {
          Object obj = i.next();
          _log.debug("[Object]......................");
          _log.debug("Object class=" + getObjectClassName(obj));
          _log.debug("Object value=" + obj);
        }
      }
      _log.debug("Completed ----------------------------------------");
    }
  }

  /*
   * Utility method for use in debugging. Will dump the contents of a FormFileElement[] to log
   * @todo refactor to take stream and/or log param
   * @param map
   */
  public static void dumpFormFileElementArray(FormFileElement[] elements)
  {
    if(_log.isDebugEnabled())
    {
      _log.debug("Started ------------------------------------------");
      for(int i=0; i < elements.length; i++)
      {
        _log.debug("elements[" + i + "]=" + elements[i]);
      }
      _log.debug("Completed ----------------------------------------");
    }
  }

  /*
   * Extract filename portion from a path.
   * @param fullPath
   * @return filename
   */
  public static String extractFilename(String filePath)
  {
    int max = Math.max(filePath.lastIndexOf('/'),filePath.lastIndexOf('\\'));
    String filename = filePath.substring( max + 1);
    return filename;
  }

  /*
   * Extract path segment from a full path
   * @param fullPath
   * @return path
   */
  public static String extractPath(String filePath)
  {
    int max = Math.max(filePath.lastIndexOf('/'),filePath.lastIndexOf('\\'));
    String path = filePath.substring( 0, max + 1);
    return path;
  }

  /*
   * A very simple capitaliser for capitalising property names.
   * @todo: improve algorithm
   * @param propertyName
   * @return capitalisedPropertyName
   */
  public static String capitalise(String field)
  { //20030325 - A rather poor capitaliser. @todo: improve!
    if(field == null) return null;
    if(field.length() < 1) return field;
    String initialChar = field.substring(0,1).toUpperCase();
    if(field.length() < 2) return initialChar;
    String remainder = field.substring(1,field.length());
    return initialChar + remainder;
  }

  /*
   * A rather naive and potentially unreliable implementation of substring replacement that is
   * not guaranteed to work in all cases.
   * @todo: improve
   * @param string the string to be modified
   * @param oldStr the substring to be found in string
   * @param newStr the substring to be used to replace oldStr
   * @return modifiedString
   */
  public static String replaceSubstring(String string, String oldStr, String newStr)
  { //20030407AH - unreliable impl
    StringBuffer buffer = new StringBuffer(string);
    boolean cont = false;
    do
    {
      int index = buffer.toString().indexOf(oldStr,0);
      if(index != -1)
      {
        buffer.replace(index, index + oldStr.length(), newStr);
        cont = true;
      }
      else
      {
        cont = false;
      }
    }
    while(cont);
    return buffer.toString();
  }


  /*
   * Find first index of specified String in array of Strings.
   * You can also search for null values in the array.
   * @param values
   * @param value the value we are searching for
   * @param caseSensitive a flag indicating if comparison should be case sensitive
   * @return index of value or -1 if not found
   */
  public static int findValueInArray(String[] values, String value, boolean caseSensitive)
  { //20030410AH
    if(values == null) return -1;
    for(int i=0; i < values.length; i++)
    {
      if(values[i] == null)
      {
        if(value == null) return i;
      }
      else
      {
        if(caseSensitive)
        {
          if(values[i].equals(value)) return i;
        }
        else
        {
          if(values[i].equalsIgnoreCase(value)) return i;
        }
      }
    }
    return -1;
  }

  /*
   * Given a filename returns the file extension (the bit after the '.').
   * If filename is null, empty or has no extension returns null
   * @param filename
   * @return extension
   */
  public static String getFileExtension(String filename)
  { //20030410AH
    if(filename == null) return null;
    if("".equals(filename)) return null;
    int extSep = filename.lastIndexOf(".");
    if(extSep == -1) return null;
    extSep += 1;
    String extension = (extSep == filename.length()) ? null : filename.substring(extSep);
    return extension;
  }
  
  /*
   * Returns a new array containing the contents of both passed arrays
   * @param array1
   * @param array2
   * @return concatenatedArray
   */
  public static String[] concatArrays(String[] array1, String[] array2)
  { //200305013AH
    if( (array1 == null) && (array2 == null)) return null;
    if(array1 == null) array1 = EMPTY_STRING_ARRAY;
    if(array2 == null) array2 = EMPTY_STRING_ARRAY;
    String[] array3 = new String[array1.length + array2.length];
    int i=0;
    int j=0;
    for(i=0; i < array1.length; i ++)
    {
      array3[i] = array1[i];
    }
    for(j=0; j < array2.length; j++)
    {
      array3[i + j] = array2[j];
    }
    return array3;
  }
  
  /*
   * Returns a new array containing the contents of both passed arrays
   * @param array1
   * @param array2
   * @return concatenatedArray
   */
  public static long[] concatArrays(long[] array1, long[] array2)
  { //20030513AH
    if( (array1 == null) && (array2 == null)) return null;
    if(array1 == null) array1 = EMPTY_PRIMITIVE_LONG_ARRAY;
    if(array2 == null) array2 = EMPTY_PRIMITIVE_LONG_ARRAY;
    long[] array3 = new long[array1.length + array2.length];
    int i=0;
    int j=0;
    for(i=0; i < array1.length; i ++)
    {
      array3[i] = array1[i];
    }
    for(j=0; j < array2.length; j++)
    {
      array3[i + j] = array2[j];
    }
    return array3;
  }
  
  /*
   * Returns a new array containing the contents of both passed arrays
   * @param array1
   * @param array2
   * @return concatenatedArray
   */
  public static Object[] concatArrays(Object[] array1, Object[] array2)
  { //20030513AH
    if( (array1 == null) && (array2 == null)) return null;
    if(array1 == null) array1 = EMPTY_OBJECT_ARRAY;
    if(array2 == null) array2 = EMPTY_OBJECT_ARRAY;
    Object[] array3 = new Object[array1.length + array2.length];
    int i=0;
    int j=0;
    for(i=0; i < array1.length; i ++)
    {
      array3[i] = array1[i];
    }
    for(j=0; j < array2.length; j++)
    {
      array3[i + j] = array2[j];
    }
    return array3;
  }
  
  /*
   * Returns a new array containing all the non-null elements in the passed array.
   * If array reference itself is null then returns null.
   * @param array with zero or more null elements
   * @return array without null elements
   */
  public static String[] removeNulls(String[] array)
  { //20030513AH
    if(array == null) return null;
    int nonNullCount = 0;
    for(int i=0; i < array.length; i++)
    {
      if(array[i] != null)
      {
        nonNullCount++;
      }
    }
    String[] returnArray = new String[nonNullCount];
    int j=0;
    for(int i=0; i < array.length; i++)
    {
      if(array[i] != null)
      {
        returnArray[j] = array[i];
        j++;
      }
    }
    return returnArray;
  }
  
  /*
   * Return a new array that contains all those elements of array that are not in removals array and
   * are not null.
   * @param array
   * @param removals - elements to remove from the array
   * @return resultArray
   */
  public static String[] removeFromArray(String[] array, String[] removals)
  { //20030513AH
    if(array == null) return null;
    array = (String[])array.clone(); //Clone so we dont damage original array
    for(int i=0; i < array.length; i++)
    {
      if( arrayContains(removals, array[i]) )
      {
        array[i] = null;
      }
    }
    String[] results = removeNulls(array);
    return results;
  }
  
  /*
   * Return a new array that is 1 element larger than array passed, and contains the same
   * contents as array with athe addition of value as the last element.
   * @param array to copy
   * @param value to append
   * @return array with contents of array and value
   */
  public static Object appendArray(Object[] array, Object value)
  {
    if( (array == null) && (value == null) ) return null;
    if(array == null) array = EMPTY_OBJECT_ARRAY;
    Object[] result = new String[array.length + 1];
    for(int i=0; i < array.length; i++)
    {
      result[i] = array[i];
    }
    result[result.length - 1] = value;
    return result;
  }
  
  /*
   * Returns the value trimmed and converted to null if it was an empty string
   * @param value
   * @return cleanValue
   */
  public static String clean(String value)
  { //20030514AH
    return stringEmpty(value) ? null : value.trim();
  }


/*
   * Convienience method to convert a string into an object of specified type.
   * This is far from being a 'universal' converter - Available target types
   * currently supported (and the order they are evaluated in the big if else if
   * statement inside the class) are:
   * java.lang.String
   * java.lang.Long
   * java.lang.Boolean
   * java.lang.Integer
   * java.lang.Short
   * java.lang.Byte
   * java.lang.Float
   * java.lang.Double
   * java.lang.Number
   * java.lang.Object
   * java.util.Date
   * java.sql.Timestamp
   * java.sql.Date
   * In all cases passing null to this method will result in null being returned.
   * There are two flags that may be set to modify the behaviour of this method.
   * The first - emptyIsNull - specifies that (no matter what the valueClass) an empty
   * string "" for value will result in null being returned. The second flag
   * useStaticUtils - determines the nature of the conversion. If false, then standard methods
   * are used to perform the conversion (such as passing the string to the constructor
   * for the relevant type). If the string is not valid for that type then an exception
   * will likely be thrown (depends on the type. All the primitive wrappers will throw
   * an exception). If useStaticUtils is true then the conversion methods in the StaticUtils
   * class are used. These will generally return null for invalid values. In the case of
   * Boolean values the difference also extends to the range of values that will be recognised
   * as true. While java.lang.Boolean constructor only recognises "true" as true, the
   * StaticUtils version recognises most non null or empty strings as true with the exception
   * of a predifined hardcoded list of false values such as "false","no","negative", etc...
   * If the valueClass is not one of those supported by this method then an
   * UnsupportedOperationException will be thrown. (Unless value is null or "" and
   * emptyIsNull is true in which case null will of course be returned)
   * If the valueClass is java.lang.Number then for strings containing a '.' the concrete
   * implementation will use a Double, otherwise will use a Long.
   * Note that for conversion to Date and TimeStamp the converted value will have been based
   * on the default timezone and locale - which for the gtas p-tier isnt necessarily the
   * same ones that should be used. That makes this method primarily useful only for
   * testing if the value *can* be converted to a Date or TimeStamp. The logic applied here
   * is special. The values is first tested to see if it can be converted to a Long - and if so
   * is considered a millisecond value, otherwise it will attempt to convert it using a
   * DateFormat with the pattern "yy-MM-dd HH:mm:ss" - which is our standard for textual
   * input of a date at the p-tier (for now at least). In these cases the useStaticUtils flag is
   * re-interpreted to mean 'return null if cant convert'.
   * @param value A String value to be converted to object of specified class or null
   * @param valueClass The full classname of class to convert value to
   * @param emptyIsNull If true the empty string "" for value will ALWAYS return null
   * @param useStaticUtils Set to true to use methods in StaticUtils class to convert the value
   * @throws UnsupportedOperationException if valueClass is not one the method can handle
   * @throws GTClientException which will wrap any exception thrown when trying to convert
   */
  public static Object convert(String value,
                        String valueClass,
                        boolean emptyIsNull,
                        boolean useStaticUtils)
    throws GTClientException, UnsupportedOperationException
  { //20030519AH
    if( (value == null) || ("".equals(value) && emptyIsNull) )
    { //If the value is already null or is empty string and we specifically want to consider
      //empty strings as null, then we return null immediately
      return null;
    } 
    try
    {
      if(String.class.getName().equals(valueClass))
      {
        return value; //that was easy ;->
        //nb that if we set emptyIsNull and passed in an empty String and a
        //valueClass of string, we got back a null of course... 
      }
      else if(Long.class.getName().equals(valueClass))
      {
        return useStaticUtils ? longValue(value) : new Long(value);
      }
      else if(Boolean.class.getName().equals(valueClass))
      {
        return useStaticUtils ? booleanValue(value) : new Boolean(value); 
      }
      else if(Integer.class.getName().equals(valueClass))
      {
        return useStaticUtils ? integerValue(value) : new Integer(value); 
      }
      else if(Short.class.getName().equals(valueClass))
      {
        return useStaticUtils ? shortValue(value) : new Short(value); 
      }
      else if(Byte.class.getName().equals(valueClass))
      {
        return useStaticUtils ? byteValue(value) : new Byte(value); 
      }
      else if(Float.class.getName().equals(valueClass))
      {
        return useStaticUtils ? floatValue(value) : new Float(value); 
      }
      else if(Double.class.getName().equals(valueClass))
      {
        return useStaticUtils ? doubleValue(value) : new Double(value); 
      }
      else if(Number.class.getName().equals(valueClass))
      {
        if( (value != null) && (value.indexOf(".") != -1) )
        {
          return useStaticUtils ? doubleValue(value) : new Double(value);
        }
        else
        { 
          return useStaticUtils ? longValue(value) : new Long(value);
        } 
      }
      else if(Object.class.getName().equals(valueClass))
      {
        return value; //another easy one ;-)
        //nb again that the emptyIsNull flag will be respected
      }
      else if(java.util.Date.class.getName().equals(valueClass))
      {
        try
        { //Try assuming its a milliseconds value first
          long milliseconds = Long.parseLong(value);
          return useStaticUtils ? dateValue(value) : new java.util.Date(milliseconds);
        }
        catch(Throwable t)
        { //Guess not!
          java.util.Date date = DateUtils.parseDate(value, null, null, null);
          if(!useStaticUtils)
          {
            if( !stringEmpty(value) && (date == null) )
            {
              throw new IllegalArgumentException(value + " cannot be converted to a date");
            }
            else
            {
              return date;
            }
          }
          else
          {
            return date;
          } 
        }
      }
      else if(java.sql.Timestamp.class.getName().equals(valueClass))
      {
        try
        { 
          long milliseconds = Long.parseLong(value);
          return useStaticUtils ? timestampValue(value) : new java.sql.Timestamp(milliseconds);
        }
        catch(Throwable t)
        { 
          java.util.Date date = (java.util.Date)convert(value,java.util.Date.class.getName(),true,true);
          if( !stringEmpty(value) && (date == null) && (!useStaticUtils))
          {
            throw new IllegalArgumentException(value + " cannot be converted to a timestamp");
          }
          else
          {
            return new java.sql.Timestamp(date.getTime());
          }
        }
      }
      else if(java.sql.Date.class.getName().equals(valueClass))
      {
        try
        { 
          long milliseconds = Long.parseLong(value);
          return useStaticUtils ? sqlDateValue(value) : new java.sql.Date(milliseconds);
        }
        catch(Throwable t)
        { 
          java.util.Date date = (java.util.Date)convert(value,java.util.Date.class.getName(),true,true);
          if( !stringEmpty(value) && (date == null) && (!useStaticUtils))
          {
            throw new IllegalArgumentException(value + " cannot be converted to an sql date");
          }
          else
          {
            return new java.sql.Date(date.getTime());
          }
        }
      }
    }
    catch(Throwable t)
    {
      if(useStaticUtils)
      {
        throw new GTClientException("Error converting value:"
            + value
            + " to class \""
            + valueClass, t);
      }
      else
      {
        throw new GTClientException("Error converting value:"
            + value
            + " to class \""
            + valueClass
            + " using StaticUtils conversions", t);
      }
    }
    
    //If execution reaches this point then the valueClass is not one we have been
    //trained to handle. :-(
    throw new UnsupportedOperationException("Conversion to class \""
                + valueClass
                + "\" is not supported by this method.");
  }
  
  /*
   * Checks a string array to see if it is null or of zero length and returns true
   * if this is the case. You can also have it call stringEmpty() on all the elements
   * of the array and return true if any of them are empty according to that method by
   * passing a value of true for checkAllElements.
   * @param array the array of String to be checked
   * @param checkAllElements set to true to call stringEmpty() on each element
   * @return empty a true value if the array is 'empty'
   */
  public static boolean stringArrayEmpty(String[] array, boolean checkAllElements)
  { //20030520AH
    if(array == null) return true;
    if(array.length == 0) return true;
    if(checkAllElements)
    {
      for(int i=0; i < array.length; i++)
      {
        if(stringEmpty(array[i])) return true;
      }
    }
    return false;    
  } 
  
  /*
   * Convienience method that calls reDim using a defaultValue of null.
   * See javadoc for redim for details.
   * @param array the array to be redimmed (new array created)
   * @param newSize the size of the new array
   * @return newArray containing elements copied from array where applicable
   * @throws IllegalArgumentException if newSize < 0
   */
  public static String[] reDim(String[] array, int newSize)
  { //20030521AH
    return reDim(array, newSize, null);
  }
  
  /*
   * Create a new array with elements copied from array passed having newSize specified.
   * A new array is created with the size specified in newSize, and the elements in the
   * array passed are copied into it. If the new array is smaller than the one passed
   * only those elements that will fit are copied. If larger, then defaultValue is used
   * to populate the additional elements (note that null elements inside the passed array
   * are copied as normal and not overwritten with defaultValue).
   * If newSize is the same as the existing size then instead of creating a new array,
   * the existing one is returned unaffected.
   * You may pass a null array, for which you will get in return a new array allocated with
   * the newSize specified (may be 0 - but not negative of course).
   * This method never returns null.
   * @param array The array to be copied into new array
   * @param newSize The size of the new array (if same size then original array is returned)
   * @param defaultValue The default value to populate extra elements with (may be null)
   * @return newArray A newly created array (unless size was same) containing elements from array
   * @throws IllegalArgumentException if newSize < 0
   */
  public static String[] reDim(String[] array, int newSize, String defaultValue)
  { //20030521AH
    if(newSize < 0)
      throw new IllegalArgumentException("newSize is negative (" + newSize + ")");
    if( (array != null) && (array.length == newSize) )
    {
      return array;
    }
    else
    {
      String[] newArray = new String[newSize];
      int oldSize = getArrayLength(array);
      for(int i=0; i < newArray.length; i++)
      {
        if(i < oldSize)
        {
          newArray[i] = array[i];
        }
        else
        {
          if(defaultValue != null)
          {
            newArray[i] = defaultValue;
          }
          else
          { //Cause loop to terminate as no need to iterate more
            i = newArray.length; 
          }
        }
      }
      return newArray;
    }  
  }
  
  /*
   * If the array is null returns 0 otherwise returns array.length
   * @param array Array to get size of (may be null)
   * @return array length or 0 if null or 0 length
   */
  public static int getArrayLength(Object[] array)
  {
    if(array == null) return 0;
    return array.length;
  }
}