/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BindingInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003        Neo Sok Lay         Created
 * Mar 01 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.bizreg.pub.model;

import java.util.HashSet;
import java.util.Set;

/**
 * A Binding information object. This models the 
 * technical information on a specific way to access a specific interface 
 * offered by a Service interface.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class BindingInfo extends AbstractRegistryInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9201109026843499771L;
	private String _accessUri;
  private Set<SpecificationInfo> _specificationLinks = new HashSet<SpecificationInfo>();
  
  /**
   * Constructor for BindingInfo.
   * 
   * @param name The name of the Binding information object.
   */
  public BindingInfo(String name)
  {
    setName(name);
  }

  /**
   * Sets the URI that gives access to the service via this binding
   * 
   * @param accessUri The URI to access the service via this binding.
   */
  public void setAccessUri(String accessUri)
  {
    _accessUri = accessUri;
  }
 
  /**
   * Gets the URI that gives access to the service via this binding
   * 
   * @return The URI to access the service via this binding.
   */ 
  public String getAccessUri()
  {
    return _accessUri;
  }
  
  /**
   * Adds a Specification information object.
   * 
   * @param specInfo The Specification information object to add.
   */
  public void addSpecificationLink(SpecificationInfo specInfo)
  {
    _specificationLinks.add(specInfo);
  }
 
 /**
  * Gets all Specification information objects in this
  * Binding information object.
  * 
  * @return Set of SpecificationInfo objects.
  */ 
  public Set<SpecificationInfo> getSpecificationLinks()
  {
    return _specificationLinks;
  }
  
  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#getObjectType()
   */
  public int getObjectType()
  {
    return TYPE_BINDING;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#isPublishSupported()
   */
  public boolean isPublishSupported()
  {
    return false;
  }
}
