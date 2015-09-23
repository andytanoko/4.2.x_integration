package com.gridnode.pdip.framework.config;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Jagadeesh.
 * @version 1.0
 */
import java.util.*;
 
/**
 * <p>
 * The Configuration Object encapsulates the Configuration of per Module., specific
 * Configuration.
 *
 * <p>
 * A Configuration Object contains a list of Property - which represents a Key - Value pair.
 * And each Configuration is associated with a name.
 */


public class Configuration implements IConfigurationObserver
{
  private Vector _property = new Vector();
  private String _name;

  public Configuration()
  {
  }

  /**
   * Get the name of this configuration.
   *
   * @return The name of the configuration
   *
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Sets the name of the configuration.
   *
   * @param name The name of the configuration
   */
  public void setName(String name)
  {
    _name = name;
  }

  /**
   * Get all the properties in this configuration.
   *
   * @return Vector of the {@link Property properties} in the configuration
   *
   */
  public Vector getProperty()
  {
    return _property;
  }

  /**
   * Add a property into the configuration.
   *
   * @param prop The property to add
   *
   */
  public void addProperty(Property prop)
  {
    _property.add(prop);
    //prop.setConfiguration(this);
  }

  /**
   * Create a Property object.
   *
   * @return A new Property object instance
   *
   */
  public Property createProperty()
  {
    return new Property();
  }

  /**
   * Finds the property with the given key.
   *
   * @param key The key of the property
   * @return The Property found, or <B>null</B> if no property in the configuration
   * has the <I>key</I>.
   *
   */
  public Property getProperty(String key)
  {
    for (int i=0; i<_property.size(); i++)
    {
      Property prop = (Property)_property.get(i);
      if (prop.getKey().equalsIgnoreCase(key))
        return prop;
    }
    return null;
  }

  /**
   * Get the keys of all properties in this configuration.
   *
   * @return An enumeration of the keys (String) of all properties in
   * this configuration.
   *
   */
  public Enumeration getPropertyKeys()
  {
    Enumeration properties = getProperty().elements();
    Vector propKeys = new Vector();
    while (properties.hasMoreElements())
    {
      Property prop = (Property)properties.nextElement();
      propKeys.add(prop.getKey());
    }
    return propKeys.elements();
  }

  /**
   * Gives a short description of this configuration object.
   *
   * @return A summary of the properties in the configuration
   *
   */
  public String toString()
  {
    return "\nConfig:       " + getName() + "\n" +
           "  Properties: " + "\n" +
           "    " + _property.toString();
  }

  /**
   * Get a property value.
   *
   * @param propertyKey Key of the property
   * @return The string value of the property with key <I>propertyKey</I>.
   * <B>null</B> if no property with the key exists in the configuration.
   *
   */
  public String getString(String propertyKey)
  {
    try
    {
      return getProperty(propertyKey).getValue();
    }
    catch (Exception ex)
    {
      return "";
    }
  }

  /**
   * Get a property value.
   *
   * @param propertyKey Key of the property
   * @param defaultVal Default value to return if property does not exists
   * in the configuration.
   * @return The string value of the property with key <I>propertyKey</I>.
   * <I>defaultVal</I> if no property with the key exists in the configuration.
   *
   */
  public String getString(String propertyKey, String defaultVal)
  {
    try
    {
      return getProperty(propertyKey).getValue(defaultVal);
    }
    catch (Exception ex)
    {
      return defaultVal;
    }
  }

  /**
   * Get a property value.
   *
   * @param propertyKey Key of the property
   * @return The "int" value of the property with key <I>propertyKey</I>.
   * <B>0</B> if no property with the key exists in the configuration, or
   * value of the property cannot be converted to "int".
   *
   */
  public int getInt(String propertyKey)
  {
    try
    {
      return getProperty(propertyKey).getIntegerValue().intValue();
    }
    catch (Exception ex)
    {
      return 0;
    }
  }

  /**
   * Get a property value.
   *
   * @param propertyKey Key of the property
   * @param defaultVal Default value to return if property not found in
   * configuration.
   * @return The "int" value of the property with key <I>propertyKey</I>.
   * <B>defaultVal</B> if no property with the key exists in the configuration, or
   * value of the property cannot be converted to "int".
   *
   */
  public int getInt(String propertyKey, int defaultVal)
  {
    try
    {
      return getProperty(propertyKey).getIntegerValue(defaultVal).intValue();
    }
    catch (Exception ex)
    {
      return defaultVal;
    }
  }

  /**
   * Get a property value.
   *
   * @param propertyKey Key of the property
   * @return The "boolean" value of the property with key <I>propertyKey</I>.
   * <B>false</B> if no property with the key exists in the configuration, or
   * value of the property cannot be converted to "boolean".
   *
   */
  public boolean getBoolean(String propertyKey)
  {
    try
    {
      return getProperty(propertyKey).getBooleanValue().booleanValue();
    }
    catch (Exception ex)
    {
      return false;
    }
  }

  /**
   * Get a property value.
   *
   * @param propertyKey Key of the property
   * @param defaultVal Default value to return if property not found in
   * configuration
   * @return The "boolean" value of the property with key <I>propertyKey</I>.
   * <B>defaultVal</B> if no property with the key exists in the configuration, or
   * value of the property cannot be converted to "boolean".
   *
   */
  public boolean getBoolean(String propertyKey, boolean defaultVal)
  {
    try
    {
      return getProperty(propertyKey).getBooleanValue().booleanValue();
    }
    catch (Exception ex)
    {
      return defaultVal;
    }
  }

  /**
   * Get a property value.
   *
   * @param propertyKey Key of the property
   * @return The "long" value of the property with key <I>propertyKey</I>.
   * <B>0</B> if no property with the key exists in the configuration, or
   * value of the property cannot be converted to "long".
   *
   */
  public long getLong(String propertyKey)
  {
    try
    {
      return getProperty(propertyKey).getLongValue().longValue();
    }
    catch (Exception ex)
    {
      return 0L;
    }
  }

  /**
   * Get a property value.
   *
   * @param propertyKey Key of the property
   * @param defaultVal Default value to return if property not found in the
   * configuration.
   * @return The "long" value of the property with key <I>propertyKey</I>.
   * <B>defaultVal</B> if no property with the key exists in the configuration, or
   * value of the property cannot be converted to "long".
   *
   */
  public long getLong(String propertyKey, long defaultVal)
  {
    try
    {
      return getProperty(propertyKey).getLongValue(defaultVal).longValue();
    }
    catch (Exception ex)
    {
      return defaultVal;
    }
  }

  /**
   * Get a property value.
   *
   * @param propertyKey Key of the property
   * @return The "short" value of the property with key <I>propertyKey</I>.
   * <B>0</B> if no property with the key exists in the configuration, or
   * value of the property cannot be converted to "short".
   *
   */
  public short getShort(String propertyKey)
  {
    try
    {
      return getProperty(propertyKey).getShortValue().shortValue();
    }
    catch (Exception ex)
    {
      return 0;
    }
  }

  /**
   * Get a property value.
   *
   * @param propertyKey Key of the property
   * @param defaultVal The default value to return if property not found in
   * the configuration.
   * @return The "short" value of the property with key <I>propertyKey</I>.
   * <B>defaultVal</B> if no property with the key exists in the configuration, or
   * value of the property cannot be converted to "short".
   *
   */
  public short getShort(String propertyKey, short defaultVal)
  {
    try
    {
      return getProperty(propertyKey).getShortValue(defaultVal).shortValue();
    }
    catch (Exception ex)
    {
      return defaultVal;
    }
  }

  /**
   * Get a property value.
   *
   * @param propertyKey Key of the property
   * @return The "double" value of the property with key <I>propertyKey</I>.
   * <B>0.0</B> if no property with the key exists in the configuration, or
   * value of the property cannot be converted to "double".
   *
   */
  public double getDouble(String propertyKey)
  {
    try
    {
      return getProperty(propertyKey).getDoubleValue().doubleValue();
    }
    catch (Exception ex)
    {
      return 0.0;
    }
  }

  /**
   * Get a property value.
   *
   * @param propertyKey Key of the property
   * @param defaultVal Default value to return if property not found in the
   * configuration.
   * @return The "int" value of the property with key <I>propertyKey</I>.
   * <B>defaultVal</B> if no property with the key exists in the configuration, or
   * value of the property cannot be converted to "double".
   *
   */
  public double getDouble(String propertyKey, double defaultVal)
  {
    try
    {
      return getProperty(propertyKey).getDoubleValue(defaultVal).doubleValue();
    }
    catch (Exception ex)
    {
      return defaultVal;
    }
  }

  public List getList(String propertyKey, String delimiter)
  {
    try
    {
      String prop = this.getString(propertyKey,"");
      StringTokenizer st = new StringTokenizer(prop, delimiter);
      List list = new ArrayList();
      while (st.hasMoreTokens())
      {
        list.add(st.nextToken());
      }
      return list;
    }
    catch (Exception ex)
    {
      return new ArrayList();
    }
  }

  /**
   * Sets a property value. The property will be added to the configuration
   * if it does not already exists.
   *
   * @param propertyKey Key of the property
   * @param value The string value to set into the property.
   *
   */
  public void setString(String propertyKey, String value)
  {
    try
    {
      getProperty(propertyKey).setValue(value);
    }
    catch (Exception ex)
    {
      Property prop = new Property();
      prop.setKey(propertyKey);
      prop.setValue(value);
      addProperty(prop);
    }
  }
  /**
   * Sets a property value. The property will be added to the configuration
   * if it does not already exists.
   *
   * @param propertyKey Key of the property
   * @param value The "int" value to set into the property.
   *
   */
  public void setInt(String propertyKey, int value)
  {
    setString(propertyKey, String.valueOf(value));
  }

  /**
   * Sets a property value. The property will be added to the configuration
   * if it does not already exists.
   *
   * @param propertyKey Key of the property
   * @param value The "long" value to set into the property.
   *
   */
  public void setLong(String propertyKey, long value)
  {
    setString(propertyKey, String.valueOf(value));
  }

  /**
   * Sets a property value. The property will be added to the configuration
   * if it does not already exists.
   *
   * @param propertyKey Key of the property
   * @param value The "short" value to set into the property.
   *
   */
  public void setShort(String propertyKey, short value)
  {
    setString(propertyKey, String.valueOf(value));
  }

  /**
   * Sets a property value. The property will be added to the configuration
   * if it does not already exists.
   *
   * @param propertyKey Key of the property
   * @param value The "boolean" value to set into the property.
   *
   */
  public void setBoolean(String propertyKey, boolean value)
  {
    setString(propertyKey, String.valueOf(value));
  }

  /**
   * Sets a property value. The property will be added to the configuration
   * if it does not already exists.
   *
   * @param propertyKey Key of the property
   * @param value The "double" value to set into the property.
   *
   */
  public void setDouble(String propertyKey, double value)
  {
    setString(propertyKey, String.valueOf(value));
  }

  public void setList(String propertyKey, List valueList, String delimiter)
  {
    Object[] obj = valueList.toArray();
    StringBuffer buff = new StringBuffer();
    for (int i=0; i<obj.length; i++)
    {
      if (i!=0) buff.append(delimiter);
      buff.append(obj[i]);
    }
    setString(propertyKey, buff.toString());
  }

  /**
   * Update's the Configuration object.
   *
   * This method is a callback, when invoked., updates the configuration
   * object.
   * @param p_observable
   * @param p_object
   */

  public void update(Observable p_observable, Object p_object)
  {
      System.out.println(" In Configuration Update  ");
      if( p_observable instanceof ObservableConfiguration)
      {
        ObservableConfiguration l_observableConf = (ObservableConfiguration) p_observable;
        String l_configName = l_observableConf.getConfigName();
        if(l_configName.equals(this.getName()))
        {
            this._property = l_observableConf.getProperty();
        }

      }

  }

  /**
   * Gets the properties of this Configuration.
   * @return java.util.Properties.
   */

 public Properties getProperties()
 {
    Properties l_props = new Properties();
    for(int i=0;i<_property.size();i++)
    {
      Property l_property = (Property)_property.get(i);
      l_props.setProperty(l_property.getKey(),l_property.getValue());
    }
    return l_props;
 }


}