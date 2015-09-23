/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AS2DocTypeMappingEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-08-28    Wong Yee Wah         Created
 */
package com.gridnode.gtas.server.document.helpers;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the AS2DocTypeMappingBean.
 *
 * @author Wong Yee Wah
 *
 * @version 4.1.3
 * @since 4.1.3
 */

import java.util.Collection;



import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.gtas.server.document.entities.ejb.IAS2DocTypeMappingLocalHome;
import com.gridnode.gtas.server.document.entities.ejb.IAS2DocTypeMappingLocalObj;
import com.gridnode.gtas.server.document.model.AS2DocTypeMapping;


public class AS2DocTypeMappingEntityHandler 
            extends LocalEntityHandler
{
  
  private AS2DocTypeMappingEntityHandler()
  {
    super(AS2DocTypeMapping.ENTITY_NAME);
  }
  
  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   */
  protected Class getHomeInterfaceClass() throws Exception
  {
    return IAS2DocTypeMappingLocalHome.class;
  }
  
  /**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IAS2DocTypeMappingLocalObj.class;
  }
  
  /**
   * Get All AS2DocTypeMapping
   * @return Collection : A set of all AS2DocTypeMapping.
   * @throws Throwable - thrown when AS2DocTypeMapping cannot be retrieved.
   */
   public Collection getAllAS2DocTypeMapping() throws Throwable
   {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addSingleFilter(null,AS2DocTypeMapping.UID,filter.getNotEqualOperator(),null,false);
      
      Collection result = getEntityByFilter(filter);
     
      return result;
   }
   
   public Collection getAS2DocTypeMapping(IDataFilter filter) throws Throwable
   {
     Collection result = null;
     if(filter != null)
     {
        result = getEntityByFilterForReadOnly(filter);
     }
     else
     {
       result = getAllAS2DocTypeMapping();
     }
      return result;
   }
   
   public String removeAS2DocTypeMapping(Long uid) throws Throwable
   {
      String name = ((AS2DocTypeMapping)getEntityByKey(uid)).getDocType();
      remove(uid);
      return name;
   }
  
   public void update(IEntity entity) throws java.lang.Throwable
   {
//     ProcessMappingDAOHelper.getInstance().checkDuplicate(
//       (ProcessMapping)entity, true);

     super.update(entity);
   }

   public void createAS2DocTypeMapping(String as2DocType,String docType,String partnerID) 
         throws Throwable
   {   
     AS2DocTypeMapping dtMapping = new AS2DocTypeMapping();
     dtMapping.setAs2DocType(as2DocType);
     dtMapping.setDocType(docType);
     dtMapping.setPartnerID(partnerID);
     
     create(dtMapping);
   }
   
   /**
    * Get an instance of a AS2DocTypeMappingEntityHandler.
    */
   public static AS2DocTypeMappingEntityHandler getInstance()
   {
     AS2DocTypeMappingEntityHandler handler = null;

     if (EntityHandlerFactory.hasEntityHandlerFor(AS2DocTypeMapping.ENTITY_NAME, true))
     {
       handler = (AS2DocTypeMappingEntityHandler)EntityHandlerFactory.getHandlerFor(
                   AS2DocTypeMapping.ENTITY_NAME, true);
     }
     else
     {
       handler = new AS2DocTypeMappingEntityHandler();
       EntityHandlerFactory.putEntityHandler(AS2DocTypeMapping.ENTITY_NAME,
          true, handler);
     }

     return handler;
   }
}
