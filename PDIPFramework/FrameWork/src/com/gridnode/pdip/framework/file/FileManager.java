package com.gridnode.pdip.framework.file;

/**
 * <p>Title: PDIP</p>
 * <p>Description: Peer Data Interchange Platform</p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode</p>
 * @author Jagadeesh
 * @version 1.0
 */

import java.io.*;
import java.util.*;
import java.net.*;

import com.gridnode.pdip.framework.util.SystemUtil;



public class FileManager {

private Properties dbProps=null;

private static String CONFIG_DIR= SystemUtil.getWorkingDirPath() +
                                    File.separatorChar+"conf"+
                                    File.separatorChar;

  public FileManager() {
  }

public void checkFM(){


        if(dbProps==null){

        try{
        dbProps = new Properties();

        //The forward slash "/" in front of in_filename will ensure that
        //no package names are prepended to the filename when the Classloader
        //search for the file in the classpath
        System.out.println(CONFIG_DIR+"paths.properties");
        InputStream is = new FileInputStream(CONFIG_DIR+"paths.properties");
        //getClass().getResourceAsStream(CONFIG_DIR+"paths.properties");
        if(null == is)
        {
            System.out.println(" No Properties Found ");
        }
            dbProps.load(is);//this may throw IOException
        //    return dbProps;
        }
        catch (IOException ioe)
        {
            System.err.println("Properties loading failed in AppConfig");
           // throw new ConfigException(ioe,"Can't locate file:" +in_filename);
        }
  }

 }

 public String getRootDir(){
  if(dbProps != null){
  String root = dbProps.getProperty("ROOTDIR");
  return root;
  }
  return null;
 }


 public String getRoot(){
  if(dbProps != null){
  String root = dbProps.getProperty("ROOT");
  return root;
  }
  return null;
 }

 public void writeToServer(Object inObject){
 try{
  System.out.println(" Opening Connection to Servlet .... :");
  java.net.URL url = new java.net.URL("http://localhost:7001/FileServlet");
  java.net.HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
  urlConnection.setDoInput(true);
  urlConnection.setDoOutput(true);
  urlConnection.setUseCaches(false);
  urlConnection.setRequestMethod("POST");
//  urlConnection.setRequestProperty ("Content-Type", "application/octet-stream");
  System.out.println(" Connection Successfully Obtained ....  :");
  ObjectOutputStream out = new ObjectOutputStream(urlConnection.getOutputStream());
  FileWrapper wrapper = new FileWrapper();
  wrapper.setStringValue("Jaggs");
  wrapper.setArryList(inObject);
  /*  if(inObject instanceof ArrayList){
  ArrayList lis = (ArrayList)inObject;
  wrapper.setArryList(lis);
  }
*/
  out.writeObject(wrapper);
  /*OutputStreamWriter ostream = new OutputStreamWriter(urlConnection.getOutputStream());
  BufferedWriter out = new BufferedWriter(ostream);
  out.write("book=Dictionary&va=doggerel\r\n");*/
  out.flush();
  out.close();


  InputStream stream = urlConnection.getInputStream();
  BufferedInputStream in = new BufferedInputStream(stream);
  int i = 0;
  while ((i = in.read()) != -1) {
  System.out.write(i);
  }
  in.close();


  /*  OutputStream stream = urlConnection.getOutputStream();
  ObjectOutputStream out = new ObjectOutputStream(stream);
  if(inObject != null){
  System.out.println(" In Object is Not Null  .... :");
  ArrayList list  = (ArrayList)inObject;
  for(int i=0;i<list.size();i++){
  ByteArrayOutputStream output = (ByteArrayOutputStream)list.get(i);
  out.writeObject(output);
  }
*/
//  out.close();
//  }

  System.out.println(" Written Object to Servlet ..... :");
  }catch(Exception ex){
   System.out.println(" The Message out is ....  "+ex.toString());

  }
  /*InputStream in = urlConnection.getInputStream();
  BufferedInputStream bif = new BufferedInputStream(in);
  int i=0;
  while((i=bif.read()) != -1)
  {
   System.out.write(i);
  }
 bif.close();
 */

 }


}