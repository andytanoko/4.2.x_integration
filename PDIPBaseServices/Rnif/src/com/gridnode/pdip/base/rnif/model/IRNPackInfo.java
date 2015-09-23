package com.gridnode.pdip.base.rnif.model;

public interface IRNPackInfo
{
  public static final String RECEIVER_DOMAIN = "_receiverDomain";
  public static final String RECEIVER_GLOBAL_BUS_IDENTIFIER =  "_receiverGlobalBusIdentifier";
  public static final String RECEIVER_LOCATION_ID ="_receiverLocationId";
  public static final String SENDER_DOMAIN ="_senderDomain";
  public static final String SENDER_GLOBAL_BUS_IDENTIFIER = "_senderGlobalBusIdentifier";
  public static final String SENDER_LOCATION_ID = "_senderLocationId";
  public static final String DELIVERY_MESSAGE_TRACKING_ID = "_deliveryMessageTrackingId";
  public static final String BUS_ACTIVITY_IDENTIFIER = "_busActivityIdentifier";
  public static final String FROM_GLOBAL_PARTNER_ROLE_CLASS_CODE = "_fromGlobalPartnerRoleClassCode";
  public static final String FROM_GLOBAL_BUS_SERVICE_CODE ="_fromGlobalBusServiceCode";
  public static final String IN_REPLY_TO_GLOBAL_BUS_ACTION_CODE = "_inReplyToGlobalBusActionCode";
  public static final String IN_REPLY_TO_MESSAGE_STANDARD = "_inReplyToMessageStandard";
  public static final String IN_REPLY_TO_STANDARD_VERSION = "_inReplyToStandardVersion";
  public static final String IN_REPLY_TO_VERSION_IDENTIFIER = "_inReplyToVersionIdentifier";
  public static final String IN_RESPONSE_TO_ACTION_ID = "_inResponseToActionID";
  public static final String SERVICE_MESSAGE_TRACKING_ID = "_serviceMessageTrackingId";
  public static final String ACTION_IDENTITY_GLOBAL_BUS_ACTION_CODE = "_actionIdentityGlobalBusActionCode";
  public static final String ACTION_IDENTITY_TO_MESSAGE_STANDARD = "_actionIdentityToMessageStandard";
  public static final String ACTION_IDENTITY_STANDARD_VERSION = "_actionIdentityStandardVersion";
  public static final String ACTION_IDENTITY_VERSION_IDENTIFIER = "_actionIdentityVersionIdentifier";
  public static final String RNIF_VERSION = "_RnifVersion";
  public static final String SIGNAL_IDENTITY_GLOBAL_BUS_SIGNAL_CODE = "_signalIdentityGlobalBusSignalCode";
  public static final String SIGNAL_IDENTITY_VERSION_IDENTIFIER = "_signalIdentityVersionIdentifier";
  public static final String TO_GLOBAL_PARTNER_ROLE_CLASS_CODE = "_toGlobalPartnerRoleClassCode";
  public static final String TO_GLOBAL_BUS_SERVICE_CODE = "_toGlobalBusServiceCode";
  public static final String GLOBAL_USAGE_CODE = "_globalUsageCode";
  public static final String PARTNER_GLOBAL_BUS_IDENTIFIER = "_partnerGlobalBusIdentifier";
  public static final String PIP_GLOBAL_PROCESS_CODE ="_PIPGlobalProcessCode";
  public static final String PIP_INSTANCE_IDENTIFIER = "_PIPInstanceIdentifier";
  public static final String PIP_VERSION_IDENTIFIER = "_PIPVersionIdentifier";
  public static final String PROCESS_TRANSACTION_ID = "_processTransactionId";
  public static final String PROCESS_ACTION_ID = "_processActionId";
  public static final String FROM_GLOBAL_PARTNER_CLASS_CODE = "_fromGlobalPartnerClassCode";
  public static final String TO_GLOBAL_PARTNER_CLASS_CODE = "_toGlobalPartnerClassCode";
  public static final String NUMBER_OF_ATTAS = "_numberOfAttas";
//  public static final String HAS_ATTACHMENT = "_hasAttachment";
//  public static final String ATTACHMENTS = "_attachments";
//  public static final String ATTACHMENT_NAMES = "_attachmentNames";

  public static final String GN_MSG_ID  = "_RNMsgId"; //String
  public static final String ATTEMPT_COUNT  = "_attemptCount"; //int
  public static final String MSG_DIGEST  = "_msgDigest"; //String

  public static final String IS_NON_REPUDIATION_REQUIRED="_isNonRepudiationRequired";//String
  public static final String IS_COMPRESS_REQUIRED = "_isCompressRequired"; //boolean

}


