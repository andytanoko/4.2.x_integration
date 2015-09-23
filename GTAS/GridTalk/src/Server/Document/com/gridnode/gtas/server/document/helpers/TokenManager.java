/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TokenManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 25 2002    Koh Han Sing        Created
 *
 */
package com.gridnode.gtas.server.document.helpers;

import java.util.ArrayList;
import java.util.Iterator;
 
public class TokenManager extends Thread
{
  private static TokenManager _self = null;
  private static int _maxToken = 20;
  //private static int _tokenCount = 0;
  private static ArrayList _tokens;

  private TokenManager()
  {
    _tokens = new ArrayList();
    for (long i = 1; i <= _maxToken; i++)
    {
      _tokens.add(new Token(i));
    }
  }

  public static TokenManager getInstance()
  {
    if (_self == null)
    {
      synchronized (TokenManager.class)
      {
        if (_self == null)
        {
          _self = new TokenManager();
          _self.start();
        }
      }
    }
    return _self;
  }

  public synchronized Token getToken()
  {
    while (true)
    {
      checkExpiry();

      for (Iterator i = _tokens.iterator(); i.hasNext(); )
      {
        Token token = (Token)i.next();
        if (!token.isIssued())
        {
          token.issue();
          return token;
        }
      }

      try
      {
        wait();
      }
      catch (InterruptedException ex)
      {
      }
    }
  }

//    if (_tokenCount < _maxToken)
//    {
//      System.out.println("WHAT :Token for GDoc : "+uid);
//      _tokenCount++;
//    }
//    else
//    {
//      System.out.println("WHAT :Waiting for token GDoc : "+uid);
//      try
//      {
//        _queue.add(this);
//        this.wait();
//        _tokenCount++;
//      }
//      catch (InterruptedException ex)
//      {
//        _tokenCount++;
//      }
//      System.out.println("WHAT :Token for GDoc after wait : "+uid);
//    }

  public synchronized void releaseToken(Token retToken)
  {
    for (Iterator i = _tokens.iterator(); i.hasNext(); )
    {
      Token token = (Token)i.next();
      if (token.getId() == retToken.getId())
      {
        token.returned();
        break;
      }
    }
    notify();

//    System.out.println("WHAT :Release token for GDoc : "+uid);
//    _tokenCount--;
//    if (_tokenCount < 0)
//    {
//      _tokenCount = 0;
//    }
//    Iterator i = _queue.iterator();
//    if (i.hasNext())
//    {
//      Thread t = (Thread)i.next();
//      t.notify();
//    }
  }

  private void checkExpiry()
  {
    for (Iterator i = _tokens.iterator(); i.hasNext(); )
    {
      Token token = (Token)i.next();
      if (token.isIssued())
      {
        if ((System.currentTimeMillis() - token.getTimeIssued()) > 3600000)
        {
          token.returned();
        }
      }
    }
  }

}