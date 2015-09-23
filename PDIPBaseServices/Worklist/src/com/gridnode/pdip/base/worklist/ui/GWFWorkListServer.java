package com.gridnode.pdip.base.worklist.ui;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
 

public class GWFWorkListServer implements Runnable {
  Socket s;
  ServerSocket ss;
  DataInputStream dis;
  PrintStream ps;
  int port;
  int i=0;
  public GWFWorkListServer(int port) {
   System.out.println("Constructor called  "+port);
   this.port = port;
  }



  public void run(){

  try{
      int count=0;
      ss = new ServerSocket(port,5);
      System.out.println("In  Run ");
      System.out.println("Server Observer Created ... ");
      while(true){
         System.out.println("Count  ... "+(++count));
         s= ss.accept();
         GWFServerObserver obs = GWFServerObserver.getInstance();
         GWFServerHandler handler = new GWFServerHandler(s);
         obs.addEventListener(handler);
         new Thread(handler).start();
     }

   }catch(Exception ex){
    ex.printStackTrace();
  }


}

}
