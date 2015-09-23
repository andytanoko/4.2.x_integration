/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AdaptorRunner.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2003    Neo Sok Lay         Created
 * Jan 14 2004    Neo Sok Lay         Refactor to extend from EnhancedJavaRunner
 */
package com.gridnode.utadaptor;

import com.gridnode.ext.util.EnhancedJavaRunner;

/**
 * This is the driver for running another Java application in separate JVM.
 * Specifically, this runner accepts only AdaptorRunConfig to configure the
 * startup properties for running the UCCnet Adaptor.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since GT 2.3 I1
 */
public class AdaptorRunner extends EnhancedJavaRunner
{

  /**
   * Constructs an AdaptorRunner.
   *
   * @param adaptorConfig The configuration to use to run the Adaptor.
   */
  public AdaptorRunner(AdaptorRunConfig adaptorConfig)
  {
    super(adaptorConfig);
  }

  /**
   * Override to allow subsitution parameters in the application params specified.
   *
   * @see com.gridnode.ext.util.JavaAppRunner#getAppParams()
   */
  protected String getAppParams()
  {
    String appParamStr = super.getAppParams();
    appParamStr = substitute(appParamStr, SenderProperties.KEY_PAYLOAD_FILE);
    appParamStr = substitute(appParamStr, SenderProperties.KEY_ADAPTOR_RUN_DIR);
    appParamStr = substitute(appParamStr, AdaptorRunConfig.KEY_WORKING_DIR);
    return appParamStr;
  }

  protected boolean setup()
  {
    //adaptor.dir is mandatory, thus can directly set as working directory
    workDir = _config.getSubstitutionProperty(SenderProperties.KEY_ADAPTOR_RUN_DIR);

    //allow working.dir be used for substitution
    _config.putSubstitutionProperty(AdaptorRunConfig.KEY_WORKING_DIR, workDir);
    return true;
  }

}