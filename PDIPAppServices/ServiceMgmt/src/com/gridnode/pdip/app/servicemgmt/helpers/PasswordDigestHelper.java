/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PasswordDigestHelper.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Feb 11, 2004 			Mahesh             	Created
 */
package com.gridnode.pdip.app.servicemgmt.helpers;

import com.gridnode.pdip.app.servicemgmt.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class PasswordDigestHelper
{
  private String _algorithm;
  
  private static PasswordDigestHelper _self=new PasswordDigestHelper();
  
  private PasswordDigestHelper()
  {
    try
    {
      Configuration config =ConfigurationManager.getInstance().getConfig(IServiceMgmtConfig.CONFIG_NAME);
      _algorithm = config.getString(IServiceMgmtConfig.PASSWORD_DIGEST_ALGORITHM);
    }
    catch(Throwable th)
    {
      Logger.error(ILogErrorCodes.SERVICEMGMT_PASSWORD_DIGESTER_HELPER_INIT,"[PasswordDigestHelper.init] Unexpected Error: "+th.getMessage(),th);
    }
  }
  
  public static PasswordDigestHelper getInstance()
  {
    return _self;
  }

  public String getEncodedDigest(String password) throws ApplicationException
  {
    try
    {
      if(password==null || password.trim().length()==0)
        throw new NullPointerException("Password is null");
      if(_algorithm==null || _algorithm.trim().length()==0)
        throw new NullPointerException("Algorithm for MessageDigest is null, algorithm="+_algorithm);
      byte data[] = GridCertUtilities.getMessageDigest(_algorithm,password.getBytes());
      //return binaryToHex(data); this is for seperate tomcat
      return GridCertUtilities.encode(data); // this is for embedded tomcat
    }
    catch(Throwable th)
    {
      throw new ApplicationException("Error in getEncodedDigest for password, algorithm="+_algorithm,th);
    }
  }
  
  public static String binaryToHex(byte data[]) {
    StringBuffer hexBuf=new StringBuffer();
    for(int i = 0;i<data.length;i++)
    {
      //int n = data[i] & 0x00ff;  
      //hexBuf.append(Integer.toHexString(n));
      hexBuf.append(Integer.toHexString((data[i] >>> 4) & 0x0F))
            .append(Integer.toHexString(data[i] & 0x0F));
      
    }
    return hexBuf.toString();
  }

  public static void main(String args[]) throws Throwable
  {
    byte data[] = GridCertUtilities.getMessageDigest("MD5","mahesh".getBytes());
    String str = binaryToHex(data);
    System.out.println(str);
    
  }


}


