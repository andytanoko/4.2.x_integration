/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResourceLinkManagerBeanTest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 01 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.facade.ejb;

import com.gridnode.gtas.server.enterprise.model.*;

import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

import junit.framework.*;

import java.io.*;
import java.util.Date;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedList;
import java.sql.Timestamp;

/**
 * Test case for testing ResourceLinkManagerBean<P>
 *
 * This tests the following business methods in the ResourceLinkManagerBean:<P>
 * <LI>addResourceLink(ResourceLink)</LI>
 * <LI>updateResourceLink(ResourceLink)</LI>
 * <LI>removeResourceLink(UID,RemoveNextLinks)</LI>
 * <LI>updateToResourceLinks(fromType,fromUID,toType,toUIDs)</LI>
 * <P>
 *
 * The following business methods are implicitly tested by this test case:<P>
 * <LI>getResourceLinkByUID(resourceLinkUID,getNextLinks)</LI>
 * <LI>getResourceLinksByFilter(filter,getNextLinks)</LI>
 * <LI>getToResourceLinks(fromType,fromUID,toType)</LI>
 * <P>
 *
 * No test cases for the following methods in the ResourceLinkManagerBean:<P>
 * <LI>getFromResourceLinks(fromType,toType,toUID)</LI>
 * <LI>removeResourceLinks(LinksToRemove, RemoveNextLinks)</LI>
 * <P>
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class ResourceLinkManagerBeanTest
  extends    TestCase
{
  private IResourceLinkManagerObj     _resourceLinkMgr;
  private ResourceLink[] _resourceLinks;
  private Long[] _uIDs;

  public ResourceLinkManagerBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(ResourceLinkManagerBeanTest.class);
  }

  protected void setUp() throws java.lang.Exception
  {
    try
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.setUp] Enter");

      _resourceLinkMgr = getResourceLinkMgr();
      cleanUp();
    }
    finally
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.setUp] Exit");
    }
  }

  protected void tearDown() throws java.lang.Exception
  {
    Log.log("TEST", "[ResourceLinkManagerBeanTest.tearDown] Enter");
    cleanUp();
    Log.log("TEST", "[ResourceLinkManagerBeanTest.tearDown] Exit");
  }


  // *********************** test cases **************************** //

  public void testFunctions() throws Throwable
  {
    prepareTestData();
    managementFunctionsTest();
    cleanupTestData();
  }

  private void prepareTestData()
  {
    _resourceLinks = new ResourceLink[]
      {
        createResourceLink(
          new Object[] {
            "Type1", new Long(1), "Type2", new Long(2), Boolean.TRUE}),
        createResourceLink(
          new Object[] {
            "Type1", new Long(3), "Type2", new Long(4), Boolean.TRUE}),
        createResourceLink(
          new Object[] {
            "Type2", new Long(2), "Type3", new Long(5), Boolean.FALSE}),
        createResourceLink(
          new Object[] {
            "Type3", new Long(5), "Type2", new Long(2), Boolean.TRUE}),
        createResourceLink(
          new Object[] {
            "Type3", new Long(6), "Type2", new Long(2), Boolean.TRUE}),
        createResourceLink(
          new Object[] {
            "Type2", new Long(4), "Type4", new Long(7), Boolean.FALSE}),
        createResourceLink(
          new Object[] {
            "Type2", new Long(2), "Type4", new Long(8), Boolean.TRUE}),
        createResourceLink(
          new Object[] {
            "Type2", new Long(4), "Type5", new Long(9), Boolean.TRUE}),
        createResourceLink(
          new Object[] {
            "Type5", new Long(9), "Type4", new Long(7), Boolean.TRUE}),
        createResourceLink(
          new Object[] {
            "Type2", new Long(4), "Type4", new Long(10), Boolean.TRUE}),
        createResourceLink(
          new Object[] {
            "Type1", new Long(1), "Type2", new Long(11), Boolean.TRUE}),
        createResourceLink(
          new Object[] {
            "Type2", new Long(11), "Type3", new Long(5), Boolean.TRUE}),
        createResourceLink(
          new Object[] {
            "Type3", new Long(5), "Type2", new Long(11), Boolean.TRUE}),
      };
  }

  private void cleanupTestData()
  {
  }


  // *************** Management **********************************
  private void managementFunctionsTest() throws Throwable
  {
    _uIDs = new Long[_resourceLinks.length];

    //Add pass
    for (int i=0; i<_uIDs.length; i++)
    {
      _uIDs[i] = checkAddSelfPass(_resourceLinks[i]);
    }

    //create fail: duplicate [Type1,1]-->[Type2,2]
    checkAddSelfFail(_resourceLinks[0]);

    //update pass: [Type1,3]-->[Type2,4]
    caseUpdateSelfPass();

    //update pass: add [Type2,11]-->[Type4,12]
    //             remove [Type2,11]-->[Type4,8]
    //update pass: update [Type3,5]-->[Type2,2], [Type2,11] order
    caseUpdateToLinksPass();

    //update fail: (remove canDelete=false [Type2,2]-->[Type3,5])
    caseUpdateToLinksFail();

    //delete fail: canDelete=false for [Type2,2]-->[Type3,5]
    checkRemoveFail(_uIDs[2], false);

    //delete fail: canDelete=false for next links [Type2,4]-->[Type4,7]
    //of [Type1,3]-->[Type2,4]
    checkRemoveFail(_uIDs[5], true);

    //delete pass: [Type2,4]-->[Type5,9] only, dangling [Type5,9]-->[Type4,7]
    checkRemovePass(_uIDs[7], false);

    //delete pass: [Type1,1]-->[Type2,11], include [Type2,11]-->[Type3,5]
    checkRemovePass(_uIDs[11], true);

    //delete pass: [Type5,9]-->[Type4,7], [Type4,7] no next link
    checkRemovePass(_uIDs[8], true);
  }

  private void caseUpdateSelfPass() throws Throwable
  {
    Log.debug("TEST", "[ResourceLinkManagerBeanTest.caseUpdateSelfPass] Enter");
    //update pass: no nextLinks [Type1,3]-->[Type2,4]
    ResourceLink resourceLink = getResourceLinkByUID(_uIDs[1], false);
    resourceLink.setPriority(9);

    checkUpdatePass(resourceLink);

    Log.debug("TEST", "[ResourceLinkManagerBeanTest.caseUpdateSelfPass] Exit");
  }

  private void caseUpdateToLinksFail()
  {
    Log.debug("TEST", "[ResourceLinkManagerBeanTest.caseUpdateToLinksFail] Enter");
    //update fail: updateToLinks
    //1. get link [Type2,2]-->[Type3,5]

    Collection toLinks = getToResourceLinks("Type2", new Long(2),
                             "Type3");
    ArrayList type3s = new ArrayList(toLinks);
    //remove [Type3,5]
    type3s.remove(0);
    ArrayList type3UIDs = new ArrayList();
    for (Iterator i=type3s.iterator(); i.hasNext(); )
    {
      ResourceLink link = (ResourceLink)i.next();
      type3UIDs.add(link.getToUID());
    }

    checkUpdateToLinksFail("Type2", new Long(2),
                             "Type3", type3UIDs);

    Log.debug("TEST", "[ResourceLinkManagerBeanTest.caseUpdateToLinksFail] Exit");
  }

  private void caseUpdateToLinksPass() throws Throwable
  {
    Log.debug("TEST", "[ResourceLinkManagerBeanTest.caseUpdateToLinks] Enter");
    ArrayList removedNodes = new ArrayList();
    ArrayList addedNodes = new ArrayList();
    ArrayList updatedNodes = new ArrayList();

    //update pass:
    //1. get  [Type2,11]-->[Type4]
    ResourceLink resourceLink = getResourceLinkByUID(_uIDs[11], false);

    ArrayList type4s = new ArrayList(getToResourceLinks("Type2",
                             new Long(11), "Type4"));
    //Add [Type4,12]
    type4s.add(new Long(12));
    //remove [Type4,8]
    type4s.remove(0);

    ArrayList type4UIDs = new ArrayList();
    for (Iterator i=type4s.iterator(); i.hasNext(); )
    {
      ResourceLink link = (ResourceLink)i.next();
      type4UIDs.add(link.getKey());
    }

    checkUpdateToLinksPass("Type2", new Long(11), "Type4", type4UIDs);


    //2. get [Type3,5]-->[Type2]
    ArrayList type2s = new ArrayList(getToResourceLinks("Type3", new Long(5), "Type2"));
    //reverse the order
    ArrayList type2UIDs = new ArrayList();
    for (int i=type2s.size()-1; i>=0; i--)
    {
      ResourceLink link = (ResourceLink)type2s.get(i);
      type2UIDs.add(link.getToUID());
    }

    checkUpdateToLinksPass("Type3", new Long(5), "Type2", type2UIDs);

    Log.debug("TEST", "[ResourceLinkManagerBeanTest.caseUpdateToLinks] Exit");
  }

  private void checkRemovePass(Long uID, boolean removeNextLinks)
  {
    Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkRemovePass] Enter");
    Collection nextLinks = getResourceLinkByUID(uID, true).getNextLinks();
    try
    {
      _resourceLinkMgr.removeResourceLink(uID, removeNextLinks);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ResourceLinkManagerBeanTest.checkRemovePass]", ex);
      assertTrue("Error in _resourceLinkMgr.removeResourceLink", false);
    }

    //check that link is removed
    checkNonExistence(uID);

    //check that the next links are removed
    for (Iterator i=nextLinks.iterator(); i.hasNext(); )
    {
      ResourceLink link = (ResourceLink)i.next();
      if (removeNextLinks)
        checkNonExistence((Long)link.getKey());
      else
        checkExistence((Long)link.getKey());
    }
  }

  private void checkExistence(Long uID)
  {
    try
    {
      getResourceLinkByUID(uID, false);
    }
    catch (Throwable ex)
    {
      Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkExistence] Unexpected: " +
        ex.getMessage());
      assertTrue("ResourceLink does not exist: "+uID, false);
    }
  }

  private void checkNonExistence(Long uID)
  {
    try
    {
      getResourceLinkByUID(uID, false);
    }
    catch (Throwable ex)
    {
      Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkNonExistence] Caught: " +
        ex.getMessage());
    }
  }

  private void checkRemoveFail(Long uID, boolean removeNextLinks)
  {
    //mark delete
    try
    {
      Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkRemoveFail] Enter");
      _resourceLinkMgr.removeResourceLink(uID, removeNextLinks);
      assertTrue("Remove not fail as expected", false);
    }
    catch (AssertionFailedError ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkRemoveFail] "+ex.getMessage());
    }
  }

  private void checkUpdateFail(ResourceLink mockLink)
  {
    try
    {
      Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkUpdateFail] Enter");
      updateToDb(mockLink);
      assertTrue("Update not fail as expected", false);
    }
    catch (AssertionFailedError ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkUpdateFail] "+ex.getMessage());
    }
  }

  private void checkUpdatePass(ResourceLink mockLink) throws Throwable
  {
    Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkUpdatePass] Enter");
    updateToDb(mockLink);

    //check
    ResourceLink updated = getResourceLinkByUID((Long)mockLink.getKey(), false);

    checkUpdatedResourceLink(mockLink, updated);
    Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkUpdatePass] Exit");
  }

  private void checkUpdatedResourceLink(
    ResourceLink mockLink, ResourceLink updated)
  {
    //check link information
    assertEquals("UID is incorrect", mockLink.getKey(), updated.getKey());
    assertEquals("FromResourceType incorrect", mockLink.getFromType(), updated.getFromType());
    assertEquals("FromResourceUID is incorrect", mockLink.getFromUID(), updated.getFromUID());
    assertEquals("ToResourceType incorrect", mockLink.getToType(), updated.getToType());
    assertEquals("ToResourceUID is incorrect", mockLink.getToUID(), updated.getToUID());
    assertEquals("CanDelete is incorrect", mockLink.canDelete(), updated.canDelete());
    assertEquals("Priority is incorrect", mockLink.getPriority(), updated.getPriority());
    assertTrue("Version is incorrect", mockLink.getVersion() < updated.getVersion());
  }

  private void checkUpdateToLinksPass(
    String fromResourceType,
    Long fromResourceUID,
    String toResourceType,
    Collection toResourceUIDs) throws Throwable
  {
    Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkUpdatePass] Enter");
    //updates
    updateToDb(fromResourceType, fromResourceUID, toResourceType,
      toResourceUIDs);

    Collection updatedToLinks = getToResourceLinks(fromResourceType, fromResourceUID, toResourceType);

    checkUpdatedResourceLink(
      fromResourceType, fromResourceUID, toResourceType, toResourceUIDs, updatedToLinks);
  }

  private void checkUpdatedResourceLink(
    String fromResourceType,
    Long fromResourceUID,
    String toResourceType,
    Collection toResourceUIDs,
    Collection updatedToLinks)
  {
    ArrayList mockToUIDs = new ArrayList(toResourceUIDs);
    int i=0;
    for (Iterator iter=updatedToLinks.iterator(); iter.hasNext(); i++)
    {
      ResourceLink updated = (ResourceLink)iter.next();

      assertEquals("FromResourceType incorrect", fromResourceType, updated.getFromType());
      assertEquals("FromResourceUID is incorrect", fromResourceUID, updated.getFromUID());
      assertEquals("ToResourceType incorrect", toResourceType, updated.getToType());
      assertEquals("ToResourceUID is incorrect", mockToUIDs.get(i), updated.getToUID());
      assertEquals("Priority is incorrect", i, updated.getPriority());
    }
  }

  private void checkUpdateToLinksFail( String fromResourceType,
    Long fromResourceUID,
    String toResourceType,
    Collection toResourceUIDs)
  {
    try
    {
      Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkUpdateFail] Enter");
      updateToDb(fromResourceType, fromResourceUID, toResourceType, toResourceUIDs);
      assertTrue("Update not fail as expected", false);
    }
    catch (AssertionFailedError ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkUpdateFail] "+ex.getMessage());
    }
  }

  private void checkAddSelfFail(ResourceLink mockLink)
  {
    try
    {
      Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkAddFail] Enter");
      addToDb(mockLink);
      assertTrue("Add not fail as expected", false);
    }
    catch (AssertionFailedError ex)
    {
      throw ex;
    }
    catch (Throwable ex)
    {
      Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkAddFail] "+ex.getMessage());
    }
  }

  private Long checkAddSelfPass(ResourceLink mockLink) throws Throwable
  {
    Log.debug("TEST", "[ResourceLinkManagerBeanTest.checkAddPass] Enter");
    Long uID = addToDb(mockLink);

    //check retrieved record
    ResourceLink created = getResourceLinkByUID(uID, false);

    assertNotNull("ResourceLink retrieved is null", created);
    checkCreatedResourceLink(mockLink, uID, created);

    return uID;
  }

  private void checkCreatedResourceLink(
    ResourceLink mockLink, Long uID, ResourceLink created)
  {
    //check node information
    if (uID != null)
      assertEquals("UID is incorrect", uID, created.getKey());

    assertEquals("FromResourceType incorrect", mockLink.getFromType(), created.getFromType());
    assertEquals("FromResourceUID is incorrect", mockLink.getFromUID(), created.getFromUID());
    assertEquals("ToResourceType incorrect", mockLink.getToType(), created.getToType());
    assertEquals("ToResourceUID is incorrect", mockLink.getToUID(), created.getToUID());
    assertEquals("CanDelete is incorrect", mockLink.canDelete(), created.canDelete());
    assertEquals("Priority is incorrect", mockLink.getPriority(), created.getPriority());
  }

  // ************** Finders ******************************************



  // ******************  utility methods ****************************
  private IResourceLinkManagerObj getResourceLinkMgr() throws Exception
  {
    IResourceLinkManagerHome enterpriseHome = (IResourceLinkManagerHome)ServiceLocator.instance(
                                         ServiceLocator.CLIENT_CONTEXT).getHome(
                                         IResourceLinkManagerHome.class.getName(),
                                         IResourceLinkManagerHome.class);
    return enterpriseHome.create();
  }


  private void cleanUp() throws Exception
  {
    undoCannotDeletes();
    deleteTestData();
  }

  private void undoCannotDeletes() throws Exception
  {
    ArrayList resourceTypes = new ArrayList();
    resourceTypes.add("Type1");
    resourceTypes.add("Type2");
    resourceTypes.add("Type3");
    resourceTypes.add("Type4");
    resourceTypes.add("Type5");

    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, ResourceLink.FROM_TYPE,
      resourceTypes, false);
    filter.addSingleFilter(filter.getAndConnector(), ResourceLink.CAN_DELETE,
      filter.getEqualOperator(), Boolean.FALSE, false);

    Collection cannotDeletes = getResourceLinksByFilter(filter, false);
    for (Iterator i=cannotDeletes.iterator(); i.hasNext(); )
    {
      ResourceLink link = (ResourceLink)i.next();
      link.setCanDelete(true);
      _resourceLinkMgr.updateResourceLink(link);
    }
  }

  private void deleteTestData() throws Exception
  {
    ArrayList resourceTypes = new ArrayList();
    resourceTypes.add("Type1");
    resourceTypes.add("Type2");
    resourceTypes.add("Type3");
    resourceTypes.add("Type4");
    resourceTypes.add("Type5");

    DataFilterImpl filter = new DataFilterImpl();
    filter.addDomainFilter(null, ResourceLink.FROM_TYPE,
      resourceTypes, false);

    Collection cannotDeletes = getResourceLinksByFilter(filter, false);
    for (Iterator i=cannotDeletes.iterator(); i.hasNext(); )
    {
      ResourceLink link = (ResourceLink)i.next();
      try
      {
        _resourceLinkMgr.removeResourceLink((Long)link.getKey(), false);
      }
      catch (Exception ex)
      {
      }
    }
  }

  private ResourceLink createResourceLink(
    Object[] linkParams)
  {
    ResourceLink link = new ResourceLink();
    link.setFromType((String)linkParams[0]);
    link.setFromUID((Long)linkParams[1]);
    link.setToType((String)linkParams[2]);
    link.setToUID((Long)linkParams[3]);
    link.setCanDelete(((Boolean)linkParams[4]).booleanValue());

    return link;
  }

  private Long addToDb(ResourceLink resourceLink) throws Throwable
  {
    Long uID = null;

    try
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.addToDb] Enter");

      uID = _resourceLinkMgr.addResourceLink(resourceLink);
    }
    finally
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.addToDb] Exit");
    }

    return uID;
  }

  private void updateToDb(ResourceLink resourceLink) throws Throwable
  {
    try
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.updateToDb] Enter");
      _resourceLinkMgr.updateResourceLink(resourceLink);
    }
    finally
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.updateToDb] Exit");
    }
  }

  private void updateToDb(String fromResourceType, Long fromResourceUID,
    String toResourceType, Collection toResourceUIDs) throws Throwable
  {
    try
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.updateToDb] Enter");
      _resourceLinkMgr.updateToResourceLinks(fromResourceType, fromResourceUID,
        toResourceType, toResourceUIDs);
    }
    finally
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.updateToDb] Exit");
    }
  }

  private ResourceLink getResourceLinkByUID(Long uId, boolean getDescendants)
  {
    ResourceLink resourceLink = null;

    try
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.getResourceLinkByUID] Enter");

      resourceLink = _resourceLinkMgr.getResourceLinkByUID(uId, getDescendants);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ResourceLinkManagerBeanTest.getResourceLinkByUID]" + ex.getMessage());
      assertTrue("Error in _resourceLinkMgr.getResourceLinkByUID", false);
    }
    finally
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.getResourceLinkByUID] Exit");
    }

    return resourceLink;
  }

  private Collection getToResourceLinks(
    String fromResourceType, Long fromResourceUID, String toResourceType)
  {
    Collection resourceLinks = null;

    try
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.getToResourceLinks] Enter");

      resourceLinks = _resourceLinkMgr.getToResourceLinks(
                      fromResourceType, fromResourceUID, toResourceType);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ResourceLinkManagerBeanTest.getToResourceLinks]"+ex.getMessage());
      assertTrue("Error in _resourceLinkMgr.getToResourceLinks", false);
    }
    finally
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.getToResourceLinks] Exit");
    }

    return resourceLinks;
  }

  private Collection getResourceLinksByFilter(IDataFilter filter, boolean getDescendants)
  {
    Collection results = null;
    try
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.getResourceLinksByFilter] Enter");

      results = _resourceLinkMgr.getResourceLinksByFilter(filter, getDescendants);
    }
    catch (Exception ex)
    {
      Log.err("TEST", "[ResourceLinkManagerBeanTest.getResourceLinksByFilter]"+ ex.getMessage());
      assertTrue("Error in _resourceLinkMgr.getResourceLinksByFilter", false);
    }
    finally
    {
      Log.log("TEST", "[ResourceLinkManagerBeanTest.getResourceLinksByFilter] Exit");
    }
    return results;
  }

  // *********************** Runner *****************************

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }


}