/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizCertMappingBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.partnerprocess.entities.ejb;

import com.gridnode.gtas.server.partnerprocess.helpers.BizCertMappingDAOHelper;
import com.gridnode.gtas.server.partnerprocess.model.BizCertMapping;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
 
/**
 * A BizCertMappingBean provides persistency services for BizCertMapping.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class BizCertMappingBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6559673408026124389L;

	public String getEntityName()
  {
    return BizCertMapping.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity entity)
    throws Exception
  {
    BizCertMappingDAOHelper.getInstance().checkDuplicate(
      (BizCertMapping)entity, false);
  }

}