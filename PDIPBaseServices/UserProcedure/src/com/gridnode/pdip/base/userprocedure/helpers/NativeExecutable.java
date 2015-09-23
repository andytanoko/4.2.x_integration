/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NativeExecutable.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 02 2002    Jagadeesh               Created
 * Oct 17 2002    Ang Meng Hua            Refactor to enhance code hygiene
 */
package com.gridnode.pdip.base.userprocedure.helpers;

import java.io.*;

public class NativeExecutable
{

  public NativeExecutable()
  {
  }

  /**
   * This Method Executes a Native command.
   * Note here the streams are read in a while loop until they hit "End-Of-Stream".
   * Another eligent approch is to read this streams in a new thread (meaning
   * a new Thread for each stream to process them respectively).
   * By doing so we can free the current thread waiting.
   * @param command - Command to execute.
   * @return - exit value -a integer (Ex: 0 - Success, 2 - FileNotFound,
   * 3.Win32 Error value is - not a valid Win32 Application).
   * @throws Exception - thrown when this process cannot  execute.
   */

  public static int executeNative(String command) throws Exception
  {
    Runtime runtime = Runtime.getRuntime();
    Process process = null;
    process = runtime.exec(command);

    StreamHandler glerr = new StreamHandler(process.getErrorStream(),"ERROR");
    StreamHandler glout = new StreamHandler(process.getInputStream(),"OUTPUT");

    glerr.start();
    glout.start();

    int exitValue = process.waitFor();
    Logger.debug("Exit Value -->"+exitValue);
    return exitValue;

/*    BufferedInputStream stdout = new BufferedInputStream(process.getInputStream());
    BufferedInputStream stderr = new BufferedInputStream(process.getErrorStream());
    BufferedOutputStream stdin = new BufferedOutputStream(process.getOutputStream());
    byte [] output= new byte[10240000];
    byte [] error= new byte[10240000];
    byte [] input = new  byte[10240000];
    int availableoutput = 0;
    int availableerror=0;
    int availableinput = 0;
    int exitValue = 0;

    boolean fin = false;
    int c = 0;
    while(!fin)
    {
      availableoutput = stdout.available();
      availableinput = System.in.available();
      availableerror = stderr.available();

      if(availableoutput > 0)
      {
        c = stdout.read(output);
        if(c == -1)
          fin=true;
      }
      if(availableerror > 0)
      {
        c = stderr.read(error);
        if(c == -1)
          fin=true;
      }
      if(availableinput > 0)
      {
        c = System.in.read(input);
        if(c == -1)
          fin=true;
        else
        {
          try
          {
            stdin.write(input,0,c);
            stdin.flush();
          }
          catch(Exception e)
          {
          }
        }
      }

      try
      {
        exitValue = process.exitValue();
        fin = true;
      }
      catch(IllegalThreadStateException e)
      {
      }
    }

    process.waitFor();
    Thread.sleep(10); //wait for the native program to flush its own output buffer
    availableoutput = stdout.available();
    if(availableoutput > 0)
    {
      c = stdout.read(output);
      if(c == -1)
        fin=true;
    }
    stdin.close();
    stdout.close();
    stderr.close();

    return exitValue;
*/
  }


static class StreamHandler extends Thread
{
    InputStream is;
    String type;

    StreamHandler(InputStream is, String type)
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
                System.out.println(line);
            } catch (IOException ioe)
              {
                ioe.printStackTrace();
              }
    }
}

}