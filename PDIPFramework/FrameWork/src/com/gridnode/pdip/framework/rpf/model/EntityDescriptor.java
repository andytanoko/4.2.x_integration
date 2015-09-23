/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityDescriptor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 9, 2003        Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.rpf.model;

import java.text.MessageFormat;

/**
 * This is an implementation of the IEntityDescriptor interface.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class EntityDescriptor implements IEntityDescriptor
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8701249134342657170L;

	private static String TOSTR_PATTERN = "EntityDescriptor[Key={0},Description={1}]";
  
  private Object _key;
  private String _description;
  
  /**
   * Constructor for EntityDescriptor.
   */
  protected EntityDescriptor()
  {
  }
  
  /**
   * Constructor for EntityDescriptor.
   * 
   * @param key         Key for the entity described.
   * @param description Description for the entity described.
   */
  public EntityDescriptor(Object key, String description)
  {
    _key = key;
    _description = description;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.model.IEntityDescriptor#getKey()
   */
  public Object getKey()
  {
    return _key;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.model.IEntityDescriptor#getDescription()
   */
  public String getDescription()
  {
    return _description;
  }

  /**
   * Sets the Key value of the entity described.
   * 
   * @param key The Key value.
   */
  public void setKey(Object key)
  {
    _key = key;
  }
  
  /**
   * Sets a description of the entity described.
   * 
   * @param description A description.
   */
  public void setDescription(String description)
  {
    _description = description;
  }
  
  /**
   * This method checks for equality with another EntityDescriptor object
   * based on the Key values. 
   * 
   * @param obj The object to compare with. It is considered as not equal 
   * <code>obj</code> is not an instance of EntityDescriptor.
   * @return <b>true</b> iff <code>obj</code> is an instance of EntityDescriptor
   * class and its Key value equals the Key value of this object.
   * 
   * @see java.lang.Object#equals(Object)
   */
  public boolean equals(Object obj)
  {
    boolean equals = false;
    
    if (obj != null && obj instanceof EntityDescriptor)
    {
      // IMPORTANT:
      // ==> The hashCode of Long object may give same value
      // ==> for different numerical Long values. 
      // ==> For e.g. both Long value 1 and -2 results in a hashCode of 1.
      // ==> Thus, explicitly compare the actual value instead of using hashCode.
      equals = ((EntityDescriptor)obj).getKey().equals(this.getKey());
    } 
    return equals;
  }

  /**
   * Returns the hashCode value of the Key value of this object.
   * 
   * @return The hashCode value of the Key value.
   * @see java.lang.Object#hashCode()
   */
  public int hashCode()
  {
    return getKey().hashCode();
  }

  public String toString()
  {
    return MessageFormat.format(TOSTR_PATTERN, new Object[]{getKey(), getDescription()});
  }
}
