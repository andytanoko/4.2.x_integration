/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDAOImpl.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 *                Mahesh              Created
 * Apr 12 2002    Neo Sok Lay         To skip fieldmetainfo with no SqlName
 *                                    when constructing sql queries.
 * Apr 25 2002    Mahesh              To use ObjectName from EntityMetaInfo since
 *                                    entityName is only ShortName and ObjectName is
 *                                    Class name.
 * Apr 19 2002    Neo Sok Lay         Patch the converted SQL filter to change
 *                                    "boolean" values to '0's (false) or '1's(true).
 * Apr 25 2002    Neo Sok Lay         Add methods to retrieve entity by a filter.
 * Apr 26 2002    Neo Sok Lay         Throw more specific exceptions.
 * May 02 2002    Neo Sok Lay         Add method to get count of entities.
 * May 03 2002    Neo Sok Lay         Use SqlFilterFactory to construct sql statements.
 *                                    Add "Order By" in query.
 *                                    Re-format according to coding standard.
 * May 16 2002    Neo Sok Lay         Remove patching for boolean values --
 *                                    taken care of by SqlFilterFactory.
 * May 23 2002    Neo Sok Lay         Convert field value before set into
 *                                    prepared statement. This is to handle
 *                                    conversion required for special object
 *                                    types.
 * Jun 12 2002    Ang Meng Hua        Modify setRsToEntity(), setEntityToPs() &
 *                                    getEntityFromRs() to handle embedded entity
 * Jul 08 2002    Neo Sok Lay         Enhance key generation: make sure that
 *                                    generated key is not already used.
 * Jul 30 2002    Jagadeesh           Modify all reference of ConfigManager to
 *                                    use new ConfigurationManager.Restructur
 *                                    the Configuration Constants to IFrameworkConfig.
 * Sep 04 2002    Neo Sok Lay         Obtain DataSource from DataSourceSelector
 *                                    instead of embedding the logic here. Also
 *                                    this allows for accessing different
 *                                    datasources for different tables.
 * Sep 16 2002    Neo Sok Lay         Obtain column value from result set using
 *                                    specific getters for binary & blob fields.
 * Oct 16 2002    Neo Sok Lay         Ensure the embedded entity is persistent
 *                                    before loading/saving.
 * Dec 11 2002    Neo Sok Lay         Use MetaInfoFactory instead of EntityMetaInfoLoader.
 * Dec 22 2002    Neo Sok Lay         Rectify bug introduced by Mahesh when
 *                                    checking for "embedded" property. Should
 *                                    be "type" property having the value "embedded".
 *                                    "foreign" types with caching must also be
 *                                    considered. In order not to make the same
 *                                    lengthy checking everywhere, this check is
 *                                    built into FieldMetaInfo.isEntity() method.
 * Oct 17 2003    Neo Sok Lay         Include SortOrders when applying Order syntax to
 *                                    the filter query.
 * Jan 05 2004    Mahesh              Added new create method to create entity with uid
 *                                    passed with entity
 * Jul 13 2004    Mahesh              Added code to convert binary varchar to string in 
 *                                    getColValueFromRs method
 * Oct 17 2005    Neo Sok Lay         For JDK1.5 compliance: change getFieldValuesByFilter()
 *                                    - empty while stmt to empty block                                   
 * Oct 21 2005    Neo Sok Lay         Support select distinct by fields for single field
 *                                    as well as entity.                                   
 * Nov 06 2006    Tam Wei Xiang       Resolved the inefficient way to generate the UID for
 *                                    the entity. See GNDB00027928 for detail       
 * Jan 25 2007    Neo Sok Lay         For Oracle, the Date and Timestamp datatypes need to use
 *                                    TODATE function when querying or update.
 * Jan 30 2007    Chong SoonFui       For Oracle, all NUMBER datatypes are mapped to 
 *                                    java.math.BigDecimal, so it's needed to map the 
 *                                    datatypes to the correct datatype according to 
 *                                    ValueClass in fieldmetainfo.
 *                                    TODATE function when querying or update.
 * Feb 05 2007    Neo Sok Lay         Use SELECT...FOR UPDATE for load().
 * Feb 09 2007    Neo Sok Lay         Revert FOR UPDATE change. --- may cause system hang.                                                                                                  
 *                                    the entity. See GNDB00027928 for detail
 * Feb 07 2007		Alain Ah Ming				Use new error codes
 * 																		Log warning message if throwing up exception
 * Feb 28 2007    Neo Sok Lay         Only convert to Long for Number types.  
 * Jun 09 2007    Tam Wei Xiang       Support Select MIN, MAX                                                                     
 */
package com.gridnode.pdip.framework.db.dao;

import java.sql.*;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import javax.sql.DataSource;

import com.gridnode.pdip.framework.db.ObjectConverter;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterFactory;
import com.gridnode.pdip.framework.db.filter.FilterFactoryRegistry;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.keygen.KeyGen;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.Log;

/**
 * 
 * @author Mahesh
 * @version GT 4.0 VAN
 */
public class EntityDAOImpl implements IEntityDAO
{
  public String _entityName;
  public String _sqlTableName;
  public IEntity _entity;

  private DataFilterFactory _sqlFactory;
  private Number _pkFieldId;
  private Number _fieldIds[];
  private String _sqlFieldNames[];
  private String _pkSqlName;
  private Hashtable<String,Integer> _colDefs = new Hashtable<String,Integer>();
  //private Collection _keySet;

  protected String  _insertQuery,
                  _updateQuery,
                  _loadQuery,
                  _deleteQuery,
                  _filterQuery,
                  _finderQuery,
                  _selectQuery,
                  _countQuery,
                  _maxQuery;
  
  private Object _lock = new Object();
  
  public EntityDAOImpl(String entityName)
  {
    this._entityName = entityName;
    this._sqlFactory = FilterFactoryRegistry.getFilterFactory("sql");
    try
    {
      EntityMetaInfo entityMetaInfo = getMetaInfo();
      //250402MAHESH: Changed to use entityMetaInfo.getObjectName() since entityName is only the short name
      //pkFieldId=((IEntity)Class.forName(entityName).newInstance()).getKeyId();
      try
			{
				_pkFieldId=((IEntity)Class.forName(entityMetaInfo.getObjectName()).newInstance()).getKeyId();
	      setQueries(entityMetaInfo);
	      initColumnDefinitions();
			}
			catch (InstantiationException e)
			{
				Log.error(ILogErrorCodes.ENTITY_META_INFO_READ, 
				          Log.DB, "[EntityDAOImpl.<init>] Failed to load class for: "+entityName+". Error: "+e.getMessage() ,
				          e);
			}
			catch (IllegalAccessException e)
			{
				Log.error(ILogErrorCodes.ENTITY_META_INFO_READ, 
				          Log.DB, "[EntityDAOImpl.<init>] Failed to load class for: "+entityName+". Error: "+e.getMessage() ,
				          e);
			}
			catch (ClassNotFoundException e)
			{
				Log.error(ILogErrorCodes.ENTITY_META_INFO_READ, 
				          Log.DB, "[EntityDAOImpl.<init>] Failed to load class for: "+entityName+". Error: "+e.getMessage() ,
				          e);
			}
    }
    catch(Exception e)
    {
      Log.error(ILogErrorCodes.ENTITY_META_INFO_READ, 
                Log.DB,
                "[EntityDAOImpl.<init>]Failed to initialize column definitions for: "+entityName+". Error: "+e.getMessage(),
                e);
    }
  }

  public void setQueries(EntityMetaInfo entityMetaInfo)
  {
    _sqlTableName=entityMetaInfo.getSqlName();
    setFieldIds(entityMetaInfo);
    setInsertQuery(entityMetaInfo);
    setLoadQuery(entityMetaInfo);
    setUpdateQuery(entityMetaInfo);
    setDeleteQuery(entityMetaInfo);
    setFilterQuery(entityMetaInfo);
    setFinderQuery(entityMetaInfo);
    setSelectQuery(entityMetaInfo);
    setCountQuery(entityMetaInfo);
    setMaxQuery(entityMetaInfo);
//    Log.debug(Log.DB,"From EntityDAOImpl : \r\n["+toString()+"\r\n]");
  }

  public Long create(IEntity entity) throws Exception
  {
		return create(entity,false);
  }

	public Long create(IEntity entity,boolean useUID) throws Exception
	{
		Long primaryKey=null;
		Connection dbConnection=null;
		PreparedStatement stmt = null;
		Log.debug(Log.DB,"create enter");
		if(useUID)
			primaryKey=(Long)entity.getKey();
		else
		{	
			//TWX 06112006 Check whether the PK generated by the KeyGen is in used in the table. If it is,
			//a new PK will be generated which is derived from the MAX(UID)+1 of the table.
			primaryKey = createNewKey(true);
		}

		try
		{
			entity.setFieldValue(entity.getKeyId(),primaryKey);
			((AbstractEntity)entity).setAvailableFields(false, null);
		}
		catch (Exception ex)
		{
			Log.warn(Log.DB, "[EntityDAOImpl.create] Error ", ex);
			throw new ApplicationException(ex.getMessage());
		}
		
		try
		{
			dbConnection=getConnection();
			stmt=getPreparedStatement(dbConnection, _insertQuery);
			setEntityToPs(entity,stmt);
			executeUpdate(stmt);
			Log.debug(Log.DB,"create stmt executed");  
		}
		catch(Exception e)
		{
			//Log.err(Log.DB, "[EntityDAOImpl.create] System Error ", e);
			throw new SystemException("Create entity error", e);
		}
		finally
		{
			releaseResources(stmt,dbConnection);
			Log.debug(Log.DB,"create resource released");        
		}
		return primaryKey;
	}
	
  public IEntity load(Long primaryKey) throws Exception
  {
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    Collection results = null;

    try
    {
      dbConnection=getConnection();
      stmt = getPreparedStatement(dbConnection, _loadQuery);
      stmt.setLong(1,primaryKey.longValue());
      results = executeQuery(stmt);
    }
    catch(Exception ex)
    {
      throw new SystemException(ex);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }

    if(results==null || results.size()==0)
    {
      throw new ApplicationException(
        "Record with primaryKey ["+primaryKey+"] does not exist in database");
    }
    return (IEntity)results.toArray()[0];
  }

  public void store(IEntity entity) throws  Exception
  {
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection=getConnection();
      stmt=getPreparedStatement(dbConnection, _updateQuery);
      ((AbstractEntity)entity).setAvailableFields(false, null);
      setEntityToPs(entity,stmt);
      executeUpdate(stmt);
    }
    catch(Exception e)
    {
      throw new SystemException(e);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
  }

  public void remove(Long primaryKey) throws Exception
  {
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection=getConnection();
      stmt=getPreparedStatement(dbConnection, _deleteQuery);
      if(primaryKey!=null)
      {
        stmt.setObject(1,primaryKey);
        executeUpdate(stmt);
      }
    }
    catch(Exception e)
    {
      throw new SystemException(e);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
  }

  public void removeByFilter(IDataFilter filter) throws Exception
  {
    String deleteQuery = _sqlFactory.getDeleteSyntax(_sqlTableName);

    if(filter==null)
      throw new Exception("IDataFilter for removing is null");
    filter = ObjectConverter.convertToSQLFilter(_entityName,filter);
    
    String condition = filter.getFilterExpr();
    if(condition==null || condition.trim().length()==0)
      throw new Exception("condition for removing is invalid, condition="+condition);

    deleteQuery = _sqlFactory.applyConditionSyntax(deleteQuery, condition);    
    
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection=getConnection();
      stmt=getPreparedStatement(dbConnection, deleteQuery);
      executeUpdate(stmt);
    }
    catch(Exception e)
    {
      throw new SystemException(e);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
  }

  public Long findByPrimaryKey(Long primaryKey) throws Exception
  {
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    Collection result = null;
    try
    {
      dbConnection=getConnection();
      stmt=getPreparedStatement(dbConnection, _finderQuery);
      stmt.setObject(1,primaryKey);
      result = executeFinder(stmt);
    }
    catch(Exception ex)
    {
      Log.warn(Log.DB, "[EntityDAOImpl.findByPrimaryKey] for entity["+_entityName+"]"
        +" with primaryKey["+primaryKey+"]"
        +" ,The SQL Query ["+_finderQuery+"]", ex);
      throw new SystemException(ex);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }

    if (result == null || result.isEmpty())
      throw new ApplicationException(
        "Record with primaryKey ["+primaryKey+"] does not exist in database");

    return (Long)result.toArray()[0];
  }

  public Collection findByFilter(IDataFilter filter) throws Exception
  {
    String sqlQuery=getFilterQuery(_filterQuery, filter);

    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection=getConnection();
      stmt=getPreparedStatement(dbConnection,sqlQuery);
      return executeFinder(stmt);
    }
    catch(Exception e)
    {
      Log.warn(Log.DB, "[EntityDAOImpl.findByFilter] for entity["+_entityName+"]"
        +",The SQL Query ["+sqlQuery+"]", e);
      throw new SystemException(e);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
  }

  /**
   * Retrieve a collection of entities by a filtering condition.
   *
   * @param filter The filtering condition.
   * @return A Collection of IEntity objects retrieved based on the filtering
   * condition. If the filter specified distinct selection, the returned entities
   * may contain a distinct set with selected fields.
   *
   * @exception SystemException Error in executing the query.
   */
  public Collection getEntityByFilter(IDataFilter filter) throws Exception
  {
    //NSL20051021 Support distinct selection  
    //String sqlQuery=getFilterQuery(_selectQuery, filter);
    String sqlQuery = filter!=null && filter.getDistinct() ?
			  getDistinctFilterQuery(filter)
			: getFilterQuery(_selectQuery, filter);
		              
    Log.debug(Log.DB, "[EntityDAOImpl.getEntityByFilter] SqlQuery = "+sqlQuery);

    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection=getConnection();
      stmt=getPreparedStatement(dbConnection,sqlQuery);
      return executeQuery(stmt);
    }
    catch(Exception e)
    {
      Log.warn(Log.DB, "[EntityDAOImpl.getEntityByFilter] for entity["+_entityName+"]"
        +",The SQL Query ["+sqlQuery+"]", e);
      throw new SystemException(e);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
  }

  /**
   * Get the values for a particular field
   * @param fieldId The field for whose values to return
   * @param filter The filter condition for the selection. If distinct is specified,
   * the returned results would contain only unique values.
   * @return Collection of values for the specified field
   * @throws Exception
   */
  public Collection getFieldValuesByFilter(Number fieldId,IDataFilter filter) throws Exception
  {
    String sqlFieldName = null;
    if(fieldId.equals(_pkFieldId))
      sqlFieldName=_pkSqlName;
    else
    {
      int fieldIndex = -1;
      while(++fieldIndex<_fieldIds.length && !fieldId.equals(_fieldIds[fieldIndex]))
      {
      }
      if(fieldIndex<_fieldIds.length)
        sqlFieldName=_sqlFieldNames[fieldIndex];
    }
    if(sqlFieldName==null)
      throw new Exception("Cannot find sql field name for fieldId ="+fieldId+" in entity ="+_entityName);
 
    //NSL20051021 Support distinct selection
    //String selectQuery = _sqlFactory.getSelectSyntax(_sqlTableName,new String[]{sqlFieldName,});
    String selectQuery = filter != null && filter.getDistinct() ? _sqlFactory.getSelectDistinctSyntax(_sqlTableName, new String[]{sqlFieldName,})
    					: _sqlFactory.getSelectSyntax(_sqlTableName,new String[]{sqlFieldName,});
    String sqlQuery=getFilterQuery(selectQuery, filter);
    Log.debug(Log.DB, "[EntityDAOImpl.getEntityByFilter] SqlQuery = "+sqlQuery);

    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection=getConnection();
      stmt=getPreparedStatement(dbConnection,sqlQuery);
      return executeFinder(stmt);
    }
    catch(Exception e)
    {
      Log.warn(Log.DB, "[EntityDAOImpl.getFieldValuesByFilter] for entity["+_entityName+"]"
        +",The SQL Query ["+sqlQuery+"]", e);
      throw new SystemException(e);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
  }
  
  public Collection getMaxValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
  {
    String sqlFieldName = null;
    if(fieldId.equals(_pkFieldId))
      sqlFieldName=_pkSqlName;
    else
    {
      int fieldIndex = -1;
      while(++fieldIndex<_fieldIds.length && !fieldId.equals(_fieldIds[fieldIndex]))
      {
      }
      if(fieldIndex<_fieldIds.length)
        sqlFieldName=_sqlFieldNames[fieldIndex];
    }
    if(sqlFieldName==null)
      throw new Exception("Cannot find sql field name for fieldId ="+fieldId+" in entity ="+_entityName);
    
    String selectMaxQuery = _sqlFactory.getSelectMaxSyntax(_sqlTableName, sqlFieldName);
    String sqlQuery = getFilterQuery(selectMaxQuery, filter);
    
    Log.debug(Log.DB, "[EntityDAOImpl.getMaxValuesByFilter] SqlQuery = "+sqlQuery);

    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection=getConnection();
      stmt=getPreparedStatement(dbConnection,sqlQuery);
      return executeFinder(stmt);
    }
    catch(Exception e)
    {
      Log.warn(Log.DB, "[EntityDAOImpl.getMaxValuesByFilter] for entity["+_entityName+"]"
        +",The SQL Query ["+sqlQuery+"]", e);
      throw new SystemException(e);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
    
  }
  
  public Collection getMinValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
  {
    String sqlFieldName = null;
    if(fieldId.equals(_pkFieldId))
      sqlFieldName=_pkSqlName;
    else
    {
      int fieldIndex = -1;
      while(++fieldIndex<_fieldIds.length && !fieldId.equals(_fieldIds[fieldIndex]))
      {
      }
      if(fieldIndex<_fieldIds.length)
        sqlFieldName=_sqlFieldNames[fieldIndex];
    }
    if(sqlFieldName==null)
      throw new Exception("Cannot find sql field name for fieldId ="+fieldId+" in entity ="+_entityName);
    
    String selectMinQuery = _sqlFactory.getSelectMinSyntax(_sqlTableName, sqlFieldName);
    String sqlQuery = getFilterQuery(selectMinQuery, filter);
    
    Log.debug(Log.DB, "[EntityDAOImpl.getMinValuesByFilter] SqlQuery = "+sqlQuery);

    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection=getConnection();
      stmt=getPreparedStatement(dbConnection,sqlQuery);
      return executeFinder(stmt);
    }
    catch(Exception e)
    {
      Log.warn(Log.DB, "[EntityDAOImpl.getMinValuesByFilter] for entity["+_entityName+"]"
        +",The SQL Query ["+sqlQuery+"]", e);
      throw new SystemException(e);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
    
  }
  
  public int getEntityCount(IDataFilter filter) throws Exception
  {
    String sqlQuery=getFilterQuery(_countQuery, filter);

    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection=getConnection();
      stmt=getPreparedStatement(dbConnection,sqlQuery);
      return executeCount(stmt);
    }
    catch(Exception e)
    {
      Log.warn(Log.DB, "[EntityDAOImpl.getEntityCount] for entity["+_entityName+"]"
        +",The SQL Query ["+sqlQuery+"]", e);
      throw new SystemException(e);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
  }
  
  private String getDistinctFilterQuery(IDataFilter filter) throws ApplicationException
  {
    if (filter == null)
	{
      throw new ApplicationException("Specified filter must not be null");
	}
    try
    {
      filter = ObjectConverter.convertToSQLFilter(_entityName,filter);
      
      String condition = filter.getFilterExpr();
      String selectQuery = _sqlFactory.getSelectDistinctSyntax(_sqlTableName, (String[])filter.getSelectFields());
      String query = _sqlFactory.applyConditionSyntax(selectQuery, condition);
      query = _sqlFactory.applyOrderSyntax(query, filter.getOrderFields(), filter.getSortOrders());
      return query;
    }
    catch (Exception ex)
    {
      throw new ApplicationException("Problem converting filter to SqlFilter", ex);
    }
  }
/*
  protected void setRsToEntity(ResultSet rs,IEntity entity) throws Exception
  {
    EntityMetaInfo entityMetaInfo=entity.getMetaInfo();
    //120402NSL: Only interested in Sql fields.
    //FieldMetaInfo fieldMetaInfo[]=entityMetaInfo.getFieldMetaInfo();
    FieldMetaInfo fieldMetaInfo[] = entityMetaInfo.getSqlFieldMetaInfo();
    for(int loop=0;loop<fieldMetaInfo.length;loop++)
    {
      Object obj=rs.getObject(fieldMetaInfo[loop].getSqlName());
      if(obj!=null)
      {
        if(EntityMetaInfoLoader.getEntityMetaInfo(fieldMetaInfo[loop].getValueClass())!=null)
        {
          IEntity valueEntity=(IEntity)Class.forName(fieldMetaInfo[loop].getValueClass()).newInstance();

          IEntityDAO dao =
            EntityDAOFactory.getInstance().getDAOFor(valueEntity.getEntityName());
          valueEntity = (obj == null) ? null : dao.load((Long)obj);

          //valueEntity.setFieldValue(valueEntity.getKeyId(),obj);
          entity.setFieldValue(fieldMetaInfo[loop].getFieldId(),valueEntity);
        }
        else
        {
          entity.setFieldValue(fieldMetaInfo[loop].getFieldId(),rs.getObject(fieldMetaInfo[loop].getSqlName()));
        }
      }
    }
  }
*/
  protected void setEntityToPs(IEntity entity,PreparedStatement ps) throws Exception
  {
    EntityMetaInfo entityMetaInfo = getMetaInfo();
    setFieldIds(entityMetaInfo);
    for(int loop=0;loop<_fieldIds.length;loop++)
    {
      FieldMetaInfo fieldMetaInfo = entityMetaInfo.findFieldMetaInfo(_fieldIds[loop]);
      //161002NSL: ensure must be persistent then load the embedded entity
      /*221202NSL: Buggy check here!!!
      Properties constrProp=fieldMetaInfo.getConstraints();
      EntityMetaInfo fieldEMI=null;
      if(constrProp!=null && constrProp.getProperty("embedded")!=null)
      */
      EntityMetaInfo fieldEMI=null;
      //221202NSL: only retrieve EntityMetaInfo if the field is an entity.
      if (fieldMetaInfo.isEntity())
        fieldEMI = MetaInfoFactory.getInstance().getEntityMetaInfo(fieldMetaInfo.getValueClass());
      if(fieldEMI!=null && fieldEMI.getSqlName() != null &&
         fieldEMI.getSqlName().trim().length()>0)
      {
        IEntity valueEntity=(IEntity)entity.getFieldValue(_fieldIds[loop]);
        ps.setObject(loop+1,(valueEntity==null)?null:valueEntity.getKey());
      }
      else
      {
        ps.setObject(loop+1,
          convert(fieldMetaInfo,
          entity.getFieldValue(_fieldIds[loop])));
      }
    }
  }

  public void setInsertQuery(EntityMetaInfo entityMetaInfo)
  {
    //030502NSL: Switch to use SqlFilterFactory to formulate the query statement
    //  String sqlQuery="INSERT INTO "+sqlTableName+" ";
    //  String fieldStr="",valueStr="";
    //  for(int loop=0;loop<fieldIds.length;loop++){
    //      fieldStr+=entityMetaInfo.findFieldMetaInfo(fieldIds[loop]).getSqlName()+(((loop+1)<fieldIds.length)?",":"");
    //      valueStr+="?"+(((loop+1)<fieldIds.length)?",":"");
    //  }
    //  sqlQuery+="("+fieldStr+") VALUES ";
    //  sqlQuery+="("+valueStr+")";
    //  insertQuery=sqlQuery;

    //need all fields in Insert query
    String[] fieldNames = new String[_sqlFieldNames.length+1];
    System.arraycopy(_sqlFieldNames, 0, fieldNames, 0, _sqlFieldNames.length);
    fieldNames[_sqlFieldNames.length] = _pkSqlName;

    _insertQuery = _sqlFactory.getInsertSyntax(
                     _sqlTableName,
                     fieldNames);
  }

  public void setUpdateQuery(EntityMetaInfo entityMetaInfo)
  {
    //030502NSL: Switch to use SqlFilterFactory to formulate the query statement
    //String sqlQuery="UPDATE "+sqlTableName+" SET ";
    //for(int loop=0;loop<fieldIds.length-1;loop++){
    //        sqlQuery+=entityMetaInfo.findFieldMetaInfo(fieldIds[loop]).getSqlName()+
    //                  "=?"+(((loop+2)<fieldIds.length)?",":" ");
    //}
    //sqlQuery+="WHERE "+entityMetaInfo.findFieldMetaInfo(pkFieldId).getSqlName()+"=?";
    //updateQuery=sqlQuery;

    _updateQuery = _sqlFactory.getUpdateSyntax(_sqlTableName, _sqlFieldNames);
    _updateQuery = _sqlFactory.applyConditionSyntax(
                     _updateQuery,
                     _pkSqlName+"=?");
  }

  public void setLoadQuery(EntityMetaInfo entityMetaInfo)
  {
    //030502NSL: Switch to use SqlFilterFactory to formulate the query statement
    //String sqlQuery="SELECT * FROM "+sqlTableName;
    //sqlQuery+=" WHERE "+entityMetaInfo.findFieldMetaInfo(pkFieldId).getSqlName()+"=?";
    //loadQuery=sqlQuery;

    if (_selectQuery == null)
      setSelectQuery(entityMetaInfo);

    _loadQuery = _sqlFactory.applyConditionSyntax(
                  _selectQuery,
                  _pkSqlName+"=?");
    
    //_loadQuery += " FOR UPDATE"; //NSL20070205 Lock the row
  }

  public void setDeleteQuery(EntityMetaInfo entityMetaInfo)
  {
    //030502NSL: Switch to use SqlFilterFactory to formulate the query statement
    //String sqlQuery="DELETE FROM "+sqlTableName;
    //sqlQuery+=" WHERE "+entityMetaInfo.findFieldMetaInfo(pkFieldId).getSqlName()+"=?";
    //deleteQuery=sqlQuery;

    _deleteQuery = _sqlFactory.getDeleteSyntax(_sqlTableName);
    _deleteQuery = _sqlFactory.applyConditionSyntax(
                     _deleteQuery,
                     _pkSqlName+"=?");
  }

  public void setFinderQuery(EntityMetaInfo entityMetaInfo)
  {
    if (_filterQuery==null)
      setFilterQuery(entityMetaInfo);

    //030502NSL: Switch to use SqlFilterFactory to formulate the query statement
    //finderQuery=filterQuery+" WHERE "+entityMetaInfo.findFieldMetaInfo(pkFieldId).getSqlName()+"=?";

    _finderQuery = _sqlFactory.applyConditionSyntax(
                     _filterQuery,
                     _pkSqlName+"=?");
  }

  public void setFilterQuery(EntityMetaInfo entityMetaInfo)
  {
    //030502NSL: Switch to use SqlFilterFactory to formulate the query statement
    //filterQuery="SELECT "+entityMetaInfo.findFieldMetaInfo(pkFieldId).getSqlName()+" FROM "+sqlTableName;

    _filterQuery = _sqlFactory.getSelectSyntax(
                     _sqlTableName,
                     new String[]
                     {
                       _pkSqlName,
                     });
  }

  public void setSelectQuery(EntityMetaInfo entityMetaInfo)
  {
    //030502NSL: Switch to use SqlFilterFactory to formulate the query statement
    //selectQuery = "SELECT * FROM "+sqlTableName;

    _selectQuery = _sqlFactory.getSelectSyntax(
                     _sqlTableName,
                     null);
  }

  public void setCountQuery(EntityMetaInfo entityMetaInfo)
  {
    //030502NSL: Switch to use SqlFilterFactory to formulate the query statement
    //countQuery = "SELECT COUNT(1) FROM "+sqlTableName;

    _countQuery = _sqlFactory.getSelectSyntax(
                    _sqlTableName,
                    new String[]
                    {
                      "COUNT(1)"
                    });
  }
  
  /**
   * TWX 03112006 Initiate the maxQuery
   * @param entityMetaInfo
   */
  public void setMaxQuery(EntityMetaInfo entityMetaInfo)
  {
  	_maxQuery = _sqlFactory.getSelectSyntax(
    			       _sqlTableName,
    			       //new String[]{"MAX(UID)"}
                 new String[]{"MAX("+_pkSqlName+")"} //NSL20070126 Should not hard-code the column name
  			         );  
  }
  
  public String getFilterQuery(String selectQuery, IDataFilter filter)
    throws ApplicationException
  {
    //030502NSL: Switch to use SqlFilterFactory to formulate the query statement
    //StringBuffer buff = new StringBuffer(selectQuery);
    //if (filter != null)
    //{
    //  buff.append(" WHERE ").append(convertFilterToQuery(filter));
    //}
    //return buff.toString();

    try
    {
      if (filter != null)
      {
        filter = ObjectConverter.convertToSQLFilter(_entityName,filter);
        
        String condition = filter.getFilterExpr();
        String query = _sqlFactory.applyConditionSyntax(selectQuery, condition);
        query = _sqlFactory.applyOrderSyntax(query, filter.getOrderFields(), filter.getSortOrders());
        return query;
      }
      else
        return selectQuery;
    }
    catch (Exception ex)
    {
      throw new ApplicationException("Problem converting filter to SqlFilter", ex);
    }
  }

  protected String convertFilterToQuery(IDataFilter filter)
    throws ApplicationException
  {
    try
    {
      filter=ObjectConverter.convertToSQLFilter(_entityName,filter);
      return (filter==null)?null:filter.getFilterExpr();
    }
    catch(Exception ex)
    {
      throw new ApplicationException("Problem converting filter to SqlFilter", ex);
    }
  }

  public void setFieldIds(EntityMetaInfo entityMetaInfo)
  {
    if(_fieldIds==null)
    {
      //120402NSL: Only interested in Sql fields
      //FieldMetaInfo fieldMetaInfo[]=entityMetaInfo.getFieldMetaInfo();
      FieldMetaInfo fieldMetaInfo[] = entityMetaInfo.getSqlFieldMetaInfo();
      _fieldIds=new Number[fieldMetaInfo.length];
      //030502NSL: Keep the Sql names
      _sqlFieldNames = new String[fieldMetaInfo.length-1];

      int pkIndex=-1;
      for(int loop=0;loop<fieldMetaInfo.length;loop++)
      {
        pkIndex=(_pkFieldId.equals(fieldMetaInfo[loop].getFieldId()))?loop:pkIndex;
        //030502NSL: put the pkFieldId at the back
        //but instead of just switching positions,
        //the rest of the fields are shifted left.
        //fieldIds[loop]=fieldMetaInfo[loop].getFieldId();
         if (pkIndex == loop)
           //Keep SqlName of primary key separately
           _pkSqlName = fieldMetaInfo[loop].getSqlName();
         else
         {
           _sqlFieldNames[pkIndex>-1?loop-1:loop] = fieldMetaInfo[loop].getSqlName();
           _fieldIds[pkIndex>-1?loop-1:loop] = fieldMetaInfo[loop].getFieldId();
         }
      }
      //fieldIds[pkIndex]=fieldIds[fieldIds.length-1];
      _fieldIds[_fieldIds.length-1] = _pkFieldId;
    }
  }

  public int executeUpdate(PreparedStatement stmt) throws Exception
  {
    return stmt.executeUpdate();
  }

  public Collection  executeQuery(PreparedStatement stmt) throws Exception
  {
    Vector entityCol=new Vector();
    ResultSet rs=null;
    try
    {
      rs=stmt.executeQuery();
      while(rs!=null && rs.next())
      {
        entityCol.add(getEntityFromRs(rs));
      }
    }
    catch(Exception e)
    {
      Log.warn(Log.DB, "[EntityDAOImpl.executeQuery] Database error ", e);
      throw e;
    }
    finally
    {
      closeResultSet(rs);
    }
    return entityCol;
  }

  public Collection executeFinder(PreparedStatement stmt) throws Exception
  {
    Vector entityCol=new Vector();
    ResultSet rs=null;
    try
    {
      rs=stmt.executeQuery();
      while(rs.next())
      {
        //NSL20070228 Check only if the value is a number
        Object o = rs.getObject(1);
        if (o instanceof Number)
        {
        	// CSF20070130 Cast the primary key to java.lang.Long
        	//if (!rs.getObject(1).getClass().equals(Long.class))
        		entityCol.add(AbstractEntity.convert(o, Long.class.toString()));
        }
      	else
      	  entityCol.add(rs.getObject(1));
      }
    }
    catch(Exception e)
    {
      throw e;
    }
    finally
    {
      closeResultSet(rs);
    }
    return entityCol;
  }

  public int executeCount(PreparedStatement stmt) throws Exception
  {
    int count = 0;
    ResultSet rs=null;
    try
    {
      rs=stmt.executeQuery();
      if (rs.next())
        count = rs.getInt(1);
    }
    catch(Exception e)
    {
      throw e;
    }
    finally
    {
      closeResultSet(rs);
    }
    return count;
  }

  public IEntity getEntityFromRs(ResultSet rs) throws Exception
  {
    EntityMetaInfo entityMetaInfo = getMetaInfo();
    //290402MAHESH: Changed to use objectName insted of entityName
    //IEntity entity=(IEntity)Class.forName(entityName).newInstance();
    IEntity entity = (IEntity)Class.forName(entityMetaInfo.getObjectName()).newInstance();
    //120402NSL: Only interested in Sql fields
    //FieldMetaInfo fieldMetaInfo[]=entityMetaInfo.getFieldMetaInfo();
    FieldMetaInfo fieldMetaInfo[] = entityMetaInfo.getSqlFieldMetaInfo();
    for(int loop=0;loop<fieldMetaInfo.length;loop++)
    {
      //160902NSL
      //Object obj=rs.getObject(fieldMetaInfo[loop].getSqlName());
      //300107CSF
      //Object obj = getColValueFromRs(rs, fieldMetaInfo[loop].getSqlName());
      Object obj = getColValueFromRs(rs, fieldMetaInfo[loop]);
      if(obj!=null)
      {
        //161002NSL: ensure must be persistent then load the embedded entity
        /*221202NSL: Buggy check here!!!
        Properties constrProp=fieldMetaInfo[loop].getConstraints();
        EntityMetaInfo fieldEMI=null;
        if(constrProp!=null && constrProp.getProperty("embedded")!=null)
        */
        EntityMetaInfo fieldEMI=null;
        //221202NSL: only retrieve EntityMetaInfo if the field is an entity.
        if (fieldMetaInfo[loop].isEntity())
          fieldEMI = MetaInfoFactory.getInstance().getEntityMetaInfo(fieldMetaInfo[loop].getValueClass());

        //EntityMetaInfo fieldEMI = MetaInfoFactory.getInstance().getEntityMetaInfo(fieldMetaInfo[loop].getEntityName());
        if(fieldEMI!=null && fieldEMI.getSqlName() != null &&
           fieldEMI.getSqlName().trim().length()>0)
        {
          IEntity valueEntity=(IEntity)Class.forName(fieldMetaInfo[loop].getValueClass()).newInstance();
          IEntityDAO dao =
            EntityDAOFactory.getInstance().getDAOFor(valueEntity.getEntityName());
          valueEntity = dao.load((Long)obj);

          //valueEntity.setFieldValue(valueEntity.getKeyId(),obj);
          entity.setFieldValue(fieldMetaInfo[loop].getFieldId(),valueEntity);
        }
        else
        {
          entity.setFieldValue(fieldMetaInfo[loop].getFieldId(), obj);
        }
      }
    }
    return entity;
  }

  /**
   * To get the value of a column from a result set.
   * This retrieves column value using specific getters from the result set
   * instead of the getObject() for some column types.
   *
   * @param rs The result set.
   * @param field Fieldmetainfo object of the column.
   * @return The retrieved column value.
   */
  private Object getColValueFromRs(ResultSet rs, FieldMetaInfo field) throws Exception
  {
    String sqlName = stripQuote(field.getSqlName());//NSL20070126 strip any surrounding quotes
    Integer sqlType = _colDefs.get(sqlName); 
    if (sqlType == null)
      throw new SystemException(
                  "Column definition not found for "+sqlName, null);

    Object value = null;

    switch (sqlType.intValue())
    {
      case Types.BINARY        :
      case Types.VARBINARY     :
      case Types.LONGVARBINARY :
      case Types.BLOB          :
           value = rs.getBytes(sqlName);
           break;
      case Types.VARCHAR:
           value = rs.getObject(sqlName);
           if(value!=null && value instanceof byte[])
           {
             value=new String((byte[])value);
           }
           break;
      case Types.DECIMAL: // CSF20070130 Cast the Decimal to different number datatypes accordingly
      	   if (field.isEntity())
      	  	 value = AbstractEntity.convert(rs.getObject(sqlName),Long.class.getName());
      	   else
      	  	 value = AbstractEntity.convert(rs.getObject(sqlName), field.getValueClass());
      	   break;
      case Types.DATE:
        value = rs.getTimestamp(sqlName);
        break;
      default                  :
           value = rs.getObject(sqlName);

    }

    return value;
  }

  public PreparedStatement getPreparedStatement(Connection dbConnection,String sqlQuery)
    throws Exception
  {
    PreparedStatement stmt = dbConnection.prepareStatement(sqlQuery);
    return stmt;
  }

  /**
   * Initialize the column definitions for each field in the table.
   */
  protected void initColumnDefinitions() throws Exception
  {
    Connection conn = null;
    try
    {
      conn = getConnection();
      ResultSet rs = conn.getMetaData().getColumns(null, null,
                       stripQuote(_sqlTableName), "%"); //CSF20070130 strip any surrounding quotes

      while (rs.next())
      {
        String col = rs.getString("COLUMN_NAME");
        int type   = rs.getInt("DATA_TYPE");
        //System.out.println("putting Column def:"+col + ", type="+type);
        _colDefs.put(col, new Integer(type));
      }
      //NSL20070125 Set the engine type for SQLFilterFactory
      String dbProdName = conn.getMetaData().getDatabaseProductName();
      _sqlFactory.setEngineType(dbProdName);
    }
    finally
    {
      closeConnection(conn);
    }
  }


  /**
   * Convert a value to the specified type in the metainfo, according to the
   * sqlType of the field in the database.
   *
   * @param value The value to convert.
   * @param metaInfo the metainfo containing the column name (SqlName) and the
   * target type expected (ValueClass).
   */
  protected Object convert(FieldMetaInfo metaInfo, Object value)
    throws Exception
  {
    if (value == null)
      return value;

    Integer sqlType = _colDefs.get(stripQuote(metaInfo.getSqlName())); //NSL20070126 strip any surrounding quotes
    if (sqlType == null)
    {
      
      throw new SystemException(
                  "Column definition not found for "+metaInfo.getSqlName(), null);
    }
    String toClass = null;

    switch (sqlType.intValue())
    {
      case Types.BIGINT        : toClass = Long.class.getName();
                                 break;
      case Types.INTEGER       : toClass = Integer.class.getName();
                                 break;
      case Types.REAL          : toClass = Float.class.getName();
                                 break;
      case Types.SMALLINT      : toClass = Short.class.getName();
                                 break;
      case Types.TINYINT       : toClass = Byte.class.getName();
                                 break;
      case Types.DECIMAL       :
      case Types.NUMERIC       : toClass = java.math.BigDecimal.class.getName();
                                 break;
      case Types.DOUBLE        :
      case Types.FLOAT         : toClass = Double.class.getName();
                                 break;
      case Types.CHAR          :
      case Types.VARCHAR       :
      case Types.LONGVARCHAR   : toClass = String.class.getName();
                                 break;
      case Types.DATE          : toClass = java.sql.Date.class.getName();
                                 break;
      case Types.TIME          : toClass = Time.class.getName();
                                 break;
      case Types.TIMESTAMP     : toClass = Timestamp.class.getName();
                                 break;
      case Types.BIT           : toClass = Boolean.class.getName();
                                 break;
      case Types.BINARY        :
      case Types.VARBINARY     :
      case Types.LONGVARBINARY :
      case Types.BLOB          : toClass = "byte[]";
                                 break;
    }

    if (toClass != null)
      return AbstractEntity.convert(value, toClass);
    else
      return value;
  }

  protected String stripQuote(String str)
  {
    return str.replaceAll("\"", "");
  }
  
  protected Connection getConnection() throws Exception
  {
    return getDataSource().getConnection();
  }

  public void releaseResources(Statement stmt,Connection conn)
  {
    closeStatement(stmt);
    closeConnection(conn);
  }

  public void closeResultSet(ResultSet rs)
  {
    try
    {
      if(rs!=null)
        rs.close();
    }
    catch(Exception e)
    {
    }
  }

  public void closeStatement(Statement stmt)
  {
    try
    {
      if(stmt!=null)
          stmt.close();
    }
    catch(Exception e)
    {
    }
  }

  protected void closeConnection(Connection conn)
  {
    try
    {
      if(conn!=null)
        conn.close();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Obtain the data source for this DAO.
   *
   * @return A DataSource for this DAO.
   * @exception SystemException Error in obtaining the DataSource.
   *
   * @since 2.0 I5
   */
  protected DataSource getDataSource() throws SystemException
  {
    return DataSourceSelector.getInstance().getDataSource(_entityName);
  }

  public String toString()
  {
    String lineSep = System.getProperty("line.separator") + "\t";
    String lineSep2t = lineSep + "\t";

    StringBuffer buff = new StringBuffer();

    buff.append(lineSep).append("Database Queries for ").append(_entityName);
    buff.append(lineSep2t).append("sqlTableName\t :").append(_sqlTableName);
    buff.append(lineSep2t).append("insertQuery\t :").append(_insertQuery);
    buff.append(lineSep2t).append("updateQuery\t :").append(_updateQuery);
    buff.append(lineSep2t).append("loadQuery\t :").append(_loadQuery);
    buff.append(lineSep2t).append("deleteQuery\t :").append(_deleteQuery);
    buff.append(lineSep2t).append("deleteQuery\t :").append(_deleteQuery);
    buff.append(lineSep2t).append("filterQuery\t :").append(_filterQuery);
    buff.append(lineSep2t).append("finderQuery\t :").append(_finderQuery);
    buff.append(lineSep2t).append("selectQuery\t :").append(_selectQuery);
    buff.append(lineSep2t).append("countQuery\t :").append(_countQuery);
    return buff.toString();
  }

  public Collection getAllKeys() throws Exception
  {
    String sqlQuery = _filterQuery;

    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection=getConnection();
      stmt=getPreparedStatement(dbConnection, sqlQuery);
      return executeFinder(stmt);
    }
    catch(Exception e)
    {
      Log.warn(Log.DB, "[EntityDAOImpl.getAllKeys] for entity["+_entityName+"]"
        +",The SQL Query ["+sqlQuery+"]", e);
      throw new SystemException(e);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
  }
  
  /**
   * This is a helper method to create new PK (the UID) for the entity.
   * @param ensureUniquePK False to indicate we get the PK directly from the Reference Num table. True we
   *                       will get the PK from the ref num table and perform additional checking on the existing
   *                       of PK in that table. If the PK is being in used, we will generate a new PK by querying
   *                       MAX(UID) + 1.
   * @return a PK.
   * @throws Exception
   */
  public Long createNewKey(boolean ensureUniquePK) throws Exception
  {
	/*  
    Collection existingKeyColl = null;
    if(checkIsKeyUsed)
      existingKeyColl = getAllKeys();
    Long primaryKey = KeyGen.getNextId(_sqlTableName, existingKeyColl);
    return primaryKey; */ 
    
	Long primaryKey = null;  
	
	if(ensureUniquePK)
	{
		synchronized(_lock)
		{
			primaryKey = KeyGen.getNextId(_sqlTableName, true);
			primaryKey = ensureUniquePK(primaryKey, _sqlTableName);
			Log.debug(Log.DB,"createNewKey done. Unique PK for "+_sqlTableName+"is "+primaryKey);
		}
	}
	else
	{
		//Directly get from the reference num table without doing checking on the usage of the PK.
		primaryKey = KeyGen.getNextId(_sqlTableName, true);
	}
    
	return primaryKey;
  }
  
  private EntityMetaInfo getMetaInfo()
  {
    return MetaInfoFactory.getInstance().getMetaInfoFor(_entityName);
  }
  
  /**
   * TWX Ensure the PK we got it from the reference number is unique in the table specified by
   * tableName. If not, a new PK will be generated which will be the MAX UID so far in that table
   * plus 1.
   * @param primaryKey
   * @param tableName
   * @return
   * @throws Exception
   */
  private Long ensureUniquePK(Long primaryKey, String tableName)
  	throws Exception
  {
	  try
	  {
		  if(! isPKExist(primaryKey))
		  {
			  return primaryKey;
		  }
		  else
		  {
			  Log.debug(Log.DB, "PK "+primaryKey+" for context "+tableName+" is exist. Require generated new one !");
			  //Get the MAX UID
			  Long maxPK = getMaxPK();
			  if(maxPK == null)
			  {
				  throw new ApplicationException("[EntityDAOImpl.ensureUniquePK] maxPK is null , no record in the table "+_sqlTableName+" !!!");
			  }
			  
			  Log.debug(Log.DB, "The max PK for context "+tableName+" so far is "+maxPK);
			  //Get the next PK based on (maxPK1) 
			  Long nextPK = KeyGen.getNextId(_sqlTableName, false, maxPK);
			  
			  return nextPK;
		  }
	  }
	  catch(Exception ex)
	  {
		  Log.warn(Log.DB, "[EntityDAOImpl.ensureUniquePK] Error in getting unique PK for table "+_sqlTableName);
		  throw ex; 
	  }
  }
  
  /**
   * Return the current MAX PK for a particular table
   * @return the max PK
   * @throws Exception
   */
  private Long getMaxPK() throws Exception
  {
	  Connection dbConnection = null;
	  PreparedStatement stmt = null;
	  Collection result = null;
	  
	  try
	  {
		  dbConnection = getConnection();
		  stmt = getPreparedStatement(dbConnection, _maxQuery);
		  result = executeFinder(stmt);
	  }
	  catch(Exception ex)
	  {
		  Log.warn(Log.DB, "[EntityDAOImpl.getMaxPK] for entity ["+_entityName+"]"+
				  " The SQL query ["+_maxQuery+"]", ex);
		  throw new SystemException(ex);
	  }
	  finally
	  {
		  releaseResources(stmt, dbConnection);
	  }
	  
	  if(result == null || result.size() == 0)
	  {
		  return null; //no record in the table !
	  }
	  else
	  {
		  return (Long)result.iterator().next();
	  }
  }
  
  /**
   * Check whether the primarkKey is in used.
   * @param primaryKey
   * @return true if the primaryKey is in used, false otherwise.
   * @throws SystemException
   */
  private boolean isPKExist(Long primaryKey) throws SystemException
  {
	  Connection dbConnection=null;
	    PreparedStatement stmt = null;
	    Collection result = null;
	    try
	    {
	      dbConnection=getConnection();
	      stmt=getPreparedStatement(dbConnection, _finderQuery);
	      stmt.setObject(1,primaryKey);
	      result = executeFinder(stmt);
	    }
	    catch(Exception ex)
	    {
	      Log.warn(Log.DB, "[EntityDAOImpl.findByPrimaryKey] for entity["+_entityName+"]"
	        +" with primaryKey["+primaryKey+"]"
	        +" ,The SQL Query ["+_finderQuery+"]", ex);
	      throw new SystemException(ex);
	    }
	    finally
	    {
	      releaseResources(stmt,dbConnection);
	    }

	    if (result == null || result.isEmpty())
	      return false;

	    return true;
  }
}
