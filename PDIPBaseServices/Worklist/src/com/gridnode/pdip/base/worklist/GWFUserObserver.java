package com.gridnode.pdip.base.worklist;

/**
 * <p>Title: GridFlow</p>
 * <p>Description: GridFlow - Extended Enterprise Businessware</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: GridNode Pte Ltd.</p>
 * @author Jagadeesh
 * @version 1.0
 */

import java.util.*;

 public class GWFUserObserver {

  private static GWFUserObserver obsvr;
  private static Vector usrCollection;

  private GWFUserObserver() {
    usrCollection = new Vector();
  }

  public static GWFUserObserver getInstance(){
   if(obsvr == null){
    obsvr = new GWFUserObserver();
   }
   return obsvr;
  }

  public static void setUser(String user){
    if(usrCollection.contains(user))
     return;
    usrCollection.add(user);
  }


  public boolean isUserAvailable(){
  return true;
  }

  public Vector getAvailableUsers(){
  return usrCollection;
  }

}
