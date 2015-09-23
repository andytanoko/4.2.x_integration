package com.gridnode.pdip.base.locale.helpers;

import com.gridnode.pdip.base.locale.model.LanguageCode;
import com.gridnode.pdip.framework.db.DirectDAOEntityHandler;

public class LanguageCodeEntityHandler
  extends DirectDAOEntityHandler
{
  private static LanguageCodeEntityHandler _self;
  private static Object _lock = new Object();

  private LanguageCodeEntityHandler()
  {
    super(LanguageCode.ENTITY_NAME);
  }

  public static LanguageCodeEntityHandler getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
          _self = new LanguageCodeEntityHandler();
      }
    }
    return _self;
  }
}