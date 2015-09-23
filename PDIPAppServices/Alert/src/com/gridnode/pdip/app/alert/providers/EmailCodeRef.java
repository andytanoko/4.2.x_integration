/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmailCodeRef.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 16 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.alert.providers;

import java.io.File;

import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.helpers.ITempDirConfig;
import com.gridnode.pdip.framework.db.DataObject;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

/**
 * An EmailCodeRef is a Reference object that can be used to lookup the mappings
 * of Email Codes configured in the application.
 * <P>
 * The EmailCodeRef object is obtained from a "emailcode-ref.xml" file. The
 * object mapping of this object is defined in the "emailcode-ref.map" file.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class EmailCodeRef extends DataObject
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1302273133223594377L;

	/**
   * "emailcode-ref.xml" filename
   */
  public static String EMAIL_CODE_REF = "emailcode-ref.xml";

  private EmailCode[] _codes;

  public EmailCodeRef()
  {
  }

  public EmailCode[] getEmailCodes()
  {
    return _codes;
  }

  public void setEmailCodes(EmailCode[] emailCodes)
  {
    _codes = emailCodes;
  }

  /**
   * Get an EmailCode using the specified code.
   *
   * @param code The code of the Email code mapping.
   * @return The EmailCode with the specified code, or <B>null</b> if none exists.
   */
  public EmailCode getEmailCode(String code)
  {
    EmailCode emailCode = null;
    if (_codes != null)
    {
      for (int i=0; i<_codes.length; i++)
      {
        if (_codes[i].getId().equals(code))
        {
          emailCode = _codes[i];
          break;
        }
      }
    }

    return emailCode;
  }

  /**
   * Loads an EmailCodeRef from the <code>EMAIL_CODE_REF</code> file.
   *
   * @return The loaded EmailCodeRef object. If error occurs while loading,
   * an empty EmailCodeRef object will be returned.
   */
  public static EmailCodeRef load()
  {
    EmailCodeRef ref = new EmailCodeRef();
    try
    {
      File file = FileUtil.getFile(
                    ITempDirConfig.EMAIL_DIR,
                    EMAIL_CODE_REF);

      return (EmailCodeRef)ref.deserialize(
                               file.getAbsolutePath());
    }
    catch (Throwable t)
    {
      AlertLogger.warnLog("EmailCodeRef", "load", t.getMessage());
    }
    return ref;
  }
}

