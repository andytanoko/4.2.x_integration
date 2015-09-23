/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFileHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 8 2002   Girish R         Created
 */
package com.gridnode.pdip.base.docservice.filesystem.ejb;

import com.gridnode.pdip.framework.db.entity.*;
import com.gridnode.pdip.framework.db.filter.*;

import java.util.*;

import javax.ejb.*;

public interface IFileHome extends EJBLocalHome
{

    public IFileObj create(IEntity data)
        throws CreateException;

    public IFileObj findByPrimaryKey(Long primaryKey)
        throws FinderException;

    public Collection findByFilter(IDataFilter filter)
        throws FinderException;
}
