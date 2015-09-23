/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridNodeManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 09 2002    Neo Sok Lay         Created
 * Sep 11 2002    Neo Sok Lay         Add methods to get/save My CompanyProfile.
 * Sep 17 2002    Neo Sok Lay         Add methods for managing Gridnodes. These
 *                                    methods may be temporary, and may change
 *                                    to more specific and restrictive methods
 *                                    as the module evolves.
 * Oct 30 2002    Neo Sok Lay         Add updateConnectionStatus().
 */
package com.gridnode.gtas.server.gridnode.facade.ejb;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.server.gridnode.exceptions.SaveCoyProfileException;
import com.gridnode.gtas.server.gridnode.helpers.*;
import com.gridnode.gtas.server.gridnode.model.ConnectionStatus;
import com.gridnode.gtas.server.gridnode.model.GnCategory;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * SessionBean for managing GridNode related entities.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I5
 */
public class GridNodeManagerBean implements SessionBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8623788414549739581L;
	private SessionContext _ctx;

  public GridNodeManagerBean()
  {
  }

  public void ejbCreate()
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

  public void setSessionContext(SessionContext ctx)
  {
    _ctx = ctx;
  }

  // **************************** Methods ********************************

  // ********************** GnCategory ****************************

  /**
   * Find GnCategory by CategoryCode.
   *
   * @param categoryCode The category code of the GnCategory to find.
   *
   * @return GnCategory found, or <b>null</b> if none found.
   */
  public GnCategory findGnCategoryByCode(String categoryCode)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getCategoryFacadeLogger();
    String methodName   = "findGnCategoryByCode";
    Object[] params     = new Object[] {categoryCode};

    GnCategory code = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, GnCategory.CATEGORY_CODE, filter.getEqualOperator(),
        categoryCode, false);
      Collection results = GnCategoryEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);

      if (results != null && !results.isEmpty())
        code = (GnCategory)results.toArray()[0];
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return code;
  }

  /**
   * Find all GnCategory(s).
   *
   * @return Collection of GnCategory(s) found.
   */
  public Collection findAllGnCategories()
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getCategoryFacadeLogger();
    String methodName   = "findAllGnCategories";
    Object[] params     = new Object[] {};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      results = GnCategoryEntityHandler.getInstance().getEntityByFilterForReadOnly(null);
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  /**
   * Find all GnCategory(s) that satisfy the specified filtering condition.
   *
   * @param filter The filtering condition.
   * @return Collection of GnCategory(s) found, or empty collection if none.
   */
  public Collection findGnCategoriesByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getCategoryFacadeLogger();
    String methodName   = "findGnCategoriesByFilter";
    Object[] params     = new Object[] {filter};

    Collection results = new ArrayList();

    try
    {
      logger.logEntry(methodName, params);

      results = GnCategoryEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable ex)
    {
      logger.logFinderError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return results;
  }

  // ************************ End GnCategory *********************************
  // ********************* CompanyProfile ************************************

  /**
   * @see IGridNodeManagerObj#getMyCompanyProfile
   */
  public CompanyProfile getMyCompanyProfile()
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProfileFacadeLogger();
    String methodName   = "getMyCompanyProfile";
    Object[] params     = new Object[] {};

    CompanyProfile prof = null;

    try
    {
      logger.logEntry(methodName, params);

      prof = ServiceLookupHelper.getCoyProfileManager().findMyCompanyProfile();
      if (prof == null)
      {
        prof = new CompanyProfile();
        prof.setPartner(Boolean.FALSE);
      }
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return prof;

  }

  /**
   * @see IGridNodeManagerObj#getCompanyProfile
   */
  public CompanyProfile getCompanyProfile(Long gridnodeUID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getProfileFacadeLogger();
    String methodName   = "getCompanyProfile";
    Object[] params     = new Object[] {};

    CompanyProfile prof = null;

    try
    {
      logger.logEntry(methodName, params);

      GridNode gn = findGridNode(gridnodeUID);
      if (gn.getCoyProfileUID() != null)
      {
        prof = ServiceLookupHelper.getCoyProfileManager().findCompanyProfile(
                 gn.getCoyProfileUID());
      }
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return prof;
  }

  /**
   * @see IGridNodeManagerObj#saveMyCompanyProfile
   */
  public void saveMyCompanyProfile(Map coyProfileMap)
    throws SaveCoyProfileException, SystemException
  {
    FacadeLogger logger = Logger.getProfileFacadeLogger();
    String methodName   = "saveMyCompanyProfile";
    Object[] params     = new Object[] {coyProfileMap};

    CompanyProfile prof = null;

    try
    {
      logger.logEntry(methodName, params);

      prof = ServiceLookupHelper.getCoyProfileManager().findMyCompanyProfile();
      if (prof == null)
      {
        prof = new CompanyProfile();
        copyProfileFields(coyProfileMap, prof);
        ServiceLookupHelper.getCoyProfileManager().createCompanyProfile(prof);
      }
      else
      {
        if (!prof.getKey().equals(coyProfileMap.get(CompanyProfile.UID)))
           throw new ApplicationException("Unknown CompanyProfile for modification!");

        copyProfileFields(coyProfileMap, prof);
        ServiceLookupHelper.getCoyProfileManager().updateCompanyProfile(prof);
      }
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new SaveCoyProfileException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  private void copyProfileFields(Map from, CompanyProfile prof)
  {
    prof.setAddress((String)from.get(CompanyProfile.ADDRESS));
    prof.setAltEmail((String)from.get(CompanyProfile.ALT_EMAIL));
    prof.setAltTel((String)from.get(CompanyProfile.ALT_TEL));
    prof.setCity((String)from.get(CompanyProfile.CITY));
    prof.setCountry((String)from.get(CompanyProfile.COUNTRY));
    prof.setCoyName((String)from.get(CompanyProfile.COY_NAME));
    prof.setEmail((String)from.get(CompanyProfile.EMAIL));
    prof.setFax((String)from.get(CompanyProfile.FAX));
    prof.setLanguage((String)from.get(CompanyProfile.LANGUAGE));
    prof.setState((String)from.get(CompanyProfile.STATE));
    prof.setTel((String)from.get(CompanyProfile.TEL));
    prof.setZipCode((String)from.get(CompanyProfile.ZIP_CODE));
    prof.setPartner(Boolean.FALSE);
    prof.setCanDelete(false);
  }

  // ***************** End CompanyProfile *******************************
  // *********************** GridNode ***********************************
  /**
   * @see IGridNodeManagerObj#createGridNode
   */
  public Long createGridNode(GridNode gridnode)
    throws CreateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getGridNodeFacadeLogger();
    String methodName   = "createGridNode";
    Object[] params     = new Object[] {gridnode};

    Long key = null;

    try
    {
      logger.logEntry(methodName, params);

      /**@todo check state */
      gridnode.setDTCreated(new Timestamp(System.currentTimeMillis()));
      key = (Long)GridNodeEntityHandler.getInstance().createEntity(gridnode).getKey();

      ConnectionStatus status = new ConnectionStatus();
      status.setGridNodeID(gridnode.getID());
      ConnectionStatusEntityHandler.getInstance().create(status);
    }
    catch (Throwable t)
    {
      logger.logCreateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return key;
  }

  /**
   * @see IGridNodeManagerObj#updateGridNode
   */
  public void updateGridNode(GridNode gridnode)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getGridNodeFacadeLogger();
    String methodName   = "updateGridNode";
    Object[] params     = new Object[] {gridnode};

    try
    {
      logger.logEntry(methodName, params);

      /**@todo check state */
      GridNodeEntityHandler.getInstance().update(gridnode);

    }
    catch (Throwable t)
    {
      logger.logUpdateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  /**
   * @see IGridNodeManagerObj#deleteGridNode
   */
  public void deleteGridNode(Long uID)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getGridNodeFacadeLogger();
    String methodName   = "deleteGridNode";
    Object[] params     = new Object[] {uID};

    try
    {
      logger.logEntry(methodName, params);

      GridNode gn = findGridNode(uID);

      GridNodeEntityHandler.getInstance().remove(uID);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ConnectionStatus.GRIDNODE_ID,
        filter.getEqualOperator(), gn.getID(), false);
      ConnectionStatusEntityHandler.getInstance().removeByFilter(filter);
    }
    catch (Throwable t)
    {
      logger.logDeleteError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IGridNodeManagerObj#findGridNode
   */
  public GridNode findGridNode(Long uID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getGridNodeFacadeLogger();
    String methodName   = "findGridNode";
    Object[] params     = new Object[] {uID};

    GridNode gn = null;

    try
    {
      logger.logEntry(methodName, params);

      gn = (GridNode)GridNodeEntityHandler.getInstance().getEntityByKeyForReadOnly(uID);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return gn;
  }

  /**
   * @see IGridNodeManagerObj#findGridNodeByID
   */
  public GridNode findGridNodeByID(String nodeID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getGridNodeFacadeLogger();
    String methodName   = "findGridNode";
    Object[] params     = new Object[] {nodeID};

    GridNode gn = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, GridNode.ID, filter.getEqualOperator(),
        nodeID, false);

      Collection results = GridNodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);

      Object[] gns = results.toArray();
      if (gns.length == 0)
        throw new ApplicationException("No such GridNodeID: "+nodeID);

      gn = (GridNode)gns[0];
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return gn;
  }

  /**
   * @see IGridNodeManagerObj#findGridNodesByFilter
   */
  public Collection findGridNodesByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getGridNodeFacadeLogger();
    String methodName   = "findGridNodesByFilter";
    Object[] params     = new Object[] {filter};

    Collection results = null;
    try
    {
      logger.logEntry(methodName, params);

      results = GridNodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }


    return results;
  }

  /**
   * @see IGridNodeManagerObj#findGridNodeKeys
   */
  public Collection findGridNodesKeys(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getGridNodeFacadeLogger();
    String methodName   = "findGridNodesKeys";
    Object[] params     = new Object[] {filter};

    Collection results = null;
    try
    {
      logger.logEntry(methodName, params);

      results = GridNodeEntityHandler.getInstance().getKeyByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }


    return results;
  }

  /**
   * @see IGridNodeManagerObj#findMyGridNode
   */
  public GridNode findMyGridNode()
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getGridNodeFacadeLogger();
    String methodName   = "findMyGridNode";
    Object[] params     = new Object[] {};

    GridNode gn = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, GridNode.STATE, filter.getEqualOperator(),
        new Short(GridNode.STATE_ME), false);

      Collection results = GridNodeEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);

      Object[] gns = results.toArray();
      if (gns.length == 0)
        throw new ApplicationException("My GridNode not setup yet!");

      gn = (GridNode)gns[0];
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return gn;
  }

  // ******************************* End GridNode ****************************
  // ********************** Connection Status ********************************

  /**
   * @see IGridNodeManagerObj#findConnectionStatus
   */
  public ConnectionStatus findConnectionStatus(Long uID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "findConnectionStatus";
    Object[] params     = new Object[] {uID};

    ConnectionStatus cs = null;

    try
    {
      logger.logEntry(methodName, params);

      cs = (ConnectionStatus)ConnectionStatusEntityHandler.getInstance().getEntityByKeyForReadOnly(uID);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return cs;
  }

  /**
   * @see IGridNodeManagerObj#findConnectionStatusByNodeID
   */
  public ConnectionStatus findConnectionStatusByNodeID(String nodeID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "findConnectionStatusByNodeID";
    Object[] params     = new Object[] {nodeID};

    ConnectionStatus status = null;

    try
    {
      logger.logEntry(methodName, params);

      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null, ConnectionStatus.GRIDNODE_ID,
        filter.getEqualOperator(), nodeID, false);

      Collection results = ConnectionStatusEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);

      Object[] csArray = results.toArray();
      if (csArray.length == 0)
        throw new ApplicationException("No such GridNodeID: "+nodeID);

      status = (ConnectionStatus)csArray[0];
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return status;
  }

  /**
   * @see IGridNodeManagerObj#findConnectionStatusByFilter
   */
  public Collection findConnectionStatusByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "findConnectionStatusByFilter";
    Object[] params     = new Object[] {filter};

    Collection results = null;
    try
    {
      logger.logEntry(methodName, params);

      results = ConnectionStatusEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }


    return results;
  }

  /**
   * @see IGridNodeManagerObj#updateConnectionStatus
   */
  public void updateConnectionStatus(ConnectionStatus connStatus)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getConnectionFacadeLogger();
    String methodName   = "updateConnectionStatus";
    Object[] params     = new Object[] {connStatus};

    Collection results = null;
    try
    {
      logger.logEntry(methodName, params);

      ConnectionStatusEntityHandler.getInstance().update(connStatus);
    }
    catch (Throwable t)
    {
      logger.logUpdateError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }
}