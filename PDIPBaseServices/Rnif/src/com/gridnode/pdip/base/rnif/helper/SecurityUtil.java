package com.gridnode.pdip.base.rnif.helper;

import java.io.File;

import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class SecurityUtil
{
  
  public static ICertificateManagerObj getCertMgr() throws ServiceLookupException
  {
    return (ICertificateManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      ICertificateManagerHome.class.getName(),
      ICertificateManagerHome.class,
      new Object[0]);
  }
  
 static  public byte[] getMsgDigest(File afile, String alg)
  {
    return null;
  }


}
