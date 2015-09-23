package com.gridnode.gtas.server.rnif.actions;

import com.gridnode.gtas.events.rnif.UpdateProcessDefEvent;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.gtas.server.rnif.helpers.ActionHelper;
import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Map;

/**
 * This Action class handles the update of a ProcessDef.
 */
public class UpdateProcessDefAction extends AbstractUpdateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7721827422716446810L;

	public static final String ACTION_NAME = "UpdateProcessDefAction";

  private  ProcessDef _defToUpdate;

  // ************************** AbstractUpdateEntityAction methods ************

  protected void updateEntity(AbstractEntity entity) throws java.lang.Exception
  {
    ActionHelper.updateProcessDef((ProcessDef) entity);
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {
    UpdateProcessDefEvent updEvent = (UpdateProcessDefEvent) event;
    ProcessDef defToUpdate = _defToUpdate;
    ActionHelper.copyProcessDefFields(updEvent.getOtherFields(), defToUpdate);

    ProcessAct requestAct = defToUpdate.getRequestAct();
    ActionHelper.copyProcessActFields(updEvent.getRequestAct(), requestAct);

    Map responseActMap = updEvent.getResponseAct();
    if (responseActMap != null)
    {
      ProcessAct responseAct = defToUpdate.getResponseAct();
      if(responseAct != null)
        ActionHelper.copyProcessActFields(responseActMap, responseAct);
    }
    return defToUpdate;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateProcessDefEvent updEvent = (UpdateProcessDefEvent) event;
    /**@todo TBD */
    return new Object[] { ProcessDef.ENTITY_NAME, updEvent.getDefUID(), };
  }

  // *********************** AbstractGridTalkAction methods ********************

  protected Class getExpectedEventClass()
  {
    return UpdateProcessDefEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateProcessDefEvent updEvent = (UpdateProcessDefEvent) event;
    _defToUpdate = ActionHelper.verifyProcessDef(updEvent.getDefUID());
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return ActionHelper.getProcessDefManager().findProcessDef(key);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
    return ActionHelper.convertProcessDefToMap((ProcessDef) entity);
  }

}