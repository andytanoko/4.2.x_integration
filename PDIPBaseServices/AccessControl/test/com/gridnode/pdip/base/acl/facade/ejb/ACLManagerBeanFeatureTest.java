/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ACLManagerBeanFeatureTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 22 2002    Goh Kan Mun             Created
 * Feb 08 2007		Alain Ah Ming						Changed error logs to warn logs
 */

package com.gridnode.pdip.base.acl.facade.ejb;

import com.gridnode.pdip.base.acl.model.Feature;
import com.gridnode.pdip.base.acl.helpers.ACLLogger;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.log.Log;

import junit.framework.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Partial test case for testing ACLManagerBean. This class tests methods in the manager
 * that involve only with the <code>Feature</code> entity.<P>
 *
 * This tests the following business methods in the UserManagerBean:
 * <LI>testCreateFeature()          - mgr.createFeature(Feature)</LI>
 * <LI>testUpdateFeature()          - mgr.updateFeature(Feature)</LI>
 * <LI>testDeleteFeature()          - mgr.deleteFeature(featureUId)</LI>
 * <LI>testGetAllFeatures()         - mgr.getAllFeatures()</LI>
 * <LI>testGetFeatureByFeatureUId() - mgr.getFeatureByFeatureName(featureName)</LI>
 * <LI>testGetFeatureByFeatureUId() - mgr.getFeatureByFeatureUId(featureUId)</LI>
 * <P>
 *
 * @author Goh Kan Mun
 *
 * @version GT 4.0 VAN
 * @since 2.0
 */

public class ACLManagerBeanFeatureTest
  extends    TestCase
{
  private static final String FEATURE_NAME_1 = "Test Feature 1";
  private static final String DESC_1 = "Test Description 1";
  private static ArrayList ACTIONS_ELEMENT_1 = null;
  private static List ACTIONS_1 = null;
  private static ArrayList DATA_TYPE_ELEMENT_1 = null;
  private static List DATA_TYPE_1 = null;

  private static final String FEATURE_NAME_2 = "Test feature 2";
  private static final String DESC_2 = "Test Description 2";
  private static ArrayList ACTIONS_ELEMENT_2 = null;
  private static List ACTIONS_2 = null;
  private static ArrayList DATA_TYPE_ELEMENT_2 = null;
  private static List DATA_TYPE_2 = null;

  private static final String CLASSNAME = "ACLManagerBeanFeatureTest";

  IACLManagerHome home;
  IACLManagerObj remote;

  public ACLManagerBeanFeatureTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ACLManagerBeanFeatureTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    ACLLogger.infoLog(CLASSNAME, "setUp", "Enter");
    home = (IACLManagerHome)ServiceLookup.getInstance(
            ServiceLookup.CLIENT_CONTEXT).getHome(
             IACLManagerHome.class);
    assertNotNull("Home is null", home);
    remote = home.create();
    assertNotNull("remote is null", remote);
    createTestData();
    cleanup();
    ACLLogger.infoLog(CLASSNAME,"setUp", "Exit");
  }

  private void createTestData()
  {
    ACTIONS_ELEMENT_1 = new ArrayList();
    ACTIONS_ELEMENT_1.add("Add");
    ACTIONS_ELEMENT_1.add("Delete");
    ACTIONS_1 = ACTIONS_ELEMENT_1;
    ACTIONS_ELEMENT_2 = new ArrayList();
    ACTIONS_ELEMENT_2.add("Update");
    ACTIONS_ELEMENT_2.add("Retrieve");
    ACTIONS_2 = ACTIONS_ELEMENT_2;

    DATA_TYPE_ELEMENT_1 = new ArrayList();
    DATA_TYPE_ELEMENT_1.add("Integer");
    DATA_TYPE_ELEMENT_1.add("Short");
    DATA_TYPE_1 = DATA_TYPE_ELEMENT_1;
    DATA_TYPE_ELEMENT_2 = new ArrayList();
    DATA_TYPE_ELEMENT_2.add("Long");
    DATA_TYPE_ELEMENT_2.add("NUMBER");
    DATA_TYPE_2 = DATA_TYPE_ELEMENT_2;
  }

  protected void tearDown() throws java.lang.Exception
  {
    ACLLogger.infoLog(CLASSNAME, "tearDown", "Enter");
    cleanup();
    ACLLogger.infoLog(CLASSNAME, "tearDown", "Exit");
  }

  protected void cleanup() throws Exception
  {
    deleteTestFeature(FEATURE_NAME_1);
    deleteTestFeature(FEATURE_NAME_2);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  private Feature createFeature(List actions, List dataTypes, String desc, String featureName)
  {
    Feature feature = new Feature();
    feature.setActions(actions);
    feature.setDataTypes(dataTypes);
    feature.setDescr(desc);
    feature.setFeature(featureName);
    return feature;
  }

  private Feature retrieveTestFeature(String featureName) throws Exception
  {
    return remote.getFeatureByFeatureName(featureName);
  }

  private void deleteTestFeature(String featureName) throws Exception
  {
    Feature retrieve = retrieveTestFeature(featureName);
    if (retrieve != null)
       remote.deleteFeature(new Long(retrieve.getUId()));
  }

  private Feature addTestFeature(List actions, List dataTypes, String desc, String featureName)
          throws Exception
  {
    Feature inserted = createFeature(actions, dataTypes, desc, featureName);
    remote.createFeature(inserted);
    return remote.getFeatureByFeatureName(inserted.getFeature());
  }

  private void checkIdenticalFeature(Feature feature1, Feature feature2, boolean checkUId)
  {
    assertEquals("Description not the same!", feature1.getDescr(), feature2.getDescr());
    assertEquals("Feature name not the same!", feature1.getFeature(), feature2.getFeature());
    assertEquals("Actions not the same!", feature1.getActions(), feature2.getActions());
    assertEquals("DataTypes not the same!", feature1.getDataTypes(), feature2.getDataTypes());
    if (checkUId)
       assertEquals("UId not the same!", feature1.getUId(), feature2.getUId());
  }

  private boolean isIdenticalFeature(Feature feature1, Feature feature2, boolean checkUId)
  {
    if ((feature1.getDescr().equals(feature2.getDescr())) &&
        (feature1.getFeature().equals(feature2.getFeature())) &&
        (feature1.getActions().equals(feature2.getActions())) &&
        (feature1.getDataTypes().equals(feature2.getDataTypes())))
    {
      if (checkUId)
      {
        if (feature1.getUId() == feature2.getUId())
          return true;
        else
          return false;
      }
      else
        return true;
    }
    return false;
  }


  public void testCreateFeature() throws Exception
  {
    try
    {
      Feature retrieved = null;

      // test create feature.
      Feature feature1 = createFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);
      checkCreateFeaturePass(feature1, FEATURE_NAME_1);

      // test create duplicate feature.
      Feature feature2 = createFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);
      checkCreateFeatureFail(feature2, "Able to add in duplicate feature into the database.");

      // test create 2nd feature
      Feature feature3 = createFeature(ACTIONS_2, DATA_TYPE_2, DESC_2, FEATURE_NAME_2);
      checkCreateFeaturePass(feature3, FEATURE_NAME_2);

      // delete previous test
      deleteTestFeature(feature3.getFeature());

      // test create feature with same featureName, dataType and desc but different action.
      Feature feature4 = createFeature(ACTIONS_2, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);
      checkCreateFeatureFail(feature4, "Able to add in same featureName, dataType and " +
                                       "desc but different action.");

      // test create feature with same featureName, desc and action but different dataType.
      Feature feature5 = createFeature(ACTIONS_1, DATA_TYPE_2, DESC_1, FEATURE_NAME_1);
      checkCreateFeatureFail(feature5, "Able to add in same featureName, desc and action " +
                                       "but different dataType.");

      // test create feature with same featureName, dataType and action but different desc.
      Feature feature6 = createFeature(ACTIONS_1, DATA_TYPE_1, DESC_2, FEATURE_NAME_1);
      checkCreateFeatureFail(feature6, "Able to add in same featureName, dataType and " +
                                       "action but different desc.");

      // test create feature with same desc, dataType and action but different featureName.
      Feature feature7 = createFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_2);
      checkCreateFeaturePass(feature7, FEATURE_NAME_2);

    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testCreateFeature", "fails", ex);
      throw ex;
    }
  }

  private void checkCreateFeaturePass(Feature feature, String featureName) throws Exception
  {
    remote.createFeature(feature);
    Feature retrieved = retrieveTestFeature(featureName);
    checkIdenticalFeature(feature, retrieved, false);
  }

  private void checkCreateFeatureFail(Feature feature, String errorMessage)
  {
    try
    {
      remote.createFeature(feature);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }


  public void testUpdateFeature() throws Exception
  {
    try
    {
      Feature existing = null;

      // test update non-existing feature.
      Feature feature1 = createFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);
      checkUpdateFeatureFail(feature1, "Able to update non-existing feature.");

      // create feature in database
      existing = addTestFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);

      // test update existing feature change desc2.
      existing.setDescr(DESC_2);
      checkUpdateFeaturePass(existing, FEATURE_NAME_1);

      // test update existing feature change action but using old version.
      existing.setActions(ACTIONS_2);
      checkUpdateFeatureFail(existing, FEATURE_NAME_1);

      // test update existing feature change action using updated version.
      existing = retrieveTestFeature(FEATURE_NAME_1);
      existing = remote.getFeatureByFeatureUId(new Long(existing.getUId()));
      existing.setActions(ACTIONS_2);
      checkUpdateFeaturePass(existing, FEATURE_NAME_1);

      // test update existing feature change dataType using updated version.
      existing = retrieveTestFeature(FEATURE_NAME_1);
      existing.setDataTypes(DATA_TYPE_2);
      checkUpdateFeaturePass(existing, FEATURE_NAME_1);

      // test update existing feature change featureName using updated version.
      existing = retrieveTestFeature(FEATURE_NAME_1);
      assertNull("Feature 2 exist. Test environment not set properly.", retrieveTestFeature(FEATURE_NAME_2));
      existing.setFeature(FEATURE_NAME_2);
      checkUpdateFeaturePass(existing, FEATURE_NAME_2);
      assertNull("Feature 1 exist. Result not correct. Two copies of same data: Feature1 and Feature2!", retrieveTestFeature(FEATURE_NAME_1));
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testUpdateFeature", "fails", ex);
      throw ex;
    }
  }

  private void checkUpdateFeatureFail(Feature feature, String errorMessage)
  {
    try
    {
      remote.updateFeature(feature);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }

  private void checkUpdateFeaturePass(Feature feature, String featureName) throws Exception
  {
    remote.updateFeature(feature);
    Feature retrieved = retrieveTestFeature(featureName);
    checkIdenticalFeature(feature, retrieved, false);
  }

  public void testDeleteFeature() throws Exception
  {
    try
    {
      Feature insert1 = null;
      // Delete non-existing Feature
      Feature feature1 = createFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);
      checkDeleteFeatureFail(feature1, "delete non-existing feature");

      // Create Feature.
      insert1 = addTestFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);

      // Delete existing feature
      checkDeleteFeaturePass(insert1, FEATURE_NAME_1);

    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testDeleteFeature", "fails", ex);
      throw ex;
    }
  }

  private void checkDeleteFeaturePass(Feature feature, String featureName) throws Exception
  {
    remote.deleteFeature(new Long(feature.getUId()));
    Feature retrieve = retrieveTestFeature(featureName);
    assertNull("Delete not successful.", retrieve);
  }

  private void checkDeleteFeatureFail(Feature feature, String errorMessage) throws Exception
  {
    try
    {
      remote.deleteFeature(new Long(feature.getUId()));
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
    }
  }


  public void testGetFeatureByFeatureName() throws Exception
  {
    try
    {
      Feature exist = addTestFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);
      // Find exisitng
      Feature mock = createFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);
      checkGetFeatureByFeatureNameSuccess(FEATURE_NAME_1, mock);

      deleteTestFeature(exist.getFeature());
      // Find non-exisitng
      checkGetFeatureByFeatureNameFail(FEATURE_NAME_1, "Deleting non-existing");
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetFeatureByFeatureName", "fails", ex);
      throw ex;
    }
  }

  private void checkGetFeatureByFeatureNameFail(String featureName, String errorMsg) throws Exception
  {
    Feature findFeature = remote.getFeatureByFeatureName(featureName);
    assertNull(errorMsg, findFeature);
  }

  private void checkGetFeatureByFeatureNameSuccess(String featureName, Feature feature) throws Exception
  {
    Feature findFeature = remote.getFeatureByFeatureName(featureName);
    assertNotNull(findFeature);
    checkIdenticalFeature(feature, findFeature, false);
  }


  public void testGetFeatureByFeatureUId() throws Exception
  {
    try
    {
      Feature exist = addTestFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);
      // Find exisitng
      Feature mock = createFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);
      checkGetFeatureByFeatureUIdSuccess(exist.getUId(), mock);

      deleteTestFeature(exist.getFeature());
      // Find non-exisitng
      checkGetFeatureByFeatureUIdFail(exist.getUId(), "Deleting non-existing");
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testGetFeatureByFeatureUId", "fails", ex);
      throw ex;
    }
  }

  private void checkGetFeatureByFeatureUIdFail(long uId, String errorMessage)
  {
    try
    {
      Feature feature = remote.getFeatureByFeatureUId(new Long(uId));
      assertNull(errorMessage, feature);
    }
    catch (Exception ex)
    {
    }
  }

  private void checkGetFeatureByFeatureUIdSuccess(long uId, Feature feature)  throws Exception
  {
    Feature findFeature = remote.getFeatureByFeatureUId(new Long(uId));
    assertNotNull(findFeature);
    checkIdenticalFeature(feature, findFeature, false);
  }


  public void testGetAllFeatures() throws Exception
  {
    try
    {
      Feature exist1 = addTestFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);
      Feature exist2 = addTestFeature(ACTIONS_2, DATA_TYPE_2, DESC_2, FEATURE_NAME_2);

      Feature mock1 = createFeature(ACTIONS_1, DATA_TYPE_1, DESC_1, FEATURE_NAME_1);
      Feature mock2 = createFeature(ACTIONS_2, DATA_TYPE_2, DESC_2, FEATURE_NAME_2);

      Feature[] features = null;

      //Check existing 2
      features = new Feature[2];
      features[0] = mock1;
      features[1] = mock2;
      checkGetAllFeaturesPass(features);

      // Delete feature1
      deleteTestFeature(FEATURE_NAME_1);

      // Check Existing feature1
      features = new Feature[1];
      features[0] = mock1;
      checkGetAllFeaturesFail(features);

      // Check Existing feature2
      features = new Feature[1];
      features[0] = mock2;
      checkGetAllFeaturesPass(features);

      // Delete feature2
      deleteTestFeature(FEATURE_NAME_2);

      // Check Existing feature1
      features = new Feature[1];
      features[0] = mock1;
      checkGetAllFeaturesFail(features);

      // Check Existing feature2
      features = new Feature[1];
      features[0] = mock2;
      checkGetAllFeaturesFail(features);

      // Check non-Existing
      features = null;
      checkGetAllFeaturesPass(features);
    }
    catch (Exception ex)
    {
      ACLLogger.warnLog(CLASSNAME, "testFindFeatures", "fails", ex);
      throw ex;
    }
  }

  private void checkGetAllFeaturesFail(Feature[] features)
          throws Exception
  {
    Collection findFeatures = remote.getAllFeatures();
    if (findFeatures != null)
    {
      if (features != null)
      {
        if (features.length != findFeatures.size())
        {
          ACLLogger.infoLog(CLASSNAME, "checkGetAllFeaturesFail",
                            "Fail because the expected size(" + features.length +
                            ") and the retrieved size(" + findFeatures.size() +
                            ") are not the same.");
          return;
        }
        boolean found = false;
        boolean[] isThere = new boolean[features.length];
        // Reset isThere.
        for (int i = 0; i < isThere.length; i++)
            isThere[i] = false;
        Iterator iter = findFeatures.iterator();
        while (iter.hasNext())
        {
          found = false;
          Feature retrieved = (Feature) iter.next();
          for (int i = 0; i < features.length; i++)
          {
            if (retrieved.getFeature().equals(features[i].getFeature()))
            {
              if (isIdenticalFeature(retrieved, features[i], false))
              {
                isThere[i] = true;
                found = true;
                break;
              }
            }
          }
          if (!found)
          {
            ACLLogger.infoLog(CLASSNAME, "checkGetAllFeaturesFail",
                              "Fail because retrieved feature(" +
                              retrieved.getEntityDescr() + ") not found");
            return;
          }
        }
        for (int i = 0; i < isThere.length; i++)
        {
          if (!isThere[i])
          {
            ACLLogger.infoLog(CLASSNAME, "checkGetAllFeaturesFail",
                              "Fail because expected feature(" +
                              features[i].getEntityDescr() + ") not found");
            return;
          }
        }
      }
      else //features == null
      {
        ACLLogger.infoLog(CLASSNAME, "checkGetAllFeaturesFail",
                          "Fail because the expected size(null" +
                          ") and the retrieved size(" + findFeatures.size() +
                          ") are not the same.");
      }
    }
    else // findFeatures == null
    {
      if (features != null && features.length > 0)
      {
        ACLLogger.infoLog(CLASSNAME, "checkGetAllFeaturesFail",
                          "Fail because the expected size(" + features.length +
                          ") and the retrieved size(null" +
                          ") are not the same.");
      }
      else
        assertTrue("Expected list and retrieved list are both empty!", false);
    }
    assertTrue("Expected list and retrieved list are the same! Should not be the same!!!", false);
  }

  private void checkGetAllFeaturesPass(Feature[] features)
          throws Exception
  {
    Collection findFeatures = remote.getAllFeatures();
    if (findFeatures != null && findFeatures.size() > 0)
    {
      if (features != null)
      {
        assertEquals("Retrieved size is not expected!", features.length, findFeatures.size());
        Feature retrieved = null;
        boolean found = false;
        boolean[] isThere = new boolean[features.length];
        // Reset isThere.
        for (int i = 0; i < isThere.length; i++)
          isThere[i] = false;
        Iterator iter = findFeatures.iterator();
        while (iter.hasNext())
        {
          found = false;
          retrieved = (Feature) iter.next();
          for (int i = 0; i < features.length; i++)
          {
            if (retrieved.getFeature().equals(features[i].getFeature()))
            {
              checkIdenticalFeature(features[i], retrieved, false);
              isThere[i] = true;
              found = true;
              break;
            }
          }
          if (!found)
            assertTrue("Feature " + retrieved.getEntityDescr() + " not expected!", false);
        }
        for (int i = 0; i < isThere.length; i++)
          assertTrue("Feature " + features[i].getEntityDescr() +
                     " not in the retrieved list!", isThere[i]);
      }
      else //features == null
      {
        assertTrue("Expected empty list but retrieved not empty!" +
                   " Could be caused by already existing data.", false);
      }
    }
    else // findFeatures == null
    {
      if (features != null && features.length > 0)
        assertTrue("Data not found!", false);
    }
  }


}