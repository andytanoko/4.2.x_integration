package com.gridnode.pdip.framework.db.meta;

import com.gridnode.pdip.framework.db.filter.*;
import com.gridnode.pdip.framework.log.*;
import junit.framework.*;

/**
 * <p>Title: PDIP</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode</p>
 * @author Mahesh
 * @version 1.0
 */

public class DataFilterImplTestCase extends TestCase {

    IDataFilter filter;

    public DataFilterImplTestCase(String name){
        super(name);
    }

    public static Test suite() {
        return new TestSuite(DataFilterImplTestCase.class);
    }

    public void setUp(){
        try{
            filter=new DataFilterImpl();
        }catch(Exception e){
        	Log.warn("DataFilterImplTestCase","Error in setUp",e);
        }
    }

    public void testConvertToSQLFilter() throws Exception {
/*        filter.addSingleFilter(null,IFileType.CAN_DELETE,filter.getEqualOperator(),"true",false);
        DataFilterImpl sqlFilter=(DataFilterImpl)ObjectConverter.convertToSQLFilter(FileType.class.getName(),filter);
        assertNotNull(" sqlFilter retutned by ObjectConverter.convertToSQLFilter() is null in testConvertToSQLFilter ",sqlFilter.applySyntax());
        Log.debug(Log.DB,"DataFilterImplTestCase ->SQL Query "+sqlFilter.getFilterExpr());
*/  }

    public static void main(String args[]){
        junit.textui.TestRunner.run (suite());
    }

}