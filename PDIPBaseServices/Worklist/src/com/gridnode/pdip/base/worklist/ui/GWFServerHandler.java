package com.gridnode.pdip.base.worklist.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;


public class GWFServerHandler implements Runnable,GWFEventChangeListener{
  boolean flag = true;
  public Socket clientSocket = null;
  public String observerData="Waiting for Event ... ";
  public BufferedReader dis=null;
  public PrintStream pis = null;
  public ObjectOutputStream outstream = null;
  public GWFServerHandler(){
  }

  public GWFServerHandler(Socket clientSocket) {
  try{
      this.clientSocket = clientSocket;

  }catch(Exception ex){
    ex.printStackTrace();
  }
  }


 public void run(){
 try{
        System.out.println(" In Server Handler ... ");
        dis = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outstream = new ObjectOutputStream(clientSocket.getOutputStream());

        //int cnt =0;
        while(true){
           if(flag == true){
            //  System.out.println(" Message No is "+(++cnt));
              String str = dis.readLine();
           //   pis.println(observerData);
           //   System.out.println("Observer Data is  "+observerData);
           //   flag=false;
              if(str != null){
               if(str.equals("Remove")){
                System.out.println(" Listener Removed   "+str);
                GWFServerObserver.getInstance().removeEventListener(this);
                outstream.writeObject("Close");
                outstream.flush();
                flag = false;
              }

               if(str.equals("done")){
                System.out.println(" Listener Removed   "+str);
                GWFServerObserver.getInstance().removeEventListener(this);
                outstream.writeObject("Destroy");
                outstream.flush();
                //pis.println();
                flag = false;
              }

               if(str.equals("destroy")){       //Called when Applet is foceably destroyed
                System.out.println(" Listener Removed   "+str);
                GWFServerObserver.getInstance().removeEventListener(this);
                flag = false;
              }

            }
           }
         // Thread.currentThread().sleep(1000);

        }

 }catch(Exception ex){

    ex.printStackTrace();
 }
 }

public void updateEventChange(GWFEventChangeEvent evt){
 try{
   //System.out.println(" Event Triggered .... ");
   this.observerData = evt.getEventString();
   if(outstream == null){
   outstream = new ObjectOutputStream(clientSocket.getOutputStream());
   }
   outstream.writeObject(evt.getUsers());
   //pis = new PrintStream(clientSocket.getOutputStream());
   //pis.println(observerData);
   outstream.flush();
   System.out.println(" Stream Written .. ");
   //stream.close();
  }catch(Exception ex){
   ex.printStackTrace();
  }

  }

}
