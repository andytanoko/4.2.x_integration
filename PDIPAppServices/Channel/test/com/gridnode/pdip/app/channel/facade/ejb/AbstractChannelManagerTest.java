package com.gridnode.pdip.app.channel.facade.ejb;

import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.helpers.ChannelLogger;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;

import com.gridnode.pdip.framework.j2ee.ServiceLookup;
import com.gridnode.pdip.framework.exceptions.*;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import junit.framework.*;

abstract public class AbstractChannelManagerTest extends TestCase
{
  protected static String CLASSNAME = null;
  protected IChannelManagerHome home = null;
  protected IChannelManagerObj remote = null;

  public AbstractChannelManagerTest(String name, String classname)
  {
    super(name);
    CLASSNAME = classname;
  }

  protected final void setUp() throws Exception
  {
    ChannelLogger.infoLog(CLASSNAME, "setUp", "Enter");
    home = (IChannelManagerHome)ServiceLookup.getInstance(
            ServiceLookup.CLIENT_CONTEXT).getHome(
             IChannelManagerHome.class);
    assertNotNull("Home is null", home);
    remote = home.create();
    assertNotNull("remote is null", remote);
    cleanupData();
    setupData();
    ChannelLogger.infoLog(CLASSNAME,"setUp", "Exit");
  }

  protected final void tearDown() throws Exception
  {
    ChannelLogger.infoLog(CLASSNAME, "tearDown", "Enter");
    cleanupData();
    ChannelLogger.infoLog(CLASSNAME, "tearDown", "Exit");
  }

  abstract protected void setupData() throws Exception;

  abstract protected void cleanupData() throws Exception;

  //---------------------------------------------------------------------------------------------
  //  Methods to manipulate test data.
  //---------------------------------------------------------------------------------------------

  protected final CommInfo createTestDataCommInfo(String host, String implVersion,
            int port, String protocolType, Hashtable protocolDetail, String protocolVersion,
            String refId, boolean isDefaultTpt, String name, String description)
  {
    CommInfo info = new CommInfo();
    info.setDescription(description);
    //info.setHost(host);
    info.setIsDefaultTpt(isDefaultTpt);
    info.setName(name);
    //info.setPort(port);
    //info.setProtocolDetail(protocolDetail);
    info.setProtocolType(protocolType);
    //info.setProtocolVersion(protocolVersion);
    info.setRefId(refId);
    info.setTptImplVersion(implVersion);
    info.setURL(host+":"+port);
    return info;
  }

  protected final Long addTestDataCommInfo(CommInfo info) throws Exception
  {
    return remote.createCommInfo(info);
  }

  protected final void deleteTestDataCommInfo(String refId, String type) throws Exception
  {
    Collection c = findTestDataCommInfoByRefIdOrType(refId, type);
    if (c == null || c.size() <= 0)
       return;
    Iterator iter = c.iterator();
    CommInfo info = null;
    while (iter.hasNext())
    {
      info = (CommInfo) iter.next();
      deleteTestDataCommInfo(new Long(info.getUId()));
    }
  }

  protected final void deleteTestDataCommInfo(Long uId) throws Exception
  {
    remote.deleteCommInfo(uId);
  }

  protected final Collection findTestDataCommInfoByRefIdOrType(String refId, String type) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, CommInfo.REF_ID, filter.getEqualOperator(), refId, false);
    filter.addSingleFilter(filter.getOrConnector(), CommInfo.PROTOCOL_TYPE,
                                                     filter.getEqualOperator(), type, false);
    return remote.getCommInfo(filter);
  }

  protected final CommInfo findTestDataCommInfoByUId(Long uId) throws Exception
  {
    return remote.getCommInfo(uId);
  }


  protected final ChannelInfo createTestDataChannelInfo(String description, CommInfo commInfo,
            String refId, String type, String name)
  {
    ChannelInfo info = new ChannelInfo();
    info.setDescription(description);
    info.setName(name);
    info.setReferenceId(refId);
    info.setTptCommInfo(commInfo);
    info.setTptProtocolType(type);
    return info;
  }

  protected final Long addTestDataChannelInfo(ChannelInfo info) throws Exception
  {
    return remote.createChannelInfo(info);
  }

  protected final void deleteTestDataChannelInfo(String refId, String type) throws Exception
  {
    Collection c = findTestDataChannelInfoByRefIdOrType(refId, type);
    if (c == null || c.size() <= 0)
       return;
    Iterator iter = c.iterator();
    ChannelInfo info = null;
    while (iter.hasNext())
    {
      info = (ChannelInfo) iter.next();
      deleteTestDataChannelInfo(new Long(info.getUId()));
    }
  }

  protected final void deleteTestDataChannelInfo(Long uId) throws Exception
  {
    remote.deleteChannelInfo(uId);
  }

  protected final ChannelInfo findTestDataChannelInfoByUId(Long uId) throws Exception
  {
    return remote.getChannelInfo(uId);
  }

  protected final Collection findTestDataChannelInfoByRefIdOrType(String refId, String type) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ChannelInfo.REF_ID, filter.getEqualOperator(), refId, false);
    filter.addSingleFilter(filter.getOrConnector(), ChannelInfo.TPT_PROTOCOL_TYPE,
                                                     filter.getEqualOperator(), type, false);
    return remote.getChannelInfo(filter);
  }

  protected final void updateTestDataChannelInfo(ChannelInfo info) throws Exception
  {
    remote.updateChannelInfo(info);
  }

  protected final void checkIdentical(Object obj1, Object expectedObj2, boolean checkUId) throws Exception
  {
    if ((obj1 instanceof CommInfo) && (expectedObj2 instanceof CommInfo))
       checkIdenticalCommInfo((CommInfo) obj1, (CommInfo) expectedObj2, checkUId);
    else if ((obj1 instanceof ChannelInfo) && (expectedObj2 instanceof ChannelInfo))
       checkIdenticalChannelInfo((ChannelInfo) obj1, (ChannelInfo) expectedObj2, checkUId);
    else
      throw new Exception("Objects not of the same type or unrecognize type!");
  }

  protected final void checkIdenticalCommInfo(CommInfo info1, CommInfo expectedInfo2, boolean checkUId)
  {
    assertEquals("IsTptDefaults are different.", expectedInfo2.isDefaultTpt(), info1.isDefaultTpt());
    assertEquals("Descriptions are different.", expectedInfo2.getDescription(), info1.getDescription());
    //assertEquals("Hosts are different.", expectedInfo2.getHost(), info1.getHost());
    assertEquals("Names are different.", expectedInfo2.getName(), info1.getName());
    //assertEquals("Ports are different.", expectedInfo2.getPort(), info1.getPort());
    //assertEquals("Protocol Details are different.", expectedInfo2.getProtocolDetail(), info1.getProtocolDetail());
    assertEquals("Protocol Types are different.", expectedInfo2.getProtocolType(), info1.getProtocolType());
    //assertEquals("Protocol Versions are different.", expectedInfo2.getProtocolVersion(), info1.getProtocolVersion());
    assertEquals("Reference Ids are different.", expectedInfo2.getRefId(), info1.getRefId());
    assertEquals("Implementaion versions are different.", expectedInfo2.getTptImplVersion(), info1.getTptImplVersion());
    if (checkUId)
      assertEquals("UId are different.", expectedInfo2.getUId(), info1.getUId());
  }

  protected final void checkIdenticalChannelInfo(ChannelInfo info1, ChannelInfo expectedInfo2, boolean checkUId)
  {
    assertEquals("Descriptions are different.", expectedInfo2.getDescription(), info1.getDescription());
    assertEquals("Names are different.", expectedInfo2.getName(), info1.getName());
    assertEquals("ReferenceIds are different.", expectedInfo2.getReferenceId(), info1.getReferenceId());
    assertEquals("CommInfos are different.", expectedInfo2.getTptCommInfo(), info1.getTptCommInfo());
    assertEquals("Type are different.", expectedInfo2.getTptProtocolType(), info1.getTptProtocolType());
    if (checkUId)
      assertEquals("UIds are different.", expectedInfo2.getUId(), info1.getUId());
  }

  //--------------------------------------------------------------------------------------------
  // Helper methods to test find methods.
  //--------------------------------------------------------------------------------------------

  protected final boolean isEmpty (Collection c)
  {
    return (c == null || c.size() <= 0);
  }

  protected final boolean isEmpty (Object[] o)
  {
    return (o == null || o.length <= 0);
  }


  protected final void checkGetResultPass(Collection resultList, Object[] expectedList)
            throws Exception
  {
    if (isEmpty(resultList) && isEmpty(expectedList))
      return;
    else if (isEmpty(resultList))
      assertTrue("Return result is empty but expected result size is " + expectedList.length, false);
    else if (isEmpty(expectedList))
      assertTrue("Expected result is empty but return result size is " + resultList.size(), false);
    assertEquals("Expected result is not the same as the result.", + expectedList.length, resultList.size());
    Iterator iter = resultList.iterator();
    Object result = null;
    boolean[] expectedResultFound = new boolean[expectedList.length];
    for (int i = 0; i < expectedResultFound.length; i++)
      expectedResultFound[i] = false;
    while (iter.hasNext())
    {
      result = iter.next();
      for (int i = 0; i < expectedList.length; i++)
      {
        try
        {
          checkIdentical(result, expectedList[i], false);
          expectedResultFound[i] = true;
          break;
        }
        catch (AssertionFailedError er)
        {
        }
      }
    }
    for (int i = 0; i < expectedResultFound.length; i++)
      assertTrue("Expected item [" + i + "] not found!", expectedResultFound[i]);
  }

  protected final void checkGetResultFail(Collection resultList, Object[] expectedList)
            throws Exception
  {
    // Check both not empty.
    if (isEmpty(resultList) && isEmpty(expectedList))
      assertTrue("Return result and expected result are both empty!", false);
    //Check not the same size.

    if (resultList.size() != expectedList.length)
    {
      ChannelLogger.infoLog(CLASSNAME, "checkGetResultFail",
            "Expected size and return result size is not the same!");
      return;
    }
    Iterator iter = resultList.iterator();
    Object result = null;
    boolean[] expectedResultFound = new boolean[expectedList.length];
    for (int i = 0; i < expectedResultFound.length; i++)
      expectedResultFound[i] = false;
    while (iter.hasNext())
    {
      result = iter.next();
      for (int i = 0; i < expectedList.length; i++)
      {
        try
        {
          checkIdentical(result, expectedList[i], false);
          expectedResultFound[i] = true;
          break;
        }
        catch (AssertionFailedError er)
        {
        }
      }
    }
    for (int i = 0; i < expectedResultFound.length; i++)
      if (expectedResultFound [i] == false)
         return;
    assertTrue("Expected and result are the same!", false);
  }

}