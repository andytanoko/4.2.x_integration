/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTMappingFileManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-12     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.List;

import com.gridnode.gtas.client.GTClientException;

public interface IGTMappingFileManager extends IGTManager
{
  public IGTMappingFileEntity getMappingFileByUID(long uid)
    throws GTClientException;

  public Collection getAllOfType(Short type)
    throws GTClientException;
  
  //added by ming qian
  public List listClassesInJar(Long longUID) 
    throws GTClientException; //20030716AH
  //end of added by ming qian
  
//  public void update(IGTEntity entity, boolean updateFile)
//    throws com.gridnode.gtas.client.GTClientException;
}