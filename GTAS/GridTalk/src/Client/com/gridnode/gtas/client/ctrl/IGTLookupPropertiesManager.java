/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTLookupPropertiesManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 30 Dec 2005		SC									Created
 */

package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTLookupPropertiesManager extends IGTManager
{
	public IGTLookupPropertiesEntity newLookupProperties() throws GTClientException;
}
