package com.gridnode.gridtalk.testkit.http.relay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpBackendRelay implements FilenameFilter
{
  public static final String DATA_FOLDER = "be_data";
  private final static String CONTENTTYPE = "contenttype";

  File dataFolder = new File(DATA_FOLDER);
  private Logger logger = Logger.getLogger("HttpBackendRelay");
  private Config _conf = null;
  private Timer _timer;
  
  
  HttpBackendRelay()
  {
	init();
  }
  
  private void init()
  {
	_conf = new Config("conf/be_relay.properties");
	
	SendTask sendTask = new SendTask();
    _timer = new Timer("", true);
    _timer.schedule(sendTask, 1000, _conf.getSendInterval()*1000);	
  }

  public void startSending()
  {
//	logger.log(Level.FINE, "Checking for msg to relay ...");
	int numMsg = _conf.getSendNumTx();
	String files[] = dataFolder.list(this);
	if(files.length == 0)
	  return;
	logger.log(Level.INFO, files.length + " msg to relay");
	
	TreeSet<String> ts = new TreeSet<String>();
	for (int i = 0; i < files.length; i++)
	{
	  ts.add(files[i]);
	}
	files = ts.toArray(files);
	
	if(files.length < numMsg)
	  numMsg = files.length ;
	
	for (int i = 0; i < numMsg; i++)
	{
	  sendFile(files[i]);
	}
  }

  private void sendFile(String file)
  {
	String payloadFile = getFilenameNoExt(file) + ".xml";
	File pFile = new File(dataFolder, payloadFile);
	if(pFile.isFile())
	{
	  SendThread st = new SendThread(new File(dataFolder, file), pFile);
	  st.start();
	}
	else
	{
	  logger.log(Level.WARNING, "Payload file: " + pFile + " do not exist!");
	  File pf = new File(dataFolder, file);
	  logger.log(Level.INFO, "Deleting " + pf);
	  pf.delete();
	}
  }

  static String getFilenameNoExt(String file)
  {
	int endIndex = file.lastIndexOf('.'); 
	return file.substring(0, endIndex);
  }

  public boolean accept(File dir, String name)
  {
	return name.toLowerCase().endsWith(".properties");
  }
  
  class SendThread extends Thread
  {
    private Properties _props = new Properties();
    private File _propsFile;
    private File _payload;
    
    public SendThread(File propsFile, File payload)
    {
      try
	  {
    	FileInputStream fi = new FileInputStream(propsFile);
		_props.load(fi);
		fi.close();
	  }
	  catch (Exception e)
	  {
		// TODO: handle exception
	  }
	  this._payload = payload;
	  this._propsFile = propsFile;
    }
    
    public void run()
    {
      byte[] msg = generateDocMsg(_payload);
      

      if (msg != null)
      {
    	try
    	{
          new HttpBackendClient(_conf.getURL(), msg, _props, _props.getProperty(CONTENTTYPE, "text/html"));
          _payload.delete();
          _propsFile.delete();
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, "Unable to send to Backend receiver", ex);
      }

      }
    }
    
    private byte[] generateDocMsg(File payload)
    {
      try
      {
    	FileInputStream fi = new FileInputStream(payload);
    	byte data[] = new byte[(int)payload.length()];
    	fi.read(data);
    	fi.close();
    	return data;
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE,"Error reading payload " + payload, ex);
      }
      return null;
    }
  }
  
  class Config
  {
	  private URL _url;
	  private int _sendInterval;
	  private int _sendNumTx;

	public Config(String confFile)
	{
	  Properties props = new Properties();
	  File f = new File(confFile);
	  try
	  {
		  FileInputStream fi = new FileInputStream(f);
		  props.load(fi);
	  }
	  catch (Exception ex)
	  {
		logger.log(Level.SEVERE,"Unable to load configuration file " + f, ex);
	  }
	  
	  String url = props.getProperty("url", "http://127.0.0.1:8080/Relay/dummy/receiver");
	  try
	  {
		_url = new URL(url);
	  }
	  catch (Exception e)
	  {
		logger.log(Level.SEVERE,"Invalid URL " + url, e);
		_url = null;
	  }
	  try
	  {
		_sendInterval = Integer.parseInt(props.getProperty("send.interval", "10"));
	  }
	  catch (Exception e)
	  {
		// TODO: handle exception
		_sendInterval = 10;
		logger.log(Level.SEVERE,"Invalid send interval, set to default " + _sendInterval, e);
	  }
	  try
	  {
		_sendNumTx = Integer.parseInt(props.getProperty("send.num.tx", "1"));
	  }
	  catch (Exception e)
	  {
		// TODO: handle exception
		_sendNumTx = 1;
		logger.log(Level.SEVERE,"Invalid send # Tx, set to default " + _sendNumTx, e);
	  }
	  logger.log(Level.INFO, "Interval=" + _sendInterval + " Tx=" + _sendNumTx + " url=" + _url);
	}

	public URL getURL()
	{
	  return _url;
	}

	/**
	 * @return the _sendInterval
	 */
	public int getSendInterval()
	{
	  return _sendInterval;
	}

	/**
	 * @return the _sendNumTx
	 */
	public int getSendNumTx()
	{
	  return _sendNumTx;
	}
	
  }
  
  public class SendTask extends TimerTask
  {

	@Override
	public void run()
	{
	 startSending();
	}
  
  }
}