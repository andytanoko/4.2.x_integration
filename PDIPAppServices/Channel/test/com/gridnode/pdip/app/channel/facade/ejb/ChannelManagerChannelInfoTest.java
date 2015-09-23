/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelManagerChannelInfoTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul ?? 2002    Goh Kan Mun             Created
 * Jul 11 2002    Goh Kan Mun             Modified - uncomment test updating null value
 *                                        to unique non-null field. (Limitation of DB)
 */
package com.gridnode.pdip.app.channel.facade.ejb;

import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.rmi.RemoteException;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import junit.framework.*;
import junit.framework.AssertionFailedError;

public class ChannelManagerChannelInfoTest extends AbstractChannelManagerTest
{
  // Static value for test data.
  private static final Long INVALID_UID = new Long(-123);

  private static final String HOST_1 = "Host 1";
  private static final String HOST_2 = "Host 2";

  private static final int PORT_1 = 11;
  private static final int PORT_2 = 22;

  private static final String IMPL_VERSION_1 = "000011";
  private static final String IMPL_VERSION_2 = "000022";

  private static final Hashtable PROTOCOL_DETAIL_1 = new Hashtable();
  private static final Hashtable PROTOCOL_DETAIL_2 = new Hashtable();

  private static final String PROTOCOL_VERSION_1 = "1.0.0";
  private static final String PROTOCOL_VERSION_2 = "2.0.0";

  private static final String COMMINFO_NAME_1 = "NEW COMMINFO NAME 1";
  private static final String COMMINFO_NAME_2 = "NEW COMMINFO NAME 2";

  private static final boolean IS_DEFAULT_TPT_1 = Boolean.FALSE.booleanValue();
  private static final boolean IS_DEFAULT_TPT_2 = Boolean.TRUE.booleanValue();

  private static final String COMMINFO_DESCRIPTION_1 = "NEW COMMINFO DESCRIPTION 1";
  private static final String COMMINFO_DESCRIPTION_2 = "NEW COMMINFO DESCRIPTION 2";

  private static final String NAME_1 = "Name 1";
  private static final String NAME_2 = "Name 2";
  private static final String NAME_3 = "Name 3";
  private static final String NAME_4 = "Name 4";
  private static final String NAME_5 = "Name 5";
  private static final String NAME_6 = "Name 6";
  private static final String NAME_7 = "Name 7";
  private static final String NAME_8 = "Name 8";
  private static final String NAME_9 = "Name 9";
  private static final String NAME_10 = "Name 10";

  private static final String DESC_1 = "Description 1";
  private static final String DESC_2 = "Description 2";
  private static final String DESC_3 = "Description 3";
  private static final String DESC_4 = "Description 4";
  private static final String DESC_5 = "Description 5";
  private static final String DESC_6 = "Description 6";
  private static final String DESC_7 = "Description 7";
  private static final String DESC_8 = "Description 8";
  private static final String DESC_9 = "Description 9";

  private static final String REF_ID_1 = "Reference Id 1";
  private static final String REF_ID_2 = "Reference Id 2";
  private static final String REF_ID_3 = "Reference Id 3";
  private static final String REF_ID_4 = "Reference Id 4";
  private static final String REF_ID_5 = "Reference Id 5";
  private static final String REF_ID_6 = "Reference Id 6";
  private static final String REF_ID_7 = "Reference Id 7";
  private static final String REF_ID_8 = "Reference Id 8";
  private static final String REF_ID_9 = "Reference Id 9";

  private static final String TYPE_1 = "Type 1";
  private static final String TYPE_2 = "Type 2";
  private static final String TYPE_3 = "Type 3";
  private static final String TYPE_4 = "Type 4";
  private static final String TYPE_5 = "Type 5";
  private static final String TYPE_6 = "Type 6";
  private static final String TYPE_7 = "Type 7";
  private static final String TYPE_8 = "Type 8";
  private static final String TYPE_9 = "Type 9";

  private static CommInfo COMM_INFO_1 = null;
  private static CommInfo COMM_INFO_2 = null;

  public ChannelManagerChannelInfoTest(String name)
  {
    super(name, "ChannelManagerChannelInfoTest");
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite()
  {
    return new TestSuite(ChannelManagerChannelInfoTest.class);
  }

  protected void cleanupData() throws Exception
  {
    deleteTestDataChannelInfo(REF_ID_1, TYPE_1);
    deleteTestDataChannelInfo(REF_ID_2, TYPE_2);
    deleteTestDataChannelInfo(REF_ID_3, TYPE_3);
    deleteTestDataChannelInfo(REF_ID_4, TYPE_4);
    deleteTestDataChannelInfo(REF_ID_5, TYPE_5);
    deleteTestDataChannelInfo(REF_ID_6, TYPE_6);
    deleteTestDataChannelInfo(REF_ID_7, TYPE_7);
    deleteTestDataChannelInfo(REF_ID_8, TYPE_8);
    deleteTestDataChannelInfo(REF_ID_9, TYPE_9);

    deleteTestDataCommInfo(REF_ID_1, TYPE_1);
    deleteTestDataCommInfo(REF_ID_2, TYPE_2);
  }

  protected void setupData()
  {
    COMM_INFO_1 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1, TYPE_1,
                PROTOCOL_DETAIL_1, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
                COMMINFO_NAME_1, COMMINFO_DESCRIPTION_1);
    COMM_INFO_2 = createTestDataCommInfo(HOST_2, IMPL_VERSION_2, PORT_2, TYPE_2,
                PROTOCOL_DETAIL_2, PROTOCOL_VERSION_2, REF_ID_2, IS_DEFAULT_TPT_2,
                COMMINFO_NAME_2, COMMINFO_DESCRIPTION_2);
  }

  //---------------------------------------------------------------------------------------------
  //  Test cases
  //---------------------------------------------------------------------------------------------
  public void testCreateChannelInfo() throws Exception
  {

    //Add new (non-existing CommInfo in DB)
    ChannelInfo info1 = createTestDataChannelInfo(DESC_1, COMM_INFO_1, REF_ID_1, TYPE_1, NAME_1);
    checkCreateChannelInfoFail(info1, "Invalid CommInfo");

    //Add CommInfo to DB.
    Long comm_uId2 = addTestDataCommInfo(COMM_INFO_2);
    COMM_INFO_2 = findTestDataCommInfoByUId(comm_uId2);

    Long comm_uId = addTestDataCommInfo(COMM_INFO_1);
    COMM_INFO_1 = findTestDataCommInfoByUId(comm_uId);

    // Add null CommInfo
    ChannelInfo info5 = createTestDataChannelInfo(DESC_6, null, REF_ID_6, TYPE_6, NAME_7);
    checkCreateChannelInfoFail(info5, "Null Comm Info");

    //Add new (with CommInfo in DB)
    ChannelInfo info5a = createTestDataChannelInfo(DESC_1, COMM_INFO_1, REF_ID_1, TYPE_1, NAME_1);
    checkCreateChannelInfoPass(info5a);

    // Add duplicate CommInfo
    ChannelInfo info5b= createTestDataChannelInfo(DESC_7, COMM_INFO_1, REF_ID_7, TYPE_7, NAME_8);
    checkCreateChannelInfoPass(info5b);

    //Add new (with CommInfo in DB) : Duplicate name
    checkCreateChannelInfoFail(info1, "Duplicate Channel Name");

    //Add existing (CommInfo in DB): null name
    ChannelInfo info2 = createTestDataChannelInfo(DESC_1, COMM_INFO_1, REF_ID_1, TYPE_1, null);
    checkCreateChannelInfoFail(info2, "Null name");

    //Add existing (CommInfo in DB): Different name
    ChannelInfo info2a = createTestDataChannelInfo(DESC_1, COMM_INFO_1, REF_ID_1, TYPE_1, NAME_2);
    checkCreateChannelInfoPass(info2a);

    // Add null reference Id
    ChannelInfo info3 = createTestDataChannelInfo(DESC_2, COMM_INFO_1, null, TYPE_2, NAME_3);
    checkCreateChannelInfoPass(info3);

    // Add duplicate reference Id
    ChannelInfo info3a = createTestDataChannelInfo(DESC_3, COMM_INFO_1, REF_ID_1, TYPE_3, NAME_4);
    checkCreateChannelInfoPass(info3a);

    // Add null type
    ChannelInfo info4 = createTestDataChannelInfo(DESC_4, COMM_INFO_1, REF_ID_4, null, NAME_5);
    checkCreateChannelInfoFail(info4, "Null type");

    // Add duplicate type
    ChannelInfo info4a = createTestDataChannelInfo(DESC_5, COMM_INFO_1, REF_ID_5, TYPE_1, NAME_6);
    checkCreateChannelInfoPass(info4a);

    // Add null Description
    ChannelInfo info6 = createTestDataChannelInfo(null, COMM_INFO_1, REF_ID_8, TYPE_8, NAME_9);
    checkCreateChannelInfoPass(info6);

    // Add duplicate Description
    ChannelInfo info6a = createTestDataChannelInfo(DESC_1, COMM_INFO_1, REF_ID_9, TYPE_9, NAME_10);
    checkCreateChannelInfoPass(info6a);

  }

  private void checkCreateChannelInfoPass(ChannelInfo info)
          throws Exception
  {
    Long uId = remote.createChannelInfo(info);
    ChannelInfo retrieved = findTestDataChannelInfoByUId(uId);
    checkIdenticalChannelInfo(info, retrieved, false);
  }

  private void checkCreateChannelInfoFail(ChannelInfo info, String errorMessage)
          throws SystemException, RemoteException
  {
    try
    {
      remote.createChannelInfo(info);
      assertTrue(errorMessage, false);
    }
    catch (CreateEntityException e)
    {
      ChannelLogger.debugLog(CLASSNAME, "checkCreateChannelInfoFail",
                                    "Expected CreateEntityException :" + e.getMessage());
    }
    catch (SystemException e)
    {
      ChannelLogger.debugLog(CLASSNAME, "checkDeleteChannelInfoFail",
                                    "Expected SystemException :" + e.getMessage());
      return;
    }
  }



  public void testUpdateChannelInfo() throws Exception
  {
    //Add CommInfo first.
    Long comm_uId1 = addTestDataCommInfo(COMM_INFO_1);
    COMM_INFO_1 = findTestDataCommInfoByUId(comm_uId1);
    Long comm_uId2 = addTestDataCommInfo(COMM_INFO_2);
    COMM_INFO_2 = findTestDataCommInfoByUId(comm_uId2);

    ChannelInfo info1 = createTestDataChannelInfo(DESC_1, COMM_INFO_1, REF_ID_1, TYPE_1, NAME_1);
    ChannelInfo info2 = createTestDataChannelInfo(DESC_2, COMM_INFO_2, REF_ID_2, TYPE_2, NAME_2);

    // Update to unexisting ChannelInfo
    checkUpdateChannelInfoFail(info1, "Updating non-existing CahnnelInfo");

    //Add new
    Long uId1 = addTestDataChannelInfo(info1);
    Long uId2 = addTestDataChannelInfo(info2);

    // Update exsiting (No change)
    ChannelInfo retrieved1 = findTestDataChannelInfoByUId(uId1);
    checkUpdateChannelInfoPass(retrieved1, uId1);

    // Update exsiting (Wrong version)
    checkUpdateChannelInfoFail(retrieved1, "Wrong version");

    // Updating existing with null CommInfo.
    ChannelInfo retrieved2 = findTestDataChannelInfoByUId(uId2);
    retrieved2.setTptCommInfo(null);
    checkUpdateChannelInfoFail(retrieved2, "Null Comm Info");

    // Updating existing with invalid CommInfo.
    retrieved2 = findTestDataChannelInfoByUId(uId2);
    COMM_INFO_2.setUId(INVALID_UID.longValue());
    retrieved2.setTptCommInfo(COMM_INFO_2);
    checkUpdateChannelInfoFail(retrieved2, "Invalid Comm Info");

    // Updating existing with duplicate CommInfo.
    retrieved2 = findTestDataChannelInfoByUId(uId2);
    retrieved2.setTptCommInfo(COMM_INFO_1);
    checkUpdateChannelInfoPass(retrieved2, uId2);

    // Updating existing with null Description.
    retrieved2 = findTestDataChannelInfoByUId(uId2);
    retrieved2.setDescription(null);
    checkUpdateChannelInfoPass(retrieved2, uId2);

    // Updating existing with duplicate Description.
    retrieved2 = findTestDataChannelInfoByUId(uId2);
    retrieved2.setDescription(DESC_1);
    checkUpdateChannelInfoPass(retrieved2, uId2);

    // Limitation of the db. Use ChannelManager to check for null.
    // Updating existing with null Name.
    retrieved2 = findTestDataChannelInfoByUId(uId2);
    retrieved2.setName(null);
    checkUpdateChannelInfoFail(retrieved2, "Name is null");

    // Updating existing with duplicate Name.
    retrieved2 = findTestDataChannelInfoByUId(uId2);
    retrieved2.setName(NAME_1);
    checkUpdateChannelInfoFail(retrieved2, "Duplicate name");

    // Updating existing with new Name.
    retrieved2 = findTestDataChannelInfoByUId(uId2);
    retrieved2.setName(NAME_3);
    checkUpdateChannelInfoPass(retrieved2, uId2);

    // Updating existing with null Reference Id.
    retrieved2 = findTestDataChannelInfoByUId(uId2);
    retrieved2.setReferenceId(null);
    checkUpdateChannelInfoPass(retrieved2, uId2);

    // Updating existing with duplicate Reference Id.
    retrieved2 = findTestDataChannelInfoByUId(uId2);
    retrieved2.setReferenceId(REF_ID_1);
    checkUpdateChannelInfoPass(retrieved2, uId2);

    // Limitation of the db. Use ChannelManager to check for null.
    // Updating existing with null Protocol Type.
    retrieved2 = findTestDataChannelInfoByUId(uId2);
    retrieved2.setTptProtocolType(null);
    checkUpdateChannelInfoFail(retrieved2, "Protocol Type is null");

    // Updating existing with duplicate Protocol Type.
    retrieved2 = findTestDataChannelInfoByUId(uId2);
    retrieved2.setTptProtocolType(TYPE_1);
    checkUpdateChannelInfoPass(retrieved2, uId2);

  }

  private void checkUpdateChannelInfoPass(ChannelInfo info, Long uId) throws Exception
  {
    remote.updateChannelInfo(info);
    ChannelInfo retrieved = findTestDataChannelInfoByUId(uId);
    checkIdenticalChannelInfo(info, retrieved, false);
  }

  private void checkUpdateChannelInfoFail(ChannelInfo info, String errorMessage)
          throws RemoteException
  {
    try
    {
      remote.updateChannelInfo(info);
      assertTrue(errorMessage, false);
    }
    catch (UpdateEntityException e)
    {
      ChannelLogger.debugLog(CLASSNAME, "checkUpdateChannelInfoFail",
                                    "Expected UpdateEntityException :" + e.getMessage());
    }
    catch (SystemException e)
    {
      ChannelLogger.debugLog(CLASSNAME, "checkDeleteChannelInfoFail",
                                    "Expected SystemException :" + e.getMessage());
      return;
    }
    catch (Exception e)
    {
      ChannelLogger.debugLog(CLASSNAME, "checkDeleteChannelInfoFail",
                                    "Unrecognized Exception :", e);
      return;
    }
  }



  public void testDeleteChannelInfo() throws Exception
  {
    //Add CommInfo first.
    Long comm_uId1 = addTestDataCommInfo(COMM_INFO_1);
    COMM_INFO_1 = findTestDataCommInfoByUId(comm_uId1);
    Long comm_uId2 = addTestDataCommInfo(COMM_INFO_2);
    COMM_INFO_2 = findTestDataCommInfoByUId(comm_uId2);

    ChannelInfo info1 = createTestDataChannelInfo(DESC_1, COMM_INFO_1, REF_ID_1, TYPE_1, NAME_1);
    ChannelInfo info2 = createTestDataChannelInfo(DESC_2, COMM_INFO_2, REF_ID_2, TYPE_2, NAME_2);
    Long uId1 = addTestDataChannelInfo(info1);
    Long uId2 = addTestDataChannelInfo(info2);

    // Delete existing
    checkDeleteChannelInfoPass(uId1);

    // Delete non-existing
    checkDeleteChannelInfoFail(uId1, "Non-exisitng");

    // Delete exisiting 2
    checkDeleteChannelInfoPass(uId2);
  }

  private void checkDeleteChannelInfoPass(Long channelInfoUId)
          throws Exception
  {
    remote.deleteChannelInfo(channelInfoUId);
    try
    {
      ChannelInfo retrieve = findTestDataChannelInfoByUId(channelInfoUId);
      assertNull("Delete not successful.", retrieve);
    }
    catch (FindEntityException e)
    {
      ChannelLogger.debugLog(CLASSNAME, "checkDeleteChannelInfoPass",
                                    "Expected FindEntityException :" + e.getMessage());
    }
    catch (SystemException e)
    {
      ChannelLogger.debugLog(CLASSNAME, "checkDeleteChannelInfoFail",
                                    "Expected SystemException :" + e.getMessage());
      return;
    }
  }

  private void checkDeleteChannelInfoFail(Long channelInfoUId, String errorMessage)
          throws RemoteException
  {
    try
    {
      remote.deleteChannelInfo(channelInfoUId);
    }
    catch (DeleteEntityException e)
    {
      ChannelLogger.debugLog(CLASSNAME, "checkDeleteChannelInfoFail",
                                    "Expected DeleteEntityException :" + e.getMessage());
      return;
    }
    catch (SystemException e)
    {
      ChannelLogger.debugLog(CLASSNAME, "checkDeleteChannelInfoFail",
                                    "Expected SystemException :" + e.getMessage());
      return;
    }
    assertTrue(errorMessage, false);
  }



  public void testGetChannelInfoByFilter() throws Exception
  {
    //Add CommInfo first.
    Long comm_uId1 = addTestDataCommInfo(COMM_INFO_1);
    COMM_INFO_1 = findTestDataCommInfoByUId(comm_uId1);
    Long comm_uId2 = addTestDataCommInfo(COMM_INFO_2);
    COMM_INFO_2 = findTestDataCommInfoByUId(comm_uId2);

    ChannelInfo info1 = createTestDataChannelInfo(DESC_1, COMM_INFO_1, REF_ID_1, TYPE_1, NAME_1);
    ChannelInfo info2 = createTestDataChannelInfo(DESC_1, COMM_INFO_1, REF_ID_1, TYPE_2, NAME_2);
    ChannelInfo info3 = createTestDataChannelInfo(DESC_1, COMM_INFO_2, REF_ID_2, TYPE_2, NAME_3);
    Long uId1 = addTestDataChannelInfo(info1);
    Long uId2 = addTestDataChannelInfo(info2);
    Long uId3 = addTestDataChannelInfo(info3);
    DataFilterImpl filter = null;

    ChannelInfo[] expected = null;

    // Retrieve 2 info (REF_ID_1)
    expected = new ChannelInfo[2];
    expected[0] = info1;
    expected[1] = info2;
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(),
                                 REF_ID_1, false);
    checkGetChannelInfoByFilterPass(filter, expected);

    // Retrieve 1 info (REF_ID_2)
    expected = new ChannelInfo[1];
    expected[0] = info3;
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(),
                                 REF_ID_2, false);
    checkGetChannelInfoByFilterPass(filter, expected);

    // Retrieve 0 info (REF_ID_3)
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(),
                                 REF_ID_3, false);
    checkGetChannelInfoByFilterPass(filter, null);

    // Retrieve 0 info (REF_ID_3)
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(),
                                 REF_ID_3, false);
    checkGetChannelInfoByFilterFail(filter, expected);

    // Retrieve 1 info (TYPE_2, REF_ID_1)
    expected = new ChannelInfo[1];
    expected[0] = info2;
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(),
                                 REF_ID_1, false);
    filter.addSingleFilter(filter.getAndConnector(), ChannelInfo.TPT_PROTOCOL_TYPE, filter.getEqualOperator(),
                                 TYPE_2, false);
    checkGetChannelInfoByFilterPass(filter, expected);

    // Retrieve 1 info (TYPE_1, REF_ID_1)
    expected = new ChannelInfo[1];
    expected[0] = info1;
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(),
                                 REF_ID_1, false);
    filter.addSingleFilter(filter.getAndConnector(), ChannelInfo.TPT_PROTOCOL_TYPE, filter.getEqualOperator(),
                                 TYPE_1, false);
    checkGetChannelInfoByFilterPass(filter, expected);

    // Retrieve 0 info (TYPE_2, REF_ID_3)
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(),
                                 REF_ID_3, false);
    filter.addSingleFilter(filter.getAndConnector(), ChannelInfo.TPT_PROTOCOL_TYPE, filter.getEqualOperator(),
                                 TYPE_2, false);
    checkGetChannelInfoByFilterPass(filter, null);

    // Retrieve 0 info (TYPE_2, REF_ID_3)
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(),
                                 REF_ID_3, false);
    filter.addSingleFilter(filter.getAndConnector(), ChannelInfo.TPT_PROTOCOL_TYPE, filter.getEqualOperator(),
                                 TYPE_2, false);
    checkGetChannelInfoByFilterFail(filter, expected);

  }

  private void checkGetChannelInfoByFilterPass(DataFilterImpl filter, ChannelInfo[] channelList)
          throws Exception
  {
    Collection c = remote.getChannelInfo(filter);
    checkGetResultPass(c, channelList);
  }

  private void checkGetChannelInfoByFilterFail(DataFilterImpl filter, ChannelInfo[] channelList)
          throws RemoteException, Exception
  {
    try
    {
      Collection c = remote.getChannelInfo(filter);
      checkGetResultFail(c, channelList);
    }
    catch (FindEntityException e)
    {
      ChannelLogger.infoLog(CLASSNAME, "checkGetChannelInfoByRefIdFail",
                                   "Expected FindEntityException :" + e.getMessage());
    }
    catch (SystemException e)
    {
      ChannelLogger.infoLog(CLASSNAME, "checkGetChannelInfoByRefIdFail",
                                   "Expected SystemException :" + e.getMessage());
    }
  }



  public void testGetChannelInfoByUId() throws Exception
  {
    //Add CommInfo first.
    Long comm_uId1 = addTestDataCommInfo(COMM_INFO_1);
    COMM_INFO_1 = findTestDataCommInfoByUId(comm_uId1);
    Long comm_uId2 = addTestDataCommInfo(COMM_INFO_2);
    COMM_INFO_2 = findTestDataCommInfoByUId(comm_uId2);

    ChannelInfo info1 = createTestDataChannelInfo(DESC_1, COMM_INFO_1, REF_ID_1, TYPE_1, NAME_1);
    ChannelInfo info2 = createTestDataChannelInfo(DESC_1, COMM_INFO_1, REF_ID_1, TYPE_2, NAME_2);
    Long uId1 = addTestDataChannelInfo(info1);
    Long uId2 = addTestDataChannelInfo(info2);

    ChannelInfo[] expected = null;

    // Retrieve 1 info (uid1)
    checkGetChannelInfoByUIdPass(uId1, info1);

    // Retrieve 1 info (uid2)
    checkGetChannelInfoByUIdPass(uId2, info2);

    // Retrieve 0 info (-123)
    checkGetChannelInfoByUIdFail(INVALID_UID, null);

    // Retrieve 0 info (-123)
    checkGetChannelInfoByUIdFail(INVALID_UID, info1);

    // Retrieve 0 info (-123)
    checkGetChannelInfoByUIdFail(INVALID_UID, info2);

  }

  private void checkGetChannelInfoByUIdPass(Long uId, ChannelInfo channel) throws Exception
  {
    ChannelInfo info1 = remote.getChannelInfo(uId);
    checkIdenticalChannelInfo(info1, channel, false);
  }

  private void checkGetChannelInfoByUIdFail(Long uId, ChannelInfo channel)
          throws RemoteException
  {
    try
    {
      ChannelInfo info1 = remote.getChannelInfo(uId);
      checkIdenticalChannelInfo(info1, channel, false);
    }
    catch (AssertionFailedError e)
    {
      ChannelLogger.infoLog(CLASSNAME, "checkGetChannelInfoByUIdFail", "Exception encountered :" + e);
      return;
    }
    catch (FindEntityException e)
    {
      ChannelLogger.infoLog(CLASSNAME, "checkGetChannelInfoByRefIdFail",
                                   "Expected FindEntityException :" + e.getMessage());
      return;
    }
    catch (SystemException e)
    {
      ChannelLogger.infoLog(CLASSNAME, "checkGetChannelInfoByRefIdFail",
                                   "Expected SystemException :" + e.getMessage());
      return;
    }
    assertTrue("Channel is the same", false);
  }

}