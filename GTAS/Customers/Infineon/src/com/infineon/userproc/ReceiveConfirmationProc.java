/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveConfirmationProc.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 04 2003    Neo Sok Lay         Created
 * Mar 10 2003    Neo Sok Lay         Generate a PDF report, based on the
 *                                    received confirmation document, which
 *                                    will be attachment in the alert email.
 * Oct 27 2003    Neo Sok Lay         Modify sendEmailNotification() method
 *                                    signature to accept Object instead of
 *                                    GridDocument.
 * Nov 23 2005    Neo Sok Lay         Change manner of providing alert attachment.                                   
 */
package com.infineon.userproc;

import com.gridnode.ext.util.JavaAppRunner;

import com.infineon.userproc.helpers.ReportConfig;
import com.infineon.userproc.helpers.ServiceLookupHelper;
import com.infineon.userproc.helpers.Logger;

import com.gridnode.gtas.server.document.helpers.FileHelper;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.userprocedure.exceptions.AbortUserProcedureException;
import com.gridnode.gtas.server.docalert.helpers.AlertHelper;
import com.gridnode.gtas.server.docalert.helpers.IAttachmentProvider;

import com.gridnode.pdip.framework.log.FacadeLogger;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.io.File;

/**
 * This User Procedure is written for the Infineon Project.
 * <p>
 * When Infineon GTAS receives a PO confirmation, the system will send an
 * email notification, attaching the PO confirmation document, to the Infineon
 * Buyers.<p>
 * The PO confirmation document can be converted into a different format, e.g.
 * PDF or CSV formats, and sent as the attachment in the email notification. To
 * do this, the report configuration options have to be specified in the
 * "receive-conf.properties" file for the PO confirmation document type. The
 * following options have to be specified:
 * <ul>
 *   <li>doctypes - List of document types (delimited by comma) of documents that
 *   need to convert into a different format.</li>
 *   <li>dsm - The Data Source manager file for report generations</li>
 *   <li>[doctype].template - The Report template for that document type.</li>
 *   <li>[doctype].urlKey - The parameter key in the Report template to replace
 *   with the url of the source document for generating report</li>
 *   <li>[doctype].format - The target report format e.g. PDF, CSV, TXT.
 * </li>
 * This User Procedure works for Xml documents only.
 * <p>
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1
 * @since 2.0 I7
 */
public class ReceiveConfirmationProc
{
  private static final String CONFIG_FILE = "receive-conf.properties";

  /**
   * If the receive document is an expected document response, then
   * an email notification will be dispatched (depends on the alert
   * configurations).
   *
   * @param gdocObj The GridDocument object.
   * @throws Exception Error in execution.
   */
  public void sendEmailNotification(Object gdocObj)
    throws Exception
  {
    FacadeLogger logger = FacadeLogger.getLogger(
                            "ReceiveConfirmationProc",
                            Logger.CATEGORY_MAIN);
    String methodName   = "sendEmailNotification";
    Object[] params     = { gdocObj };

    try
    {
      logger.logEntry(methodName, params);

      GridDocument gdoc = (GridDocument)gdocObj;

      // If the receive document is an expected document response, then
      // an email notification will be dispatched (depends on the alert
      // configurations).
      ServiceLookupHelper.getDocAlertMgr().invalidateResponseTracking(
        gdoc,
        getAttachmentProvider());
        //getAttachmentMethod(),
        //this);
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new AbortUserProcedureException("Error in [ReceiveConfirmationProc.sendEmailNotification]", t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Get the Method that would return the filename of the attachment for the
   * alert email.
   *
   * @return The Method for <code>formatAttachmentFile()</code>.
   */
//  private Method getAttachmentMethod() throws Exception
//  {
//    return getClass().getMethod("formatAttachmentFile",
//                                new Class[] {GridDocument.class});
//  }

  private IAttachmentProvider getAttachmentProvider()
  {
    return new IAttachmentProvider()
    {
      /**
       * Serial version UID
       */
      private static final long serialVersionUID = 9164731126347295231L;

      public String getAlertAttachment(GridDocument gdoc) throws Exception
      {
        return formatAttachmentFile(gdoc);
      }
    };
  }
  
  /**
   * Formats the received document for email attachment. The formatting will be
   * according to the Report Configurations specified in "received-conf.properties"
   * file for the type of document. If no formatting options are specified, the
   * user document will be used as the attachment.
   * <p>
   * This method is callbacked from the DocAlert module for obtaining
   * the attachment file.
   *
   * @param gdoc The GridDocument of the received document.
   * @return Filename of the attachment file.
   */
  public static String formatAttachmentFile(GridDocument gdoc)
    throws Exception
  {
    FacadeLogger logger = FacadeLogger.getLogger(
                            "ReceiveConfirmationProc",
                            Logger.CATEGORY_MAIN);
    String methodName   = "formatAttachmentFile";
    Object[] params     = { gdoc };

    try
    {
      logger.logEntry(methodName, params);

      ReportConfig config = ReportConfig.getReportConfig(CONFIG_FILE);

      String attachment = AlertHelper.getUdocFilename(gdoc);

      String docType = gdoc.getUdocDocType();
      boolean found  = config.isDocTypeExists(docType);

      if (found)
      {
        // generate report according to configured options
        attachment = generateReport(docType, attachment, config);
      }
      //else use the original udoc by default

      return attachment;
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new AbortUserProcedureException(
        "Error in [ReceiveConfirmationProc.formatAttachmentFile]", t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  private static final String FORMAT_PDF = "PDF";
  //private static final String FORMAT_TEXT = "TXT";
  //private static final String FORMAT_CSV = "CSV";

  /**
   * Generate report according to the configuration options for the output
   * report.
   *
   * @param docType The document type of the received document.
   * @param udocFilename Full pathname of the received document.
   * @param config The configuration options for each type of report.
   * @return Absolute filename of the generated report.
   */
  private static String generateReport(
    String docType, String udocFilename, ReportConfig config)
    throws Throwable
  {
    FacadeLogger logger = FacadeLogger.getLogger(
                            "ReceiveConfirmationProc",
                            Logger.CATEGORY_MAIN);
    String methodName   = "generateReport";
    Object[] params     = { docType, udocFilename, config };

    boolean generateSuccess = false;
    try
    {
      logger.logEntry(methodName, params);

      String format = config.getFormat(docType);
      String attachmentFilename = getTempFilename(format);
      String reportProperties = writeReportProperties(
                                  docType, udocFilename, config,
                                  attachmentFilename, format);
      String library = config.getLibrary();
      generateSuccess = generateReport(reportProperties, library);

      if (generateSuccess)
        return attachmentFilename;
      else
      {
        throw new Exception("Report generation fails!");
      }
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, "Unable to generate report", t);
      throw new ApplicationException("Unable to generate report: "+t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Get full pathname of a file under the directory for user procedures.
   *
   * @param shortFn The short filename of the file.
   * @return The full pathname of the file.
   */
  private static String getFullFilename(String shortFn) throws Exception
  {
    String fn = FileUtil.getFile(ReportConfig.CONFIG_PATH, shortFn, "").getCanonicalPath();
    return fn;
  }

  /**
   * Obtain a temporary filename for the report.
   *
   * @param udocFilename Full pathname of the received document.
   * @param extension The desired file extension for the report.
   * @return The destination filename for the report to be generated -- this will
   * be prefixed with the short filename of the received document, and suffixed
   * by the <code>extension</code> specified. A time factor is included as part
   * of the name to prevent concurrency issue.
   */
  private static synchronized String getTempFilename(String extension) throws Exception
  {
    StringBuffer buff = new StringBuffer().append(System.currentTimeMillis());
    String fileExt = new StringBuffer(".").append(extension).toString();

    File tempFile = File.createTempFile(buff.toString(), fileExt);
    tempFile.deleteOnExit();

    return tempFile.getCanonicalPath();
  }

  /**
   * Write out the ReportProperties file to a temporary location.
   *
   * @param docType The Document type of the input document.
   * @param udocFilename Filename of the input document.
   * @param config The configuration options for each type of document.
   * @param attachmentFilename Name of the report that is to be generated.
   * @param format The file type of the report.
   * @return The filename of the written ReportProperties file.
   */
  private static synchronized String writeReportProperties(
    String docType, String udocFilename, ReportConfig config,
    String attachmentFilename, String format)
    throws Exception
  {
    String dsm = config.getDSM();
    String urlKey = config.getUrlKey(docType);
    String template = config.getTemplate(docType);

    Properties props = new Properties();
    props.setProperty(ReportConfig.REPORT_DATASOURCE_MODELS, getFullFilename(dsm));
    props.setProperty(ReportConfig.REPORT_DATASOURCE_NAME, urlKey);
    props.setProperty(ReportConfig.REPORT_INPUT_FILES, udocFilename);
    props.setProperty(ReportConfig.REPORT_INPUT_TYPE, "xml");
    props.setProperty(ReportConfig.REPORT_OUTPUT_FILES, attachmentFilename);
    props.setProperty(ReportConfig.REPORT_OUTPUT_TYPE, format);
    props.setProperty(ReportConfig.REPORT_TEMPLATE, getFullFilename(template));

    String propsFile = getTempFilename("properties");
    props.store(new FileOutputStream(propsFile), null);

    return propsFile;
  }

  /**
   * Generate the report by calling the report extension library as a standalone
   * program.
   *
   * @param reportProperties Filename of the ReportProperties file.
   * @param library The filename of the report extension library to call,
   * relative to the userprocedure directory.
   */
  private static boolean generateReport(
    String reportProperties, String library) throws Exception
  {
    String command = getRunReportGeneratorCommand(library) + " \"" + reportProperties + "\"";
    return JavaAppRunner.runApp(command, true);
  }

  private static String getRunReportGeneratorCommand(String library) throws Exception
  {
    String jarPath = " -jar \"" + getFullFilename(library) + "\"";
    String endorsedDir = " -Djava.endorsed.dirs=\""+getFullFilename("report/endorsed") +"\"";
    return getJavaExecutable() + endorsedDir + jarPath;
  }

  private static String getJavaExecutable()
  {
    return "\"" + JavaAppRunner.getJavaExeName() + "\"";
  }


}