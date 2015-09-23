/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PublishBusinessEntityActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 19 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.actions;

import com.gridnode.gtas.events.enterprise.PublishBusinessEntityEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.bizreg.helpers.PublishDelegate;
import com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.model.RegistryConnectInfo;
import com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo;
import com.gridnode.pdip.app.bizreg.pub.model.OrganizationInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This test case tests the PublishBusinessEntityAction class.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class PublishBusinessEntityActionTest extends ActionTestHelper
{
  PublishBusinessEntityEvent[] _events;
  //String _queryUrl = "http://localhost:8080/RegistryServer";
  //String _publishUrl = "http://localhost:8080/RegistryServer";
  String _queryUrl = "http://192.168.213.186:8080/juddi/inquiry";
  String _publishUrl = "http://192.168.213.186:8080/juddi/publish";
  String _user = "testuser";
  String _password = "testuser";
  String _saveName = "PublishBusinessEntityActionTest"  ;
  String _enterpriseId = "543"; //"521";
  String _beId = "PUBE";
  
  /**
   * Creates the PublishBusinessEntityActionTest.
   * @param name
   */
  public PublishBusinessEntityActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(PublishBusinessEntityActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#checkActionEffect(com.gridnode.pdip.framework.rpf.event.BasicEventResponse, com.gridnode.pdip.framework.rpf.event.IEvent, com.gridnode.pdip.framework.rpf.ejb.StateMachine)
   */
  protected void checkActionEffect(
    BasicEventResponse response,
    IEvent event,
    StateMachine sm)
  {
    PublishBusinessEntityEvent publishEvent = (PublishBusinessEntityEvent)event;

    Long beUid;
    OrganizationInfo orgInfo;
    for (Iterator i=publishEvent.getBeUIdS().iterator(); i.hasNext(); )
    {
      try
      {
        beUid = (Long)i.next();
        orgInfo = (OrganizationInfo)findRegistryInfo(BusinessEntity.ENTITY_NAME,
                    beUid, OrganizationInfo.TYPE_ORGANIZATION,
                    _queryUrl);
                    
        assertNotNull("OrganizationInfo is null", orgInfo);
        //TODO print the orgInfo
        System.out.println("Published OrgInfo Key = "+orgInfo.getKey());
      }
      catch (Exception e)
      {
        Log.err("TEST", "[PublishBusinessEntityActionTest.checkActionEffect]", e);
      }
    }
  }

  private IRegistryInfo findRegistryInfo(String entityName, Long uid, int registryType, String queryUrl)
    throws Exception
  {
    return PublishDelegate.getPublicRegistryMgr().findQueriableObject(
      registryType, entityName, uid.toString(), queryUrl);
  }
  
  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#cleanTestData()
   */
  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#cleanUp()
   */
  protected void cleanUp()
  {
    cleanUpConnInfos();
    purgeSessions();
  }

  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#createNewAction()
   */
  protected IEJBAction createNewAction()
  {
    return new PublishBusinessEntityAction();
  }

  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#prepareTestData()
   */
  protected void prepareTestData() throws Exception
  {
    createStateMachines(1);
    createSessions(1);
    
    //retrieve the testing be
    BusinessEntity be = findBizEntity(_enterpriseId, _beId);
    _uIDs = new Long[] {
              new Long(be.getUId()),
            };
    
    RegistryConnectInfo connInfo = new RegistryConnectInfo();
    connInfo.setName(_saveName);
    connInfo.setPublishPassword(_password);
    connInfo.setPublishUrl(_publishUrl);
    connInfo.setPublishUser(_user);
    connInfo.setQueryUrl(_queryUrl);
    _bizRegMgr.createRegistryConnectInfo(connInfo);
    
    _events = new PublishBusinessEntityEvent[] 
    {
      createTestEvent(_uIDs, _saveName),
    };
    
  }

  private PublishBusinessEntityEvent createTestEvent(
    Long[] beUids, String saveName)
    throws EventException
  {
    return new PublishBusinessEntityEvent(
                wrapCollection(beUids),
                saveName);
  }
  
  /**
   * @see com.gridnode.gtas.server.enterprise.helpers.ActionTestHelper#unitTest()
   */
  protected void unitTest() throws Exception
  {
    publishCheckSuccess(_events[0], _sessions[0], _sm[0]);
  }

  private void publishCheckFail(
    PublishBusinessEntityEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.REGISTRY_PUBLISH_ERROR);
  }

  private void publishCheckSuccess(
    PublishBusinessEntityEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void cleanUpConnInfos()
  {
    try
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, RegistryConnectInfo.NAME,
        filter.getLikeOperator(), _saveName, false);
      
      Collection uids = _bizRegMgr.findRegistryConnectInfoKeys(filter);
      for (Iterator i=uids.iterator(); i.hasNext(); )
      {
        _bizRegMgr.deleteRegistryConnectInfo((Long)i.next());
      }
    }
    catch (Throwable e)
    {
    }
  }

}
