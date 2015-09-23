package com.gridnode.gtas.server.dbarchive.actions;

import java.util.HashMap;

import com.gridnode.gtas.events.dbarchive.CreateArchiveEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.dbarchive.helpers.ArchiveHelper;
import com.gridnode.gtas.server.dbarchive.model.ArchiveMetaInfo;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

public class CreateArchiveAction extends AbstractGridTalkAction
{
	/**
   * 
   */
  private static final long serialVersionUID = 4249015424120784238L;
  public static final String ACTION_NAME = "ArchiveAction";

  protected Class getExpectedEventClass()
  {
    return CreateArchiveEvent.class;
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
    CreateArchiveEvent archiveEvent = (CreateArchiveEvent) event;
  }

  protected IEventResponse doProcess(IEvent event) throws Throwable
  {
    CreateArchiveEvent archiveEvent = (CreateArchiveEvent) event;
    ArchiveHelper.saveToArchive(archiveEvent);
    return constructEventResponse(new HashMap());
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateArchiveEvent archiveEvent = (CreateArchiveEvent) event;
    return new Object[] { ArchiveMetaInfo.ENTITY_NAME, archiveEvent.getArchiveID()};
  }

  protected Object[] getSuccessMessageParams(IEvent event)
  {
    CreateArchiveEvent archiveEvent = (CreateArchiveEvent) event;
    return new Object[] { ArchiveMetaInfo.ENTITY_NAME, archiveEvent.getArchiveID()};
  }
}