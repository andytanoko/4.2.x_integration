package com.gridnode.gtas.server.rnif.helpers;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

public class EntityUtil
{

  public static IDataFilter getEqualFilter(Number[] fieldIds, Object[] value)
  {
    if (fieldIds == null || (0 == fieldIds.length))
      return null;
    IDataFilter filter= new DataFilterImpl();
    filter.addSingleFilter(null, fieldIds[0], filter.getEqualOperator(), value[0], false);

    for (int i= 1; i < fieldIds.length; i++)
    {

      filter.addSingleFilter(
        filter.getAndConnector(),
        fieldIds[i],
        filter.getEqualOperator(),
        value[i],
        false);
    }
    return filter;
  }
  


}
