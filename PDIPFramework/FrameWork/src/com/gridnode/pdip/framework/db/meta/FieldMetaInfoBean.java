/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FieldMetaInfoBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 24 2002    Neo Sok Lay         Enhance to add 4 more fields for
 *                                    presentation requirements:
 *                                    - CollectionValue
 *                                    - Presentation
 *                                    - ConstraintType
 *                                    - ConstraintDetail
 *                                    Deprecate Length,Displayable,Editable and
 *                                    Mandatory fields.
 * Feb 07 2007		Alain Ah Ming				Log warning message if throwing up exception                                    
 */
package com.gridnode.pdip.framework.db.meta;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;

import com.gridnode.pdip.framework.db.keygen.KeyGen;
import com.gridnode.pdip.framework.log.Log;
 
/**
 * Entity bean to manage Field meta info records.
 *
 * @author Mahesh
 * @author Neo Sok Lay
 *
 * @version GT 4.0 VAN
 * @since 2.0 I1
 */
public abstract class FieldMetaInfoBean implements EntityBean
{
  private EntityContext _ctx = null;
  private Number _fieldId = null;

  public void setEntityContext(EntityContext ctx)
  {
    _ctx = ctx;
  }

  public void unsetEntityContext()
  {
    _ctx = null;
  }

  public Long ejbCreate(FieldMetaInfo data) throws CreateException
  {
    //setUId(); generate new Id
    try
    {
      //Long uid = new Long((((IKeyGeneratorHome)new InitialContext().lookup(
      //             IKeyGeneratorHome.class.getName())).findByPrimaryKey("fieldmetainfo")).getNextId());

      Long uid = KeyGen.getNextId("fieldmetainfo", true);
      setUId(uid);
    }
    catch(Exception e)
    {
      Log.warn(Log.DB,"Error in getting UId in ejbCreate of FieldMetaInfoBean ",e);
      throw new CreateException(e.toString());
    }

    setEntityName(data.getEntityName());
    setDisplayable(data.isDisplayable());
    setFieldName(data.getFieldName());
    setEditable(data.isEditable());
    setLabel(data.getLabel());
    setLength(data.getLength());
    setMandatory(data.isMandatory());
    setProxy(data.isProxy());
    setSequence(data.getSequence());
    setObjectName(data.getObjectName());
    setOqlName(data.getOqlName());
    setSqlName(data.getSqlName());
    setValueClass(data.getValueClass());
    setConstraints(data.getConstraintsStr());
    setPresentation(data.getPresentationStr());

    return null;
  }

  public void ejbPostCreate(FieldMetaInfo data)
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  public void ejbLoad()
  {
  }

  public void ejbStore()
  {
  }

  public abstract Long getUId();

  /**
   * Whether the field is to be displayed in listing view.
   *
   * @return <B>true</B> if field can be displayed in listing view, <B>false</B>
   * otherwise.
   */
  public abstract boolean getDisplayable();
  public boolean isDisplayable()
  {
    return getDisplayable();
  }


  /**
   * Whether the field can be updated by user.
   *
   * @return <B>true</B> if user is allowed to update this field, <B>false</B>
   * otherwise.
   *
   */
  public abstract boolean getEditable();
  public boolean isEditable()
  {
    return getEditable();
  }

  /**
   * Whether the field must be provided by user.
   *
   * @return <B>true</B> if user must provide value for this field, <B>false</B>
   * otherwise.
   *
   */
  public abstract boolean getMandatory();
  public boolean isMandatory()
  {
    return getMandatory();
  }

  /**
   * Whether the field will be excluded in a "proxy" retrieval.
   *
   * @return <B>true</B> if this field will be excluded in "proxy" retrieval.
   *
   */
  public abstract boolean getProxy();
  public boolean isProxy()
  {
    return getProxy();
  }

  /**
   * Get the display label for this field.
   *
   * @return The label for the field if isDisplayable() returns <B>true</B>
   *
   */
  public abstract String  getLabel();

  /**
   * Get the maximum length for this field on input.
   *
   * @return The maximum length for the field
   *
   */
  public abstract int getLength();

  /**
   * Get the class name of the value of this field.
   *
   * @return The class name of the field value type
   *
   */
  public abstract String  getValueClass();

  /**
   * Get the id of this field in its entity.
   *
   * @return Unique Id of the field in the entity
   *
   */
  public Number  getFieldId()
  {
    return _fieldId;
  }


  public abstract String getEntityName();

  public abstract String  getSqlName();

  public abstract String  getOqlName();

  public abstract String  getObjectName();

  public abstract String  getFieldName();

  public abstract int getSequence();

  /**
   * Get the presentation properties for the field.
   *
   * @return The presentation properties for the field in a form of String.
   *
   * @since 2.0 I4.
   */
  public abstract String getPresentation();

  /**
   * Get the constraint properties for the field.
   *
   * @return The constraint properties on the values of the field in a form of
   * String.
   *
   * @since 2.0 I4.
   */
  public abstract String getConstraints();

  public abstract void setUId(Long uid);

  public abstract void setEntityName(String entityName);

  public abstract void setDisplayable(boolean displayable);

  public abstract void setEditable(boolean editable);

  public abstract void setMandatory(boolean mandatory);

  public abstract void setProxy(boolean proxy);

  public abstract void setLabel(String label);

  public abstract void setLength(int length);

  public abstract void  setSequence(int sequence);

  public abstract void setFieldName(String fieldName);

  public abstract void setValueClass(String valueClass);

  public abstract void setSqlName(String sqlName);

  public abstract void setOqlName(String oqlName);

  public abstract void setObjectName(String objName);

  /**
   * Set the presentation properties for the field.
   *
   * @param presentation The properties string for Presentation field.
   *
   * @since 2.0 I4.
   */
  public abstract void setPresentation(String presentation);

  /**
   * Set the constraint properties for the field.
   *
   * @param constraints The properties string for the Constraints
   * field.
   *
   * @since 2.0 I4.
   */
  public abstract void setConstraints(String constraints);

  public FieldMetaInfo getData()
  {
    FieldMetaInfo data = new FieldMetaInfo();
    data.setDisplayable(isDisplayable());
    data.setEditable(isEditable());
    data.setEntityName(getEntityName());
    data.setFieldName(getFieldName());
    data.setLabel(getLabel());
    data.setLength(getLength());
    data.setMandatory(isMandatory());
    data.setObjectName(getObjectName());
    data.setOqlName(getOqlName());
    data.setProxy(isProxy());
    data.setSequence(getSequence());
    data.setSqlName(getSqlName());
    data.setValueClass(getValueClass());
    data.setConstraints(getConstraints());
    data.setPresentation(getPresentation());

    return data;
  }
}