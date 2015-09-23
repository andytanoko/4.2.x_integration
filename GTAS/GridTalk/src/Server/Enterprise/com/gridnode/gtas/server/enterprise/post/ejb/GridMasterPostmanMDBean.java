/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridMasterPostmanMDB.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Neo Sok Lay         Created
 * Nov 06 2002    Neo Sok Lay         Moved from GridNode module.
 * Jan 14 2003    Neo Sok Lay         Use fileID from PostInstruction if set.
 * Jan 23 2003    Neo Sok Lay         Pass My GNCI as header to Channel for sending.
 * Oct 01 2003    Neo Sok Lay         Use ChannelSendHeader to create fixed-size
 *                                    header array if one is not specified
 *                                    in PostInstruction.
 * Jan 06 2004    Neo Sok Lay         Fix: Wrongly set RecipientNodeId to FileId in
 *                                    uploadToGm().
 */
package com.gridnode.gtas.server.enterprise.post.ejb;

import com.gridnode.gtas.server.enterprise.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.enterprise.post.PostInstruction;
import com.gridnode.gtas.server.enterprise.helpers.Logger;

import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;
import com.gridnode.pdip.app.channel.model.ChannelInfo;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.log.FacadeLogger;

import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import java.io.File;

/**
 * This MDB listens to the GridMaster Postman queue for posting instructions.
 * Each post instruction is a message to be sent to a connected GridMaster.
 * The task of the MDB is to invoke the Channel Manager to send the message
 * to the currently connected GridMaster.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class GridMasterPostmanMDBean
  implements MessageDrivenBean,
             MessageListener
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5531584819858673355L;
	private MessageDrivenContext _mdx = null;

  public GridMasterPostmanMDBean()
  {
  }

  public void setMessageDrivenContext(MessageDrivenContext ctx)
    throws javax.ejb.EJBException
  {
    _mdx = ctx;
  }

  public void ejbRemove()
  {
  }

  public void ejbCreate()
  {
  }

  public void onMessage(Message message)
  {
    FacadeLogger logger = Logger.getPostmanFacadeLogger();
    String methodName   = "onMessage";
    Object[] params     = new Object[] {message};

    try
    {
      logger.logEntry(methodName, params);

      PostInstruction instruction = (PostInstruction)((ObjectMessage)message).getObject();

      handleInstruction(instruction);
    }
    catch (Throwable ex)
    {
      logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handle the PostInstruction by sending to the currently connected GridMaster
   * via the GridMaster Channel.
   * @param instruction The PostInstruction.
   * @throws Exception If no GridMaster connected currently.
   */
  private void handleInstruction(PostInstruction instruction)
    throws Throwable
  {
    FacadeLogger logger = Logger.getPostmanFacadeLogger();
    String methodName   = "handleInstruction";
    Object[] params     = new Object[] {instruction};

    GridMasterState.getInstance().lock();
    try
    {
      logger.logEntry(methodName, params);

logger.logMessage(methodName, params, "Before checking gridmaster state");
      if (GridMasterState.getInstance().isOnline())
      {
     logger.logMessage(methodName, params, "GridMaster is online");
        ChannelSendHeader header = instruction.getHeader();
        if (header == null)
        {
          logger.logMessage(methodName, params, "send header is null");
          header = new ChannelSendHeader(
                        instruction.getEventID(), 
                        instruction.getTransID(), 
                        instruction.getEventSubID(),
                        instruction.getFileID());
          header.setGridnodeHeaderInfo(
            instruction.getSenderNodeID(), 
            instruction.getRecipientNodeID(),
            GridMasterState.getInstance().getMyGNCI());             
        }
        
        if (instruction.getRecipientChannel() == null)
        {
          logger.logMessage(methodName, params, "recipient channel is null");
          // recipient is GridMaster
          // send to GridMaster channel
          sendToGm(
            instruction.getDataPayload(),
            instruction.getFilePayload(),
            header.getHeaderArray());
          /*031001NSL  
          sendToGm(
            instruction.getDataPayload(),
            instruction.getFilePayload(),
            instruction.getEventID(),
            instruction.getEventSubID(),
            instruction.getTransID(),
            instruction.getFileID(),
            instruction.getSenderNodeID(),
            instruction.getRecipientNodeID());
          */  
        }
        else
        {
          logger.logMessage(methodName, params, "uploading to GM");
          
          // recipient is another GridTalk
          // upload via GridMaster channel
          uploadToGm(
            instruction.getDataPayload(),
            instruction.getFilePayload(),
            header.getHeaderArray(),
            instruction.getRecipientChannel());
          /*031001NSL  
          uploadToGm(
            instruction.getDataPayload(),
            instruction.getFilePayload(),
            instruction.getEventID(),
            instruction.getEventSubID(),
            instruction.getTransID(),
            instruction.getFileID(),
            instruction.getSenderNodeID(),
            instruction.getRecipientNodeID(),
            instruction.getRecipientChannel());
          */  
        }
      }
      else
        throw new Exception("No connection to GridMaster established! Message cannot be sent.");
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
      logger.logMessage(methodName, params, ex.toString());
      //logger.logMessage(methodName, params, ex.getMessage());
    }
    finally
    {
      GridMasterState.getInstance().releaseLock();
      logger.logExit(methodName, params);
    }

  }

  private void sendToGm(
    String[] dataPayload, File[] filePayload,
    String[] header)
    //String eventID, String eventSubID,
    //String transID, String fileID,
    //String sender,  String recipient)
    throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();

    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(),
      GridMasterState.getInstance().getGmNodeID(), false);

    Object[] channels = ServiceLookupHelper.getChannelManager().getChannelInfo(
                          filter).toArray();

    if (channels.length > 0)
    {
      ChannelInfo channel = (ChannelInfo)channels[0];
      
      String fileID = header[ChannelSendHeader.FILE_ID_POS];
      String recipient = header[ChannelSendHeader.RECIPIENT_NODEID_POS];
      
      if (header[ChannelSendHeader.FILE_ID_POS] == null)
      {
        String eventID = header[ChannelSendHeader.EVENT_ID_POS];
        String transID = header[ChannelSendHeader.TRANSACTION_ID_POS];
        header[ChannelSendHeader.FILE_ID_POS] = "sendToGm_E"+eventID+"_T"+transID;
      }
      if (header[ChannelSendHeader.RECIPIENT_NODEID_POS] == null)
        header[ChannelSendHeader.RECIPIENT_NODEID_POS] = GridMasterState.getInstance().getGmNodeID();

      ServiceLookupHelper.getChannelManager().send(
        channel, dataPayload, filePayload,
        header
        /*031001NSL
        new String[]
        {
          eventID, transID, eventSubID,
          fileID,
          sender, //Sender node id
          recipient, //Recipient node id
          GridMasterState.getInstance().getMyGNCI(),
        }*/
        );
    }
    else
      throw new Exception("Channel not found for GridMaster "+
                  GridMasterState.getInstance().getGmNodeID());
  }

  private void uploadToGm(
    String[] dataPayload, File[] filePayload,
    String[] header,
    //String eventID, String eventSubID,
    //String transID, String fileID,
    //String sender, String recipient,
    Object recipientChannel)
    throws Throwable
  {
    ChannelInfo channel = null;
    
    if (recipientChannel instanceof Long) //uid
    {
      System.out.println("--------- recipientChannel is Long");
      channel = ServiceLookupHelper.getChannelManager().getChannelInfo((Long)recipientChannel);
    }
    else if (recipientChannel instanceof ChannelInfo)  
    {
      System.out.println("--------- recipientChannel is ChannelInfo");
      channel = (ChannelInfo)recipientChannel;
    }
    else
      throw new Exception("Invalid RecipientChannel specified: "+recipientChannel);  

    if (header[ChannelSendHeader.FILE_ID_POS] == null)
    {
      String eventID = header[ChannelSendHeader.EVENT_ID_POS];
      String transID = header[ChannelSendHeader.TRANSACTION_ID_POS];
      header[ChannelSendHeader.FILE_ID_POS] = "uploadToGm_E"+eventID+"_T"+transID;
    }

    ServiceLookupHelper.getChannelManager().send(
      channel, dataPayload, filePayload,
      header
      /*031001NSL
      new String[]
      {
        eventID, transID, eventSubID,
        fileID,
        sender, //Sender node id
        recipient, //Recipient node id
        GridMasterState.getInstance().getMyGNCI(),
      }*/
      );
  }

}