/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DivMsgList.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-02     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.util.*;

public class DivMsgList extends ArrayList
{
  public DivMsgList()
  {
    super();
  }

  public DivMsgList(Object[] contents)
  { //Convienience constructor
    super(contents.length);
    for(int i=0; i<contents.length; i++)
    {
      add(contents[i]);
    }
  }
}