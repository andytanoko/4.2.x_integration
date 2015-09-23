package com.gridnode.gtas.server.rnif.facade.ejb;

import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

import java.rmi.RemoteException;
import java.util.Collection;


public interface IRnifManager
{
  /**
     * Create a new RNProfile.
     *
     * @param def The RNProfile entity.
     * @return The UID of the created RNProfile.
     */
  public RNProfile createRNProfile(RNProfile profile)
    throws CreateEntityException, SystemException, RemoteException;
  /**
   * Update a RNProfile.
   *
   * @param profile The RNProfile entity with changes.
   */
  public void updateRNProfile(RNProfile profile) throws UpdateEntityException, SystemException, RemoteException;
  ;
  /**
  * Delete a RNProfile.
  *
  * @param defUId The UID of the RNProfile to delete.
  */
  public void deleteRNProfile(Long defUId) throws DeleteEntityException, SystemException, RemoteException;

  // ********************** Finders ******************************************

  /**
   * Find a RNProfile using the RNProfile UID.
   *
   * @param uID The UID of the RNProfile to find.
   * @return The RNProfile found
   * @exception FindRNProfileException Unable to find the record with specified
   * UID, or system problem in retrieval.
   */
  public RNProfile findRNProfile(Long uID) throws FindEntityException, SystemException, RemoteException;

  /**
   * Find a number of RNProfiles that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of RNProfiles found, or empty collection if none
   * exists.
   */

  public Collection findRNProfiles(IDataFilter filter) throws FindEntityException, SystemException, RemoteException;

  /**
   * Find the keys of the RNProfiles that satisfy the filtering condition.
   *
   * @param filter The filtering condition.
   * @return a Collection of the keys (Long) of RNProfiles found, or empty
   * collection if none.
   * @excetpion FindEntityException Error in executing the finder.
   */
  public Collection findRNProfilesKeys(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;
    
    
  public Object invokeMethod(Object obj, String className, String methodName, Class[] paramTypes, Object[] params) throws Exception, RemoteException;

}