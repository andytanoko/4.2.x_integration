package com.gridnode.gtas.client.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import junit.framework.TestCase;
public class TestDateUtils extends TestCase
{
  private static final long EIGHT_HOURS = 8 * 60 * 60 * 1000;
  private static final long EIGHT_AM_2003_05_21_SGT = Long.parseLong("1053475200000");
  private static final long MIDNIGHT_2003_05_21_UTC = EIGHT_AM_2003_05_21_SGT;
  private static final String EPOCH_STRING = "1970-01-01 00:00:00";
  private static final String EIGHT_AM_PAST_EPOCH = "1970-01-01 08:00:00";
  private static final TimeZone _tzSingapore = TimeZone.getTimeZone("Asia/Singapore");
  private static final TimeZone _tzUtc = TimeZone.getTimeZone("UTC");
  
  public TestDateUtils(String name)
  {
    super(name);
  }
  
  protected void setUp() throws Exception
  {
    
  }
  
  protected void tearDown() throws Exception
  {
    
  }
  
  public void testParseDate1()
  {
    java.util.Date date = DateUtils.parseDate(EPOCH_STRING, _tzSingapore, null, null);
    Long expectedMs = new Long( 0 - EIGHT_HOURS );
    Long actualMs = new Long(date.getTime());
    assertEquals("Date parsed incorrectly.",expectedMs,actualMs);
  }
  
  public void testParseDate2()
  {
    java.util.Date date = DateUtils.parseDate(EIGHT_AM_PAST_EPOCH, _tzUtc, null, null);
    Long expectedMs = new Long( EIGHT_HOURS );
    Long actualMs = new Long(date.getTime());
    assertEquals("Date parsed incorrectly.",expectedMs,actualMs);
  }
  
  public void testParseDate3()
  {
    java.util.Date date = DateUtils.parseDate(EIGHT_AM_PAST_EPOCH, _tzSingapore, null, null);
    Long expectedMs = new Long( 0 );
    Long actualMs = new Long(date.getTime());
    assertEquals("Date parsed incorrectly.",expectedMs,actualMs);
  }
  
  /*java.util.Date date = DateUtils.parseDate("2003-05-21 08:00:00", _tzSingapore, null, null);
    System.out.println("" + date.getTime());*/
  
  public void testZeroDate()
  {
    
    java.util.Date date = new java.util.Date(EIGHT_AM_2003_05_21_SGT); //2003-05-21 08:00:00 SGT
    DateUtils.zeroDate(date, _tzSingapore);
    Long expectedMs = new Long( 0 ); //1970-01-01 08:00:00 SGT
    Long actualMs = new Long(date.getTime());
    assertEquals("Date portion of date zeroed incorrectly.",expectedMs,actualMs);
  }
  
  public void testZeroDate1()
  {
    java.util.Date date = new java.util.Date(MIDNIGHT_2003_05_21_UTC); //2003-05-21 00:00:00 UTC
    DateUtils.zeroDate(date, _tzUtc);
    Long expectedMs = new Long( 0 ); //1970-01-01 00:00:00 UTC
    Long actualMs = new Long(0);
    assertEquals("Date portion of date zeroed incorrectly.",expectedMs,actualMs);
  }
  
  public void testZeroDate2()
  {
    java.util.Date date = new java.util.Date(0); //1970-01-01 00:00:00 UTC
    DateUtils.zeroDate(date, _tzUtc);
    Long expectedMs = new Long( 0 ); //1970-01-01 00:00:00 UTC
    Long actualMs = new Long(0);
    assertEquals("Date portion of date zeroed incorrectly.",expectedMs,actualMs);
  }
  
  
  
  
  
  public void testZeroTime()
  {
    java.util.Date date = new java.util.Date(EIGHT_HOURS); //1970-01-01 08:00:00 UTC
    DateUtils.zeroTime(date, _tzUtc);
    Long expectedMs = new Long( 0 ); //1970-01-01 00:00:00 UTC
    Long actualMs = new Long(date.getTime());
    assertEquals("Time portion of date zeroed incorrectly.",expectedMs,actualMs);
  }
  
  public void testZeroTime1()
  {
    java.util.Date date = new java.util.Date(0); //1970-01-01 08:00:00 SGT
    DateUtils.zeroTime(date, _tzSingapore);
    Long expectedMs = new Long( 0 - EIGHT_HOURS ); //1970-01-01 00:00:00 SGT
    Long actualMs = new Long(date.getTime());
    assertEquals("Time portion of date zeroed incorrectly.",expectedMs,actualMs);
  }
   
  public void testZeroTime2()
  {
    java.util.Date date = new java.util.Date(0); //1970-01-01 00:00:00 UTC
    DateUtils.zeroTime(date, _tzUtc);
    Long expectedMs = new Long(0); //1970-01-01 00:00:00 UTC
    Long actualMs = new Long(date.getTime());
    assertEquals("Time portion of date zeroed incorrectly.",expectedMs,actualMs);
  }
    
}
