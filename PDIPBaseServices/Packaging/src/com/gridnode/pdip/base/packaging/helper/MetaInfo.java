/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MetaInfo.java
 *
 ****************************************************************************
 * Date           Author                      Changes
 ****************************************************************************
 * Jan 30 2003    Goh Kan Mun                 Created
 * Mar 21 2003    Goh Kan Mun                 Make the file splitting process works.
 * Apr 03 2003    Goh Kan Mun                 Modified - Add originalData field to store
 *                                                       the BL data so as to allow to send
 *                                                       the next 10 packets
 *                                            Modified - Add processing and processingTime
 *                                                       fields to use for preventing
 *                                                       concurrency problem.
 *                                            Modified - Add timeLastAction field to keep track
 *                                                       of last processing time and method to
 *                                                       clear expired processing MetaInfo in memory.
 *                                                       This method can be used by timer to clear
 *                                                       MetaInfo in memory in future.
 */
package com.gridnode.pdip.base.packaging.helper;

import com.gridnode.pdip.base.packaging.helper.FileHelper;
import com.gridnode.pdip.base.packaging.helper.IPackagingConstants;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.util.TimeUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * This class is used to store the information of the split files operation.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class MetaInfo
{
  private final static String CLASS_NAME = "MetaInfo";
  private final static String META_INFO_FILENAME = "metaInfo.txt";
  private final static String DELIMITER = ",";
  private final static String NULL_DELIMITER = "NULL+-*/";

  private static Hashtable allMetaInfo = new Hashtable();

  private Hashtable _blocksReceived = new Hashtable();
  private String _fileId = "";
  private String _transId = "";
  private String _originalEventId = "";
  private int _blockSize = -1;
  private int _thresholdSize = -1;
  private int _totalBlocks = -1;
  private int _lastBlockSize = -1;
  private int _lastBlockSent = 0;
  private int _lastBlock = -1;
  private String _subPath = "";
  private Hashtable _blockFileName = new Hashtable();
  private String _timeCreated = "";
  private long _timeLastAction = 0l;
  private String _GNCI = "";
  private boolean _finishedOnce = false;
  private String[] _originalData = null;
  private transient boolean _processing = false;
  private transient long _processingTime = 0l;

  /**
   * This method is used to retrieve metaInfo during sending.
   */
  public static synchronized MetaInfo retrieveMetaInfo(String fileId, int blockSize,
         int thresholdSize, String transId,
         String originalEventId, int totalBlocks, int lastBlockSize)
         throws FileAccessException, ApplicationException
  {
    if (fileId == null)
      throw new ApplicationException("FileId cannot null!");
    if (transId == null)
      throw new ApplicationException("Transaction Id cannot null!");
    if (originalEventId == null)
      throw new ApplicationException("Original event Id cannot null!");
    if (allMetaInfo.containsKey(fileId))
    {
      MetaInfo meta = (MetaInfo) allMetaInfo.get(fileId);
      meta.checkMetaInfo(fileId,
                         blockSize,
                         thresholdSize,
                         transId,
                         originalEventId,
                         totalBlocks,
                         lastBlockSize);
      return meta;
    }
    else
    {
      MetaInfo meta = new MetaInfo(fileId,
                                   blockSize,
                                   thresholdSize,
                                   transId,
                                   originalEventId,
                                   totalBlocks,
                                   lastBlockSize);
      allMetaInfo.put(fileId, meta);
      return meta;
    }
  }

  /**
   * This method is used to retrieve metaInfo when receiving file.
   */
  public static synchronized MetaInfo retrieveMetaInfo(String fileId, int totalBlocks)
         throws FileAccessException, ApplicationException
  {
    if (fileId == null)
      throw new ApplicationException("FileId cannot null!");
    if (allMetaInfo.containsKey(fileId))
    {
      MetaInfo meta = (MetaInfo) allMetaInfo.get(fileId);
      meta.checkMetaInfo(fileId);
      if (meta.getTotalBlocks() < totalBlocks)
        meta.setTotalBlocks(totalBlocks);
      return meta;
    }
    else
    {
      MetaInfo meta = new MetaInfo(fileId, totalBlocks);
      allMetaInfo.put(fileId, meta);
      return meta;
    }
  }

  /**
   * This method is used to retrieve metaInfo when receiving acknowledgement
   * and when sending Acknowledgement..
   */
  public static synchronized MetaInfo retrieveMetaInfo(String fileId)
         throws FileAccessException, ApplicationException
  {
    if (fileId == null)
      throw new ApplicationException("FileId cannot null!");
    if (allMetaInfo.containsKey(fileId))
      return (MetaInfo) allMetaInfo.get(fileId);
    else
    {
      MetaInfo meta = new MetaInfo(fileId);
      allMetaInfo.put(fileId, meta);
      return meta;
    }
  }

  public static synchronized void clearAllMetaInfoInMem()
  {
    allMetaInfo.clear();
  }

  public static synchronized void clearExpiredMetaInfoInMem()
  {
    Enumeration fileIdList = allMetaInfo.keys();
    Vector expiredFileIdList = new Vector();
    MetaInfo metaInfo = null;
    String fileId = null;
    while (fileIdList.hasMoreElements())
    {
      fileId = (String) fileIdList.nextElement();
      metaInfo = (MetaInfo) allMetaInfo.get(fileId);
      if ((metaInfo.getTimeLastAction() + 300000) < TimeUtil.localToUtc())
        expiredFileIdList.add(fileId);
    }
    for (int i = 0; i < expiredFileIdList.size(); i++)
    {
      fileId = (String) expiredFileIdList.get(i);
      removeMetaInfoInMem(fileId);
    }
  }

  private static synchronized void removeMetaInfoInMem(String fileId)
  {
    if (fileId == null)
      return;
    allMetaInfo.remove(fileId);
  }

  public static synchronized void removeMetaInfo(String fileId) throws FileAccessException
  {
    if (fileId == null)
      return;
    removeMetaInfoInMem(fileId);
    if (FileHelper.exist(IPackagingConstants.PACKAGING_PATH,
                         getSubPath(fileId),
                         META_INFO_FILENAME))
      FileHelper.delete(IPackagingConstants.PACKAGING_PATH,
                        getSubPath(fileId),
                        META_INFO_FILENAME);
  }

  /**
   * This Constructor is used to create metaInfo during sending.
   */
  private MetaInfo(String fileId, int blockSize, int thresholdSize, String transId,
          String originalEventId, int totalBlocks, int lastBlockSize)
          throws FileAccessException, ApplicationException
  {
    _subPath = getSubPath(fileId);

    if (FileHelper.exist(IPackagingConstants.PACKAGING_PATH, _subPath, META_INFO_FILENAME))
    {
      getMetaInfoFromFile(fileId);
      checkMetaInfo(fileId,
                    blockSize,
                    thresholdSize,
                    transId,
                    originalEventId,
                    totalBlocks,
                    lastBlockSize
                    );
    }
    else
    {
      _fileId = fileId;
      _transId = transId;
      _blockSize = blockSize;
      _thresholdSize = thresholdSize;
      _originalEventId = originalEventId;
      _totalBlocks = totalBlocks;
      _lastBlockSize = lastBlockSize;
      createMetaInfo();
      storeMetaInfoIntoFile();
    }
  }

  /**
   * This Constructor is used to create metaInfo when receiving.
   */
  private MetaInfo(String fileId, int totalBlocks)
         throws FileAccessException, ApplicationException
  {
    _subPath = getSubPath(fileId);
    if (FileHelper.exist(IPackagingConstants.PACKAGING_PATH, _subPath, META_INFO_FILENAME))
    {
      getMetaInfoFromFile(fileId);
      checkMetaInfo(fileId);
      if (_totalBlocks < totalBlocks)
      {
        _totalBlocks = totalBlocks; //new total Block value
        storeMetaInfoIntoFile();
      }
    }
    else
    {
      _fileId = fileId;
      createMetaInfo();
      if (_totalBlocks < totalBlocks)
        _totalBlocks = totalBlocks;
      storeMetaInfoIntoFile();
    }
  }

  /**
   * This Constructor is used to create metaInfo when receiving acknowledgement.
   */
  private MetaInfo(String fileId) throws FileAccessException
  {
    _subPath = getSubPath(fileId);
    if (FileHelper.exist(IPackagingConstants.PACKAGING_PATH, _subPath, META_INFO_FILENAME))
      getMetaInfoFromFile(fileId);
    else
      throw new FileAccessException("Meta Info file not found!");
  }

  /**
   * This method is initialize the meta info during sending. It calculates the total
   * block size and the last block size and initialize the block filenames and the
   * block received status. It also stores the UTC time that the meta info is created.
   */
  private synchronized void createMetaInfo()
          throws FileAccessException
  {
    initBlockReceived(_totalBlocks);
    _lastBlock = _totalBlocks;
    _timeCreated = TimeUtil.localToUtcTimestamp().toString();
  }

  private synchronized void storeMetaInfoIntoFile() throws FileAccessException
  {
    _timeLastAction = TimeUtil.localToUtc();
    File tmpFile = new File(META_INFO_FILENAME);
    FileWriter file = null;
    BufferedWriter bw = null;
    PrintWriter pw = null;
    try
    {
      file = new FileWriter(tmpFile);
      bw = new BufferedWriter(file);
      pw = new PrintWriter(bw);
      pw.println(_blockSize);
      pw.println(parseBlockReceivedToString());
      pw.println(_totalBlocks);
      pw.println(_fileId);
      pw.println(_lastBlock);
      pw.println(_lastBlockSent);
      pw.println(_lastBlockSize);
      pw.println(_subPath);
      pw.println(_thresholdSize);
      pw.println(_transId);
      pw.println(parseBlockFilenameToString());
      pw.println(_timeCreated);
      pw.println(_originalEventId);
      pw.println(_GNCI);
      pw.println(_finishedOnce);
      int originalDataLen = (_originalData == null) ? -1 : _originalData.length;
      pw.println(originalDataLen);
      for (int i = 0; i < originalDataLen; i++)
      {
        if (_originalData[i] != null)
          pw.println(_originalData[i]);
        else
          pw.println(NULL_DELIMITER);
      }
      pw.println(_timeLastAction);
      close(pw);
      if (FileHelper.exist(IPackagingConstants.PACKAGING_PATH,
                           _subPath,
                           META_INFO_FILENAME))
        FileHelper.replace(IPackagingConstants.PACKAGING_PATH,
                           _subPath,
                           META_INFO_FILENAME,
                           tmpFile);
      else
        FileHelper.create(IPackagingConstants.PACKAGING_PATH,
                          _subPath,
                          META_INFO_FILENAME,
                          tmpFile);
      tmpFile.delete();
    }
    catch (IOException ioe)
    {
      throw new FileAccessException("Unable to store meta info to file.");
    }
    finally
    {
      close(pw);
    }
  }

  private synchronized void getMetaInfoFromFile(String fileId) throws FileAccessException
  {
    //String[] metaInfo = new String[6]; 
    File fileDesc = null;
    FileReader reader = null;
    BufferedReader breader = null;
    try
    {
      fileDesc = FileHelper.getFile(IPackagingConstants.PACKAGING_PATH,
                                    getSubPath(fileId),
                                    META_INFO_FILENAME);
      reader = new FileReader(fileDesc);
      breader = new BufferedReader(reader);

      _blockSize = Integer.parseInt(breader.readLine());
      parseStringToBlockReceived(breader.readLine());
      _totalBlocks = Integer.parseInt(breader.readLine());
      _fileId = breader.readLine();
      _lastBlock = Integer.parseInt(breader.readLine());
      _lastBlockSent = Integer.parseInt(breader.readLine());
      _lastBlockSize = Integer.parseInt(breader.readLine());
      _subPath = breader.readLine();
      _thresholdSize = Integer.parseInt(breader.readLine());
      _transId = breader.readLine();
      parseStringToBlockFilename(breader.readLine());
      _timeCreated = breader.readLine();
      _originalEventId = breader.readLine();
      _GNCI = breader.readLine();
      _finishedOnce = Boolean.valueOf(breader.readLine()).booleanValue();

      int originalDataLen = Integer.parseInt(breader.readLine());
      if (originalDataLen < 0)
        _originalData = null;
      else
      {
        _originalData = new String[originalDataLen];
        for (int i = 0; i < originalDataLen; i++)
        {
          _originalData[i] = breader.readLine();
          if (NULL_DELIMITER.equals(_originalData[i]))
            _originalData[i] = null;
        }
      }
      _timeLastAction = Long.valueOf(breader.readLine()).longValue();
    }
    catch (IOException ioe)
    {
      throw new FileAccessException("Unable to read meta info from file.", ioe);
    }
    finally
    {
      close(breader);
    }
  }

  private void close(Reader r)
  {
    try
    {
      if (r != null)
        r.close();
    }
    catch (IOException ioe)
    {
      PackagingLogger.warnLog(CLASS_NAME, "close", "Unable to close Reader", ioe);
    }
  }

  private void close(Writer w)
  {
    try
    {
      if (w != null)
      {
        w.flush();
        w.close();
      }
    }
    catch (IOException ioe)
    {
      PackagingLogger.warnLog(CLASS_NAME, "close", "Unable to close Reader", ioe);
    }
  }

  private void checkMetaInfo(String fileId, int blockSize, int thresholdSize,
          String transactionId, String originalEventId, int totalBlocks,
          int lastBlockSize)
          throws ApplicationException
  {
    if ((fileId.equals(_fileId)) &&
        (blockSize == _blockSize) &&
        (thresholdSize == _thresholdSize) &&
        (originalEventId == _originalEventId) &&
        (totalBlocks == _totalBlocks) &&
        (lastBlockSize == _lastBlockSize)
       )
    {
      if (_transId.equals(transactionId))
        return;
      else
      {
        PackagingLogger.infoLog(CLASS_NAME, "checkMetaInfo", "new transaction Id, new session");
        _transId = transactionId;
        resetBlockReceivedStatus(); //Reset any previous sent.
        _finishedOnce = false; //Reset any previous sent.
      }
    }
    else
    {
      throw new ApplicationException("Meta Info changed.");
    }
  }

  private void checkMetaInfo(String fileId)
          throws ApplicationException
  {
    if ((fileId.equals(_fileId)))
      return;
    else
    {
      throw new ApplicationException("Meta Info changed.");
    }
  }

  private String parseBlockReceivedToString()
  {
    StringBuffer sb = new StringBuffer();
    synchronized (_blocksReceived)
    {
      Enumeration keys = _blocksReceived.keys();
      boolean first = true;
      Integer key = null;
      Boolean status = null;
      while (keys.hasMoreElements())
      {
        key = (Integer) keys.nextElement();
        status = (Boolean) _blocksReceived.get(key);
        if (first)
          first = false;
        else
          sb.append(DELIMITER);
        sb.append(key.intValue());
        sb.append(DELIMITER);
        sb.append(status.booleanValue());
      }
    }
    return sb.toString();
  }

  private void parseStringToBlockReceived(String list)
  {
    synchronized (_blocksReceived)
    {
      StringTokenizer st = new StringTokenizer(list, DELIMITER, false);
      Integer key = null;
      Boolean status = null;
      while (st.hasMoreTokens())
      {
        key = Integer.valueOf(st.nextToken());
        status = Boolean.valueOf(st.nextToken());
        _blocksReceived.put(key, status);
      }
    }
  }

  private String parseBlockFilenameToString()
  {
    StringBuffer sb = new StringBuffer();
    synchronized (_blockFileName)
    {
      Enumeration keys = _blockFileName.keys();
      boolean first = true;
      Integer key = null;
      String filename = null;
      while (keys.hasMoreElements())
      {
        key = (Integer) keys.nextElement();
        filename = (String) _blockFileName.get(key);
        if (first)
          first = false;
        else
          sb.append(DELIMITER);
        sb.append(key.intValue());
        sb.append(DELIMITER);
        sb.append(filename);
      }
    }
    return sb.toString();
  }

  private void parseStringToBlockFilename(String list)
  {
    synchronized (_blockFileName)
    {
      StringTokenizer st = new StringTokenizer(list, DELIMITER, false);
      Integer key = null;
      String filename = null;
      while (st.hasMoreTokens())
      {
        key = Integer.valueOf(st.nextToken());
        filename = st.nextToken();
        _blockFileName.put(key, filename);
      }
    }
  }

  public int getBlockSize(int blockNo)
  {
    if (blockNo != _lastBlock)
      return _blockSize;
    else
      return _lastBlockSize;
  }

  public void setBlocksFilename(String[] filename) throws FileAccessException
  {
    for (int i = 0; i < filename.length; i++)
      _blockFileName.put(new Integer(i), filename[i]);
    storeMetaInfoIntoFile();
  }

  public void setBlockFilename(int blockNo, String filename) throws FileAccessException
  {
    _blockFileName.put(new Integer(blockNo), filename);
    storeMetaInfoIntoFile();
  }

  public String getBlockFilename(int blockNo)
  {
    return (String)_blockFileName.get(new Integer(blockNo));
  }

  public String getSubPath()
  {
    return _subPath;
  }

  public String getTransId()
  {
    return _transId;
  }

  public int getTotalBlocks()
  {
    return _totalBlocks;
  }

  public boolean checkBlockReceivedStatus(int blockNo)
  {
    synchronized (_blocksReceived)
    {
      Integer blockNumber = new Integer(blockNo);
      if (!_blocksReceived.containsKey(blockNumber))
        return false;
      else
        return ((Boolean)_blocksReceived.get(blockNumber)).booleanValue();
    }
  }

  public synchronized boolean checkAllBlocksReceived()
  {
    synchronized (_blocksReceived)
    {
      if (_totalBlocks < 0)
      {
        PackagingLogger.debugLog(CLASS_NAME,
                                 "checkAllBlocksReceived",
                                 "totalBlocks < 0 : " + _totalBlocks);
        return false;
      }
      else if (_blocksReceived.size() < _totalBlocks)
      {
        PackagingLogger.debugLog(CLASS_NAME,
                "checkAllBlocksReceived",
                "_blocksReceived.size=" + _blocksReceived.size()
                + " , _totalBlocks=" + _totalBlocks);
        return false;
      }
      else
      {
        Enumeration elements = _blocksReceived.elements();
        while (elements.hasMoreElements())
        {
          if (!((Boolean)elements.nextElement()).booleanValue())
          {
            PackagingLogger.infoLog(CLASS_NAME,
                                    "checkAllBlocksReceived",
                                    "element = false!");
            return false;
          }
        }
        PackagingLogger.infoLog(CLASS_NAME, "checkAllBlocksReceived", "return true!!!!!");
        return true;
      }
    }
  }

  public int[] getAllBlocksReceived()
  {
    synchronized (_blocksReceived)
    {
      Vector v = new Vector();
      //int count = 0;
      Enumeration keys = _blocksReceived.keys();
      Integer key = null;
      Boolean status = null;
      while (keys.hasMoreElements())
      {
        key = (Integer) keys.nextElement();
        status = (Boolean) _blocksReceived.get(key);
        if (status.booleanValue())
          v.add(key);
      }
      int[] receivedBlock = new int[v.size()];
      for (int i = 0; i < v.size(); i++)
        receivedBlock[i] = ((Integer) v.get(i)).intValue();
      return receivedBlock;
    }
  }

  public synchronized void setBlockReceivedStatus(int[] blockNo) throws FileAccessException
  {
    if (blockNo == null || blockNo.length < 1)
      return;
    for (int i = 0; i < blockNo.length; i++)
      _blocksReceived.put(new Integer(blockNo[i]), Boolean.TRUE);
    _finishedOnce = false; // Reset to allow send again if not all packet received.
    storeMetaInfoIntoFile();
  }

  public synchronized void setBlockReceivedStatus(int blockNo, String filename)
         throws FileAccessException
  {
    Integer blockNumber = new Integer(blockNo);
    _blocksReceived.put(blockNumber, Boolean.TRUE);
    _blockFileName.put(blockNumber, filename);
    storeMetaInfoIntoFile();
  }

  public synchronized void unsetBlockReceivedStatus(int blockNo) throws FileAccessException
  {
    _blocksReceived.put(new Integer(blockNo), Boolean.FALSE);
    storeMetaInfoIntoFile();
  }

  public synchronized void resetBlockReceivedStatus() throws FileAccessException
  {
    initBlockReceived(_totalBlocks);
    storeMetaInfoIntoFile();
  }

  private void initBlockReceived(int length)
  {
    synchronized (_blocksReceived)
    {
      _blocksReceived.clear();
      for (int i = 0; i < length; i++)
        _blocksReceived.put((new Integer(i)), Boolean.FALSE);
    }
  }

  public synchronized int[] getBlocksNotReceived(int maxBlocks) throws FileAccessException
  {
    int noOfBlock = 0;
    int startBlock = _lastBlockSent;
    int currentBlock = _lastBlockSent;
    Vector v = new Vector();
    do
    {
      if (currentBlock >= _totalBlocks)
      {
        _finishedOnce = true; // Finish 1 round of send.
        currentBlock = 0;
        continue;
      }
      if (!checkBlockReceivedStatus(currentBlock))
      {
        v.add(new Integer(currentBlock));
        noOfBlock++;
      }
      currentBlock++;
    }
    while ((maxBlocks > noOfBlock) && (currentBlock != startBlock) && _finishedOnce == false);
    _lastBlockSent = currentBlock;
    storeMetaInfoIntoFile();

    if (v.size() > 0)
    {
      int[] block = new int[v.size()];
      for (int i = 0; i < v.size(); i++)
        block[i] = ((Integer) v.get(i)).intValue();
      return block;
    }
    else
    {
      return null;
    }
  }

  public int getNoOfBlocksReceived()
  {
    return _blocksReceived.size();
  }

  public String getTimeCreated()
  {
    return _timeCreated;
  }

  private static String getSubPath(String fileId)
  {
    return fileId + File.separator;
  }

  public String getOriginalEventId()
  {
    return _originalEventId;
  }

  public void setOriginalEventId(String originalEventId) throws FileAccessException
  {
    if (originalEventId == null || originalEventId.equals("") || originalEventId.equals(_originalEventId))
      return;
    else
      _originalEventId = originalEventId;
    storeMetaInfoIntoFile();
  }

  public String getGNCI()
  {
    return _GNCI;
  }

  public void setGNCI(String GNCI) throws FileAccessException
  {
    if (GNCI == null || GNCI.equals(_GNCI))
      return;
    _GNCI = GNCI;
    storeMetaInfoIntoFile();
  }

  protected void setTotalBlocks(int totalBlocks) throws FileAccessException
  {
    _totalBlocks = totalBlocks;
    storeMetaInfoIntoFile();
  }

  public void setOriginalData(String[] originalData) throws FileAccessException
  {
    if (originalData == null)
      return;
    else
      _originalData = originalData;
    storeMetaInfoIntoFile();
  }

  public String[] getOriginalData()
  {
    return _originalData;
  }

  public synchronized boolean isProcessing()
  {
    if (_processing)
    {
      if (_processingTime > TimeUtil.localToUtc())
        return true;
      else
       _processing = false;
    }
    return _processing;
  }

  public synchronized void setProcessing(boolean flag)
  {
    _processingTime = TimeUtil.localToUtc() + 60000;
    _processing = true;
  }

  public long getTimeLastAction()
  {
    return _timeLastAction;
  }

}