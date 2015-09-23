/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: LockUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 4, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.gridtalk.httpbc.common.util;

import java.util.Hashtable;

/**
 * @author Tam Wei Xiang
 * @version GT4.1.4.2
 * @since GT4.1.4.2
 */
public class LockUtil
{
  private static final LockUtil _lockUtil = new LockUtil();
  private Hashtable<String, String> _lockPool = new Hashtable<String,String>();
  
  private LockUtil()
  {
    //System.out.println(Thread.currentThread().getName()+" Initialized lock util");
  }
  
  public static synchronized LockUtil getInstance()
  {
    //System.out.println(Thread.currentThread().getName()+" Get Instance");
    return _lockUtil;
  }
  
  public boolean acquireLock(String lock)
  {
    synchronized(this)
    {
      if(_lockPool.containsKey(lock))
      {
        //System.out.println(Thread.currentThread().getName()+" containing lock: "+lock+". lock not acquire");
        
        return false;
      }
      else
      {
        //System.out.println(Thread.currentThread().getName()+" not containing lock: "+lock+". lock acquire");
        
        _lockPool.put(lock, lock);
        return true;
      }
    }
  }
  
  public void releaseLock(String lock)
  {
    synchronized(this)
    {
      //System.out.println(Thread.currentThread().getName()+" releasing lock: "+lock);
      
      if(_lockPool.containsKey(lock))
      {
        _lockPool.remove(lock);
      }
    }
  }
}
