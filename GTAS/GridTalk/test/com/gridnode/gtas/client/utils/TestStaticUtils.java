package com.gridnode.gtas.client.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;
public class TestStaticUtils extends TestCase
{
  private static DateFormat _dateFormat = null;
  
  public TestStaticUtils(String name)
  {
    super(name);
  }
  
  protected void setUp() throws Exception
  {
    _dateFormat = new SimpleDateFormat(DateUtils.DATE_INPUT_PATTERN);
  }
  
  protected void tearDown() throws Exception
  {
    _dateFormat = null;
  }
  
  public void testConvertDate0() throws Throwable
  {
    java.util.Date expectedValue = new java.util.Date(0);
    
    String dateStr = _dateFormat.format(expectedValue);
    boolean emptyIsNull = false;
    boolean useStaticUtils = false;
    
    internalTestDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertDate1() throws Throwable
  {
    java.util.Date expectedValue = new java.util.Date(0);
    
    String dateStr = "0";
    boolean emptyIsNull = false;
    boolean useStaticUtils = false;
    
    internalTestDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertDate2() throws Throwable
  {
    java.util.Date expectedValue = null;
    
    String dateStr = null;
    boolean emptyIsNull = false;
    boolean useStaticUtils = false;
    
    internalTestDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertDate3() throws Throwable
  {
    java.util.Date expectedValue = null;
    
    String dateStr = "";
    boolean emptyIsNull = true;
    boolean useStaticUtils = false;
    
    internalTestDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertDate4() throws Throwable
  {
    java.util.Date expectedValue = null;
    
    String dateStr = "xxx";
    boolean emptyIsNull = false;
    boolean useStaticUtils = true;
    
    internalTestDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertDate5() throws Throwable
  {
    java.util.Date expectedValue = null;
    
    String dateStr = "";
    boolean emptyIsNull = false;
    boolean useStaticUtils = true;
    
    internalTestDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertDate6() throws Throwable
  {
    java.util.Date expectedValue = new java.util.Date(0);
    
    String dateStr = "0";
    boolean emptyIsNull = false;
    boolean useStaticUtils = true;
    
    internalTestDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  private void internalTestDate(String dateString,
                                boolean emptyIsNull,
                                boolean useStaticUtils,
                                java.util.Date expectedValue)
    throws Exception
  {
    java.util.Date retval = (java.util.Date)StaticUtils.convert( dateString,
                                                                   java.util.Date.class.getName(),
                                                                   emptyIsNull,
                                                                   useStaticUtils);
                                                                   
    String retvalMs = retval == null ? null : "" + retval.getTime();
    String expectMs = expectedValue == null ? null : "" + expectedValue.getTime();         
    assertEquals("Didnt convert java.util.Date as expected. retvalMs=" 
                  + retvalMs
                  + " and not "
                  + expectMs
                  + ".",
                  expectedValue, retval);
  }  
  
  //..................................................................................
  
  public void testConvertSqlDate0() throws Throwable
  {
    java.sql.Date expectedValue = new java.sql.Date(0);
    
    String dateStr = _dateFormat.format(expectedValue);
    boolean emptyIsNull = false;
    boolean useStaticUtils = false;
    
    internalTestSqlDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertSqlDate1() throws Throwable
  {
    java.sql.Date expectedValue = new java.sql.Date(0);
    
    String dateStr = "0";
    boolean emptyIsNull = false;
    boolean useStaticUtils = false;
    
    internalTestSqlDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertSqlDate2() throws Throwable
  {
    java.sql.Date expectedValue = null;
    
    String dateStr = null;
    boolean emptyIsNull = false;
    boolean useStaticUtils = false;
    
    internalTestSqlDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertSqlDate3() throws Throwable
  {
    java.sql.Date expectedValue = null;
    
    String dateStr = "";
    boolean emptyIsNull = true;
    boolean useStaticUtils = false;
    
    internalTestSqlDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertSqlDate4() throws Throwable
  {
    java.sql.Date expectedValue = null;
    
    String dateStr = "xxx";
    boolean emptyIsNull = false;
    boolean useStaticUtils = true;
    
    internalTestSqlDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertSqlDate5() throws Throwable
  {
    java.sql.Date expectedValue = null;
    
    String dateStr = "";
    boolean emptyIsNull = false;
    boolean useStaticUtils = true;
    
    internalTestSqlDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertSqlDate6() throws Throwable
  {
    java.sql.Date expectedValue = new java.sql.Date(0);
    
    String dateStr = "0";
    boolean emptyIsNull = false;
    boolean useStaticUtils = true;
    
    internalTestSqlDate(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  private void internalTestSqlDate(String dateString,
                                boolean emptyIsNull,
                                boolean useStaticUtils,
                                java.sql.Date expectedValue)
    throws Exception
  {
    java.sql.Date retval = (java.sql.Date)StaticUtils.convert( dateString,
                                                                   java.sql.Date.class.getName(),
                                                                   emptyIsNull,
                                                                   useStaticUtils);
                                                                   
    String retvalMs = retval == null ? null : "" + retval.getTime();
    String expectMs = expectedValue == null ? null : "" + expectedValue.getTime();         
    assertEquals("Didnt convert java.sql.Date as expected. retvalMs=" 
                  + retvalMs
                  + " and not "
                  + expectMs
                  + ".",
                  expectedValue, retval);
  } 
  
  //............................................................................................
  
    public void testConvertTimestamp0() throws Throwable
  {
    java.sql.Timestamp expectedValue = new java.sql.Timestamp(0);
    
    String dateStr = _dateFormat.format(expectedValue);
    boolean emptyIsNull = false;
    boolean useStaticUtils = false;
    
    internalTestTimestamp(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertTimestamp1() throws Throwable
  {
    java.sql.Timestamp expectedValue = new java.sql.Timestamp(0);
    
    String dateStr = "0";
    boolean emptyIsNull = false;
    boolean useStaticUtils = false;
    
    internalTestTimestamp(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertTimestamp2() throws Throwable
  {
    java.sql.Timestamp expectedValue = null;
    
    String dateStr = null;
    boolean emptyIsNull = false;
    boolean useStaticUtils = false;
    
    internalTestTimestamp(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertTimestamp3() throws Throwable
  {
    java.sql.Timestamp expectedValue = null;
    
    String dateStr = "";
    boolean emptyIsNull = true;
    boolean useStaticUtils = false;
    
    internalTestTimestamp(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertTimestamp4() throws Throwable
  {
    java.sql.Timestamp expectedValue = null;
    
    String dateStr = "xxx";
    boolean emptyIsNull = false;
    boolean useStaticUtils = true;
    
    internalTestTimestamp(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertTimestamp5() throws Throwable
  {
    java.sql.Timestamp expectedValue = null;
    
    String dateStr = "";
    boolean emptyIsNull = false;
    boolean useStaticUtils = true;
    
    internalTestTimestamp(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  public void testConvertTimestamp6() throws Throwable
  {
    java.sql.Timestamp expectedValue = new java.sql.Timestamp(0);
    
    String dateStr = "0";
    boolean emptyIsNull = false;
    boolean useStaticUtils = true;
    
    internalTestTimestamp(dateStr,emptyIsNull,useStaticUtils,expectedValue);  
  }
  
  private void internalTestTimestamp(String dateString,
                                boolean emptyIsNull,
                                boolean useStaticUtils,
                                java.sql.Timestamp expectedValue)
    throws Exception
  {
    java.sql.Timestamp retval = (java.sql.Timestamp)StaticUtils.convert( dateString,
                                                                   java.sql.Timestamp.class.getName(),
                                                                   emptyIsNull,
                                                                   useStaticUtils);
                                                                   
    String retvalMs = retval == null ? null : "" + retval.getTime();
    String expectMs = expectedValue == null ? null : "" + expectedValue.getTime();         
    assertEquals("Didnt convert java.sql.Timestamp as expected. retvalMs=" 
                  + retvalMs
                  + " and not "
                  + expectMs
                  + ".",
                  expectedValue, retval);
  }  
  
  //.............................................................................................
  
  public void testReDimString()
  {
    String[] array = 
    {
      "hello",
      "bonjour",
    };
    String[] newArray = StaticUtils.reDim(array, 3);
    Integer newLength = new Integer(newArray.length);
    Integer expectedLength = new Integer(3);
    
    assertEquals("New array is wrong size.",expectedLength,newLength);
    assertEquals("newArray[2] is not null.",null,newArray[2]); 
  }
  
  public void testReDimString1()
  {
    String[] array = 
    {
      "hello",
      "bonjour",
    };
    String[] newArray = StaticUtils.reDim(array, 3, "jambo");
    Integer newLength = new Integer(newArray.length);
    Integer expectedLength = new Integer(3);
    
    assertEquals("New array is wrong size.",expectedLength,newLength);
    assertEquals("newArray[2] is not \"jambo\".","jambo",newArray[2]); 
  }
  
  public void testReDimString2()
  {
    String[] array = 
    {
      "hello",
      "bonjour",
    };
    String[] newArray = StaticUtils.reDim(array, 2);
    if(!(array == newArray))
    {
      fail("Did not return same instance for same size array");
    }
  }
  
  public void testReDimString3()
  {
    String[] array = null;
    String[] newArray = StaticUtils.reDim(array, 50, "bob");
    assertNotNull("newArray", newArray);
    Integer newLength = new Integer(newArray.length);
    Integer expectedLength = new Integer(50);
    
    assertEquals("New array is wrong size.",expectedLength,newLength);
    assertEquals("newArray[2] is not \"bob\".","bob",newArray[2]); 
  }
  
  public void testReDimString4()
  {
    String[] array = 
    {
      "hello",
      "bonjour",
    };
    try
    {
      String[] newArray = StaticUtils.reDim(array, -42, "bob");
      fail("Didnt throw an IllegalArgumentException for negative newSize");
    }
    catch(IllegalArgumentException goodPain)
    {
      //Expecting it. Is correct :-)
    }
  }
  
  //............................................................................................
  
  public void testGetArrayLength()
  {
    String[] array = 
    {
      "foo",
      "bar",
      "baz",
    };
    
    Integer retval = new Integer( StaticUtils.getArrayLength(array) );
    Integer expectedValue = new Integer(3);
    assertEquals("Wrong length returned!", expectedValue, retval );
  }
  
  public void testGetArrayLength1()
  {
    Long[] array = null;
    
    Integer retval = new Integer( StaticUtils.getArrayLength(array) );
    Integer expectedValue = new Integer(0);
    assertEquals("Wrong length returned!", expectedValue, retval );
  }
  
  public void testGetArrayLength2()
  {
    Long[] array = new Long[0];
    
    Integer retval = new Integer( StaticUtils.getArrayLength(array) );
    Integer expectedValue = new Integer(0);
    assertEquals("Wrong length returned!", expectedValue, retval );
  }
  
  public void testGetArrayLength3()
  {
    Long[] array =
    {
      new Long(1),
    };
    
    Integer retval = new Integer( StaticUtils.getArrayLength(array) );
    Integer expectedValue = new Integer(1);
    assertEquals("Wrong length returned!", expectedValue, retval );
  }
}
