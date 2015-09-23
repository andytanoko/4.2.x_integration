/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultSharedFMI.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-25     Andrew Hill         Created.
 * 2002-10-25     Andrew Hill         Implement IGTFieldMetaInfo (which is now shared)
 * 2002-11-01     Daniel D'Cotta      Added support for dynamic entities
 * 2007-02-06     Chong SoonFui       Check on null label in extractFieldName(String)
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.GTClientException;

import java.util.Properties;

/**
 * Shared metaInfo class for a single entity field.
 */
class DefaultSharedFMI implements IGTFieldMetaInfo
{
  protected static final String GTAS_CTYPE_OTHER          = "other";
  protected static final String GTAS_CTYPE_TEXT           = "text";
  protected static final String GTAS_CTYPE_ENUMERATED     = "enum";
  protected static final String GTAS_CTYPE_RANGE          = "range";
  protected static final String GTAS_CTYPE_LOCAL_ENTITY   = "embedded";
  protected static final String GTAS_CTYPE_DYNAMIC_ENTITY = "dynamic";
  protected static final String GTAS_CTYPE_FOREIGN_ENTITY = "foreign";
  protected static final String GTAS_CTYPE_FILE           = "file";
  protected static final String GTAS_CTYPE_UID            = "uid";
  protected static final String GTAS_CTYPE_TIME           = "datetime";

  protected Number _fieldId;
  protected FieldMetaInfo _gtasFmi;

  protected boolean _isDisplayableCreate;
  protected boolean _isDisplayableUpdate;

  protected boolean _isEditableCreate;
  protected boolean _isEditableUpdate;

  protected boolean _isMandatoryCreate;
  protected boolean _isMandatoryUpdate;

  protected boolean _isCollection;
  protected String _elementClass;

  protected boolean _isAvailableInCache;

  protected int _length = 0;

  protected String _fieldName;

  protected int _constraintType;

  protected IConstraint _constraint;

  public DefaultSharedFMI(FieldMetaInfo gtasFmi)
    throws GTClientException
  {
    _fieldId = gtasFmi.getFieldId();
    _gtasFmi = gtasFmi;
    _fieldName = extractFieldName(gtasFmi.getLabel());
    extractDetails();
  }

  public String toString()
  {
    String label = _gtasFmi == null ? "null _gtasFmi" : _gtasFmi.getLabel();
    String fieldName = _gtasFmi == null ? "null _gtasFmi" : _gtasFmi.getFieldName();
    return "DefaultSharedFMI[{" + fieldName + "}"
            + _fieldId + "(" + label + "):"
            + ", idC=" + _isDisplayableCreate
            + ", idU=" + _isDisplayableUpdate
            + ", ieC=" + _isEditableCreate
            + ", ieU=" + _isEditableUpdate
            + ", imC=" + _isMandatoryCreate
            + ", imU=" + _isMandatoryUpdate + "]";
  }

  void extractDetails()
    throws GTClientException
  {
    Properties presentation = _gtasFmi.getPresentation();
    Properties detail = _gtasFmi.getConstraints();
    _isCollection = makeBoolean(detail.getProperty("collection","false"));
    _isAvailableInCache = makeBoolean(detail.getProperty("isAvailableInCache","true"));
    if(_isCollection)
    {
      _elementClass = presentation.getProperty("collection.element","java.lang.Object");
    }
    else
    {
      _elementClass = _gtasFmi.getValueClass();
      if(_elementClass == null)
      {
        throw new java.lang.NullPointerException("null valueClass for field");
      }
    }
    String cType = detail.getProperty("type","other");
    _constraintType = convertConstraintType(cType, detail);

    boolean isDisplayable = isAbleMode("displayable", null, presentation, true);
    _isDisplayableCreate = isAbleMode("displayable", "create", presentation, isDisplayable);
    _isDisplayableUpdate = isAbleMode("displayable", "update", presentation, isDisplayable);

    boolean isEditable = isAbleMode("editable", null, presentation, true);
    _isEditableCreate = isAbleMode("editable", "create", presentation, isEditable);
    _isEditableUpdate = isAbleMode("editable", "update", presentation, isEditable);


    boolean isMandatory = isAbleMode("mandatory", null, presentation, false);
    _isMandatoryCreate = isAbleMode("mandatory", "create", presentation, isMandatory);
    _isMandatoryUpdate = isAbleMode("mandatory", "update", presentation, isMandatory);
  }

  private boolean isAbleMode(String setting, String mode, Properties presentation, boolean def)
  {
    if(mode != null)
    {
      if(!"".equals(mode))
      {
        mode = "." + mode;
      }
    }
    else
    {
      mode = "";
    }
    String ableMode = presentation.getProperty(setting + mode,"" + def);
    return makeBoolean(ableMode);
  }


  private int convertConstraintType(String constraintType, Properties detail)
    throws GTClientException
  {
    if(GTAS_CTYPE_TEXT.equals(constraintType))
    {
      _constraint = new TextConstraint(detail);
      return IConstraint.TYPE_TEXT;
    }

    if(GTAS_CTYPE_ENUMERATED.equals(constraintType))
    {
      _constraint = new EnumeratedConstraint(detail);
      return IConstraint.TYPE_ENUMERATED;
    }

    if(GTAS_CTYPE_RANGE.equals(constraintType))
    {
      _constraint = new SingleRangeConstraint(detail, _elementClass);
      return IConstraint.TYPE_RANGE;
    }

    if(GTAS_CTYPE_LOCAL_ENTITY.equals(constraintType))
    {
      _constraint = new LocalEntityConstraint(detail);
      return IConstraint.TYPE_LOCAL_ENTITY;
    }

    if(GTAS_CTYPE_FOREIGN_ENTITY.equals(constraintType))
    {
      _constraint = new ForeignEntityConstraint(detail);
      return IConstraint.TYPE_FOREIGN_ENTITY;
    }

    if(GTAS_CTYPE_FILE.equals(constraintType))
    {
      _constraint = new FileConstraint(detail);
      return IConstraint.TYPE_FILE;
    }

    if(GTAS_CTYPE_UID.equals(constraintType))
    {
      _constraint = new UidConstraint(detail);
      return IConstraint.TYPE_UID;
    }

    if(GTAS_CTYPE_TIME.equals(constraintType))
    {
      _constraint = new TimeConstraint(detail);
      return IConstraint.TYPE_TIME;
    }

    if(GTAS_CTYPE_DYNAMIC_ENTITY.equals(constraintType))
    {
      _constraint = new DynamicEntityConstraint(detail);
      return IConstraint.TYPE_DYNAMIC_ENTITY;
    }

    if(GTAS_CTYPE_OTHER.equals(constraintType))
    {
      _constraint = null;
      return IConstraint.TYPE_OTHER;
    }

    throw new java.lang.IllegalArgumentException("Unrecognised constraint type \""
                                                  + constraintType + "\"");
  }

  // 20031009 DDJ: added this package protected method
  FieldMetaInfo getGtasFmi()
  {
    return _gtasFmi;
  }

  public int getConstraintType()
  {
    return _constraintType;
  }

  public IConstraint getConstraint()
  {
    return _constraint;
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
    return _gtasFmi.getLabel();
  }

  public boolean isDisplayableCreate()
  {
    return _isDisplayableCreate;
  }

  public boolean isDisplayableUpdate()
  {
    return _isDisplayableUpdate;
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

  public boolean isEditableCreate()
  {
    return _isEditableCreate;
  }

  public boolean isEditableUpdate()
  {
    return _isEditableUpdate;
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

  public boolean isMandatoryCreate()
  {
    return _isMandatoryCreate;
  }

  public boolean isMandatoryUpdate()
  {
    return _isMandatoryUpdate;
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

  public String getValueClass()
  {
    return _gtasFmi.getValueClass();
  }

  public String getElementClass()
  {
    return _elementClass;
  }

  public boolean isCollection()
  {
    return _isCollection;
  }

  public boolean isAvailableInCache()
  {
    return _isAvailableInCache;
  }

  /**
   * @deprecated this method is to support existing interface and should be removed asap
   */
  public int getLength()
  {
    if(_constraintType == IConstraint.TYPE_TEXT)
    {
      return ((ITextConstraint)_constraint).getMaxLength();
    }
    else
    {
      return 0;
    }
  }

  static boolean makeBoolean(String value)
  {
    if(value == null) throw new java.lang.NullPointerException("value is null");
    if("false".equalsIgnoreCase(value)) return false;
    if("true".equalsIgnoreCase(value))  return true;
    throw new java.lang.IllegalArgumentException("Not a recognised flag value:" + value);
  }

  static String extractFieldName(String label)
    throws GTClientException
  {
    try
    {
    	//20070206CSF Check on null label
    	if ( label == null ||label.equals(""))
    	{
//    		Log.debug(Log.WEB, "--- Null Label ---");
    		return "";
    	}
//    	Log.debug(Log.WEB, "Get label:" + label);
      return label.substring(label.indexOf(".",0)+1,label.length());
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to extract fieldName from label \"" + label + "\"",t);
    }
  }

  static String extractEntityName(String label)
    throws GTClientException
  {
    try
    {
      return label.substring(0,label.indexOf(".",0));
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to extract entityType from label \"" + label + "\"",t);
    }
  }

  public boolean isVirtualField()
  {
    return false;
  }
}