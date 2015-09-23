/**
 * This software is the proprietary information of CrimsonLogic eTrade Services Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006-2009 (C) CrimsonLogic eTrade Services Pte Ltd. All Rights Reserved.
 *
 * File: SecurityServices.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 25, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.certificate.helpers;

/**
 * Port some of the method from SecurityDB
 * 
 * @author Tam Wei Xiang
 * @version GT4.2.1
 * @since GT4.2.1
 */
public class SecurityServices
{
  private static String DEF_PRIVATE_PASSWORD = "GridNode";
  private static boolean issetPassword = false;
  protected static String privatepassword = null;
  
  public static void setPrivatepassword(String privatepassword)
  {
    CertificateLogger.log("[SecurityDB] Setting private password...");
    privatepassword = privatepassword;
    issetPassword = true;
  }

  public static boolean isPasswordset()
  {
    CertificateLogger.log("[SecurityDB] is password set...");
    return issetPassword;
  }
  
  public static String getPrivatePassword()
  {
    CertificateLogger.log("[SecurityDB] Getting private password...");
    return (privatepassword == null ? DEF_PRIVATE_PASSWORD : privatepassword);
  }
}
