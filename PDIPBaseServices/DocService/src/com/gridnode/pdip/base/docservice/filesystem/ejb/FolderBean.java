/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FolderBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 8 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.ejb;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.base.docservice.filesystem.model.Folder;

public class FolderBean extends AbstractEntityBean
{

    public String getEntityName()
    {
        return Folder.ENTITY_NAME;
    }

}
