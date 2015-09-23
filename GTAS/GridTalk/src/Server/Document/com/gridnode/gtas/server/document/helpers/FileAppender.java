/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileAppender.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Apr 15, 2005 			Mahesh             	Created
 */
package com.gridnode.gtas.server.document.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
 
public class FileAppender
{

  private static Map fileLockMap = Collections.synchronizedMap(new HashMap());
  private static Map concurrentAppendCountMap= Collections.synchronizedMap(new HashMap());

  public static void appendFile(String srcFileStr,String desFileStr) throws Exception
  {
    Object lock=addFileLock(desFileStr);
    try
    {
      synchronized(lock)
      {
        File srcFile = new File(srcFileStr);
        File desFile=new File(desFileStr);
        Logger.debug("[FileAppender.appendFile] start, srcFileStr="+srcFileStr+", desFile="+desFile+", srcFileLength="+srcFile.length()+", desFileLength="+desFile.length());
        FileOutputStream out = new FileOutputStream(desFile, true);
        try
        {
          FileInputStream in   = new FileInputStream(srcFileStr);
          byte[] buffer = new byte[16];
          int numberRead;
          while ((numberRead = in.read(buffer)) >= 0)
            out.write(buffer, 0, numberRead);
          in.close();          
        }
        finally
        {
          out.close();
        }
        Logger.debug("[FileAppender.appendFile] end, desFile="+desFile+", desFileLength="+desFile.length());        
      }
    }
    finally
    {
      releaseFileLock(desFileStr);
    }
  }

  private static synchronized Object addFileLock(String desFileStr)
  {
    Object lock=fileLockMap.get(desFileStr);
    if(lock==null)
    {
      lock=new Object();
      fileLockMap.put(desFileStr,lock);
    }
    incConcurrentAppendCount(desFileStr);
    Logger.debug("[FileAppender.addFileLock] desFileStr="+desFileStr+", concurrentCount="+getConcurrentAppendCount(desFileStr));
    return lock;
  }

  private static synchronized void releaseFileLock(String desFileStr)
  {
    decConcurrentAppendCount(desFileStr);
    int concurrentFileCount=getConcurrentAppendCount(desFileStr);
    if(concurrentFileCount<=0)
    {
      fileLockMap.remove(desFileStr);
    }
    Logger.debug("[FileAppender.releaseFileLock] desFileStr="+desFileStr+", concurrentCount="+getConcurrentAppendCount(desFileStr));
  }
  
  
  private static synchronized void incConcurrentAppendCount(String desFileStr)
  {
    Integer countObj = (Integer)concurrentAppendCountMap.get(desFileStr);
    if(countObj==null)
    {
      countObj=new Integer(0);
    }
    countObj=new Integer(countObj.intValue()+1);
    concurrentAppendCountMap.put(desFileStr,countObj);
  }

  private static synchronized void decConcurrentAppendCount(String desFileStr)
  {
    Integer countObj = (Integer)concurrentAppendCountMap.get(desFileStr);
    if(countObj!=null)
    {
      countObj=new Integer(countObj.intValue()-1);
      if(countObj.intValue()>0)
        concurrentAppendCountMap.put(desFileStr,countObj);
      else 
        concurrentAppendCountMap.remove(desFileStr);
    }
  }

  private static synchronized int getConcurrentAppendCount(String desFileStr)
  {
    Integer conCount =(Integer)concurrentAppendCountMap.get(desFileStr);
    if(conCount!=null) 
      return conCount.intValue();
    return 0;
  }
  
}
