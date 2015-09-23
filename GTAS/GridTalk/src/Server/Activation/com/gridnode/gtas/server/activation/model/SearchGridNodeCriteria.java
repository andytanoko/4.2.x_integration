/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchGridNodeCriteria.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2001    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.activation.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for SearchGridNodeCriteria entity. This represents
 * the search criteria that will be converted to a data filter condition for
 * submission of GridNode search. This entity is not persistent to the database.
 *
 * The Model:<BR><PRE>
 *   CritieriaType  - Type of Criteria: None, By Gridnode, By Whitepage
 *   MatchType      - Type of match: All, Any
 *   GridNodeID     - GridNode ID.
 *   GridNodeName   - GridNode Name.
 *   Category       - GridNode Category.
 *   BusDesc        - Business description.
 *   DUNS           - Data Universal Numbering System
 *   ContactPerson  - Contact person.
 *   Email          - Email of ContactPerson.
 *   Tel            - Telephone number of ContactPerson.
 *   Fax            - Fax number of ContactPerson.
 *   Website        - Website URL.
 *   Country        - Country code.
 * </PRE>
 * <P>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class SearchGridNodeCriteria
  extends    AbstractEntity
  implements ISearchGridNodeCriteria
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5748348948840432668L;
	protected short   _criteriaType;
  protected short   _matchType;
  protected Integer _gridnodeID;
  protected String  _gridnodeName;
  protected String  _category;
  protected String  _busDesc;
  protected String  _dUNS;
  protected String  _globalSCCode;
  protected String  _contactPerson;
  protected String  _email;
  protected String  _tel;
  protected String  _fax;
  protected String  _website;
  protected String  _address;
  protected String  _country;

  public SearchGridNodeCriteria()
  {
  }

  // ***************** Methods from AbstractEntity *************************

  public Number getKeyId()
  {
   return null;
  }

  public String getEntityDescr()
  {
   return "Search:"+ getCriteriaType() + "/Match:" + getMatchType();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  // ******************** Getters for attributes ***************************

  public short getCriteriaType()
  {
    return _criteriaType;
  }

  public short getMatchType()
  {
    return _matchType;
  }

  public Integer getGridNodeID()
  {
    return _gridnodeID;
  }

  public String getGridNodeName()
  {
    return _gridnodeName;
  }

  public String getCategory()
  {
    return _category;
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

  public String getCountry()
  {
    return _country;
  }

  // ******************** Setters for attributes ***************************

  public void setCriteriaType(short criteriaType)
  {
    _criteriaType = criteriaType;
  }

  public void setMatchType(short matchType)
  {
    _matchType = matchType;
  }

  public void setGridNodeID(Integer gridnodeID)
  {
    _gridnodeID = gridnodeID;
  }

  public void setGridNodeName(String gridnodeName)
  {
    _gridnodeName = gridnodeName;
  }

  public void setCategory(String category)
  {
    _category = category;
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

  public void setCountry(String country)
  {
    _country = country;
  }

}
