/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IUserProcedureServiceManagerObj.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 21 2003    Jagadeesh              Created
 * Oct 20 2005    Neo Sok Lay             No corresponding business method in the bean class 
 *                                        com.gridnode.gtas.server.userprocedure.facade.ejb.UserProcedureServiceManagerBean 
 *                                        was found for method performUserProcedure.
 */

package com.gridnode.gtas.server.userprocedure.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.gtas.server.userprocedure.exceptions.InvalidParamDefException;
import com.gridnode.gtas.server.userprocedure.exceptions.InvalidReturnDefException;
import com.gridnode.gtas.server.userprocedure.exceptions.UserProcedureException;
import com.gridnode.pdip.base.userprocedure.exceptions.UserProcedureExecutionException;
import com.gridnode.pdip.framework.exceptions.SystemException;


public interface IUserProcedureServiceManagerObj extends EJBObject
{

  /**
   * This method is essentially responsible to PRE/EXECUTE/POST process the given
   * UserProcedure. The Collection of GridDocuments are iterated and executed.
   *
   * @param gridDocs - A Collection of GridDocuments
   * @param userProcedureUID - UID of UserProcedure Entity.
   * @return - Collection of GridDocuments.
   * @throws InvalidParamDefException - thrown when improper ParamDefinition specified.
   * @throws UserProcedureException - thrown when this UserProcedure Cannot be executed.
   * @throws InvalidReturnDefException - thrown when improper ReturnDefinition specified.
   * @throws UserProcedureExecutionException - thrown when UserProcedure Executing engine cannot execute.
   * @throws RemoteException - EJB-Remote(Stub-Exception).
   */

   public Collection performUserProcedure(Collection gridDocs,Long userProcedureUID)
     throws    InvalidParamDefException,
               UserProcedureException,
               InvalidReturnDefException,
               UserProcedureExecutionException,
               SystemException,
               RemoteException;

   public void performUserProcedure(String userProcedureName)
     throws    InvalidParamDefException,
               UserProcedureException,
               InvalidReturnDefException,
               UserProcedureExecutionException,
               SystemException,
               RemoteException;
}
