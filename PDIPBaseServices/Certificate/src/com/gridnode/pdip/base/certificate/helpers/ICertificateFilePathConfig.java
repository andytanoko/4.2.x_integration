/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICertificateFilePathConfig.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 05 2003    Jagadeesh              Created
 * Jan 21 2003    Jagadeesh              Modified: To update path to sync with path.properties
 */


package com.gridnode.pdip.base.certificate.helpers;

import com.gridnode.pdip.framework.file.helpers.IPathConfig;

public interface ICertificateFilePathConfig extends IPathConfig
{

  /**
   * Path At Cert file .cert
   */
  public static final String PATH_IMPORT_CERTIFICATE = "certificate.path.import.cert";

  /**
   * Path at P12 Files.
   */

  public static final String PATH_IMPORT_PKCS12 = "certificate.path.import.pkcs12";

  /**
   * Path at Exported Certificates.
   */

  public static final String PATH_EXPORT_CERTIFICATE = "certificate.path.export.cert";


}


