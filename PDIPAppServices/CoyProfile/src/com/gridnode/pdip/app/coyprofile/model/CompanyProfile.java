/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CompanyProfile.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 05 2001    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.coyprofile.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for CompanyProfile entity. A CompanyProfile contains the
 * information of a company.<P>
 *
 * The Model:<BR><PRE>
 *   UId            - UID for a CompanyProfile entity instance.
 *   CoyName        - Name of the Company.
 *   Email          - Email of ContactPerson.
 *   AltEmail       - Alternate Email of contact person.
 *   Tel            - Telephone number of contact person.
 *   AltTel         - Altenate telephone number of contact person.
 *   Fax            - Fax number of contact person.
 *   Address        - Address of business.
 *   City           - City of Address.
 *   State          - State of Address.
 *   ZipCode        - Zip code of Address.
 *   Country        - Country code.
 *   Language       - Language code.
 *   IsPartner      - Whether the profile belongs to a partner company.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class CompanyProfile
  extends    AbstractEntity
  implements ICompanyProfile
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -541244223393623242L;
	protected String  _coyName;
  protected String  _altEmail;
  protected String  _altTel;
  protected String  _email;
  protected String  _tel;
  protected String  _fax;
  protected String  _address;
  protected String  _city;
  protected String  _state;
  protected String  _zipCode;
  protected String  _country;
  protected String  _language;
  protected Boolean  _isPartner = Boolean.TRUE;

  public CompanyProfile()
  {
  }

  // ***************** Methods from AbstractEntity *************************

  public Number getKeyId()
  {
   return UID;
  }

  public String getEntityDescr()
  {
    return getCoyName() + "/Partner:" + isPartner();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  // ******************** Getters for attributes ***************************

  public String getCoyName()
  {
    return _coyName;
  }

  public String getAltTel()
  {
    return _altTel;
  }

  public String getEmail()
  {
    return _email;
  }

  public String getTel()
  {
    return _tel;
  }

  public String getFax()
  {
    return _fax;
  }

  public String getAddress()
  {
    return _address;
  }

  public String getZipCode()
  {
    return _zipCode;
  }

  public String getCountry()
  {
    return _country;
  }

  public String getLanguage()
  {
    return _language;
  }

  public Boolean isPartner()
  {
    return _isPartner;
  }

  public String getAltEmail()
  {
    return _altEmail;
  }

  public String getCity()
  {
    return _city;
  }

  public String getState()
  {
    return _state;
  }

  // ******************** Setters for attributes ***************************

  public void setCoyName(String name)
  {
    _coyName = name;
  }

  public void setAltTel(String altTel)
  {
    _altTel = altTel;
  }

  public void setEmail(String email)
  {
    _email = email;
  }

  public void setTel(String tel)
  {
    _tel = tel;
  }

  public void setFax(String fax)
  {
    _fax = fax;
  }

  public void setAddress(String address)
  {
    _address = address;
  }

  public void setZipCode(String zipcode)
  {
    _zipCode = zipcode;
  }

  public void setCountry(String country)
  {
    _country = country;
  }

  public void setLanguage(String language)
  {
    _language = language;
  }

  public void setPartner(Boolean isPartner)
  {
    if (isPartner == null)
      isPartner = Boolean.TRUE;

    _isPartner = isPartner;
  }

  public void setAltEmail(String altEmail)
  {
    _altEmail = altEmail;
  }

  public void setCity(String city)
  {
    _city = city;
  }

  public void setState(String state)
  {
    _state = state;
  }

}
