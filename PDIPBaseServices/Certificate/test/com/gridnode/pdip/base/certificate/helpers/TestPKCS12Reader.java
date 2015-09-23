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
 * Jun 16, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.certificate.helpers;

import java.io.File;
import java.math.BigInteger;

/**
 * @author Tam Wei Xiang
 * @version GT4.2.1
 * @since GT4.2.1
 */
public class TestPKCS12Reader
{
  public static void main(String[] args) throws Exception
  {
    //read certificate, private key from .p12.
    //check whether the key match
    readPKCSFile();
    
    testBigInteger();
  }
  
  private static void readPKCSFile() throws Exception
  {
    File p12 = new File("data/pkcs12/sample.p12");
    char[] password = "password".toCharArray();
    PKCS12Reader reader = new PKCS12Reader(p12.getAbsolutePath(), password);
    reader.read();
    
    //reader.read();
  }
  
  private static void testBigInteger()
  {
    String s = "test";
    System.out.println(new BigInteger(s.getBytes()));
    
    byte[] b = s.getBytes();
    for(byte b1 : b)
    {
      System.out.println("byte:"+b1);
    }
  }
}
