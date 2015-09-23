/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: KeyGen.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * ??? ?? ????    Mahesh              Created
 * Jul 08 2002    Neo Sok Lay         Add method to generate next Id, reject
 *                                    if contained in a specified set.
 * Oct 31 2003    Neo Sok Lay         Fix defect GNDB00015778:-
 *                                      Concurrency issue for getNextId()
 * Nov 02 2005    Neo Sok Lay         Get NextId from KeyGenBean instead of KeyGeneratorBean
 * Dec 22 2005    Neo Sok Lay         Use config property to decide whether to use
 *                                    entitybean or session bean implementation.                                     
 * Nov 06 2006    Tam Wei Xiang       Fix defect GNDB00027928. 
 *                                    modified method getNextId(.. , ,,)
 * Feb 07 2007		Alain Ah Ming				Log warning message if throwing up exception                                                                       
 */
package com.gridnode.pdip.framework.db.keygen;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.log.Log;
import com.gridnode.pdip.framework.util.ServiceLocator;

import javax.ejb.FinderException;

/**
 * This is the "facade" for generating unique ids for specific context.
 * 
 * @version GT 4.0 VAN
 * @since GT 2.1
 */
public class KeyGen
{
	private static boolean _useEntityBean = true;
  public KeyGen()
  {
  }

  /**
   * Generate nextId for a specific context (arbitrary value).
   * 
   * @param context The context to generate nextId.
   * @return The generated nextId.
   * @throws Exception Error generating nextId.
   */
  public static Long getNextId(String context) throws Exception
  {
    return getNextId(context, true);
  }

  /**
   * Generate nextId for a specific context (arbitrary value), rejecting
   * those that are specified in <code>excludeIdSet</code>.
   * 
   * @param context The context to generate nextId.
   * @param excludeIdSet The set of Ids to reject if generated. Can be <b>null</b>
   * if not applicable.
   * @return The generated nextId.
   * @throws Exception Error generating nextId.
   */
  /*
  public static Long getNextId(String context, Collection excludeIdSet)
    throws Exception
  {
    return getNextId(context, excludeIdSet, true);
  }*/
  
  /**
   * TWX 06112006 Modified to invoke the KeyGeneratorBean directly.
   * 
   * Generate nextId for a specific context (arbitrary value).
   * 
   * @param context The context to generate nextId.
   * @param isLocal Access the KeyGeneratedBean from Local/Remote Jndi context.
   * @return The generated nextId.
   * @throws Exception Error generating nextId.
   */
  public static Long getNextId(String context, boolean isLocal)
    throws Exception
  {
    return getNextId(context, isLocal, null);
    /*
	  try
	    {
	      IKeyGeneratorHome home =
	        (IKeyGeneratorHome) ServiceLocator
	          .instance(
	            ((isLocal)
	              ? ServiceLocator.LOCAL_CONTEXT
	              : ServiceLocator.CLIENT_CONTEXT))
	          .getHome(IKeyGeneratorHome.class.getName(), IKeyGeneratorHome.class);

	      IKeyGeneratorObj keyGenerator = null;
	      try
	      {
	        keyGenerator = home.findByPrimaryKey(context);
	      }
	      catch (FinderException fEx)
	      {
	        Log.log(
	          Log.DB,
	          "[KeyGen.getNextId] FinderException in getting next UId for "
	            + context
	            + ",Creating entry for "
	            + context);
	        keyGenerator = home.create(context, 0);
	      }
	      return new Long(keyGenerator.getNextId());
	    }
	    catch (Exception ex)
	    {
	      Log.err(
	        Log.DB,
	        "[KeyGen.getNextId] Error in getting next UId for " + context,
	        ex);
	      throw ex;
	    }
      */ 
  }

  /**
   * Get the nextId for a context, excluding generated ids from a specified set.
   * 
   * @param context The Context to generate nextId for.
   * @param excludeIdSet The set of Ids to be rejected if the generated Id is
   * in the set. That means, the generation will continues until the generated
   * nextId does not exists in the specified excludeIdSet. 
   * @param isLocal Whether the client is accessing from local/remote Jndi context.
   * @return The generated nextId.
   * @throws Exception Error while generating nextId.
   */
  /*
  public static synchronized Long getNextId(
    String context,
    Collection excludeIdSet,
    boolean isLocal)
    throws Exception
  {
    try
    {
    	if (_useEntityBean)
    	{
        IKeyGeneratorHome home =
          (IKeyGeneratorHome) ServiceLocator
            .instance(
              ((isLocal)
                ? ServiceLocator.LOCAL_CONTEXT
                : ServiceLocator.CLIENT_CONTEXT))
            .getHome(IKeyGeneratorHome.class.getName(), IKeyGeneratorHome.class);

        IKeyGeneratorObj keyGenerator = null;
        try
        {
          keyGenerator = home.findByPrimaryKey(context);
        }
        catch (FinderException fEx)
        {
          Log.log(
            Log.DB,
            "[KeyGen.getNextId] FinderException in getting next UId for "
              + context
              + ",Creating entry for "
              + context);
          keyGenerator = home.create(context, 0);
        }
        return new Long(keyGenerator.getNextId(excludeIdSet));
    	}
    	else
    	{
	    	IKeyGenObj keyGenObj = (IKeyGenObj)ServiceLocator.instance(
	    	                    isLocal?ServiceLocator.LOCAL_CONTEXT:ServiceLocator.CLIENT_CONTEXT)
	    	                    .getObj(IKeyGenHome.class.getName(),
	    	                            IKeyGenHome.class,
	    	                            new Object[0]);
	
	    	return new Long(keyGenObj.getNextId(context, excludeIdSet));
    	}
    }
    catch (Exception ex)
    {
      Log.err(
        Log.DB,
        "[KeyGen.getNextId] Error in getting next UId for " + context,
        ex);
      throw ex;
    }
  } */
  
/**
   * TWX 09112006 By passing in the currentMaxId in a particular table, we will generate the next available ID. 
   * The new generated ID will also be updated into the ref num table.
   * @param context The context to generate nextId.
   * @param isLocal Access the KeyGeneratedBean from Local/Remote Jndi context.
   * @param currentMaxId The current Max ID from a particular table. If <b>null</b>, will be the same
   * as <code>getNextId(context, isLocal)</code>.
   */
  public static Long getNextId(String context, boolean isLocal, Long currentMaxId) throws Exception
  {
	  Log.debug(Log.DB, "getNextId(), currentMaxId="+currentMaxId);
    /*
	  if(currentMaxPK == null)
	  {
		  throw new IllegalArgumentException("currentMaxPK cannot be null !");
	  }*/
    
	  try 
	  {
      if (_useEntityBean)
      {
        IKeyGeneratorHome home =
          (IKeyGeneratorHome) ServiceLocator
            .instance(
              ((isLocal)
                ? ServiceLocator.LOCAL_CONTEXT
                : ServiceLocator.CLIENT_CONTEXT))
            .getHome(IKeyGeneratorHome.class.getName(), IKeyGeneratorHome.class);
  
        IKeyGeneratorObj keyGenerator = null;
        try
        {
          keyGenerator = home.findByPrimaryKey(context);
        }
        catch (FinderException fEx)
        {
          Log.log(
            Log.DB,
            "[KeyGen.getNextId] FinderException in getting next UId for "
              + context
              + ",Creating entry for "
              + context);
          keyGenerator = home.create(context, 0);
        }
        
        if (currentMaxId != null)
        {
          return new Long(keyGenerator.getNextId(currentMaxId.longValue()));
        }
        else
        {
          return new Long(keyGenerator.getNextId());
        }
      }
      else
      {
        IKeyGenObj keyGenObj = (IKeyGenObj)ServiceLocator.instance(
                            isLocal?ServiceLocator.LOCAL_CONTEXT:ServiceLocator.CLIENT_CONTEXT)
                            .getObj(IKeyGenHome.class.getName(),
                                    IKeyGenHome.class,
                                    new Object[0]);
  
        return new Long(keyGenObj.getNextId(context, currentMaxId));
      }
	  }
	  catch (Exception ex)
	  {
      Log.warn(
        Log.DB,
        "[KeyGen.getNextId] Error in locating Key generator for " + context,
        ex);
      throw ex;
	  } 
  }
  
  static
  {
    try
    {
      Configuration dbConfig = ConfigurationManager.getInstance().getConfig(IFrameworkConfig.FRAMEWORK_DATABASE_CONFIG);
      _useEntityBean = dbConfig.getBoolean(IFrameworkConfig.CMP_ON , true);
    }
    catch (Exception ex)
    {
    	Log.error(ILogErrorCodes.CONFIGURATION_LOAD, 
    	          Log.DB, 
    	          "[KeyGen.isUseEntityBean] Unable to load config. Setting cmp.on to true. Unexpected Error: "+ex.getMessage(), 
    	          ex);
    }
  }

}