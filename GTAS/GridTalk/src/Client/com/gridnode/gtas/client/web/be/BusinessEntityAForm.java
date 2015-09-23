/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BusinessEntityAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-07     Andrew Hill         Created
 * 2002-10-08     Andrew Hill         "partnerCat" mods
 * 2002-11-25     Andrew Hill         Clear address in reset as using textarea now
 * 2002-12-17     Andrew Hill         beTab
 * 2004-01-02     Daniel D'Cotta      Added DomainIdentifiers
 */
package com.gridnode.gtas.client.web.be;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class BusinessEntityAForm extends GTActionFormBase
{
  private String _beTab; //20021217AH

  private boolean _isNewEntity;

  private String _enterpriseId;
  private String _id;
  private String _description;
  private String _forPartner;
  private String _publishable;
  private String _syncToServer;
  private String _beState;
  private String[] _channels;
  private String _partnerCategory;

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

  private ArrayList _domainIdentifiers;
  private String _updateAction;
  
  public BusinessEntityAForm()
  {
    _domainIdentifiers = new ArrayList();
  }

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    // Fields for checkboxes must be reset due to dumb way html checkboxes are(nt) submitted
    if(_isNewEntity)
    {
      _forPartner = "false";
    }
    _publishable = "false";
    _syncToServer = "false";
    _channels = null;
    _address = null;

    // reset all the nested action forms
    if(_domainIdentifiers != null)
    {
      Iterator i = _domainIdentifiers.iterator();
      while(i.hasNext())
      {
        ((DomainIdentifierAForm)i.next()).reset(mapping, request);
      }
    }
  }

  public void doNetscape6Reset(ActionMapping p0, HttpServletRequest request)
  {
    //I hate Netscape6
    //die netscape! die!
    //<sigh/>
    String tab = request.getParameter("beTabOld");
    if("be_tab".equals(tab))
    {
      if(_isNewEntity)
      {
        _forPartner = "false";
      }
      _publishable = "false";
      _syncToServer = "false";
      _channels = null;
    }
    else if("whitePage_tab".equals(tab))
    {
      _address = null;
    }
  }

  public void setBeTab(String tab)
  { //20021217AH
    _beTab = tab;
  }

  public String getBeTab()
  { //20021217AH
    return _beTab;
  }

  void setIsNewEntity(boolean flag)
  { _isNewEntity = flag; }

  public boolean isNewEntity()
  { return _isNewEntity; }

  public String[] getChannels()
  {
    return _channels;
  }

  public void setChannels(String[] values)
  {
    _channels = values;
  }

  public String getBeState()
  {
    return _beState;
  }

  public void setBeState(String value)
  {
    _beState = value;
  }

  public String getEnterpriseId()
  {
    return _enterpriseId;
  }

  public void setEnterpriseId(String value)
  {
    _enterpriseId = value;
  }

  public String getId()
  {
    return _id;
  }

  public void setId(String value)
  {
    _id = value;
  }

  public String getDescription()
  {
    return _description;
  }

  public void setDescription(String value)
  {
    _description = value;
  }

  public String getForPartner()
  {
    return _forPartner;
  }

  public void setForPartner(String value)
  {
    _forPartner = value;
  }

  public String getPublishable()
  {
    return _publishable;
  }

  public void setPublishable(String value)
  {
    _publishable = value;
  }

  public String getSyncToServer()
  {
    return _syncToServer;
  }

  public void setSyncToServer(String value)
  {
    _syncToServer = value;
  }

  public void setPartnerCategory(String value)
  { _partnerCategory = value; }

  public String getPartnerCategory()
  { return _partnerCategory; }

  //

  public String getBusinessDesc()
  {
    return _businessDesc;
  }

  public void setBusinessDesc(String value)
  {
    _businessDesc = value;
  }

  public String getDuns()
  {
    return _duns;
  }

  public void setDuns(String value)
  {
    _duns = value;
  }

  public String getGlobalSupplyChainCode()
  {
    return _globalSupplyChainCode;
  }

  public void setGlobalSupplyChainCode(String value)
  {
    _globalSupplyChainCode = value;
  }

  public String getContactPerson()
  {
    return _contactPerson;
  }

  public void setContactPerson(String value)
  {
    _contactPerson = value;
  }

  public String getEmail()
  {
    return _email;
  }

  public void setEmail(String value)
  {
    _email = value;
  }

  public String getTel()
  {
    return _tel;
  }

  public void setTel(String value)
  {
    _tel = value;
  }

  public String getFax()
  {
    return _fax;
  }

  public void setFax(String value)
  {
    _fax = value;
  }

  public String getWebsite()
  {
    return _website;
  }

  public void setWebsite(String value)
  {
    _website = value;
  }

  public String getAddress()
  {
    return _address;
  }

  public void setAddress(String value)
  {
    _address = value;
  }

  public String getPoBox()
  {
    return _poBox;
  }

  public void setPoBox(String value)
  {
    _poBox = value;
  }

  public String getCity()
  {
    return _city;
  }

  public void setCity(String value)
  {
    _city = value;
  }

  public String getState()
  {
    return _state;
  }

  public void setState(String value)
  {
    _state = value;
  }

  public String getZipCode()
  {
    return _zipCode;
  }

  public void setZipCode(String value)
  {
    _zipCode = value;
  }

  public String getCountry()
  {
    return _country;
  }

  public void setCountry(String value)
  {
    _country = value;
  }

  public String getLanguage()
  {
    return _language;
  }

  public void setLanguage(String value)
  {
    _language = value;
  }
  
  public DomainIdentifierAForm[] getDomainIdentifiers()
  {
    return (DomainIdentifierAForm[])_domainIdentifiers.toArray(new DomainIdentifierAForm[_domainIdentifiers.size()]);
  }

  public void setDomainIdentifiers(DomainIdentifierAForm[] forms)
  {
    _domainIdentifiers = StaticUtils.arrayListValue(forms);
  }
  
  public void addNewDomainIdentifier(DomainIdentifierAForm domainIdentifier)
  {
    if(domainIdentifier == null)
    {
      domainIdentifier = new DomainIdentifierAForm();
    } 
    _domainIdentifiers.add(domainIdentifier);
  }
  
  public void removeSelectedDomainIdentifiers()
  {
    if( _domainIdentifiers == null) return;
    ListIterator li = _domainIdentifiers.listIterator();
    while(li.hasNext())
    {
      DomainIdentifierAForm domainIdentifierForm = (DomainIdentifierAForm)li.next();
      if(domainIdentifierForm.isSelected())
      {
        li.remove();
      } 
    }
  }

  public String getUpdateAction()
  {
    return _updateAction;
  }

  public void setUpdateAction(String updateAction)
  {
    _updateAction = updateAction;
  }  
  
}