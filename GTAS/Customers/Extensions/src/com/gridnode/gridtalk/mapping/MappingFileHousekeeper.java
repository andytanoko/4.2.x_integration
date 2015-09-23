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
 * File: MappingFileHousekeeper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 3, 2010   Tam Wei Xiang       Created
 */
package com.gridnode.gridtalk.mapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.util.SystemUtil;

/**
 * @author Tam Wei xiang
 * @since 4.2.2
 */
public class MappingFileHousekeeper
{
  public MappingFileHousekeeper()
  {
    
  }
  
  /**
   * 
   * @param mapFileRegistryDir The directory storing list of meta data of mapping file that will be housekept
   */
  //gtas\data\sys\mapping\javabinary\jarMarkedAsDeleted
  public void housekeepMappingFile(String mapFileRegistryDir, String mapFileDir, String mapfileRegistryFilenamePattern) throws ApplicationException
  {
    File workingDir = SystemUtil.getWorkingDir();
    File mappingFileRegistryDir = new File(workingDir+"/"+mapFileRegistryDir);
    if(!mappingFileRegistryDir.exists())
    {
      System.out.println("MappingFileHousekeeper mapFileRegistryDir="+mappingFileRegistryDir.getAbsolutePath()+" does not exists!");
      return;
    }
    File[] mapFileRegistries = mappingFileRegistryDir.listFiles(new MappingFileRegistryFilter(mapfileRegistryFilenamePattern));
    
    if(mapFileRegistries != null && mapFileRegistries.length > 0)
    {
      for(File fileRegistry : mapFileRegistries)
      {
        if(deleteMappingFile(fileRegistry, new File(workingDir+"/"+mapFileDir)))
        {
          boolean isDeleted = fileRegistry.delete();
          if(! isDeleted)
          {
            System.out.println("Mapping file registry="+fileRegistry.getAbsolutePath()+" can not be deleted!");
          }
        }
      }
    }
  }
  
  private boolean deleteMappingFile(File fileRegistry, File mapFileDir) throws ApplicationException
  {
    FileReader reader = null;
    try
    {
      reader = new FileReader(fileRegistry);
      BufferedReader buffReader = new BufferedReader(reader);
      String mapFilename = buffReader.readLine();
      
      File mapFile = new File(mapFileDir.getAbsoluteFile()+"/"+mapFilename);
      if(mapFile.exists())
      {
        boolean isDeleted = mapFile.delete();
        if(!isDeleted)
        {
          System.out.println("Map file="+mapFile.getAbsolutePath()+" can not be deleted!");
        }
        return isDeleted;
      }
      else
      {
        System.out.println("Map file="+mapFile.getAbsolutePath()+" does not exists!");
        return true;
      }
    } catch(Exception ex)
    {
      throw new ApplicationException("Failed to delete mapping file given map file registry="+fileRegistry.getAbsolutePath(), ex);
    }
    finally
    {
      if(reader != null)
      {
        try
        {
          reader.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    
  }
}

class MappingFileRegistryFilter implements FilenameFilter
{

  private String filenamePattern;
  
  public MappingFileRegistryFilter(String filenamePattern)
  {
    this.filenamePattern = filenamePattern;
  }
  
  public boolean accept(File dir, String name)
  {
    if(name.indexOf(filenamePattern) >= 0)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
}