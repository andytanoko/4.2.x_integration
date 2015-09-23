package com.gridnode.pdip.base.worklist.test;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */

import com.gridnode.pdip.framework.j2ee.*;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.db.entity.*;

import junit.framework.*;
import java.util.*;
import javax.naming.*;
import com.gridnode.pdip.base.worklist.entities.model.GWFIWorkListUserEntity;
import com.gridnode.pdip.base.worklist.entities.model.GWFWorkListUserEntity;
import com.gridnode.pdip.base.worklist.entities.ejb.*;



 public class GWFWorkListUserTestCase extends TestCase{

 private GWFWorkListUserEntityHome entityHome;
 private GWFWorkListUserEntityObject entityObject;

  public GWFWorkListUserTestCase(String name) {
   super(name);
  }

  public static Test suite() {
    	return new TestSuite(GWFWorkListUserTestCase.class);
    }

  public void setUp(){
    try{
      entityHome=(GWFWorkListUserEntityHome)ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(GWFWorkListUserEntityHome.class);
      System.out.println("Object Found ... ");
    }catch(Exception ex){
      System.out.println(" Exception in SetUp  : "+ex.getMessage());
    }
  }

  public void testWorkListValueCreate(){
   try{
      System.out.println("In Test1 Case ");
       GWFWorkListUserEntity valueEntity = new GWFWorkListUserEntity();
      valueEntity.setWorkItemID(new Long(10));
      valueEntity.setUserID("user2");
      System.out.println("After Test Case ");
      entityObject = entityHome.create(valueEntity);
      assertNotNull("WorkListEntity Value NULL in GWFWorkListUserTestCase",entityObject);
      valueEntity = (GWFWorkListUserEntity)entityObject.getData();
      System.out.println("User Name "+valueEntity.getUserID());
      System.out.println("WorkItem ID "+valueEntity.getWorkItemID());

     }catch(Exception ex){
      System.out.println("testWorkListValueCreate  "+ex.getMessage());
      ex.printStackTrace();
    }

  }

  public void testWorkListValuefindByFilter(){
   try{
        IDataFilter filter=new DataFilterImpl();
        filter.addSingleFilter(null,GWFIWorkListUserEntity.USER_ID,filter.getEqualOperator(),"user2",false);
        Collection col = entityHome.findByFilter(filter);
        assertNotNull("WorkListUserEntity Value NULL in GWFWorkListTestCase FindBy Filter",col);
        Iterator iterator=col.iterator();
        while(iterator.hasNext()){
            GWFWorkListUserEntity valueEntity = new GWFWorkListUserEntity();
            entityObject=(GWFWorkListUserEntityObject)iterator.next();
            valueEntity =(GWFWorkListUserEntity)entityObject.getData();
            System.out.println("From Filter User Name "+valueEntity.getUserID());
            System.out.println("WorkItem ID "+valueEntity.getWorkItemID());
        }

   }catch(Exception ex){
      System.out.println("testWorkListValuefindByFilter  Fail "+ex.getMessage());
      ex.printStackTrace();
   }
  }


  public static void main(String args[]) throws Exception{

          junit.textui.TestRunner.run (suite());

  }


}



