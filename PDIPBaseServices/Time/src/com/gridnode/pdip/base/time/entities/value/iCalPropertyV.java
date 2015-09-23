// %1023788048840:com.gridnode.pdip.base.time.value%
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

import java.util.ArrayList;
import java.util.List;

public class iCalPropertyV
  implements java.io.Serializable
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5299680192347660217L;
	private short _kind;
  private List _params;
  private iCalValueV _value;

  /**
   * Creates a new iCalPropertyV object.
   * 
   * @param kind DOCUMENT ME!
   */
  public iCalPropertyV(short kind)
  {
    _kind = kind;
  }
  
  public iCalPropertyV(int kind)
	{
	   this((short)kind);
	}

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
  public List getParams()
  {
    return _params;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param paramKind DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public List getParamList(int paramKind)
  {
    if (_params == null || _params.isEmpty())
      return null;
    List res = new ArrayList();
    int size = _params.size();
    for (int i = 0; i < size; i++)
    {
      iCalParameterV param = (iCalParameterV)_params.get(i);
      if (param.getKind() == (short)paramKind)
      {
      }
      res.add(param);
    }
    return res;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param paramKind DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public iCalParameterV getParam(int paramKind)
  {
    if (_params == null || _params.isEmpty())
      return null;
    //List res = new ArrayList(); 
    int size = _params.size();
    for (int i = 0; i < size; i++)
    {
      iCalParameterV param = (iCalParameterV)_params.get(i);
      if (param.getKind() == (short)paramKind)
      {
        return param;
      }
    }
    return null;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param params DOCUMENT ME!
   */
  public void setParams(List params)
  {
    _params = params;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param param DOCUMENT ME!
   */
  public void addParam(iCalParameterV param)
  {
    if (_params == null)
    {
      _params = new ArrayList();
    }
    _params.add(param);
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
}