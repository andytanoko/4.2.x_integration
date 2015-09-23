/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All rights reserved.
 *
 * File: RNUnpackagingHandler.java
 *
 * **********************************************************
 * Date           Author            Changes
 * **********************************************************
 *
 * Feb 25 2003    Jagadeesh       Modififed: To implement IRNIFPackaignHandler
 *                                from RNIF base services.
 * Dec 20 2003   Jagadeesh        Modified: To Refactor to use Messaging Framework.
 * Jan 29 2004    Neo Sok Lay     Refactored pack() to factor out the
 *                                setMessageDigest() for reuse.
 * Feb 18 2004   Jagadeesh        Modified: To use getEnvelopeType(), to return the
 *                                EnvelopeType - Ex: RNIF1, RNIF2.
 */

package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.base.packaging.exceptions.PackagingException;
import com.gridnode.pdip.base.packaging.helper.PackagingInfo;
import com.gridnode.pdip.base.rnif.handler.IRNPackagingHandler;
import com.gridnode.pdip.base.rnif.handler.RNPackagingHandler;
import com.gridnode.pdip.base.rnif.helper.RNCertInfo;
import com.gridnode.pdip.base.rnif.helper.SecurityInfoFinder;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.messaging.Message;

public class RNUnpackagingHandler implements IRNPackagingHandler
{

  public RNUnpackagingHandler()
  {

  }


  public Message pack(PackagingInfo info, Message message)
    throws PackagingException
  {
    //String envelopeType = (String)message.getCommonHeaders().get(ICommonHeaders.PAYLOAD_TYPE);
    String envelopeType = info.getEnvelopeType();
    SecurityInfoFinder securityInfoFinder= new RNSecurityInfoFinder();

    RNPackagingHandler handler = new RNPackagingHandler(envelopeType);
    handler.setSecurityInfoFinder(securityInfoFinder);
    message = handler.pack(info,message);
    //info= handler.packageAndEnvelope(info); /** @todo invoke pack() method */
    RNPackInfo packInfo= securityInfoFinder.getPackInfo();
    try
    {
      setMessageDigest(packInfo);
    }
    catch (RnifException e)
    {
      Logger.warn("Cannot set Msg digest", e);
      throw new PackagingException("Cannot set Msg digest", e);
    }
    return message;
  }

  public static void setMessageDigest(RNPackInfo packInfo) throws RnifException
  {
    if (packInfo != null)
    {
      String msgDigest= packInfo.getMsgDigest();
      if (msgDigest != null)
      {
        ProfileUtil profileUtil = new ProfileUtil();
        Long profileUid= packInfo.getRNProfileUid();
        RNProfile profile= profileUtil.getProfile(profileUid);
        profile.setMsgDigest(msgDigest);
        profileUtil.updateProfile(profile);
      }
    }
  }

  public Message unPack(PackagingInfo info, Message message)
    throws PackagingException
  {

    SecurityInfoFinder securityInfoFinder= new RNSecurityInfoFinder();
    String envelopeType = info.getEnvelopeType();
    RNPackagingHandler handler= new RNPackagingHandler(envelopeType);
    handler.setSecurityInfoFinder(securityInfoFinder);
    return handler.unPack(info,message);
    //return handler.unPackage(info); /** @todo To invoke upPack at base services. */
    //return message;
  }




  public PackagingInfo packageAndEnvelope(PackagingInfo info) throws PackagingException
  {
    SecurityInfoFinder securityInfoFinder= new RNSecurityInfoFinder();
    RNPackagingHandler handler = new RNPackagingHandler(info.getEnvelopeType());
    handler.setSecurityInfoFinder(securityInfoFinder);
    info= handler.packageAndEnvelope(info);
    RNPackInfo packInfo= securityInfoFinder.getPackInfo();
    if (packInfo != null)
    {
      String msgDigest= packInfo.getMsgDigest();
      if (msgDigest != null)
      {
        try
        {
          ProfileUtil profileUtil = new ProfileUtil();
          Long profileUid= packInfo.getRNProfileUid();
          RNProfile profile= profileUtil.getProfile(profileUid);
          profile.setMsgDigest(msgDigest);
          profileUtil.updateProfile(profile);
        }
        catch (RnifException ex)
        {
          Logger.warn("Cannot get the RNProfile to set Msg digest", ex);
          throw new PackagingException("Cannot get the RNProfile to set Msg digest", ex);
        }
      }
    }
    return info;

  }

  public PackagingInfo unPackage(PackagingInfo info) throws PackagingException
  {
    SecurityInfoFinder securityInfoFinder= new RNSecurityInfoFinder();

    RNPackagingHandler handler= new RNPackagingHandler(info.getEnvelopeType());
    handler.setSecurityInfoFinder(securityInfoFinder);
    return handler.unPackage(info);

  }

  static class RNSecurityInfoFinder implements SecurityInfoFinder
  {
    RNPackInfo _packInfo= null;
    String _partnerId= null;

    public RNCertInfo getSecurityInfoFromDUNS(String dunsNum) throws Exception
    {
      Partner partner= EnterpriseUtil.get1stPartner4DUNS(dunsNum);
      if (partner == null)
        throw new FindEntityException(
          "[RNSecurityInfoFinder.getSecurityInfoFromDUNS]Cannot find Enabled Partner based on DunsNum "
            + dunsNum);
      _partnerId= partner.getPartnerID();
      return ProcessUtil.getBizCertMappingForPartner(_partnerId);
    }

    public void setPackInfo(RNPackInfo packinfo)
    {
      _packInfo= packinfo;
    }

    public RNPackInfo getPackInfo()
    {
      return _packInfo;
    }

    public void setPartnerId(String partnerId)
    {
      _partnerId= partnerId;
    }

    //called during unpackage processing
    public ProcessAct getCurAct(ProcessDef def, boolean isInitiator, boolean isSignal)
    {
      boolean isRequestAct = isInitiator == isSignal;
      return isRequestAct ? def.getRequestAct() : def.getResponseAct();
    }

    public String getDigestAlgorithm(RNPackInfo packinfo)
    {
      String receiverDUNS= packinfo.getReceiverGlobalBusIdentifier();
      String senderDUNS= packinfo.getSenderGlobalBusIdentifier();
      String originatorDUNS= packinfo.getPartnerGlobalBusIdentifier();

      String initiatorId= null;
      String instanceId= null;
      String responderId= null;
      Boolean isInitiator= null;

      if (receiverDUNS.equals(originatorDUNS))
      {
        isInitiator= Boolean.TRUE;
      }
      else
      {
        isInitiator= Boolean.FALSE;
        }

      String versionId= packinfo.getPIPVersionIdentifier();
      String gProcessCode= packinfo.getPIPGlobalProcessCode();

      String defName= null;
      ProcessDef def= null;
      try
      {
        defName=
          ProcessUtil.getProcessDefName(gProcessCode, versionId, _partnerId, isInitiator, null);
        def= ProcessUtil.getProcessDef(defName);
      }
      catch (Exception ex)
      {
        Logger.warn(
          "Error occured when trying to get the ProcessDef to retrieve diestAlgrithm, use SHA1",
          ex);
        return "SHA1";
      }
      boolean isSignal= packinfo.getIsSignalDoc();
      ProcessAct curAct= getCurAct(def, isInitiator.booleanValue(), isSignal);
      String alg= curAct.getDigestAlgorithm();
      if (null == alg || alg.length() == 0)
      {
        Logger.warn("ProcessAct " + curAct.getUId() + "'s digestAlgrithm is not set, use SHA1");
        return "SHA1";
      }
      return alg;
    }
  }

}
