/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 31 2002    MAHESH              Created
 */
package com.gridnode.pdip.app.workflow.runtime.ejb;


import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import com.gridnode.pdip.framework.exceptions.domain.*;
import com.gridnode.pdip.app.workflow.runtime.model.*;
import com.gridnode.pdip.framework.db.filter.*;


public interface IGWFRtManagerObj extends EJBObject {

    public GWFRtRestriction createRtRestrictionIfNotExists(String subRestrictionType, Long restrictionUId, String restrictionType, Long rtProcessUId) throws GWFException , RemoteException;

    public GWFRtRestriction createRtRestriction(Long restrictionUId, String restrictionType, String subRestrictionType, Long rtProcessUId) throws GWFException , RemoteException;

    public GWFRtProcess incrementMaxConcurrency(Long rtProcessUId,int incrementValue) throws GWFException , RemoteException;



    /**
     * Removes runtime Restriction
     */
    public void removeRtRestriction(Long rtRestrictionUId) throws GWFException , RemoteException;

    public GWFRtProcess getRtProcess(Long rtProcessUId) throws GWFException , RemoteException;

    public GWFRtRestriction getRtRestriction(Long rtRestrictionUId) throws GWFException , RemoteException;

    public GWFRtActivity getRtActivity(Long rtActivityUId) throws GWFException , RemoteException;

    public Collection getRtActivityByFilter(IDataFilter filter) throws GWFException , RemoteException;
    public Collection getRtProcessByFilter(IDataFilter filter) throws GWFException , RemoteException;
    public Collection getRtRestrictionByFilter(IDataFilter filter) throws GWFException , RemoteException;

    /**
     * Change TransActivationState
     */
    public GWFTransActivationState changeTransActivationState(Long transActivationStateUId ,boolean state) throws GWFException , RemoteException;

    public GWFRtRestriction getRtRestriction(String subRestrictionType, Long restrictionUId, String restrictionType, Long rtProcessUId) throws GWFException , RemoteException;

    public Collection getRtRestrictions(String subRestrictionType, Long restrictionUId, String restrictionType, Long rtProcessUId) throws GWFException , RemoteException;


    public Collection getTransActivationStates(Long transitionUId, Long listUId) throws GWFException , RemoteException;


}
