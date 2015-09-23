/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FieldMetaInfoDAO.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 02 2005    Neo Sok Lay         Created
 * Feb 07 2007		Alain Ah Ming				Log warning message if throwing up exception
 */
package com.gridnode.pdip.framework.db.meta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;

import com.gridnode.pdip.framework.db.dao.GenericDAO;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.Log;

/**
 * DAO for use by MetaInfoBean to access the FieldMetaInfo dataset
 * @author i00107
 * @since
 * @version GT 4.0 VAN
 */
public class FieldMetaInfoDAO extends GenericDAO
{
	private String _objectNameCol,
								_entityNameCol,
								_fieldNameCol,
								_sqlNameCol,
								_oqlNameCol,
								_presentationCol,
								_constraintsCol,
								_valueClassCol,
								_labelCol,
								_lengthCol,
								_sequenceCol,
								_displayableCol,
								_editableCol,
								_mandatoryCol,
								_proxyCol;
	
	private String _findByLabelQuery,
							_findByEntityNameQuery;
	
	private static FieldMetaInfoDAO _self = null;
	
	public static synchronized FieldMetaInfoDAO getInstance() throws Exception
	{
		if (_self == null)
		{
			_self = new FieldMetaInfoDAO();
		}
		return _self;
	}
	
	private FieldMetaInfoDAO() throws Exception
	{
		super("com/gridnode/pdip/framework/db/meta/FieldMetaInfo.properties", "FieldMetaInfo");
	}
	
	protected void initQueries() throws SystemException
	{
		_findByLabelQuery = getNonNullProperty("query.findByLabel");
		_findByEntityNameQuery = getNonNullProperty("query.findByEntityName");
		
	}
	
	protected void initCols() throws SystemException
	{
		_objectNameCol = getNonNullProperty("_objectName");
		_entityNameCol = getNonNullProperty("_entityName");
		_sqlNameCol = getNonNullProperty("_sqlName");
		_fieldNameCol = getNonNullProperty("_fieldName");
		_valueClassCol = getNonNullProperty("_valueClass");
		_presentationCol = getNonNullProperty("_presentation");
		_constraintsCol = getNonNullProperty("_constraints");
		_labelCol = getNonNullProperty("_label");
		_lengthCol = getNonNullProperty("_length");
		_displayableCol = getNullableProperty("_displayable");
		_editableCol = getNullableProperty("_editable");
		_mandatoryCol = getNullableProperty("_mandatory");
		_proxyCol = getNullableProperty("_proxy");
		_sequenceCol = getNullableProperty("_sequence");
		_oqlNameCol = getNullableProperty("_oqlName");
	}

	protected Object getDataFromRs(ResultSet rs) throws Exception
  {
  	FieldMetaInfo fieldMetaInfo = new FieldMetaInfo();
  	fieldMetaInfo.setEntityName(rs.getString(_entityNameCol));
  	fieldMetaInfo.setObjectName(rs.getString(_objectNameCol));
  	fieldMetaInfo.setSqlName(rs.getString(_sqlNameCol));
  	fieldMetaInfo.setConstraints(rs.getString(_constraintsCol));
  	fieldMetaInfo.setFieldName(rs.getString(_fieldNameCol));
  	fieldMetaInfo.setLabel(rs.getString(_labelCol));
  	fieldMetaInfo.setLength(rs.getInt(_lengthCol));
  	fieldMetaInfo.setPresentation(rs.getString(_presentationCol));
  	fieldMetaInfo.setValueClass(rs.getString(_valueClassCol));
  	if (_displayableCol != null) 
  	{
  		fieldMetaInfo.setDisplayable(getBoolean(_displayableCol, rs));
  	}
  	if (_editableCol != null)
  	{
  		fieldMetaInfo.setEditable(getBoolean(_editableCol, rs));
  	}
  	if (_mandatoryCol != null)
  	{
  		fieldMetaInfo.setMandatory(getBoolean(_mandatoryCol, rs));
  	}
  	if (_oqlNameCol != null)
  	{
  		fieldMetaInfo.setOqlName(rs.getString(_oqlNameCol));
  	}
  	if (_proxyCol != null)
  	{
  		fieldMetaInfo.setProxy(getBoolean(_proxyCol, rs));
  	}
  	if (_sequenceCol != null)
  	{
  		fieldMetaInfo.setSequence(rs.getInt(_sequenceCol));
  	}
  	return fieldMetaInfo;
  }

	/**
	 * Find FieldMetaInfo by its label
	 * @param label The label to search
	 * @return Collection of FieldMetaInfo(s) with the specified label
	 * @throws Exception Unable to perform retrieval
	 */
	public Collection findByLabel(String label) throws Exception
	{
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection = getConnection();
      stmt = getPreparedStatement(dbConnection, _findByLabelQuery);
      stmt.setString(1, label);
      return executeQuery(stmt);
    }
    catch(Exception ex)
    {
      Log.warn(Log.DB, "[FieldMetaInfoDAO.findByLabel] with label["+label+"]", ex);
      throw new SystemException(ex);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
	}
	
	/**
	 * Find FieldMetaInfo by its entityName
	 * @param entityName The entity name
	 * @return Collection of FieldMetaInfo with the specified entityName
	 * @throws Exception Unable to perform retrieval
	 */
	public Collection findByEntityName(String entityName) throws Exception
	{
    Connection dbConnection=null;
    PreparedStatement stmt = null;
    try
    {
      dbConnection = getConnection();
      stmt = getPreparedStatement(dbConnection, _findByEntityNameQuery);
      stmt.setString(1, entityName);
      return executeQuery(stmt);
    }
    catch(Exception ex)
    {
      Log.warn(Log.DB, "[FieldMetaInfoDAO.findByEntityName] with entityName["+entityName+"]", ex);
      throw new SystemException(ex);
    }
    finally
    {
      releaseResources(stmt,dbConnection);
    }
	}
	
}
