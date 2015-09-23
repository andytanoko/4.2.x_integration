/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerEntityImporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 30, 2006   i00107              Created
 */

package com.gridnode.pdip.app.partner.imports;

import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.base.exportconfig.imports.DefaultEntityImporter;
import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;

/**
 * @author i00107
 * Special handling of partner import logic.
 */
public class PartnerEntityImporter extends DefaultEntityImporter
{
	private static PartnerEntityImporter _self;
	
	/**
	 * 
	 */
	private PartnerEntityImporter()
	{
	}
	
	public static synchronized PartnerEntityImporter getInstance()
	{
		if (_self == null)
		{
			_self = new PartnerEntityImporter();
		}
		return _self;
	}

	/**
	 * @see com.gridnode.pdip.base.exportconfig.imports.AbstractEntityImporter#checkFields(com.gridnode.pdip.base.exportconfig.model.ImportEntity, com.gridnode.pdip.base.exportconfig.imports.ImportRegistry)
	 */
	@Override
	public boolean checkFields(	ImportEntity entityToImport,
															ImportRegistry registry) throws Exception
	{
		Partner partner = (Partner)entityToImport.getEntity();

		//all partners imported will be default to 'Disabled' but 'Deleted' partner will still remain 'Deleted'
		if (partner.getState() != Partner.STATE_DELETED)
		{
			partner.setState(Partner.STATE_DISABLED);
		}
		return super.checkFields(entityToImport, registry);
	}

}
