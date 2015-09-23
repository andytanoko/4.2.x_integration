

package com.gridnode.pdip.base.time.entities.model;


import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.ArrayList;
import java.util.List;

abstract public class iCalEntity extends AbstractEntity
{
//	 static class  PropertyInfo
//	 {
//	 	 int _propKind;
//	 	 String[] _valueFieldName;
//	 	 int _valueKind;
//	 	 int _paramKind;
//	 	 String _paramFieldName;
//	 }
//	 
//	 public List getDefaultPropertyInfo()
//	 {
//	 	 return null;
//	 }
//	 
//   public List getAllProperty()
//   {
//   	 List res = new ArrayList();
//   	 
//   	 List propertyInfoList = getDefaultPropertyInfo();
//   	 int size = propertyInfoList.size();
//   	 for(int i=0; i<size; i++)
//   	 {
//   	 	 PropertyInfo info = (PropertyInfo)propertyInfoList.get(i);
//   	 
//   	   boolean isNullValue = false;
//   	   String[] fieldNames = info._fieldName;
//   	   Object[] fieldValues = new Object[fieldNames.length];
//   	   for(int num = 0; num<fieldNames.length; num++)
//   	   {
//   	     Field field= ReflectionUtility.getAccessibleField(getClass(), );
//         Object fieldValue= field.get(this);
//         if (fieldValue == null)
//         {
//          if(num == 0)
//          {
//          	isNullValue = true;
//          	break;
//          }
//          continue;
//         }
//         fieldValues[num] = fieldValue;
//   	   }
//   	  
//   	   if(isNullValue) 
//   	     continue;
//   	   
//   	   iCalValueV  
//   	   
//   	  iCalPropertyV prop = new iCalPropertyV(info._propKind);
//   	  res.add(prop);
//   	  prop.setValue();
//   	 
//   	  if(info._paramFieldName == null)
//   	    continue
//   	
//   }
//
//  static iCalValueV getIcalValue(Object value, int valueKind)
//  {
//    switch (valueKind)
//    {
//      case iCalValueKind.ICAL_INTEGER_VALUE :
//        {
//          return new iCalIntV((Integer) value);
//        }
//      case       case ICAL_DATE :
//        return new iCalDate((Date) value); :
//        return new iCalStringV((String) value);
//          
//    }
//    return null;
//  }

 public List getAllProperty()
 {
 	return new ArrayList();
 }
 abstract public void  addAnyProperty(iCalPropertyV prop);
}
