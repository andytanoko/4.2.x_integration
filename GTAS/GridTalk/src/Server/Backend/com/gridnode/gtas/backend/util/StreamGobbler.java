/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: StreamGobbler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 19 2002    Koh Han Sing        Port to GTAS
 */
package com.gridnode.gtas.backend.util;

import java.io.*;

public class StreamGobbler extends Thread
{
  InputStream is;
  String type;

  public StreamGobbler(InputStream is, String type)
  {
    this.is = is;
    this.type = type;
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
        Log.log("StreamGobbler", type + ">" + line);
//        System.out.println(type + ">" + line);
      }
    }
    catch (IOException ioe)
    {
      ioe.printStackTrace();
    }
  }
}
