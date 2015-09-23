/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GtasClient.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 12, 2007   i00107              Created
 */

package com.gridnode.gridtalk.httpbc.ishb.senders;

import java.rmi.RemoteException;
import java.util.*;

import com.gridnode.gridtalk.httpbc.common.model.FileContent;
import com.gridnode.gridtalk.httpbc.common.util.IConfigCats;
import com.gridnode.gridtalk.httpbc.common.util.ILogTypes;
import com.gridnode.gridtalk.httpbc.ishb.GtasServiceException;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author i00107
 * @since
 * @version 2.0.3
 * This handles all direct interactions with the GTAS component.
 */
public class GtasClient
{
  private static Hashtable<String,GtasClient> _gtasClients = new Hashtable<String,GtasClient>();
  
  private String _targetId;
  
  private static final String DOC_MGR_JNDI = "gtas.docmgr.jndi";
  
  public static synchronized GtasClient getGtasClient(String targetId)
  {
    GtasClient c = _gtasClients.get(targetId);
    if (c == null)
    {
      c = new GtasClient(targetId);
      _gtasClients.put(targetId, c);
    }
    return c;
  }
  
  private GtasClient(String targetId)
  {
    _targetId = targetId;
  }
  
  /**
   * Sends a document to Gtas.
   * @param senderBeId The send Business entity Id
   * @param partnerId The recipient partner Id
   * @param docType The document type
   * @param doc The The document to send
   * @param attachments The attachments to send with the document.
   * @param tracingId The tracing Id for the imported document
   * @return <b>true</b> if send is successful, <b>false</b> otherwise.
   * @throws GtasServiceException
   */
  public synchronized boolean send(String senderBeId, String partnerId, String docType, 
                           FileContent doc, FileContent[] attachments, String tracingId) throws GtasServiceException
  {
    Logger logger = getLogger();
    String mtd = "send";
    Object[] params = new Object[]{
        senderBeId, partnerId, docType, doc, attachments, tracingId};
    
    logger.logEntry(mtd, params);
    
    try
    {
      GtasLoginConfig props = getGtasLoginProperties();
      
      GtasFileClient fileClient = new GtasFileClient(props);
      
      //prepare the data
      fileClient.storeFile(doc.getContent(), tracingId, doc.getFilename());
      String[] docFile = new String[]{ doc.getFilename() };
      List<String> atts = new ArrayList<String>();
      if (attachments != null && attachments.length > 0)
      {
        for (int i=0; i<attachments.length; i++)
        {
          fileClient.storeFile(attachments[i].getContent(), tracingId, attachments[i].getFilename());
          atts.add(attachments[i].getFilename());
        }
      }
      List partnerList = Arrays.asList(new String[]{partnerId});
      List docList = Arrays.asList(docFile);

      getDocMgr().importDocument(props.getLoginUser(), senderBeId, null, senderBeId, partnerList,
                             docType, docList, atts, props.getClientRootPathKey(), props.getGtasSubPath()+"/"+tracingId+"/",
                             null, null, null, tracingId);
      
      //ImportBackendDocNotification notf = new ImportBackendDocNotification(props.getLoginUser(), senderBeId, null, senderBeId, partnerList,
//                                                                           docType, docList, atts, props.getClientRootPathKey(), props.getGtasSubPath()+"/"+tracingId+"/",
//                                                                           null, null, null, tracingId);
      
      //Notifier.getInstance().broadcast(notf);
      return true;
    }
    catch (Exception ex)
    {
      GtasServiceException gse = new GtasServiceException("Unable to send document to GridTalk", ex);
      throw gse;
    }
    finally
    {
      logger.logExit(mtd, params);
    }
  }
                           
  private IDocumentManagerObj getDocMgr() throws GtasServiceException
  {
    try
    {
      Properties jndiProps = getGtasJndiProperties();
      JndiFinder finder = new JndiFinder(jndiProps);
      String jndiName = jndiProps.getProperty(DOC_MGR_JNDI, "com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome");
      IDocumentManagerHome home = (IDocumentManagerHome)finder.lookup(jndiName, IDocumentManagerHome.class);
      return home.create();
    }
    catch (RemoteException ex)
    {
      GtasServiceException gtse = new GtasServiceException("Unable to obtain document manager", ex.getCause());
      throw gtse;     
    }
    catch (Exception ex)
    {
      GtasServiceException gtse = new GtasServiceException("Unable to login to document manager", ex);
      throw gtse;
    }
  }
  
 
  private Properties getGtasJndiProperties() throws Exception
  {
    return ConfigurationStore.getInstance().getProperties(IConfigCats.GTAS_JNDI);
  }
  
  private GtasLoginConfig getGtasLoginProperties() throws Exception
  {
    Properties props =  ConfigurationStore.getInstance().getProperties(IConfigCats.GTAS_LOGIN+_targetId);
    if (props.isEmpty())
    {
      props = ConfigurationStore.getInstance().getProperties(IConfigCats.GTAS_LOGIN_DEFAULT);
    }
    return new GtasLoginConfig(props);
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_ISHB, "GtasClient");
  }
  
}
