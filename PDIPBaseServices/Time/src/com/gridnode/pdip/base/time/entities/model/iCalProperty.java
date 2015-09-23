// %1023788042668:com.gridnode.pdip.base.entity%
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
package com.gridnode.pdip.base.time.entities.model;

import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class iCalProperty
  extends AbstractEntity
  implements IiCalProperty
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6315111892315348790L;
	protected Short _kind;  //Property Kind
  protected Short _compKind;  //the table iCalCompId refers to.
  protected Long _iCalCompId;

  /**
   * Creates a new iCalProperty object.
   */
  public iCalProperty()
  {
  }

  /**
   * Creates a new iCalProperty object.
   * 
   * @param compKind DOCUMENT ME!
   * @param compUid DOCUMENT ME!
   * @param valueObj DOCUMENT ME!
   */
  public iCalProperty(Short compKind, Long compUid, iCalPropertyV valueObj)
  {
    _kind = new Short(valueObj.getKind());
    _compKind = (compKind);
    _iCalCompId = compUid;
  }

  // ******************* Methods from AbstractEntity ******************

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getEntityName()
  {
    return iCalProperty.class.getName();
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getEntityDescr()
  {
    return getEntityName();
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Number getKeyId()
  {
    return UID;
  }

  // ******************* get/set Methods******************

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Short getKind()
  {
    return _kind;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setKind(Short value)
  {
    _kind = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Short getCompKind()
  {
    return _compKind;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setCompKind(Short value)
  {
    _compKind = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Long getiCalCompId()
  {
    return _iCalCompId;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setiCalCompId(Long value)
  {
    _iCalCompId = value;
  }
}
