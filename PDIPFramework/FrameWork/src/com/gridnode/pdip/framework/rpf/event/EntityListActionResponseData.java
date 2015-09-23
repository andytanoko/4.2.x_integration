/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityListActionResponseData
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 9 2003     Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.rpf.event;

import com.gridnode.pdip.framework.rpf.model.EntityDescriptorList;
import com.gridnode.pdip.framework.rpf.model.IEntityDescriptor;
 
/**
 * This class defines a holder for the results of performing an action on
 * a list of entities of the same type.
 * This class extends from EntityDescriptorList which defines the basic
 * structure -- to contain a list of EntityActionResponseData objects.
 * 
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityListActionResponseData 
  extends EntityDescriptorList
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7353515363287737912L;
	private boolean _isAllSuccess;
  private int _overallErrorType = -1;
  
  /**
   * Constructor for EntityListActionResponseData.
   * 
   * @param entityType The Entity Type of the entities that are acted on.
   * @param keyId The FieldId for the Key field for the type of entities acted on.
   */
  public EntityListActionResponseData(
    String entityType,
    Number keyId)
  {
    super(entityType, keyId);
    _isAllSuccess = true;
  }

  /**
   * Whether the action was successful for all entities.
   * 
   * @return <b>true</b> if all EntityActionResponseData.isSuccessful() returns true,
   * <b>false</b> otherwise.
   */
  public boolean isAllSuccess()
  {
    return _isAllSuccess;
  }

  /**
   * Get the overall error type of all entities.
   * 
   * @return Overall error type, e.g. SYSTEM or APPLICATION, or -1
   * if no error occurred.
   */
  public int getOverallErrorType()
  {
    return _overallErrorType;
  }
  
  /**
   * Get all EntityActionResponseData objects contained in this object.
   * 
   * @return Array of EntityActionResponseData objects (one for each entity
   * that was acted on.
   */  
  public EntityActionResponseData[] getResponseDataList()
  {
    IEntityDescriptor[] interfaceArray = getEntityDescriptors();
    EntityActionResponseData[] implArray = new EntityActionResponseData[interfaceArray.length];
    
    System.arraycopy(interfaceArray, 0, implArray, 0, interfaceArray.length);
    return implArray;
  } 

  /**
   * Add an EntityActionResponseData to this object if no existing EntityActionResponseData
   * with the same Key value.
   * 
   * @param responseData The EntityActionResponseData to add.
   */
  public void addEntityActionResponseData(EntityActionResponseData responseData)
  {
    if (!responseData.isSuccess())
    {
      if (_isAllSuccess)
      {
        _overallErrorType = responseData.getErrorType();
        _isAllSuccess = false;      
      }
      else
      {
        // set to the lower level error type
        if (_overallErrorType > responseData.getErrorType())
          _overallErrorType = responseData.getErrorType();
      }
    } 
    super.addEntityDescriptor(responseData);
  }  

  /**
   * @see com.gridnode.pdip.framework.rpf.model.EntityDescriptorList#addEntityDescriptor(IEntityDescriptor)
   */
  public void addEntityDescriptor(IEntityDescriptor descriptor)
  {
    addEntityActionResponseData((EntityActionResponseData)descriptor);
  }

}
