/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityActionResponseData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 9 2003     Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.rpf.event;

import com.gridnode.pdip.framework.rpf.model.EntityDescriptor;
import com.gridnode.pdip.framework.rpf.model.IEntityDescriptorListSet;

/**
 * This class defines a holder for the result of performing an Action on an entity.
 * It extends the EntityDescriptor class which describes the entity that
 * was acted on. In addition, the following are defined:<p>
 * <PRE>
 * IsSuccess     - Whether the action is successful.
 * FailCode      - If action is not successful, gives the exact failure code
 * FailTrace     - If action is not successful due to exceptions, 
 *                 gives a stack trace of the failure point
 * DependentSet  - If action is not successful due to dependencies in other entities,
 *                 gives the set of dependent entity list (one list for each entity type).
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityActionResponseData 
  extends EntityDescriptor
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3405186231492177835L;
	private boolean _isSuccess;
  private int _failCode;
  private int _errorType;
  private String _failTrace;
  private IEntityDescriptorListSet _dependents;

  /**
   * Constructor for EntityActionResponseData.
   * 
   * @param key          Key value of the entity being acted on
   * @param description  Description of the entity acted on
   * @param isSuccess    <b>true</b> if the action is successful, <b>false</b> otherwise.
   * @param failCode     If action is not successful, gives the exact failure code.
   * @param failTrace    If action is not successful due to exceptions, 
   *                      gives a stack trace of the failure point
   * @param dependentSet If action is not successful due to dependencies in other entities,
   *                      gives the set of dependent entity list (one list for each entity type).
   */
  private EntityActionResponseData(
    Object key,
    String description,
    boolean isSuccess,
    int errorType,
    int failCode,
    String failTrace,
    IEntityDescriptorListSet dependentSet)
  {
    super(key, description);
    _isSuccess = isSuccess;
    _errorType = errorType;
    _failCode = failCode;
    _failTrace = failTrace;
    _dependents = dependentSet; 
  }

  /**
   * Creates an instance of this class for successful action.
   * 
   * @param key         Key value of the entity successfully deleted.
   * @param description Description of the entity successfully deleted.
   * @return The EntityActionResponseData created.
   */
  public static EntityActionResponseData newSuccessResponseData(
    Object key,
    String description)
  {
    return new EntityActionResponseData(key, description, true, -1, -1, "", null);
  }

  /**
   * Creates an instance of this class for unsuccessful action due to
   * dependencies in other entities.
   * 
   * @param key           Key value of the entity that failed action.
   * @param description   Description of the entity that failed action.
   * @param failCode      Failure code.
   * @param dependentList Set of dependent entity list.
   * @return The EntityActionResponseData created.
   */
  public static EntityActionResponseData newFailResponseData(
    Object key,
    String description,
    int errorType,
    int failCode,
    IEntityDescriptorListSet dependentList)
  {
    return new EntityActionResponseData(key, description, false, errorType, failCode, "", dependentList);
  }

  /**
   * Creates an instance of this class for unsuccessful action due to other
   * reasons other than dependencies in other entities.
   * 
   * @param key           Key value of the entity that failed action.
   * @param description   Description of the entity that failed action.
   * @param failCode      Failure code.
   * @param failTrace     If failure is due to exceptions, 
   *                       gives a stack trace of the failure point.
   * @return The EntityActionResponseData created.
   */
  public static EntityActionResponseData newFailResponseData(
    Object key,
    String description,
    int errorType,
    int failCode,
    String failTrace)
  {
    return new EntityActionResponseData(key, description, false, errorType, failCode, failTrace, null);
  }

  /**
   * Whether the action was successful.
   * 
   * @return <b>true</b> if the action was successful, <b>false</b> otherwise.
   */
  public boolean isSuccess()
  {
    return _isSuccess;
  }

  /**
   * Get the failure code if action was not successful.
   * 
   * @return The failure code for the type of action failure. 
   * returns <code>-1</code> if there is no error.
   */  
  public int getFailCode()
  {
    return _failCode;
  }

  /**
   * Get the exception trace if action was not successful due to exceptions.
   * 
   * @return The stack trace string for the exception that occurred.
   */
  public String getFailTrace()
  {
    return _failTrace;
  }

  /**
   * Get the set of dependent entity list if action failure was due to dependencies in other
   * entities.
   * 
   * @return The dependent eneity list set, or <b>null</b> if action was successful or
   * failure was not due to dependencies in other entities.
   */    
  public IEntityDescriptorListSet getDependentSet()
  {
    return _dependents;
  }
  
  /**
   * Get the error type if action was not successful.
   * 
   * @return The type of error e.g SYSTEM or APPLICATION, or
   * -1 if action was successful.
   */  
  public int getErrorType()
  {
    return _errorType;
  }
}
