/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 24 2002    MAHESH              Created
 */
package com.gridnode.pdip.base.rolemap.facade.ejb;

import java.util.*;

import javax.ejb.*;

import com.gridnode.pdip.base.rolemap.entities.model.*;
import com.gridnode.pdip.base.rolemap.exceptions.*;
import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.util.*;


public class RoleMapManagerBean implements SessionBean {
  /**
	 * Serial Version UID 
	 */
	private static final long serialVersionUID = -2504779089059180616L;
		private SessionContext _sessionCtx = null;

    public void setSessionContext(SessionContext parm1) {
        _sessionCtx = parm1;
    }

    public void ejbCreate() throws CreateException {
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate(){
    }

    public RoleMapEntity createRoleMapping(RoleMapEntity roleMapEntity) throws RoleMapException,SystemException{
        try{
            return (RoleMapEntity)UtilEntity.createEntity(roleMapEntity,true);
        }catch(Throwable th){
            throw new SystemException("[RoleMapManagerBean.createRoleMapping] Exception ",th);
        }
    }

    public void updateRoleMapping(RoleMapEntity roleMapEntity) throws RoleMapException,SystemException{
        try{
            UtilEntity.update(roleMapEntity,true);
        }catch(Throwable th){
            throw new SystemException("[RoleMapManagerBean.updateRoleMapping] Exception roleMapEntity="+roleMapEntity,th);
        }
    }

    public void removeRoleMapping(Long roleMapUId) throws RoleMapException,SystemException{
        try{
            UtilEntity.remove(roleMapUId,RoleMapEntity.ENTITY_NAME,true);
        }catch(Throwable th){
            throw new SystemException("[RoleMapManagerBean.removeRoleMapping] Exception roleMapUId="+roleMapUId,th);
        }
    }

    public void removeRoleMapping(IDataFilter filter) throws RoleMapException,SystemException{
        try{
            UtilEntity.remove(filter,RoleMapEntity.ENTITY_NAME,true);
        }catch(Throwable th){
            throw new SystemException("[RoleMapManagerBean.removeRoleMapping] Exception filter="+filter.getFilterExpr(),th);
        }
    }

    public RoleMapEntity getRoleMapping(Long roleMapUId) throws RoleMapException,SystemException{
        try{
            return (RoleMapEntity)UtilEntity.getEntityByKey(roleMapUId,RoleMapEntity.ENTITY_NAME,true);
        }catch(Throwable th){
            throw new SystemException("[RoleMapManagerBean.getRoleMapping] Exception roleMapUId="+roleMapUId,th);
        }
    }

    public Collection getRoleMapping(IDataFilter filter) throws SystemException{
        try{
            return UtilEntity.getEntityByFilter(filter,RoleMapEntity.ENTITY_NAME,true);
        }catch(Throwable th){
            throw new SystemException("[RoleMapManagerBean.getRoleMapping] Exception filter="+filter.getFilterExpr(),th);
        }
    }

    public Collection getRoles(String partnerKey,String processDefKey) throws RoleMapException,SystemException{
        try{
            IDataFilter filter=new DataFilterImpl();
            filter.addSingleFilter(null,IRoleMapEntity.PARTNER_KEY,filter.getEqualOperator(),partnerKey,false);
            filter.addSingleFilter(filter.getAndConnector(),IRoleMapEntity.PROCESSDEF_KEY,filter.getEqualOperator(),processDefKey,false);
            return getRoles(filter);
        }catch(RoleMapException ex){
            throw ex;
        }catch(Throwable th){
            throw new SystemException("[RoleMapManagerBean.getRoles] cannot get roles with partnerKey="+partnerKey+", processDefKey="+processDefKey,th);
        }
    }

    public Collection getRoles(IDataFilter filter) throws RoleMapException,SystemException{
        try{
            Collection roleMapColl=getRoleMapping(filter);
            Collection roles=new Vector();
            if(roleMapColl!=null){
                for(Iterator iterator=roleMapColl.iterator();iterator.hasNext();){
                    roles.add(((RoleMapEntity)iterator.next()).getRoleKey());
                }
            } else throw new RoleMapException("[RoleMapManagerBean.getRoles] No roles exists with filter="+filter.getFilterExpr());
            return roles;
        }catch(RoleMapException ex){
            throw ex;
        }catch(Throwable th){
            throw new SystemException("[RoleMapManagerBean.getRoles] cannot get roles with filter="+filter.getFilterExpr(),th);
        }
    }

    public Collection getPartners(String roleKey,String processDefKey) throws RoleMapException,SystemException{
        try{
            IDataFilter filter=new DataFilterImpl();
            filter.addSingleFilter(null,IRoleMapEntity.ROLE_KEY,filter.getEqualOperator(),roleKey,false);
            filter.addSingleFilter(filter.getAndConnector(),IRoleMapEntity.PROCESSDEF_KEY,filter.getEqualOperator(),processDefKey,false);
            return getPartners(filter);
        }catch(RoleMapException ex){
            throw ex;
        }catch(Throwable th){
            throw new SystemException("[RoleMapManagerBean.getRoles] cannot get partners with roleKey="+roleKey+", processDefKey="+processDefKey,th);
        }
    }

    public Collection getPartners(IDataFilter filter) throws RoleMapException,SystemException{
        try{
            Collection roleMapColl=getRoleMapping(filter);
            Collection partners=new Vector();
            if(roleMapColl!=null){
                for(Iterator iterator=roleMapColl.iterator();iterator.hasNext();){
                    partners.add(((RoleMapEntity)iterator.next()).getPartnerKey());
                }
            } else throw new RoleMapException("[RoleMapManagerBean.getRoles] No partners exists with filter="+filter.getFilterExpr());
            return partners;
        }catch(RoleMapException ex){
            throw ex;
        }catch(Throwable th){
            throw new SystemException("[RoleMapManagerBean.getRoles] cannot get partners with filter="+filter.getFilterExpr(),th);
        }
    }

}