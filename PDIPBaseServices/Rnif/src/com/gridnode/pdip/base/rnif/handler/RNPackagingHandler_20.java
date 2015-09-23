package com.gridnode.pdip.base.rnif.handler;

import com.gridnode.pdip.base.packaging.helper.IPackagingInfo;

public class RNPackagingHandler_20 extends RNPackagingHandler
{
  public static final String SENDER_DUNS= "SenderDUNS";
  public static final String RNDOC_FLAG= "IS_RN_DOC";

  private static final String CLASS_NAME= "RNPackagingHandler_20";


  public RNPackagingHandler_20()
  {
    super(IPackagingInfo.RNIF2_ENVELOPE_TYPE);
  }

  protected String getClassName()
  {
    return CLASS_NAME;
  }
/*
  public Hashtable getHTTPHeaders(RNPackInfo packinfo, Hashtable headers)
  {
    GNTransportHeader header= new GNTransportHeader(headers);
    header.setRNVersion("RosettaNet/V02.00");
    header.setRNSyncMessageID(packinfo.getRNMsgId());
    header.setRNReplyMessage(!packinfo.getIsRequestMsg());
    header.setRNSyncMessage(packinfo.getIsSynchronous());
   // header.setContentType("multipart/related");
    header.setType(packinfo.getIsEnableSignature() ? "multipart/signed" : "multipart/related");


    //    } else if (ProcessUtil.isRNIF11(def))
    //    {
    //             headers.put( "Content-type", "application/x-rosettanet-agent; version=1.0");
    //        }
    //        return headers;
    //    }
    return header.getProperties();
  }
*/
}