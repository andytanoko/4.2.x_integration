/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XmlEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 18 2003    Neo Sok Lay         Created
 * Sep 19 2003    Neo Sok Lay         Throw Exception instead of Throwable.
 */
package com.gridnode.pdip.framework.db;

import com.gridnode.pdip.framework.exceptions.FieldValidationException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.db.entity.AbstractXmlEntity;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.Configuration;

import java.util.List;
import java.io.FileInputStream;
import java.io.File;

/**
 * This EntityHandler handles the persistent entities on Xml files.
 * Modules should sub-class this XmlEntityHandler to override the default
 * behaviours e.g. field validation, default return value if file Xml file 
 * does not exists, etc.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since GT 2.2
 */
public class XmlEntityHandler
{
  private static Configuration _config = null;
  private IXmlEntityConfig _xmlConfig;
  private String _entityFileKey;
  protected final int MIN_INDEX = 0;
  protected final int MAX_INDEX = 1;

  private XmlEntityHandler(IXmlEntityConfig xmlConfig) throws Exception
  {
    _xmlConfig = xmlConfig;
    loadConfig();
  }

  public XmlEntityHandler(IXmlEntityConfig xmlConfig, String entityFileKey) throws Exception
  {
    this(xmlConfig);
    _entityFileKey = entityFileKey;
  }

  /**
   * Retrieve the entity..
   *
   * @return The most current entity saved.
   */
  public synchronized final AbstractXmlEntity retrieve() throws Exception
  {
    return readEntity();
  }

  /**
   * Save the XmlEntity back to entity file.
   *
   * @param entity The modified Xml entity.
   */
  public synchronized final void save(AbstractXmlEntity entity)
    throws Exception
  {
    validate(entity);

    // save to xml file
    String filename = _config.getString(_entityFileKey);
    File tmpFile = new File(filename);
    try
    {
      entity.serialize(tmpFile.getAbsolutePath());
      FileUtil.replace(_xmlConfig.getStoragePathKey(), filename, new FileInputStream(tmpFile));
    }
    finally
    {
      //delete temporary file after use
      if (tmpFile.exists())
        tmpFile.delete();
    }

  }


  // *****************  Utility methods *******************************

  /**
   * Validate the fields in the specified Xml persistent entity.
   *
   * @param setting The Xml persistent entity to be validated.
   */
  protected void validate(AbstractXmlEntity entity)
    throws Exception
  {
  }

  /**
   * Get the primitive short value from an object Short.
   *
   * @param field The Short value.
   * @param err The error message if field is null.
   * @throws FieldValidationException If field is null.
   */
  protected final short getShort(Short field, String err)
    throws Exception
  {
    if (field == null)
       throw new FieldValidationException(err);

    return field.shortValue();
  }

  /**
   * Get the primitive int value from an object Integer.
   *
   * @param field The Integer value.
   * @param err The error message if field is null.
   * @throws FieldValidationException If field is null.
   */
  protected final int getInt(Integer field, String err)
    throws Exception
  {
    if (field == null)
       throw new FieldValidationException(err);

    return field.intValue();
  }

  /**
   * Validate that the specified String is not null and empty.
   *
   * @param val The String value.
   * @param err The error message if val is null or empty.
   * @throws FieldValidationException If field is null or empty.
   */
  protected final void validateString(String val, String err) throws Exception
  {
    if (val == null || val.trim().length() == 0)
      throw new FieldValidationException(err);
  }

  /**
   * Validates an integer to be within a defined range.
   *
   * @param num The integer to validate
   * @param min The low boundary of the range
   * @param max The max boundary of the range
   * @return <B>true</B> if <I>num</I> is between <I>min</I> and <I>max</I>
   * inclusive. <B>false</B> otherwise.
   *
   * @since GT 2.2
   */
  protected final boolean validateNumRange(int num, int min, int max)
  {
    if (num < min || num > max)
      return false;
    return true;
  }

  /**
   * Validates an integer to be not lower than a limit.
   *
   * @param num The integer to validate.
   * @param min The low boundary (limit)
   * @return <B>true</B> if <I>num</I> is not lower than <I>min</I>, otherwise
   * <B>false</B>.
   *
   * @since GT 2.2
   */
  protected final boolean validateMin(int num, int min)
  {
    if (num < min)
      return false;
    return true;
  }

  /**
   * Read the Xml persistent entity from the entity file. This method guarantees
   * a non-null entity is returned. A default entity
   * is return if the entity file does not exist or the system could not read the
   * file.
   *
   * @return The Xml persistent entity read.
   */
  private AbstractXmlEntity readEntity()
  {
  	String mn = "readEntity";
    AbstractXmlEntity entity = getDefaultEntity();
    File entityFile = getEntityFile();
    if (entityFile != null)
    {
      // deserialize
      try
      {
        entity = (AbstractXmlEntity)entity.deserialize(entityFile.getAbsolutePath());
      }
      catch (Exception ex)
      {
        logError(ILogErrorCodes.OBJECT_DESERIALIZE_FROM_XML_FILE, mn, "Failed to deserialize entity from file :["+entityFile.getAbsolutePath()+"]. Error: "+ex.getMessage(), ex);
      }
    }
    // else using default setting.

    return entity;
  }

  /**
   * Get a default instance for the Xml persistent entity that the
   * concrete handler is handling. 
   * 
   * @return The default XmlEntity instance to return. Returns <b>null</b>
   * by default. Sub-classes should overwrite this method to return
   * non-null instance.
   */
  protected AbstractXmlEntity getDefaultEntity()
  {
    return null;
  }

  /**
   * Obtain the Xml Persistent entity file handle.
   *
   * @return The Xml Persistent entity file handle (local copy).
   */
  private File getEntityFile()
  {
  	String mn = "getEntityFile";
    File file = null;
    try
    {
      file = FileUtil.getFile(_xmlConfig.getStoragePathKey(), _config.getString(_entityFileKey));
    }
		catch (FileAccessException e)
		{
      logError(ILogErrorCodes.XML_ENTITY_FILE_ACCESS, mn, "Failed to get file. Error: ", e);
		}
    catch (Exception ex)
    {
      logError(ILogErrorCodes.XML_ENTITY_FILE_ACCESS, mn, "Failed to get file. Unexpected error: "+ex.getMessage(), ex);
    }

    return file;
  }

  /**
   * Get the configuration.
   *
   * @return the loaded Configuration.
   */
  protected Configuration getConfig()
  {
    return _config;
  }

  /**
   * Loads the Connection configurations.
   *
   * @throws Exception The Connection configuration could not be loaded.
   */
  private void loadConfig() throws Exception
  {
    _config = ConfigurationManager.getInstance().getConfig(_xmlConfig.getConfigName());
    if (_config == null)
      throw new Exception(_xmlConfig.getConfigName()+ " Xml configuration file not found!");

    readConfig();
  }

  /**
   * Read sub-class specific configurations from the configuration
   * file.
   */
  protected void readConfig() throws Exception
  {
  }

  /**
   * Read the range setting for a specified property key from the 
   * configuration file.
   *
   * @param key The property key.
   * @return The Min and Max range setting for the specified property key.
   * @throws Exception Missing Min or Max value in the range setting, or
   * any of them is not a numeric digit.
   */
  protected final int[] readRange(String key) throws Exception
  {
    String err = key + " property is not configured properly!";
    List range = _config.getList(key, ",");
    if (range.size() < 2)
      throw new Exception(err + ": Min and Max required.");

    int[] intRange = new int[2];
    try
    {
      intRange[MIN_INDEX] = Integer.parseInt((String)range.get(MIN_INDEX));
      intRange[MAX_INDEX] = Integer.parseInt((String)range.get(MAX_INDEX));
    }
    catch (NumberFormatException ex)
    {
      throw new Exception(err + ": "+ex.getMessage());
    }
    return intRange;
  }
  
  private static void logError(String errorCode, String methodName, String msg, Throwable t)
  {
  	StringBuffer buf = new StringBuffer("[");
  	buf.append(XmlEntityHandler.class.getSimpleName()).append(".").append(methodName).append("] ").append(msg);
  	Log.error(errorCode, Log.DB, buf.toString(), t);
  }
}