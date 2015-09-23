/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Partner.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 19, 2007   i00107              Created
 */

package com.gridnode.gridtalk.testkit.http;

/**
 * @author i00107
 * A Partner to simulate.
 */
public class Partner implements Comparable
{
	
  private String _id;
  private int _numTx;
  private int _counter = 0;
  
  /**
   * 
   * @param id Partner id
   * @param numTx Number of transactions to send for this Partner
   */
  Partner(String id, int numTx)
  {
    _id = id;
    _numTx = numTx;
  }
  
  /**
   * Increment the counter of transactions sent
   */
  public void incrementCounter()
  {
    _counter++;
  }
  
  /**
   * @return <b>true</b> if has finished sending all tx for this Partner
   */
  public boolean hasFinishedTx()
  {
    return _numTx <= _counter;
  }
  
  /**
   * @return Partner Id
   */
  public String getId()
  {
    return _id;
  }
  
  public int compareTo(Object obj) 
  {
	Partner p  = (Partner)obj;
	return _id.compareTo(p.getId( ));
  }
  
  /**
   * @return Counter of transactions sent
   */
  public int getCounter()
  {
    return _counter;
  }

  public int getNumTx() 
  {
	return _numTx;
  }
}
