package com.gridnode.pdip.framework.log.remote;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.*;
import org.apache.log4j.spi.LoggingEvent;
import java.net.*;
import java.io.*;

public class  RemoteLogAppender extends AppenderSkeleton {

  public static final String REMOTE_HOST_OPTION="RemoteHost";
  public static final String LOCAL_HOST_OPTION="LocalHost";
  public static final String LOCAL_SERVER_OPTION="LocalServer";
  public static final String LOG_BUFFER_OPTION="LogBufferSize";
  public static final String FLUSHINTERVAL_OPTION="LogFlushInterval";

  CyclicBuffer cyclicBuffer=new CyclicBuffer(20);
  FlusherThread flusher=new FlusherThread(60000); // by default FlusherThread will flush for every 1 minute
  private URL remoteHost;
  String localHost;
  String localServer;

/*
  public String[] getOptionStrings() {
    return OptionConverter.concatanateArrays(super.getOptionStrings(),
                                                    new String[] {REMOTE_HOST_OPTION ,
                                                                  LOCAL_HOST_OPTION,
                                                                  LOCAL_SERVER_OPTION});
  }
*/
  /**
   * @deprecated Use the setter method for the option directly instead
   * of the generic <code>setOption</code> method.
   */
/*
  public void setOption(String option, String value) {
    if(value == null) return;
      super.setOption(option, value);

    if(option.equals(REMOTE_HOST_OPTION))
      setRemoteHost(value);
    else if(option.equals(LOCAL_HOST_OPTION))
      setLocalHost(value);
    else if(option.equals(LOCAL_SERVER_OPTION))
      setLocalServer(value);
    else if(option.equals(LOG_BUFFER_OPTION))
      setLogBufferSize(value);
    else if(option.equals(FLUSHINTERVAL_OPTION))
      setFlushInterval(value);

  }
*/

  public void setFlushInterval(String value){
    try{
      long interval=Long.parseLong(value);
      flusher.setFlushInterval(interval);
    }catch(Exception e){
      LogLog.warn("Error in Log flush interval parameter:"+value+", "+e);
    }
  }

  public void setLogBufferSize(String value){
    try{
      int size=Integer.parseInt(value);
      cyclicBuffer.resize(size);
    }catch(Exception e){
      LogLog.warn("Error in Log buffer resize parameter:"+value+", "+e);
    }
  }


  public void setRemoteHost(String value){
    try{
      remoteHost=new URL(value);
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  public void setLocalHost(String value){
    localHost=value;
  }
  public void setLocalServer(String value){
    localServer=value;
  }


  /**
   * Connect to the specified <b>RemoteHost</b> and <b>Port</b>.
   */
  public void activateOptions() {
    if(flusher.getFlushInterval()>0)
      flusher.start();
  }

  private HttpURLConnection connect() throws Exception {
    try{
      HttpURLConnection con=(HttpURLConnection)remoteHost.openConnection();
      con.setDoOutput(true);
      con.setUseCaches(false);
      return con;
    }catch(Exception e){
      LogLog.warn("Error in connecting to Log server "+e);
      throw e;
    }
  }

  private void disconnect(HttpURLConnection con) {
    try{
      con.disconnect();
    }catch(Exception e){
    }
  }

  public  void append(LoggingEvent event) {
    if(event == null)  return;
    cyclicBuffer.add(event);
    flushLoggingEvent(flusher.getFlushInterval()<=0);
  }

  public synchronized void flushLoggingEvent(boolean flush){
    if(cyclicBuffer.length()>0 && (flush || (cyclicBuffer.getMaxSize()==cyclicBuffer.length()))){
      HttpURLConnection con=null;
      try{
        con=connect();
        ObjectOutputStream oos=new ObjectOutputStream(con.getOutputStream());
        LoggingEvent event=null;
        while((event=cyclicBuffer.get())!=null){
          oos.writeObject(new RemoteLoggingEvent(event,localHost,localServer));
          oos.flush();
        }
        oos.close();
        con.getInputStream().close();
      }catch(Exception e){
        LogLog.warn("Error in flushing Logging event to Log server "+e);
      }finally{
        disconnect(con);
      }
    }
  }

  /**
   * Close this appender.
   * <p>This will mark the appender as closed and
   * call then cleanUp method.
   */
  synchronized public void close() {
      try{
            if(flusher.getFlushInterval()>0)
            flusher.interrupt();
      }catch(Exception e){
      }
  }

  /**
   * Flush the LoggingEvents in buffer to Log server
   *
   */
  public void finalize() {
    flushLoggingEvent(true);
    super.finalize();
  }

  /**
   * The RemoteLogAppender does not use a layout. Hence, this method returns
   * <code>false</code>.
   */
  public	boolean requiresLayout() {
    return false;
  }

  protected boolean isClosed()
  {
  	return closed;
  }
  
  private class FlusherThread extends Thread {
    long flushInterval;

    public FlusherThread(long flushInterval){
      setFlushInterval(flushInterval);
      setPriority(MIN_PRIORITY);
    }

    public void run() {
      try{
        while(!isClosed()){
          flushLoggingEvent(true);
          sleep(flushInterval);
        }
      }catch(Exception e){
        LogLog.warn("Error in run method of FlusherThread "+e);
      }
    }

    public void setFlushInterval(long flushInterval){
      this.flushInterval=flushInterval;
    }

    public long getFlushInterval(){
      return flushInterval;
    }

  }
}
