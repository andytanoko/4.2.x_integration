/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Namespace.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 15 2003    Neo Sok Lay         Created
 * Sep 18 2003    Neo Sok Lay         Add Taxonomy.
 * Sep 23 2003    Neo Sok Lay         Add method: findFingerprintByValue()
 *                                    Change method: getFingerprintTypes() -->
 *                                                   getFingerprintValues()
 * Sep 30 2003    Neo Sok Lay         Add method: findFingerprints(String[] types)
 */
package com.gridnode.gtas.server.bizreg.model;

import java.util.ArrayList;

/**
 * A Namespace is a technical specification that provides a context
 * under which a name makes sense.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class Namespace
{
  private String _key;
  private String _type;
  private String _value;
  private String _description;
  private Fingerprint[] _fingerprints;
  private Taxonomy[] _taxonomies;
  
  /**
   *  Constructs a Namespace. 
   */
  public Namespace()
  {
  }

  /**
   * Gets a description of the Namespace.
   * 
   * @return description of the namespace
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * Gets the Fingerprints in this Namespace.
   * 
   * @return The fingerprints in this namespace.
   */
  public Fingerprint[] getFingerprints()
  {
    return _fingerprints;
  }

  /**
   * Get the type code of this Namespace. This identifies
   * a Namespace.
   * 
   * @return type code of this namespace.
   */
  public String getType()
  {
    return _type;
  }

  /**
   * Gets the value of this Namespace.
   * 
   * @return Value of this Namespace.
   */
  public String getValue()
  {
    return _value;
  }

  /**
   * Sets a description of this Namespace.
   * 
   * @param description The description to set.
   */
  public void setDescription(String description)
  {
    _description = description;
  }

  /**
   * Sets the fingerprints of this Namespace.
   * 
   * @param fingerprints the Fingerprints to set.
   */
  public void setFingerprints(Fingerprint[] fingerprints)
  {
    _fingerprints = fingerprints;
  }

  /**
   * Sets the type code of this Namespace.
   * 
   * @param type The type code to set.
   */
  public void setType(String type)
  {
    _type = type;
  }

  /**
   * Sets the value of this Namespace.
   * 
   * @param value The value to set.
   */
  public void setValue(String value)
  {
    _value = value;
  }

  /**
   * Gets the universally unique identifier for this Namespace in the registry.
   * 
   * @return Key of this Namespace. This field is not persistent. Default is null.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Sets the universally unique identifier for this Namespace in
   * the registry.
   *  
   * @param key The Key to set.
   */
  public void setKey(String key)
  {
    _key = key;
  }

  public static String getObjectType()
  {
    return "Namespace";
  }
  
  /**
   * Finds in this Namespace for a Fingerprint with the specified type.
   * 
   * @param type The Fingerprint Type.
   * @return The Fingerprint with the specified type, or <b>null</b> if
   * none exists.
   */
  public Fingerprint findFingerprint(String type)
  {
    Fingerprint found = null;
    if (_fingerprints != null)
    {
      for (int i=0; i<_fingerprints.length && found==null; i++)
      {
        if (_fingerprints[i].getType().equals(type))
          found = _fingerprints[i];
      }
    }
    
    return found;
  }

  /**
   * Find in this Namespace for Fingerprints with the specified types.
   * 
   * @param types The Fingerprint types.
   * @return Array of Fingerprints found, in order of each specified type. 
   * If a Fingerprint cannot be found for the type, a <b>null</b> will
   * take the place of the respective position in the array.
   */
  public Fingerprint[] findFingerprints(String[] types)
  {
    Fingerprint[] found = new Fingerprint[types.length];
    for (int i=0; i<types.length; i++)
    {
      found[i] = findFingerprint(types[i]);
    }
    
    return found;
  }
  
  /**
   * Finds in this Namespace for a Fingerprint with the specified value.
   * 
   * @param value The Fingerprint Value.
   * @return The Fingerprint with the specified value, or <b>null</b> if
   * none exists.
   */
  public Fingerprint findFingerprintByValue(String value)
  {
    Fingerprint found = null;
    if (_fingerprints != null)
    {
      for (int i=0; i<_fingerprints.length && found==null; i++)
      {
        if (_fingerprints[i].getValue().equals(value))
          found = _fingerprints[i];
      }
    }
    
    return found;
  }

  /**
   * Get a list of the Values of the Fingerprints contained in this Namespace.
   * 
   * @param ArrayList of String objects.
   */
  public ArrayList getFingerprintValues()
  {
    ArrayList values = new ArrayList();
    if (_fingerprints != null)
    {
      for (int i=0; i<_fingerprints.length; i++)
      {
        values.add(_fingerprints[i].getValue());
      }
    }
    
    return values;
  }  
  
  /**
   * Gets the Taxonomies for this Namespace.
   * @return The Taxonomy(s) of this Namespace. 
   */
  public Taxonomy[] getTaxonomies()
  {
    return _taxonomies;
  }

  /**
   * Sets the Taxonomies for this Namespace.
   * @param taxonomy The Taxonomy(s) to set.
   */
  public void setTaxonomies(Taxonomy[] taxonomies)
  {
    _taxonomies = taxonomies;
  }

}
