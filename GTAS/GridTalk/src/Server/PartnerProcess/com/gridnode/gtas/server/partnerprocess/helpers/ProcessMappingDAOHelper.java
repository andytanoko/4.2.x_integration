package com.gridnode.gtas.server.partnerprocess.helpers;

import com.gridnode.gtas.server.partnerprocess.model.ProcessMapping;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

 

/**
 * Helper for DAO level checking.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class ProcessMappingDAOHelper
{
  private static ProcessMappingDAOHelper _self = null;
  private static Object                  _lock = new Object();

  private ProcessMappingDAOHelper()
  {

  }

  public static ProcessMappingDAOHelper getInstance()
  {
    if (_self == null)
    {
      synchronized (_lock)
      {
        if (_self == null)
          _self = new ProcessMappingDAOHelper();
      }
    }
    return _self;
  }

  /**
   * Check if the specified ProcessMapping will result in duplicate when
   * created or updated.
   *
   * @param mapping The ProcessMapping to check
   * @param checkKey <b>true</b> if to include the key in the checking, i.e.
   * should ensure that the found 'duplicate' is not the mapping itself,
   * <b>false</b> otherwise. Usually <b>false</b> during create, and <b>true</b>
   * during update.
   *
   * @exception DuplicateEntityException A create or update of the specified
   * ProcessMapping will result in duplicates.
   */
  public void checkDuplicate(
    ProcessMapping mapping, boolean checkKey) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ProcessMapping.PROCESS_DEF,
      filter.getEqualOperator(), mapping.getProcessDef(), false);
    filter.addSingleFilter(filter.getAndConnector(), ProcessMapping.PARTNER_ID,
      filter.getEqualOperator(), mapping.getPartnerID(), false);
    filter.addSingleFilter(filter.getAndConnector(), ProcessMapping.IS_INITIATING_ROLE,
      filter.getEqualOperator(), mapping.isInitiatingRole()?Boolean.TRUE:Boolean.FALSE, false);

    if (checkKey)
      filter.addSingleFilter(filter.getAndConnector(), ProcessMapping.UID,
        filter.getNotEqualOperator(), mapping.getKey(), false);

    if (getDAO().getEntityCount(filter) > 0)
      throw new DuplicateEntityException(
        "Process Mapping ["+ mapping.getEntityDescr() + "] already exists!");
  }

  /**
   * Check whether a ProcessMapping can be deleted.
   *
   * @param mapping The ProcessMapping to check.
   *
   * @exception ApplicationException The ProcessMapping is not allowed to be
   * deleted.
   */
  public void checkCanDelete(ProcessMapping mapping) throws Exception
  {
    if (!mapping.canDelete())
      throw new ApplicationException("ProcessMapping not allowed to be deleted!");
  }

  private IEntityDAO getDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(ProcessMapping.ENTITY_NAME);
  }


}