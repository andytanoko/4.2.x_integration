/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Mahesh              Created
 *
 */
package com.gridnode.pdip.base.rolemap.entities.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

public class RoleMapEntity
    extends AbstractEntity
    implements IRoleMapEntity {

  /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = 2465514374713244649L;
		protected String _roleKey;
    protected String _partnerKey;
    protected String _processDefKey;

    // ******************* Methods from AbstractEntity ******************
    public String getEntityName(){
        return ENTITY_NAME;
    }

    public String getEntityDescr(){
        return toString();
    }

    public Number getKeyId(){
        return UID;
    }

   // *********************** Getters for attributes **********************

    public String getRoleKey(){
        return _roleKey;
    }
    public String getPartnerKey(){
        return _partnerKey;
    }
    public String getProcessDefKey(){
        return _processDefKey;
    }

   // *********************** Setters for attributes **********************

    public void setRoleKey(String roleKey){
        _roleKey=roleKey;
    }
    public void setPartnerKey(String partnerKey){
        _partnerKey=partnerKey;
    }
    public void setProcessDefKey(String processDefKey){
        _processDefKey=processDefKey;
    }

}