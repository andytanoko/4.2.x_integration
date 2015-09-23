// %1023788044028:com.gridnode.pdip.base.time%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.value;

import com.gridnode.pdip.framework.db.dao.EntityDAOImpl;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.FilterConnector;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.keygen.KeyGen;
import com.gridnode.pdip.framework.log.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ExEntityDAOImpl
  extends EntityDAOImpl
{

  /**
   * Constructor for ExEntityDAOImpl.
   *
   * @param entityName
   */
  public ExEntityDAOImpl(String entityName)
  {
    super(entityName);
  }

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public Long getNextKeyId()
                    throws Exception
  {
    return (KeyGen.getNextId(_sqlTableName));
  }

  /**
   * DOCUMENT ME!
   *
   * @param size DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public List getNextKeyIds(int size)
                     throws Exception
  {
    List res = new ArrayList(size);
    for (int i = 0; i < size; i++)
    {
      res.add(KeyGen.getNextId(_sqlTableName));
    }
    return res;
  }

  /**
   * DOCUMENT ME!
   *
   * @param entityList DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public void create(List entityList)
              throws Exception
  {
    Long primaryKey = null;
    Connection dbConnection = null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection = getConnection();
      stmt = getPreparedStatement(dbConnection, _insertQuery);
      int size = entityList.size();
      for (int i = 0; i < size; i++)
      {
        AbstractEntity entity = (AbstractEntity)entityList.get(i);
        if (entity.getKey() == null)
        {
          primaryKey = KeyGen.getNextId(_sqlTableName);
          entity.setFieldValue(entity.getKeyId(), primaryKey);
        }
        entity.setAvailableFields(false, null);
        setEntityToPs(entity, stmt);
        executeUpdate(stmt);
      }
    }
    catch (Exception e)
    {
      throw e;
    }
    finally
    {
      releaseResources(stmt, dbConnection);
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param filter DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public String getDeleteFilterQuery(IDataFilter filter)
                              throws Exception
  {
    try
    {
      return "DELETE FROM " + _sqlTableName +
             ((filter != null) ? (" WHERE " + convertFilterToQuery(filter)) : "");
    }
    catch (Exception e)
    {
      throw new Exception("Exception in [ExEntityDAOImpl.getDeleteFilterQuery] making Sql Query :" +
                          e.toString());
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param filter DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public int deleteByFilter(IDataFilter filter)
                     throws Exception
  {
    String sqlQuery = getDeleteFilterQuery(filter);
    Log.debug(Log.DB,
              "[ExEntityDAOImpl.deleteByFilter] for entity [" + _entityName +
              "] filterQuery=" + sqlQuery);
    Connection dbConnection = null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection = getConnection();
      stmt = getPreparedStatement(dbConnection, sqlQuery);
      return executeUpdate(stmt);
    }
    catch (Exception e)
    {
      throw new Exception("[EntityDAOImpl.deleteByFilter] for entity[" +
                          _entityName + "]" + ",The SQL Query [" + sqlQuery +
                          "] ,The Exception is [" + e.toString() + "]");
    }
    finally
    {
      releaseResources(stmt, dbConnection);
    }
  }

  //delete by foreign key

  /**
   * DOCUMENT ME!
   *
   * @param fieldIds DOCUMENT ME!
   * @param values DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public int deleteByFKey(Number[] fieldIds, Object[] values)
                   throws Exception
  {
    IDataFilter filter = fKey2Filter(fieldIds, values);
    return deleteByFilter(filter);
  }

  /**
   * DOCUMENT ME!
   *
   * @param filter DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public String getLoadFilterQuery(IDataFilter filter)
                            throws Exception
  {
    try
    {
      return "Select * FROM " + _sqlTableName +
             ((filter != null) ? (" WHERE " + convertFilterToQuery(filter)) : "");
    }
    catch (Exception e)
    {
      throw new Exception("Exception in [ExEntityDAOImpl.getLoadFilterQuery] making Sql Query :" +
                          e.toString());
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param filter DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public List loadByFilter(IDataFilter filter)
                    throws Exception
  {
    String sqlQuery = getLoadFilterQuery(filter);
    Log.debug(Log.DB,
              "[ExEntityDAOImpl.loadByFilter] for entity [" + _entityName +
              "] filterQuery=" + sqlQuery);
    Connection dbConnection = null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection = getConnection();
      stmt = getPreparedStatement(dbConnection, sqlQuery);
      Collection results = executeQuery(stmt);
      if (results == null || results.size() == 0)
      {
        return new ArrayList();
        //                throw new Exception("Exception in [ExEntityDAOImpl.load] for entity["+entityName+"],"
        //                                        +" Record with filter ["+filter+"] does not exist in database");
      }
      return new ArrayList(results);
    }
    catch (Exception e)
    {
      throw new Exception("Exception in [ExEntityDAOImpl.load] for entity[" +
                          _entityName + "] with filter [" + filter +
                          "] ,The Exception is [" + e.toString() + "]");
    }
    finally
    {
      releaseResources(stmt, dbConnection);
    }
  }

  //delete by foreign key

  /**
   * DOCUMENT ME!
   *
   * @param fieldIds DOCUMENT ME!
   * @param values DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public List loadByFKey(Number[] fieldIds, Object[] values)
                  throws Exception
  {
    IDataFilter filter = fKey2Filter(fieldIds, values);
    return loadByFilter(filter);
  }

  protected IDataFilter fKey2Filter(Number[] fieldIds, Object[] values)
  {
    IDataFilter filter = new DataFilterImpl();
    int size = fieldIds.length;
    FilterConnector connector = null;
    for (int i = 0; i < size; i++)
    {
      filter.addSingleFilter(connector, fieldIds[i], filter.getEqualOperator(),
                             values[i], false);
      connector = filter.getAndConnector();
    }
    return filter;
  }
}