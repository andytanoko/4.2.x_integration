/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveSummary.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 16, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.archive;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public class ArchiveSummary
{
  private String _archiveDescription;
  private String _archiveName;
  private ArchiveCriteria _archiveCriteria;
  
  private String _group;
  private String _zipFileList;
  private String _gtPISummaryFilename;
  private String _gtDocSummaryFilename;
  private String _archiveID;
  
  private boolean _isGTPIArchivedSuccess = true;
  private boolean _isGTDocArchivedSuccess = true;
  private boolean _isTMArchivedSuccess = true;
  
  private boolean _isTMRestoreSuccess = false;
  private boolean _isGTPIRestoreSuccess = false;
  private boolean _isGTDocRestoreSuccess = false;
  
  private Integer _totalArchiveProcessTransaction;
  private Integer _totalIncompleteDocument;
  private Integer _totalIncompleteProcessTrans;
  
  public ArchiveSummary()
  {
    
  }
  
  public String getArchiveDescription()
  {
    return _archiveDescription;
  }

  public void setArchiveDescription(String description)
  {
    _archiveDescription = description;
  }

  public String getArchiveName()
  {
    return _archiveName;
  }

  public void setArchiveName(String name)
  {
    _archiveName = name;
  }

  public String getGroup()
  {
    return _group;
  }

  public void setGroup(String _group)
  {
    this._group = _group;
  }

  public String getZipFileList()
  {
    return _zipFileList;
  }

  public void setZipFileList(String fileList)
  {
    _zipFileList = fileList;
  }

  public ArchiveCriteria getArchiveCriteria()
  {
    return _archiveCriteria;
  }

  public void setArchiveCriteria(ArchiveCriteria criteria)
  {
    _archiveCriteria = criteria;
  }

  public String getArchiveID()
  {
    return _archiveID;
  }

  public void setArchiveID(String _archiveid)
  {
    _archiveID = _archiveid;
  }

  public String getGtPISummaryFilename()
  {
    return _gtPISummaryFilename;
  }

  public void setGtPISummaryFilename(String summaryFilename)
  {
    _gtPISummaryFilename = summaryFilename;
  }

  public String getGtDocSummaryFilename()
  {
    return _gtDocSummaryFilename;
  }

  public void setGtDocSummaryFilename(String docSummaryFilename)
  {
    _gtDocSummaryFilename = docSummaryFilename;
  }

  public boolean isGTDocArchivedSuccess()
  {
    return _isGTDocArchivedSuccess;
  }

  public void setGTDocArchivedSuccess(boolean docArchivedSuccess)
  {
    _isGTDocArchivedSuccess = docArchivedSuccess;
  }

  public boolean isGTPIArchivedSuccess()
  {
    return _isGTPIArchivedSuccess;
  }

  public void setGTPIArchivedSuccess(boolean archivedSuccess)
  {
    _isGTPIArchivedSuccess = archivedSuccess;
  }

  public boolean isTMArchivedSuccess()
  {
    return _isTMArchivedSuccess;
  }

  public void setTMArchivedSuccess(boolean archivedSuccess)
  {
    _isTMArchivedSuccess = archivedSuccess;
  }
  
  public boolean isGTDocRestoreSuccess()
  {
    return _isGTDocRestoreSuccess;
  }

  public void setGTDocRestoreSuccess(boolean docRestoreSuccess)
  {
    _isGTDocRestoreSuccess = docRestoreSuccess;
  }

  public boolean isGTPIRestoreSuccess()
  {
    return _isGTPIRestoreSuccess;
  }

  public void setGTPIRestoreSuccess(boolean restoreSuccess)
  {
    _isGTPIRestoreSuccess = restoreSuccess;
  }

  public boolean isTMRestoreSuccess()
  {
    return _isTMRestoreSuccess;
  }

  public void setTMRestoreSuccess(boolean restoreSuccess)
  {
    _isTMRestoreSuccess = restoreSuccess;
  }

  public Integer getTotalIncompleteDocument()
  {
    return _totalIncompleteDocument;
  }

  public void setTotalIncompleteDocument(Integer archivedOrphanRecord)
  {
    _totalIncompleteDocument = archivedOrphanRecord;
  }

  public Integer getTotalArchiveProcessTransaction()
  {
    return _totalArchiveProcessTransaction;
  }

  public void setTotalArchiveProcessTransaction(Integer archiveProcessTransaction)
  {
    _totalArchiveProcessTransaction = archiveProcessTransaction;
  }
  
  public Integer getTotalIncompleteProcessTrans()
  {
    return _totalIncompleteProcessTrans;
  }

  public void setTotalIncompleteProcessTrans(Integer incompleteProcessTrans)
  {
    _totalIncompleteProcessTrans = incompleteProcessTrans;
  }

  public String toString()
  {
    return "ArchiveSummary[ archiveDescription:"+getArchiveDescription()+" archiveName: "+getArchiveName()+" archiveCriteria: "+getArchiveCriteria()+
           " group: "+getGroup()+" zipFileList: "+getZipFileList()+" piSummaryFilename: "+getGtPISummaryFilename()+" docSummaryFilename: "+getGtDocSummaryFilename()+
           " archiveID: "+getArchiveID()+" isGTPiArchivedSuccess "+isGTPIArchivedSuccess()+" isGTDocArchivedSuccess "+isGTDocArchivedSuccess()+
           " isTMArchivedSuccess: "+isTMArchivedSuccess()+" isGTDocRestoreSucess: "+isGTDocRestoreSuccess()+
           " isGTIRestoreSuccess: "+isGTPIRestoreSuccess()+" isTMRestoreSuccess+"+isTMRestoreSuccess()+
           " totalArchiveProcessInstance: "+getTotalArchiveProcessTransaction()+" totalArchiveOrphanRecord: "+getTotalIncompleteDocument()+
           " totalIncompleteDocument: "+getTotalIncompleteDocument()+" totalIncompleteProcessTrans: "+getTotalIncompleteProcessTrans()+"]";
  }
  
  
  public static void main(String[] args) throws Exception
  {
    /*
    ArchiveCriteria criteria = new ArchiveCriteria();
    criteria.setFromStartDate(new Date());
    criteria.setToStartDate(new Date());
    
    ArchiveSummary summ = new ArchiveSummary();
    summ.setArchiveID("123456");
    summ.setArchiveName("archive name");
    summ.setArchiveCriteria(criteria);
    summ.setGTDocArchivedSuccess("false");
    
    String xml = XMLBeanUtil.beanToXml(summ, ArchiveSummary.class.getSimpleName());
    System.out.println("XML is "+xml);
    
    
    File out = new File("c:/yahoo.xml");
    /*
    FileOutputStream output = new FileOutputStream(out);
    IOUtil.write(out, xml.getBytes());
    
    FileInputStream input = new FileInputStream(out);
    byte[] xmlContent = IOUtil.read(input);
    xml = new String(xmlContent);
    
    summ = (ArchiveSummary)XMLBeanUtil.xmlToBean(xml, ArchiveSummary.class);
    System.out.println("Summary is "+summ); 
    
    /*
    File f = new File("E:/GT4/GT_VAN/JBOSS-~1.GA/bin/gtvan/archive/20070228_065813082.xml");
    FileInputStream in = new FileInputStream(f);
    
    byte[] bytes  = IOUtil.read(in);
    String xmlC = new String(bytes);
    System.out.println("XML C s "+xmlC);
    
    ArchiveSummary summ = (ArchiveSummary)XMLBeanUtil.xmlToBean(xmlC, ArchiveSummary.class);
    System.out.println(summ); */
  }
}
