package com.gridnode.pdip.app.workflow.impl.bpss.helpers;

public class IBpssConstants {

    public static final String EXCEPTION_SENDERKEY="exceptionSenderKey";
    public static final String EXCEPTION_DOCUMENTTYPE="exceptionDocumentType";
    public static final String EXCEPTION_RTPROCESSDOC_UID="exceptionRtProcessDocUId";

    public static final String CHECK_TIMETO_ACK_RECEIPT="CheckTimeToAckReceipt";
    public static final String CHECK_TIMETO_ACK_ACCEPT="CheckTimeToAckAccept";

    //signal types
    public static final String ACK_RECEIPT_SIGNAL="AcknowledgeReceiptSignal";
    public static final String ACK_ACCEPTANCE_SIGNAL="AcknowledgeAcceptanceSignal";
    public static final String EXCEPTION_SIGNAL="EXCEPTION_SIGNAL";
    public static final String EXCEPTION_TIMETO_ACK="EXCEPTION_TIMETO_ACK";
    public static final String EXCEPTION_TIMETO_PERFORM="EXCEPTION_TIMETO_PERFORM";
    public static final String EXCEPTION_VALIDATE="EXCEPTION_VALIDATE";
    public static final String EXCEPTION_CANCEL="EXCEPTION_CANCEL";


    //guards
    public static final String SUCCESS_GUARD="SUCCESS";
    public static final String BUSINESSFAILURE_GUARD="BUSINESSFAILURE";
    public static final String TECHNICALFAILURE_GUARD="TECHNICALFAILURE";
    public static final String ANYFAILURE_GUARD="ANYFAILURE";

    //role types
    public static final String INITIATING_ROLE="InitiatingRole";
    public static final String RESPONDING_ROLE="RespondingRole";

    //keys
    public static final String PARTNER_CONSTANT="SELF";
    public static final String DOCUMENTID="documentId";
    public static final String DOCUMENT_TYPE="documentType";
    public static final String DOCUMENT_OBJECT="documentObject";
    public static final String PARTNER_KEY="partnerKey";
    public static final String SENDER_KEY="senderKey";
    public static final String SIGNAL_TYPE="signalType";
    public static final String REASON="reason";
    public static final String RETRY_COUNT="retryCount";
    public static final String RTPROCESSDOC_UID="rtProcessDocUId";
    public static final String ISREQUEST_DOCUMENT="isRequestDocument";
    public static final String INITIATOR_PARTNERKEY="initiatorPartnerKey";
    public static final String RESPONDER_PARTNERKEY="responderPartnerKey";

}