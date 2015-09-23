/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LocaleFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 04 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.model.locale;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Locale module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class LocaleEntityFieldID
{
  private Hashtable _table;
  private static LocaleEntityFieldID _self = null;

  private LocaleEntityFieldID()
  {
    _table = new Hashtable();

    //CountryCode
    _table.put(ICountryCode.ENTITY_NAME,
      new Number[]
      {
        ICountryCode.ALPHA_2_CODE,
        ICountryCode.ALPHA_3_CODE,
        ICountryCode.NAME,
        ICountryCode.NUMERICAL_CODE,
      });

    //LanguageCode
    _table.put(ILanguageCode.ENTITY_NAME,
      new Number[]
      {
        ILanguageCode.ALPHA_2_CODE,
        ILanguageCode.B_ALPHA_3_CODE,
        ILanguageCode.NAME,
        ILanguageCode.T_ALPHA_3_CODE,
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new LocaleEntityFieldID();
    }
    return _self._table;
  }
}