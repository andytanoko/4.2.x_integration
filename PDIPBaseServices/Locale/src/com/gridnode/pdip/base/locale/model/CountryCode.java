/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CountryCode.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 02 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.locale.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This class defines the data model for the ISO-3166 country code.
 * <p>
 * The data model:<PRE>
 * Name           - Country name or area name
 * NumericalCode  - Numerical code for the country or area.
 * Alpha2Code     - ISO-3166-1 code (2-letter) for the country or area.
 * Alpha3Code     - ISO-3166-2 code (3-letter) for the country or area.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class CountryCode
  extends    AbstractEntity
  implements ICountryCode
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5578099795617935364L;
	private String  _name;
  private Integer _numericalCode;
  private String  _alpha2Code;
  private String  _alpha3Code;

  public CountryCode()
  {
  }

  public String getEntityDescr()
  {
    return getAlpha3Code()+ "(" + getNumericalCode() + ")";
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return NUMERICAL_CODE;
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

  public Integer getNumericalCode()
  {
    return _numericalCode;
  }

  public void setNumericalCode(Integer numericalCode)
  {
    _numericalCode = numericalCode;
  }

  public String getAlpha2Code()
  {
    return _alpha2Code;
  }

  public void setAlpha2Code(String alpha2Code)
  {
    _alpha2Code = alpha2Code;
  }

  public String getAlpha3Code()
  {
    return _alpha3Code;
  }

  public void setAlpha3Code(String alpha3Code)
  {
    _alpha3Code = alpha3Code;
  }

}