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


public interface IBpssBusinessAction
{

    /**
     * FieldId for UId. A Number.
     */
    public static final Number UID = new Integer(0);  //Integer

    public static final Number NAME = new Integer(1);  //String(10)

    public static final Number ISINTELLIGIBLE_REQUIRED = new Integer(2);  //Boolean

    public static final Number ISAUTH_REQUIRED = new Integer(3);  //Boolean

    public static final Number ISNONREPUDIATION_REQUIRED = new Integer(4);  //Boolean

    public static final Number TIMETO_ACK_RECEIPT = new Integer(5);  //String(80)

    public static final Number ISNONREPUDIATION_RECEIPT_REQUIRED = new Integer(6);  //Boolean

}
