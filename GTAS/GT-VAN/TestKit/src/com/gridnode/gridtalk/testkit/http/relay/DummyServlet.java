package com.gridnode.gridtalk.testkit.http.relay;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DummyServlet extends HttpServlet
{

  /**
   * 
   */
  private static final long serialVersionUID = 6546456456561L;
  private File dataFolder = new File("received_data");
  private static int counter;

  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("text/html");
    ServletOutputStream os = response.getOutputStream();
    String msg = "Please use POST";
    os.write(msg.getBytes());
    response.setStatus(HttpServletResponse.SC_OK);
    return;
  }

  /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
  @SuppressWarnings("unchecked")
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
	ServletInputStream is = request.getInputStream();
	String docType = request.getHeader("DocType");
	String docFilename = request.getHeader("DocFilename");
	String sender = request.getHeader("SenderId");
	String recipient = request.getHeader("RecipientId");
	Properties params = new Properties();
	Enumeration e = request.getHeaderNames();
	String key = null;
	while(e.hasMoreElements())
	{
	  key = (String)e.nextElement();
	  params.put(key, request.getHeader(key));
	}
	log("DummyServlet.doPost() docType="+docType+" docFilename="+docFilename);
	saveContent(docType, docFilename, sender, recipient, params, is);
	response.setStatus(HttpServletResponse.SC_ACCEPTED);
  }
  private void saveContent(String docType, String docFilename, String sender, String recipient, Properties params, ServletInputStream is) throws IOException
  {
	log("Received backend POST request:>>> Sender: " + sender + ", Recipient: " + recipient + ", DocType: " + docType + ", DocFilename: " + docFilename);
	if (!dataFolder .isDirectory())
	  dataFolder.mkdirs();
	String uniqueId = getUniqueID();
	File doc = new File(dataFolder, uniqueId + ".xml");
	FileOutputStream fo = new FileOutputStream(doc);
	BufferedOutputStream bos = new BufferedOutputStream(fo);
	byte[] buff = new byte[1024];
	int len = -1;
	while ((len = is.read(buff)) > 0)
	{
	  bos.write(buff, 0, len);
	}
	bos.close();
	fo.close();
	
	log("Written content to file: " + doc.getAbsolutePath());
	File pf = new File(dataFolder, uniqueId + ".properties");
	FileOutputStream out = new FileOutputStream(pf);
	params.store(out, "HTTP headers for " + docFilename);
	out.close();

  }
  private static synchronized String getUniqueID()
  {
	if(counter > 99)
	  {
	  counter = 0;
	  }
	counter++;
	long t = (System.nanoTime() * 100);
	t = t + counter;
	return t + "";
  }
}
