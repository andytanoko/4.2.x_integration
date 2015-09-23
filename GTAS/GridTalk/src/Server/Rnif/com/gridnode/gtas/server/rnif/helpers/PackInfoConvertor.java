package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.base.rnif.model.IRNPackInfo;
import com.gridnode.pdip.base.rnif.model.RNPackInfo;


public class PackInfoConvertor
{

  static class FieldIdMap
  {
    String _packinfoId;
    Number _profileId;
    FieldIdMap(String packinfoId, Number profileId)
    {
      _packinfoId = packinfoId;
      _profileId = profileId;
    }
  }


  static FieldIdMap[] strFieldArray= new FieldIdMap[]
  {

    new FieldIdMap(IRNPackInfo.RECEIVER_DOMAIN, RNProfile.RECEIVER_DOMAIN),
    new FieldIdMap(IRNPackInfo.RECEIVER_LOCATION_ID,RNProfile.RECEIVER_LOCATION_ID),
    new FieldIdMap(IRNPackInfo.SENDER_DOMAIN,RNProfile.SENDER_DOMAIN),
    new FieldIdMap(IRNPackInfo.SENDER_LOCATION_ID,RNProfile.SENDER_LOCATION_ID),
    new FieldIdMap(IRNPackInfo.DELIVERY_MESSAGE_TRACKING_ID,RNProfile.DELIVERY_MESSAGE_TRACKING_ID),

    new FieldIdMap(IRNPackInfo.BUS_ACTIVITY_IDENTIFIER,RNProfile.BUS_ACTIVITY_IDENTIFIER),
    new FieldIdMap(IRNPackInfo.FROM_GLOBAL_PARTNER_ROLE_CLASS_CODE,RNProfile.FROM_GLOBAL_PARTNER_ROLE_CLASS_CODE),
    new FieldIdMap(IRNPackInfo.FROM_GLOBAL_BUS_SERVICE_CODE,RNProfile.FROM_GLOBAL_BUS_SERVICE_CODE),
    new FieldIdMap(IRNPackInfo.IN_REPLY_TO_GLOBAL_BUS_ACTION_CODE,RNProfile.IN_REPLY_TO_GLOBAL_BUS_ACTION_CODE),
    new FieldIdMap(IRNPackInfo.IN_REPLY_TO_MESSAGE_STANDARD,RNProfile.IN_REPLY_TO_MESSAGE_STANDARD),
    new FieldIdMap(IRNPackInfo.IN_REPLY_TO_STANDARD_VERSION,RNProfile.IN_REPLY_TO_STANDARD_VERSION),
    new FieldIdMap(IRNPackInfo.IN_REPLY_TO_VERSION_IDENTIFIER,RNProfile.IN_REPLY_TO_VERSION_IDENTIFIER),
    new FieldIdMap(IRNPackInfo.IN_RESPONSE_TO_ACTION_ID, RNProfile.IN_RESPONSE_TO_ACTION_ID),
    new FieldIdMap(IRNPackInfo.SERVICE_MESSAGE_TRACKING_ID,RNProfile.SERVICE_MESSAGE_TRACKING_ID),
    new FieldIdMap(IRNPackInfo.ACTION_IDENTITY_GLOBAL_BUS_ACTION_CODE,RNProfile.ACTION_IDENTITY_GLOBAL_BUS_ACTION_CODE),
    new FieldIdMap(IRNPackInfo.ACTION_IDENTITY_TO_MESSAGE_STANDARD,RNProfile.ACTION_IDENTITY_TO_MESSAGE_STANDARD),
    new FieldIdMap(IRNPackInfo.ACTION_IDENTITY_STANDARD_VERSION,RNProfile.ACTION_IDENTITY_STANDARD_VERSION),
    new FieldIdMap(IRNPackInfo.ACTION_IDENTITY_VERSION_IDENTIFIER,RNProfile.ACTION_IDENTITY_VERSION_IDENTIFIER),
    new FieldIdMap(IRNPackInfo.SIGNAL_IDENTITY_GLOBAL_BUS_SIGNAL_CODE,RNProfile.SIGNAL_IDENTITY_GLOBAL_BUS_SIGNAL_CODE),
    new FieldIdMap(IRNPackInfo.SIGNAL_IDENTITY_VERSION_IDENTIFIER,RNProfile.SIGNAL_IDENTITY_VERSION_IDENTIFIER),

    new FieldIdMap(IRNPackInfo.TO_GLOBAL_PARTNER_ROLE_CLASS_CODE,RNProfile.TO_GLOBAL_PARTNER_ROLE_CLASS_CODE),
    new FieldIdMap(IRNPackInfo.TO_GLOBAL_BUS_SERVICE_CODE,RNProfile.TO_GLOBAL_BUS_SERVICE_CODE),
    new FieldIdMap(IRNPackInfo.GLOBAL_USAGE_CODE,RNProfile.GLOBAL_USAGE_CODE),
    new FieldIdMap(IRNPackInfo.PIP_GLOBAL_PROCESS_CODE,RNProfile.PIP_GLOBAL_PROCESS_CODE),
    new FieldIdMap(IRNPackInfo.PIP_INSTANCE_IDENTIFIER,RNProfile.PIP_INSTANCE_IDENTIFIER),
    new FieldIdMap(IRNPackInfo.PIP_VERSION_IDENTIFIER,RNProfile.PIP_VERSION_IDENTIFIER),
    new FieldIdMap(IRNPackInfo.PROCESS_TRANSACTION_ID,RNProfile.PROCESS_TRANSACTION_ID),
    new FieldIdMap(IRNPackInfo.PROCESS_ACTION_ID,RNProfile.PROCESS_ACTION_ID),
    new FieldIdMap(IRNPackInfo.FROM_GLOBAL_PARTNER_CLASS_CODE,RNProfile.FROM_GLOBAL_PARTNER_CLASS_CODE),
    new FieldIdMap(IRNPackInfo.RNIF_VERSION,RNProfile.RNIF_VERSION),
    new FieldIdMap(IRNPackInfo.TO_GLOBAL_PARTNER_CLASS_CODE,RNProfile.TO_GLOBAL_PARTNER_CLASS_CODE),
//    new FieldIdMap(IRNPackInfo.ATTACHMENTS, RNProfile.ATTACHMENTS),
//    new FieldIdMap(IRNPackInfo.ATTACHMENT_NAMES, RNProfile.ATTACHMENT_NAMES),

    new FieldIdMap(IRNPackInfo.RECEIVER_GLOBAL_BUS_IDENTIFIER,RNProfile.RECEIVER_GLOBAL_BUS_IDENTIFIER),
    new FieldIdMap(IRNPackInfo.SENDER_GLOBAL_BUS_IDENTIFIER,RNProfile.SENDER_GLOBAL_BUS_IDENTIFIER),
    new FieldIdMap(IRNPackInfo.PARTNER_GLOBAL_BUS_IDENTIFIER,RNProfile.PARTNER_GLOBAL_BUS_IDENTIFIER),
    new FieldIdMap(IRNPackInfo.GN_MSG_ID,RNProfile.GN_MSG_ID),
    new FieldIdMap(IRNPackInfo.MSG_DIGEST,RNProfile.MSG_DIGEST),
    
  };

// static FieldIdMap[] padIntFieldArray= new FieldIdMap[]
//  {
//   new FieldIdMap(IRNPackInfo.NUMBER_OF_ATTAS,RNProfile.NUMBER_OF_ATTAS)
//  };


 public void profileToPackInfo(RNProfile profile, RNPackInfo packinfo)
  {
     for (int i = 0; i < strFieldArray.length; i++)
    {
      FieldIdMap mapElement = strFieldArray[i];
      String value = (String)profile.getFieldValue(mapElement._profileId);
      if(value != null)
      {
        packinfo.setFieldValue(mapElement._packinfoId, value);
      }
    }

//    for (int i = 0; i < padIntFieldArray.length; i++)
//    {
//      FieldIdMap mapElement = padIntFieldArray[i];
//      Integer value =(Integer) profile.getFieldValue(mapElement._profileId);
//      if(value != null)
//      {
//        packinfo.setFieldValue(mapElement._packinfoId, XMLUtil.padInteger(value));
//      }
//    }
    packinfo.setIsSignalDoc(profile.getIsSignalDoc());
    packinfo.setIsRequestMsg(profile.getIsRequestMsg());
    packinfo.setNumberOfAttas(profile.getNumberOfAttas().intValue());
    packinfo.setAttemptCount(profile.getAttemptCount().intValue());
    packinfo.setRNProfileUid(new Long(profile.getUId()));
    packinfo.setProcessOriginatorId(profile.getProcessOriginatorId()); //#1053 TWX 20091005: add originator so that we know which
                                                                       //locationID to be used in ServiceHeader's KnownInitiatingRole element
    
  }

  public  void packInfoToProfile(RNProfile profile, RNPackInfo packinfo)
  {
     for (int i = 0; i < strFieldArray.length; i++)
    {
      FieldIdMap mapElement = strFieldArray[i];
      String value = packinfo.getFieldValue(mapElement._packinfoId);
      Logger.debug("[PackInfoConvertor.packInfoToProfile()] packInfoId is " +
        mapElement._packinfoId + " value is " + value);
      if(value != null)
      {
        profile.setFieldValue(mapElement._profileId, value);
      }
    }

//    for (int i = 0; i < padIntFieldArray.length; i++)
//    {
//      FieldIdMap mapElement = padIntFieldArray[i];
//      String value = packinfo.getFieldValue(mapElement._packinfoId);
//      if(value != null)
//      {
//        profile.setFieldValue(mapElement._profileId, value);
//      }
//    }
    profile.setIsSignalDoc(packinfo.getIsSignalDoc());
    profile.setIsRequestMsg(packinfo.getIsRequestMsg());
    profile.setNumberOfAttas(new Integer(packinfo.getNumberOfAttas()));
    profile.setAttemptCount(new Integer(packinfo.getAttemptCount()));

  }




}
