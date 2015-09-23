/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: StreamGobbler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 14 2003    Neo Sok Lay         Created.
 */
package com.gridnode.util.io;

import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.StringWriter;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Gobblers the output from an external program.
 *
 * @author Neo Sok Lay
 */
public class StreamGobbler extends Thread
{
  private InputStream is;
  private String type;
  private CacheDataWriter _cacheWriter;
  private boolean _readEnded = false;

  /**
   * Constructs a StreamGlobber.
   *
   * @param is The InputStream to grab the bytes from.
   * @param type A customizable string to identify bytes from the specified
   * InputStream.
   */
  public StreamGobbler(InputStream is, String type)
  {
    this.is = is;
    this.type = type;
  }

  /**
   * Constructs a StreamGlobber.
   *
   * @param is The InputStream to grab the bytes from.
   * @param type A customizable string to identify bytes from the specified
   * InputStream.
   * @param cache <b>true</b> to cache the bytes read from the input stream.
   */
  public StreamGobbler(InputStream is, String type, boolean cache)
  {
    this.is = is;
    this.type = type;
    if (cache)
      _cacheWriter = new CacheDataWriter();
  }

  public void run()
  {
    try
    {
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      String line=null;
      while ( (line = br.readLine()) != null)
      {
        if (_cacheWriter != null)
          _cacheWriter.addToCache(line);
        System.out.println(type + ">" + line);
      }
      _readEnded = true;
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
    finally
    {
      if (_cacheWriter != null)
         _cacheWriter.close();
    }
  }

  /**
   * Get the cached data gobbled from the InputStream.
   *
   * @return Any data read from the InputStream, or <b>null</b> if cache is
   * not turned on.
   */
  public String getCachedGobbledData()
  {
    if (_cacheWriter != null)
    {
      while (!_readEnded)
      {
      }
      return _cacheWriter.getCachedData();
    }
    return null;
    //return (_cacheWriter != null ? _cacheWriter.getCachedData() : null);
  }

  private class CacheDataWriter
  {
    private PrintWriter _pw;
    private StringWriter _sw;

    CacheDataWriter()
    {
      _sw = new StringWriter();
      _pw = new PrintWriter(new BufferedWriter(_sw), true);
    }

    void addToCache(String data)
    {
      _pw.println(data);
    }

    String getCachedData()
    {
      _pw.flush();
      return _sw.toString();
    }

    void close()
    {
      _pw.close();
    }
  }
}