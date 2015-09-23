/**
 * 
 */
package com.gridnode.gridtalk.tester.loopback;

import com.gridnode.gridtalk.tester.loopback.command.CommandException;
import com.gridnode.gridtalk.tester.loopback.command.impl.ReceiveBackendCommand;
import com.gridnode.gridtalk.tester.loopback.command.impl.SendBackendCommand;
import com.gridnode.gridtalk.tester.loopback.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * @author Alain
 *
 */
public class BackendServlet extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6229214655192185304L;

		/**
     * 
     */
    public BackendServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        /*
         * Send backend document 
         */
        
        String pipCode= request.getParameter("pip");
        try {
            new SendBackendCommand().execute(pipCode);
            
        } catch (CommandException e) {
        	Logger.error(this.getClass().getSimpleName(), "doGet", "Error", e);
            
        }
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       /*
        * Receive backend document
        */
        String content = getContent(request.getInputStream());
        int responseCode = 0;
        try {
            new ReceiveBackendCommand(content).execute(null);
            responseCode = HttpServletResponse.SC_OK;
        } catch (CommandException e) {
        	Logger.warn(this.getClass().getSimpleName(), "doPost", e.getMessage());
        	responseCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        response.setStatus(responseCode);
    }
    
    private String getContent(ServletInputStream is) throws IOException
    {
//      System.out.println("RnifVersion: "+rnifVersion + ", ResponseType is "+synchronous);
      byte[] buff = new byte[1024];
      int len = -1;
      String content = "";
      while ((len = is.read(buff))>0)
      {
        content += new String(buff, 0, len);
      }
//      System.out.print("Content: "+content);
      return content;
    }
    

}
