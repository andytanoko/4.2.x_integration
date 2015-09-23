/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessInstanceMetaInfoEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 20, 2005        Tam Wei Xiang       Created
 * Apr 26, 2006        Tam Wei Xiang       Added in method to look for process 
 *                                         instance meta info based on PI ID,
 *                                         orignatorID and PI start time
 * May 17, 2006        Tam Wei Xiang       To improve the way we create process
 *                                         instance meta info obj.  
 * Jun 28, 2006        Tam Wei Xiang       To update the process instance meta info
 *                                         if duplicate record found. 
 *                                                                                                                     
 */
package com.gridnode.gtas.server.dbarchive.helpers;

import com.gridnode.gtas.server.dbarchive.entities.ejb.IProcessInstanceMetaInfoLocalHome;
import com.gridnode.gtas.server.dbarchive.entities.ejb.IProcessInstanceMetaInfoLocalObj;
import com.gridnode.gtas.server.dbarchive.model.ProcessInstanceMetaInfo;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.dao.EntityDAOImpl;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.gtas.server.dbarchive.helpers.Logger;


import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.CreateException;

/**
 *
 *
 * Tam Wei Xiang
 * 
 * @version
 * @since
 */
public class ProcessInstanceMetaInfoEntityHandler
						 extends LocalEntityHandler
{
	private ProcessInstanceMetaInfoEntityHandler()
  {
    super(ProcessInstanceMetaInfo.ENTITY_NAME);
  }
	
	/**
	 * Modified 26 Apr 2006 : add in the return of the primary key
	 * 
	 * Insert the process instance meta info in DB. Duplicate checking is provided.
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public Long createProcessInstanceMetaInfo(ProcessInstanceMetaInfo info)
				 throws Exception
	{
		try
  	{
  		checkDuplicate(info.getProcessInstanceID(), info.getOriginatorID(), 
  				           info.getProcessStartDate());
  	}
  	catch(DuplicateEntityException ex)
  	{
  		//log quietly, ignore the error
  		Logger.log("[ProcessInstanceMetaInfoEntityHandler.createProcessInstanceMetaInfo] Duplicate entry found in DB, Process_Instance_ID "+ info.getProcessInstanceID()+" and originatorID " +info.getOriginatorID());
  		
  		//28062006 to update the existing PIMetaInfo (just in case the DocNo and DocDate for the existing record is populated with wrong value)
  		ProcessInstanceMetaInfo duplicateInfo = findByPIIDOriginatorIDAndPIStartDate(info.getProcessInstanceID(), info.getOriginatorID(),
  				                                    info.getProcessStartDate());
  		info.setVersion(duplicateInfo.getVersion());
			info.setKey(duplicateInfo.getKey());
			info.setCanDelete(duplicateInfo.canDelete());
			
			try
  		{
				super.update(info);
  		}
			catch(EntityModifiedException ex1)
    	{
    		Logger.warn("[ProcessInstanceMetaInfoEntityHandler.createProcessInstanceMetaInfo] App Exception ",ex1);
    		throw new UpdateEntityException(ex1.getMessage());
    	}
  		catch(Throwable e)
  		{
  			Logger.warn("[ProcessInstanceMetaInfoEntityHandler.createProcessInstanceMetaInfo] Error ", e);
        throw new SystemException(
          "ProcessInstanceMetaInfoEntityHandler.createProcessInstanceMetaInfo Error ", e);
  		}//end
			
  		return null;
  	}
  	try
  	{
  		/*17052006 a bit slow if create obj using this way
  		EJBLocalObject localObj = (EJBLocalObject)super.create(info);
  		return (Long)localObj.getPrimaryKey(); */
  		
  		EntityDAOImpl proMetaDAO = (EntityDAOImpl)super.getDAO();
  		Long uniqueKey = proMetaDAO.createNewKey(false);
  		info.setKey(uniqueKey);
  		
  		super.create(info, Boolean.TRUE);
  		return uniqueKey;
  		
  	}
  	catch(CreateException ex)
		{
			Logger.warn("[ProcessInstanceMetaInfoEntityHandler.createProcessInstanceMetaInfo] Exception ",ex);
			throw new CreateEntityException(ex.getMessage());
		}
  	catch(Throwable ex)
  	{
  		Logger.warn("[ProcessInstanceMetaInfoEntityHandler.createProcessInstanceMetaInfo] Error ", ex);
      throw new SystemException(
        "[ProcessInstanceMetaInfoEntityHandler.createProcessInstanceMetaInfo] Error ",
        ex);
  	}
	}

	private void checkDuplicate(String processInstanceID, String originatorID, Date processStartDate)
		     throws Exception
	{
		DataFilterImpl filter = new DataFilterImpl();
  	filter.addSingleFilter(null,ProcessInstanceMetaInfo.Process_Instance_ID,filter.getEqualOperator(),
													 processInstanceID,false);
  	filter.addSingleFilter(filter.getAndConnector(),ProcessInstanceMetaInfo.Originator_ID, filter.getEqualOperator(),
  												 originatorID, false);
  	filter.addSingleFilter(filter.getAndConnector(),ProcessInstanceMetaInfo.Process_Start_Date, filter.getEqualOperator(),
				                   processStartDate, false);
  	
  	if(super.getEntityCount(filter) > 0)
  	{
  		throw new DuplicateEntityException("[ProcessInstanceMetaInfoEntityHandler.checkDuplicate] Entry with Process_Instance_ID "+processInstanceID+" and originatorID " +originatorID+
  		"has already exist in table Process_Instance_Meta_Info");
  	}
	}

  /**
   * Get an instance of a ProcessInstanceMetaInfoEntityHandler.
   */
  public static ProcessInstanceMetaInfoEntityHandler getInstance()
  {
    ProcessInstanceMetaInfoEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(ProcessInstanceMetaInfo.ENTITY_NAME, true))
    {
      handler = (ProcessInstanceMetaInfoEntityHandler)EntityHandlerFactory.getHandlerFor(
      		ProcessInstanceMetaInfo.ENTITY_NAME, true);
    }
    else
    {
      handler = new ProcessInstanceMetaInfoEntityHandler();
      EntityHandlerFactory.putEntityHandler(ProcessInstanceMetaInfo.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }
  
  /**
   * Locate a process instance meta info obj by its UID
   * @param UID PK of table Process_Instance_Meta_Info
   * @return ProcessInstanceMetaInfo obj
   * @throws Exception
   */
  public ProcessInstanceMetaInfo findByUID(Long UID) throws Exception
  {
  	DataFilterImpl filter = new DataFilterImpl();
  	filter.addSingleFilter(null,ProcessInstanceMetaInfo.UID,filter.getEqualOperator(),UID,false);
  	Collection result = super.getEntityByFilterForReadOnly(filter);
  	if(result!=null && result.size()>0)
  	{
  		Iterator i = result.iterator();
  		return (ProcessInstanceMetaInfo)i.next();
  	}
  	return null;
  }
  
  /**
   * 26 Apr 2006
   * @param piID
   * @param originID
   * @param startDate
   * @return
   * @throws Exception
   */
  public ProcessInstanceMetaInfo findByPIIDOriginatorIDAndPIStartDate(String piID,
  		                                        String originID, Date startDate)
  	throws Exception
  {
  	DataFilterImpl filter = new DataFilterImpl();
  	filter.addSingleFilter(null, ProcessInstanceMetaInfo.Process_Instance_ID, 
  			                   filter.getEqualOperator(), piID, false);
  	filter.addSingleFilter(filter.getAndConnector(), ProcessInstanceMetaInfo.Originator_ID,
  			                   filter.getEqualOperator(), originID, false);
  	filter.addSingleFilter(filter.getAndConnector(), ProcessInstanceMetaInfo.Process_Start_Date,
                           filter.getEqualOperator(), startDate, false);
  	Collection result = super.getEntityByFilterForReadOnly(filter);
  	if(result!=null && result.size()>0)
  	{
  		Iterator i = result.iterator();
  		return (ProcessInstanceMetaInfo)i.next();
  	}
  	return null;
  }
  
  /**
   * Fetch a list of process def
   * @return
   */
  public Collection getProcessDef() throws Exception
  {
  	DataFilterImpl filter = new DataFilterImpl();
  	filter.setSelectFields(new Object[]{ProcessInstanceMetaInfo.Process_Def,}, true);
  	//the method only support retrieving one particular field regardless 
  	//how many field we set in the filter.
  	//the field we select is specified inside the getFieldValueByFilter method.
  	return getDAO().getFieldValuesByFilter(ProcessInstanceMetaInfo.Process_Def, filter);
  	
  	//FYI: to retrieve multiple field, use getEntityByFilter. It will return list of 
  	//entities. Only those seletected field contain value.
  	
  }
  
  /**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IProcessInstanceMetaInfoLocalObj.class;
  }
  
  /**
   * TWX 30082006 
   */
	@Override
	protected Class getHomeInterfaceClass() throws Exception
	{
		return IProcessInstanceMetaInfoLocalHome.class;
	}
}
