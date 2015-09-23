package com.gridnode.gtas.client.web.error;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
public class DirectInvokeView extends HttpServlet
{
  private static final String CONTENT_TYPE = "text/html";
  /**Initialize global variables*/
  public void init(ServletConfig config) throws ServletException
  {
    super.init(config);
  }
  /**Process the HTTP Get request*/
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType(CONTENT_TYPE);
    PrintWriter out = response.getWriter();
    //@todo: fancy layout and i18n
    out.println("<html>");
    out.println("<head><title>View Error!</title></head>");
    out.println("<body>");
    out.println("<h1>View components may not be invoked directly</h1>");
    out.println("Please login and use the provided options to navigate.");
    out.println("<a href=\"" + request.getContextPath() + "/index.html\">Login page</a>");
    out.println("</body></html>");
  }
  /**Process the HTTP Post request*/
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    doGet(request, response);
  }
  /**Clean up resources*/
  public void destroy()
  {
  }
}