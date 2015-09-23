/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: InboundTransporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 03 2003    Neo Sok Lay         Created
 * Jan 15 2004    Neo Sok Lay         Use "user.dir" property for creating dirs
 *                                   & log files.
 */
package com.gridnode.gridtalk;

import com.gridnode.ext.util.ErrorUtil;
import com.gridnode.ext.util.StringUtil;
import com.gridnode.ext.util.ValidationUtil;
import com.gridnode.ext.util.exceptions.ValidationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * This program's main task is to push a file into GridTalk receive channel.
 * Properties to configure the behavior should be specified in the
 * <code>inbound-transporter.properties</code> file. In addition, dynamic
 * properties can be passed in from command line or in another properties file
 * to add-on/override the default properties in <code>inbound-transporter.properties</code>.
 *
 * The method of transportation can be configured through the <code>inbound.transporter.method</code>
 * property. Currently supported methods are: <code>soap</code>.
 * The default method used is <code>soap</code>.
 *
 *
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class InboundTransporter implements ITransporterReturnCodes
{

  public InboundTransporter()
  {
  }

  /**
   * Starts the processing
   *
   * @param args The input arguments from command line.
   * @throws Exception Error loading the transporter properties, or invalid
   * properties specified, or error transmitting the file to the GridTalk
   * receive channel.
   */
  public void start(String[] args) throws Exception
  {
    InboundTransporterProperties props = new InboundTransporterProperties();
    props.addPropertiesFromArray(args);
    props.loadOverridingPropertiesFile();

    createLogFile(props.getLogFile());
    System.out.println(
      "##### InboundTransporter Started on " + new Date() + " #####");
    transportFile(props);
  }

  /**
   * Create the log file
   *
   * @param logFilename The name of the log file to create.
   * If specified, the System.out and System.err will be channelled to
   * the log file instead of the console.
   */
  private void createLogFile(String logFilename)
  {
    if (!StringUtil.isBlank(logFilename))
    {
      try
      {
        File logFile = new File(InboundTransporterProperties.getWorkingDir(), logFilename);
        if (logFile.getParentFile() != null)
          logFile.getParentFile().mkdirs();

        FileOutputStream logOS = new FileOutputStream(logFile, true);
        PrintStream logPS = new PrintStream(logOS, true);
        System.setErr(logPS);
        System.setOut(logPS);
      }
      catch (Exception ex)
      {
        System.err.println("Error creating log file " + logFilename);
      }
    }
  }

  /**
   * Validate the InboundTransporterProperties for validity of the properties
   * specified. This method is only responsible for the general properties.
   *
   * @param props The InboundTransporterProperties.
   * @throws ValidationException Invalid property encountered.
   */
  private void validateProperties(InboundTransporterProperties props)
    throws ValidationException
  {
    // Validate the payloads
    String payloadDir = props.getPayloadDir();
    String payloadFile = props.getPayloadFile();
    boolean hasFile = false;

    // check file existence as long as it is specified
    if (!StringUtil.isBlank(payloadFile))
    {
      ValidationUtil.checkFileExists(
        InboundTransporterProperties.KEY_PAYLOAD_FILE,
        null,
        payloadFile,
        false);
      hasFile = true;
    }

    if (!StringUtil.isBlank(payloadDir))
    {
      // check the directory is not empty (must contain some files)
      // if there is still no file determined to transmit
      if (!hasFile)
      {
        ValidationUtil.checkHasFiles(
          InboundTransporterProperties.KEY_PAYLOAD_DIR,
          null,
          payloadDir);
        hasFile = true;
      }
    }

    if (!hasFile)
      throw new ValidationException("No file specified to transmit!");

    // Validate the Transport method
    ValidationUtil.checkEnumeration(
      InboundTransporterProperties.KEY_TRANSPORT_METHOD,
      props.getTransportMethod(),
      new String[] { InboundTransporterProperties.TRANSPORT_METHOD_SOAP, });
  }

  /**
   * Transport a file to the GridTalk receive channel using the specified
   * InboundTransporterProperties.
   *
   * @param props The InboundTransporterProperties that specifies how
   * the file will get across to the GridTalk receive channel.
   * @throws Exception Validation exception on the properties, or
   * error during transportation.
   */
  public void transportFile(InboundTransporterProperties props)
    throws Exception
  {
    // validate common properties
    validateProperties(props);

    // collect the payloads
    File[] payloads = getPayloads(props);

    if (payloads.length == 0)
    {
      System.out.println("No file to transmit by this InboundTransporter.");
      return;
    }

    String transportMethod = props.getTransportMethod();
    boolean isSoap =
      InboundTransporterProperties.TRANSPORT_METHOD_SOAP.equals(
        transportMethod);
    String successDir = props.getPayloadSuccessDir();
    String failDir = props.getPayloadFailDir();

    int failCount = 0;
    int successCount = 0;
    for (int i = 0; i < payloads.length; i++)
    {
      props.setProperty(
        InboundTransporterProperties.KEY_PAYLOAD_FILE,
        payloads[i].getAbsolutePath());
      generateFileId(props);

      SoapTransporter soapTransporter = new SoapTransporter();
      try
      {
        if (isSoap)
        {
          soapTransporter.invokeInboundService(props);
        }
        successCount++;
        handleSuccessFile(payloads[i], successDir);
      }
      catch (Exception ex)
      {
        ErrorUtil.printErrorMessage(TRANSPORTER_RUN_ERROR, ex);
        failCount++;
        handleFailFile(payloads[i], failDir);
      }
    }

    System.out.println("Successfully transmitted " + successCount + " files.");

    if (failCount > 0)
      throw new TransporterImplException(
        "Error transmitting " + failCount + " files.");
  }

  /**
   * Decide what to do with the payload file that has been transmitted
   * successfully.
   *
   * @param successFile The payload file that succeeded.
   * @param successDir The directory to keep the successFile. If omitted,
   * the successFile will be deleted.
   */
  private void handleSuccessFile(File successFile, String successDir)
  {
    String fileName = successFile.getName();
    System.out.println(fileName + " has been successfully tranmitted.");

    moveOrDeleteFile(successFile, successDir);
  }

  /**
   * Decide what to do with the payload file that has failed to be
   * transmitted.
   *
   * @param failFile The payload file that failed.
   * @param failDir The directory to keep the failFile. If omitted,
   * the failFile will be deleted.
   */
  private void handleFailFile(File failFile, String failDir)
  {
    String fileName = failFile.getName();
    System.out.println(fileName + " has failed to be tranmitted.");

    moveOrDeleteFile(failFile, failDir);
  }

  /**
   * Move or delete a file.
   *
   * @param file The file to move or delete.
   * @param dir The directory to move to. If specified, the file will be moved to this
   * directory, retaining its original name. Otherwise, the file will be deleted.
   */
  private void moveOrDeleteFile(File file, String dir)
  {
    String fileName = file.getName();
    if (!StringUtil.isBlank(dir))
    {
      File directory = new File(InboundTransporterProperties.getWorkingDir() ,dir);
      if (!directory.exists())
        directory.mkdirs();

      if (directory.isDirectory())
      {
        // move successFile to successDir
        File destFile = new File(directory, fileName);
        if (destFile.exists())
          destFile.delete();

        file.renameTo(destFile);
        System.out.println(fileName + " has been moved to directory " + dir);
      }
    }
    else
    {
      file.delete();
      System.out.println(fileName + " has been deleted.");
    }
  }

  /**
   * Collect the payloads to be transmitted in this session.
   *
   * @param props The InboundTransporterProperties
   * @return Array of File(s) to be transmitted.
   * @throws Exception Error creating lock file.
   */
  private File[] getPayloads(InboundTransporterProperties props)
    throws Exception
  {
    ArrayList payloads = new ArrayList();
    String payloadDir = props.getPayloadDir();
    String payloadFile = props.getPayloadFile();

    if (!StringUtil.isBlank(payloadFile))
    {
      payloads.add(new File(payloadFile));
    }

    if (!StringUtil.isBlank(payloadDir))
    {
      File dir = new File(payloadDir);

      // do not bother with the files in the directory
      // since another InboundTransporter session could be in the midst of
      // processing.
      File[] files = dir.listFiles();
      if (files != null && files.length > 0)
      {
        File lock = new File(dir, ".lock");
        if (lock.createNewFile())
        {
            for (int i = 0; i < files.length; i++)
              payloads.add(files[i]);
            lock.deleteOnExit();
        }
      }
    }

    return (File[]) payloads.toArray(new File[payloads.size()]);
  }

  /**
   * If fileid property name is specified in <code>inbound.payload.file.id</code>,
   * generate the unique file id for the payload file to transmit, and set into
   * the fileid property specified.
   * @param props
   */
  private void generateFileId(InboundTransporterProperties props)
  {
    String fileIdProperty = props.getPayloadFileIdProperty();
    if (!StringUtil.isBlank(fileIdProperty))
    {
      File payloadFile = new File(props.getPayloadFile());
      String filePrefix = props.getPayloadFileIdPrefix();
      filePrefix =
        StringUtil.substitute(
          filePrefix,
          InboundTransporterProperties.NOW_SUBST_PROPERTY,
          String.valueOf(System.currentTimeMillis()));

      StringBuffer buff =
        new StringBuffer(filePrefix).append(payloadFile.getName());

      props.setProperty(fileIdProperty, buff.toString());
      System.out.println(
        "Setting property "
          + fileIdProperty
          + " with value "
          + buff.toString());
    }
  }

  /**
   * Main driver for the InboundTransporter program.<p>
   * Exit code:<p>
   * <pre>
   * 0    : Successful execution. File is transmitted to GridTalk receive channel.
   * -10  : Invalid properties for running the InboundTransporter program.
   * -20  : Error while transmitting file to GridTalk receive channel.
   * -100 : Other general errors.
   * </pre>
   *
   * @param args Optional. Additional properties to configure the
   * InboundTransporter program. If specified, each argument should be
   * specified in the form: <property>=<value>
   * @see InboundTransporterProperties
   */
  public static void main(String[] args)
  {
    InboundTransporter transporter = new InboundTransporter();
    try
    {
      transporter.start(args);
      exit(SUCCESS);
    }
    catch (ValidationException ex)
    {
      ErrorUtil.printErrorMessage(INVALID_INPUT_PROPS, ex);
      exit(INVALID_INPUT_PROPS);
    }
    catch (TransporterImplException ex)
    {
      ErrorUtil.printErrorMessage(TRANSPORTER_RUN_ERROR, ex);
      exit(TRANSPORTER_RUN_ERROR);
    }
    catch (Exception ex)
    {
      ErrorUtil.printErrorMessage(UNKNOWN_ERROR, ex);
      exit(UNKNOWN_ERROR);
    }
  }

  private static void exit(int code)
  {
    System.out.println(
      "##### InboundTransporter Ended with code "+code + " on " + new Date() + " #####");
    System.exit(code);
  }
}