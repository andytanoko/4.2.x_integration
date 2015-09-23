/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityOrderComparator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 19 2002    Neo Sok Lay         Created
 * Sep 04 2002    Neo Sok Lay         Allow sorting according to natural
 *                                    order of the field.
 * Oct 31 2003    Neo Sok Lay         Fix for GNDB00016037:-
 *                                      Allow "Entity" to be in Map form. 
 */
package com.gridnode.pdip.framework.db.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

/**
 * This comparator class is used for sorting a list of homogeneous entities
 * according to a single field in the entity, preserving the order of the
 * entities according to the comparing field as specified in a desired order
 * list.<p>
 * Another option is to sort the entities according to the natural order of a
 * field of the entities. This will only work if the compare fields contain
 * values that are <b>Comparable</b>.
 * <p>
 * The "entities" for comparison could be of either the <code>java.util.Map</code> 
 * type or <code>com.gridnode.pdip.framework.db.entity.IEntity</code> type. 
 * In either case, the compare field key must be of type <code>java.lang.Number</code>.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I3
 * @since 2.0 I4
 */
public final class EntityOrderComparator implements Comparator
{
  private ArrayList _desiredOrder;
  private Number     _compareField;

  /**
   * Construct an EntityOrderComparator
   *
   * @param compareField The ID of the field of the entity to compare.
   * @param desiredOrder A Collection of values of the <code>compareField</code>
   * dictating the desired order of the entities.
   */
  public EntityOrderComparator(Number compareField, Collection desiredOrder)
  {
    _compareField = compareField;
    _desiredOrder = new ArrayList(desiredOrder);
  }

  public EntityOrderComparator(Number compareField)
  {
    _compareField = compareField;
  }

  /**
   * @see Comparator#compare
   */
  public int compare(Object o1, Object o2)
  {
    /*031031NSL: Fix defect GNDB00016037
    Object val1 = ((IEntity)o1).getFieldValue(_compareField);
    Object val2 = ((IEntity)o2).getFieldValue(_compareField);
    */
    Object val1 = getCompareFieldValue(o1);
    Object val2 = getCompareFieldValue(o2);
    
    int result = 0;

    if (_desiredOrder == null || _desiredOrder.isEmpty())
      result = sortByNaturalOrder(val1, val2);
    else
      result = sortByDesiredOrder(val1, val2);

    return result;
  }

  /**
   * Get the value to use for comparison from the specified object.
   * 
   * @param o The object to compare value for. Can be either of <code>java.util.Map</code> type or
   * <code>com.gridnode.pdip.framework.db.entity.IEntity</code> type.
   * 
   * @return The value extracted from object <code>o</code>.
   */
  private Object getCompareFieldValue(Object o)
  {
    Object compareVal = null;
    if (o instanceof Map)
    {
      compareVal = ((Map)o).get(_compareField);
    } 
    else if (o instanceof IEntity)
    {
      compareVal = ((IEntity)o).getFieldValue(_compareField);
    }
    
    return compareVal;
  }
  private int sortByNaturalOrder(Object val1, Object val2)
  {
    int result = 0;

    if (val1 == null)
    {
      if (val2 != null)
        result = 1;
    }
    else if (val2 == null)
    {
      result = -1;
    }
    else
      result = ((Comparable)val1).compareTo(val2);

    return result;
  }

  private int sortByDesiredOrder(Object val1, Object val2)
  {
    int result = 0;

    int idx1 = _desiredOrder.indexOf(val1);
    int idx2 = _desiredOrder.indexOf(val2);

    int diff1 = idx1 - idx2;
    int diff2 = idx2 - idx1;

    if (diff1 <= -1)      //o1's position is in front
    {
      if (idx1 == -1)       //o1 not in desiredOrder, put it behind
        result = 1;       //positive, o2 < o1
      else
        result = diff1;   //negative, o1 < o2
    }
    else if (diff1 > 0)   //o2's position is in front
    {
      if (idx2 == -1)     //o2 not in desiredOrder, put it behind
        result = diff2;   //negative, o1 < o2
      else
        result = diff1;   //positive, o2 < o1
    }
    //else both the same

    return result;
  }

  /**
   * @see Comparator#equals
   */
  public boolean equals(Object obj)
  {
    return (obj != null) && (obj instanceof EntityOrderComparator);
  }

}