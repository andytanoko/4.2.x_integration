/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TransformationHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 15 2004    Jagadeesh           Created
*/


package com.gridnode.gtas.server.document.helpers;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

import com.gridnode.pdip.app.mapper.helpers.IMapperPathConfig;
import com.gridnode.pdip.base.xml.exceptions.XMLException;

import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;


import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.Configuration;


import java.io.File;


public class TransformationHelper
{

  public static final int OUTBOUND_TRANSFORMATION = 1;
  public static final int INBOUND_TRANSFORMATION = 2;


  public static File transformDocument(File gdocFile,int transMode)
    throws Exception
  {

      if (OUTBOUND_TRANSFORMATION == transMode)
        return getOutBoundTransformedDoc(gdocFile);
      else if (INBOUND_TRANSFORMATION == transMode)
        return getInBoundTransformedDoc(gdocFile);
      else
        return gdocFile;
  }

  private static File getOutBoundTransformedDoc(File gdocFile)
    throws Exception
  {
    try
    {
      File tGdoc = getTransformedGDocFile(getOutBoundMappingFileName(),gdocFile);
      return (tGdoc == null ? gdocFile : tGdoc);
    }
    catch(XMLException ex)
    {
      Logger.warn("[SendDocumentHelper][performTransformation()][Could Not perform BackwardCompatible Transformation]",ex);
      throw new Exception("[SendDocumentHelper][prepareFilesToSend][Could Not perform BackwardCompatible Transformation]\n"+ex.getMessage());
    }
    catch(FileAccessException fileEx)
    {
      Logger.warn("[SendDocumentHelper][performTransformation()]"+
      "[Could Not Transform BackwardCompatible GridDocument with FileName]"+getOutBoundMappingFileName(),fileEx);
    }
    catch(ServiceLookupException slEx)
    {
      Logger.warn("[SendDocumentHelper][performTransformation()][Could Not Lookup XMLServiceFacade]",slEx);
      throw new Exception("[Could Not Lookup XMLServiceFacade]"+
        slEx.getMessage());
    }
    return null;

  }

  private static File getInBoundTransformedDoc(File gdocFile)
    throws Exception
  {
    try
    {
      File tGdoc = getTransformedGDocFile(getInBoundMappingFileName(),gdocFile);
      return (tGdoc == null ? gdocFile : tGdoc);
    }
    catch(XMLException ex)
    {
      Logger.warn("[SendDocumentHelper][performTransformation()][Could Not perform BackwardCompatible Transformation]",ex);
      throw new Exception("[SendDocumentHelper][prepareFilesToSend][Could Not perform BackwardCompatible Transformation]\n"+ex.getMessage());
    }
    catch(FileAccessException fileEx)
    {
      Logger.warn("[SendDocumentHelper][performTransformation()]"+
      "[Could Not Transform BackwardCompatible GridDocument with FileName]"+getOutBoundMappingFileName(),fileEx);
    }
    catch(ServiceLookupException slEx)
    {
      Logger.warn("[SendDocumentHelper][performTransformation()][Could Not Lookup XMLServiceFacade]",slEx);
      throw new Exception("[Could Not Lookup XMLServiceFacade]"+
        slEx.getMessage());
    }
    return null;

  }


  private static File getTransformedGDocFile(String mappingFileName,File gdocFile)
    throws XMLException,FileAccessException,ServiceLookupException
  {

      File xslFile = FileUtil.getFile(IMapperPathConfig.PATH_XSL,mappingFileName);
      File transformedGdoc = XMLDelegate.getManager().transform(
              xslFile.getAbsolutePath(),
              gdocFile.getAbsolutePath()
              );
      if (transformedGdoc == null)
        throw new XMLException("[BackwardCompatible Transformation Unsuccessful]");
      else
        return transformedGdoc;
  }

  private static String getOutBoundMappingFileName()
  {
    return getDocumentConfig().getString(
      IDocumentConfig.BACKWARDCOMPATIBLE_OUTBOUND_MAPPING);
  }

  private static String getInBoundMappingFileName()
  {
    return getDocumentConfig().getString(
      IDocumentConfig.BACKWARDCOMPATIBLE_INBOUND_MAPPING);
  }


  private static Configuration getDocumentConfig()
  {

     return ConfigurationManager.getInstance().getConfig(
                          IDocumentConfig.CONFIG_NAME);
  }


  public TransformationHelper()
  {
  }
}