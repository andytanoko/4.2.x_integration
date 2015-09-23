/**
 * This software is the proprietary information of CrimsonLogic eTrade Services Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006-2009 (C) CrimsonLogic eTrade Services Pte Ltd. All Rights Reserved.
 *
 * File: TestPKCS12Reader.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 30, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.security.helpers;

import java.io.File;


/**
 * @author Tam Wei Xiang
 * @version GT4.2.1
 * @since GT4.2.1
 */
public class TestPKCS12Reader
{
  public static void main(String[] args) throws Exception
  {
    loadPKCS12();
  }
  
  private static void loadPKCS12() throws Exception
  {
    File p12 = new File("data/pkcs12/sample.p12");
    char[] password = "password".toCharArray();
    PKCS12Reader reader = new PKCS12Reader(p12.getAbsolutePath(), password);
    //reader.readBC();
    
    reader.read();
  }
}
