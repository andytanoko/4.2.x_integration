/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateTemplateEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Jared Low           Created.
 * Aug 13 2002    Daniel D'Cotta      Modified for new field meta info.
 */
package com.gridnode.gtas.events.gridform;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 * Event for updating a template entity.
 *
 * @version 2.0
 * @since 2.0
 * @author Jared Low
 */
public class UpdateTemplateEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6236051200593307415L;
	public static final String UID          = "UID";
  public static final String NAME         = "Name";
  public static final String FILENAME     = "Filename";
  public static final String IS_UPLOADING = "IsUploading";

  public UpdateTemplateEvent(Long uid, String name, String filename)
    throws EventException
  {
    this(uid, name, filename, new Boolean(filename.length() != 0));

    /** @todo Use proper logging. */
    System.out.println("[UpdateTemplateEvent.<init>] filename = " +
               (filename == null ? "null" : filename));
  }

  public UpdateTemplateEvent(Long uid, String name, String filename, Boolean isUploading)
    throws EventException
  {
    checkSetLong(UID, uid);
    checkSetString(NAME, name);
    setEventData(FILENAME, filename); // may be empty string to if file was not uploaded
    setEventData(IS_UPLOADING, isUploading);
  }

  public Long getUID()
  {
    return (Long)getEventData(UID);
  }

  public String getName()
  {
    return (String)getEventData(NAME);
  }

  public String getFilename()
  {
    return (String)getEventData(FILENAME);
  }

  public Boolean getIsUploading()
  {
    return (Boolean)getEventData(IS_UPLOADING);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateTemplateEvent";
  }
}