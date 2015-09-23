/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteBusinessEntityActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 21 2002    Neo Sok Lay         Re-write test case.
 */
package com.gridnode.gtas.server.bizreg.actions;

import com.gridnode.gtas.events.bizreg.DeleteBusinessEntityEvent;
import com.gridnode.gtas.server.bizreg.helpers.ActionTestHelper;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.rdm.IAttributeKeys;

import com.gridnode.pdip.app.bizreg.model.BusinessEntity;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;

import junit.framework.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * This Test case tests the DeleteBusinessEntityAction class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class DeleteBusinessEntityActionTest extends ActionTestHelper
{
  DeleteBusinessEntityEvent[] _events;
  Long[] _cannotDeletes;
  static final DataFilterImpl FILTER = new DataFilterImpl();
  static
  {
    FILTER.addSingleFilter(null, BusinessEntity.ENTERPRISE_ID,
      FILTER.getEqualOperator(), ENTERPRISE, false);
  }

  public DeleteBusinessEntityActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(DeleteBusinessEntityActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  // ****************** ActionTestHelper methods ***********************

  protected void unitTest() throws Exception
  {
    // ******** REJECTED ************************
    // Delete single:non existing.
    deleteCheckFail(_events[0], _sessions[0], _sm[0], true);
    // Delete single:canDelete=false
    deleteCheckFail(_events[1], _sessions[0], _sm[0], false);
    // Delete single:gtas Partner BE
    deleteCheckFail(_events[2], _sessions[0], _sm[0], true);
    // Delete multi:some canDelete=false
    deleteCheckFail(_events[3], _sessions[0], _sm[0], false);
    // Delete multi:some gtas Partner BEs
    deleteCheckFail(_events[4], _sessions[0], _sm[0], true);

    // *********** ACCEPTED **********************
    // Delete single:existing, own, canDelete=true.
    deleteCheckSuccess(_events[5], _sessions[0], _sm[0]);
    // Delete single:existing, partner, canDelete=true.
    deleteCheckSuccess(_events[6], _sessions[0], _sm[0]);
    // Delete multi:allexisting(some own, some partner), canDelete=true
    deleteCheckSuccess(_events[7], _sessions[0], _sm[0]);
    // Delete multi:some non existing, canDelete=true
    deleteCheckSuccess(_events[8], _sessions[0], _sm[0]);

    //other role not deleted
    //getBusinessEntityByUId(_roleUIDs[1].longValue());
  }

  protected void prepareTestData() throws Exception
  {
    _cannotDeletes = new Long[2];
    for (int i=0; i<_cannotDeletes.length; i++)
    {
      _cannotDeletes[i] = createBe(ENTERPRISE, "CBE"+i, "Cannot delete BE "+i, false, false);
    }

    Long[] gtasPartners = new Long[2];
    for (int i=0; i<gtasPartners.length; i++)
    {
      gtasPartners[i] = createBe("ENT1", "TBE"+i, "Gtas Partner BE "+i, true);
    }

    createBes(5, 2);
    createSessions(1);
    createStateMachines(1);

    //for event[3]
    ArrayList list1 = new ArrayList();
    list1.add(_uIDs[0]);
    list1.add(_uIDs[2]);
    list1.add(_uIDs[3]);
    list1.add(_cannotDeletes[1]);

    //for event[4]
    ArrayList list2 = new ArrayList();
    list2.add(_uIDs[1]);
    list2.add(_uIDs[3]);
    list2.add(_uIDs[4]);
    list2.add(gtasPartners[1]);

    //for event[7]
    ArrayList list3 = new ArrayList();
    list3.add(_uIDs[0]);
    list3.add(_uIDs[2]);
    list3.add(_uIDs[3]);

    //for event[8]
    ArrayList list4 = new ArrayList();
    list4.add(_uIDs[0]);
    list4.add(_uIDs[1]);
    list4.add(DUMMY_UID);
    list4.add(_uIDs[3]);

    _events = new DeleteBusinessEntityEvent[]
              {
                //rejected: non existing single BE
                deleteBusinessEntityEvent(DUMMY_UID),
                //rejected: single,canDelete=false
                deleteBusinessEntityEvent(_cannotDeletes[0]),
                //rejected: single,gtasPartner
                deleteBusinessEntityEvent(gtasPartners[0]),

                //rejected: multi,some canDelete=false
                deleteBusinessEntityEvent(list1),
                //rejected: multi,some gtas partner
                deleteBusinessEntityEvent(list2),

                //accepted: single,own,canDelete=true
                deleteBusinessEntityEvent(_uIDs[2]),
                //accepted: single,partner,canDelete=true
                deleteBusinessEntityEvent(_uIDs[0]),

                //accepted: multi,canDelete=true,some own,some partner
                deleteBusinessEntityEvent(list3),
                //accepted: multi,canDelete=true,some non existing
                deleteBusinessEntityEvent(list4),

              };
  }

  protected void cleanUp()
  {
    clearCanDeletes();
    cleanUpBEs(null);
    cleanUpBEs(ENTERPRISE);
    cleanUpBEs("ENT1");
    purgeSessions();
  }

  protected void cleanTestData() throws Exception
  {
    BusinessEntity bizEntity = null;
    for (int i=0; i<_cannotDeletes.length; i++)
    {
      try
      {
        bizEntity = findBizEntityByUId(_cannotDeletes[i]);
        bizEntity.setCanDelete(true);
        updateToDb(bizEntity);
      }
      catch (Throwable ex)
      {
      }
    }

    closeAllSessions();
  }

  private void clearCanDeletes()
  {
    try
    {
      BusinessEntity bizEntity = null;
      Collection bes = findBizEntitiesByFilter(FILTER);
      for (Iterator i=bes.iterator(); i.hasNext(); )
      {
        bizEntity = (BusinessEntity)i.next();
        bizEntity.setCanDelete(true);
        updateToDb(bizEntity);
      }
    }
    catch (Throwable ex)
    {

    }

  }

  protected IEJBAction createNewAction()
  {
    return new DeleteBusinessEntityAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    DeleteBusinessEntityEvent delEvent = (DeleteBusinessEntityEvent)event;

//    if (delEvent.getBeUID() == null)
      checkBusinessEntitiesInDb(delEvent.getUids());
//    else
//      checkBusinessEntityInDb(delEvent.getBeUID());
  }


  // ************************ Own methods ********************

  private DeleteBusinessEntityEvent deleteBusinessEntityEvent(Long uID) throws Exception
  {
    return new DeleteBusinessEntityEvent(uID);
  }

  private DeleteBusinessEntityEvent deleteBusinessEntityEvent(Collection uIDs) throws Exception
  {
    return new DeleteBusinessEntityEvent(uIDs);
  }

  private void deleteCheckSuccess(
    IEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void deleteCheckFail(
    IEvent event, String session, StateMachine sm, boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.DELETE_ENTITY_ERROR);
  }

  private void checkBusinessEntitiesInDb(Collection beUIDs)
  {
    for (Iterator i=beUIDs.iterator(); i.hasNext(); )
    {
      checkBusinessEntityInDb((Long)i.next());
    }
  }

  private void checkBusinessEntityInDb(Long uID)
  {
    BusinessEntity dbBusinessEntity = null;
    try
    {
      dbBusinessEntity = findBizEntityByUId(uID);
    }
    catch (Throwable ex)
    {
      Log.debug("TEST",
        "[DeleteBusinessEntityActionTest.checkBusinessEntityInDb] Error:"+ex.getMessage());
    }

    if (dbBusinessEntity != null)
      assertEquals("Be state is not deleted!", BusinessEntity.STATE_DELETED,
        dbBusinessEntity.getState());

  }

}