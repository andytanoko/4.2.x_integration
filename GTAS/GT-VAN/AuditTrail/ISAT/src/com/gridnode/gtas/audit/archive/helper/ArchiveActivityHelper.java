/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveActivityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 27, 2007    Tam Wei Xiang       Created
 * Mar 07 2007		Alain Ah Ming				Refine exceptions being thrown
 * May 23, 2007    Tam Wei Xiang      Support archive by customer
 */
package com.gridnode.gtas.audit.archive.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.ejb.CreateException;
import javax.naming.NamingException;

import com.gridnode.gtas.audit.archive.ArchiveSummary;
import com.gridnode.gtas.audit.archive.IArchiveConstant;
import com.gridnode.gtas.audit.archive.cluster.helper.IArchiveClusterConstant;
import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerLocalHome;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerLocalObj;
import com.gridnode.gtas.audit.dao.BizEntityGroupMappingDAO;
import com.gridnode.gtas.audit.dao.CommonResourceDAO;
import com.gridnode.gtas.audit.model.BizEntityGroupMapping;
import com.gridnode.gtas.audit.model.CommonResource;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerHome;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerObj;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.tracking.helpers.IISATProperty;
import com.gridnode.util.SystemUtil;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.io.IOUtil;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.xml.XMLBeanUtil;

/**
 * This class provide some services for the ArchiveHandler and RestoreHandler.
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveActivityHelper
{
  public ArchiveActivityHelper()
  {
    
  }
  
  /**
   * Get the Archive Summary File given the archiveID
   * @param archiveID the unique identifier that can identify a summary file
   * @return the archive summary file or throw ArchiveTrailDataException if the summary file is not found
   * @throws Exception
   */
  public static File getArchiveSummaryFile(String archiveID) throws Exception
  {
    IAuditTrailArchiveManagerLocalObj archiveMgr = getArchiveMgr();
    String archiveFolderPath = archiveMgr.getArchiveFolderPath();
    File archiveSummaryFile = new File(archiveFolderPath+archiveID+".xml");
    if(archiveSummaryFile.exists() && archiveSummaryFile.isFile())
    {
      return archiveSummaryFile;
    }
    else
    {
      throw new ArchiveTrailDataException("Archive summary path: "+archiveSummaryFile.getAbsolutePath()+" does not exist or is a directory");
    }
  }
  
  public static File getArchiveSummaryFile(String archiveID, String archiveFolderPath) throws Exception
  {
    File archiveSummaryFile = new File(archiveFolderPath+archiveID+".xml");
    if(archiveSummaryFile.exists() && archiveSummaryFile.isFile())
    {
      return archiveSummaryFile;
    }
    else
    {
      throw new ArchiveTrailDataException("Archive summary path: "+archiveSummaryFile.getAbsolutePath()+" does not exist or is a directory");
    }
  }
  
  /**
   * Get the ArchiveSummary entity given the archiveSummary file.
   * @param archiveSummaryFile The archive summary file
   * @return the ArchiveSummary entity or throw ArchiveTrailDataException if the deserialze from the given archiveSummaryFile fail.
   * @throws ArchiveTrailDataException 
   * @throws Exception
   */
  public static ArchiveSummary getArchiveSummary(File archiveSummaryFile) throws ArchiveTrailDataException
  {
    FileInputStream input;
		try
		{
			input = new FileInputStream(archiveSummaryFile);
		}
		catch (FileNotFoundException e)
		{
			String filename = archiveSummaryFile == null? null : archiveSummaryFile.getAbsolutePath(); 
			throw new ArchiveTrailDataException("Summary file not found: " +filename, e);
		}
    byte[] summaryContent;
		try
		{
			summaryContent = IOUtil.read(input);
		}
		catch (IOException e)
		{
			String filename = archiveSummaryFile == null? null : archiveSummaryFile.getAbsolutePath(); 
			throw new ArchiveTrailDataException("IO Error while reading archive summary file: "+filename,e);
		}
		
    ArchiveSummary archiveSummary = (ArchiveSummary) XMLBeanUtil.xmlToBean(new String(summaryContent), ArchiveSummary.class);
    if(archiveSummary != null)
    {
      return archiveSummary;
    }
    else
    {
			String filename = archiveSummaryFile == null? null : archiveSummaryFile.getAbsolutePath(); 
      throw new ArchiveTrailDataException("Failed to deserialize from archiveSummaryFile: "+filename);
    }
  }
  
  /**
   * Get the ArchiveSummary entity given the summary Abs path
   * @param summaryAbsPath Absolute path to the ArchiveSummary file.
   * @return
   * @throws Exception
   */
  public static ArchiveSummary getArchiveSummary(String summaryAbsPath) throws Exception
  {
    File summaryFile = new File(summaryAbsPath);
    return getArchiveSummary(summaryFile);
  }
  
  public static Properties getArchiveJobProperties(String archiveID) throws ArchiveTrailDataException
  {
    try
    {
      IAuditTrailArchiveManagerLocalObj archiveMgr = getArchiveMgr();
      String archiveFolderPath = archiveMgr.getArchiveFolderPath();
      return getArchiveJobProperties(archiveID, archiveFolderPath);
    }
    catch(ArchiveTrailDataException ex)
    {
      throw ex;
    }
    catch(Exception ex)
    {
      throw new ArchiveTrailDataException("Error in getting the archive job properties given archiveID: "+archiveID+". "+ex.getMessage(), ex);
    }
  }
  
  public static Properties getArchiveJobProperties(String archiveID, String archiveFolderPath) throws ArchiveTrailDataException
  {
    try
    {
      File archiveProperties = new File(archiveFolderPath+archiveID+".properties");
      if(archiveProperties.exists() && archiveProperties.isFile())
      {
        FileInputStream input = null;
        
        try
        {
          input = new FileInputStream(archiveProperties);
          Properties pro = new Properties();
          pro.load(input);
          return pro;
        }
        finally
        {
          if(input != null)
          {
            input.close();
          }
        }
      }
      else
      {
        throw new ArchiveTrailDataException("Archive Job Properties "+archiveProperties.getAbsolutePath()+" is not exist or is a directory");
      }
    }
    catch(ArchiveTrailDataException ex)
    {
      throw ex;
    }
    catch(Exception ex)
    {
      throw new ArchiveTrailDataException("Error in getting the archive job properties given archiveID: "+archiveID+". "+ex.getMessage(), ex);
    }
  }
  
  public static void updateArchiveProperties(String archiveID, Properties archiveJobProperties) throws Exception
  {
    IAuditTrailArchiveManagerLocalObj archiveMgr = getArchiveMgr();
    String archiveFolderPath = archiveMgr.getArchiveFolderPath();
    updateArchiveProperties(archiveID, archiveJobProperties, archiveFolderPath);
  }
  
  public static void updateArchiveProperties(String archiveID, Properties archiveJobProperties, String archiveFolderPath) throws Exception
  {
    FileOutputStream out = null;
    try
    {
      File archiveProperties = new File(archiveFolderPath+archiveID+".properties");
      out = new FileOutputStream(archiveProperties);
      archiveJobProperties.store(out, "");
    }
    finally
    {
      if(out != null)
      {
        out.close();
      }
    }
  }
  
  public static void generatePropertiesFile(String folderPath, Properties pro, String id) throws Exception
  {
    File properties = null;
    FileOutputStream out = null;
    try
    {
      properties = new File(folderPath + "/"+id+".properties");
      out = new FileOutputStream(properties);
      pro.store(out, "");
    }
    finally
    {
      if(out != null)
      {
        out.close();
      }
    }
    
  }
  
  public static Properties getPropertiesFile(String folderPath, String id) throws Exception
  {
    FileInputStream in = null;
    
    try
    {
      File properties = new File(folderPath + "/"+ id+ ".properties");
      in = new FileInputStream(properties);
    
      Properties pro = new Properties();
      pro.load(in);
    
      return pro;
    }
    finally
    {
      if(in != null)
      {
        in.close();
      }
    }
  }
  
  public static void obtainArchiveSummaryLock()
  {
    CommonResourceDAO dao = new CommonResourceDAO();
    List list =  dao.lockResource(IISATConstant.TYPE_LOCK, IArchiveConstant.ARCHIVE_SUMMARY_LOCK); 
    if(list != null && list.size() > 0)
    {
      CommonResource cr = (CommonResource)list.iterator().next();
    }
    else
    {
      throw new NullPointerException("Can't find archive lock in table isat_resource with type: "+IISATConstant.TYPE_LOCK+" and code: "+IArchiveConstant.ARCHIVE_SUMMARY_LOCK);
    }
  }
  
  public static IAuditTrailArchiveManagerLocalObj getArchiveMgr() throws Exception
  {
    JndiFinder finder;
		try
		{
			finder = new JndiFinder(null);
		}
		catch (NamingException e)
		{			
			throw new Exception("Failed to initialize JNDI Finder", e);
		}
    IAuditTrailArchiveManagerLocalHome archiveHome = (IAuditTrailArchiveManagerLocalHome)finder.lookup(IAuditTrailArchiveManagerLocalHome.class.getName(), 
                                                                             IAuditTrailArchiveManagerLocalHome.class);
    try
		{
			return archiveHome.create();
		}
		catch (CreateException e)
		{
			throw new Exception("Failed to create Audit Trail Archive Manager EJB", e);
		}
  }
  
  /**
   * Delivered the jms msg to the destination as identified by the given category. All the jms
   * msg will be tagged with the current node id.
   * @param archiveCriteria
   * @param category
   * @throws Exception
   */
  /*
  public static void sendJmsMsg(Hashtable archiveCriteria, String category) throws Exception
  {
    JmsSender jmsSender = new JmsSender();
    Properties p = null;
		try
		{
			p = getJMSSenderProperties(category);
		}
		catch (RemoteException e)
		{
			throw new Exception("EJB Invocation Error. Unable to retrieve JMS sender properties", e);
		}
		catch (NamingException e)
		{
			throw new Exception("JNDI Error. Unable to retrieve JMS sender properties", e);
		}
		catch (CreateException e)
		{
			throw new Exception("EJB Creation Error. Unable to retrieve JMS sender properties", e);
		}
		jmsSender.setSendProperties(p);
		
    //Hashtable msgSelector = getJmsMsgProps();
    jmsSender.send(archiveCriteria);
  }*/
  
  public static Hashtable getJmsMsgProps()
  {
    //tag with current node id
    Hashtable msgProps = new Hashtable();
    msgProps.put(SystemUtil.HOSTID_PROP_KEY, SystemUtil.getHostId());
    //System.out.println("Delegating request to GT archival: Getting jms host ID: "+SystemUtil.getHostId());
    
    return msgProps;
  }
  
  public static Properties getJMSSenderProperties(String category) throws RemoteException, NamingException, CreateException
  {
    IAuditPropertiesManagerObj propertiesMgr = getPropertiesMgr();
    Properties pro = propertiesMgr.getAuditProperties(category);
    return pro;
  }
  
  private static IAuditPropertiesManagerObj getPropertiesMgr() throws NamingException, RemoteException, CreateException
  {
    JndiFinder finder = new JndiFinder(null);
    IAuditPropertiesManagerHome home = (IAuditPropertiesManagerHome)finder.lookup(IAuditPropertiesManagerHome.class.getName(),
                                                                                  IAuditPropertiesManagerHome.class);
    return home.create();
  }
  
  /**
   * Serialize the ArchiveSummary entity into a file
   * @param archiveSummary The ArchiveSummary entity
   * @param summaryFile The file that we will serialize the ArchiveSummary entity to
   * @return The summaryFile obj
   * @throws Exception 
   * @throws IOException 
   * @throws Exception
   */
  public static File generateArchiveSummaryFile(ArchiveSummary archiveSummary, File summaryFile) throws Exception
  {
    String archiveSummaryInXML = XMLBeanUtil.beanToXml(archiveSummary, ArchiveSummary.class.getSimpleName());
    try
		{
			IOUtil.write(summaryFile, archiveSummaryInXML.getBytes());
		}
		catch (IOException e)
		{
			String filename = summaryFile != null? summaryFile.getAbsolutePath(): null;
			throw new Exception("IO Error while writing summary file: "+filename, e);
		}
    return summaryFile;
  }
  
  public static Collection<String> convertStrToList(String zipFilenameConcat, String delim)
  {
    ArrayList<String> list = new ArrayList<String>();
    
    if(zipFilenameConcat != null)
    {
      StringTokenizer st = new StringTokenizer(zipFilenameConcat, delim);
      while(st.hasMoreTokens())
      {
        list.add(st.nextToken());
      }
    }
    return list;
  }
  
  public static String convertListToStr(List<String> zipFileList, String delim)
  {
    String concatStr = "";
    if(zipFileList == null)
    {
      return concatStr;
    }
    
    for(int i = 0; i < zipFileList.size(); i++)
    {
      concatStr += zipFileList.get(i)+delim;
    }
    
    if(zipFileList.size() > 0 && concatStr.lastIndexOf(delim) == (concatStr.length() -1))
    {
      concatStr= concatStr.substring(0, concatStr.lastIndexOf(delim));
    }
    return concatStr;
  }
  
  public static String convertArrToStr(String[] zipFileList, String delim)
  {
    String concatStr = "";
    if(zipFileList == null) return "";
    
    for(int i = 0; i < zipFileList.length; i++)
    {
      concatStr += zipFileList[i]+delim;
    }
    
    if(zipFileList.length > 0 && concatStr.lastIndexOf(delim) == (concatStr.length() -1))
    {
      concatStr= concatStr.substring(0, concatStr.lastIndexOf(delim));
    }
    return concatStr;
  }
  
  /**
   * Get the stack trace in String given the Exception.
   * @param t
   * @return
   */
  public static String getStackTrace(Exception t)
  {
    String stackTrace = null;
    if (t != null)
    {
      StringWriter s = new StringWriter();
      t.printStackTrace(new PrintWriter(s));
      stackTrace = s.toString();
    }
    else
      stackTrace = "";

    return stackTrace;
  }
  
  /**
   * Format the Date obj based on the given datePattern 
   * @param d
   * @param datePattern
   * @return
   */
  public static String formatDateToString(Date d, String datePattern)
  {
    if(d == null) return "";
    
    SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
    return dateFormat.format(d);
  }
  
  /**
   * Get the date pattern
   * @return the date pattern in DB or a default 'yyyy-MM-dd HH:mm:ss.SSS Z' will be returned if no value exist in DB.
   */
  public static String getDatePattern()
  {
    ConfigurationStore configStore = ConfigurationStore.getInstance();
    return configStore.getProperty(IISATProperty.CATEGORY, IISATProperty.EMAIL_ALERT_DATE_PATTERN, "yyyy-MM-dd HH:mm:ss.SSS Z");
  }
  
  public static ArrayList<String> mapGroupToCustomerID(String groups)
  {
    Collection<String> groupInList = convertStrToList(groups, ";");
    ArrayList<String> customerList = new ArrayList<String>();
    
    if(groupInList != null && groupInList.size() > 0)
    {
      BizEntityGroupMappingDAO dao = new BizEntityGroupMappingDAO();
      Collection<BizEntityGroupMapping> bizEntities = dao.getBizEntityGroupMappingsByGroups(groupInList);
      if(bizEntities != null && bizEntities.size() > 0 )
      {
        for(Iterator<BizEntityGroupMapping> i = bizEntities.iterator(); i.hasNext(); )
        {
          BizEntityGroupMapping mapping = i.next();
          customerList.add(mapping.getBeID());
        }
        return customerList;
      }
    }
    return null;
  }
  
  public static void isAllGroupExist(String groups) throws ArchiveTrailDataException
  {
    Collection<String> groupInList = convertStrToList(groups, ";");
    if(groupInList != null && groupInList.size() > 0)
    {
      BizEntityGroupMappingDAO dao = new BizEntityGroupMappingDAO();
      Collection<BizEntityGroupMapping> bizEntities = dao.getBizEntityGroupMappingsByGroups(groupInList);
      
      if(bizEntities == null || (groupInList.size() != bizEntities.size()))
      {
        String groupToArchive = groups;
        String availableGroup = "";
        
        if(bizEntities != null)
        {
          for(BizEntityGroupMapping bizEntity : bizEntities)
          {
            availableGroup += ";"+ bizEntity.getGroupName();
          }
        }
        
        throw new ArchiveTrailDataException("Some group is missing. Please ensure the group is exist. Group to archive: "+groupToArchive+". Available group: "+availableGroup);
      }
    }
  }
  
  public static List<String> getAllGroup()
  {
    BizEntityGroupMappingDAO dao = new BizEntityGroupMappingDAO();
    Collection<BizEntityGroupMapping> bizEntities = dao.getAllBizEntityGroupMapping();
    
    ArrayList<String> groups = new ArrayList<String>();
    if(bizEntities != null)
    {
      Iterator<BizEntityGroupMapping> ite = bizEntities.iterator();
      while(ite.hasNext())
      {
        BizEntityGroupMapping groupMapping = ite.next();
        groups.add(groupMapping.getGroupName());
      }
    }
    else
    {
      throw new NullPointerException("No biz entity group mapping can be found");
    }
    return groups;
  }
  
  public static boolean isEnabledClustered()
  {
    String isNodeEnabled = System.getProperty(IArchiveClusterConstant.NODE_CLUSTERED);
    if(isNodeEnabled == null )
    {
      return false;
    }
    else
    {
      return new Boolean(isNodeEnabled);
    }
  }
}
