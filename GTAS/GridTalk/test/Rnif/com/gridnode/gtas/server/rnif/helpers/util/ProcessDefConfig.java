package com.gridnode.gtas.server.rnif.helpers.util;

import com.gridnode.gtas.server.rnif.helpers.Logger;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.util.ReflectionUtility;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Hashtable;

public class ProcessDefConfig extends GNConfigFile
{

  public ProcessDefConfig()
  {
    super();
  }

  public ProcessDefConfig(String fileName)
  {
    super(fileName);
  }

  public ProcessDef getProcessDef() throws Exception
  {

    Hashtable table= getProperties();
    return (ProcessDef) getEntityFromHashtable(table, ProcessDef.class);
  }

  public static Object getEntityFromHashtable(Hashtable table, Class objClass) throws Exception
  {
    Object obj= objClass.getConstructors()[0].newInstance((Object[])null);
    Enumeration en= table.keys();

    while (en.hasMoreElements())
    {

      String key= (String) en.nextElement();
      Object va= table.get(key);
      try
      {
        Field field= ReflectionUtility.getAccessibleField(objClass,key);

        if (va instanceof String)
        {
          String value= (String) va;
          String fieldType= field.getType().getName();
          Object res= AbstractEntity.convert(va, fieldType);
          field.set(obj, res);
        }
        else
        {
          if (va instanceof Hashtable)
          {
            Object subObj= getEntityFromHashtable((Hashtable) va, field.getType());
            field.set(obj, subObj);
          }
        }
      }
      catch (Exception ex)
      {
        Logger.err("field Name is " + key + " va is  " + va, ex);
        throw ex;
      }

    }

    return obj;
  }

}
