package com.gridnode.pdip.base.worklist.test;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */
import java.util.Collection;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity;
import com.gridnode.pdip.base.worklist.manager.ejb.GWFWorkListSessionHome;
import com.gridnode.pdip.base.worklist.manager.ejb.GWFWorkListSessionObject;
import com.gridnode.pdip.framework.j2ee.ServiceLookup;

public class GWFWorkListSessionTestCase extends TestCase {

 public GWFWorkListSessionHome sessionHome;
 public GWFWorkListSessionObject sessionObject;

  public GWFWorkListSessionTestCase(String name) {
    super(name);
  }

  public static Test suite() {
     return new TestSuite(GWFWorkListSessionTestCase.class);
  }

  public void setUp(){
    try{
      sessionHome=(GWFWorkListSessionHome)ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(GWFWorkListSessionHome.class);
    }catch(Exception ex){
      System.out.println(" Exception in SetUp  : "+ex.getMessage());
    }
  }

  public void addWorkItem(String user) throws Exception {
  try{
      sessionObject = sessionHome.create();
      assertNotNull("Session Object Not Null testInsertWorkList",sessionObject);
      GWFWorkListValueEntity entity = new GWFWorkListValueEntity();
      entity.setUserId(user);
      entity.setWorkItemComments("WorkItem Comments for user1");
      entity.setWorkItemDescription("WorkItem Description ");
      entity.setCreationDate(new Date());
      sessionObject.addWorkItem(entity);
  }catch(Exception ex){
      System.out.println("testaddWorkItem Failed"+ex.getMessage());
      ex.printStackTrace();
  }
  }


  public void testaddWorkItem(){
  try{
      addWorkItem("user1");
      addWorkItem("user2");
      addWorkItem("user3");
      addWorkItem("user4");
      addWorkItem("user5");
      addWorkItem("user6");
  }catch(Exception ex){
      System.out.println("testaddWorkItem Failed"+ex.getMessage());
      ex.printStackTrace();
  }
  }

  public void testgetWorkList(){
  try{
      sessionObject = sessionHome.create();
      assertNotNull("Session Object Null testrejectWorkList",sessionObject);
      Collection workList = sessionObject.getWorkItemsByUser("user2");
      assertNotNull("WorkList Object Null testgetWorkList",workList);
   }catch(Exception ex){
      System.out.println("testgetWorkItem Failed"+ex.getMessage());
      ex.printStackTrace();
   }

  }


  public void testdropWorkItem(){
  try{
      sessionObject = sessionHome.create();
      assertNotNull("Session Object Not Null testdropWorkList",sessionObject);
      GWFWorkListValueEntity entity = new GWFWorkListValueEntity();
      entity.setUId(10);
      sessionObject.dropWorkItem((Long)((GWFWorkListValueEntity)sessionObject.getWorkItemsByUser("user2").iterator().next()).getKey());
      //.addWorkItem(entity);

  }catch(Exception ex){
      System.out.println("testdropWorkItem Failed"+ex.getMessage());
      ex.printStackTrace();
  }
 }

  public void testperformWorkItem(){
  try{
      sessionObject = sessionHome.create();
      assertNotNull("Session Object Not Null testperformWorkList",sessionObject);
      GWFWorkListValueEntity entity = new GWFWorkListValueEntity();
      entity.setUId(13);
      sessionObject.performWorkItem((Long)((GWFWorkListValueEntity)sessionObject.getWorkItemsByUser("user3").iterator().next()).getKey());
  }catch(Exception ex){
      System.out.println("testperformWorkItem Failed"+ex.getMessage());
      ex.printStackTrace();
  }
 }

/*
  public void testplaceWorkItem(){
  try{
      sessionObject = sessionHome.create();
      assertNotNull("Session Object Not Null testplaceWorkItem",sessionObject);
      GWFWorkListValueEntity entity = new GWFWorkListValueEntity();
      entity.setUserId("user1");
      entity.setWorkItemComments("WorkItem Comments for user1");
      entity.setWorkItemDescription("WorkItem Description ");
      entity.setCreationDate(new Date());
      sessionObject.placeWorkItem((GWFWorkListValueEntity)sessionObject.getWorkItemsByUser("user4").elementAt(0));
   }catch(Exception ex){
      System.out.println("testplaceWorkItem Failed"+ex.getMessage());
      ex.printStackTrace();
   }
  }
*/
  public void testrejectWorkItem(){
  try{
      sessionObject = sessionHome.create();
      assertNotNull("Session Object Not Null testrejectWorkList",sessionObject);
      GWFWorkListValueEntity entity = new GWFWorkListValueEntity();
      entity.setUId(5);
      sessionObject.rejectWorkItem((Long)((GWFWorkListValueEntity)sessionObject.getWorkItemsByUser("user5").iterator().next()).getKey(),"testrejectWorkItem");
   }catch(Exception ex){
      System.out.println("testrejectWorkItem Failed"+ex.getMessage());
      ex.printStackTrace();
   }
  }

/*
  public void testclearWorkList(){
  try{
      sessionObject = sessionHome.create();
      assertNotNull("Session Object Null testclearWorkList",sessionObject);
      Vector workList = new Vector();
      GWFWorkListValueEntity entity = new GWFWorkListValueEntity();
      entity.setUId(27);
      workList.add((GWFWorkListValueEntity)sessionObject.getWorkList("user6").elementAt(0));
      sessionObject.clearWorkList(workList);
   }catch(Exception ex){
      System.out.println("testclearWorkItem Failed"+ex.getMessage());
      ex.printStackTrace();
   }

  }
*/
/*
  public void testassignWorkItems(){
  try{
      sessionObject = sessionHome.create();
      assertNotNull("Session Object Null testassignWorkList",sessionObject);
      Vector users = GWFUserObserver.getInstance().getAvailableUsers();
      if(users != null){
      sessionObject.assignWorkItems(users);
      }
   }catch(Exception ex){
      System.out.println("testassignWorkItem Failed"+ex.getMessage());
      ex.printStackTrace();
   }

  }
*/


 public static void main(String args[]){
 junit.textui.TestRunner.run (suite());
 }

}
