// %1023788044465:com.gridnode.pdip.base.time.ejb%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 */

package com.gridnode.pdip.base.time.entities.ejb;

import com.gridnode.pdip.base.time.entities.model.iCalComponent;
import com.gridnode.pdip.base.time.entities.model.iCalProperty;
import com.gridnode.pdip.base.time.entities.model.iCalValue;
import com.gridnode.pdip.base.time.entities.value.ExEntityDAOImpl;
import com.gridnode.pdip.base.time.entities.value.IValueRefKind;
import com.gridnode.pdip.base.time.entities.value.iCalParameterV;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.base.time.entities.value.iCalValueV;
import com.gridnode.pdip.base.time.entities.value.exchange.GenMime;
import com.gridnode.pdip.base.time.entities.value.exchange.ParseMime;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.log.Log;

import javax.ejb.CreateException;
import javax.ejb.EJBException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

abstract public class iCalComponentBean
  extends AbstractEntityBean
  implements com.gridnode.pdip.base.time.entities.value.IEntityDAOs
{
  protected static final String LogCat = iCalComponentBean.class.getName();
  protected boolean _changed = false;

  abstract Short getCompKind();

  /**
   * DOCUMENT ME!
   *
   * @param entity DOCUMENT ME!
   * @throws EntityModifiedException DOCUMENT ME!
   */
  public void setData(IEntity entity) throws EntityModifiedException
  {
    super.setData(entity);
    _changed = true;
  }

  /**
   * DOCUMENT ME!
   *
   * @param entity DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws CreateException DOCUMENT ME!
   */
  public Long ejbCreate(IEntity entity) throws CreateException
  {
    Log.debug(LogCat, " ejbCreate called for " + entity.getEntityDescr());
    iCalComponent comp = (iCalComponent) entity;
    String mimeStr = GenMime.genComponentProperties(comp);
    comp.setPropertiesStr(mimeStr);
    Long uid = super.ejbCreate(comp);
    //    try
    //    {
    //      storeProperties(comp, uid);
    //    }
    //    catch (Exception e)
    //    {
    //      Log.err(Log.DB, "Exception in ejbCreate ", e);
    //      throw new CreateException(e.toString());
    //    }
    _changed = false;
    return uid;
  }

  /**
   * @deprecated 
   * @param compEntity
   * @param uid
   * @throws Exception
   */
  protected void storeProperties(iCalComponent compEntity, Long uid) throws Exception
  {
    Log.debug(LogCat, " storeProperties called for Uid=" + uid);
    Short compKind = getCompKind();
    List propertyList = compEntity.getProperties();
    if (propertyList == null)
      return;
    int size = propertyList.size();
    List propertyEntityList = new ArrayList(size);
    List intList = new ArrayList();
    List dateList = new ArrayList();
    List stringList = new ArrayList();
    List textList = new ArrayList();
    List[] valueLists = new List[] { intList, dateList, stringList, textList };
    for (int i = 0; i < size; i++)
    {
      iCalPropertyV property = (iCalPropertyV) propertyList.get(i);
      Short propKind = new Short(property.getKind());
      iCalProperty propEntity = new iCalProperty(compKind, uid, property);
      propertyEntityList.add(propEntity);
      Long pUid = (Long) _propDAO.getNextKeyId();
      propEntity.setFieldValue(propEntity.getKeyId(), pUid);
      iCalValueV value = property.getValue();
      System.out.println(" In iCalComponentBean " + value.getClass());
      List[] valueEntityArray =
        value.toiCalValues(compKind, uid, propKind, null, IValueRefKind.IREF_PROPERTY, pUid);
      addResToValueList(valueLists, valueEntityArray);
      List parameterList = property.getParams();
      if (parameterList != null)
      {
        int count = parameterList.size();
        for (int j = 0; j < count; j++)
        {
          iCalParameterV parameter = (iCalParameterV) parameterList.get(j);
          List[] res = parameter.toiCalValues(compKind, uid, propKind, pUid);
          addResToValueList(valueLists, res);
        }
      }
    }
    _propDAO.create(propertyEntityList);
    ExEntityDAOImpl[] daoList = new ExEntityDAOImpl[] { _intDAO, _DateDAO, _StringDAO, _textDAO };
    for (int num = 0; num < 4; num++)
    {
      daoList[num].create(valueLists[num]);
    }
  }

  static class ValueCache
  {
    public iCalValueV _value;
    public List _subEntityList = new ArrayList();
  }

  /**
   * DOCUMENT ME!
   */
  public void ejbLoad()
  {
    try
    {
      Long compUid = (Long) _ctx.getPrimaryKey();
      Log.debug(LogCat, " ejbLoad called for Uid=" + compUid);
      _entity = getDAO().load(compUid);
      iCalComponent comp = (iCalComponent) _entity;
      String propertiesStr = comp.getPropertiesStr();
      if (propertiesStr == null)
        loadComponent(_entity, compUid, getCompKind());
      else
      {
        ParseMime parser = new ParseMime();
        StringReader reader = new StringReader(propertiesStr);
        List res = parser.parse(reader);
        if (res != null && !res.isEmpty())
        {
          iCalComponent firstComp = (iCalComponent) res.get(0);
          List propertyList = firstComp.getProperties();
          if (propertyList != null && !propertyList.isEmpty())
            comp.setProperties(propertyList);
        }
      }
    }
    catch (Exception e)
    {
      Log.warn(Log.DB, "Exception in ejbLoad ", e);
      throw new EJBException(e);
    }
    _changed = false;
  }

  /**
   * @deprecated
   * @param comp
   * @param compUid
   * @param compKind
   * @throws Exception
   */
  static void loadComponent(IEntity comp, Long compUid, Short compKind) throws Exception
  {
    Object[] values = new Object[] { compUid, compKind };
    Number[] propFKeyId = new Number[] { iCalProperty.iCal_COMP_ID, iCalProperty.COMP_KIND };
    List propertyEntityList = _propDAO.loadByFKey(propFKeyId, values);
    if (propertyEntityList == null || propertyEntityList.isEmpty())
      return;
    int pSize = propertyEntityList.size();
    List propList = new ArrayList(pSize);
    HashMap propMap = new HashMap(pSize);
    for (int i = 0; i < pSize; i++)
    {
      iCalProperty propEntity = (iCalProperty) propertyEntityList.get(i);
      iCalPropertyV prop = new iCalPropertyV(propEntity.getKind().shortValue());
      propList.add(prop);
      Long pUid = (Long) propEntity.getKey();
      propMap.put(pUid, prop);
    }
    ((iCalComponent) comp).setProperties(propList);
    Log.debug(LogCat, " Property Load for Uid=" + compUid);
    Number[] valueFKeyId = new Number[] { iCalValue.iCal_COMP_ID, iCalValue.COMP_KIND };
    ExEntityDAOImpl[] daoList = new ExEntityDAOImpl[] { _intDAO, _DateDAO, _StringDAO, _textDAO };
    List[] valueEntityLists = new List[4];
    for (int i = 0; i < daoList.length; i++)
    {
      valueEntityLists[i] = daoList[i].loadByFKey(valueFKeyId, values);
    }
    HashMap paramMap = new HashMap();
    HashMap valueMap = new HashMap();
    for (int j = 0; j < valueEntityLists.length; j++)
    {
      List valueEntityList = valueEntityLists[j];
      if (valueEntityList == null)
        continue;
      int listSize = valueEntityList.size();
      for (int i = 0; i < listSize; i++)
      {
        iCalValue valueEntity = (iCalValue) valueEntityList.get(i);
        Short paramKind = valueEntity.getParamKind();
        Short refKind = valueEntity.getRefKind();
        Short valueKind = valueEntity.getValueKind();
        Long refId = valueEntity.getRefId();
        Long valueUid = (Long) valueEntity.getKey();
        if (IValueRefKind.IREF_VALUE.equals(refKind))
        {
          //parts of a iCalValueV
          ValueCache valueEntry = (ValueCache) valueMap.get(refId);
          if (valueEntry == null)
          {
            valueEntry = new ValueCache();
            valueMap.put(refId, valueEntry);
          }
          valueEntry._subEntityList.add(valueEntity);
        }
        else if (IValueRefKind.IREF_PARAMETER.equals(refKind))
        { //iCal_value of Parameter
          iCalValueV value = iCalValueV.getValueByKind(valueKind.shortValue());
          ValueCache valueEntry = (ValueCache) valueMap.get(valueUid);
          if (valueEntry == null)
          {
            valueEntry = new ValueCache();
            valueMap.put(valueUid, valueEntry);
          }
          valueEntry._value = value;
          iCalParameterV param = (iCalParameterV) paramMap.get(refId);
          if (param == null)
          {
            param = new iCalParameterV(paramKind.shortValue());
            paramMap.put(refId, param);
          }
          param.setValue(value);
        }
        else if (IValueRefKind.IREF_PROPERTY.equals(refKind))
        {
          iCalPropertyV prop = (iCalPropertyV) propMap.get(refId);
          if (prop == null)
          {
            Log.err(
              LogCat,
              "property corresponding to value Entity " + valueEntity + "is not found",
              new Exception());
            continue;
          }
          if (paramKind == null) //property value
          {
            iCalValueV value = iCalValueV.getValueByKind(valueKind.shortValue());
            ValueCache valueEntry = (ValueCache) valueMap.get(valueUid);
            if (valueEntry == null)
            {
              valueEntry = new ValueCache();
              valueMap.put(valueUid, valueEntry);
            }
            valueEntry._value = value;
            prop.setValue(value);
          }
          else
          { //parameter entry
            iCalParameterV param = (iCalParameterV) paramMap.get(valueUid);
            if (param == null)
            {
              param = new iCalParameterV(paramKind.shortValue());
              paramMap.put(valueUid, param);
            }
            prop.addParam(param);
          }
        }
        else
        {
          Log.err(
            LogCat,
            "Wrong refType field for value Entity " + valueEntity + "is not found",
            new Exception());
          continue;
        }
      }
    }
    Object[] valueArray = valueMap.values().toArray();
    for (int i = 0; i < valueArray.length; i++)
    {
      ValueCache valueEntry = (ValueCache) valueArray[i];
      iCalValueV valueV = valueEntry._value;
      if (valueV == null)
      {
        Log.err(
          LogCat,
          "Missed value declaration for " + valueEntry._subEntityList.get(0),
          new Exception());
        continue;
      }
      valueV.fromiCalValues(valueEntry._subEntityList);
    }
    return;
  }
  /**
   * DOCUMENT ME!
   */
  public void ejbStore()
  {
    if (!_changed)
      return;
    try
    {
      Log.debug(LogCat, "ejbStore called  for " + _entity.getEntityDescr());
      //Long compUid = (Long) _entity.getKey(); 
			iCalComponent comp = (iCalComponent) _entity;
			String mimeStr = GenMime.genComponentProperties(comp);
			comp.setPropertiesStr(mimeStr);
//      removeProperties(compUid);
      getDAO().store(comp);
//      storeProperties((iCalComponent) _entity, compUid);
    }
    catch (Exception e)
    {
      Log.warn(Log.DB, "Exception in ejbStore ", e);
      throw new EJBException(e);
    }
    _changed = false;
  }

  /**
   * DOCUMENT ME!
   */
  public void ejbRemove()
  {
    try
    {
      Long compUid = (Long) _ctx.getPrimaryKey();
      removeProperties(compUid);
      Log.debug(LogCat, "Remove component " + compUid);
      getDAO().remove(compUid);
    }
    catch (Exception e)
    {
      Log.warn(Log.DB, "Exception in ejbRemove ", e);
      throw new EJBException(e);
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param compUid DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  protected void removeProperties(Long compUid) throws Exception
  {
    if (compUid != null)
    {
      Log.debug(LogCat, " removeProperties called for Uid=" + compUid);
      Object[] values = new Object[] { compUid, getCompKind()};
      Number[] propFKeyId = new Number[] { iCalProperty.iCal_COMP_ID, iCalProperty.COMP_KIND };
      Number[] valueFKeyId = new Number[] { iCalValue.iCal_COMP_ID, iCalValue.COMP_KIND };
      Number[][] foreignKeyIds =
        new Number[][] { propFKeyId, valueFKeyId, valueFKeyId, valueFKeyId, valueFKeyId };
      ExEntityDAOImpl[] daoList =
        new ExEntityDAOImpl[] { _propDAO, _intDAO, _DateDAO, _StringDAO, _textDAO };
      for (int i = 0; i < daoList.length; i++)
      {
        daoList[i].deleteByFKey(foreignKeyIds[i], values);
      }
    }
  }

  //   public Collection ejbFindByFilter(IDataFilter filter) throws FinderException
  //   {
  //      try
  //      {
  //         return getDAO().findByFilter(filter);
  //      }
  //      catch (Exception e)
  //      {
  //         Log.err(Log.DB, "Exception in ejbFindByFilter ", e);
  //         throw new FinderException(e.toString());
  //      }
  //   }
  void addResToValueList(List[] valueLists, List[] res)
  {
    if (res == null)
      return;
    for (int i = 0; i < 4; i++)
    {
      if (res[i] != null)
        valueLists[i].addAll(res[i]);
    }
  }
}
