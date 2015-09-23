// %1023788041793:com.gridnode.pdip.base.entity%
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
 * Feb 09 2004    Koh Han Sing        Add TaskId. 
 */

/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.model;

import com.gridnode.pdip.base.time.entities.helpers.iCalUtil;
import com.gridnode.pdip.base.time.entities.value.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class iCalAlarm extends iCalEntity implements IiCalAlarm
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8592221114111571792L;
	protected Long _startDuration; // from the Trigger definition.  relative starttime
  protected Date _startDt; //  absolute starttime.
  protected Integer _related;
  //IRelated.ABSOLUTE;     // type of starttime: absolute, relatively defined in IRelated
  protected Long _delayPeriod;
  protected Integer _repeat;
  //the following is defined for trigger callback when time comes.
  protected String _category;
  protected String _senderKey;
  protected String _receiverKey;
  //the following is defined for implementation purpose only.
  protected Boolean _disabled; //is the alarm diabled?
  protected Date _nextDueTime; //next due time
  protected Integer _count;
  //how many time has the alarm been trigger. corresponding to the repeat field/

  //newly added to integrate with iCalComponent  
  protected Long _parentUid;
  protected Short _parentKind;
  protected String _recurListStr;
  protected Boolean _isRecurComplete;
  protected String _curRecur;
  protected Boolean _isPseudoParent;
  protected Boolean _includeParentStartTime;
  protected String _taskId;

  // ******************* Methods from AbstractEntity ******************

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getEntityName()
  {
    return iCalAlarm.class.getName();
  }

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
  public Long getStartDuration()
  {
    return _startDuration;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setStartDuration(Long value)
  {
    _startDuration = value;
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
  public Integer getRelated()
  {
    return _related;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setRelated(Integer value)
  {
    _related = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Long getDelayPeriod()
  {
    return _delayPeriod;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setDelayPeriod(Long value)
  {
    _delayPeriod = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Integer getRepeat()
  {
    return _repeat;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setRepeat(Integer value)
  {
    _repeat = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getCategory()
  {
    return _category;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setCategory(String value)
  {
    _category = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getSenderKey()
  {
    return _senderKey;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setSenderKey(String value)
  {
    _senderKey = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getReceiverKey()
  {
    return _receiverKey;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setReceiverKey(String value)
  {
    _receiverKey = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Boolean getDisabled()
  {
    return _disabled;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setDisabled(Boolean value)
  {
    _disabled = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Date getNextDueTime()
  {
    return _nextDueTime;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setNextDueTime(Date value)
  {
    if(value != null)
      value = iCalUtil.getTimeInSecod(value);
    _nextDueTime = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Integer getCount()
  {
    return _count;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setCount(Integer value)
  {
    _count = value;
  }

  /**
   * DOCUMENT ME!
   */
  public void increaseCount()
  {
    if (_count == null)
      _count = new Integer(1);
    else
      _count = new Integer(_count.intValue() + 1);
  }

  public Long getParentUid()
  {
    return _parentUid;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setParentUid(Long value)
  {
    _parentUid = value;
  }

  public Short getParentKind()
  {
    return _parentKind;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setParentKind(Short value)
  {
    _parentKind = value;
  }

  public String getRecurListStr()
  {
    return _recurListStr;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setRecurListStr(String value)
  {
    _recurListStr = value;
  }

  public String getCurRecur()
  {
    return _curRecur;
  }

  public void setCurRecur(String value)
  {
    _curRecur = value;
  }

  public Boolean getIsRecurComplete()
  {
    return _isRecurComplete;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setIsRecurComplete(Boolean value)
  {
    _isRecurComplete = value;
  }

  public Boolean getIsPseudoParent()
  {
    return _isPseudoParent;
  }

  public void setIsPseudoParent(Boolean isPseudoParent)
  {
    _isPseudoParent = isPseudoParent;
  }
  
  public String getTaskId()
  {
    return _taskId;
  }

  public void setTaskId(String taskId)
  {
    _taskId = taskId;
  }

  public boolean isAbsolute()
  {
    boolean res = ((_related == null) || (IRelated.ABSOLUTE == _related.intValue()));
    return res;
  }

  public List getAllProperty()
  {
    List res = new ArrayList();

    iCalPropertyV property = null;

    if (_startDt != null)
    {
      property = new iCalPropertyV(iCalPropertyKind.ICAL_TRIGGER_PROPERTY);
      property.setValue(new iCalDateV(false, _startDt));
      iCalParameterV param = new iCalParameterV((short) iCalParameterKind.ICAL_VALUE_PARAMETER);
      param.setValue(new iCalIntV(new Integer(IValueType.ICAL_VALUE_DATETIME)));
      property.addParam(param);
      res.add(property);
    }
    else if (_startDuration != null)
    {
      property = new iCalPropertyV(iCalPropertyKind.ICAL_TRIGGER_PROPERTY);
      property.setValue(new iCalDurationV(new Integer(_startDuration.intValue())));
      if (_related != null)
      {
        iCalParameterV param = new iCalParameterV((short) iCalParameterKind.ICAL_RELATED_PARAMETER);
        param.setValue(new iCalIntV(_related));
        property.addParam(param);
      }
      res.add(property);
    }
    if (_delayPeriod != null)
    {
      property = new iCalPropertyV(iCalPropertyKind.ICAL_DURATION_PROPERTY);
      property.setValue(new iCalDurationV(new Integer(_delayPeriod.intValue())));
      res.add(property);
    }

    if (_repeat != null)
    {
      property = new iCalPropertyV(iCalPropertyKind.ICAL_REPEAT_PROPERTY);
      property.setValue(new iCalIntV(_repeat));
      res.add(property);
    }
    return res;
  }

  public void addAnyProperty(iCalPropertyV prop)
  {
    int propKind = (int) prop.getKind();
    if (iCalPropertyKind.ICAL_TRIGGER_PROPERTY == propKind)
    {
      iCalValueV value = prop.getValue();
      if (value instanceof iCalDateV)
        _startDt = ((iCalDateV) prop.getValue()).getTime();
      else
      {
        _startDuration = new Long(((iCalDurationV) prop.getValue()).getIntValue());
        iCalParameterV param = prop.getParam(iCalParameterKind.ICAL_RELATED_PARAMETER);
        if (param != null)
          _related = new Integer(((iCalIntV) param.getValue()).getIntValue());
      }
    }
    else if (iCalPropertyKind.ICAL_DURATION_PROPERTY == propKind)
    {
      _delayPeriod = new Long(((iCalDurationV) prop.getValue()).getIntValue());
    }

    else if (iCalPropertyKind.ICAL_REPEAT_PROPERTY == propKind)
    {
      _repeat = new Integer(((iCalIntV) prop.getValue()).getIntValue());
    }
  }

  public void setPseudoParent(Long  eventUid)
  {
    setParentKind(iCalEvent.KIND_EVENT);
    setParentUid(eventUid);
    setIsPseudoParent(Boolean.TRUE);
    if (getRelated() == null)
    {
      setRelated(new Integer(IRelated.START));
    }
  }

  public void setIncludeParentStartTime(Boolean _includeParentStartTime)
  {
    this._includeParentStartTime = _includeParentStartTime;
  }

  public Boolean getIncludeParentStartTime()
  {
    return _includeParentStartTime;
  }

}
