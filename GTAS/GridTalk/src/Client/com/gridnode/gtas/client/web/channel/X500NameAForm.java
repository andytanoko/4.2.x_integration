/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: X500NameAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-23     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.channel;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class X500NameAForm extends GTActionFormBase
{
  //20030121AH - X500Name Fields
  //(@todo: I really got to get around to supporting embedded entity fields with nested
  // ActionForms!...)
  //20030122AH - You sure do - silly boy - youve got 2 not one x500Name entities to display
  //...oh woe and torment :-( Doh!
  private String _country;
  private String _state;
  private String _organization;
  private String _locality;
  private String _organizationalUnit;
  private String _streetAddress;
  private String _commonName;
  private String _title;
  private String _emailAddress;
  private String _businessCategory;
  private String _telephoneNumber;
  private String _postalCode;
  private String _unknownAttributeType;

  public String getCountry()
  { return _country; }

  public void setCountry(String country)
  { _country=country; }

  public String getState()
  { return _state; }

  public void setState(String state)
  { _state=state; }

  public String getOrganization()
  { return _organization; }

  public void setOrganization(String organization)
  { _organization=organization; }

  public String getLocality()
  { return _locality; }

  public void setLocality(String locality)
  { _locality=locality; }

  public String getOrganizationalUnit()
  { return _organizationalUnit; }

  public void setOrganizationalUnit(String organizationalUnit)
  { _organizationalUnit=organizationalUnit; }

  public String getStreetAddress()
  { return _streetAddress; }

  public void setStreetAddress(String streetAddress)
  { _streetAddress=streetAddress; }

  public String getCommonName()
  { return _commonName; }

  public void setCommonName(String commonName)
  { _commonName=commonName; }

  public String getTitle()
  { return _title; }

  public void setTitle(String title)
  { _title=title; }

  public String getEmailAddress()
  { return _emailAddress; }

  public void setEmailAddress(String emailAddress)
  { _emailAddress=emailAddress; }

  public String getBusinessCategory()
  { return _businessCategory; }

  public void setBusinessCategory(String businessCategory)
  { _businessCategory=businessCategory; }

  public String getTelephoneNumber()
  { return _telephoneNumber; }

  public void setTelephoneNumber(String telephoneNumber)
  { _telephoneNumber=telephoneNumber; }

  public String getPostalCode()
  { return _postalCode; }

  public void setPostalCode(String postalCode)
  { _postalCode=postalCode; }

  public String getUnknownAttributeType()
  { return _unknownAttributeType; }

  public void setUnknownAttributeType(String unknownAttributeType)
  { _unknownAttributeType=unknownAttributeType; }
}