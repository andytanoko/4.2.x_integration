/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTActionMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-11     Andrew Hill         Created
 * 2003-01-16     Andrew Hill         Change pwlRefreshInterval default to 0 from 30000
 * 2003-01-24     Andrew Hill         Added deferFormCreation, populate property
 * 2003-03-26     Andrew Hill         Factored out pwl attributes to GTWatchListActionMapping
 * 2003-04-25     Andrew Hill         logParameters flag
 * 2003-05-02     Andrew Hill         requiresGtSession flag & javadocs
 * 2003-08-18     Andrew Hill         Check if frozen and throw exception on set
 * 2005-03-17     Andrew Hill         Added the requiresAdmin property
 * 2006-04-24     Neo Sok Lay         Added the requiresP2P and requiresUDDI properties
 */
package com.gridnode.gtas.client.web.strutsbase;


/*
 * Extension of ActionMapping that adds extra properties that are needed by the pTier 
 * to support OperationContext use, RenderingAction chaining, etc...
 */
public class GTActionMapping extends org.apache.struts.action.SessionActionMapping
{
  private boolean _deferFormCreation = false; //20030124AH
  private boolean _populate = true; //20030124AH
  private boolean _logParameters = false; //20030425AH
  private boolean _requiresGtSession = true; //20030502AH
  private boolean _requiresAdmin = false; //20050317AH
  private boolean _requiresP2P = false; //NSL20060424
  private boolean _requiresUDDI = false; //NSL22060424

  /**
   * Method setDeferFormCreation.
   * @param dontAutomaticallyCreateTheActionFormPlease
   */
  /*
   * Set to true to prevent struts automatically creating an ActionForm instance.
   * Normally struts will create the ActionForm instance for us based on the properties set
   * in the mapping but in many cases (such as Actions that make use of OperationContext) we do
   * not want to do this, but would rather create the form ourselves after doing some additional
   * processing. Set this flag to true to prevent struts from creating the ActionForm. (nb: the
   * responsibility for creating it becomes yours if you set this). The default value for this
   * in a GTActionMapping is false. For further information see the javadocs for the processActionForm()
   * method in GTRequestProcessor.
   * @param deferCreation
   */
  public void setDeferFormCreation(boolean dontAutomaticallyCreateTheActionFormPlease)
  {
    if (configured)
    { //20030818AH
      throw new IllegalStateException("Configuration is frozen");
    }
    _deferFormCreation = dontAutomaticallyCreateTheActionFormPlease;
  } //20030124AH

  /*
   * Return true if form creation is deferred and not the responsibility of struts, false for
   * normal struts form creation.
   * @return deferFormCreation
   */
  public boolean isDeferFormCreation()
  { return _deferFormCreation; } //20031224AH

  /*
   * Set to true to allow struts to populate the ActionForm. This is the default, however for certain
   * (chained) actions such as the RenderingAction, we dont want this. Chaining actions is not
   * usually a very good idea, however in the case of the rendering action, the benefits seem to
   * outweigh the costs. For such actions set the flag to false so that the ActionForm wont be
   * repopulated. One thing to note is that the values in a MultipartRequestWrappers parameters
   * are also populated - and so this flag will also disable that if set to false.
   * Default value is true.
   * For further information see the javadocs for the processPopulate()
   * method in GTRequestProcessor.
   * @param populateForm
   */
  public void setPopulate(boolean autoPopulateFormFromRequest)
  {
    if (configured)
    { //20030818AH
      throw new IllegalStateException("Configuration is frozen");
    }
    _populate = autoPopulateFormFromRequest;
  } //20030124AH

  /*
   * Returns true if this Action allows normal struts form population to proceed. 
   */
  public boolean isPopulate()
  { return _populate; } //20030124AH
  
  /*
   * Useful for debugging specific Actions by writing info on request parameters to the log.
   * Set to true to log parameters and some other information about the request. Default is false.
   * @param logRequest
   */
  public void setLogParameters(boolean logParameters)
  { //20030425AH
    if (configured)
    { //20030818AH
      throw new IllegalStateException("Configuration is frozen");
    }
    _logParameters = logParameters;
  }

  /*
   * Return true if we are logging request info for requests that hit this Action.
   * @return loggingRequest 
   */
  public boolean isLogParameters()
  { //20030425AH
    return _logParameters;
  }

  /*
   * Indicate if this action requires a valid (though not necessarily authenticated) IGTSession to work.
   * If set to true (the default) then a check will be made before the action is executed and if
   * no IGTSession is found, a NoSessionException thrown. (This may be caught by the GTExceptionHandler
   * which will forward to the login page for such exceptions).
   * Be sure to mark the DisplayExceptionAction, RenderingAction, Login and PreLogin Actions and
   * such like as false for this flag!
   * @param requiresGtSession
   */
  public void setRequiresGtSession(boolean requiresGtSession)
  { //20030502AH
    if (configured)
    { //20030818AH
      throw new IllegalStateException("Configuration is frozen");
    }
    _requiresGtSession = requiresGtSession;
  }
  
  /*
   * Returns true if this Action needs an IGTSession instance.
   * @return needsGtSession
   */
  public boolean isRequiresGtSession()
  { //20030502AH
    return _requiresGtSession;
  }
  
  /**
   * Returns true if the action requires the current user to have admin priveledges
   * as defined by the isAdmin() method of the IGTSession instance
   * @return requiresAdmin
   * 
   */
  public boolean isRequiresAdmin()
  { //20050317AH
    return _requiresAdmin;
  }

  /**
   * Set to try to enforce check that the user has admin priveledges. If they dont
   * then the RP will throw an exception. By default the value is false.
   * @param requiresAdmin
   */
  public void setRequiresAdmin(boolean requiresAdmin)
  { //20050317AH
    _requiresAdmin = requiresAdmin;
  }

	/**
	 * @return Returns the requiresP2P.
	 */
	public boolean isRequiresP2P()
	{
		return _requiresP2P;
	}

	/**
	 * @param requiresP2P The requiresP2P to set.
	 */
	public void setRequiresP2P(boolean requiresP2P)
	{
		_requiresP2P = requiresP2P;
	}

	/**
	 * @return Returns the requiresUDDI.
	 */
	public boolean isRequiresUDDI()
	{
		return _requiresUDDI;
	}

	/**
	 * @param requiresUDDI The requiresUDDI to set.
	 */
	public void setRequiresUDDI(boolean requiresUDDI)
	{
		_requiresUDDI = requiresUDDI;
	}

  
}