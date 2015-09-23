package com.gridnode.pdip.base.worklist.test;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */

import java.util.*;

import com.gridnode.pdip.base.worklist.entities.ejb.*;
import com.gridnode.pdip.base.worklist.entities.model.*;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.j2ee.*;
import junit.framework.*;


public class GWFWorkListTestCase extends TestCase{

 private GWFWorkListEntityHome   entityHome;
 private GWFWorkListEntityObject entityObject;

  public GWFWorkListTestCase(String name) {
    super(name);
  }

  public static Test suite() {
    	return new TestSuite(GWFWorkListTestCase.class);
    }


  public void setUp(){
    try{
      entityHome=(GWFWorkListEntityHome)ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(GWFWorkListEntityHome.class);
      System.out.println("Object Found ... ");
    }catch(Exception ex){
      System.out.println(" Exception in SetUp  : "+ex.getMessage());
    }
  }

  public void testWorkListValueCreate(){
   try{
      GWFWorkListValueEntity valueEntity = new GWFWorkListValueEntity();
      valueEntity.setUserId("user2");
      valueEntity.setWorkItemDescription("WorkItem 2");
      valueEntity.setWorkItemComments("WorkItem through Entity Bean 2");
      entityObject = entityHome.create(valueEntity);
      assertNotNull("WorkListEntity Value NULL in GWFWorkListTestCase",entityObject);
      valueEntity = (GWFWorkListValueEntity)entityObject.getData();
      System.out.println("User Name "+valueEntity.getUserId());
      System.out.println("WorkItem Comments "+valueEntity.getWorkItemComments());
      System.out.println("WorkItem Description "+valueEntity.getWorkItemDescription());
     }catch(Exception ex){
      System.out.println("testWorkListValueCreate  "+ex.getMessage());
      ex.printStackTrace();
     }

  }

/*  public void testWorkListValuefindByPrimaryKey(){
  try{
      entityObject = entityHome.findByPrimaryKey(new Long(4));
      assertNotNull("WorkListEntity Value NULL in GWFWorkListTestCase FindBy PrimaryKey",entityObject);
      GWFWorkListValueEntity valueEntity = (GWFWorkListValueEntity)entityObject.getData();
      System.out.println("User Name "+valueEntity.getUserId());
      System.out.println("WorkItem Comments "+valueEntity.getWorkItemComments());
      System.out.println("WorkItem Description "+valueEntity.getWorkItemDescription());

  }catch(Exception ex){
      System.out.println("testWorkListValuefindByPrimaryKey  Fail "+ex.getMessage());
      ex.printStackTrace();
  }
}
*/
  public void testWorkListValuefindByFilter(){
   try{
        IDataFilter filter=new DataFilterImpl();
        filter.addSingleFilter(null,GWFIWorkListValueEntity.USER_ID,filter.getEqualOperator(),"user2",false);
        Collection col = entityHome.findByFilter(filter);
        assertNotNull("WorkListEntity Value NULL in GWFWorkListTestCase FindBy Filter",col);
        Iterator iterator=col.iterator();
        while(iterator.hasNext()){
            GWFWorkListValueEntity valueEntity;
            entityObject=(GWFWorkListEntityObject)iterator.next();
            valueEntity =(GWFWorkListValueEntity)entityObject.getData();
            System.out.println("From Filter User Name "+valueEntity.getUserId());
            System.out.println("WorkItem Comments "+valueEntity.getWorkItemComments());
            System.out.println("WorkItem Description "+valueEntity.getWorkItemDescription());
        }

   }catch(Exception ex){
      System.out.println("testWorkListValuefindByFilter  Fail "+ex.getMessage());
      ex.printStackTrace();
   }
  }


/*  public void testWorkListValueRemove(){
  try{
     //entityObject = entityHome.findByPrimaryKey(new Long(4));
    // assertNotNull("WorkListEntity Value NULL in GWFWorkListTestCase Remove",entityObject);
     //entityObject.remove();
     //entityHome.remove(entityObject);
    // assertNull("Successfully Removed",entityObject);
   }catch(Exception ex){
      System.out.println("testWorkListValueRemove  Fail "+ex.getMessage());
      ex.printStackTrace();
   }

  }


/*
  public void perform(String user){
     try{
      GWFWorkListValueEntity valueEntity = new GWFWorkListValueEntity();
      valueEntity.setUserId("user2");
      valueEntity.setWorkItemDescription("WorkItem 2");
      valueEntity.setWorkItemComments("WorkItem through Entity Bean 2");
      entityObject = entityHome.create(valueEntity);
      valueEntity = (GWFWorkListValueEntity)entityObject.getData();
      System.out.println("User Name "+valueEntity.getUserId());
      System.out.println("WorkItem Comments "+valueEntity.getWorkItemComments());
      System.out.println("WorkItem Description "+valueEntity.getWorkItemDescription());
      entityObject = entityHome.findByPrimaryKey(new Long(4));

/*      entityObject = entityHome.findByPrimaryKey(new Long(4));
      valueEntity = (GWFWorkListValueEntity)entityObject.getData();
      System.out.println("User Name "+valueEntity.getUserId());
      System.out.println("WorkItem Comments "+valueEntity.getWorkItemComments());
      System.out.println("WorkItem Description "+valueEntity.getWorkItemDescription());

        IDataFilter filter=new DataFilterImpl();
        filter.addSingleFilter(null,GWFIWorkListValueEntity.ICAL_CREATION_DT,filter.getLessOperator(),new Date(),false);
        Collection col = entityHome.findByFilter(filter);
        Iterator iterator=col.iterator();
        while(iterator.hasNext()){
            entityObject=(GWFWorkListEntityObject)iterator.next();
             valueEntity =(GWFWorkListValueEntity)entityObject.getData();
      System.out.println("User Name "+valueEntity.getUserId());
      System.out.println("WorkItem Comments "+valueEntity.getWorkItemComments());
      System.out.println("WorkItem Description "+valueEntity.getWorkItemDescription());
        }



        //executePartnerGroupByFilter(filter);




     }catch(Exception ex){
        System.out.println(" Exception in Perfrom ");
        ex.printStackTrace();
     }

  }
*/
  public static void main(String args[]) throws Exception{

          junit.textui.TestRunner.run (suite());
  //    GWFWorkListTestCase testCase = new GWFWorkListTestCase();

    /*    testCase.setUp();
    testCase.perform("user1");*/
  }

  }
