

package com.gridnode.gtas.server.rnif.model;


 public interface IRNProfile

{
  public static final String ENTITY_NAME = "RNProfile";
  public static final Number UID = new Integer(0);  //Integer
  public static final Number DOCUMENT_UID = new  Integer(1); // Long
  public static final Number PROCESS_INSTANCE_ID = new  Integer(2); // String
  public static final Number PROCESS_ORIGINATOR_ID = new  Integer(3); // String
//	public static final Number PROCESS_MSG_TYPE = new  Integer(4); // String
  public static final Number PROCESS_DEF_NAME = new  Integer(5); // String
  public static final Number RECEIVER_DOMAIN = new  Integer(6); // String
  public static final Number RECEIVER_GLOBAL_BUS_IDENTIFIER = new  Integer(7); // String
  public static final Number RECEIVER_LOCATION_ID = new  Integer(8); // String
  public static final Number SENDER_DOMAIN = new  Integer(9); // String
  public static final Number SENDER_GLOBAL_BUS_IDENTIFIER = new  Integer(10); // String
  public static final Number SENDER_LOCATION_ID = new  Integer(11); // String
  public static final Number DELIVERY_MESSAGE_TRACKING_ID = new  Integer(12); // String
  public static final Number BUS_ACTIVITY_IDENTIFIER = new  Integer(13); // String
  public static final Number FROM_GLOBAL_PARTNER_ROLE_CLASS_CODE = new  Integer(14); // String
  public static final Number FROM_GLOBAL_BUS_SERVICE_CODE = new  Integer(15); // String
  public static final Number IN_REPLY_TO_GLOBAL_BUS_ACTION_CODE = new  Integer(16); // String
  public static final Number IN_REPLY_TO_MESSAGE_STANDARD = new  Integer(17); // String
  public static final Number IN_REPLY_TO_STANDARD_VERSION = new  Integer(18); // String
  public static final Number IN_REPLY_TO_VERSION_IDENTIFIER = new  Integer(19); // String
  public static final Number SERVICE_MESSAGE_TRACKING_ID = new  Integer(20); // String
  public static final Number ACTION_IDENTITY_GLOBAL_BUS_ACTION_CODE = new  Integer(21); // String
  public static final Number ACTION_IDENTITY_TO_MESSAGE_STANDARD = new  Integer(22); // String
  public static final Number ACTION_IDENTITY_STANDARD_VERSION = new  Integer(23); // String
  public static final Number ACTION_IDENTITY_VERSION_IDENTIFIER = new  Integer(24); // String
  public static final Number SIGNAL_IDENTITY_GLOBAL_BUS_SIGNAL_CODE = new  Integer(25); // String
  public static final Number SIGNAL_IDENTITY_VERSION_IDENTIFIER = new  Integer(26); // String
  public static final Number TO_GLOBAL_PARTNER_ROLE_CLASS_CODE = new  Integer(27); // String
  public static final Number TO_GLOBAL_BUS_SERVICE_CODE = new  Integer(28); // String
  public static final Number GLOBAL_USAGE_CODE = new  Integer(29); // String
  public static final Number PARTNER_GLOBAL_BUS_IDENTIFIER = new  Integer(30); // String
  public static final Number PIP_GLOBAL_PROCESS_CODE = new  Integer(31); // String
  public static final Number PIP_INSTANCE_IDENTIFIER = new  Integer(32); // String
  public static final Number PIP_VERSION_IDENTIFIER = new  Integer(33); // String
  public static final Number PROCESS_TRANSACTION_ID = new  Integer(34); // String
  public static final Number PROCESS_ACTION_ID = new  Integer(35); // String
  public static final Number FROM_GLOBAL_PARTNER_CLASS_CODE = new  Integer(36); // String
  public static final Number TO_GLOBAL_PARTNER_CLASS_CODE = new  Integer(37); // String
  public static final Number NUMBER_OF_ATTAS = new  Integer(38); // Integer
//	public static final Number HAS_ATTACHMENT = new  Integer(39); // Boolean
//	public static final Number ATTACHMENTS = new  Integer(40); // String
//	public static final Number ATTACHMENT_NAMES = new  Integer(41); // String

  public static final Number  PROCESS_RESPONDER_ID= new Integer(39) ;// String;

  //newly added
  public static final Number IS_SIGNAL_DOC  = new Integer(42); //boolean
  public static final Number IS_REQUEST_MSG  = new Integer(43); //boolean

  public static final Number UNIQUE_VALUE  = new Integer(44); //String
  public static final Number GN_MSG_ID  = new Integer(45); //String
  public static final Number ATTEMPT_COUNT = new Integer(46); //Integer

  public static final Number MSG_DIGEST = new  Integer(47); // String
  public static final Number RNIF_VERSION = new Integer(48); //String
  public static final Number IN_RESPONSE_TO_ACTION_ID = new Integer(49); //String
  public static final Number USER_TRACKING_ID = new Integer(50); //String  
}
