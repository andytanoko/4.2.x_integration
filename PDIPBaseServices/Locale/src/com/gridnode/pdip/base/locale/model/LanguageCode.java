/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LanguageCode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 03 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.locale.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This class defines the data model for the ISO-639 language code.
 * <p>
 * The data model:<PRE>
 * Name           - Language name
 * Alpha2Code     - ISO-639-1 Alpha-2 code for the langauge
 * BAlpha3Code    - ISO-639-2 B Alpha-3 code for the language
 * TAlpha3Code    - ISO-639-2 T Alpha-3 code for the language
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class LanguageCode
  extends    AbstractEntity
  implements ILanguageCode
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5394996466110278698L;
	private String  _name;
  private String  _alpha2Code;
  private String  _bAlpha3Code;
  private String  _tAlpha3Code;

  public LanguageCode()
  {
  }

  public String getEntityDescr()
  {
    return getName()+ "(" + getAlpha2Code() + ")";
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return ALPHA_2_CODE;
  }

  // ***************** Getters & Setters *************************
  public String getName()
  {
    return _name;
  }

  public void setName(String name)
  {
    _name = name;
  }

  public String getTAlpha3Code()
  {
    return _tAlpha3Code;
  }

  public void setTAlpha3Code(String tAlpha3Code)
  {
    _tAlpha3Code = tAlpha3Code;
  }

  public String getAlpha2Code()
  {
    return _alpha2Code;
  }

  public void setAlpha2Code(String alpha2Code)
  {
    _alpha2Code = alpha2Code;
  }

  public String getBAlpha3Code()
  {
    return _bAlpha3Code;
  }

  public void setBAlpha3Code(String bAlpha3Code)
  {
    _bAlpha3Code = bAlpha3Code;
  }

}