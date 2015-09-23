/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Sender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2003    Neo Sok Lay         Created
 * Jan 14 2004    Neo Sok Lay         Enhance to allow sending the response.
 */
package com.gridnode.utadaptor;

import com.gridnode.ext.util.ErrorUtil;
import com.gridnode.ext.util.ValidationUtil;
import com.gridnode.ext.util.StringUtil;
import com.gridnode.ext.util.EnhancedRunnerConfig;
import com.gridnode.ext.util.exceptions.ValidationException;
import com.gridnode.ext.util.exceptions.ConfigurationException;

import java.io.File;
import java.io.FileWriter;
import java.io.StringReader;

/**
 * This Sender program is an utility program that wraps around the UCCnet Adaptor
 * program to sender and receive messages from UCCnet. There are two ways the
 * Sender program may be used:<p>
 * <OL>
 *   <LI>Command-line: Clients have to examine the system exit code to verify the status of the run.</LI>
 *   <LI>API call (sendMessage): Errors would be returned as an error message from the method call.</LI>
 * </OL>
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since GT 2.3 I1
 */
public class Sender implements ISenderReturnCodes
{

  public Sender()
  {
  }

  /**
   * Start the sending process.
   *
   * @param args The input arguments for the Sender program.
   * @throws Exception Error encountered.
   */
  public void start(String[] args) throws Exception
  {
    SenderProperties properties = new SenderProperties();
    properties.addPropertiesFromArray(args);

    sendFile(properties);
  }

  /**
   * Validate the sender properties configured.
   *
   * @param properties The Sender properties.
   * @throws ValidationException Invalid properties configured.
   */
  private void validateProperties(SenderProperties properties)
    throws ValidationException
  {
    ValidationUtil.checkFileExists(
      SenderProperties.KEY_PAYLOAD_FILE,
      null,
      properties.getPayloadFile(),
      false);
    ValidationUtil.checkFileExists(
      SenderProperties.KEY_ADAPTOR_RUN_DIR,
      null,
      properties.getAdaptorRunDir(),
      true);
    ValidationUtil.checkFileExists(
      SenderProperties.KEY_RESPONSE_SAVE_DIR,
      null,
      properties.getResponseSaveDir(),
      true);
    ValidationUtil.checkFileExists(
      SenderProperties.KEY_ADAPTOR_CONF_FILE,
      properties.getAdaptorRunDir(),
      properties.getAdaptorConfigFile(),
      false);
    ValidationUtil.checkNonBlank(
      SenderProperties.KEY_RESPONSE_FILE_SUFFIX,
      properties.getResponseFileSuffix());

    if (isSendResponseRequired(properties))
    {
      ValidationUtil.checkFileExists(SenderProperties.KEY_RESPONSE_SEND_CONF,
                                     properties.getAdaptorRunDir(),
                                     properties.getResponseSendConfig(),
                                     false);
    }
  }

  /**
   * Check if it is required to send the response message (back to the sender system, probably)
   *
   * @param properties The properties
   * @return <b>true</b> if the "response.doctype" and "partner.id" properties
   * are specified.
   */
  private boolean isSendResponseRequired(SenderProperties properties)
  {
    return (!StringUtil.isBlank(properties.getPartnerId()) &&
            !StringUtil.isBlank(properties.getResponseDocType()));
  }

  /**
   * Sends a file payload to the adaptor.
   *
   * @param properties The Sender properties.
   * @throws Exception Error encounterd.
   */
  public void sendFile(SenderProperties properties) throws Exception
  {
    validateProperties(properties);

    String adaptorConf = properties.getAdaptorConfigFile();
    String adaptorDir = properties.getAdaptorRunDir();
    String payload = properties.getPayloadFile();

    String adaptorConfFile = new File(adaptorDir, adaptorConf).getCanonicalPath();

    //load adaptor config
    AdaptorRunConfig conf = new AdaptorRunConfig(adaptorConfFile);

    conf.putSubstitutionProperty(
      SenderProperties.KEY_ADAPTOR_RUN_DIR,
      adaptorDir);
    conf.putSubstitutionProperty(SenderProperties.KEY_PAYLOAD_FILE, payload);

    String responseMsg = adaptorSend(conf);

    String respSaveDir = properties.getResponseSaveDir();
    String respFileSuffix = properties.getResponseFileSuffix();

    //save responseMsg
    String responseFN = saveToFile(responseMsg, payload, respSaveDir, respFileSuffix);

    //if come to this point, it means send is SUCCESS

    //need to send responseMsg?
    if (isSendResponseRequired(properties))
    {
      String respDocType = properties.getResponseDocType();
      String partnerId = properties.getPartnerId();
      String respConf = properties.getResponseSendConfig();
      respConf = new File(adaptorDir, respConf).getCanonicalPath();
      sendResponseFile(respConf, responseFN, respDocType, partnerId);
    }
  }

  /**
   * Send the response file
   *
   * @param sendConfig The configuration for running the ResponseSender
   * @param payloadFN The payload file to send
   * @param docType The document type for the response file (configured in target system)
   * @param partnerId The partner id of the responding party
   * @throws java.lang.Exception Error initializing the ResponseSender or runner config.
   */
  private void sendResponseFile(String sendConfig, String payloadFN, String docType, String partnerId)
    throws Exception
  {
    EnhancedRunnerConfig config = new EnhancedRunnerConfig(sendConfig);
    config.putSubstitutionProperty(SenderProperties.KEY_PAYLOAD_FILE, payloadFN);
    config.putSubstitutionProperty(SenderProperties.KEY_PARTNER_ID, partnerId);
    config.putSubstitutionProperty(SenderProperties.KEY_RESPONSE_DOC_TYPE, docType);

    ResponseSender sender = new ResponseSender(config);
    if (sender.runApp())
    {
      int returnCode = sender.getReturnCode();
      if (returnCode != 0)
      {
        System.err.println("Error sending response file: "+payloadFN);
      }
    }
    else
      System.err.println("Error running response sender program");
  }

  /**
   * Invoke the Adaptor to send the file over.
   *
   * @param config The configuration to use to run the Adaptor.
   * @return The error message returned by the adaptor.
   * @throws Exception Error encountered during the process.
   */
  private String adaptorSend(AdaptorRunConfig config) throws Exception
  {
    AdaptorRunner runner = new AdaptorRunner(config);
    if (runner.runApp()) //runs successful, safe return from process
    {
      int returnCode = runner.getReturnCode();
      if (returnCode == 0) //no error
      {
        return runner.getSystemOutCache();
      }
      else //error detected by adaptor
      {
        String errorResponseXml = runner.getSystemOutCache();
        XmlParser parser = new XmlParser(new StringReader(errorResponseXml));

        String errorCode = parser.getValueAtXPath(config.getErrorCodeXpath());
        String errorDesc = parser.getValueAtXPath(config.getErrorDescXpath());

        throw new AdaptorReturnException(errorCode, errorDesc);
      }
    }
    else // unable to run the process
    {
      //ADAPTOR_RUN_ERROR
      throw new AdaptorRunException("Unable to run adaptor");
    }
  }

  /**
   * Save a string content to a file.
   *
   * @param content The string content to save.
   * @param baseFN The base filename to use for the target filename
   * @param targetDir The directory to write the file to.
   * @param targetFnSuffix The suffix for the target filename.
   * @return Filename of the save file.
   * @throws Exception Error saving the content to file.
   */
  private String saveToFile(
    String content,
    String baseFN,
    String targetDir,
    String targetFnSuffix)
    throws Exception
  {
    /**
     * @todo In future may consider saving the base directory as well in
     * case there are more than 1 payloads. Currently only support the udoc payload.
     * May consider attachments support.
     */
    //determine filename to save as
    baseFN = removeExtension(new File(baseFN).getName());
    String targetFN = baseFN.concat(targetFnSuffix);

    File targetFile = new File(targetDir, targetFN);
    targetFile.getParentFile().mkdirs();

    //write to file
    FileWriter fw = new FileWriter(targetFile);
    fw.write(content);
    fw.close();

    return targetFile.getCanonicalPath();
  }

  /**
   * Removes the file extension from a filename
   *
   * @param filename the Filename
   * @return The filename, with its extension removed, if any.
   */
  private String removeExtension(String filename)
  {
    int start = filename.lastIndexOf('.');
    if (start > -1)
    {
      return filename.substring(0, start);
    }
    else
      return filename;
  }

  /**
   * Sends a message (which is a file).
   *
   * @param payload The filename of the file payload to send.
   * @param adaptorDir The working directory of the Adaptor program
   * @param adaptorConf The run config file for the Adaptor program.
   * @param responseSaveDir The directory to save any response message returned by the
   * Adaptor.
   * @param respFileSuffix The suffix for the response message file.
   * @return Error message, if any, or <b>null</b> if no error encountered.
   */
  public String sendMessage(
    String payload,
    String adaptorDir,
    String adaptorConf,
    String responseSaveDir,
    String respFileSuffix)
  {
    return sendMessage(payload, adaptorDir, adaptorConf, responseSaveDir,
                       respFileSuffix, null, null, null);
  }

  /**
   * Sends a message (which is a file).
   *
   * @param payload The filename of the file payload to send.
   * @param adaptorDir The working directory of the Adaptor program
   * @param adaptorConf The run config file for the Adaptor program.
   * @param responseSaveDir The directory to save any response message returned by the
   * Adaptor.
   * @param respFileSuffix The suffix for the response message file.
   * @param respDoc The Document type of the response document, to be sent
   * @param partnerId The ID of the trading partner receiving the message and sending the response
   * message.
   * @return Error message, if any, or <b>null</b> if no error encountered.
   */
  public String sendMessage(
    String payload,
    String adaptorDir,
    String adaptorConf,
    String responseSaveDir,
    String respFileSuffix,
    String respDocType,
    String partnerId,
    String responseSendConf)
  {
    SenderProperties props = new SenderProperties();
    props.setProperty(SenderProperties.KEY_PAYLOAD_FILE, payload);
    props.setProperty(SenderProperties.KEY_ADAPTOR_RUN_DIR, adaptorDir);
    props.setProperty(SenderProperties.KEY_ADAPTOR_CONF_FILE, adaptorConf);
    props.setProperty(SenderProperties.KEY_RESPONSE_SAVE_DIR, responseSaveDir);
    props.setProperty(
      SenderProperties.KEY_RESPONSE_FILE_SUFFIX,
      respFileSuffix);
    props.setProperty(SenderProperties.KEY_RESPONSE_DOC_TYPE, respDocType);
    props.setProperty(SenderProperties.KEY_PARTNER_ID, partnerId);
    props.setProperty(SenderProperties.KEY_RESPONSE_SEND_CONF, responseSendConf);

    return sendMessage(props);
  }

  /**
   * Send a message based on the specified properties. The payload should
   * also be specified in the properties.
   *
   * @param props The properties that control the sending
   * @return Error message, if any, or <b>null</b> if no error encountered.
   */
  protected String sendMessage(SenderProperties props)
  {
    String retValue = null;
    try
    {
      sendFile(props);
    }
    catch (ValidationException ex)
    {
      retValue = ErrorUtil.getErrorMessage(INVALID_INPUT_ARGS, ex);
    }
    catch (ConfigurationException ex)
    {
      retValue = ErrorUtil.getErrorMessage(ADAPTOR_CONFIG_ERROR, ex);
    }
    catch (AdaptorRunException ex)
    {
      retValue = ErrorUtil.getErrorMessage(ADAPTOR_RUN_ERROR, ex);
    }
    catch (AdaptorReturnException ex)
    {
      retValue = ErrorUtil.getErrorMessage(ADAPTOR_RETURNS_ERROR, ex);
    }
    catch (Exception ex)
    {
      retValue = ErrorUtil.getErrorMessage(UNKNOWN_ERROR, ex);
    }
    return retValue;
  }

  /**
   * Main Driver for the Sender program.
   * @param args Arguments to configure the Sender program.
   * Each argument should be specified in the form: <property>=<value>
   * @see SenderProperties
   */
  public static void main(String[] args)
  {
    Sender sender = new Sender();
    try
    {
      sender.start(args);
      System.exit(SUCCESS);
    }
    catch (ValidationException ex)
    {
      ErrorUtil.printErrorMessage(INVALID_INPUT_ARGS, ex);
      System.exit(INVALID_INPUT_ARGS);
    }
    catch (ConfigurationException ex)
    {
      ErrorUtil.printErrorMessage(ADAPTOR_CONFIG_ERROR, ex);
      System.exit(ADAPTOR_CONFIG_ERROR);
    }
    catch (AdaptorRunException ex)
    {
      ErrorUtil.printErrorMessage(ADAPTOR_RUN_ERROR, ex);
      System.exit(ADAPTOR_RUN_ERROR);
    }
    catch (AdaptorReturnException ex)
    {
      ErrorUtil.printErrorMessage(ADAPTOR_RETURNS_ERROR, ex);
      System.exit(ADAPTOR_RETURNS_ERROR);
    }
    catch (Exception ex)
    {
      ErrorUtil.printErrorMessage(UNKNOWN_ERROR, ex);
      System.exit(UNKNOWN_ERROR);
    }

  }
}