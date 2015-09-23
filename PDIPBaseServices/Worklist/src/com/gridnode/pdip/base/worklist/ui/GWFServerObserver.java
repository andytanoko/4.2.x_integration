package com.gridnode.pdip.base.worklist.ui;

import java.util.Vector;

public class GWFServerObserver implements Runnable {
  private String toSend;
  private static GWFServerObserver observer = null;
  private  javax.swing.event.EventListenerList list = null;

  public GWFServerObserver() {
  }

  public void run(){
  }
  public static synchronized GWFServerObserver getInstance(){

   if(observer == null){
        observer = new GWFServerObserver();
   }
   return observer;

  }


  public String getToSend(){
    return toSend;
  }

  public void setToSend(String sendData){
    try{
    Thread.sleep(3000);
    this.toSend = sendData;
    fireChangeEvent(toSend);
    //System.out.println("Send Data ... "+sendData);
    //notifyObservers();
   }catch(Exception ex){
    ex.printStackTrace();
   }
  }

/**
 * Backward Compatibility....
 *
 */

  public void fireChangeEvent(String data){
   GWFEventChangeEvent evt =new GWFEventChangeEvent(this,data);
   java.util.EventListener[] list1 = list.getListeners(GWFEventChangeListener.class);
   for(int i=0 ; i<list1.length ; i++){
     GWFEventChangeListener tcl = (GWFEventChangeListener)list1[i];
     tcl.updateEventChange(evt);
   }

  }

  /**
   * This method shld be used if Users need to be passed back.
   * @param Vector users
   */
  public void fireChangeEvent(String data,Vector users){

   GWFEventChangeEvent evt =new GWFEventChangeEvent(this,users,data);
   java.util.EventListener[] list1 = list.getListeners(GWFEventChangeListener.class);
   for(int i=0 ; i<list1.length ; i++){
     GWFEventChangeListener tcl = (GWFEventChangeListener)list1[i];
     tcl.updateEventChange(evt);
   }

  }


  public  synchronized void addEventListener(GWFEventChangeListener lis){
    if(list == null){
      list = new javax.swing.event.EventListenerList();
    }
    list.add(GWFEventChangeListener.class,lis);
  }


  public  synchronized void removeEventListener(GWFEventChangeListener lis){
      list.remove(GWFEventChangeListener.class,lis);
  }


}



