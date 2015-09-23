/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultProcedureDefFileEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 * 2003-07-16     Andrew Hill         listClassesInJar() convienience method
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;

class DefaultProcedureDefFileEntity
  extends AbstractGTEntity
  implements IGTProcedureDefFileEntity
{
  
  public Collection listClassesInJar() throws GTClientException
  {
    if( isNewEntity() )
    {
      throw new UnsupportedOperationException("Reading of classnames is not supported prior to saving of entity");
    }
    else
    {
      return ((IGTProcedureDefFileManager)getSession().getManager(getType())).listClassesInJar(getUidLong());
    }
  }

}