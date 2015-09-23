/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationData.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 28 Apr 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.helpers;

import com.gridnode.pdip.app.alert.providers.AbstractDetails;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * This Data provider provides the Activation related data.<p>
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class ActivationData extends AbstractDetails
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -885130975995101814L;

	public static final String NAME = "Activation";

  public static final String FIELD_TYPE             = "TYPE";
  public static final String FIELD_PARTNER_GN_NAME  = "PARTNER_GN_NAME";
  public static final String FIELD_PARTNER_GN_ID    = "PARTNER_GN_ID";
  public static final String FIELD_DT_RECEIVED      = "DT_RECEIVED";

  public static final String TYPE_REQ_ACTIVATION    = "1";
  public static final String TYPE_REQ_DEACTIVATION  = "2";
  public static final String TYPE_CANCEL_ACTIVATION = "3";
  public static final String TYPE_APPROVE_ACTIVATION= "4";
  public static final String TYPE_REJECT_ACTIVATION = "5";

  /**
   * Constructs a Activation data provider for when receive activation messages
   * from a (potential) partner: e.g Request Activation, Cancel Activation Request,
   * Approve Activation Request, Reject Activation Request, Request Deactivation.
   *
   * @param type The type of the activation message received.
   * @param partnerNodeID GridNodeID of the (potential) partner involved.
   * @param partnerNodeName GridNode Name of the (potential) partner involved.
   * @param received Date/Time when the message was received.
   */
  public ActivationData(String type,
                        String partnerNodeID,
                        String partnerNodeName,
                        Date received)
  {
    set(FIELD_TYPE, type);
    set(FIELD_PARTNER_GN_NAME, partnerNodeName);
    set(FIELD_PARTNER_GN_ID, partnerNodeID);
    set(FIELD_DT_RECEIVED, received);
  }

  public String getName()
  {
    return NAME;
  }

  /**
   * Get the names of the fields available in the data provider.
   * @return List of field names (String)
   */
  public static final List getFieldNameList()
  {
    ArrayList list = new ArrayList();
    list.add(FIELD_TYPE);
    list.add(FIELD_PARTNER_GN_NAME);
    list.add(FIELD_PARTNER_GN_ID);
    list.add(FIELD_DT_RECEIVED);

    return list;
  }
}