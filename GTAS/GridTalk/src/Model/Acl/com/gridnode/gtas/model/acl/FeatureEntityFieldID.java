/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FeatureEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 28 2002    Goh Kan Mun             Created
 */

package com.gridnode.gtas.model.acl;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Feature module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public class FeatureEntityFieldID
{
  private Hashtable _table;
  private static FeatureEntityFieldID _self = null;

  private FeatureEntityFieldID()
  {
    _table = new Hashtable();

    _table.put(IFeature.ENTITY_NAME,
      new Number[]
      {
        IFeature.UID,
        IFeature.VERSION,
        IFeature.ACTIONS,
        IFeature.DATA_TYPES,
        IFeature.DESCRIPTION,
        IFeature.FEATURE
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new FeatureEntityFieldID();
    }
    return _self._table;
  }

}