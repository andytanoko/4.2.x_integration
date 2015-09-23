/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 15, 2007    Tam Wei Xiang       Created
 * Jun 01, 2007    Tam Wei xiang       To support archive by customer
 */
package com.gridnode.gtas.audit.archive.helper;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

import com.gridnode.gtas.audit.archive.IArchiveConstant;
import com.gridnode.gtas.audit.archive.exception.ArchiveTrailDataException;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerLocalHome;
import com.gridnode.gtas.audit.archive.facade.ejb.IAuditTrailArchiveManagerLocalObj;
import com.gridnode.gtas.audit.model.IAuditTrailEntity;
import com.gridnode.gtas.audit.tracking.helpers.IISATConstant;
import com.gridnode.gtas.audit.util.XMLBeanMarshal;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class is responsible to add the AuditTrail entities into the ArchiveZipFile. 
 * 
 * @author Tam Wei Xiang
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveHelper implements Serializable
{
  private static final String CLASS_NAME = ArchiveHelper.class.getSimpleName();
  private Logger _logger;
  
  private ArchiveZipFile _archiveZipFile = null;
  private SimpleDateFormat _dateFormat = null;
  private String _archiveZipCategory = "";
  private ArrayList<String> _archiveFilename = new ArrayList<String>();
  
  private int _maxAllowedSetInZipFile = 0;
  private int _totalSetInZipFile = 0; //indicate the total number of set (set means either the total number of ProcessTransaction
                                      // or total number of tracingIDs (those trace event's tracing id but no process be created) in the current active zip file given the context)
  private String _archiveDir = "";
  
  public ArchiveHelper() 
  {
    _logger = getLogger();
  }
  
  public ArchiveHelper(ArchiveZipFile archiveZipFile)
  {
    _archiveZipFile = archiveZipFile;
  }
  
  public void addAuditTrailEntity(IAuditTrailEntity entity, String filename, String category)
    throws ArchiveTrailDataException
  {
    filename = getFilename(filename, entity);
    String xml = XMLBeanMarshal.objToXML(entity);
    if(xml != null)
    {
      checkCreateNewArchiveZip(getTotalSetInZipFile());
      _archiveZipFile.addByteArrayToZip(xml.getBytes(), filename, category);
    }
    else
    {
      throw new ArchiveTrailDataException("Error in adding the AuditTrail entity "+entity+" into zip file. The serialize from bean to xml was failed !");
    }
  }
  
  private boolean checkCreateNewArchiveZip(int totalSetInZip) throws ArchiveTrailDataException
  {
    String methodName = "checkCreateNewArchiveZip";
    
    try
    {
      if(_archiveZipFile == null)
      {
        createZipFile();
        return true;
      }
      else if(totalSetInZip >= _maxAllowedSetInZipFile)
      {
        _logger.logMessage(methodName, null, "Creating new archive zip ");
      
        _archiveZipFile.closeZipFile();
        createZipFile();
        return true;
      }
      return false;
    }
    catch(Exception ex)
    {
      throw new ArchiveTrailDataException("Error in creating new archive zip "+ex.getMessage(), ex);
    }
  }
  
  private void createZipFile() throws Exception
  {
    createFolder(_archiveDir);
    
    String zipFilename = getZipFilename();
    _archiveFilename.add(zipFilename);
    
    String archiveZipDestPath = _archiveDir +"/"+ zipFilename;
    _archiveZipFile = new ArchiveZipFile(archiveZipDestPath);
    _archiveZipFile.open(ArchiveZipFile.OPEN_MODE_WRITE);
    
    _totalSetInZipFile = 0;
  }
  
  private void createFolder(String folderPath)
  {
    File folder = new File(folderPath);
    if(! folder.exists())
    {
      folder.mkdirs();
    }
  }
  
  private String getZipFilename()
  {
    return IArchiveConstant.ARCHIVE_FILENAME_PREFIX+"-"+UUID.randomUUID().toString()+".zip";
  }
  
  public void closeArchiveZip() throws Exception
  {
    if(_archiveZipFile != null && _archiveZipFile.getTotalEntries() > 0)
    {
      System.out.println("closing archive zip: entry size is "+_archiveZipFile.getTotalEntries());
      _archiveZipFile.closeZipFile();
    }
  }
  
  public void openArchiveZip() throws Exception
  {
    if(_archiveZipFile != null)
    {
      _archiveZipFile.open(ArchiveZipFile.OPEN_MODE_WRITE);
    }
  }
  
  public void setArchiveDateFormat(SimpleDateFormat dateFormat)
  {
    _dateFormat = dateFormat;
  }
  
  public SimpleDateFormat getArchiveDateFormat()
  {
    return _dateFormat;
  }
  
  public String getArchiveZipCategory()
  {
    return _archiveZipCategory;
  }

  public void setArchiveZipCategory(String zipCategory)
  {
    _archiveZipCategory = zipCategory+"/";
  }

  public int getMaxAllowedSetInZipFile()
  {
    return _maxAllowedSetInZipFile;
  }

  public void setMaxAllowedSetInZipFile(int allowedSetInZipFile)
  {
    _maxAllowedSetInZipFile = allowedSetInZipFile;
  }

  public int getTotalSetInZipFile()
  {
    return _totalSetInZipFile;
  }

  public void incrementTotalInZip()
  {
    _totalSetInZipFile++;
  }

  public String[] getArchiveFilename()
  {
    return (String[])_archiveFilename.toArray(new String[]{});
  }
  
  public void addArchiveFilename(String[] archiveFilename)
  {
    for(int i = 0; archiveFilename != null && i < archiveFilename.length; i++)
    {
      _archiveFilename.add(archiveFilename[i]);
    }
  }
  
  private void addArchiveFilename(String filename)
  {
    _archiveFilename.add(filename);
  }
  
  public String getArchiveDir()
  {
    return _archiveDir;
  }

  public void setArchiveDir(String dir)
  {
    _archiveDir = dir;
  }

  private IAuditTrailArchiveManagerLocalObj getArchiveMgr() throws Exception
  {
    JndiFinder finder = new JndiFinder(null);
    IAuditTrailArchiveManagerLocalHome archiveHome = (IAuditTrailArchiveManagerLocalHome)finder.lookup(IAuditTrailArchiveManagerLocalHome.class.getName(), 
                                                                             IAuditTrailArchiveManagerLocalHome.class);
    return archiveHome.create();
  }
  
  private String getFilename(String filename, IAuditTrailEntity entity)
  {
    if(filename == null || "".equals(filename))
    {
      filename = entity.getClass().getSimpleName()+"-"+entity.getUID()+".xml";
    }
    return filename;
  }
  
  private Logger getLogger()
  {
    return LoggerManager.getInstance().getLogger(IISATConstant.LOG_TYPE, CLASS_NAME);
  }
}
