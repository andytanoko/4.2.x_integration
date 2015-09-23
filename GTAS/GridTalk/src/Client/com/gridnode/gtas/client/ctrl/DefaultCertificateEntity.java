/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultCertificateEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-13     Andrew Hill         Created
 * 2002-01-17     Andrew Hill         Unload x500Name if certain fields are changed
 * 2003-01-21     Andrew Hill         canEdit();
 * 2003-01-22     Andrew Hill         There are 2 x500Name entities
 * 2003-04-15     Andrew Hill         No longer implement canEdit, export store methods
 * 2006-07-26     Tam Wei Xiang       Override super class getFieldValue(...) to provide
 *                                    additional handling for SerialNum, StartDate, and
 *                                    EndDate.
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

class DefaultCertificateEntity extends AbstractGTEntity
  implements IGTCertificateEntity
{
  protected Object fieldValueChanging(Number fieldId,
                                      IGTFieldMetaInfo fmi,
                                      Object newValue,
                                      Object oldValue)
    throws GTClientException
  { //20030117AH
    if( fieldId.equals(CERT_FILE)
        || fieldId.equals(IS_PARTNER)
        || fieldId.equals(NAME) )
    { //If any of these change, invalidate the x500Name info to force a reload next time
      //it is accessed
      Object x500Name = UnloadedFieldToken.getInstance();
      setNewFieldValue(ISSUER_DETAILS,x500Name); //20030122AH
      setNewFieldValue(SUBJECT_DETAILS,x500Name); //20030122AH
    }
    //Delegate back to superclass for normal handling
    return super.fieldValueChanging(fieldId,fmi,newValue,oldValue);
  }

  /*public boolean canEdit() throws GTClientException
  { //20030121AH
    //Current requirements do not allow editing of existing certificate entities
    return this.isNewEntity();
  }*/

  public void exportKeyStore() throws GTClientException
  { //20030415AH
    if(Boolean.TRUE.equals((Boolean)getFieldValue(IS_PARTNER)))
    {
      throw new UnsupportedOperationException("Partner certificates may not be exported to key store");
    }
    else
    {
      ((IGTCertificateManager)_manager).exportKeyStore(getUidLong());
    }
  }

  public void exportTrustStore() throws GTClientException
  { //20030415AH
    if(Boolean.TRUE.equals((Boolean)getFieldValue(IS_PARTNER)))
    {
      ((IGTCertificateManager)_manager).exportTrustStore(getUidLong());
    }
    else
    {
      throw new UnsupportedOperationException("Only partner certificates may be exported to trust store");
    }
  }
  
  //TWX 26072006 In certain stage(the cert entity is new and user has clicked the add/remove btn),
  //             we need to treat the serialNum, startDate and endDate like a virtualField.
  //             However, while the cert record already in DB, serialNum, startDate, endDate has to be
  //             loaded from DB.
  //             see DefaultCertificateManager.loadField
  public Object getFieldValue(Number field)
  	throws GTClientException
  {
  	Object o = _fields.get(field);

  	if(o == null)
  	{ //20020911AH
  		if(isFromCache() && !getFieldMetaInfo(field).isAvailableInCache())
  		{
  			throw new java.lang.UnsupportedOperationException("Field "
  			                                                  + field + " not available in cached entity " + this);
  		}
  	}

  	if(o instanceof UnloadedFieldToken 
  			|| 
  			(this.isNewEntity() && o ==null && (IGTCertificateEntity.START_DATE.equals(field) ||
  					                    IGTCertificateEntity.END_DATE.equals(field) ||
  					                    IGTCertificateEntity.SERIAL_NUM.equals(field)
  			                       )		
  			)
  		)
  	{ // Process request for value of load-on-demand field
  		_manager.loadField(field,this);
  		o = _fields.get(field);
  	}
  	return o;
  }
}