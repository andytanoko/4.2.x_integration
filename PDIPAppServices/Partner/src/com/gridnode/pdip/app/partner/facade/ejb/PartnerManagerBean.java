/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 18 2002    Ang Meng Hua        Created
 * Jun 06 2002    Ang Meng Hua        Added services for managing
 *                                    PartnerType & PartnerGroup
 * Jul 15 2003    Neo Sok Lay         Add methods:
 *                                    findPartnerKeys(IDataFilter),
 *                                    findPartnerTypeKeys(IDataFilter),
 *                                    findPartnerGroupKeys(IDataFilter)
 * Mar 28 2006    Neo Sok Lay         Add method: setMaxActivePartners(int).                                   
 */
package com.gridnode.pdip.app.partner.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.*;

import com.gridnode.pdip.app.partner.helpers.Logger;
import com.gridnode.pdip.app.partner.helpers.PartnerEntityHandler;
import com.gridnode.pdip.app.partner.helpers.PartnerGroupEntityHandler;
import com.gridnode.pdip.app.partner.helpers.PartnerTypeEntityHandler;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.partner.model.PartnerGroup;
import com.gridnode.pdip.app.partner.model.PartnerType;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * This bean provides the app service layer implementation of the
 * partner management module. It serves as the facade to the business
 * methods of this module.
 *
 * @author Ang Meng Hua
 *
 * @version 4.0
 * @since 2.0.1
 */
public class PartnerManagerBean implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4055829807678962644L;
	transient private SessionContext _ctx = null;

  public PartnerManagerBean()
  {
  }

  /****************************** EJB required methods **************************************/
  public void setSessionContext(SessionContext ctx)
  {
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  /************** Business methods for Partner Type Entity Mgmt ****************************/
  /**
   * Create a new PartnerType
   *
   * @param partnerType The PartnerType entity value object
   * @return the primary key of the created entity
   * @exception CreateEntityException
   */
  public Long createPartnerType(PartnerType partnerType)
    throws CreateEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.createPartnerType] Enter");
    IEntity entity = null;
    try
    {
      entity = getPartnerTypeEntityHandler().createEntity(partnerType);
    }
    catch (CreateException ex)
    {
      Logger.warn("[PartnerManagerBean.createPartnerType] BL Exception", ex);
      throw new CreateEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.createPartnerType] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.createPartnerType(PartnerType: " + partnerType + ") Error ",
        ex);
    }
    Logger.log("[PartnerManagerBean.createPartnerType] Exit");
    return (Long)entity.getKey();
  }

  /**
   * Update an existing PartnerType
   *
   * @param partnerType The PartnerType entity value object
   * @exception UpdateEntityException
   */
  public void updatePartnerType(PartnerType partnerType)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.updatePartnerType] Enter");
    try
    {
      getPartnerTypeEntityHandler().update(partnerType);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[PartnerManagerBean.updatePartnerType] BL Exception", ex);
      throw new UpdateEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.updatePartnerType] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.updatePartnerType(PartnerType: " + partnerType + ") Error ",
        ex);
    }
    Logger.log("[PartnerManagerBean.updatePartnerType] Exit");
  }

  /**
   * Delete a PartnerType.
   *
   * @param uID The UID of the PartnerType to delete.
   * @exception DeleteEntityException
   */
  public void deletePartnerType(Long uID)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.deletePartnerType] Enter");
    try
    {
      getPartnerTypeEntityHandler().remove(uID);
    }
    catch (ObjectNotFoundException ex)
    {
      Logger.warn("[PartnerManagerBean.deletePartnerType] BL Exception", ex);
      throw new DeleteEntityException(ex.getLocalizedMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[PartnerManagerBean.deletePartnerType] BL Exception", ex);
      throw new DeleteEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.deletePartnerType] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.deletePartnerType(UID: " + uID + ") Error ",
        ex);
    }
    Logger.log("[PartnerManagerBean.deletePartnerType] Exit");
  }

  /**
   * Find a Partner Type based on UID
   *
   * @param uID The UID of the partner type to find.
   * @return The Partner Type found
   * @exception FindEntityException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public PartnerType findPartnerType(Long uID)
    throws FindEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.findPartnerType] UID: "+uID);

    PartnerType partnerType = null;
    try
    {
      partnerType =
        (PartnerType)getPartnerTypeEntityHandler().getEntityByKeyForReadOnly(uID);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerType] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerType] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerType] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerType(uID) Error ",
        ex);
    }
    return partnerType;
  }

  /**
   * Find a number of PartnerType that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of PartnerType found, or empty collection if none
   * exists.
   */
  public Collection findPartnerType(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[PartnerManagerBean.findPartnerType] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection collection = null;
    try
    {
      collection = getPartnerTypeEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerType] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerType(filter) Error ",
        ex);
    }
    return collection;
  }

  /**
   * Find a number of PartnerType that satisfy the filtering condition
   * and return their keys.
   *
   * @param filter The filtering condition.
   * @return a Collection of UIDs of the PartnerTypes found, or empty collection if none
   * exists.
   * @since GT 2.2 I1
   */
  public Collection findPartnerTypeKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[PartnerManagerBean.findPartnerTypeKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection collection = null;
    try
    {
      collection = getPartnerTypeEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerTypeKeys] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerTypeKeys(filter) Error ",
        ex);
    }
    return collection;
  }

  /**
   * Find all PartnerType using the PartnerType Name.
   *
   * @param name The name of the partner type to find.
   * @return The PartnerType found, or <B>null</B> if none exists with that
   * exists.
   */
  public PartnerType findPartnerTypeByName(String name)
    throws FindEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.findPartnerTypeByName] PartnerType Name: " + name);

    PartnerType partnerType = null;
    try
    {
      partnerType = getPartnerTypeEntityHandler().findByName(name);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerTypeByName] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerTypeByName(PartnerType Name: " + name + ") Error ",
        ex);
    }
    return partnerType;
  }


  /***************** Business methods for PartnerGroup Entity Mgmt *************************/
  /**
   * Create a new PartnerGroup
   *
   * @param partnerGroup The PartnerGroup entity value object
   * @return the primary key of the created entity
   * @exception CreateEntityException
   */
  public Long createPartnerGroup(PartnerGroup partnerGroup)
    throws CreateEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.createPartnerGroup] Enter");
    IEntity entity = null;
    try
    {
      entity = getPartnerGroupEntityHandler().createEntity(partnerGroup);
    }
    catch (CreateException ex)
    {
      Logger.warn("[PartnerManagerBean.createPartnerGroup] BL Exception", ex);
      throw new CreateEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.createPartnerGroup] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.createPartnerGroup(PartnerGroup: " + partnerGroup + ") Error ",
        ex);
    }
    Logger.log("[PartnerManagerBean.createPartnerGroup] Exit");
    return (Long)entity.getKey();
  }

  /**
   * Update an existing PartnerGroup
   *
   * @param partnerGroup The PartnerGroup entity value object
   * @exception UpdateEntityException
   */
  public void updatePartnerGroup(PartnerGroup partnerGroup)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.updatePartnerGroup] Enter");
    try
    {
      getPartnerGroupEntityHandler().update(partnerGroup);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[PartnerManagerBean.updatePartnerGroup] BL Exception", ex);
      throw new UpdateEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.updatePartnerGroup] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.updatePartnerGroup(PartnerGroup: " + partnerGroup + ") Error ",
        ex);
    }
    Logger.log("[PartnerManagerBean.updatePartnerGroup] Exit");
  }

  /**
   * Delete a PartnerGroup.
   *
   * @param uID The UID of the PartnerGroup to delete.
   * @exception DeleteEntityException
   */
  public void deletePartnerGroup(Long uID)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.deletePartnerGroup] Enter");
    try
    {
      getPartnerGroupEntityHandler().remove(uID);
    }
    catch (ObjectNotFoundException ex)
    {
      Logger.warn("[PartnerManagerBean.deletePartnerGroup] BL Exception", ex);
      throw new DeleteEntityException(ex.getLocalizedMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[PartnerManagerBean.deletePartnerGroup] BL Exception", ex);
      throw new DeleteEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.deletePartnerGroup] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.deletePartnerGroup(UID: " + uID + ") Error ",
        ex);
    }
    Logger.log("[PartnerManagerBean.deletePartnerGroup] Exit");
  }

  /**
   * Find a Partner Group based on UID
   *
   * @param uID The UID of the partner group to find.
   * @return The Partner Group found
   * @exception FindEntityException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public PartnerGroup findPartnerGroup(Long uID)
    throws FindEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.findPartnerGroup] UID: "+uID);

    PartnerGroup partnerGroup = null;
    try
    {
      partnerGroup =
        (PartnerGroup)getPartnerGroupEntityHandler().getEntityByKeyForReadOnly(uID);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerGroup] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerGroup] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerGroup] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerGroup(uID) Error ",
        ex);
    }
    return partnerGroup;
  }

  /**
   * Find a number of PartnerGroup that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of PartnerGroup found, or empty collection if none
   * exists.
   */
  public Collection findPartnerGroup(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[PartnerManagerBean.findPartnerGroup] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection collection = null;
    try
    {
      collection = getPartnerGroupEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerGroup] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerGroup(filter) Error ",
        ex);
    }
    return collection;
  }

  /**
   * Find a number of PartnerGroup that satisfy the filtering condition
   * and return their keys.
   *
   * @param filter The filtering condition.
   * @return a Collection of UIDs of the PartnerGroups found, or empty collection if none
   * exists.
   * @since GT 2.2 I1
   */
  public Collection findPartnerGroupKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[PartnerManagerBean.findPartnerGroupKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection collection = null;
    try
    {
      collection = getPartnerGroupEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerGroupKeys] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerGroupKeys(filter) Error ",
        ex);
    }
    return collection;
  }

  /**
   * Find all PartnerGroup using the PartnerGroup Name.
   *
   * @param name The name of the partner group to find.
   * @return The PartnerGroup found, or <B>null</B> if none exists with that
   * exists.
   */
  public PartnerGroup findPartnerGroupByName(String name)
    throws FindEntityException, SystemException
 {
    Logger.log("[PartnerManagerBean.findPartnerGroupByName] PartnerGroup Name: " + name);

    PartnerGroup partnerGroup = null;
    try
    {
      partnerGroup = getPartnerGroupEntityHandler().findByName(name);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerGroupByName] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerGroupByName(PartnerGroup Name: " + name + ") Error ",
        ex);
    }
    return partnerGroup;
  }
  /**
   * Find all PartnerGroup using the Partner Type.
   *
   * @param partnerType The Partner Type of the partner groups to find.
   * @return A Collection of PartnerGroup found, or empty collection if none
   * exists.
   */
  public Collection findPartnerGroupByPartnerType(String partnerType)
    throws FindEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.findPartnerGroupByPartnerType] Partner Type: " + partnerType);

    Collection partnerGroup = null;
    try
    {
      partnerGroup = getPartnerGroupEntityHandler().findByPartnerType(partnerType);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerGroupByPartnerType] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerGroupByPartnerType(Partner Type: " + partnerType + ") Error ",
        ex);
    }
    return partnerGroup;
  }

  /******************* Business methods for Partner Entity Mgmt ****************************/
  /**
   * Create a new Partner
   *
   * @param partner The Partner entity value object
   * @return the primary key of the created entity
   * @exception CreateEntityException
   */
  public Long createPartner(Partner partner)
    throws CreateEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.createPartner] Enter");
    IEntity entity = null;
    try
    {
      entity = getPartnerEntityHandler().createEntity(partner);
    }
    catch (CreateException ex)
    {
      Logger.warn("[PartnerManagerBean.createPartner] BL Exception", ex);
      throw new CreateEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.createPartner] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.createPartner(Partner: " + partner + ") Error ",
        ex);
    }
    Logger.log("[PartnerManagerBean.createPartner] Exit");
    return (Long)entity.getKey();
  }

  /**
   * Delete a Partner. This will not physically delete the partner
   * from the database. This will only mark the partner as deleted
   * if markeDeleteOnly is true
   *
   * @param uID The UID of the Partner to delete.
   * @param markDeleteOnly flag to mark delete the entity
   * @exception DeleteEntityException
   */
  public void deletePartner(Long uID, boolean markDeleteOnly)
    throws DeleteEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.deletePartner] Enter");
    try
    {
      if (markDeleteOnly)
        getPartnerEntityHandler().markDelete(uID);
      else
        getPartnerEntityHandler().remove(uID);
    }
    catch (ObjectNotFoundException ex)
    {
      Logger.warn("[PartnerManagerBean.deletePartner] BL Exception", ex);
      throw new DeleteEntityException(ex.getLocalizedMessage());
    }
    catch (RemoveException ex)
    {
      Logger.warn("[PartnerManagerBean.deletePartner] BL Exception", ex);
      throw new DeleteEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.deletePartner] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.deletePartner(UID: " + uID + ") Error ",
        ex);
    }
    Logger.log("[PartnerManagerBean.deletePartner] Exit");
  }

  /**
   * Update an existing Partner
   *
   * @param partner The Partner entity value object
   * @exception UpdateEntityException
   */
  public void updatePartner(Partner partner)
    throws UpdateEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.updatePartner] Enter");
    try
    {
      getPartnerEntityHandler().update(partner);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[PartnerManagerBean.updatePartner] BL Exception", ex);
      throw new UpdateEntityException(ex.getLocalizedMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.updatePartner]] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.updatePartner(Partner: " + partner + ") Error ",
        ex);
    }
    Logger.log("[PartnerManagerBean.updatePartner] Exit");
  }


  /**
   * Find a Partner based on UID
   *
   * @param uID The UID of the partner to find.
   * @return The Partner found
   * @exception FindEntityException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public Partner findPartner(Long uID)
    throws FindEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.findPartner] UID: "+uID);

    Partner partner = null;
    try
    {
      partner =
        (Partner)getPartnerEntityHandler().getEntityByKeyForReadOnly(uID);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[PartnerManagerBean.findPartner] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[PartnerManagerBean.findPartner] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartner] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartner(uID) Error ",
        ex);
    }
    return partner;
  }

  /**
   * Find a number of Partner that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of Partner found, or empty collection if none
   * exists.
   */
  public Collection findPartner(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[PartnerManagerBean.findPartner] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection collection = null;
    try
    {
      collection = getPartnerEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartner] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartner(filter) Error ",
        ex);
    }
    return collection;
  }

  /**
   * Find a number of Partners that satisfy the filtering condition
   * and return their keys.
   *
   * @param filter The filtering condition.
   * @return a Collection of UIDs of Partners found, or empty collection if none
   * exists.
   * @since GT 2.2 I1
   */
  public Collection findPartnerKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Logger.log( "[PartnerManagerBean.findPartnerKeys] filter: "+
      (filter==null?"null":filter.getFilterExpr()));

    Collection collection = null;
    try
    {
      collection = getPartnerEntityHandler().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerKeys] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerKeys(filter) Error ",
        ex);
    }
    return collection;
  }

  /**
   * Find a Partner using the Partner ID.
   *
   * @param partnerID The Partner ID of the partner to find.
   * @return The Partner found, or <B>null</B> if none exists with that
   * ID.
   */
  public Partner findPartnerByID(String partnerID)
    throws FindEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.findPartnerByID] Partner ID: " + partnerID);

    Partner partner = null;
    try
    {
      partner = getPartnerEntityHandler().findByID(partnerID);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerByID] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerByName(Partner ID: " + partnerID + ") Error ",
        ex);
    }
    return partner;
  }

  /**
   * Find all Partners using the Partner Name.
   *
   * @param name The name of the partners to find.
   * @return A Collection of Parners found, or empty collection if none
   * exists.
   */
  public Collection findPartnerByName(String name)
    throws FindEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.findPartnerByName] Partner Name: " + name);

    Collection partners = null;
    try
    {
      partners = getPartnerEntityHandler().findByName(name);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerByName] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerByName(Partner Name: " + name + ") Error ",
        ex);
    }
    return partners;
  }

  /**
   * Find all Partners using the Partner Type.
   *
   * @param partnerType The Partner Type of the partners to find.
   * @return A Collection of Partners found, or empty collection if none
   * exists.
   */
  public Collection findPartnerByPartnerType(String partnerType)
    throws FindEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.findPartnerByPartnerType] Partner Type: " + partnerType);

    Collection partners = null;
    try
    {
      partners = getPartnerEntityHandler().findByPartnerType(partnerType);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerByPartnerType] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerByName(Partner Type: " + partnerType + ") Error ",
        ex);
    }
    return partners;
  }

  /**
   * Find all Partners using the Partner Group.
   *
   * @param partnerGroup The Partner Group of the partners to find.
   * @return A Collection of Partners found, or empty collection if none
   * exists.
   */
  public Collection findPartnerByPartnerGroup(String partnerGroup)
    throws FindEntityException, SystemException
  {
    Logger.log("[PartnerManagerBean.findPartnerByPartnerGroup] Partner Group: " + partnerGroup);

    Collection partners = null;
    try
    {
      partners = getPartnerEntityHandler().findByPartnerGroup(partnerGroup);
    }
    catch (Throwable ex)
    {
      Logger.warn("[PartnerManagerBean.findPartnerByPartnerGroup] Error ", ex);
      throw new SystemException(
        "PartnerManagerBean.findPartnerByName(Partner Group: " + partnerGroup + ") Error ",
        ex);
    }
    return partners;
  }

  /**
   * Set the limit on the maximum number of active partners that could be created.
   * @param maxActivePartners The maximum number of active partners allowed to be created.
   */
  public void setMaxActivePartners(int maxActivePartners) throws SystemException
  {
  	getPartnerEntityHandler().setMaxActivePartners(maxActivePartners);
  	try
  	{
  		int currActiveBal = getActivePartnersBalCount(null);
  		if (currActiveBal < 0) //i.e. exceed the maximum allowed
  		{
  			//so we will trim the exceeded partners to inactive
  			getPartnerEntityHandler().deactivePartners(0-currActiveBal);
  		}
  	}
  	catch (Throwable ex)
  	{
  		Logger.warn("[PartnerManagerBean.setMaxActivePartners] Error", ex);
  		throw new SystemException("PartnerManagerBean.setMaxActivePartners("+maxActivePartners+") Error ",
  		                          ex);
  	}
  }
  
  /**
   * Get the balance count of active partners allowed to be configured
   * @param excludePartnerID The partner id to exclude from the count, or <b>null</b> if no exclusion desired
   * @return The balance count
   * @throws SystemException
   */
  public int getActivePartnersBalCount(String excludePartnerID)
  	throws SystemException
  {
  	try
  	{
  		int currCount = getPartnerEntityHandler().countActivePartners(excludePartnerID);
  		int max = getPartnerEntityHandler().getMaxActivePartners();
  		return max - currCount;
  	}
  	catch (Exception ex)
  	{
  		Logger.warn("[PartnerManagerBean.getActivePartnersCount] Error", ex);
  		throw new SystemException("PartnerManagerBean.getActivePartnersCount(exclude:"+excludePartnerID+") Error ", 
  		                          ex);
  	}
  }

  private PartnerTypeEntityHandler getPartnerTypeEntityHandler()
  {
    return PartnerTypeEntityHandler.getInstance();
  }

  private PartnerGroupEntityHandler getPartnerGroupEntityHandler()
  {
    return PartnerGroupEntityHandler.getInstance();
  }

  private PartnerEntityHandler getPartnerEntityHandler()
  {
    return PartnerEntityHandler.getInstance();
  }
}