/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProductKey.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Neo Sok Lay         Created
 * Apr 08 2003    Koh Han Sing        Set hours, minutes and seconds to 0s
 *                                    in license start date and end date.
 */
package com.gridnode.gtas.server.registration.product;

import com.gridnode.gtas.server.registration.exceptions.InvalidProductKeyException;
import java.util.Date;
import java.util.Calendar;
import java.util.Random;
import java.io.Serializable;

/**
 * The class represents a GridTalk Product Key. A GridTalk Product Key
 * is a 22-char string in which encoded are the following information for
 * GridTalk product registration:<p>
 * Gridnode ID registered by this product key<br>
 * Gridnode Category<br>
 * Number Business Connections allowed for this Gridnode (GridTalk)<br>
 * Validity Period (Start & End Dates)<br>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class ProductKey
  implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -281140231003401040L;
	private static final int[][] PARAM_INDICES = {
                                                {0, 2},
                                                {3, 3},
                                                {4, 4},
                                                {5, 7},
                                                {8, 8},
                                                {9, 9},
                                                {10, 15},
                                                {16, 21},
                                               };
  private static final int IDX_START_INDEX = 0;
  private static final int IDX_END_INDEX   = 1;
  private static final int NUM_PARAMS      = 8;
  private static final int PRODUCT_KEY_LENGTH = 22;
  private static final int DIGIT_TABLE_LENGTH = 33;
  private static final String DIGIT_TABLE_STR = "FHARWCZ2BXG3QMP97EYDV8NLT56IJU4KS";

  private int _startYear, _startMth, _startDay;
  private int _endYear, _endMth, _endDay;
  private int _nodeID;
  private int _numConnections;
  private String _category;

  private Random _random = new Random();
  private String _messedDigitTableStr;

  private ProductKey(String prodKeyStr, int nodeId)
  {
    char[] pkTmp = new char[PRODUCT_KEY_LENGTH];
    char[] digitTable = new char[DIGIT_TABLE_LENGTH];
    int[] params = new int[NUM_PARAMS];
    int paramBuf;
    byte[] categoryBytes = new byte[3];

    prodKeyStr.getChars(0, PRODUCT_KEY_LENGTH, pkTmp, 0);
    DIGIT_TABLE_STR.getChars(0, DIGIT_TABLE_LENGTH, digitTable, 0);

    _messedDigitTableStr = messItUp(digitTable, nodeId);

    for (int i=0; i<NUM_PARAMS; i++)
    {
      if (i == NUM_PARAMS-1) //last param
        _messedDigitTableStr = messItUp(digitTable, params[i-1]);

      params[i] = getDigit(pkTmp, PARAM_INDICES[i][IDX_START_INDEX],
                    PARAM_INDICES[i][IDX_END_INDEX]);
    }

    //Start Date
    _startYear = params[0] / 3;
    _startMth  = params[1] / 2;
    _startDay  = params[2] - 1;

    //End Date
    _endYear   = params[3] / 2 - _startYear;
    _endMth    = params[4];
    _endDay    = params[5];

    //Gridnode ID
    _nodeID = params[7] - params[6] -
                (_startDay*1000000 + _startMth + _startYear +
                 _endDay*1000000 + _endMth*10000 + _endYear*100);

    paramBuf = params[6] - _startYear*_startMth*_endDay -
                   _endYear*_endMth*_startDay;

    //Number of Business Connections
    _numConnections = paramBuf % 10000;

    //GridNode Category
    int boundary = categoryBytes.length-1;
    for (int i=0; i<categoryBytes.length; i++)
    {
      paramBuf /= (i==0?10000:26);

      if (i==boundary)
        categoryBytes[boundary-i] = new Integer(paramBuf+'A').byteValue();
      else
        categoryBytes[boundary-i] = new Integer(paramBuf%26+'A').byteValue();
    }
    _category = new String(categoryBytes);
  }

  /**
   * Mess up a specified digit table using a particular seed for the randomizer.
   *
   * @param table The digit table.
   * @param seed The seed for the randomizer.
   * @return Messed up digit table in String form.
   *
   * @since 2.0 I5
   */
  private String messItUp(char[] table, long seed)
  {
    _random.setSeed(seed);

    int idx1, idx2;
    char tmpChar;
    for (int i=0; i<100; i++)
    {
      idx1 = i % table.length;
      idx2 = _random.nextInt(table.length);

      //swap the digits at idx1 & idx2
      tmpChar = table[idx1];
      table[idx1] = table[idx2];
      table[idx2] = tmpChar;
    }

    return new String(table);
  }

  /**
   * Obtain a digit from the digit table (messed-up) for an array of characters.
   *
   * @param cArray The character array.
   * @param startPos Starting position in the character array.
   * @param endPos Ending position in the character array. Characters between
   * startPos and endPos inclusive will be used to obtain the digit from
   * the digit table.
   *
   * @return The digit for the specified array of characters.
   */
  private int getDigit(char[] cArray, int startPos, int endPos)
  {
    int digit = 0;
    int index;
    for (int i=startPos; i<=endPos; i++)
    {
      index = _messedDigitTableStr.indexOf(cArray[i]);
      digit = digit * DIGIT_TABLE_LENGTH + index;
    }

    return digit;
  }

  public int getStartYear()
  {
    return _startYear;
  }

  public int getStartMth()
  {
    return _startMth;
  }

  public int getStartDay()
  {
    return _startDay;
  }

  /**
   * Get the Start date of the validity period.
   *
   * @return validity period start date.
   */
  public Date getStartDate()
  {
    Calendar cal = Calendar.getInstance();
    cal.set(getStartYear(), getStartMth()-1, getStartDay(), 0, 0, 0);
    return cal.getTime();
  }

  public int getEndYear()
  {
    return _endYear;
  }

  public int getEndMth()
  {
    return _endMth;
  }

  public int getEndDay()
  {
    return _endDay;
  }

  /**
   * Get the End date of the validity period.
   *
   * @return validity period end date.
   */
  public Date getEndDate()
  {
    Calendar cal = Calendar.getInstance();
    cal.set(getEndYear(), getEndMth()-1, getEndDay(), 0, 0, 0);
    return cal.getTime();
  }

  public int getNodeID()
  {
    return _nodeID;
  }

  public int getNumConnections()
  {
    return _numConnections;
  }

  public String getCategory()
  {
    return _category;
  }

  /**
   * Get the Product Key for the specified product key string.
   *
   * @param prodKeyStr The 22-char product key string.
   * @param nodeId The GridNode ID of the product key. This will determine
   * whether the Product Key is valid or not.
   *
   * @return The Product Key for the specified product key string and node id.
   * @throws InvalidProductKeyException The Product Key created does not tally
   * with the nodeId specified.
   */
  public static ProductKey getProductKey(String prodKeyStr, int nodeId)
    throws InvalidProductKeyException
  {
    ProductKey pKey = new ProductKey(prodKeyStr, nodeId);
    if (pKey.getNodeID() != nodeId)
      throw new InvalidProductKeyException(prodKeyStr, nodeId);

    return pKey;
  }

  public static void main(String[] args)
  {
    try
    {
      //521,"RLYMMXD4KKKNN7HN5I3KDX"
      //522,"6G3QQ9CHKKKRRW2RBSIKCD"
      ProductKey pKey = getProductKey("6G3QQ9CHKKKRRW2RBSIKCD", 522);

      System.out.println("NodeID: "+pKey.getNodeID());
      System.out.println("Category: "+pKey.getCategory());
      System.out.println("Connections: "+pKey.getNumConnections());
      System.out.println("StartDate: "+pKey.getStartDate());
      System.out.println("EndDate: "+pKey.getEndDate());
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}