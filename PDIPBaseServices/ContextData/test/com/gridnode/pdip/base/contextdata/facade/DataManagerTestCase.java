package com.gridnode.pdip.base.contextdata.facade;


import java.util.*;

import com.gridnode.pdip.base.contextdata.entities.model.*;
import com.gridnode.pdip.base.contextdata.facade.ejb.*;
import com.gridnode.pdip.base.contextdata.facade.exceptions.*;
import com.gridnode.pdip.framework.j2ee.*;
import junit.framework.*;

public class DataManagerTestCase extends TestCase
{
    static IDataManagerObj dataManager;
    static Long contextUId;
    static HashMap testMap;

    public DataManagerTestCase(String s)
    {
        super(s);
    }

    protected void setUp()  throws Exception
    {
        IDataManagerHome home=(IDataManagerHome)ServiceLookup.getInstance(ServiceLookup.CLIENT_CONTEXT).getHome(IDataManagerHome.class);
        dataManager=home.create();
        testMap=new HashMap();
        testMap.put(new ContextKey("key1"),"data for key1");
        testMap.put(new ContextKey("key2","branch1"),"data for key2 branch1");
    }

    public void testCreateContext() throws Exception
    {
        contextUId=dataManager.createContextUId();
        assertNotNull("Unable to create contextUId",contextUId);
    }

    public void testInsertContextData()  throws Exception
    {
        assertNotNull("contextUId is null",contextUId);
        dataManager.setContextData(contextUId,testMap);
        HashMap retMap=dataManager.getContextData(contextUId);
        assertNotNull("retMap returned by dataManager.getContextData is null for context "+contextUId,retMap);
        for(Iterator iterator=testMap.keySet().iterator();iterator.hasNext();){
            Object key=iterator.next();
            assertTrue("Context doesnot contain inserted data "+key,retMap.containsKey(key));
            assertEquals("Context data is not equal for key "+key,testMap.get(key),retMap.get(key));
        }
    }

    public void testUpdataContextData() throws Exception
    {
        assertNotNull("contextUId is null",contextUId);
        HashMap updateMap=new HashMap();
        //updateMap.put(new ContextKey("key1"),"updated:data for key1");
        //updateMap.put(new ContextKey("key2","branch1"),"updated:data for key2 branch1");
        updateMap.put("key1","updated:data for key1");
        updateMap.put("key2(branch1)","updated:data for key2 branch1");
        dataManager.setContextData(contextUId,updateMap);
        HashMap retMap=dataManager.getContextData(contextUId);
        assertNotNull("retMap returned by dataManager.getContextData is null for context "+contextUId,retMap);
        assertEquals("retMap returned by dataManager.getContextData is not of same size for context "+contextUId,testMap.size(),retMap.size());
        for(Iterator iterator=updateMap.keySet().iterator();iterator.hasNext();){
            Object key=iterator.next();
            assertTrue("Context doesnot contain inserted data "+key,retMap.containsKey(new ContextKey(key.toString())));
            assertEquals("Context data is not equal for key "+key,updateMap.get(key),retMap.get(new ContextKey(key.toString())));
        }
    }

    public void testRemoveContextData() throws Exception
    {
        assertNotNull("contextUId is null",contextUId);
        dataManager.removeContextData(contextUId,new ArrayList(testMap.keySet()));
        HashMap retMap=dataManager.getContextData(contextUId);
        assertNotNull("retMap returned by dataManager.getContextData is null for context "+contextUId,retMap);
        assertEquals("Unable to remove context data for context "+contextUId,0,retMap.size());
    }

    public void testRemoveContext() throws Exception
    {
        assertNotNull("contextUId is null",contextUId);
        try{
            dataManager.removeContextUId(contextUId);
            HashMap retMap=dataManager.getContextData(contextUId);
            throw new Exception("Unable to remove context for context "+contextUId);
        }catch(DataException de){
        }
    }

    public static Test suite()
    {
        return new TestSuite(DataManagerTestCase.class);
    }

    public static void main(String args[]) throws Exception
    {
        junit.textui.TestRunner.run(suite());
    }

}

