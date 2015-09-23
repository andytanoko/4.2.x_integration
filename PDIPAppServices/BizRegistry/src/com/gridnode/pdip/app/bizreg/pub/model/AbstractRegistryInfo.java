/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractRegistryInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Sep 08 2003    Neo Sok Lay         Implements Comparable.
 * Mar 01 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.bizreg.pub.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract implementation for a registry information object.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public abstract class AbstractRegistryInfo implements IRegistryInfo, Comparable
{
  private String _key;
  private String _name;
  private String _description;
  private Set<String[]> _identifiers = new HashSet<String[]>();
  private Set<String[]> _categories = new HashSet<String[]>();
  private Set<String[]> _links = new HashSet<String[]>();
  private String _proprietaryKey;
  private String _proprietaryType;
  
  
  /**
   * Returns the description.
   * @return description of the registry information object.
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * Returns the key.
   * @return key of the registry information object.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Returns the name.
   * @return name of the registry information object.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Sets the description.
   * @param description The description to set
   */
  public void setDescription(String description)
  {
    this._description = description;
  }

  /**
   * Sets the key.
   * @param key The key to set
   */
  public void setKey(String key)
  {
    this._key = key;
  }

  /**
   * Sets the name.
   * @param name The name to set
   */
  public void setName(String name)
  {
    this._name = name;
  }

  /**
   * The hashCode of a registry information object is based on it's
   * Name.
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    return getName().hashCode();
  }

  /**
   * If the key is specified in this registry information object AND in
   * <code>obj</code>, then the comparison is based on the equality of their keys.
   * Otherwise, the comparison will be based on the hashCode() of both objects.
   * Collectively, <code>obj</code> must be an instance of AbstractRegistryInfo.
   * 
   * @see java.lang.Object#equals(Object)
   */
  public boolean equals(Object obj)
  {
    boolean equals = false;
    
    if (obj != null && getClass().isInstance(obj))
    {
      equals = obj.hashCode() == hashCode();
      AbstractRegistryInfo regInfo = (AbstractRegistryInfo)obj;
      if (equals && getKey() != null && regInfo.getKey() != null)
        equals = getKey().equals(regInfo.getKey());
    }
    
    return equals;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#addIdentifier(java.lang.String[])
   */
  public void addIdentifier(String[] identifierInfo)
  {
    _identifiers.add(identifierInfo);
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#getIdentifiers()
   */  
  public String[][] getIdentifiers()
  {
    return _identifiers.toArray(
      new String[_identifiers.size()][IIdentifierInfo.NUM_FIELDS]);
  }
  
  /**
   * Remove all Identifier information objects from this registry information
   * object.
   */  
  public void clearIdentifiers()
  {
    _identifiers.clear();
  }
  
  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#addCategory(java.lang.String[])
   */  
  public void addCategory(String[] categoryInfo)
  {
    _categories.add(categoryInfo);
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#getCategories()
   */  
  public String[][] getCategories()
  {
    return _categories.toArray(
      new String[_categories.size()][ICategoryInfo.NUM_FIELDS]);
  }

  /**
   * Remove all Category information objects from this registry information 
   * object.
   */  
  public void clearCategories()
  {
    _categories.clear();
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#addLink(java.lang.String[])
   */
  public void addLink(String[] linkInfo)
  {
    _links.add(linkInfo);
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#getLinks()
   */  
  public String[][] getLinks()
  {
    return _links.toArray(
      new String[_links.size()][ILinkInfo.NUM_FIELDS]);
  }

  /**
   * Remove all Link information objects from this registry information
   * object.
   */  
  public void clearLinks()
  {
    _links.clear();
  }
  
  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#getProprietaryObjectKey()
   */
  public String getProprietaryObjectKey()
  {
    return _proprietaryKey;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#getProprietaryObjectType()
   */
  public String getProprietaryObjectType()
  {
    return _proprietaryType;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#setProprietaryObjectKey(java.lang.String)
   */
  public void setProprietaryObjectKey(String key)
  {
    _proprietaryKey = key;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#setProprietaryObjectType(java.lang.String)
   */
  public void setProprietaryObjectType(String type)
  {
    _proprietaryType = type;
  }

  /**
   * Get a description for the specified registry information object type.
   * 
   * @param objectType The registry information object type.
   * @return A description for the specified type.
   * @see IRegistryInfo#TYPE_ORGANIZATION
   * @see IRegistryInfo#TYPE_SERVICE
   * @see IRegistryInfo#TYPE_BINDING
   * @see IRegistryInfo#TYPE_SCHEME
   * @see IRegistryInfo#TYPE_CONCEPT
   * @see IRegistryInfo#TYPE_SPECIFICATION
   */
  public static String getTypeDescription(int objectType)
  {
    String desc = null;
    switch (objectType)
    {
      case TYPE_ORGANIZATION:
        desc = "Organization";
        break;
      case TYPE_SERVICE:
        desc = "Service";
        break;
      case TYPE_BINDING:
        desc = "Binding";
        break;
      case TYPE_SCHEME:
        desc = "Scheme";
        break;
      case TYPE_CONCEPT:
        desc = "Concept";
        break;
      case TYPE_SPECIFICATION:
        desc = "Specification";
        break;
    }
    
    return desc;
  }
  
  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#getObjectTypeDescr()
   */
  public String getObjectTypeDescr()
  {
    return getTypeDescription(getObjectType());
  }

  /**
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(Object o)
  {
    int result = -1;
    // will throw ClassCastException if incompatible type
    if (getClass().isInstance(o))
    {
      result = equals(o) ? 0 : hashCode() - o.hashCode();
    }    
    else
      throw new ClassCastException("Unable to compare "+getClass().getName() + " to "+o.getClass().getName());

    return result;
  }

}
