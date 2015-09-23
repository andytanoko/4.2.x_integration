/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    Mahesh              Created
 *
 */
package com.gridnode.pdip.base.gwfbase.bpss.model;


public interface IBpssBinaryCollaborationActivity
{

    public static final String ENTITY_NAME = "BpssBinaryCollaborationActivity";

    /**
     * FieldId for UId. A Number.
     */
    public static final Number UID = new Integer(0);  //Integer

    public static final Number NAME = new Integer(1);

    public static final Number BINARY_COLLABORATION_UID = new Integer(2);

    public static final Number DOWNLINK_UID = new Integer(3);

}
