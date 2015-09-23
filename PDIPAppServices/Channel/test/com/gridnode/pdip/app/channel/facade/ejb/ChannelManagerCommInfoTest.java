/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelManagerCommInfoTest.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
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
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import junit.framework.*;
import junit.framework.AssertionFailedError;

public class ChannelManagerCommInfoTest extends AbstractChannelManagerTest
{
  // Static value for test data.
  private static final String HOST_1 = "Host 1";
  private static final int PORT_1 = 11;
  private static final String IMPL_VERSION_1 = "000011";
  private static final Hashtable PROTOCOL_DETAIL_1 = new Hashtable();
  private static final String PROTOCOL_VERSION_1 = "1.0.0";
  private static final String REF_ID_1 = "Reference Id 1";
  private static final String TYPE_1 = "Type 1";
  private static final boolean IS_DEFAULT_TPT_1 = Boolean.FALSE.booleanValue();
  private static final String COMMINFO_NAME_1 = "NEW COMMINFO NAME 1";
  private static final String COMMINFO_DESCRIPTION_1 = "NEW COMMINFO DESCRIPTION 1";

  private static final String HOST_2 = "Host 2";
  private static final int PORT_2 = 22;
  private static final String IMPL_VERSION_2 = "000022";
  private static final Hashtable PROTOCOL_DETAIL_2 = new Hashtable();
  private static final String PROTOCOL_VERSION_2 = "2.0.0";
  private static final String REF_ID_2 = "Reference Id 2";
  private static final String TYPE_2 = "Type 2";
  private static final boolean IS_DEFAULT_TPT_2 = Boolean.TRUE.booleanValue();
  private static final String COMMINFO_NAME_2 = "NEW COMMINFO NAME 2";
  private static final String COMMINFO_DESCRIPTION_2 = "NEW COMMINFO DESCRIPTION 2";

  private static final String REF_ID_3 = "Reference Id 3";
  private static final String TYPE_3 = "Type 3";
  private static final Long INVALID_UID = new Long(-123);
  private static final String DESC_1 = "DESC 1";
  private static final String DESC_2 = "DESC 2";
  private static final String NAME_1 = "NAME 1";
  private static final String NAME_2 = "NAME 2";

  private static final String COMMINFO_NAME_3 = "COMMINFO_NAME 3";
  private static final String COMMINFO_NAME_4 = "COMMINFO_NAME 4";
  private static final String COMMINFO_NAME_5 = "COMMINFO_NAME 5";
  private static final String COMMINFO_NAME_6 = "COMMINFO_NAME 6";
  private static final String COMMINFO_NAME_7 = "COMMINFO_NAME 7";
  private static final String COMMINFO_NAME_8 = "COMMINFO_NAME 8";
  private static final String COMMINFO_NAME_9 = "COMMINFO_NAME 9";
  private static final String COMMINFO_NAME_10 = "COMMINFO_NAME 10";
  private static final String COMMINFO_NAME_11 = "COMMINFO_NAME 11";

  public ChannelManagerCommInfoTest(String name)
  {
    super(name, "ChannelManagerCommInfoTest");
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite()
  {
    return new TestSuite(ChannelManagerCommInfoTest.class);
  }

  protected void setupData()
  {
    PROTOCOL_DETAIL_1.put("JNDI", "TEST1");
    PROTOCOL_DETAIL_1.put("Topic Name", "TEST1 TopicName");
    PROTOCOL_DETAIL_1.put("Password", "TEST1 Password");
    PROTOCOL_DETAIL_1.put("User", "Test1 user");

    PROTOCOL_DETAIL_2.put("JNDI", "TEST2");
    PROTOCOL_DETAIL_2.put("Topic Name", "TEST2 TopicName");
    PROTOCOL_DETAIL_2.put("Password", "TEST2 Password");
    PROTOCOL_DETAIL_2.put("User", "TEST2 User");
  }

  protected void cleanupData() throws Exception
  {
    deleteTestDataChannelInfo(REF_ID_1, TYPE_1);
    deleteTestDataChannelInfo(REF_ID_2, TYPE_1);
    deleteTestDataChannelInfo(REF_ID_1, TYPE_2);
    deleteTestDataChannelInfo(REF_ID_2, TYPE_2);
    deleteTestDataCommInfo(REF_ID_1, TYPE_1);
    deleteTestDataCommInfo(REF_ID_2, TYPE_1);
    deleteTestDataCommInfo(REF_ID_1, TYPE_2);
    deleteTestDataCommInfo(REF_ID_2, TYPE_2);
    PROTOCOL_DETAIL_1.clear();
    PROTOCOL_DETAIL_2.clear();
  }

  //---------------------------------------------------------------------------------------------
  //  Test cases
  //---------------------------------------------------------------------------------------------
  public void testCreateCommInfo() throws Exception
  {

    //Add new
    CommInfo info1 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1,
             TYPE_1, PROTOCOL_DETAIL_1, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_1, COMMINFO_DESCRIPTION_1);

    checkCreateCommInfoPass(info1);

    // Add valid comminfo : host = null
    CommInfo info2 = createTestDataCommInfo(null, IMPL_VERSION_1, PORT_1,
             TYPE_1, PROTOCOL_DETAIL_1, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_2, COMMINFO_DESCRIPTION_1);
    checkCreateCommInfoPass(info2);

    // Add invalid comminfo : Implemention Version = null
    CommInfo info3 = createTestDataCommInfo(HOST_1, null, PORT_1,
             TYPE_1, PROTOCOL_DETAIL_1, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_3, COMMINFO_DESCRIPTION_1);
    checkCreateCommInfoFail(info3, "NULL Implementation Version");

    // Add invalid comminfo : Type = null
    CommInfo info4 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1,
             null, PROTOCOL_DETAIL_1, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_4, COMMINFO_DESCRIPTION_1);
    checkCreateCommInfoFail(info4, "NULL Protocol Type");

    // Add invalid comminfo : Protocol Detail = null
    CommInfo info5 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1,
             TYPE_1, null, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_5, COMMINFO_DESCRIPTION_1);
    checkCreateCommInfoFail(info5, "Null ProtocolDetail");

    // Add valid comminfo : Protocol Version = null
    CommInfo info6 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1,
             TYPE_1, PROTOCOL_DETAIL_1, null, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_6, COMMINFO_DESCRIPTION_1);
    checkCreateCommInfoPass(info6);

    // Add invalid comminfo : Reference Id = null
    CommInfo info7 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1,
             TYPE_1, PROTOCOL_DETAIL_1, PROTOCOL_VERSION_1, null, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_7, COMMINFO_DESCRIPTION_1);
    checkCreateCommInfoPass(info7);

    // Add invalid comminfo : Name = null
    CommInfo info8 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1,
             TYPE_1, PROTOCOL_DETAIL_1, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
             null, COMMINFO_DESCRIPTION_1);
    checkCreateCommInfoFail(info8, "NULL Name");

    // Add valid comminfo : Description = null
    CommInfo info9 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1,
             TYPE_1, PROTOCOL_DETAIL_1, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_8, null);
    checkCreateCommInfoPass(info9);

    // Add different CommInfo
    CommInfo info10 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1,
             TYPE_1, PROTOCOL_DETAIL_2, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_9, COMMINFO_DESCRIPTION_2);
    checkCreateCommInfoPass(info10);

    //Add new
    CommInfo info11= createTestDataCommInfo(HOST_1, IMPL_VERSION_2, PORT_2,
             TYPE_2, PROTOCOL_DETAIL_2, PROTOCOL_VERSION_2, REF_ID_2, IS_DEFAULT_TPT_2,
             COMMINFO_NAME_10, COMMINFO_DESCRIPTION_2);
    checkCreateCommInfoPass(info11);

    // Add existing HOSTNAME
    CommInfo info12= createTestDataCommInfo(HOST_1, IMPL_VERSION_2, PORT_2,
             TYPE_2, PROTOCOL_DETAIL_2, PROTOCOL_VERSION_2, REF_ID_2, IS_DEFAULT_TPT_2,
             COMMINFO_NAME_10, COMMINFO_DESCRIPTION_2);
    checkCreateCommInfoFail(info12, "Existing Host name");

    // Add non-existing HOSTNAME
    CommInfo info13= createTestDataCommInfo(HOST_1, IMPL_VERSION_2, PORT_2,
             TYPE_2, PROTOCOL_DETAIL_2, PROTOCOL_VERSION_2, REF_ID_2, IS_DEFAULT_TPT_2,
             COMMINFO_NAME_11, COMMINFO_DESCRIPTION_2);
    checkCreateCommInfoPass(info13);

  }

  private void checkCreateCommInfoPass(CommInfo info) throws Exception
  {
    Long uId = remote.createCommInfo(info);
    CommInfo retrieved = findTestDataCommInfoByUId(uId);
    checkIdenticalCommInfo(info, retrieved, false);
  }

  private void checkCreateCommInfoFail(CommInfo info, String errorMessage)
  {
    try
    {
      remote.createCommInfo(info);
      assertTrue(errorMessage, false);
    }
    catch (Exception e)
    {
      ChannelLogger.debugLog(CLASSNAME, "checkCreateCommInfoFail", "Expected exception :", e);
    }
  }



  public void testUpdateCommInfo() throws Exception
  {
    CommInfo info1 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1,
             TYPE_1, PROTOCOL_DETAIL_1, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_1, COMMINFO_DESCRIPTION_1);
    CommInfo info2 = createTestDataCommInfo(HOST_2, IMPL_VERSION_2, PORT_2,
             TYPE_2, PROTOCOL_DETAIL_2, PROTOCOL_VERSION_2, REF_ID_2, IS_DEFAULT_TPT_2,
             COMMINFO_NAME_2, COMMINFO_DESCRIPTION_2);

    // Update to unexisting CommInfo
    checkUpdateCommInfoFail(info1, "Updating non-existing CommInfo");

    //Add new
    Long uId1 = addTestDataCommInfo(info1);
    Long uId2 = addTestDataCommInfo(info2);

    // Update exsiting
    CommInfo retrieved1 = findTestDataCommInfoByUId(uId1);
    checkUpdateCommInfoPass(retrieved1, uId1);

    // Update exsiting but wrong version
    checkUpdateCommInfoFail(retrieved1, "Wrong version");

    // Updating existing (Change in description).
    CommInfo retrieved2 = findTestDataCommInfoByUId(uId2);
    retrieved2.setDescription(null);
    checkUpdateCommInfoPass(retrieved2, uId2);

    // Updating existing (Change in description - existing).
    CommInfo retrieved2a = findTestDataCommInfoByUId(uId2);
    retrieved2a.setDescription(COMMINFO_DESCRIPTION_1);
    checkUpdateCommInfoPass(retrieved2a, uId2);

    // Updating existing (Change in host - null).
    //CommInfo retrieved3 = findTestDataCommInfoByUId(uId2);
    //retrieved3.setHost(null);
    //checkUpdateCommInfoPass(retrieved3, uId2);

    // Updating existing (Change in Host - existing).
    //CommInfo retrieved3a = findTestDataCommInfoByUId(uId2);
    //retrieved3a.setHost(HOST_1);
    //checkUpdateCommInfoPass(retrieved3a, uId2);

    // Updating existing (Change in isDefaultTpt).
    CommInfo retrieved4 = findTestDataCommInfoByUId(uId2);
    retrieved4.setIsDefaultTpt(IS_DEFAULT_TPT_1);
    checkUpdateCommInfoPass(retrieved4, uId2);

    // Limitation of DB. Using hardcoded check in the ChannelManager.
    // Updating existing (Change in name - null).
    CommInfo retrieved5 = findTestDataCommInfoByUId(uId2);
    retrieved5.setName(null);
    checkUpdateCommInfoFail(retrieved5, "Name is null");

    // Updating existing (Change in name - non-unique).
    CommInfo retrieved5a = findTestDataCommInfoByUId(uId2);
    retrieved5a.setName(COMMINFO_NAME_1);
    checkUpdateCommInfoFail(retrieved5a, "Existing name in database");

    // Updating existing (Change in name - unique).
    CommInfo retrieved5b = findTestDataCommInfoByUId(uId2);
    retrieved5b.setName(COMMINFO_NAME_3);
    checkUpdateCommInfoPass(retrieved5b, uId2);

    // Updating existing (Change in port - 0).
    //CommInfo retrieved6 = findTestDataCommInfoByUId(uId2);
    //retrieved6.setPort(0);
    //checkUpdateCommInfoPass(retrieved6, uId2);

    // Updating existing (Change in port - existing).
    //CommInfo retrieved6a = findTestDataCommInfoByUId(uId2);
    //retrieved6a.setPort(PORT_1);
    //checkUpdateCommInfoPass(retrieved6a, uId2);

    // Updating existing (Change in ProtocolDetail - null).
    //CommInfo retrieved7 = findTestDataCommInfoByUId(uId2);
    //retrieved7.setProtocolDetail(null);
    //checkUpdateCommInfoFail(retrieved7, "Null ProtocolDetail");

    // Updating existing (Change in ProtocolDetail - existing).
    //CommInfo retrieved7a = findTestDataCommInfoByUId(uId2);
    //retrieved7a.setProtocolDetail(PROTOCOL_DETAIL_1);
    //checkUpdateCommInfoPass(retrieved7a, uId2);

    // Limitation of DB. Check for null in the ChannelManager.
    // Updating existing (Change in protocol type - null).
    CommInfo retrieved8 = findTestDataCommInfoByUId(uId2);
    retrieved8.setProtocolType(null);
    checkUpdateCommInfoFail(retrieved8, "Null protocol type");

    // Updating existing (Change in protocol type - existing).
    CommInfo retrieved8a = findTestDataCommInfoByUId(uId2);
    retrieved8a.setProtocolType(TYPE_1);
    checkUpdateCommInfoPass(retrieved8a, uId2);

    // Updating existing (Change in protocol version - null).
    //CommInfo retrieved9 = findTestDataCommInfoByUId(uId2);
    //retrieved9.setProtocolVersion(null);
    //checkUpdateCommInfoPass(retrieved9, uId2);

    // Updating existing (Change in protocol version - existing).
    //CommInfo retrieved9a = findTestDataCommInfoByUId(uId2);
    //retrieved9a.setProtocolVersion(PROTOCOL_VERSION_1);
    //checkUpdateCommInfoPass(retrieved9a, uId2);

    // Updating existing (Change in refId - null).
    CommInfo retrieved10 = findTestDataCommInfoByUId(uId2);
    retrieved10.setRefId(null);
    checkUpdateCommInfoPass(retrieved10, uId2);

    // Updating existing (Change in refId - existing).
    CommInfo retrieved10a = findTestDataCommInfoByUId(uId2);
    retrieved10a.setRefId(REF_ID_1);
    checkUpdateCommInfoPass(retrieved10a, uId2);

    // Limitation of the db. Check at the ChannelManager.
    // Updating existing (Change in Tpt Impl Version - null).
    CommInfo retrieved11 = findTestDataCommInfoByUId(uId2);
    retrieved11.setTptImplVersion(null);
    checkUpdateCommInfoFail(retrieved11, "Impl version is null");

    // Updating existing (Change in Tpt Impl Version - existing).
    CommInfo retrieved11a = findTestDataCommInfoByUId(uId2);
    retrieved11a.setTptImplVersion(IMPL_VERSION_1);
    checkUpdateCommInfoPass(retrieved11a, uId2);

  }

  private void checkUpdateCommInfoPass(CommInfo info, Long uId) throws Exception
  {
    remote.updateCommInfo(info);
    CommInfo retrieved = findTestDataCommInfoByUId(uId);
    checkIdenticalCommInfo(info, retrieved, false);
  }

  private void checkUpdateCommInfoFail(CommInfo info, String errorMessage)
  {
    try
    {
      remote.updateCommInfo(info);
      assertTrue(errorMessage, false);
    }
    catch (Exception ex)
    {
      ChannelLogger.debugLog(CLASSNAME, "checkDeleteChannelInfoFail",
                                    "Unrecognized Exception :", ex);
      return;
    }
  }


  public void testDeleteCommInfo() throws Exception
  {
    CommInfo info1 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1,
             TYPE_1, PROTOCOL_DETAIL_1, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_1, COMMINFO_DESCRIPTION_1);
    CommInfo info2 = createTestDataCommInfo(HOST_2, IMPL_VERSION_2, PORT_2,
             TYPE_2, PROTOCOL_DETAIL_2, PROTOCOL_VERSION_2, REF_ID_2, IS_DEFAULT_TPT_2,
             COMMINFO_NAME_2, COMMINFO_DESCRIPTION_2);
    Long uId1 = addTestDataCommInfo(info1);
    Long uId2 = addTestDataCommInfo(info2);

    CommInfo retrieved = findTestDataCommInfoByUId(uId2);
    ChannelInfo cInfo = createTestDataChannelInfo(DESC_1, retrieved, REF_ID_1, TYPE_1, NAME_1);
    Long uIdc = addTestDataChannelInfo(cInfo);

    // Delete existing
    checkDeleteCommInfoPass(uId1);

    // Delete non-existing
    checkDeleteCommInfoFail(uId1, "Non-exisitng");

    // Delete exisiting 2 (With dependent CommInfo)
    checkDeleteCommInfoFail(uId2, "Dependent Comm Info exist");

    // Delete dependent ChannelInfo
    deleteTestDataChannelInfo(uIdc);
    // Delete exisiting 2
    checkDeleteCommInfoPass(uId2);
  }

  private void checkDeleteCommInfoPass(Long commInfoUId) throws Exception
  {
    remote.deleteCommInfo(commInfoUId);
    try
    {
      CommInfo retrieve = findTestDataCommInfoByUId(commInfoUId);
      assertNull("Delete not successful.", retrieve);
    }
    catch (FindEntityException ex)
    {
      ChannelLogger.infoLog(CLASSNAME, "checkDeleteCommInfoPass", "Exception", ex);
    }
  }

  private void checkDeleteCommInfoFail(Long commInfoUId, String errorMessage) throws Exception
  {
    try
    {
      remote.deleteCommInfo(commInfoUId);
    }
    catch (Exception ex)
    {
      return;
    }
    assertTrue(errorMessage, false);
  }



  public void testGetCommInfoByFilter() throws Exception
  {
    CommInfo info1 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1,
             TYPE_1, PROTOCOL_DETAIL_1, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_1, COMMINFO_DESCRIPTION_1);
    CommInfo info2 = createTestDataCommInfo(HOST_2, IMPL_VERSION_2, PORT_2,
             TYPE_2, PROTOCOL_DETAIL_2, PROTOCOL_VERSION_2, REF_ID_2, IS_DEFAULT_TPT_2,
             COMMINFO_NAME_2, COMMINFO_DESCRIPTION_2);
    CommInfo info3 = createTestDataCommInfo(HOST_2, IMPL_VERSION_2, PORT_2,
             TYPE_2, PROTOCOL_DETAIL_2, PROTOCOL_VERSION_2, REF_ID_2, IS_DEFAULT_TPT_2,
             COMMINFO_NAME_3, COMMINFO_DESCRIPTION_2);
    CommInfo info4 = createTestDataCommInfo(HOST_2, IMPL_VERSION_1, PORT_2,
             TYPE_2, PROTOCOL_DETAIL_2, PROTOCOL_VERSION_2, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_4, COMMINFO_DESCRIPTION_1);
    Long uId1 = addTestDataCommInfo(info1);
    Long uId2 = addTestDataCommInfo(info2);
    Long uId3 = addTestDataCommInfo(info3);
    Long uId4 = addTestDataCommInfo(info4);
    info1 = findTestDataCommInfoByUId(uId1);
    info2 = findTestDataCommInfoByUId(uId2);
    info3 = findTestDataCommInfoByUId(uId3);
    info4 = findTestDataCommInfoByUId(uId4);

    CommInfo[] expected = null;
    DataFilterImpl filter = null;

    // Retrieve 2 info (REF_ID_1)
    expected = new CommInfo[2];
    expected[0] = info1;
    expected[1] = info4;
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, CommInfo.REF_ID, filter.getEqualOperator(), REF_ID_1, false);
    checkGetCommInfoByFilterPass(filter, expected);

    // Retrieve 1 info (TYPE_1)
    expected = new CommInfo[1];
    expected[0] = info1;
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, CommInfo.PROTOCOL_TYPE, filter.getEqualOperator(), TYPE_1, false);
    checkGetCommInfoByFilterPass(filter, expected);

    // Retrieve 0 info (REF_ID_3)
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, CommInfo.REF_ID, filter.getEqualOperator(), REF_ID_3, false);
    checkGetCommInfoByFilterPass(filter, null);

    // Retrieve 0 info (REF_ID_3)
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, CommInfo.REF_ID, filter.getEqualOperator(), REF_ID_3, false);
    checkGetCommInfoByFilterFail(filter, expected);

    // Retrieve 1 info (TYPE_2, REF_ID_1)
    expected = new CommInfo[1];
    expected[0] = info4;
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, CommInfo.REF_ID, filter.getEqualOperator(), REF_ID_1, false);
    filter.addSingleFilter(filter.getAndConnector(), CommInfo.PROTOCOL_TYPE, filter.getEqualOperator(),
                                                     TYPE_2, false);
    checkGetCommInfoByFilterPass(filter, expected);

    // Retrieve 2 info (TYPE_2, REF_ID_2)
    expected = new CommInfo[2];
    expected[0] = info2;
    expected[1] = info3;
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, CommInfo.REF_ID, filter.getEqualOperator(), REF_ID_2, false);
    filter.addSingleFilter(filter.getAndConnector(), CommInfo.PROTOCOL_TYPE, filter.getEqualOperator(),
                                                     TYPE_2, false);
    checkGetCommInfoByFilterPass(filter, expected);

    // Retrieve 0 info (TYPE_1, REF_ID_2)
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, CommInfo.REF_ID, filter.getEqualOperator(), REF_ID_2, false);
    filter.addSingleFilter(filter.getAndConnector(), CommInfo.PROTOCOL_TYPE, filter.getEqualOperator(),
                                                     TYPE_1, false);
    checkGetCommInfoByFilterPass(filter, null);

    // Retrieve 0 info (TYPE_1, REF_ID_2)
    filter = new DataFilterImpl();
    filter.addSingleFilter(null, CommInfo.REF_ID, filter.getEqualOperator(), REF_ID_2, false);
    filter.addSingleFilter(filter.getAndConnector(), CommInfo.PROTOCOL_TYPE, filter.getEqualOperator(),
                                                     TYPE_1, false);
    checkGetCommInfoByFilterFail(filter, expected);
  }

  private void checkGetCommInfoByFilterPass(DataFilterImpl filter, CommInfo[] commInfoList) throws Exception
  {
    Collection c = remote.getCommInfo(filter);
    checkGetResultPass(c, commInfoList);
  }

  private void checkGetCommInfoByFilterFail(DataFilterImpl filter, CommInfo[] commList)
  {
    try
    {
      Collection c = remote.getCommInfo(filter);
      checkGetResultFail(c, commList);
    }
    catch (Exception e)
    {
      ChannelLogger.infoLog(CLASSNAME, "checkGetCommInfoByRefIdFail", "Exception encountered :" + e);
    }
  }



  public void testGetCommInfoByUId() throws Exception
  {
    CommInfo info1 = createTestDataCommInfo(HOST_1, IMPL_VERSION_1, PORT_1,
             TYPE_1, PROTOCOL_DETAIL_1, PROTOCOL_VERSION_1, REF_ID_1, IS_DEFAULT_TPT_1,
             COMMINFO_NAME_1, COMMINFO_DESCRIPTION_1);
    CommInfo info2 = createTestDataCommInfo(HOST_2, IMPL_VERSION_2, PORT_2,
             TYPE_2, PROTOCOL_DETAIL_2, PROTOCOL_VERSION_2, REF_ID_2, IS_DEFAULT_TPT_2,
             COMMINFO_NAME_2, COMMINFO_DESCRIPTION_2);
    Long uId1 = addTestDataCommInfo(info1);
    Long uId2 = addTestDataCommInfo(info2);

    CommInfo[] expected = null;

    // Retrieve 1 info (uid1)
    checkGetCommInfoByUIdPass(uId1, info1);

    // Retrieve 1 info (uid2)
    checkGetCommInfoByUIdPass(uId2, info2);

    // Retrieve 0 info (-123)
    checkGetCommInfoByUIdFail(INVALID_UID, null);

    // Retrieve 0 info (-123)
    checkGetCommInfoByUIdFail(INVALID_UID, info1);

    // Retrieve 0 info (-123)
    checkGetCommInfoByUIdFail(INVALID_UID, info2);

  }

  private void checkGetCommInfoByUIdPass(Long uId, CommInfo channel) throws Exception
  {
    CommInfo info1 = remote.getCommInfo(uId);
    checkIdenticalCommInfo(info1, channel, false);
  }

  private void checkGetCommInfoByUIdFail(Long uId, CommInfo channel)
  {
    try
    {
      CommInfo info1 = remote.getCommInfo(uId);
      checkIdenticalCommInfo(info1, channel, false);
    }
    catch (AssertionFailedError e)
    {
      ChannelLogger.infoLog(CLASSNAME, "checkGetCommInfoByUIdFail", "Exception encountered :" + e);
      return;
    }
    catch (Exception e)
    {
      ChannelLogger.infoLog(CLASSNAME, "checkGetCommInfoByUIdFail", "Exception encountered :" + e);
      return;
    }
    assertTrue("Channel is the same", false);
  }

}