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
 * Jun 10 2002   Mahesh	      Created
 * Jun 13 2002   Mathew         Repackaged
 */

package com.gridnode.pdip.app.deploy.manager.exceptions;

import com.gridnode.pdip.framework.exceptions.ApplicationException;

public class DeploymentException
  extends ApplicationException
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1150469181171522489L;

	/**
   * Creates a new DefinitionParserException object.
   *
   * @param reason DOCUMENT ME!
   */
  public DeploymentException(String reason)
  {
    super(reason);
  }

  /**
   * Creates a new DefinitionParserException object.
   *
   * @param ex DOCUMENT ME!
   */
  public DeploymentException(Throwable ex)
  {
    super(ex);
  }

  /**
   * Creates a new DefinitionParserException object.
   *
   * @param reason DOCUMENT ME!
   * @param ex DOCUMENT ME!
   */
  public DeploymentException(String reason, Throwable ex)
  {
    super(reason, ex);
  }
}