/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: SoapMessageRetrieveService.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * ??             ??                  Created
 * 17 OCT 2010    Tam Wei Xiang       #1900 -  
 *                                    1) Change the array of byte[] to array of DataHandler
 *                                       as the Axis2 can not reflect it correctly in the wsdl file
 *                                    2) changed saveFile(...)
 * 04 OCT 2012    Tam Wei Xiang       #3850: BackendImport web service sometime failed to function.
 *                                           We will explicitly set the gridtalkClientCtrl instance
 *                                           as null after processed the web service request.
 *                                    
 */

package com.gridnode.gtas.ws.backend;

import com.gridnode.gtas.ws.*;
import com.gridnode.gtas.events.document.ImportBackendDocumentEvent;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.activation.DataHandler;

public class BackendImport extends BaseWebService
{


  protected List saveFiles(
    String[] filenames,
    byte[][] files)
  {
    ArrayList savedfilenames = new ArrayList();
    if((filenames != null) && (filenames.length > 0))
    {
      for(int i = 0;i < filenames.length;i++)
      {
        try
        {
          String newFilename = FileUtil.create(
              IPathConfig.PATH_TEMP,
              getUserID() + "/in/",
              filenames[i],
              new ByteArrayInputStream(files[i]));
          savedfilenames.add(newFilename);
        }
        catch(Exception ex)
        {
          _log.error("saveFile [" + filenames[i] + " ] error:",ex);
        }
      }
    }
    return savedfilenames;
  }

  private List saveFiles(
    String[] filenames,
    DataHandler[] files)
  {
    ArrayList savedfilenames = new ArrayList();
    if((filenames != null) && (filenames.length > 0))
    {
      for(int i = 0;i < filenames.length;i++)
      {
        try
        {
          String recName = files[i].getName();
          System.out.println(
            "saveFile [" + filenames[i] + " ] name:" + recName);

          String newFilename = FileUtil.create(
              IPathConfig.PATH_TEMP,
              getUserID() + "/in/",
              filenames[i],
              files[i].getInputStream()); //TWX change to use input stream, as the axis2 is no longer returning the full file path to the content
          savedfilenames.add(newFilename);
        }
        catch(Exception ex)
        {
          _log.error("saveFile [" + filenames[i] + " ] error:",ex);
        }
      }
    }
    return savedfilenames;
  }
  
  public Boolean backendImport1(
	String username,
	String password,
	String recipient,
	String contentFileName,
	DataHandler content,
	String docType)
  {
	if(!login(username,password))
	  return false;

	try
	{
	  ArrayList recipients = new ArrayList();
	  recipients.add(recipient);
	  fireEvent(
		new ImportBackendDocumentEvent(
		  null,
		  docType,
		  saveFiles(new String[]{contentFileName},new DataHandler[]{content}),
		  recipients,
		  null,
		  null));
	  reset(); //twx 20121004 #3850
	  _log.info("Successfully imported documents " + contentFileName);
	  return true;
	}
	catch(Exception ex)
	{
	  _log.error("Failed to import documents :" + contentFileName ,ex);
	  return false;
	}
  }
  
  public boolean backendImport2(
	String username,
	String password,
	String recipient,
	String contentFileName,
	DataHandler content,
	String[] attachmentFileNames,
	DataHandler[] attachments,
	String docType)
  {
	if(!login(username,password))
	  return false;

	try
	{
	  ArrayList recipients = new ArrayList();
	  recipients.add(recipient);
	  fireEvent(
		new ImportBackendDocumentEvent(
		  null,
		  docType,
		  saveFiles(new String[]{contentFileName},new DataHandler[]{content}),
		  recipients,
		  saveFiles(attachmentFileNames,attachments),
		  null));
	  reset(); //twx 20121004 #3850
	  _log.info("Successfully imported documents " + contentFileName);
	  return true;
	}
	catch(Exception ex)
	{
	  _log.error("Failed to import documents :" + contentFileName ,ex);
	  return false;
	}
  }

  public boolean backendImport3(
        String username,
        String password,
        String senderID,
	String recipient,
        String contentFileName,
	DataHandler content,
	String[] attachmentFileNames,
        DataHandler[] attachments,
	String docType,
        String processID,
        String docID)
  {
    if(!login(username,password))
      return false;

    try
    {
	ArrayList recipients = new ArrayList();
	recipients.add(recipient);
        fireEvent(
          new ImportBackendDocumentEvent(
          senderID,
          docType,
          saveFiles(new String[]{contentFileName},new DataHandler[]{content}),
	  recipients,
          saveFiles(attachmentFileNames,attachments),
          processID,
          docID));
        reset(); //twx 20121004 #3850
      _log.info("Successfully imported documents " + contentFileName);
      return true;
    }
    catch(Exception ex)
    {
      _log.error("Failed to import documents :" + contentFileName ,ex);
      return false;
    }
  }

  public boolean backendImport4(
	String username,
	String password,
	String recipient,
	String contentFileName,
	byte[] content,
	String docType)
  {
	if(!login(username,password))
	  return false;

	try
	{
	  ArrayList recipients = new ArrayList();
	  recipients.add(recipient);
	  fireEvent(
		new ImportBackendDocumentEvent(
		  null,
		  docType,
		  saveFiles(new String[]{contentFileName},new byte[][]{content}),
		  recipients,
		  null,
		  null));
	  reset(); //twx 20121004 #3850
	  _log.info("Successfully imported documents " + contentFileName);
	  return true;
	}
	catch(Exception ex)
	{
	  _log.error("Failed to import documents :" + contentFileName ,ex);
	  return false;
	}
  }
  

  public boolean backendImport5(
	String username,
	String password,
	String recipient,
	String contentFileName,
	byte[] content,
	String[] attachmentFileNames,
	DataHandler[] attachments,
	String docType)
  {
	if(!login(username,password))
	  return false;

	try
	{
	  ArrayList recipients = new ArrayList();
	  recipients.add(recipient);
	  fireEvent(
		new ImportBackendDocumentEvent(
		  null,
		  docType,
		  saveFiles(new String[]{contentFileName},new byte[][]{content}),
		  recipients,
		  saveFiles(attachmentFileNames,attachments),
		  null));
	  reset(); //twx 20121004 #3850
	  	  
	  _log.info("Successfully imported documents " + contentFileName);
	  return true;
	}
	catch(Exception ex)
	{
	  _log.error("Failed to import documents :" + contentFileName ,ex);
	  return false;
	}
  }
  
  //TWX: change to use DataHandler[] instead of byte[][]
  public boolean backendImport6(
        String username,
        String password,
        String senderID,
	String recipient,
        String contentFileName,
	byte[] content,
	String[] attachmentFileNames,
        DataHandler[] attachments,
	String docType,
        String processID,
        String docID)
  {
    if(!login(username,password))
      return false;

    try
    {
	ArrayList recipients = new ArrayList();
	recipients.add(recipient);
        fireEvent(
          new ImportBackendDocumentEvent(
          senderID,
          docType,
          saveFiles(new String[]{contentFileName},new byte[][]{content}),
	  recipients,
          saveFiles(attachmentFileNames,attachments),
          processID,
          docID));
        
        reset(); //twx 20121004 #3850
      _log.info("Successfully imported documents " + contentFileName);
      return true;
    }
    catch(Exception ex)
    {
      _log.error("Failed to import documents :" + contentFileName ,ex);
      return false;
    }
  }

  
  private void reset()
  {
    boolean isSuccess = logout();
    _log.info("logout status = "+isSuccess+" .Set client controller as null");
    setClientController(null);
  }
}
