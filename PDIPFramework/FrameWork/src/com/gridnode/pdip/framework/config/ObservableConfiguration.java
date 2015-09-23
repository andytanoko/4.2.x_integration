package com.gridnode.pdip.framework.config;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */


import java.util.Observable;
import java.util.Observer;
import java.util.List;
import java.util.Enumeration;
import java.util.Vector;
/**
 * Extends the functions of a {@link Configuration Configuration} to provide
 * observability for changes.
 *
 *
 * @version 1.1
 * @since 1.0a build 0.9.9.6
 */
public class ObservableConfiguration extends Observable
{
  private Configuration _config;


  public ObservableConfiguration()
  {
  }

  /**
   * Construct an observable configuration.
   *
   * @param config The configuration object to be observed
   *
   * @since 1.0a build 0.9.9.6
   */
  public ObservableConfiguration(Configuration config)
  {
    _config = config;
  }

  /**
   * Add an observer for the configuration. The observer must implement
   * IConfigurationObserver.
   *
   * @param observer The observer to add
   * @see IConfigurationObserver
   *
   * @since 1.0a build 0.9.9.6
   */
  public void addObserver(Observer observer)
  {
    if (observer instanceof IConfigurationObserver)
    {
      System.out.println(" ADDing This ");
      super.addObserver(observer);
    }
  }

  /**
   * Update the configuration object. All obervers for this configuration
   * will be notified.
   *
   * @param config The configuration object to update with
   *
   * @since 1.0a build 0.9.9.6
   */
  public void updateConfiguration(Configuration config)
  {
    _config = config;
    setChanged();
    notifyObservers();
  }



  /**
   * Remove the configuration object. All observers for this configuration
   * will be notified of the removal with "REMOVE" as the argument.
   *
   * @since 1.1
   */


  public void removeConfiguration()
  {
    setChanged();
    notifyObservers("REMOVE");
  }

  /**
   * Get the name of this configuration.
   *
   * @return The name of the configuration object
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getConfigName()
  {
    return _config.getName();
  }

  /**
   * Refer to {@link Configuration#getString(String)}
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getString(String propertyKey)
  {
    return _config.getString(propertyKey);
  }

  /**
   * Refer to {@link Configuration#getString(String,String)}
   *
   * @since 1.0a build 0.9.9.6
   */
  public String getString(String propertyKey, String defaultVal)
  {
    return _config.getString(propertyKey, defaultVal);
  }

  /**
   * Refer to {@link Configuration#getInt(String)}
   *
   * @since 1.0a build 0.9.9.6
   */
  public int getInt(String propertyKey)
  {
    return _config.getInt(propertyKey);
  }

  /**
   * Refer to {@link Configuration#getInt(String,int)}
   *
   * @since 1.0a build 0.9.9.6
   */
  public int getInt(String propertyKey, int defaultVal)
  {
    return _config.getInt(propertyKey, defaultVal);
  }

  /**
   * Refer to {@link Configuration#getBoolean(String)}
   *
   * @since 1.0a build 0.9.9.6
   */
  public boolean getBoolean(String propertyKey)
  {
    return _config.getBoolean(propertyKey);
  }

  /**
   * Refer to {@link Configuration#getBoolean(String,boolean)}
   *
   * @since 1.0a build 0.9.9.6
   */
  public boolean getBoolean(String propertyKey, boolean defaultVal)
  {
    return _config.getBoolean(propertyKey, defaultVal);
  }

  /**
   * Refer to {@link Configuration#getLong(String)}
   *
   * @since 1.0a build 0.9.9.6
   */
  public long getLong(String propertyKey)
  {
    return _config.getLong(propertyKey);
  }

  /**
   * Refer to {@link Configuration#getLong(String,long)}
   *
   * @since 1.0a build 0.9.9.6
   */
  public long getLong(String propertyKey, long defaultVal)
  {
    return _config.getLong(propertyKey, defaultVal);
  }

  /**
   * Refer to {@link Configuration#getShort(String)}
   *
   * @since 1.0a build 0.9.9.6
   */
  public short getShort(String propertyKey)
  {
    return _config.getShort(propertyKey);
  }

  /**
   * Refer to {@link Configuration#getShort(String,short)}
   *
   * @since 1.0a build 0.9.9.6
   */
  public short getShort(String propertyKey, short defaultVal)
  {
    return _config.getShort(propertyKey, defaultVal);
  }

  /**
   * Refer to {@link Configuration#getDouble(String)}
   *
   * @since 1.0a build 0.9.9.6
   */
  public double getDouble(String propertyKey)
  {
    return _config.getDouble(propertyKey);
  }

  /**
   * Refer to {@link Configuration#getDouble(String,double)}
   *
   * @since 1.0a build 0.9.9.6
   */
  public double getDouble(String propertyKey, double defaultVal)
  {
    return _config.getDouble(propertyKey, defaultVal);
  }

  public List getList(String propertyKey, String delimiter)
  {
    return _config.getList(propertyKey, delimiter);
  }

  /**
   * Change the value of a property in this configuration.
   * All observers of this configuration will be notified on the change.
   *
   * @see Configuration#setString(String,String)
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setString(String propertyKey, String value)
  {
    _config.setString(propertyKey, value);
    setChanged();
    notifyObservers();
  }

  /**
   * Change the value of a property in this configuration.
   *
   * @see #setString(String,String)
   * @see Configuration#setInt(String,int)
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setInt(String propertyKey, int value)
  {
    setString(propertyKey, String.valueOf(value));
  }

  /**
   * Change the value of a property in this configuration.
   *
   * @see #setString(String,String)
   * @see Configuration#setLong(String,long)
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setLong(String propertyKey, long value)
  {
    setString(propertyKey, String.valueOf(value));
  }

  /**
   * Change the value of a property in this configuration.
   *
   * @see #setString(String,String)
   * @see Configuration#setShort(String,short)
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setShort(String propertyKey, short value)
  {
    setString(propertyKey, String.valueOf(value));
  }

  /**
   * Change the value of a property in this configuration.
   *
   * @see #setString(String,String)
   * @see Configuration#setBoolean(String,boolean)
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setBoolean(String propertyKey, boolean value)
  {
    setString(propertyKey, String.valueOf(value));
  }

  /**
   * Change the value of a property in this configuration.
   *
   * @see #setString(String,String)
   * @see Configuration#setDouble(String,double)
   *
   * @since 1.0a build 0.9.9.6
   */
  public void setDouble(String propertyKey, double value)
  {
    setString(propertyKey, String.valueOf(value));
  }

  public void setList(String propertyKey, List valueList, String delimiter)
  {
    _config.setList(propertyKey, valueList, delimiter);
  }

  /**
   * Get the keys of the properties in this configuration.
   *
   * @return An enumeration of the keys to all properties in this configuration
   *
   * @since 1.0a build 0.9.9.6
   */
  public Enumeration getPropertyKeys()
  {
    return _config.getPropertyKeys();
  }

  /**
   * Gives a short description of this configuration object.
   *
   * @return A summary of the configuration
   *
   * @since 1.0a build 0.9.9.6
   */
  public String toString()
  {
    return _config.toString();
  }

  public Vector getProperty()
  {
    return _config.getProperty();
  }

}
