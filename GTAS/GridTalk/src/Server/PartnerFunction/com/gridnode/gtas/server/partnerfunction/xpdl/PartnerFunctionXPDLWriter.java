/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionXPDLWriter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 18 2002    Koh Han Sing        Created
 * Apr 29 2003    Neo Sok Lay         Add Raise Alert Xpdl.
 */
package com.gridnode.gtas.server.partnerfunction.xpdl;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;

import com.gridnode.gtas.server.partnerfunction.helpers.IPartnerFunctionPathConfig;
import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

public class PartnerFunctionXPDLWriter
       extends XPDLWriter
{
//  protected static final String XSL_PARAM1 = "partnerfunction.xsl.param1";
//  protected static final String XSL_PARAM2 = "partnerfunction.xsl.param2";
//  protected static final String XSL_PARAM3 = "partnerfunction.xsl.param3";
//  protected static final String XSL_PARAM4 = "partnerfunction.xsl.param4";
//  protected static final String XSL_PARAM5 = "partnerfunction.xsl.param5";
//  protected static final String XSL_FILENAME = "partnerfunction.xsl.filename";
//  protected static final String EXIT_XPDL_FILENAME = "exit.xpdl.filename";
//  protected static final String MAPPER_XPDL_FILENAME = "mapper.xpdl.filename";
//  protected static final String USERPROC_XPDL_FILENAME = "userprocedure.xpdl.filename";
//  protected static final String ALERT_XPDL_FILENAME    = "alert.xpdl.filename";

  private static PartnerFunctionXPDLWriter self = null;

  private PartnerFunctionXPDLWriter()
  {
    super();
  }

  public static PartnerFunctionXPDLWriter getInstance()
  {
    if(self == null)
    {
      synchronized(PartnerFunctionXPDLWriter.class)
      {
        if (self == null)
        {
          self = new PartnerFunctionXPDLWriter();
        }
      }
    }
    return self;
  }

  public synchronized Collection writeToXPDL(IEntity entity)
    throws Throwable
  {
    String newPfFilename = generateRandomFileName();
    ((AbstractEntity)entity).serialize(newPfFilename);
    File pfFile = new File(newPfFilename);

    File xslFile = FileUtil.getFile(IPartnerFunctionPathConfig.PATH_XSL,
                                    "PartnerFunctionXPDL.xsl");

    File newXpdlFile = getXMLServiceBean().transform(
                         xslFile.getAbsolutePath(),
                         pfFile.getAbsolutePath());

    File oldXPDLFile = FileUtil.getFile(
                         IPartnerFunctionPathConfig.PATH_XPDL,
                         entity.getFieldValue(PartnerFunction.PARTNER_FUNCTION_ID)+".xpdl");

    if (oldXPDLFile != null)
    {
      FileUtil.delete(
        IPartnerFunctionPathConfig.PATH_XPDL,
        entity.getFieldValue(PartnerFunction.PARTNER_FUNCTION_ID)+".xpdl");
    }

    FileUtil.create(
      IPartnerFunctionPathConfig.PATH_XPDL,
      entity.getFieldValue(PartnerFunction.PARTNER_FUNCTION_ID)+".xpdl",
      new FileInputStream(newXpdlFile));

    pfFile.delete();
    ArrayList xpdlFiles = new ArrayList();
    xpdlFiles.add(newXpdlFile);
    return xpdlFiles;
  }
}