/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDataObject.java
 * This class is modified from GT 1.x: com.gridnode.db.IDataObject
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 07 2000    Neo Sok Lay         Created
 * May 02 2002    Neo Sok Lay         Add getVersion() method.
 * Aug 22 2002    Neo Sok Lay         Add (de)serializing (from)to xml functionality.
 */
package com.gridnode.pdip.framework.db;
import java.io.StringWriter;
import java.io.StringReader;

/**
 * A basic data object interface.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 1.0a build 0.9.9.6
 */
public interface IDataObject
{
  /**
   * Get the identity of the data object.
   *
   * @return Unique Id for the object
   *
   * @since 1.0a build 0.9.9.6
   */
  long getUId();

  /**
   * Make a copy of the data object. Follows the contract of
   * {@link Object#clone Object.clone()}.
   *
   * @return a full copy of the object
   */
  Object clone();

    /**
   * Get the version of this data object.
   *
   * @return The version of this data object.
   *
   * @since 2.0
   */
  public double getVersion();

  /**
   * Serialize this data object to a file in xml format.
   *
   * @param toFile Output filename.
   * @exception Exception Error in serializing.
   * @since 2.0 I4
   */
  public void serialize(String toFile) throws Exception;

  /**
   * Serialize this data object to a StringWriter in xml format.
   *
   * @param writer The StringWriter.
   * @exception Exception Error in serializing.
   * @since 2.0 I4
   */
  public void serialize(StringWriter writer) throws Exception;

  /**
   * Deserialize this data object from a file in xml format.
   *
   * @param fromFile Input filename.
   * @exception Exception Error in deserializing.
   * @since 2.0 I4
   */
  public IDataObject deserialize(String fromFile) throws Exception;

  /**
   * Deserialize this data object from a StringReader in xml format.
   *
   * @param reader Input StringReader.
   * @exception Exception Error in deserializing.
   * @since 2.0 I4
   */
  public IDataObject deserialize(StringReader reader) throws Exception;
}