/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-24     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.xml;

import java.util.*;

import com.gridnode.gtas.client.utils.*;

public class DocumentConfig extends FreezeableConfigObject
{
  private HashMap _descriptors;

  public DocumentConfig()
  {
    _descriptors = new HashMap();
  }

  public DocumentDescriptor getDescriptor(String id)
  {
    return (DocumentDescriptor)_descriptors.get(id);
  }

  public Iterator getDescriptors()
  {
    return _descriptors.values().iterator();
  }

  public void addDescriptor(DocumentDescriptor descriptor)
  {
    assertNotFrozen();
    _descriptors.put(descriptor.getKey(), descriptor);
  }

  public void freeze()
  {
    super.freeze();
    Iterator i = getDescriptors();
    while(i.hasNext())
    {
      ((FreezeableConfigObject)i.next()).freeze();
    }
  }
}