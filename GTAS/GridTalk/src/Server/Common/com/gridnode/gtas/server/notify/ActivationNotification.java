/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 07 2002    Neo Sok Lay         Created
 * Auh 04 2003    Neo Sok Lay         Add additional parameter to indicate the
 *                                    role that this GridTalk participates in 
 *                                    the activation/deactivation.
 */
package com.gridnode.gtas.server.notify;

/**
 * Notification message on the Activation status of a GridNode partner.<p>
 * Currently the following are contained in the notification:<p>
 * <pre>
 * State         - (1) Activated or (2) Deactivated
 *               - Can be used for message selection, e.g. state='2'
 * PartnerNode   - GridNode ID of the GridNode partner being activated or deactivated.
 * PartnerBeUIDs - UIDs of the BusinessEntity(s) of the GridNode partner exchanged
 *                 during the Activation.
 * MyBeUIDs      - UIDs of this GridTalk's BusinessEntity(s) exchanged with the
 *                 partner during the Activation.
 * MyRole        - Indicates what role this GridTalk participate in the Activation/Deactivation:
 *                 (approve) Approve the activation request, or
 *                 (deactivate) Request the deactivation, or
 *                 (passive) Receives the approval or deactivation.
 *               - Can be used for message selection, e.g. myRole='approve'              
 * </pre>
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1
 * @since 2.0
 */
public class ActivationNotification
  extends    AbstractNotification
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4969286540885375353L;
	public static final short STATE_ACTIVATED   = 1;
  public static final short STATE_DEACTIVATED = 2;
  public static final String ROLE_APPROVE = "approve";
  public static final String ROLE_DEACTIVATE = "deactivate";
  public static final String ROLE_PASSIVE = "passive";
  
  private short _state;
  private String _node;
  private Long[] _partnerBes;
  private Long[] _myBes;
  private String _myRole;

  public ActivationNotification(
    short state, String partnerNode, Long[] myBeUIDs, Long[] partnerBeUIDs,
    String myRole)
  {
    _state = state;
    _node = partnerNode;
    _partnerBes = partnerBeUIDs;
    _myBes = myBeUIDs;
    _myRole = myRole;
    putProperty("state", String.valueOf(state));
    putProperty("myRole", String.valueOf(myRole));
  }

  public short getState()
  {
    return _state;
  }

  public String getPartnerNode()
  {
    return _node;
  }

  public Long[] getPartnerBeUIDs()
  {
    return _partnerBes;
  }

  public Long[] getMyBeUIDs()
  {
    return _myBes;
  }

  public String getMyRole()
  {
    return _myRole;
  }
  
  public String getNotificationID()
  {
    return "Activation";
  }

  public String toString()
  {
    StringBuffer buff = new StringBuffer(getNotificationID());
    buff.append(" - Node[").append(getPartnerNode()).append("], State[").append(
      getState()).append("]");

    return buff.toString();
  }
}