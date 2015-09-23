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

package com.gridnode.pdip.framework.util;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;


public class UtilString
{
	private static final String DEF_ENCODING = "UTF-8";
    public static String replace(String src, String searchStr, String replaceStr)
    {
        int index = 0;
        int searchStrLen = searchStr.length();
        //StringBuffer sb = new StringBuffer();

        while ((index = src.indexOf(searchStr)) > -1)
        {
            src = src.substring(0, index) + replaceStr +
                    src.substring(index + searchStrLen);
        }
        return src;
    }

    /**
     * Creates a string seperated by delimimiter from a List of strings
     *
     * @param list a list of strings to join
     * @param delim the delimiter character(s) to use. (null value will join
     *        with no delimiter)
     * @return a String of all values in the list seperated by the delimiter
     */
    public static String join(List list, String delim)
    {
        if (list == null || list.size() < 1)
            return null;
        StringBuffer buf = new StringBuffer();
        Iterator i = list.iterator();

        while (i.hasNext())
        {
            buf.append((String) i.next());
            if (i.hasNext())
                buf.append(delim);
        }
        return buf.toString();
    }

    /**
     * Splits a String on a delimiter into a List of Strings.
     *
     * @param str the String to split
     * @param delim the delimiter character(s) to join on (null will split on
     *        whitespace)
     * @return a list of Strings
     */
    public static List split(String str, String delim)
    {
        List splitList = null;
        StringTokenizer st = null;

        if (str == null)
            return splitList;
        if (delim != null)
            st = new StringTokenizer(str, delim);
        else
            st = new StringTokenizer(str);
        if (st != null && st.hasMoreTokens())
        {
            splitList = new ArrayList();
            while (st.hasMoreTokens())
                splitList.add(st.nextToken());
        }
        return splitList;
    }

    /**
     * Creates a Map from an encoded name/value pair string
     *
     * @param str The string to decode and format
     * @return a Map of name/value pairs
     */
    public static Map strToMap(String str)
    {
        if (str == null)
            return null;
        Map decodedMap = new HashMap();
        List elements = split(str, "|");
        Iterator i = elements.iterator();

        while (i.hasNext())
        {
            String s = (String) i.next();
            List e = split(s, "=");

            if (e.size() != 2)
                continue;
            String name = (String) e.get(0);
            String value = (String) e.get(1);
            if(name.equals("^"))
                name="";
            if(value.equals("^"))
                value="";
            decodedMap.put(decodeStr(name), decodeStr(value));
        }
        return decodedMap;
    }

    /**
     * Creates an encoded String from a Map of name/value pairs .
     * name/value pairs should be strings
     *
     * @param map The Map of name/value pairs
     * @return String The encoded String
     */
    public static String mapToStr(Map map)
    {
        if (map == null)
            return null;
        StringBuffer buf = new StringBuffer();
        Set keySet = map.keySet();
        Iterator i = keySet.iterator();
        boolean first = true;

        while (i.hasNext())
        {
            Object key = i.next();
            Object value = map.get(key);

            if (!(key instanceof String) || !(value instanceof String))
                continue;
            String encodedName = encodeStr((String) key);
            String encodedValue = encodeStr((String) value);

            if (first)
                first = false;
            else
                buf.append("|");

            if( encodedName.equals(""))
                encodedName="^";
            if( encodedValue.equals(""))
                encodedValue="^";

            buf.append(encodedName);
            buf.append("=");
            buf.append(encodedValue);
        }
        return buf.toString();
    }
    
    private static String encodeStr(String val) 
    {
    	try 
    	{
    		return URLEncoder.encode(val, DEF_ENCODING);
    	} catch (Exception e) {
    		
    		return val;
    	}
    }
    
    private static String decodeStr(String val) 
    {
    	try
    	{
    		return URLDecoder.decode(val, DEF_ENCODING);
    	} catch (Exception e) {
    		return val;
    	}
    }
    /*
    public static void main(String args[]){
        Map map1=new HashMap();
        map1.put("param1","abc");
        map1.put("param2","");
        map1.put("param3","efg");
        map1.put("param4","");
        String str=mapToStr(map1);
        Map map2=strToMap(str);
        System.out.println(map1.equals(map2)+"\t\t"+map1+"\t\t"+map2);
    }*/
}
