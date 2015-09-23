package com.gridnode.helpmaker;

/**
 * Title:        Help Maker
 * Description:
 * Copyright:    Copyright (c) 2003
 * Company:      GridNode
 * @author Ferry
 * @version 1.0
 */
import java.io.*;
import javax.swing.*;

/**
 * A singleton class to log error messages to a log file and to the pane displayed to user.
 */
public class Logger
{

  private static JTextPane out = new JTextPane();
  private static BufferedWriter buf;

  public Logger()
  {
  }

  //sets the logger output
  public static void setLogger(JTextPane output)
  {
    out = output;
  }

  // writes a message to the output and to the log file
  public static void write(String msg)
  {
    out.setText(msg);
    System.out.println("[LOG] " + msg);
    try
    {
      buf = new BufferedWriter(new FileWriter("log.txt", true));
      buf.write(msg);
      buf.newLine();
      buf.close();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
