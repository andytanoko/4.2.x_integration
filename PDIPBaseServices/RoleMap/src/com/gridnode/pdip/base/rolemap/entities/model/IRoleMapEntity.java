/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Mahesh              Created
 *
 */
package com.gridnode.pdip.base.rolemap.entities.model;


public interface IRoleMapEntity {

    public static final String ENTITY_NAME = "RoleMapEntity";

    /**
    * FieldId for UId. A Number.
    */
    public static final Number UID = new Integer(0);  //Integer

    public static final Number ROLE_KEY = new Integer(1);

    public static final Number PARTNER_KEY = new Integer(2);

    public static final Number PROCESSDEF_KEY = new Integer(3);

    //constants
    public static final String ROLEMAP_SELF="ROLEMAP_SELF";

}