/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchedBusinessEntityAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 * 2003-10-03     Neo Sok Lay         Add uuid field.
 */
package com.gridnode.gtas.client.web.be;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class SearchedBusinessEntityAForm extends GTActionFormBase
{
  private String _uuid;
  private String _id;
  private String _enterpriseId;
  private String _description;
  private String _beState;
  private String[] _channels;
  
  private String _businessDesc;
  private String _duns;
  private String _globalSupplyChainCode;
  private String _contactPerson;
  private String _email;
  private String _tel;
  private String _fax;
  private String _website;
  private String _address;
  private String _poBox;
  private String _city;
  private String _state;
  private String _zipCode;
  private String _country;
  private String _language;

  /**
   * @return UUID of the SearchedBusinessEntity.
   */
  public String getUuid()
  {
    return _uuid;
  }
 
  /**
   * @param uuid UUID to set.
   */
  public void setUuid(String uuid)
  {
    _uuid = uuid;
  }

  /**
   * @return
   */
  public String getId()
  {
    return _id;
  }

  /**
   * @param string
   */
  public void setId(String string)
  {
    _id = string;
  }

  /**
   * @return
   */
  public String getEnterpriseId()
  {
    return _enterpriseId;
  }

  /**
   * @param string
   */
  public void setEnterpriseId(String string)
  {
    _enterpriseId = string;
  }

  /**
   * @return
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * @param string
   */
  public void setDescription(String string)
  {
    _description = string;
  }

  /**
   * @return
   */
  public String getBeState()
  {
    return _beState;
  }

  public Short getBeStateShort()
  {
    return StaticUtils.shortValue(_beState);
  }

  /**
   * @param string
   */
  public void setBeState(String string)
  {
    _beState = string;
  }

  /**
   * @return
   */
  public String[] getChannels()
  {
    return _channels;
  }

  /**
   * @param strings
   */
  public void setChannels(String[] strings)
  {
    _channels = strings;
  }

  /**
   * @return
   */
  public String getBusinessDesc()
  {
    return _businessDesc;
  }

  /**
   * @param string
   */
  public void setBusinessDesc(String string)
  {
    _businessDesc = string;
  }

  /**
   * @return
   */
  public String getDuns()
  {
    return _duns;
  }

  /**
   * @param string
   */
  public void setDuns(String string)
  {
    _duns = string;
  }

  /**
   * @return
   */
  public String getGlobalSupplyChainCode()
  {
    return _globalSupplyChainCode;
  }

  /**
   * @param string
   */
  public void setGlobalSupplyChainCode(String string)
  {
    _globalSupplyChainCode = string;
  }

  /**
   * @return
   */
  public String getContactPerson()
  {
    return _contactPerson;
  }

  /**
   * @param string
   */
  public void setContactPerson(String string)
  {
    _contactPerson = string;
  }

  /**
   * @return
   */
  public String getEmail()
  {
    return _email;
  }

  /**
   * @param string
   */
  public void setEmail(String string)
  {
    _email = string;
  }

  /**
   * @return
   */
  public String getTel()
  {
    return _tel;
  }

  /**
   * @param string
   */
  public void setTel(String string)
  {
    _tel = string;
  }

  /**
   * @return
   */
  public String getFax()
  {
    return _fax;
  }

  /**
   * @param string
   */
  public void setFax(String string)
  {
    _fax = string;
  }

  /**
   * @return
   */
  public String getWebsite()
  {
    return _website;
  }

  /**
   * @param string
   */
  public void setWebsite(String string)
  {
    _website = string;
  }

  /**
   * @return
   */
  public String getAddress()
  {
    return _address;
  }

  /**
   * @param string
   */
  public void setAddress(String string)
  {
    _address = string;
  }

  /**
   * @return
   */
  public String getPoBox()
  {
    return _poBox;
  }

  /**
   * @param string
   */
  public void setPoBox(String string)
  {
    _poBox = string;
  }

  /**
   * @return
   */
  public String getCity()
  {
    return _city;
  }

  /**
   * @param string
   */
  public void setCity(String string)
  {
    _city = string;
  }

  /**
   * @return
   */
  public String getState()
  {
    return _state;
  }

  /**
   * @param string
   */
  public void setState(String string)
  {
    _state = string;
  }

  /**
   * @return
   */
  public String getZipCode()
  {
    return _zipCode;
  }

  /**
   * @param string
   */
  public void setZipCode(String string)
  {
    _zipCode = string;
  }

  /**
   * @return
   */
  public String getCountry()
  {
    return _country;
  }

  /**
   * @param string
   */
  public void setCountry(String string)
  {
    _country = string;
  }

  /**
   * @return
   */
  public String getLanguage()
  {
    return _language;
  }

  /**
   * @param string
   */
  public void setLanguage(String string)
  {
    _language = string;
  }

}