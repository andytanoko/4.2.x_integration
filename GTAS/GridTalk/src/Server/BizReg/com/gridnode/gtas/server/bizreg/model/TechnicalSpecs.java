/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: TechnicalSpecs.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14 2003    Neo Sok Lay         Created
 * Sep 18 2003    Neo Sok Lay         Add: getNamespaceTypes()
 */
package com.gridnode.gtas.server.bizreg.model;

import java.util.ArrayList;

/**
 * A TechnicalSpecs is a collection of Namespace(s) 
 * (technical specifications).
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class TechnicalSpecs
{
  private Namespace[] _namespaces;
  
  /**
   * Constructs a TechnicalSpecs.
   */
  public TechnicalSpecs()
  {
  }

  /**
   * Gets the Namespace(s) in this collection.
   * 
   * @return The Namespace(s) contained in this collection.
   */
  public Namespace[] getNamespaces()
  {
    return _namespaces;
  }

  /**
   * Sets the Namespace(s) into this collection.
   *  
   * @param namespaces The Namespace(s) to set.
   */
  public void setNamespaces(Namespace[] namespaces)
  {
    this._namespaces = namespaces;
  }

  /**
   * Find the key of the published Namespace.
   * 
   * @param type The type code of the Namespace.
   * @return The key of the Namespace in the registry. 
   * <b>null</b> if the keys have not been fetched from
   * the registry.
   */
  public String findNamespaceKey(String type)
  {
    Namespace found = findNamespace(type);
    if (found != null)
      return found.getKey();
    else
      return null;
  }

  public Namespace findNamespace(String type)
  {
    Namespace found = null;
    if (_namespaces != null)
    {
      for (int i=0; i<_namespaces.length && found==null; i++)
      {
        if (_namespaces[i].getType().equals(type))
          found = _namespaces[i];
      }
    }
    
    return found;
  }
  
  /**
   * Get a list of the Types of the Namespaces contained in this TechnicalSpecs.
   * 
   * @param ArrayList of String objects.
   */
  public ArrayList getNamespaceTypes()
  {
    ArrayList types = new ArrayList();
    if (_namespaces != null)
    {
      for (int i=0; i<_namespaces.length; i++)
      {
        types.add(_namespaces[i].getType());
      }
    }
    
    return types;
  }  
}
