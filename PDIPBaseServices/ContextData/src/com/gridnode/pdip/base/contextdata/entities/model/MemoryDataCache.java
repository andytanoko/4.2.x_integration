package com.gridnode.pdip.base.contextdata.entities.model;

import java.util.*;
import java.io.*;

public class MemoryDataCache {
    private static Map memDataCacheTable=new HashMap();
    private static String DEFAULT_DATA_CACHE="default";

    private String memDataCacheName;
    private Map cacheTable;
    private int keyIndex=0;

    public static MemoryDataCache getInstance(String dataCacheName){
        if(dataCacheName==null)
            dataCacheName=DEFAULT_DATA_CACHE;
        MemoryDataCache memDataCache=(MemoryDataCache)memDataCacheTable.get(dataCacheName);
        if(memDataCache==null){
            synchronized(memDataCacheTable){
                memDataCache=(MemoryDataCache)memDataCacheTable.get(dataCacheName);
                if(memDataCache==null){
                    memDataCache=new MemoryDataCache(dataCacheName);
                    memDataCacheTable.put(dataCacheName,memDataCache);
                }
            }
        }
        return memDataCache;
    }

    private MemoryDataCache(String memDataCacheName){
        this.memDataCacheName=memDataCacheName;
        cacheTable=new HashMap();
    }

    public String putData(Object obj){
        String key="http://Memory/"+keyIndex+"/"+memDataCacheName;
        cacheTable.put(key,cloneObject(obj));
        return key;
    }

    public void putData(String key,Object obj){
        cacheTable.put(key,cloneObject(obj));
    }

    public Object getData(String key){
        return cloneObject(cacheTable.get(key));
    }

    public Object removeData(String key){
        return cacheTable.remove(key);
    }

    public void clear(){
        cacheTable.clear();
    }

    private Object cloneObject(Object obj){
        return convertToObject(convertToBytes(obj));
    }

    public static Object convertToObject(byte[] data){
        try{
            ByteArrayInputStream bais=new ByteArrayInputStream(data);
            ObjectInputStream ois=new ObjectInputStream(bais);
            return ois.readObject();
        }catch(Exception e){
        }finally{
            //System.gc();
        }
        return null;
    }
    public static byte[] convertToBytes(Object obj) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream objOS = null;

        try {
            baos = new ByteArrayOutputStream();
            objOS = new ObjectOutputStream(new BufferedOutputStream(baos));
            objOS.writeObject(obj);
            objOS.close();
            return baos.toByteArray();
        } catch (Exception ex) {
            System.out.println("[MemoryDataCache.convertToBytes]");
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (objOS != null) objOS.close();
            } catch (Exception ex) {
            }
        }
    }


}