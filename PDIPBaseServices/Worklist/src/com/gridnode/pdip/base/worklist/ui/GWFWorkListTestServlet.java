package com.gridnode.pdip.base.worklist.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.pdip.base.worklist.manager.ejb.GWFWorkListSessionHome;
import com.gridnode.pdip.base.worklist.manager.ejb.GWFWorkListSessionObject;



public class GWFWorkListTestServlet extends HttpServlet {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6031336567286573849L;
public GWFWorkListSessionHome sessionHome;
 public GWFWorkListSessionObject sessionObject;

 public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
  }
  //Process the HTTP Post request
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  System.out.println(" Do Post  :");
      try{
/*
         sessionHome=(GWFWorkListSessionHome)ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(GWFWorkListSessionHome.class);
         sessionObject = sessionHome.create();
         Vector users = GWFUserObserver.getInstance().getAvailableUsers();
         if(users != null){
          System.out.println(" Value of Size from Servlet  "+users.size());
         }
         Vector workItems = sessionObject.assignWorkItems(users);
         GWFServerObserver observer = GWFServerObserver.getInstance();
         observer.fireChangeEvent("Changed",workItems);
*/
    }catch(Exception ex){
      System.out.println(" Exception in SetUp  : "+ex.getMessage());
      ex.printStackTrace();
    }


  }

}
