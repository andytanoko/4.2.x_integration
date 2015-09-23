/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 25 2002   MAHESH              Created
 */
package com.gridnode.pdip.framework.util;

import java.util.*;

public class KeyConverter {

  /**
   * Extracts uid from the key
   * @param key String
   * @return Long uid from the key
   */
  public static Long getUID(String key){
    String tempStr=null;
    StringTokenizer strTok=new StringTokenizer(key,"/");
    if(strTok.hasMoreTokens()) strTok.nextToken();
    if(strTok.hasMoreTokens()) strTok.nextToken();
    if(strTok.hasMoreTokens()){
        tempStr=strTok.nextToken();
        return tempStr.equals("null")?null:(new Long(tempStr));
    } else return null;
  }

  /**
   * Froms the key from the parameters
   * @param uId
   * @param name
   * @param type
   * @return key
   */
  public static String getKey(Long uId,String name,String type){
    return "http://"+type+"/"+uId+"/"+name;
  }

  /**
   * Froms the key from the parameters
   * @param name
   * @param type
   * @return key
   */
  public static String getKey(String name,String type){
    return "http://"+type+"/"+name+"/";
  }

  /**
   * Extracts name from key
   * @param key
   * @return entity name
   */
  public static String getEntityName(String key){
    StringTokenizer strTok=new StringTokenizer(key,"/");
    if(strTok.hasMoreTokens()) strTok.nextToken();
    if(strTok.hasMoreTokens()) strTok.nextToken();
    if(strTok.hasMoreTokens()) strTok.nextToken();
    if(strTok.hasMoreTokens()){
        return strTok.nextToken();
    } else return null;

  }

  /**
   * Extracts entity type from key
   * @param key
   * @return entity type
   */
  public static String getType(String key){
    StringTokenizer strTok=new StringTokenizer(key,"/");
    if(strTok.hasMoreTokens()) strTok.nextToken();
    if(strTok.hasMoreTokens()){
        return strTok.nextToken();
    } else return null;
  }

  /**
   * Extracts the token at specified index
   * @param key
   * @param index
   * @return String
   */
  public static String getValue(String key,int index){
    StringTokenizer strTok=new StringTokenizer(key,"/");
    int loop=0;
    for(loop=0;loop<index && strTok.hasMoreTokens();loop++)
        strTok.nextToken();
    if(strTok.hasMoreTokens() && loop==index )
        return strTok.nextToken();
    else return null;
  }

  /**
   * Modifies the key with the new value at the specified index
   * @param key
   * @param index index where the old value to be replaced
   * @param value new value
   * @return modified key
   */
  public static String getKey(String key,int index,String value){
    StringTokenizer strTok=new StringTokenizer(key,"/");
    String tempKey="";
    for(int loop=0;strTok.hasMoreTokens();loop++){
        if(loop==index){
            strTok.nextToken();
            tempKey+=value;
        } else tempKey+=strTok.nextToken();
        if(loop==0)
            tempKey+="/";
        if(strTok.hasMoreTokens())
            tempKey+="/";
    }
    return tempKey;
  }
}