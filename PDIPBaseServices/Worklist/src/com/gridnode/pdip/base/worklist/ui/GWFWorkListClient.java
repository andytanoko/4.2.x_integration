package com.gridnode.pdip.base.worklist.ui;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */

import java.util.Vector;

import com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity;
import com.gridnode.pdip.base.worklist.manager.ejb.GWFWorkListSessionHome;
import com.gridnode.pdip.base.worklist.manager.ejb.GWFWorkListSessionObject;
import com.gridnode.pdip.framework.util.ServiceLocator;

 public class GWFWorkListClient {

  public static GWFWorkListSessionHome sessionHome;
  public static GWFWorkListSessionObject  sessionObject;
  public GWFWorkListClient() {
   if(sessionHome == null){
    try{
      sessionHome=(GWFWorkListSessionHome)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getHome(GWFWorkListSessionHome.class);
    }catch(Exception ex){
      System.out.println(" Exception in SetUp  : "+ex.getMessage());
    }
   }

  }

 public void InsertWorkListValue(GWFWorkListValueEntity entity){
  try{
    sessionObject = sessionHome.create();
/*    assertNotNull("Session Object Not Null testInsertWorkList",sessionObject);
    GWFWorkListValueEntity entity = new GWFWorkListValueEntity();
    entity.setCreationDate(new Date());
    entity.setUserId("user3");
    entity.setWorkItemDescription("WorkItemDescription Three");
    entity.setWorkItemComments("Comments For WorkItem Description");
*/
    sessionObject.addWorkItem(entity);
  }catch(Exception ex){
    System.out.println(" Exception in WorkListValueRetreive : "+ex.getMessage());
    ex.printStackTrace();
  }

 }


 public void rejectWorkItem(GWFWorkListValueEntity entity){
 try{
        sessionObject = sessionHome.create();
        sessionObject.rejectWorkItem((Long)entity.getKey(),entity.getWorkItemComments());
   }catch(Exception ex){
      System.out.println("testUpdateWorkListValue "+ex.getMessage());
      ex.printStackTrace();
  }

 }

public Vector GetWorkItems(String userName){
 Vector workItems=null;
 try{
      sessionObject = sessionHome.create();
      workItems = new Vector(sessionObject.getWorkItemsByUser(userName));

      Vector testWorkItems = new Vector();
      testWorkItems = workItems;
      for(int i=0;i<workItems.size();i++){
          GWFWorkListValueEntity entity = (GWFWorkListValueEntity)testWorkItems.get(i);
          System.out.println(" User Name ...        "+entity.getUserId());
          System.out.println(" WorkList Description "+entity.getWorkItemDescription());
          System.out.println(" WorkItem Comments    "+entity.getWorkItemComments());
          System.out.println(" Creation Date        "+entity.getCreationDate());

     }

 }catch(Exception ex){
  System.out.println(" Message   "+ex.getMessage());
  ex.printStackTrace();
 }

     return workItems;
}


public void performWorkItems(GWFWorkListValueEntity entity){
try{
    sessionObject = sessionHome.create();
    sessionObject.performWorkItem((Long)entity.getKey());
}catch(Exception ex){
  System.out.println(" Message   "+ex.getMessage());
  ex.printStackTrace();
}

}
/*
public void assignWorkItems(){
try{
    sessionObject = sessionHome.create();
    sessionObject.assignWorkItems();
}catch(Exception ex){
  System.out.println(" Message   "+ex.getMessage());
  ex.printStackTrace();
}
}
*/
}
