package com.gridnode.pdip.base.certificate.helpers;

import java.util.*;
import com.gridnode.pdip.base.transport.helpers.*;
//import com.rsa.certj.*;
//import com.rsa.jsafe.*;

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PKCS12KeyStoreHandler.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 28 April 2003  Qingsong            Initial creation for GTAS 2.0
 * 12 July 2009   Tam Wei Xiang       #560 Commented since no class reference it
 */


public class PKCS12KeyStoreHandler extends JavaKeyStoreHandler
{
  /*
  boolean opened = false;
  public PKCS12KeyStoreHandler(String pkcs12Filename, String password)
  {
    try
    {
      setPKCS12Keystore();
      open(pkcs12Filename, password);
      opened = true;
    }
    catch (Exception ex)
    {
    }
  }

  public PKCS12KeyStoreHandler(String pkcs12Filename, char[] password)
  {
    this(pkcs12Filename, new String(password));
  }

  public void read() throws Exception
  {
    if(!opened)
      throw new Exception("read: cannot load the file");
  }

 public JSAFE_PrivateKey getPrivateKey()
  {
    try
    {
      Vector alias = getAlias();
      return JavaKeyStoreHelper.convertPrivateKey(getKey((String)alias.get(0)));
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public com.rsa.certj.cert.X509Certificate[] getCertificates()
  {
    try
    {
      Vector alias = getAlias();
      return JavaKeyStoreHelper.convertCertificateChain(getCertChain((String)alias.get(0)));
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public com.rsa.certj.cert.X509Certificate getCertificate()
  {
    try
    {
      Vector alias = getAlias();
      return JavaKeyStoreHelper.convertCertificate(getCert((String)alias.get(0)));
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public void setServiceName(String serviceName)
  {
  }

  public void setCertJ(CertJ certJ)
  {
  }

public static void main(String[] args)  throws Exception
  {
    PKCS12KeyStoreHandler PKCS12KeyStoreHandler1 = new PKCS12KeyStoreHandler("c:\\p12.p12", "changeit");
    System.out.println(PKCS12KeyStoreHandler1);
  } */
}