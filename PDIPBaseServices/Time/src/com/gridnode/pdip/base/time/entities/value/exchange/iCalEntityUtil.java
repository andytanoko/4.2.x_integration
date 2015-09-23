package com.gridnode.pdip.base.time.entities.value.exchange;

import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.entities.model.iCalEntity;
import com.gridnode.pdip.base.time.entities.model.iCalEvent;
import com.gridnode.pdip.base.time.entities.model.iCalTodo;


class iCalEntityUtil
{
  static class CompNameMap
  {
    private String _iCalName;
    private Class _compClass;

    String getiCalName()
    {
      return _iCalName;
    }
    Class getCompClass()
    {
      return _compClass;
    }

    CompNameMap(String name, Class compClass)
    {
      _iCalName = name;
      _compClass = compClass;
    }

  }
  static iCalEntity getComponent(String value)
  {
    int size = compNameMaps.length;
    iCalEntity comp = null;
    for (int i = 0; i < size; i++)
    {
      if (value.equalsIgnoreCase(compNameMaps[i].getiCalName()))
      {
        Class icalClass = iCalEntityUtil.compNameMaps[i].getCompClass();
        try
        {
          comp = (iCalEntity) icalClass.newInstance();
        }
        catch (Exception ex)
        {
          System.out.println(ex);
        }
        return comp;
      }
    }
    throw new IllegalArgumentException(value + " is not supported");
  }
  
  static String getCompiCalName(iCalEntity comp)
  {
    int size = compNameMaps.length;
    for (int i = 0; i < size; i++)
    {
      if (comp.getClass().equals(compNameMaps[i].getCompClass()))
      {
        return compNameMaps[i].getiCalName();
      }
    }
    throw new IllegalArgumentException(comp.getClass() + " is not supported");
  }
  

  static CompNameMap[] compNameMaps = new CompNameMap[] { 
  	new CompNameMap("vevent", iCalEvent.class),
  	new CompNameMap("vtodo",  iCalTodo.class),
  	new CompNameMap("valarm", iCalAlarm.class)};


}
