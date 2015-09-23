/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultSearchGridNodeQueryEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-14     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

class DefaultSearchGridNodeQueryEntity extends AbstractGTEntity
  implements IGTSearchGridNodeQueryEntity
{
  public boolean canEdit() throws GTClientException
  {
    // Once submitted we dont allow edits. (Later may provide a duplication feature but not today)
    return isNewEntity();
  }

  public boolean canDelete() throws GTClientException
  {
    // For now we arent providing the ability to explicitly delete. Anyhow it will vanish when the
    // session is terminated as it is not persisted.
    return false;
  }
}