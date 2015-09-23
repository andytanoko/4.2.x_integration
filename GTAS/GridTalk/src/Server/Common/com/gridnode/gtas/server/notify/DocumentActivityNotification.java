/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentActivityNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 04 2003    Neo Sok Lay         Created
 * Jun 19 2003    Neo Sok Lay         Add activity type: 
 *                                    TYPE_END_SEND, TYPE_END_TRANS, TYPE_REJECT_TRANS
 */
package com.gridnode.gtas.server.notify;

import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * Notification of the activities that are happening to a Document.<p>
 * Currently the following are contained in the notification:<p>
 * <pre>
 * Type     - (1) Start Sending the document
 *            (2) End Sending i.e. document is sent out of the system
 *            (3) End Transaction i.e. document is received by the recipient's system
 *          - Can be used for message selection, e.g. type='1'
 * Document - Document object that is involved in the event.
 * </pre>
 *
 * @author Neo Sok Lay
 *
 * @version 2.1 I1
 * @since 2.0 I7
 */
public class DocumentActivityNotification
  extends    AbstractNotification
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2375006780919516666L;

	/**
   * The point when send is performed
   */
  public static final short TYPE_START_SEND = 1;
  
  /**
   * The point when document is sent out of the system
   */
  public static final short TYPE_END_SEND   = 2;
  
  /**
   * The point when document is received by the recipient's system
   */
  public static final short TYPE_END_TRANS  = 3;

  /**
   * The point when document is rejected by the recipient's system
   */
  public static final short TYPE_REJECT_TRANS  = 4;

  private short _type;
  private Object _document;

  /**
   * Construct a DocumentActivityNotification object.
   *
   * @param type The type of the activity e.g TYPE_START_SEND, etc
   * @param document The document object.
   */
  public DocumentActivityNotification(short type, Object document)
  {
    _type = type;
    _document = (document instanceof IEntity)?
                ((IEntity)document).clone()  :
                document;
    putProperty("type", String.valueOf(type));
  }

  public short getType()
  {
    return _type;
  }

  public Object getDocument()
  {
    return _document;
  }

  public String getNotificationID()
  {
    return "DocumentActivity";
  }

  public String toString()
  {
    StringBuffer buff = new StringBuffer(getNotificationID());
    buff.append(" - Type[").append(getType()).append("]");

    return buff.toString();
  }
}