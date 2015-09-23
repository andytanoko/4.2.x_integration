/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CompanyProfileAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-10     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.gridnode;

import org.apache.struts.action.*;
import javax.servlet.http.*;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class CompanyProfileAForm extends GTActionFormBase
{
  private String _coyName;
  private String _email;
  private String _altEmail;
  private String _tel;
  private String _altTel;
  private String _fax;
  private String _address;
  private String _city;
  private String _state;
  private String _zipCode;
  private String _country;
  private String _language;

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _address = null;
  }

  public String getCoyName()
  { return _coyName; }

  public void setCoyName(String coyName)
  { _coyName=coyName; }

  public String getEmail()
  { return _email; }

  public void setEmail(String email)
  { _email=email; }

  public String getAltEmail()
  { return _altEmail; }

  public void setAltEmail(String altEmail)
  { _altEmail=altEmail; }

  public String getTel()
  { return _tel; }

  public void setTel(String tel)
  { _tel=tel; }

  public String getAltTel()
  { return _altTel; }

  public void setAltTel(String altTel)
  { _altTel=altTel; }

  public String getFax()
  { return _fax; }

  public void setFax(String fax)
  { _fax=fax; }

  public String getAddress()
  { return _address; }

  public void setAddress(String address)
  { _address=address; }

  public String getCity()
  { return _city; }

  public void setCity(String city)
  { _city=city; }

  public String getState()
  { return _state; }

  public void setState(String state)
  { _state=state; }

  public String getZipCode()
  { return _zipCode; }

  public void setZipCode(String zipCode)
  { _zipCode=zipCode; }

  public String getCountry()
  { return _country; }

  public void setCountry(String country)
  { _country=country; }

  public String getLanguage()
  { return _language; }

  public void setLanguage(String language)
  { _language=language; }
}