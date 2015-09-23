/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007(c) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPipMessageHelper.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 21, 2007			Alain Ah Ming						Created
 */
package com.gridnode.gridtalk.tester.loopback.helpers;


/**
 * Interface for PIP message helper classes
 * 
 *  @author Alain Ah Ming
 *  @since
 *  @version
 */
public interface IPipMessageHelper
{
	public final static String CONTENT_ID_PATTERN = "<{0}.{1}.GridNode.{2}@{3}>";

	public final static String PREAMPLE_CID_PLACEHOLDER = "###_PREAMBLE_CID_###";

	public final static String DELIVERY_CID_PLACEHOLDER = "###_DELIVERY_CID_###";

	public final static String DELIVERY_TS_PLACEHOLDER = "###_DELIVERY_TS_###";

	public final static String RECEIVER_DUNS_PLACEHOLDER = "###_RECEIVER_DUNS_###";

	public final static String SENDER_DUNS_PLACEHOLDER = "###_SENDER_DUNS_###";

	public final static String MSG_TRACKING_ID_PLACEHOLDER = "###_MSG_TRACKING_ID_###";

	public final static String SERVICEH_CID_PLACEHOLDER = "###_SERVICEH_CID_###";

	public final static String PIP_INST_ID_PLACEHOLDER = "###_PIP_INST_ID_###";

	public final static String SERVICEC_CID_PLACEHOLDER = "###_SERVICEC_CID_###";

	public final static String DOC_GEN_TS_PLACEHOLDER = "###_DOC_GEN_TS_###";

	public final static String DOC_ID_PLACEHOLDER = "###_DOC_ID_###";

	public final static String INITIATOR_DUNS_PLACEHOLDER = "###_INITIATOR_DUNS_###";

	public final static String SRC_MSG_TRACKING_ID_PLACEHOLDER = "###_SRC_MSG_TRACKING_ID_###";

	public final static String PIP_CODE_PLACEHOLDER = "###_PIP_CODE_###";
	
	public String getPipCode();
	
	public String findValue(String content, String commaDelimitedTokenList);
  
	public String findPipInstId(String content) throws MessageHelperException;
  
  public String findReceiverDuns(String content) throws MessageHelperException;

  public String findSenderDuns(String content) throws MessageHelperException;

  public String findMsgTrackingId(String content) throws MessageHelperException;

  public String findInitiatorDuns(String content) throws MessageHelperException;
}
