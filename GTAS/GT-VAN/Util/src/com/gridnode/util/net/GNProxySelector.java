/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001 - 2007 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: GNProxySelector.java
 * 
 * *****************************************************************************
 * Date         Author               Changes
 * *****************************************************************************
 * Jan 08 2007  Lim Soon Hsiung      Created
 * Feb 12 2007  Lim Soon Hsiung      Append disabled proxy servers to the end of
 *   the available proxy list.  They may be active before the timeout period.
*  Mar 05 2007  Alain Ah Ming		 Added error code
 */

package com.gridnode.util.net;

import java.io.IOException;
import java.net.*;
import java.util.*;

import com.gridnode.util.exceptions.ILogErrorCodes;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * This class implement a ProxySelector which takes the proxy parameter from a
 * java properties. The properties defines a list of available proxy setting
 *
 * <p>Sample properties file
 * <div>
 * proxy.servers=proxy1, proxy2
 *
 * proxy.proxy1.hostname=192.168.213.147
 * proxy.proxy1.port=80
 * proxy.proxy1.priority=1
 * proxy.proxy1.retry.second=600
 * proxy.proxy1.username=gtas
 * proxy.proxy1.password=gridnode
 *
 * proxy.proxy2.hostname=192.168.213.141
 * proxy.proxy2.port=80
 * proxy.proxy2.priority=2
 * proxy.proxy2.retry.second=1200
 * </div>
 * 
 * <p>
 * <p> The <code>proxy.servers</code> defines all the proxy servers id  available
 * by the selector.  The server id is used as the property key in the rest of the properies file.
 * The server Ids are ',', ';' or space delimited (combination of all are allowed, e.g. "proxy1 proxy2, proxy3;proxy4")
 *
 * <p>The <code>proxy.</code><b>Server Id</b><code>.hostname</code> defines the hostname/IP of the
 * proxy server. This is a mandatory field, if it is not define, this server will not be
 * added in the proxies list.
 *
 * <p>The <code>proxy.</code><b>Server Id</b><code>.port</code> defines the port used by
 * the proxy server. If not defined, default value is 80.
 *
 * <p>The <code>proxy.</code><b>Server Id</b><code>.priority</code> defines the priority
 * of this proxy server. The smaller the value the higher the priority.
 * If not defined, default value is 1.
 *
 * <p>The <code>proxy.</code><b>Server Id</b><code>.retry.second</code> defines
 * the time in second to put this proxy temporary out of service when it was first detected
 * to be not reachable.
 * If not defined, default value is 1800 (second, 30 minutes).
 *
 * <p>The <code>proxy.</code><b>Server Id</b><code>.username</code> and
 * <code>proxy.</code><b>Server Id</b><code>.password</code> is used for proxy
 * authentation.
 * 
 * @author Lim Soon Hsiung
 * @version 1.0
 */
final class GNProxySelector extends ProxySelector
{
  /*
   * Inner class representing a Proxy and a few extra data
   */
  class InnerProxy implements Comparable<InnerProxy>
  {
    Proxy proxy;

    InetSocketAddress addr;

    /**
     * Priority of this proxy, the samller the value, the higher the priority.
     */
    int priority = 0;

    /**
     * This parameter defines the time in second before it is put back to
     * service if it was disable previously.
     */
    int retrySecond;

    /**
     * Time in millisecond (e.g. <code>System.currentTimeMillis()</code>).
     * use together with <code>retrySecond</code> to determine when to
     * activate this proxy again.
     */
    long downtime = 0;

    PasswordAuthentication _pwdAuth;
    
    InnerProxy(String hostname, int port, int priority, int retrySecond, String username, char[] password)
    {
      this.priority = priority;
      this.retrySecond = retrySecond;
      addr = new InetSocketAddress(hostname, port);
      proxy = new Proxy(Proxy.Type.HTTP, addr);
      if (username != null)
      {
        _pwdAuth = new PasswordAuthentication(username, password);
      }
    }

    PasswordAuthentication getPasswordAuthentication()
    {
      return _pwdAuth;
    }


    InetSocketAddress address()
    {
      return addr;
    }

    Proxy toProxy()
    {
      return proxy;
    }

    boolean isdDown()
    {
      return downtime != 0;
    }

    long getDowntime()
    {
      return downtime;
    }

    void setDowntime(long downtime)
    {
      this.downtime = downtime;
      _logger.logWarn("setDowntime", null, proxy + " down at " + downtime, null);
    }

    int getRetryinMinute()
    {
      return retrySecond;
    }

    /**
     * Use to sort the InnerProxy based on <code>priority</code>.
     * 
     * @param anotherProxy
     *          the other instance to compare with
     * @return int
     */
    public int compareTo(InnerProxy anotherProxy)
    {
      return this.priority - anotherProxy.priority;
    }

    /**
     * Returns a string representation of the object.
     * 
     * @return a string representation of the object.
     */
    public String toString()
    {
      return "[" + addr + " priority:" + priority + " retrySecond:"
             + retrySecond + "]";
    }

  }

  // Keep a reference on the previous default
  private ProxySelector defsel = null;
  private java.util.List<Proxy> defProxyList = null;
  
  private boolean enable = false;

  /*
   * A list of proxies, indexed by their address.
   */
  HashMap<SocketAddress, InnerProxy> proxies = new HashMap<SocketAddress, InnerProxy>();

  InnerProxy[] ps = new InnerProxy[0];

  private Logger _logger = LoggerManager.getInstance().getLogger(LoggerManager.TYPE_UTIL, "GNProxySelector");
  
  GNProxySelector(ProxySelector def, Properties setting)
  {
    logProxySetting(setting);
    
    // Save the previous default
    defsel = def;
    
    if (defsel != null)
    {
      try
      {
        java.util.List<Proxy> l = def.select(new URI("http://www.gridnode.com")); // dummy select to get proxy list
        defProxyList = new java.util.ArrayList<Proxy>(l.size());
        for (Proxy p : l)
        {
          _logger.logMessage("<init>", null, "Default Proxy: " + p);
          if (p.type() != Proxy.Type.DIRECT)
          {
            _logger.logMessage("<init>", null, "Adding " + p + " to default proxy list");
            defProxyList.add(p);
          }
        }
      }
      catch (URISyntaxException ex)
      {
        defProxyList = new java.util.ArrayList<Proxy>(0);
        _logger.logError( ILogErrorCodes.GNPROXY_SELECTOR_INITIALIZE, "<init>",  null,"URI Syntax: "+ex.getMessage(), ex);
      }
      catch(Throwable t)
      {
        defProxyList = new java.util.ArrayList<Proxy>(0);
        _logger.logError(ILogErrorCodes.GNPROXY_SELECTOR_INITIALIZE, "<init>",  null, "Unexpected Error: "+t.getMessage(), t);      	
      }
    }
    String servers = setting.getProperty("proxy.servers", "").trim();
    StringTokenizer st = new StringTokenizer(servers, " ,;");
    InnerProxy i = null;
    while (st.hasMoreTokens())
    {
      String s = st.nextToken().trim();
      String hostname = setting.getProperty("proxy." + s + ".hostname");
      if (hostname == null)
      {
        continue; // ignore this server
      }
      String port = setting.getProperty("proxy." + s + ".port", "8080").trim();
      String priority = setting.getProperty("proxy." + s + ".priority", "1").trim();
      String retrySecond = setting.getProperty("proxy." + s + ".retry.second",
                                               "1800").trim(); // default 30  minutes
      String username = setting.getProperty("proxy." + s + ".username", "").trim();
      char[] password = null;
      if (username.length() != 0)
      {
          password = setting.getProperty("proxy." + s + ".password", "").trim().toCharArray();
          // may need to handle empty password, ignore this problem for now!!!!!!!
          /*
                           if(password.length == 0)
                           {
              username = null;
              password = null;
                           }
           */
      }
      else
      {
          // then must be "", set to null so that password auth will not be used.
          username = null;
      }
      i = new InnerProxy(hostname, 
                         getInt(port, 8080),
                         getInt(priority, 1),
                         getInt(retrySecond, 1800), // default 30 minutes
                         username,
                         password);
      proxies.put(i.address(), i);
      _logger.logMessage("<init>", null, "Adding " + i + " to proxies list");
    }
    enable = !proxies.isEmpty();

    if (enable)
    {
      TreeSet<InnerProxy> ts = new TreeSet<InnerProxy>(proxies.values()); // to sort the proxies according to priority
      ps = new InnerProxy[proxies.size()];
      ps = ts.toArray(ps);
    }
  }

  /**
   * Ask the authenticator that has been registered with the system
   * for a password.
   * <p>This method signature (ONLY signature) was copied from
   * <code>java.net.Authenticator.requestPasswordAuthentication()</code>
   * Current only the <code>InetAddress addr</code> parameter is used to
   * identify the Proxy server, which will in turn return the
   * <code>PasswordAuthentication</code> object.  Future development may want
   * to include other parameter for identification of the password authenticator.</p>
   *
   * @param host The hostname of the site requesting authentication.
   * @param addr The InetAddress of the site requesting authorization,
   *             or null if not known.
   * @param port the port for the requested connection
   * @param protocol The protocol that's requesting the connection
   *          ({@link java.net.Authenticator#getRequestingProtocol()})
   * @param prompt A prompt string for the user
   * @param scheme The authentication scheme
   * @param url The requesting URL that caused the authentication
   *
   * @return The username/password, or null if one can't be gotten.
   */
  PasswordAuthentication requestPasswordAuthentication(
          String host,
          InetAddress addr,
          int port,
          String protocol,
          String prompt,
          String scheme,
          URL url)
  {
    PasswordAuthentication pa = null;
    for (InnerProxy p : ps)
    {
      if (p.addr.getAddress().equals(addr))
      {
        pa = p.getPasswordAuthentication();
        break; // found
      }
    }

    return pa;
  }

  private int getInt(String value, int defaultValue)
  {
    try
    {
      return Integer.parseInt(value);
    }
    catch (Exception ex)
    {
      return defaultValue;
    }
  }

  /*
   * This is the method that the handlers will call. Returns a List of proxy.
   */
  public java.util.List<Proxy> select(URI uri)
  {
    // Let's stick to the specs.
    if (uri == null) 
    { 
      throw new IllegalArgumentException("URI can't be null."); 
    }

    // If disable, use default selector
    if (!enable && defsel != null) 
    { 
      return defsel.select(uri); 
    }

    String mtdName = "select";
    Object[] params = {uri};
    
    /*
     * If it's a http (or https) URL, then we use our own list.
     */
    String protocol = uri.getScheme();
    String host = uri.getHost();
    if ("http".equalsIgnoreCase(protocol) || "https".equalsIgnoreCase(protocol))
    {
      ArrayList<Proxy> l = new ArrayList<Proxy>(ps.length);
      ArrayList<Proxy> l2 = new ArrayList<Proxy>(ps.length); // for disabled proxy

      //always add one for local connection
      if ("localhost".equals(host) || "127.0.0.1".equals(host))
      {
        l.add(Proxy.NO_PROXY);
        return l;
      }
      for (InnerProxy p : ps)
      {
        if (p.isdDown())
        {
          long t1 = System.currentTimeMillis() - p.getDowntime();
          // debug("T1: " + t1);
          long duration = (long) ((t1) / 1000); // in second
          // error("Is down: " + p.toProxy() +
          // " duration: " + duration + " second");
          if (duration > p.retrySecond)
          { // ok, time is up
            _logger.logMessage(mtdName, params, "Putting " + p.toProxy() + " back to action");
            p.setDowntime(0); // reset, enable this proxy
          }
          else
          {
            continue; // Ignore this one first, wait for retry timeout
          }
        }

        _logger.debugMessage(mtdName, params, "Adding " + p.toProxy());
        l.add(p.toProxy());
      }
      
      if(!l2.isEmpty())
      {
        l.addAll(l2);
      // add all disabled proxies to the end of the list
      // In scenario where all proxy servers are down for maintenence, they
      // may be down for very short duration.  If all proxy are restarted
      // at very short interval one after another, it may appeared to the
      // application that all proxy are down (due to the timeout
      // (proxy.proxy1.retry.second) that may be set to a duration that
      // is long than the total restart time of all proxy server.)
      // So it is safer to add all the disable to the end of the list in case
      // they are actually available before the timeout period.
      }
      
      //always add one for direct connection
      l.add(Proxy.NO_PROXY);
      return l;
    }

    /*
     * Not HTTP or HTTPS (could be SOCKS or FTP) defer to the default selector.
     */
    if (defsel != null)
    {
      return defsel.select(uri);
    }
    else
    {
      ArrayList<Proxy> l = new ArrayList<Proxy>();
      l.add(Proxy.NO_PROXY);
      return l;
    }
  }

  /*
   * Method called by the handlers when it failed to connect to one of the
   * proxies returned by select().
   */
  public void connectFailed(URI uri, SocketAddress sa, IOException ioe)
  {
    // Let's stick to the specs again.
    if (uri == null || sa == null || ioe == null) 
    { 
      throw new IllegalArgumentException("Arguments can't be null."); 
    }

    // If disable, use default selector
    if (!enable && defsel != null)
    {
      defsel.connectFailed(uri, sa, ioe);
      return;
    }

    _logger.logError( ILogErrorCodes.GNPROXY_SELECT_CONNECT, "connectFailed", null,"sa " + sa, null);
    
    /*
     * Let's lookup for the proxy
     */
    InnerProxy p = proxies.get(sa);
    if (p != null)
    {
      _logger.logError(ILogErrorCodes.GNPROXY_SELECT_CONNECT,"connectFailed", null, "Proxy failed: " + p.proxy.toString() + " at " + new Date(), null);
      p.setDowntime(System.currentTimeMillis());
    }
    else
    {
      /*
       * Not one of ours, let's delegate to the default.
       */
      if (defsel != null)
      {
        defsel.connectFailed(uri, sa, ioe);
      }
    }
  }

  private void logProxySetting(Properties props)
  {
    Enumeration keys = props.propertyNames();
    StringBuffer buffer = new StringBuffer();
    buffer.append("Installing Proxy Setting:\r\n");
    while (keys.hasMoreElements())
    {
      String key = (String)keys.nextElement();
      String val = props.getProperty(key);
      buffer.append("       "+key+"["+val+"]\r\n");
    }
    _logger.debugMessage("logProxySettings", null, buffer.toString());
  }

  /**
   * Install this implementation of the <code>ProxySelector</code> as the
   * default selector. All proxy setting are taken for the <code>setting</code>
   * properties.
   * 
   * @param setting
   *          Properties
   */
  public static void install(Properties setting)
  {
    GNProxySelector ps = new GNProxySelector(ProxySelector.getDefault(),
                                             setting);
    ProxySelector.setDefault(ps);
    GNAuthenticator.setProxySelector(ps);
  }

}
