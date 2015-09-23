package com.gridnode.gtas.server.dbarchive.actions;

import java.util.HashMap;

import com.gridnode.gtas.events.dbarchive.ArchiveDocumentEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.dbarchive.helpers.ArchiveHelper;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

public class ArchiveDocumentAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4895334056226204132L;
	public static final String ACTION_NAME = "ArchiveDocumentAction";

  protected Class getExpectedEventClass()
  {
    return ArchiveDocumentEvent.class;
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
    ArchiveDocumentEvent archiveEvent = (ArchiveDocumentEvent) event;
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    ArchiveDocumentEvent archiveEvent = (ArchiveDocumentEvent) event;
    ArchiveHelper.sendToArchiveDest(archiveEvent);
    return constructEventResponse(new HashMap());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    ArchiveDocumentEvent archiveEvent = (ArchiveDocumentEvent) event;
    return new Object[] { GridDocument.ENTITY_NAME, archiveEvent.getArchiveName()};
  }

  protected Object[] getSuccessMessageParams(IEvent event)
  {
    ArchiveDocumentEvent archiveEvent = (ArchiveDocumentEvent) event;
    return new Object[] { GridDocument.ENTITY_NAME, archiveEvent.getArchiveName()};
  }
}