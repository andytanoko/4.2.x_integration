/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SessionData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 05 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.session.model;

import java.io.*;

/**
 * A wrapper for a piece of data to be saved for a session.
 *
 * @author Neo Sok Lay
 * @version 2.0
 * @since 2.0
 */
public class SessionData implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 995969793281003019L;
	private String _key;
  private byte[] _contents;

  /**
   * Constuct a SessionData object based on another SessionData object.
   * This is done by copying the contents to this new constructed SessionData
   * object.
   * @param sessionData The SessionData object to construct from.
   */
  public SessionData(SessionData sessionData)
  {
    this(sessionData.getDataKey(), sessionData.getDataContents());
  }

  /**
   * Construct a SessionData object
   *
   * @param key The key to identify this piece of data in the session
   * @param data The data to saved. This data will be converted to bytes to
   * be saved.
   */
  public SessionData(String key, Object data)
  {
    _key = key;
    _contents = convertToDataContents(data);
  }

  /**
   * Get the key to this piece of data stored in the session
   */
  public String getDataKey()
  {
    return _key;
  }

  /**
   * Get the byte contents of this piece of session data.
   */
  public byte[] getDataContents()
  {
    return _contents;
  }

  /**
   * Set the byte contents of this piece of session data.
   */
  public void setDataContents(byte[] contents)
  {
    _contents = contents;
  }

  /**
   * Convert the session data to bytes.
   *
   * @param data the data to convert.
   */
  private byte[] convertToDataContents(Object data)
  {
    if (data == null)
      return null;

    if (data instanceof byte[])
      return (byte[])data;

    ByteArrayOutputStream baos = null;
    ObjectOutputStream objOS = null;

    try
    {
      //write
      baos = new ByteArrayOutputStream();
      objOS = new ObjectOutputStream(new BufferedOutputStream(baos));

      objOS.writeObject(data);
      objOS.close();

      //return the object in byte content
      return baos.toByteArray();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
    finally
    {
      try
      {
        if (objOS != null) objOS.close();
      }
      catch (Exception ex) {}
    }
  }

  /**
   * Get the data from the byte contents.
   */
  public Object getData()
  {
    if (_contents == null)
      return null;

    ByteArrayInputStream bais = null;
    ObjectInputStream objIS = null;

    try
    {
      //read
      bais = new ByteArrayInputStream(_contents);
      objIS = new ObjectInputStream(new BufferedInputStream(bais));
      Object ret = objIS.readObject();
      objIS.close();

      return ret;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
      return null;
    }
    finally
    {
      try
      {
        if (objIS != null) objIS.close();
      }
      catch (Exception ex) {}
    }
  }

}