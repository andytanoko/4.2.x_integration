/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteException.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-09     Andrew Hill         Created
 */

package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EntityActionResponseData;
import com.gridnode.pdip.framework.rpf.event.EntityListActionResponseData;
import com.gridnode.pdip.framework.rpf.model.IEntityDescriptorListSet;

public class DeleteException extends ResponseException
{  
  private IGTMheReference _failedDeletions;  
  
  private DeleteException(BasicEventResponse response)
  {
    super(response);
  }
  
  static DeleteException newDeleteException(BasicEventResponse response) throws GTClientException
  {
    DeleteException e = new DeleteException(response);
    e.init();
    return e;
  }
  
  void init() throws GTClientException
  {
    try
    {
      BasicEventResponse response = getResponse();
      EntityListActionResponseData elard = (EntityListActionResponseData)response.getReturnData();
      if (elard == null)
        throw new NullPointerException("elard is null");
      _errorType = (short)elard.getOverallErrorType();
      EntityActionResponseData[] eards = elard.getResponseDataList();
      if (eards == null)
        throw new NullPointerException("eards is null");
      SimpleMheReference failedDeletions = new SimpleMheReference();
System.out.println("eards.length=" + eards.length );
      for(int i=0; i < eards.length; i++)
      {
        EntityActionResponseData eard = eards[i];
        if (eard == null)
          throw new NullPointerException("eards[" + i + "] is null");
System.out.println("eards[" + i + "].isSuccess=" + eard.isSuccess() + " > " + eard.getDescription());
        if(!eard.isSuccess())
        {
          FailedDeletionEntityReference fder = new FailedDeletionEntityReference();
          fder.setDisplay(eard.getDescription());        
          ResponseException rex = new ResponseException((short)eard.getFailCode(),
                                                        (short)eard.getErrorType(),
                                                        "unspecified",
                                                        eard.getFailTrace());
          fder.setFailureException(rex);
          String serverEntityType = elard.getEntityType();
          if (serverEntityType == null)
            throw new NullPointerException("serverEntityType is null");
          String entityType = null;
          try
          {
            entityType = DefaultGTEntities.getClientMappedName(serverEntityType); 
          }
          catch(UnsupportedOperationException noServerMappedName)
          {
            entityType = "GTAS_TYPE[" + serverEntityType + "]"; //hmmm... feel free to change to something better ;-)
          }
          fder.setType(entityType);
          fder.setKeyFieldId( elard.getKeyId() );
          fder.setKeyValue(eard.getKey());
          IEntityDescriptorListSet edls = eard.getDependentSet();
          if(edls != null)
          {
System.out.println("depends=" + EntityReferenceHelper.getLogableDetails(edls));
            IGTMheReference dependancies = null;
            try
            {
              dependancies = EntityReferenceHelper.getInstance().getMheReference(edls);
            }
            catch(Throwable t)
            {
              throw new GTClientException("Error converting dependancy set to IGTMheReference",t);
            }
            fder.setDependantEntities(dependancies);
          }
          failedDeletions.addReference(fder);
        }
      }
      _failedDeletions = failedDeletions; 
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error initialising new DeleteException object",t);
    }
  }
  
  /**
   * Individual entity references in the returned object will implement IGTFailedDeletionEntityReference
   * @return failedDeletions
   */
  public IGTMheReference getFailedDeletions() throws GTClientException
  {
    return _failedDeletions;
  }
}
