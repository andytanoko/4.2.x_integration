/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ConceptInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.pub.model;

/**
 * A Concept information object. This corresponds to a technical fingerprint
 * in the public registry. A Concept information object only exists under a
 * particular Scheme information object which is a namespace.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class ConceptInfo extends AbstractRegistryInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8914097699982278558L;
	private String _parentKey;
  private String _value;
  
  /**
   * Constructs a Concept information object
   * 
   * @param parentKey The key of the parent of this Concept information object.
   * It is assumed that the parent is a Scheme information object.
   * @param name The name of this concept.
   * @param value The value associated with this concept.
   */
  public ConceptInfo(String parentKey, String name, String value)
  {
    setName(name);
    setValue(value);
    setParentKey(parentKey);
  }
  
  /**
   * Sets the value associated with this Concept information object.
   * 
   * @param value The value to set.
   */
  public void setValue(String value)
  {
    _value = value;
  }
  
  /**
   * Gets the value associated with this Concept information object.
   * 
   * @return The value associated with this Concept information object.
   */
  public String getValue()
  {
    return _value;
  }
  
  /**
   * Sets the key of the parent registry information object of this Concept
   * information object.
   * 
   * @param parentKey The key of the parent Scheme information object.
   */
  public void setParentKey(String parentKey)
  {
    _parentKey = parentKey;
  }
  
  /**
   * Gets the key of the parent registry information object of this
   * Concept information object.
   * 
   * @return The key of the parent Scheme information object.
   */
  public String getParentKey()
  {
    return _parentKey;
  }
  
  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#getObjectType()
   */
  public int getObjectType()
  {
    return TYPE_CONCEPT;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#isPublishSupported()
   */
  public boolean isPublishSupported()
  {
    return true;
  }

  /**
   * Sets the overview link information object of this concept.
   * 
   * @param linkInfo The Link information object to set.
   */
  public void setOverviewLink(String[] linkInfo)
  {
    clearLinks();
    addLink(linkInfo);
  }
 
  /**
   * Get the overview link information object of this concept.
   * 
   * @return The first Link information object contained in this Concept
   * information object, or <b>null</b> if none exists.
   */
  public String[] getOverviewLink()
  {
    String[][] allLinks = getLinks();
    
    if (allLinks.length > 0)
      return allLinks[0];
    else
      return null;
  }

}
