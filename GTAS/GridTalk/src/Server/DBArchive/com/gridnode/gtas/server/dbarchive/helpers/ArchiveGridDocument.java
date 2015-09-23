/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveGridDocument.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 29, 2004 			Mahesh             	Created
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.folder.*;
import com.gridnode.gtas.server.document.helpers.FileHelper;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

public class ArchiveGridDocument
{
  public static final String PATH_GDOC = "gdoc";
  public static final String PATH_UDOC = "udoc";
  public static final String PATH_AUDIT= "audit";
  public static final String PATH_ATT  = "attachment";

  //private List failedGDocUIdList = new ArrayList();
  private Hashtable sysFolderTable = null;
  private IDocumentManagerObj docMgr = null;
  private DbArchive dbArchive = null;
  private int gridDocumentCount;
  private int failedCount;
  private File tempGdocFile=null; // this file is used to create gdoc file if it is missing

  public ArchiveGridDocument(DbArchive dbArchive) throws Exception
  {
    this.dbArchive = dbArchive;

    sysFolderTable = new Hashtable();
    sysFolderTable.put(ImportFolder.FOLDER_NAME, SystemFolder.getSpecificFolder(ImportFolder.FOLDER_NAME));
    sysFolderTable.put(ExportFolder.FOLDER_NAME, SystemFolder.getSpecificFolder(ExportFolder.FOLDER_NAME));
    sysFolderTable.put(InboundFolder.FOLDER_NAME, SystemFolder.getSpecificFolder(InboundFolder.FOLDER_NAME));
    sysFolderTable.put(OutboundFolder.FOLDER_NAME, SystemFolder.getSpecificFolder(OutboundFolder.FOLDER_NAME));

    docMgr = ArchiveHelper.getDocumentManager();
    tempGdocFile = File.createTempFile("TempGridDoc",".xml");
  }

  public int getGridDocumentCount()
  {
    return gridDocumentCount;
  }
  
  public int getFailedCount()
  {
    return failedCount;
  }
    
  public void archive(IDataFilter gridDocFilter)
  {
    try
    {
      Collection docKeys = docMgr.findGridDocumentsKeys(gridDocFilter);
      Logger.log("[ArchiveGridDocument.archive] Total griddocuments to archive = "+docKeys.size());
      for (Iterator i = docKeys.iterator(); i.hasNext();)
      {
        gridDocumentCount++;
        Long docUId = (Long) i.next();
        try
        {
          archiveGridDoc(docUId);
        }
        catch (Throwable th)
        {
          failedCount++;
          dbArchive.err("[ArchiveGridDocument.archive] Error while archiveing GridDocument with docUId=" + docUId, th);
        }
      }
    }
    catch (Throwable th)
    {
      dbArchive.err("[ArchiveGridDocument.archive] Error in archive, gridDocFilter=" + gridDocFilter.getFilterExpr(), th);
    }
  }

  private void archiveGridDoc(Long docUId) throws Throwable
  {
    Logger.log("[ArchiveGridDocument.archiveGridDoc] Enter, docUId=" + docUId);
    List auditFileNameList = new ArrayList();
    GridDocument gDoc = docMgr.findGridDocument(docUId);
    List filesToAdd= new ArrayList();
    //get the gDoc absolute filepath and zip it
    File gDocFile = getGdocFile(gDoc,true);
    if (gDocFile != null && gDocFile.exists())
    {
      String fileName = gDoc.getGdocFilename();
      String category = PATH_GDOC + "/";
      filesToAdd.add(new Object[]{gDocFile,fileName,category});
    }
    else
      dbArchive.err("[ArchiveGridDocument.archiveGridDoc] gDocFile doesnot exists for docUId=" + docUId + ", gDocFile=" + gDocFile);

    //get the uDoc absolute filepath and zip it
    File uDocFile = FileHelper.getUdocFile(gDoc);
    if (uDocFile != null && uDocFile.exists())
    {
      String fileName = gDoc.getUdocFilename();
      String category = PATH_UDOC + "/" + gDoc.getFolder() + "/";
      filesToAdd.add(new Object[]{uDocFile,fileName,category});
    }
    else
      dbArchive.err("[ArchiveGridDocument.archiveGridDoc] uDocFile doesnot exists for docUId=" + docUId + ", UdocFilename="+gDoc.getUdocFilename()+", uDocFile=" + uDocFile);

    String auditFileNames = gDoc.getAuditFileName(); 
    if(auditFileNames!=null && auditFileNames.trim().length()>0)
    {
      Logger.debug("[ArchiveGridDocument.archiveGridDoc] docUId="+docUId+", auditFileNames="+auditFileNames);
      StringTokenizer strTok = new StringTokenizer(auditFileNames,";");
      while(strTok.hasMoreTokens())
      {
        String auditFileName = strTok.nextToken();
        Logger.debug("[ArchiveGridDocument.archiveGridDoc] auditFileName="+auditFileName);
        File auditFile = FileUtil.getFile(IDocumentPathConfig.PATH_AUDIT, auditFileName);
        if(auditFile!=null)
        {
          String fileName = auditFile.getName();
          auditFileNameList.add(fileName);
          String category = PATH_AUDIT + "/";
          filesToAdd.add(new Object[]{auditFile,fileName,category});
        }
      }
    }
    
    if (gDoc.hasAttachment().booleanValue())
    {
      Iterator i = gDoc.getAttachments().iterator();
      while (i.hasNext())
      {
        Long attachmentUId = new Long(i.next().toString());
        try
        {
          Attachment attachment = docMgr.findAttachment(attachmentUId);
          String fileName = attachment.getFilename();
          File attachmentFile = FileUtil.getFile(IDocumentPathConfig.PATH_ATTACHMENT, fileName);
          if (attachmentFile != null && attachmentFile.exists())
          {
            String category = PATH_ATT + "/" + attachmentUId + "/";
            //dbArchive.addFileToArchive(attachmentFile, fileName, category);
            filesToAdd.add(new Object[]{attachmentFile,fileName,category});
          }
          else
            Logger.warn(
              "[ArchiveGridDocument.archiveGridDoc] attachmentFile doesnot exists for docUId="
                + docUId
                + ", attachmentUId="
                + attachmentUId
                + ", attachmentFile="
                + attachmentFile);
        }
        catch (Throwable th)
        {
          dbArchive.err("[ArchiveGridDocument.archiveGridDoc] Error while archiveing attachment, attachmentUId:" + attachmentUId, th);
        }
      }
    }
    dbArchive.addFilesToZip(filesToAdd);
    for(Iterator i = auditFileNameList.iterator();i.hasNext();)
    {
      String auditFileName = (String) i.next();
      try
      {
        FileUtil.delete(IDocumentPathConfig.PATH_AUDIT, auditFileName);
      }
      catch(Throwable th)
      {
        dbArchive.err("[ArchiveGridDocument.archiveGridDoc] Error while deleting auditFile, auditFileName=" + auditFileName, th);
      }
    }
    docMgr.deleteGridDocument(docUId);
  }

  public void restoreGridDocs(ArrayList filenames)
  {
    //insert GridDocument to database and handle gDoc & uDoc.
    for (int i = 0; i < filenames.size(); i++)
    {
      String filename = (String) filenames.get(i);

      try
      {
        //if this is a gDoc file(not a uDoc file)
        if (filename!=null && filename.startsWith(dbArchive.TMP_GDOC_DIR))
        {
          gridDocumentCount++;
          importGridDocument(filename);
        }
      }
      catch(Throwable th)
      {
        failedCount++;  
        dbArchive.err("[ArchiveGridDocument.restoreGridDocs] Error while restoring filename=" + filename, th);      
      }
    }
  }

  /**
   * Create GridDocument entity from gdoc file and insert the entity
   * into database
   * @param fileName The name of gdoc xml file
   */
  private void importGridDocument(String fileName) throws Exception
  {
    boolean regenGDocId = false;
    //construct GridDocument entity from gDoc file
    GridDocument gDoc = new GridDocument();
    gDoc = (GridDocument) gDoc.deserialize(fileName);

    Number gDocId = gDoc.getGdocId();
    String folder = (String) gDoc.getFolder();

    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, GridDocument.G_DOC_ID, filter.getEqualOperator(), gDocId, false);
    filter.addSingleFilter(filter.getAndConnector(), GridDocument.FOLDER, filter.getEqualOperator(), folder, false);

    try
    {
      Collection oldGDocColl = docMgr.findGridDocumentsKeys(filter);
      if (oldGDocColl != null && !oldGDocColl.isEmpty())
      {
        regenGDocId = true; 
        dbArchive.err("GridDocument with GDOCID=" + gDocId + " AND FOLDER= " + folder + " already exist! ");
        //A NEW GDOCID is generated for the restored GridDocument!");
      }
    }
    catch (Exception ex)
    {
    }

    if (regenGDocId)
    {
      throw new ApplicationException(
        "Repeated Restore:" + "GridDocument with GDOCID=" + gDocId + " AND FOLDER= " + folder + " already exist! ");
    }

    //construct new udoc filename
    boolean uDocFileCreated=false;
    String uDocFilename = (String) gDoc.getFieldValue(GridDocument.U_DOC_FILENAME);
    File tmpUDocFile = new File(dbArchive.TMP_UDOC_DIR + DbArchive.FILE_SEPARATOR + folder + DbArchive.FILE_SEPARATOR + uDocFilename);
    if (tmpUDocFile.isFile())
    {
      //save udoc file to respective directory
      if(!FileUtil.exist(IDocumentPathConfig.PATH_UDOC, folder + File.separator, uDocFilename))
      {
        uDocFilename = FileUtil.create(IDocumentPathConfig.PATH_UDOC, folder + File.separator, uDocFilename, tmpUDocFile);
        File newUDocFile = FileUtil.getFile(IDocumentPathConfig.PATH_UDOC, folder + File.separator, uDocFilename);
        if(newUDocFile!=null)
          newUDocFile.setLastModified(tmpUDocFile.lastModified());
        uDocFileCreated=true;          
      }
    }
    gDoc.setFieldValue(GridDocument.U_DOC_FILENAME, uDocFilename);

    //construct audit filename
    List auditFileNameList = new ArrayList();
    String auditFileNames = (String) gDoc.getFieldValue(GridDocument.AUDIT_FILE_NAME);
    if(auditFileNames!=null && auditFileNames.trim().length()>0)
    {
      StringTokenizer strTok = new StringTokenizer(auditFileNames,";");
      auditFileNames="";
      while(strTok.hasMoreTokens())
      {
        String auditFileName=strTok.nextToken();
        File tmpAuditFile = new File(dbArchive.TMP_AUDIT_DIR + DbArchive.FILE_SEPARATOR + auditFileName);
        if(tmpAuditFile != null)
        {
          auditFileName = FileUtil.create(IDocumentPathConfig.PATH_AUDIT , auditFileName, tmpAuditFile);
          File newAuditFile = FileUtil.getFile(IDocumentPathConfig.PATH_AUDIT, auditFileName);
          if(newAuditFile!=null)
          {
            newAuditFile.setLastModified(tmpAuditFile.lastModified());
          }
          auditFileNameList.add(auditFileName);
          auditFileNames+=auditFileName+(strTok.hasMoreTokens()?";":"");
        }
      }
    }
    gDoc.setFieldValue(GridDocument.AUDIT_FILE_NAME, auditFileNames);

    Hashtable attachmentTable = new Hashtable();
    //construct attachments
    if (gDoc.hasAttachment().booleanValue())
    {
      try
      {
        gDoc = processAttachments(gDoc,attachmentTable);
      }
      catch (Exception ex)
      {
        dbArchive.err("[ArchiveGridDocument.importGridDocument] Error restoring attachments", ex);
      }
    }

    try
    {
      gDoc = docMgr.createGridDocument(gDoc, regenGDocId);
      if (gDoc != null) // this is to set the timestamp of the gdoc file
      {
        File newGdocFile = null;
        try
        {
          newGdocFile = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC, gDoc.getFolder() + File.separator, gDoc.getGdocFilename());
          if(newGdocFile!=null)
            newGdocFile.setLastModified((new File(fileName)).lastModified());
        }
        catch (Throwable th)
        {
          dbArchive.err("[ArchiveGridDocument.importGridDocument] Error while setting File timestamp for newGdocFile="+newGdocFile, th);
        }
      }
    }
    catch (Exception ex)
    {
      if(uDocFileCreated)
        FileUtil.delete(IDocumentPathConfig.PATH_UDOC, folder + File.separator, uDocFilename);
      if(!auditFileNameList.isEmpty())
      {
        for(Iterator i = auditFileNameList.iterator();i.hasNext();)
        {
          String auditFileName = (String)i.next();
          try
          {
            FileUtil.delete(IDocumentPathConfig.PATH_AUDIT,auditFileName);
          }
          catch(Throwable th)
          {
            dbArchive.err("[ArchiveGridDocument.importGridDocument] Error while deleting auditfile ="+auditFileName, th);
          }
        }      
      }
      Enumeration enu = attachmentTable.elements();
      while (enu.hasMoreElements())
      {
        Long delUid = (Long) enu.nextElement();
        docMgr.deleteAttachment(delUid);
      }
      
      throw ex;
    }
  }

  private GridDocument processAttachments(GridDocument gDoc,Hashtable attachmentTable) throws Exception
  {
    Iterator i = gDoc.getAttachments().iterator();
    ArrayList newAttUids = new ArrayList();
    while (i.hasNext())
    {
      Long attUid = new Long(i.next().toString());
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, Attachment.UID, filter.getEqualOperator(), attUid, false);
      Collection attList = docMgr.findAttachments(filter);
      if (attList.isEmpty())
      {
        try
        {
          Long newAttUid = constructAttachment(attUid, gDoc,attachmentTable);
          newAttUids.add(newAttUid);
        }
        catch(Exception ex)
        {
          dbArchive.err("[ArchiveGridDocument.processAttachments] Error while constructAttachment, attUid="+attUid,ex);
        }
      }
      else
      {
        newAttUids.add(attUid);
      }
    }
    gDoc.setAttachments(newAttUids);
    return gDoc;
  }

  private Long constructAttachment(Long orgUid, GridDocument gDoc,Hashtable attachmentTable) throws Exception
  {
    Long newAttUid = (Long) attachmentTable.get(orgUid);
    if (newAttUid == null)
    {
      File tmpAttDir = new File(dbArchive.TMP_ATT_DIR + DbArchive.FILE_SEPARATOR + orgUid);
      if (tmpAttDir.exists())
      {
        File[] tmpAttFiles = tmpAttDir.listFiles();
        if (tmpAttFiles.length > 0)
        {
          File tmpAttFile = tmpAttFiles[0];
          String attFilename =
            FileUtil.create(IDocumentPathConfig.PATH_ATTACHMENT, tmpAttFile.getName(), new FileInputStream(tmpAttFile));
          Attachment att = new Attachment();
          att.setOriginalFilename(tmpAttFile.getName());
          att.setFilename(attFilename);
          if (gDoc.getFolder().equals(InboundFolder.FOLDER_NAME))
          {
            att.setOutgoing(Boolean.FALSE);
          }
          else
          {
            att.setOutgoing(Boolean.TRUE);
          }
          newAttUid = docMgr.createAttachment(att);
          attachmentTable.put(orgUid, newAttUid);
        }
      }
    }
    return newAttUid;
  }
  
  private File getGdocFile(GridDocument gDoc,boolean generateNew) throws Exception
  {
    File gdocFile = null;
    String gdocFilename = gDoc.getGdocFilename();

    if (gdocFilename == null || gdocFilename.trim().length() == 0)
    {
      gdocFilename = getGdocFilename(gDoc);
      gDoc.setGdocFilename(gdocFilename);
      gdocFile = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC, gDoc.getFolder() + File.separator, gdocFilename);
    }
    else
    {
      gdocFile = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC, gDoc.getFolder() + File.separator, gdocFilename);
      
      if(gdocFile==null)
      {
        String newGdocFilename = getGdocFilename(gDoc);
        if(!newGdocFilename.equals(gdocFilename))
        {
          gdocFilename = newGdocFilename;
          gDoc.setGdocFilename(gdocFilename);
          gdocFile = FileUtil.getFile(IDocumentPathConfig.PATH_GDOC, gDoc.getFolder() + File.separator, gdocFilename);
        }
      }
    }

    if (gdocFile == null && generateNew) //if still gdocFile is null create the gdocFile
    {
      gDoc.serialize(tempGdocFile.getAbsolutePath());
      gdocFile = tempGdocFile;
    }
    return gdocFile;
  }

  private String getGdocFilename(GridDocument gDoc) throws Exception
  {
    SystemFolder sysFolder = (SystemFolder) sysFolderTable.get(gDoc.getFolder());
    return sysFolder.getGdocFilename(gDoc);
  }
}
