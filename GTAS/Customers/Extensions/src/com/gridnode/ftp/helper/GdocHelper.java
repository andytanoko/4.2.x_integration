/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GdocHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 11, 2009   Tam Wei Xiang       Created
 * Mar 19, 2009   Tam Wei Xiang       #134 : Add in prefix, suffix, msgID as part of
 *                                           the filename substitude list
 * May 04, 2009   Tam Wei Xiang       #150: support for linking the OB XML and the 
 *                                          corresponding IB ACK.
 */
package com.gridnode.ftp.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Properties;

import com.gridnode.ext.util.DocumentUtil;
import com.gridnode.ext.util.FileUtil;
import com.gridnode.ext.util.exceptions.ILoggerConstant;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.IGridDocument;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author <developer name>
 * @version
 * @since
 */
public class GdocHelper
{
  
  public static final String RN_ACK_DOC_TYPE = "RN_ACK";
  public static final String RN_EXCEPTION_TYPE = "RN_EXCEPTION";
  public static final String FOLDER_INBOUND = IGridDocument.FOLDER_INBOUND;
  public static final String FOLDER_OUTBOUND = IGridDocument.FOLDER_OUTBOUND;
  public static final String FOLDER_EXPORT = IGridDocument.FOLDER_EXPORT;
  public static final String FOLDER_IMPORT = IGridDocument.FOLDER_IMPORT;
  
  private String _jndiProPath;
  private Logger _logger;
  
  public GdocHelper()
  {
    _logger = getLogger();
  }
  
  public GdocHelper(String jndiPropPath)
  {
    this();
    _jndiProPath = jndiPropPath;
  }
  
  public String getJndiPropPath()
  {
    return _jndiProPath;
  }
  
  public void setJndiPropPath(String jndiPropPath)
  {
    _jndiProPath = jndiPropPath;
  }
  
  /**
   * Get the formatted filename expected in ATX.
   * @param udoc
   * @param gdocID
   * @param folder
   * @param documentType
   * @param processInstanceUID
   * @param filenameFormat The filename format expected in ATX.
   * @param filenameAppend The additional filename we will append to the filename we store in ATX  
   * @return
   * @throws Exception
   */
  public Object[] getSubstitutionResourcesList(File udoc, String gdocID, String folder, String documentType, Long processInstanceUID,
                                  String filenameFormat, String filenameAppend, String tracingID, String msgID,
                                  String embeddedContentTagFormat) throws Exception
  {
    String method = "getActualFilename";
    if(filenameFormat == null && embeddedContentTagFormat == null)
    {
      return null;
    }
    
    String filenameExtension = FileUtil.getFileExtension(udoc);
    String udocName = udoc.getName();
    String filename = FileUtil.getFilenameWithoutExtension(udocName);
    String srcUdocFilename = "";
    String srcMessageID = "";
    
    GridDocument srcGdoc = getSourceGdoc(gdocID, folder, tracingID);
    if(srcGdoc != null)
    {
      srcUdocFilename = srcGdoc.getUdocFilename();
      srcMessageID = DocumentUtil.getMessageID(srcGdoc.getFolder(), srcGdoc.getGdocId()+"");
    }
    
    String prefix = "";
    String suffix = "";
    
    //#134 The udoc may go through transformation, conversion or splitting. In order for ATX side to easily identified the original filename
    //of the imported udoc from ATX or the first received doc from TP (The RN XML doc), we will allow to include the prefix
    // and suffix into the filename we sent to ATX.
    if(srcUdocFilename != null && srcUdocFilename.length() >= 0)
    {
      srcUdocFilename = FileUtil.getFilenameWithoutExtension(srcUdocFilename);
      prefix = getFilePrefix(srcUdocFilename, filename);
      suffix = getFileSuffix(srcUdocFilename, filename);
    }
    
    //see /conf/ftpBackend.properties for the explanation of each substitude variable
    Object[] substitudeList = new Object[]{filenameAppend, filename, filenameExtension, "", prefix, 
                                           suffix, msgID, srcUdocFilename == null? "": srcUdocFilename,
                                           srcMessageID, ""};
    
    //  For the IB ACK, we need to tie back to the OB udoc we sent to ATX
    String atxOBUdocFilename = "";
    String atxOBMessageID = "";
    if( (GdocHelper.FOLDER_INBOUND.equals(folder) || GdocHelper.FOLDER_EXPORT.equals(folder) )&& 
            (GdocHelper.RN_ACK_DOC_TYPE.equals(documentType) || GdocHelper.RN_EXCEPTION_TYPE.equals(documentType)) && processInstanceUID !=null)
    {
      GridDocument obGdoc = getOBGdoc(processInstanceUID, folder, gdocID, getJndiPropPath());
      atxOBUdocFilename = obGdoc.getUdocFilename();
      substitudeList[3] = atxOBUdocFilename; 
      
      //#150 TWX 04052009
      atxOBMessageID = DocumentUtil.getMessageID(obGdoc.getFolder(), obGdoc.getGdocId()+"");
      substitudeList[9] = atxOBMessageID;
    }

    _logger.debugMessage(method, null, "substitude list filenameExtension:"+filenameExtension+" udocName:"+udocName
                       +" filename:"+filename+" OB udoc to atx:"+atxOBUdocFilename+" prefix:"+prefix+" suffix:"+suffix+" msgID: "+msgID+" srcUdoc:"+srcUdocFilename+" srcMsgID: "+srcMessageID+" atxOBMessageID: "+atxOBMessageID);
    
    //return formatFilename(substitudeList, filenameFormat);
    return substitudeList;
  }
  
  public static String formatFilename(Object[] substitudeList, String filenameFormat)
  {   
      return MessageFormat.format(filenameFormat, substitudeList);
  }
  
  /**
   * Retrieve the source gdoc given tracingID. It will contain the original udoc filename 
   * (without may go through transformation, conversion, and split.)
   * 
   * The source gdoc will be in the IB or IP folder.
   * 
   * @param gdocID
   * @param folder
   * @return
   */
  public GridDocument getSourceGdoc(String gdocID, String folder, String tracingID) throws Exception
  {
    String method = "getSourceGdoc";
    try
    {
      Properties props = getJMSProps(getJndiPropPath());
      IDocumentManagerObj manager = getDocumentMgr(props);
      
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(filter.getAndConnector(), IGridDocument.TRACING_ID, filter.getEqualOperator(), tracingID, false);
      filter.addOrderField(IGridDocument.UID, true);
      
      Collection gdocList = manager.findGridDocuments(filter);
      if(gdocList != null && gdocList.size() > 0)
      {
        GridDocument srcGdoc = (GridDocument)gdocList.iterator().next();
        
        _logger.debugMessage(method, null, "Source gdoc retrieved with gdocID:"+srcGdoc.getGdocId()+" folder: "+srcGdoc.getFolder() +" udocFilename: "+srcGdoc.getUdocFilename());
        
        return srcGdoc;
      }
      else
      {
        return null;
      }
    }
    catch(Exception ex)
    {
      throw new Exception("Error in retrieving raw filename given tracingID:"+tracingID, ex);
    }
  }
  
  /**
   * Locate the OB GDoc that corresponding to the given processInstanceUID
   * @param processInstanceUID UID of the process instance that is related to gdoc that identified by the given gdocID and folder
   * @param folder The inbound folder
   * @param gdocID the IB gdoc id
   * @param jndiProps The path to the jndi properties
   * @return
   * @throws Exception
   */
  public static GridDocument getOBGdoc(Long processInstanceUID, String folder, String gdocID, String jndiProps) throws Exception
  {
    if(processInstanceUID == null)
    {
      throw new Exception("Expecting process instance uid given folder:"+folder+" gdocID:"+gdocID);
    }
    Properties props = getJMSProps(jndiProps);
    IDocumentManagerObj manager = getDocumentMgr(props);
    IDataFilter filter = new DataFilterImpl();
    
    filter.addSingleFilter(null, IGridDocument.PROCESS_INSTANCE_UID, filter.getEqualOperator(), processInstanceUID, false);
    filter.addSingleFilter(filter.getAndConnector(), IGridDocument.FOLDER, filter.getEqualOperator(), IGridDocument.FOLDER_OUTBOUND, false);
    filter.addOrderField(IGridDocument.G_DOC_ID, true); //sort gdocID in asc order
    
    Collection<GridDocument> gdocList = manager.findGridDocuments(filter);
    if(gdocList != null && gdocList.size() > 0)
    {
      GridDocument gdoc = (GridDocument)gdocList.iterator().next();
      Long refGdocID = gdoc.getRefGdocId();
      
      /*
      //Get the corresponding import gdoc
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, IGridDocument.TRACING_ID, filter.getEqualOperator(), tracingID, false);
      filter.addSingleFilter(filter.getAndConnector(), IGridDocument.FOLDER, filter.getEqualOperator(), IGridDocument.FOLDER_IMPORT, false);
      */
      
      filter = new DataFilterImpl();
      filter.addSingleFilter(null, IGridDocument.G_DOC_ID, filter.getEqualOperator(), refGdocID , false);
      filter.addSingleFilter(filter.getAndConnector(), IGridDocument.FOLDER, filter.getEqualOperator(), IGridDocument.FOLDER_OUTBOUND, false);
      
      gdocList = manager.findGridDocuments(filter);
      if(gdocList != null && gdocList.size() > 0)
      {
        GridDocument obGdocToATX = (GridDocument)gdocList.iterator().next(); //retrieve the OB udoc filename we push to ATX
        return obGdocToATX; //ATX OB scenario--> Exit To OB, SaveToFolder, Push to ATX, Exit channel, Exit WF
      }
      else
      {
        return gdoc;//normal OB scenario--> Exit To OB, Exit channel, Push to ATX, Exit WF
      }
    }
    else
    {
      throw new Exception("Can't find any OB gdoc given IB gdocID: "+gdocID+" with process instance UID: "+processInstanceUID+". We can't locate import gdoc !");
    }
  }
  
  private static String getFilePrefix(String srcFilename, String destFilename)
  {
    int index = destFilename.indexOf(srcFilename); 
    if( index >=0 )
    {
      return destFilename.substring(0, index);
    }
    else
    {
      return "";
    }
  }
  
  private static String getFileSuffix(String srcFilename, String destFilename)
  {
    int index = destFilename.indexOf(srcFilename);
    if(index >= 0)
    {
      return destFilename.substring(index+ srcFilename.length(), destFilename.length());
    }
    else
    {
      return "";
    }
  }
  
  private static IDocumentManagerObj getDocumentMgr(Properties jndiProps) throws Exception
  {
    JndiFinder finder = new JndiFinder(jndiProps, LoggerManager.getOneTimeInstance());
    IDocumentManagerHome home = (IDocumentManagerHome)finder.lookup(IDocumentManagerHome.class.getName(), IDocumentManagerHome.class);
    return home.create();
  }
  
  private static Properties getJMSProps(String jndiProps) throws IOException
  {
    Properties jmsProps = new Properties();
    jmsProps.load(new FileInputStream(jndiProps));
    return jmsProps;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getOneTimeInstance().getLogger(ILoggerConstant.LOG_TYPE, "GdocHelper");
  }
}
