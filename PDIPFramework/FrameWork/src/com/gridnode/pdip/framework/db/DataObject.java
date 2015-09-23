/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DataObject.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Apr 29 2002    Neo Sok Lay         Add version attribute.
 * May 15 2002    Neo Sok Lay         Use BigDecimal to increment the version.
 * Aug 22 2002    Neo Sok Lay         Add (de)serializing (from)to xml functionality.
 */
package com.gridnode.pdip.framework.db;

import java.io.*;
import java.math.BigDecimal;

/**
 * A general data object implementation. Most JDO objects should extend
 * from this class.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 1.0a build 0.9.9.6
 */
public class DataObject implements IDataObject, Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1919973270991075495L;
	protected transient ObjectXmlSerializer _ser;
  protected transient XmlObjectDeserializer _deser;

  protected long _uId;
  protected double _version = 1;

  public DataObject()
  {
    _ser = new ObjectXmlSerializer();
    _deser = new XmlObjectDeserializer();
  }

  /**
   * Sets the UID of the data object.
   * <BR>NOTE: This method is required for JDO marhsalling/unmarshalling.
   *
   * @param uId UID
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setUId(long uId)
  {
    _uId = uId;
  }

  /**
   * Get the identity of this data object.
   * <BR>NOTE: This method is required for JDO marhsalling/unmarshalling.
   *
   * @return UID of this data object
   *
   * @since 1.0a build 0.9.9.6
   */
  public long getUId()
  {
    return _uId;
  }

  /**
   * Set the version of this data object.
   *
   * @param version The version of this data object.
   *
   * @since 2.0
   */
  public void setVersion(double version)
  {
    _version = version;
  }

  /**
   * Get the version of this data object.
   *
   * @return The version of this data object.
   *
   * @since 2.0
   */
  public double getVersion()
  {
    return _version;
  }

  /**
   * Increment the current version of this data object.
   *
   * @since 2.0
   */
  public void increaseVersion()
  {
    //150502NSL: Cannot just add to the version
    //will cause precision problem.
    //_version += 0.00001;
    //must use string
    BigDecimal version = new BigDecimal(""+getVersion());
    version = version.add(new BigDecimal(0.00001));
    setVersion(version.doubleValue());
  }

  /**
   * Make a copy of this data object.
   *
   * @return A copy of this data object, or <B>null</B> if error in cloning
   * the data object
   *
   * @since 1.0a build 0.9.9.6
   */
  public Object clone()
  {
    ByteArrayOutputStream baos = null;
    ObjectOutputStream objOS = null;
    ByteArrayInputStream bais = null;
    ObjectInputStream objIS = null;
    try
    {
      //write
      baos = new ByteArrayOutputStream();
      objOS = new ObjectOutputStream(new BufferedOutputStream(baos));
      objOS.writeObject(this);
      objOS.close();

      //read
      bais = new ByteArrayInputStream(baos.toByteArray());
      objIS = new ObjectInputStream(new BufferedInputStream(bais));
      Object ret = objIS.readObject();
      objIS.close();
      return ret;
    }
    catch (Exception ex)
    {
      err("[DataObject.clone]", ex);
      return null;
    }
    finally
    {
      try
      {
        if (objOS != null) objOS.close();
        if (objIS != null) objIS.close();
      }
      catch (Exception ex) {}
    }
  }

  public void serialize(String toFile) throws Exception
  {
    preSerialize();
    if (_ser == null)
      _ser = new ObjectXmlSerializer();
    _ser.serialize(this, toFile);
    postSerialize();
  }

  public void serialize(StringWriter writer) throws Exception
  {
    preSerialize();
    if (_ser == null)
      _ser = new ObjectXmlSerializer();
    _ser.serialize(this, writer);
    postSerialize();
  }

  /**
   * Call just before the object is being serialized into XML format.
   * This allows some pre-processing (e.g. conversion, initialization) to
   * be performed before serialization take on.
   */
  public void preSerialize()
  {
  }

  /**
   * Called immediately after the object has been serialized to XML format.
   * This allows for some post-processing (e.g. clean-up)
   * to be done after serialization.
   */
  public void postSerialize()
  {
  }

  public IDataObject deserialize(String fromFile) throws Exception
  {
    if (_deser == null)
      _deser = new XmlObjectDeserializer();
    DataObject obj = (DataObject)_deser.deserialize(this.getClass(), fromFile);
    obj.postDeserialize();

    return obj;
  }

  public IDataObject deserialize(StringReader reader) throws Exception
  {
    if (_deser == null)
      _deser = new XmlObjectDeserializer();
    DataObject obj = (DataObject)_deser.deserialize(this.getClass(), reader);
    obj.postDeserialize();

    return obj;
  }

  /**
   * Called immediately after the object has been deserialized from XML format.
   * This allows for some post-processing (e.g. conversion, re-initialization)
   * to be done after deserialization.
   */
  public void postDeserialize()
  {
  }

  // ******************* Logging methods *****************
  /**
   * Log a message.
   *
   * @param msg The message to log.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected static void log(String msg)
  {
    System.out.println(msg);
  }

  /**
   * Log an error.
   *
   * @param msg The error message.
   * @param ex The exception error.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected static void err(String msg, Exception ex)
  {
    if (msg != null)
      System.out.println(msg);
    if (ex != null)
      ex.printStackTrace();
  }

  /**
   * Log a debugging message.
   *
   * @param msg The debug message.
   *
   * @since 1.0a build 0.9.9.6
   */
  protected static void debug(String msg)
  {
    System.out.println(msg);
  }
}