/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTProcedureDefFileManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 * 2003-07-16     Andrew Hill         getMethodsForClass() and getClassesInJar()
 * 2003-07-30     Andrew Hill         list wsdl methods
 */
package com.gridnode.gtas.client.ctrl;

import java.util.List;

import com.gridnode.gtas.client.GTClientException;

public interface IGTProcedureDefFileManager extends IGTManager
{
  public List listMethodsOfClass(String className) throws GTClientException; //20030716AH
  public List listGroupedMethodsOfClass(String className) throws GTClientException; //20030717AH
  public List listClassesInJar(Long procDefFileUid) throws GTClientException; //20030716AH
  public List listMethodsOfWSDL(Long procDefFileUid) throws GTClientException; //20030730AH
  public List listGroupedMethodsOfWSDL(Long procDefFileUid) throws GTClientException; //20030730AH
}