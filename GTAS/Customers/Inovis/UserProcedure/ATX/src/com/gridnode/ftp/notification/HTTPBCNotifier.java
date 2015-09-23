/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: HTTPBCNotifier.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 9, 2009   <Developer Name>       Created
 */
package com.gridnode.ftp.notification;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import com.gridnode.gridtalk.httpbc.common.model.FileContent;
import com.gridnode.gridtalk.httpbc.common.model.TransactionDoc;
import com.gridnode.gridtalk.httpbc.common.util.IJndiNames;
import com.gridnode.gridtalk.httpbc.ishb.ejb.ITransactionHandler;
import com.gridnode.gridtalk.httpbc.ishb.ejb.ITransactionHandlerHome;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.LoggerManager;

/**
 * This class notify the HTTPBC module to receive a document.
 * @author <developer name>
 * @version
 * @since
 */
public class HTTPBCNotifier
{
  private String _jndPropsFile;
  
  public HTTPBCNotifier(String jndPropsFile)
  {
    _jndPropsFile = jndPropsFile;
  }
  
  /**
   * Push doc to HTTPBC for handling backend import case, and with appropriate tracing event be kept tracked in TXMR
   * @param filename
   * @param content
   * @param custBeID
   * @param partnerID
   * @param docType
   * @throws Exception
   */
  public void notifyHTTPBCToReceive(String filename, byte[] content, String custBeID, String partnerID, String docType) throws Exception
  {
    String tracingId = generateTracingId();
    FileContent fc = new FileContent(filename, content);
    TransactionDoc tDoc = new TransactionDoc(tracingId, partnerID, custBeID, docType, fc, null, TransactionDoc.DIRECTION_OUT);
    tDoc.setTimestamp(System.currentTimeMillis());
    getTransactionHandlerMgr().handleOutgoingTransaction(tDoc);
  }
  
  public void notifyHTTPBCToReceive(String filename, String attachmentFilename, byte[] content, byte[] attachmentContent, String custBeID, String partnerID, String docType) throws Exception
  {	  
    String tracingId = generateTracingId();
    FileContent fc = new FileContent(filename, content);
    FileContent[] attachmentFCs = new FileContent[1];
    FileContent attachmentFC = new FileContent(attachmentFilename, attachmentContent);
    
    attachmentFCs[0] = attachmentFC;
    TransactionDoc tDoc = new TransactionDoc(tracingId, partnerID, custBeID, docType, fc, attachmentFCs, TransactionDoc.DIRECTION_OUT);
    tDoc.setTimestamp(System.currentTimeMillis());
    getTransactionHandlerMgr().handleOutgoingTransaction(tDoc);
  }
  
  private ITransactionHandler getTransactionHandlerMgr() throws Exception
  {
    JndiFinder jndi = new JndiFinder(loadJndiProps(), LoggerManager.getOneTimeInstance());
    ITransactionHandlerHome home = (ITransactionHandlerHome)jndi.lookup("ISHB_TransactionHandlerBean", ITransactionHandlerHome.class);
    return home.create();
  }
  
  private String generateTracingId()
  {
    return UUID.randomUUID().toString();
  }
  
  public String getJndPropsFile()
  {
    return _jndPropsFile;
  }

  public void setJndPropsFile(String lookupProps)
  {
    _jndPropsFile = lookupProps;
  }

  private Properties loadJndiProps() throws IOException
  {
    File props = new File(getJndPropsFile());
    Properties jndiProps = new Properties();
    jndiProps.load(new FileInputStream(props));
    return jndiProps;
  }
  
  public static void main(String[] args) throws Exception
  {
    InitialContext context = new InitialContext(getProps());
    Object obj = context.lookup("ISHB_TransactionHandlerBean");
    ITransactionHandlerHome home = (ITransactionHandlerHome)PortableRemoteObject.narrow(obj, ITransactionHandlerHome.class);
    ITransactionHandler handler = home.create();
    handler.deliverOutgoingTransaction(1, 1, 1);
    System.out.println("Deliver completed");
  }
  
  private static Properties getProps()
  {
    Properties props = new Properties();
    props.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory" ) ;
    props.setProperty("java.naming.provider.url","alfgtappt01:1100") ;
    props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces" ) ; 
    return props;
  }
}
