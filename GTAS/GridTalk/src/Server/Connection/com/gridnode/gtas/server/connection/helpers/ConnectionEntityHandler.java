/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConnectionEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import com.gridnode.gtas.server.connection.exceptions.FieldValidationException;
import com.gridnode.gtas.server.connection.model.AbstractXmlEntity;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

/**
 * This EntityHandler handles the persistent entities on Xml files..
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public abstract class ConnectionEntityHandler
  implements IConnectionConfig
{
  //private static Configuration _config = null;
  private Configuration _config = null;
  private String _entityFileKey;
  private final Object _lock = new Object();
  protected final int MIN_INDEX = 0;
  protected final int MAX_INDEX = 1;

  private ConnectionEntityHandler() throws Throwable
  {
    loadConfig();
  }

  protected ConnectionEntityHandler(String entityFileKey) throws Throwable
  {
    this();
    _entityFileKey = entityFileKey;
  }

  /**
   * Retrieve the entity..
   *
   * @return The most current entity saved.
   */
  public final AbstractXmlEntity retrieve() throws Throwable
  {
    return readEntity();
  }

  /**
   * Save Connection back to entity file.
   *
   * @param setting The modified Connection entity.
   */
  public final void save(AbstractXmlEntity entity)
    throws Throwable
  {
    validate(entity);

    // save to xml file
    String filename = _config.getString(_entityFileKey);
    File tmpFile = new File(filename);
    try
    {
      synchronized (_lock)
      {
        entity.serialize(tmpFile.getAbsolutePath());
        /**@todo to be replaced by replaceFile */
        if (FileUtil.exist(PATH_CONNECTION, filename))
          FileUtil.delete(PATH_CONNECTION, filename);
        FileUtil.create(PATH_CONNECTION, filename, new FileInputStream(tmpFile));
      }
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
    throws Throwable
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
    throws FieldValidationException
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
    throws FieldValidationException
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
  protected final void validateString(String val, String err) throws FieldValidationException
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
   * @since 2.0 I6
   */
  protected final boolean validateNumRange(int num, int min, int max)
  {
    if (num < min || num > max)
      return false;
    return true;
  }

  protected final void validateNumRange(Integer numInt, int min, int max, String err)
    throws FieldValidationException
  {
    int num = getInt(numInt, err);
    if (!validateNumRange(num, min, max))
      throw new FieldValidationException(err + "- Expected Range: "+min + "-"+max);
  }
  /**
   * Validates an integer to be not lower than a limit.
   *
   * @param num The integer to validate.
   * @param min The low boundary (limit)
   * @return <B>true</B> if <I>num</I> is not lower than <I>min</I>, otherwise
   * <B>false</B>.
   *
   * @since 2.0 I6
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
    AbstractXmlEntity entity = getDefaultEntity();
    File entityFile = getEntityFile();
    if (entityFile != null)
    {
      // deserialize
      try
      {
        entity = (AbstractXmlEntity)entity.deserialize(entityFile.getAbsolutePath());
      }
      catch (Throwable ex)
      {
        Logger.err("[ConnectionEntityHandler.readEntity] Error, return default entity ", ex);
      }
    }
    // else using default setting.

    return entity;
  }

  /**
   * Get a default instance for the Xml persistent entity that the
   * concrete handler is handling.
   */
  protected abstract AbstractXmlEntity getDefaultEntity();

  /**
   * Obtain the Xml Persistent entity file handle.
   *
   * @return The Xml Persistent entity file handle (local copy).
   */
  private File getEntityFile()
  {
    File file = null;
    try
    {
      file = FileUtil.getFile(PATH_CONNECTION, _config.getString(_entityFileKey));
    }
    catch (Throwable ex)
    {
      Logger.err("[ConnectionEntityHandler.getEntityFile] Error, return default entity ", ex);
    }

    return file;
  }

  /**
   * Get the Connection configuration.
   *
   * @return the loaded Configuration for Connection module.
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
  private void loadConfig() throws Throwable
  {
    _config = ConfigurationManager.getInstance().getConfig(CONFIG_NAME);
    if (_config == null)
      throw new Exception("Connection configuration file not found!");

    readConfig();
  }

  /**
   * Read sub-class specific configurations from the Connection configuration
   * file.
   */
  protected abstract void readConfig() throws Throwable;
//  protected void readConfig() throws Throwable
//  {
//  }

  /**
   * Read the range setting for a specified property key from the Connection
   * configuration file.
   *
   * @param key The property key.
   * @return The Min and Max range setting for the specified property key.
   * @throws Exception Missing Min or Max value in the range setting, or
   * any of them is not a numeric digit.
   */
  protected final int[] readRange(String key) throws Throwable
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
}