package com.gridnode.pdip.base.locale.helpers;

import com.gridnode.pdip.base.locale.model.CountryCode;
import com.gridnode.pdip.framework.db.DirectDAOEntityHandler;

public class CountryCodeEntityHandler
  extends DirectDAOEntityHandler
{
  private static CountryCodeEntityHandler _self;
  private static Object _lock = new Object();

  private CountryCodeEntityHandler()
  {
    super(CountryCode.ENTITY_NAME);
  }

  public static CountryCodeEntityHandler getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
          _self = new CountryCodeEntityHandler();
      }
    }
    return _self;
  }
}