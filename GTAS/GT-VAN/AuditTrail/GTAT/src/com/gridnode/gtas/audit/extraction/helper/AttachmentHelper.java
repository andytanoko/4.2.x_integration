/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AttachmentHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 20, 2006    Tam Wei Xiang       Created
 * Aug 13, 2007    Tam Wei Xiang       Resolved defect: GNDB00028437
 */
package com.gridnode.gtas.audit.extraction.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerHome;
import com.gridnode.gtas.server.document.facade.ejb.IDocumentManagerObj;
import com.gridnode.gtas.server.document.helpers.IDocumentPathConfig;
import com.gridnode.gtas.server.document.model.Attachment;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class AttachmentHelper
{
  private static final String CLASS_NAME = "AttachmentHelper";
  
  /**
   * Return a collection of attachment file given the attUID list.
   * @param attUIDs
   * @return a collection of attachmetn files or empty collection if the attUIDs is empty or null
   * @throws Exception
   */
  public static Collection<File> getAttachmentFiles(List attUIDs) throws Exception
  {
    ArrayList<File> attFileList = new ArrayList<File>();
    if(attUIDs != null && attUIDs.size() > 0)
    {
      IDocumentManagerObj docMgr = getDocumentManager();
      Iterator ite = attUIDs.iterator();
      while(ite.hasNext())
      {
        Long attUID = new Long((String)ite.next()); //TWX resolved defect GNDB00028437
        Attachment att = docMgr.findAttachment(attUID);
        File attFile = FileUtil.getFile(IDocumentPathConfig.PATH_ATTACHMENT, att.getFilename());
        if(attFile == null)
        {
          throw new NullPointerException("["+CLASS_NAME+".getAttachmentFilePaths] Attachment file ["+att.getFilename()+"Attachment UID "+att.getKey()+"]. The file is not existed !");
        }
        else
        {
          attFileList.add(attFile);
        }
      }
    }
    return attFileList;
  }
  
  private static IDocumentManagerObj getDocumentManager() throws ServiceLookupException
  {
    return (IDocumentManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IDocumentManagerHome.class.getName(),
      IDocumentManagerHome.class,
      new Object[0]);
  }
}
