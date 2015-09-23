// %1023788048512:com.gridnode.pdip.base.time.value%
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

import java.util.List;

import com.gridnode.pdip.base.time.entities.model.iCalInt;
import com.gridnode.pdip.base.time.entities.model.iCalValue;

public class iCalParameterV
  implements IEntityDAOs,
             java.io.Serializable
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3333377417539526820L;

	/**
   * Creates a new iCalParameterV object.
   * 
   * @param kind DOCUMENT ME!
   */
  public iCalParameterV(short kind)
  {
    _kind = kind;
  }

  private short _kind;  //parameter Kind
  private iCalValueV _value;

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public short getKind()
  {
    return _kind;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setKind(short value)
  {
    _kind = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public iCalValueV getValue()
  {
    return _value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setValue(iCalValueV value)
  {
    _value = value;
  }

  //arrays of iCalValue List

  /**
   * DOCUMENT ME!
   * 
   * @param compKind DOCUMENT ME!
   * @param cUid DOCUMENT ME!
   * @param propKind DOCUMENT ME!
   * @param pUid DOCUMENT ME!
   * @return DOCUMENT ME! 
   * @throws Exception DOCUMENT ME!
   */
  public List[] toiCalValues(Short compKind, Long cUid, Short propKind, 
                             Long pUid)
                      throws Exception
  {
    if (_value == null)
      return null;
    Long uId = _intDAO.getNextKeyId();
    Short paramKind = new Short(_kind);
    List[] res = null;
    {
      iCalValue param = new iCalInt(null);
      param.setFieldValue(param.getKeyId(), uId);
      param.setRefFields(compKind, cUid, propKind, paramKind, 
                         IValueRefKind.IREF_PROPERTY, pUid);
      res = _value.toiCalValues(compKind, cUid, propKind, paramKind, 
                                IValueRefKind.IREF_PARAMETER, uId);
      res[0].add(0, param);
    }
    return res;
  }
}