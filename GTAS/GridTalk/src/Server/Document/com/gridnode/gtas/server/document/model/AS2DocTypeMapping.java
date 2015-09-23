/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-08-28    Wong Yee Wah         Created
 */

package com.gridnode.gtas.server.document.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for AS2DocTypeMapping entity. A AS2DocTypeMapping is used to
 * define TP and Doc type mapping to a internal Doc Type. The mapping would
 * tell what document type of the AS2 Message. the.<P>
 *
 *The Model:<BR><PRE>
 *   UId                   - UID for a ProcessMapping entity instance.
 *   as2DocType            - Whether the ProcessMapping can be deleted.
 *   docType               - Current version of the ProcessMapping instance.
 *   partnerID             - Name of the Process to map.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all geters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Wong Yee Wah
 *
 * @version GT 4.0.2
 * @since 4.0.2
 */
public class AS2DocTypeMapping
            extends AbstractEntity
           implements IAS2DocTypeMapping
{
  private static final long serialVersionUID = -4255720561789105998L;
  protected String  _as2DocType;
  protected String  _docType;
  protected String  _partnerID ;
  
  public AS2DocTypeMapping()
  {
    
  }
  
//**************** Methods from AbstractEntity *********************

  public String getEntityDescr()
  {

    return new StringBuffer().append(getAs2DocType()).append('/').append(
                  getPartnerID()).append('/').append(getDocType()).toString();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // *************** Getters for attributes *************************
  public String getAs2DocType()
  {
    return _as2DocType;
  }
  
  public String getDocType()
  {
    return _docType;
  }
  
  public String getPartnerID()
  {
    return _partnerID;
  }
  
  //*************** Setters for attributes *************************
  public void setAs2DocType(String as2DocType)
  {
    _as2DocType = as2DocType;
  }
  
  public void setDocType(String docType)
  {
    _docType = docType;
  }
  
  public void setPartnerID(String partnerID)
  {
    _partnerID = partnerID;
  }
  
}
