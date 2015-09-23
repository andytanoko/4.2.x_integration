/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConvertorHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 23 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.xml.helpers;

import java.io.File;

import com.gridnode.convertor.ConvertorFacade;
import com.gridnode.convertor.rules.RulesFile;
import com.gridnode.convertor.rules.RulesFileReader;

/**
 * Helper class for Convertor.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ConvertorHelper
{
  public static File convert(ConvertorFacade convertor,
                             String inputFile,
                             String ruleFile)
    throws Exception
  {
    RulesFileReader reader = new RulesFileReader();
    RulesFile rule = reader.loadRules(ruleFile, null);
    String ext = rule.getOutputFileExt();
    if (ext == null)
    {
      ext = UtilHelper.getOriginalFileExt(inputFile);
    }
    String tempFilename = UtilHelper.generateRandomFileName();
    File tempFile = File.createTempFile(tempFilename, "."+ext);
    String outputFile = tempFile.getAbsolutePath();

    convertor.convert(inputFile, ruleFile, outputFile);
    return tempFile;
  }

}