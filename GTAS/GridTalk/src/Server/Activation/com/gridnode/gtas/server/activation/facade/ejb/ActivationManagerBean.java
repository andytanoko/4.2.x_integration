/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActivationManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 10 2002    Neo Sok Lay         Created
 * Nov 07 2002    Neo Sok Lay         Check that the GridTalk is connected to
 *                                    GridMaster before performing the operations
 *                                    requiring connection.
 * Feb 05 2004    Neo Sok Lay         GNDB00017037: Handle feedback for acknowledgement
 */
package com.gridnode.gtas.server.activation.facade.ejb;

import com.gridnode.gtas.server.activation.exceptions.ConnectionRequiredException;
import com.gridnode.gtas.server.activation.exceptions.GridNodeActivationException;
import com.gridnode.gtas.server.activation.exceptions.SearchGridNodeException;
import com.gridnode.gtas.server.activation.helpers.*;
import com.gridnode.gtas.server.activation.model.*;
import com.gridnode.gtas.server.enterprise.sync.models.SyncGridNode;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.keygen.KeyGen;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.log.FacadeLogger;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

/**
 * SessionBean for managing Activation related entities.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since 2.0 I6
 */
public class ActivationManagerBean implements SessionBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -249639659414365645L;
	private SessionContext _ctx;

  public ActivationManagerBean()
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

  // ************************ Searching *************************************
  /**
   * @see IActivationManagerObj#submitSearch
   */
  public Long submitSearch(SearchGridNodeCriteria criteria)
    throws GridNodeActivationException, SystemException
  {
    FacadeLogger logger = Logger.getSearchFacadeLogger();
    String methodName   = "submitSearch";
    Object[] params     = new Object[] {criteria};

    Long searchID = null;
    try
    {
      logger.logEntry(methodName, params);

      assertSubmitEnabled();

      searchID = KeyGen.getNextId(ISearchGridNodeKeys.GN_SEARCH_KEY_ID);

      SearchGridNodeHandler.getInstance().submitSearchQuery(
        searchID, criteria);
    }
    catch (GridNodeActivationException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new SearchGridNodeException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return searchID;
  }

  /**
   * @see IActivationManagerObj#retrieveSearch
   */
  public SearchGridNodeQuery retrieveSearch(Long searchID)
    throws SearchGridNodeException, SystemException
  {
    FacadeLogger logger = Logger.getSearchFacadeLogger();
    String methodName   = "retrieveSearch";
    Object[] params     = new Object[] {searchID};

    SearchGridNodeQuery query = null;
    try
    {
      logger.logEntry(methodName, params);

      query = SearchGridNodeHandler.getInstance().retrieveSearchQuery(searchID);
      SearchGridNodeHandler.getInstance().determineGridNodeStates(
        query.getSearchResults());
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new SearchGridNodeException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return query;
  }

  /**
   * @see IActivationManagerObj#notifySearchResults
   */
  public void notifySearchResults(Long searchID, SearchGridNodeResults results)
    throws SearchGridNodeException, SystemException
  {
    FacadeLogger logger = Logger.getSearchFacadeLogger();
    String methodName   = "notifySearchResults";
    Object[] params     = new Object[] {searchID, results};

    SearchGridNodeQuery query = null;
    try
    {
      logger.logEntry(methodName, params);

      ArrayList searchedGns = new ArrayList();
      if (results.getGridNodes() != null)
      {
        for (int i=0; i<results.getGridNodes().length; i++)
        {
          SearchedGridNode gn = new SearchedGridNode();
          GridNode returnedGn = results.getGridNodes()[i].getGridnode();
          gn.setGridNodeID(new Integer(returnedGn.getID()));
          gn.setGridNodeName(returnedGn.getName());

          searchedGns.add(gn);
        }
      }
      SearchGridNodeHandler.getInstance().onSearchResultsReturned(
        searchID, searchedGns);
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new SearchGridNodeException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  // ************************** Activation ***********************************
  /**
   * @see IActivationManagerObj#submitActivationRequest
   */
  public void submitActivationRequest(
    Integer gridnodeID, String gridnodeName, String activateReason,
    Collection requestorBeUIDs)
    throws GridNodeActivationException, SystemException
  {
    FacadeLogger logger = Logger.getMainFacadeLogger();
    String methodName   = "submitActivationRequest";
    Object[] params     = new Object[] {
                            gridnodeID,
                            gridnodeName,
                            activateReason,
                            requestorBeUIDs};

    try
    {
      logger.logEntry(methodName, params);

      assertSubmitEnabled();
      new RequestActivationHandler().submitActivationRequest(
        gridnodeID, gridnodeName, activateReason, requestorBeUIDs);
    }
    catch (GridNodeActivationException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new GridNodeActivationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IActivationManagerObj#approveActivationRequest
   */
  public void approveActivationRequest(
    Long recordUID, Collection approverBeUIDs)
    throws GridNodeActivationException, SystemException
  {
    FacadeLogger logger = Logger.getMainFacadeLogger();
    String methodName   = "approveActivationRequest";
    Object[] params     = new Object[] {
                            recordUID,
                            approverBeUIDs};

    try
    {
      logger.logEntry(methodName, params);

      assertSubmitEnabled();
      new ReplyActivationHandler().approveActivationRequest(
           recordUID, approverBeUIDs);
    }
    catch (GridNodeActivationException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new GridNodeActivationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IActivationManagerObj#denyActivationRequest
   */
  public void denyActivationRequest(Long recordUID)
    throws GridNodeActivationException, SystemException
  {
    FacadeLogger logger = Logger.getMainFacadeLogger();
    String methodName   = "denyActivationRequest";
    Object[] params     = new Object[] {
                            recordUID};

    try
    {
      logger.logEntry(methodName, params);

      /**@todo to update only after ack received?? */
      new ReplyActivationHandler().denyActivationRequest(recordUID);
    }
    catch (GridNodeActivationException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new GridNodeActivationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IActivationManagerObj#abortActivationRequest
   */
  public void abortActivationRequest(Long recordUID)
    throws GridNodeActivationException, SystemException
  {
    FacadeLogger logger = Logger.getMainFacadeLogger();
    String methodName   = "abortActivationRequest";
    Object[] params     = new Object[] {
                            recordUID};

    try
    {
      logger.logEntry(methodName, params);

      assertSubmitEnabled();
      new AbortActivationHandler().abortActivationRequest(recordUID);
    }
    catch (GridNodeActivationException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new GridNodeActivationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IActivationManagerObj#submitDeactivationRequest
   */
  public void submitDeactivationRequest(
    Integer gridnodeID)
    throws GridNodeActivationException, SystemException
  {
    FacadeLogger logger = Logger.getMainFacadeLogger();
    String methodName   = "submitDeactivationRequest";
    Object[] params     = new Object[] {
                            gridnodeID};

    try
    {
      logger.logEntry(methodName, params);

      assertSubmitEnabled();
      new RequestDeactivationHandler().submitDeactivationRequest(gridnodeID);
    }
    catch (GridNodeActivationException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new GridNodeActivationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IActivationManagerObj#receiveRequest
   */
  public void receiveRequest(
    String eventID, String refTransID, String[] dataPayload, File[] filePayload)
    throws GridNodeActivationException, SystemException
  {
    FacadeLogger logger = Logger.getMainFacadeLogger();
    String methodName   = "receiveRequest";
    Object[] params     = new Object[] {
                            eventID,
                            refTransID,
                            dataPayload,
                            filePayload};

    try
    {
      logger.logEntry(methodName, params);

      SyncGridNode received = new SyncGridNode();
      received = (SyncGridNode)received.deserialize(filePayload[0].getAbsolutePath());

      //find out which handler to handle
      getHandler(eventID).handleIncomingMessage(
        refTransID, received, dataPayload, filePayload);
    }
    catch (GridNodeActivationException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new GridNodeActivationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IActivationManagerObj#receiveAcknowledgement
   */
//  public void receiveAcknowledgement(
//    Long recordUID, String[] dataPayload, File[] filePayload)
//    throws GridNodeActivationException, SystemException
//  {
//    FacadeLogger logger = Logger.getMainFacadeLogger();
//    String methodName   = "receiveAcknowledgement";
//    Object[] params     = new Object[] {
//                            recordUID,
//                            dataPayload,
//                            filePayload};
//
//    try
//    {
//      logger.logEntry(methodName, params);
//
//      Boolean succeed = new Boolean(dataPayload[0]);
//      if (succeed.booleanValue())
//        completeTrans(recordUID, filePayload);
//      else
//        transFailed(recordUID, dataPayload[1]);
//    }
//    catch (GridNodeActivationException ex)
//    {
//      throw ex;
//    }
//    catch (Throwable t)
//    {
//      logger.logError(methodName, params, t);
//      throw new GridNodeActivationException(t.getMessage());
//    }
//    finally
//    {
//      logger.logExit(methodName, params);
//    }
//  }

  /**
   * @see IActivationManagerObj#receiveAcknowledgement
   */
  public void receiveAcknowledgement(
    String eventID, Long recordUID, String[] dataPayload, File[] filePayload)
    throws GridNodeActivationException, SystemException
  {
    FacadeLogger logger = Logger.getMainFacadeLogger();
    String methodName   = "receiveAcknowledgement";
    Object[] params     = new Object[] {
                            eventID,
                            recordUID,
                            dataPayload,
                            filePayload};

    try
    {
      logger.logEntry(methodName, params);

      getHandler(eventID).handleSubmissionAck(recordUID, dataPayload, filePayload);
    }
    catch (GridNodeActivationException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new GridNodeActivationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IActivationManagerObj#receiveSubmissionFeedback
   */
  public void receiveSubmissionFeedback(
    String eventID, Long recordUID, boolean success, String message)
    throws GridNodeActivationException, SystemException
  {
    FacadeLogger logger = Logger.getMainFacadeLogger();
    String methodName   = "receiveSubmissionFeedback";
    Object[] params     = new Object[] {
                            eventID,
                            recordUID,
                            new Boolean(success),
                            message};

    try
    {
      logger.logEntry(methodName, params);

      getHandler(eventID).handleSubmissionFeedback(recordUID, success, message);
    }
    catch (GridNodeActivationException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new GridNodeActivationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * @see IActivationManagerObj#receiveAcknowledgementFeedback
   */
  public void receiveAcknowledgementFeedback(
    String eventID, String refTransID, boolean success, String message)
    throws GridNodeActivationException, SystemException
  {
    FacadeLogger logger = Logger.getMainFacadeLogger();
    String methodName   = "receiveAcknowledgementFeedback";
    Object[] params     = new Object[] {
                            eventID,
                            refTransID,
                            new Boolean(success),
                            message};

    try
    {
      logger.logEntry(methodName, params);

      getHandler(eventID).handleAcknowledgementFeedback(refTransID, success, message);
    }
    catch (GridNodeActivationException ex)
    {
      throw ex;
    }
    catch (Throwable t)
    {
      logger.logWarn(methodName, params, t);
      throw new GridNodeActivationException(t.getMessage());
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  // *********************** Finders ************************************

  /**
   * @see IActivationManagerObj#findActivationRecord
   */
  public ActivationRecord findActivationRecord(Long uID)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getMainFacadeLogger();
    String methodName   = "findActivationRecord";
    Object[] params     = new Object[] {uID};

    ActivationRecord record = null;

    try
    {
      logger.logEntry(methodName, params);

      record = (ActivationRecord)ActivationRecordEntityHandler.getInstance().getEntityByKeyForReadOnly(uID);
    }
    catch (Throwable t)
    {
      logger.logFinderError(methodName, params, t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

    return record;
  }

  /**
   * @see IActivationManagerObj#findActivationRecordsByFilter
   */
  public Collection findActivationRecordsByFilter(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    FacadeLogger logger = Logger.getMainFacadeLogger();
    String methodName   = "findActivationRecordsByFilter";
    Object[] params     = new Object[] {filter};

    Collection results = null;
    try
    {
      logger.logEntry(methodName, params);

      results = ActivationRecordEntityHandler.getInstance().getEntityByFilterForReadOnly(filter);
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
   * @see IActivationManagerObj#deleteActivationRecord
   */
  public void deleteActivationRecord(Long uID)
    throws DeleteEntityException, SystemException
  {
    FacadeLogger logger = Logger.getMainFacadeLogger();
    String methodName   = "deleteActivationRecord";
    Object[] params     = new Object[] {uID};

    try
    {
      logger.logEntry(methodName, params);

      ActivationRecordEntityHandler.getInstance().remove(uID);
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


  // ************************* Own methods *********************************

  /**
   * Get the handler that can handle messages of the specified event ID.
   *
   * @param eventID the Event ID.
   * @return the handler that is capable of handling messages of the specified
   * event ID.
   * @throws Exception No handler found for the specified event ID.
   */
  private AbstractActivationProcessHandler getHandler(String eventID)
    throws Exception
  {
    String handlerClass = ConfigurationManager.getInstance().getConfig(
                            IActivationProcessKeys.CONFIG_NAME).getString(eventID);

    if (handlerClass == null)
      throw new Exception("Cannot handle message of eventID: "+eventID);
    else
      return (AbstractActivationProcessHandler)Class.forName(handlerClass).newInstance();
  }

  /**
   * Get the handler that can handle messages of the specified event ID.
   *
   * @param type Type of activation record.
   * @return the handler that is capable of handling activation record of the
   * specified type.
   * @throws Exception No handler found for the specified type.
   */
//  private AbstractActivationProcessHandler getHandler(short type)
//    throws Exception
//  {
//    switch (type)
//    {
//      case ActivationRecord.TYPE_ABORTION :
//           return new AbortActivationHandler();
//      case ActivationRecord.TYPE_ACTIVATION :
//           return new RequestActivationHandler();
//      case ActivationRecord.TYPE_APPROVAL :
//      case ActivationRecord.TYPE_DENIAL :
//           return new ReplyActivationHandler();
//      case ActivationRecord.TYPE_DEACTIVATION :
//           return new RequestDeactivationHandler();
//      default :
//           throw new Exception("Cannot handle activation record of type : "+type);
//    }
//  }

  /**
   * Update the relevant ActivationRecord to transCompleted, and notify the
   * relevant handler (if necessary) that the transaction is completed for a
   * previous sent message.
   *
   * @param recordUID The UID of the relevant ActivationRecord.
   * @param filePayload The file payload received.
   * @throws InvalidActivationRecordStateException The record has already
   * completed trans.
   */
//  private void completeTrans(Long recordUID, File[] filePayload)
//    throws Throwable
//  {
//    FacadeLogger logger = Logger.getMainFacadeLogger();
//    String methodName   = "completeTrans";
//    Object[] params     = new Object[] {
//                            recordUID,
//                            filePayload};
//    try
//    {
//      logger.logEntry(methodName, params);
//
//      ActivationRecord record = (ActivationRecord)ActivationRecordEntityHandler.getInstance().getEntityByKey(recordUID);
//
//      if (record.isTransCompleted())
//        throw new InvalidActivationRecordStateException(
//                  "Transaction has already been completed for Record "+recordUID);
//
//      record.setTransCompleted(true);
//
//      getHandler(record.getCurrentType().shortValue()).onTransCompleted(
//        record, filePayload);
//
//      ActivationRecordEntityHandler.getInstance().update(record);
//    }
//    finally
//    {
//      logger.logExit(methodName, params);
//    }
//
//  }

  /**
   * Update the relevant ActivationRecord that the transaction has failed (
   * trans is also completed).
   *
   * @param recordUID the UID of the relevant ActivationRecord.
   * @param errorMessage The error message received from the acknowledgement.
   * @throws InvalidActivationRecordStateException The record has already completed
   * trans.
   */
//  private void transFailed(Long recordUID, String errorMessage)
//    throws Throwable
//  {
//    FacadeLogger logger = Logger.getMainFacadeLogger();
//    String methodName   = "transFailed";
//    Object[] params     = new Object[] {
//                            recordUID,
//                            errorMessage};
//    try
//    {
//      logger.logEntry(methodName, params);
//      ActivationRecord record = (ActivationRecord)ActivationRecordEntityHandler.getInstance().getEntityByKey(recordUID);
//
//      if (record.isTransCompleted())
//        throw new InvalidActivationRecordStateException(
//                  "Transaction has already been completed for Record "+recordUID);
//
//      record.setTransCompleted(true);
//      record.setTransFailReason(errorMessage);
//
//      ActivationRecordEntityHandler.getInstance().update(record);
//    }
//    finally
//    {
//      logger.logExit(methodName, params);
//    }
//  }

  /**
   * Check if Submission of message is enabled. If not, the process cannot be
   * continue.
   *
   * @return <b>true</b> if message can be submitted.
   * @throws ConnectionRequiredException If connection to gridmaster has not been
   * established, i.e. Submission is disable.
   */
  private void assertSubmitEnabled() throws ConnectionRequiredException
  {
    boolean enabled = false;
    try
    {
      enabled = ServiceLookupHelper.getPostOffice().isGridMasterPostOfficeOpened();
    }
    catch (Exception ex)
    {
      Logger.warn("[ActivationManagerBean.assertSubmitEnabled] Error ", ex);
    }
    if (!enabled)
      throw new ConnectionRequiredException("Operation requires active connection to GridMaster!");
  }


}