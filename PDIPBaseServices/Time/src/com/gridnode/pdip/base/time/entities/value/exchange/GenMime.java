package com.gridnode.pdip.base.time.entities.value.exchange;

import com.gridnode.pdip.base.time.entities.model.iCalComponent;
import com.gridnode.pdip.base.time.entities.model.iCalEntity;
import com.gridnode.pdip.base.time.entities.value.iCalParameterV;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;

import java.util.List;

public class GenMime
{
  static String NewLine = "\r\n";
  static public String genCalendar(List compList)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("BEGIN:VCALENDAR" + NewLine);
    iCalEntity comp = null;
    int size = compList.size();
    for (int i = 0; i < size; i++)
    {
      comp = (iCalEntity) compList.get(i);
      buf.append(genComponent(comp));
      // 	  buf.append(NewLine);
    }
    buf.append("END:VCALENDAR" + NewLine);
    return buf.toString();
  }

  static public String genComponentProperties(iCalComponent comp)
  {
    String compName = iCalEntityUtil.getCompiCalName(comp).toUpperCase();
    StringBuffer buf = new StringBuffer();
    buf.append("BEGIN:" + compName + NewLine);

    List propList = comp.getProperties();
    iCalPropertyV prop = null;
    int size = propList.size();
    for (int i = 0; i < size; i++)
    {
      prop = (iCalPropertyV) propList.get(i);
      buf.append(genProperty(prop));
      buf.append(NewLine);
    }
    buf.append("END:" + compName + NewLine);
    return buf.toString();
  }

  static public String genComponent(iCalEntity comp)
  {
    String compName = iCalEntityUtil.getCompiCalName(comp).toUpperCase();
    StringBuffer buf = new StringBuffer();
    buf.append("BEGIN:" + compName + NewLine);

    List propList = comp.getAllProperty();
    iCalPropertyV prop = null;
    int size = propList.size();
    for (int i = 0; i < size; i++)
    {
      prop = (iCalPropertyV) propList.get(i);
      buf.append(genProperty(prop));
      buf.append(NewLine);
    }
    buf.append("END:" + compName + NewLine);
    return buf.toString();
  }

  static public String genProperty(iCalPropertyV prop)
  {
    String propName = iCalPropertyUtil.getPropertyName(prop.getKind()).toUpperCase();
    StringBuffer buf = new StringBuffer();
    buf.append(propName);

    List paramList = prop.getParams();
    if (paramList != null)
    {
      int size = paramList.size();
      for (int i = 0; i < size; i++)
      {
        iCalParameterV param = (iCalParameterV) paramList.get(i);
        buf.append(";" + iCalParameterUtil.toString(param));
      }
    }
    buf.append(":");
    buf.append(prop.getValue().toString());
    return buf.toString();
  }

}
