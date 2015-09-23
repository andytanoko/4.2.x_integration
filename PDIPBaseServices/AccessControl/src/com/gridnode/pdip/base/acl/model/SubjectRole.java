/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SubjectRole.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 15 2002    Goh Kan Mun             Created
 */
package com.gridnode.pdip.base.acl.model;

import com.gridnode.pdip.framework.db.entity.*;

/**
 * This is an object model for SubjectRole entity. A SubjectRole contains the subject-role related
 * information.<P>
 *
 * The Model:<BR><PRE>
 *   UId          - UID for a SubjectRole entity instance.
 *   Subject      - UID for Subject that is tied to the Role.
 *   Role         - UID for Role that is tied to Subject.
 *   SubjectType  - The type of Subject.
 *   Version      - Version.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 *
 */

public class SubjectRole extends AbstractEntity implements ISubjectRole
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5380977188842144268L;
	protected long _role = 0;
  protected long _subject = 0;
  protected String _subjectType = null;

  public SubjectRole()
  {
  }
  // ***************** Methods from AbstractEntity *************************

  public String getEntityDescr()
  {
    return getUId() + "/" + getRole() + "/" + getSubject() + "/" + getSubjectType();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ******************** Getters for attributes ***************************

  public long getRole()
  {
    return _role;
  }

  public long getSubject()
  {
    return _subject;
  }

  public String getSubjectType()
  {
    return _subjectType;
  }

  // ******************** Setters for attributes ***************************

  public void setRole(long role)
  {
    this._role = role;
  }

  public void setSubject(long subject)
  {
    this._subject = subject;
  }

  public void setSubjectType(String subjectType)
  {
    this._subjectType = subjectType;
  }

}