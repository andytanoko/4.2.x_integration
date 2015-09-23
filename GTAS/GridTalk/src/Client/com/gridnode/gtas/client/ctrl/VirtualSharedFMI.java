/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: VirtualSharedFMI.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-02     Andrew Hill         Created
 * 2002-10-25     Andrew Hill         Implement IGTFieldMetaInfo (which is now shared)
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;

/**
 * For use with virtual fields
 */
class VirtualSharedFMI implements IGTFieldMetaInfo
{
  protected static final IConstraint _defaultConstraint = new TextConstraint(0,0);

  protected Number _fieldId;
  protected String _label;
  protected String _fieldName;

  protected int _length = 0;
  protected boolean _isDisplayableCreate = true;
  protected boolean _isDisplayableUpdate = true;
  protected boolean _isEditableCreate = true;
  protected boolean _isEditableUpdate = true;
  protected boolean _isMandatoryCreate = false;
  protected boolean _isMandatoryUpdate = false;
  protected String _valueClass = "java.lang.String";
  protected boolean _isCollection = false;
  protected String _elementClass = _valueClass;
  protected int _constraintType = IConstraint.TYPE_TEXT;
  protected IConstraint _constraint = _defaultConstraint;
  protected boolean _isAvailableInCache = true;

  VirtualSharedFMI(String label, Number fieldId)
    throws GTClientException
  {
    _fieldId = fieldId;
    _label = label;
    _fieldName = DefaultSharedFMI.extractFieldName(label);
  }

  public Number getFieldId()
  {
    return _fieldId;
  }

  public String getFieldName()
  {
    return _fieldName;
  }

  public String getLabel()
  {
    return _label;
  }

  public int getLength()
  {
    if(_constraintType == IConstraint.TYPE_TEXT)
    {
      return ((ITextConstraint)_constraint).getMaxLength();
    }
    else
    {
      return _length;
    }
  }

  public boolean isDisplayableCreate()
  {
    return _isDisplayableCreate;
  }

  void setDisplayableCreate(boolean flag)
  {
    _isDisplayableCreate = flag;
  }

  public boolean isDisplayableUpdate()
  {
    return _isDisplayableUpdate;
  }

  void setDisplayableUpdate(boolean flag)
  {
    _isDisplayableUpdate = flag;
  }

  public boolean isEditableCreate()
  {
    return _isEditableCreate;
  }

  void setEditableCreate(boolean flag)
  {
    _isEditableCreate = flag;
  }

  public boolean isEditableUpdate()
  {
    return _isEditableUpdate;
  }

  void setEditableUpdate(boolean flag)
  {
    _isEditableUpdate = flag;
  }

  public boolean isMandatoryCreate()
  {
    return _isMandatoryCreate;
  }

  void setMandatoryCreate(boolean flag)
  {
    _isMandatoryCreate = flag;
  }

  public boolean isMandatoryUpdate()
  {
    return _isMandatoryUpdate;
  }

  void setMandatoryUpdate(boolean flag)
  {
    _isMandatoryUpdate = flag;
  }

  public String getValueClass()
  {
    return _valueClass;
  }

  void setValueClass(String value)
  {
    if(!isCollection())
    {
      _elementClass = value;
    }
    _valueClass = value;
  }

  public boolean isCollection()
  {
    return _isCollection;
  }

  void setCollection(boolean isCollection)
  {
    _isCollection = isCollection;
  }

  public String getElementClass()
  {
    if(_elementClass == null)
    {
      return _valueClass;
    }
    else
    {
      return _elementClass;
    }
  }

  void setElementClass(String elementClass)
  {
    if(!isCollection()) throw new java.lang.RuntimeException("Not a collection");
    _elementClass = elementClass;
  }

  public int getConstraintType()
  {
    return _constraintType;
  }

  public IConstraint getConstraint()
  {
    return _constraint;
  }

  public void setConstraint(IConstraint constraint)
  {
    if(constraint != null)
    {
      _constraintType = constraint.getType();
    }
    else
    {
      _constraintType = IConstraint.TYPE_OTHER;
    }
    _constraint = constraint;
  }

  public boolean isVirtualField()
  {
    return true;
  }

  public void setIsAvailableInCache(boolean flag)
  {
    _isAvailableInCache = flag;
  }

  public boolean isAvailableInCache()
  {
    return _isAvailableInCache;
  }

  public String toString()
  {
    return "VirtualSharedFMI[" + _fieldId + ":"
            + ", idC=" + _isDisplayableCreate
            + ", idU=" + _isDisplayableUpdate
            + ", ieC=" + _isEditableCreate
            + ", ieU=" + _isEditableUpdate
            + ", imC=" + _isMandatoryCreate
            + ", imU=" + _isMandatoryUpdate + "]";
  }

  public boolean isDisplayable(boolean newEntity)
  {
    if(newEntity)
    {
      return _isDisplayableCreate;
    }
    else
    {
      return _isDisplayableUpdate;
    }
  }

  public boolean isEditable(boolean newEntity)
  {
    if(newEntity)
    {
      return _isEditableCreate;
    }
    else
    {
      return _isEditableUpdate;
    }
  }

  public boolean isMandatory(boolean newEntity)
  {
    if(newEntity)
    {
      return _isMandatoryCreate;
    }
    else
    {
      return _isMandatoryUpdate;
    }
  }
}