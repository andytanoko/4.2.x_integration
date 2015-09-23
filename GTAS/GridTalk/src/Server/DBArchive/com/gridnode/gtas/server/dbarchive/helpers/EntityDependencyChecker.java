/**
 * This software is the proprietary information of CrimsonLogic eTrade Services Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006-2009 (C) CrimsonLogic eTrade Services Pte Ltd. All Rights Reserved.
 *
 * File: EntityDependencyChecker.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 17, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.gridnode.gtas.server.dbarchive.facade.ejb.IArchiveManagerHome;
import com.gridnode.gtas.server.dbarchive.facade.ejb.IArchiveManagerObj;
import com.gridnode.gtas.server.dbarchive.model.ArchiveMetaInfo;
import com.gridnode.gtas.server.dbarchive.model.IArchiveMetaInfo;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * @author Tam Wei Xiang
 * @version GT4.2.1
 * @since GT4.2.1
 */
public class EntityDependencyChecker
{
  public EntityDependencyChecker()
  {
    
  }
  
  /**
   * Check is there any ArchiveMetaInfo that referring the give documentType
   * @param documentType the documentType that will be deleted from the system
   * @return return a set of ArchiveMetaInfo that depended on the given documentType or
   * empty set if no dependencies, or NULL if error occurs during the check.
   */
  public Set<ArchiveMetaInfo> checkDependentArchiveMetaInfoForDocumentType(String documentType)
  {
    Set<ArchiveMetaInfo> dependents = null;
    try
    {
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, IArchiveMetaInfo.ARCHIVE_TYPE, filter.getEqualOperator(), ArchiveMetaInfo.ARCHIVE_TYPE_DOCUMENT, false);
      Collection archiveMetaInfos =  getArchiveMetaInfoList(filter);
      ArrayList<ArchiveMetaInfo> dependentArchiveMetas = new ArrayList<ArchiveMetaInfo>();
      
      if(archiveMetaInfos.size() > 0)
      {
        for(Iterator i = archiveMetaInfos.iterator(); i.hasNext(); )
        {
          ArchiveMetaInfo info = (ArchiveMetaInfo)i.next();
          String docTypeList = info.getDocumentTypeList();
          int size = docTypeList.indexOf(";"+documentType+";"); //this base on the docType behaviour that we will have ; surrounded the doc type
          if(size >= 0)
          {
            dependentArchiveMetas.add(info);
          }
        }
        dependents = new HashSet<ArchiveMetaInfo>();
        dependents.addAll(dependentArchiveMetas);
      }

    }
    catch(Throwable th)
    {
      Logger.warn("EntityDependencyChecker.checkDependentArchiveMetaInfoForDocumentType err", th);
    }
    return dependents;
  }
  
  /**
   * Check is there any ArchiveMetaInfo that referring the give partnerID
   * @param partnerID the partnerID that will be deleted from the system
   * @return return a set of ArchiveMetaInfo that depended on the given partnerID or
   * empty set if no dependencies, or NULL if error occurs during the check.
   */
  public Set<ArchiveMetaInfo> checkDependentArchiveMetaInfoForPartner(String partnerID)
  {
    Set<ArchiveMetaInfo> dependents = null;
    try
    {
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, IArchiveMetaInfo.PARTNER_ID_FOR_ARCHIVE, filter.getNotEqualOperator(), ";", false);
      Collection archiveMetaInfos = getArchiveMetaInfoList(filter);
      ArrayList<ArchiveMetaInfo> dependentArchiveMetas = new ArrayList<ArchiveMetaInfo>();
      
      if(archiveMetaInfos != null && archiveMetaInfos.size() > 0)
      {
        for(Iterator i = archiveMetaInfos.iterator(); i.hasNext(); )
        {
          ArchiveMetaInfo info = (ArchiveMetaInfo)i.next();
          String partnerIDList = info.getPartnerIDForArchive();
          int size = partnerIDList.indexOf(";"+partnerID+";"); //this base on the docType behaviour that we will have ; surrounded the doc type
          if(size >= 0)
          {
            dependentArchiveMetas.add(info);
          }
        }

        dependents = new HashSet<ArchiveMetaInfo>();
        dependents.addAll(dependentArchiveMetas);
      }
      
      
      
      
    }
    catch(Throwable th)
    {
      Logger.warn("EntityDependencyChecker.checkDependentArchiveMetaInfoForPartner err", th);
    }
    return dependents;
  }
  
  /**
   * Check is there any ArchiveMetaInfo that referring the give process defID 
   * @param processDefID the Process Def ID that will be deleted from the system
   * @return return a set of ArchiveMetaInfo that depended on the given processDefID or
   * empty set if no dependencies, or NULL if error occurs during the check.
   */
  public Set<ArchiveMetaInfo> checkDependentArchiveMetaInfoForProcessDef(String processDefID)
  {
    Set<ArchiveMetaInfo> dependents = null;
    try
    {
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, IArchiveMetaInfo.ARCHIVE_TYPE, filter.getEqualOperator(), ArchiveMetaInfo.ARCHIVE_TYPE_PROCESS_INSTANCE, false);
      Collection archiveMetaInfos = getArchiveMetaInfoList(filter);
      ArrayList<ArchiveMetaInfo> dependentArchiveMetas = new ArrayList<ArchiveMetaInfo>();
      
      if(archiveMetaInfos != null && archiveMetaInfos.size() > 0)
      {
        for(Iterator i = archiveMetaInfos.iterator(); i.hasNext(); )
        {
          ArchiveMetaInfo info = (ArchiveMetaInfo)i.next();
          String processDefNameList = info.getProcessDefNameList();
          int size = processDefNameList.indexOf(";"+processDefID+";"); //this base on the processDefName behaviour that we will have ; surrounded the doc type
          if(size >= 0)
          {
            dependentArchiveMetas.add(info);
          }
        }
        dependents = new HashSet<ArchiveMetaInfo>();
        dependents.addAll(dependentArchiveMetas);
      }
      
    }
    catch(Throwable th)
    {
      Logger.warn("EntityDependencyChecker.checkDependentArchiveMetaInfoForProcessDef err", th);
    }
    return dependents;
  }
  
  public IArchiveManagerObj getArchiveManager() throws Exception
  {
    return(IArchiveManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
                                IArchiveManagerHome.class.getName(), IArchiveManagerHome.class,new Object[0]);
  }
  
  private Collection getArchiveMetaInfoList(IDataFilter filter) throws Exception
  { 
    return getArchiveManager().getArchive(filter);
   
  }
  
  public static void main(String[] args)
  {
    String processDefID = "PartnerID";
    String s = ";testQATP;";
    int size = s.indexOf(";"+processDefID+";");
    System.out.println("Tokens size: "+size);
  }
}
