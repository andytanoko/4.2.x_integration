package com.gridnode.pdip.app.user.login;

import com.gridnode.pdip.base.session.model.SessionData;
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AuthSubject.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 05 2002    Neo Sok Lay         Created
 */
import javax.security.auth.Subject;

import java.util.Collection;

/**
 * A wrapper object for storing a Subject to the session.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class AuthSubject extends SessionData
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3846951459921024668L;
	public static final String KEY = "SignOnSubject";

  //private byte[] _contents; 

  /**
   * Construct an AuthSubject with no Subject.
   */
  public AuthSubject()
  {
    super(KEY, null);
  }

  /**
   * Construct an AuthSubject for a Subject.
   *
   * @param subject The subject to wrap around.
   */
  public AuthSubject(Subject subject)
  {
    super(KEY, subject.getPrincipals());
  }

  public AuthSubject(SessionData sessionData)
  {
    super(sessionData);
  }

  /**
   * Get the subject wrapped around by this AuthSubject.
   *
   * @return The Subject wrapped around by this AuthSubject. Note that the
   * subject returned is not the "same" copy that was originally used for
   * constructing this AuthSubject. Only the Principals are retained in the
   * returned Subject.
   */
  public Subject getSubject()
  {
    Subject subject = new Subject();

    Object data = getData();
    if (data != null && data instanceof Collection)
      subject.getPrincipals().addAll((Collection)data);

    return subject;
  }
}