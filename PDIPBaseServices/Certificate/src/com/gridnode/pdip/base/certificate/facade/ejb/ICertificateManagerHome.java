/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICertificateManagerHome
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 03-July-2002    Jagadeesh           Created.
 */


package com.gridnode.pdip.base.certificate.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;
import java.rmi.RemoteException;



public interface ICertificateManagerHome extends EJBHome
{
  public ICertificateManagerObj create()
         throws CreateException,RemoteException;

}