// %1023788041778:com.gridnode.pdip.base.entity%
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

/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.model;

import com.gridnode.pdip.base.time.entities.helpers.iCalUtil;
import com.gridnode.pdip.base.time.entities.value.iCalDateV;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyKind;
import com.gridnode.pdip.base.time.entities.value.iCalPropertyV;
import com.gridnode.pdip.base.time.entities.value.iCalStringV;
import com.gridnode.pdip.base.time.entities.value.iCalValueKind;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

abstract public class iCalComponent extends iCalEntity implements IiCalComponent
{
  protected Short _kind; //is it event or todo;
  protected Integer _ownerId;
  //access classifcation: is private? public? confidential? can be other values(defined in IClassification)
  protected String _classification;
  protected Date _createDt;
  protected Date _lastModifyDt;
  //wether the startDate(endDt) is Date type: to specify anniversary or daily reminder
  protected Boolean _isDateType;
  protected Boolean _isDtFloat;
  protected Date _startDt;
  protected Integer _priority;
  protected Integer _sequenceNum;
  protected Integer _status; //inProcess? cancelled?
  protected String _iCalUid; //UID defined in iCal specification
  //only one of the following two property id can appear at once.
  protected Date _endDt; //is the due time in todo.
  protected Long _duration;
	protected String _propertiesStr; // Mime representation of property List 

  //not saved , saved ub the _propertiesStr
  protected List _properties;

  // ******************* Methods from AbstractEntity ******************

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getEntityDescr()
  {
    return getEntityName();
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Number getKeyId()
  {
    return UID;
  }

  // ******************* get/set Methods******************

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Short getKind()
  {
    return _kind;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setKind(Short value)
  {
    _kind = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Integer getOwnerId()
  {
    return _ownerId;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setOwnerId(Integer value)
  {
    _ownerId = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getClassification()
  {
    return _classification;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setClassification(String value)
  {
    _classification = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Date getCreateDt()
  {
    return _createDt;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setCreateDt(Date value)
  {
    _createDt = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Date getLastModifyDt()
  {
    return _lastModifyDt;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setLastModifyDt(Date value)
  {
    _lastModifyDt = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Boolean getIsDateType()
  {
    return _isDateType;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setIsDateType(Boolean value)
  {
    _isDateType = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Boolean getIsDtFloat()
  {
    return _isDtFloat;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setIsDtFloat(Boolean value)
  {
    _isDtFloat = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Date getStartDt()
  {
    return _startDt;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setStartDt(Date value)
  {
    if(value != null)
      value = iCalUtil.getTimeInSecod(value);    
    _startDt = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Integer getPriority()
  {
    return _priority;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setPriority(Integer value)
  {
    _priority = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Integer getSequenceNum()
  {
    return _sequenceNum;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setSequenceNum(Integer value)
  {
    _sequenceNum = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Integer getStatus()
  {
    return _status;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setStatus(Integer value)
  {
    _status = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getiCalUid()
  {
    return _iCalUid;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setiCalUid(String value)
  {
    _iCalUid = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Date getEndDt()
  {
    return _endDt;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setEndDt(Date value)
  {
    if(value != null)
      value = iCalUtil.getTimeInSecod(value);
    _endDt = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Long getDuration()
  {
    return _duration;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setDuration(Long value)
  {
    _duration = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public List getProperties()
  {
    return _properties;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setProperties(List value)
  {
    _properties = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param prop DOCUMENT ME!
   */
  public void addProperty(iCalPropertyV prop)
  {
    if (_properties == null)
    {
      _properties = new ArrayList();
    }
    _properties.add(prop);
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Date getSureEndDt()
  {
    if (_endDt == null && _duration != null)
      return new Date(_startDt.getTime() + _duration.intValue() * 1000);
    return _endDt;
  }
  

  /**
   * DOCUMENT ME!
   * 
   * @param propKind DOCUMENT ME!
   * @return DOCUMENT ME! 
   */
  public List getPropertyList(int propKind)
  {
    if (_properties == null || _properties.isEmpty())
      return null;
    List res = new ArrayList();
    int size = _properties.size();
    for (int i = 0; i < size; i++)
    {
      iCalPropertyV prop = (iCalPropertyV) _properties.get(i);
      if (prop.getKind() == (short) propKind)
      {
        res.add(prop);
      }
    }
    return res;
  }

  public List getAllProperty()
  {
    List res = new ArrayList();

    iCalPropertyV property = null;

    if (_classification != null && !"".equals(_classification))
    {
      property = new iCalPropertyV(iCalPropertyKind.ICAL_CLASS_PROPERTY);
      property.setValue(new iCalStringV(iCalValueKind.ICAL_CLASS_VALUE));
      res.add(property);
    }

    if (_createDt != null)
    {
      property = new iCalPropertyV(iCalPropertyKind.ICAL_CREATED_PROPERTY);
      property.setValue(new iCalDateV(false, _createDt));
      res.add(property);
    }

    if (_lastModifyDt != null)
    {
      property = new iCalPropertyV(iCalPropertyKind.ICAL_DTSTAMP_PROPERTY);
      property.setValue(new iCalDateV(false, _lastModifyDt));
      res.add(property);
    }

    boolean isDateType = _isDateType == null ? false : _isDateType.booleanValue();
    if (_startDt != null)
    {
      property = new iCalPropertyV(iCalPropertyKind.ICAL_DTSTART_PROPERTY);
      property.setValue(new iCalDateV(isDateType, _startDt));
      res.add(property);
    }

    if (_endDt != null)
    {
      property = new iCalPropertyV(iCalPropertyKind.ICAL_DTEND_PROPERTY);
      property.setValue(new iCalDateV(isDateType, _endDt));
      res.add(property);
    }

    res.addAll(getProperties());
    return res;
  }

  public void addAnyProperty(iCalPropertyV prop)
  {
    int propKind = (int) prop.getKind();
    if (iCalPropertyKind.ICAL_DTSTART_PROPERTY == propKind)
    {
      _startDt = ((iCalDateV) prop.getValue()).getTime();
      boolean isDateType = ((iCalDateV) prop.getValue()).getIsDate();
      if (isDateType)
        _isDateType = Boolean.TRUE;
    }
    else
      addProperty(prop);
  }


  public boolean hasNoRecurrence()
  {
    List temp = getPropertyList(iCalPropertyKind.ICAL_RRULE_PROPERTY);
    List temp1 = getPropertyList(iCalPropertyKind.ICAL_RDATE_PROPERTY);
    
    return ((temp == null || temp.isEmpty()) && 
        (temp1 == null || temp1.isEmpty()));
  }

  public void setPropertiesStr(String _propertiesStr)
  {
    this._propertiesStr = _propertiesStr;
  }

  public String getPropertiesStr()
  {
    return _propertiesStr;
  }
}
