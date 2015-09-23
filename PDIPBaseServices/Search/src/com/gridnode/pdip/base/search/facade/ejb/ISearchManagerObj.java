/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISearchManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 18 2002    H.Sushil              Created
 */
package com.gridnode.pdip.base.search.facade.ejb;

import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;

import java.util.Collection;
import javax.ejb.EJBObject;
import java.rmi.RemoteException;
/**
 * Object for SearchManagerBean.
 *
 * @author H.Sushil
 *
 * @version 1.0
 * @since 1.0
 */
public interface ISearchManagerObj
  extends        EJBObject
{
  public String addComparisonConditionToQuery(Object connector,Object field,Object compOperator,Object value,boolean isFieldValue,boolean negate) throws SystemException,RemoteException;
  public String addInConditionToQuery(Object connector,Object field,Collection valueList,boolean negate) throws SystemException,RemoteException;
  public String addRangeConditionToQuery(Object connector,Object field,Object lowValue,Object highValue,boolean negate) throws SystemException,RemoteException;
  public Collection getResultFromQuery(String query) throws SystemException,RemoteException;
  public Collection getResultFromQuery() throws SystemException,RemoteException;
  public String getSelectQueryForTable(String tableName) throws SystemException,RemoteException;
  public Collection getValuesForFieldName(String tableName,String fieldName,Object field,Object compOperator,Object value,boolean isFieldValue,boolean negate,boolean distinct) throws SystemException,RemoteException;
  public void setQuery(String query) throws SystemException,RemoteException;

}