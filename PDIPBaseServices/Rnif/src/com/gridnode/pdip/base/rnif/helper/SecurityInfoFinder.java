package com.gridnode.pdip.base.rnif.helper;

import com.gridnode.pdip.base.rnif.model.RNPackInfo;

public interface SecurityInfoFinder
{
   RNCertInfo getSecurityInfoFromDUNS(String dunsNumber) throws Exception;
   
   void setPackInfo(RNPackInfo packinfo);
   
   RNPackInfo getPackInfo();
   
   String getDigestAlgorithm(RNPackInfo packinfo);
}
