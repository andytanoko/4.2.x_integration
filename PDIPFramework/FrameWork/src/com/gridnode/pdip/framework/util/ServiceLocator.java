/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceLocator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 16 2002    Ang Meng Hua        Ceated
 * May 02 2002    Neo Sok Lay         Change HashMap to Map. Cannot cast to
 *                                    HashMap for Collections.synchronizedMap.
 *                                    Combine the functionality from
 *                                    j2ee.ServiceLookup.
 * May 04 2002    Ang Meng Hua        Construct ServiceLocatorException with
 *                                    meaningful message
 *
 * Jul 30 2002    Jagadeesh           Modify all reference of ConfigManager to
 *                                    use new ConfigurationManager.Restructur
 *                                    the Configuration Constants to IFrameworkConfig.
 * Oct 31 2005    Neo Sok Lay         Load properties for local jndi lookups.    
 *                                    Add getJNDIFinder() method.         
 * Feb 06 2007    Neo Sok Lay         Add 'use.explicit.jndi' in local jndi properties.                                                         
 *                                    Add getJNDIFinder() method.   
 * Feb 07 2007		Alain Ah Ming				Log warning message if throwing up exception
 * Apr 09 2007		Alain Ah Ming				Remove catching of NameNotFoundException from getHome(String)
 * 																			as it's not being thrown                                                               
 */
package com.gridnode.pdip.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.NamingException;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.log.Log;

/**
* A general purpose service locator for enterprise bean components.
*
* @author Ang Meng Hua
* @since 2.0 build 2.0.1
* @version GT 4.0 VAN
*/

public class ServiceLocator
{
   //public static String LOCAL_CONTEXT  = "_LOCAL";
   public static String LOCAL_CONTEXT  = IFrameworkConfig.FRAMEWORK_LOCALJNDI_CONFIG;
   public static String CLIENT_CONTEXT = IFrameworkConfig.FRAMEWORK_JNDI_CONFIG;
   //"j2ee" + File.separatorChar + "j2ee.properties";

   private static final String LOCAL_JNDI_PREFIX = "local.jndi.prefix";
   private static final String USE_EXPLICIT_JNDI = "use.explicit.jndi";
   
   private static Hashtable _services = new Hashtable();

   //private String _configName=IFrameworkConfig.FRAMEWORK_JNDI_CONFIG;
   //   private String _configName="default";
   private JNDIFinder _jndiFinder = null;
   private Map _homes;
   private String _localJndiPrefix = "";

   /**
   * This private constructor sets the default JNDIFinder to use and
   * initialises the service cache. The default JNDI finder assumes the
   * local intial context. Client can change the default by calling the
   * setJNDIFinder method.
   *
   * The JNDIFinder class is just a wrapper around JNDI functionality such
   * as object lookups.
   */
   private ServiceLocator(String configName) throws
    ServiceLookupException
   {
     try
     {
       //this._configName = configName;
       this._homes = Collections.synchronizedMap(new HashMap());

       Log.debug("LOOKUP]", "[ServiceLocator] Instantiating ServiceLocator with config " + configName);
       Configuration configManager = ConfigurationManager.getInstance().getConfig(configName);
       if (LOCAL_CONTEXT.equals(configName))
       {
         //if use.explicit.jndi is set to true, the jndi properties set in the file will be used for local context
         boolean useExplicit = configManager == null ? false : configManager.getBoolean(USE_EXPLICIT_JNDI, false);
         this._jndiFinder = useExplicit ? new JNDIFinder(configManager.getProperties()) : new JNDIFinder();
         _localJndiPrefix = configManager==null? "" : configManager.getString(LOCAL_JNDI_PREFIX, "");
         Log.debug("[ServiceLocator.<init>]", "localJndiPrefix="+_localJndiPrefix);
       }
       else
       {
         //Log.debug("LOOKUP]", "[ServiceLocator] Instantiating ServiceLocator with config " + configName);
         //Configuration configManager = ConfigurationManager.getInstance().getConfig(configName);
//         ConfigManager configManager = ConfigManager.getInstance(configName);
         setJNDIFinder(new JNDIFinder(configManager.getProperties()));
       }
     }
     catch (NamingException ex)
     {
       throw new ServiceLookupException("JNDIFinder initialisation failure", ex);
     }
   }

   /**
   * Returns an singleton instance of the ServiceLocator, using local context.
   *
   * @exception ServiceLookupException Error in setting the initial context of
   * the lookup.
   */
   public static ServiceLocator instance() throws
    ServiceLookupException
   {
     return instance(LOCAL_CONTEXT);
   }

   /**
   * Returns an singleton instance of the ServiceLocator, using a specific
   * context.
   *
   * @param configName The name of the configuration to load the context properties.
   *
   * @exception ServiceLookupException Error in setting the initial context of
   * the lookup.
   */
   public static ServiceLocator instance(String configName)
     throws ServiceLookupException
   {
     if (configName==null)
       configName=LOCAL_CONTEXT;

     if (_services.get(configName)==null)
     {
       _services.put(configName, new ServiceLocator(configName));
    }
     return (ServiceLocator)_services.get(configName);
  }

   public Object getHome(String jndiName) throws ServiceLookupException
   {
     try
     {
    	 Log.debug("[ServiceLocator.getHome]", "Lookup jndiName = "+jndiName);
     	//NSL20051031 Hack to put in prefix for local ejbs
     	if (jndiName.endsWith("LocalHome")) {
     		jndiName = _localJndiPrefix + jndiName;
     	  Log.debug("[ServiceLocator.getHome]", "Change Lookup jndiName to "+jndiName);
     	}
       Object obj = _homes.get(jndiName);
       if (obj == null)
       {
         obj = _jndiFinder.lookup(jndiName);
         _homes.put(jndiName, obj);
       }
       return obj;
     }
     catch (javax.naming.NamingException ex)
     {
     	Log.warn("[ServiceLocator.getHome]", "NamingException: ", ex);
       throw new ServiceLookupException(
         "Fail to locate home with JNDI name :" + jndiName, ex);
     }
   }
   /** Method to get the home interface for an EJB with
   * the specified JNDI name.  The method it uses for lookup
   * is through a helper JNDIFinder object.
   *
   * @param jndiName  The name of the EJB home to get
   * @param homeClass The class object of the EJB home
   *
   * @return An Object that is the home interface
   * @throws javax.naming.NameNotFoundException if the
   *         name does not exists in JNDI.  Also throws
   *         javax.naming.NamingException if there is
   *         a problem in looking up the name.
   *
   */
   public Object getHome(String jndiName, Class homeClass) throws
    ServiceLookupException
   {
      try
      {
     	 Log.debug("[ServiceLocator.getHome]", "Lookup jndiName = "+jndiName);
      	//NSL20051031 Hack to put in prefix for local ejbs
      	if (jndiName.endsWith("LocalHome")) {
      		jndiName = _localJndiPrefix + jndiName;
       	  Log.debug("[ServiceLocator.getHome]", "Change Lookup jndiName to "+jndiName);
      	}
        Object obj = _homes.get(jndiName);
        if (obj == null)
        {
          obj = _jndiFinder.lookup(jndiName, homeClass);
          _homes.put(jndiName, obj);
        }
        return obj;
      }
      catch (javax.naming.NameNotFoundException ex)
      {
      	Log.warn("[ServiceLocator.getHome]", "NameNotFoundException: ", ex);
        throw new ServiceLookupException(
          "Fail to locate home with JNDI name :" + jndiName, ex);
      }
      catch (javax.naming.NamingException ex)
      {
      	Log.warn("[ServiceLocator.getHome]", "NamingException: ", ex);
        throw new ServiceLookupException(
          "Fail to locate home with JNDI name :" + jndiName, ex);
      }
   }

   /**
    * This method will return home object ,class name is used as the JNDI name
    */
   public Object getHome(Class homeClass) throws ServiceLookupException 
   {
  	 return getHome(homeClass.getName(), homeClass);
   }

   /** Method to create an enterprise bean.  Right now the
   * Vector of params is getting reflected to determine their
   * class type.
   * This might be an expensive operation and so we might
   * want to use something more efficient in the future to
   * speed things up.  The important thing to remember is
   * that ORDER IS IMPORTANT for these parameters.  Otherwise
   * if you have a create method that takes multiple parame-
   * ters of the same type, then you will get the method
   * invocation wrong.
   *
   * @param jndiName  The JNDI name of the EJB
   * @param homeClass The class object of the EJB home
   * @param params    A vector containing the arguments to
   * the create method.
   *
   * @return An EJBObject that references the remote inter
   * face for the specified EJB runtime instance if local is false,
   * otherwise retuen an EJBLocalObject.
   *
   * @throws InvocationTargetException or IllegalAccessExcep-
   * tion if the method has trouble invoking the create
   * method.  See the java.lang.reflect.Method class for
   * details on these exceptions.  Also throws javax.naming.*
   * exceptions if there is a lookup failure of the home
   * interface of the specified EJB.   A RemoteException is
   * thrown per standard EJB rules.  Throw a generic excep-
   * tion so that any subclasses (e.g. wrappers) can  throw
   * their own exceptions.
   */
   public Object getObj(
    String jndiName,
    Class homeClass,
    Object [] params) throws
     ServiceLookupException
   {
      try
      {
        Object bean = null;

        // Get the home interface and its associated Class
        // object.  We need the Class object for reflection...
        //
        Object homeInterface = getHome(jndiName, homeClass);
        Class beanClass = homeInterface.getClass();

        // Need a class array for the method lookup...
        //
        Class[] signature = getSignature(params);

        // Look up the method--throw exception if method not
        // there.
        // All create methods in the home interface are named
        // "create" so search for the create method with the
        // appropriate  signature
        //
        Method createMethod = beanClass.getDeclaredMethod("create",  signature);

        // This is the key.  Here homeInterface is a reference
        // to a REAL remote object -- i.e. the skeleton class
        // on the server.  The invocation is done on this spe-
        // cific instance of the remote object created by the
        // EJB container on the server.
        //
        bean = createMethod.invoke(homeInterface, params);
        return bean;
      }
      catch (InvocationTargetException ex)
      {
         throw new ServiceLookupException(
          "Fail to create EJBObject with Home JNDI name :" + jndiName, ex);
      }
      catch (IllegalAccessException ex)
      {
         throw new ServiceLookupException(
          "Fail to create EJBObject with Home JNDI name :" + jndiName, ex);
      }
      catch (NoSuchMethodException ex)
      {
         throw new ServiceLookupException(
          "Fail to create EJBObject with Home JNDI name :" + jndiName, ex);
      }
       catch (Exception ex)
      {
         throw new ServiceLookupException(
          "Fail to create EJBObject with Home JNDI name :" + jndiName, ex);
      }
   }

//   public void invalidate() throws ServiceLookupException
//   {
//     Log.debug("LOOKUP", "[ServiceLocator.invalidate] Invalidating Service Locator ....");
//     try
//     {
//       _jndiFinder.close();
//       _services.remove(_configName);
//     }
//     catch (NamingException ex)
//     {
//       throw new ServiceLookupException(
//          "Fail to invalidate ServiceLocator with config name:" + _configName, ex);
//     }
//   }

   public void invalidate()
   {
     Log.debug("LOOKUP", "[ServiceLocator.invalidate] Invalidating Service Locator ....");
     _homes.clear();
   }

   /** Method to construct an array of Class objects repre-
   * senting a method signature
   *
   * @param parameters  A vector whose elements will be
   * reflected or their specified Classes.
   * @return A Class array
   *
   */
   private Class[] getSignature(Object [] parameters)
   {
      Class sig[] = new Class[parameters.length];

      for (int i =0; i<sig.length; i++)
      {
        sig[i] = parameters[i].getClass();
      }
      return sig;
   }

   /** Method to set the internal JDNIFinder variable */
   private void setJNDIFinder(JNDIFinder finder)
   {
      _jndiFinder = finder;
   }
   
   /**
    * Get the JNDIFInder used by the ServiceLocator. Usage of the returned JNDIFinder should be limited
    * to lookups. Clients making use of the returned JNDIFinder should not attempt to close the JNDIFinder.
    * @return the JNDIFinder used by the ServiceLocator instance
    */
   public JNDIFinder getJndiFinder()
   {
  	 return _jndiFinder;
   }
}
