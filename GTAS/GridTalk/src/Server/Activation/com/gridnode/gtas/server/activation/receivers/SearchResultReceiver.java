/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchResultReceiver.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 14 2002    Neo Sok Lay         Created
 * Dec 09 2002	  Jagadeesh			  Modified - IReceiveMessageHandler to 
 *									  include Hashtable additionalHeader.
 * Oct 01 2003    Neo Sok Lay         Use ChannelReceiveHeader to index the
 *                                    header array.
 * Dec 05 2003    Neo Sok Lay         Fix GNDB00016285: Did not check for
 *                                    results file is null when no results
 *                                    returned from GridMaster.		
 */
package com.gridnode.gtas.server.activation.receivers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.activation.helpers.Logger;
import com.gridnode.gtas.server.activation.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.activation.model.SearchGridNodeResults;

import com.gridnode.pdip.app.channel.helpers.ChannelReceiveHeader;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;

import java.io.File;
import java.util.Hashtable;

/**
 * This is a receiver for acknowledgements to Search Gridnode requests, essentially
 * for the search results.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since 2.0 I6
 */
public class SearchResultReceiver
  implements IReceiveMessageHandler
{

  public SearchResultReceiver()
  {
  }

  public void handlerMessage(
    String[] header, String[] dataReceived, File[] filesReceived,Hashtable additionalHeader)
  {
    try
    {
      Logger.log("[SearchResultReceiver.handlerMessage] "+
        "Received SearchGridNode result for searchID: "+header[ChannelReceiveHeader.TRANSACTION_ID_POS]);

      SearchGridNodeResults received = new SearchGridNodeResults();
      if (filesReceived != null && filesReceived.length>0)
        received = (SearchGridNodeResults)received.deserialize(filesReceived[0].getAbsolutePath());

      ServiceLookupHelper.getActivationManager().notifySearchResults(
        new Long(header[ChannelReceiveHeader.TRANSACTION_ID_POS]), received);
    }
    catch (Exception ex)
    {
      Logger.error(ILogErrorCodes.GT_SEARCH_RESULT_RECEIVER,
        "[SearchResultReceiver.handlerMessage] Error processing received search results: "+ex.getMessage(),
        ex);
      /**@todo raise alert/log in future */
    }
  }
}