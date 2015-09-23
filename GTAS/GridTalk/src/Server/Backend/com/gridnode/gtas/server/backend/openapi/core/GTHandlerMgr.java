package com.gridnode.gtas.server.backend.openapi.core;

import  java.util.ArrayList;

public class GTHandlerMgr
{
  public static ArrayList handler = new ArrayList();

  public GTHandlerMgr()
  {
  }

  static public void addHandler(GTAPIHandler handlerSrc)
  {
    handler.add(handlerSrc);
  }

  static public GTAPIHandler getHandler(int i)
  {
    return (GTAPIHandler)handler.get(i);
  }
}
