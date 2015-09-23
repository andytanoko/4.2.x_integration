/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002-2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceLookup.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 *
 *
 * Jul 30 2002    Jagadeesh           Modify all reference of ConfigManager to
 *                                    use new ConfigurationManager.Restructur
 *                                    the Configuration Constants to IFrameworkConfig.
 * Feb 07 2007		Alain Ah Ming				Use new error codes                                   
 */
package com.gridnode.pdip.framework.j2ee;

import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.config.IFrameworkConfig;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.log.Log;

/**
 * This class is used to do JNDI lookup.
 * @version GT 4.0 VAN
 */
public class ServiceLookup {

    private static Hashtable services=new Hashtable();

    public static String LOCAL_CONTEXT="_LOCAL";
    public static String CLIENT_CONTEXT=IFrameworkConfig.FRAMEWORK_JNDI_CONFIG;
//    "j2ee"+File.separatorChar+"j2ee.properties";

    //private static String FACTORY="pdip.jndi.factory";
    //private static String HOST="pdip.jndi.url";
    //private static String USER="pdip.jndi.user";
    //private static String PASSWORD="pdip.jndi.password";

    private String configName=IFrameworkConfig.FRAMEWORK_JNDI_CONFIG;
    private InitialContext context =null;

    private ServiceLookup(String configName) {
        this.configName=configName;
        setInitialContext();
    }


    public static ServiceLookup getInstance(){
        return getInstance(LOCAL_CONTEXT);
    }

    public static ServiceLookup getInstance(String configName){
        if(configName==null)
            configName=LOCAL_CONTEXT;

        if(services.get(configName)==null){
            services.put(configName,new ServiceLookup(configName));
        }

        return (ServiceLookup)services.get(configName);
    }

    /**
     * This method will do jndi lookup to server specified in the properties file
     */
    public Object lookup(String jndiName){
        try{
           return context.lookup(jndiName);
        }catch(Exception e){
           Log.error(Log.FRAMEWORK,"Error in lookup of "+jndiName,e);
        }
        return null;
    }

    /**
     * This method will return home object ,class name is used as the JNDI name
     */
    public Object getHome(Class homeClass) {
        return getHome(homeClass.getName(), homeClass);
    }

    /**
     * This method will return home object
     */
    public Object getHome(String jndiName,Class homeClass) {
    	String mn = "getHome";
        try{
            Object home=lookup(jndiName);
            return PortableRemoteObject.narrow(home, homeClass);
        }catch(Throwable e){
            logError(ILogErrorCodes.SERVICE_LOOKUP_GENERIC, mn ,"Unexpected error: "+e.getMessage(),e);
        }
        return null;
    }

    /**
     * This method will return initial context
     */
    public InitialContext getInitialContext(){
        return context;
    }

    /**
     * This method loads the config data from the properties file
     * and creates the InitialContest object
     */
    public void setInitialContext()
    {
    	String mn = "setInitialContext";
        try{
            if(context==null){
                if(configName.equals(LOCAL_CONTEXT)){
                    context=new InitialContext();
                    return;
                }else {
                  Configuration configManager = ConfigurationManager.getInstance().getConfig(configName);
//                    ConfigManager configManager=ConfigManager.getInstance(configName);
                    System.out.println("Config "+configManager.toString());
                    context=new InitialContext(configManager.getProperties());
                }
            }
        }catch(Exception e){
            logError(ILogErrorCodes.SERVICE_LOOKUP_GENERIC, mn,"Failed to create initial context. Error: "+e.getMessage(),e);
        }
    }
    
    private static void logError(String errorCode, String methodName, String msg, Throwable t)
    {
    	StringBuffer buf = new StringBuffer("[");
    	buf.append(ServiceLookup.class.getSimpleName()).append(".").append(methodName).append("] ").append(msg);
    	Log.error(errorCode, Log.FRAMEWORK, buf.toString(), t);
    }
}