package com.gridnode.pdip.base.worklist.ui;


import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gridnode.pdip.base.worklist.GWFUserObserver;
import com.gridnode.pdip.base.worklist.entities.model.GWFWorkListValueEntity;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */

public class GWFWorkListServlet extends HttpServlet {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2028305795952279866L;

	//private static final String CONTENT_TYPE = "text/html";
  //Initialize global variables
  public void init() throws ServletException {
  GWFWorkListServer server = new GWFWorkListServer(8189);
  new Thread(server).start();
  }
  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
/*    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<head><title>GWFWorkListServlet</title></head>");
    out.println("<body>");
    out.println("<p>The servlet has received a GET. This is the reply.</p>");
    out.println("</body></html>");
  */
  }
  //Process the HTTP Post request
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  System.out.println(" Do Post  Main :");
  //Enumeration enm = request.getParameterNames();

  String action = request.getParameter("reqAction");
  System.out.println("Request Action is "+action);
  if(action == null||action.equals("null")){
  System.out.println("Request Action is Null ");
  String userName = request.getParameter("user");
  String passWord = request.getParameter("password");
  if( userName == null){
    userName = (String)request.getAttribute("user");
  }
  GWFUserObserver.getInstance().setUser(userName);
  System.out.println("Added to User Observer ... ");
  System.out.println(" User Name is  "+userName);
  System.out.println(" Password is "+passWord);
  GWFWorkListClient client = new GWFWorkListClient();
  //client.assignWorkItems();
  Vector workItems = client.GetWorkItems(userName);
  if(workItems != null)
  System.out.println(" WorkItems not Null ");
  request.setAttribute("user",userName);
  request.setAttribute("workitems",convertWorkitemsTable(workItems));
  //RequestDispatcher rd = request.getRequestDispatcher("/GWFWorkListJSP.jsp");
  //rd.forward(request,response);
  getServletContext().getRequestDispatcher("/GWFWorkListJSP.jsp").forward(request,response);
  }
  if(action.equals("Execute")){
  Enumeration enm = request.getParameterNames();
  GWFWorkListClient client = new GWFWorkListClient();
  while(enm.hasMoreElements()){
  String name = (String)enm.nextElement();
  String value = request.getParameter(name);
  System.out.println(" Name is "+name+" Value is "+value);
  if(name.startsWith("uid")){
      GWFWorkListValueEntity entity = new GWFWorkListValueEntity();
      String val = request.getParameter(name);
      System.out.println(" Value Removed is  "+val);
      Long l = new Long(val);
      entity.setUId(l.longValue());
      client.performWorkItems(entity);
      System.out.println("Successfully Removed the ID ... "+l);
  }
 }
 String userName = request.getParameter("user");
  Vector workItems = client.GetWorkItems(userName);
  if(workItems != null){
  System.out.println(" WorkItems not Null ");
  request.setAttribute("user",userName);
  request.setAttribute("workitems",convertWorkitemsTable(workItems));
  getServletContext().getRequestDispatcher("/GWFWorkListJSP.jsp").forward(request,response);
  }

}

  if(action.equals("Reject")){
  Enumeration enm = request.getParameterNames();
  GWFWorkListClient client = new GWFWorkListClient();
  while(enm.hasMoreElements()){
  String name = (String)enm.nextElement();
  String value = request.getParameter(name);
  System.out.println(" Name is "+name+" Value is "+value);
  if(name.startsWith("uid")){
      GWFWorkListValueEntity entity = new GWFWorkListValueEntity();
      String val = request.getParameter(name);
      System.out.println(" Value Removed is  "+val);
      Long l = new Long(val);
      entity.setUId(l.longValue());
      entity.setWorkItemComments(request.getParameter("rejectNote"));
      System.out.println("Reject Note "+entity.getWorkItemComments());
      client.rejectWorkItem(entity);
      System.out.println("Successfully Rejected the ID ... "+l);
  }
 }
 String userName = request.getParameter("user");
  Vector workItems = client.GetWorkItems(userName);
  if(workItems != null){
  System.out.println(" WorkItems not Null ");
  request.setAttribute("user",userName);
  request.setAttribute("workitems",convertWorkitemsTable(workItems));
  getServletContext().getRequestDispatcher("/GWFWorkListJSP.jsp").forward(request,response);

}






  }
  /*    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<head><title>GWFWorkListServlet</title></head>");
    out.println("<body>");
    out.println("<p>The servlet has received a POST. This is the reply.</p>");
    out.println("</body></html>");
  */
  }
  //Clean up resources

  private String convertWorkitemsTable(Vector workitems){
    StringBuffer sb=new StringBuffer();
    for(int i=0;workitems!=null && i<workitems.size();i++){
        GWFWorkListValueEntity entity = (GWFWorkListValueEntity)workitems.get(i);
        sb.append("<tr>");
            sb.append("<td><input type=\"checkbox\" name=\"uid").append(i).append("\" value=\"").append(entity.getUId()).append("\">");
            sb.append("<td>").append(entity.getWorkItemDescription()).append("</td>");
            sb.append("<td>").append(entity.getWorkItemComments()).append("</td>");
            sb.append("<td>").append(entity.getCreationDate()).append("</td>");
        sb.append("</tr>");
    }
    return sb.toString();
  }

  public void destroy() {
  }
}
