/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2009 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveListAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2009-02-11     Ong Eu Soon         Created
 * 2009-04-11     Tam Wei Xiang       #122 - Remove ARCHIVE_NAME
 */
package com.gridnode.gtas.client.web.archive;

import com.gridnode.gtas.client.ctrl.*;
import com.gridnode.gtas.client.web.strutsbase.*;
import com.gridnode.gtas.client.GTClientException;

public class ArchiveListAction extends EntityListAction
{
  protected String getNavgroup(com.gridnode.gtas.client.web.strutsbase.ActionContext actionContext)
    throws com.gridnode.gtas.client.GTClientException
  {
    return "navgroup_server";
  }

  protected Object[] getColumnReferences(ActionContext actionContext)
    throws GTClientException
  {
    Object[] columns = {  IGTArchiveEntity.ARCHIVE_ID,
        IGTArchiveEntity.ARCHIVE_DESCRIPTION,
        IGTArchiveEntity.ARCHIVE_TYPE,
        IGTArchiveEntity.ENABLE_SEARCH_ARCHIVED,
        IGTArchiveEntity.ENABLE_RESTORE_ARCHIVED,
        IGTArchiveEntity.IS_ARCHIVE_FREQUENCY_ONCE,
        IGTArchiveEntity.ARCHIVE_OLDER_THAN,
        IGTArchiveEntity.INCLUDE_INCOMPLETE_PROCESSES,
                       };
    return columns;
  }

  protected int getManagerType(ActionContext actionContext)
  {
    return IGTManager.MANAGER_ARCHIVE;
  }

  protected String getResourcePrefix(ActionContext actionContext)
  {
    return IGTEntity.ENTITY_ARCHIVE;
  }
  
}
