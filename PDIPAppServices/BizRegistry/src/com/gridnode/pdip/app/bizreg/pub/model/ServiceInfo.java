/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Mar 01 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.bizreg.pub.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A Service information object. This provides information on services
 * offered by an organization. A Service may have a set of 
 * Binding information objects.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class ServiceInfo extends AbstractRegistryInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2919334258063429929L;
	private Set<BindingInfo> _bindings = new HashSet<BindingInfo>();
  private String _orgKey;
  
  /**
   * Constructor for ServiceInfo.
   */
  public ServiceInfo(String name, String organizationKey)
  {
    setName(name);
    setOrganizationKey(organizationKey);
  }

  
  /**
   * Returns the bindings.
   * @return Set of BindingInfo(s)
   */
  public Set<BindingInfo> getBindings()
  {
    return _bindings;
  }

  /**
   * Sets the bindings.
   * @param bindings The Set of BindingInfo(s).
   */
  public void setBindings(Set<BindingInfo> bindings)
  {
    _bindings.clear();
    if (bindings != null)
    {
      for (Iterator i=bindings.iterator(); i.hasNext(); )
      {
        Object obj = i.next();
        if (obj instanceof BindingInfo)
          addBinding((BindingInfo)obj);
      }
    }
  }

  /**
   * Adds a Binding information object.
   * 
   * @param binding The binding to add.
   */
  public void addBinding(BindingInfo binding)
  {
    _bindings.add(binding);
  }
  
  /**
   * Sets the key of the Organization information object that
   * this service is offered by.
   * 
   * @param organizationKey Key of the Organization information object.
   */
  public void setOrganizationKey(String organizationKey)
  {
    _orgKey = organizationKey;
  }
 
  /**
   * Gets the Key of the Organization information object that
   * offerred this Service.
   * 
   * @return Key of the Organization information object.
   */ 
  public String getOrganizationKey()
  {
    return _orgKey;
  }
  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#getObjectType()
   */
  public int getObjectType()
  {
    return TYPE_SERVICE;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#isPublishSupported()
   */
  public boolean isPublishSupported()
  {
    return true;
  }
}
