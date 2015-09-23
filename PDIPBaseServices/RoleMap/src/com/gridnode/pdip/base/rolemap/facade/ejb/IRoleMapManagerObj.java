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

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import com.gridnode.pdip.base.rolemap.exceptions.*;
import com.gridnode.pdip.base.rolemap.entities.model.*;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.db.filter.*;

public interface IRoleMapManagerObj extends EJBObject {

    public RoleMapEntity createRoleMapping(RoleMapEntity roleMapEntity) throws RoleMapException,SystemException,RemoteException;

    public void updateRoleMapping(RoleMapEntity roleMapEntity) throws RoleMapException,SystemException,RemoteException;

    public void removeRoleMapping(Long roleMapUId) throws RoleMapException,SystemException,RemoteException;

    public void removeRoleMapping(IDataFilter filter) throws RoleMapException,SystemException,RemoteException;

    public RoleMapEntity getRoleMapping(Long roleMapUId) throws RoleMapException,SystemException,RemoteException;

    public Collection getRoleMapping(IDataFilter filter) throws SystemException,RemoteException;

    public Collection getRoles(String partnerKey,String processDefKey) throws RoleMapException,SystemException,RemoteException;

    public Collection getRoles(IDataFilter filter) throws RoleMapException,SystemException,RemoteException;

    public Collection getPartners(String roleKey,String processDefKey) throws RoleMapException,SystemException,RemoteException;

    public Collection getPartners(IDataFilter filter) throws RoleMapException,SystemException,RemoteException;

}