/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JbossNodeDiscover.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 29, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive.cluster.helper;

import javax.management.MBeanServer;

import org.jboss.ha.framework.server.ClusterPartitionMBean;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.mx.util.MBeanServerLocator;


import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is responsible to obtain the available nodes within the clustered environment.
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.1.2)
 */
public class JbossNodeDiscover implements INodeDiscover
{
  private static final JbossNodeDiscover _nodeDiscover = new JbossNodeDiscover();
  private static final String CLASS_NAME = "JbossNodeDiscover";
  private Logger _logger;
  
  public static JbossNodeDiscover getInstance()
  {
    return _nodeDiscover;
  }
  
  private JbossNodeDiscover()
  {
    _logger = getLogger();
  }
  
  /* (non-Javadoc)
   * @see com.gridnode.gtas.audit.archive.cluster.helper.INodeDiscover#getAvailableNodes()
   */
  public int getAvailableNodes() throws ArchiveTrailDataException
  {
    String mn = "getAvailableNodes";
    
    try
    {
      if(isEnabledClustered())
      {
        MBeanServer server = MBeanServerLocator.locateJBoss();
        ClusterPartitionMBean cluster;
        
        String mbeanObjName = IArchiveClusterConstant.JBOSS_SERVICE_NAME_PREFIX+getPartitionName();
        _logger.logMessage(mn, null, "Mbean obj name "+mbeanObjName);
        
        cluster = (ClusterPartitionMBean) MBeanProxyExt.create(ClusterPartitionMBean.class, mbeanObjName, server);
        if(cluster != null)
        {
          return cluster.getCurrentView().size();
        }
        else
        {
          throw new NullPointerException("Error in creating Mbean proxy given the proxy interface: "+ClusterPartitionMBean.class.getSimpleName()+" and object name "+getPartitionName());
        }
      }
      else
      {
        _logger.logMessage(mn, null, "Clustered is not enabled");
        return 0;
      }
    }
    catch(Exception ex)
    {
      throw new ArchiveTrailDataException("Error in getting the available nodes in cluster env "+ex.getMessage(), ex);
    }
  }
 
  private boolean isEnabledClustered()
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
  
  public static void main(String[] args) throws Exception
  {
    JbossNodeDiscover d = new JbossNodeDiscover();
    //System.out.println(d.getAvailableNodes());
    System.out.println(System.getProperty("node.clustered"));
  }
}
