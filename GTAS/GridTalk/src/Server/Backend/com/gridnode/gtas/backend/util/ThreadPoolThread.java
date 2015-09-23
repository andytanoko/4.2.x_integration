/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ThreadPoolThread.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 26 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.backend.util;


public class ThreadPoolThread extends Thread
{
  private ThreadPool pool;

  public ThreadPoolThread(ThreadPool pool)
  {
    this.pool = pool;
  }

  public void run()
  {
    while(true)
    {
      Thread task = pool.getNextThread();
      try
      {
        task.run();
      }
      catch (Exception ex)
      {
      }
    }
  }
}