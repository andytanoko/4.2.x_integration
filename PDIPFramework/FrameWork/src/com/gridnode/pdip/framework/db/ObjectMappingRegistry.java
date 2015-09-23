/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ObjectMappingRegistry.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 22 2002    Neo Sok Lay         Created
 * Oct 27 2005    Neo Sok Lay         Load mapping files relative to a workingDir.
 * Dec 22 2005    Neo Sok Lay         Load mapping files using URL instead of file path string.
 * Feb 07 2007		Alain Ah Ming				Use new error codes
 */
package com.gridnode.pdip.framework.db;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.SystemUtil;

/**
 * This registry loads on demand and keeps track of all class xml mapping files
 * loaded so far.
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 2.0 I4
 */
public class ObjectMappingRegistry
{
  private static final String ENTITY_CONFIG_NAME = "entity";
  private static final String OBJECT_MAPPING     = "object.mapping";

  private static final String LOG_CAT            = "ENTITY.MAPPING";

  private static final ObjectMappingRegistry _self = new ObjectMappingRegistry();
  //private static Object                _lock = new Object();

  private Configuration _objectMappingConfig;
  private Mapping       _mappingLoader;
  private List<String>          _loadedMaps;

  private ObjectMappingRegistry()
  {
    _mappingLoader = new Mapping( ObjectMappingRegistry.class.getClassLoader() );
    _mappingLoader.setBaseURL(SystemUtil.getWorkingDirURL().toExternalForm());
    _loadedMaps = new ArrayList<String>();
    loadConfiguration();
  }

  /**
   * Get an ObjectMappingRegistry instance.
   *
   * @return an ObjectMappingRegistry instance.
   *
   * @since 2.0 I4
   */
  public static ObjectMappingRegistry getInstance()
  {
    /*
    if (_self == null)
    {
      synchronized(_lock)
      {
        if (_self == null)
          _self = new ObjectMappingRegistry();
      }
    }
    */
    return _self;
  }

  private void loadConfiguration()
  {
    try
    {
      _objectMappingConfig = ConfigurationManager.getInstance().getConfig(
                               ENTITY_CONFIG_NAME);
      loadMappings();
    }
    catch (Exception ex)
    {
      Log.error(
              ILogErrorCodes.CONFIGURATION_LOAD,
        LOG_CAT,
        "[ObjectMappingRegistry.loadConfiguration] Fail to load configuration. Error: "+ex.getMessage(),
        ex);
    }
  }

  /**
   * Get the mapping file loader which contains all loaded mappings.
   *
   * @return The mapping file loader.
   *
   * @since 2.0
   */
  public Mapping getMappingLoader()
  {
    return _mappingLoader;
  }

  private void loadMappings() throws Exception
  {
  	String mn = "loadMappings";
    if (_objectMappingConfig == null)
      throw new Exception("ObjectMappingRegistry is null");

    List mappingList = _objectMappingConfig.getList(OBJECT_MAPPING, ",");
    String workingDir = SystemUtil.getWorkingDirPath(); //NSL20051027

    for (Iterator i=mappingList.iterator(); i.hasNext(); )
    {
    	String fileName = null;
      try
      {
      	fileName = (String)i.next();
        File mappingFile = new File(workingDir, fileName); //NSL20051222
        Log.debug(LOG_CAT, "[ObjectMappingRegistry.loadMappings] mappingFile.getCanonicalPath() ="+
          mappingFile.getCanonicalPath());
        loadMapping(fileName, mappingFile.toURL()); //NSL20051222

        //loadMapping((String)i.next());
      }
			catch (IOException e)
			{
				logError(ILogErrorCodes.OBJECT_MAPPING_LOAD, 
				         mn, 
				         "Failed to get canonical path or URL of mapping file. Error: "+e.getMessage(), 
				         e);
			}
      catch (Exception e)
      {
				logError(ILogErrorCodes.OBJECT_MAPPING_LOAD, 
				         mn, 
				         "Failed to load mapping file:["+fileName+"]. Error: "+e.getMessage(), 
				         e);
      }
    }
  }

  /**
   * Loads, if necessary, a mapping file. If the mapping file has already been
   * loaded before, no attempt would be made to load the mapping file again.
   *
   * @param mappingKey The identifier for the mapping file, as specified in
   * the <b>entity</b> configuration properties.
   * @param mappingFileURL the actual URL to the mapping file to load. 
   * @return <b>true</b> if the mapping file loader has at least one mapping
   * loaded, <b>false</b> otherwise. Note that if the mapping file loader does
   * not have any mapping loaded, no (de)serialization can be done using the
   * mapping file loader.
   * @exception Exception
   *
   * @since 2.0 I4
   */
  public boolean loadMapping(String mappingKey, URL mappingFileURL)
    throws Exception
  {
  	//NSL20051222 Use URL to load mapping files
    if (mappingKey != null && !_loadedMaps.contains(mappingKey))
    {
      try
      {
        _mappingLoader.loadMapping(mappingFileURL);
        _loadedMaps.add(mappingKey);
      }
			catch (IOException e)
			{
				throw new Exception("Castor mapping loading exception: " + e.getMessage());
			}
			catch (MappingException e)
			{
				throw new Exception("Castor mapping exception: " + e.getMessage());
			}
      catch (Exception ex)
      {
        throw new Exception("Unexpected error: "+ex.getMessage());
      }
    }

    return (_loadedMaps.size() > 0);
  }

  /**
   * Checks if any mappings have been successfully loaded so far.
   *
   * @return <b>true</b> if at least one mapping file has been successfully
   * loaded.
   *
   * @since 2.0 I4
   */
  public boolean hasLoadedMappings()
  {
    return _loadedMaps.size() > 0;
  }
  
  private static void logError(String errorCode, String methodName, String msg, Throwable t)
  {
  	StringBuffer buf = new StringBuffer("[");
  	buf.append(ObjectMappingRegistry.class.getSimpleName()).append(".").append(methodName).append("] ").append(msg);
  	Log.error(errorCode, Log.DB, buf.toString(), t);
  }

}