/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PackagingInfo
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 16-OCT-2002    Jagadeesh           Created.
 * 04-OCT-2002    Jagadeesh           Modified - To return Hashtable fro envelopeHeader.
 *                                               Add AdditionalHeader Field to represent
 *                                               additonalHeader used by Specific Pkging
 *                                               Services.
 *
 * 05-OCT-2002    Jagadeesh           Modified -  Create Constructor to accept Hashtable of
 *                                                packaged header in Hashtable.
 * 17 Jan 2003    Goh Kan Mun         Modified - Added fields and methods for split variables.
 *                                               Added fields and methods for packaged split data content.
 *                                               Added fields and methods for packaged split file content.
 *                                               Added fields and methods for file descriptor.
 *                                               Removed setters for processId and isZip.
 * 21 Jan 2003    Goh Kan Mun         Modified - Added isSplit and isRelay fields at constructor.
 *                                             - Add fields and methods for allPacketsProcessed, resend,
 *                                               resendAll, isRelay, gnci, moreBlocksToProcess,
 *                                               noOfBlocksProcessed, packagedEventId, isSplitAck
 *                                               to store data for file splitting.
 * 25 Sep 2003    Jagadeesh           Modified - Added setter methods for Zip and PackagingType attributes.
 *
 * 06 OCT 2003		Jagadeesh						Modified - Added header attributes required to support SOA-HTTP.
 * Oct 29 2003    Guo Jianyu          Added _moreHeader, a string array that hosts headers
 *                                             specific to message protocols.
 */

package com.gridnode.pdip.base.packaging.helper;

import java.io.File;
import java.util.Hashtable;

public class PackagingInfo implements IPackagingInfo
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7195648134938564309L;
	private String _eventId = null;
  private String _transactionId=null;
  private String _recepNodeId = null;
  private String _senderNodeId = null;
  private String _eventSubId = null;
  private String _fileId = null;
  private String _channelName = null;
  private File[] _payLoadToPackage = null;
  private File[] _packagedPayLoad;
  private byte[] _payLoadToUnPackage;
  private File[] _unPackagedPayLoad;
  private byte[] _packagedSplitContent;
  private String _processId;
  private boolean _isZip;
  private int _zipThreshold;
  private boolean _isSplit;
  private int _splitThreshold;
  private int _splitSize;
  private String _type=null; 
  //private String[] _envelopeHeader = {};
  private String[] _defaultUnPackagedHeader = {};
  private Hashtable _envlHeader=null;
  private Hashtable _unPackagedHeader = null;
  private Hashtable _additionalHeader = null;
  private String _fileDesc = null;
  private String[] _dataContent = null;
  private String[] _packagedDataContent = null;
  private boolean _allPacketsProcessed = false;
  private boolean _resend = false;
  private boolean _resendAll = false;
  private boolean _isRelay = false;
  private String _gnci = null;
  private boolean _moreBlocksToProcess = false;
  private int _noOfBlocksProcessed = 0;
  private String _packagedEventId = null;
  private boolean _isSplitAck = false;

  /*Attributes required for SOAP-HTTP */

  private String _senderUUID;
  private String _senderQueryURL;
  private String _recipientUUID;
  private String _recipientQueryURL;
  private String _docType;


  private String[] _moreHeader = {};

  public PackagingInfo(String type,
                       int zipThreshold,
                       boolean isZip,
                       String eventId,
                       String transactionId,
                       String recpnodeId,
                       String sendernodeId,
                       String eventSubId,
                       String processId,
                       String fileId,
                       String channelName,
                       int splitThreshold,
                       int splitSize,
                       boolean isSplit,
                       boolean isRelay,
                       String	senderUUID,
                       String	senderQueryURL,
                       String	recipientUUID,
                       String	recipientQueryURL,
                       String	docType)
  {
     _type = type;
    _zipThreshold = zipThreshold;
    _isZip = isZip;
    _eventId = eventId;
    _transactionId = transactionId;
    _recepNodeId = recpnodeId;
    _senderNodeId = sendernodeId;
    _eventSubId = eventSubId;
    _processId = processId;
    _fileId = fileId;
    _channelName = channelName;
    _splitThreshold = splitThreshold;
    _splitSize = splitSize;
    _isSplit = isSplit;
    _isRelay = isRelay;
    _senderUUID = senderUUID;
    _senderQueryURL = senderQueryURL;
    _recipientUUID = recipientUUID;
    _recipientQueryURL = recipientQueryURL;
    _docType = docType;
  }


  public PackagingInfo(String type,
                       int zipThreshold,
                       boolean isZip,
                       String fileId,
                       Hashtable unpackHeader,
                       boolean isSplit,
                       boolean isRelay
                       )
  {
    _type = type;
    _zipThreshold = zipThreshold;
    _isZip = isZip;
    _fileId = fileId;
    _unPackagedHeader = unpackHeader;
    _isSplit = isSplit;
    _isRelay = isRelay;
  }

  public PackagingInfo()
  {
  }

	public void setPackagingType(String packagingType)
	{
	  _type = packagingType;
	}

        //Added since needed by existing code.., apparently this method was deleted after rebase : 12/01/04
      public void setEnvelopeType(String envelopeType)
      {
        _type = envelopeType;
      }

	public String getPackagingType()
	{
	  return _type;
	}


	/** Getter Methods used to get    **/


	public Hashtable getUnPackagedHeader()
  {
    return _unPackagedHeader;
  }

  public String[] getDefaultUnPackagedHeader()
  {
    return _defaultUnPackagedHeader;
  }

  public String getEnvelopeType()
  {
    return _type;
  }

  public int getZipThreshold()
  {
   return _zipThreshold;
  }

  public int getSplitThreshold()
  {
   return _splitThreshold;
  }

  public int getSplitSize()
  {
   return _splitSize;
  }

  public String getEventId()
  {
    return _eventId;
  }

  public String getTransactionId()
  {
    return _transactionId;
  }

  public String getRecepientNodeId()
  {
    return _recepNodeId;
  }

  public String getSenderNodeId()
  {
    return _senderNodeId;
  }

  public String getEventSubId()
  {
    return _eventSubId;
  }

  public String getFileId()
  {
    return _fileId;
  }

  public String getChannelName()
  {
    return _channelName;
  }

  public String getProcessId()
  {
    return _processId;
  }

  public String getFileDesc()
  {
    return _fileDesc;
  }

  public String[] getMoreHeader()
  {
    return _moreHeader;
  }

  public void setFileDesc(String fileDesc)
  {
    _fileDesc = fileDesc;
  }

  public boolean isZip()
  {
    return _isZip;
  }

	public void setIsZip(boolean isZip)
	{
	  _isZip = isZip;
	}

  public boolean isSplit()
  {
    return _isSplit;
  }

  public void setAllPacketsProcessed(boolean allPacketsProcessed)
  {
    _allPacketsProcessed = allPacketsProcessed;
  }

  public boolean isAllPacketsProcessed()
  {
    return _allPacketsProcessed;
  }

  public void setResend(boolean resend)
  {
    _resend = resend;
  }

  public boolean isResend()
  {
    return _resend;
  }

  public void setResendAll(boolean resendAll)
  {
    _resendAll = resendAll;
  }

  public boolean isResendAll()
  {
    return _resendAll;
  }

  public boolean isRelay()
  {
    return _isRelay;
  }

  public boolean isSplitAck()
  {
    return _isSplitAck;
  }

  public void setGNCI(String gnci)
  {
    _gnci = gnci;
  }

  public String getGNCI()
  {
    return _gnci;
  }

  public boolean moreBlocksToProcess()
  {
    return _moreBlocksToProcess;
  }

  public void setMoreBlocksToProcess(boolean moreBlocksToProcess)
  {
    _moreBlocksToProcess = moreBlocksToProcess;
  }

  public int getNoOfBlocksProcessed()
  {
    return _noOfBlocksProcessed;
  }

  public void setNoOfBlocksProcessed(int noOfBlocksProcessed)
  {
    _noOfBlocksProcessed = noOfBlocksProcessed;
  }

/** Methods to Package/UnPackage the contents **/

  public void setIsSplitAck(boolean isSplitAck)
  {
    _isSplitAck = isSplitAck;
  }

  public void setDefaultUnPackagedHeader(String[] unpackHeader)
  {
    _defaultUnPackagedHeader = unpackHeader;
  }

  public void setEnvelopeHeader(Hashtable envlHeader)
  {
    _envlHeader = envlHeader;
  }

  public Hashtable getEnvelopeHeader()
  {
    return _envlHeader;
  }


  public void setAdditionalHeader(Hashtable addlHeader)
  {
    _additionalHeader = addlHeader;
  }

  public Hashtable getAdditionalHeader()
  {
    return _additionalHeader;
  }

  public void setPayLoadToPackage(File[] payload)
  {
    _payLoadToPackage = payload;
  }

  public File[] getPayLoadToPackage()
  {
    return _payLoadToPackage;
  }

  public File[] getPackagedPayLoad()
  {
    return _packagedPayLoad;
  }

  public void setPayLoadToUnPackage(byte[] payload)
  {
    _payLoadToUnPackage = payload;
  }

  public byte[] getPayLoadToUnPackage()
  {
    return _payLoadToUnPackage;
  }

  public File[] getUnPackagedPayLoad()
  {
    return _unPackagedPayLoad;
  }

  public String[] getDataContent()
  {
    return _dataContent;
  }

  public String[] getPackagedDataContent()
  {
    return _packagedDataContent;
  }

/*** Methods After Processing **/

  public void setPackagedEventId(String packagedEventId)
  {
    _packagedEventId = packagedEventId;
  }

  public String getPackagedEventId()
  {
    return _packagedEventId;
  }

  public void setPackagedPayLoad(File[] payload)
  {
    _packagedPayLoad = payload;
  }

  public void setUnPackagedPayLoad(File[] unpackpayload)
  {
    _unPackagedPayLoad = unpackpayload;
  }

  public void setPackagedDataContent(String[] packagedDataContent)
  {
    _packagedDataContent = packagedDataContent;
  }

  public void setDataContent(String[] dataContent)
  {
    _dataContent = dataContent;
  }

  public byte[] getPackagedSplitContent()
  {
    return _packagedSplitContent;
  }

  public void setPackagedSplitContent(byte[] packagedSplitContent)
  {
    _packagedSplitContent = packagedSplitContent;
  }

  public void setEventId(String eventId)
  {
    _eventId = eventId;
  }

  /* Attribute setter and getter methods for SOAP-HTTP requirement */

  public void setSenderUUID(String senderUUID)
  {
  	_senderUUID = senderUUID;
  }

  public void setSenderQueryURL(String senderQueryURL)
  {
  	_senderQueryURL = senderQueryURL;
  }

  public void setRecipientUUID(String recipeintQueryUUID)
  {
  	_recipientUUID = recipeintQueryUUID;
  }

  public void setRecipientQueryURL(String recipientQueryURL)
  {
  	_recipientQueryURL = recipientQueryURL;
  }

  public void setDocType(String docType)
  {
  	_docType = docType;
  }


  public String getSenderUUID()
  {
  	return _senderUUID;
  }

  public String getSenderQueryURL()
  {
  	return _senderQueryURL;
  }

  public String getRecipientUUID()
  {
  	return _recipientUUID;
  }

  public String getRecipeintQueryURL()
  {
  	return _recipientQueryURL;
  }

  public String getDocType()
  {
  	return _docType;
  }

  public void setMoreHeader(String[] moreHeader)
  {
    _moreHeader = moreHeader;
  }
}