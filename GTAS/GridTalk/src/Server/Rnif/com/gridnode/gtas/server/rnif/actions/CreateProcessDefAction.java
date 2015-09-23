package com.gridnode.gtas.server.rnif.actions;

import com.gridnode.gtas.events.rnif.CreateProcessDefEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.gtas.server.rnif.helpers.ActionHelper;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the creation of a new ProcessDef.
 *
 */
public class CreateProcessDefAction extends AbstractCreateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1675569172454108976L;
	public static final String ACTION_NAME = "CreateProcessDefAction";

  // ****************** AbstractGridTalkAction methods *****************

  protected Class getExpectedEventClass()
  {
    return CreateProcessDefEvent.class;
  }
 
  protected String getActionName()
  {
    return ACTION_NAME;
  }

  // ******************* AbstractCreateEntityAction methods *************

  protected AbstractEntity retrieveEntity(Long key) throws java.lang.Exception
  {
    return ActionHelper.getProcessDefManager().findProcessDef(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    CreateProcessDefEvent addEvent = (CreateProcessDefEvent) event;

    ProcessDef newDef = new ProcessDef();
    ActionHelper.copyProcessDefFields(addEvent.getDefFields(), newDef);

    ProcessAct requestAct = new ProcessAct();
    ActionHelper.copyProcessActFields(addEvent.getRequestAct(), requestAct);
    newDef.setRequestAct(requestAct);

    Map responseActMap = addEvent.getResponseAct();
    if (responseActMap != null && !responseActMap.isEmpty())
    {
      ProcessAct responseAct = new ProcessAct();
      ActionHelper.copyProcessActFields(responseActMap, responseAct);
      newDef.setResponseAct(responseAct);
    }
    return newDef;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateProcessDefEvent addEvent = (CreateProcessDefEvent) event;
    /**@todo TBD */
    return new Object[] { ProcessDef.ENTITY_NAME, addEvent.getDefFields()};
  }

  protected Long createEntity(AbstractEntity entity) throws java.lang.Exception
  {
    return ActionHelper.createProcessDef((ProcessDef) entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertProcessDefToMap((ProcessDef) entity);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
     CreateProcessDefEvent addEvent = (CreateProcessDefEvent) event;

     Map defFields = addEvent.getDefFields();
     Object actionType = defFields.get(ProcessDef.PROCESS_TYPE);
     Map responseAct = addEvent.getResponseAct();
     if(ProcessDef.TYPE_SINGLE_ACTION.equals(actionType))
     {
         if(responseAct != null && !responseAct.isEmpty())
         throw new CreateEntityException("Single Action Process cannot have response Action");
     }
     else   if(ProcessDef.TYPE_TWO_ACTION.equals(actionType))
     {
       if(responseAct== null || responseAct.isEmpty())
         throw new CreateEntityException("Two Action Process must have response Action");
     }
     else
       throw new CreateEntityException("Process Type not defined");
  }


  // ****************** Own methods **********************************
}