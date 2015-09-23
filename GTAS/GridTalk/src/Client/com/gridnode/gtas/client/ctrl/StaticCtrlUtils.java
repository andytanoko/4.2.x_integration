/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: StaticCtrlUtils.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-04     Andrew Hill         Created
 * 2002-11-08     Andrew Hill         getUids()
 * 2002-12-26     Andrew Hill         getLongCollection()
 * 2002-12-27     Andrew Hill         EMPTY_STRING_ARRAY, EMPTY_COLLECTION
 * 2003-07-18     Andrew Hill         longArray()
 */
package com.gridnode.gtas.client.ctrl;

import java.util.*;
import com.gridnode.gtas.client.GTClientException;

public class StaticCtrlUtils
{
  public static final String[] EMPTY_STRING_ARRAY = new String[0];
  public static final Collection EMPTY_COLLECTION = Collections.EMPTY_LIST;

  /**
   * Returns true if the fieldName passed has reference to nested entity field
   */
  public static boolean isNestedFieldName(String fieldName)
  {
    return (fieldName.indexOf(".") != -1);
  }

  /**
   * Extracts the subfieldreference from the nested fieldName.If the fieldname is not nested
   * returns fieldName
   * (Returns the substring after the dot or the full string)
   */
  public static String extractSubFieldName(String fieldName)
  {
    int index = fieldName.indexOf(".",0);
    if(index == -1) return fieldName;
    String subFieldName = fieldName.substring(index+1,fieldName.length());
    return subFieldName;
  }

  /**
   * Returns the name of the first field of the nesting heirarchy of fieldName.
   * If fieldName is not a nested field reference, returns null
   * (Returns the substring before the dot or null)
   */
  public static String extractSuperFieldName(String fieldName)
  {
    int index = fieldName.indexOf(".",0);
    if(index == -1) return null;
    return fieldName.substring(0,index);
  }

  public static Collection getLongCollection(long[] longArray)
  { //20021226AH
    if(longArray == null) return null; //@todo: should I perhaps return empty collection????
    ArrayList collection = new ArrayList(longArray.length);
    for(int i=0; i < longArray.length; i++)
    {
      collection.add( new Long(longArray[i]) );
    }
    return collection;
  }

  public static long[] getPrimitiveLongArray(Collection longCollection)
  { //20021226AH
    if(longCollection == null) return null;
    long[] longArray = new long[longCollection.size()];
    Iterator iterator = longCollection.iterator();
    int i = 0;
    while(iterator.hasNext())
    {
      try
      {
        longArray[i] = ((Long)iterator.next()).longValue();
        i++;
      }
      catch(ClassCastException cce)
      {
        throw new java.lang.ClassCastException("Illegal non-Long value in collection at index " + i);
      }
      catch(NullPointerException npe)
      {
        throw new java.lang.NullPointerException("Null value in collection at index " + i);
      }
    }
    return longArray;
  }

  public static Collection getUids(Collection entities) throws GTClientException
  {
    if(entities == null) return null;
    try
    {
      Vector v = new Vector(entities.size());
      Iterator i = entities.iterator();
      while(i.hasNext())
      {
        IGTEntity entity = (IGTEntity)i.next();
        if(entity == null)
        {
          v.add(null);
        }
        else
        {
          v.add(entity.getUidLong());
        }
      }
      return v;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting collection of entity uids from collection of entities",t);
    }
  }

  /**
   * Creating a read-only vField for displaying something is common enough to deserve
   * a static utility method to create its FMI.
   * @param entityAndField - the lable such as "x500Name.organizationalUnit"
   * @param fieldId
   * @param minLen
   * @param maxLen
   * @return fmi
   * @throws GTClientException
   */
  static VirtualSharedFMI createReadOnlyTextSharedVFMI( String entityAndField,
                                                        Number fieldId,
                                                        int minLen,
                                                        int maxLen)
    throws GTClientException
  { //20030121AH
    VirtualSharedFMI fmi = new VirtualSharedFMI(entityAndField, fieldId);
    fmi.setMandatoryCreate(false);
    fmi.setMandatoryUpdate(false);
    fmi.setEditableCreate(false);
    fmi.setEditableUpdate(false);
    fmi.setDisplayableCreate(true);
    fmi.setDisplayableUpdate(true);
    fmi.setCollection(false);
    fmi.setValueClass("java.lang.String");
    ITextConstraint constraint = new TextConstraint(minLen,maxLen);
    fmi.setConstraint(constraint);
    return fmi;
  }
  
  /**
   * Utility method to convert a long[] to a Long[]
   * @param longs
   * @return Longs
   */
  public static Long[] longArray(long[] longs)
  { //20030717AH
    if(longs == null) return null;
    Long[] values = new Long[longs.length];
    for(int i=0; i < longs.length; values[i]= new Long(longs[i++]))
    {
    }
    return values;
  }
  
  public static String objectClassName(Object o)
  { //20030718AH
    return o == null ? "null" : o.getClass().getName();
  }
}