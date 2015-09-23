/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DependencyDescriptorHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 8 2003     Neo Sok Lay         Created
 * Feb 07 2007		Alain Ah Ming				Use new error codes
 */
package com.gridnode.pdip.framework.db.dependency;

import com.gridnode.pdip.framework.db.XmlObjectDeserializer;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.log.Log;

import java.io.File;
import java.text.MessageFormat;

/**
 * This is a handler for read/write of DependencyDescriptor XML files.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 * @version GT 4.0 VAN
 */
public class DependencyDescriptorHandler
{
  private static final DependencyDescriptorHandler _self = new DependencyDescriptorHandler();
  private XmlObjectDeserializer _deser;
  private DependencyConfig _config;
  
  /**
   * Constructor for DependencyDescriptorHandler.
   */
  private DependencyDescriptorHandler()
  {
    _config = new DependencyConfig();
    _deser = new XmlObjectDeserializer();
  }

  /**
   * Get an instance of this handler.
   */
  public static DependencyDescriptorHandler getInstance()
  {
    return _self;
  }
  
  /**
   * Load dependency descriptor of a specified name.
   * 
   * @param name The unique name for the descriptor file. Note that this
   * is only a part of the descriptor filename. The actual filename is
   * in the format (<code>descriptor.name.pattern</code>) specified in the dependency config file. 
   * The default format is "dependency-<name>.xml". All descriptor files will be
   * loaded from the path (<code>descriptor.path</code>) specified in dependency config file.
   * @return A DependencyDescriptor object loaded from the descriptor file,
   * or <b>null</b> if no such descriptor file is found.
   */
  public DependencyDescriptor loadDescriptor(String name)
  {
  	String mn = "loadDescriptor";
    DependencyDescriptor descriptor = null;
    File descriptorFile = getDescriptorFile(name);
    if (descriptorFile != null && descriptorFile.exists())
    {
      // deserialize
      try
      {
        descriptor = (DependencyDescriptor)_deser.deserialize(null, descriptorFile.getAbsolutePath());
      }
      catch (Throwable ex)
      {
        logError(ILogErrorCodes.OBJECT_DESERIALIZE_FROM_XML_FILE, mn, "Failed to deserialize descriptor file: ["+descriptorFile.getAbsolutePath()+"]. Error: "+ex.getMessage(), ex);
      }
    }

    return descriptor;
  }

  /**
   * Obtain a descriptor file using the specified name.
   * 
   * @param name The name portion of the descriptor filename pattern.
   * @return The File handle to the descriptor file, or <b>null</b> if no
   * such file exists.
   */  
  private File getDescriptorFile(String name)
  {
    File file = null;
    try
    {
      String filename = MessageFormat.format(_config.getDescriptorNamePattern(), new Object[]{name});
      file = new File(_config.getDescriptorPath(), filename);
    }
    catch (Throwable ex)
    {
      Log.error(ILogErrorCodes.DESCRIPTOR_FILE_RETRIEVE, Log.DB, "[DependencyDescriptorHandler.getDescriptorFile] Unexpected Error: "+ex.getMessage(), ex);
    }

    return file;
  }
  
  private static void logError(String errorCode, String methodName, String msg, Throwable t)
  {
  	StringBuffer buf = new StringBuffer("[");
  	buf.append(DependencyDescriptor.class.getSimpleName()).append(".").append(methodName).append("] ").append(msg);
  	Log.error(errorCode, Log.DB, buf.toString(), t);
  }
}
