/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMappingBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 21 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.partnerprocess.entities.ejb;

import com.gridnode.gtas.server.partnerprocess.helpers.ProcessMappingDAOHelper;
import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * A ProcessMappingBean provides persistency services for ProcessMapping.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ProcessMappingBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7568245263651175490L;

	public String getEntityName()
  {
    return ProcessMapping.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity entity)
    throws Exception
  {
    ProcessMappingDAOHelper.getInstance().checkDuplicate(
      (ProcessMapping)entity, false);
  }

}