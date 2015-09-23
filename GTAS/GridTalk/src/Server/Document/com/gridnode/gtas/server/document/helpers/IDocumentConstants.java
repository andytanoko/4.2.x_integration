/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentConfig.java
 *
 ****************************************************************************
 * Date            Author              Changes
 ****************************************************************************
 * Mar 12 2004    Jagadeesh            Created
 */


package com.gridnode.gtas.server.document.helpers;

/**
 * This class defines constants used by Document Module.
 *
 * Route constants define the ORIGINATOR and final ENDPOINT, are explained as
 * follows.
 *
 *                      ORIGINATOR       --->             ENDPOINT
 *                      -------------------------------------------
 * ROUTE_DIRECT         GRIDTALK2                         GRIDTALK2 (not using master comm profile)
 *
 * ROUTE_GM             GRIDTALK2   --> GM(/OR DIRECT)--> GRIDTALK2 (using master comm profile)
 *
 *
 * ROUTE_GT1_GM         GRIDTALK2   --> GM(/OR DIRECT)--> GRIDTALK1
 *
 *
 *
 */


public interface IDocumentConstants
{

  public static final int ROUTE_DIRECT = 0;

  public static final int ROUTE_GM = 1;

  public static final int ROUTE_GT1_GM = 2;

}