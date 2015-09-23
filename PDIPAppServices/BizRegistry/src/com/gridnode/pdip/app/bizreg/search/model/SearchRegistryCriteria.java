/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SearchRegistryCriteria.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 01 2003    Neo Sok Lay         Created
 * Sep 09 2003    Neo Sok Lay         Add DUNS field.
 * Sep 18 2003    Neo Sok Lay         Add MessagingStandardClassifications 
 *                                    (non-persistent).
 * Mar 01 2006    Neo Sok Lay         Use generics                                   
 */
package com.gridnode.pdip.app.bizreg.search.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This is an object model for SearchRegistryCriteria entity. This represents
 * the search criteria that will be submitted for Registry search. 
 * This entity is not persistent to the database.
 *
 * The Model:<BR><PRE>
 *   BusEntityDesc       - Business Entity description.
 *   DUNS                - DUNS number
 *   MessagingStandards  - Collection of messaging standards supported by the business entities to search
 *   QueryUrl            - URL to use for querying from the registry.
 *   Match               - MATCH_ANY: Match any of the MessagingStandards specified.
 *                         MATCH_ALL: Match all of the MessagingStandards specified.
 *   MessagingStandardClassifications - Collection of classifications for the MessagingStandards. 
 * </PRE>
 * <P>
 *
 * @author Neo Sok Lay
 *
 * @version GT 4.0
 * @since GT 2.2
 */
public class SearchRegistryCriteria
  extends    AbstractEntity
  implements ISearchRegistryCriteria
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1478960228421060919L;
	protected String _busEntityDesc;
  protected String _duns;
  protected String _queryUrl;
  protected Collection<String> _messagingStandards = new ArrayList<String>();
  protected short _match = MATCH_ANY;
  protected Collection<String[]> _messagingStandardClassifications;
  protected String _dunsIdentifierName;
  
  public SearchRegistryCriteria()
  {
  }

  // ***************** Methods from AbstractEntity *************************

  public Number getKeyId()
  {
   return null;
  }

  public String getEntityDescr()
  {
    StringBuffer buff = new StringBuffer("Search ");
    buff.append(getQueryUrl()).append("/Match: ").append(getMessagingStandards());
    if (getBusEntityDesc() != null)
      buff.append(" and '").append(getBusEntityDesc()).append("'");
    if (getDUNS() != null)
      buff.append(" and '").append(getDUNS()).append("'");
      
    return buff.toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  // ******************** Getters for attributes ***************************

  public String getBusEntityDesc()
  {
    return _busEntityDesc;
  }

  public String getQueryUrl()
  {
    return _queryUrl;
  }

  public Collection<String> getMessagingStandards()
  {
    return _messagingStandards;
  }

  public short getMatch()
  {
    return _match;
  }
  
  public String getDUNS()
  {
    return _duns;
  }
  
  public String getDunsIdenfierName()
  {
    return _dunsIdentifierName;
  }
  
  public Collection<String[]> getMessagingStandardClassifications()
  {
    return _messagingStandardClassifications;
  }
  
  // ******************** Setters for attributes ***************************

  public void setBusEntityDesc(String desc)
  {
    _busEntityDesc = desc;
  }
  
  public void setDUNS(String duns)
  {
    _duns = duns;
  }

  public void setQueryUrl(String queryUrl)
  {
    _queryUrl = queryUrl;
  }

  public void setMessagingStandards(Collection<String> messagingStandards)
  {
    _messagingStandards = messagingStandards;
  }
  
  public void setMatch(short match)
  {
    _match = match;
  }
  
  public void setDunsIdentifierName(String name)
  {
    _dunsIdentifierName = name;  
  }  
  
  public void setMessagingStandardClassifications(Collection<String[]> classifications)
  {
    _messagingStandardClassifications = classifications;
  }
}
