/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FieldMetaInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 24 2002    Neo Sok Lay         Enhance to add 2 more fields for
 *                                    presentation requirements:
 *                                    - Presentation
 *                                    - Constraints
 *                                    Deprecate Length,Displayable,Editable and
 *                                    Mandatory fields.
 * Dec 23 2002    Neo Sok Lay         Add isEntity() to check if the field is
 *                                    referring to an entity.
 */
//value object
package com.gridnode.pdip.framework.db.meta;

import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;

/**
 * A meta data representation used for mapping between a data entity field
 * and its data object field or persistent field representation.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I1
 */
public class FieldMetaInfo
  implements Comparable, Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8124859002331827170L;
	private boolean _displayable = false;
  private boolean _editable    = false;
  private boolean _mandatory   = false;
  private boolean _proxy       = false;
  private int     _length      = 0;
  private String  _label       = "";
  private String  _valueClass  = "";
  private Number _fieldId     = null;
  private String  _sqlName     = "";
  private String  _oqlName     = "";
  private String  _fieldName   = "";
  private String _objName      = "";
  private int    _sequence     = 999;
  private String _entityName   = "";

  //converted properties from the string counterparts.
  private Properties _presentation = new Properties();
  private Properties _constraints  = new Properties();

  //private EntityMetaInfo _entityMeta;

  public FieldMetaInfo()
  {
  }

  // ********************** Methods from FieldMetaInfoBean ******************

  /**
   * @deprecated As of 2.0 I4. Replaced by Presentation field
   */
  public boolean isDisplayable()
  {
    return _displayable;
  }

  /**
   * @deprecated As of 2.0 I4. Replaced by Presentation field
   */
  public boolean isEditable()
  {
    return _editable;
  }

  /**
   * @deprecated As of 2.0 I4. Replaced by Presentation field
   */
  public boolean isMandatory()
  {
    return _mandatory;
  }

  public boolean isProxy()
  {
    return _proxy;
  }

  public String  getLabel()
  {
    return _label;
  }

  /**
   * @deprecated As of 2.0 I4. Replaced by Presentation field
   */
  public int     getLength()
  {
    return _length;
  }

  public String   getValueClass()
  {
    return _valueClass;
  }

  public Number getFieldId()
  {
    return _fieldId;
  }

  /**
   * Get the name of this field in sql database.
   *
   * @return Name of the field in database if it is persistable
   *
   */
  public String  getSqlName()
  {
    return _sqlName;
  }

  /**
   * Get the name of this field in JDO.
   *
   * @return Name of the field as known to JDO
   *
   */
  public String  getOqlName()
  {
    return _oqlName;
  }

  /**
   * Get the name of this field declared in its entity.
   *
   * @return Name of the field as declared in the entity
   *
   */
  public String  getObjectName()
  {
    return _objName;
  }

  /**
   * Get the name of the field in the entity that gives the fieldId for this
   * field.
   *
   * @return Name of the field that gives its FieldId
   *
   */
  public String  getFieldName()
  {
    return _fieldName;
  }

  /**
   * Get the display order of this field in listing view.
   *
   * @return The display order of the field in the entity if isDisplayable
   * returns <B>true</B>.
   *
   */
  public int  getSequence()
  {
    return _sequence;
  }

  public String getEntityName()
  {
    return _entityName;
  }

  /**
   * Get the presentation properties for the field.
   *
   * @return The presentation properties for the field. This method guarantees
   * the returned Properties is not null.
   *
   * @since 2.0 I4.
   */
  public Properties getPresentation()
  {
    return _presentation;
  }

  /**
   * Get the presentation properties for the field in string format.
   *
   * @return The properties string format of presentation properties.
   *
   * @since 2.0 I4.
   */
  public String getPresentationStr()
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      getPresentation().store(baos, null);
      baos.close();
      return baos.toString();
    }
    catch (Exception ex)
    {

    }
    return null;
  }
  /**
   * Get the constraint properties for the field.
   *
   * @return The constraint properties on the values of the field. This method
   * guarantees that the returned Properties is not null.
   *
   * @since 2.0 I4.
   */
  public Properties getConstraints()
  {
    return _constraints;
  }

  /**
   * Get the constraint properties for the field in string format.
   *
   * @return The properties string format of constraint properties.
   *
   * @since 2.0 I4.
   */
  public String getConstraintsStr()
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      getConstraints().store(baos, null);
      baos.close();
      return baos.toString();
    }
    catch (Exception ex)
    {

    }
    return null;
  }

  /**
   * Set whether the field is displayable in list view.
   *
   * @param displayable <B>true</B> to display this field in listing view,
   * <B>false</B> otherwise.
   *
   */
  public void    setDisplayable(boolean displayable)
  {
    _displayable = displayable;
  }

  /**
   * Set whether the field can be updated by the user.
   *
   * @param editable <B>true</B> to allow user to update this field,
   * <B>false</B> otherwise.
   *
   */
  public void    setEditable(boolean editable)
  {
    _editable = editable;
  }

  /**
   * Set whether the field must be provided by the user.
   *
   * @param mandatory <B>true</B> to force use to provide value for this field
   * on add or update.
   *
   * @since 1.0a build 0.9.9.6
   */
  public void    setMandatory(boolean mandatory)
  {
    _mandatory = mandatory;
  }

  /**
   * Set whether the field is excluded in a "proxy" retrieval.
   *
   * @param proxy <B>true</B> to exclude this field during "proxy" retrieval.
   *
   */
  public void    setProxy(boolean proxy)
  {
    _proxy = proxy;
  }

  /**
   * Set the label for the field.
   *
   * @param label The display label for this field.
   */
  public void    setLabel(String label)
  {
    _label = label;
  }

  /**
   * Set the maximum length of the field.
   *
   * @param length The maximum length.
   *
   */
  public void    setLength(int length)
  {
    _length = length;
  }

  /**
   * Set the display order of the field in the entity.
   *
   * @param sequence Order of this field in listing view.
   */
  public void     setSequence(int sequence)
  {
    _sequence = sequence;
  }

  /**
   * Sets the name of the field that provides the FieldId for this field.
   *
   * @param fieldName The "name" of the FieldId
   *
   */
  public void    setFieldName(String fieldName)
  {
    _fieldName = fieldName;
  }

  /**
   * Sets the class name of the value type of this field.
   *
   * @param valueClass The class name of the value type
   *
   */
  public void    setValueClass(String valueClass)
  {
    _valueClass = valueClass;
  }

  /**
   * Sets the name of this field in the database.
   *
   * @param sqlName The name to set
   *
   */
  public void    setSqlName(String sqlName)
  {
    _sqlName = sqlName;
  }

  /**
   * Sets the name that this field is represented in JDO context.
   *
   * @param oqlName The name to set
   */
  public void    setOqlName(String oqlName)
  {
    _oqlName = oqlName;
  }

  /**
   * Sets the name of this field as declared in its entity class.
   *
   * @param objName The name to set
   *
   */
  public void    setObjectName(String objName)
  {
    _objName = objName;
  }

  /**
   * Sets the FieldId for this field.
   *
   * @param fieldId The FieldId to set
   */
  public void    setFieldId(Number fieldId)
  {
    _fieldId = fieldId;
  }

  public void setEntityName(String entityName)
  {
    _entityName = entityName;
  }

  /**
   * Sets the meta info for the entity that this field belongs to.
   *
   * @param entityMetaInfo The meta info to set
   *
   * @since 1.0a build 0.9.9.6
   */
//  public void setEntityMetaInfo(EntityMetaInfo entityMetaInfo)
//  {
//    _entityMeta = entityMetaInfo;
//  }

  /**
   * Get the meta info of the entity of this field.
   *
   * @return The meta info of the entity that this field belongs to
   *
   * @since 1.0a build 0.9.9.6
   */
//  public EntityMetaInfo getEntityMetaInfo()
//  {
//    return _entityMeta;
//  }

  /**
   * Set the presentation properties for the field.
   *
   * @param presentationStr The properties string for Presentation field.
   *
   * @since 2.0 I4.
   */
  public void setPresentation(String presentationStr)
  {
    try
    {
      getPresentation().clear();
      if (presentationStr != null)
        getPresentation().load(
          new ByteArrayInputStream(presentationStr.getBytes()));
    }
    catch (Exception ex)
    {

    }
  }

  /**
   * Set the presentation properties for the field.
   *
   * @param presentationStr The properties string for Presentation field.
   *
   * @since 2.0 I4.
   */
  public void setPresentation(Properties presentation)
  {
    getPresentation().clear();
    if (presentation != null)
      getPresentation().putAll(presentation);
  }

  /**
   * Set the constraint properties for the field.
   *
   * @param constraintsStr The properties string for the Constraints
   * field.
   *
   * @since 2.0 I4.
   */
  public void setConstraints(String constraintsStr)
  {
    try
    {
      getConstraints().clear();
      if (constraintsStr != null)
        getConstraints().load(
          new ByteArrayInputStream(constraintsStr.getBytes()));
    }
    catch (Exception ex)
    {

    }

  }

  /**
   * Set the constraint properties for the field.
   *
   * @param constraints The properties for the Constraints
   * field.
   *
   * @since 2.0 I4.
   */
  public void setConstraints(Properties constraints)
  {
    try
    {
      getConstraints().clear();
      if (constraints != null)
        getConstraints().putAll(constraints);
    }
    catch (Exception ex)
    {

    }

  }

  /**
   * Checks if the field is an Entity. This method examines the Constraints
   * field and determines that the type is "embedded" or "foreign". If type is
   * "embedded", the field is an Entity. If type is "foreign", the "foreign.cached"
   * property is examined. If true, the field is an Entity.<p>
   * Note that if "collection" is true, the field is not considered as an Entity.
   *
   * @returns <b>true</b> if the Constraints contains the properties defined
   * as above, <b>false</b> otherwise.
   */
  public boolean isEntity()
  {
    boolean isEntity = false;
    Properties constraints = getConstraints();
    if (constraints != null)
    {
      String isCollection = constraints.getProperty("collection");

      // ensure that it is not a collection
      if (isCollection == null || !Boolean.valueOf(isCollection).booleanValue())
      {
        String type = constraints.getProperty("type");
        if ("embedded".equals(type))
          isEntity = true;
        else if ("foreign".equals(type))
        {
          String cached = constraints.getProperty("foreign.cached");
          isEntity = (cached != null && Boolean.valueOf(cached).booleanValue());
        }
      }
    }

    return isEntity;
  }

  /**
   * Gives a description of this field meta info object.
   *
   * @return A printable representation of this meta info
   *
   * @since 1.0a build 0.9.9.6
   */
  public String toString()
  {
    String lineSep = System.getProperty("line.separator");

    return "Field:              " + getObjectName() + lineSep +
           "  Name:             " + getFieldName() + lineSep +
           "  Id:               " + getFieldId() + lineSep +
           "  SqlName:          " + getSqlName() + lineSep +
           "  Class:            " + getValueClass() + lineSep +
           "  Label:            " + getLabel() + lineSep +
           //"  Disp:             " + isDisplayable() + lineSep +
           //"  Edit:             " + isEditable() + lineSep +
           //"  Mand:             " + isMandatory() + lineSep +
           "  Proxy:            " + isProxy() + lineSep +
           "  Seq:              " + getSequence() + lineSep +
           "  Presentation:     " + getPresentation() + lineSep +
           "  ConstraintDetail: " + getConstraints() + lineSep +
           "  Entity:           " + getEntityName();
  }

  // ******************* Methods from Comparable ************************
  /**
   * Determine the order of this field as compared to other fields in
   * the same entity.
   *
   * @see Comparable#compareTo
   *
   * @since 1.0a build 0.9.9.6
   */
  public int compareTo(Object obj)
  {
    if (obj instanceof FieldMetaInfo)
    {
      FieldMetaInfo other = (FieldMetaInfo)obj;
      return (getSequence() - other.getSequence());
    }
    throw new ClassCastException();
  }
}