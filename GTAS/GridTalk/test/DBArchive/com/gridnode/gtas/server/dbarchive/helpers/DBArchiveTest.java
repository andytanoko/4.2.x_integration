package com.gridnode.gtas.server.dbarchive.helpers;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.helpers.BpssHandler;
import com.gridnode.gtas.server.rnif.helpers.RnifTestCase;
import com.gridnode.pdip.app.workflow.impl.bpss.helpers.IBpssConstants;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

public class DBArchiveTest extends RnifTestCase
{

  public DBArchiveTest(String name)
  {
    super(name);
  }

  public void testArchiveGridDoc() throws Throwable
  {
     try
    {
      Logger.debug("testArchiveGridDoc Enter");
      
      IDataFilter filter = new DataFilterImpl();
      filter .addSingleFilter(null, GridDocument.G_DOC_ID, filter.getEqualOperator(), new Long(20),false);
      
      _rnifMgr.invokeMethod(
        null,		
        "com.gridnode.gtas.server.dbarchive.helpers.DbArchive",
        "archiveGridDoc",
        new Class[]{IDataFilter.class},
        new Object[] {filter});
      Logger.log("testArchiveGridDoc finished");

    }
    catch (Throwable ex)
    {
      Logger.err("testArchiveGridDoc", ex);
      throw ex;
    }
    Logger.debug("testArchiveGridDoc Exit");

  }


 public void testRestoreGridDoc() throws Throwable
  {
     try
    {
      Logger.debug("testRestoreGridDoc Enter");
       String fileName = "C:/GTASArchive.arc";
        _rnifMgr.invokeMethod(
        null,		
        "com.gridnode.gtas.server.dbarchive.helpers.DbArchive",
        "restoreGridDoc",
        new Class[]{String.class},
        new Object[] {fileName});
      Logger.log("testRestoreGridDoc finished");

    }
    catch (Throwable ex)
    {
      Logger.err("testRestoreGridDoc", ex);
      throw ex;
    }
    Logger.debug("testRestoreGridDoc Exit");
  }


}
