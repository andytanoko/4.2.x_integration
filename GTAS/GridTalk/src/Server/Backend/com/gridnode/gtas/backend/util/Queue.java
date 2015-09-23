/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Queue.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 26 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.backend.util;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Queue implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2843007056551384355L;
	private List _list = new ArrayList();

  public synchronized void put(Thread thread)
  {
    _list.add(thread);
    notify();
  }

  public synchronized Thread get()
  {
    while (_list.isEmpty())
    {
      try
      {
        wait();
      }
      catch (InterruptedException ex)
      {

      }
    }
    return (Thread)_list.remove(0);
  }

}