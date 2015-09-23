package com.gridnode.pdip.framework.config;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.log4j.helpers.*;

/**
 * @deprecated use ConfigurationManager instead */
public class ConfigManager{

    private static Hashtable configList=new Hashtable();

    public static String CONFIG_DIR=System.getProperty("user.dir")+
                                    File.separatorChar+"conf"+
                                    File.separatorChar;

    Properties prop=new Properties();

    public static ConfigManager getInstance(){
        return getInstance("default",null);
    }

    public static ConfigManager getInstance(String configName){
        return getInstance(configName,null);
    }

    public static ConfigManager getInstance(String configName,boolean isRemote){
        if(isRemote) return getRemoteInstance(configName);
        else return getInstance(configName,null);
    }

    public static ConfigManager getInstance(String configName,Locale locale){
        if(configList.get(configName)==null){
            configList.put(configName,new ConfigManager(configName,locale));
        }
        return (ConfigManager)configList.get(configName);
    }

    public static ConfigManager getRemoteInstance(String configURL){
        if(configList.get(configURL)==null){
            configList.put(configURL,new ConfigManager(configURL));
        }
        return (ConfigManager)configList.get(configURL);
    }

    private ConfigManager(String configURL){
        try{
            ResourceBundle resourceBundle=(ResourceBundle)new PropertyResourceBundle(new URL(configURL).openStream());
            if(resourceBundle!=null){
                loadProperties(resourceBundle);
            }
        }catch(Exception e){
            LogLog.warn("Exception in Loading Remote ConfigManager :"+configURL,e);
            e.printStackTrace();
        }
    }


    private ConfigManager(String configName,Locale locale){
        try{
            ResourceBundle resourceBundle =null;
            if(locale!=null)
                resourceBundle=ResourceBundle.getBundle(configName,locale);
            else {
                configName+=(configName.indexOf(".")==-1)?".properties":"";
                resourceBundle=(ResourceBundle)new PropertyResourceBundle(new FileInputStream(CONFIG_DIR+configName));
            }
            if(resourceBundle!=null){
                loadProperties(resourceBundle);
            }
        }catch(Exception e){
            LogLog.warn("Exception in Loading ConfigManager :"+configName,e);
            e.printStackTrace();
        }
    }

    private void loadProperties(ResourceBundle resourceBundle){
        Enumeration keys=resourceBundle.getKeys();
        while(keys.hasMoreElements()){
            String key=(String)keys.nextElement();
            prop.put(key,resourceBundle.getString(key));
        }
    }

    public String get(String key){
        try{
          return (String)prop.get(key);
        }catch(Exception e){
            LogLog.warn("Exception in getting config property :"+key,e);
        }
        return null;
    }


    public Properties getProperties(){
        return prop;
    }

    public String toString(){
        StringWriter stringWriter=new StringWriter();
        prop.list(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    }