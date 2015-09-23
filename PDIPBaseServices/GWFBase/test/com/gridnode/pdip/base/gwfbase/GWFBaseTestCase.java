package com.gridnode.pdip.base.gwfbase;

 
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.gridnode.pdip.base.gwfbase.bpss.model.BpssFork;
import com.gridnode.pdip.framework.util.UtilEntity;


public class GWFBaseTestCase extends TestCase
{
    public GWFBaseTestCase(String s)
    {
        super(s);
    }

    protected void setUp()
    {

    }

    /**
     * Since this module does not have any functionality
     * just checking beans r deployed or not
     */
    public void testUtilEntity()
    {
System.out.println("Inside the method.");
        try
        {
            BpssFork bpssFork = new BpssFork();
System.out.println("Inside the 2 method.");
            bpssFork.setRestrictionName("TestFork");
System.out.println("Inside the 3 method.");
            bpssFork = (BpssFork) UtilEntity.createEntity(bpssFork, false);
System.out.println("Inside the 4 method.");
            assertNotNull("Unable to create BpssFork with RestrictionName TestFork", bpssFork);
            Long uId = (Long) bpssFork.getKey();

            bpssFork = (BpssFork) UtilEntity.getEntityByKey(uId, BpssFork.ENTITY_NAME, false);
            assertNotNull("Unable to getEntityByKey BpssFork with UID :" + uId, bpssFork);
            UtilEntity.remove(uId, BpssFork.ENTITY_NAME, false);
            try
            {
                bpssFork = (BpssFork) UtilEntity.getEntityByKey(uId, BpssFork.ENTITY_NAME, false);
                assertNull("Unable to remove BpssFork with UID :" + uId, bpssFork);
            }
            catch (Exception e)
            {
            }
        }
        catch (Throwable th)
        {
            th.printStackTrace();
        }
    }

    public void tearDown()
    {
    }

    public static Test suite()
    {
        return new TestSuite(GWFBaseTestCase.class);
    }

    public static void main(String args[]) throws Exception
    {
        junit.textui.TestRunner.run(suite());
    }

}
