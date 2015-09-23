/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetFeatureListActionTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 21 2002    Neo Sok Lay         Rewrite test case.
 */
package com.gridnode.gtas.server.acl.actions;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.event.EntityListResponseData;
import com.gridnode.pdip.framework.util.ServiceLocator;

import com.gridnode.pdip.base.acl.model.Feature;

import com.gridnode.gtas.server.rdm.IAttributeKeys;
import com.gridnode.gtas.events.acl.GetFeatureListEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.acl.helpers.ACLLogger;
import com.gridnode.gtas.server.acl.helpers.ActionTestHelper;

import junit.framework.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GetFeatureListActionTest extends ActionTestHelper
{
  private static final String CLASSNAME = "GetFeatureListActionTest";

  private static final String FEATURE_NAME = "Feature Name";
  private static final String FEATURE_DESC = "Feature Description";
  private static List ACTIONS_1 = null;
  private static List DATA_TYPE_1 = null;
  private static List ACTIONS_2 = null;
  private static List DATA_TYPE_2 = null;

  GetFeatureListEvent _events;
  Collection features = null;

  public GetFeatureListActionTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(GetFeatureListActionTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void prepareTestData() throws Exception
  {
    ArrayList alist1 = new ArrayList();
    alist1.add("Action 1");
    alist1.add("Action 1 a");
    ACTIONS_1 = alist1;

    ArrayList alist2 = new ArrayList();
    alist2.add("Action 2");
    alist2.add("Action 2 a");
    ACTIONS_2 = alist2;

    ArrayList dlist1 = new ArrayList();
    dlist1.add("Data Type 1");
    dlist1.add("Data Type 1 a");
    DATA_TYPE_1 = dlist1;

    ArrayList dlist2 = new ArrayList();
    dlist2.add("Data Type 2");
    dlist2.add("Data Type 2 a");
    DATA_TYPE_2 = dlist2;

    createSessions(1);
    createStateMachines(1);

  }

  protected void cleanUp()
  {
    for (int i=0; i<4; i++)
      deleteTestFeature(FEATURE_NAME+i);
  }

  protected void cleanTestData() throws Exception
  {
    closeAllSessions();
  }

  protected void unitTest() throws Exception
  {
    GetFeatureListEvent event = null;
    DataFilterImpl filter = null;
    DataFilterImpl allFilter = new DataFilterImpl();
    allFilter.addSingleFilter(null, Feature.UID, allFilter.getEqualOperator(),
      null, true);

    // Retrieve event is null
    getCheckFail(null, _sessions[0], _sm[0], true);

    // Retrieve All without data
    event = getFeatureListEvent();
    features = _aclMgr.getAllFeatures();
    getCheckSuccess(event, _sessions[0], _sm[0]);

    // Retrieve All with 1 data
    createTestFeature(FEATURE_NAME+0, FEATURE_DESC+0, DATA_TYPE_1, ACTIONS_1);
    event = getFeatureListEvent();
    features = _aclMgr.getAllFeatures();
    getCheckSuccess(event, _sessions[0], _sm[0]);

    // Retrieve All with 2 data
    createTestFeature(FEATURE_NAME+1, FEATURE_DESC+1, DATA_TYPE_2, ACTIONS_2);
    event = getFeatureListEvent();
    features = _aclMgr.getAllFeatures();
    getCheckSuccess(event, _sessions[0], _sm[0]);

    // Retrieve All with 3 data
    createTestFeature(FEATURE_NAME+2, FEATURE_DESC+2, DATA_TYPE_2, ACTIONS_1);
    event = getFeatureListEvent();
    features = _aclMgr.getAllFeatures();
    getCheckSuccess(event, _sessions[0], _sm[0]);

    // Retrieve All with 4 data
    createTestFeature(FEATURE_NAME+3, FEATURE_DESC+3, DATA_TYPE_1, ACTIONS_2);
    event = getFeatureListEvent();
    features = _aclMgr.getAllFeatures();
    getCheckSuccess(event, _sessions[0], _sm[0]);
  }

  protected IEJBAction createNewAction()
  {
    return new GetFeatureListAction();
  }

  protected void checkActionEffect(
    BasicEventResponse response, IEvent event, StateMachine sm)
  {
    Object returnData = response.getReturnData();

    assertNotNull("responsedata is null", returnData);
    assertTrue("response data type incorrect", returnData instanceof EntityListResponseData);

    EntityListResponseData listData = (EntityListResponseData) returnData;

    Collection entityList = listData.getEntityList();
    assertNotNull("Entity list is null", entityList);

    Object[] entityObjs = entityList.toArray();
    Object[] featuresArray = features.toArray();
    for (int i = 0; i < entityObjs.length; i++ )
    {
      checkReturnFeature(entityObjs[i], (Feature) featuresArray[i]);
    }
  }


  // ***************** Own methods ********************************

  private Feature createFeature(String featureName, String description,
          List dataTypes, List actions)
  {
    Feature feature = new Feature();
    feature.setActions(actions);
    feature.setDataTypes(dataTypes);
    feature.setDescr(description);
    feature.setFeature(featureName);
    return feature;
  }

  private void deleteTestFeature(String featureName)
  {
    try
    {
      Feature deleted = getTestFeature(featureName);
      if (deleted != null)
         _aclMgr.deleteFeature(new Long(deleted.getUId()));
    }
    catch (Exception ex)
    {
      ACLLogger.errorLog(CLASSNAME, "deleteTestFeature", "Unable to delete test data with featurename = "
                                    + featureName, ex);
    }
  }

  private Feature getTestFeature(String featureName) throws Exception
  {
    return _aclMgr.getFeatureByFeatureName(featureName);
  }

  private void createTestFeature(String featureName, String description,
          List dataTypes, List actions)
  {
    Feature feature = createFeature(featureName, description, dataTypes, actions);
    try
    {
      _aclMgr.createFeature(feature);
    }
    catch (Exception ex)
    {
      ACLLogger.errorLog(CLASSNAME, "createTestFeature", "Error creating feature with featurename = "
                                    + featureName, ex);
      assertTrue("Error in createTestData", false);
    }
    ACLLogger.infoLog(CLASSNAME, "createTestFeature", "Exit");
  }

  private GetFeatureListEvent getFeatureListEvent()
  {
    return new GetFeatureListEvent();
  }

  private void getCheckFail(
    IEvent event, String session, StateMachine sm,
    boolean eventEx)
  {
    checkFail(event, session, sm, eventEx, IErrorCode.FIND_ENTITY_LIST_ERROR);
  }

  private void getCheckSuccess(
    IEvent event, String session, StateMachine sm)
  {
    checkSuccess(event, session, sm, IErrorCode.NO_ERROR);
  }

  private void checkReturnFeature(Object entityObject, Feature feature)
  {
    assertNotNull("Entity is null", entityObject);
    assertTrue("Entity data type incorrect", entityObject instanceof Map);

    Map featureMap = (Map) entityObject;

    assertEquals("Feature is incorrect", feature.getFeature(),
                 featureMap.get(Feature.FEATURE));
    assertEquals("Description is incorrect", feature.getDescr(),
                 featureMap.get(Feature.DESCRIPTION));
    assertEquals("Actions is incorrect",
                            feature.getActions(), featureMap.get(Feature.ACTIONS));
    assertEquals("DataTypes is incorrect",
                            feature.getDataTypes(), featureMap.get(Feature.DATA_TYPES));
  }

}