package com.gridnode.pdip.base.time.entities.value.exchange;


import com.gridnode.pdip.framework.log.Log;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class iCalInputTestCase extends TestCase
{
	 static String LogCat= "iCalInputTestCase";
	 
   public iCalInputTestCase(String name)
  {
    super(name);
  }

  public static Test suite()
  {
    return new TestSuite(iCalInputTestCase.class);
  }

  public void setUp()
  {
  }

  public void tearDown()
  {
  }


  public void testParse_0()
  {
  	testParse("test-data/0", "testParse_0");
  }
  
   public void testParse_1()
  {
  	testParse("test-data/1", "testParse_1");
  }

   public void testParse_1_1()
  {
  	testParse("test-data/1.1", "testParse_1_1");
  }
    public void testParse_2445()
  {
  	testParse("test-data/2445.ics", "testParse_2445");
  }


  protected void testParse(String filename, String testName)
  {
  	Log.debug(LogCat, "enter " + testName);
  	try
  	{
  	ParseMime  parser = new ParseMime();
  	List res = parser.parse(filename);
  	String resStr = GenMime.genCalendar(res);
  	Log.log(LogCat, "result is " + resStr);
  	}
  	catch(Exception ex)
  	{
  		Log.err(LogCat, testName,ex);
  	}
  	Log.debug(LogCat, "leave " + testName);
  }


  public static void main(String args[]) throws Exception
  {

    junit.textui.TestRunner.run(suite());

  }




}
