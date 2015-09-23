/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Locker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 16 2002    Koh Han Sing        Create
 */
package com.gridnode.gtas.backend.sender;

import com.gridnode.gtas.backend.util.Log;

import java.io.*;

public class Locker
{
  private static final String CATEGORY = "Locker";
  public static final int UNLOCKED_SUCCESSFUL = 1;
  public static final int UNLOCKED_UNSUCCESSFUL = 2;
  public static final int WRONG_ID = 3;

  public Locker()
  {
  }

  public static boolean lock(File directory, String filename, long timestamp)
  {
    try
    {
      String path = directory.getAbsolutePath();
      File lockFile = new File(path, filename);
      boolean locked = lockFile.createNewFile();
      if (locked)
      {
        writeTimestamp(lockFile, timestamp);
        lockFile.deleteOnExit();
      }
      return locked;
    }
    catch (Exception ex)
    {
      return false;
    }
  }

  public static boolean isLockExpired(File directory, String filename,
    long timestamp, long timeout)
  {
    try
    {
      boolean expired = false;
      String path = directory.getAbsolutePath();
      File lockFile = new File(path, filename);
      long lastTimestamp = readTimestamp(lockFile);
      log("timestamp = " + timestamp);
      log("lastTimestamp = " + lastTimestamp);
      log("timeout = " + timeout);
      if (timestamp - lastTimestamp > timeout)
      {
        log("TIMEOUT");
        lockFile.delete();
        lock(directory, filename, timestamp);
        expired = true;
      }
      return expired;
    }
    catch (Exception ex)
    {
      return false;
    }
  }

  private static void writeTimestamp(File lockFile, long timestamp)
    throws Exception
  {
    FileOutputStream fos = new FileOutputStream(lockFile);
    DataOutputStream out = new DataOutputStream(fos);
    out.writeLong(timestamp);
    out.flush();
    out.close();
    fos.close();
  }

  private static long readTimestamp(File lockFile)
    throws Exception
  {
    FileInputStream fis = new FileInputStream(lockFile);
    DataInputStream in = new DataInputStream(fis);
    long lastTimestamp = in.readLong();
    in.close();
    fis.close();
    return lastTimestamp;
  }

//  public static int unLock(File directory, String filename, long id)
//  {
//    try
//    {
//      int status = UNLOCKED_UNSUCCESSFUL;
//      String path = directory.getAbsolutePath();
//      File lockFile = new File(path, filename);
//      if (lockFile.exists())
//      {
//        FileInputStream fis = new FileInputStream(lockFile);
//        DataInputStream in = new DataInputStream(fis);
//        long lockID = in.readLong();
//        in.close();
//        fis.close();
//        System.out.println("id = " + id);
//        System.out.println("ID = " + lockID);
//        if (lockID == id)
//        {
//          System.out.println("ID correct");
//          boolean deleted = lockFile.delete();
//          if (!deleted)
//          {
//            status = UNLOCKED_UNSUCCESSFUL;
//          }
//          else
//          {
//            status = UNLOCKED_SUCCESSFUL;
//          }
//        }
//        else
//        {
//          status = WRONG_ID;
//        }
//      }
//      return status;
//    }
//    catch (Exception ex)
//    {
//      ex.printStackTrace();
//      return UNLOCKED_UNSUCCESSFUL;
//    }
//  }

  private static void log(String msg, Exception e)
  {
    Log.err(CATEGORY, msg, e);
//    System.out.println(msg);
  }

  private static void log(String msg)
  {
    Log.log(CATEGORY, msg);
//    System.out.println("[sender] " + msg);
  }
}