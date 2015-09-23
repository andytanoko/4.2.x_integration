/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPackagingConfig
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 25-FEB-2003    Jagadeesh           Created.
 * Mar 21 2003    Kan Mun             Modified - Change BackwardCompatiblePackagingHandler
 *                                               to FileSplitPackagingHandler.
 * Oct 29 2003    Guo Jianyu          Added AS2_PACKAGING_HANDLER
 */

package com.gridnode.pdip.base.packaging.helper;

public interface IPackagingConfig
{

  public static final String CONFIG_NAME = "packaging";

  public static final String DEFAULT_PACKAGING_HANDLER = "default.packaging.handler";

  public static final String FILE_SPLIT_PACKAGING_HANDLER = "filesplit.packaging.handler";

  public static final String RN_PACKAGING_HANDLER = "rn.packaging.handler";

  public static final String SOAP_PACKAGING_HANDLER = "soap.packaging.handler";

  public static final String AS2_PACKAGING_HANDLER = "as2.packaging.handler";

}