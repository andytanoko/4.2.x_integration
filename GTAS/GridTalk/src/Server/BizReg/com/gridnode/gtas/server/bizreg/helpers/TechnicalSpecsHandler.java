/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TechnicalSpecsHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 15 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.bizreg.helpers;

import com.gridnode.gtas.server.bizreg.model.TechnicalSpecs;
import com.gridnode.pdip.framework.db.XmlObjectDeserializer;

import java.io.File;
import java.text.MessageFormat;

/**
 * This is a handler for read/write of TechnicalSpecs XML files.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class TechnicalSpecsHandler
{
  private static final TechnicalSpecsHandler _self = new TechnicalSpecsHandler();
  private XmlObjectDeserializer _deser;
  private RegistryConfig _config;

  /**
   * Constructs a TechnicalSpecsHandler.
   */
  private TechnicalSpecsHandler()
  {
    _config = new RegistryConfig();
    _deser = new XmlObjectDeserializer();
  }

  /**
   * Get an instance of this handler.
   */
  public static TechnicalSpecsHandler getInstance()
  {
    return _self;
  }
  
  /**
   * Load TechnicalSpecs of a specified name.
   * 
   * @param name The name for the technicalspecs file. Note that this
   * is only a part of the technicalspecs filename. The actual filename is
   * in the format (<code>techspecs.name.pattern</code>) specified in the 
   * registry config file. 
   * The default format is "technicalspecs-<name>.xml". All technicalspecs files will be
   * loaded from the path (<code>technicalspecs.path</code>) specified in registry config file.
   * @return A TechnicalSpecs object loaded from the technicalspecs file,
   * or <b>null</b> if no such technicalspecs file is found.
   */
  public TechnicalSpecs loadSpecs(String name)
  {
    TechnicalSpecs specs = null;
    File specsFile = getTechSpecsFile(name);
    if (specsFile != null && specsFile.exists())
    {
      // deserialize
      try
      {
        specs = (TechnicalSpecs)_deser.deserialize(null, specsFile.getAbsolutePath());
      }
      catch (Throwable ex)
      {
        Logger.warn("[TechnicalSpecsHandler.loadSpecs] Error, return null", ex);
      }
    }
    else
      Logger.warn("[TechnicalSpecsHandler.loadSpecs] Unable to obtain Techspecs file "+name);
    

    return specs;
  }

  /**
   * Obtain a technicalspecs file using the specified name.
   * 
   * @param name The name portion of the technicalspecs filename pattern.
   * @return The File handle to the technicalspecs file, or <b>null</b> if no
   * such file exists.
   */  
  private File getTechSpecsFile(String name)
  {
    File file = null;
    try
    {
      String filename = MessageFormat.format(_config.getTechSpecsNamePattern(), new Object[]{name});
      file = new File(_config.getTechSpecsPath(), filename);
    }
    catch (Throwable ex)
    {
      Logger.warn("[TechnicalSpecsHandler.getTechSpecsFile] Error ", ex);
    }

    return file;
  }
}
