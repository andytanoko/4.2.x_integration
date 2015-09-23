/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTMessageTemplateManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-01-28     Daniel D'Cotta      Created
 * 2005-01-05			SC									Add newMessageProperty method
 */
package com.gridnode.gtas.client.ctrl;

import java.util.*;

import com.gridnode.gtas.client.GTClientException;

public interface IGTMessageTemplateManager extends IGTManager
{
    public abstract Map getSubstitutionList() throws GTClientException;
    public IGTMessagePropertyEntity newMessageProperty() throws GTClientException;
}