/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveDocumentHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 27 2002    Koh Han Sing        Created
 * Oct 28 2002    Neo Sok Lay         Modified handlerMessage() params in
 *                                    IReceiveMessageHandler.
 * Dec 09 2002	  Jagadeesh	      Modified - IReceiveMessageHandler to
 *				      include Hashtable additionalHeader.
 * Jan 17 2003    Neo Sok Lay         Pass same parameters no matter RN document
 *                                    or normal document.
 * May 07 2003    Neo Sok Lay         Raise alerts when partner function failure
 *                                    during receive.
 * Nov 05 2003    Neo Sok Lay         Pass the additionalHeaders to document manager
 *                                    to processNormalDoc().
 * Jan 26 2004    Neo Sok Lay         Check for returned Gdoc from processNormalDoc().
 *                                    If null, do not continue processing the receive.
 * Jan 25 2005    Mahesh              Modified to pass sigVerifyExMsg
 * Oct 27 2005    Neo Sok Lay         Change exception handling for handleReceiveDoc()
 */
package com.gridnode.gtas.server.document.handlers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.document.exceptions.PartnerFunctionFailure;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.helpers.IDocProcessingErrorConstants;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.server.document.helpers.ReceiveDocumentHelper;
import com.gridnode.gtas.server.document.helpers.RegistrationDelegate;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;
import com.gridnode.pdip.base.rnif.helper.IRNHeaderConstants;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.io.File;
import java.util.Hashtable;

public class ReceiveDocumentHandler
       implements IReceiveMessageHandler
{
  private static final String IS_RNIF = "IS_RN_DOC";
  private String status = null;

  public ReceiveDocumentHandler()
  {
  }

  public static IDocumentManagerObj getDocumentManager()
    throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      IDocumentManagerHome.class.getName(),
      IDocumentManagerHome.class,
      new Object[0]);
  }

  public void handlerMessage(/*String eventID,*/
                             String[] header,
                             String[] dataReceived,
                             File[] filesReceived,
                             Hashtable additionalHeader)
  {
    try
    {
      Logger.debug("[ReceiveDocumentHandler.handlerMessage] Receiving document");

      if (isReceiveAllowed())
      {
        boolean isRnif = false;
        if (additionalHeader != null && additionalHeader.get(IS_RNIF) != null)
        {
          isRnif = ((Boolean)additionalHeader.get(IS_RNIF)).booleanValue();
        }

        handleReceivedDoc(header, dataReceived, filesReceived, isRnif, additionalHeader);
        status = "Receive OK";
      }
    }
    catch (Throwable t)
    {
      Logger.error(ILogErrorCodes.GT_RECEIVE_DOC_HANDLER,
                   "[ReceiveDocumentHandler.handlerMessage] Error in receiving document: "+t.getMessage(), t);
      status = "Error receiving document: " + t.getLocalizedMessage();
    }
  }

  private boolean isReceiveAllowed() throws Exception
  {
    boolean valid = RegistrationDelegate.hasValidLicense();

    if (!valid)
    {
      Logger.log("[[ReceiveDocumentHandler.isReceiveAllowed] No --> License invalid");
      new PartnerFunctionFailure(
            IDocProcessingErrorConstants.TYPE_RECEIVE_DOC_FAILURE,
            IDocProcessingErrorConstants.REASON_LICENSE_INVALID,
            null).raiseAlert();
    }
    return valid;
  }

  private void handleReceivedDoc(String[] header,
                                 String[] dataReceived,
                                 File[] filesReceived,
                                 boolean isRnif,
                                 Hashtable additionalHeaders)
  {
    GridDocument gdoc = null;

    try
    {
      if (isRnif)
      {
        String sigVerifyExMsg = null;
        if(additionalHeaders!=null)
          sigVerifyExMsg=(String)additionalHeaders.get(IRNHeaderConstants.SIGNATURE_VERIFY_EXCEPTION);
        ReceiveDocumentHelper.receiveRnifDoc(header, dataReceived, filesReceived, false, null,sigVerifyExMsg);
      }
      else
      {
        gdoc = getDocumentManager().processNormalDoc(header,
                                                     dataReceived,
                                                     filesReceived,
                                                     additionalHeaders);
        //040126NSL: check returned gdoc, possible null if process is delegated to Rnif handling.                                             
        if (gdoc != null)                                             
          getDocumentManager().receiveDoc(gdoc);
      }
    }
    catch (Exception ex) //NSL20051027
    {
    	if (ex.getCause() != null && ex.getCause() instanceof PartnerFunctionFailure)
    	{
    		PartnerFunctionFailure failure = (PartnerFunctionFailure)ex.getCause();
    		failure.raiseAlert();
    	}
    } /*
    catch (PartnerFunctionFailure failure)
    {
      Logger.err("[ReceiveDocumentHandler.handleReceivedDoc] PartnerFunctionFailure "+failure);
      failure.raiseAlert();
    }*/
    catch (Throwable t)
    {
      Logger.warn("[ReceiveDocumentHandler.handleReceivedDoc] Error in handling ", t);
      new PartnerFunctionFailure(
            gdoc,
            IDocProcessingErrorConstants.TYPE_RECEIVE_DOC_FAILURE,
            IDocProcessingErrorConstants.REASON_GENERAL_ERROR,
            t
          ).raiseAlert();
    }
  }

  public String getReceiveStatus()
  {
    return status;
  }

}