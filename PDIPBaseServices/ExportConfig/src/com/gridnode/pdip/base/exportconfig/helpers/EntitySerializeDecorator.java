/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntitySerializeDecorator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 16 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.exportconfig.helpers;

import java.util.Collection;

import com.gridnode.pdip.framework.db.entity.EntityListVisitableDecorator;

/**
 * This class will serialize a collection of entities into a XML file.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public class EntitySerializeDecorator extends EntityListVisitableDecorator
{

  public EntitySerializeDecorator(Collection entities)
  {
    super(entities);
  }

}