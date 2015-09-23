package com.gridnode.pdip.base.worklist.ui;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

import netscape.javascript.JSObject;
 

public class GWFWorkListApplet extends Applet  implements Runnable {
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5994835275137291683L;
	Socket s = null;
  DataInputStream dis;
  PrintStream pis;
  Thread runner;
  ObjectInputStream obj;
  boolean runFlag = true;
  String name;
  boolean destroyFlag = true;
  public void init() {
    name = getParameter("name");
    setBackground(Color.white);
  }

 public void start(){
  if(runner == null){
    runner = new Thread(this);
    runner.start();
  }

 }


 public void stop(){
  System.out.println(" In Stop .. ");
  destroyFlag = false;
  try{
    if(pis != null){
     pis.println("Remove");
     pis.flush();
    }
  }catch(Exception ex){
     ex.printStackTrace();
  }

 }

 public void run(){
   connect();
 }

  public void update(Graphics g)
  {
    paint(g);
  }


  public void connect(){
  try{

      System.out.println("Opening a Connection ");
      InetAddress inet = InetAddress.getLocalHost();
      System.out.println(" This is Host "+inet.toString());
      s = new Socket(getDocumentBase().getHost(),8189);

      obj = new ObjectInputStream(s.getInputStream());
      //dis = new DataInputStream(s.getInputStream());
      pis = new PrintStream(s.getOutputStream());
      System.out.println(" Opened a Connection ");

      //ts.append("Host: "+getDocumentBase().getHost());
      //ts.append("\n"+inet.toString());
      System.out.println(" Host "+getDocumentBase().getHost());
      System.out.println(" User is "+name);
      Vector oVec = null;
      String removed = null;
      while(true && runFlag==true){
          System.out.println(" In While Loop .. ");

         if( s != null){
          Object obj1 = obj.readObject();
          if(obj1 instanceof String){
           removed = (String)obj1;
          }
          if(obj1 instanceof Vector){
            oVec = (Vector)obj1;
          }

          if(removed != null){
             if(removed.equals("Close") || removed.equals("Destroy")){
               System.out.println("Closing the Socket ... "+removed);
               //pis.println("Remove");
               pis.flush();
               pis.close();
               obj.close();
               s.close();
               runFlag = false;
             }
          }

          if(oVec != null){
             System.out.println("Vector is Not Null ");
            if(oVec.contains(name)){
                 System.out.println("Vector has this user "+name);
                 JSObject win = JSObject.getWindow(this);
                 pis.println("done");
                 pis.close();
                 obj.close();
                 s.close();
                 System.out.println(" Socket Closed For .. "+name);
                 win.call("update",null);
                 runFlag = false;
            }
            //System.out.println("Vector is Not Null "+);
          }

/*          if(str != null){
             JSObject win = JSObject.getWindow(this);
             win.call("update",null);
             pis.println(str);
             runFlag = false;
          }
  */
        }
          //String str1 = di.readLine();
          //pis.println(str);
          //repaint();
  //       break;
      }

    // s.close();

}catch(Exception ex){
  ex.printStackTrace();
 }


}

  public void destroy(){
  System.out.println(" In Destroy ... ");
  if(destroyFlag == true){
  try{
    if(pis != null){
     pis.println("destroy");
     pis.flush();
    }
  }catch(Exception ex){
     ex.printStackTrace();
  }



  }
  }



 }



