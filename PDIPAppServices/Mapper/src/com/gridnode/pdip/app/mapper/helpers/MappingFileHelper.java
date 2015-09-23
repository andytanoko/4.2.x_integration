/**
 * PROPRIETARY AND CONFIDENTIALITY NOTICE
 *
 * The code contained herein is confidential information and is the property 
 * of CrimsonLogic eTrade Services Pte Ltd. It contains copyrighted material 
 * protected by law and applicable international treaties. Copying,         
 * reproduction, distribution, transmission, disclosure or use in any manner 
 * is strictly prohibited without the prior written consent of Crimsonlogic 
 * eTrade Services Pte Ltd. Parties infringing upon such rights may be      
 * subject to civil as well as criminal liability. All rights are reserved. 
 *
 * File: MappingFileHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 3, 2010    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.mapper.helpers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.UUIDUtil;

/**
 * @author Tam Wei Xiang
 * @since GT4.2.2
 */
public class MappingFileHelper
{
  public static final String MAPPING_JAR_DELETED_FOLDER = "jarMarkedAsDeleted";
  private static final String MAPPING_FILENAME_PREFIX = "mapfile_";
  
  /**
   * Record down the mapping file that will be deleted after the system is restarted.
   * @param pathKey The java binary path key
   * @param filename The filename of the java binary
   * @throws FileAccessException thrown if encountering error in locating the path key corresponding file
   * @throws SystemException thrown if failed to write the filename into a random generated file.
   */
  public static void markMappingFileAsDeleted(String pathKey, String filename) throws FileAccessException, SystemException
  {
    File mapFile = FileUtil.getFile(pathKey, MAPPING_JAR_DELETED_FOLDER, "");
    outputFilename(filename, mapFile.getAbsolutePath());
  }
  
  private static void outputFilename(String filename, String targetFolder) throws SystemException
  {
    File f = new File(targetFolder+"/"+MAPPING_FILENAME_PREFIX+UUIDUtil.getRandomUUIDInStr()+".txt");
    FileWriter writer = null;
    try
    {
      writer = new FileWriter(f);
      writer.write(filename);
    } catch(IOException e)
    {
      throw new SystemException("Failed to record mapping filename="+filename+" for deletion", e);
    }
    finally
    {
      if(writer != null)
      {
        try
        {
          writer.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    
  }
  
}
