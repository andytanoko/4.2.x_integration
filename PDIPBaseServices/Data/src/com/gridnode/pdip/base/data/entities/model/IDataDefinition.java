/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 25 2002   MAHESH              Created
 */
package com.gridnode.pdip.base.data.entities.model;


import java.util.*;


public interface IDataDefinition {

    public static final Number UID = new Integer(0);  //Long

    public static final Number NAME = new Integer(1);  //String;

    public long getUId();

    public String getName();

    public Map getDataLocationKeys();
}
