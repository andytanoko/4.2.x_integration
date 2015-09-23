/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TypedException.java
 *
 ****************************************************************************
 * Date           Author                    Changes
 ****************************************************************************
 * Sep 05 2005    Sumedh Chalermkanjana     Created
 * Oct 05 2005    Lim Soon Hsiung           Modify to used APIParam
 * Sep 07 2006    Tam Wei Xiang             Moved from estore stream. API param is
 *                                          not required.
 */

/**
 * NOTE: Various types will be added on later.
 */
package com.gridnode.gtas.server.dbarchive.exception;

import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.gtas.server.dbarchive.helpers.EStoreHelper;;

/**
 * Exception class for estore module.  
 */
public class EStoreException extends ApplicationException {
  private String _nestedStackTrace;
  
  public EStoreException(String msg)
  {
    super(msg);
  }
  
  public EStoreException(Throwable nestedException)
  {
    super(nestedException);
  }
  
  public EStoreException(String msg, Throwable nestedException)
  {
    super(msg, nestedException);
  }

}
