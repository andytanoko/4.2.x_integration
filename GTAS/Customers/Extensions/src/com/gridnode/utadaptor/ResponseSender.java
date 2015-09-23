/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2004 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResponseSender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 14 2004    Neo Sok Lay         Created
 */
package com.gridnode.utadaptor;

import com.gridnode.ext.util.EnhancedJavaRunner;
import com.gridnode.ext.util.EnhancedRunnerConfig;

/**
 * This is a driver for running another Java application to send a document,
 * using parameters like partner.id, payload.file, response.doctype.
 *
 * @since GT 2.3 I1
 * @version GT 2.3 I1
 */
public class ResponseSender extends EnhancedJavaRunner
{
  public ResponseSender(EnhancedRunnerConfig config)
  {
    super(config);
  }

  /**
   * Override to allow subsitution parameters in the application params specified.
   *
   * @see com.gridnode.ext.util.JavaAppRunner#getAppParams()
   */
  protected String getAppParams()
  {
    String appParamStr = super.getAppParams();
    appParamStr = substitute(appParamStr, SenderProperties.KEY_RESPONSE_DOC_TYPE);
    appParamStr = substitute(appParamStr, SenderProperties.KEY_PAYLOAD_FILE);
    appParamStr = substitute(appParamStr, SenderProperties.KEY_PARTNER_ID);
    appParamStr = substitute(appParamStr, EnhancedRunnerConfig.KEY_WORKING_DIR);
    return appParamStr;
  }

}