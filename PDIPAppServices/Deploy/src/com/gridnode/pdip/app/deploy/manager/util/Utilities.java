// %1023962283192:com.gridnode.pdip.app.deployment.util%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 10 2002   Mathew	        Created
 * Jun 13 2002   Mathew         Repackaged
 * Feb 09 2007    Neo Sok Lay         Change setParentEntryUId(): revise data filter
 *                                    to support NULL value in addition to empty string.
 * May 05 2010   Tam Wei Xiang        #1461 - Handle the scenario where user specifying
 *                                    both GlobalProcessCode and BusinessActionCode to be
 *                                    the same. 
 *                                    Modified parseAgain(...)
 */


package com.gridnode.pdip.app.deploy.manager.util;
 
import java.util.*;

import com.gridnode.pdip.app.deploy.manager.IConstants;
import com.gridnode.pdip.app.deploy.manager.bpss.*;
import com.gridnode.pdip.base.gwfbase.bpss.model.*;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.domain.GWFException;
import com.gridnode.pdip.framework.util.KeyConverter;

// duplicate entries are not allowed in at present
// i.e for example BpssBusinessTransactionActivity's with same name occure more then once in
// different BpssBinaryCollaboration ,
// but we assume that there won't be any duplicates
public class Utilities
  implements IConstants
{

  /**
   * Creates a new Utilities object.
   */
  public Utilities()
  {
  }

  /**
   * DOCUMENT ME!
   *
   * @param specUId DOCUMENT ME!
   * @param parentEntryUId DOCUMENT ME!
   * @param entryName DOCUMENT ME!
   * @param entryType DOCUMENT ME!
   * @throws GWFException DOCUMENT ME!
   */
  public static void setParentEntryUId(long specUId, long parentEntryUId,
                                       String entryName, String entryType)
                                throws GWFException
  {
    try
    {
      AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                          BpssProcessSpecEntry.ENTITY_NAME,
                                          true);
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, IBpssProcessSpecEntry.SPEC_UID,
                             filter.getEqualOperator(), new Long(specUId),
                             false);
      /*NSL20070209
      filter.addSingleFilter(filter.getAndConnector(),
                             IBpssProcessSpecEntry.ENTRY_NAME,
                             filter.getEqualOperator(), entryName, false);
      */                       
      filter.addSingleFilter(filter.getAndConnector(),
                             IBpssProcessSpecEntry.ENTRY_TYPE,
                             filter.getEqualOperator(), entryType, false);
      
      //NSL20070209 
      filter.addFilter(filter.getAndConnector(), getEntryNameFilter(entryName));
      System.out.println("Filter: "+filter.getFilterExpr());
      Collection coll = handler.getEntityByFilter(filter);
      System.out.println(
          "Spec :" + specUId + "   Parent :" + parentEntryUId + "   Name :" +
          entryName + "   Type :" + entryType + "   Size :" + coll.size());
      if (coll.size() == 1)
      {
        BpssProcessSpecEntry bpssProcSpecEntry = (BpssProcessSpecEntry)(coll.iterator().next());
        bpssProcSpecEntry.setParentEntryUId(parentEntryUId);
        handler.update(bpssProcSpecEntry);
      }
      else
      {
        throw new GWFException();
      }
    }
    catch (Throwable th)
    {
      th.printStackTrace();
      throw new GWFException(th);
    }
  }

  private static IDataFilter getEntryNameFilter(String entryName)
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null,
                           IBpssProcessSpecEntry.ENTRY_NAME,
                           filter.getEqualOperator(), entryName, false);
    
    if (entryName != null && entryName.length()==0)
    {
      filter.addSingleFilter(filter.getOrConnector(), IBpssProcessSpecEntry.ENTRY_NAME, filter.getEqualOperator(), null, false);
    }
    return filter;
  }
  /**
   * DOCUMENT ME!
   *
   * @param specUId DOCUMENT ME!
   * @param entryName DOCUMENT ME!
   * @param entryType DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws GWFException DOCUMENT ME!
   */
  public static BpssProcessSpecEntry findSpecificEntry(long specUId,
                                                       String entryName,
                                                       String entryType)
                                                throws GWFException
  {
    try
    {
      AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                          BpssProcessSpecEntry.ENTITY_NAME,
                                          true);
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, IBpssProcessSpecEntry.SPEC_UID,
                             filter.getEqualOperator(), new Long(specUId),
                             false);
      /*NSL20070209
      filter.addSingleFilter(filter.getAndConnector(),
                             IBpssProcessSpecEntry.ENTRY_NAME,
                             filter.getEqualOperator(), entryName, false);
      */                       
      if (entryType.equals("null"))
      {
        //must be one of the activity types
        /*
        IDataFilter filter2 = new DataFilterImpl();
        filter2.addSingleFilter(null, IBpssProcessSpecEntry.ENTRY_TYPE,
                                filter2.getEqualOperator(),
                                BpssCollaborationActivity.ENTITY_NAME, false);
        filter2.addSingleFilter(filter2.getOrConnector(),
                                IBpssProcessSpecEntry.ENTRY_TYPE,
                                filter2.getEqualOperator(),
                                BpssBusinessTransActivity.ENTITY_NAME, false);
        filter2.addSingleFilter(filter2.getOrConnector(),
                                IBpssProcessSpecEntry.ENTRY_TYPE,
                                filter2.getEqualOperator(),
                                BpssReqBusinessActivity.ENTITY_NAME, false);
        filter2.addSingleFilter(filter2.getOrConnector(),
                                IBpssProcessSpecEntry.ENTRY_TYPE,
                                filter2.getEqualOperator(),
                                BpssResBusinessActivity.ENTITY_NAME, false);
        filter.addFilter(filter.getAndConnector(), filter2);
        */
        //NSL20070209
        ArrayList<String> values = new ArrayList<String>();
        values.add(BpssCollaborationActivity.ENTITY_NAME);
        values.add(BpssBusinessTransActivity.ENTITY_NAME);
        values.add(BpssReqBusinessActivity.ENTITY_NAME);
        values.add(BpssResBusinessActivity.ENTITY_NAME);
        filter.addDomainFilter(filter.getAndConnector(), IBpssProcessSpecEntry.ENTRY_TYPE, values, false);
      }
      else
      {
        filter.addSingleFilter(filter.getAndConnector(),
                               IBpssProcessSpecEntry.ENTRY_TYPE,
                               filter.getEqualOperator(), entryType, false);
      }
      
      //NSL20070209
      filter.addFilter(filter.getAndConnector(), getEntryNameFilter(entryName));
      System.out.println("findSpecificEntry() filter: "+filter.getFilterExpr());
      
      Collection entryColl = handler.getEntityByFilter(filter);
      if (entryColl.isEmpty() || entryColl.size() > 1)
      {
        System.out.println(
            "specUId = " + String.valueOf(specUId) + "entryName = " +
            entryName + "entryType = " + entryType);
        System.out.println("entryColl = " + entryColl.size());
        throw new GWFException();
      }
      return (BpssProcessSpecEntry)entryColl.iterator().next();
    }
    catch (GWFException gex)
    {
      throw gex;
    }
    catch (Throwable th)
    {
      throw new GWFException(th);
    }
  }
  
  public static Collection<BpssProcessSpecEntry> findSpecificEntryCollection(long specUId,String entryName,
                                                                       String entryType) throws GWFException
  {
    try
    {
      AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                                                       BpssProcessSpecEntry.ENTITY_NAME,
                                                                       true);
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, IBpssProcessSpecEntry.SPEC_UID,
                             filter.getEqualOperator(), new Long(specUId),false);
                                                          
      if (entryType.equals("null"))
      {
        //must be one of the activity types
        //refactor, see above
                                     
        //NSL20070209
        ArrayList<String> values = new ArrayList<String>();
        values.add(BpssCollaborationActivity.ENTITY_NAME);
        values.add(BpssBusinessTransActivity.ENTITY_NAME);
        values.add(BpssReqBusinessActivity.ENTITY_NAME);
        values.add(BpssResBusinessActivity.ENTITY_NAME);
        filter.addDomainFilter(filter.getAndConnector(), IBpssProcessSpecEntry.ENTRY_TYPE, values, false);
      }
      else
      {
        filter.addSingleFilter(filter.getAndConnector(),
                               IBpssProcessSpecEntry.ENTRY_TYPE,
                               filter.getEqualOperator(), entryType, false);
      }
                                   
      //NSL20070209
      filter.addFilter(filter.getAndConnector(), getEntryNameFilter(entryName));
      System.out.println("findSpecificEntry() filter: "+filter.getFilterExpr());
                                   
      Collection entryColl = handler.getEntityByFilter(filter);
//                                   if (entryColl.isEmpty() || entryColl.size() > 1)
//                                   {
//                                     System.out.println(
//                                         "specUId = " + String.valueOf(specUId) + "entryName = " +
//                                         entryName + "entryType = " + entryType);
//                                     System.out.println("entryColl = " + entryColl.size());
//                                     
//                                     //debug what is in the spec entry
//                                     Iterator<BpssProcessSpecEntry> ite = entryColl.iterator();
//                                     for(; ite.hasNext(); )
//                                     {
//                                       BpssProcessSpecEntry debugEntry = ite.next();
//                                       System.out.println("More than 1 Debugging spec entry name= "+debugEntry.getEntityName()+" type="+debugEntry.getEntryType()+" specuid="+debugEntry.getSpecUId()+" uid="+debugEntry.getSpecUId());
//                                     }
//                                     
//                                     
//                                     throw new GWFException();
//                                   }
      return entryColl;
    }
    catch (GWFException gex)
    {
      throw gex;
    }
    catch (Throwable th)
    {
      throw new GWFException(th);
    }
  }

  /**
   * DOCUMENT ME!
   */
  public static void insertKey()
  {
  }

  /**
   * DOCUMENT ME!
   *
   * @param incomplete DOCUMENT ME!
   * @param specUId DOCUMENT ME!
   * @throws GWFException DOCUMENT ME!
   */
  public static void parseAgain(Hashtable incomplete, long specUId)
                         throws GWFException
  {
    //String className;
    Enumeration incompleteEnum = incomplete.elements();
    Enumeration keys = incomplete.keys();
    while (incompleteEnum.hasMoreElements())
    {
      try
      {
        Object tmpObj = incompleteEnum.nextElement();
        if (tmpObj instanceof BpssBusinessTransActivity)
        {
          BpssBusinessTransActivity obj = (BpssBusinessTransActivity)tmpObj;
          BusinessTransactionActivity xmlObj = (BusinessTransactionActivity)keys.nextElement();
          //          System.out.println("BpssBusinessTransActivity = Name : " + xmlObj.getBusinessTransaction() + " Type : " + BpssBusinessTrans.ENTITY_NAME);
          BpssProcessSpecEntry entry = findSpecificEntry(specUId,
                                                         xmlObj.getBusinessTransaction(),
                                                         BpssBusinessTrans.ENTITY_NAME);
          obj.setBusinessTransUId(new Long(entry.getEntryUId()));
          AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                              BpssBusinessTransActivity.ENTITY_NAME,
                                              true);
          handler.update(obj);
        }
        else if (tmpObj instanceof BpssBusinessTrans)
        {
          BpssBusinessTrans obj = (BpssBusinessTrans)tmpObj;
          BusinessTransaction xmlObj = (BusinessTransaction)keys.nextElement();
          //          System.out.println("BpssBusinessTrans = Name : " + xmlObj.getRequestingBusinessActivity() + " Type : " + BpssReqBusinessActivity.ENTITY_NAME);
          BpssProcessSpecEntry entryReq = findSpecificEntry(specUId,
                                                            xmlObj.getRequestingBusinessActivity().getName(),
                                                            BpssReqBusinessActivity.ENTITY_NAME);
          //          System.out.println("BpssBusinessTrans = Name : " + xmlObj.getRespondingBusinessActivity() + " Type : " + BpssResBusinessActivity.ENTITY_NAME);
          BpssProcessSpecEntry entryRes = findSpecificEntry(specUId,
                                                            xmlObj.getRespondingBusinessActivity().getName(),
                                                            BpssResBusinessActivity.ENTITY_NAME);
          obj.setBpssReqBusinessActivityUId(new Long(entryReq.getEntryUId()));
          obj.setBpssResBusinessActivityUId(new Long(entryRes.getEntryUId()));
          AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                              BpssBusinessTrans.ENTITY_NAME,
                                              true);
          handler.update(obj);
        }
        else if (tmpObj instanceof BpssCollaborationActivity)
        {
          BpssCollaborationActivity obj = (BpssCollaborationActivity)tmpObj;
          CollaborationActivity xmlObj = (CollaborationActivity)keys.nextElement();
          //          System.out.println("BPSSCollaborationActivity = Name : " + xmlObj.getBinaryCollaboration() + " Type : " + BpssBinaryCollaboration.ENTITY_NAME);
          BpssProcessSpecEntry entry = findSpecificEntry(specUId,
                                                         xmlObj.getBinaryCollaboration(),
                                                         BpssBinaryCollaboration.ENTITY_NAME);
          obj.setBinaryCollaborationUId(new Long(entry.getEntryUId()));
          AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                              BpssCollaborationActivity.ENTITY_NAME,
                                              true);
          handler.update(obj);
        }
        else if (tmpObj instanceof BpssCompletionState)
        {
          BpssCompletionState obj = (BpssCompletionState)tmpObj;
          keys.nextElement();  //Just iterate to the next element
          BpssProcessSpecEntry currEntry = getProcessSpecEntry(obj.getUId(),
                                                               BpssCompletionState.ENTITY_NAME,
                                                               specUId);
          AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                              BpssProcessSpecEntry.ENTITY_NAME,
                                              true);
          BpssProcessSpecEntry processEntry = (BpssProcessSpecEntry)handler.getEntityByKey(new Long(currEntry.getParentEntryUId()));  // first parent SpecEntry
          obj.setProcessUID(new Long(processEntry.getEntryUId()));
          obj.setProcessType(processEntry.getEntryType());
          //String objName = KeyConverter.getEntityName(obj.getFromBusinessStateKey());
          //String objType = KeyConverter.getType(obj.getFromBusinessStateKey());
          //          System.out.println("BPSSCompletionState = Name : " + objName + " Type : " + objType);
          
          //TWX #1461
          BpssProcessSpecEntry entry = null;
          Collection<BpssProcessSpecEntry> specEntrys = findSpecificEntryCollection(specUId, 
                                                                                    KeyConverter.getEntityName(obj.getFromBusinessStateKey()),
                                                                                    KeyConverter.getType(obj.getFromBusinessStateKey()));
          if(specEntrys != null && specEntrys.size() == 2)
          {
            //#1461 - if user has specified GlobalProcessCode and BusinessAction code to be the same,
            //        the specEntrys will contain 2 record; Based on the GT's Bpss usage so far (for BpssCompletionState),
            //        the related specEntry type is always with type BpssBusinessTransActivity.ENTITY_NAME. 
            //        By specifying the type explicitly, we can ensure the following query only returning one spec entry record.
            
            System.out.println("BpssCompletionState Deploy Utilities found 2 bpss process spec entries , set to type="+BpssBusinessTransActivity.ENTITY_NAME);
            entry = findSpecificEntry(specUId,
                                      KeyConverter.getEntityName(obj.getFromBusinessStateKey()),
                                      BpssBusinessTransActivity.ENTITY_NAME);
          }
          else
          {
            //TWX: we still preserving the initial design as long as user do not specify the same value for the
            //     GlobalProcessCode and BusinessAction code
            
            entry = findSpecificEntry(specUId,
                                      KeyConverter.getEntityName(obj.getFromBusinessStateKey()),
                                      KeyConverter.getType(obj.getFromBusinessStateKey()));
          }
          
          obj.setFromBusinessStateKey(getKey(new Long(entry.getEntryUId()),
                                             entry.getEntryType(),
                                             entry.getEntryName()));
          handler = EntityHandlerFactory.getHandlerFor(
                        BpssCompletionState.ENTITY_NAME, true);
          handler.update(obj);
        }
        else if (tmpObj instanceof BpssStart)
        {
          BpssStart obj = (BpssStart)tmpObj;
          keys.nextElement();  //Just iterate to the next element
          BpssProcessSpecEntry currEntry = getProcessSpecEntry(obj.getUId(),
                                                               BpssStart.ENTITY_NAME,
                                                               specUId);
          AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                              BpssProcessSpecEntry.ENTITY_NAME,
                                              true);
          BpssProcessSpecEntry processEntry = (BpssProcessSpecEntry)handler.getEntityByKey(new Long(currEntry.getParentEntryUId()));  // first parent SpecEntry
          obj.setProcessUID(new Long(processEntry.getEntryUId()));
          String objName = KeyConverter.getEntityName(obj.getToBusinessStateKey());
          String objType = KeyConverter.getType(obj.getToBusinessStateKey());
          //          System.out.println("BPSSSTART = Name : " + objName + " Type : " + objType);
          
          
          //TWX #1461
          BpssProcessSpecEntry entry = null;
          Collection<BpssProcessSpecEntry> specEntrys = findSpecificEntryCollection(specUId, objName,
                                                                                    objType);
          if(specEntrys != null && specEntrys.size() == 2)
          {
            //#1461 - if user has specified GlobalProcessCode and BusinessAction code to be the same,
            //        the specEntrys will contain 2 record; Based on the GT's Bpss usage so far (for BpssStart),
            //        the related specEntry type is always with type BpssBusinessTransActivity.ENTITY_NAME. 
            //        By specifying the type explicitly, we can ensure the following query only returning one spec entry record.
            
            System.out.println("BpssStart Deploy Utilities found 2 bpss process spec entries , set to type="+BpssBusinessTransActivity.ENTITY_NAME);
            entry = findSpecificEntry(specUId, objName,
                                      BpssBusinessTransActivity.ENTITY_NAME);
          }
          else
          {
            //TWX: we still preserving the initial design as long as user do not specify the same value for the
            //     GlobalProcessCode and BusinessAction code
            
            entry = findSpecificEntry(specUId,objName,
                                      objType);
          }
          
          obj.setToBusinessStateKey(getKey(new Long(entry.getEntryUId()),
                                           entry.getEntryType(),
                                           entry.getEntryName()));
          handler = EntityHandlerFactory.getHandlerFor(BpssStart.ENTITY_NAME,
                                                       true);
          handler.update(obj);
        }
        else if (tmpObj instanceof BpssTransition)
        {
          BpssTransition obj = (BpssTransition)tmpObj;
          keys.nextElement();  //Just iterate to the next element
          String objName = KeyConverter.getEntityName(obj.getToBusinessStateKey());
          String objType = KeyConverter.getType(obj.getToBusinessStateKey());
          System.out.println(
              "BPSSTransition = Name : " + objName + " Type : " + objType);
          BpssProcessSpecEntry entryFrom = findSpecificEntry(specUId,
                                                             KeyConverter.getEntityName(obj.getFromBusinessStateKey()),
                                                             KeyConverter.getType(obj.getFromBusinessStateKey()));
          obj.setFromBusinessStateKey(getKey(new Long(entryFrom.getEntryUId()),
                                             entryFrom.getEntryType(),
                                             entryFrom.getEntryName()));
          BpssProcessSpecEntry entryTo = findSpecificEntry(specUId,
                                                           KeyConverter.getEntityName(obj.getToBusinessStateKey()),
                                                           KeyConverter.getType(obj.getToBusinessStateKey()));
          obj.setToBusinessStateKey(getKey(new Long(entryTo.getEntryUId()),
                                           entryTo.getEntryType(),
                                           entryTo.getEntryName()));
          // set process uid,type
          // if the parent of transition is BusinessPartnerRole then process will be MPC ,
          // so set parent of BusinessPartnerRole which is MPC
          AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                              BpssProcessSpecEntry.ENTITY_NAME,
                                              true);
          BpssProcessSpecEntry processEntry = null;
          //BpssProcessSpecEntry parentEntry = null;
          BpssProcessSpecEntry currEntry = getProcessSpecEntry(obj.getUId(),
                                                               BpssTransition.ENTITY_NAME,
                                                               specUId);
          processEntry = (BpssProcessSpecEntry)handler.getEntityByKey(new Long(currEntry.getParentEntryUId()));  // first parent SpecEntry
          if (processEntry.getEntryType().equals(
                  BpssBusinessPartnerRole.ENTITY_NAME))  //means not a process
          {
            processEntry = (BpssProcessSpecEntry)handler.getEntityByKey(new Long(processEntry.getParentEntryUId()));  // second parent SpecEntry
          }
          obj.setProcessUID(new Long(processEntry.getEntryUId()));
          obj.setProcessType(processEntry.getEntryType());
          handler = EntityHandlerFactory.getHandlerFor(
                        BpssTransition.ENTITY_NAME, true);
          handler.update(obj);
        }
        else if (tmpObj instanceof BpssMultiPartyCollaboration)
        {
          keys.nextElement();
        }
        else
        {
          throw new GWFException();
        }
      }
      catch (Throwable th)
      {
        //TODO handle RemoteException, ParseException
        System.out.println("Unable to complete 2nd parse during deployment");
        th.printStackTrace();
        throw new GWFException(th);
      }
    }
  }

  /**
   * This method will create required BusinessCollaborationActivities,UpLink,DownLink's
   * @param incomplete
   * @param specUId
   * @throws Exception
   */
  public static void parseMultiPartyCollaboration(Hashtable incomplete,
                                                  long specUId)
                                           throws Exception
  {
    Enumeration incompleteEnum = incomplete.elements();
    //Enumeration keys = incomplete.keys();
    AbstractEntityHandler handler = null;
    IDataFilter filter = null;
    while (incompleteEnum.hasMoreElements())
    {
      try
      {
        Object tmpObj = incompleteEnum.nextElement();
        if (tmpObj instanceof BpssMultiPartyCollaboration)
        {
          BpssMultiPartyCollaboration obj = (BpssMultiPartyCollaboration)tmpObj;
          BpssProcessSpecEntry mpcProcessSpecEntry = getProcessSpecEntry(obj.getUId(),
                                                                         BpssMultiPartyCollaboration.ENTITY_NAME,
                                                                         specUId);
          //fetch transitions in this MPC
          filter = new DataFilterImpl();
          filter.addSingleFilter(null, BpssTransition.PROCESS_UID,
                                 filter.getEqualOperator(),
                                 new Long(obj.getUId()), false);
          handler = EntityHandlerFactory.getHandlerFor(
                        BpssTransition.ENTITY_NAME, true);
          Collection transitionColl = handler.getEntityByFilter(filter);
          if (transitionColl == null || transitionColl.size() == 0)
            continue;
          Iterator iterator = transitionColl.iterator();
          //Vector transActivityVect = new Vector();
          HashSet fromSet = new HashSet();
          HashSet toSet = new HashSet();
          while (iterator.hasNext())
          {
            //collect the UID's of BpssBusinessTransActivity's pointed by the transitions
            BpssTransition transition = (BpssTransition)iterator.next();
            String key = transition.getFromBusinessStateKey();
            if (KeyConverter.getEntityName(key).equals(
                    BpssBusinessTransActivity.ENTITY_NAME))
              fromSet.add(KeyConverter.getUID(key));
            key = transition.getToBusinessStateKey();
            if (KeyConverter.getEntityName(key).equals(
                    BpssBusinessTransActivity.ENTITY_NAME))
              toSet.add(KeyConverter.getUID(key));
          }
          Collection transActivityColl = new Vector((Collection)fromSet);
          transActivityColl.addAll((Collection)toSet);
          System.out.println(
              "In parseMultiPartyCollaboration " + fromSet.size() + "\t" +
              toSet.size() + "\t" + transActivityColl.size());
          if (transActivityColl.size() == 0)
            continue;
          //fetch the process spec entry for BpssBusinessTransActivity
          filter = new DataFilterImpl();
          filter.addSingleFilter(null, IBpssProcessSpecEntry.SPEC_UID,
                                 filter.getEqualOperator(), new Long(specUId),
                                 false);
          filter.addSingleFilter(filter.getAndConnector(),
                                 IBpssProcessSpecEntry.ENTRY_TYPE,
                                 filter.getEqualOperator(),
                                 BpssBusinessTransActivity.ENTITY_NAME, false);
          filter.addDomainFilter(filter.getAndConnector(),
                                 IBpssProcessSpecEntry.ENTRY_UID,
                                 transActivityColl, false);
          handler = EntityHandlerFactory.getHandlerFor(
                        BpssProcessSpecEntry.ENTITY_NAME, true);
          Collection pseColl = handler.getEntityByFilter(filter);
          iterator = pseColl.iterator();
          Collection bcaUpLinkColl = new Vector();
          Collection bcaDownLinkColl = new Vector();
          while (iterator.hasNext())
          {
            //process spec entry for BpssBusinessTransActivity
            BpssProcessSpecEntry btaProcessSpecEntry = (BpssProcessSpecEntry)iterator.next();
            handler = EntityHandlerFactory.getHandlerFor(
                          BpssProcessSpecEntry.ENTITY_NAME, true);
            //process spec entry for BpssBinaryCollaboration
            BpssProcessSpecEntry bcProcessSpecEntry = (BpssProcessSpecEntry)handler.getEntityByKey(new Long(btaProcessSpecEntry.getParentEntryUId()));
            if (bcProcessSpecEntry.getEntryType().equals(
                    BpssBinaryCollaboration.ENTITY_NAME))
            {
              Long bcUId = new Long(bcProcessSpecEntry.getEntryUId());
              if (fromSet.contains(new Long(btaProcessSpecEntry.getEntryUId())))
              {
                String fromBusinessStateKey = getKey(new Long(btaProcessSpecEntry.getEntryUId()),
                                                     BpssBusinessTransActivity.ENTITY_NAME,
                                                     btaProcessSpecEntry.getEntryName());
                //check whether uplink exists if does not exist create uplink.
                filter = new DataFilterImpl();
                filter.addSingleFilter(null,
                                       BpssCompletionState.FROM_BUSINESS_STATE_KEY,
                                       filter.getEqualOperator(),
                                       fromBusinessStateKey, false);
                filter.addSingleFilter(filter.getAndConnector(),
                                       BpssCompletionState.PROCESS_UID,
                                       filter.getEqualOperator(),
                                       new Long(bcProcessSpecEntry.getUId()),
                                       false);
                filter.addSingleFilter(filter.getAndConnector(),
                                       BpssCompletionState.MPC_UID,
                                       filter.getEqualOperator(),
                                       new Long(mpcProcessSpecEntry.getEntryUId()),
                                       false);
                handler = EntityHandlerFactory.getHandlerFor(
                              BpssCompletionState.ENTITY_NAME, true);
                Collection upLinkColl = handler.findByFilter(filter);
                if (upLinkColl.size() == 0)
                {
                  // create upLink BpssCompletionState for BpssBinaryCollaboration with from pointing to BusinessTransactionActivity
                  BpssCompletionState bpssCompState = new BpssCompletionState();
                  bpssCompState.setFromBusinessStateKey(fromBusinessStateKey);
                  bpssCompState.setMpcUId(new Long(mpcProcessSpecEntry.getEntryUId()));
                  bpssCompState.setProcessUID(new Long(bcProcessSpecEntry.getEntryUId()));
                  bpssCompState.setProcessType(bcProcessSpecEntry.getEntryType());
                  handler = EntityHandlerFactory.getHandlerFor(
                                BpssCompletionState.ENTITY_NAME, true);
                  bpssCompState = (BpssCompletionState)handler.createEntity(
                                      bpssCompState);
                  //create ProcessSpecEntry for BpssCompletionState
                  createProcessSpecEntry(specUId,
                                         new Long(bpssCompState.getUId()),
                                         null, BpssCompletionState.ENTITY_NAME,
                                         new Long(bcProcessSpecEntry.getUId()));
                }
                //check whether BpssBinaryCollaborationActivity for UpLink exists if does not exist create.
                if (!bcaUpLinkColl.contains(bcUId))
                {
                  //create BpssBinaryCollaborationActivity
                  BpssBinaryCollaborationActivity bcActivity =
                      new BpssBinaryCollaborationActivity();
                  bcActivity.setActivityName(bcProcessSpecEntry.getEntryName());
                  bcActivity.setBinaryCollaborationUId(new Long(bcProcessSpecEntry.getEntryUId()));
                  handler = EntityHandlerFactory.getHandlerFor(
                                BpssBinaryCollaborationActivity.ENTITY_NAME,
                                true);
                  bcActivity = (BpssBinaryCollaborationActivity)handler.createEntity(
                                   bcActivity);
                  //create ProcessSpecEntry for BpssBinaryCollaborationActivity
                  createProcessSpecEntry(specUId,
                                         new Long(bcActivity.getUId()),
                                         bcProcessSpecEntry.getEntryName(),
                                         BpssBinaryCollaborationActivity.ENTITY_NAME,
                                         new Long(mpcProcessSpecEntry.getUId()));
                  bcaUpLinkColl.add(bcUId);
                }
              }
              if (toSet.contains(new Long(btaProcessSpecEntry.getEntryUId())))
              {
                String toBusinessStateKey = getKey(new Long(btaProcessSpecEntry.getEntryUId()),
                                                   BpssBusinessTransActivity.ENTITY_NAME,
                                                   btaProcessSpecEntry.getEntryName());
                //check whether DownLink exists if does not exist create DownLink.
                filter = new DataFilterImpl();
                filter.addSingleFilter(null, BpssStart.TO_BUSINESS_STATE_KEY,
                                       filter.getEqualOperator(),
                                       toBusinessStateKey, false);
                filter.addSingleFilter(filter.getAndConnector(),
                                       BpssStart.ISDOWNLINK,
                                       filter.getEqualOperator(),
                                       new Integer(1), false);
                filter.addSingleFilter(filter.getAndConnector(),
                                       BpssStart.PROCESS_UID,
                                       filter.getEqualOperator(), bcUId, false);
                handler = EntityHandlerFactory.getHandlerFor(
                              BpssStart.ENTITY_NAME, true);
                Collection downLinkColl = handler.findByFilter(filter);
                Long downLinkUId = null;
                if (downLinkColl.size() == 0)
                {
                  // create downLink BpssStart for BpssBinaryCollaboration with toBusinessStateKey pointing to BusinessTransactionActivity
                  BpssStart bpssStart = new BpssStart();
                  bpssStart.setToBusinessStateKey(toBusinessStateKey);
                  bpssStart.setIsDownLink(Boolean.TRUE);
                  bpssStart.setProcessUID(new Long(bcProcessSpecEntry.getEntryUId()));
                  handler = EntityHandlerFactory.getHandlerFor(
                                BpssStart.ENTITY_NAME, true);
                  bpssStart = (BpssStart)handler.createEntity(bpssStart);
                  downLinkUId = new Long(bpssStart.getUId());
                  //create ProcessSpecEntry for BpssStart
                  createProcessSpecEntry(specUId, downLinkUId, null,
                                         BpssStart.ENTITY_NAME,
                                         new Long(bcProcessSpecEntry.getUId()));
                }
                else
                {
                  BpssStart bpssStart = (BpssStart)downLinkColl.toArray()[0];
                  downLinkUId = new Long(bpssStart.getUId());
                }
                if (!bcaDownLinkColl.contains(bcUId + "," + downLinkUId))
                {
                  // create BpssBinaryCollaborationActivity
                  BpssBinaryCollaborationActivity bcActivity =
                      new BpssBinaryCollaborationActivity();
                  bcActivity.setActivityName(bcProcessSpecEntry.getEntryName());
                  bcActivity.setBinaryCollaborationUId(new Long(bcProcessSpecEntry.getEntryUId()));
                  bcActivity.setDownLinkUId(downLinkUId);
                  handler = EntityHandlerFactory.getHandlerFor(
                                BpssBinaryCollaborationActivity.ENTITY_NAME,
                                true);
                  bcActivity = (BpssBinaryCollaborationActivity)handler.createEntity(
                                   bcActivity);
                  //create ProcessSpecEntry for BpssBinaryCollaborationActivity
                  createProcessSpecEntry(specUId,
                                         new Long(bcActivity.getUId()),
                                         bcProcessSpecEntry.getEntryName(),
                                         BpssBinaryCollaborationActivity.ENTITY_NAME,
                                         new Long(mpcProcessSpecEntry.getUId()));
                  bcaDownLinkColl.add(bcUId + "," + downLinkUId);
                }
              }
            }
          }
        }
      }
      catch (Throwable th)
      {
        th.printStackTrace();
        throw new GWFException("Exception in parseMultiPartyCollaboration", th);
      }
    }
  }

  /**
   * Helper method to create BpssProcessSpecEntry
   * @param specUId
   * @param entryUId
   * @param entryName
   * @param entryType
   * @param parentEntryUId
   * @return BpssProcessSpecEntry
   * @throws Exception
   */
  public static BpssProcessSpecEntry createProcessSpecEntry(long specUId,
                                                            Long entryUId,
                                                            String entryName,
                                                            String entryType,
                                                            Long parentEntryUId)
                                                     throws Exception
  {
    //create ProcessSpecEntry
    try
    {
      BpssProcessSpecEntry processSpecEntry = new BpssProcessSpecEntry();
      processSpecEntry.setSpecUId(specUId);
      processSpecEntry.setEntryName(entryName);
      processSpecEntry.setEntryType(entryType);
      if (entryUId != null)
        processSpecEntry.setEntryUId(entryUId.longValue());
      if (parentEntryUId != null)
        processSpecEntry.setParentEntryUId(parentEntryUId.longValue());
      AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                          BpssProcessSpecEntry.ENTITY_NAME,
                                          true);
      return (BpssProcessSpecEntry)handler.createEntity(processSpecEntry);
    }
    catch (Throwable th)
    {
      throw new GWFException(th);
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param processUId DOCUMENT ME!
   * @param fromKey DOCUMENT ME!
   * @param toKey DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public Collection getTransitions(Long processUId, String fromKey,
                                   String toKey)
                            throws Exception
  {
    try
    {
      AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                          BpssTransition.ENTITY_NAME, true);
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, BpssTransition.PROCESS_UID,
                             filter.getEqualOperator(), processUId, false);
      if (fromKey != null)
        filter.addSingleFilter(filter.getAndConnector(),
                               BpssTransition.FROM_BUSINESS_STATE_KEY,
                               filter.getEqualOperator(), fromKey, false);
      else if (toKey != null)
        filter.addSingleFilter(filter.getAndConnector(),
                               BpssTransition.TO_BUSINESS_STATE_KEY,
                               filter.getEqualOperator(), toKey, false);
      return handler.getEntityByFilter(filter);
    }
    catch (Throwable th)
    {
      throw new GWFException(th);
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param entryUId DOCUMENT ME!
   * @param entryType DOCUMENT ME!
   * @param specUId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public static BpssProcessSpecEntry getProcessSpecEntry(long entryUId,
                                                         String entryType,
                                                         long specUId)
                                                  throws Exception
  {
    try
    {
      AbstractEntityHandler handler = EntityHandlerFactory.getHandlerFor(
                                          BpssProcessSpecEntry.ENTITY_NAME,
                                          true);
      IDataFilter filter = new DataFilterImpl();
      filter.addSingleFilter(null, IBpssProcessSpecEntry.SPEC_UID,
                             filter.getEqualOperator(), new Long(specUId),
                             false);
      filter.addSingleFilter(filter.getAndConnector(),
                             IBpssProcessSpecEntry.ENTRY_UID,
                             filter.getEqualOperator(), new Long(entryUId),
                             false);
      filter.addSingleFilter(filter.getAndConnector(),
                             IBpssProcessSpecEntry.ENTRY_TYPE,
                             filter.getEqualOperator(), entryType, false);
      Collection coll = handler.getEntityByFilter(filter);
      if (coll != null && coll.size() == 1)
      {
        return (BpssProcessSpecEntry)(coll.iterator().next());
      }
      else
      {
        throw new GWFException();
      }
    }
    catch (GWFException gex)
    {
      throw gex;
    }
    catch (Throwable th)
    {
      throw new GWFException(th);
    }
  }

  private static String getKey(Long uId, String entityName, String entryName)
  {
    String wfType = "";
    if (entityName.equals(BpssReqBusinessActivity.ENTITY_NAME) ||
        entityName.equals(BpssResBusinessActivity.ENTITY_NAME) ||
        entityName.equals(BpssBusinessTransActivity.ENTITY_NAME) ||
        entityName.equals(BpssBinaryCollaborationActivity.ENTITY_NAME) ||
        entityName.equals(BpssCollaborationActivity.ENTITY_NAME))
    {
      wfType = "wfActivity";
    }
    else if (entityName.equals(BpssCompletionState.ENTITY_NAME) ||
             entityName.equals(BpssFork.ENTITY_NAME) ||
             entityName.equals(BpssJoin.ENTITY_NAME))
    {
      wfType = "wfRestriction";
      if (entryName.startsWith("Success"))
        entityName = "BpssSuccess";
      else if (entryName.startsWith("Failure"))
        entityName = "BpssFailure";
    }
    else if (entityName.equals(BpssStart.ENTITY_NAME))
    {
      wfType = "wfTransition";
    }
    return KeyConverter.getKey(uId, entityName, wfType);
  }

  //GWF_SUCCESS_DIV = 0

  /**
   * DOCUMENT ME!
   *
   * @param finalResult DOCUMENT ME!
   * @param result DOCUMENT ME!
   * @return DOCUMENT ME!
   */
  public static int combineResult(int finalResult, int result)
  {
    if (result < GWF_SUCCESS)
      finalResult = (finalResult < GWF_SUCCESS_DIV) ? finalResult + 1 : result;
    else
      finalResult = (finalResult < GWF_SUCCESS_DIV)
                    ? finalResult : finalResult + 1;
    return result;
  }

  /**
   * DOCUMENT ME!
   *
   * @param mc DOCUMENT ME!
   * @param transitions DOCUMENT ME!
   * @param forks DOCUMENT ME!
   * @param joins DOCUMENT ME!
   * @return DOCUMENT ME!
   */
  public static int calcMaxConcurrency(int mc, Transition[] transitions,
                                       Fork[] forks, Join[] joins)
  {
    for (int i = 0; i < forks.length; i++)
    {
      String forkName = forks[i].getName();
      int outCount = 0;
      for (int j = 0; j < transitions.length; j++)
      {
        if (transitions[j].getFromBusinessState().equals(forkName))
          outCount++;
      }
      if (outCount > 0)
      {
        outCount--;
        mc += outCount;
      }
    }
    for (int i = 0; i < joins.length; i++)
    {
      String joinName = joins[i].getName();
      int inCount = 0;
      for (int j = 0; j < transitions.length; j++)
      {
        if (transitions[j].getToBusinessState().equals(joinName))
          inCount++;
      }
      if (inCount > 0)
      {
        inCount--;
        mc += inCount;
      }
    }
    return mc;
  }

  /*
  public static BpssProcessSpecification findMaxVersion(Iterator it) {
    String maxVersion = "";
    BpssProcessSpecification matchSpec=null;
    while (it.hasNext()) {
      BpssProcessSpecification current = (BpssProcessSpecification)it.next();
      if (current.getSpecVersion().compareToIgnoreCase(maxVersion) > 0) {
        maxVersion = current.getSpecVersion();
        matchSpec = current;
      }
    }
    return matchSpec;
  }
  */
  /*
    public static void createSpecEntries(Collection coll, AbstractEntityHandler handler) {
      Iterator it = coll.iterator();
      try {
        while (it.hasNext()) {
          handler.create((BpssProcessSpecEntry)it.next());
        }
      }
      catch (Exception e) {
        System.out.println("Unable to create BpssProcessSpecEntry");
        e.printStackTrace();
      }

    }

    public static void removeSpecEntries(Collection coll, AbstractEntityHandler handler) {
      Iterator it = coll.iterator();
      try {
        while (it.hasNext()) {
          handler.remove(new Long(((BpssProcessSpecEntry)it.next()).getUId()));
        }
      }
      catch (Exception e) {
        System.out.println("Unable to remove BpssProcessSpecEntry");
        e.printStackTrace();
      }
    }

    public static void setSpecEntries(Collection coll, AbstractEntityHandler handler) {
      Iterator it = coll.iterator();
      try {
        while (it.hasNext()) {
          handler.update(((BpssProcessSpecEntry)it.next()));
        }
      }
      catch (Exception e) {
        System.out.println("Unable to remove BpssProcessSpecEntry");
        e.printStackTrace();
      }
    }

    public static Collection undeploySpecEntries(long UId) {
      IDataFilter filter=new DataFilterImpl();
      filter.addSingleFilter(null,IBpssProcessSpecEntry.SPEC_UID,filter.getEqualOperator(),new Long(UId),false);
      AbstractEntityHandler entryHandler=EntityHandlerFactory.getHandlerFor(BpssProcessSpecEntry.ENTITY_NAME,true);
      Collection bpssProcSpecEntryColl=entryHandler.getEntityByFilter(filter);
      Utilities.removeSpecEntries(bpssProcSpecEntryColl, entryHandler);
      return bpssProcSpecEntryColl;
    }

    public static Collection updateSpecEntries(long UId) {
      IDataFilter filter=new DataFilterImpl();
      filter.addSingleFilter(null,IBpssProcessSpecEntry.SPEC_UID,filter.getEqualOperator(),new Long(UId),false);
      AbstractEntityHandler entryHandler=EntityHandlerFactory.getHandlerFor(BpssProcessSpecEntry.ENTITY_NAME,true);
      Collection bpssProcSpecEntryColl=entryHandler.getEntityByFilter(filter);
      Utilities.setSpecEntries(bpssProcSpecEntryColl, entryHandler);
      return bpssProcSpecEntryColl;
    }
  */
}