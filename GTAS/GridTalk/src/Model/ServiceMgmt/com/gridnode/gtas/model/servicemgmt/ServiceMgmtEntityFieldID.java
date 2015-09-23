/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceMgmtEntityFieldID.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Feb 9, 2004 			Mahesh             	Created
 * Feb 06 2007    Chong SoonFui       Commented IWebService.CAN_DELETE,IServiceAssignment.CAN_DELETE
 */
package com.gridnode.gtas.model.servicemgmt;

import java.util.Hashtable;

public class ServiceMgmtEntityFieldID
{
  private Hashtable _table;
  private static ServiceMgmtEntityFieldID _self = null;

  private ServiceMgmtEntityFieldID()
  {
    _table = new Hashtable();


    //WebService
    _table.put(IWebService.ENTITY_NAME,
      new Number[]
      {
        IWebService.UID,
        IWebService.WSDL_URL,
        IWebService.END_POINT,
        IWebService.SERVICE_NAME,
        IWebService.SERVICE_GROUP,
//        IWebService.CAN_DELETE,
        IWebService.VERSION
      });

    //ServiceAssignment
    _table.put(IServiceAssignment.ENTITY_NAME,
      new Number[]
      {
        IServiceAssignment.UID,
        IServiceAssignment.USER_NAME,
        IServiceAssignment.PASSWORD,
        IServiceAssignment.USER_TYPE,
        IServiceAssignment.WEBSERVICE_UIDS,
//        IServiceAssignment.CAN_DELETE,
        IServiceAssignment.VERSION
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new ServiceMgmtEntityFieldID();
    }
    return _self._table;
  }
}
