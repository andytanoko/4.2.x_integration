package com.gridnode.gtas.server.locale.listener.ejb;

import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.util.DefaultTimeServer;
import com.gridnode.gtas.events.locale.GetUtcTimeEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.locale.actions.GetUtcTimeAction;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.gtas.server.notify.TimeUpdateNotification;
import com.gridnode.gtas.server.locale.helpers.*;

import com.gridnode.pdip.framework.util.ITimeServer;
import com.gridnode.pdip.framework.rpf.ejb.action.IEJBAction;
import com.gridnode.pdip.framework.rpf.ejb.StateMachine;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.util.TimeUtil;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

public class TimeUpdateListenerMDBeanTest extends ActionTestHelper
{
  private long[][] _times;
  private Properties _props = new Properties();
  private Timestamp _testDate;
  private UtcTimeServer _timeServer;
  private GetUtcTimeEvent _event;


  public TimeUpdateListenerMDBeanTest(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(TimeUpdateListenerMDBeanTest.class);
  }

  public static void main(String args[])
  {
    junit.textui.TestRunner.run(suite());
  }

  protected void cleanUp()
  {
    //purgeSessions();

  }

  protected void cleanTestData() throws java.lang.Exception
  {
  //  closeAllSessions();
  }

  protected void prepareTestData() throws java.lang.Exception
  {
    long currentTime = TimeUtil.getCurrentLocalTimeMillis();
    long oneHour = 3600000;
    long positive = 8 * oneHour + currentTime;
    long negative = -5 * oneHour + currentTime;

    _times = new long[][] {
                 {positive, currentTime},
                 {currentTime, currentTime},
                 {negative, currentTime},
                 };


    Calendar cal = GregorianCalendar.getInstance();
    cal.set(2002, 9, 19, 5, 5, 29);
    _testDate = new Timestamp(cal.getTime().getTime());

    Logger.debug("Test date (ts)= "+_testDate);
    Logger.debug("Test date (ms)= "+_testDate.getTime());

    _timeServer = UtcTimeServer.getInstance();

    TimeUtil.setTimeServer(_timeServer);

    _event = new GetUtcTimeEvent();

//    createSessions(1);
//    createStateMachines(1);

  }

  protected void unitTest() throws java.lang.Exception
  {
    for (int i=0; i<_times.length; i++)
      checkTimeUpdate(_times[i][0], _times[i][1]);
  }

  protected IEJBAction createNewAction()
  {
    return new GetUtcTimeAction();
  }

  // not used
  protected void checkActionEffect(
   BasicEventResponse response, IEvent event, StateMachine sm)
  {
    Long utcOffset = (Long)response.getReturnData();

    // update the utc offset at client site
    Logger.debug("Retrieved offset = "+utcOffset);
//    _timeServer.setUtcOffset(utcOffset);
  }

  // ********************* Own methods ************************

  private synchronized void checkTimeUpdate(long localTime, long utcTime)
    throws Exception
  {
    // broadcast
    _props.setProperty("utc", String.valueOf(utcTime));
    _props.setProperty("local", String.valueOf(localTime));

    TimeUpdateNotification notification =
      new TimeUpdateNotification(TimeUpdateNotification.UPDATE_UTC_TIME, _props);

    Notifier.getInstance().broadcast(notification);

    // wait
    try
    {
      wait(30000L); // 30 seconds
    }
    catch (Exception ex)
    {
    }

    //refreshTimeServer();
    //checkSuccess(_event, _sessions[0], _sm[0], IErrorCode.NO_ERROR);

    long offset = utcTime - localTime;
    Logger.debug("Input UTC Time = "+new Timestamp(utcTime));
    Logger.debug("Input Local Time = "+new Timestamp(localTime));
    Logger.debug("Expected offset: "+offset);

    // check local to utc
    Timestamp ts = TimeUtil.localToUtcTimestamp(_testDate);
    Logger.debug("Converted local to UTc timestamp: "+ts);
    assertEquals("LocalToUtc conversion fail", offset, ts.getTime() - _testDate.getTime());

    // check utc to local
    ts = TimeUtil.utcToLocalTimestamp(_testDate);
    Logger.debug("Converted utc to local timestamp: "+ts);
    assertEquals("UtcToLocal conversion fail", offset, _testDate.getTime() - ts.getTime());
  }


//  private void refreshTimeServer()
//  {
//    Logger.log("Current time = "+new Date());
//    ConfigurationManager.getInstance().refreshConfiguration(
//      IFrameworkConfig.FRAMEWORK_TIME_CONFIG);
//    Logger.debug("Time config: "+ConfigurationManager.getInstance().getConfig(
//      IFrameworkConfig.FRAMEWORK_TIME_CONFIG).getProperties());
//
//    long retrievedOffset = ConfigurationManager.getInstance().getConfig(
//      IFrameworkConfig.FRAMEWORK_TIME_CONFIG).getLong("utc.offset");
//
//    Logger.debug("Retrieved offset: "+retrievedOffset);
//
//    Logger.debug("offset in timeServer: "+_timeServer.getUtcOffset());
//  }
}