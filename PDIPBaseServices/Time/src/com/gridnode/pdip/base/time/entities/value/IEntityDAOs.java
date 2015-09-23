// %1023788049434:com.gridnode.pdip.base.time.value%
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
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.value;

import com.gridnode.pdip.base.time.entities.model.iCalDate;
import com.gridnode.pdip.base.time.entities.model.iCalInt;
import com.gridnode.pdip.base.time.entities.model.iCalProperty;
import com.gridnode.pdip.base.time.entities.model.iCalString;
import com.gridnode.pdip.base.time.entities.model.iCalText;


public interface IEntityDAOs
{
  public static final ExEntityDAOImpl _propDAO = new ExEntityDAOImpl(iCalProperty.class.getName());
  public static final ExEntityDAOImpl _intDAO = new ExEntityDAOImpl(iCalInt.class.getName());
  public static final ExEntityDAOImpl _StringDAO = new ExEntityDAOImpl(iCalString.class.getName());
  public static final ExEntityDAOImpl _DateDAO = new ExEntityDAOImpl(iCalDate.class.getName());
  public static final ExEntityDAOImpl _textDAO = new ExEntityDAOImpl(iCalText.class.getName());
}
