package com.gridnode.pdip.app.rnif.helpers;

import com.gridnode.pdip.app.rnif.model.ProcessAct;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

import java.util.Collection;
import java.util.Iterator;

/**
 * This is a EntityDAO implementation for ProcessDefBean. It takes care of
 * the ProcessDef entity as well as its dependent entity: ProcessAct.
 */
public class ProcessDefDAOHelper implements IEntityDAO
{
  private static ProcessDefDAOHelper _self;

  private ProcessDefDAOHelper()
  {
  }

  /**
   * Get the singleton instance of this DAO.
   */
  public static ProcessDefDAOHelper getInstance()
  {
    if (_self == null)
      _self= new ProcessDefDAOHelper();

    return _self;
  }

  // ******************* Start Implement methods in IEntityDAO ****************

  public Long create(IEntity entity) throws Exception
  {
    ProcessDef processDef= (ProcessDef) entity;
    ProcessAct requestAct= processDef.getRequestAct();

    Long defKey= getProcessDefDAO().create(processDef);

    requestAct.setFieldValue(ProcessAct.PROCESS_DEF_ACT, ProcessAct.REQUEST_ACT);
    requestAct.setFieldValue(ProcessAct.PROCESS_DEF_UID, defKey);
    //Long wpKey= 
    getProcessActDAO().create(requestAct);

    ProcessAct responseAct= processDef.getResponseAct();
    if (responseAct != null
      && !ProcessDef.TYPE_SINGLE_ACTION.equalsIgnoreCase(processDef.getProcessType()))
    {
      responseAct.setFieldValue(ProcessAct.PROCESS_DEF_ACT, ProcessAct.RESPONSE_ACT);
      responseAct.setFieldValue(ProcessAct.PROCESS_DEF_UID, defKey);
      //wpKey= 
      getProcessActDAO().create(responseAct);

    }

    return defKey;
  }

  public IEntity load(Long defUID) throws Exception
  {
    ProcessDef def= (ProcessDef) getProcessDefDAO().load(defUID);
    loadProcessAct(def);

    return def;
  }

  public void store(IEntity processDef) throws Exception
  {
    ProcessAct ProcessAct= ((ProcessDef) processDef).getRequestAct();
    getProcessDefDAO().store(processDef);
    getProcessActDAO().store(ProcessAct);

    ProcessAct responseAct= ((ProcessDef) processDef).getResponseAct();
    if (responseAct != null
      && !ProcessDef.TYPE_SINGLE_ACTION.equalsIgnoreCase( ((ProcessDef) processDef).getProcessType()))
    {
      getProcessActDAO().store(responseAct);

    }
  }

  public void remove(Long defUID) throws Exception
  {
    ProcessDef def= (ProcessDef) load(defUID);

    ProcessAct requestAct= def.getRequestAct();
    ProcessAct responseAct= def.getResponseAct();

    getProcessDefDAO().remove(defUID);
    getProcessActDAO().remove((Long) requestAct.getKey());

    if (responseAct != null)
      getProcessActDAO().remove((Long) responseAct.getKey());
  }

  public Long findByPrimaryKey(Long primaryKey) throws Exception
  {
    return getProcessDefDAO().findByPrimaryKey(primaryKey);
  }

  public Collection findByFilter(IDataFilter filter) throws Exception
  {
    return getProcessDefDAO().findByFilter(filter);
  }

  public Collection getEntityByFilter(IDataFilter filter) throws Exception
  {
    Collection bizEntities= getProcessDefDAO().getEntityByFilter(filter);
    for (Iterator i= bizEntities.iterator(); i.hasNext();)
    {
      ProcessDef ProcessDef= (ProcessDef) i.next();
      loadProcessAct(ProcessDef);
    }
    return bizEntities;
  }

  public int getEntityCount(IDataFilter filter) throws Exception
  {
    return getProcessDefDAO().getEntityCount(filter);
  }

  /**
	 * @see com.gridnode.pdip.framework.db.dao.IEntityDAO#getFieldValuesByFilter(java.lang.Number, com.gridnode.pdip.framework.db.filter.IDataFilter)
	 */
	public Collection getFieldValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
	{
		return getProcessDefDAO().getFieldValuesByFilter(fieldId, filter);
	}

  
  // ******************* Ends Implement methods in IEntityDAO ****************

	/**
   * Loads the ProcessAct for a ProcessDef
   *
   * @param ProcessDef The ProcessDef entity.
   */
  private void loadProcessAct(ProcessDef def) throws Exception
  {
    DataFilterImpl filter= new DataFilterImpl();
    filter.addSingleFilter(
      null,
      ProcessAct.PROCESS_DEF_UID,
      filter.getEqualOperator(),
      new Long(def.getUId()),
      false);
    Collection result= getProcessActDAO().findByFilter(filter);
    if (result == null || result.isEmpty())
      return;
    for (Iterator iter= result.iterator(); iter.hasNext();)
    {
      Long ProcessActUId= (Long) iter.next();
      ProcessAct act= (ProcessAct) getProcessActDAO().load(ProcessActUId);
      if (ProcessAct.REQUEST_ACT.equals(act.getProcessDefAct()))
        def.setRequestAct(act);
      else
        if (ProcessAct.RESPONSE_ACT.equals(act.getProcessDefAct())
          && !ProcessDef.TYPE_SINGLE_ACTION.equalsIgnoreCase(def.getProcessType()))
          def.setResponseAct(act);
    }

  }

  /**
   * Check if the specified ProcessDef will result in duplicate when
   * created or updated.
   *
   * @param ProcessDef The ProcessDef check
   * @param checkKey <b>true</b> if to include the key in the checking, i.e.
   * should ensure that the found 'duplicate' is not the ProcessDef itself,
   * <b>false</b> otherwise. Usually <b>false</b> during create, and <b>true</b>
   * during update.
   *
   * @exception DuplicateEntityException A create or update of the specified
   * ProcessDef will result in duplicates.
   */
  public void checkDuplicate(ProcessDef def, boolean checkKey) throws Exception
  {
    DataFilterImpl filter= new DataFilterImpl();
    filter.addSingleFilter(
      null,
      ProcessDef.DEF_NAME,
      filter.getEqualOperator(),
      def.getDefName(),
      false);
    if (checkKey)
      filter.addSingleFilter(
        filter.getAndConnector(),
        ProcessDef.UID,
        filter.getNotEqualOperator(),
        def.getKey(),
        false);

    if (getEntityCount(filter) > 0)
      throw new DuplicateEntityException(
        "ProcessDef Name " + def.getDefName() + " already used for ProcessDef: " + def);
  }

  /**
   * Get the data access object for the ProcessDef entity.
   *
   * @return the IEntityDAO for ProcessDef entity.
   */
  public IEntityDAO getProcessDefDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(ProcessDef.ENTITY_NAME);
  }

  /**
   * Get the data access object for the ProcessAct entity.
   *
   * @return the IEntityDAO for ProcessAct entity.
   */
  public IEntityDAO getProcessActDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(ProcessAct.ENTITY_NAME);
  }
  
/* (non-Javadoc)
 * @see com.gridnode.pdip.framework.db.dao.IEntityDAO#create(com.gridnode.pdip.framework.db.entity.IEntity, boolean)
 */
 public Long create(IEntity entity, boolean useUID) throws Exception 
 { 
		throw new Exception("[ProcessDefDAOHelper.create(IEntity entity, boolean useUID)] Not Supported");
 }   
 
 public Collection getMinValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
 {
   return getProcessDefDAO().getMinValuesByFilter(fieldId, filter);
 }
 
 public Collection getMaxValuesByFilter(Number fieldId, IDataFilter filter) throws Exception
 {
   return getProcessDefDAO().getMaxValuesByFilter(fieldId, filter);
 }
}