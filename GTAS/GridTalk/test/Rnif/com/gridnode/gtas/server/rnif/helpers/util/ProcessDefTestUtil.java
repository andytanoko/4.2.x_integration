package com.gridnode.gtas.server.rnif.helpers.util;

import com.gridnode.pdip.app.rnif.model.ProcessDef;

public class ProcessDefTestUtil 
{
  public static ProcessDef create3A4() throws Exception
  {
     ProcessDefConfig defConfig = new ProcessDefConfig("testdata/rnif/3A4Test.def");   
     return defConfig.getProcessDef();
  }
}
