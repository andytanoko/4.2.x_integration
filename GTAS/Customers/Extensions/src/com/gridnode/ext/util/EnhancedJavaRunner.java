/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2004 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EnhancedJavaRunner.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 14 2004    Neo Sok Lay         Created
 */
package com.gridnode.ext.util;

import java.io.File;

/**
 * Enhanced JavaAppRunner. It reads a new format of start config file.
 *
 * @since GT 2.3 I1
 * @version GT 2.3 I1
 */
public class EnhancedJavaRunner extends JavaAppRunner
{
  protected EnhancedRunnerConfig _config;

  /**
   * Constructs an EnhancedJavaRunner.
   *
   * @param config The startup configuration file
   */
  public EnhancedJavaRunner(EnhancedRunnerConfig config)
  {
    super(false);
    _config = config;
    startupProp = config; //accessed by superclass
  }


  /**
   * Override to construct classpath differently from parent runner.
   *
   * @see com.gridnode.ext.util.JavaAppRunner#constructClassPath()
   */
  protected boolean constructClassPath()
  {
    String[] libNames = _config.getClasspathLibNames();
    String workDirKey =
      _config.getSubstitutionPropertyKey(EnhancedRunnerConfig.KEY_WORKING_DIR);

    for (int i = 0; i < libNames.length; i++)
    {
      String libDir = _config.getLibDir(libNames[i]);
      String[] libset = _config.getLibSet(libNames[i]);
      libDir = StringUtil.substitute(libDir, workDirKey, workDir);
      for (int j = 0; j < libset.length; j++)
      {
        classPath.append(libDir).append(File.separator).append(libset[j]);
        classPath.append(File.pathSeparator);
      }
    }
    return true;
  }

  /**
   * Override this com.gridnode.ext.util.JavaAppRunner method
   * to customize own reading of startup prop
   */
  protected boolean readStartupProp()
  {
    return true;
  }

  /**
   * Substitute parts of a string for every occurrence of a property key marker
   *
   * @param str the string to undo substitution.
   * @param propertyKey The property key to look for. Property key markers are
   * of the form: ${<propertyKey>}. Currently only valid for "adaptor.dir" and
   * "payload.file"
   */
  protected String substitute(String str, String propertyKey)
  {
    String substKey = _config.getSubstitutionPropertyKey(propertyKey);
    String substVal = _config.getProperty(substKey);
    return StringUtil.substitute(str, substKey, substVal);
  }

  /**
   * Override this com.gridnode.ext.util.JavaAppRunner method
   * for setup tasks before actual running of the Java application.
   */
  protected boolean setup()
  {
    String workDir = _config.getProperty(EnhancedRunnerConfig.KEY_WORKING_DIR, null);
    if (!StringUtil.isBlank(workDir))
      this.workDir = workDir;

    _config.putSubstitutionProperty(EnhancedRunnerConfig.KEY_WORKING_DIR, this.workDir);

    return true;
  }

}