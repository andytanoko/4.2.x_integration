/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: OrganizationInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2003    Neo Sok Lay         Created
 * Mar 01 2006    Neo Sok Lay         Use generics
 */
package com.gridnode.pdip.app.bizreg.pub.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An Organization information object. This models the business entity
 * information in the public registry.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class OrganizationInfo extends AbstractRegistryInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8375320991854150843L;
	private String[] _postalAddress;
  private List<String[]> _contacts = new ArrayList<String[]>();
  private Set<String[]> _phoneNumbers = new HashSet<String[]>();
  private List<ServiceInfo> _services = new ArrayList<ServiceInfo>();
    
  /**
   * Constructor for OrganizationInfo.
   * 
   * @param name The name of the Organization.
   */
  public OrganizationInfo(String name)
  {
    setName(name);
  }

  /**
   * Sets the postal address information.
   * 
   * @param addressInfo The Address information object to set.
   * 
   * @see IAddressInfo
   */
  public void setPostalAddress(String[] addressInfo)
  {
    _postalAddress = addressInfo;
  }
 
  /**
   * Gets the postal address information.
   * 
   * @return The Address information object, or <b>null</b> if
   * none is set previously.
   * @see IAddressInfo
   */
  public String[] getPostalAddress()
  {
    return _postalAddress;
  }
   
  /**
   * Adds a Contact information object.
   * 
   * @param contactInfo The Contact information object to add.
   * @see IContactInfo
   */ 
  public void addContact(String[] contactInfo)
  {
    _contacts.add(contactInfo);
  }
  
  /**
   * Get all Contact information objects added to this Organization
   * information object.
   * 
   * @return Array of Contact information objects.
   * @see IContactInfo
   */
  public String[][] getContacts()
  {
    return _contacts.toArray(new String[_contacts.size()][IContactInfo.NUM_FIELDS]);
  }
  
  /**
   * Add a Phone information object.
   * 
   * @param phoneInfo The Phone information object to add.
   * @see IPhoneInfo
   */  
  public void addPhoneNumber(String[] phoneInfo)
  {
    _phoneNumbers.add(phoneInfo);
  }
  
  /**
   * Get all Phone information objects added to this Organization
   * information object.
   * 
   * @return Array of Phone inforamtion objects 
   * @see IPhoneInfo
   */
  public String[][] getPhoneNumbers()
  {
    return _phoneNumbers.toArray(new String[_phoneNumbers.size()][IPhoneInfo.NUM_FIELDS]);
  }
  
  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#getObjectType()
   */
  public int getObjectType()
  {
    return TYPE_ORGANIZATION;
  }

  /**
   * @see com.gridnode.pdip.app.bizreg.pub.model.IRegistryInfo#isPublishSupported()
   */
  public boolean isPublishSupported()
  {
    return true;
  }
  
  /**
   * Get the list of Services provided by the Organization.
   * 
   * @return List of ServiceInfo objects.
   */
  public List<ServiceInfo> getServices()
  {
    return _services;
  }

  /**
   * Sets the list of Services provided by the Organization.
   * 
   * @param services List of ServiceInfo objects to set. The OrgKey of 
   * these ServiceInfo objects, if specified, must equal this OrganizationInfo's Key.
   */
  public void setServices(List<ServiceInfo> services)
  {
    _services = services;
  }

}
