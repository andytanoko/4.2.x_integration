package com.gridnode.gtas.server.dbarchive.actions;

import java.io.File;
import java.util.HashMap;

import com.gridnode.gtas.events.dbarchive.RestoreDocumentEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.dbarchive.helpers.ArchiveHelper;
import com.gridnode.gtas.server.dbarchive.helpers.Logger;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

public class RestoreDocumentAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 217433807386534371L;
	public static final String ACTION_NAME = "RestoreDocumentAction";

  protected Class getExpectedEventClass()
  {
    return RestoreDocumentEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    return constructEventResponse(IErrorCode.GENERAL_ERROR, getErrorMessageParams(event), ex);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    RestoreDocumentEvent archiveEvent = (RestoreDocumentEvent) event;
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    RestoreDocumentEvent restoreEvent = (RestoreDocumentEvent) event;
    String summaryFileName = restoreEvent.getArhiveFile();
    File summaryFile = FileUtil.getFile(IPathConfig.PATH_TEMP, _userID + "/in/", summaryFileName);
    summaryFileName = summaryFile.getAbsolutePath();
    Logger.debug("[RestoreDocumentAction.doProcess] new summaryFileName = " + summaryFileName);
    restoreEvent.setEventData(RestoreDocumentEvent.ARCHIVE_FILE,summaryFileName);
    ArchiveHelper.sendToArchiveDest(restoreEvent);
    return constructEventResponse(new HashMap());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    RestoreDocumentEvent archiveEvent = (RestoreDocumentEvent) event;
    return new Object[] { GridDocument.ENTITY_NAME, String.valueOf(archiveEvent.getArhiveFile())};
  }

  protected Object[] getSuccessMessageParams(IEvent event)
  {
    RestoreDocumentEvent restoreEvent = (RestoreDocumentEvent) event;
    return new Object[] { GridDocument.ENTITY_NAME, String.valueOf(restoreEvent.getArhiveFile())};
  }
}