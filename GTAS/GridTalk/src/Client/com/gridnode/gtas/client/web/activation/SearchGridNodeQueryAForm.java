/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchGridNodeQueryAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-17     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.activation;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

import com.gridnode.gtas.client.utils.StaticUtils;

public class SearchGridNodeQueryAForm extends GTActionFormBase
{
  private String _searchId;
  private String _dtSubmitted;
  private String _dtResponded;
  private String _criteriaType;
  private String _matchType;
  private String _gridNodeId;
  private String _gridNodeName;
  private String _category;
  private String _businessDesc;
  private String _duns;
  private String _contactPerson;
  private String _email;
  private String _tel;
  private String _fax;
  private String _website;
  private String _country;

  public String getSearchId()
  { return _searchId; }

  public void setSearchId(String searchId)
  { _searchId=searchId; }

  public String getDtSubmitted()
  { return _dtSubmitted; }

  public void setDtSubmitted(String dtSubmitted)
  { _dtSubmitted=dtSubmitted; }

  public String getDtResponded()
  { return _dtResponded; }

  public void setDtResponded(String dtResponded)
  { _dtResponded=dtResponded; }

  public String getCriteriaType()
  { return _criteriaType; }

  public Short getCriteriaTypeShort()
  { return StaticUtils.shortValue(_criteriaType); }

  public void setCriteriaType(String criteriaType)
  { _criteriaType=criteriaType; }

  public String getMatchType()
  { return _matchType; }

  public void setMatchType(String matchType)
  { _matchType=matchType; }

  public String getGridNodeId()
  { return _gridNodeId; }

  public void setGridNodeId(String gridNodeId)
  { _gridNodeId=gridNodeId; }

  public String getGridNodeName()
  { return _gridNodeName; }

  public void setGridNodeName(String gridNodeName)
  { _gridNodeName=gridNodeName; }

  public String getCategory()
  { return _category; }

  public void setCategory(String category)
  { _category=category; }

  public String getBusinessDesc()
  { return _businessDesc; }

  public void setBusinessDesc(String businessDesc)
  { _businessDesc=businessDesc; }

  public String getDuns()
  { return _duns; }

  public void setDuns(String duns)
  { _duns=duns; }

  public String getContactPerson()
  { return _contactPerson; }

  public void setContactPerson(String contactPerson)
  { _contactPerson=contactPerson; }

  public String getEmail()
  { return _email; }

  public void setEmail(String email)
  { _email=email; }

  public String getTel()
  { return _tel; }

  public void setTel(String tel)
  { _tel=tel; }

  public String getFax()
  { return _fax; }

  public void setFax(String fax)
  { _fax=fax; }

  public String getWebsite()
  { return _website; }

  public void setWebsite(String website)
  { _website=website; }

  public String getCountry()
  { return _country; }

  public void setCountry(String country)
  { _country=country; }
}