package com.gridnode.pdip.base.worklist.ui;

import java.util.*;

public class GWFEventChangeEvent extends EventObject{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7918124475134011351L;
	private String chageEvent;
  private String user;
  private Vector users;
  public GWFEventChangeEvent(Object source,String changeEvent) {
    super(source);
    this.chageEvent = changeEvent;
  }

  public GWFEventChangeEvent(Object source, Vector changeVector, String changeEvent) {
    super(source);
    this.users = changeVector;
    this.chageEvent = changeEvent;
  }


  public String getEventString(){
    return chageEvent;
  }

  public Vector getUsers(){
   return users;
  }

  public void setUsers(Vector usrs){
   users = usrs;
  }

  public void setChangeEvent(String changeEvent){
   this.chageEvent = changeEvent;
  }

  public void setUser(String user){
   this.user = user;
  }

  public String getUser(){
   return user;
  }


}
