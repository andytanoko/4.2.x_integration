/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpcTransportHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 17 2003    Neo Sok Lay         Created
 * Nov 14 2003    Neo Sok Lay         Determine if continue to send the file
 *                                    base on send options. This is to limit
 *                                    the types of files being sent: Gdoc, Udoc, attachment
 *                                    Save the payload with original filename
 */
package com.gridnode.gtas.server.userprocedure.handlers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Hashtable;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.userprocedure.helpers.DocumentUtil;
import com.gridnode.gtas.server.userprocedure.helpers.Logger;
import com.gridnode.gtas.server.userprocedure.helpers.UserProcedureDelegate;
import com.gridnode.gtas.server.userprocedure.model.IUpcCommInfo;
import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.exceptions.*;
import com.gridnode.pdip.base.transport.handler.ITransportHandler;
import com.gridnode.pdip.framework.messaging.ICommonHeaders;

/**
 * Transport Handler for User Procedure Call.
 *
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class UpcTransportHandler implements ITransportHandler
{
  private static final Hashtable _track = new Hashtable();

  /**
   * Constructs an UpcTransportHandler
   */
  public UpcTransportHandler()
  {
  }

  /**
   * @see com.gridnode.pdip.base.transport.handler.ITransportHandler#send(com.gridnode.pdip.base.transport.comminfo.ICommInfo, java.util.Hashtable, java.lang.String[], byte[])
   */
  public void send(
    ICommInfo commInfo,
    Hashtable header,
    String[] dataToSend,
    byte[] fileData)
    throws
      InvalidCommInfoException,
      InvalidProtocolException,
      GNTransportException
  {
    IUpcCommInfo upcCommInfo = getUpcCommInfo(commInfo);
    String userProc = upcCommInfo.getSendUserProcedure();
    String gdocId = (String)header.get(ICommonHeaders.MSG_TRANSACTION_ID);
    String fileId = (String)header.get(ICommonHeaders.PAYLOAD_ID);
    boolean isZip = Boolean.valueOf((String)header.get(ICommonHeaders.PAYLOAD_IS_ZIP)).booleanValue();

    checkEmpty(userProc, "User Procedure");
    checkEmpty(gdocId, "GridDoc. ID");
    checkEmpty(fileId, "File ID");
    Logger.debug("[UpcTransportHandler.send] FileId = "+fileId);

    if (!isContinueSend(upcCommInfo, fileId, isZip))
      return;

    //retrieve griddoc using transaction id from header
    GridDocument gdoc = getGridDocument(gdocId);

    String originalPayloadName = getOriginalPayloadName(fileId, gdoc, (Integer)_track.get(fileId));

    //save fileData to fileid from header
    // this will return a filename different from fileId
    String payloadFile = saveFileData(fileId, originalPayloadName, fileData);

    //set persisted filename into griddoc
    gdoc.setOBPayloadFile(payloadFile);

    //invoke userproceduredelegate using userprocedure from comminfo and griddoc
    invokeUserProcedure(upcCommInfo.getSendUserProcedure(), gdoc);

    //clean temp file
    cleanupFile(payloadFile);
  }

  /**
   * Check if continue with the sending process based on the send options:
   * <code>sendAll</code>, <code>sendUdocOnly</code>, and <code>skipGdoc</code>.
   * <p>
   * If <code>sendAll</code>, then the send process proceeds.<p>
   * If <code>sendUdocOnly</code>, then the send process will only proceed
   * if the current file payload is the User document.<p>
   * If <code>skipGdoc</code>, then the send process will only proceed if the
   * current file payload is the User document or attachment.<p>
   * Otherwise, the send process proceeds.
   *
   * @param upcCommInfo The UpcCommInfo to use.
   * @param fileId The fileId for the file payloads
   * @param isZip Whether the file payloads are zipped. If <b>true</b>, then the send options
   * are not evaluated.
   * @return <b>true</b> if
   */
  protected boolean isContinueSend(IUpcCommInfo upcCommInfo, String fileId, boolean isZip)
  {
    boolean proceed = false;

    /**
     * @todo This scheme may not work correctly for resume send. The fileId may be
     * reused. Best is to have the information passed down from Channel.
     * To be revised.
     */

    if (!isZip) //track only if the payloads are not zipped
    {
      Integer count = (Integer)_track.get(fileId);
      if (count == null)
      {
        _track.put(fileId, new Integer(1));
      }
      else
      {
        _track.put(fileId, new Integer(count.intValue()+1));
      }
    }

    if (isZip || upcCommInfo.isSendAll() || (!upcCommInfo.isSendUdocOnly() && !upcCommInfo.isSkipSendGdoc()))
    {
      proceed = true;
    }
    else
    {
      Integer count = (Integer)_track.get(fileId);
      if (count.intValue() == 1) //called first time, always send the udoc
      {
        proceed = true;
      }
      else
      {
        if (upcCommInfo.isSendUdocOnly()) // don't send any other files
        {
          Logger.log("[UpcTransportHandler.isContinueSend] Not sending GridDoc/Attachment file payload.");
        }
        else //skipSendGdoc
        {
          if (count.intValue()==2)//this is the second time i.e. GridDoc, skip
          {
            Logger.log("[UpcTransportHandler.isContinueSend] Not sending GridDoc file payload.");
          }
          else //third time onwards are the attachments
          {
            proceed = true;
          }
        }
      }
    }

    return proceed;
  }

  /**
   * Get the original name of the payload.
   *
   * @param fileId The fileId for this batch of payloads
   * @param gdoc The GridDocument for the document to send
   * @param payloadIndex The index number of the payload being sent this round.
   * 1 = udoc, 2 = gdoc, 3 or more = attachments.
   * @return The original filename of the payload.
   * @throws GNTransportException Error retrieving attachment filenames
   */
  protected String getOriginalPayloadName(String fileId, GridDocument gdoc, Integer payloadIndex)
    throws GNTransportException
  {
    String payloadFn;
    if (payloadIndex == null) //zipped payload
    {
      payloadFn = fileId + ".zip";
    }
    else
    {
      int index = payloadIndex.intValue();
      switch (index)
      {
        case 1:
          //udoc
          payloadFn = gdoc.getUdocFilename();
          break;
        case 2:
          //gdoc
          payloadFn = gdoc.getGdocFilename();
          break;
        default:
          //attachments
          int attachmentIndex = index-2;
          Long attachmentUid = (Long)gdoc.getAttachments().get(attachmentIndex);
          payloadFn = getAttachmentFilename(attachmentUid);
      }
    }
    return payloadFn;
  }

  /**
   * Get the filename of an Attachment.
   *
   * @param attachmentUid The UID of the Attachment.
   * @return The original filename of the Attachment.
   * @throws GNTransportException Unable to find an Attachment using the specified UID.
   */
  protected String getAttachmentFilename(Long attachmentUid)
    throws GNTransportException
  {
    try
    {
      return DocumentUtil.getAttachmentOriginalName(attachmentUid);
    }
    catch (Exception e)
    {
      throw new GNTransportException("Unable to determine attachment original filename!", e);
    }
  }
  /**
   * @see com.gridnode.pdip.base.transport.handler.ITransportHandler#connect(com.gridnode.pdip.base.transport.comminfo.ICommInfo, java.lang.String[])
   */
  public void connect(ICommInfo commInfo, String[] header)
    throws
      InvalidCommInfoException,
      InvalidProtocolException,
      GNTptPersistenceConnectionException,
      GNTptWrongConfigException,
      GNTransportException
  {
    Logger.log("[UpcTransportHandler.connect] Connect is currently unsupported for UPC");
  }

  /**
   * @see com.gridnode.pdip.base.transport.handler.ITransportHandler#connectAndListen(com.gridnode.pdip.base.transport.comminfo.ICommInfo, java.lang.String[])
   */
  public void connectAndListen(ICommInfo commInfo, String[] header)
    throws
      InvalidCommInfoException,
      InvalidProtocolException,
      GNTptPersistenceConnectionException,
      GNTptWrongConfigException,
      GNTransportException
  {
    Logger.log("[UpcTransportHandler.connectAndListen] ConnectAndListen is currently unsupported for UPC");
  }

  /**
   * @see com.gridnode.pdip.base.transport.handler.ITransportHandler#disconnect(com.gridnode.pdip.base.transport.comminfo.ICommInfo)
   */
  public void disconnect(ICommInfo commInfo)
    throws
      InvalidCommInfoException,
      InvalidProtocolException,
      GNTptPersistenceConnectionException,
      GNTptWrongConfigException,
      GNTransportException
  {
    Logger.log("[UpcTransportHandler.disconnect] Disconnect is currently unsupported for UPC");
  }

  // ******************* OWN METHODS **************************

  /**
   * Validate and return an UPC CommInfo.
   *
   * @param commInfo The CommInfo to be validated.
   * @throws InvalidCommInfoException The specified commInfo is <b>null</b>
   * or is not an UPC CommInfo.
   */
  private IUpcCommInfo getUpcCommInfo(ICommInfo commInfo)
    throws InvalidCommInfoException
  {
    if (commInfo == null)
      throw new InvalidCommInfoException("Invalid CommInfo! CommInfo must not be null");

    if (commInfo instanceof IUpcCommInfo)
      return (IUpcCommInfo)commInfo;

    throw new InvalidCommInfoException("Invalid CommInfo! Expected UPC but got "+commInfo.getProtocolType());
  }

  /**
   * Retrieves the GridDocument (OB) with the specified GridDoc. ID.
   *
   * @param gdocId The GridDoc. ID.
   * @return The retrieved GridDocument.
   * @throws GNTransportException Error retrieving the GridDocument from the
   * database, or the GridDocument does not already exists in the database.
   */
  private GridDocument getGridDocument(String gdocId) throws GNTransportException
  {
    GridDocument gdoc = null;
    try
    {
      gdoc = DocumentUtil.findOutboundGridDocument(gdocId);
      if (gdoc == null)
        throw new Exception("GridDoc does not exist in database!");
    }
    catch (Exception e)
    {
      throw new GNTransportException("Unable to find OB GridDoc with GdocId "+gdocId, e);
    }

    return gdoc;
  }

  /**
   * Checks if a value is <b>null</b> or contains only whitespaces.
   *
   * @param value The value to check.
   * @param desc The description for the value to be checked.
   * @throws InvalidCommInfoException The value is <b>null</b> or contains only whitespaces.
   */
  private void checkEmpty(String value, String desc) throws InvalidCommInfoException
  {
    if (value == null || value.trim().length()==0)
      throw new InvalidCommInfoException(desc + " is expected!");
  }

  /**
   * Save the byte array into the specified file.
   *
   * @param fileId Parent directory for the saved file
   * @param filename Name of the file to save to.
   * @param fileData Byte array.
   * @return Path to the saved file.
   * @throws GNTransportException
   */
  private String saveFileData(String fileId, String filename, byte[] fileData) throws GNTransportException
  {
    try
    {
      File baseDir = getBaseDirectory(fileId);
      File tempFile = new File(baseDir, filename);
      /*
      int index = filename.lastIndexOf('.');

      String ext = index > -1 ? filename.substring(index) : null;
      String baseName = index > -1 ? filename.substring(0, index) : filename;
      File tempFile = File.createTempFile(baseName,
                                          ext,
                                          baseDir);
      */
      BufferedOutputStream out = new BufferedOutputStream(
                                      new FileOutputStream(tempFile));
      out.write(fileData);
      out.close();

      return tempFile.getCanonicalPath();
    }
    catch (Exception e)
    {
      throw new GNTransportException("Unable to persist file content!", e);
    }
  }

  /**
   * Get a base directory under the system temp directory. Creates one if it does not
   * already exists.
   *
   * @param directoryName The base directory name.
   * @return The base directory created.
   */
  private File getBaseDirectory(String directoryName)
  {
    String tmpDir = System.getProperty("java.io.tmpdir");
    File baseDir = new File(tmpDir, directoryName);
    baseDir.mkdirs();
    return baseDir;
  }
  /**
   * Invokes an user procedure on a GridDocument.
   *
   * @param userProc The name of the user procedure to invoke.
   * @param gdoc The GridDocument to invoke on.
   * @throws GNTransportException Error in execution or the User procedure
   * executed returns non-empty value.
   */
  private void invokeUserProcedure(
    String userProc, GridDocument gdoc)
    throws GNTransportException
  {
    Object returnVal = null;
    try
    {
      returnVal = UserProcedureDelegate.execute(userProc, gdoc);
    }
    catch (Exception e)
    {
      throw new GNTransportException("Error invoking user procedure", e);
    }

    if (returnVal != null) // assumes the return value is the error message.
    {
      //if returnVal is a Number then the error code is returned
      if (returnVal instanceof Number)
      {
        Number numVal = (Number)returnVal;
        if (numVal.intValue() != 0)
          throw new GNTransportException("Error code "+returnVal, null);
      }
      else //otherwise the error message is returned
      {
        String errorMsg = String.valueOf(returnVal);
        if (errorMsg.trim().length() > 0)
          throw new GNTransportException(errorMsg, null);
      }
    }
  }

  /**
   * Cleanup the specified file i.e. Delete the file.
   *
   * @param file The file to be deleted.
   */
  private void cleanupFile(String file)
  {
    new File(file).delete();
  }
}

