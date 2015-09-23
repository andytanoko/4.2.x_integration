/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SynchronizationFailException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 22 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.exceptions;

/**
 * This exception class is used for indicating a failure in synchronizing
 * a resource at the recipient's end.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class SynchronizationFailException
  extends Exception
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6908177827212251418L;
	public static final String RECIPIENT_FAILED =
                               "Recipient failed to synchronize the resource";
  public static final String SERIALIZE_FAILED =
                               "Failed to serialize resource";
  public static final String DESERIALIZE_FAILED =
                               "Failed to deserialize resource";
  public static final String SYNC_CONTENT_FAILED =
                               "Failed to synchronize resource content to database";
  public static final String ILLEGAL_PAYLOAD_FORMAT =
                               "Illegal format of payload received";

  public SynchronizationFailException(
    String failReason)
  {
    super("Synchronization failed due to: "+failReason);
  }


  public static SynchronizationFailException recipientSyncFailed()
  {
    return new SynchronizationFailException(RECIPIENT_FAILED);
  }

  public static SynchronizationFailException serializeResourceFailed()
  {
    return new SynchronizationFailException(SERIALIZE_FAILED);
  }

  public static SynchronizationFailException deserializeResourceFailed()
  {
    return new SynchronizationFailException(DESERIALIZE_FAILED);
  }

  public static SynchronizationFailException syncContentFailed()
  {
    return new SynchronizationFailException(SYNC_CONTENT_FAILED);
  }

  public static SynchronizationFailException illegalPayloadFormat()
  {
    return new SynchronizationFailException(ILLEGAL_PAYLOAD_FORMAT);
  }

}