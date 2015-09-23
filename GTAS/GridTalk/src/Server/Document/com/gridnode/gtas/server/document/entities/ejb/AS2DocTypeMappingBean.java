/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AS2DocTypeMappingEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-08-28    Wong Yee Wah         Created
 */
package com.gridnode.gtas.server.document.entities.ejb;

import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.gtas.server.document.model.AS2DocTypeMapping;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the AS2DocTypeMappingBean.
 *
 * @author Wong Yee Wah
 *
 * @version 4.1.3
 * @since 4.1.3
 */

public class AS2DocTypeMappingBean extends AbstractEntityBean
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -3183693159802650340L;

  public String getEntityName()
  {
    return AS2DocTypeMapping.ENTITY_NAME;
  }
}
