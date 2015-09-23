/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommandExecutor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 16 2002    Koh Han Sing        Create
 * Dec 19 2002    Koh Han Sing        Port to GTAS
 */
package com.gridnode.gtas.backend.util;

import java.util.ArrayList;

public class CommandExecutor
{

  public CommandExecutor()
  {
  }

  public static int execute(String commandLine, ArrayList params)
  {
    try
    {
      String osName = System.getProperty("os.name" );
      ArrayList temp = new ArrayList();
      if(osName.equals("Windows NT"))
      {
        temp.add("cmd.exe");
        temp.add("/C");
        temp.add(commandLine);
      }
      else if(osName.equals("Windows 95") || osName.equals("Windows 98"))
      {
        temp.add("command.com");
        temp.add("/C");
        temp.add(commandLine);
      }
      else
      {
        temp.add(commandLine);
      }

      for (int i = 0; i < params.size(); i++)
      {
        temp.add(params.get(i));
      }

      int size = temp.size();
      String[] cmd = new String[size];
      for (int i = 0; i < size; i++)
      {
        cmd[i] = (String)temp.get(i);
      }
      Runtime runtime = Runtime.getRuntime();
      Process proc = runtime.exec(cmd);
      StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR");
      StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT");
      errorGobbler.start();
      outputGobbler.start();
      return proc.waitFor();

    }
    catch(Exception e)
    {
      e.printStackTrace(System.out);
      return -1;
    }
  }
}