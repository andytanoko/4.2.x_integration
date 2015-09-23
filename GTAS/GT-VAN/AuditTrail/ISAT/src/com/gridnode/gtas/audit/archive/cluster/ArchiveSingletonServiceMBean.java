package com.gridnode.gtas.audit.archive.cluster;

import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;

public interface ArchiveSingletonServiceMBean 
{
	public void startArchivalSingleton();
	
	public void stopArchivalSingleton();
  
  public boolean isMasterNode();
  
  public String getNodeID();
  
  public void archive(Hashtable archiveCriteria) throws Exception;
  
  /**
   * Retrieve the currently processing archive ID
   * @return
   * @throws Exception
   */
  public String getProcessingArchiveID() throws Exception;
  
/**
   * Manually update the archive status. The lost of update information may due to some system error
   * eg can't lookup the destination for sending the archive status.
   * @param archiveID
   * @param jobID
   * @param archiveType either ProcessInstance or Document
   * @param isArchiveSuccess
   * @param summaryFilename
   */
  public void updateArchiveStatus(String archiveID, String jobID, String archiveType, boolean isArchiveSuccess, String summaryFilename) throws Exception;
  
  /**
   * Check whether an archival process is processing
   * @return
   */
  public boolean isArchiveProcessing();
  
/**
   * Get the last activate time of the MBean. 
   * @return
   */
  public Date getLastActivateTime();
  
  /**
   * To enable the archival task to be distributed among the nodes within the clustered. Def is false.
   * @param isEnabled
   */
  public void setEnabledMultiNodes(boolean isEnabled);
  
  /**
   * To indicate whether the archival task is distributed among the nodes in the cluster.
   * @return
   */
  public boolean isEnabledMultiNodes();
  
  /**
   * Check the archive status of the on going archival process
   * @throws Exception
   */
  public void checkArchiveStatus() throws Exception;
  
  public void dropCurrentArchiveProcess();
}
