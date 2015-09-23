/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultSearchRegistryQueryEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

class DefaultSearchRegistryQueryEntity
  extends AbstractGTEntity
  implements IGTSearchRegistryQueryEntity
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