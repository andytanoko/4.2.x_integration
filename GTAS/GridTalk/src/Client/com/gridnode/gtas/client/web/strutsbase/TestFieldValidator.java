/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TestFieldValidator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-19     Andrew Hill         Created (My first GTAS junit test!)
 */
package com.gridnode.gtas.client.web.strutsbase;

import junit.framework.TestCase;
import junit.textui.TestRunner;

import org.apache.struts.action.ActionErrors;

import com.gridnode.gtas.client.ctrl.IRangeConstraint;
import com.gridnode.gtas.client.ctrl.ISingleRangeConstraint;

public class TestFieldValidator extends TestCase
{
  
  public TestFieldValidator(String name)
  {
    super(name);
  }
  
  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testValidateValue()
  {
    try
    {
      String[] labels = { "label1","label2","label3", };
      String[] values = { "1","2","3", };
      FakeEnumeratedConstraint enumConst = new FakeEnumeratedConstraint(labels,values);
      
      ISingleRangeConstraint range1
        = new FakeSingleRangeConstraint(new Integer(5), new Integer(15));
      ISingleRangeConstraint range2
        = new FakeSingleRangeConstraint(new Integer(25), new Integer(35));
      ISingleRangeConstraint[] rangesArray = 
      {
        range1,
        range2,
      };
      IRangeConstraint ranges = new FakeMultipleRangeConstraint(rangesArray);
      
      /*
       0:actionErrors,
       1:value,
       2:valueClass,
       3:required,
       4:constraint,
       5:fieldName,
       6:errorMsgPrefix,
       7:expectTrue
      */
      Object[][] testParams = 
      {
        { null, "1234", Integer.class.getName(), Boolean.TRUE,  null, null, "test", Boolean.TRUE, },  //0
        { null, "",     Integer.class.getName(), Boolean.TRUE,  null, null, "test", Boolean.FALSE, }, //1
        { null, "xxx",  Integer.class.getName(), Boolean.TRUE,  null, null, "test", Boolean.FALSE, }, //2
        { null, "0123", Integer.class.getName(), Boolean.TRUE,  null, null, "test", Boolean.TRUE, },  //3
        { null, "00",   Integer.class.getName(), Boolean.TRUE,  null, null, "test", Boolean.TRUE, },  //4
        { null, "",     Integer.class.getName(), Boolean.FALSE, null, null, "test", Boolean.TRUE, },  //5
        
        { null, "",     String.class.getName(), Boolean.TRUE,   null, null, "test", Boolean.FALSE, }, //6
        { null, "1234", String.class.getName(), Boolean.TRUE,   null, null, "test", Boolean.TRUE, },  //7
        { null, "xxx",  String.class.getName(), Boolean.TRUE,   null, null, "test", Boolean.TRUE, },  //8
        { null, null,   String.class.getName(), Boolean.FALSE,  null, null, "test", Boolean.TRUE, },  //9
        { null, null,   String.class.getName(), Boolean.FALSE,  null, null, "test", Boolean.TRUE, },  //10
        
        { null, "1",    Integer.class.getName(),Boolean.TRUE,   enumConst, null, "test", Boolean.TRUE, },    //11
        { null, "4",    Integer.class.getName(),Boolean.TRUE,   enumConst, null, "test", Boolean.FALSE, },    //12
        { null, "",     Integer.class.getName(),Boolean.FALSE,  enumConst, null, "test", Boolean.TRUE, },    //13
        { null, "",     Integer.class.getName(),Boolean.TRUE,   enumConst, null, "test", Boolean.FALSE, },    //14
        { null, "xxx",  Integer.class.getName(),Boolean.TRUE,   enumConst, null, "test", Boolean.FALSE, },    //15
      
        { null, "1",    Integer.class.getName(),Boolean.TRUE,   range1, null, "test", Boolean.FALSE, },    //16
        { null, "1",    Integer.class.getName(),Boolean.FALSE,  range1, null, "test", Boolean.FALSE, },    //17
        { null, "10",   Integer.class.getName(),Boolean.TRUE,   range1, null, "test", Boolean.TRUE, },    //18
        
        { null, "20",   Integer.class.getName(),Boolean.TRUE,   ranges, null, "test", Boolean.FALSE, },    //19
        { null, "25",   Integer.class.getName(),Boolean.TRUE,   ranges, null, "test", Boolean.TRUE, },    //20
        { null, "35",   Integer.class.getName(),Boolean.TRUE,   ranges, null, "test", Boolean.TRUE, },    //21
        
        { null, "36",   Integer.class.getName(),Boolean.TRUE,   ranges, null, "test", Boolean.FALSE, },    //22
        { null, "",     Integer.class.getName(),Boolean.FALSE,  ranges, null, "test", Boolean.TRUE, },    //23
        { null, "xxx",  Integer.class.getName(),Boolean.FALSE,  ranges, null, "test", Boolean.FALSE, },    //24
      
      };
      
      for(int i=0; i < testParams.length; i++)
      {
        ActionErrors errors = (ActionErrors)testParams[i][0];
        String value = (String)testParams[i][1];
        String valueClass = (String)testParams[i][2];
        boolean required = ((Boolean)testParams[i][3]).booleanValue();
        Object constraint = testParams[i][4];
        String fieldName = (String)testParams[i][5];
        String errorMsgPrefix = (String)testParams[i][6];
        boolean ok = FieldValidator.validateValue(errors,
                                                  value,
                                                  valueClass,
                                                  required,
                                                  constraint,
                                                  fieldName,
                                                  errorMsgPrefix);
        boolean expectOk = ((Boolean)testParams[i][7]).booleanValue();
        boolean passed = (expectOk == ok);
        super.assertTrue("Test " + i + " failed", passed);  
        //System.out.println("Test:" + i + " " + (passed ? "passed" : "FAILED"));
      }
    }
    catch(Throwable t)
    {
      fail("An (unexpected) throwable was thrown:" + t.getMessage() );
    }  
  }

  public static void main(String[] args)
  {
    TestRunner.run(TestFieldValidator.class);
  }

}
