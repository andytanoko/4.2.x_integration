/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subject to license terms.
 *
 * Copyright 2002 (c) GridNode Pte Ltd. All rights reserved.
 * File: FeedbackMsg.java
 *
 * ********************************************************
 * Date            Author                Changes
 * ********************************************************
 * Jun 05, 2002    Guo Jianyu            created
 * Jun 20, 2002    Kan Mun               Modified - Add a data field and its getter and setter methods
 *                                        so as to be able to return the BL data.
 * Oct 24, 2002    Kan Mun               Modified - to keep the Exception, error message and the GNTransport.
 *
 */
package com.gridnode.pdip.base.transport.helpers;

import com.gridnode.pdip.framework.messaging.Message;

/**
 * This class describes the object used in feedback messages.
 * For example, messages for status notification of a data send, messages
 * for reporting a connection failure, etc.
 *
 * @author Guo Jianyu
 *
 * @version 1.0
 * @since  1.0
 */
public class Feedback implements java.io.Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4877657007319571493L;
	private boolean _status      = false; // feedback status code
  private String  _description = null;  // feedback description
  private Message _message     = null;  // feedback message object

  /**
   *@todo to be deprecated
   */
  private String[] _header;

  /**
   * this constructor is meant for non-message-oriented feedback messages
   */
  public Feedback(boolean status, String description)
  {
    setStatus(status);
    setDescription(description);
    setHeader(null);
  }

  /**
   * @todo to be deprecated
   * default constructor
   * @since 1.0
   */
  public Feedback(boolean status, String description, String[] header)
  {
    setStatus(status);
    setDescription(description);
    setHeader(header);
  }

  /**
   * default constructor
   * @since 1.0
   */
  public Feedback(boolean status, String description, Message message)
  {
    setStatus(status);
    setDescription(description);
    setMessage(message);
  }

  /**
   * @return "status" is returned
   */
  public boolean getStatus()
  {
    return _status;
  }

  /**
   * @return "info" is returned
   */
  public String[] getHeader()
  {
    return _header;
  }

  /**
   * @return "desc" is returned
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * set "code"
   * @param code  -- the code to be set to
   */
  public void setStatus(boolean status)
  {
    _status = status;
  }

  /**
   * set "info"
   * @param info -- the info to be set to
   */
  public void setHeader(String[] header)
  {
    _header = header;
  }

  /**
   * set "description"
   * @param desc -- the description to be set to
   */
  public void setDescription(String description)
  {
    _description = description;
  }

  /**
   * return the contents of the message in the form of a string
   */
  public String toString()
  {
    return "[Feedback: status = " + getStatus() +
           ", description = " + getDescription() + "]";
  }

  /**
   * @return
   */
  public Message getMessage()
  {
    return _message;
  }

  /**
   * @param message
   */
  public void setMessage(Message message)
  {
    this._message = message;
  }
}