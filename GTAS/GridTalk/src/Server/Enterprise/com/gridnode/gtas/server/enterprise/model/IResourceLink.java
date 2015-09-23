/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IResourceLink.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 05 2001    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.enterprise.model;

/**
 * This interface defines the the properties and FieldIds for accessing fields
 * in ResourceLink entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface IResourceLink
{
  /**
   * Name of the ResourceLink entity.
   */
  public static final String ENTITY_NAME = "ResourceLink";

  /**
   * FieldId for UID of a ResourceLink entity instance. A Long.
   */
  public static final Number UID         = new Integer(0);  //Long

  /**
   * FieldId for UID of the "From" party of ResourceLink entity instance.
   * A Long.
   */
  public static final Number FROM_UID    = new Integer(1);  //Long

  /**
   * FieldId for UID of the "To" party of ResourceLink entity instance.
   * A Long.
   */
  public static final Number TO_UID      = new Integer(2);  //Long

  /**
   * FieldId for ResourceType of "From" party of ResourceLink entity instance.
   * A Long.
   */
  public static final Number FROM_TYPE   = new Integer(3);  //String(50)

  /**
   * FieldId for ResourceType of "To" party of ResourceLink entity instance.
   * A Long.
   */
  public static final Number TO_TYPE     = new Integer(4);  //String(50)

  /**
   * FieldId for Priority flag of the link among all links from the same
   * "From" resource node to resource nodes of the same resource types.
   * An Integer.
   */
  public static final Number PRIORITY    = new Integer(5);  //Integer

  /**
   * FieldId for CanDelete flag of a ResourceLink instance. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(6);  //Boolean

  /**
   * FieldId for Version of a ResourceLink instance, A Double.
   */
  public static final Number VERSION     = new Integer(7);  //Double

  /**
   * FieldId for NextLinks of a ResourceLink instance. A Collection of ResourceLink(s).
   */
  public static final Number NEXT_LINKS  = new Integer(8);
  //Collection of ResourceLink


}
