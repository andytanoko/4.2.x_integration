
package com.gridnode.gtas.events.dbarchive;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

/**
 */
public class RestoreDocumentEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4929346712806228066L;
	public static final String ARCHIVE_FILE = "Archive File Name";
	public static final String ARCHIVE_ID = "Archive ID";
  public static final String RESTORE_TYPE = "Restore Type";
  
  public RestoreDocumentEvent(String archiveFileName)
     throws EventException
  {
    checkSetString(ARCHIVE_FILE, archiveFileName);
  }

  public String getArhiveFile()
  {
    return (String)getEventData(ARCHIVE_FILE);
  }
  
  //Add archive ID, use to link back the TM archive
  /**
   * Use only while receiving the restore request from TM
   */
  public String getArchiveID()
  {
    return (String)getEventData(ARCHIVE_ID);
  }
  
  public void setArchiveID(String archiveID) throws EventException
  {
    checkSetString(ARCHIVE_ID, archiveID);
  }
  
  /**
   * TWX Use only while receiving the restore request from TM
   * @return
   */
  public String getRestoreType()
  {
    return (String)getEventData(RESTORE_TYPE);
  }
  
  public void setRestoreType(String restoreType) throws EventException
  {
    checkSetString(RESTORE_TYPE, restoreType);
  }
  
  public String getEventName()
  {
    return "java:comp/env/param/event/RestoreDocumentEvent";
  }

}