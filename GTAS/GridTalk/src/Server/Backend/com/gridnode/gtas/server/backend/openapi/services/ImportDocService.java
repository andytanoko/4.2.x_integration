/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportDocService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * unknown       unknown              created
 * 9  Dec 2005    SC                   add support for process instance id.	
 * 10 Nov 2006   Tam Wei Xiang        Added support for the tracingID (for use in OTC)
 *                                    Modified method handleService(...)
 * 19 Jan 2007    Neo Sok Lay         Use common Notifier to broadcast notification.                                   
 */
package com.gridnode.gtas.server.backend.openapi.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.backend.openapi.core.APIParams;
import com.gridnode.gtas.server.notify.ImportBackendDocNotification;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.pdip.framework.exceptions.NestingException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;

public class ImportDocService extends AbstractService implements IGTAPIService
{
  private static final String CLASS_NAME = "ImportDocService";

  public ImportDocService(GTServiceFactory factory)
  {
    super(factory);
  }

  public APIParams handleService(APIParams params)
  {
    Object[] returnParam = null;
    try
    {
      File[] fileArray = params.getFileArray();
      Object[] paramArray = params.getParamArray();

      Object sessionID        = paramArray[0];
      String userID           = (String) paramArray[1];
      String partnerID        = null;
      String documentType     = null;
      if (paramArray[2] != null)
      {
        partnerID = (String) paramArray[2];
      }
      if (paramArray[3] != null)
      {
        documentType = (String) paramArray[3];
      }
      int attachmentIndex     = ((Integer)paramArray[4]).intValue();
      String businessEntityId = (String) paramArray[5];
      String rnProfile        = (String) paramArray[6];
      String uniqueDocId      = (String) paramArray[7];
      String processInstanceId = (String) paramArray[8];
      String tracingID        = (String) paramArray[9]; //TWX 10112006

      //SC LOG
      log("processInstanceId = " + processInstanceId);

      if ((sessionID != null) && (fileArray != null) && (fileArray.length > 0))
      {
        ArrayList partner = new ArrayList();
        if (partnerID != null)
        {
          partner.add(partnerID);
        }

        ArrayList attachmentFileName = new ArrayList();
        ArrayList inputFileName = new ArrayList();
        for (int i = 0; i < fileArray.length; i++)
        {
          // move the file to the share in folder for backend
          String fileName     = fileArray[i].getName();
          String fullFilePath = fileArray[i].getAbsolutePath();
          File fromFile = new File(fullFilePath);
          if (!fromFile.exists())
          {
            throw new FileNotFoundException("Import file "+
                                            fullFilePath+" not found");
          }

          FileInputStream fis = new FileInputStream(fromFile);
          String newFilename = FileUtil.create(IPathConfig.PATH_TEMP,
                                               userID+"/in/", fileName, fis);

          log(CLASS_NAME, "handleService", "newFilename = "+newFilename);
          log(CLASS_NAME, "handleService",
              "attachmentIndex = "+attachmentIndex);
          if (attachmentIndex < 0 || attachmentIndex < i)
          {
            //Input Files
            inputFileName.add(newFilename);
            log(CLASS_NAME, "handleService", "inputFileName.add(newFilename)");
          }
          else
          {
            // Attachment Files
            attachmentFileName.add(newFilename);
            log(CLASS_NAME, "handleService",
                "attachmentFileName.add(newFilename)");
          }
        }

        ImportBackendDocNotification notification =
          new ImportBackendDocNotification(
            businessEntityId,
            documentType,
            inputFileName,
            partner,
            attachmentFileName,
            rnProfile,
            uniqueDocId,
            processInstanceId,
            getFactory().getGridTalkClientController().getHandle(),
            tracingID
          );
        try
        {
	        //NSL20070119 Use common notifier to broadcast
	        Notifier.getInstance().broadcast(notification);
	        //ImportBackendDocNotifier.getInstance().broadcast(notification);
        }
        catch (Exception e)
        {
        	error(ILogErrorCodes.GT_IMPORT_DOC_SERVICE,
                CLASS_NAME, "handleService", "Failed to broadcast notification. Error: "+e.getMessage(), e);
        }
      }
      params.deleteFiles();
    }
    catch (Throwable t)
    {
      warn(CLASS_NAME, "handleService", "Not successful", t);
      
      //TWX 10112006 return the status
      returnParam = new Object[3];
      returnParam[0] = Boolean.FALSE;
      returnParam[1] = t.getMessage();
      returnParam[2] = NestingException.generateStackTraceString(t);
    }
    //finally
    //{
      return new APIParams(returnParam);
    //}
  }
  
  //SC LOG
  private void log(String message)
  {
  	com.gridnode.gtas.server.backend.helpers.Logger.log("message");
  }

}
