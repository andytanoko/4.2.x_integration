/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ValidationUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 10 2006    Neo Sok Lay         Created
 */
package com.gridnode.util;

import com.gridnode.util.ValidationException;

import java.io.File;

/**
 * Util class for validation.
 * 
 * @author Neo Sok Lay
 */
public class ValidationUtil
{
  /**
   * Checks if a file-type property points to a valid physical file.
   *
   * @param propertyName The name of the property.
   * @param parentDir Parent directory for the file, if any.
   * @param filename The name of the file.
   * @param isDirectory Whether the file should be a directory.
   * @throws ValidationException Filename is not specified, or the file does not exists, or
   * the file is not a directory where it should be, or the file is a directory where it
   * should not be.
   */
  public static void checkFileExists(
    String propertyName,
    String parentDir,
    String filename,
    boolean isDirectory)
    throws ValidationException
  {
    checkNonBlank(propertyName, filename);

    File f = new File(parentDir, filename);
    if (!f.exists())
      throw new ValidationException(
        propertyName + " " + filename + " does not exists!");

    if (isDirectory && !f.isDirectory())
      throw new ValidationException(
        propertyName + " " + filename + " must be a directory!");

    if (!isDirectory && f.isDirectory())
      throw new ValidationException(
        propertyName + " " + filename + " must not be a directory!");
  }

  /**
   * Check that a directory property contains some files.
   * 
   * @param propertyName The name of the property.
   * @param parentDir The parent directory of the directory property
   * @param filename The filename of the directory.
   * @throws ValidationException The directory property is not an existing directory or
   * it does not contain any files inside.
   */
  public static void checkHasFiles(
    String propertyName,
    String parentDir,
    String filename)
    throws ValidationException
  {
    File directory = new File(parentDir, filename);
    if (!directory.isDirectory())
      throw new ValidationException(propertyName + " " + filename + " is not a directory!");
    
    File[] files = directory.listFiles();
    if (files.length==0)
      throw new ValidationException(propertyName + " " + filename + " does not contain any files!");     
  }
  
  /**
   * Checks whether a property value is specified, ie. not a null or contains only whitespaces.
   *
   * @param propertyName The name of the property
   * @param value The value to check.
   * @throws ValidationException The value is not specified.
   */
  public static void checkNonBlank(String propertyName, String value)
    throws ValidationException
  {
    if (StringUtil.isBlank(value))
      throw new ValidationException(
        propertyName + " is a required input property!");
  }

  /**
   * Check whether a property value is an allowed value.
   * 
   * @param propertyName The name of the property.
   * @param value The property value to check.
   * @param enumValues Array of permitted values.
   * @throws ValidationException If the property value is not specified, or the value is not
   * permitted (i.e. none of the enumValues match).
   */
  public static void checkEnumeration(String propertyName, String value, String[] enumValues)
    throws ValidationException
  {
    checkNonBlank(propertyName, value);
    boolean match = false;
    for (int i=0; i<enumValues.length && !match; i++)
    {
      if (value.equals(enumValues[i]))
        match = true;
    }
    
    if (!match)
      throw new ValidationException(propertyName + " " + value + " is not supported!");
  }
}