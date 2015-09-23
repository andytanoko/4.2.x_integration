package com.gridnode.pdip.framework.file;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * <p>Title: PDIP</p>
 * <p>Description: Peer Data Interchange Platform</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode</p>
 * @author Jagadeesh
 * @version 1.0
 */

public class FileServlet extends HttpServlet {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3800940764084785519L;
	private static final String CONTENT_TYPE = "text/vnd.wap.wml";
  private static final String DOC_TYPE = "<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.2//EN\"\n" +
      "  \"http://www.wapforum.org/DTD/wml12.dtd\">";
  //Initialize global variables
  public void init() throws ServletException {
  }
  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request,response);
/*    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    out.println("<?xml version=\"1.0\"?>");
    out.println(DOC_TYPE);
    out.println("<wml>");
    out.println("<card>");
    out.println("<p>The servlet has received a GET. This is the reply.</p>");
    out.println("</card></wml>");
  */
  }
  //Process the HTTP Post request
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  try{
  System.out.println(" In DoPost ..... ");
  ObjectInputStream input = new ObjectInputStream(request.getInputStream());
  FileWrapper wrapper = (FileWrapper)input.readObject();
  if(wrapper != null){
  System.out.println(" Wrapper is Not Null .... : "+wrapper.getString());
  Object obj = wrapper.getArrayList();
  System.out.println("Class name .... :"+obj.getClass().getName());
  Hashtable ht = (Hashtable)obj;
  Enumeration enm = ht.keys();
  //int i=1;
  while(enm.hasMoreElements()){
  String fileName = (String)enm.nextElement();
  BufferedWriter  fos = new BufferedWriter(new FileWriter(fileName));
  String content = (String)ht.get(fileName);
  fos.write(content);
  fos.flush();
  fos.close();
  System.out.println(" File is Written to Server ... with File Name "+fileName);
  }

  }
  input.close();
  /*InputStream stream = request.getInputStream();
  BufferedInputStream in = new BufferedInputStream(stream);
  int i = 0;
  while ((i = in.read()) != -1) {
  System.out.write(i);
  }
  in.close();*/
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    out.println("<?xml version=\"1.0\"?>");
    out.println(DOC_TYPE);
    out.println("<wml>");
    out.println("<card>");
    out.println("<p>The servlet has received a GET. This is the reply.</p>");
    out.println("</card></wml>");



  /*  ObjectInputStream input = new ObjectInputStream(request.getInputStream());
  Object objectInput = input.readObject();
    System.out.println(" Object Instance of .... :"+objectInput.getClass());
    if(objectInput instanceof Hashtable){
    Hashtable inputParam = (Hashtable)objectInput;
    Enumeration enm = inputParam.keys();
        while(enm.hasMoreElements()){
        String fileName = (String)enm.nextElement();
        ByteArrayOutputStream out = (ByteArrayOutputStream)inputParam.get(fileName);
        FileOutputStream filewriter = new FileOutputStream(fileName);
        filewriter.write(out.toByteArray());
        filewriter.flush();
        filewriter.close();
        System.out.println("Written to Servlet And File Name is ... "+fileName);
        }
      }*/
   }catch(Exception ex){
   System.out.println("Error in File Servlet "+ex.getMessage());
   throw new ServletException(ex.getMessage());
   }
  }
  //Clean up resources
  public void destroy() {
  }
}