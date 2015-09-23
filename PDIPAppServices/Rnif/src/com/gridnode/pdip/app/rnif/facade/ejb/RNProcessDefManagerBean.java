/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RNProcessDefManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 14 2003    Neo Sok Lay         Add method: 
 *                                    findProcessDefsByProcessAct(IDataFilter)
 */
package com.gridnode.pdip.app.rnif.facade.ejb;

import com.gridnode.pdip.app.rnif.entities.ejb.IProcessDefLocalObj;
import com.gridnode.pdip.app.rnif.helpers.Logger;
import com.gridnode.pdip.app.rnif.helpers.ProcessDefEntityHandler;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * This bean provides services to manage the Process Def.
 *
 */
public class RNProcessDefManagerBean implements SessionBean, IRNProcessDefManager
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7457527849429513422L;
	transient private SessionContext _sessionCtx = null;

  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {}

  public void ejbRemove()
  {}

  public void ejbActivate()
  {}

  public void ejbPassivate()
  {}

  // ************************ Implementing methods in IRNProcessDefManagerObj

  /**
   * Create a new ProcessDef.
   *
   * @param bizEntity The ProcessDef entity.
   * @return The UID of the created ProcessDef.
   */
  public Long createProcessDef(ProcessDef def) throws CreateEntityException, SystemException
  {
    ProcessDef created = null;
    try
    {
      Logger.log("[RNProcessDefManagerBean.createProcessDef] Enter");

      created = getEntityHandler().createProcessDef(def);
      return (Long) created.getKey();
    } catch (CreateException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.createProcessDef] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    } catch (DuplicateEntityException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.createProcessDef] BL Exception", ex);
      throw ex;
    } catch (ApplicationException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.createProcessDef] BL Exception", ex);
      throw new CreateEntityException(ex.getMessage());
    } catch (Throwable ex)
    {
      Logger.warn("[RNProcessDefManagerBean.createProcessDef] Error ", ex);
      throw new SystemException("RNProcessDefManagerBean.createProcessDef(ProcessDef) Error ", ex);
    } finally
    {
      Logger.log("[RNProcessDefManagerBean.createProcessDef] Exit");
    }
  }

  /**
   * Update a ProcessDef.
   *
   * @param bizEntity The ProcessDef entity with changes.
   */
  public void updateProcessDef(ProcessDef def) throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getRnifFacadeLogger();
    String methodName = "updateProcessDef";
    Object[] params = new Object[] { def };    

    try
    {
      logger.logEntry(methodName, params);
      getEntityHandler().updateProcessDef(def);
    } catch (EntityModifiedException ex)
    {
      logger.logUpdateError(methodName, params, ex);
      throw new UpdateEntityException(ex.getMessage());
    } catch (ApplicationException ex)
    {
      logger.logUpdateError(methodName, params, ex);
      throw new UpdateEntityException(ex.getMessage());
    } catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
      throw new SystemException("RNProcessDefManagerBean.updateProcessDef(ProcessDef) Error ", ex);
    } finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
  * Delete a ProcessDef.
  *
  * @param defUId The UID of the ProcessDef to delete.
  */
  public void deleteProcessDef(Long defUId) throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getRnifFacadeLogger();
    String methodName = "deleteProcessDef";
    Object[] params = new Object[] { defUId };

    try
    {
      logger.logEntry(methodName, params);

      getEntityHandler().remove(defUId);
    } catch (Throwable ex)
    {
      logger.logDeleteError(methodName, params, ex);
    } finally
    {
      logger.logExit(methodName, params);
    }
  }

  // ********************** Finders ******************************************

  /**
   * Find a ProcessDef using the ProcessDef ID.
   *
   * @param defname The Name of the ProcessDef.
   * @return The ProcessDef found, or <B>null</B> if none exists with that
   * name.
   */
  public ProcessDef findProcessDefByName(String defName) throws FindEntityException, SystemException
  {
    ProcessDef def = null;

    try
    {
      Logger.log("[RNProcessDefManagerBean.findProcessDef] Def Name is: " + defName);

      def = getEntityHandler().findByProcessDefName(defName);
    } catch (ApplicationException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDef] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    } catch (SystemException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDef] System Exception", ex);
      throw ex;
    } catch (Throwable ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDef] Error ", ex);
      throw new SystemException("RNProcessDefManagerBean.findProcessDef(defName) Error ", ex);
    }

    return def;
  }

  /**
   * Find a ProcessDef using the ProcessDef ID and Enterprise ID.
   *
   * @param defName The ProcessDef Name
   * @return The UID of the ProcessDef found, or <B>null</B> if none
   * exists with the specified inputs.
   */
  public Long findProcessDefKeyByName(String defName) throws FindEntityException, SystemException
  {
    ProcessDef def = findProcessDefByName(defName);
    Long key = null;
    if (def != null)
      key = (Long) def.getKey();

    return key;
  }

  /**
   * Find a ProcessDef using the ProcessDef UID.
   *
   * @param uID The UID of the ProcessDef to find.
   * @return The ProcessDef found
   * @exception FindProcessDefException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public ProcessDef findProcessDef(Long uID) throws FindEntityException, SystemException
  {
    ProcessDef def = null;

    try
    {
      Logger.log("[RNProcessDefManagerBean.findProcessDef] UID: " + uID);

      IProcessDefLocalObj defObj = (IProcessDefLocalObj) getEntityHandler().findByPrimaryKey(uID);
      if(defObj != null)
         def = (ProcessDef)defObj.getData();
      else
         def = null;
    } catch (ApplicationException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDef] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    } catch (SystemException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDef] System Exception", ex);
      throw ex;
    } catch (Throwable ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDef] Error ", ex);
      throw new SystemException("RNProcessDefManagerBean.findProcessDef(UID) Error ", ex);
    }

    return def;
  }

  /**
   * Find the keys of the ProcessDefs that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of ProcessDefs found, or empty
   * collection if none.
   * @excetpion FindProcessDefException Error in executing the finder.
   */
  public Collection findProcessDefsKeys(IDataFilter filter) throws FindEntityException, SystemException
  {
    Collection defKeys = null;
    try
    {
      Logger.log("[RNProcessDefManagerBean.findProcessDefsKeys] filter: " + filter.getFilterExpr());

      defKeys = getEntityHandler().getKeyByFilterForReadOnly(filter);
    } catch (ApplicationException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDefsKeys] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    } catch (SystemException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDefsKeys] System Exception", ex);
      throw ex;
    } catch (Throwable ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDefsKeys] Error ", ex);
      throw new SystemException("RNProcessDefManagerBean.findProcessDefsKeys(filter) Error ", ex);
    }

    return defKeys;
  }


  /**
   * Find a number of ProcessDefs that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of ProcessDefs found, or empty collection if none
   * exists.
   */
  public Collection findProcessDefs(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    Collection defEntities = new ArrayList();
    try
    {
      Logger.log( "[RNProcessDefManagerBean.findProcessDefs] filter: "+
        (filter==null?"null":filter.getFilterExpr()) );

      Collection defObjColl = getEntityHandler().findByFilter(filter);
      for (Iterator iter= defObjColl.iterator(); iter.hasNext();)
      {
        IProcessDefLocalObj defObj= (IProcessDefLocalObj) iter.next();
        defEntities.add(defObj.getData());
      } 
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDefs] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDefs] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDef] Error ", ex);
      throw new SystemException(
        "RNProcessDefManagerBean.findProcessDefs(filter) Error ",
        ex);
    }

    return defEntities;

  }

  /**
   * Find a number of ProcessDefs whose ResponseAct or RequestAct satisfy the
   * specified filter condition.
   * 
   * @param filter The Filtering condition on ProcessAct entity.
   * @return Collection of ProcessDef entities found.
   * @since GT 2.2 I1
   */
  public Collection findProcessDefsByProcessAct(IDataFilter filter) throws FindEntityException, SystemException
  {
    Collection defEntities = new ArrayList();
    try
    {
      Logger.log( "[RNProcessDefManagerBean.findProcessDefsByProcessAct] filter: "+
        (filter==null?"null":filter.getFilterExpr()) );

      defEntities = getEntityHandler().findByProcessActFilter(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDefsByProcessAct] BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDefsByProcessAct] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[RNProcessDefManagerBean.findProcessDefsByProcessAct] Error ", ex);
      throw new SystemException(
        "RNProcessDefManagerBean.findProcessDefsByProcessAct(filter) Error ",
        ex);
    }

    return defEntities;        
  }


  private ProcessDefEntityHandler getEntityHandler()
  {
    return ProcessDefEntityHandler.getInstance();
  }
}