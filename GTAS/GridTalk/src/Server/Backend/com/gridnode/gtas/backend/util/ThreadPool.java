/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ThreadPool.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 26 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.backend.util;

public class ThreadPool
{
  private Queue threads = new Queue();

  public ThreadPool(int size)
  {
    for (int i = 0; i < size; i++)
    {
      ThreadPoolThread thread = new ThreadPoolThread(this);
      thread.start();
    }
  }

  public void run(Thread task)
  {
    threads.put(task);
  }

  public Thread getNextThread()
  {
    return (Thread)threads.get();
  }
}