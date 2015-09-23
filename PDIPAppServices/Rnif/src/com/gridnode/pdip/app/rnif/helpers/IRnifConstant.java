package com.gridnode.pdip.app.rnif.helpers;

public interface IRnifConstant
{

  /** RNIF 1.10 **/
  String ACK_MESSAGE_TYPE_1= "ReceiptAcknowledgementMessageGuideline.dtd";
  String EXCEPTION_MESSAGE_TYPE_1= "ExceptionMessageGuideline.dtd";
  String FAILURE_NOTIFY_MSG_TYPE_1= "0A1FailureNotificationMessageGuideline.dtd";

  /** RNIF 2.0 **/
  String ACK_MESSAGE_TYPE_2= "AcknowledgmentOfReceipt_MS_V02_00.dtd";
  String EXCEPTION_MESSAGE_TYPE_2= "Exception_MS_V02_00.dtd";
  String FAILURE_NOTIFY_MSG_TYPE_2= "0A1_MS_V02_00_FailureNotification.dtd";


  //default pip name
  String RN_FAILNOTIFY_DEFNAME20= "0A1FailureNotification";


  /**
    * DocumentType for RosettaNet FailureNotification.
    */
  String RN2_FAILNOTF= "RN_FAILNOTF2";
  /**
   * DocumentType for RosettaNet FailureNotification.
   */
  String RN1_FAILNOTF= "RN_FAILNOTF1";

  /**
   * DocumentType for RosettaNet Exception.
   */
  String RN2_EXCEPTION= "RN_EXCEPTION";

  /**
   * DocumentType for RosettaNet Acknowledgement.
   */
  String RN2_ACK= "RN_ACK";

  //using the same DocumentType as RNIF 2.0
  /**
   * DocumentType for RosettaNet Exception.
   */
  String RN1_EXCEPTION= "RN_EXCEPTION";

  /**
   * DocumentType for RosettaNet Acknowledgement.
   */
  String RN1_ACK= "RN_ACK";

//BPSS Message Type
  String BPSS_MSG_TYPE="BPSS1.01.dtd";

  int   INDEX_PROCESS_INITIATOR=0;
  int   INDEX_PROCESS_INSTANCE_ID=1;
}
