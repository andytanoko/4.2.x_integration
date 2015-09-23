/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistryInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.pub.model;

import java.io.Serializable;

/**
 * This interface defines the common fields and methods of a Registry information
 * object.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IRegistryInfo extends Serializable
{
  /**
   * Organization registry object type.
   */
  static final int TYPE_ORGANIZATION = 1;
  
  /**
   * Service registry object type.
   */
  static final int TYPE_SERVICE = 2;
  
  /**
   * Binding registry object type.
   */
  static final int TYPE_BINDING = 3;
  
  /**
   * Scheme registry object type.
   */
  static final int TYPE_SCHEME = 4;
  
  /**
   * Concept registry object type.
   */
  static final int TYPE_CONCEPT = 5;
  
  /**
   * SpecificationLink registry object type.
   */
  static final int TYPE_SPECIFICATION = 6;
  
  /**
   * Returns the description of this registry information object.
   * @return description of the registry information object.
   */
  public String getDescription();

  /**
   * Returns the key of this registry information object.
   * @return The key of this registry information object, or <b>null</b>
   * if unspecified.
   */
  public String getKey();

  /**
   * Returns the name of this registry information object.
   * @return Name of this registry information object.
   */
  public String getName();

  /**
   * Sets the description for this registry information object.
   * @param description The description to set
   */
  public void setDescription(String description);

  /**
   * Sets the key for this registry information object.
   * @param key The key to set
   */
  public void setKey(String key);

  /**
   * Sets the name for this registry information object.
   * @param name The name to set
   */
  public void setName(String name);

  /**
   * Gets the Identifier information objects associated with this 
   * registry information object.
   * 
   * @return The Identifier information objects associated with 
   * this registry information object. The length of first dimension array
   * may be 0 if no identifiers have been associated.
   * 
   * @see IIdentifierInfo 
   */
  public String[][] getIdentifiers();

  /**
   * Adds an Identifier information object to this registry information object.
   * 
   * @param identifierInfo The Identifier information object to associate
   * with this registry information object.
   * 
   * @see IIdentifierInfo
   */
  public void addIdentifier(String[] identifierInfo);

  /**
   * Get the Category information objects associated with this registry
   * information object.
   * 
   * @return The Category information objects associated with this registry
   * information object. The length of first dimension array
   * may be 0 if no categories have been associated.
   * @see ICategoryInfo
   */
  public String[][] getCategories();

  /**
   * Adds a Category information object to this registry information object.
   * 
   * @param categoryInfo The Category information object to associate with
   * this registry information object.
   * @see ICategoryInfo
   */
  public void addCategory(String[] categoryInfo);
  
  /**
   * Get the Link information objects associated with this registry
   * information object.
   * 
   * @return The Link information objects associated with this registry
   * information object. The length of first dimension array
   * may be 0 if no links have been associated.
   * @see ILinkInfo
   */
  public String[][] getLinks();

  /**
   * Adds a Link information object to this registry information object.
   * 
   * @param linkInfo The Link information object to associate with
   * this registry information object.
   * @see ILinkInfo
   */
  public void addLink(String[] linkInfo);

  /**
   * Get the type of this registry information object. 
   * 
   * @return The Type of this registry information object.
   * @see #TYPE_BINDING
   * @see #TYPE_CONCEPT
   * @see #TYPE_ORGANIZATION
   * @see #TYPE_SCHEME
   * @see #TYPE_SERVICE
   * @see #TYPE_SPECIFICATION
   */
  public int getObjectType();
  
  /**
   * Get whether Publish method is supported for this
   * registry information object. <b>Supported</b> means the
   * <b>Publish</b> method can be issued directly on the
   * registry information object.
   * 
   * @return <b>true</b> if supported, <b>false</b> otherwise.
   */
  public boolean isPublishSupported();

  /**
   * Get the type of the proprietary object that maps to
   * this registry information object.
   * 
   * @return Type of proprietary object that maps to this
   * registry information object.
   */
  public String getProprietaryObjectType();
  
  /**
   * Sets the type of the proprietary object that maps to
   * this registry information object.
   * 
   * @param type The type to set.
   */
  public void setProprietaryObjectType(String type);
  
  /**
   * Gets the key of the proprietary object that maps to
   * this registry information object.
   * 
   * @return The Key of the proprietary object that maps to
   * this registry information object.
   */
  public String getProprietaryObjectKey();
  
  /**
   * Sets the Key of the proprietary object that maps to this
   * registry information object.
   * 
   * @param key The key to set.
   */
  public void setProprietaryObjectKey(String key);
  
  /**
   * Gets the description string for the ObjectType.
   * 
   * @return A short description string for the ObjectType.
   */
  public String getObjectTypeDescr();
}
