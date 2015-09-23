/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XPDLWriter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 16 2002    Koh Han Sing        Created
 * Nov 10 2005    Neo Sok Lay         Use LocalContext to lookup XMLService
 */
package com.gridnode.gtas.server.partnerfunction.xpdl;


import java.io.File;
import java.util.Collection;
import java.util.Random;

import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;


public abstract class XPDLWriter
{
  //protected static final String CONFIG_NAME       = "xpdl";

  //protected Configuration xpdlConfig;

  public XPDLWriter()
  {
    //xpdlConfig = ConfigurationManager.getInstance().getConfig(CONFIG_NAME);
  }

  public abstract Collection writeToXPDL(IEntity entity)
    throws Throwable;

  protected IXMLServiceLocalObj getXMLServiceBean()
    throws ServiceLookupException
  {
    return (IXMLServiceLocalObj)ServiceLocator.instance(
      ServiceLocator.LOCAL_CONTEXT).getObj(
      IXMLServiceLocalHome.class.getName(),
      IXMLServiceLocalHome.class,
      new Object[0]);
  }

  protected String generateRandomFileName()
  {
    Random random = new Random();
    String filename = String.valueOf(random.nextInt());
    File newFile = new File(filename);
    while (newFile.exists())
    {
      filename = String.valueOf(random.nextInt());
      newFile = new File(filename);
    }
    return filename;
  }

//  protected void copyFile(File sourceFile, File targetFile)
//  {
//    BufferedInputStream bis = null;
//    BufferedOutputStream bos = null;
//    try
//    {
//      byte[] buffer = new byte[512];
//
//      bis = new BufferedInputStream(new FileInputStream(sourceFile));
//      bos = new BufferedOutputStream(new FileOutputStream(targetFile));
//
//      int size;
//      while ((size = bis.read(buffer, 0, 512)) != -1)
//      {
//        bos.write(buffer, 0, size);
//        bos.flush();
//      }
//    }
//    catch (Exception ex)
//    {
//      ex.printStackTrace();
//    }
//    finally
//    {
//      try
//      {
//        if (bis != null) bis.close();
//        if (bos != null) bos.close();
//      }
//      catch (Exception ex)
//      {
//        ex.printStackTrace();
//      }
//    }
//  }
//
}