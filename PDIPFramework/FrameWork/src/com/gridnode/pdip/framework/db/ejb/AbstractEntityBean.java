/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractEntityBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 26 2002    Neo Sok Lay         Catch and throw more specific exceptions.
 * Jun 07 2002    Neo Sok Lay         Add method for making version checking
 *                                    optional.
 * Jun 12 2002    Ang Meng Hua        Added checkDuplicate() method
 * Aug 13 2002    Neo Sok Lay         Send Created/Updated/Deleted entity
 *                                    events.
 * Oct 24 2002    Ang Meng Hua        Moved Send Updated entity events from
 *                                    ejbStore to setData() method
 * Oct 26 2002    Mahesh              Added a flag to store the data only if it is
 *                                    modified,This flag can be set by overriding
 *                                    optimizeEjbStore method.   
 * Feb 07 2007		Alain Ah Ming				Log warning message if throwing up exception                                   
 */
package com.gridnode.pdip.framework.db.ejb;

import java.text.NumberFormat;
import java.util.Collection;

import javax.ejb.*;

import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.log.Log;

/**
 * Abstract class for entity bean classes.
 * @author Neo Sok Lay
 * @since
 * @version GT 4.0 VAN
 */
public abstract class AbstractEntityBean implements EntityBean {
  protected EntityContext _ctx;

  protected IEntity _entity;

  protected boolean _isEntityModified = false;

  /**
   * Call when create() is called on Home.
   * 
   * @param entity
   *          The entity to create.
   * @return The primary key of the created entity.
   * @exception CreateException
   *              Create failed due to data error.
   * @exception EJBException
   *              Create failed due to system service errors.
   * 
   * @since 2.0
   */
  public Long ejbCreate(IEntity entity) throws CreateException {
    Log.debug(Log.DB, "[AbstractEntityBean.ejbCreate] Enter");

    _entity = entity;
    try {
      checkDuplicate(_entity);
      return getDAO().create(entity);
    } catch (ApplicationException ex) {
      Log.warn(Log.DB, "[AbstractEntityBean.ejbCreate] Error Exit ", ex);
      throw new CreateException(ex.getLocalizedMessage());
    } catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  public Long ejbCreate(IEntity entity, Boolean bool) throws CreateException {
    Log.debug(Log.DB, "[AbstractEntityBean.ejbCreate with bool] Enter");

    _entity = entity;
    try {
      checkDuplicate(_entity);
      return getDAO().create(entity, bool.booleanValue());
    } catch (ApplicationException ex) {
      Log.warn(Log.DB, "[AbstractEntityBean.ejbCreate with bool] Error Exit ", ex);
      throw new CreateException(ex.getLocalizedMessage());
    } catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  public void ejbPostCreate(IEntity entity) {
    EntityEventSender.getInstance().sendCreatedEvent(this.getClass().getName(),
        entity, _ctx);
  }

  public void ejbPostCreate(IEntity entity, Boolean bool) {
    ejbPostCreate(entity);
  }
  
  /**
   * Called when business method is invoked.
   * 
   * @exception NoSuchEntityException
   *              Load failed when required entity cannot be found using the
   *              current entity context primary key.
   * @exception EJBException
   *              Load failed due to other system service errors.
   * 
   * @since 2.0
   */
  public void ejbLoad() {
    // Log.debug(Log.DB, "[AbstractEntityBean.ejbLoad] Enter");
    try {
      _entity = getDAO().load((Long) _ctx.getPrimaryKey());
      setEntityModified(false);
    } catch (ApplicationException ex) {
      Log.warn(Log.DB, "[AbstractEntityBean.ejbLoad] Error Exit ", ex);
      throw new NoSuchEntityException(ex.getLocalizedMessage());
    } catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Called when the container decides to persist the bean instance.
   * 
   * @exception NoSuchEntityException
   *              Store failed due to no object found having the current entity
   *              context primary key.
   * @exception EJBException
   *              Store failed due to system service errors.
   * 
   * @since 2.0
   */
  public void ejbStore() {

    if (optimizeEjbStore() && !isEntityModified())
      return;

    try {
      // Log.debug(Log.DB, "[AbstractEntityBean.ejbStore] Enter");

      getDAO().findByPrimaryKey((Long) _ctx.getPrimaryKey());
      getDAO().store(_entity);
      setEntityModified(false);
    } catch (ApplicationException ex) {
      Log.warn(Log.DB, "[AbstractEntityBean.ejbStore] Error Exit ", ex);
      throw new NoSuchEntityException(ex.getLocalizedMessage());
    } catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Called when remove() is called on Home or EJB object.
   * 
   * @exception NoSuchEntityException
   *              Store failed due to no object found having the current entity
   *              context primary key.
   * @exception EJBException
   *              Remove failed due to system service errors.
   * 
   * @since 2.0
   */
  public void ejbRemove() {
    Log.debug(Log.DB, "[AbstractEntityBean.ejbRemove] Start");
    try {
      Long currKey = (Long) _ctx.getPrimaryKey();
      IEntity entity = getDAO().load(currKey);
      getDAO().remove(currKey);

      EntityEventSender.getInstance().sendDeletedEvent(
          this.getClass().getName(), entity, _ctx);
    } catch (ApplicationException ex) {
      throw new NoSuchEntityException(ex);
    } catch (Exception e) {
      throw new EJBException(e);
    }
  }

  /**
   * Called when finder method is invoked on Home.
   * 
   * @param primaryKey
   *          Primary key of object to find.
   * @return The primary key of the found object
   * @exception ObjectNotFoundException
   *              Find failed due to object of the specified primary key does
   *              not exists in database.
   * @exception EJBException
   *              Find failed due to system service errors.
   * 
   * @since 2.0
   */
  public Long ejbFindByPrimaryKey(Long primaryKey) throws FinderException {
    try {
      return getDAO().findByPrimaryKey(primaryKey);
    } catch (ApplicationException ex) {
      Log.warn(Log.DB, "[AbstractEntityBean.ejbFindByPrimaryKey] Error exit ",
          ex);
      throw new ObjectNotFoundException(ex.getLocalizedMessage());
    } catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Called when finder method is invoked on Home.
   * 
   * @param filter
   *          Filtering condition.
   * @return A collection of the primary keys of the found objects
   * @exception FinderException
   *              Find failed due to invalid input data (filter).
   * @exception EJBException
   *              Find failed due to system service errors.
   * 
   * @since 2.0
   */
  public Collection ejbFindByFilter(IDataFilter filter) throws FinderException {
    try {
      return getDAO().findByFilter(filter);
    } catch (ApplicationException ex) {
      Log.warn(Log.DB, "[AbstractEntityBean.ejbFindByFilter] Error exit ", ex);
      throw new FinderException(ex.getLocalizedMessage());
    } catch (Exception ex) {
      throw new EJBException(ex);
    }
  }

  /**
   * Required by the EJB specification
   */
  public void ejbActivate() {
  }

  /**
   * This method is required by the EJB Specification,
   */
  public void ejbPassivate() {
  }

  public void setEntityContext(EntityContext ctx) {
    _ctx = ctx;
  }

  public void unsetEntityContext() {
    _ctx = null;
  }

  public IEntity getData() {
    return (IEntity) _entity.clone();
  }

  public void setData(IEntity entity) throws EntityModifiedException {
    // Log.debug(Log.DB, "[AbstractEntityBean.setData] Enter ");
    if (isVersionCheckRequired()) {
      // Log.debug(Log.DB, "[AbstractEntityBean.setData] Version check required
      // ");
      /*
       * 091102 New method of checking version if (_entity.getVersion() >
       * entity.getVersion()) { throw new EntityModifiedException("Concurrent
       * entity modification: version "+ _entity.getVersion() + ">" +
       * entity.getVersion()); }
       */
      checkVersion(entity.getVersion());
      entity.increaseVersion();
    }

    // Log.debug(Log.DB, "[AbstractEntityBean.setData] cloning entity ");
    _entity = (IEntity) entity.clone();

    setEntityModified(true);

    EntityEventSender.getInstance().sendUpdatedEvent(this.getClass().getName(),
        _entity, _ctx);
    // Log.debug(Log.DB, "[AbstractEntityBean.setData] Exit ");
  }

  /*------------------------------------------------------------*/

  protected void checkDuplicate(IEntity entity) throws Exception {
    // empty implementation, do nothing by default
  }

  protected IEntityDAO getDAO() {
    return EntityDAOFactory.getInstance().getDAOFor(getEntityName());
  }

  // Abstract methods to be implemented by concrete Entity beans
  public abstract String getEntityName();

  protected boolean isVersionCheckRequired() {
    return true;
  }

  private void checkVersion(double version) throws EntityModifiedException {
    NumberFormat format = NumberFormat.getInstance();
    format.setMaximumFractionDigits(5);

    String currV = format.format(_entity.getVersion());
    String newV = format.format(version);

    if (currV.compareTo(newV) > 0)
      throw new EntityModifiedException(
          "Concurrent entity modification: version " + currV + ">" + newV);
  }

  protected boolean isEntityModified() {
    return _isEntityModified;
  }

  protected void setEntityModified(boolean isEntityModified) {
    _isEntityModified = isEntityModified;
  }

  /**
   * If this method returns true then ejbStore is only done when the entity is
   * modified
   * 
   * @return
   */
  protected boolean optimizeEjbStore() {
    return false;
  }

  /*
   * public static void main(String args[]) { NumberFormat format =
   * NumberFormat.getInstance(); format.setMaximumFractionDigits(5);
   * 
   * String num1 = format.format(7.000000000000001E-5); String num2 =
   * format.format(7.0E-5);
   * 
   * System.out.println("num1 = "+num1); System.out.println("num2 = "+num2); }
   */
}
