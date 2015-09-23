/**
 * This software is the proprietary information of CrimsonLogic eTrade Services Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006-2009 (C) CrimsonLogic eTrade Services Pte Ltd. All Rights Reserved.
 *
 * File: TestGridCertUtilities.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 28, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.security.helpers;

import java.io.File;
import java.math.BigInteger;
import java.security.cert.X509Certificate;

import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;

/**
 * @author Tam Wei Xiang
 * @version GT4.2.1
 * @since GT4.2.1
 */
public class TestGridCertUtilities
{

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    // TODO Auto-generated method stub
    //loadX509CertFromByte();
    
    String s = com.gridnode.pdip.base.security.helpers.GridCertUtilities.getSecurityProvider();
    com.gridnode.pdip.base.security.helpers.GridCertUtilities.getSecurityProvider();
    com.gridnode.pdip.base.security.helpers.GridCertUtilities.getSecurityProvider();
    com.gridnode.pdip.base.security.helpers.GridCertUtilities.getSecurityProvider();
  }

  private static void loadX509CertFromByte()
  {
    File certFile = new File("c:/seagateE2open.cer");
    byte[] certInByte = GridCertUtilities.loadFileToByte(certFile);
    
    //Java
    X509Certificate cert = com.gridnode.pdip.base.security.helpers.GridCertUtilities.loadX509Certificate(certInByte);
    System.out.println("Java Cert: "+cert);
    
    BigInteger serialNum = cert.getSerialNumber();
    System.out.println("Serial num: "+convertByteToHex(serialNum.toByteArray()));
    
  }
  
  private static String convertByteToHex(byte[] byteArray)
  {
    StringBuilder serialNumInHex = new StringBuilder();
    for(int i = 0; byteArray != null && byteArray.length > i; i++)
    {
      serialNumInHex.append(GridCertUtilities.hexEncode(byteArray[i]));
      if(! ((i+1) > byteArray.length) )
      {
        serialNumInHex.append(" ");
      }
    }
    return serialNumInHex.toString();
  }
}
