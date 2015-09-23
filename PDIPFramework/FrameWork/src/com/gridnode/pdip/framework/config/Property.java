package com.gridnode.pdip.framework.config;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class Property
{
  private String _value = "";
  private String _key;
  //private Configuration _config;

  public Property()
  {
  }

  /**
   * Get the key of this property.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return The key of the property
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Set the key of this property.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param key The key
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setKey(String key)
  {
    _key = key;
  }

  /**
   * Get the value of this property.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @return The value of the property
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getValue()
  {
    return _value;
  }

  /**
   * Set the value of the property.
   * <BR>NOTE: This method is required for JDO marshalling/unmarshalling.
   *
   * @param value The value to set
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setValue(String value)
  {
    _value = (value == null)? "" : value;
  }

  /**
   * Gives a short description of this property object.
   *
   * @return A description of the property
   *
   * @since 1.0a build 0.9.9.6
   */
  public String toString()
  {
    return getKey()+":"+getValue();
  }

  /**
   * Set the configuration object that this property belongs to.
   *
   * @param config The Configuration object
   *
   * @since 1.0a build 0.9.9.6
   *//*
  public void setConfiguration(Configuration config)
  {
    _config = config;
  }*/

  // ******************** Helper methods **********************

  /**
   * Get the value of this property.
   *
   * @param defaultVal The value to return if its value is null.
   * @return The value of this property, or <B>null</B> if value is null.
   *
   */
  public String getValue(String defaultVal)
  {
    if (_value == null)
      return defaultVal;
    return _value;
  }

  /**
   * Get the value of this property in Short.
   *
   * @return Short value of this property. <B>null</B> if value is not numeric.
   *
   */
  public Short getShortValue()
  {
    try
    {
      return Short.decode(getValue());
    }
    catch (NumberFormatException ex)
    {
      return null;
    }
  }

  /**
   * Get the value of this property in Short.
   *
   * @param dafaultValue The value to return if property value is not numeric
   * @return Short value of this property. <I>defaultValue</I> if value is
   * not numeric.
   *
   */
  public Short getShortValue(short defaultValue)
  {
    try
    {
      return Short.decode(getValue());
    }
    catch (NumberFormatException ex)
    {
      return new Short(defaultValue);
    }
  }

  /**
   * Get the value of this property in Integer.
   *
   * @return Integer value of this property. <B>null</B> if value is not numeric
   *
   */
  public Integer getIntegerValue()
  {
    try
    {
      return Integer.decode(getValue());
    }
    catch (NumberFormatException ex)
    {
      return null;
    }
  }

  /**
   * Get the value of this property in Integer.
   *
   * @param dafaultValue The value to return if property value is not numeric
   * @return Integer value of this property. <I>defaultValue</I> if value is not
   * numeric.
   *
   */
  public Integer getIntegerValue(int defaultValue)
  {
    try
    {
      return Integer.decode(getValue());
    }
    catch (NumberFormatException ex)
    {
      return new Integer(defaultValue);
    }
  }

  /**
   * Get the value of this property in Double.
   *
   * @return Double value of this property. <B>null</B> if value is not numeric.
   *
   */
  public Double getDoubleValue()
  {
    try
    {
      return Double.valueOf(getValue());
    }
    catch (NumberFormatException ex)
    {
      return null;
    }
  }

  /**
   * Get the value of this property in Double.
   *
   * @param dafaultValue The value to return if property value is not numeric
   * @return Double value of this property. <I>defaultValue</I> if value is not
   * numeric.
   *
   */
  public Double getDoubleValue(double defaultValue)
  {
    try
    {
      return Double.valueOf(getValue());
    }
    catch (NumberFormatException ex)
    {
      return new Double(defaultValue);
    }
  }

  /**
   * Get the value of this property in Long.
   *
   * @return Long value of this property. <B>null</B> if value is not numeric.
   *
   */
  public Long getLongValue()
  {
    try
    {
      return Long.decode(getValue());
    }
    catch (NumberFormatException ex)
    {
      return null;
    }
  }

  /**
   * Get the value of this property in Long.
   *
   * @param dafaultValue The value to return if property value is not numeric
   * @return Long value of this property. <I>defaultValue</I> if value is not
   * numeric.
   *
   */
  public Long getLongValue(long defaultValue)
  {
    try
    {
      return Long.decode(getValue());
    }
    catch (NumberFormatException ex)
    {
      return new Long(defaultValue);
    }
  }

  /**
   * Get the value of this property in Boolean.
   *
   * @return Boolean value of this property. <B>null</B> if value is not numeric
   *
   */
  public Boolean getBooleanValue()
  {
    return Boolean.valueOf(getValue());
  }
}