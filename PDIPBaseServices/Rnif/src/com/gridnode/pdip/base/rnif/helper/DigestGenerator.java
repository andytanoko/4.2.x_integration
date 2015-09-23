package com.gridnode.pdip.base.rnif.helper;

import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class DigestGenerator
{
  public DigestGenerator()
  {
  }

  static ICertificateManagerObj getCertMgr() throws ServiceLookupException
  {
    return (ICertificateManagerObj) ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
      ICertificateManagerHome.class.getName(),
      ICertificateManagerHome.class,
      new Object[0]);
  }

  public byte[] getMsgDigest(byte[] content, String alg)
  {
    //String digestAlg = null;

//    if("SHA1".equals(alg))
//    {
//      digestAlg = GridCertUtilities.MSG_DIGEST_SHA1;
//    }else
//    {
//      if(!"MD5".equals(alg))
//      {
//        Logger.err("[SecurityUtil.getMsgDigest]digest alg:["+ alg + "]is not supported, use MD5 instead!", new Exception());
//      }
//      digestAlg = GridCertUtilities.MSG_DIGEST_MD5;
//    }
    return GridCertUtilities.getMessageDigest(alg, content);
  }

 public String getEncodedDigest(byte[] digest)
  {
     return GridCertUtilities.encode(digest);
  }

 public String getEncodedDigest(byte[] content, String alg)
 {
  return getEncodedDigest(getMsgDigest(content, alg));
 }

 public byte[] getDecodedDigest(String digestStr)
 {
     return GridCertUtilities.decode(digestStr);
  }


}
