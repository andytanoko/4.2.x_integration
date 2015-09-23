/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JbossPartitionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 15, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.cluster.helper;

import java.io.Serializable;
import java.util.Collection;

import javax.management.MBeanServer;

import org.jboss.ha.framework.interfaces.DistributedState;
import org.jboss.ha.framework.interfaces.HAPartition;
import org.jboss.ha.framework.server.ClusterPartitionMBean;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;

import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class responsible to take care the task related to the partition. For example
 * accessing/storing the distributed state of the partition.
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.1.1)
 */
public class JbossPartitionHelper
{
  private static JbossPartitionHelper _instance = new JbossPartitionHelper();
  private static String CLASS_NAME = "JbossPartitionHelper";
  
  private Logger _logger;
  
  private JbossPartitionHelper()
  {
    _logger = getLogger();
  }
  
  public static JbossPartitionHelper getInstance()
  {
    return _instance;
  }
  
  public void setAttr(String category, Serializable key, Serializable value) throws Exception
  {
    getDs().set(category, key, value);
  }
  
  public Serializable getAttr(String category, Serializable key) throws Exception
  {
    return getDs().get(category, key);
  }
  
  public Collection getAllKeyInDs(String category) throws Exception
  {
    return getDs().getAllKeys(category);
  }
  
  public void remoteAttr(String category, Serializable key) throws Exception
  {
    getDs().remove(category, key);
  }
  
  private DistributedState getDs() throws Exception
  {
    return getHAPartition().getDistributedStateService();
  }
  
  private HAPartition getHAPartition() throws Exception
  {
    String mn = "getHAPartition";
    MBeanServer server = MBeanServerLocator.locateJBoss();
    ClusterPartitionMBean cluster;
    
    String mbeanObjName = IArchiveClusterConstant.JBOSS_SERVICE_NAME_PREFIX+getPartitionName();
    _logger.logMessage(mn, null, "Mbean obj name "+mbeanObjName);
    
    cluster = (ClusterPartitionMBean) MBeanProxyExt.create(ClusterPartitionMBean.class, mbeanObjName, server);
    if(cluster != null)
    {
      return cluster.getHAPartition();
    }
    else
    {
      throw new NullPointerException("Error in creating Mbean proxy given the proxy interface: "+ClusterPartitionMBean.class.getSimpleName()+" and object name "+getPartitionName());
    }
  }
  
  private String getPartitionName()
  {
    String partitionName = System.getProperty(IArchiveClusterConstant.JBOSS_PARTITION_NAME);
    if(partitionName == null)
    {
      throw new NullPointerException("Cluster mode is enabled; however no partition name can be found from the java option");
    }
    else
    {
      return partitionName;
    }
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
