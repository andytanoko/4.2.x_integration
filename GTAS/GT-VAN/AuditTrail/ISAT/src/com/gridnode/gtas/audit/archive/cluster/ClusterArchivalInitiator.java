/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ClusterArchivalInitiator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 28, 2007   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.cluster;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Properties;

import javax.ejb.CreateException;
import javax.management.ObjectName;
import javax.naming.NamingException;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;

import com.gridnode.gtas.audit.archive.IArchivalInitiator;
import com.gridnode.gtas.audit.archive.IArchiveConstant;
import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerHome;
import com.gridnode.gtas.audit.properties.facade.ejb.IAuditPropertiesManagerObj;
import com.gridnode.util.jndi.JndiFinder;

/**
 * This class serves as the gateway to the ArchiveServiceMBean. 
 * @author Tam Wei Xiang
 * @version GT 4.1.2 (GTVAN)
 */
public class ClusterArchivalInitiator implements IArchivalInitiator
{

  /* (non-Javadoc)
   * @see com.gridnode.gtas.audit.archive.IArchivalInitiator#checkArchiveStatus()
   */
  public void checkArchiveStatus() throws Exception
  {
    String mbeanName = getArchiveSingletonMBeanName();
    String rmiAdaptorLookupName = getRMIAdaptorName();
    Properties jndiLookup = getJndiLookupProperties();
    
    System.out.println("checkArchiveStatus() Extracted props for looking up archives singleton : archiveMBean name: "+mbeanName+" rmi adaptor: "+rmiAdaptorLookupName+" jndilookup props "+jndiLookup);
    
    JndiFinder finder = new JndiFinder(jndiLookup);
    RMIAdaptor rmiAdaptor = (RMIAdaptor) finder.lookup(rmiAdaptorLookupName);
    
    if( (rmiAdaptor != null) && (rmiAdaptor.isRegistered(new ObjectName(mbeanName))) ) 
    {
      rmiAdaptor.invoke( new ObjectName(mbeanName), "checkArchiveStatus", null, null);
    }
    else
    {
      throw new ArchiveTrailDataException("Archival failed to be initiated !");
    }
   
  }

  /* (non-Javadoc)
   * @see com.gridnode.gtas.audit.archive.IArchivalInitiator#initArchive(java.util.Hashtable)
   */
  public void initArchive(Hashtable criteria) throws Exception
  {
    String mbeanName = getArchiveSingletonMBeanName();
    String rmiAdaptorLookupName = getRMIAdaptorName();
    Properties jndiLookup = getJndiLookupProperties();
    
    System.out.println("initArchive() Extracted props for looking up archives singleton : archiveMBean name: "+mbeanName+" rmi adaptor: "+rmiAdaptorLookupName+" jndilookup props "+jndiLookup);
    
    JndiFinder finder = new JndiFinder(jndiLookup);
    RMIAdaptor rmiAdaptor = (RMIAdaptor) finder.lookup(rmiAdaptorLookupName);
    
    if( (rmiAdaptor != null) && (rmiAdaptor.isRegistered(new ObjectName(mbeanName))) ) 
    {
      rmiAdaptor.invoke( new ObjectName(mbeanName), "archive", new Object[]{criteria}, new String[]{"java.util.Hashtable"});
      //String masterID = (String)rmiAdaptor.invoke( new ObjectName(mbeanName), "getNodeID", null, null);
      //String previousMaster = (String)rmiAdaptor.invoke(new ObjectName(mbeanName), "getPreviousMaster", null, null);
      //rmiAdaptor.invoke(new ObjectName(mbeanName), "setPreviousMaster", new Object[]{masterID}, new String[]{"java.lang.String"});
    }
    else
    {
      throw new ArchiveTrailDataException("Archival failed to be initiated !");
    }
  }
  
  private String getArchiveSingletonMBeanName() throws Exception
  {
    IAuditPropertiesManagerObj mgr = null;
    
    try
    {
      mgr = getPropertiesMgr();
    }
    catch (NamingException e)
    {
      throw new Exception("Failed to look up AuditProperties manager", e);
    }
    catch (CreateException e)
    {
      throw new Exception("Failed to create AuditProperties manager", e);
    }
    catch (Exception e)
    {
      throw e;
    }
    
    String lookupProps = mgr.getAuditProperties(IArchiveConstant.SINGLETON_ARCHIVE, IArchiveConstant.SINGLETON_ARCHIVE_MBEAN, null);
    if(lookupProps == null)
    {
      throw new NullPointerException("Can't find the MBean name for Archive Singleton MBean given the category: "+IArchiveConstant.SINGLETON_ARCHIVE+" propertyKey: "+IArchiveConstant.SINGLETON_ARCHIVE_MBEAN);
    }
    else
    {
      return lookupProps;
    }
  }
  
  private String getRMIAdaptorName() throws Exception
  {
    IAuditPropertiesManagerObj mgr = null;
    
    try
    {
      mgr = getPropertiesMgr();
    }
    catch (NamingException e)
    {
      throw new Exception("Failed to look up AuditProperties manager", e);
    }
    catch (CreateException e)
    {
      throw new Exception("Failed to create AuditProperties manager", e);
    }
    catch (Exception e)
    {
      throw e;
    }
    
    String lookupProps = mgr.getAuditProperties(IArchiveConstant.SINGLETON_ARCHIVE, IArchiveConstant.RMI_ADAPTOR, null);
    if(lookupProps == null)
    {
      throw new NullPointerException("Can't find the MBean name for RMI Adaptor given the category: "+IArchiveConstant.SINGLETON_ARCHIVE+" propertyKey: "+IArchiveConstant.RMI_ADAPTOR);
    }
    else
    {
      return lookupProps;
    }
  }
  
  private Properties getJndiLookupProperties() throws Exception
  {
    IAuditPropertiesManagerObj mgr = null;
    
    try
    {
      mgr = getPropertiesMgr();
    }
    catch (NamingException e)
    {
      throw new Exception("Failed to look up AuditProperties manager", e);
    }
    catch (CreateException e)
    {
      throw new Exception("Failed to create AuditProperties manager", e);
    }
    catch (Exception e)
    {
      throw e;
    }
    
    Properties lookupProps = mgr.getAuditProperties(IArchiveConstant.JNDI_LOOKUP);
    if(lookupProps == null)
    {
      throw new NullPointerException("Can't find jndi lookup properties given the category: "+IArchiveConstant.JNDI_LOOKUP);
    }
    else
    {
      return lookupProps;
    }
  }
  
  private IAuditPropertiesManagerObj getPropertiesMgr() throws RemoteException, CreateException, NamingException
  {
    JndiFinder finder = new JndiFinder(null);
    IAuditPropertiesManagerHome home = (IAuditPropertiesManagerHome)finder.lookup(IAuditPropertiesManagerHome.class.getName(),
                                                                                  IAuditPropertiesManagerHome.class);
    return home.create();
  }
}
