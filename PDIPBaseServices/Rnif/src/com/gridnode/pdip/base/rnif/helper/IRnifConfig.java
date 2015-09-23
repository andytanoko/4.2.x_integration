/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRnifConfig.java
 *
 * *****************************************************************
 * Date             Author                  Changes
 * *****************************************************************
 * Feb 28 2003      Guo Jianyu              Created
 * Apr 02 2003      Guo Jianyu              Added SIGN_CERT_TAKEOVER
 * Aug 01 2008		Wong Yee Wah			#38  remarked method
 */
package com.gridnode.pdip.base.rnif.helper;

public interface IRnifConfig
{
  public static final String CONFIG_NAME = "rnif_base";

  //WYW 20080801 has been moved to com.gridnode.pdip.base.certificate.helpers.ICertificateConfig
  //public static final String SIGN_CERT_TAKEOVER = "SignCertTakeover";
  
}