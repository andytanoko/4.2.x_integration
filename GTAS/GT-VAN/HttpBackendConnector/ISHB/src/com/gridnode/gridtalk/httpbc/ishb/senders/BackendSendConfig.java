/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackendSendConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 14, 2006        i00107             Created
 * Feb 24 2007    i00107              Support variable command args.
 */

package com.gridnode.gridtalk.httpbc.ishb.senders;

import java.io.File;
import java.util.Properties;

import com.gridnode.util.SystemUtil;

/**
 * @author i00107
 * The configuration containing the properties required for backend send.
 */
public class BackendSendConfig extends Properties
{

  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = 472673464625506628L;
  
  /**
   * The command prefix for a command template property key.
   */
  public static final String CMD_TEMPLATE = "cmd.template.";
  
  /**
   * The property key for the default command template.
   */
  public static final String CMD_TEMPLATE_DEFAULT = "cmd.template.default";
  
  /**
   * The property key for the path to the senderlite jar on the filesystem.
   */
  public static final String SENDER_JAR_PATH = "sender.jar.path";
  
  /**
   * The property key for the working directory to use when running the sender program.
   */
  public static final String WORK_DIR = "working.dir";
  
  /**
   * @param defaults
   */
  public BackendSendConfig(Properties defaults)
  {
    super(defaults);
  }

  /**
   * Get the command template for the specified target. 
   * @param targetId The identifier for the target. This can be based on the partner that the document
   * will be sent to.
   * @return The command template obtained for the target. <b>null</b> if no such template is found.
   */
  public String getCommandTemplate(String targetId)
  {
    return getProperty(CMD_TEMPLATE+targetId, null);
  }
  
  /**
   * Get the default command template to use if no specialized command template for a target. This
   * must be configured as a fallback mechanism.
   * @return The default command template obtained. <b>null</b> if no default command template configured.
   */
  public String getDefaultCommandTemplate()
  {
    return getProperty(CMD_TEMPLATE_DEFAULT, null);
  }
  
  /**
   * Get the working directory for the backend sender program.
   * @return The working directory obtained. If not configured, will use the system temp directory.
   */
  public String getWorkingDir()
  {
    String dir = getProperty(WORK_DIR, null);
    if (dir == null)
    {
      return System.getProperty("java.io.tmpdir");
    }
    return SystemUtil.getWorkingDirPath() + "/" + dir;
  }
  
  /**
   * Get the name of the command file to generate. This should be specific depending on
   * the platform the application is run.
   * @return The command filename.
   */
  public String getCmdFilename()
  {
    if (File.separatorChar == '/')
    {
      return "send.sh";
    }
    else
    {
      return "send.bat";
    }
  }
  
  /**
   * Get the template to invoke the command file. This is specific depending on the platform the application
   * is run.
   * @return The template to invoke the command file.
   */
  public String[] getInvokeCommandTemplate()
  {
    if (File.separatorChar == '/')
    {
      return new String[] {"/bin/sh", "{0}"};
    }
    else
    {
      return new String[]{"{0}"};
    }
  }
  
  /**
   * Get the path of the sender jar program. This must be configured.
   * @return The path of the sender jar program obtained. <b>null</b> if not configured.
   */
  public String getSenderJarPath()
  {
    return getProperty(SENDER_JAR_PATH, null);
  }
}
