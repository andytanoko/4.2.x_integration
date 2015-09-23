/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WhitePage.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2001    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for WhitePage entity. A WhitePage contains the
 * business information of a company.<P>
 *
 * The Model:<BR><PRE>
 *   UId            - UID for a WhitePage entity instance.
 *   BEUId          - UID of BusinessEntity owning this WhitePage.
 *   BusDesc        - Business description.
 *   DUNS           - Data Universal Numbering System
 *   GlobalSupplyChainCode -
 *   ContactPerson  - Contact person.
 *   Email          - Email of ContactPerson.
 *   Tel            - Telephone number of ContactPerson.
 *   Fax            - Fax number of ContactPerson.
 *   Website        - Website URL.
 *   Address        - Address of business.
 *   City           - City of Address.
 *   State          - State of Address.
 *   ZipCode        - Zip code of Address.
 *   Country        - Country code.
 *   Language       - Language code.
 *   POBox          - Post office box.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public class WhitePage
  extends    AbstractEntity
  implements IWhitePage
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2816232531744816865L;
	
	protected Long    _beUId;
  protected String  _busDesc;
  protected String  _dUNS;
  protected String  _globalSCCode;
  protected String  _contactPerson;
  protected String  _email;
  protected String  _tel;
  protected String  _fax;
  protected String  _website;
  protected String  _address;
  protected String  _city;
  protected String  _state;
  protected String  _zipCode;
  protected String  _country;
  protected String  _language;
  protected String  _pOBox;

  public WhitePage()
  {
  }

  // ***************** Methods from AbstractEntity *************************

  public Number getKeyId()
  {
   return UID;
  }

  public String getEntityDescr()
  {
   if (getBeUId() != null)
     return getBeUId() + "/" +
            getUId() + "-" + getBusinessDesc();

   return "(null)/" + getUId() + "-" + getBusinessDesc();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  // ******************** Getters for attributes ***************************

  public Long getBeUId()
  {
    return _beUId;
  }

  public String getBusinessDesc()
  {
    return _busDesc;
  }

  public String getDUNS()
  {
    return _dUNS;
  }

  public String getContactPerson()
  {
    return _contactPerson;
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

  public String getWebsite()
  {
    return _website;
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

  public String getPOBox()
  {
    return _pOBox;
  }

  public String getGlobalSupplyChainCode()
  {
    return _globalSCCode;
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

  public void setBeUId(Long beUId)
  {
    _beUId = beUId;
  }

  public void setBusinessDesc(String desc)
  {
    _busDesc = desc;
  }

  public void setDUNS(String dUNS)
  {
    _dUNS = dUNS;
  }

  public void setContactPerson(String contactperson)
  {
    _contactPerson = contactperson;
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

  public void setWebsite(String website)
  {
    _website = website;
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

  public void setPOBox(String pOBox)
  {
    _pOBox = pOBox;
  }

  public void setGlobalSupplyChainCode(String globalSupplyChainCode)
  {
    _globalSCCode = globalSupplyChainCode;
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
